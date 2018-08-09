
$(function () {
    $('.btn-cancel').on('click', function () {
        history.go(-1);
    });
});
function openFreightModal() {
    $('#freightModal').modal('show');
}

function hideFreightModal() {
    $('#freightModal').modal('hide');
}

var $page = {
    information: {
        show: function (orderNumber) {
            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);
                $.ajax({
                    url: '/rest/order/delivery/' + orderNumber,
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
                            $('#menuTitle').html(" 物流详情");

                            if (null === data.warehouse) {
                                data.warehouse = '-';
                            }
                            $('#warehouse').html(data.warehouse);

                            if (null === data.deliveryClerkName) {
                                data.deliveryClerkName = '-';
                            }
                            $('#deliveryClerkName').html(data.deliveryClerkName);

                            if (null === data.shipmentNumber) {
                                data.shipmentNumber = '-';
                            }
                            $('#shipmentNumber').html(data.shipmentNumber);

                            if (null === data.deliveryTime) {
                                data.deliveryTime = '-';
                            }
                            $('#deliveryTime').html(data.deliveryTime);

                            if (null != data.orderDeliveryInfoDetailsList) {
                                var time = '';
                                var status = '';
                                var b = '';
                                if (data.orderDeliveryInfoDetailsList.length != 0) {
                                    for (var i = 0; i < data.orderDeliveryInfoDetailsList.length; i++) {
                                        time = formatDateTime(data.orderDeliveryInfoDetailsList[i].createTime);
                                        if ("INITIAL" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '等待物流接收';
                                        } else if ("RECEIVED" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '已接收';
                                        } else if ("ALREADY_POSITIONED" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '已定位';
                                        } else if ("PICKING_GOODS" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '已拣货';
                                        } else if ("LOADING" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '已装车';
                                        } else if ("SEALED_CAR" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '已封车';
                                        } else if ("SENDING" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '未投妥';
                                        } else if ("CONFIRM_ARRIVAL" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '已签收';
                                        } else if ("REJECT" == data.orderDeliveryInfoDetailsList[i].logisticStatus) {
                                            status = '拒签';
                                        }

                                        var a = '<ul><span class="logisticCreateTime" id="logisticCreateTime">' + time + '</span><i class="fa fa-circle" style="color: red"></i> <span class="logisticStatus" id="logisticStatus" style="padding-left: 100px">' + status + '</span></ul><div style="padding-left: 140px"> <ul class="timeline" style="margin-bottom: 0px"> <i class="fa fa-circle" style="color: white"></i> </ul></div>';
                                        /*        $('#logisticCreateTime').html();
                                         $('#logisticStatus').html();*/
                                        b += a;
                                    }
                                }
                                $('#test').html(b);
                            } else {
                                date.orderDeliveryInfoDetailsList = '';
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
