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
                                <label class="col-xs-5" style="padding-right: 0px">会员电话：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="memberPhone" id="memberPhone" class="form-control" \>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">建单人姓名：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="creatorName" id="creatorName" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">

                        <div class=" col-xs-3" style="margin-left: 2%">
                            <select name="store" id="storeCode" class="selectpicker" style="padding: 2%"
                                    onchange="findOrderByCondition()" data-live-search="true">
                                <option value="-1">选择门店</option>
                            </select>
                        </div>
                        <div class=" col-xs-2" style="margin-left: -3.5%">
                            <select name="orderStatus" id="orderStatus" class="form-control" style="width:auto;"
                                    onchange="findOrderByCondition()" data-live-search="true">
                                <option value="-1">退单状态</option>
                                <option value="PENDING_PICK_UP">已提交</option>
                                <option value="CANCELING">取消中</option>
                                <option value="CANCELED">已取消</option>
                                <option value="PENDING_REFUND">待退款</option>
                                <option value="FINISHED">已完成</option>
                            </select>
                        </div>

                        <div class="col-xs-4" style="margin-left: -5%">
                            <input type="text" name="queryOrderInfo" id="queryOrderInfo" class="form-control"
                                   style="width:auto;"
                                   placeholder="请输订单号" onkeypress="findBykey()">
                            <span class="">
                                <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                        onclick="findOrderByCondition()">查找</button>
                        </span>
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


    $(function () {
        initDateGird('/rest/returnOrder/page/grid');
        findStoreSelection();
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
    });


    function findStoreSelection() {
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
            title: '门店名称',
            align: 'center'
        }, {
            field: 'returnNo',
            title: '退货单号',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="/views/admin/returnOrder/detail/' + value + '" target="_blank">' + value + '</a>';
                }
            }
        }, {
            field: 'returnTime',
            title: '退货时间',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<span class="label label-danger">-</span>';
                } else {
                    return formatDateTime(value);
                }
            }
        }, {
            field: 'creatorName',
            title: '建单人姓名',
            align: 'center',
        }, {
            field: 'returnStatus',
            title: '退单状态',
            align: 'center',
            formatter: function (value, row, index) {
                if ('PENDING_PICK_UP' === value) {
                    return '已提交';
                } else if ('RETURNING' === value) {
                    return '退货中';
                } else if ('CANCELING' === value) {
                    return '取消中';
                } else if ('CANCELED' === value) {
                    return '已取消';
                } else if ('PENDING_REFUND' === value) {
                    return '待退款';
                } else if ('FINISHED' === value) {
                    return '已完成';
                }
            }
        }, {
            field: 'returnType',
            title: '退货类型',
            align: 'center',
            formatter: function (value, row, index) {
                if ('CANCEL_RETURN' == value) {
                    return '取消退货';
                } else if ('REFUSED_RETURN' == value) {
                    return '拒签退货';
                } else if ('NORMAL_RETURN' == value) {
                    return '正常退货';
                } else {
                    return '-';
                }
            }
        }, {
            field: 'orderType',
            title: '退货方式',
            align: 'center',
            formatter: function (value, row, index) {
                if ('SHIPMENT' == value) {
                    return '出货';
                } else if ('COUPON' == value) {
                    return '买券';
                } else {
                    return '-';
                }
            }
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
        var queryOrderInfo = $("#queryOrderInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var storeId = $("#storeCode").val();
        var orderStatus = $("#orderStatus").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var memberName = $("#memberName").val();
        var memberPhone = $("#memberPhone").val();
        var creatorName = $("#creatorName").val();
        initDateGird('/rest/returnOrder/page/screenGrid?storeId=' + storeId + '&orderStatus=' + orderStatus
                + '&beginTime=' + beginTime+ '&endTime=' + endTime+ '&memberName='
                + memberName+ '&memberPhone=' + memberPhone+ '&creatorName=' + creatorName+ '&queryOrderInfo=' + queryOrderInfo);
    }


    function findOrderByInfo() {
        var queryOrderInfo = $("#queryOrderInfo").val();
        $('#storeCode').val("-1");
        $('#orderStatus').val("-1");
        $("#dataGrid").bootstrapTable('destroy');
        if (null == queryOrderInfo || "" == queryOrderInfo) {
            initDateGird('/rest/returnOrder/page/grid');
        } else {
            initDateGird('/rest/returnOrder/page/infoGrid?info=' + queryOrderInfo);
        }
    }
    function findBykey() {
        if (event.keyCode == 13) {
            findOrderByInfo();
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



</script>
</body>