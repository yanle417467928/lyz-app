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
                            <b>订单编号</b> <a class="pull-right" id="orderNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品列表</b>

                            <table id="goodsList" class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>商品编码</th>
                                    <th>商品名称</th>
                                    <th>数量</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </li>
                        <li class="list-group-item">
                            <b>收货地址</b>

                            <table id="detailAddress" class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>门店名称</th>
                                    <th>门店地址</th>
                                    <th>门店电话</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </li>
                        <li class="list-group-item">
                            <b>备注信息</b>
                            <textarea id="remarkInfo"></textarea>
                        </li>
                        <li class="list-group-item">
                            <b>后台备注信息</b>
                            <textarea id="managerRemarkInfo"></textarea>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/store/requiring/page/grid', 'get', true, function (params) {
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
            field: 'orderNumber',
            title: '订单号',
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
            field: 'city',
            title: '城市',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        }, {
            field: 'remarkInfo',
            title: '商户备注',
            align: 'center'
        }, {
            field: 'orderTime',
            title: '下单时间',
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
                        url: '/rest/store/requiring/' + id,
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
                                $('#menuTitle').html("要货单详情");

                                if (null === data.orderNumber) {
                                    data.orderNumber = '-';
                                }
                                $('#orderNumber').html(data.orderNumber);

                                if (null === data.goodsList) {
                                    data.goodsList = '-';
                                }
                                var str = "";
                                $.each(data.goodsList, function (i, item) {
                                    str += "<tr><td>" + item.sku + "</td><td>" + item.skuName + "</td><td>" + item.qty + "</td></tr>";
                                });
                                $('#goodsList').find('tbody').append(str);


                                if (null === data.detailAddress) {
                                    data.detailAddress = '-';
                                }
                                var $tr = "";
                                $.each(data.detailAddress, function (i, item) {
                                    str += "<tr><td>" + item.storeName + "</td><td>" + item.storeAddress + "</td><td>" + item.storePhone + "</td></tr>";
                                });
                                $('#detailAddress').find('tbody').append($tr);

                                if (null === data.remarkInfo) {
                                    data.remarkInfo = '-';
                                }
                                $('#remarkInfo').html(data.remarkInfo);

                                if (null === data.managerRemarkInfo) {
                                    data.managerRemarkInfo = '-';
                                }
                                $('#managerRemarkInfo').html(data.managerRemarkInfo);

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