<head>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">

    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script type="text/javascript" src="/javascript/activity/activity_page.js"></script>
</head>
<body>

<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parent??>
            <li><a href="javascript:void(0);">${selectedMenu.parent.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>促销管理</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="btn-group">
                    <button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_copy" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-copy" aria-hidden="true"></span> 复制
                    </button>

                    <button id="btn_publish" type="button" class="btn btn-default" style="margin-left: 10px;">
                        <span class="glyphicon glyphicon-cloud" aria-hidden="true"></span> 发布
                    </button>
                    <button id="btn_delete" type="button" class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 失效
                    </button>

                    <select name="status" id="status" class="form-control select" style="width:auto;margin-left: 10px;"
                            onchange="statusChange(this.value)" >
                        <option value="">选择状态</option>
                        <option value="NEW">新建</option>
                        <option value="PUBLISH">已发布</option>
                    </select>
                </div>
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<div id="information" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="user-block">
                    <span class="username" style="margin-left: 0px;">
                        <a id="companyTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="resourceDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>城市</b> <a class="pull-right" id="city"></a>
                        </li>
                        <li class="list-group-item">
                            <b>促销编码</b> <a class="pull-right" id="actCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>促销名称</b> <a class="pull-right" id="title"></a>
                        </li>
                        <li class="list-group-item">
                            <b>范围</b> <a class="pull-right" id="scope"></a>
                        </li>
                        <li class="list-group-item">
                            <b>促销类型</b> <a class="pull-right" id="type"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>目标对象</b> <a class="pull-right" id="target"></a>
                        </li>
                        <li class="list-group-item">
                            <b>状态</b> <a class="pull-right" id="status"></a>
                        </li>
                        <li class="list-group-item">
                            <b>排序号</b> <a class="pull-right" id="sortId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>专供等级</b> <a class="pull-right" id="rankCode"></a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>


</body>