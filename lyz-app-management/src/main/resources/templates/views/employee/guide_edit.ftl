<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>

    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
</head>
<body>

<section class="content-header">
    <h1>修改导购额度信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-2" data-toggle="tab" ">变更描述</a></li>
        </ul>
                <form id="guideCredit_edit">
                    <div class="tab-content">
                        <div class="tab-pane active" id="tab_1-1">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    ID
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="empId" type="text" class="form-control" id="id" readonly
                                           value="${guideVO.id!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    姓名
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input type="text" class="form-control" id="name" readonly
                                           value="${guideVO.name!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    归属城市
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input type="text" class="form-control" id="cityName" readonly
                                           value="<#if guideVO??><#if guideVO.cityId??>${guideVO.cityId.name!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    归属门店
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input type="text" class="form-control" id="storeName" readonly
                                           value="<#if guideVO??><#if guideVO.storeId??>${guideVO.storeId.storeName!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    额度余额
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="originalCreditLimitAvailable" type="hidden" class="form-control"
                                           id="originalCreditLimitAvailable" readonly
                                           value="<#if guideVO??><#if guideVO.guideCreditMoney??>${guideVO.guideCreditMoney.creditLimitAvailable!''}</#if></#if>">
                                    <input name="creditLimitAvailable" type="text" class="form-control"
                                           id="creditLimitAvailable" readonly
                                           value="<#if guideVO??><#if guideVO.guideCreditMoney??>${guideVO.guideCreditMoney.creditLimitAvailable!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    临时额度
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name=" originalTempCreditLimit" type="hidden" class="form-control"
                                           id="originalTempCreditLimit" readonly
                                           value="<#if guideVO??><#if guideVO.guideCreditMoney??>${guideVO.guideCreditMoney.tempCreditLimit!''}</#if></#if>">
                                    <input name="tempCreditLimit" type="text" class="form-control" id="tempCreditLimit"
                                           value="<#if guideVO??><#if guideVO.guideCreditMoney??>${guideVO.guideCreditMoney.tempCreditLimit!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    固定额度
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="originalCreditLimit" type="hidden" class="form-control"
                                           id="originalCreditLimit"
                                           readonly
                                           value="<#if guideVO??><#if guideVO.guideCreditMoney??>${guideVO.guideCreditMoney.creditLimit!''}</#if></#if>">
                                    <input name="creditLimit" type="text" class="form-control" id="creditLimit"
                                           value="<#if guideVO??><#if guideVO.guideCreditMoney??>${guideVO.guideCreditMoney.creditLimit!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                        </div>
                    <div class="tab-pane" id="tab_1-2">
                        <div class="box-body table-reponsive">
                        </div>
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <div class="form-group">
                                    <label for="cityName">变更原因
                                        <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                                    </label>
                                    <div>
                                    <textarea id="modifyReason" class="form-control" rows="8"
                                              style="border-color:#808080">
                                    </textarea>
                                    </div>
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
</section>
<script>
    $(function () {

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

        $('#guideCredit_edit').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                id: {
                    message: 'ID校验失败',
                    validators: {
                        notEmpty: {
                            message: 'ID不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]*[1-9][0-9]*$/,
                            message: 'ID只能输入数字'
                        }
                    }
                }, creditLimit: {
                    message: '固定额度校验失败',
                    validators: {
                        notEmpty: {
                            message: '固定额度不能为空'
                        },
                        regexp: {
                            regexp: /^((\d{1,3}(,\d{3})*)|(\d+))(\.\d{1,2})?$/,
                            message: '固定额度只能输入数字'
                        }
                    }
                }, tempCreditLimit: {
                    message: '临时额度校验失败',
                    validators: {
                        notEmpty: {
                            message: '临时额度不能为空'
                        },
                        regexp: {
                            regexp: /^((\d{1,3}(,\d{3})*)|(\d+))(\.\d{1,2})?$/,
                            message: '临时额度只能输入数字'
                        }
                    }
                }, creditLimitAvailable: {
                    message: '可用校验失败',
                    validators: {
                        notEmpty: {
                            message: '可用额度不能为空'
                        },
                        regexp: {
                            regexp: /^([-]?)((\d{1,3}(,\d{3})*)|(\d+))(\.\d{1,2})?$/,
                            message: '可用额度只能输入数字'
                        },
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var $form = $(e.target);
            var origin = $form.serializeArray();
            var data = {};
            var originalCreditLimitAvailable = parseFloat($('#originalCreditLimitAvailable').val().replace(/,/g, ''));
            var originalTempCreditLimit = parseFloat($('#originalTempCreditLimit').val().replace(/,/g, ''));
            var tempCreditLimitAfterChange = parseFloat($('#tempCreditLimit').val().replace(/,/g, ''));
            var originalCreditLimit = parseFloat($('#originalCreditLimit').val().replace(/,/g, ''));
            var creditLimit = parseFloat($('#creditLimit').val().replace(/,/g, ''));
            var empId = $('#id').val();
            var modifyReason = $("#modifyReason").val();
            data = {
                'empId': empId,
                'originalCreditLimitAvailable': originalCreditLimitAvailable,
                'originalTempCreditLimit': originalTempCreditLimit,
                'originalCreditLimit': originalCreditLimit,
                'creditLimit': creditLimit,
                'tempCreditLimit': tempCreditLimitAfterChange,
                "modifyReason": modifyReason
            }

            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);
                var url = '/rest/guideLine';
                if (null !== data.id && 0 != data.id) {
                    data._method = 'PUT';
                }
                $.ajax({
                    url: url,
                    method: 'POST',
                    data: data,
                    error: function () {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger('网络异常，请稍后重试或联系管理员');
                        $('#guideCredit_edit').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            /*      saveCreditChange();*/
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger(result.message);
                            $('#guideCredit_edit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });

</script>
</body>