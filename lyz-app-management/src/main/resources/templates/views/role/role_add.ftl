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

            $('#role_add').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {
                    name: {
                        message: '角色名称校验失败',
                        validators: {
                            notEmpty: {
                                message: '角色名称不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z\u4E00-\u9FA5]+$/,
                                message: '角色名称只能输入字母或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 20,
                                message: '角色名称的长度必须在2~20位之间'
                            }
                        }
                    },
                    description: {
                        message: '角色描述校验失败',
                        validators: {
                            notEmpty: {
                                message: '角色描述不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z\u4E00-\u9FA5]+$/,
                                message: '角色描述只能输入字母或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 20,
                                message: '角色描述的长度必须在2~20位之间'
                            }
                        }
                    },
                    seq: {
                        message: '排序号校验失败',
                        validators: {
                            notEmpty: {
                                message: '排序号不能为空'
                            },
                            stringLength: {
                                min: 1,
                                max: 5,
                                message: '排序号长度必须在1~5位之间'
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

                    var url = '/rest/role';

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
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#member_add').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#member_add').bootstrapValidator('disableSubmitButtons', false);
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
    <h1>新增角色</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="role_add">
                    <input type="hidden" id="id" name="id"
                    <#if menuDO?? && menuDO.id??>
                           value="${menuDO.id?c}"
                    <#else>
                           value="0"
                    </#if>
                    >
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    角色名称
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中文，长度在2~10之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="name" placeholder="角色名称">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    角色描述
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="资源详细说明,只能输入中文，长度在2~10之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="description" type="text" class="form-control" id="description"
                                           placeholder="角色描述">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="seq">
                                    排序号
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="菜单的排序号，数字越小，排序越靠前，范围在1~999之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="seq" type="number" class="form-control" id="seq"
                                           placeholder="排序号">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <div class="form-group">
                                    <label for="status">状态</label>
                                    <br>
                                    <input name="status" class="switch" id="status" type="checkbox" checked data-on-text="启用" data-off-text="停用"/>
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


    function checkMenuType(menuType) {
        if (menuType == "CHILD") {
            $('#parent_info').show()

        } else if (menuType == "PARENT") {
            $('#parent_info').hide()

        }
    }
</script>
</body>