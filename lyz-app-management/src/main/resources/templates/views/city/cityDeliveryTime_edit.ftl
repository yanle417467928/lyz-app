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
    <h1>修改城市配送时间信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="cityDeliveryTime_edit">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    城市ID
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="id" type="hidden" class="form-control" id="id" readonly
                                           value="${cityDeliveryTimeVO.id?c}">
                                    <input name="cityId" type="text" class="form-control" id="cityId" readonly
                                           value="${cityDeliveryTimeVO.cityId?c}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    城市名称
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="cityName" type="text" class="form-control" id="cityName" readonly
                                           value="${cityDeliveryTimeVO.cityName!''}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    配送开始时间
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon">从</span>
                                    <input name="startTime" type="text" class="form-control" id="startTime"
                                           value="${cityDeliveryTimeVO.startTime!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    配送结束时间
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon">至</span>
                                    <input name="endTime" type="text" class="form-control" id="endTime"
                                           value="${cityDeliveryTimeVO.endTime!''}">
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

        $('#cityDeliveryTime_edit').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                startTime: {
                    message: '配送开始时间校验失败',
                    validators: {
                        notEmpty: {
                            message: '配送开始时间不能为空'
                        },
                        regexp: {
                            regexp: /^([0-1]{1}\d|2[0-3]):([0-5]\d)$/,
                            message: '时间格式为 xx:xx'
                        }
                    }
                },
                endTime: {
                    message: '配送结束时间校验失败',
                    validators: {
                        notEmpty: {
                            message: '配送结束时间不能为空'
                        },
                        regexp: {
                            regexp: /^([0-1]{1}\d|2[0-3]):([0-5]\d)$/,
                            message: '时间格式为 xx:xx'
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            if (endTime <= startTime) {
                $notify.danger('结束时间小于或等于开始时间');
                $('#cityDeliveryTime_edit').bootstrapValidator('disableSubmitButtons', false);
                return false;
            }
            var $form = $(e.target);
            var origin = $form.serializeArray();
            var data = {};

            $.each(origin, function () {
                data[this.name] = this.value;
            });
            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);
                var url = '/rest/cityDeliveryTime';
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
                        $('#cityDeliveryTime_edit').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('时间段与已有时间段冲突');
                            $('#cityDeliveryTime_edit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });
</script>
</body>