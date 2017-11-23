<head>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="/plugins/tree-multiselect/jquery.tree-multiselect.min.css" rel="stylesheet"/>
    <script src="/plugins/tree-multiselect/jquery.tree-multiselect.min.js"></script>
<#-- <script src="/plugins/jquery/jquery.min.js"></script>
 <script src="/plugins/jquery/jquery-ui.min.js"></script>
 <script src="/plugins/bootstrap/bootstrap.min.js"></script>
 <script src="/plugins/tree-multiselect/jquery.tree-multiselect.min.js"></script>
 <script src="/plugins/doT/doT.js"></script>
 <script src="/javascript/index.js"></script>-->
    <script>
        $(function () {
            $("#dataGrid").on('click', '#btn_auth', function () {
                var username = $(this).parents('tr').find('td').eq(2).text();
                var roleId = $(this).parents('tr').find('td').eq(1).text();
                $.ajax({
                    url: "/rest/role/showResource",    //请求的url地址
                    dataType: "json",   //返回格式为json
                    async: true,//请求是否异步，默认为异步，这也是ajax重要特性
                    data: {
                        "roleId": roleId,
                    },    //参数值
                    type: "POST",   //请求方式
                    beforeSend: function () {
                        //请求前的处理
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            var frontHtml = result.content + '<input type="hidden" id="roleId" value="">';
                            $("#authorityForm").empty().append(frontHtml);
                            $("#authorifyselect").treeMultiselect({searchable: true, hideSidePanel: true});
                            $("#roleId").val(roleId);
                            $("#AuthorityTitle").text("给 " + username + " 分配权限");
                            $("#grantAuthorityModal").modal('show');
                        }
                        if (-1 === result.code) {
                            $notify.danger(message);
                        }

                    },
                    complete: function () {
                        //请求完成的处理
                    },
                    error: function () {
                        //请求出错处理
                        $notify.danger("请求异常，请联系管理员!");
                    }
                });
                /*var html = '<select id="authorifyselect" multiple="multiple">' +
                        '<option  value="monitor_index" data-section="旅游管理" data-description="首页描述" selected>首页</option>' +
                        '<option  value="manage_logs" data-section="旅游管理" data-description="用户日志描述" selected>用户信息</option>' +
                        '<option  value="interface_logs" data-section="旅游管理" data-description="接口调用日志描述" selected>酒店信息</option>' +
                        '<option  value="abnormal_logs" data-section="旅游管理">出行信息</option>' +
                        '<option  value="empty_logs" data-section="旅游管理">景点信息</option>' +
                        '<option  value="monitor_api" data-section="系统监控">用户监控</option>' +
                        '<option  value="monitor_apiback" data-section="系统监控">车辆监控</option>' +
                        '<option  value="monitor_usercert" data-section="系统监控">酒店监控</option>' +
                        '<option  value="monitor_processor" data-section="系统监控">景点监控</option>' +
                        '<option  value="monitor_connector" data-section="系统监控">导游监控</option>' +
                        '<option  value="monitor_agent" data-section="系统监控">旅客监控</option>' +
                        '<option  value="monitor_dispatcher" data-section="系统监控">地铁监控</option>' +
                        '<option  value="monitor_dbreceive" data-section="系统监控">天气监控</option>' +
                        '<option  value="monitor_dbquery" data-section="系统监控">就餐监控</option>' +
                        '<option  value="monitor_backFile" data-section="系统监控">成员监控</option>' +
                        '<option  value="manage_staff" data-section="用户权限">用户管理</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
                        '<option  value="interface_test" data-section="接口测试">开始测试</option>' +
                        '</select>' +
                        '<input type="hidden" id="roleId" value="">';
                $("#authorityForm").empty().append(html);
                $("#authorifyselect").treeMultiselect({searchable: true, hideSidePanel: true});
                $("#roleId").val(roleId);
                $("#AuthorityTitle").text("给 " + username + " 分配权限");
                $("#grantAuthorityModal").modal('show');*/
            });
        });

    </script>
</head>
<body>

