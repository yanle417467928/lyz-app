<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>

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
            <div class="nav-tabs-custom">
                <div id="toolbar" class="form-inline ">

                    <button id="btn_add_order" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>

                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>

                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findCusByCity()" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findCusByStoreId()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>

                    <select name="status" id="status" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findStorePreByStatus()" data-live-search="true">
                        <option value="">选择订单状态</option>
                    <#if photoOrderStatus??>
                        <#list photoOrderStatus as status>
                            <option value="${status}">${status.value}</option>
                        </#list>
                    </#if>
                    </select>

                    <button id="btn_add" type="button" class="btn btn-default pull-left" onclick="downloadPhoto()">
                        <i class="fa fa-download"></i>
                        下载图片
                    </button>

                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control"
                               style="width:auto;"
                               placeholder="请输入要查找的姓名或电话、单号" onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCusByNameOrPhone()">查找</button>
                        </span>
                    </div>
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
                    <ul id="photoOrder" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>下单人姓名</b> <a class="pull-right" id="username"></a>
                        </li>
                        <li class="list-group-item">
                            <b>下单人手机号码</b> <a class="pull-right" id="userMobile"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属门店</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>下单人身份类型</b> <a class="pull-right" id="identityType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>联系人姓名</b> <a class="pull-right" id="contactName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>联系人电话</b> <a class="pull-right" id="contactPhone"></a>
                        </li>
                        <li class="list-group-item" style="height: 200px;">
                            <b>图片</b> <a class="pull-right" id="photos"></a>
                        </li>
                        <li class="list-group-item">
                            <b>拍照下单单号</b> <a class="pull-right" id="photoOrderNo"></a>
                        </li>
                        <li class="list-group-item">
                            <b>下单时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>备注</b> <a class="pull-right" id="remark"></a>
                        </li>
                        <li class="list-group-item">
                            <b>状态</b> <a class="pull-right" id="status"></a>
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



<div id="photoGoodsDetails" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document" style="width: 60%">
        <div class="modal-content">
            <div class="modal-header">
                <h4>拍照下单说所选商品</h4>
                <button type="button" name="search" class="btn btn-default pull-left"
                        onclick="returnGoods()" style="margin-left:700px;margin-top: -35px;">关闭
                </button>
            </div>
            <div class="modal-body">
                <!--  设置这个div的大小，超出部分显示滚动条 -->
                <div id="addressDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box box-primary">
                                    <div id="goodsToolbar" class="form-inline">

                                        <#--<div class="input-group col-md-3"-->
                                             <#--style="margin-top:0px positon:relative">-->
                                            <#--<input type="text" name="sellerAddressConditions"-->
                                                   <#--id="sellerAddressConditions"-->
                                                   <#--class="form-control" style="width:300px;height:34px;"-->
                                                   <#--placeholder="请输入收货人姓名、电话、小区、楼盘、详细地址">-->
                                            <#--<span class="input-group-btn">-->
                            <#--<button type="button" name="search" id="search-btn" class="btn btn-info btn-search"-->
                                    <#--onclick="openAddressModal('/rest/order/photo/find/address')">查找</button>-->
                        <#--</span>-->
                                        <#--</div>-->
                                    </div>
                                    <div class="box-body table-reponsive">
                                        <table id="photoGoodsList"
                                               class="table table-bordered table-hover">

                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</div>


