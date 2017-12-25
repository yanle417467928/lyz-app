<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet">
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

            $('#cityDeliveryTime_add').bootstrapValidator({
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
                    } ,
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
                    },
                }
            }).on('success.form.bv', function (e) {
                var startTime = $('#startTime').val();
                var endTime = $('#endTime').val();
                var cityId =$('#cityId').val();
                var isconflict;
                if(endTime<=startTime){
                    $notify.danger('结束时间小于或等于开始时间');
                    $('#cityDeliveryTime_add').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
            //判断时间段是否有冲突

                    $.ajax({
                    url: '/rest/cityDeliveryTime/judgmentTime',
                    method: 'POST',
                    data: {'startTime':startTime,'endTime':endTime,'cityId':cityId},
                    async: false,
                    error: function () {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        isconflict=1;
                    },
                    success: function (result) {
                           if(result.valid==false){
                               clearTimeout($global.timer);
                               $loading.close();
                               $global.timer = null;
                               isconflict=2
                           }
                        }
                });

                if(isconflict==1){
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#cityDeliveryTime_add').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }else if( isconflict==2){
                    $notify.danger('时间段与现有时间段冲突，请检查');
                    $('#cityDeliveryTime_add').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }

                e.preventDefault();
                var $form = $(e.target);
                var origin = $form.serializeArray();
                var data = {};
                $.each(origin, function () {
                    data[this.name] = this.value;
                });
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    var url = '/rest/cityDeliveryTime';
                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: data,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#cityDeliveryTime_add').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                               window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#cityDeliveryTime_add').bootstrapValidator('disableSubmitButtons', false);
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
    <h1>新增城市配送时间</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="cityDeliveryTime_add" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    城市名称
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="cityId" type="hidden" class="form-control" id="cityId" readonly
                                           value="${cityId}">
                                    <input name="cityName" type="text" class="form-control" id="name" readonly
                                           value="${cityName}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    配送开始时间
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="startTime" type="text" class="form-control" id="startTime"
                                           placeholder="默认格式 xx:xx (例 08:00)">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    配送结束时间
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="endTime" type="text" class="form-control" id="endTime"
                                           placeholder="默认格式 xx:xx (例 08:00)">
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

/*    function findCitySelection(){
        var city;
        $.ajax({
            url: '/rest/citys/findCity',
            method: 'GET',
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);
            }
        });

    }*/

</script>
</body>