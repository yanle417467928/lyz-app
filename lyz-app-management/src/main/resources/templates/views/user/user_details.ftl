<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
</head>
<body>
<section class="content-header">
    <h1>会员信息详情</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:void(0);">会员列表</a></li>
        <li class="active">会员信息详情</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">

            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-2" data-toggle="tab">账户安全</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="user-panel">
                                <div class="pull-left image">
                                    <img src="/images/user2-160x160.jpg" class="img-circle"
                                         style="max-width: 70px;" alt="User Image">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>所在城市</label>
                                <input  readOnly="true" class="form-control select" name="city" data-live-search="true" value="成都">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label> 归属门店</label>
                                <input readOnly="true" class="form-control select" name="store" data-live-search="true" value="富之美">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>专属导购</label>
                                <input readOnly="true" class="form-control select" name="seller" data-live-search="true" value="谢娜">
                                </input>
                            </div>
                            </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>账户状态</label>
                                <input readOnly="true" class="form-control select" name="status" data-live-search="true" value="启用">
                                </input>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">会员姓名</label>
                                <div class="input-group">
                                    <input readOnly="true" name="name" type="text" class="form-control" id="name" value="${member.name}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="mobile">联系电话</label>
                                <div class="input-group">
                                    <input readOnly="true" name="mobile" type="text" class="form-control" id="mobile" value="${member.auth.mobile}">
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>出生日期</label>
                                <div class="input-group">
                                    <input readOnly="true" name="birthday" type="text" class="form-control datepicker"
                                           id="birthday" value="${birthday}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别</label>
                                <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="男">
                                </input>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="email">企业邮箱</label>
                                <div class="input-group">
                                    <input readOnly="true" name="email" type="text" class="form-control" id="email" value="${member.auth.email}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>会员性质</label>
                                <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${member.identityType}">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>注册时间</label>
                                <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${registryTime}">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>会员等级</label>
                                <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${member.level.title}">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>有效消费额</label>
                            <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${member.effectiveConsumption}">
                            </input>
                        </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>有效单量</label>
                            <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${member.effectiveOrderCount}">
                            </input>
                        </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>会员角色</label>
                                <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${member.role.title}">
                                </input>
                            </div>
                        </div>

            <div class="tab-pane" id="tab_1-2">
                <p>Something is interesting</p>
            </div>
        </div>
    </div>
</section>
<script>
    $(function() {
        if (!$global.validateMobile()) {
            $('.select').selectpicker();
        }

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
    });

    function submitForm() {
        $("#add_member").submit();
    }
</script>
</body>