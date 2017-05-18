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
    <h1>新增菜单</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">

            <li class="active"><a href="#tab_1-1" data-toggle="tab">菜单信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="add_user" action="/views/admin/menu/add" method="post">

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>菜单标题</label>
                                <div class="input-group">
                                    <input name="title" type="text" class="form-control" id="title" placeholder="请输入菜单标题">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>图标样式</label>
                                <div class="input-group">
                                    <input name="iconStyle" type="text" class="form-control" id="iconStyle" placeholder="请输入图标样式">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>链接地址</label>
                                <div class="input-group">
                                    <input name="linkUri" type="text" class="form-control" id="linkUri" placeholder="请输入链接地址">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>排序号</label>
                                <div class="input-group">
                                    <input name="sortId" type="text" class="form-control" id="sortId" placeholder="请输入排序号">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="name">菜单类型</label>
                                <select class="form-control select" name="type" data-live-search="true" onchange="checkMenuType(this.value);" >
                                <option disabled selected>请选择菜单类型</option>
                                <option value="PARENT">一级菜单</option>
                                <option value="CHILD">二级菜单</option>
                            </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="mobile">相关数据表</label>
                                <div class="input-group">
                                    <input name="referenceTable" type="text" class="form-control" id="referenceTable" placeholder="请输入相关数据表">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row" id="parent_info">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>父节点ID</label>
                                <div class="input-group">
                                    <input name="parentID" type="text" class="form-control" id="parentID" placeholder="请输入父节点ID">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="email">父节点标题</label>
                                <div class="input-group">
                                    <input name="parentTitle" type="text" class="form-control" id="parentTitle" placeholder="请输入父节点标题">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                </div>
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

    function checkMenuType(menuType){
        if (menuType == "CHILD") {
            $('#parent_info').show()

        }else if (menuType == "PARENT"){
            $('#parent_info').hide()

        }
    }
</script>
</body>