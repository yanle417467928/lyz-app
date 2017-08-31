<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
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
                    <ul id="structureAttributes" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>菜单类型</b> <a class="pull-right" id="menuType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>链接地址</b> <a class="pull-right" id="menuLinkUri"></a>
                        </li>
                        <li class="list-group-item">
                            <b>图标样式</b> <a class="pull-right" id="menuIconStyle"></a>
                        </li>
                        <li class="list-group-item">
                            <b>相关表名</b> <a class="pull-right" id="menuReferenceTable"></a>
                        </li>
                        <li class="list-group-item">
                            <b>排序号</b> <a class="pull-right" id="menuSortId"></a>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/menu/page/grid', 'get', true, function(params) {
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
            title: 'ID',
            align: 'center'
        }, {
            field: 'title',
            title: '菜单标题',
            events: {
                'click .scan': function(e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function(value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
            field: 'type',
            title: '菜单类型',
            formatter: function(value) {
                if ('PARENT' === String(value)) {
                    return '<span class="label label-primary">一级菜单</span>'
                } else {
                    return '<span class="label label-success">二级菜单</span>'
                }
            },
            align: 'center'
        }, {
            field: 'linkUri',
            title: '链接地址'
        }, {
            field: 'referenceTable',
            title: '相关表名'
        }, {
            field: 'sortId',
            title: '排序号',
            align: 'center'
        }]);

        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/menu/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function() {
            $grid.modify($('#dataGrid'), '/views/admin/menu/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#btn_delete').on('click', function() {
            $grid.remove($('#dataGrid'), '/rest/menu', 'delete');
        });
    });

    var $page = {
        information: {
            show: function(id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/menu/' + id,
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
                                $('#menuTitle').html(data.title);

                                if ('PARENT' === data.type) {
                                    data.type = '一级菜单';
                                } else if ('CHILD' === data.type) {
                                    data.type = '二级菜单';
                                } else {
                                    data.type = '??';
                                }
                                $('#menuType').html(data.type);

                                if (null === data.linkUri) {
                                    data.linkUri = '-';
                                }
                                $('#menuLinkUri').html(data.linkUri);

                                if (null === data.iconStyle) {
                                    data.iconStyle = 'fa fa-circle-o';
                                }
                                $('#menuIconStyle').html('<i class="' + data.iconStyle + '"></i>');

                                if (null === data.referenceTable) {
                                    data.referenceTable = '-';
                                }
                                $('#menuReferenceTable').html(data.referenceTable);

                                if (null === data.sortId) {
                                    data.sortId = '-';
                                }
                                $('#menuSortId').html(data.sortId);

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

</script>
</body>