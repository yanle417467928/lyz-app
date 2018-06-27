<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>

</head>
<body>

<section class="content-header">
    <h1>装饰公司账单详情</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:history.back();">小型装饰公司未出账单</a></li>
        <li class="active">装饰公司账单详情</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="row" style="margin-top: 1%">
            <div class="col-xs-12">
                <div class="col-sm-3" style="margin-left: 2%">
                    <h3>账单日期信息</h3>
                    <p>
                        <span>账单日期:</span>
                        <span>${maFitBillVO.billStartDate?string("yyyy-MM-dd")}</span>
                        <span>~</span>
                        <span>${maFitBillVO.billEndDate?string("yyyy-MM-dd")}</span><br/>
                        <span>账单还款截止日:</span>
                        <span style="color: red;font-weight:bold">${maFitBillVO.repaymentDeadlineDate?string("yyyy-MM-dd")}</span>
                    </p>
                </div>
                <div class="col-sm-3">
                    <h3>上期账单信息</h3>
                    <p>
                        <span>上期滞纳金:</span>
                        <span>${maFitBillVO.priorPaidInterestAmount!''}</span><br/>
                        <span>上期未还款金额:</span>
                    </p>
                </div>
                <div class="col-sm-3">
                    <h3>本期账单信息</h3>
                    <p>
                        <span>本期调整金额:</span>
                        <span>${maFitBillVO.priorPaidInterestAmount!''}</span><br/>
                        <span>本期账单金额:</span>
                        <span>${maFitBillVO.priorPaidInterestAmount!''}</span><br/>
                        <span>本期已还金额:</span>
                        <span>${maFitBillVO.priorPaidInterestAmount!''}</span>
                    </p>
                </div>
                <div class="col-sm-2">
                    <h3>账单汇总信息</h3>
                    <p>
                        <span>账单总金额:</span>
                        <span style="color: red;font-weight:bold">${maFitBillVO.billTotalAmount!""}</span><br/>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;本期应还:</span>
                        <span style="color: red;font-weight:bold">${maFitBillVO.billTotalAmount!""}</span><br/>
                        <span>最低还款额:</span>
                        <span style="color: red;font-weight:bold">${maFitBillVO.billTotalAmount!""}</span>
                    </p>
                </div>
            </div>
        </div>
        <hr>
        <div class="row" style="margin-top: -1%;">
            <div class="col-xs-12">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab_1-1" data-toggle="tab" >账单明细</a></li>
                        <li><a href="#tab_1-2" data-toggle="tab" >账单还款记录</a></li>
                    </ul>
                    <div id="toolbar1" class="form-inline ">
                        <input name="startTime" <#--onchange="findByCondition()"--> type="text"
                               class="form-control datepicker" id="startTime" style="width: 140px;"
                               placeholder="出货开始时间"
                               readonly>
                        <input name="endTime" <#--onchange="findByCondition()"--> type="text"
                               class="form-control datepicker" id="endTime" style="width: 140px;"
                               placeholder="出货结束时间"
                               readonly>
                        <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                            <input type="text" name="info" id="info" class="form-control "
                                   style="width:auto;" placeholder="请输入订单号" onkeypress="findBykey()">
                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findBillByCondition()">查找</button>
                           </span>
                        </div>
                    </div>
                    <div id="toolbar2" class="form-inline ">
                        <input name="startTime" <#--onchange="findByCondition()"--> type="text"
                               class="form-control datepicker" id="startTime" style="width: 140px;"
                               placeholder="出货开始时间"
                               readonly>
                        <input name="endTime" <#--onchange="findByCondition()"--> type="text"
                               class="form-control datepicker" id="endTime" style="width: 140px;"
                               placeholder="出货结束时间"
                               readonly>
                        <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                            <input type="text" name="info" id="info" class="form-control "
                                   style="width:auto;" placeholder="请输入订单号" onkeypress="findBykey()">
                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findBillByCondition()">查找</button>
                           </span>
                        </div>
                    </div>
                    <div class="tab-content">
                        <div class="tab-pane active" id="tab_1-1">
                            <div class="box-body table-reponsive">
                                <table id="dataGrid1" class="table table-bordered table-hover">
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane" id="tab_1-2">
                            <div class="box-body table-reponsive">
                                <table id="dataGrid2" class="table table-bordered table-hover">
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>


