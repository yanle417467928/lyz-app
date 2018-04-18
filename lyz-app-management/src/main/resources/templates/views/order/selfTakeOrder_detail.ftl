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
                <div class="col-sm-4">
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
            </div>
        </div>
        <HR>
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
                                    <th style="width: 20%">门店名称</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maOrderDetail.storeName!""}
                                            </span>
                                        </div>
                                    </td>
                                    <th style="width: 20%">导购</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maOrderDetail.sellerName!""}
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
                                    <th>客户ID</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maOrderDetail.customerId!""}
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
                                            </span>
                                    </div>
                                        门店自提</#if>
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
                                    <th>发货时间</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${shippingTime!""}
                                            </span>
                                        </div>
                                    </td>
                                    <th>客户姓名</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maOrderDetail.customerName!""}
                                            </span>
                                        </div>

                                    </td>
                                </tr>
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
            <div class="col-xs-12 col-md-6"></div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-primary footer-btn" id="ship"
                        onclick="confirmShip()">
                    <i class="fa fa-check"></i>确认发货
                </button>
            </div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-success footer-btn" id="receivable"
                        onclick="confirmReceivables()">
                    <i class="fa fa-check"></i>确认收款
                </button>
            </div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-danger footer-btn btn-cancel">
                    <i class="fa fa-close"></i>返回
                </button>
            </div>
        </div>
        <div class="modal fade" id="confirmShipForGuide">
            <div class="modal-dialog" style="width: 30%;">
                <div class="modal-content message_align">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-label="Close"><span
                                aria-hidden="true">×</span></button>
                        <h4 class="modal-title">提示信息</h4>
                    </div>
                    <div class="modal-body">
                        <p>您确认要发货吗？</p>
                    </div>
                    <div class="modal-footer">
                        <a onclick=" return OrderShippingSubmit()" class="btn btn-danger"
                           data-dismiss="modal">确定</a>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="confirmShipForCustomer">
            <div class="modal-dialog" style="width: 30%;">
                <div class="modal-content message_align">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-label="Close"><span
                                aria-hidden="true">×</span></button>
                        <h4 class="modal-title">提示信息</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-inline">
                            <label style="padding-right: 0px">提货码:&nbsp</label>
                            <input type="text" name="code" id="code" class="form-control "
                                   style="width:auto;"
                                   onKeyUp="judgmentVerification(this.value)"
                                   placeholder="请输提货码">
                            <input type="hidden" name="secondCode" id="secondCode" class="form-control">
                            <span id="msg"></span>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button onclick="return OrderShippingSubmit()"
                                class="btn btn-success "
                                data-dismiss="modal"
                                id="confirmShip">确定
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="confirmReceivables">
            <div class="modal-dialog" style="width: 30%;">
                <div class="modal-content message_align">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-label="Close"><span
                                aria-hidden="true">×</span></button>
                        <h4 class="modal-title">确认收款</h4>
                    </div>
                    <form id="confirmReceivablesFrom">
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="name">总金额(元)</label>
                                <input type="text" class="form-control" id="count"
                                       readonly
                                       value="${orderBillingDetail.arrearage!'0.00'}">
                                <input type="hidden" class="form-control"
                                       id="orderNumberInfrom"
                                       name="orderNumber"
                                       readonly
                                       value="${maOrderDetail.orderNumber!""}">
                            </div>
                            <div class="form-group">
                                <label for="name">现金(元)</label>
                                <input type="text" class="form-control" id="cashAmount"
                                       name="cashAmount">
                            </div>
                            <div class="form-group">
                                <label for="name">POS金额(元)</label>
                                <input type="text" class="form-control" id="posAmount"
                                       name="posAmount">
                            </div>
                            <div class="form-group">
                                <label for="name">其他金额(元)</label>
                                <input type="text" class="form-control" id="otherAmount"
                                       name="otherAmount">
                            </div>
                            <div class="form-group">
                                <label for="name">POS交易流水号</label>
                                <input type="text" class="form-control" id="serialNumber"
                                       name="serialNumber">
                            </div>
                            <div class="form-group">
                                <label for="name">实际收款日期</label>
                                <input type="text" class="form-control datepicker" id="date"
                                       name="date">
                            </div>
                            <span id="message" style="color: red;"></span>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-success "
                                    class="btn btn-default" id="confirmSubmit">确定
                            </button>
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">取消
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
</section>

