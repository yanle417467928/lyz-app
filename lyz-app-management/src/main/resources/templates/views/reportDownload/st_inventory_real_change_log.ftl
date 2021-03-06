<head>

    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>

</head>

<body>
<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parentResourceName??>
            <li><a href="javascript:void(0);">${selectedMenu.parentResourceName!'??'}</a></li>
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
                    <button id="btn_add" type="button" class="btn btn-default pull-left" onclick="openBillModal()">
                        <i class="fa fa-download"></i>
                        下载报表
                    </button>

                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findStorelist()" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>

                    <#--<select name="storeType" id="storeType" class="form-control selectpicker" data-width="120px"-->
                            <#--style="width:auto;"-->
                            <#--onchange="findStorelist()" data-live-search="true">-->
                        <#--<option value="">选择门店类型</option>-->
                    <#--<#if storeTypes??>-->
                        <#--<#list storeTypes as storeType>-->
                            <#--<option value="${storeType.value}">${storeType.description}</option>-->
                        <#--</#list>-->
                    <#--</#if>-->
                    <#--</select>-->

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>

                    <input name="startTime" onchange="findByCondition()" type="text" class="form-control datepicker"
                           id="startTime" style="width: 120px;" placeholder="开始时间">

                    <input name="endTime" onchange="findByCondition()" type="text" class="form-control datepicker"
                           id="endTime" style="width: 120px;" placeholder="截止时间">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findByCondition()">查找</button>

                <#--<@shiro.hasPermission name="/views/admin/resource/add">-->
                    <#--<button id="btn_add" type="button" class="btn btn-default pull-left" onclick="openBillModal()">-->
                        <#--<i class="fa fa-download"></i>-->
                        <#--下载报表-->
                    <#--</button>-->
                <#--</@shiro.hasPermission>-->

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

        //获取数据
        initDateGird(null, null, null, null, null);
        //时间选择框样式
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

    });

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
                    city += "<option value=" + item.cityId + ">" + item.name + "</option>";
                });
                $("#cityCode").append(city);
                $("#cityCode").selectpicker('refresh');
                $("#cityCode").selectpicker('render');
            }
        });
    }

    function findStorelist() {
        initSelect("#storeCode", "选择门店");
        var store = "";
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        $.ajax({
            url: '/rest/stores/findStoresListByCityIdAndStoreType',
            method: 'GET',
            data: {
                storeType: storeType,
                cityId: cityId
            },
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
                });
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
//                findByCondition();
            }
        });
    }

    function initDateGird(endTime, storeId, cityId,startTime) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/st/inventory/chang/log', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                storeId: storeId,
                endTime: endTime,
                cityId: cityId,
                startTime:startTime
            }
        }, [ {
            field: 'store',
            title: '门店',
            align: 'center'
        }, {
            field: 'skuName',
            title: '商品名称',
            align: 'center'
        }, {
            field: 'sku',
            title: '商品编码',
            align: 'center'
        }, {
            field: 'initialQty',
            title: '初始数量',
            align: 'center'
        }, {
            field: 'changeQty',
            title: '变更数量',
            align: 'center'
//            visible: false
        }, {
            field: 'afterChangeQty',
            title: '变更后数量',
            align: 'center'
        },  {
            field: 'changeTypeDesc',
            title: '变更类型',
            align: 'center'
        }, {
            field: 'referenceNumber',
            title: '订单号',
            align: 'center'
        }, {
            field: 'returnNumber',
            title: '退单号',
            align: 'center'
        },{
            field: 'changeTime',
            title: '变更时间',
            align: 'center'
        },{
            field: 'changeTypeDesc',
            title: '变更类型',
            align: 'center'
        }, {
            field: 'changeTime',
            title: '变更时间',
            align: 'center'
        }
        ]);
    }

    function findByCondition() {
        $("#dataGrid").bootstrapTable('destroy');
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var startTime = $('#startTime').val();
        initDateGird(endTime, storeId, cityId,startTime);
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function openBillModal() {
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();

        var url = "/rest/reportDownload/st/inventory/changeLog/excel?&storeId=" + storeId + "&startTime=" + startTime
                + "&endTime=" + endTime + "&storeType=" + storeType + "&cityId=" + cityId;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);
    }

</script>
</body>