<head>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
</head>
<body>

<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.title!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parent??>
            <li><a href="javascript:void(0);">${selectedMenu.parent.title!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.title!'??'}</li>
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
                    <ul id="resourceDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>序列号</b> <a class="pull-right" id="id"></a>
                        </li>
                        <li class="list-group-item">
                            <b>资源名称</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>资源描述</b> <a class="pull-right" id="description"></a>
                        </li>
                        <li class="list-group-item">
                            <b>资源路径</b> <a class="pull-right" id="url"></a>
                        </li>
                        <li class="list-group-item">
                            <b>图标</b> <a class="pull-right" id="icon"></a>
                        </li>
                        <li class="list-group-item">
                            <b>资源类型</b> <a class="pull-right" id="resourceType"></a>
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
    $(function() {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/user/page/grid', 'get', true, function(params) {
            return {
                offset: params.offset ,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'id',
            title: '序号',
            align: 'center'
        }, {
            field: 'loginName',
            title: '登录名',
            align: 'center',
            events: {
                'click .scan': function(e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function(value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        },{
            field: 'name',
            title: '姓名',
            align: 'center'
        },{
            field: 'createTime',
            title: '创建时间',
            align: 'center',
            formatter: function(value) {
            return formatDateTime(value);
        }
        },{
            field: 'sex',
            align: 'center',
            title: '性别',
            formatter: function(value) {
                if (0 === value) {
                    return '<span class="label label-success">男</span>'
                } else {
                    return '<span class="label label-danger">女</span>'
                }
            },
        },{
            field: 'age',
            align: 'center',
            title: '年龄'
        },{
            field: 'phone',
            align: 'center',
            title: '电话'
        },/*{
            field: 'resourceType',
            title: '资源类型',
            formatter: function(value) {
                if (0 === value) {
                    return '<i class="fa fa-list text-primary">&nbsp &nbsp<span class="label label-primary">菜单</span>'
                } else {
                    return '<i class="fa fa-hand-pointer-o text-primary">&nbsp &nbsp<span class="label label-success">按钮</span>'
                }
            },
            align: 'center'
        }, {
            field: 'status',
            align: 'center',
            title: '状态',
            formatter: function(value) {
                if (true === value) {
                    return '<span class="label label-primary">正常</span>'
                } else if(false===value) {
                    return '<span class="label label-danger">停用</span>'
                }else{
                    return '<span class="label label-danger">-</span>'
                }
            }*/
            {
                field: 'roleList',
                align: 'center',
                title: '角色',
                sortable: true,
                formatter : function(value, row, index) {
                    var roles = [];
                    for(var p in value) {
                        roles.push(value[p].name);
                    }
                    return(roles.join('\n'));
                }
            },{
                field: 'userType',
                title: '用户类型',
                formatter: function(value) {
                    if (0 === value) {
                        return '<span class="label label-primary">管理员</span>'
                    } else {
                        return '<span class="label label-success">用户</span>'
                    }
                },
                align: 'center'
            },{
                field: 'status',
                align: 'center',
                title: '状态',
                formatter: function(value) {
                    if (true === value) {
                        return '<span class="label label-primary">正常</span>'
                    } else if(false===value) {
                        return '<span class="label label-danger">停用</span>'
                    }else{
                        return '<span class="label label-danger">-</span>'
                    }
                }
            }
        ]);

        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/resource/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function() {
            $grid.modify($('#dataGrid'), '/views/admin/resource/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#btn_delete').on('click', function() {
            $grid.remove($('#dataGrid'), '/rest/resource', 'delete');
        });
    });

    var $page = {
        information: {
            show: function(id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/resource/' + id,
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
                                $('#menuTitle').html("资源详情");

                                $('#id').html(data.id);

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);
                                if (null === data.description) {
                                    data.description = '-';
                                }
                                $('#description').html(data.description);

                                if (null === data.url) {
                                    data.url = '-';
                                }
                                $('#url').html(data.url);
                                if (null === data.icon) {
                                    data.icon = '-';
                                }
                                $('#icon').html('<i class="' + data.icon + '"></i>');

                                if (data.resourceType===0) {
                                    $('#resourceType').html("菜单");
                                }else if(data.resourceType===1){
                                    $('#resourceType').html("资源");
                                }else{
                                    $('#resourceType').html("-");
                                }
                                if (true === data.status) {
                                    $('#status').html('<span class="label label-primary">正常</span>');
                                } else if (false === data.status) {
                                    $('#status').html('<span class="label label-danger">停用</span>');
                                } else {
                                    $('#status').html('-');
                                }
                                $('#information').modal();
                            } else {
                                $notify.danger(result.message);
                            }
                        }
                    })
                }
            },
            close: function() {
                $('#information').modal('hide');
            }
        }
    }
    var formatDateTime = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = dt.getHours();
        h=h < 10 ? ('0' + h) : h;
        var minute = dt.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        var second=dt.getSeconds();
        second=second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
    };
</script>
</body>