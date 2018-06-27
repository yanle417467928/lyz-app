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
                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="140px"
                            onchange="findBillByCondition()" data-live-search="true">
                        <option value="-1">选择装饰公司</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="info" id="info" class="form-control "
                               style="width:auto;" placeholder="请输入账单单号或名称" onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findBillByCondition()">查找</button>
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
        initDateGird('/rest/fitBill/history/page/grid');
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
            url: '/rest/stores/findSmallFitStoresListByStoreId',
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
                keywords: $("#info").val(),
                storeId: $("#storeCode").val()
            }
        }, [{
            checkbox: true,
            title: '选择'
        },{
            field: 'billName',
            title: '账单名称',
            align: 'center'
        }, {
            field: 'billNo',
            title: '账单单号',
            align: 'center',
            formatter: function (value, row, index) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="/views/admin/fit/bill/historyDetail/'+ value+'" target="_blank">' + value + '</a>';
                }
            }
        }, {
            field: 'storeName',
            title: '装饰公司名称',
            align: 'center'
        }, {
            field: 'billStartDate',
            title: '账单开始日期',
            align: 'center'
        }, {
            field: 'billEndDate',
            title: '账单结束日期',
            align: 'center'
        }, {
            field: 'repaymentDeadlineDate',
            title: '账单还款截止日',
            align: 'center'
        }, {
            field: 'billTotalAmount',
            title: '账单总额',
            align: 'center'
        }, {
            field: 'currentPaidAmount',
            title: '已还金额',
            align: 'center'
        }, {
            field: 'currentUnpaidAmount',
            title: '未还金额',
            align: 'center'
        }

        ]);
    }



    function findBillByCondition() {
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird('/rest/fitBill/notOut/page/grid');
    }



    function findBykey(){
        if(event.keyCode==13){
            findBillByCondition();
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