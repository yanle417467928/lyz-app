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
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                <#--<@shiro.hasPermission name="/customer/add">-->
                    <button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                <#--</@shiro.hasPermission>-->
                <#--<@shiro.hasPermission name="/customer/add">-->
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                <#--</@shiro.hasPermission>-->
                    <select name="cityCode" id="cityCode" class="form-control " data-width="120px"
                            style="width:auto;"
                            onchange="findListByCityId(this.value)" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="storeType" id="storeType" class="form-control " data-width="120px"
                            style="width:auto;"
                            onchange="findListByStoreType()" data-live-search="true">
                        <option value="-1">选择门店类型</option>
                    <#if fitCompanyTyoes??>
                        <#list fitCompanyTyoes as fitCompanyTyoe>
                            <option value="${fitCompanyTyoe.value}">${fitCompanyTyoe.description}</option>
                        </#list>
                    </#if>

                    </select>
                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findListByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <#--<div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control"
                               style="width:auto;"
                               placeholder="请输入要查找的姓名或电话.." onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCusByNameOrPhone()">查找</button>
                        </span>
                    </div>-->

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

    $(function () {
        findCitylist();
        findStorelist();
        initDateGird('/rest/decorationCompany/billRule/page/grid',null,null,null);
    });
    
    
    function findListByCityId(cityId) {
        findStorelist();
        var storeType = $('#storeType').val();
        initDateGird('/rest/decorationCompany/billRule/page/grid',null,storeType,cityId);
    }

    function findCitylist() {
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
                    city += "<option value='" + item.cityId + "'>" + item.name + "</option>";
                });
                $("#cityCode").append(city);
                $("#cityCode").selectpicker('refresh');
                $("#cityCode").selectpicker('render');
            }
        });
    }

    function  findListByStoreType() {
        findStorelist();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        initDateGird('/rest/decorationCompany/billRule/page/grid',null,storeType,cityId);
    }

    function findListByCondition(){
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        var storeId = $("#storeCode").val();
        initDateGird('/rest/decorationCompany/billRule/page/grid',storeId,storeType,cityId);
    }

    function findStorelist() {
        var store = ""
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        initSelect("#storeCode", "选择门店");
        $.ajax({
            url: '/rest/stores/findZSStoresListByCityIdAndStoreType',
            data:{
                cityId:cityId,
                storeType:storeType
            },
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
                    store += "<option value=" + item.storeId + ">" + item.storeName + "</option>";
                })
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
            }
        });
    }

    function initDateGird(url,storeId,storeType,cityId) {
        $("#dataGrid").bootstrapTable('destroy');
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
                storeId:storeId,
                storeType:storeType,
                cityId:cityId
            }
        }, [{
                checkbox: true,
                title: '选择'
            }, {
                field: 'id',
                title: 'ID',
                align: 'center'
            }, {
                field: 'cityName',
                title: '城市',
                align: 'center'
            }, {
                field: 'storeName',
                title: '门店',
                align: 'center'
            }, {
                field: 'storeCode',
                title: '门店编码',
                align: 'center'
            }, {
                field: 'billDate',
                title: '出账日',
                align: 'center'
            }, {
                field: 'repaymentDeadlineDate',
                title: '还款截至日',
                align: 'center'
            }, {
                field: 'interestRate',
                title: '利率(单位：万分之一/天 )',
                align: 'center'
            }, {
                field: 'createTime',
                title: '创建时间',
                align: 'center'
            }
            , {
                title: '操作',
                align: 'center',
                formatter: function(value,row) {
                    return '<button class="btn btn-primary btn-xs" onclick="showChangeLog('+row.id+')"> 查看变更日志</button>';
                }
            }
        ]);

       $('#btn_add').on('click', function () {
            $grid.add('/views/decorationCompany/billRule/add?parentMenuId=${(parentMenuId!'0')}');
       })
        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/decorationCompany/billRule/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
        <#--$('#btn_update').on('click', function () {-->
            <#--modify($('#dataGrid'), '/views/admin/customers/update/{id}?parentMenuId=${parentMenuId!'0'}')-->
        <#--});-->
    }

    function findListByStoreId() {
        var storeId = $("#storeCode").val();
        initDateGird('/rest/decorationCompany/billRule/page/grid', storeId);
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
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

    function getSelectedIds(container) {
        var ids = [];
        var selected = container.bootstrapTable('getSelections');
        for (var i = 0; i < selected.length; i++) {
            var data = selected[i];
            ids.push(data.cusId);
        }cityCode
        return ids;
    }

    function showChangeLog(id){
        window.location.href = '/views/decorationCompany/billRule/log/list/'+id;
    }
</script>
</body>