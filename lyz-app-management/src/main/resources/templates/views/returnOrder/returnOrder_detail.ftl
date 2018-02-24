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
                    退货单详情
                </h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12" align="center">
                <div class="col-sm-3" style="margin-left: 1%">
                    <b>退单号:</b>
                    <spanp>${maReturnOrderDetailVO.returnNumber!""}</spanp>
                </div>
                <div class="col-sm-4">
                    <b>退货单状态:</b>
                    <spanp style="color: red;font-weight:bold">
                    <#if maReturnOrderDetailVO.returnStatus == 'PENDING_PICK_UP'>已提交
                    <#elseif maReturnOrderDetailVO.returnStatus == 'CANCELING'>取消中
                    <#elseif maReturnOrderDetailVO.returnStatus == 'CANCELED'>已取消
                    <#elseif maReturnOrderDetailVO.returnStatus == 'PENDING_REFUND'>待退款
                    <#elseif maReturnOrderDetailVO.returnStatus == 'FINISHED'>已完成
                    </#if>
                    </spanp>
                </div>
                <div class="col-sm-3" style="margin-left: 1%">
                    <b>退单配送类型:</b>
                    <spanp style="color: red;font-weight:bold">
                    <#if returnOrderType??>
                        <#if returnOrderType == 'RETURN_STORE'>退货到店
                        <#elseif returnOrderType == 'HOUSE_PICK'>工地取货
                        </#if>
                    <#else>未知
                    </#if>
                    </spanp>
                </div>
                <input type="hidden" id="returnNumber" value="${maReturnOrderDetailVO.returnNumber}">
                <input type="hidden" id="orderStatus" value="${maReturnOrderDetailVO.returnStatus}">
                <input type="hidden" id="returnOrderType" value="${returnOrderType!''}">
            </div>
        </div>
        <HR>
        <div class="row" style="margin-top: 2%">
            <div class="col-md-12" style="margin-left:20px">
                <div class="col-md-2" style="text-align:center;margin-top: 10px">
                    <span>退货单基本信息</span>
                </div>
                <div class="col-md-10">
                    <dl>
                        <dd>
                            <table class="table table-bordered table-hover" style="width: 98%">
                                <tbody>
                                <tr>
                                    <th style="width: 20%">建单人姓名</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maReturnOrderDetailVO.creatorName!""}
                                            </span>
                                        </div>
                                    </td>
                                    <th style="width: 20%">建单人身份</th>
                                    <td>
                                        <div>
                                            <span>
                                            <#if  maReturnOrderDetailVO.creatorIdentityType== 'SELLER'>导购
                                            <#elseif maReturnOrderDetailVO.creatorIdentityType == 'CUSTOMER'>顾客
                                            <#elseif maReturnOrderDetailVO.creatorIdentityType == 'DECORATE_MANAGER'>
                                                装饰公司经理
                                            </#if>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>顾客ID</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maReturnOrderDetailVO.customerId!""}
                                            </span>
                                        </div>
                                    </td>
                                    <th>顾客姓名</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maReturnOrderDetailVO.customerName!""}
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>门店名称</th>
                                    <td>
                                        <div>
                                        <span>
                                        ${maReturnOrderDetailVO.storeName!""}
                                        </span>
                                        </div>
                                    </td>
                                    <th>退货时间</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maReturnOrderDetailVO.returnTime!""}
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>原订单号</th>
                                    <td>
                                        <div>
                                            <span>
                                            ${maReturnOrderDetailVO.orderNumber!""}
                                            </span>
                                        </div>
                                    </td>
                                    <th>原订单类型</th>
                                    <td>
                                        <div>
                                            <span>
                                            <#if  maReturnOrderDetailVO.orderType== 'SHIPMENT'>出货
                                            <#elseif maReturnOrderDetailVO.orderType == 'COUPON'>买券
                                            </#if>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        退货原因
                                    </th>
                                    <td colspan='3'>
                                        <div>
                                            <span>
                                            ${maReturnOrderDetailVO.reasonInfo!""}
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
                    <span>退款明细</span>
                </div>
                <div class="col-md-10">
                    <dl>
                        <dd>
                            <table class="table table-bordered table-hover" style="width: 98%">
                                <tr align="center" style="font-weight:bold;">
                                    <td style="width: 20%">退款时间</td>
                                    <td>退款方式</td>
                                    <td>退款金额</td>
                                </tr>
                            <#if maReturnOrderDetailVO?? && (maReturnOrderDetailVO.retrunBillingList)??&&(maReturnOrderDetailVO.retrunBillingList)?size gt 0>
                                <#list (maReturnOrderDetailVO.retrunBillingList)as retrunBilling>
                                    <tr align="center">
                                        <td>${retrunBilling.createTime?string("yyyy-MM-dd HH:mm:ss")!""}</td>
                                        <td>
                                            <#if (retrunBilling.returnPayType)??&& retrunBilling.returnPayType=='CUS_PREPAY'>
                                                顾客预存款
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='ST_PREPAY'>
                                                门店预存款
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='ALIPAY'>
                                                支付宝
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='WE_CHAT'>
                                                微信
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='UNION_PAY'>
                                                银联
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='POS'>
                                                POS
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='CASH'>
                                                现金
                                            <#elseif (retrunBilling.returnPayType)??&&retrunBilling.returnPayType=='OTHER'>
                                                门店其它（对公转账）
                                            </#if>
                                        </td>
                                        <td>¥${retrunBilling.returnMoney!'0.00'}</td>
                                    </tr>
                                </#list>
                            </#if>
                            </table>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12" style="margin-left:20px">
                <div class="col-md-2" style="text-align:center;margin-top: 10px">
                    <span>退券明细</span>
                </div>
                <div class="col-md-10">
                    <dl>
                        <dd>
                            <table class="table table-bordered table-hover" style="width: 98%">
                                <tbody>
                                <tr align="center" style="font-weight:bold;">
                                    <td style="width: 20%">券ID</td>
                                    <td>商品编码</td>
                                    <td>退券数量</td>
                                    <td>是否已退</td>
                                </tr>
                                <#if maReturnOrderDetailVO?? && (maReturnOrderDetailVO.returnOrderProductCouponList)??&&(maReturnOrderDetailVO.returnOrderProductCouponList)?size gt 0>
                                    <#list (maReturnOrderDetailVO.returnOrderProductCouponList) as returnOrderProductCoupon>
                                    <tr>
                                        <td align="center">${returnOrderProductCoupon.pcid!''}</td>
                                        <td align="center">${returnOrderProductCoupon.sku!''}</td>
                                        <td align="center">${returnOrderProductCoupon.returnQty!''}</td>
                                        <td align="center">
                                            <#if (returnOrderProductCoupon.isReturn)??&& returnOrderProductCoupon.isReturn?c=='true'>
                                                是
                                            <#elseif (returnOrderProductCoupon.isReturn)??&&returnOrderProductCoupon.isReturn?c=='false'>
                                                否
                                            </#if>
                                        </td>
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
                    <span>退货商品列表</span>
                </div>
                <div class="col-md-10">
                    <dl>
                        <dd>
                            <table class="table table-bordered table-hover" style="width: 98%">
                                <thead>
                                <tr align="center" style="font-weight:bold;">
                                    <td style="width: 20%">商品编码</td>
                                    <td>商品名称</td>
                                    <td>退货数量</td>
                                    <td>退货单价</td>
                                    <td>退货金额</td>
                                </tr>
                                </thead>
                                <tbody>
                                <#if maReturnOrderDetailVO?? && maReturnOrderDetailVO.goodsList?size gt 0 >
                                    <#list maReturnOrderDetailVO.goodsList as goods>
                                    <tr>
                                        <td align="center">${goods.sku!""}</td>
                                        <td align="center">${goods.skuName!""}</td>
                                        <td align="center">${goods.returnQty!'0'}</td>
                                        <td align="center">¥${goods.returnPrice!'0.00'}</td>
                                        <td align="center">¥${goods.totalPrice!"0.00"}</td>
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
            <div class="col-xs-12 col-md-8"></div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-primary footer-btn" id="receiving"
                        onclick="confirmReceiving()">
                    <i class="fa fa-check"></i>确认收货
                </button>
            </div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-danger footer-btn btn-cancel">
                    <i class="fa fa-close"></i>返回
                </button>
            </div>
        </div>

        <div class="modal fade" id="receivingStatus">
            <div class="modal-dialog" style="width: 30%;">
                <div class="modal-content message_align">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-label="Close"><span
                                aria-hidden="true">×</span></button>
                        <h4 class="modal-title">提示信息</h4>
                    </div>
                    <div class="modal-body">
                        <p>您确认收货吗？</p>
                    </div>
                    <div class="modal-footer">
                        <a onclick=" return OrderReturnReceiving()" class="btn btn-success"
                           data-dismiss="modal">是</a>
                        <button type="button" class="btn btn-danger" data-dismiss="modal">
                            否
                        </button>
                    </div>
                </div>
            </div>
        </div>
</section>

<script>
    $(function () {
        Initialization();
        $('.btn-cancel').on('click', function () {
            history.go(-1);
        });
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
    });

    function confirmReceiving() {
        $('#receivingStatus').modal();
    }


    function OrderReturnReceiving() {
        var returnNumber = $("#returnNumber").val();
        if (isBlank(returnNumber)) {
            return false;
        }
        $.ajax({
            url: '/rest/returnOrder/returnOrderReceive',
            method: 'PUT',
            data: {"returnNumber": returnNumber},
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
                    $notify.danger('退货失败，请稍后重试或联系管理员');
                }
            }
        });
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
        var orderStatus = $('#orderStatus').val();
        var returnOrderType = $('#returnOrderType').val();
        if (orderStatus == 'PENDING_PICK_UP' && returnOrderType == 'RETURN_STORE') {
            $('#receiving').attr("disabled", false);
        } else {
            $('#receiving').attr("disabled", true);
        }
    }
</script>
</body>
