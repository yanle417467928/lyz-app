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
                            <b>城市id</b> <a class="pull-right" id="cityId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市编码</b> <a class="pull-right" id="cityCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市名称</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店id</b> <a class="pull-right" id="storeId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店编码</b> <a class="pull-right" id="storeCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品id</b> <a class="pull-right" id="gid"></a>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/store/inventory/page/grid', 'get', true, function (params) {
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
            field: 'storeName',
            title: '门店名称',
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
            field: 'goodsName',
            title: '商品名称',
            align: 'center'
        }, {
            field: 'goodsCode',
            title: '商品编码',
            align: 'center'
        }, {
            field: 'realInventory',
            title: '真实库存',
            align: 'center'
        }, {
            field: 'soldInventory',
            title: '可售库存',
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
                        url: '/rest/store/inventory/' + id,
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

                                if (null === data.cityId) {
                                    data.cityId = '-';
                                }
                                $('#cityId').html(data.cityId);

                                if (null === data.cityCode) {
                                    data.cityCode = '-';
                                }
                                $('#cityCode').html(data.cityCode);

                                if (null === data.cityName) {
                                    data.cityName = 'fa fa-circle-o';
                                }
                                $('#cityName').html(data.cityName);

                                if (null === data.storeId) {
                                    data.storeId = '-';
                                }
                                $('#storeId').html(data.storeId);

                                if (null === data.storeCode) {
                                    data.storeCode = '-';
                                }
                                $('#storeCode').html(data.storeCode);

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (null === data.gid) {
                                    data.gid = '-';
                                }
                                $('#gid').html(data.gid);

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
                                $('#lastUpdateTime').html(formatDateTime(data.lastUpdateTime));

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                }
                                $('#createTime').html(formatDateTime(data.createTime));

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