<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parentResourceName??>
            <li><a href="javascript:void(0);">${selectedMenu.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>加载中...</h1>
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
                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>
                </div>
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<!--给用户分配角色modal-->
<div class="modal inmodal fade" id="grantAuthorityModal" tabindex="-1" role="dialog" aria-hidden="true"
     data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content animated fadeIn">
            <div class="modal-header btn-primary">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">关闭</span>
                </button>
                <h4 class="modal-title" id="AuthorityTitle"></h4>
            </div>
            <div class="modal-body" id="authorityBody">
                <form action="/" id="authorityForm">

                </form>
            </div>
            <div class="modal-footer ">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="authoritysubmit">提交</button>
            </div>
        </div>
    </div>
</div>
<div id="information" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="user-block">
                    <span class="username" style="margin-left: 0px;">
                        <a id="menuTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="roleDetails" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>角色名称</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>角色描述</b> <a class="pull-right" id="description"></a>
                        </li>
                        <li class="list-group-item">
                            <b>排序号</b> <a class="pull-right" id="seq"></a>
                        </li>
                        <li class="list-group-item">
                            <b>状态</b> <a class="pull-right" id="status"></a>
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
<script>
    $(function () {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/role/page/grid', 'get', true, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'id',
            title: '序号',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'name',
            title: '名称',
            align: 'center',
            valign: 'middle',
            width: '20%',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
            field: 'sortId',
            align: 'center',
            valign: 'middle',
            title: '排序号'
        }, {
            field: 'description',
            align: 'center',
            valign: 'middle',
            width: '20%',
            title: '描述'
        }, {
            field: 'status',
            align: 'center',
            valign: 'middle',
            title: '状态',
            width: '10%',
            formatter: function (value) {
                if (true === value) {
                    return '<span class="label label-primary ">正常</span>'
                } else {
                    return '<span class="label label-danger " >停用</span>'
                }
            }
        }, {
            title: '操作',
            align: 'center',
            valign: 'middle',
            width: '10%',
            formatter: function () {
                return '<button id="btn_auth" type="button" class="btn btn-success"> <span class="glyphicon glyphicon-ok btn-mini" aria-hidden="true"></span> 授权</button>'

            }

        }
        ]);
        $("#authoritysubmit").click(function () {
            var varArray = $("#authorifyselect").val();
            if (null === varArray) {
                varArray = ['-1'];
            }
            var roleId = $("#roleId").val();
            console.log(varArray);
            console.log(roleId);
            $.ajax({
                url: "/rest/role/grant",    //请求的url地址
                dataType: "json",   //返回格式为json
                async: true,//请求是否异步，默认为异步，这也是ajax重要特性
                data: {
                    "id": roleId,
                    "resourceIds": varArray
                },    //参数值
                type: "POST",   //请求方式
                beforeSend: function () {
                    //请求前的处理
                },
                success: function (result) {
                    if (0 === result.code) {
                        $("#grantAuthorityModal").modal('hide');
                        if (null !== result.message) {
                            $notify.success(result.message);
                        }
                    }

                },
                complete: function () {
                    //请求完成的处理
                },
                error: function () {
                    //请求出错处理
                }
            });
        });
        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/role/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function (container) {
            $grid.modify($('#dataGrid'), '/views/admin/role/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#btn_delete').on('click', function () {
            $grid.remove($('#dataGrid'), '/rest/role', 'delete');
        });
    });

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/role/' + id,
                        method: 'GET',
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            if (0 === result.code) {
                                var data = result.content;
                                $('#menuTitle').html("角色详情");

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);

                                if (null === data.description) {
                                    data.description = '-';
                                }
                                $('#description').html(data.description);

                                if (null === data.seq) {
                                    data.seq = '-';
                                }
                                $('#seq').html(data.seq);

                                if (true === data.status) {
                                    data.status = '正常';
                                } else if (false === data.status) {
                                    data.status = '停用';
                                } else {
                                    data.status = '-';
                                }
                                $('#status').html(data.status);

                                $('#information').modal();
                            } else {
                                $notify.danger(result.message);
                            }
                        }
                    })
                }
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }
</script>
</body>