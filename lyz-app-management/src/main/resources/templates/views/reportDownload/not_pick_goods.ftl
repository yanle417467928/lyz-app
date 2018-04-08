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
                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findStorelist()" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>

                    <select name="storeType" id="storeType" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findStorelist()" data-live-search="true">
                        <option value="">选择门店类型</option>
                    <#if storeTypes??>
                        <#list storeTypes as storeType>
                            <option value="${storeType.value}">${storeType.description}</option>
                        </#list>
                    </#if>
                    </select>

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>

                    <select name="pickType" id="pickType" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="">未提货类型</option>
                    <#if pickTypes??>
                        <#list pickTypes as pickType>
                            <option value="${pickType.value}">${pickType.description}</option>
                        </#list>
                    </#if>
                    </select>

                    <input name="startTime" onchange="findByCondition()" type="text" class="form-control datepicker"
                           id="startTime" style="width: 120px;" placeholder="购买时间">
                    <input name="endTime" onchange="findByCondition()" type="text" class="form-control datepicker"
                           id="endTime" style="width: 120px;" placeholder="过期时间">

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
//        initDateGird(null, null, null, null, null, null);
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
                })
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
                findByCondition();
            }
        });
    }

    function initDateGird(startTime, endTime, storeId, storeType, cityId, pickType) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/not/pickGoods/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                storeId: storeId,
                startTime: startTime,
                endTime: endTime,
                storeType: storeType,
                cityId: cityId,
                pickType: pickType
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
        }, {
            field: 'pickType',
            title: '自提类型',
            align: 'center'
        }, {
            field: 'buyTime',
            title: '购买时间',
            align: 'center'
        }, {
            field: 'effectiveTime',
            title: '过期时间',
            align: 'center'
        }, {
            field: 'customerId',
            title: '顾客ID',
            align: 'center'
        }, {
            field: 'customerName',
            title: '顾客姓名',
            align: 'center'
        }, {
            field: 'customerPhone',
            title: '顾客电话',
            align: 'center'
        }, {
            field: 'customerType',
            title: '顾客类型',
            align: 'center'
        }, {
            field: 'sellerName',
            title: '导购',
            align: 'center'
        }, {
            field: 'sku',
            title: '商品编码',
            align: 'center'
        }, {
            field: 'skuName',
            title: '商品名称',
            align: 'center'
        }, {
            field: 'quantity',
            title: '数量',
            align: 'center'
        }, {
            field: 'buyPrice',
            title: '购买价',
            align: 'center'
        }, {
            field: 'totalBuyPrice',
            title: '购买总价',
            align: 'center'
        }, {
            field: 'referenceNumber',
            title: '关联单号',
            align: 'center'
        }
        ]);
    }

    function findByCondition() {
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        var pickType = $('#pickType').val();
        initDateGird(startTime, endTime, storeId, storeType, cityId, pickType);
    }

    function findByOrderNumber() {
        $('#startTime').val('');
        $('#endTime').val('');
        $("#storeCode").val(-1);
        $('#cityCode').val(-1);
        $('#storeType').val('');
        $('#pickType').val('');
        $('#storeCode').selectpicker('refresh');
        $('#storeCode').selectpicker('render');
        $('#cityCode').selectpicker('refresh');
        $('#cityCode').selectpicker('render');
        $('#storeType').selectpicker('refresh');
        $('#storeType').selectpicker('render');
        $('#pickType').selectpicker('refresh');
        $('#pickType').selectpicker('render');
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        var pickType = $('#pickType').val();
        initDateGird(startTime, endTime, storeId, storeType, cityId, pickType);
    }

    function showDetails(id) {
        window.location.href = '/views/decorationCompany/creditBilling/' + id;
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
        var pickType = $('#pickType').val();

        var url = "/rest/reportDownload/not/pickGoods/download?storeId=" + storeId + "&startTime=" + startTime
                + "&endTime=" + endTime + "&storeType=" + storeType + "&cityId=" + cityId + "&pickType=" + pickType;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);
    }

</script>
</body>