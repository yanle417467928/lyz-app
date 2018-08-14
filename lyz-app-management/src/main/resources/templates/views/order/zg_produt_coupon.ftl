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
<#--<script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.js"></script>-->
<#--<script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>-->

    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script type="text/javascript" src="/javascript/order/zg_produt_coupon.js"></script>
</head>
<body>
<section class="content-header">
    <h1>购买专供产品券</h1>
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
                        <select name="cityId" id="cityId" class="form-control select"
                                onchange="findStoreByCity(this.value)">
                            <option value="-1">选择城市</option>
                        </select>
                    </div>

                    <div class="col-xs-12 col-md-2" style="margin-left: 180px;">
                        <label for="title">
                            门店
                        </label>
                        <select name="store" id="storeId" class="selectpicker"
                                onchange="storeChangeRefresh()" data-live-search="true">
                            <option value="-1">选择门店</option>
                        </select>
                    </div>
                </div>
                <br>
                <div class="row">
                <#--<div class="col-xs-12 col-md-2">-->
                    <div class="col-md-3 invoice-col">
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
                                <spanp id="storeCode" class="span" style="display:none"></spanp>
                                <spanp id="balance" class="span" style="display:none"></spanp>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-3 invoice-col">
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
                    <div class="col-md-6 invoice-col">

                        <ul style="line-height: 30px; position: relative; left:-30px;">

                            <li style="list-style: none">
                                <b>产品券使用说明:</b>
                                <spanp id="sellerId" class="span"></spanp>
                            </li>
                            1) 产品券购买：依照下订单当下之产品价格计算。
                        <br>
                            2) 产品券提货：依照产品券上之关联产品进行出货。
                        <br>
                            3) 产品券提货时效：需于六个月内（含）出货完毕，若逾期，则此订单自动取消，按产品券购买之价格退至会员预存款。
                        <br>
                            4) 专供产品券：需要指定专供会员归属导购，才可购买。
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
                                        <button id="selectGoods" type="button" class="btn btn-primary btn-xs"
                                                onclick="openGoodsModal('selectedGoodsTable')">
                                            选择商品
                                        </button>

                                        <button id="updateGoods" type="button" style="display: none"
                                                class="btn btn-primary btn-xs"
                                                onclick="openGoodsModal('selectedGoodsTable')">
                                            变更商品
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
                                                <th style="width: 7%">ID</th>
                                                <th style="width: 18%">sku</th>
                                                <th style="width: 35%">商品名</th>
                                                <th style="width: 10%">零售价</th>
                                                <th style="width: 10%">会员价</th>
                                                <th style="width: 10%">数量</th>
                                                <th style="width: 10%">操作</th>
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
                                <!-- 立减金额 -->
                                <div class="row" id="subAmount_div">

                                </div>


                            </div>
                        </div>
                    </div>

                    <div class="row" id="fullAmount_div">
                        <div class="col-xs-12 col-md-12">
                            <div class="box box-success">
                                <ul style="line-height: 40px;">
                                    <li style="list-style: none">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                style="width:200px;height:50px;"
                                                onclick="openGoPay()">
                                            去支付
                                        </button>
                                        <input id="goPayType" type="hidden" value="1"/>
                                    </li>
                                </ul>
                                <div id="goPay">
                                    <div class="col-xs-12">
                                        <h3 class="box-titl">
                                            收款
                                        </h3>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="col-xs-12" style="margin-left: 50px;">
                                            <h4>买券账单明细</h4>
                                        </div>

                                        <div id="billingDetails">
                                            <div class="col-xs-12">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">商品金额
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <span id="totalGoodsPrice">0.00</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">会员折扣
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <span id="vipDiscount">0.00</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">促销折扣
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <span id="promotionsDiscount">0.00</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">应付金额
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <span id="amountsPayable">0.00</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div id="salesNum" style="margin-top: 50px">
                                            <div class="col-xs-12">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3"
                                                           style="padding-right: 0px;text-align:right;display: none"
                                                           id="salesNumTitle">销售单号
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <input type="text" name="salesNumber" id="salesNumber"
                                                               class="form-control" placeholder="销售纸质单号"
                                                               style="display: none"
                                                               onblur="verificationSalesNumber()"
                                                               \>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="col-xs-6">
                                        <div class="col-xs-4" style="margin-left: 115px;">
                                            <select name="selectPaymnet" id="selectPaymnet" class="form-control select"
                                                    onchange="selectPaymnets(this.value)">
                                                <option value="-1">请选择支付方式</option>
                                                <option value="offlinePayments">线下支付</option>
                                                <option value="preDeposit">预存款支付</option>
                                            </select>
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
                                                        <input type="text" name="posNumber" id="posNumber"
                                                               class="form-control" placeholder="POS交易流水号后六位"
                                                               onKeyUp="if(this.value.length>6){this.value=this.value.substr(0,6)};this.value=this.value.replace(/[^\d]/g,'');"
                                                               \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12" style="display: none">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">微信
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <input type="number" name="weMoney" id="weMoney"
                                                               class="form-control" placeholder="微信金额"
                                                               onblur="priceBlur('weMoney')" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12" style="display: none">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">支付宝
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <input type="number" name="aliyMoney" id="aliyMoney"
                                                               class="form-control" placeholder="支付宝金额"
                                                               onblur="priceBlur('aliyMoney')" \>
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

                                                        <input name="collectMoneyTime" type="text"
                                                               class="form-control datepicker" id="collectMoneyTime"
                                                               placeholder="请选择收款时间">
                                                    <#--<input name="collectMoneyTime" type="text" class="form-control"-->
                                                    <#--id="collectMoneyTime"-->
                                                    <#--readonly placeholder="请选择收款时间">-->
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12">
                                                <div class="col-xs-11" style="margin-top: 10px">
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">收款金额
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <input type="number" name="totalMoney" id="totalMoney"
                                                               class="form-control" readonly
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
                                                    <label class="col-xs-3" style="padding-right: 0px;text-align:right">备注
                                                        &ensp;</label>
                                                    <div class=" col-xs-8" style="padding-left: 0px">
                                                        <input type="text" name="preDepositRemarks"
                                                               id="preDepositRemarks"
                                                               class="form-control"
                                                               \>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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
                                                                    class="selectpicker" data-width="120px" style="width:auto;"
                                                                    onchange="screenGoods()">
                                                                <option value="-1">选择品牌</option>
                                                            </select>
                                                            <select name="categoryCode" id="categoryCode"
                                                                    class="selectpicker" data-width="120px" style="width:auto;"
                                                                    onchange="screenGoods()">
                                                                <option value="-1">选择分类</option>
                                                            </select>
                                                            <select name="companyCode" id="companyCode"
                                                                    class="selectpicker" data-width="120px" style="width:auto;"
                                                                    onchange="screenGoods()">
                                                                <option value="-1">选择公司</option>
                                                                <option value="LYZ">乐易装</option>
                                                                <option value="HR">华润</option>
                                                                <option value="YR">莹润</option>
                                                            </select>
                                                            <select name="productType" id="productType"
                                                                    class="selectpicker" data-width="120px" style="width:auto;"
                                                                   >
                                                                <option value="zg">专供</option>

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

</body>
