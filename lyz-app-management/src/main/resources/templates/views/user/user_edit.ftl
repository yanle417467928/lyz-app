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

            $('#user_add').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {
                    loginName: {
                        message: '登录名称校验失败',
                        validators: {
                            notEmpty: {
                                message: '登录名称不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                                message: '登录名称只能输入字母、数字或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 20,
                                message: '资源名称的长度必须在2~10位之间'
                            },
                        }
                    },
                    name: {
                        message: '用户姓名校验失败',
                        validators: {
                            notEmpty: {
                                message: '用户姓名不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                                message: '用户姓名只能输入字母、数字或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 20,
                                message: '资源名称的长度必须在2~20位之间'
                            }
                        }
                    },
                    description: {
                        message: '资源描述校验失败',
                        validators: {
                            notEmpty: {
                                message: '资源描述不能为空！'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                                message: '资源描述只能输入字母或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 10,
                                message: '资源描述的长度必须在2~10位之间'
                            }
                        }
                    },
                    url: {
                        message: '资源路径校验失败',
                        validators: {
                            notEmpty: {
                                message: '资源路径不允许为空'
                            }
                        }
                    },
                    seq: {
                        message: '排序号校验失败',
                        validators: {
                            notEmpty: {
                                message: '排序号不允许为空!'
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
                var reslist = $("#role").select2("data");
                var roleIds = [];
                $.each(reslist, function () {
                    roleIds.push(this.id);
                });
                data["roleIdsStr"] = roleIds;

                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);

                    var url = '/rest/user/' + data.id;

                    data.headImageUri = $('#headImageUri').attr("src");
                    data.status = (undefined === data.status) ? false : data.status;
                    $.ajax({
                        url: url,
                        method: 'PUT',
                        data: data,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#user_add').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#user_add').bootstrapValidator('disableSubmitButtons', false);
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
    <h1>编辑用户</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="user_add">
                    <input type="hidden" id="id" name="id" value="${user.uid}"/>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    登录名称
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="用户登录名不允许修改"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="loginName" type="text" class="form-control" id="loginName"
                                           placeholder="登录名称" value="${user.loginName}" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    用户姓名
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中英文，长度在2~20之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="name"
                                           placeholder="用户姓名" value="${user.name}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择用户性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="sex" id="sex" data-live-search="true">
                                    <option value="MALE" <#if user?? && user.sex?? && user.sex=="MALE" > selected</#if>>
                                        男
                                    </option>
                                    <option value="FEMALE" <#if user?? && user.sex?? && user.sex=="FEMALE" >
                                            selected</#if>>女
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="userType">
                                    用户类型
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="请选择用户类型"></i>
                                </label>
                                <select id="userType" name="userType" class="form-control select"
                                        data-live-search="true">
                                    <option value=2  <#if user?? && user.userType?? && user.userType==2 >
                                            selected</#if>>普通用户
                                    </option>
                                    <option value=1 <#if user?? && user.userType?? && user.userType==1 > selected</#if>>
                                        超级管理员
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="type">
                                    用户密码
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="请填写用户密码，如不填，默认123456"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="password" type="text" class="form-control" id="password"
                                           placeholder="填写新密码；如不修改，请不填">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <div class="form-group">
                                    <label for="role">用户角色</label>
                                    <select class="form-control select2" multiple="multiple"
                                            data-placeholder="请为用户分配角色" style="width: 100%;" id="role" name="role">
                                    <#if roleList??>
                                        <#list roleList as item>
                                            <option value="${item.id}"
                                                    <#if roleIds?? && roleIds?seq_contains(item.id)>selected</#if>> ${item.name}</option>
                                        </#list>
                                    </#if>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <div class="form-group" style="margin-left: 2%">
                                    <label for="status">是否启用</label>
                                    <br>
                                    <input name="status" class="switch" id="status" type="checkbox" data-on-text="启用"
                                           data-off-text="停用"<#if user?? && user.status?? && user.status==true >
                                           checked </#if>/>
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


    /*function checkMenuType(menuType) {
        if (menuType == "CHILD") {
            $('#parent_info').show()

        } else if (menuType == "PARENT") {
            $('#parent_info').hide()

        }
    }*/
</script>
</body>