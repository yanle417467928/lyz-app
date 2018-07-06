<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
</head>
<body>

<section class="content-header">
    <h1>装饰公司账单详情</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:history.back();">小型装饰公司历史账单</a></li>
        <li class="active">装饰公司账单详情</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="row" style="margin-top: 1%">
            <input name="billNo" type="hidden" id="billNo" value="${maFitBillVO.billNo!''}" readonly>
            <div class="col-xs-12">
                <div class="col-sm-3" style="margin-left: 2%">
                    <h3>账单日期信息</h3>
                    <p>
                        <span>账单开始日期:</span>
                        <span>${maFitBillVO.billStartDate?string("yyyy-MM-dd")}</span><br/>
                        <span>账单结束日期:</span>
                        <span>${maFitBillVO.billEndDate?string("yyyy-MM-dd")}</span><br/>
                        <span>还款截止日期:</span>
                        <span style="color: red;font-weight:bold">${maFitBillVO.repaymentDeadlineDate?string("yyyy-MM-dd")}</span>
                    </p>
                </div>
                <div class="col-sm-3">
                    <h3>上期账单信息</h3>
                    <p>
                        <span>上&nbsp;期&nbsp;滞&nbsp;纳&nbsp;金:</span>
                        <span>${maFitBillVO.priorNotPaidBillAmount!''}</span><br/>
                        <span>上期未还金额:</span>
                        <span>${maFitBillVO.priorNotPaidInterestAmount!''}</span>
                    </p>
                </div>
                <div class="col-sm-3">
                    <h3>本期账单信息</h3>
                    <p>
                        <span>本期调整金额:</span>
                        <span>${maFitBillVO.currentAdjustmentAmount!''}</span><br/>
                        <span>本期账单金额:</span>
                        <span>${maFitBillVO.currentBillAmount!''}</span><br/>
                        <span>本期已还金额:</span>
                        <span>${maFitBillVO.currentPaidAmount!''}</span>
                    </p>
                </div>
                <div class="col-sm-2">
                    <h3>账单汇总信息</h3>
                    <p>
                        <span>账单总金额:</span>
                        <span style="color: red;font-weight:bold">${maFitBillVO.billTotalAmount!""}</span><br/>
                    </p>
                </div>
            </div>
        </div>
        <hr>
        <div class="row" style="margin-top: -1%;">
            <div class="col-xs-12">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab_1-1" data-toggle="tab">账单还款记录</a></li>
                    </ul>
                    <div id="toolbar2" class="form-inline ">
                        <input name="startTime2" onchange="findRepaymentByCondition()" type="text"
                               class="form-control datepicker" id="startTime2" style="width: 140px;"
                               placeholder="还款开始时间">
                        <input name="endTime2" onchange="findRepaymentByCondition()" type="text"
                               class="form-control datepicker" id="endTime2" style="width: 140px;"
                               placeholder="还款结束时间">
                        <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                            <input type="text" name="info2" id="info2" class="form-control "
                                   style="width:auto;" placeholder="请输入还款单号" onkeypress="findBykey()">
                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findRepaymentByCondition()">查找</button>
                           </span>
                        </div>
                    </div>
                    <div class="box-body table-reponsive">
                        <table id="dataGrid2" class="table table-bordered table-hover">
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="orderDetail">
        <div class="modal-dialog" style="width: 50%;">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">还款订单</h4>
                </div>
                <div class="modal-body">
                    <div class="box-body table-reponsive">
                        <table id="dataGrid3" class="table table-bordered table-hover">
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                </div>
            </div>
        </div>
    </div>
</section>


<script>
    var billNo = $('#billNo').val();

    $(function () {
        //inDataGrid1();
        inDataGrid2();
        $('#btn_back').on('click', function () {
            window.history.back()
        });

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
    });


    function inDataGrid2() {
        $("#dataGrid2").bootstrapTable('destroy');
        $grid.init($('#dataGrid2'), $('#toolbar2'), '/rest/fitBill/payOrderBill/page/' + billNo, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
                startTime: $('#startTime2').val(),
                endTime: $('#endTime2').val(),
                repaymentNo: $('#info2').val()
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'id',
                title: 'ID',
                align: 'center'
            }, {
                field: 'repaymentNo',
                title: '还款单号',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '<a class="scan" href="#">' + '未知' + '</a>';
                    } else {
                        return '<a class="scan" href="#" onclick="showDetail(' + '\'' + value + '\'' + ')">' + value + '</a>';
                    }
                }
            }, {
                field: 'repaymentUserName',
                title: '还款人',
                align: 'center'
            }, {
                field: 'repaymentSystem',
                title: '还款系统',
                align: 'center',
                formatter: function (value, row) {
                    if ('MANAGE' == value) {
                        return '后台';
                    } else {
                        return 'APP';
                    }
                }
            },
            {
                field: 'repaymentTime',
                title: '还款时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-';
                    } else if (null != value) {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'totalRepaymentAmount',
                title: '还款总金额',
                align: 'center'
            }
        ]);
    }

    function inDataGrid3(repaymentNo) {
        $("#dataGrid3").bootstrapTable('destroy');
        $grid.init($('#dataGrid3'), null, '/rest/fitBill/billOrderDetail/page/' + repaymentNo, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'orderTime',
                title: '订单日期',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-';
                    } else if (null != value) {
                        return formatDateTime(value);
                    }
                }
            }, {
                field: 'shipmentTime',
                title: '订单出货日',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-';
                    } else if (null != value) {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'orderNo',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'totalAmount',
                title: '金额',
                align: 'center'
            }
        ]);
    }


    function showDetail(repaymentNo) {
        inDataGrid3(repaymentNo);
        $('#payOrderBill').modal('hide');
        $('#orderDetail').modal('show');

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


    function findRepaymentByCondition() {
        var startTime2 = $('#startTime2').val();
        var endTime2 = $('#endTime2').val();
        var info2 = $('#info2').val();
        if (startTime2 == null || startTime2 == '') {
            var a = 0
        }
        if (endTime2 == null || endTime2 == '') {
            var b = 0
        }
        if (info2 == null || info2 == '') {
            var c = 0
        }
        if (a == 0 && b == 0 && c == 0) {
            return;
        } else {
            inDataGrid2();
        }
    }

    function findBykey() {
        if (event.keyCode == 13) {
            findRepaymentByCondition();
        }
    }

</script>
</body>