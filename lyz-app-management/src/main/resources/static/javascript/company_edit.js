$(function () {
    if (!$global.validateMobile()) {
        $('.select').selectpicker({
            size: '5'
        });
    }

    $('[name="frozen"]').bootstrapSwitch({
        onText: "是",
        offText: "否",
        onColor: "primary",
        offColor: "danger",
        onSwitchChange: function (event, state) {
            if (state == true) {
                $(this).val("on");
            } else {
                $(this).val("off");
            }
        }
    });

    $(function () {
        $('[data-toggle="tooltip"]').popover();
    });

    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd 00:00:00',
        language: 'zh-CN',
        autoclose: true
    });

    $('.switch').bootstrapSwitch();

    $('#companyFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            name: {
                message: '装饰公司名称校验失败',
                validators: {
                    notEmpty: {
                        message: '装饰公司名称不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 25,
                        message: '装饰公司名称的长度必须在2~25位之间'
                    }
                }
            },
            address: {
                message: '装饰公司地址校验失败',
                validators: {
                    stringLength: {
                        min: 0,
                        max: 25,
                        message: '商品简称的长度必须在0~25位之间'
                    }
                }
            },
            phone: {
                message: '装饰公司电话校验失败',
                validators: {
                    regexp: {
                        regexp: /^(1[3584]\d{9})$/,
                        message: '请输入正确的装饰公司电话！'
                    },
                    stringLength: {
                        min: 0,
                        max: 11,
                        message: '装饰公司电话的长度必须在0~11位之间'
                    }
                }
            },
            code: {
                message: '装饰公司编码校验失败',
                validators: {
                    notEmpty: {
                        message: '装饰公司编码不能为空'
                    },
                    regexp: {
                        regexp: /^[^\u4e00-\u9fa5]+$/,
                        message: '装饰公司编码不能输入汉字！'
                    },
                    stringLength: {
                        min: 2,
                        max: 25,
                        message: '装饰公司编码的长度必须在2~25位之间'
                    }
                }
            },
            credit: {
                message: '信用金余额校验失败',
                validators: {
                    regexp: {
                        regexp: /^[0-9.-]+$/,
                        message: '只能输入表示金额的数字'
                    },
                    numeric: {
                        message: '信用金余额只能输入数字'
                    },
                    stringLength: {
                        min: 0,
                        max: 12,
                        message: '信用金余额的长度必须在0~12位之间'
                    },
                    callback: {
                        message: '请输入正确的金额,并且信用金余额小数点后最多为两位',
                        callback: function (value, validator) {
                            var returnVal = true;
                            if ((value + "").indexOf(".") >= 0) {
                                var ArrMen = value.split(".");    //截取字符串
                                if (ArrMen.length != 2) {
                                    returnVal = false;
                                    return returnVal;
                                } else {
                                    if (ArrMen[0].length == 0) {
                                        returnVal = false;
                                        return returnVal;
                                    }
                                    if (ArrMen[1].length > 2 || ArrMen[1].length == 0) {    //判断小数点后面的字符串长度
                                        returnVal = false;
                                        return returnVal;
                                    }
                                }
                            }
                            return returnVal;
                        }
                    }
                }
            },
            promotionMoney: {
                message: '赞助金余额校验失败',
                validators: {
                    numeric: {
                        message: '赞助金余额只能输入数字'
                    },
                    stringLength: {
                        min: 0,
                        max: 12,
                        message: '赞助金余额的长度必须在0~12位之间'
                    },
                    callback: {
                        message: '请输入正确的金额,并且赞助金余额小数点后最多为两位',
                        callback: function (value, validator) {
                            var returnVal = true;
                            if ((value + "").indexOf(".") >= 0) {
                                var ArrMen = value.split(".");    //截取字符串
                                if (ArrMen.length != 2) {
                                    returnVal = false;
                                    return returnVal;
                                } else {
                                    if (ArrMen[0].length == 0) {
                                        returnVal = false;
                                        return returnVal;
                                    }
                                    if (ArrMen[1].length > 2 || ArrMen[1].length == 0) {    //判断小数点后面的字符串长度
                                        returnVal = false;
                                        return returnVal;
                                    }
                                }
                            }
                            return returnVal;
                        }
                    }
                }
            },
            walletMoney: {
                message: '钱包余额校验失败',
                validators: {
                    numeric: {
                        message: '钱包余额只能输入数字'
                    },
                    stringLength: {
                        min: 0,
                        max: 12,
                        message: '钱包余额的长度必须在0~12位之间'
                    },
                    callback: {
                        message: '请输入正确的金额,并且钱包余额小数点后最多为两位',
                        callback: function (value, validator) {
                            var returnVal = true;
                            if ((value + "").indexOf(".") >= 0) {
                                var ArrMen = value.split(".");    //截取字符串
                                if (ArrMen.length != 2) {
                                    returnVal = false;
                                    return returnVal;
                                } else {
                                    if (ArrMen[0].length == 0) {
                                        returnVal = false;
                                        return returnVal;
                                    }
                                    if (ArrMen[1].length > 2 || ArrMen[1].length == 0) {    //判断小数点后面的字符串长度
                                        returnVal = false;
                                        return returnVal;
                                    }
                                }
                            }
                            return returnVal;
                        }
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        $.each(origin, function () {
            data[this.name] = this.value;
        });

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/company';

            if (null != data.id && 0 != data.id) {
                data._method = 'PUT';
                url += ('/' + data.id);
            }
            data.frozen = (undefined === data.frozen) ? false : data.frozen;
            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#companyFrom').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#companyFrom').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });

    $('.btn-cancel').on('click', function () {
        history.go(-1);
    });

})
