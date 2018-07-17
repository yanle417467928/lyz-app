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
                <div class="box-body table-reponsive">
                    <div id="toolbar" class="form-inline">
                        <form>
                        <#--<@shiro.hasPermission name="/views/admin/resource/add">-->
                            <button id="btn_add" type="button" class=" btn btn-default" onclick="openBillModal()">
                                <i class="fa fa-download"></i>
                                下载报表
                            </button>
                        <#--</@shiro.hasPermission>-->
                            <select name="company" id="companyCode" class="form-control" style="width:auto;"
                                    onchange="findStorelist()" data-live-search="true">

                            <#if structureList?? && structureList?size gt 0 >
                                <#list structureList as structure>
                                    <option value="${structure.number!''}">${structure.structureName!''}</option>
                                </#list>
                            </#if>
                                <option value="ALL">所有分公司</option>
                            </select>
                            <select name="storeType" id="storeType" class="form-control" style="width:auto;"
                                    onchange="findStorelist()" data-live-search="true">
                                <option value="ZY">直营</option>
                                <option value="JM">加盟</option>
                                <option value="FXAll">分销(总)</option>
                                <option value="FX">分销门店</option>
                                <option value="FXCK">分销仓库</option>
                                <option value="ZS">装饰公司</option>
                            </select>
                            <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                                    style="width:auto;"
                            <#--onchange="findByCondition()"--> data-live-search="true">
                                <option value="-1">选择门店</option>
                            </select>
                            <select name="productType" id="productType" class="form-control selectpicker"
                                    data-width="120px"
                                    style="width:auto;"
                            <#--onchange="findByCondition()"--> data-live-search="true">
                                <option value="ALL">全部品牌</option>
                                <option value="LYZ">乐易装（包含莹润）</option>
                                <option value="HR">华润</option>
                            </select>
                            <input name="startTime" <#--onchange="findByCondition()"--> type="text"
                                   class="form-control datepicker" id="startTime" style="width: 140px;"
                                   placeholder="开始时间"
                                   readonly>
                            <input name="endTime" <#--onchange="findByCondition()"--> type="text"
                                   class="form-control datepicker" id="endTime" style="width: 140px;"
                                   placeholder="结束时间"
                                   readonly>
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findByCondition()">查找
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
    </div>
</section>
<script>

    $(function () {
        //时间选择框样式
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
        findStorelist();
    });


    function initDateGird(keywords, companyCode, storeType, product, startTime, endTime, productType, storeId) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/salesReport/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                companyCode: companyCode,
                startTime: startTime,
                endTime: endTime,
                storeType: storeType,
                isProductCoupon: product,
                storeId: storeId,
                productType: productType
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
            field: 'name',
            title: '导购名称',
            align: 'center'
        }, {
            field: 'customerName',
            title: '会员名称',
            align: 'center'
        }, {
            field: 'ordNo',
            title: '订单号',
            align: 'center'
        }, {
            field: 'isPayUp',
            title: '是否结清',
            align: 'center'
        }, {
            field: 'payUpTime',
            title: '结清时间',
            align: 'center'
        }, {
            field: 'sku',
            title: '产品编码',
            align: 'center'
        }, {
            field: 'financialSales',
            title: '财务销量',
            align: 'center'
        }, {
            field: 'distributionSales',
            title: '经销财务销量',
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
        var storeType = $('#storeType').val();
        var isProductCoupon = true;
        var companyCode = $('#companyCode').val();
        var storeCode = $('#storeCode').val();
        var productType = $('#productType').val();
        initDateGird(keywords, companyCode, storeType, isProductCoupon, startTime, endTime, productType, storeCode);
    }


    function openBillModal() {
        var keywords = $("#queryCusInfo").val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var companyCode = $('#companyCode').val();
        var isProductCoupon = true;
        var storeType = $('#storeType').val();
        var storeCode = $('#storeCode').val();
        var productType = $('#productType').val();
        var url = "/rest/reportDownload/salesReport/download?companyCode=" + companyCode + "&startTime=" + startTime
                + "&endTime=" + endTime + "&storeType=" + storeType + "&isProductCoupon=" + isProductCoupon + "&storeId=" + storeCode
                + "&productType=" + productType;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);
    }

    function clearAll() {
        $('select').prop('selectedIndex', 0);
        $("select").selectpicker('refresh');
        $("#dataGrid").bootstrapTable('destroy');
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function findStorelist() {
        initSelect("#storeCode", "选择门店");
        var store = "";
        var companyCode = $('#companyCode').val();
        var storeType = $('#storeType').val();
        /*    if('FX'==storeType||'ZS'==storeType){
                $('#companyCode').val(-1);
                $('#storeCode').val(-1);
                companyCode=-1;
                $('#companyCode').selectpicker('refresh');
                $('#companyCode').selectpicker('render');
            }*/
        $.ajax({
            url: '/rest/stores/findStoresListByCompanyCodeAndStoreTypeForSale',
            method: 'GET',
            data: {
                storeType: storeType,
                companyCode: companyCode
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
            }
        });
    }

</script>
</body>