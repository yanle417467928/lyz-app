<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <style type="text/css">
        .box-default {
            border-top-width: 0px;
            margin-bottom: 0px;
            position: absolute;
            z-index: 99;
        }

        .box-title {
            font-size: 12px !important;
        }

        .box-body {
            display: block;
        }

        .box-footer {
            text-align: right;
        }
    </style>
</head>
<body>
<section class="content-header">
    <h1>编辑装饰公司信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="tab_1-1">
                    <form id="decorativeCompanyCreditFrom" class="bv-form tab-content">
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">装饰公司名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入装饰公司名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input type="hidden" name="id" id="id"
                                    <#if decorativeCompanyVO?? && decorativeCompanyVO.id??>
                                           value="${(decorativeCompanyVO.id)?c}"
                                    <#else>
                                           value="0"
                                    </#if>/>
                                    <input name="name" type="text" class="form-control" id="name"
                                           placeholder="装饰公司名称" readonly
                                           value="${(decorativeCompanyVO.storeName)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="code">装饰公司编码
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入装饰公司编码"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="code" type="text" class="form-control" id="code"
                                           placeholder="装饰公司编码" readonly
                                           value="${(decorativeCompanyVO.storeCode)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="credit">信用金余额
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入信用金余额"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="cid" type="hidden" class="form-control" id="cid"
                                           value="${(decorativeCompanyVO.credit.cid)!''}">
                                    <input name="credit" type="text" class="form-control" id="credit"
                                           placeholder="信用金余额"
                                           value="${(decorativeCompanyVO.credit.credit)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="promotionMoney">赞助金余额
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入赞助金余额"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sid" type="hidden" class="form-control" id="sid"
                                           value="${(decorativeCompanyVO.sponsorship.sid)!''}">
                                    <input name="sponsorship" type="text" class="form-control" id="sponsorship"
                                           placeholder="赞助金余额"
                                           value="${(decorativeCompanyVO.sponsorship.sponsorship)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
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
            <!-- =================================下面是弹框======================================= -->
            <div class="modal modal-primary fade" id="modal-primary">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">正在添加信息</h4>
                        </div>
                        <div class="modal-body">
                            <p id="primaryTitle">正在执行添加操作，您可以点击确认继续，点击关闭则退出。</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" id="primaryCloseBtn" class="btn btn-outline pull-left"
                                    data-dismiss="modal">关闭
                            </button>
                            <button type="button" id="modalAddBtn" class="btn btn-outline">确认添加</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
            <!-- /.modal -->
            <div class="modal modal-danger fade" id="modal-danger">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">正在删除信息</h4>
                        </div>
                        <div class="modal-body">
                            <p id="dangerTitle">是否将此职位信息从数据中移除，点击确认继续，点击关闭取消操作。</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline pull-left" id="modalCloseBtn"
                                    data-dismiss="modal">关闭
                            </button>
                            <button type="button" id="modalDelBtn" class="btn btn-outline">确定，我要删除</button>
                        </div>
                    </div>
                </div>
            </form>
         </div>
    </div>
</section>
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
            format: 'yyyy-MM-dd',
            language: 'zh-CN',
            autoclose: true
        });


        $('.switch').bootstrapSwitch();

        $('#decorativeCompanyCreditFrom').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                credit: {
                    message: '信用金校验失败',
                    validators: {
                        notEmpty: {
                            message: '信用金不能为空'
                        },  regexp: {
                            regexp: /^[0-9]+(.[0-9]{2})?$/,
                            message: '信用金只能为正实数(两位小数)'
                        }
                    }
                }, promotionMoney: {
                    message: '赞助金校验失败',
                    validators: {
                        notEmpty: {
                            message: '赞助金不能为空'
                        },  regexp: {
                            regexp: /^[0-9]+(.[0-9]{2})?$/,
                            message: '赞助金只能为正实数(两位小数)'
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
            data['credit'] = data.credit.replace(/,/g,'');
            data['sponsorship']= data.sponsorship.replace(/,/g,'');
            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);

                var url = '/rest/decorativeCredit';

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
                        $('#decorativeCompanyCreditFrom').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('装饰公司信用保存失败');
                            $('#decorativeCompanyCreditFrom').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });


</script>
</body>