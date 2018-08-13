
var sourceUrl;
var rotationImage;

$(function () {
    findCitylist()
    findStorelist();

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
            keywords: params.search,
            company: $("#company").val(),
            status: $("#status").val(),
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
        url: '/rest/stores/find/decorativeCompany',
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
        url: '/rest/stores/find/company/StoresListByCityId/' + cityId,
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
    var status = $('#status').val();
    var company = $('#company').val();
    $("#dataGrid").bootstrapTable('destroy');
    if (orderNumber != null && orderNumber != "") {
        initDateGird('/rest/company/order/page/byOrderNumber/' + orderNumber);
    } else {
        initDateGird('/rest/company/order/page/condition?cityId=' + cityId + '&storeId=' + storeId
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
