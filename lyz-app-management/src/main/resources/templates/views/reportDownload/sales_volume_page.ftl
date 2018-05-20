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
                <form class="form-horizontal" id="formSearch" style="margin:10px 5px;padding-bottom: 1px;">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="form-group">
                                <label class="control-label col-md-2 col-xs-3" for="number" style="text-align: center">分公司:</label>
                                <div class="col-md-2 col-xs-3" style="text-align: left">
                                    <select id="companyCode" name="companyCode" class="form-control selectpicker">
                                        <option value="RCC001">润成分公司</option>
                                        <option value="PCC001">鹏成分公司</option>
                                        <option value="BYC001">北宇分公司</option>
                                        <option value="RDC001">润东分公司</option>
                                        <option value="ZZC001">郑州分公司</option>
                                        <option value="GZC001">贵州分公司</option>
                                        <option value="SXC001">陕西分公司</option>
                                        <option value="CQC001">重庆分公司</option>
                                    </select>
                                </div>
                                <label class="control-label col-md-1 col-xs-3" for="startTime">日期:</label>
                                <div class="col-md-2 col-xs-3" style="text-align:left;">
                                    <input name="startTime" type="text" class="form-control datepicker" id="startTime"
                                           placeholder="开始时间"
                                           readonly>
                                </div>
                                <label class="control-label col-md-1 col-xs-3" for="endTime"
                                       style="text-align: center">至</label>
                                <div class="col-md-2 col-xs-3" style="text-align:left;">
                                    <input name="endTime" type="text" class="form-control datepicker" id="endTime"
                                           placeholder="结束时间"
                                           readonly>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="form-group">
                                <label class="control-label col-md-2 col-xs-3" for="storeType"
                                       style="text-align: center">门店类型:</label>
                                <label class="control-label margin-6" style="margin-left: 5%">
                                    <input type="radio" name="storeType" value="ZY" class="iradio_square-blue "
                                           checked>
                                    直营
                                </label>
                                <label class="control-label margin-6" style="margin-left: 5%">
                                    <input type="radio" name="storeType" value="JM" class="iradio_square-blue ">
                                    加盟
                                </label>
                                <label class="control-label margin-6" style="margin-left: 5%">
                                    <input type="radio" name="storeType" value="FX" class="iradio_square-blue ">
                                    分销(不分公司)
                                </label>
                                <label class="control-label margin-6" style="margin-left: 5%">
                                    <input type="radio" name="storeType" value="ZS" class="iradio_square-blue ">
                                    装饰公司(不分公司)
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="form-group">
                                <label class="control-label col-md-2 col-xs-3" for="number" style="text-align: center">是否平铺产品劵:</label>
                                <label class="control-label margin-6" style="margin-left: 5%">
                                    <input type="radio" name="product" value="1" class="iradio_square-blue "
                                           checked>
                                    是&nbsp;&nbsp;&nbsp;
                                </label>
                                <#--<label class="control-label margin-6" style="margin-left: 5%">-->
                                    <#--<input type="radio" name="product" value="0" class="iradio_square-blue ">-->
                                    <#--否-->
                                <#--</label>-->
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="form-group">
                                <div class="col-md-2 col-xs-3" style="text-align:center;">
                                    <button id="btn_add" type="button" class=" btn btn-default"
                                            onclick="openBillModal()">
                                        <i class="fa fa-download"></i>
                                        下载报表
                                    </button>
                                </div>
                                <div class="col-md-7 col-xs-3">
                                </div>
                                <div class="col-md-1 col-xs-3" style="text-align:center;">
                                    <button type="button" id="btn_query"
                                            class="btn btn-primary" onclick="return findByCondition()">
                                        <i class="fa fa-search"></i> 查询
                                    </button>
                                </div>
                                <div class="col-md-1 col-xs-3" style="text-align:left;">
                                    <button type="reset" class="btn btn-default" onclick="clearAll()">
                                        <i class="fa fa-print"></i> 重置
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                </form>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary">
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
        //时间选择框样式
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

    });


    function initDateGird(keywords, companyCode, storeType, product, startTime, endTime) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/salesReport/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                companyCode: companyCode,
                startTime: startTime,
                endTime: endTime,
                storeType: storeType,
                isProductCoupon: product
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
        var storeType = $('input[name="storeType"]:checked ').val()
        var isProductCoupon = $('input[name="product"]:checked ').val()
        var companyCode = $('#companyCode').val();
        initDateGird(keywords, companyCode, storeType, isProductCoupon, startTime, endTime);
    }


    function openBillModal() {
        var keywords = $("#queryCusInfo").val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var companyCode = $('#companyCode').val();
        var isProductCoupon = $('input[name="product"]:checked ').val()
        var storeType =  $('input[name="storeType"]:checked ').val()

        var url = "/rest/reportDownload/salesReport/download?companyCode=" + companyCode + "&startTime=" + startTime
                + "&endTime=" + endTime + "&storeType=" + storeType + "&isProductCoupon=" + isProductCoupon;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);

    }

    function clearAll() {
        $('select').prop('selectedIndex', 0);
        $("select").selectpicker('refresh');
        $("#dataGrid").bootstrapTable('destroy');
    }

</script>
</body>