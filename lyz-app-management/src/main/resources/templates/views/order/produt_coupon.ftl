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


</head>
<body>
<section class="content-header">
    <h1>购买产品券</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <div class="row">
                    <div class="col-xs-12 col-md-2">
                        <label for="title">
                            城市
                        </label>
                        <select name="city" id="cityId" class="form-control select"
                                onchange="findStoreByCity(this.value)">
                            <option value="-1">选择城市</option>
                        </select>
                    </div>

                    <div class="col-xs-12 col-md-2" style="margin-left: 180px;">
                        <label for="title">
                            门店
                        </label>
                        <select name="store" id="storeId" class="form-control select">
                            <option value="-1">选择门店</option>
                        </select>
                    </div>
                </div>
                <br>
                <div class="row">
                <#--<div class="col-xs-12 col-md-2">-->
                    <div class="col-md-4 invoice-col">
                        <ul style="line-height: 40px;">
                            <li style="list-style: none">
                                <button type="button" class="btn btn-primary btn-xs"
                                        onclick="openSellerModal()">
                                    选择导购
                                </button>
                            </li>
                            <li style="list-style: none">
                                <b>导购id:</b>
                                <spanp id="sellerId" class="span"></spanp>
                            </li>
                            <li style="list-style: none">
                                <b>导购姓名:</b>
                                <spanp id="sellerName" class="span"></spanp>
                            </li>
                            <li style="list-style: none">
                                <b>导购电话:</b>
                                <spanp id="sellerPhone" class="span"></spanp>
                                <spanp id="storeType" class="span" style="display:none"></spanp>
                                <spanp id="balance" class="span" style="display:none"></spanp>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-4 invoice-col">
                        <ul style="line-height: 40px;">
                            <li style="list-style: none">
                                <button type="button" class="btn btn-primary btn-xs"
                                        onclick="openCustomerModal()">
                                    选择顾客
                                </button>
                            </li>
                            <li style="list-style: none">
                                <b>顾客id:</b>
                                <spanp id="customerId" class="span"></spanp>
                            </li>
                            <li style="list-style: none">
                                <b>顾客姓名:</b>
                                <spanp id="customerName" class="span"></spanp>
                            </li>
                            <li style="list-style: none">
                                <b>顾客电话:</b>
                                <spanp id="customerPhone" class="span"></spanp>
                                <spanp id="customerType" style="display:none"></spanp>
                            </li>
                        </ul>
                    </div>
                <#--</div>-->
                </div>
                <di>


                    <!-- 选择商品table -->
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header">
                                    <h3 class="box-title">选择商品</h3>

                                    <div class="box-tools">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openGoodsModal('selectedGoodsTable')">
                                            选择商品
                                        </button>

                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                    class="fa fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">

                                    <div class="col-xs-12">
                                        <table class="table table-hover">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>sku</th>
                                                <th>商品名</th>
                                                <th>会员价</th>
                                                <th>零售价</th>
                                                <th>数量</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody id="selectedGoodsTable">

                                            </tbody>

                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 选择赠品table -->
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header">
                                    <h3 class="box-title">选择赠品</h3>

                                    <div class="box-tools">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openGiftsModal()">
                                            选择赠品
                                        </button>

                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                    class="fa fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-header -->
                            <#--赠品促销标题-->
                                <div id="giftMessage">


                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="fullAmount_div">
                        <div class="col-xs-12 col-md-12">
                            <div class="box box-success">
                                <div class="col-xs-12">
                                    <h3 class="box-titl">
                                        收款
                                    </h3>
                                </div>
                                <div class="col-xs-6">
                                    <div class="col-xs-2" style="margin-left: 115px;">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="selectPaymnet('offlinePayments')">
                                            选择线下支付
                                        </button>
                                    </div>
                                    <div id="offlinePayments">
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">现金
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="cashMoney" id="cashMoney"
                                                           class="form-control" placeholder="现金金额"
                                                           onblur="priceBlur('cashMoney')"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">POS
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="posMoney" id="posMoney"
                                                           class="form-control" placeholder="POS金额"
                                                           onblur="priceBlur('posMoney')"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">POS流水号
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="posNumber" id="posNumber"
                                                           class="form-control" placeholder="POS交易流水号后六位"
                                                           onKeyUp="if(this.value.length>6){this.value=this.value.substr(0,6)};this.value=this.value.replace(/[^\d]/g,'');"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">其他
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="otherMoney" id="otherMoney"
                                                           class="form-control" placeholder="其他收款金额"
                                                           onblur="priceBlur('otherMoney')"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">收款时间
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input name="collectMoneyTime" type="text" class="form-control"
                                                           id="collectMoneyTime"
                                                           readonly placeholder="请选择收款时间">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">收款金额
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="totalMoney" id="totalMoney"
                                                           class="form-control" placeholder="合计收款金额"
                                                           onblur="priceBlur('totalMoney')"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">备注
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="text" name="remarks" id="remarks"
                                                           class="form-control"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>


                                <div class="col-xs-6">
                                    <div class="col-xs-2" style="margin-left: 115px;">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="selectPaymnet('preDeposit')">
                                            选择预存款支付
                                        </button>
                                    </div>
                                    <div id="preDeposit">
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">可用预存款
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="availableMoney" id="availableMoney"
                                                           class="form-control" readonly="readonly"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">预存款
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="number" name="preDepositMoney" id="preDepositMoney"
                                                           class="form-control" placeholder="预存款金额"
                                                           onblur="priceBlur('preDepositMoney')"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">收款时间
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input name="preDepositCollectMoneyTime" type="text"
                                                           class="form-control"
                                                           id="preDepositCollectMoneyTime"
                                                           readonly placeholder="请选择收款时间">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="col-xs-11" style="margin-top: 10px">
                                                <label class="col-xs-3" style="padding-right: 0px;text-align:right">备注
                                                    &ensp;</label>
                                                <div class=" col-xs-8" style="padding-left: 0px">
                                                    <input type="text" name="preDepositRemarks" id="preDepositRemarks"
                                                           class="form-control"
                                                           \>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            <#--</div>-->
                            </div>
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-2">
                            <button type="submit" class="btn btn-primary footer-btn" onclick="save()">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button id="btn-cancel" type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 取消
                            </button>
                        </div>
                    </div>


                    <!-- 商品选择框 -->
                    <div id="goodsModal" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document" style="width: 60%">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4>选择商品</h4>
                                </div>
                                <div class="modal-body">
                                    <!--  设置这个div的大小，超出部分显示滚动条 -->
                                    <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                        <section class="content">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="box box-primary">
                                                        <div id="toolbar" class="form-inline">
                                                            <select name="brandCode" id="brandCode"
                                                                    class="form-control select" style="width:auto;"
                                                                    onchange="screenGoods()">
                                                                <option value="-1">选择品牌</option>
                                                            </select>
                                                            <select name="categoryCode" id="categoryCode"
                                                                    class="form-control select" style="width:auto;"
                                                                    onchange="screenGoods()">
                                                                <option value="-1">选择分类</option>
                                                            </select>
                                                            <select name="companyCode" id="companyCode"
                                                                    class="form-control select" style="width:auto;"
                                                                    onchange="screenGoods()">
                                                                <option value="-1">选择公司</option>
                                                                <option value="LYZ">乐易装</option>
                                                                <option value="HR">华润</option>
                                                                <option value="YR">莹润</option>
                                                            </select>
                                                            <div class="input-group col-md-3"
                                                                 style="margin-top:0px positon:relative">
                                                                <input type="text" name="queryGoodsInfo"
                                                                       id="queryGoodsInfo"
                                                                       class="form-control" style="width:auto;"
                                                                       placeholder="请输入要查找的物料编码或物料名称..">
                                                                <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findGoodsByNameOrCode()">查找</button>
                        </span>
                                                            </div>
                                                        </div>
                                                        <div class="box-body table-reponsive">
                                                            <table id="goodsDataGrid"
                                                                   class="table table-bordered table-hover">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button id="goodsModalConfirm" type="button" class="btn btn-primary">确定</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                </div>
                            </div>
                        </div>
                    </div>


                    <!-- 导购选择框 -->
                    <div id="selectSeller" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document" style="width: 60%">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4>选择导购</h4>
                                </div>
                                <div class="modal-body">
                                    <!--  设置这个div的大小，超出部分显示滚动条 -->
                                    <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                        <section class="content">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="box box-primary">
                                                        <div id="sellerToolbar" class="form-inline">

                                                            <div class="input-group col-md-3"
                                                                 style="margin-top:0px positon:relative">
                                                                <input type="text" name="sellerQueryConditions"
                                                                       id="sellerQueryConditions"
                                                                       class="form-control" style="width:auto;"
                                                                       placeholder="请输入导购姓名或电话">
                                                                <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findSellerByNameOrMobil()">查找</button>
                        </span>
                                                            </div>
                                                        </div>
                                                        <div class="box-body table-reponsive">
                                                            <table id="sellerDataGrid"
                                                                   class="table table-bordered table-hover">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <!-- 顾客选择框 -->
                    <div id="selectCustomer" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document" style="width: 60%">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4>选择顾客</h4>
                                </div>
                                <div class="modal-body">
                                    <!--  设置这个div的大小，超出部分显示滚动条 -->
                                    <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                        <section class="content">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="box box-primary">
                                                        <div id="customerToolbar" class="form-inline">

                                                            <div class="input-group col-md-3"
                                                                 style="margin-top:0px positon:relative">
                                                                <input type="text" name="customerQueryConditions"
                                                                       id="customerQueryConditions"
                                                                       class="form-control" style="width:auto;"
                                                                       placeholder="请输入顾客姓名或电话">
                                                                <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCustomerByNameOrMobil()">查找</button>
                        </span>
                                                            </div>
                                                        </div>
                                                        <div class="box-body table-reponsive">
                                                            <table id="customerDataGrid"
                                                                   class="table table-bordered table-hover">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


            </div>
        </div>
    </div>