<script>

    $(function () {
        inDataGrid1();
        inDataGrid2();
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });

    function inDataGrid1() {
        $("#dataGrid1").bootstrapTable('destroy');
        var guideId = $('#guideId').val();
        $grid.init($('#dataGrid1'), $('#toolbar1'), '/rest/guideLine/availableCreditChangePage/grid/' + guideId, 'get', false, function (params) {
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
                field: 'id',
                title: 'ID',
                align: 'center'
            }, {
                field: 'availableCreditChangId.creditLimitAvailableChangeAmount',
                title: '订单日期',
                align: 'center'
            }, {
                field: 'availableCreditChangId.creditLimitAvailableAfterChange',
                title: '订单出货日',
                align: 'center'
            },
            {
                field: 'referenceNumber',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '金额',
                align: 'center',
                formatter: function (value, row) {
                    if ('PLACE_ORDER' == value) {
                        return '订单消费';
                    } else if ('RETURN_ORDER' == value) {
                        return '退单返还';
                    } else if ('CANCEL_ORDER' == value) {
                        return '取消订单返还';
                    } else if ('TEMPORARY_ADJUSTMENT' == value) {
                        return '临时额度调整';
                    } else if ('ADMIN_RECHARGE' == value) {
                        return '管理员修改';
                    } else if ('ORDER_REPAYMENT' == value) {
                        return '订单还款';
                    } else if ('TEMPORARY_CLEAR' == value) {
                        return '临时额度清零';
                    } else if ('FIXEDAMOUNT_ADJUSTMENT' == value) {
                        return '固定额度调整';
                    } else if ('AVALIABLED_CHANGE_BY_FIXE' == value) {
                        return '可用额度因固定额度调整修改';
                    } else if ('AVALIABLED_CHANGE_BY_TEMP' == value) {
                        return '可用额度因临时额度调整修改';
                    }
                }
            }
        ]);
    }


    function inDataGrid2() {
        $("#dataGrid2").bootstrapTable('destroy');
        var guideId = $('#guideId').val();
        $grid.init($('#dataGrid2'), $('#toolbar2'), '/rest/guideLine/tempCreditChangePage/grid/' + guideId, 'get', false, function (params) {
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
                field: 'id',
                title: 'ID',
                align: 'center'
            }, {
                field: 'tempCreditChangeId.tempCreditLimitChangeAmount',
                title: '订单日期',
                align: 'center'
            }, {
                field: 'tempCreditChangeId.tempCreditLimitAfterChange',
                title: '订单出货日',
                align: 'center',
            },
            {
                field: 'referenceNumber',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '金额',
                align: 'center',
                formatter: function (value, row) {
                    if ('PLACE_ORDER' == value) {
                        return '订单消费';
                    } else if ('RETURN_ORDER' == value) {
                        return '退单返还';
                    } else if ('CANCEL_ORDER' == value) {
                        return '取消订单返还';
                    } else if ('TEMPORARY_ADJUSTMENT' == value) {
                        return '临时额度调整';
                    } else if ('ADMIN_RECHARGE' == value) {
                        return '管理员修改';
                    } else if ('ORDER_REPAYMENT' == value) {
                        return '订单还款';
                    } else if ('TEMPORARY_CLEAR' == value) {
                        return '临时额度清零';
                    } else if ('FIXEDAMOUNT_ADJUSTMENT' == value) {
                        return '固定额度调整';
                    } else if ('AVALIABLED_CHANGE_BY_FIXE' == value) {
                        return '可用额度因固定额度调整修改';
                    } else if ('AVALIABLED_CHANGE_BY_TEMP' == value) {
                        return '可用额度因临时额度调整修改';
                    }
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

</script>
</body>