<head>
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="/plugins/iCheck/all.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/plugins/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>

    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script src="/plugins/iCheck/icheck.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.js"></script>
    <script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script type="text/javascript" src="/javascript/cashCoupon/cashCoupon_send.js"></script>

</head>

<body>
<section class="content-header">
    <h1>现金券发放</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">现金券基本信息</a></li>
        </ul>
        <input type="hidden" id="cashCouponId" value="<#if cashCoupon??>${cashCoupon.id?c}</#if>">
        <div class="row">
            <div class="col-md-2">
                <h3>标题：<#if cashCoupon??>${cashCoupon.title}</#if></h3>
            </div>
            <div class="col-md-2">
                <h3>面额￥：<#if cashCoupon??>${cashCoupon.denomination}</#if></h3>
            </div>
            <div class="col-md-2">
                <h3>使用条件￥：<#if cashCoupon??>${cashCoupon.condition}</#if></h3>
            </div>
            <div class="col-md-2">
                <h3>剩余数量：<#if cashCoupon??>${cashCoupon.remainingQuantity}</#if></h3>
            </div>
        </div>
    </div>

    <div class="nav-tabs-custom">
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary">
                    <div id="toolbar" class="form-inline">

                        <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                                onchange="findCusByCity(this.value)">
                            <option value="-1">选择城市</option>
                        </select>


                        <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                                onchange="findCusByStoreId()"   data-live-search="true" >
                            <option value="-1">选择门店</option>
                        </select>
                    <#--             <select name="guideCode" id="guideCode" class="form-control select" style="width:auto;"
                                         onchange="findCusByGuide()">
                                     <option value="-1">选择导购</option>
                                 </select>-->

                        <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                            <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                                   placeholder="请输入要查找的姓名或电话..">
                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCusByNameOrPhone()">查找</button>
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

    </div>
</section>

<script>

    $(function () {

    })

</script>
</body>