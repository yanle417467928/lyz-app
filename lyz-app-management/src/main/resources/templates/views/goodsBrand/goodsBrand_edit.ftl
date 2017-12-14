<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet">
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
    <h1>修改商品分类信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="goodsBrand_edit">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    品牌ID
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="brdId" type="text" class="form-control" id="brdId" readonly
                                           value="${goodsBrand.brdId}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    品牌名称
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="brandName" type="text" class="form-control" id="brandName"
                                           value="${goodsBrand.brandName}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    品牌编码
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="brandCode" type="text" class="form-control" id="brandCode"
                                           value="${goodsBrand.brandCode}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    排序号
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sortId" type="text" class="form-control" id="sortId"
                                           value="${goodsBrand.sortId}">
                                </div>
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

        $('#goodsBrand_edit').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                brandName: {
                    message: '品牌名称校验失败',
                    validators: {
                        notEmpty: {
                            message: '品牌名称不能为空'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z\u4E00-\u9FA5]+$/,
                            message: '品牌名称只能输入字母或汉字'
                        },
                        stringLength: {
                            min: 2,
                            max: 20,
                            message: '品牌名称的长度必须在2~20位之间'
                        },remote: {
                            type: 'POST',
                            url:  '/rest/goodsBrand/editIsExistBrandName',
                            message: '已存在品牌名称',
                            delay: 500,
                            data: function () {
                                return {
                                    brandName: $('#brandName').val(),
                                    id: $('#brdId').val(),
                                }
                            }
                        }
                    }
                },
                brandCode: {
                    message: '品牌编码校验失败',
                    validators: {
                        notEmpty: {
                            message: '品牌名称不能为空'
                        },
                        stringLength: {
                            min: 2,
                            max: 20,
                            message: '品牌名称的长度必须在2~20位之间'
                        }
                    }
                },
                sortId: {
                    message: '排序号校验失败',
                    validators: {
                        notEmpty: {
                            message: '排序号不能为空'
                        },
                        regexp: {
                            regexp: /^[1-9]\d{0,3}|10000$ /,
                            message: '分类名称只能输入数字'
                        },
                        stringLength: {
                            min: 1,
                            max: 5,
                            message: '排序号长度必须在1~5位之间'
                        },remote: {
                            type: 'POST',
                            url:  '/rest/goodsBrand/editIsExistSort',
                            message: '已存在品牌名称',
                            delay: 500,
                            data: function () {
                                return {
                                    sortId: $('#sortId').val(),
                                    id: $('#brdId').val(),
                                }
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

                var url = '/rest/goodsBrand';

                if (null !== data.brdId && 0 != data.brdId) {
                    data._method = 'PUT';
                    url += ('/' + data.brdId);
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
                        $('#goodsBrand_edit').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger(result.message);
                            $('#goodsBrand_edit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });
</script>
</body>