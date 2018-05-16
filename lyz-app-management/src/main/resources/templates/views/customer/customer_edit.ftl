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
            var sex = "${customer.sex}";
            if ('MALE' == sex) {
                $('#sex').val("MALE");
            } else if ('FEMALE' == sex) {
                $('#sex').val("FEMALE");
            } else if ('SECRET' == sex) {
                $('#sex').val("SECRET");
            }

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

            $('#customer_edit').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {
                    name: {
                        message: '顾客姓名校验失败',
                        validators: {
                            notEmpty: {
                                message: '顾客姓名不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                                message: '顾客姓名只能输入字母或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 10,
                                message: '顾客姓名的长度必须在2~10位之间'
                            },
                        }
                    },
                    mobile: {
                        message: '手机号码校验失败',
                        validators: {
                            notEmpty: {
                                message: '手机号码不能为空'
                            },
                            regexp: {
                                regexp: /^1[3456789]\d{9}$/,
                                message: '请输入正确的手机号'
                            },
                            remote: {
                                type: 'POST',
                                url: '/rest/customers/isExistPhoneNumberByCusId',
                                message: '该电话已被使用',
                                delay: 500,
                                data: function () {
                                    return {
                                        mobile: $('#mobile').val(),
                                        cusId: $('#cusId').val(),
                                    }
                                }
                            }
                        }
                    },
                }
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                var mobile = $('#mobile').val();
                var cusId = $('#cusId').val();
                var birthday = $('#birthday').val();
                var sex = $('#sex').val();
                var name = $('#name').val();
                var data = {
                    "mobile": mobile,
                    "cusId": cusId,
                    "birthday": birthday,
                    "sex": sex,
                    "name": name
                };
                var url = '/rest/customers';
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
                            $('#customer_edit').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#customer_edit').bootstrapValidator('disableSubmitButtons', false);
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
    <h1>编辑顾客</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="customer_edit" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    顾客姓名
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中英文字符，长度在2~20之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="cusId" type="hidden" class="form-control" id="cusId"
                                           value="${customer.cusId?c}">
                                    <input name="name" type="text" class="form-control" id="cusName"
                                           value="${customer.name!''}" placeholder="顾客姓名">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    电话号码
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入11位数字"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="mobile" type="text" class="form-control" id="mobile"
                                           value="${customer.mobile!''}" placeholder="电话号码">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    性别
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择用户性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="sex" id="sex">
                                    <option value="MALE">男</option>
                                    <option value="FEMALE">女</option>
                                    <option value="SECRET">保密</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    出生日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="birthday" type="text" class="form-control datepicker" id="birthday"
                                           value="<#if customer??&&customer.birthday??>${customer.birthday?string('yyyy-MM-dd')}</#if>" readonly placeholder="出生日期">
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