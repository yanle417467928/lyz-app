<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
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
                <#--<button id="btn_delete" type="button" class="btn btn-default">
                    <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                </button>-->
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
                    <img id="memberHeadImage" class="img-circle img-bordered-sm" src="" alt="用户头像">
                    <span class="username">
                        <a id="memberName" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <span id="identityType" class="description"></span>
                    <ul id="memberAttributes" class="list-group list-group-unbordered" style="margin-top:10px;">
                    <#-- <li class="list-group-item">
                         <b>所在城市</b> <a class="pull-right" id="city"></a>
                     </li>-->
                        <li class="list-group-item">
                            <b>归属门店</b> <span class="pull-right" id="store"></span>
                        </li>
                        <li class="list-group-item">
                            <b>服务导购</b> <span class="pull-right" id="salesConsult"></span>
                        </li>
                        <li class="list-group-item">
                            <b>联系电话</b> <span class="pull-right" id="mobile"></span>
                        </li>
                        <li class="list-group-item">
                            <b>出生日期</b> <span class="pull-right" id="birthday"></span>
                        </li>
                        <li class="list-group-item">
                            <b>性别</b> <span class="pull-right" id="sex"></span>
                        </li>
                        <li class="list-group-item">
                            <b>状态</b> <span class="pull-right" id="status"></span>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/member/page/grid', 'get', true, function (params) {
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
            title: 'ID',
            align: 'center'
        }, {
            field: 'name',
            title: '姓名',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function (value, row) {
                var id = row.id;
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
            field: 'mobile',
            title: '电话',
            align: 'center',
            width: '20%'
        }, {
            field: 'storeName',
            title: '门店',
            align: 'center'
        }, {
            field: 'salesConsultName',
            title: '导购',
            align: 'center'
        }, {
            field: 'identityType',
            title: '会员身份',
            align: 'center',
            formatter: function (value) {
                if ('MEMBER' === value) {
                    return '<span class="label label-success">会员</span>';
                } else {
                    return '<span class="label label-danger">零售</span>';
                }
            }
        }, {
            field: 'status',
            title: '会员状态',
            align: 'center',
            formatter: function (value) {
                if (true === value) {
                    return '<span class="label label-success">生效</span>';
                } else {
                    return '<span class="label label-danger">失效</span>';
                }
            }
        }
        ]);

        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/member/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/member/edit/{id}?parentMenuId=${(parentMenuId!'0')?c}')
        });

        /*$('#btn_delete').on('click', function() {
            $grid.remove($('#dataGrid'), '/rest/menu', 'delete');
        });*/
    });

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/member/' + id,
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
                                $('#memberHeadImage').attr('src', "/images/user2-160x160.jpg");
                                $('#memberName').html(data.name);
                                if (null === data.identityType || "MEMBER" === data.identityType) {
                                    $('#identityType').html("<i class='fa fa-user-plus'></i> 会员");
                                } else {
                                    $('#identityType').html("<i class='fa fa-user'></i> 零售");
                                }
                                if (null === data.storeName) {
                                    data.storeName = '-'
                                }
                                $('#store').html(data.storeName);

                                if (null === data.salesConsultName) {
                                    data.salesConsultName = '-'
                                }
                                $('#salesConsult').html(data.salesConsultName);

                                if (null === data.mobile) {
                                    data.mobile = '-';
                                }
                                $('#mobile').html(data.mobile);

                                if (null === data.birthday) {
                                    data.birthday = '-';
                                }
                                $('#birthday').html(data.birthday);

                                if (null === data.sex) {
                                    data.sex = '保密';
                                } else if ("FEMALE" === data.sex) {
                                    data.sex = "女";
                                } else {
                                    data.sex = "男";
                                }
                                $('#sex').html(data.sex);

                                if (null === data.status || true === data.status) {
                                    data.status = '启用';
                                } else {
                                    data.status = '停用';
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