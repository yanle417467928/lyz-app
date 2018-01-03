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
    <style>
        b {
            line-height: 30px;
        }

        .span,.fa {
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
                                <ul >
                                    <span class="logisticCreateTime" id="logisticCreateTime">2017-11-11 11:00:00</span>
                                    <i class="fa fa-circle" style="color: red"></i>
                                    <span class="logisticStatus" id="logisticStatus" style="padding-left: 100px">已装车</span>
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
        <div class="box">
            <div class="row invoice-info">
                <div class="col-sm-6 invoice-col">
                    <div class="box-header">
                        <h3 class="box-title">基本信息</h3>
                    </div>
                    <b>门店:</b>
                    <spanp class="span">${maOrderDetail.storeName!""}</spanp>
                    <br>
                    <b>下单人id:</b>
                    <spanp class="span">${maOrderDetail.creatorId!""}</spanp>
                    <br>
                    <b>配送方式:</b>
                    <spanp class="span"><#if maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>送货上门
                    <#elseif maOrderDetail.deliveryType = 'SELF_TAKE'>门店自提</#if></spanp>
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
                            <b></b>
                            <span style="float: right;padding-top: 4px;color: #1c94c4" ;
                                  onclick="$page.information.show($(dfasd).val())">点击查看物流详情</span>
                        </div>
                    </div>
                </#if>
                <#if maOrderDetail.deliveryType = 'SELF_TAKE'>
                    <b>发货时间:</b>
                    <spanp class="span">${maOrderDetail.shipTime!""}</spanp>
                    <br>
                </#if>
                    <b>订单备注:</b>
                    <spanp class="span">${maOrderDetail.orderRemarks!""}</spanp>
                </div>
                <div class="col-sm-3 invoice-col">
                    <div class="box-header">
                        <h3 class="box-title"></h3>
                    </div>
                    <b>导购:</b>
                    <spanp class="span"> ${maOrderDetail.sellerName!""}</spanp>
                    <br>
                    <b>客户id:</b>
                    <spanp class="span">${maOrderDetail.customerId!""}</spanp>
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
                    <div class="box-header">
                        <h3 class="box-title"></h3>
                    </div>
                    <b>下单人:</b>
                    <spanp class="span">${maOrderDetail.creatorName!""}</spanp>
                    <br>
                    <b>客户姓名:</b>
                    <spanp class="span"> ${maOrderDetail.customerName!""}</spanp>
                    <br>
                    <b></b>
                    <br>
                <#if maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>
                    <b>是否主家收货:</b>
                    <spanp class="span"><#if maOrderDetail.isOwnerReceiving>是<#else>否</#if></spanp>
                    <br>
                </#if>
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
                                    <td>${(paymentDetail.payTime?string("yyyy-MM-dd hh:mm:ss"))!""}</td>
                                    <td>${paymentDetail.paymentType!""}</td>
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

        <#--<div class="col-xs-6">-->
        <#--</div>-->

        <#--<div class="col-xs-6">-->
        <#--<div class="box">-->
        <#--<div class="table-responsive">-->
        <#--<table class="table">-->
        <#--<tr>-->
        <#--<th>应付金额:</th>-->
        <#--<td>$265.24</td>-->
        <#--</tr>-->
        <#--</table>-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->

            <script>
                var $page = {
                    information: {
                        show: function (orderNumber) {
                            if (null === $global.timer) {
                                $global.timer = setTimeout($loading.show, 2000);
                                $.ajax({
                                    url: '/rest/order/delivery/' + orderNumber,
                                    method: 'GET',
                                    error: function () {
                                        clearTimeout($global.timer);
                                        $loading.close();
                                        $global.timer = null;
                                        $notify.danger('网络异常，请稍后重试或联系管理员');
                                    },
                                    success: function (result) {
                                        clearTimeout($global.timer);
                                        $loading.close();

                                        if (0 === result.code) {
                                            $global.timer = null;
                                            var data = result.content;
                                            $('#menuTitle').html(" 物流详情");

                                            if (null === data.warehouse) {
                                                data.warehouse = '-';
                                            }
                                            $('#warehouse').html(data.warehouse);

                                            if (null === data.deliveryClerkName) {
                                                data.deliveryClerkName = '-';
                                            }
                                            $('#deliveryClerkName').html(data.deliveryClerkName);

                                            if (null === data.shipmentNumber) {
                                                data.shipmentNumber = '-';
                                            }
                                            $('#shipmentNumber').html(data.shipmentNumber);

                                            if (null === data.deliveryTime) {
                                                data.deliveryTime = '-';
                                            }
                                            $('#deliveryTime').html(data.deliveryTime);

                                            if (null != data.orderDeliveryInfoDetailsList){
                                                var time ='';
                                                var status='';
                                                var b ='';
                                                if(data.orderDeliveryInfoDetailsList.length!=0){
                                                    for(var i=0;i<data.orderDeliveryInfoDetailsList.length;i++){
                                                        time = formatDateTime(data.orderDeliveryInfoDetailsList[i].createTime);
                                                        if ("INITIAL" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '等待物流接收';
                                                        }else if ("RECEIVED" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '已接收';
                                                        }else if ("ALREADY_POSITIONED" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '已定位';
                                                        }else if ("PICKING_GOODS" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '已拣货';
                                                        }else if ("LOADING" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '已装车';
                                                        }else if ("SEALED_CAR" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '已封车';
                                                        }else if ("SENDING" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '未投妥';
                                                        }else if ("CONFIRM_ARRIVAL" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '已签收';
                                                        }else if ("REJECT" == data.orderDeliveryInfoDetailsList[i].logisticStatus){
                                                            status = '拒签';
                                                        }

                                                      var a ='<ul><span class="logisticCreateTime" id="logisticCreateTime">'+time+'</span><i class="fa fa-circle" style="color: red"></i> <span class="logisticStatus" id="logisticStatus" style="padding-left: 100px">'+status+'</span></ul><div style="padding-left: 140px"> <ul class="timeline" style="margin-bottom: 0px"> <i class="fa fa-circle" style="color: white"></i> </ul></div>';
                                                /*        $('#logisticCreateTime').html();
                                                        $('#logisticStatus').html();*/
                                                   b+=a;
                                                    }
                                                }
                                                $('#test').html(b);
                                            } else {
                                                date.orderDeliveryInfoDetailsList = '';
                                            }

                                            $('#information').modal();
                                        } else {
                                            $notify.danger(result.message);
                                        }
                                    }
                                })
                            }
                        },
                        close: function () {
                            $('#information').modal('hide');
                        }
                    }
                }

                //转换时间
                var formatDateTime = function (date) {
                    var dt = new Date(date);
                    var y = dt.getFullYear();
                    var m = dt.getMonth() + 1;
                    m = m < 10 ? ('0' + m) : m;
                    var d = dt.getDate();
                    d = d < 10 ? ('0' + d) : d;
                    var h = dt.getHours();
                    h = h < 10 ? ('0' + h) : h;
                    var minute = dt.getMinutes();
                    minute = minute < 10 ? ('0' + minute) : minute;
                    var second = dt.getSeconds();
                    second = second < 10 ? ('0' + second) : second;
                    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
                };
            </script>
    </section>
</div>
</body>
</html>
