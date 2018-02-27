<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script type="text/javascript" src="/javascript/user/user_add.js?v=20180226"></script>
    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
</head>
<body>

<section class="content-header">
    <h1>新增用户</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="user_add">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    登录名称
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中英文字符，长度在2~20之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="loginName" type="text" class="form-control" id="loginName"
                                           placeholder="登录名称">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    用户姓名
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中英文，长度在2~20之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="name"
                                           placeholder="用户姓名">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>性别
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择用户性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="sex" id="sex" data-live-search="true">
                                    <option value="MALE">男</option>
                                    <option value="FEMALE">女</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="userType">
                                    用户类型
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="请选择用户类型"></i>
                                </label>
                                <select id="userType" name="userType" class="form-control select"
                                        data-live-search="true">
                                    <option value=2>普通用户</option>
                                    <option value=1>超级管理员</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="type">
                                    用户密码
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="请填写用户密码，如不填，默认123456"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="password" type="text" class="form-control" id="password"
                                           placeholder="用户密码">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <div class="form-group">
                                    <label for="role">用户角色</label>
                                    <select class="form-control select2" multiple="multiple"
                                            data-placeholder="请为用户分配角色" style="width: 100%;" id="role" name="role">
                                    <#if roleList??>
                                        <#list roleList as item>
                                            <option value="${item.id}">${item.name}</option>
                                        </#list>
                                    </#if>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                    <#--<div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="type">
                                用户密码
                                <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                   data-content="请填写用户密码，如不填，默认123456"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="password" type="text" class="form-control" id="password"
                                       placeholder="用户密码">
                            </div>
                        </div>
                    </div>-->
                        <div class="row">
                            <div class="col-md-6 col-xs-12">
                                <div class="form-group" style="margin-left: 2%">
                                    <label for="status">是否启用</label>
                                    <br>
                                    <input name="status" class="switch" id="status" type="checkbox" checked
                                           data-on-text="启用" data-off-text="停用"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <label for="title">
                            </label>
                            <div class="box box-success ">
                                <div class="box-header with-border">
                                    <h3 class="box-title">选择管辖门店</h3>
                                    <div class="box-tools pull-right">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openCityModal('citys')">
                                            选择城市
                                        </button>
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openStoreModal('stores')">
                                            选择门店
                                        </button>
                                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                </div>
                                <div class="box-body">
                                    <div id="citys" style="margin-top: 10px; width: 85%;word-wrap:break-word;">
                                    </div>
                                    <br>
                                    <div id="stores" style="margin-top: 10px; width: 85%;word-wrap:break-word;">
                                    </div>
                                </div>
                                <!-- /.box-body -->
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

                <!-- 城市选择框 -->
                <div id="cityModal" class="modal fade" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document" style="width: 60%">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4>选择城市<span style="color: red; font-size: small">（注：如果选择城市，则该城市下的所有门店的数据都可查询）</span></h4>
                            </div>
                            <div class="modal-body">
                                <!--  设置这个div的大小，超出部分显示滚动条 -->
                                <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                    <section class="content">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="box box-primary">
                                                    <div id="toolbar2" class="form-inline">
                                                        <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                                            <input type="text" name="queryCityInfo" id="queryCityInfo" class="form-control" style="width:auto;"
                                                                   placeholder="请输入要查找的城市名或编码..">
                                                            <span class="input-group-btn">
                                                                <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                                                        onclick="return findCityByNameOrCode()">查找</button>
                                                            </span>
                                                        </div>
                                                    </div>
                                                    <div class="box-body table-reponsive">
                                                        <table id="cityDataGrid"
                                                               class="table table-bordered table-hover">

                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="cityModalConfirm" type="button" class="btn btn-primary">确定</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 门店选择框 -->
                <div id="storeModal" class="modal fade" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document" style="width: 60%">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4>选择门店</h4>
                            </div>
                            <div class="modal-body">
                                <!--  设置这个div的大小，超出部分显示滚动条 -->
                                <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                    <section class="content">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="box box-primary">
                                                    <div id="toolbar1" class="form-inline">
                                                        <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                                                                onchange="findStoreByCity(this.value)">
                                                            <option value="-1">选择城市</option>
                                                        </select>
                                                        <select name="storeType" id="storeType" class="form-control select" style="width:auto;"
                                                                onchange="findStoreByStoreType(this.value)">
                                                            <option value="-1">选择门店类型</option>
                                                        <#if storeTypes??>
                                                            <#list storeTypes as storeType>
                                                                <option value="${storeType.value}">${storeType.description}</option>
                                                            </#list>
                                                        </#if>
                                                        </select>

                                                        <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                                            <input type="text" name="queryStoreInfo" id="queryStoreInfo" class="form-control" style="width:auto;"
                                                                   placeholder="请输入要查找的店名或编码..">
                                                            <span class="input-group-btn">
                                                                <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                                                        onclick="return findStoreByNameOrCode()">查找</button>
                                                            </span>
                                                        </div>
                                                    </div>
                                                    <div class="box-body table-reponsive">
                                                        <table id="storeDataGrid"
                                                               class="table table-bordered table-hover">

                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="storeModalConfirm" type="button" class="btn btn-primary">确定</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

</body>