<script>

    $(function () {
        findCitylist();
        findStorelist();
        showAvailableCredit(null, null, null, null);
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });

    function findCitylist() {
        var city = "";
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
                $("#cityCode").append(city);
                $('#cityCode').selectpicker('refresh');
                $('#cityCode').selectpicker('render');
            }
        });
    }


    function findStorelist() {
        var store = "";
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
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
            }
        });
    }

    function showAvailableCredit(keywords, cityId, storeId, status) {
        $("#dataGrid").bootstrapTable('destroy');
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/order/photo/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                cityId: cityId,
                storeId: storeId,
                status: status
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'id',
                title: 'ID',
                align: 'center',
                events: {
                    'click .scan': function (e, value, row) {
                        $page.information.show(row.id);
                    }
                },
                formatter: function (value) {
                    if (null == value) {
                        return '<a class="scan" href="#">' + '未知' + '</a>';
                    } else {
                        return '<a class="scan" href="#">' + value + '</a>';
                    }
                }
            }, {
                field: 'username',
                title: '下单人姓名',
                align: 'center'
            }, {
                field: 'userMobile',
                title: '下单人手机号码',
                align: 'center'
            },
            {
                field: 'storeName',
                title: '归属门店',
                align: 'center'
            },
            {
                field: 'identityType',
                title: '下单人身份类型',
                align: 'center'
            },
            {
                field: 'status',
                title: '状态',
                align: 'center'
            },
            {
                field: 'photoOrderNo',
                title: '拍照下单单号',
                align: 'center'
            },
            {
                field: 'createTime',
                title: '下单时间',
                align: 'center'
            }, {
                field: 'statusEnum',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if ('PENDING' === value || 'PROCESSING' === value) {
                        return '<button class="btn btn-primary btn-xs" onclick="handle(' + row.id + ')"> 处理</button>';
                    } else if ('FINISH' === value){
                        var photoNo = "'"+row.photoOrderNo+"'";
                        return '<button class="btn btn-success btn-xs" onclick="showPhotoGoodsList(' + photoNo + ')"> 查看</button>';
                    }else{
                        return '-';
                    }
                }
            }
        ]);
        $('#btn_delete').on('click', function () {
            $grid.remove($('#dataGrid'), '/rest/order/photo', 'delete');
        });
    }

    function showPhotoGoodsList(photoNo) {
        var url = '/rest/order/photo/find/photo/goods';
        $("#photoGoodsList").bootstrapTable('destroy');
        $grid.init($('#photoGoodsList'),$('#goodsToolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
                photoNo: photoNo
            }
        }, [
            {
                field: 'gid',
                title: 'gid',
                align: 'center'
            }, {
                field: 'skuName',
                title: '商品名称',
                align: 'center'
            }, {
                field: 'goodsQty',
                title: '数量',
                align: 'center'
            },
            {
                field: 'photoOrderNo',
                title: '拍照下单单号',
                align: 'center'
            }
        ]);
        $('#photoGoodsDetails').modal('show');
    }

    function returnGoods() {
        $('#photoGoodsDetails').modal('hide');
    }

    function findCusByCity() {
        initSelect("#storeCode", "选择门店");
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        var keywords = $('#queryCusInfo').val();
        var status = $("#status").val();
        $("#dataGrid").bootstrapTable('destroy');
        findCusLebiLogByCityIdOrstoreIdOrKeywords(keywords, cityId, storeId, status);
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
                })
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
            }
        });
    }

    function findCusLebiLogByCityIdOrstoreIdOrKeywords(keywords, cityId, storeId, status) {
        showAvailableCredit(keywords, cityId, storeId, status);
    }

    function findCusByStoreId() {
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        var status = $("#status").val();
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        findCusLebiLogByCityIdOrstoreIdOrKeywords(keywords, cityId, storeId, status);

    }

    function findStorePreByStatus() {
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        var status = $("#status").val();
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        findCusLebiLogByCityIdOrstoreIdOrKeywords(keywords, cityId, storeId, status);

    }

    function findBykey() {
        if (event.keyCode == 13) {
            findCusByNameOrPhone();
        }
    }

    function findCusByNameOrPhone() {
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        var status = $("#status").val();
        findCusLebiLogByCityIdOrstoreIdOrKeywords(queryCusInfo, cityId, storeId, status);
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
                        url: '/rest/order/photo/' + id,
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

                            if (0 === result.code) {
                                $global.timer = null;
                                var data = result.content;
                                $('#menuTitle').html(" 拍照下单详情");

                                if (null === data.userMobile) {
                                    data.userMobile = '-';
                                }
                                $('#userMobile').html(data.userMobile);

                                if (null === data.username) {
                                    data.username = '-';
                                }
                                $('#username').html(data.username);

                                if (null == data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (null == data.identityType) {
                                    data.identityType = '-';
                                }
                                $('#identityType').html(data.identityType);

                                if (null === data.remark) {
                                    data.remark = '-';
                                }
                                $('#remark').html(data.remark);

                                if (null === data.status) {
                                    data.status = '-';
                                }
                                $('#status').html(data.status);

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                }
                                $('#createTime').html(data.createTime);

                                if (null === data.photoOrderNo) {
                                    data.photoOrderNo = '-';
                                }
                                $('#photoOrderNo').html(data.photoOrderNo);

                                if (null === data.contactName) {
                                    data.contactName = '-';
                                }
                                $('#contactName').html(data.contactName);

                                if (null === data.contactPhone) {
                                    data.contactPhone = '-';
                                }
                                $('#contactPhone').html(data.contactPhone);
                                if (null === data.photos || '' == data.photos) {
                                    $('#photos').html('-');
                                } else {
                                    var img = '';
                                    for (var i = 0; i < data.photos.length; i++) {
                                        img += '<img  src="';
                                        img += data.photos[i];
                                        img += '" class="img-rounded" style="height: 80px;width: 80px;" >';
                                        if (i == 6) {
                                            img += '<br/>';
                                        }
                                    }
                                    $('#photos').html(img);
                                }

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
    function handle(id) {
        window.location.href = '/views/admin/order/photo/edit/' + id;
    }

    function downloadPhoto() {
        var selected = this.getSelectedIds($('#dataGrid'));
        if (null === selected || 0 === selected.length) {
            $notify.warning('请在点击按钮前选中至少一条数据');
            return;
        }
        var url = "/rest/order/photo//download/photo?photoIds=" + selected;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);
    }

    function getSelectedIds(container) {
        var ids = [];
        var selected = container.bootstrapTable('getSelections');
        for (var i = 0; i < selected.length; i++) {
            var data = selected[i];
            ids.push(data.id);
        }
        return ids;
    }

    $('#btn_add_order').on('click', function () {
        $grid.add('/views/admin/order/photo/add');
    });
</script>
</body>