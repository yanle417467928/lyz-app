<head>

    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/lightbox2/2.10.0/css/lightbox.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/lightbox2/2.10.0/js/lightbox.js"></script>
</head>
<body>
<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parent??>
            <li><a href="javascript:void(0);">${selectedMenu.parent.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>加载中...</h1>
</#if>
</section>

<section class="content">
    <div class="box box-primary" style="padding: 10px  2%">
        <form id="">
            <div class="form-inline">
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">开始时间:</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input name="beginTime" type="text" class="form-control datepicker" id="beginTime"
                                           placeholder="开始时间">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">结束时间：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input name="endTime" type="text" class="form-control datepicker" id="endTime"
                                           placeholder="开始时间">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人姓名：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="creatorName" id="creatorName" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">收货地址:</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="shippingAddress" id="shippingAddress" class="form-control"
                                           \>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">收货人姓名:</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="receiverName" id="receiverName" class="form-control" \>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人电话：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="creatorPhone" id="creatorPhone" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">

                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">收货人电话：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="receiverPhone" id="receiverPhone" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-7">
                            <div class="col-xs-12">
                                <div class=" col-xs-3">
                                    <select name="city" id="cityCode" class="form-control select"
                                            onchange="findStoreByCity(this.value)">
                                        <option value="-1">选择城市</option>
                                    </select>
                                </div>
                                <div class=" col-xs-3">
                                    <select name="store" id="storeCode" class="selectpicker"
                                            onchange="findOrderByOrderNumber()" data-live-search="true">
                                        <option value="-1">选择门店</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-5">
                            <div class="col-xs-12">
                                <input type="text" name="orderNumber" id="orderNumber" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输订单号" onkeypress="findBykey()">
                                <span class="">
                                <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                        onclick="return findOrderByOrderNumber()">查找</button>
                        </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                <#--<button id="btn_edit" type="button" class="btn btn-default">-->
                <#--<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑-->
                <#--</button>-->
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
                        <a id="goodsTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>
                    </span>
                    <ul id="goodsDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>商品id</b> <a class="pull-right" id="id"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物料编号</b> <a class="pull-right" id="sku"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物料名称</b> <a class="pull-right" id="materialsName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>电商名称</b> <a class="pull-right" id="skuName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物料条码</b> <a class="pull-right" id="materialsCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物料单位</b> <a class="pull-right" id="goodsUnit"></a>
                        </li>
                        <li class="list-group-item">
                            <b>电商分类</b> <a class="pull-right" id="categoryName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品品牌</b> <a class="pull-right" id="brdName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物理分类</b> <a class="pull-right" id="physicalClassify"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物料类型</b> <a class="pull-right" id="typeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>物料状态</b> <a class="pull-right" id="materialsEnable"></a>
                        </li>
                        <li class="list-group-item">
                            <b>公司标识</b> <a class="pull-right" id="companyFlag"></a>
                        </li>
                        <li class="list-group-item">
                            <b>排序号</b> <a class="pull-right" id="sortId"></a>
                        </li>
                        <li class="list-group-item" style="height: 100px;">
                            <b>商品封面图</b> <a class="pull-right" id="coverImageUri"></a>
                        </li>
                        <li class="list-group-item" style="height: 100px;">
                            <b>商品轮播图</b> <a class="pull-right" id="rotationImageUri">
                        </a>
                        </li>
                    <#--                <li class="list-group-item">
                                        <b>商品详情页</b> <a class="pull-right" id="goodsDetail"></a>
                                    </li>-->
                        <li class="list-group-item">
                            <b>是否为热门商品</b> <a class="pull-right" id="isHot"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否为调色商品</b> <a class="pull-right" id="isColorMixing"></a>
                        </li>
                        <li class="list-group-item">
                            <b>资料创建时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>搜索关键字</b> <a class="pull-right" id="searchKeyword"></a>
                        </li>
                        <li class="list-group-item">
                            <b>产品档次</b> <a class="pull-right" id="productGrade"></a>
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


    var sourceUrl;
    var rotationImage;

    $(function () {
        findCitylist()
        findStorelist();
        initDateGird('/rest/order/pendingShipment/list');

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/goods/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
    });


    function initDateGird(url) {
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'orderNumber',
            title: '订单号',
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
            field: 'meberName',
            title: '下单人姓名',
            align: 'center'

        }, {
            field: 'meberNumber',
            title: '下单人电话',
            align: 'center'

        }, {
            field: 'orderPrice',
            title: '订单金额',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<span class="label label-danger">0.00</span>';
                } else {
                    var aprice = value.toFixed(2)
                    return aprice;
                }
            }
        }, {
            field: 'deliveryType',
            title: '配送方式',
            align: 'center',
            formatter: function (value, row, index) {
                if ('HOUSE_DELIVERY' === value) {
                    return '<span class="">送货上门</span>';
                } else if ('SELF_TAKE' === value) {
                    return '<span class="">门店自提</span>';
                } else if ('PRODUCT_COUPON' === value) {
                    return '<span class="">购买产品券</span>';
                }
            }
        }, {
            field: 'status',
            title: '订单状态',
            align: 'center',
            formatter: function (value, row, index) {
                if ('UNPAID' === value) {
                    return '<span class="">待付款</span>';
                } else if ('PENDING_SHIPMENT' === value) {
                    return '<span class="">待发货</span>';
                } else if ('PENDING_RECEIVE' === value) {
                    return '<span class="">待收货</span>';
                } else if ('FINISHED' === value) {
                    return '<span class="">已完成</span>';
                } else if ('CLOSED' === value) {
                    return '<span class="">已结案</span>';
                } else if ('CANCELED' === value) {
                    return '<span class="">已取消</span>';
                } else if ('REJECTED' === value) {
                    return '<span class="">拒签</span>';
                } else if ('CANCELING' === value) {
                    return '<span class="">取消中</span>';
                }
            }
        }, {
            field: 'storeName',
            title: '门店',
            align: 'center'

        }, {
            field: 'createTime',
            title: '下单时间',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<span class="label label-danger">-</span>';
                } else {
                    return formatDateTime(value);
                }
            }
        }
        ]);
    }

    var $page = {
        information: {
            show: function (id) {
                var URL = '/rest/goods/' + id;
                var success = function (result) {
                    $('#rotationImageContainer').css('display', 'none')
                    if (0 === result.code) {
                        var data = result.content;
                        $('#goodsTitle').html("商品详情");
                        if (null === data.id) {
                            data.id = '-';
                        }
                        $('#id').html(data.id);

                        if (null === data.sku) {
                            data.goodsName = '-';
                        }
                        $('#sku').html(data.sku);

                        if (null === data.materialsName) {
                            data.materialsName = '-';
                        }
                        $('#materialsName').html(data.materialsName);

                        if (null === data.createTime) {
                            data.createTime = '-';
                        }
                        $('#createTime').html(data.createTime);

                        if (null === data.skuName) {
                            data.title = '-';
                        }
                        $('#skuName').html(data.skuName);

                        if (null === data.materialsCode) {
                            data.materialsCode = '-';
                        }
                        $('#materialsCode').html(data.materialsCode);

                        if (null === data.goodsUnit) {
                            data.goodsUnit = '-';
                        }
                        $('#goodsUnit').html(data.goodsUnit);

                        if (null === data.categoryName) {
                            data.categoryName = '-';
                        }
                        $('#categoryName').html(data.categoryName);

                        if (null === data.brdName) {
                            data.brdName = '-';
                        }
                        $('#brdName').html(data.brdName);

                        if (null === data.physicalClassify) {
                            data.physicalClassify = '-';
                        }
                        $('#physicalClassify').html(data.physicalClassify);

                        if (null === data.typeName) {
                            data.typeName = '-';
                        }
                        $('#typeName').html(data.typeName);

                        if (null === data.materialsEnable) {
                            data.materialsEnable = '<span class="label label-danger">-</span>';
                        }
                        $('#materialsEnable').html(data.materialsEnable);

                        if ('LYZ' === data.companyFlag) {
                            data.companyFlag = '乐意装';
                        } else if ('HR' === data.companyFlag) {
                            data.companyFlag = '华润'
                        } else if ('YR' === data.companyFlag) {
                            data.companyFlag = '莹润'
                        } else {
                            data.companyFlag = '-'
                        }
                        $('#companyFlag').html(data.companyFlag);

                        if (null === data.sortId) {
                            data.sortId = '-';
                        }
                        $('#sortId').html(data.sortId);


                        if (null === data.coverImageUri || '' == data.coverImageUri) {
                            $('#coverImageUri').html('-');
                        } else {
                            sourceUrl = data.coverImageUri;
                            $('#coverImageUri').html('<a href="' + data.coverImageUri + '" data-lightbox="image-1"><img src="' + data.coverImageUri + '"' + ' class="img-rounded" style="height: 80px;width: 80px;"/></a>');
                        }

                        if (null === data.rotationImageUri || '' == data.rotationImageUri) {
                            $('#rotationImageUri').html('-');
                        } else {
                            $('#rotationImageUri').empty()
                            rotationImage = data.rotationImageUri.split(",");
                            for (var a = 0; a < rotationImage.length; a++) {
                                $('#rotationImageUri').append('<a href="' + rotationImage[a] + '" data-lightbox="group"><img src="' + rotationImage[a] + '"' + ' class="img-rounded" style="height: 80px;width: 80px;margin-left: 5px"  /></a>');
                            }
                        }
                        /*                      if (null === data.goodsDetail) {
                                                  data.goodsDetail = '-';
                                              }
                                              $('#goodsDetail').html(data.goodsDetail);*/

                        if (true === data.isHot) {
                            data.isHot = ' <span class="label label-primary">是</span>';
                        } else if (false === data.isHot) {
                            data.isHot = '<span class="label label-danger">否</span>'
                        } else {
                            data.isColorMixing = '<span class="label label-danger">-</span>'
                        }
                        $('#isHot').html(data.isHot);

                        if (true === data.isColorMixing) {
                            data.isColorMixing = ' <span class="label label-primary">是</span>';
                        } else if (false === data.isColorMixing) {
                            data.isColorMixing = '<span class="label label-danger">否</span>'
                        } else {
                            data.isColorMixing = '<span class="label label-danger">-</span>'
                        }
                        $('#isColorMixing').html('' + data.isColorMixing);

                        if (null === data.searchKeyword) {
                            data.searchKeyword = '-';
                        }
                        $('#searchKeyword').html(data.searchKeyword);

                        if (null === data.productGrade) {
                            data.productGrade = '-';
                        }
                        $('#productGrade').html(data.productGrade);


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

    //获取城市列表
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
                })
                $("#cityCode").append(city);
            }
        });
    }
    //获取门店列表
    function findStorelist() {
        var store = "";
        $.ajax({
            url: '/rest/stores/find/selfDelivery/stores',
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
    //根据城市查看订单
    function findStoreByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        if (cityId == -1) {
            findStorelist();
            findOrderByOrderNumber();
            return false;
        }
        ;
        $("#queryCusInfo").val('');


        var store;
        $.ajax({
            url: '/rest/stores/find/city/selfDelivery/stores/' + cityId,
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
                findOrderByOrderNumber();
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
            }
        });
    }
    //转换时间
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

    function findBykey(){
        if(event.keyCode==13){
            findOrderByOrderNumber();
        }
    }

    //根据订单号查询订单
    function findOrderByOrderNumber() {
        var orderNumber = $("#orderNumber").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var creatorName = $("#creatorName").val();
        var shippingAddress = $("#shippingAddress").val();
        var creatorPhone = $("#creatorPhone").val();
        var receiverName = $("#receiverName").val();
        var receiverPhone = $("#receiverPhone").val();
        var cityId = $('#cityCode').val();
        var storeId = $('#storeCode').val();
        $("#dataGrid").bootstrapTable('destroy');
        if (orderNumber != null && orderNumber != "") {
            initDateGird('/rest/order/pendingShipment/byOrderNumber/' + orderNumber);
        } else {
            initDateGird('/rest/order/page/pendingShipment/condition?cityId=' + cityId + '&storeId=' + storeId
                    + '&beginTime=' + beginTime + '&endTime=' + endTime + '&creatorName=' + creatorName + '&shippingAddress=' + shippingAddress
                    + '&creatorPhone=' + creatorPhone + '&receiverName=' + receiverName + '&receiverPhone=' + receiverPhone);
        }
    }

    function findGoodsPhysical() {
        var physical;
        $.ajax({
            url: '/rest/goods/page/physicalClassifyGrid',
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
                    physical += "<option value=" + item.id + ">" + item.physicalClassifyName + "</option>";
                })
                $("#categoryCode").append(physical);
            }
        });
    }
    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

</script>
</body>