<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
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
                <div id="toolbar" class="form-inline">
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_time" type="button" class="btn btn-default" onclick="showChangeDetail()">
                        <span class="glyphicon glyphicon-time" aria-hidden="true"></span> 修改记录
                    </button>
                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findOrderByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="width:auto;"
                            onchange="findOrderByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryOrderInfo" id="queryOrderInfo" class="form-control "
                               style="width:auto;" placeholder="请输入单号、下单人或下单人电话..">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findOrderByInfo()">查找</button>
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
<#--<div id="information" class="modal fade" tabindex="-1" role="dialog">
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
                    <ul id="storeDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>城市</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>订单号</b> <a class="pull-right" id="ordNo"></a>
                        </li>
                        <li class="list-group-item">
                            <b>下单人</b> <a class="pull-right" id="creatorName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>下单人电话</b> <a class="pull-right" id="creatorPhone"></a>
                        </li>
                        <li class="list-group-item">
                            <b>下单时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>运费</b> <a class="pull-right" id="freight"></a>
                        </li>
                       <li class="list-group-item" id="goodsInfo">
                            <b>商品信息</b>
                            <table id="goodsInfoDetail" class="table table-bordered table-hover">
                            </table>
                        </li>
                        <li class="list-group-item">
                            <b>收货人</b> <a class="pull-right" id="receiver"></a>
                        </li>
                        <li class="list-group-item">
                            <b>收货人电话</b> <a class="pull-right" id="receiverPhone"></a>
                        </li>
                        <li class="list-group-item">
                            <b>收货人地址</b> <a class="pull-right" id="shippingAddress"></a>
                        </li>
                        <li class="list-group-item">
                            <b>备注</b> <a class="pull-right" id="remark"></a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>-->
<script>


    $(function () {
        initDateGird('/rest/orderFreight/page/grid');
        findCitySelection();
        findStoreSelection();
        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/freight/edit/{id}?parentMenuId=${parentMenuId!'0'}')
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
        $.ajax({
            url: '/rest/stores/findAllStorelist',
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
            field: 'id',
            title: 'ID',
            align: 'center',
            visible: false
        }, {
            field: 'cityName',
            title: '城市',
            align: 'center'
        }, {
            field: 'storeId.storeName',
            title: '门店',
            align: 'center'
        }, {
            field: 'ordNo',
            title: '订单号',
            align: 'center'
/*            events: {
                'click .scan': function(e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function(value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }*/
        }, {
            field: 'creatorName',
            title: '下单人',
            align: 'center',
        }, {
            field: 'creatorPhone',
            title: '下单人电话',
            align: 'center'
        }, {
            field: 'simpleOrderBillingDetails.freight',
            title: '运费',
            align: 'center',
            formatter: function (value, row) {
                return '¥ '+changeDecimalBuZero(value,2);
            }
        }, {
            title: '操作',
            align: 'center',
            formatter: function (value, row) {
                return '<button class="btn btn-primary btn-xs" onclick="showDetails(' + row.id + ')"> 订单详情</button>';
            }
        }]);
    }

