<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
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
                <div id="" class="box-body form-inline">
                    <form class="form-horizontal" id="formSearch">
                    <#--<div class="form-group">-->
                        <input name="birthday" type="text" class="form-control datepicker" id="birthday"
                               placeholder="开始时间">
                        至
                        <input name="birthday" type="text" class="form-control datepicker" id="birthday"
                               placeholder="结束时间">
                        <button type="button" style="margin-left:50px" id="btn_query" class="btn btn-primary">
                            <i class="fa fa-search"></i> 查询
                        </button>
                        <button type="reset" class="btn btn-default">
                            <i class="fa fa-print"></i> 重置
                        </button>
                    <#--</div>-->
                    </form>
                </div>
                <div id="toolbar" class="form-inline">
                    <button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            onchange="findStoreByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findCusByStoreId()" data-live-search="true">
                        <option value="-1">调出门店</option>
                    </select>
                    <select name="guideCode" id="guideCode" class="form-control select" style="width:auto;"
                            onchange="findCusByGuide()">
                        <option value="-1">调入门店</option>
                    </select>
                    <select name="guideCode" id="guideCode" class="form-control select" style="width:auto;"
                            onchange="findCusByGuide()">
                        <option value="-1">选择状态</option>
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

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
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