<head>
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="/plugins/iCheck/all.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/plugins/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script type="text/javascript" charset="utf-8" src="/mag/js/kindeditor-min.js"></script>

    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script src="/plugins/iCheck/icheck.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.js"></script>
    <script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script type="text/javascript" src="/javascript/message/message_add.js"></script>

</head>
<style>
    #stores label {
        white-space: normal !important;
        margin: 5px !important;
        display: inline-block !important;
    }
</style>
<body>
<section class="content-header">
    <h1>新增消息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="activity_form">


                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    消息标题
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="title" type="text" class="form-control" id="title"
                                           placeholder="消息标题">
                                </div>
                            </div>
                        </div>
                            <div class="col-xs-12 col-md-2">
                                <label for="title">
                                    消息类型
                                </label>
                                <select name="messageType" id="messageType" class="form-control select">
                                    <option value="TZ">通知</option>
                                    <option value="YH">优惠</option>
                                    <option value="JG">警告</option>
                                </select>
                            </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    开始日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="beginTime" type="text" class="form-control" id="beginTime"
                                           readonly placeholder="开始日期">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    结束日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="endTime" type="text" class="form-control" id="endTime"
                                           readonly placeholder="结束日期">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    推送范围
                                </label>
                                <div class="input-group">
                                    <input name="target" value="ALL" type="radio" class="flat-red">全部
                                    <input name="target" value="ZDY" type="radio" class="flat-red">自定义
                                </div>
                            </div>
                        </div>

                    </div>


            </div>
            <div class="row">
                <div class="col-xs-12 col-md-6">
                    <div class="form-group">
                        <label for="title">
                            身份类型
                        </label>
                        <div class="input-group">
                            <input name="identityType" value="SELLER" type="checkbox" class="flat-red">导购
                            <input name="identityType" value="DELIVERY_CLERK" type="checkbox" class="flat-red">配送员
                            <input name="identityType" value="DECORATE_MANAGER" type="checkbox" class="flat-red">装饰公司经理
                            <input name="identityType" value="DECORATE_EMPLOYEE" type="checkbox" class="flat-red">装饰公司员工
                            <input name="identityType" value="CUSTOMER" type="checkbox" class="flat-red">会员
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
                                    <h3 class="box-title">选择员工</h3>
                                    <div class="box-tools pull-right">
                                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                    <!-- /.box-tools -->
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-2">
                                            <button type="button" class="btn btn-primary btn-xs"
                                                    onclick="openEmployeesModal('employees')">选择员工
                                            </button>
                                        </div>
                                    </div>

                                    <div id="employees" style="margin-top: 10px;">

                                    </div>
                                </div>
                                <!-- /.box-body -->
                            </div>
                        </div>
                    </div>




                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <label for="title">
                            </label>
                            <div class="box box-success ">
                                <div class="box-header with-border">
                                    <h3 class="box-title">选择会员</h3>
                                    <div class="box-tools pull-right">
                                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                    <!-- /.box-tools -->
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-2">
                                            <button type="button" class="btn btn-primary btn-xs"
                                                    onclick="openPeopleModal('people')">选择会员
                                            </button>
                                        </div>
                                    </div>

                                    <div id="people" style="margin-top: 10px;">

                                    </div>
                                </div>
                                <!-- /.box-body -->
                            </div>
                        </div>
                    </div>


                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <label for="title">
                            </label>
                            <div class="box box-success ">
                                <div class="box-header with-border">
                                    <h3 class="box-title">选择门店</h3>
                                    <div class="box-tools pull-right">
                                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                    <!-- /.box-tools -->
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-2">
                                            <button type="button" class="btn btn-primary btn-xs"
                                                    onclick="openStoreModal('stores')">选择门店
                                            </button>
                                        </div>
                                    </div>

                                    <div id="stores" style="margin-top: 10px;">

                                    </div>
                                </div>
                                <!-- /.box-body -->
                            </div>
                        </div>
                    </div>




                    <div>
                        <div class="row">
                            <div class="col-md-12 col-xs-12">
                                <label for="title">
                                    消息详情
                                </label>
                                <div class="form-group">
                                    <div>
                                    <textarea name="detailed"
                                              class="editor"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


            <!-- 员工选择框 -->
            <div id="employeesModal" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 60%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>选择员工</h4>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="selectPeople" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-primary">
                                                <div id="toolbar3" class="form-inline">

                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative">
                                                        <input type="text" name="queryEmployeesInfo"
                                                               id="queryEmployeesInfo" class="form-control"
                                                               style="width:auto;"
                                                               placeholder="请输入员工姓名或电话..">
                                                        <span class="input-group-btn">
                                                                <button type="button" name="search" id="search-btn"
                                                                        class="btn btn-info btn-search"
                                                                        onclick="findEmployees()">查找</button>
                                                            </span>
                                                    </div>
                                                </div>
                                                <div class="box-body table-reponsive">
                                                    <table id="EmployeesDataGrid"
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
                            <button id="employeesModalConfirm" type="button" class="btn btn-primary">确定</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                </div>
            </div>


                    <!-- 会员选择框 -->
                    <div id="peopleModal" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document" style="width: 60%">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4>选择会员</h4>
                                </div>
                                <div class="modal-body">
                                    <!--  设置这个div的大小，超出部分显示滚动条 -->
                                    <div id="selectPeople" class="ztree" style="height: 60%;overflow:auto; ">
                                        <section class="content">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="box box-primary">
                                                        <div id="toolbar1" class="form-inline">

                                                            <div class="input-group col-md-3"
                                                                 style="margin-top:0px positon:relative">
                                                                <input type="text" name="queryPeopleInfo"
                                                                       id="queryPeopleInfo" class="form-control"
                                                                       style="width:auto;"
                                                                       placeholder="请输入会员姓名或电话..">
                                                                <span class="input-group-btn">
                                                                <button type="button" name="search" id="search-btn"
                                                                        class="btn btn-info btn-search"
                                                                        onclick="findPeople()">查找</button>
                                                            </span>
                                                            </div>
                                                        </div>
                                                        <div class="box-body table-reponsive">
                                                            <table id="peopleDataGrid"
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
                                    <button id="peopleModalConfirm" type="button" class="btn btn-primary">确定</button>
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
                                                        <div id="toolbar2" class="form-inline">
                                                            <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                                                                    onchange="findStoreByCity(this.value)" data-live-search="true">
                                                                <option value="-1">选择城市</option>
                                                            </select>
                                                            <select name="storeType" id="storeType" class="form-control selectpicker" data-width="120px" style="width:auto;"
                                                                    onchange="findStoreByStoreType(this.value)" data-live-search="true">
                                                                <option value="-1">选择门店类型</option>
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


                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-2">
                            <button type="submit" class="btn btn-primary footer-btn">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button id="btn-cancel" type="button" class="btn btn-danger footer-btn btn-cancel">
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



</script>
</body>
