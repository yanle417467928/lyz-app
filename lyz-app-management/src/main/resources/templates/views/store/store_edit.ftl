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
    <h1>修改门店信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="store_edit">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    门店ID
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="storeId" type="text" class="form-control" id="storeId" readonly
                                           value="${store.storeId}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    门店编码
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="cityCode" type="text" class="form-control" id="cityCode" readonly
                                           value="${store.cityCode}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    所属城市
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="cityName" type="text" class="form-control" id="cityName" readonly
                                           value="${store.cityId.name}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    门店名称
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="storeName" type="text" class="form-control" id="storeName" readonly
                                           value="${store.storeName}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    是否是默认门店
                                </label>
                                <select class="form-control select" name="isDefault" disabled="true" id="isDefault">
                                    <option value="true" <#if store?? && store.isDefault?? && store.isDefault > selected</#if>>是</option>
                                    <option value="false" <#if store?? && store.isDefault?? && !store.isDefault > selected</#if>>否</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    门店类型
                                </label>
                                <select class="form-control select" name="storeType" disabled="true" id="storeType">
                                    <option value="1" <#if store?? && store.storeType?? && store.storeType=="ZY" > selected</#if>>自营</option>
                                    <option value="2" <#if store?? && store.storeType?? && store.storeType=="ZS" > selected</#if>>装饰公司</option>
                                    <option value="3" <#if store?? && store.storeType?? && store.storeType=="JM" > selected</#if>>加盟店</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    是否启用
                                </label>
                                <select class="form-control select" name="enable" disabled="true" id="enable">
                                    <option value="true" <#if store?? && store.enable?? && store.enable > selected</#if>>是</option>
                                    <option value="false"<#if store?? && store.enable?? && !store.enable > selected</#if>>否</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    创建时间
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="createTime" type="text" class="form-control" readonly id="createTime"
                                           value="${store.createTime?string("yyyy-MM-dd HH:mm:ss")}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    是否支持门店自提
                                </label>
                                <select class="form-control select" name="isSelfDelivery" id="isSelfDelivery">
                                    <option value="true" <#if store?? && store.isSelfDelivery?? && store.isSelfDelivery > selected</#if>>是</option>
                                    <option value="false" <#if store?? && store.isSelfDelivery?? && !store.isSelfDelivery > selected</#if>>否</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    门店地址省
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="province" type="text" class="form-control" id="province" readonly
                                           value="${store.province!}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <div class="form-group">
                                    <label>
                                        门店地址市
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                        <input name="city" type="text" class="form-control" id="city" readonly
                                               value="${store.city!}">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    门店地址区
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="area" type="text" class="form-control" id="area" readonly
                                           value="${store.area!}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    门店地址详细地址
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="detailedAddress" type="text" class="form-control" readonly
                                           id="detailedAddress" value="${store.detailedAddress!}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    门店电话
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="phone" type="text" class="form-control" id="phone" readonly
                                           value="${store.phone!}">
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

        $('#store_edit').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var isSelfDelivery=$("#isSelfDelivery").val();
            var storeId=$("#storeId").val();
            var data = {"storeId":storeId,"isSelfDelivery":isSelfDelivery};

            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);

                var url = '/rest/stores';

                if (null !== data.storeId && 0 != data.storeId) {
                    data._method = 'PUT';
                    url += ('/' + data.storeId);
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
                        $('#store_edit').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger(result.message);
                            $('#store_edit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });

</script>
</body>