<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
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
    <div class="row">
        <div class=" col-xs-12">
            <div class="box box-primary">
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                    高级查询
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="form-inline">
                                    <div class="row text-center">
                                        <div class="col-xs-12" style="margin-top: 5px">
                                            <div class="col-xs-4 ">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">开始时间:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input name="beginTime" type="text"
                                                               class="form-control datepicker" id="beginTime"
                                                               placeholder="开始时间">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">结束时间:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input name="endTime" type="text"
                                                               class="form-control datepicker" id="endTime"
                                                               placeholder="结束时间">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4 ">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">会员姓名:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="memberName" id="memberName"
                                                               class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col-xs-12" style="margin-top: 5px">
                                            <div class="col-xs-4 ">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">收货地址:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="shippingAddress" id="shippingAddress"
                                                               class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">收货人姓名:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="receiverName" id="receiverName"
                                                               class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">会员电话:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="memberPhone" id="memberPhone"
                                                               class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col-xs-12" style="margin-top: 5px">
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">收货人电话:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="receiverPhone" id="receiverPhone"
                                                               class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">导购姓名:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="sellerName" id="sellerName"
                                                               class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="toolbar" class="form-inline">
                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findOrderByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            onchange="findOrderByFilterCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <select name="orderStatus" id="orderStatus" class="form-control"  style="width:auto;"
                            onchange="findOrderByFilterCondition()">
                        <option value="-1">选择订单状态</option>
                    <#if orderStatusList?? && orderStatusList?size gt 0 >
                        <#list orderStatusList as orderStatus>
                            <option value="${orderStatus.value!''}">${orderStatus.description!''}</option>
                        </#list>
                    </#if>
                    </select>
                    <select name="isPayUp" id="isPayUp" class="form-control" style="width:auto;"
                            onchange="findOrderByFilterCondition()" data-live-search="true">
                        <option value="-1">收款状态</option>
                        <option value="1">已付清</option>
                        <option value="0">未付清</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryOrderInfo" id="queryOrderInfo" class="form-control "
                               style="width:auto;" placeholder="请输入单号" onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findOrderByFilterCondition()">查找</button>
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
<script>


    $(function () {
        initDateGird('/rest/order/selfTakeOrder/page/grid');
        findCitySelection();
        findStoreSelection();
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
    });


    function findCitySelection() {
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


    function findStoreSelection() {
        var store = "";
        var cityCode = $("#cityCode").val();
        $.ajax({
            url: '/rest/stores/findStoresListByCityIdAndStoreIdList/'+cityCode,
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
            field: 'storeName',
            title: '门店',
            align: 'center'
        }, {
            field: 'orderNumber',
            title: '订单号',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="/views/admin/order/selfTakeOrderDetail/' + value + '" target="_blank">' + value + '</a>';
                }
            }
        }, {
            field: 'customerName',
            title: '顾客姓名',
            align: 'center'
        }, {
            field: 'creatorName',
            title: '下单人',
            align: 'center',
        }, {
            field: 'createTime',
            title: '订单创建时间',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<span class="label label-danger">-</span>';
                } else {
                    return formatDateTime(value);
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
            field: 'isPayUp',
            title: '收款状态',
            align: 'center',
            formatter: function (value, row, index) {
                if (false == value) {
                    return '未收款';
                } else if (true == value) {
                    return '已收款';
                } else {
                    return '-';
                }
            }
        }, {
            field: 'creatorIdentityType',
            title: '下单人类型',
            align: 'center',
            visible: false
        }]);
    }

    function shipShop(orderNumber, creatorIdentityType) {
        if ('SELLER' == creatorIdentityType) {
            $('#confirmShip').modal();
            $("#confirmShipId").val(orderNumber);
        } else if ('CUSTOMER' == creatorIdentityType) {
            $('#PickUpCode').modal();
            $("#codeOrderId").val(orderNumber);
        }
    }


    function findOrderByCondition() {
        $("#queryOrderInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val().trim();
        var storeId = $("#storeCode").val().trim();
        var status = $("#status").val().trim();
        var isPayUp = $("#isPayUp").val().trim();
        initDateGird('/rest/order/selfTakeOrder/page/screenGrid?cityId=' + cityId + '&storeId=' + storeId + '&status=' + status + '&isPayUp=' + isPayUp);
    }


    function findOrderByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        findOrderByFilterCondition();
        if (cityId == -1) {
            findStoreSelection();
            return false;
        };
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityIdAndStoreIdList/' + cityId,
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


    function findBykey() {
        if (event.keyCode == 13) {
            findOrderByFilterCondition();
        }
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
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

    function findOrderByFilterCondition() {
        var beginTime = $("#beginTime").val().trim();
        var endTime = $("#endTime").val().trim();
        var memberName = $("#memberName").val().trim();
        var shippingAddress = $("#shippingAddress").val().trim();
        var sellerName = $("#sellerName").val().trim();
        var memberPhone = $("#memberPhone").val().trim();
        var receiverName = $("#receiverName").val().trim();
        var receiverPhone = $("#receiverPhone").val().trim();
        var cityId = $("#cityCode").val().trim();
        var storeId = $("#storeCode").val().trim();
        var orderStatus = $("#orderStatus").val().trim();
        var isPayUp = $("#isPayUp").val().trim();
        var queryOrderInfo = $("#queryOrderInfo").val().trim().trim();
        $("#dataGrid").bootstrapTable('destroy');
        if (null != queryOrderInfo && "" != queryOrderInfo) {
            initDateGird('/rest/order/selfTakeOrder/page/infoGrid?info=' + queryOrderInfo);
        }else{
            initDateGird('/rest/order/selfTakeOrder/page/conditionGrid?beginTime=' + beginTime + '&endTime=' + endTime + '&memberName=' + memberName + '&shippingAddress=' + shippingAddress
                    + '&sellerName=' + sellerName + '&memberPhone=' + memberPhone + '&receiverName=' + receiverName + '&receiverPhone=' + receiverPhone+'&cityId=' + cityId + '&storeId=' + storeId
                    + '&isPayUp=' + isPayUp + '&orderStatus=' + orderStatus
            )
        }
    }


</script>
</body>