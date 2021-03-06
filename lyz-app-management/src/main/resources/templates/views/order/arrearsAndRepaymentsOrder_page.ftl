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
                <div id="toolbar" class="form-inline">
                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findOrderByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                            onchange="findOrderByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <select name="status" id="status" class="form-control" style="width:auto;"
                            onchange="findOrderByCondition()" data-live-search="true">
                        <option value="-1">审批状态</option>
                        <option value="2">审核已通过</option>
                        <option value="1">审核不通过</option>
                        <option value="0">审核中</option>
                    </select>
                    <select name="isPayUp" id="isPayUp" class="form-control" style="width:auto;"
                            onchange="findOrderByCondition()" data-live-search="true">
                        <option value="-1">还款状态</option>
                        <option value="1">已付清</option>
                        <option value="0">未付清</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryOrderInfo" id="queryOrderInfo" class="form-control "
                               style="width:auto;" placeholder="请输入单号" onkeypress="findBykey()">
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
        initDateGird('/rest/order/arrearsAndAgencyOrder/page/grid');
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
            field: 'id',
            title: '门店',
            align: 'center',
            visible:false
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
                    return '<a class="scan" href="/views/admin/order/arrearsAndRepaymentsOrderDetail?id=' + row.id +'&orderNumber='+ value+'">' + value + '</a>';
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
            title: '创建时间',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<span class="label label-danger">-</span>';
                } else {
                    return formatDateTime(value);
                }
            }
        }, {
            field: 'arrearsAuditStatus',
            title: '审批状态',
            align: 'center',
            formatter: function (value, row, index) {
                if ('AUDIT_PASSED' === value) {
                    return '<span class="label label-success">审核已通过</span>';
                } else if ('AUDIT_NO' === value) {
                    return '<span class="label label-danger">审核未通过</span>';
                } else if ('AUDITING' === value) {
                    return '<span class="label label-primary">审核中</span>';
                }
            }
        }, {
            field: 'isPayUp',
            title: '收款状态',
            align: 'center',
            formatter: function (value, row, index) {
                if (false == value) {
                    return '<span class="label label-danger">未收款</span>';
                } else if (true == value) {
                    return '<span class="label label-success">已收款</span>';
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

    function findBykey(){
        if(event.keyCode==13){
            findOrderByInfo();
        }
    }


    function findOrderByCondition() {
        $("#queryOrderInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var storeId = $("#storeCode").val();
        var status = $("#status").val();
        var isPayUp = $("#isPayUp").val();
        initDateGird('/rest/order/arrearsAndAgencyOrder/page/screenGrid?cityId=' + cityId + '&storeId=' + storeId + '&status=' + status + '&isPayUp=' + isPayUp);
    }


    function findOrderByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        findOrderByCondition();
        if (cityId == -1) {
            findStoreSelection();
            return false;
        };
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

    function findOrderByInfo() {
        var queryOrderInfo = $("#queryOrderInfo").val();
        $('#cityCode').val("-1");
        $('#enabled').val("-1");
        $("#dataGrid").bootstrapTable('destroy');
        if (null == queryOrderInfo || "" == queryOrderInfo) {
            initDateGird('/rest/order/arrearsAndAgencyOrder/page/grid');
        } else {
            initDateGird('/rest/order/arrearsAndAgencyOrder/page/infoGrid?info=' + queryOrderInfo);
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