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
    <script src="/javascript/member_edit.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
    <script type="text/javascript">
        function changeStore() {
            var storeId = $("#store").val();
            $.ajax({
                url: "/rest/member/change/store",
                cache: false,
                type: "post",
                dataType: "json",
                data: {"storeId": storeId},
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                success: function (data) {
                    $("#salesConsult").html("");
                    $.each(data.sales_consult_list, function (i, val) {
                        $("#salesConsult").append("<option value='" + val.id + "'>" + val.consultName + "</option>");
                    });
                    $("#salesConsult").selectpicker('refresh');
                }
            });
        }

        function changeSalesConsult() {
            var consultId = $("#salesConsult").val();
            $.ajax({
                url: "/rest/member/change/consult",
                cache: false,
                type: "post",
                dataType: "json",
                data: {"consultId": consultId},
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                success: function (data) {
                    $("#store").html("");
                    $.each(data.all_store_list, function (i, val) {
                        if (val.id == data.storeId) {
                            $("#store").append("<option value='" + val.id + "' selected >" + val.storeName + "</option>");
                        } else {
                            $("#store").append("<option value='" + val.id + "'>" + val.storeName + "</option>");
                        }

                    });
                    $("#store").selectpicker('refresh');
                }
            });
        }
    </script>
</head>
<body>
<section class="content-header">
    <h1>编辑会员信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        <#if member?? && member.id?? >
            <li><a href="#tab_1-2" data-toggle="tab">账户安全</a></li></#if>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="member_edit" action="/views/admin/member/update" method="post">
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
                    <#-- <div class="col-md-6 col-xs-12">
                         <div class="form-group">
                             <label>城市</label>
                             <select class="form-control select" name="city" data-live-search="true" >
                                 <#if city_list??>
                                     <#list city_list as item>
                                         <option <#if item == member.city > selected</#if>>${item}</option>
                                     </#list>
                                 </#if>
                             </select>
                         </div>
                     </div>-->
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>门店</label>
                                <select class="form-control select" name="store" id="store" data-live-search="true"
                                        onchange="changeStore();">
                                <#if store_list??>
                                    <#list store_list as item>
                                        <option value="${item.id}" <#if item ?? && member ?? && item.id == member.store >
                                                selected</#if>>${item.storeName}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>专属导购</label>
                                <select class="form-control select" name="salesConsult" id="salesConsult"
                                        data-live-search="true" onchange="changeSalesConsult();">
                                <#if sales_consult_list??>
                                    <#list sales_consult_list as item>
                                        <option value="${item.id}" <#if item.id == member.salesConsult >
                                                selected</#if>>${item.consultName}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>会员性质</label>
                                <select class="form-control select" name="identityType" data-live-search="true">
                                <#if identityType_list??>
                                    <#list identityType_list as item>
                                        <option value="${item}" <#if item.getValue() == member.identityType.getValue() >
                                                selected</#if>>${item.getValue()}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别</label>
                                <select class="form-control select" name="sex" data-live-search="true">
                                <#if sex_list ??>
                                    <#list sex_list as item>
                                        <option value="${item}" <#if item.getValue() == member.sex.getValue() >
                                                selected</#if>>${item.getValue()}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">会员姓名</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="name"
                                           value="<#if member.name??>${member.name}</#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="mobile">联系电话</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="mobile" type="text" class="form-control" id="mobile"
                                           value="<#if member.mobile??>${member.mobile}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">

                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="birthday">出生日期</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="birthday" type="text"
                                           value="<#if member.birthday??>${member.birthday}</#if>"
                                           class="form-control datepicker"
                                           id="birthday">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="row">
                                <div class="col-md-6 col-xs-12">
                                    <div class="form-group">
                                        <label for="status">是否启用</label>
                                        <br>
                                        <input name="status" id="status" class="switch" type="checkbox"
                                               <#if member.status?? && member.status>checked</#if>>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <#--<div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="email">企业邮箱</label>
                            <div class="input-group">
                                <input name="email" type="text" class="form-control" id="email" value="<#if member.auth.email??>${member.auth.email} </#if>">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                            </div>
                        </div>
                    </div>-->
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
                    <input type="hidden" name="id" id="id" value="${member.id}"/>
                </form>
            </div>
            <div class="tab-pane" id="tab_1-2">
                <form id="passwordFrom">
                    <input type="hidden" name="id" id="id" <#if member?? && member.id??>
                           value="${member.id}"
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
    $(function () {
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