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
                    <#--<button id="btn_add" type="button" class="btn btn-default">-->
                        <#--<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增-->
                    <#--</button>-->
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
                            <b>商品名称</b> <a class="pull-right" id="goodsName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品编码</b> <a class="pull-right" id="goodsCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品标题</b> <a class="pull-right" id="title"></a>
                        </li>
                        <li class="list-group-item">
                            <b>品牌</b> <a class="pull-right" id="brandTitle"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品类型名称</b> <a class="pull-right" id="categoryTitle"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否为小辅料</b> <a class="pull-right" id="isGift"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否上架</b> <a class="pull-right" id="isOnSale"></a>
                        </li>
                        <li class="list-group-item">
                            <b>上架时间</b> <a class="pull-right" id="onSaleTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/goods/page/grid', 'get', true, function(params) {
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
            field: 'goodsName',
            title: '商品名称',
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
            field: 'goodsCode',
            title: '商品编码',
            align: 'center'
        },{
            field: 'createTime',
            title: '创建时间',
            align: 'center'
        },{
            field: 'title',
            align: 'center',
            title: '商品标题'
        },{
            field: 'onSaleTime',
            title: '上架时间',
            align: 'center'
        },{
            field: 'brandTitle',
            align: 'center',
            title: '品牌'
        },{
            field: 'categoryTitle',
            align: 'center',
            title: '商品类型名称'
        }, {
            field: 'isGift',
            align: 'center',
            title: '是否为小辅料',
            formatter: function (value) {
                if (true === value) {
                    return '<span class="label label-primary">是</span>'
                } else if (false === value) {
                    return '<span class="label label-danger">否</span>'
                } else {
                    return '<span class="label label-danger">-</span>'
                }
            }
        },{
            field: 'isOnSale',
            align: 'center',
            title: '是否上架',
            formatter: function(value) {
                if (true === value) {
                    return '<span class="label label-primary">是</span>'
                } else if(false===value) {
                    return '<span class="label label-danger">否</span>'
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
            $grid.modify($('#dataGrid'), '/view/goods/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#btn_delete').on('click', function() {
            $grid.remove($('#dataGrid'), '/rest/goods', 'delete');
        });
    });

    var $page = {
        information: {
            show: function (id) {
                var URL = '/rest/goods/' + id;
                var success = function (result) {
                    if (0 === result.code) {
                        var data = result.content;
                        $('#menuTitle').html("商品详情");

                        if (null === data.goodsName) {
                            data.goodsName = '-';
                        }
                        $('#goodsName').html(data.goodsName);

                        if (null === data.goodsCode) {
                            data.goodsCode = '-';
                        }
                        $('#goodsCode').html(data.goodsCode);
                        if (null === data.createTime) {
                            data.createTime = '-';
                        }
                        $('#createTime').html(data.createTime);

                        if (null === data.title) {
                            data.title = '-';
                        }
                        $('#title').html(data.title);
                        if (null === data.onSaleTime) {
                            data.onSaleTime = '-';
                        }
                        $('#onSaleTime').html(data.onSaleTime);
                        if (null === data.brandTitle) {
                            data.brandTitle = '-';
                        }
                        $('#brandTitle').html(data.brandTitle);
                        if (null === data.categoryTitle) {
                            data.categoryTitle = '-';
                        }
                        $('#categoryTitle').html(data.categoryTitle);
                        if (true === data.isGift) {
                            $('#isGift').html('<span class="label label-primary">是</span>');
                        } else if (false === data.isGift) {
                            $('#isGift').html('<span class="label label-danger">否</span>');
                        } else {
                            $('#isGift').html('-');
                        }
                        if (true === data.isOnSale) {
                            $('#isOnSale').html('<span class="label label-primary">是</span>');
                        } else if (false === data.isOnSale) {
                            $('#isOnSale').html('<span class="label label-danger">否</span>');
                        } else {
                            $('#isOnSale').html('-');
                        }
                        $('#information').modal();
                    } else {
                        $notify.danger(result.message);
                    }
                };
                $http.GET(URL, null, success);
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }
</script>
</body>