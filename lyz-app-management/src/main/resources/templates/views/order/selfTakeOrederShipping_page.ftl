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
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                    模糊查询
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="form-inline">
                                    <div class="row text-center">
                                        <div class="col-xs-12" style="margin-top: 5px">
                                            <div class="col-xs-4 ">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">开始时间:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input name="beginTime" type="text" class="form-control datepicker" id="beginTime" placeholder="开始时间">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">结束时间:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input name="endTime" type="text" class="form-control datepicker" id="endTime" placeholder="结束时间">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4 ">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">下单人姓名:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="creatorName" id="creatorName" class="form-control" \>
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
                                                        <input type="text" name="shippingAddress" id="shippingAddress" class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">收货人姓名:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="receiverName" id="receiverName" class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">下单人电话:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="creatorPhone" id="creatorPhone" class="form-control" \>
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
                                                        <input type="text" name="receiverPhone" id="receiverPhone" class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                    <label class="col-xs-5" style="padding-right: 0px">导购姓名:</label>
                                                    <div class=" col-xs-7" style="padding-left: 0px">
                                                        <input type="text" name="sellerName" id="sellerName" class="form-control" \>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-4">
                                                <div class="col-xs-12">
                                                        <button type="button" name="search" id="search-btn" class="btn btn-primary btn-search"
                                                                onclick="findOrderByInfo()">查找</button>
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
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
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
<script>


    $(function () {
        initDateGird('/rest/order/selfTakeOrederShipping/page/grid');
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
            field: 'storeName',
            title: '门店',
            align: 'center'
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
            title: '操作',
            align: 'center',
            formatter: function (value, row) {
                return '<button class="btn btn-primary btn-xs" onclick="showDetails(' + row.id + ')"> 订单详情</button>';
            }
        }]);
    }


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

    function showDetails(id) {
        window.location.href = '/views/admin/freight/orderFreightDetail/' + id;
    }

    function showChangeDetail() {
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

    var changeDecimalBuZero = function (number, bitNum) {
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