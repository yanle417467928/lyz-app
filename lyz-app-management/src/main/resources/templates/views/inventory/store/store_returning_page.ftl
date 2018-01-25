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
                <#--<button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>-->
                    <select name="selectStatus" id="selectStatus" class="form-control select" style="width:auto;"
                            title="退单状态">
                        <option value="-1">退单状态</option>
                        <option value="1">退货中</option>
                        <option value="2">已取消</option>
                        <option value="3">待退货</option>
                        <option value="4">待退款</option>
                        <option value="5">已完成</option>
                    </select>
                    <select name="selectCity" id="selectCity" class="form-control select" style="width:auto;"
                            title="选择城市">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="selectStore" id="selectStore" class="form-control select"
                            style="width:auto;" title="选择门店">
                        <option value="-1">选择门店</option>
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
                        <a id="menuTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="structureAttributes" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>退单号</b> <a class="pull-right" id="returnNo"></a>
                        </li>
                        <li class="list-group-item">
                            <b>订单号</b> <a class="pull-right" id="orderNo"></a>
                        </li>
                        <li class="list-group-item">
                            <b>退货类型</b> <a class="pull-right" id="returnType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店电话</b> <a class="pull-right" id="storePhone"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店电话</b> <a class="pull-right" id="storeAddress"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>申请用户</b> <a class="pull-right" id="creatorPhone"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品编码</b> <a class="pull-right" id="sku"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品名称</b> <a class="pull-right" id="skuName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>可售门店库存</b> <a class="pull-right" id="availableIty"></a>
                        </li>
                        <li class="list-group-item">
                            <b>真实门店库存</b> <a class="pull-right" id="realIty"></a>
                        </li>
                        <li class="list-group-item">
                            <b>上次修改时间</b> <a class="pull-right" id="lastUpdateTime"></a>
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
    $(function () {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/store/returning/page/grid', 'get', true, function (params) {
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
            field: 'returnNumber',
            title: '退货单号',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            },
            align: 'center'
        }, {
            field: 'orderStatus',
            title: '订单状态',
            align: 'center'
        }, {
            field: 'creatorPhone',
            title: '申请用户',
            align: 'center'
        }, {
            field: 'customerName',
            title: '用户名称',
            align: 'center'
        }, {
            field: 'returnType',
            title: '退货类型',
            align: 'center'
        }, {
            field: 'reasonInfo',
            title: '退货原因',
            align: 'center'
        }, {
            field: 'returnTime',
            title: '申请时间',
            align: 'center'
        }]);

        /* $('#btn_add').on('click', function () {
             $grid.add('/views/admin/menu/add?parentMenuId=${(parentMenuId!'0')}
        ');
                });

                $('#btn_edit').on('click', function() {
                    $grid.modify($('#dataGrid'), '/views/admin/menu/edit/{id}?parentMenuId=${parentMenuId!'0'}
        ')
                });

                $('#btn_delete').on('click', function() {
                    $grid.remove($('#dataGrid'), '/rest/menu', 'delete');
                });*/
    });

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/store/returning/' + id,
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
                                $('#menuTitle').html("门店库存详情");

                                if (null === data.returnNo) {
                                    data.returnNo = '-';
                                }
                                $('#returnNo').html(data.returnNo);

                                if (null === data.orderNo) {
                                    data.orderNo = '-';
                                }
                                $('#orderNo').html(data.orderNo);

                                if (null === data.returnType) {
                                    data.returnType = 'fa fa-circle-o';
                                }
                                $('#returnType').html(data.returnType);

                                if (null === data.storePhone) {
                                    data.storePhone = '-';
                                }
                                $('#storePhone').html(data.storePhone);

                                if (null === data.storeAddress) {
                                    data.storeAddress = '-';
                                }
                                $('#storeAddress').html(data.storeAddress);

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (null === data.creatorPhone) {
                                    data.creatorPhone = '-';
                                }
                                $('#creatorPhone').html(data.creatorPhone);

                                if (null === data.sku) {
                                    data.sku = '-';
                                }
                                $('#sku').html(data.sku);

                                if (null === data.skuName) {
                                    data.skuName = '-';
                                }
                                $('#skuName').html(data.skuName);

                                if (null === data.availableIty) {
                                    data.availableIty = '-';
                                }
                                $('#availableIty').html(data.availableIty);

                                if (null === data.realIty) {
                                    data.realIty = '-';
                                }
                                $('#realIty').html(data.realIty);

                                if (null === data.lastUpdateTime) {
                                    data.lastUpdateTime = '-';
                                }
                                $('#lastUpdateTime').html(data.lastUpdateTime);

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                }
                                $('#createTime').html(data.createTime);

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