</section>

<script>

    $(function () {
        // 初始化城市信息
        findCitylist();
        //初始化门店信息
        findStorelist();
        //获取品牌列表（下拉框）
        findGoodsBrand();
        //物理分类列表（下拉框）
        findGoodsPhysical();
        $("#preDeposit").hide();
        $("#offlinePayments").show();
    })

    //获取城市列表
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
                $("#cityId").append(city);
                $("#cityId").selectpicker('refresh');
            }
        });
    }

    //获取门店列表
    function findStorelist() {
        var store = "";
        $.ajax({
            url: '/rest/stores/findAllStorelist',
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
                $("#storeId").append(store);
                $('#storeId').selectpicker('refresh');
                $('#storeId').selectpicker('render');
            }
        });
    }
    //获取品牌列表（下拉框）
    function findGoodsBrand() {
        var brand = '';
        $.ajax({
            url: '/rest/goodsBrand/page/brandGrid',
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
                    brand += "<option value=" + item.brdId + ">" + item.brandName + "</option>";
                })
                $("#brandCode").append(brand);
                $("#brandCode").selectpicker('refresh');
            }
        });
    }
    //物理分类列表（下拉框）
    function findGoodsPhysical() {
        var physical = '';
        $.ajax({
            url: '/rest/goods/page/physicalClassifyGrid',
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
                    physical += "<option value=" + item.id + ">" + item.physicalClassifyName + "</option>";
                })
                $("#categoryCode").append(physical);
            }
        });
    }

    //根据城市查询门店
    function findStoreByCity(cityId) {

        if (cityId == -1) {
            findStorelist();
            return false;
        }

        initSelect("#storeId", "选择门店");
        var cityId = $("#cityId").val();
        var store = "";
        $.ajax({
            url: '/rest/stores/findStoresListByCityId/' + cityId,
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
                $("#storeId").append(store);
                $('#storeId').selectpicker('refresh');
                $('#storeId').selectpicker('render');
            }
        });
    }

    function openSellerModal() {
        //查询导购列表
        initSeller('/rest/employees/select/seller');
        $("#sellerModalConfirm").unbind('click').click(function () {
        });
        $('#selectSeller').modal('show');
    }

    //条件查询导购
    function findSellerByNameOrMobil() {
        var sellerQueryConditions = $("#sellerQueryConditions").val();
        $("#sellerDataGrid").bootstrapTable('destroy');
        if (null == sellerQueryConditions || "" == sellerQueryConditions) {
            initSeller('/rest/employees/select/seller');
        } else {
            initSeller('/rest/employees/select/seller/' + sellerQueryConditions);
        }
    }
    //按条件查询商品
    function screenGoods() {
        var storeId = $('#storeId').val();

        if (-1 === storeId) {
            $notify.warning("请先选择门店");
            return false;
        }
        var brandCode = $('#brandCode').val();
        var categoryCode = $('#categoryCode').val();
        var companyCode = $('#companyCode').val();
        $("#goodsDataGrid").bootstrapTable('destroy');
        initGoodsGrid('/rest/goods/page/screen/maGoods?storeId=' + storeId + '&brandCode=' + brandCode + '&categoryCode=' + categoryCode + '&companyCode=' + companyCode);
    }
    //初始化商品信息
    function initGoodsGrid(url) {
//        alert("jinru")
        $grid.init($('#goodsDataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'gid',
            title: 'ID',
            align: 'center'
        }, {
            field: 'sku',
            title: 'sku',
            align: 'center',
            formatter: function (value) {
                if (null == value) {
                    return '<span class="scan" >' + '-' + '</span>';
                } else {
                    return '<sapn class="scan" >' + value + '</sapn>';
                }
            }
        }, {
            field: 'skuName',
            title: '名称',
            align: 'center',
            formatter: function (value) {
                if (null == value) {
                    return '<span class="scan" >' + '-' + '</span>';
                } else {
                    return '<sapn class="scan" >' + value + '</sapn>';
                }
            }
        }, {
            field: 'vipPrice',
            title: '会员价',
            align: 'center',
            formatter: function (value) {
                if (null == value) {
                    return '<span class="scan" >' + '-' + '</span>';
                } else {
                    return '<sapn class="scan" >' + value + '</sapn>';
                }
            }
        }, {
            field: 'retailPrice',
            title: '零售价',
            align: 'center',
            formatter: function (value) {
                if (null == value) {
                    return '<span class="scan" >' + '-' + '</span>';
                } else {
                    return '<sapn class="scan" >' + value + '</sapn>';
                }
            }
        }, {
            field: 'categoryName',
            title: '类型',
            align: 'center',
            visible: false
        }, {
            field: 'goodsSpecification',
            title: '规格',
            align: 'center',
            visible: false
        }, {
            field: 'materialsEnable',
            title: '状态',
            align: 'center',
            formatter: function (value) {
                if (null == value) {
                    return '<span class="scan" >' + '-' + '</span>';
                } else {
                    return '<sapn class="scan" >' + value + '</sapn>';
                }
            }
        }

        ]);
    }


    //初始化导购选择框
    function initSeller(url) {
        var cityId = $('#cityId').val();
        var storeId = $('#storeId').val();
        $("#sellerDataGrid").bootstrapTable('destroy');
        $grid.init($('#sellerDataGrid'), $('#sellerToolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'empId',
            title: 'ID',
            align: 'center'
        }, {
            field: 'name',
            title: '导购姓名',
            align: 'center',

            events: {
                'click .scan': function (e, value, row) {
                    showSeller(row.empId, value, row.mobile, row.storeType, row.balance);
                }
            },
            formatter: function (value) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="#' + value + '">' + value + '</a>';
                }
            }
        }, {
            field: 'mobile',
            title: '导购电话',
            align: 'center'
        }, {
            field: 'storeType',
            title: '门店类型',
            align: 'center'
        }, {
            field: 'balance',
            title: '门店预存款',
            align: 'center'
        }
        ]);
    }


    function openCustomerModal() {
        //查询顾客列表
        initCustomer('/rest/customers/select/customer');
        $("#customerModalConfirm").unbind('click').click(function () {
        });
        $('#selectCustomer').modal('show');
    }

    //条件查询顾客
    function findCustomerByNameOrMobil() {
        var customerQueryConditions = $("#customerQueryConditions").val();
        $("#customerDataGrid").bootstrapTable('destroy');
        if (null == customerQueryConditions || "" == customerQueryConditions) {
            initCustomer('/rest/customers/select/customer');
        } else {
            initCustomer('/rest/customers/select/customer/' + customerQueryConditions);
        }
    }

    //初始化顾客选择框
    function initCustomer(url) {
        var cityId = $('#cityId').val();
        var storeId = $('#storeId').val();
        $("#customerDataGrid").bootstrapTable('destroy');
        $grid.init($('#customerDataGrid'), $('#customerToolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'cusId',
            title: 'ID',
            align: 'center'
        }, {
            field: 'name',
            title: '顾客姓名',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    showCustomer(row.cusId, value, row.mobile, row.customerType);
                }
            },
            formatter: function (value) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="#' + value + '">' + value + '</a>';
                }
            }
        }, {
            field: 'mobile',
            title: '顾客电话',
            align: 'center'
        }, {
            field: 'customerType',
            title: '顾客类型',
            align: 'center',
            visible: false
        }
        ]);
    }

    //显示选中的导购
    function showSeller(id, name, phone, storeType, balance) {
        $('#sellerId').text(id);
        $('#sellerName').text(name);
        $('#sellerPhone').text(phone);
        $('#storeType').text(storeType);
        $('#balance').text(balance);
        $('#selectSeller').modal('hide');
    }

    //显示选中的顾客
    function showCustomer(id, name, phone, type) {
        $('#customerId').text(id);
        $('#customerName').text(name);
        $('#customerPhone').text(phone);
        $('#customerType').text(type);
        $('#selectCustomer').modal('hide');
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }
    /**
     * 检查商品详情-查看赠品
     */
    function openGiftsModal() {

        var sellerId = $('#sellerId').text();
        var customerId = $('#customerId').text();
        //检查商品添加详情
        var goodsDetails = new Array();
        cheackGoodsDetail(goodsDetails, 'selectedGoodsTable');
        if (goodsDetails.length == 0) {
            $notify.danger("请选择本品");
            return false;
        }
        if (sellerId == 0) {
            $notify.danger("请选择导购");
            return false;
        }
        if (customerId == 0) {
            $notify.danger("请选择顾客");
            return false;
        }

        var data = {};
        var url = '/rest/goods/page/gifts';
        data["goodsDetails"] = JSON.stringify(goodsDetails);
        data["sellerId"] = sellerId;
        data["customerId"] = customerId;

        $("#giftMessage").html("");
        $http.POST(url, data, function (result) {
            var title = "";
            if (0 === result.code) {
                var promotionsListResponse = result.content;
                var giftListResponse = promotionsListResponse.promotionGiftList;
                if (null != giftListResponse) {
                    for (var i = 0; i < giftListResponse.length; i++) {
                        var isArbitraryChoice = '否';
                        if (giftListResponse[i].isGiftOptionalQty) {
                            isArbitraryChoice = '是';
                        }
                        title += "<div id='giftTitle'>" +
                                "<b style='padding-left: 10px'>促销标题:</b>" +
                                "<span id='actTitle' style='padding-left: 5px'>" + giftListResponse[i].promotionTitle + "</span>" +
                                "<b style='padding-left: 150px'>最大可选数量:</b>" +
                                "<span id='actMaxQty' style='padding-left: 5px'>" + giftListResponse[i].maxChooseNumber + "</span>" +
                                "<b style='padding-left: 150px'>赠品数量是否任选:</b>" +
                                "<span id='IsArbitraryChoice' style='padding-left: 5px'>" + giftListResponse[i].isArbitraryChoice + "</span>" +
                                "</div>" +
                                "<div class='box-body table-responsive no-padding'>" +
                                "<div class='col-xs-12'>" +
                                "<table id='giftTable' class='table table-hover'>" +
                                "<thead id='giftHeader'>" +
                                "<tr>" +
                                "<th>ID</th>" +
                                "<th>商品价格</th>" +
                                "<th>商品名</th>" +
                                "<th>数量</th>" +
                                "</tr>" +
                                "</thead>" +
                                "<tbody id='giftsTable'";


                        if (null != giftListResponse[i].giftList && "" != giftListResponse[i].giftList) {
                            var giftList = giftListResponse[i].giftList
                            for (var j = 0; j < giftList.length; j++) {

                                title += "<tr>" +
                                        "<td><input type='text' id='gid'value=" + giftList[j].goodsId + " style='width:90%;border: none;' readonly /></td>" +
                                        "<td><input id='retailPrice' type='text' value='" + giftList[j].retailPrice + "' style='width:90%;border: none;' readonly></td>" +
                                        "<td><input id='title' type='text' value='" + giftList[j].skuName + "' style='width:90%;border: none;' readonly></td>" +
                                        "<td><input id='qty' type='number' value='0'></td>" +
                                        "<td><input id='promotionId' type='hidden' value='" + giftListResponse[i].promotionId + "'></td>" +
                                        "<td><input id='enjoyTimes' type='hidden' value='" + giftListResponse[i].enjoyTimes + "'></td>" +
                                        "<td><input id='maxChooseNumber' type='hidden' value='" + giftListResponse[i].maxChooseNumber + "'></td>" +
                                        "</tr>"
                            }
                        }
                        title += "</tbody>" +
                                "</table>" +
                                "</div>" +
                                "</div>";

                    }
                }
                $("#giftBigList").val(result.content);
                $("#giftMessage").append(title);
            <#--赠品促销标题-->
            } else {
                $notify.danger(result.message);
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            }
        });
    }

    /**
     * 检查商品详情
     */
    function cheackGoodsDetail(details, tableId) {
        var validateFlag = true;
        //商品sku
        var goodsSkus = new Array();

        var trs = $("#" + tableId).find("tr");

        var goodsSku;
        //数量正则
        var re = /^[0-9]+.?[0-9]*$/;

        trs.each(function (i, n) {
            var id = $(n).find("#gid").val();
            goodsSku = $(n).find("#sku").val();
            if ($.inArray(goodsSku, goodsSkus) >= 0) {
                goodRepeatFlag = true;
                validateFlag = false;
                $notify.warning("亲，【" + goodsSku + "】重复，请删除！");
                return false;
            }
            var num = $(n).find("#qty").val();
            if (num == '' || num == 0 || !re.test(num)) {
                validateFlag = false;
                $notify.warning("亲，本品【" + goodsSku + "】数量不正确");
                return false;
            }
            goodsSkus.push(goodsSku);
            details.push({
                gid: id,
                qty: $(n).find("#qty").val(),
                sku: goodsSku,
                goodsTitile: $(n).find("#title").val(),
                vipPrice: $(n).find("#vipPrice").val(),
                retailPrice: $(n).find("#retailPrice").val()
            });
        });
        return validateFlag;
    }
    //选择商品
    function openGoodsModal(id) {
        var storeId = $('#storeId').val();

        if (-1 == storeId) {
            $notify.warning("请先选择门店");
            return false;
        }
        //初始化商品信息
        initGoodsGrid("/rest/goods/page/grid/" + storeId);

        $("#goodsModalConfirm").unbind('click').click(function () {
            chooseGoods(id);
        });
        $('#goodsModal').modal('show');
    }

    function chooseGoods(tableId) {
        var tableData = $('#goodsDataGrid').bootstrapTable('getSelections');

        if (tableData.length == 0) {
            $notify.warning('请先选择数据');
        } else {
            //alert(tableData);
            var str = "";
            for (var i = 0; i < tableData.length; i++) {
                var item = tableData[i];

                // 排除已选项
                var trs = $("#" + tableId).find("tr");
                var flag = true;
                trs.each(function (i, n) {
                    var id = $(n).find("#gid").val();
                    if (id == item.id) {
                        flag = false;
                        return false;
                    }
                })

                // 此商品未添加过
                if (flag) {
                    str += "<tr>" +
                            "<td><input type='text' id='gid'value=" + item.gid + " style='width:90%;border: none;' readonly /></td>" +
                            "<td><input id='sku' type='text' value='" + item.sku + "' style='width:90%;border: none;' readonly></td>" +
                            "<td><input id='title' type='text' value='" + item.skuName + "' style='width:90%;border: none;' readonly></td>" +
                            "<td><input id='retailPrice' type='number' value='" + item.retailPrice + "' style='width:90%;border: none;' readonly></td>" +
                            "<td><input id='vipPrice' type='number' value='" + item.vipPrice + "' style='width:90%;border: none;' readonly></td>" +
                            "<td><input id='qty' type='number' value='0'></td>" +
                            "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                            "</tr>"
                }

            }
            $("#" + tableId).append(str);

            // 取消所以选中行
            $('#goodsDataGrid').bootstrapTable("uncheckAll");
        }
    }


    //保存买券订单
    function save() {
        var cashMoney = $('#cashMoney').val();
        var posMoney = $('#posMoney').val();
        var posNumber = $('#posNumber').val();
        var otherMoney = $('#otherMoney').val();
        var collectMoneyTime = $('#collectMoneyTime').val();
        var remarks = $('#remarks').val();
        var sellerId = $('#sellerId').text();
        var customerId = $('#customerId').text();
        var totalMoneys = (Number(cashMoney) + Number(posMoney) + Number(otherMoney));
        if ('' == sellerId || null == sellerId) {
            $notify.warning("请选择导购");
            return;
        }
        if ('' == customerId || null == customerId) {
            $notify.warning("请选择顾客");
            return;
        }
        var availableMoney = $('#availableMoney').val();
        var preDepositMoney = $('#preDepositMoney').val();
        var preDepositCollectMoneyTime = $('#preDepositCollectMoneyTime').val();
        var preDepositRemarks = $('#preDepositRemarks').val();

        var totalMoney = 0;

        if (null == preDepositMoney || '' == preDepositMoney) {
            totalMoney = $('#totalMoney').val();
            if (null == collectMoneyTime) {
                $notify.warning("请填写收款时间！");
                return;
            }
            if (posMoney > 0) {
                if (null == posNumber || '' == posNumber) {
                    $notify.warning("请输入POS流水号后六位！");
                    return;
                }
            }
            if (null != posNumber && '' != posNumber) {
                if ('' == posMoney || null == posMoney) {
                    $notify.warning("请输入POS流水号对应的POS金额！");
                    return;
                }
            }
            if (Number(totalMoney) != Number(totalMoneys)) {
                $notify.warning("现金、POS、其他收款总和与收款金额不相等，请检查！");
                return;
            }
        } else {
            totalMoney = Number(preDepositMoney);
            if (Number(availableMoney) < Number(preDepositMoney)) {
                $notify.warning("使用预存款金额大于可使用金额，请检查！");
                return;
            }
            if (null == preDepositCollectMoneyTime || '' == preDepositCollectMoneyTime) {
                $notify.warning("请填写收款时间！");
                return;
            }
        }


        //检查商品添加详情
        var goodsDetails = new Array();
        var a = goodsAndPriceDetail(goodsDetails, 'selectedGoodsTable', totalMoney);
        if (goodsDetails.length == 0) {
            $notify.danger("请选择本品");
            return false;
        }
        if (a == 1) {
            return;
        }


        //获取赠品详情
        var giftDetails = new Array();
        var b = giftDetail(giftDetails, 'giftMessage');
        if (b == 1) {
            return;
        }
        var datas = {};


        datas["cashMoney"] = cashMoney;
        datas["posMoney"] = posMoney;
        datas["posNumber"] = posNumber;
        datas["collectMoneyTime"] = collectMoneyTime;
        datas["otherMoney"] = otherMoney;
        datas["remarks"] = remarks;
        datas["totalMoney"] = totalMoney;
        datas["preDepositMoney"] = preDepositMoney;
        datas["preDepositCollectMoneyTime"] = preDepositCollectMoneyTime;
        datas["preDepositRemarks"] = preDepositRemarks;

        datas["goodsDetails"] = JSON.stringify(goodsDetails);
        datas["giftDetails"] = JSON.stringify(giftDetails);
        datas["sellerId"] = sellerId;
        datas["customerId"] = customerId;

        $.ajax({
            url: '/rest/order/save/productCoupon',
            method: 'POST',
            data: datas,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (result.code === 0) {
                    $notify.danger('保存成功');
                } else {
                    $notify.denger('保存失败');
                }
            }
        });
    }
    //提交保存验证本品与金额
    function goodsAndPriceDetail(details, tableId, totalMoney) {
        var cashMoney = $('#cashMoney').val();
        var posMoney = $('#posMoney').val();
        var posNumber = $('#posNumber').val();
        var otherMoney = $('#otherMoney').val();
        var collectMoneyTime = $('#collectMoneyTime').val();
        var customerType = $('#customerType').text();

        if ('' == customerType) {
            $notify.warning("顾客类型未知，请联系管理员！");
            return 1;
        }
        //零售应付总金额
        var retailTotalMoney = 0;
        //会员应付总金额
        var memberTotalMoney = 0;
        var validateFlag = true;
        //商品sku
        var goodsSkus = new Array();
        var trs = $("#" + tableId).find("tr");
        var goodsSku;
        //数量正则
        var re = /^[0-9]+.?[0-9]*$/;
        trs.each(function (i, n) {
            var id = $(n).find("#gid").val();
            goodsSku = $(n).find("#sku").val();
            if ($.inArray(goodsSku, goodsSkus) >= 0) {
                goodRepeatFlag = true;
                validateFlag = false;
                $notify.warning("亲，【" + goodsSku + "】重复，请删除！");
                return 1;
            }
            var num = $(n).find("#qty").val();
            if (num == '' || num == 0 || !re.test(num)) {
                validateFlag = false;
                $notify.warning("亲，本品【" + goodsSku + "】数量不正确");
                return 1;
            }
            var retailMoney = $(n).find("#retailPrice").val();
            var memberMoney = $(n).find("#vipPrice").val();
            if ('RETAIL' == customerType) {
                retailTotalMoney += (Number(retailMoney) * Number(num));
            } else if ('MEMBER' == customerType) {
                memberTotalMoney += (Number(memberMoney) * Number(num));
            }
            goodsSkus.push(goodsSku);
            details.push({
                gid: id,
                qty: $(n).find("#qty").val(),
                sku: goodsSku,
                goodsTitile: $(n).find("#title").val(),
                vipPrice: $(n).find("#vipPrice").val(),
                retailPrice: $(n).find("#retailPrice").val()
            });
        });
        if ('RETAIL' == customerType) {
            if (Number(retailTotalMoney) != Number(totalMoney)) {
                $notify.warning("应付金额与收款金额不等，请检查！");
                return 1;
            }
        } else if ('MEMBER' == customerType) {
            if (Number(memberTotalMoney) != Number(totalMoney)) {
                $notify.warning("应付金额与收款金额不等，请检查！");
                return 1;
            }
        }
        return 0;
    }


    //提交保存获取赠品信息
    function giftDetail(details, divId) {

        var tables = $("#" + divId).find("tbody");
        var num = 0;
        //数量正则
        var re = /^[0-9]+.?[0-9]*$/;

        tables.each(function (i, n) {
            var maxChooseNumber;
            var trs = $(n).find('tr');
            var giftGoodsList = new Array();
            var totalQty = 0;
            var promotionId;
            var enjoyTimes;

            trs.each(function (i, m) {
                var id = $(m).find("#gid").val();
                var qty = $(m).find("#qty").val();
                promotionId = $(m).find("#promotionId").val();
                enjoyTimes = $(m).find("#enjoyTimes").val();
                maxChooseNumber = $(n).find("#maxChooseNumber").val();
                totalQty += Number(qty);

                giftGoodsList.push({
                    gid: id,
                    qty: qty
                });

            });
            if (Number(totalQty) > Number(maxChooseNumber)) {
                $notify.warning("选择促销商品大于最大可选数量，请检查！");
                num = 1;
                return num;
            }
            details.push({
                promotionId: promotionId,
                discount: null,
                enjoyTimes: enjoyTimes,
                presentInfo: giftGoodsList
            });
        });
        return num;
    }


    //模糊查询商品信息
    function findGoodsByNameOrCode() {
        var storeId = $('#storeId').val();

        if (-1 == storeId) {
            $notify.warning("请先选择门店");
            return false;
        }
        var queryGoodsInfo = $("#queryGoodsInfo").val();
        $("#goodsDataGrid").bootstrapTable('destroy');
        if (null == queryGoodsInfo || "" == queryGoodsInfo) {
            //初始化商品信息
            initGoodsGrid("/rest/goods/page/grid/" + storeId);
        } else {
            initGoodsGrid('/rest/goods/page/query/goodsInfo?queryGoodsInfo=' + queryGoodsInfo + '&storeId=' + storeId);
        }
    }


    //删除商品节点
    function del_goods_comb(obj) {
        var deleteGoodsId = $(obj).parent().parent().find("#gid").val();
        $(obj).parent().parent().remove();
    }

    //失去焦点验证收款信息
    function priceBlur(id) {
        var price = document.getElementById(id).value;

        if ('posMoney' == id || 'otherMoney' == id || 'totalMoney' == id || 'preDepositMoney' == id) {
            if (price < 0) {
                $notify.warning("此处金额不能为负数，请正确填写！");
                document.getElementById(id).value = 0.00;
                return false;
            }
        }
        if (price.toString().indexOf(".") > 0 && Number(price.toString().split(".")[1].length) > 2) {
            $notify.warning("请输入正确金额，小数点后只能保留2位小数！");
            document.getElementById(id).value = 0.00;
            return false;
        }
    }

    //选择支付方式
    function selectPaymnet(id) {
        var storeType = $('#storeType').text();
        var balance = $('#balance').text();
        var preDeposit = 0;
        if (null != balance && '' != balance) {
            preDeposit = balance;
        }
        if ('offlinePayments' == id) {
            $("#preDeposit :input").each(function () {
                $(this).val("");
            });
            $("#preDeposit").hide();
            $("#offlinePayments").show();
        } else if ('preDeposit' == id) {
            if ('ZY' != storeType) {
                $notify.warning("此支付方式只有直营门店可用使用！");
                return;
            }
            $("#offlinePayments :input").each(function () {
                $(this).val("");
            });
            document.getElementById("availableMoney").value = preDeposit;
            $("#offlinePayments").hide();
            $("#preDeposit").show();
        }
    }
</script>
</body>
