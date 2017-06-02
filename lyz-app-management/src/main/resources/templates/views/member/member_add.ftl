<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet"/>

    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="/javascript/member_add.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
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
            <#if member?? && member.id?? ><li><a href="#tab_1-2" data-toggle="tab">账户安全</a></li></#if>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="member_add" method="post">
                    <input type="hidden" name="id" id="id" <#if employeeDO?? && employeeDO.id??>
                           value="${(employeeDO.id)?c}"
                    <#else>
                           value="0"
                    </#if>/>
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
                                <label>城市
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择会员所在城市/分公司"></i>
                                </label>
                                <select class="form-control select" name="city" id="city" data-live-search="true" >
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
                                <label>门店
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择会员所属门店"></i>
                                </label>
                                <select class="form-control select" name="store" id="store" data-live-search="true">
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
                                <label>专属导购
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择会员所属销售经理"></i>
                                </label>
                                <select class="form-control select" name="seller" id="seller" data-live-search="true" >
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
                                <label>会员性质
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择会员性质"></i>
                                </label>
                                <select class="form-control select" name="identityType" id="identityType" data-live-search="true">
                                    <option disabled selected>请选择会员性质</option>
                                    <option value="MEMBER">会员</option>
                                    <option value="RETAIL">零售</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">会员姓名
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="填写会员姓名（必填）"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="name" placeholder="会员姓名">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="mobile">联系电话
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="填写会员联系电话（必填）"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="mobile" type="text" class="form-control" id="mobile" placeholder="联系电话">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择会员性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="sex" id="sex" data-live-search="true" >
                                   <#-- <option disabled selected>请选择性别</option>-->
                                    <option selected value="MALE">男</option>
                                    <option value="FEMALE">女</option>
                                    <option value="SECRET">保密</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="birthday">出生日期
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="填写会员生日（可不填）"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="birthday" type="text" value="${(birthday?string('yyyy-MM-dd'))!}" class="form-control datepicker"
                                           id="birthday" placeholder="出生日期">
                                </div>
                            </div>
                        </div>
                        <#--<div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="email">会员邮箱
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="填写会员邮箱（可不填）"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="email" type="text" class="form-control" id="email" placeholder="会员邮箱地址">
                                </div>
                            </div>
                        </div>-->
                    </div>
                    <div class="row">

                        <div class="col-md-6 col-xs-12">
                            <div class="row">
                                <div class="col-md-6 col-xs-12">
                                    <div class="form-group">
                                        <label for="status">是否启用</label>
                                        <br>
                                        <input name="status" id="status" class="switch" type="checkbox"
                                               <#if !employeeDO?? || (employeeDO.status?? && employeeDO.status)>checked</#if>>
                                    </div>
                                </div>
                            </div>
                           <#-- <div class="form-group">
                                <label>账号状态
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择会员状态（默认启用）"></i>
                                </label>
                                <select class="form-control select" name="status" id="status" data-live-search="true" >
                                    &lt;#&ndash;<option disabled selected>请选择账号状态</option>&ndash;&gt;
                                    <option value="0" selected>启用</option>
                                    <option value="1">停用</option>
                                </select>
                            </div>-->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8 col-xs-12"></div>
                        <div class="col-md-2 col-xs-12">
                            <button type="submit" class="btn btn-primary footer-btn">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                        <div class="col-md-2 col-xs-12">
                            <button type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 取消
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="tab-pane" id="tab_1-2">
                <form id="passwordFrom">
                    <input type="hidden" name="id" id="id" <#if employeeDO?? && employeeDO.id??>
                           value="${(employeeDO.id)?c}"
                    <#else>
                           value="0"
                    </#if>/>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="password">输入新密码
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="输入一个新的六位数的密码，只能以字母、数字开头，可带有“_”、“.”、“-”"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="password" type="password" class="form-control" id="password"
                                           placeholder="输入新密码">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="password2">重新输入新密码
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="重新输入新密码"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="password2" type="password" class="form-control" id="password2"
                                           placeholder="重新输入新密码">
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
                </form>
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