$(function() {
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
    $('[name="sex"]').bootstrapSwitch({
        onText: "男",
        offText: "女",
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
    $('[name="isMain"]').bootstrapSwitch({
        onText: "主账号",
        offText: "子账号",
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

    $('#companyUserFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            userName: {
                message: '姓名校验失败',
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 25,
                        message: '姓名的长度必须在2~25位之间'
                    }
                }
            },
            mobile: {
                message: '手机号码校验失败',
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入正确的手机号码'
                    },
                    regexp: {
                        regexp: /^(1[3584]\d{9})$/,
                        message: '输入格式不正确'
                    }
                }
            },
            companyId: {
                message: '装饰公司校验失败',
                validators: {
                    notEmpty: {
                        message: '装饰公司不能为空'
                    }
                }
            },
            age: {
                message: '年龄校验失败',
                validators: {
                    regexp: {
                        regexp: /^[0-9]+$/,
                        message: '年龄不能输入汉字！'
                    },
                    stringLength: {
                        min: 0,
                        max: 3,
                        message: '年龄的长度必须在0~3位之间'
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

            var url = '/rest/companyUser';

            if (null != data.id && 0 != data.id) {
                data._method = 'PUT';
                url += ('/' + data.id);
            }
            data.sex = (undefined === data.sex) ? false : data.sex;
            data.isMain = (undefined === data.isMain) ? false : data.isMain;
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
