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
    <h1>修改订单运费信息</h1>
</section>

<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="freight_edit">
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="cityName">城市
                                    <i class="fa fa-question-circle  hidden-xs"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="oid" type="hidden" class="form-control" id="oid" readonly
                                           value="${(orderFreightVO.id)?c}">
                                    <input name="city" type="text" class="form-control" id="city" readonly
                                           value="${(orderFreightVO.cityName)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">门店
                                    <i class="fa fa-question-circle  hidden-xs" data-toggle="tooltip"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="storeId" type="text" class="form-control" id="storeId" readonly
                                           value="<#if orderFreightVO??><#if orderFreightVO.storeId??>${orderFreightVO.storeId.storeName!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="cityName">订单号
                                    <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="ordNo" type="text" class="form-control" id="ordNo" readonly
                                           value="${(orderFreightVO.ordNo)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">下单人
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                    ></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="creatorName" type="text" class="form-control" id="creatorName" readonly
                                           value="${(orderFreightVO.creatorName)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="cityName">下单人电话
                                    <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="creatorPhone" type="text" class="form-control" id="creatorPhone"
                                           readonly value="${(orderFreightVO.creatorPhone)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="cityName">运费(元)
                                    <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="freight" type="text" class="form-control" id="freight" readonly="readonly"
                                           value="<#if orderFreightVO??><#if orderFreightVO.simpleOrderBillingDetails??>${orderFreightVO.simpleOrderBillingDetails.freight!''}</#if></#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="changeType">变更类型
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择变更类型"></i>
                                </label>
                                <select class="form-control select" name="changeType" id="changeType">
                                    <option value="">选择变更类型</option>
                                    <if freightChangeTypes??>
                                    <#list freightChangeTypes as changeType>
                                        <option value="${changeType.value}">${changeType.description}</option>
                                    </#list>
                                    </if>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="changeAmount">变更运费金额(元)
                                    <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="changeAmount" type="text" class="form-control" id="changeAmount">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="modifyReason">变更原因
                                    <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                                </label>
                                <div>
                                    <textarea id="modifyReason" class="form-control" rows="5"
                                              style="border-color:#808080">
                                    </textarea>
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
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });

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

        $('#freight_edit').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                changeAmount: {
                    message: '变更金额校验失败',
                    validators: {
                        notEmpty: {
                            message: '变更金额不能为空'
                        },
                        regexp: {
                            regexp: /^[-+]?\d*[.]?\d{0,2}$/,
                            message: '请输入正确的金额'
                        },
                        stringLength: {
                            min: 1,
                            max: 10,
                            message: '金额的长度必须在1~10位之间'
                        }
                    }
                },
                changeType: {
                    message: '变更类型校验失败',
                    validators: {
                        notEmpty: {
                            message: '变更类型不能为空'
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var oid = parseFloat($('#oid').val().replace(/,/g, ''));
            var freight = $("#freight").val();
            var modifyReason = $("#modifyReason").val();
            var changeAmount = $("#changeAmount").val();
            var changeType = $("#changeType").find("option:selected").val();
            if (parseFloat(freight) + parseFloat(changeAmount) < 0){
                $notify.danger('运费金额不能小于0');
                return;
            }
            var data = {
                "orderId": oid,
                "freight": freight,
                "changeAmount": changeAmount,
                "changeType": changeType,
                "modifyReason": modifyReason
            };
            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);
                var url = '/rest/orderFreight/update';
                if (null !== data.storeId && 0 != data.storeId) {
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
                        $('#freight_edit').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger(result.message);
                            $('#freight_edit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });

</script>
</body>