<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <style type="text/css">
        .table-bordered th,
        .table-bordered td {
            border: 1px solid #BEBEBE !important;
        }
    </style>
</head>
<body onload="window.print();">
<section class="content">
    <div class="box">
        <div class="row">
            <div class="col-md-12">
                <h2 class="page-header" style="margin-left:15px;border: 0px">
                    门店自提订单详情
                </h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="col-sm-3">
                    <b>订单号:</b>
                    <spanp>${maOrderDetail.orderNumber!""}</spanp>
                </div>
                <div class="col-sm-3">
                    <b>创建时间:</b>
                    <spanp><#if maOrderDetail??&&maOrderDetail.createTime??>${maOrderDetail.createTime?string("yyyy-MM-dd HH:mm:ss")}</#if></spanp>
                </div>
                <div class="col-sm-2">
                    <b>订单状态:</b>
                    <spanp style="color: red;font-weight:bold">
                    <#if maOrderDetail.orderStatus == 'UNPAID'>待付款
                    <#elseif maOrderDetail.orderStatus == 'PENDING_SHIPMENT'>待发货
                    <#elseif maOrderDetail.orderStatus == 'PENDING_RECEIVE'>待收货
                    <#elseif maOrderDetail.orderStatus == 'FINISHED'>已完成
                    <#elseif maOrderDetail.orderStatus == 'CLOSED'>已结案
                    <#elseif maOrderDetail.orderStatus == 'CANCELED'>已取消
                    <#elseif maOrderDetail.orderStatus == 'REJECTED'>拒签
                    <#elseif maOrderDetail.orderStatus == 'CANCELING'>取消中
                    </#if>
                    </spanp>
                </div>
                <div class="col-sm-2">
                    <b>收款状态:</b>
                    <spanp style="color: red;font-weight:bold">
                    <#if isPayUp = false>未付清
                    <#else>已付清
                    </#if>
                    </spanp>
                    <input type="hidden" id="status" readonly value="${maOrderDetail.orderStatus!""}">
                    <input type="hidden" id="isPayUp" readonly value="<#if isPayUp??>${isPayUp?c}</#if>">
                    <input type="hidden" id="type" readonly value="${maOrderDetail.creatorIdentityType!""}">
                    <input type="hidden" id="orderNumber" readonly value="${maOrderDetail.orderNumber!""}">
                </div>
                <div class="col-sm-2">
                    <b>是否代付:</b>
                    <spanp style="color: red;font-weight:bold">
                        <spanp style="color: red;font-weight:bold">
                        <#if detailFitOrderVO.isPayOver = false>否
                        <#elseif detailFitOrderVO.isPayOver = true>是
                        </#if>
                        </spanp>
                </div>
            </div>
        </div>
        <HR>
        <div class="row" style="margin-top: 2%">
            <div class="col-md-12" style="margin-left:20px">
                <div class="col-md-2" style="text-align:center;margin-bottom: 20px">
                    <span>物流信息</span>
                </div>
                <div class="col-md-10">
                    <div class="package-status">
                    <span style="margin-bottom: 20px;color: #1c94c4" ;
                          onclick="$page.information.show($(orderNumber).val())"><a href="#">点击查看物流详情</a></span>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top: 2%">
                <div class="col-md-12" style="margin-left:20px">
                    <div class="col-md-2" style="text-align:center;margin-top: 10px">
                        <span>订单基本信息</span>
                    </div>
                    <div class="col-md-10">
                        <dl>
                            <dd>
                                <table class="table table-bordered table-hover" style="width: 98%">
                                    <tbody>
                                    <tr>
                                        <th style="width: 20%">装饰公司名称</th>
                                        <td>
                                            <div>
                                            <span>
                                            ${maOrderDetail.storeName!""}
                                            </span>
                                            </div>
                                        </td>
                                        <th style="width: 20%">代付人</th>
                                        <td>
                                            <div>
                                            <span>
                                            ${detailFitOrderVO.payhelper!""}
                                            </span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>下单人ID</th>
                                        <td>
                                            <div>
                                            <span>
                                            ${maOrderDetail.creatorId!""}
                                            </span>
                                            </div>
                                        </td>
                                        <th>下单人</th>
                                        <td>
                                            <div>
                                            <span>
                                            ${maOrderDetail.creatorName!""}
                                            </span>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr>
                                        <th>配送方式</th>
                                        <td>
                                            <div>
                                    <span>
                                    <#if maOrderDetail.deliveryType = 'SELF_TAKE'>
                                        门店自提
                                    <#elseif maOrderDetail.deliveryType = 'HOUSE_DELIVERY'>
                                        送货上门
                                    </#if>
                                            </span>
                                            </div>
                                        </td>
                                        <th>发货时间</th>
                                        <td>
                                            <div>
                                            <span>
                                            ${shippingTime!""}
                                            </span>
                                            </div>
                                        </td>
                                    </tr>
                                    <#--  <tr>
                                          <th>客户姓名</th>
                                          <td>
                                              <div>
                                                  <span>
                                                  ${maOrderDetail.customerName!""}
                                                  </span>
                                              </div>
                                          </td>
                                          <th>客户ID</th>
                                          <td>
                                              <div>
                                                  <span>
                                                  ${maOrderDetail.customerId!""}
                                                  </span>
                                              </div>
                                          </td>
                                      </tr>-->
                                    <tr>
                                        <th>
                                            订单备注
                                        </th>
                                        <td colspan='3'>
                                            <div>
                                            <span>
                                            ${maOrderDetail.orderRemarks!""}
                                            </span>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12" style="margin-left:20px">
                    <div class="col-md-2" style="text-align:center;margin-top: 10px">
                        <span>账单明细</span>
                    </div>
                    <div class="col-md-10">
                        <dl>
                            <dd>
                                <table class="table table-bordered table-hover" style="width: 98%">
                                    <tr>
                                        <th style="width:20%">商品总额</th>
                                        <td>&nbsp¥${orderBillingDetail.totalGoodsPrice!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>运费</th>
                                        <td>&nbsp¥${orderBillingDetail.freight!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>优惠券折扣</th>
                                        <td>-¥${orderBillingDetail.cashCouponDiscount!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>产品劵折扣</th>
                                        <td>-¥${orderBillingDetail.productCouponDiscount!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>促销折扣</th>
                                        <td>-¥${orderBillingDetail.promotionDiscount!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>会员折扣</th>
                                        <td>-¥${orderBillingDetail.memberDiscount!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>乐币折扣</th>
                                        <td>-¥${orderBillingDetail.lebiCashDiscount!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>订单金额小计</th>
                                        <td style="color: red;font-weight:bold">
                                            &nbsp¥${orderBillingDetail.orderAmountSubtotal!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>欠款金额</th>
                                        <td style="color: red;font-weight:bold">
                                            &nbsp¥${orderBillingDetail.arrearage!'0.00'}</td>
                                    </tr>
                                    <tr>
                                        <th>代付金额</th>
                                        <td style="color: red;font-weight:bold">
                                            &nbsp¥${detailFitOrderVO.payhelperAmount!'0.00'}</td>
                                    </tr>
                                </table>
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12" style="margin-left:20px">
                    <div class="col-md-2" style="text-align:center;margin-top: 10px">
                        <span>支付明细</span>
                    </div>
                    <div class="col-md-10">
                        <dl>
                            <dd>
                                <table class="table table-bordered table-hover" style="width: 98%">
                                    <tbody>
                                    <tr align="center" style="font-weight:bold;">
                                        <td style="width: 20%">支付日期</td>
                                        <td>支付方式</td>
                                        <td>支付金额</td>
                                    </tr>
                                    <#if paymentDetailList?? && paymentDetailList?size gt 0 >
                                        <#list paymentDetailList as paymentDetail>
                                        <tr>
                                            <td align="center"><#if paymentDetail??&&paymentDetail.payTime??>${(paymentDetail.payTime?string("yyyy-MM-dd HH:mm:ss"))!""}</#if></td>
                                            <td>${paymentDetail.paymentType!"未知"}</td>
                                            <td align="center">${paymentDetail.amount!'0.00'}</td>
                                        </tr>
                                        </#list>
                                    </#if>
                                    </tbody>
                                </table>
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12" style="margin-left:20px">
                    <div class="col-md-2" style="text-align:center;margin-top: 10px">
                        <span>订单商品</span>
                    </div>
                    <div class="col-md-10">
                        <dl>
                            <dd>
                                <table class="table table-bordered table-hover" style="width: 98%">
                                    <thead>
                                    <tr align="center" style="font-weight:bold;">
                                        <td style="width: 20%">商品编码</td>
                                        <td>商品名称</td>
                                        <td>数量</td>
                                        <td>单价</td>
                                        <td>小计</td>
                                        <td>实付款</td>
                                        <td>商品类型</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#if maOrderDetail.maOrderGoodsDetailResponseList?? && maOrderDetail.maOrderGoodsDetailResponseList?size gt 0 >
                                        <#list maOrderDetail.maOrderGoodsDetailResponseList as goods>
                                        <tr>
                                            <td align="center">${goods.sku!""}</td>
                                            <td align="center">${goods.goodsName!""}</td>
                                            <td align="center">${goods.qty!""}</td>
                                            <td align="center">${goods.unitPrice!'0.00'}</td>
                                            <td align="center">${goods.subTotalPrice!'0.00'}</td>
                                            <td align="center">${goods.realPayment!'0.00'}</td>
                                            <td align="center">${goods.goodsType!""}</td>
                                        </tr>
                                        </#list>
                                    </#if>
                                    </tbody>
                                </table>
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-10"></div>
                <div class="col-xs-12 col-md-2">
                    <button type="button" class="btn btn-danger footer-btn btn-cancel">
                        <i class="fa fa-close"></i>返回
                    </button>
                </div>
            </div>

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
                                            <div style="padding-left: 141px">
                                                <ul class="timeline" style="margin-bottom: 0px">
                                                    <i class="fa fa-circle" style="color: white"></i>
                                                </ul>
                                            </div>
                                        </div>
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
</section>

<script>
    $(function () {
        $('.btn-cancel').on('click', function () {
            history.go(-1);
        });
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
    })
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


</script>
</body>
