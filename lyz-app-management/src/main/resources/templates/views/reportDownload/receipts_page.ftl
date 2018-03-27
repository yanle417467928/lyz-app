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
                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findStorelist()" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>

                    <select name="storeType" id="storeType" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findStorelist()" data-live-search="true">
                        <option value="">选择门店类型</option>
                        <#if storeTypes??>
                            <#list storeTypes as storeType>
                                <option value="${storeType.value}">${storeType.description}</option>
                            </#list>
                        </#if>
                    </select>

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>

                    <select name="paymentType" id="paymentType" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="">选择支付方式</option>
                        <#if paymentTypes??>
                            <#list paymentTypes as paymentType>
                                <option value="${paymentType.value}">${paymentType.description}</option>
                            </#list>
                        </#if>
                    </select>

                    <input name="startTime" onchange="findByCondition()" type="text" class="form-control datepicker" id="startTime" style="width: 120px;" placeholder="开始时间">
                    <input name="endTime" onchange="findByCondition()" type="text" class="form-control datepicker" id="endTime" style="width: 120px;" placeholder="结束时间">

                    <div class="input-group col-md-3" style="margin-top:0px; positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                               placeholder="请输入要查找的单号">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findByOrderNumber()">查找</button>
                        </span>
                    </div>
                <#--<@shiro.hasPermission name="/views/admin/resource/add">-->
                    <button id="btn_add" type="button" class="btn btn-default pull-left" onclick="openBillModal()">
                        <i class="fa fa-download"></i>
                        下载报表
                    </button>
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
        initDateGird(null,null,null,null,null,null,null);
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
                })
                $("#cityCode").append(city);
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
            data:{
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
                })
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
                findByCondition();
            }
        });
    }

    function initDateGird(keywords,startTime,endTime,storeId,storeType,cityId,payType) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/receipts/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                storeId: storeId,
                startTime: startTime,
                endTime: endTime,
                storeType: storeType,
                cityId: cityId,
                payType: payType
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'cityName',
            title: '城市',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        }, {
            field: 'storeType',
            title: '门店类型',
            align: 'center'
        },  {
            field: 'payTime',
            title: '付款/退款时间',
            align: 'center'
        }, {
            field: 'payType',
            title: '支付方式',
            align: 'center'
        }, {
            field: 'money',
            title: '支付金额',
            align: 'center'
        }, {
            field: 'orderNumber',
            title: '订/退单号',
            align: 'center'
        }, {
            field: 'remarks',
            title: '备注',
            align: 'center'
        }
        ]);
    }

    function findByCondition() {
        $("#queryCusInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        var paymentType = $('#paymentType').val();
        initDateGird(keywords,startTime,endTime,storeId,storeType,cityId,paymentType);
    }

    function findByOrderNumber() {
        $('#startTime').val('');
        $('#endTime').val('');
        $("#storeCode").val(-1);
        $('#cityCode').val(-1);
        $('#storeType').val('');
        $('#paymentType').val('');
        $('#storeCode').selectpicker('refresh');
        $('#storeCode').selectpicker('render');
        $('#cityCode').selectpicker('refresh');
        $('#cityCode').selectpicker('render');
        $('#storeType').selectpicker('refresh');
        $('#storeType').selectpicker('render');
        $('#paymentType').selectpicker('refresh');
        $('#paymentType').selectpicker('render');
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        var paymentType = $('#paymentType').val();
        initDateGird(queryCusInfo,startTime,endTime,storeId,storeType,cityId,paymentType);
    }

    function showDetails(id){
        window.location.href = '/views/decorationCompany/creditBilling/'+id;
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function openBillModal() {
        var keywords = $("#queryCusInfo").val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        var payType = $('#paymentType').val();

        var url = "/rest/reportDownload/receipts/download?keywords="+ keywords + "&storeId=" + storeId + "&startTime=" + startTime
                + "&endTime=" + endTime + "&storeType=" + storeType + "&cityId=" + cityId + "&payType=" + payType;
        var escapeUrl=url.replace(/\#/g,"%23");
        window.open(escapeUrl);

//        $.ajax({
//            url: '/rest/reportDownload/receipts/download',
//            method: 'GET',
//            data:{
//                keywords: keywords,
//                storeId: storeId,
//                startTime: startTime,
//                endTime: endTime,
//                storeType: storeType,
//                cityId: cityId,
//                payType: payType
//            },
//            error: function () {
//                clearTimeout($global.timer);
//                $loading.close();
//                $global.timer = null;
//                $notify.danger('网络异常，请稍后重试或联系管理员');
//            },
//            success: function (data, textStatus, request) {
//                clearTimeout($global.timer);
//                window.open(request.getResponseHeader('Content-Disposition'));
//            }
//        });
    }

</script>
</body>