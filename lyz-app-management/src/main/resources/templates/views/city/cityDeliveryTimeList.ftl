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
    <h1>配送时间列表</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
            <li><a href="javascript:history.back();">城市配送时间</a></li>
        <li class="active">配送时间列表</li>
    </ol>
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
                </div>
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover" >

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    $(function () {
       var id =  ${cityId};
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/cityDeliveryTime/'+id, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'id',
                title: 'id',
                align: 'center',
                visible: false
            },
            {
                field: 'cityId',
                title: '城市ID',
                align: 'center',
            }, {
                field: 'cityName',
                title: '城市名称',
                align: 'center'
            },{
                field: 'startTime',
                title: '配送开始时间',
                align: 'center',
            },{
                field: 'endTime',
                title: '配送结束时间',
                align: 'center',
            }
        ]);
        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/citysDeliveryTimes/add?parentMenuId=${(parentMenuId!'0')}&cityId='+id);
        });

        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/citysDeliveryTimes/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
    });



</script>
</body>