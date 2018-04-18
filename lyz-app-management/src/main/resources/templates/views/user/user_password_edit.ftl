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
</head>
<body>

<section class="content-header">
    <h1>密码管理</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">修改密码</a></li>
        </ul>
        <div class="tab-pane" id="tab_1-1">
            <form id="user_password_edit" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-4 col-xs-12" style="margin-left: 33%">
                        <div class="form-group">
                            <label for="password">旧密码
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                   data-content="请输入原密码"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="oldPassword" type="password" class="form-control" id="oldPassword"
                                       placeholder="输入旧密码">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 col-xs-12" style="margin-left: 33%">
                        <div class="form-group">
                            <label for="password">输入新密码
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                   data-content="输入一个新的六位数的密码，只能以字母、数字开头，可带有“_”、“.”、“-”"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="password" type="password" class="form-control" id="password"
                                       placeholder="输入新密码">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 col-xs-12" style="margin-left: 33%">
                        <div class="form-group">
                            <label for="password2">重新输入新密码
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                   data-content="重新输入新密码"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="password2" type="password" class="form-control" id="password2"
                                       placeholder="重新输入新密码">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-md-4"></div>
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

        $('#user_password_edit').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                oldPassword: {
                    message: '旧密码校验失败',
                    validators: {
                        notEmpty: {
                            message: '旧密码不能为空'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._@-]){0,16}$/,
                            message: '只能输入以字母、数字开头，可带“_”、“.”、“-”、“@”'
                        },
                        stringLength: {
                            min: 5,
                            max: 16,
                            message: '密码的长度为6到16位'
                        },
                        remote: {
                            type: 'POST',
                            url: '/rest/user/validator/password',
                            message: '密码错误',
                            delay: 500,
                            data: function () {
                                return {
                                    oldPassword: $('#oldPassword').val()
                                }
                            }
                        }
                    }
                },
                password: {
                    message: '新密码校验失败',
                    validators: {
                        notEmpty: {
                            message: '新密码不能为空'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._@-]){0,16}$/,
                            message: '只能输入以字母、数字开头，可带“_”、“.”、“-”、“@”'
                        }, stringLength: {
                            min: 5,
                            max: 16,
                            message: '密码的长度为6到16位'
                        }
                    }
                }, password2: {
                    message: '重复密码校验失败',
                    validators: {
                        notEmpty: {
                            message: '重复密码不能为空'
                        },
                        identical: {
                            field: 'password',
                            message: '两次密码输入不一致'
                        }
                    }
                },
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var password = $('#password').val();
            var data = {
                "password": password,
            };
            var url = '/rest/user/editPassword';
            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);
                $.ajax({
                    url: url,
                    method: 'PUT',
                    data: data,
                    error: function () {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger('网络异常，请稍后重试或联系管理员');
                        $('#user_password_edit').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = '/login';
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger(result.message);
                            $('#user_password_edit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });
</script>
</body>