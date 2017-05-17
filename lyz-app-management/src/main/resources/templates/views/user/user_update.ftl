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
    <h1>修改会员信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">

            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-2" data-toggle="tab">账户安全</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="add_user" action="/views/admin/member/update" method="post">
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
                                    <option disabled selected>${member.city}</option>
                                    <option <#if member.city?? && member.city=="郑州分公司">selected="selected"</#if>>郑州分公司</option>
                                    <option <#if member.city?? && member.city=="成都分公司">selected="selected"</#if>>成都分公司</option>
                                    <option <#if member.city?? && member.city=="重庆分公司">selected="selected"</#if>>重庆分公司</option>
                                    <option <#if member.city?? && member.city=="西安分公司">selected="selected"</#if>>西安分公司</option>
                                    <option <#if member.city?? && member.city=="太原分公司">selected="selected"</#if>>太原分公司</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>门店</label>
                                <select class="form-control select" name="store" data-live-search="true">
                                    <option disabled selected>${member.store.name}</option>
                                    <option <#if member.store.name?? && member.store.name=="富之源">selected="selected"</#if>>富之源</option>
                                    <option <#if member.store.name?? && member.store.name=="富之美">selected="selected"</#if>>富之美</option>
                                    <option <#if member.store.name?? && member.store.name=="润彩店">selected="selected"</#if>>润彩店</option>
                                    <option <#if member.store.name?? && member.store.name=="美丽店">selected="selected"</#if>>美丽店</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>专属导购</label>
                                <select class="form-control select" name="seller" data-live-search="true" >
                                   <#-- <#if seller_list??>
                                        <#list seller_list as item>
                                            <option value="${item!''}" <#if item?? && item=="${member.manager.name}">selected="selected"</#if>>${item}</option>
                                        </#list>
                                     </#if>-->

                                    <option <#if member.manager.name?? && member.manager.name=="杨平">selected="selected"</#if>>杨平</option>
                                    <option <#if member.manager.name?? && member.manager.name=="刘申芳">selected="selected"</#if>>刘申芳</option>
                                    <option <#if member.manager.name?? && member.manager.name=="李秀琳">selected="selected"</#if>>李秀琳</option>
                                    <option <#if member.manager.name?? && member.manager.name=="程静">selected="selected"</#if>>程静</option>
                                    <option <#if member.manager.name?? && member.manager.name=="刘洁">selected="selected"</#if>>刘洁</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>会员性质</label>
                                <select class="form-control select" name="identityType" data-live-search="true">
                                    <option <#if identityType?? && identityType=="会员">selected="selected"</#if>>会员</option>
                                    <option <#if identityType?? && identityType=="零售">selected="selected"</#if>>零售</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">会员姓名</label>
                                <div class="input-group">
                                    <input name="name" type="text" class="form-control" id="name" value="${member.name}">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="mobile">联系电话</label>
                                <div class="input-group">
                                    <input name="mobile" type="text" class="form-control" id="mobile" value="${member.auth.mobile}">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别</label>
                                <select class="form-control select" name="sex" data-live-search="true">
                                    <#--<option disabled selected></option>-->
                                    <option <#if sex?? && sex=="男">selected="selected"</#if>>男</option>
                                    <option <#if sex?? && sex=="女">selected="selected"</#if>>女</option>
                                    <option <#if sex?? && sex=="保密">selected="selected"</#if>>保密</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="email">企业邮箱</label>
                                <div class="input-group">
                                    <input name="email" type="text" class="form-control" id="email" value="${member.auth.email}">
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
                                    <input name="birthday" type="text" value="${birthday}" class="form-control datepicker"
                                           id="birthday">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>账号状态</label>
                                <select class="form-control select" name="usable" data-live-search="true" >
                                    <option value="0" <#if status?? && status=="启动">selected="selected"</#if>>启动<option>
                                    <option value="1" <#if status?? && status=="停用">selected="selected"</#if>>停用</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2 col-xs-12 col-md-offset-10">
                            <button type="button" class="btn btn-block btn-primary" onclick="submitForm();">
                                <i class="fa fa-check"></i> 修改
                            </button>
                        </div>
                    </div>
                    <input type="hidden" name="id" value="${member.id}"/>
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