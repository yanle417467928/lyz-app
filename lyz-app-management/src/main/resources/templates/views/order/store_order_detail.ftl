<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>


    <script type="text/javascript" src="/javascript/order/store_order_detail.js"></script>
    <style>
        b {
            line-height: 30px;
        }

        .span, .fa {
            margin-left: 10px;
        }

        .col-sm-6 {
            padding-left: 30px;
        }

        .th1, .td1 {
            text-align: center;
        }
    </style>
</head>
<body onload="window.print();">

<div id="information" class="modal fade" tabindex="-1" role="dialog" id="modalShow">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="user-block">
                    <span class="username" style="margin-left: 0px;">
                        <a id="menuTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="cusDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>配送仓库:</b> <a class="pull-right" id="warehouse"></a>
                        </li>
                        <li class="list-group-item">
                            <b>配送员:</b> <a class="pull-right" id="deliveryClerkName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>出货单号:</b> <a class="pull-right" id="shipmentNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>预约配送时间:</b> <a class="pull-right" id="deliveryTime"></a>
                        </li>

                        <li style="list-style: none">
                            <div class="timeline-item" id="test">
                                <ul>
                                    <span class="logisticCreateTime" id="logisticCreateTime">2017-11-11 11:00:00</span>
                                    <i class="fa fa-circle" style="color: red"></i>
                                    <span class="logisticStatus" id="logisticStatus"
                                          style="padding-left: 100px">已装车</span>
                                </ul>
                                <div style="padding-left: 141px">
                                    <ul class="timeline" style="margin-bottom: 0px">
                                        <i class="fa fa-circle" style="color: white"></i>
                                    </ul>
                                </div>
                            </div>
                        <#--<div class="timeline-item">-->
                        <#--<ul>-->
                        <#--<span>2017-11-11 11:00:00</span>-->
                        <#--<i class="fa fa-circle" style="color: red"></i>-->
                        <#--</ul>-->
                        <#--<div style="padding-left: 141px">-->
                        <#--<ul class="timeline" style="margin-bottom: 0px">-->
                        <#--<i class="fa fa-circle" style="color: white"></i>-->
                        <#--</ul>-->
                        <#--</div>-->
                        <#--</div>-->
                        </li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>


