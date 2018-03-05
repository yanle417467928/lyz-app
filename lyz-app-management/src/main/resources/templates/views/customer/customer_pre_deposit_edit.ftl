<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">

    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script>
        $(function () {
            $(".select2").select2();
            $('.btn-cancel').on('click', function () {
                history.go(-1);
            });

            if (!$global.validateMobile()) {
                $('.select').selectpicker();
            }

            $(function () {
                $('[data-toggle="tooltip"]').popover();
            });

            $('.datepicker').datepicker({
                format: 'yyyy-mm-dd',
                language: 'zh-CN',
                autoclose: true
            });

            $('.switch').bootstrapSwitch();

            $('#cus_pre_deposit_edit').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {
                    changeMoney: {
                        message: '变更金额校验失败',
                        validators: {
                            notEmpty: {
                                message: '变更金额不能为空'
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
                    },
                    merchantOrderNumber: {
                        message: '商户订单号校验失败',
                        validators: {
                            stringLength: {
                                min: 0,
                                max: 6,
                                message: '请输入6位的商户订单号'
                            }
                        }
                    },
                    remarks: {
                        message: '备注校验失败',
                        validators: {
                            stringLength: {
                                min: 0,
                                max: 50,
                                message: '备注的长度必须在0~50字之间'
                            }
                        }
                    }
                }
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                var $form = $(e.target);
                var origin = $form.serializeArray();
                var data = {};
                var formData = new FormData($("#cus_pre_deposit_edit")[0]);
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    var url = '/rest/customer/preDeposit/edit';
                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#cus_pre_deposit_edit').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#cus_pre_deposit_edit').bootstrapValidator('disableSubmitButtons', false);
                            }
                        }
                    });
                }
            });
        });
    </script>
    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
</head>
<body>

<section class="content-header">
    <h1>预存款变更</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="cus_pre_deposit_edit">
                    <input type="hidden" name="cusId" id="cusId" <#if customerPreDepositVO?? && customerPreDepositVO.cusId??>
                           value="${(customerPreDepositVO.cusId)?c}"
                    <#else>
                           value="0"
                    </#if>/>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="name">
                                    顾客姓名
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="name" readonly
                                           placeholder="顾客姓名" value="${(customerPreDepositVO.name)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="mobile">
                                    顾客电话
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="mobile" type="text" class="form-control" id="mobile"  readonly
                                           placeholder="顾客电话" value="${(customerPreDepositVO.mobile)!''}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="balance">
                                    预存款余额
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="balance" type="text" class="form-control" id="balance"  readonly
                                           placeholder="预存款余额" value="${(customerPreDepositVO.balance)!'0.00'}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="changeMoney">变更金额
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入变更金额"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="changeMoney" type="text" class="form-control" id="changeMoney"
                                           placeholder="变更金额">
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                            <label for="changeType">
                                变更类型
                                <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                   data-content="选择预存款变更类型"></i>
                            </label>
                            <select class="form-control select" name="changeType" id="changeType">
                                <if paymentTypes??>
                                    <#list paymentTypes as paymentType>
                                        <option value="${paymentType.value}">${paymentType.description}</option>
                                    </#list>
                                </if>
                            </select>
                        </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="transferTime">到账日期
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                           data-content="选择到账日期,不选则默认为当日"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="transferTime" type="text" class="form-control datepicker" id="transferTime"
                                           readonly placeholder="到账日期">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="merchantOrderNumber">商户订单号
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入6位的商户订单号"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="merchantOrderNumber" type="text" class="form-control" id="merchantOrderNumber"
                                           placeholder="商户订单号">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="remarks">备注
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入0~50字备注"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="remarks" type="text" class="form-control" id="remarks"
                                           placeholder="备注">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-2">
                            <button type="submit" class="btn btn-primary footer-btn">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 取消
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<script>
    $(function () {
        if (!$global.validateMobile()) {
            $('.select').selectpicker();
        }

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

    });
</script>
</body>