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
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
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
                <form class="form-horizontal" id="formSearch">
                    <div id="" class="box-body form-inline">
                        <div class="col-xs-2">
                            <select size="5" name="city" id="city" class="form-control select" style="width:auto;"
                                    title="选择城市" onchange="findStoreListByCity(this.value)">
                                <option value="-1">选择城市</option>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <select size="5" name="store" id="store" class="form-control selectpicker" data-width="160px" data-live-search="true"
                                    style="width:auto;" title="选择门店">
                                <option value="-1">选择门店</option>
                            </select>
                        </div>
                        <div class="col-xs-8">
                            <input name="startDateTime" type="text" class="form-control datepicker" id="startDateTime"
                                   placeholder="开始时间">
                            至
                            <input name="endDateTime" type="text" class="form-control datepicker" id="endDateTime"
                                   placeholder="结束时间">
                            <button type="button" style="margin-left:50px" id="btn_query" class="btn btn-primary">
                                <i class="fa fa-search"></i> 查询
                            </button>
                            <button type="reset" class="btn btn-default">
                                <i class="fa fa-print"></i> 重置
                            </button>
                        </div>
                    </div>
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
                </form>
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
                            <b>变动数量</b> <a class="pull-right" id="changeQty"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动后数量</b> <a class="pull-right" id="afterChangeQty"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动类型</b> <a class="pull-right" id="changeTypeDesc"></a>
                        </li>
                        <li class="list-group-item">
                            <b>关联单号</b> <a class="pull-right" id="referenceNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动时间</b> <a class="pull-right" id="changeTime"></a>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/inventory/log/page/grid', 'get', true, function (params) {
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
            field: 'changType',
            title: '变动类型',
            align: 'center'
        }, {
            field: 'changeTarget',
            title: '门店/城市',
            align: 'center'
        }, {
            field: 'goodsCode',
            title: '商品编码',
            align: 'center'
        }, {
            field: 'goodsTitle',
            title: '商品名称',
            align: 'center'
        }, {
            field: 'changeValue',
            title: '变动数量',
            align: 'center'
        }, {
            field: 'changeDate',
            title: '变动时间',
            align: 'center'
        }, {
            field: 'referenceOrder',
            title: '关联单号',
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

        $('#btn_query').on('click', function () {
            $grid.searchTable('dataGrid', 'formSearch');
        });

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
        findCityList();
        findStoreList()
    });

    function findCityList() {
        var city = "";
        var $city = $('#city');
        $.ajax({
            url: '/rest/citys/findCitylist',
            method: 'GET',
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);
                $.each(result, function (i, item) {
                    city += "<option value=" + item.cityId + ">" + item.name + "</option>";
                });
                $city.append(city);
                $city.selectpicker('refresh');
                $city.selectpicker('render');
            }
        });
    }


    function findStoreList() {
        var store = "";
        var $store = $('#store');
        $.ajax({
            url: '/rest/stores/findStoresListByStoreId',
            method: 'GET',
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);
                $.each(result, function (i, item) {
                    store += "<option value=" + item.storeId + ">" + item.storeName + "</option>";
                });
                $store.append(store);
                $store.selectpicker('refresh');
                $store.selectpicker('render');
            }
        });
    }

    function findStoreListByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        if (cityId == -1) {
            findStorelist();
            return false;
        }
        ;
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityIdAndStoreId/' + cityId,
            method: 'GET',
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);
                $.each(result, function (i, item) {
                    store += "<option value=" + item.storeId + ">" + item.storeName + "</option>";
                });
                $("#store").append(store);
                $('#store').selectpicker('refresh');
                $('#store').selectpicker('render');
            }
        });
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }
    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/inventory/log' + id,
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
                                $('#menuTitle').html("库存变动详情");

                                if (null === data.cityId) {
                                    data.cityId = '-';
                                }
                                $('#cityId').html(data.cityId);

                                if (null === data.cityName) {
                                    data.cityName = '-';
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

                                if (null === data.changeQty) {
                                    data.changeQty = '-';
                                }
                                $('#changeQty').html(data.changeQty);

                                if (null === data.afterChangeQty) {
                                    data.afterChangeQty = '-';
                                }
                                $('#afterChangeQty').html(data.afterChangeQty);

                                if (null === data.changeTypeDesc) {
                                    data.changeTypeDesc = '-';
                                }
                                $('#changeTypeDesc').html(data.changeTypeDesc);

                                if (null === data.referenceNumber) {
                                    data.referenceNumber = '-';
                                }
                                $('#referenceNumber').html(data.referenceNumber);

                                if (null === data.changeTime) {
                                    data.changeTime = '-';
                                }
                                $('#changeTime').html(data.changeTime);

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