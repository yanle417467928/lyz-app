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

<div class="wrapper">
    <section class="invoice">
        <div class="row">
            <div class="col-xs-12">
                <h2 class="page-header">
                    拍照下单
                </h2>
            </div>
        </div>
        <div class="box">
            <div class="row invoice-info">
                <div class="box-header">
                    <h3 class="box-title" style="padding-left: 20px;">基本信息</h3>
                </div>
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-5 invoice-col">
                        <b>门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;店:</b>
                        <br>
                        <b>下单人身份类型:</b>
                        <br>
                        <b>拍&nbsp;照&nbsp;下&nbsp;单&nbsp;单&nbsp;号:</b>
                        <br>
                    </div>
                    <div class="col-sm-7 invoice-col">
                        <b></b>
                        <spanp class="span">${photoOrderVO.storeName!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span">${photoOrderVO.identityType!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.photoOrderNo!""}</spanp>
                        <br>
                    </div>
                </div>
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-4 invoice-col">
                        <b>下单人姓名:</b>
                        <br>
                        <b>联系人姓名:</b>
                        <br>
                        <b>下&nbsp;单&nbsp;&nbsp;时&nbsp;&nbsp;间:</b>
                        <br>
                    </div>
                    <div class="col-sm-8 invoice-col">
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.username!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.contactName!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.createTime!""}</spanp>
                        <br>
                    </div>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-5 invoice-col">
                        <b>下单人手机号码:</b>
                        <br>
                        <b>联&nbsp;&nbsp;&nbsp;系&nbsp;&nbsp;人&nbsp;&nbsp;电&nbsp;&nbsp;话:</b>
                        <br>
                        <b>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态:</b>
                        <br>
                    </div>
                    <div class="col-sm-7 invoice-col">
                        <b></b>
                        <spanp class="span">${photoOrderVO.userMobile!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.contactPhone!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.status!""}</spanp>
                        <br>
                    </div>
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
                            <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
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
                                    <td><#if paymentDetail.paymentType = 'CUS_PREPAY'>
                                        顾客预存款<#elseif paymentDetail.paymentType = 'ST_PREPAY'>
                                        门店预存款<#elseif paymentDetail.paymentType = 'ALIPAY'>
                                        支付宝<#elseif paymentDetail.paymentType = 'WE_CHAT'>
                                        微信<#elseif paymentDetail.paymentType = 'UNION_PAY'>
                                        银联<#elseif paymentDetail.paymentType = 'POS'>
                                        POS<#elseif paymentDetail.paymentType = 'CASH'>
                                        现金<#elseif paymentDetail.paymentType = 'OTHER'>门店其它</#if></td>
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
            <#--<div class="col-xs-5">-->
                <#--<div class="box">-->
                    <#--<div class="box-header">-->
                        <#--<h3 class="box-title">账单明细</h3>-->
                    <#--</div>-->
                    <#--<div class="table-responsive">-->
                        <#--<table class="table">-->
                            <#--<tr>-->
                                <#--<th style="width:50%" class="th1">商品总额:</th>-->
                                <#--<td class="td1">¥${orderBillingDetail.totalGoodsPrice!'0.00'}</td>-->
                            <#--</tr>-->
                            <#--<tr>-->
                                <#--<th class="th1">运费</th>-->
                                <#--<td class="td1">¥${orderBillingDetail.freight!'0.00'}</td>-->
                            <#--</tr>-->
                            <#--<tr>-->
                                <#--<th class="th1">优惠券折扣:</th>-->
                                <#--<td class="td1">- ¥${orderBillingDetail.cashCouponDiscount!'0.00'}</td>-->
                            <#--</tr>-->
                            <#--<tr>-->
                                <#--<th class="th1">促销折扣:</th>-->
                                <#--<td class="td1">- ¥${orderBillingDetail.promotionDiscount!'0.00'}</td>-->
                            <#--</tr>-->
                            <#--<tr>-->
                                <#--<th class="th1">会员折扣:</th>-->
                                <#--<td class="td1">- ¥${orderBillingDetail.memberDiscount!'0.00'}</td>-->
                            <#--</tr>-->
                            <#--<tr>-->
                                <#--<th class="th1">乐币折扣:</th>-->
                                <#--<td class="td1">- ¥${orderBillingDetail.lebiCashDiscount!'0.00'}</td>-->
                            <#--</tr>-->
                            <#--<tr>-->
                                <#--<th class="th1">应付金额:</th>-->
                                <#--<td style="color: red;font-weight:bold" class="td1">-->
                                    <#--¥${orderBillingDetail.amountPayable!'0.00'}</td>-->
                            <#--</tr>-->
                        <#--</table>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->

        </div>

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

                                            if (null != data.orderDeliveryInfoDetailsList) {
                                                var time = '';
                                                var status = '';
                                                var b = '';
                                                if (data.orderDeliveryInfoDetailsList.length != 0) {
                                                    for (var i = 0; i < data.orderDeliveryInfoDetailsList.length; i++) {
                                                        time = formatDateTime(data.orderDeliveryInfoDetailsList[i].createTime);
                                                        if ("INITIAL" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '等待物流接收';
                                                        } else if ("RECEIVED" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '已接收';
                                                        } else if ("ALREADY_POSITIONED" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '已定位';
                                                        } else if ("PICKING_GOODS" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '已拣货';
                                                        } else if ("LOADING" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '已装车';
                                                        } else if ("SEALED_CAR" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '已封车';
                                                        } else if ("SENDING" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '未投妥';
                                                        } else if ("CONFIRM_ARRIVAL" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '已签收';
                                                        } else if ("REJECT" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                                            status = '拒签';
                                                        }

                                                        var a = '<ul><span class="logisticCreateTime" id="logisticCreateTime">' + time + '</span><i class="fa fa-circle" style="color: red"></i> <span class="logisticStatus" id="logisticStatus" style="padding-left: 100px">' + status + '</span></ul><div style="padding-left: 140px"> <ul class="timeline" style="margin-bottom: 0px"> <i class="fa fa-circle" style="color: white"></i> </ul></div>';
                                                        /*        $('#logisticCreateTime').html();
                                                                $('#logisticStatus').html();*/
                                                        b += a;
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
