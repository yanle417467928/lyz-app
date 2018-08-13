<head>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
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
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
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
<div id="information" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="user-block">
                    <span class="username" style="margin-left: 0px;">
                        <a id="menuTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="storeDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>门店id</b> <a class="pull-right" id="storeId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店组织id</b> <a class="pull-right" id="storeOrgId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店编码</b> <a class="pull-right" id="storeCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>所属城市</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否是默认门店</b> <a class="pull-right" id="isDefault"></a>
                        </li>
                        <li class="list-group-item">
                            <b>装饰公司类型</b> <a class="pull-right" id="company"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店类型</b> <a class="pull-right" id="storeType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否启用</b> <a class="pull-right" id="enable"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否支持门店自提</b> <a class="pull-right" id="isSelfDelivery"></a>
                        </li>
                        <li class="list-group-item">
                            <b>省</b> <a class="pull-right" id="province"></a>
                        </li>
                        <li class="list-group-item">
                            <b>市</b> <a class="pull-right" id="city"></a>
                        </li>
                        <li class="list-group-item">
                            <b>区</b> <a class="pull-right" id="area"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店地址详细地址</b> <a class="pull-right" id="detailedAddress"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店电话</b> <a class="pull-right" id="phone"></a>
                        </li>

                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>
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
            field: 'storeOrgId',
            title: '门店组织id',
            align: 'center'
        },{
            field: 'storeCode',
            title: '门店编码',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.storeId);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
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

    var $page = {
        information: {
            show: function (storeId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/stores/' + storeId,
                        method: 'GET',
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            if (0 === result.code) {
                                var data = result.content;
                                $('#menuTitle').html("门店详情");

                                if (null === data.storeId) {
                                    data.storeId = '-';
                                }
                                $('#storeId').html(data.storeId);




                                if (null === data.company) {
                                    data.company = '-';
                                }
                                $('#company').html(data.company);



                                if (null === data.storeOrgId) {
                                    data.storeOrgId = '-';
                                }
                                $('#storeOrgId').html(data.storeOrgId);

                                if (null === data.storeCode) {
                                    data.storeCode = '-';
                                }
                                $('#storeCode').html(data.storeCode);

                                if (null === data.cityCode.name) {
                                    data.cityCode.name = '-';
                                }
                                $('#cityName').html(data.cityCode.name);

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (true === data.isDefault) {
                                    data.isDefault = '<span class="label label-primary">是</span>';
                                } else if (false === data.isDefault) {
                                    data.isDefault = '<span class="label label-danger">否</span>';
                                } else {
                                    data.isDefault = '<span class="label label-danger">-</span>';
                                }
                                $('#isDefault').html(data.isDefault);

                                if (true === data.enable) {
                                    data.enable = '<span class="label label-primary">是</span>';
                                }else if(false === data.enable){
                                    data.enable = '<span class="label label-danger">否</span>';
                                }else{
                                    data.enable  = '<span class="label label-danger">-</span>';
                                }
                                $('#enable').html(data.enable);

                                if ('ZY' === data.storeType) {
                                    data.storeType = '自营';
                                } else if ('ZS' === data.storeType) {
                                    data.storeType = '装饰公司';
                                } else if ('JM' === data.storeType) {
                                    data.storeType = '加盟店';
                                } else if('FXCK' === data.storeType){
                                    data.storeType = '分销仓库';
                                } else if('FX' === data.storeType){
                                    data.storeType = '分销';
                                } else{
                                    data.storeType = '-';
                                }
                                $('#storeType').html(data.storeType);

                                if (null === data.createTime) {
                                    $('#createTime').html('-');
                                } else {
                                    $('#createTime').html(formatDateTime(data.createTime));
                                }

                                if (true === data.isSelfDelivery) {
                                    data.isSelfDelivery = '是';
                                } else if (false === data.isSelfDelivery) {
                                    data.isSelfDelivery = '否';
                                } else {
                                    data.isSelfDelivery = '-';
                                }
                                $('#isSelfDelivery').html(data.isSelfDelivery);

                                if (null === data.province) {
                                    data.province = '-';
                                }
                                $('#province').html(data.province);

                                if (null === data.city) {
                                    data.city = '-';
                                }
                                $('#city').html(data.city);

                                if (null === data.area) {
                                    data.area = '-';
                                }
                                $('#area').html(data.area);

                                if (null === data.detailedAddress) {
                                    data.detailedAddress = '-';
                                }
                                $('#detailedAddress').html(data.detailedAddress);

                                if (null === data.phone) {
                                    data.phone = '-';
                                }
                                $('#phone').html(data.phone);

                                $('#information').modal();
                            } else {
                                $notify.danger(result.message);
                            }
                        }
                    })
                }
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }


/*    function findStoreByCity(cityId) {
        $("#enabled").val('-1');
        $("#enabled").append(enabled);
        findStoreByCityId(cityId);
    }

    function findStoreByCityId(cityId) {
        $("#queryStoreInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        if (cityId == -1) {
            initDateGird('/rest/stores/page/grid');
        } else {
            initDateGird('/rest/stores/findStoresListByCity/' + cityId);
        }
    }*/

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