<script>
    $(function () {
        reload();
        Initialization();
        $('.btn-cancel').on('click', function () {
            history.go(-1);
        });
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
        var form = $('#confirmReceivablesFrom');
        form.bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                cashAmount: {
                    message: '现金校验失败',
                    validators: {
                        regexp: {
                            regexp: /^[+-]?\d+(\.\d+)?$/,
                            message: '现金称只能输入正或负数'
                        },
                        stringLength: {
                            min: 1,
                            max: 10,
                            message: '长度必须在1~10位之间'
                        }
                    }
                },
                posAmount: {
                    message: 'pos金额校验失败',
                    validators: {
                        regexp: {
                            regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                            message: 'pos金额称只能输入正数'
                        },
                        stringLength: {
                            min: 1,
                            max: 10,
                            message: '长度必须在1~10位之间'
                        }
                    }
                },
                otherAmount: {
                    message: '其他金额校验失败',
                    validators: {
                        regexp: {
                            regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                            message: '其他金额只能输入正数'
                        },
                        stringLength: {
                            min: 1,
                            max: 10,
                            message: '长度必须在1~10位之间'
                        }
                    }
                },
                serialNumber: {
                    message: '流水号校验失败',
                    validators: {
                        regexp: {
                            regexp: /^\d{6}$/,
                            message: '流水号为6位数字'
                        }
                    }
                },
                date: {
                    message: '日期校验失败',
                    validators: {
                        notEmpty: {
                            message: '日期不允许为空!'
                        },
                        date: {
                            format: 'YYYY-MM-DD',
                            message: '日期格式不正确'
                        },
                    }
                }
            }
        });
        $("#confirmSubmit").click(function () {
            var isPayUp = $('#isPayUp').val();
            if ('true' == isPayUp) {
                return false;
            }
            var bv = form.data('bootstrapValidator');
            bv.validate();
            if (bv.isValid()) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/order/orderReceivables',
                        async: false,
                        type: 'POST',
                        data: form.serialize(),
                        success: function (result) {
                            if (result.code == 10100) {
                                $('#confirmReceivables').modal();
                                $("#message").html(result.message);
                                $loading.close();
                                $global.timer = null;
                            } else if (result.code == -1) {
                                $("#message").html(result.message);
                                $loading.close();
                                $global.timer = null;
                            } else if (result.code == 0) {
                                $("#message").html('');
                                window.location.reload();
                            }
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#confirmSubmit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    })
                }
            }
        });
    });


    function confirmShip() {
        var creatorIdentityType = $("#type").val();
        $('#code').val('');
        $('#msg').html('')
        $('#secondCode').val('');
        if ('SELLER' == creatorIdentityType) {
            $('#confirmShipForGuide').modal();
        } else if ('SELLER' != creatorIdentityType) {
            $('#confirmShipForCustomer').modal();
        }
    }

    function confirmReceivables() {
        $('#confirmReceivables').modal();
    }


    function OrderShippingSubmit() {
        var orderNumber = $("#orderNumber").val();
        var code = $('#secondCode').val();
        var status = $('#status').val();
        if ('PENDING_RECEIVE' != status) {
            return false;
        }
        if (isBlank(orderNumber)) {
            return false;
        }
        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);
            $.ajax({
                url: '/rest/order/orderShipping',
                method: 'GET',
                data: {'orderNumber': orderNumber, 'code': code},
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                },
                success: function (result) {
                    if (0 === result.code) {
                        window.location.reload();
                    } else {
                        $loading.close();
                        $global.timer = null;
                        $notify.danger('发货失败，请稍后重试或联系管理员');
                    }
                }
            });
        }
    }

    function judgmentVerification(data) {
        var orderNumber = $("#orderNumber").val();
        var code = $("#code").val();
        if (null == code || '' == code || 4 != code.length) {
            $('#msg').html('<font color="red">验证失败</font>')
            return false;
        }
        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);
            $.ajax({
                url: '/rest/order/judgmentVerification',
                method: 'GET',
                data: {'orderNumber': orderNumber, 'code': data},
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                },
                success: function (result) {
                    if (0 === result.code) {
                        $loading.close();
                        $global.timer = null;
                        $('#secondCode').val(data);
                        $('#confirmShip').attr("disabled", false);
                        $('#msg').html('<font color="green">验证成功</font>')
                    } else {
                        $loading.close();
                        $global.timer = null;
                        $('#msg').html('<font color="red">验证失败</font>')
                    }
                }
            });
        }
    }


    var isBlank = function (date) {
        if (null == date || '' == date)
            return true;
        else {
            return false;
        }
    };


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


    function Initialization() {
        var status = $('#status').val();
        var isPayUp = $('#isPayUp').val();
        if (status != 'PENDING_RECEIVE') {
            $('#ship').attr("disabled", true);
        }
        if (isPayUp == 'true') {
            $('#receivable').attr("disabled", true);
        } else if (status == 'CANCELED') {
            $('#receivable').attr("disabled", true);
        }
        $('#confirmShip').attr("disabled", true);
    }

    function reload() {
        $('#date').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            language: 'zh-CN'
        }).on('hide', function (e) {
            $('#confirmReceivablesFrom').data('bootstrapValidator')
                    .updateStatus('date', 'NOT_VALIDATED', null)
                    .validateField('date');
        })
    }


</script>
</body>
