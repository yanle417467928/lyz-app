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
                <div id="toolbar" class="form-inline">
                    <button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>
                    <select name="identityType" id="identityType" class="form-control select" style="width:auto;"
                            onchange="findUserByCondition()">
                        <option value="-1">选择类型</option>
                        <option value="1">管理员</option>
                        <option value="2">普通用户</option>
                    </select>
                    <select name="enabled" id="enabled" class="form-control select" style="width:auto;"
                            data-live-search="true"
                            onchange="findUserByCondition()">
                        <option value="-1">选择状态</option>
                        <option value="1">正常</option>
                        <option value="0">停用</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryInfo" id="queryInfo" class="form-control"
                               style="width:auto;"
                               placeholder="请输入登录名或姓名.." onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findUserByName()">查找</button>
                        </span>
                    </div>
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
                            <b>用户姓名</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>登录名</b> <a class="pull-right" id="loginName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>性别</b> <a class="pull-right" id="sex"></a>
                        </li>
                        <li class="list-group-item">
                            <b>年龄</b> <a class="pull-right" id="age"></a>
                        </li>
                        <li class="list-group-item">
                            <b>用户类型</b> <a class="pull-right" id="userType"></a>
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

        initDateGird('/rest/user/page/grid');

        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/user/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/user/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#btn_delete').on('click', function () {
            $grid.remove($('#dataGrid'), '/rest/user', 'delete');
        });
    });


    function initDateGird(url) {
        $grid.init($('#dataGrid'), $('#toolbar'),url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
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
                'click .scan': function (e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
            field: 'name',
            title: '姓名',
            align: 'center'
        }, {
            field: 'createTime',
            title: '创建时间',
            align: 'center',
            formatter: function (value) {
                return formatDateTime(value);
            }
        }, {
            field: 'sex',
            align: 'center',
            title: '性别',
            formatter: function (value) {
                if ("MALE" === value) {
                    return '<span class="label label-success">男</span>'
                } else if ("FEMALE" == value) {
                    return '<span class="label label-danger">女</span>'
                } else {
                    return '<span class="label label-default">-</span>'
                }
            },
        }, /*{
            field: 'age',
            align: 'center',
            title: '年龄'
        },{
            field: 'phone',
            align: 'center',
            title: '电话'
        },*//*{
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
                field: 'rolesList',
                align: 'center',
                title: '角色',
                sortable: true,
                formatter: function (value, row) {
                    var roles = [];
                    for (var p in value) {
                        roles.push(value[p].name);
                    }
                    return (roles.join('\n'));
                }
            }, {
                field: 'userType',
                title: '用户类型',
                formatter: function (value) {
                    if (1 === value) {
                        return '<span class="label label-primary">管理员</span>'
                    } else {
                        return '<span class="label label-success">普通用户</span>'
                    }
                },
                align: 'center'
            }, {
                field: 'status',
                align: 'center',
                title: '状态',
                formatter: function (value) {
                    if (true === value) {
                        return '<span class="label label-primary">正常</span>'
                    } else if (false === value) {
                        return '<span class="label label-danger">停用</span>'
                    } else {
                        return '<span class="label label-danger">-</span>'
                    }
                }
            }
        ]);
    }

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/user/' + id,
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
                                $('#menuTitle').html("用户详情");

                                $('#id').html(data.id);

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);
                                if (null === data.loginName) {
                                    data.loginName = '-';
                                }
                                $('#loginName').html(data.loginName);

                                if (null === data.sex) {
                                    data.sex = '-';
                                } else if (data.sex === "MALE") {
                                    data.sex = '男';
                                } else if (data.sex === "FEMALE") {
                                    data.sex = '女';
                                }
                                $('#sex').html(data.sex);
                                if (null === data.age) {
                                    data.age = '-';
                                }
                                $('#age').html(data.age);

                                if (data.userType === 1) {
                                    $('#userType').html("超级管理员");
                                } else if (data.resourceType === 2) {
                                    $('#userType').html("普通用户");
                                } else {
                                    $('#userType').html("-");
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
            close: function () {
                $('#information').modal('hide');
            }
        }
    }

    function findBykey() {
        if (event.keyCode == 13) {
            findUserByName();
        }
    }
    
    function findUserByName() {
        var keywords = $('#queryInfo').val();
        $('#identityType').val('-1');
        $('#enabled').val('-1');
        var url = '/rest/user/page/grid?keywords='+keywords;
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird(url);
    }

    function findUserByCondition(){
        $("#dataGrid").bootstrapTable('destroy');
        var identityType = $('#identityType').val();
        var enable = $('#enabled').val();
        $('#queryInfo').val('');
        var url = '/rest/user/page/grid?identityType='+identityType+'&enable='+enable
        initDateGird(url);
    }

    var formatDateTime = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = dt.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = dt.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        var second = dt.getSeconds();
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
    };
</script>
</body>