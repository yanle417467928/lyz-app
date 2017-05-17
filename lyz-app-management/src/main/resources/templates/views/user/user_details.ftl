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
        <li><a href="/views/admin/member/page">会员列表</a></li>
        <li class="active">会员信息详情</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">详细信息</a></li>
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
                                <input  readOnly="true" class="form-control select" name="city" data-live-search="true" value="${member.city}">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label> 归属门店</label>
                                <input readOnly="true" class="form-control select" name="storeName" data-live-search="true" value="${member.store.name}">
                                </input>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>专属导购</label>
                                <input readOnly="true" class="form-control select" name="managerName" data-live-search="true" value="${member.manager.name}">
                                </input>
                            </div>
                            </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>导购电话</label>
                                <input readOnly="true" class="form-control select" name="managerMobile" data-live-search="true" value="${member.manager.mobile}">
                                </input>
                            </div>
                        </div>
                    </div>



                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>会员姓名</label>
                            <input  readOnly="true" class="form-control select" name="name" data-live-search="true" value="${member.name}">
                            </input>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label> 联系电话</label>
                            <input readOnly="true" class="form-control select" name="mobile" data-live-search="true" value="${member.auth.mobile}">
                            </input>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>会员生日</label>
                            <input readOnly="true" class="form-control select" name="birthday" data-live-search="true" value="${birthday}">
                            </input>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>性别</label>
                            <input readOnly="true" class="form-control select" name="sex" data-live-search="true" value="${sex}">
                            </input>
                        </div>
                    </div>
                </div>




                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>企业邮箱</label>
                            <input  readOnly="true" class="form-control select" name="email" data-live-search="true" value="${member.auth.email}">
                            </input>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label> 会员性质</label>
                            <input readOnly="true" class="form-control select" name="identityType" data-live-search="true" value="${identityType}">
                            </input>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>注册时间</label>
                            <input readOnly="true" class="form-control select" name="registryTime" data-live-search="true" value="${registryTime}">
                            </input>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>有效消费额</label>
                            <input  readOnly="true" class="form-control select" name="effectiveConsumption" data-live-search="true" value="${member.effectiveConsumption}">
                            </input>
                        </div>
                    </div>
                </div>




                <div class="row">
                    <div class="col-md-6 col-xs-12">
                    <div class="form-group">
                        <label> 有效单量</label>
                        <input readOnly="true" class="form-control select" name="effectiveOrderCount" data-live-search="true" value="${member.effectiveOrderCount}">
                        </input>
                    </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label>账户状态</label>
                            <input readOnly="true" class="form-control select" name="status" data-live-search="true" value="${status}">
                            </input>
                        </div>
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