<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
</head>
<body>

<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parent??>
            <li><a href="javascript:void(0);">${selectedMenu.parent.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>加载中...</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class=" col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                    <select name="city" id="cityCode"  class="form-control select" style="width:auto;"  data-live-search="true" onchange="findStoreByCondition()">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="enabled" id="enabled" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findStoreByCondition()">
                        <option value="-1">是否可用</option>
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryStoreInfo" id="queryStoreInfo" class="form-control "
                               style="width:auto;" placeholder="请输入要查找的店名或编码.." onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findStoreByNameOrCode()">查找</button>
                        </span>
                    </div>

                </div>
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<script>


    $(function() {
        initDateGird('/rest/stores/page/grid');
        findCitySelection();
        $('#btn_edit').on('click', function () {
            modify($('#dataGrid'), '/views/admin/stores/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
    });


    function findCitySelection(){
        var city = "";
        $.ajax({
            url: '/rest/citys/findCitylist',
            method: 'GET',
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);
                $.each(result, function (i, item) {
                    city += "<option value=" + item.cityId + ">" + item.name + "</option>";
                })
                $("#cityCode").append(city);
            }
        });
    }


    function initDateGird(url) {
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'storeId',
            title: '门店id',
            align: 'center',
            visible: false
        }, {
            field: 'storeCode',
            title: '门店编码',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        },{
            field: 'cityCode.name',
            title: '所属城市',
            align: 'center'
        }, {
            field: 'enable',
            title: '是否生效',
            align: 'center',
            formatter: function(value,row,index){
                if (true === value) {
                    return '<span class="label label-primary">是</span>';
                }else if(false === value){
                    return '<span class="label label-danger">否</span>';
                } else {
                    return '<span class="label label-danger">-</span>';
                }
            }
        },{
            field: 'enable',
            title: '未提货详情',
            align: 'center',
            formatter: function (value, row) {
                return '<button class="btn btn-primary btn-xs" onclick="showDetails(' + row.storeId + ')"> 查看</button>';
            }
        }]);
    }

    function modify(container, url) {
        var selected = this.getSelectedIds(container);
        var length = selected.length;
        if (length === 0) {
            $notify.warning('请在点击按钮前选中一条数据');
        } else if (length > 1) {
            $notify.warning('您每次只能选择一条数据进行修改');
        } else {
            var id = selected[0];
            url = url.replace('{id}', id);
            window.location.href = url;
        }
    }

    function getSelectedIds(container){
        var ids = [];
        var selected = container.bootstrapTable('getSelections');
        for (var i = 0; i < selected.length; i++ ) {
            var data = selected[i];
            ids.push(data.storeId);
        }
        return ids;
    }


    function findStoreByCondition() {
        $("#queryStoreInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var enabled =  $("#enabled").val();
        if(enabled==-1&&cityId!=-1){
            initDateGird('/rest/stores/findStoresListByCity/' + cityId);
        }else if(enabled==-1&&cityId==-1){
            initDateGird('/rest/stores/page/grid');
        }else {
            initDateGird('/rest/stores/findStoresListByCondition?enabled=' + enabled + '&cityId=' + cityId);
        }
        }


    function findBykey(){
        if(event.keyCode==13){
            findStoreByNameOrCode();
        }
    }

    function findStoreByNameOrCode() {
        var queryStoreInfo = $("#queryStoreInfo").val();
        $('#cityCode').val("-1");
        $('#enabled').val("-1");
        if (null == queryStoreInfo || "" == queryStoreInfo) {
            $("#dataGrid").bootstrapTable('destroy');
            initDateGird('/rest/stores/page/grid');
        } else {
            if (!checkCharacter(queryStoreInfo)) {
                $notify.warning('请检查输入是否为汉子或者字母');
                return false;
            }
            $("#dataGrid").bootstrapTable('destroy');
            initDateGird('/rest/stores/page/storeGrid/' + queryStoreInfo);
        }
    }


    function checkCharacter(theObj) {
        var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
        if (reg.test(theObj)) {
            return true;
        }
        return false;
    }


    function showDetails(id){
        window.location.href = '/views/admin/storeNonDelivery/storeNonDeliveryDetail/'+id;
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    var formatDateTime = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = dt.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = dt.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        var second = dt.getSeconds();
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
    };
</script>
</body>