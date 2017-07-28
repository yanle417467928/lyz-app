$(function() {
    $('.btn-cancel').on('click', function() {
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
    $('#passwordFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            password: {
                message: '密码校验失败',
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._-]){0,10}$/,
                        message: '只能输入以字母、数字开头，可带“_”、“.”、“-”'
                    },
                    stringLength: {
                        min: 6,
                        max: 6,
                        message: '密码的长度必须是6位'
                    }
                }
            },
            password2: {
                message: '二次输入密码校验失败',
                validators: {
                    notEmpty: {
                        message: '二次输入密码不能为空'
                    },
                    identical: {
                        field: 'password',
                        message: '两次密码输入不一致'
                    }
                }
            },
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};

        $.each(origin, function() {
            data[this.name] = this.value;
        });

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/member/revise/password';

            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function() {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#employeeFrom').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function(result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#employeeFrom').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });

    $('#member_edit').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            name: {
                message: '会员姓名校验失败',
                validators: {
                    notEmpty: {
                        message: '会员姓名不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z\u4E00-\u9FA5]+$/,
                        message: '会员姓名只能输入字母或汉字'
                    },
                    stringLength: {
                        min: 2,
                        max: 10,
                        message: '会员姓名的长度必须在2~10位之间'
                    }
                }
            },
            store: {
                message: '会员归属门店校验失败',
                validators: {
                    notEmpty: {
                        message: '请为为会员选择归属门店！'
                    }
                }
            },
            salesConsult: {
                message: '会员服务导购校验失败',
                validators: {
                    notEmpty: {
                        message: '请为会员选择归属导购!'
                    }
                }
            },
            identityType: {
                message: '会员身份校验失败',
                validators: {
                    notEmpty: {
                        message: '请为会员选择身份!'
                    }
                }
            },

            mobile: {
                message: '联系电话校验失败',
                validators: {
                    notEmpty: {
                        message: '联系电话不能为空!'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入正确的联系电话!'
                    },
                    regexp: {
                        regexp: /^(1[3584]\d{9})$/,
                        message: '请输入正确的联系电话!'
                    },
                    threshold: 11,
                    remote: {
                        type: 'POST',
                        url: '/rest/member/validator/mobile/'+$('#id').val(),
                        message: '该联系电话已经存在!',
                        delay: 500,
                        data: function() {
                            return {
                                mobile: $('#mobile').val(),
                                //id: $('#id').val()
                            }
                        }
                    }
                }
            }
            /*email: {
                message: '会员邮箱校验失败',
                validators: {
                    notEmpty: {
                        message: '会员邮箱不能为空!'
                    },
                    regexp: {
                        regexp: /.*@*.com.*?(?=\b)/,
                        message: '请输入正确的邮箱地址!'
                    }
                }
            }*/
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};

        $.each(origin, function() {
            data[this.name] = this.value;
        });

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/member';

            if (null !== data.id && 0 != data.id) {
                data._method = 'PUT';
                url += ('/' + data.id);
            }
            data.headImageUri = $('#headImageUri').attr("src");
            data.status = (undefined === data.status) ? false : data.status;
            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function() {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#member_edit').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function(result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#member_edit').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });
});