<div class="wrapper">
    <section class="invoice">
        <div class="row">
            <div class="col-xs-12">
                <h2 class="page-header">
                    门店订单详情
                </h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <b style="margin-left: 15px">订单号:</b>
                <spanp>${maOrderDetail.orderNumber!""}</spanp>
                <b style="margin-left: 50px">创建时间:</b>
                <spanp>${maOrderDetail.createTime?string("yyyy-MM-dd HH:mm:ss")!""}</spanp>
                <b style="margin-left: 70px">订单状态:</b>
                <spanp><#if maOrderDetail.orderStatus = 'UNPAID'>待付款
                <#elseif maOrderDetail.orderStatus = 'PENDING_SHIPMENT'>
                    待发货<#elseif maOrderDetail.orderStatus = 'PENDING_RECEIVE'>
                    待收货<#elseif maOrderDetail.orderStatus = 'FINISHED'>
                    已完成<#elseif maOrderDetail.orderStatus = 'CLOSED'>
                    已结案<#elseif maOrderDetail.orderStatus = 'CANCELED'>
                    已取消<#elseif maOrderDetail.orderStatus = 'REJECTED'>
                    拒签<#elseif maOrderDetail.orderStatus = 'CANCELING'>
                    取消中</#if>
                </spanp>
                <br>
            </div>
        </div>
        <div class="box">
            <div class="row invoice-info">
                <div class="box-header">
                    <h3 class="box-title" style="padding-left: 20px;">基本信息</h3>
                </div>
                <div class="col-sm-6 invoice-col">
                    <b>门店:</b>
                    <spanp class="span">${maOrderDetail.storeName!""}</spanp>
                    <br>
                    <b>下单人id:</b>
                    <spanp class="span"><#if maOrderDetail?? && maOrderDetail.creatorId??>${maOrderDetail.creatorId?c}</#if></spanp>
                    <br>
                    <b>配送方式:</b>
                    <spanp class="span">
                    <#if maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>送货上门
                    <#elseif maOrderDetail.deliveryType = 'SELF_TAKE'>门店自提
                    </#if>
                    </spanp>
                    <br>
                <#if maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>
                    <b>收货人:</b>
                    <spanp class="span">${maOrderDetail.receiverName!""}</spanp>
                    <br>
                    <div class="row">
                        <div class="col-xs-12">
                            <b>送货地址:</b>
                            <spanp class="span">${maOrderDetail.shippingAddress!""}</spanp>
                            <input type="hidden" id="dfasd" value="${maOrderDetail.orderNumber!""}"/>
                        </div>
                    </div>
                </#if>
                <#if maOrderDetail.deliveryType = 'SELF_TAKE'>
                    <b>发货时间:</b>
                    <spanp class="span"><#if maOrderDetail?? && maOrderDetail.shipTime??>${maOrderDetail.shipTime?string("yyyy-MM-dd HH:mm:ss")}</#if></spanp>
                    <br>
                </#if>
                    <b>订单备注:</b>
                    <spanp class="span">${maOrderDetail.orderRemarks!""}</spanp>
                </div>
                <div class="col-sm-3 invoice-col">
                    <b>导购:</b>
                    <spanp class="span"> ${maOrderDetail.sellerName!""}</spanp>
                    <br>
                    <b>客户id:</b>
                    <spanp class="span"><#if maOrderDetail?? && maOrderDetail.customerId??>${maOrderDetail.customerId?c}</#if></spanp>
                    <br>
                <#if maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>
                    <b>代收金额:</b>
                    <spanp class="span">${maOrderDetail.collectionAmount!'0.00'}</spanp>
                    <br>
                </#if>
                <#if maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>
                    <b>手机号:</b>
                    <spanp class="span">${maOrderDetail.receiverPhone!""}</spanp>
                    <br>
                    <b></b>
                    <span style="padding-top: 4px;color: #1c94c4" ;
                          onclick="$page.information.show($(dfasd).val())">点击查看物流详情</span>
                    <br>
                </#if>
                <#if maOrderDetail.deliveryType = 'SELF_TAKE'>
                    <b>发货门店:</b>
                    <spanp class="span">${maOrderDetail.shipStore!""}</spanp>
                    <br>
                </#if>
                </div>
                <!-- /.col -->
                <div class="col-sm-3 invoice-col">
                    <b>下单人:</b>
                    <spanp class="span">${maOrderDetail.creatorName!""}</spanp>
                    <br>
                    <b>客户姓名:</b>
                    <spanp class="span"> ${maOrderDetail.customerName!""}</spanp>
                    <br>
                    <b></b>
                    <br>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 table-responsive">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">订单商品</h3>
                    </div>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>商品编码</th>
                            <th>商品名称</th>
                            <th>数量</th>
                            <th>单价</th>
                            <th></th>
                            <th>小计</th>
                            <th>实付款</th>
                            <th>商品类型</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if maOrderDetail.maOrderGoodsDetailResponseList?? && maOrderDetail.maOrderGoodsDetailResponseList?size gt 0 >
                            <#list maOrderDetail.maOrderGoodsDetailResponseList as goods>
                            <tr>
                                <td>${goods.sku!""}</td>
                                <td>${goods.goodsName!""}</td>
                                <td>${goods.qty!""}</td>
                                <td>${goods.unitPrice!'0.00'}</td>
                                <td></td>
                                <td>${goods.subTotalPrice!'0.00'}</td>
                                <td>${goods.realPayment!'0.00'}</td>
                                <td>${goods.goodsType!""}</td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-7">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">支付明细</h3>
                    </div>
                    <div class="box-body no-padding">
                        <table class="table table-condensed">
                            <tr>
                                <th>支付日期</th>
                                <th>支付方式</th>
                                <th style="width: 80px">支付金额</th>
                            </tr>
                            <tbody>
                            <#if paymentDetailList?? && paymentDetailList?size gt 0 >
                                <#list paymentDetailList as paymentDetail>
                                <tr>
                                    <td>${(paymentDetail.payTime?string("yyyy-MM-dd HH:mm:ss"))!""}</td>
                                    <td>${paymentDetail.paymentType!""}
                                        <#--<#if paymentDetail.paymentType = 'CUS_PREPAY'>-->
                                        <#--顾客预存款<#elseif paymentDetail.paymentType = 'ST_PREPAY'>-->
                                        <#--门店预存款<#elseif paymentDetail.paymentType = 'ALIPAY'>-->
                                        <#--支付宝<#elseif paymentDetail.paymentType = 'WE_CHAT'>-->
                                        <#--微信<#elseif paymentDetail.paymentType = 'UNION_PAY'>-->
                                        <#--银联<#elseif paymentDetail.paymentType = 'POS'>-->
                                        <#--POS<#elseif paymentDetail.paymentType = 'CASH'>-->
                                        <#--现金<#elseif paymentDetail.paymentType = 'OTHER'>门店其它</#if>-->
                                    </td>
                                    <td>${paymentDetail.amount!'0.00'}</td>
                                </tr>
                                </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                    <!-- /.box-body -->
                </div>
            </div>
            <!-- /.col -->
            <div class="col-xs-5">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">账单明细</h3>
                    </div>
                    <div class="table-responsive">
                        <table class="table">
                            <tr>
                                <th style="width:50%" class="th1">商品总额:</th>
                                <td class="td1">¥${orderBillingDetail.totalGoodsPrice!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">运费</th>
                                <td class="td1">¥${orderBillingDetail.freight!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">优惠券折扣:</th>
                                <td class="td1">- ¥${orderBillingDetail.cashCouponDiscount!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">产品券折扣:</th>
                                <td class="td1">- ¥${orderBillingDetail.productCouponDiscount!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">促销折扣:</th>
                                <td class="td1">- ¥${orderBillingDetail.promotionDiscount!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">会员折扣:</th>
                                <td class="td1">- ¥${orderBillingDetail.memberDiscount!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">乐币折扣:</th>
                                <td class="td1">- ¥${orderBillingDetail.lebiCashDiscount!'0.00'}</td>
                            </tr>
                            <tr>
                                <th class="th1">应付金额:</th>
                                <td style="color: red;font-weight:bold" class="td1">
                                    ¥${orderBillingDetail.amountPayable!'0.00'}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-10"></div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-danger footer-btn btn-cancel" style="width:80%;">
                    <i class="fa fa-close"></i>返回
                </button>
            </div>
        </div>
    </section>
</div>
</body>
</html>
