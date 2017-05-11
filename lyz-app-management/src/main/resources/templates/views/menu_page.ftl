<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
</head>
<body>

<section class="content-header">
    <#if selectedMenu??>
        <h1>${selectedMenu.title!'??'}</h1>
        <ol class="breadcrumb">
            <li><a href="/view"><i class="fa fa-home"></i> 首页</a></li>
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
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 修改
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
            formatter: function(value, row) {
                var id = row.id;
                return '<a href="/view/menu/info/' + id + '">' + value + '</a>';
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
            $grid.add('/view/menu/edit/0');
        });

        $('#btn_edit').on('click', function() {
            $grid.modify($('#dataGrid'), '/view/menu/edit/{id}')
        });

        $('#btn_delete').on('click', function() {
            $grid.remove($('#dataGrid'), '/rest/menu', 'delete');
        });
    });

</script>
</body>