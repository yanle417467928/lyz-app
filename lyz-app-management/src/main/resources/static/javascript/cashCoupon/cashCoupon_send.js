$(function () {
    findCitylist()
    findStorelist();
    initDateGird('/rest/customers/page/grid');
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
            })
            $("#cityCode").append(city);
        }
    });
}


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
        field: 'cusId',
        title: '顾客ID',
        align: 'center',
        visible:false
    }, {
        field: 'name',
        title: '顾客姓名',
        align: 'center',
        events: {
            'click .scan': function (e, value, row) {
                $page.information.show(row.cusId);
            }
        }
    }, {
        field: 'mobile',
        title: '顾客电话',
        align: 'center'
    }, {
        field: 'store.storeName',
        title: '归属门店',
        align: 'center',
        formatter: function (value, row, index) {
            if (null === value || "" == value) {
                return '-'
            } else {
                return value;
            }
        }
    }, {
        field: 'status',
        title: '是否生效',
        align: 'center',
        formatter: function (value, row, index) {
            if (true === value) {
                return '<span class="label label-primary">是</span>';
            }else if(false === value){
                return '<span class="label label-danger">否</span>';
            } else {
                return '-';
            }
        }
    }, {
        field: 'light',
        title: '顾客灯号',
        align: 'center',
        formatter: function (value, row, index) {
            if ('绿灯' === value) {
                return '<span class="label label-success">绿灯</span>';
            } else if ('红灯' === value) {
                return '<span class="label label-danger">红灯</span>';
            } else if ('黄灯' === value) {
                return '<span class="label label-warning">黄灯</span>';
            } else if ('熄灯' === value) {
                return '<span class="label label-deafult">熄灯</span>';
            } else {
                return '<span class="label label-danger">-</span>';
            }
        }
    },
        {
            field: 'qty',
            title: '发券数量',
            align: 'center',
            formatter:function (value,row,index) {
                return "<input type='number' id='qty_"+row.cusId+"' value='0'>";
            }
        },
        {
            field: '',
            title: '操作',
            align: 'center',
            formatter:function (value,row,index) {
                return "<a id='send_"+row.cusId+"' href='javascript:send("+row.cusId+");'>发券</a>";
            }
        }
    ]);
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

function findCusByCity(cityId) {
    initSelect("#storeCode", "选择门店");
    findCusByCityId(cityId);
    $("#queryCusInfo").val('');
    if(cityId==-1){
        findStorelist();
        return false;
    };
    /*  initSelect("#guideCode", "选择导购")*/
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
            $('#storeCode').selectpicker('refresh');
            $('#storeCode').selectpicker('render');
        }
    });
}



function findCusByCityId(cityId) {
    $("#dataGrid").bootstrapTable('destroy');
    if (cityId == -1) {
        initDateGird('/rest/customers/page/grid');
    } else {
        initDateGird('/rest/customers/page/cityGrid/' + cityId);
    }
}

function findCusByStoreId() {
    $("#queryCusInfo").val('');
    var storeId = $("#storeCode").val();
    var cityId = $("#cityCode").val();
    $("#dataGrid").bootstrapTable('destroy');
    if (storeId == -1&&cityId==-1) {
        initDateGird('/rest/customers/page/grid');
    }else if(storeId != -1){
        initDateGird('/rest/customers/page/storeGrid/' + storeId);
    }else if(storeId == -1&&cityId!=-1){
        initDateGird('/rest/customers/page/cityGrid/' + cityId);
    }
}

function findCusByNameOrPhone() {
    var queryCusInfo = $("#queryCusInfo").val();
    $('#cityCode').val("-1");
    initSelect("#storeCode", "选择门店");
    findStorelist();
    /*       initSelect("#guideCode", "选择导购");*/
    $("#dataGrid").bootstrapTable('destroy');
    if (null == queryCusInfo || "" == queryCusInfo) {
        initDateGird('/rest/customers/page/grid');
        return false;
    }
    var isNumber = checkNumber(queryCusInfo);
    if (isNumber) {
        initDateGird('/rest/customers/page/phoneGrid/' + queryCusInfo);
    } else {
        initDateGird('/rest/customers/page/nameGrid/' + queryCusInfo);
    }
}


function checkNumber(theObj) {
    var reg = /^[0-9]*[1-9][0-9]*$/;
    if (reg.test(theObj)) {
        return true;
    }
    return false;
}

function initSelect(select, optionName) {
    $(select).empty();
    var selectOption = "<option value=-1>" + optionName + "</option>";
    $(select).append(selectOption);
}

/**
 * 发券
 * **/
function  send(customerId) {

    var cashCouponId = $("#cashCouponId").val();
    var qty = $("#qty_"+customerId).val();

    var re = /^[0-9]+.?[0-9]*$/;
    if(qty <= 0 || !re.test(qty)){
        $notify.warning("券数量有误");
        return false;
    }
    // 防止重复发送
    $("#send_"+customerId).attr("href","#");
    $http.ajax('/rest/cashCoupon/send','POST',{'customerId':customerId,'cashCouponId':cashCouponId,'qty': qty},function (result) {
        // 防止重复发送
        //$("#send_"+customerId).attr("href","javascript:send("+customerId+")");
        if (0 === result.code) {
            $notify.info(result.message);

            setTimeout(function () {
                location.href = "/view/cashCoupon/send/"+cashCouponId;
            },1000)

        } else {
            $notify.danger(result.message);
        }
    })
}

/***
 * 批量发券
 * **/
function  sendBatch() {

    var cashCouponId = $("#cashCouponId").val();
    var qty = $("#common_qty").val();
    var customerIds = new Array();

    var selected = $("#dataGrid").bootstrapTable('getSelections');
    for (var i = 0; i < selected.length; i++) {
        var data = selected[i];
        customerIds.push(data.cusId);
    }

    if (customerIds.length == 0){
        $notify.warning("请选择顾客");
        return false;
    }

    customerIds = JSON.stringify(customerIds);

    var re = /^[0-9]+.?[0-9]*$/;
    if(qty <= 0 || !re.test(qty)){
        $notify.warning("券数量有误");
        return false;
    }
    // 防止重复请求
    $("#oneButtonSend").attr("onclick","");
    $http.ajax('/rest/cashCoupon/sendBatch','POST',{'customerIds':customerIds,'cashCouponId':cashCouponId,'qty': qty},function (result) {
       // $("#oneButtonSend").attr("onclick","sendBatch()");
        if (0 === result.code) {
            $notify.info(result.message);

            setTimeout(function () {
                location.href = "/view/cashCoupon/send/"+cashCouponId;
            },1000)
        } else {
            $notify.danger(result.message);
        }
    })
}
