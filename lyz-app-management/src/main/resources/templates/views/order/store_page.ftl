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
                                <label class="col-xs-5" style="padding-right: 0px">会员姓名：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="memberName" id="memberName" class="form-control" \>
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
                                <label class="col-xs-5" style="padding-right: 0px">导购姓名：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="sellerName" id="sellerName" class="form-control" \>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">会员电话：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="memberPhone" id="memberPhone" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
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
                                <div class=" col-xs-6">
                                    <select name="store" id="storeCode" class="selectpicker"
                                            onchange="findOrderByOrderNumber()" data-live-search="true">
                                        <option value="-1">选择门店</option>
                                    </select>
                                </div>
                                <div class=" col-xs-3">
                                    <select name="deliveryType" id="deliveryType" class="form-control select"
                                            style="width:auto;" onchange="findOrderByOrderNumber()">
                                        <option value="-1">配送方式</option>
                                        <option value="HOUSE_DELIVERY">送货上门</option>
                                        <option value="SELF_TAKE">门店自提</option>
                                        <option value="PRODUCT_COUPON">购买产品券</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-5">
                            <div class="col-xs-12">
                                <input type="text" name="orderNumber" id="orderNumber" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输订单号">
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
<script>

    var sourceUrl;
    var rotationImage;

    $(function () {
        findCitylist()
        findStorelist();
        initDateGird('/rest/order/page/grid');

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
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
            formatter: function (value) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    var url = "/rest/order/storeOrder/detail/" + value;
//                    return '<a class="scan" href="/rest/order/storeOrder/detail/'+value+'">' + value + '</a>';
                    return '<a class="scan" href="/views/admin/order/detail/' + value + '">' + value + '</a>';
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
            url: '/rest/stores/findStorelist',
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
            url: '/rest/stores/findStoresListByCityId/' + cityId,
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

    //根据订单号查询订单
    function findOrderByOrderNumber() {
        var orderNumber = $("#orderNumber").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var memberName = $("#memberName").val();
        var shippingAddress = $("#shippingAddress").val();
        var sellerName = $("#sellerName").val();
        var memberPhone = $("#memberPhone").val();
        var receiverName = $("#receiverName").val();
        var receiverPhone = $("#receiverPhone").val();
        var cityId = $('#cityCode').val();
        var storeId = $('#storeCode').val();
        var deliveryType = $('#deliveryType').val();
        $("#dataGrid").bootstrapTable('destroy');
        if (orderNumber != null && orderNumber != "") {
            initDateGird('/rest/order/page/byOrderNumber/' + orderNumber);
        } else {
            initDateGird('/rest/order/page/condition?cityId=' + cityId + '&storeId=' + storeId + '&deliveryType=' + deliveryType
                    + '&beginTime=' + beginTime + '&endTime=' + endTime + '&memberName=' + memberName + '&shippingAddress=' + shippingAddress
                    + '&sellerName=' + sellerName + '&memberPhone=' + memberPhone + '&receiverName=' + receiverName + '&receiverPhone=' + receiverPhone);
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