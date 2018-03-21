<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
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

        .cover {
            z-index: 98;
            width: 100%;
            height: 100%;
            display: none;
            float: left;
            position: absolute;
            top: 0px;
        }
    </style>
</head>
<body onload="window.print();">
<div class="wrapper">
    <section class="invoice">
        <div class="row">
            <div class="col-xs-12">
                <h2 class="page-header">
                    装饰公司信用金账单详情
                </h2>
            </div>
        </div>
        <div class="box">
            <div class="row invoice-info">
                <div class="box-header">
                    <h3 class="box-title" style="padding-left: 20px;">基本信息</h3>
                </div>
                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>账单单号:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.creditBillingNo!""}</spanp>
                    </div>
                </div>
                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>记账周期:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.cycleTime!""}</spanp>
                    </div>
                </div>

                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>装饰公司:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.storeName!""}</spanp>
                    </div>
                </div>
                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>还款状态:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span"><#if creditBillingVO?? && creditBillingVO.isPayOff>已还清<#else>未还清</#if></spanp>
                    </div>
                </div>

                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>账单名称:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.billName!""}</spanp>
                    </div>
                </div>
                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>创建时间:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.createTime!""}</spanp>
                    </div>
                </div>

                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>账单总额:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.billAmount!"0.00"}</spanp>
                    </div>
                </div>
                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>还款时间:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.updateTime!"无"}</spanp>
                    </div>
                </div>

                <div class="col-xs-12 col-md-6">
                    <div class="col-xs-12 col-md-3">
                        <b>已还金额:</b>
                    </div>
                    <div class="col-xs-12 col-md-9">
                        <b></b>
                        <spanp class="span">${creditBillingVO.repaidAmount!"0.00"}</spanp>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 table-responsive">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">订单信息</h3>
                    </div>
                    <#--<input type="hidden" name="photoId" id="photoId" <#if photoOrderVO?? && photoOrderVO.id??>
                           value="${(photoOrderVO.id)?c}"
                    <#else>
                           value="0"
                    </#if>/>-->
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th width="13%">装饰公司</th>
                            <th width="18%">订/退单编号</th>
                            <th width="13%">下单人</th>
                            <th width="15%">出/退货时间</th>
                            <th width="34%">送货地址</th>
                            <th width="7%">总金额</th>
                        </tr>
                        </thead>
                        <tbody id="tbody">
                            <#if creditBillingDetailsVOS??>
                                <#list creditBillingDetailsVOS as creditBillingDetails>
                                    <tr>
                                        <td>${creditBillingVO.storeName!""}</td>
                                        <td>${creditBillingDetails.orderNumber!""}</td>
                                        <td>${creditBillingDetails.creatorName!""}</td>
                                        <td>${creditBillingDetails.createTime!""}</td>
                                        <td>${creditBillingDetails.deliveryAddress!""}</td>
                                        <td>${creditBillingDetails.creditMoney!""}</td>
                                    </tr>
                                </#list>
                            </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-md-8"></div>
            <div class="col-xs-12 col-md-2">
                <button type="button" onclick="openRepaymentModal()" class="btn btn-primary footer-btn"
                        <#if ((creditBillingVO.billAmount!0) - (creditBillingVO.repaidAmount!0)) == 0>disabled="disabled"</#if> >
                    <i class="fa fa-check"></i> 还款
                </button>
            </div>
            <div class="col-xs-12 col-md-2">
                <button type="button" class="btn btn-danger footer-btn btn-cancel">
                    <i class="fa fa-close"></i> 返回
                </button>
            </div>
        </div>
        <div id="repaymentModal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document" style="width: 30%">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>新建账单</h4>
                    </div>
                    <form id="repaymentCreditBilling">
                        <input type="hidden" name="id" id="id" <#if creditBillingVO?? && creditBillingVO.id??>
                           value="${(creditBillingVO.id)?c}"
                        <#else>
                               value="0"
                        </#if>/>
                        <div class="modal-body">
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-4" >
                                        <label for="billName" style="margin-top: 8%">账单名称</label>
                                    </div><div class="col-md-7">
                                    <input type="text" class="form-control" id="billName" value="${creditBillingVO.billName!''}" readonly>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="storeName" style="margin-top: 8%">装饰公司</label>
                                    </div><div class="col-md-7">
                                    <input type="text" class="form-control" id="storeName" value="${creditBillingVO.storeName!''}" readonly>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="allCreditMoney" style="margin-top: 8%">账单金额</label>
                                    </div><div class="col-md-7">
                                    <input type="text" class="form-control" id="billAmount" value="${creditBillingVO.billAmount!'0.00'}" readonly>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="allCreditMoney" style="margin-top: 8%">已还金额</label>
                                    </div><div class="col-md-7">
                                    <input type="text" class="form-control" id="repaidAmount" value="${creditBillingVO.repaidAmount!'0.00'}" readonly>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="name" style="margin-top: 8%">还款金额</label>
                                    </div><div class="col-md-7">
                                    <input type="text" class="form-control datepicker" id="amount" name="amount">
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="name" style="margin-top: 8%">还款方式</label>
                                    </div><div class="col-md-7">
                                    <select class="form-control selectpicker" name="paymentType" id="paymentType">
                                        <if paymentTypes??>
                                        <#list paymentTypes as paymentType>
                                            <option value="${paymentType.value}">${paymentType.description}</option>
                                        </#list>
                                        </if>
                                    </select>
                                </div>
                                </div>
                            </div>
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

        <script>

            $(function () {
                $('.btn-cancel').on('click', function () {
                    history.go(-1);
                });
                $('#repaymentCreditBilling').bootstrapValidator({
                    framework: 'bootstrap',
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    verbose: false,
                    fields: {
                        amount: {
                            message: '还款金额校验失败',
                            validators: {
                                notEmpty: {
                                    message: '还款金额不能为空'
                                },
                                regexp: {
                                    regexp: /^[-+]?\d*[.]?\d{0,2}$/,
                                    message: '请输入正确的金额'
                                },
                                stringLength: {
                                    min: 1,
                                    max: 10,
                                    message: '金额的长度必须在1~10位之间'
                                }
                            }
                        }
                    }
                }).on('success.form.bv', function (e) {
                    var amount = $('#amount').val();
                    var billAmount = $('#billAmount').val();
                    var repaidAmount = $('#repaidAmount').val();
                    if(undefined == amount || null == amount || '' == amount || amount == 0) {
                        $notify.warning('请输入还款金额！');
                    }
                    if (billAmount > 0) {
                        if (amount < 0) {
                            $notify.warning('账单金额为正数，还款金额请输入正的金额！');
                            return;
                        }
                        if (parseFloat(amount) + parseFloat(repaidAmount) > parseFloat(billAmount)) {
                            $notify.warning('还款金额不能大于未还金额！');
                            return;
                        }

                    }
                    if (billAmount < 0) {
                        if (amount > 0) {
                            $notify.warning('账单金额为负数，还款金额请输入负的金额！');
                            return;
                        }
                        if (parseFloat(amount) + parseFloat(repaidAmount) < parseFloat(billAmount)) {
                            $notify.warning('还款金额不能小于未还金额！');
                            return;
                        }
                    }

                    e.preventDefault();
                    var $form = $(e.target);
                    var origin = $form.serializeArray();
                    var data = {};

                    $.each(origin, function () {
                        data[this.name] = this.value;
                    });
                    if (null === $global.timer) {
                        $global.timer = setTimeout($loading.show, 2000);

                        var url = '/rest/decorationCompany/creditBilling/repayment/creditBilling';
                        $.ajax({
                            url: url,
                            method: 'POST',
                            data: data,
                            error: function () {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger('网络异常，请稍后重试或联系管理员');
                                $('#repaymentCreditBilling').bootstrapValidator('disableSubmitButtons', false);
                            },
                            success: function (result) {
                                if (0 === result.code) {
                                    window.location.href = document.referrer;
                                } else {
                                    clearTimeout($global.timer);
                                    $loading.close();
                                    $global.timer = null;
                                    $notify.danger(result.message);
                                    $('#repaymentCreditBilling').bootstrapValidator('disableSubmitButtons', false);
                                }
                            }
                        });
                    }
                });
            });

            function openRepaymentModal() {
                $('#repaymentModal').modal('show');
            }
        </script>
    </section>
</div>
</body>
</html>
