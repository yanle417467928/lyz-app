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

    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
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

            $('#rule_edit').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {
                    billDate: {
                        message: '出账日校验失败',
                        validators: {
                            notEmpty: {
                                message: '出账日不能为空'
                            },
                            regexp: {
                                regexp: /^((0?[1-9])|((1|2)[0-9])|30)$/,
                                message: '出账日应在1-30日之间'
                            },
                            stringLength: {
                                min: 1,
                                max: 2,
                                message: '出账日长度必须在1~2位之间'
                            },
                        }
                    },
                    repaymentDeadlineDate: {
                        message: '还款截至日校验失败',
                        validators: {
                            notEmpty: {
                                message: '还款截至日不能为空'
                            },
                            regexp: {
                                regexp: /^((0?[1-9])|((1|2)[0-9])|30)$/,
                                message: '还款截至日应在1-30日之间'
                            },
                            stringLength: {
                                min: 1,
                                max: 2,
                                message: '还款截至日长度必须在1~2位之间'
                            },
                        }
                    },
                    interestRate: {
                        message: '利率校验失败',
                        validators: {
                            notEmpty: {
                                message: '利率不能为空'
                            },
                            regexp: {
                                regexp: /^(((\d|[1-9]\d)(\.\d{1,2})?)|100)$/,
                                message: '请输入1-100以内的正实数(最多两位小数)'
                            },
                            stringLength: {
                                min: 1,
                                max: 6,
                                message: '利率长度必须在1~6位之间'
                            }
                        }
                    },
                }
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                var storeCode = $('#storeId').val();
                var billDate =parseInt($('#billDate').val()) ;
                var repaymentDeadlineDate = parseInt($('#repaymentDeadlineDate').val());
                var interestRate =  $('#interestRate').val();
                var updateTime =  $('#updateTime').val();
                var ruleId =  $('#ruleId').val();
                if(storeCode == -1){
                    $notify.danger('请选择相关的装饰公司');
                    $('#rule_edit').bootstrapValidator('disableSubmitButtons', false);
                    return;
                }
                if(repaymentDeadlineDate > billDate){
                    if(repaymentDeadlineDate-billDate>10){
                        $notify.danger("还款截止日应在出账日之后10日以内");
                        $('#rule_edit').bootstrapValidator('disableSubmitButtons', false);
                        return;
                    }
                }else{
                    repaymentDeadlineDate = repaymentDeadlineDate +30
                    if(repaymentDeadlineDate-billDate>10){
                        $notify.danger("还款截止日应在出账日之后10日以内");
                        $('#rule_add').bootstrapValidator('disableSubmitButtons', false);
                        return;
                    }
                }
                var $form = $(e.target);
                var origin = $form.serializeArray();
                var data = {
                    'storeId':storeCode,
                    'billDate':billDate,
                    'repaymentDeadlineDate':repaymentDeadlineDate,
                    'interestRate':interestRate,
                    'ruleId':ruleId,
                };
                var formData = new FormData($("#rule_add")[0]);
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    var url = '/rest/fitBill/editRule';
                    $.ajax({
                        url: url,
                        method: 'PUT',
                        data: data,
                        processData: true,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#rule_edit').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#rule_edit').bootstrapValidator('disableSubmitButtons', false);
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
    <h1>新增账单规则</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="rule_edit" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    装饰公司
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="storeCode" type="text" class="form-control" id="storeCode" value="<#if billRule?? >${billRule.storeName!""}</#if>"
                                           readonly    placeholder="门店名称">
                                    <input name="storeId" type="hidden" class="form-control" id="storeId" value="<#if billRule?? >${billRule.storeId!""}</#if>"
                                           readonly  >
                                    <input name="ruleId" type="hidden" class="form-control" id="ruleId" value="<#if billRule?? >${billRule.id!""}</#if>"
                                           readonly  >
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    出账日
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="billDate" type="text" class="form-control" id="billDate" value="<#if billRule?? >${billRule.billDate!""}</#if>"
                                        placeholder="出账日">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    还款截至日(出账日之后10日以内)
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="repaymentDeadlineDate" type="text" class="form-control" id="repaymentDeadlineDate" value="<#if billRule?? >${billRule.repaymentDeadlineDate!""}</#if>"
                                           placeholder="还款截至日">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    利率(单位：万分之一/天 )
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="interestRate" type="text" class="form-control" id="interestRate" value="<#if billRule?? >${billRule.interestRate!""}</#if>"
                                           placeholder="利率">
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