/*    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/orderFreight/' + id,
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
                                $('#menuTitle').html("订单详情");


                                if (null === data.cityName) {
                                    data.cityName = '-';
                                }
                                $('#cityName').html(data.cityName);


                                if (null === data.storeId.storeName) {
                                    data.storeId.storeName = '-';
                                }
                                $('#storeName').html(data.storeId.storeName);


                                if (null === data.ordNo) {
                                    data.ordNo = '-';
                                }
                                $('#ordNo').html(data.ordNo);


                                if (null === data.creatorName) {
                                    data.creatorName = '-';
                                }
                                $('#creatorName').html(data.creatorName);

                                if (null === data.creatorPhone) {
                                    data.creatorPhone = '-';
                                }
                                $('#creatorPhone').html(data.creatorPhone);

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                }
                                $('#createTime').html(formatDateTime(data.createTime));


                                if (null != data.orderGoodsInfoList) {
                                    var goodsfrom ='';
                                    if(data.orderGoodsInfoList.length!=0){
                                        goodsfrom ='<tr class="text-center"><td>商品编码</td><td>商品名称</td><td>商品数量</td><td>商品单价</td><td>商品金额</td></tr>'
                                        for(var i=0;i<data.orderGoodsInfoList.length;i++){
                                            goodsfrom +='<tr class="text-center"><td>'+data.orderGoodsInfoList[i].sku+'</td><td>'+
                                                    data.orderGoodsInfoList[i].skuName+'</td><td>'+
                                                    data.orderGoodsInfoList[i].orderQty +'</td><td>'+
                                                    data.orderGoodsInfoList[i].sharePrice+'</td><td>'+
                                                    data.orderGoodsInfoList[i].totalGoodsPrice +'</td></tr>';
                                        }
                                    }
                                    $('#goodsInfoDetail').html(goodsfrom);
                                }


                                if (null === data.simpleOrderBillingDetails.freight) {
                                    data.simpleOrderBillingDetails.freight = '-';
                                }
                                $('#freight').html(data.simpleOrderBillingDetails.freight);


                                if (null === data.orderLogisticsInfo.receiver) {
                                    data.orderLogisticsInfo.receiver = '-';
                                }
                                $('#receiver').html(data.orderLogisticsInfo.receiver);

                                if (null === data.orderLogisticsInfo.receiverPhone) {
                                    data.orderLogisticsInfo.receiverPhone = '-';
                                }
                                $('#receiverPhone').html(data.orderLogisticsInfo.receiverPhone);

                                if (null === data.orderLogisticsInfo.shippingAddress) {
                                    data.orderLogisticsInfo.shippingAddress = '-';
                                }
                                $('#shippingAddress').html(data.orderLogisticsInfo.shippingAddress);

                                if (null === data.remark) {
                                    data.remark = '-';
                                }
                                $('#remark').html(data.remark);

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
    }*/


    function findOrderByCondition() {
        $("#queryOrderInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var storeId = $("#storeCode").val();
        if (storeId == -1 && cityId == -1) {
            initDateGird('/rest/orderFreight/page/grid');
        } else if (storeId != -1) {
            initDateGird('/rest/orderFreight/page/storeGrid?storeId=' + storeId);
        } else if (storeId == -1 && cityId != -1) {
            initDateGird('/rest/orderFreight/page/cityGrid?cityId=' + cityId);
        }
    }


    function findOrderByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        findOrderByCondition();
        if (cityId == -1) {
            findStoreSelection();
            return false;
        }
        ;
        var store;
        $.ajax({
            url: '/rest/stores/findAllStoresListByCityId/' + cityId,
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

    function findOrderByInfo() {
        var queryOrderInfo = $("#queryOrderInfo").val();
        $('#cityCode').val("-1");
        $('#enabled').val("-1");
        $("#dataGrid").bootstrapTable('destroy');
        if (null == queryOrderInfo || "" == queryOrderInfo) {
            initDateGird('/rest/orderFreight/page/grid');
        } else {
            initDateGird('/rest/orderFreight/page/infoGrid/' + queryOrderInfo);
        }
    }

    function showDetails(id){
        window.location.href = '/views/admin/freight/orderFreightDetail/'+id;
    }

    function showChangeDetail(){
        window.location.href = '/views/admin/freight/orderFreightChange';
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

    var changeDecimalBuZero= function (number, bitNum) {
        /// <summary>
        /// 小数位不够，用0补足位数
        /// </summary>
        /// <param name="number">要处理的数字</param>
        /// <param name="bitNum">生成的小数位数</param>
        var f_x = parseFloat(number);
        if (isNaN(f_x)) {
            return 0;
        }
        var s_x = number.toString();
        var pos_decimal = s_x.indexOf('.');
        if (pos_decimal < 0) {
            pos_decimal = s_x.length;
            s_x += '.';
        }
        while (s_x.length <= pos_decimal + bitNum) {
            s_x += '0';
        }
        return s_x;
    }
</script>
</body>