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
    <h1>新增会员</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">

            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-2" data-toggle="tab">账户安全</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="add_user" action="/views/admin/member/add" method="post">
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="user-panel">
                                <div class="pull-left image">
                                    <img src="/images/user2-160x160.jpg" class="img-circle"
                                         style="max-width: 70px;" alt="User Image">
                                </div>
                                <div class="pull-left info"
                                     style="padding: 10px 5px 5px 15px; left: 90px;">
                                    <p style="color:black;">Alexander Pierce</p>
                                    <a class="btn btn-primary btn-xs" href="#">
                                        <i class="fa fa-cloud-upload"></i> 上传头像
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>城市</label>
                                <select class="form-control select" name="city" data-live-search="true" >
                                    <option disabled selected>请选择分公司</option>
                                    <option>郑州分公司</option>
                                    <option>成都分公司</option>
                                    <option>重庆分公司</option>
                                    <option>西安分公司</option>
                                    <option>太原分公司</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>门店</label>
                                <select class="form-control select" name="store" data-live-search="true">
                                    <option disabled selected>请选择门店</option>
                                    <option>富之源</option>
                                    <option>富之美</option>
                                    <option>润彩店</option>
                                    <option>美丽店</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>专属导购</label>
                                <select class="form-control select" name="seller" data-live-search="true" >
                                    <option disabled selected>请选择专属导购</option>
                                    <option>杨平</option>
                                    <option>刘申芳</option>
                                    <option>李秀琳</option>
                                    <option>程静</option>
                                    <option>刘洁</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>会员性质</label>
                                <select class="form-control select" name="identityType" data-live-search="true">
                                    <option disabled selected>请选择会员性质</option>
                                    <option>会员</option>
                                    <option>零售</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">会员姓名</label>
                                <div class="input-group">
                                    <input name="name" type="text" class="form-control" id="name" placeholder="会员姓名">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="mobile">联系电话</label>
                                <div class="input-group">
                                    <input name="mobile" type="text" class="form-control" id="mobile" placeholder="联系电话">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别</label>
                                <select class="form-control select" name="sex" data-live-search="true" >
                                    <option disabled selected>请选择性别</option>
                                    <option>男</option>
                                    <option>女</option>
                                    <option>保密</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="email">企业邮箱</label>
                                <div class="input-group">
                                    <input name="email" type="text" class="form-control" id="email" placeholder="企业邮箱">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="birthday">出生日期</label>
                                <div class="input-group">
                                    <input name="birthday" type="text" value="${(birthday?string('yyyy-MM-dd'))!}" class="form-control datepicker"
                                           id="birthday" placeholder="出生日期">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>账号状态</label>
                                <select class="form-control select" name="status" data-live-search="true" >
                                    <option disabled selected>请选择账号状态</option>
                                    <option value="0">启用</option>
                                    <option value="1">停用</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2 col-xs-12 col-md-offset-10">
                            <button type="button" class="btn btn-block btn-primary" onclick="submitForm();">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                    </div>
                </form>
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
        $("#add_user").submit();
    }
</script>
</body>