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
                    <form>
                    <#--<@shiro.hasPermission name="/views/admin/resource/add">-->
                        <button id="btn_add" type="button" class=" btn btn-default" onclick="openBillModal()">
                            <i class="fa fa-download"></i>
                            下载报表
                        </button>
                    <#--</@shiro.hasPermission>-->
                        <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px"
                                style="width:auto;"
                                onchange="findStorelist()" data-live-search="true">
                            <option value="-1">选择城市</option>
                        </select>
                        <select name="storeType" id="storeType" class="form-control selectpicker" data-width="120px"
                                style="width:auto;"
                                onchange="findStorelist()" data-live-search="true">
                            <option value="-1">选择门店类型</option>
                        <#if storeTypes??>
                            <#list storeTypes as storeType>
                                <option value="${storeType.value}">${storeType.description}</option>
                            </#list>
                        </#if>
                        </select>
                        <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                                style="width:auto;"
                        <#--onchange="findByCondition()"--> data-live-search="true">
                            <option value="-1">选择门店</option>
                        </select>

                        <input name="startTime" <#--onchange="findByCondition()"--> type="text"
                               class="form-control datepicker" id="startTime" style="width: 140px;" placeholder="开始时间"
                               readonly>
                        <input name="endTime" <#--onchange="findByCondition()"--> type="text"
                               class="form-control datepicker" id="endTime" style="width: 140px;" placeholder="结束时间"
                               readonly>
                        <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                onclick="return findByOrderNumber()">查找
                        </button>
                        <button type="reset" class="btn btn-default" onclick="clearAll()">重置
                        </button>
                    </form>
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
        //initDateGird(null, null, null, null, null, null);
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

    function initDateGird(keywords, storeId, storeType, cityId, startTime, endTime) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/goodsShipmentAndReturn/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                storeId: storeId,
                startTime: startTime,
                endTime: endTime,
                storeType: storeType,
                cityId: cityId
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
            field: 'ordNo',
            title: '订单号',
            align: 'center'
        }, {
            field: 'operationTime',
            title: '出/退货时间',
            align: 'center'
        }, {
            field: 'sku',
            title: '产品编码',
            align: 'center'
        }, {
            field: 'skuName',
            title: '产品名称',
            align: 'center'
        }, {
            field: 'orderQty',
            title: '数量',
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
        initDateGird(keywords, storeId, storeType, cityId, startTime, endTime);
    }
    function findByOrderNumber() {
        /*$('#startTime').val('');
        $('#endTime').val('');
        $("#storeCode").val(-1);
        $('#cityCode').val(-1);
        $('#storeType').val('');
        $('#storeCode').selectpicker('refresh');
        $('#storeCode').selectpicker('render');
        $('#cityCode').selectpicker('refresh');
        $('#cityCode').selectpicker('render');
        $('#storeType').selectpicker('refresh');
        $('#storeType').selectpicker('render');*/
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        initDateGird(queryCusInfo, storeId, storeType, cityId, startTime, endTime);
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

        var url = "/rest/reportDownload/goodsShipmentAndReturn/download?storeId=" + storeId + "&startTime=" + startTime
                + "&endTime=" + endTime + "&storeType=" + storeType + "&cityId=" + cityId;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);

    }

      function clearAll() {
          $('select').prop('selectedIndex', 0);
          $("select").selectpicker('refresh');
          $("#dataGrid").bootstrapTable('destroy');
          //initDateGird(null, null, null, null, null, null);
      }

</script>
</body>