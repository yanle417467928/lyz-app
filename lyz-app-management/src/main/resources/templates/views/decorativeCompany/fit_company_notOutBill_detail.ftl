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
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
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
            <input name="storeId" type="hidden" id="storeId" value="${storeId?c}" readonly>
            <input name="billNo" type="hidden" id="billNo" value="" readonly>
            <div class="col-xs-12">
                <div class="col-sm-3" style="margin-left: 2%">
                    <h3>账单日期信息</h3>
                    <p>
                        <span>账单开始日期:</span>
                        <span id="billStartDate"></span><br/>
                        <span>账单结束日期:</span>
                        <span id="billEndDate"></span><br/>
                        <span>还款截止日期:</span>
                        <span style="color: red;font-weight:bold" id="repaymentDeadlineDate"></span>
                    </p>
                </div>
                <div class="col-sm-3">
                    <h3>上期账单信息</h3>
                    <p>
                        <span>上&nbsp;期&nbsp;滞&nbsp;纳&nbsp;金:</span>
                        <span id="priorNotPaidInterestAmount"></span><br/>
                        <span>上期未还金额:</span>
                        <span id="priorNotPaidBillAmount"></span><br/>
                    </p>
                </div>
                <div class="col-sm-3">
                    <h3>本期账单信息</h3>
                    <p>
                        <span>本期退款金额:&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <span id="currentAdjustmentAmount"></span><br/>
                        <span>本期账单金额:&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        <span id="currentBillAmount"></span><br/>
                        <span>本期已还款金额:</span>
                        <span id="currentPaidAmount"></span>
                    </p>
                </div>
                <div class="col-sm-2">
                    <h3>账单汇总信息</h3>
                    <p>
                        <span>本期应还:</span>
                        <span style="color: red;font-weight:bold" id="billTotalAmount"></span><br/>
                        <span>剩余应还:</span>
                        <span style="color: red;font-weight:bold" id="remainAmount"></span>
                    </p>
                </div>
            </div>
        </div>
        <hr>
        <div class="row" style="margin-top: -1%;">
            <div class="col-xs-12">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab_1-1" data-toggle="tab">账单明细</a>
                        </li>
                        <li><a href="#tab_1-2" data-toggle="tab">账单还款记录</a></li>
                    </ul>
                    <div id="toolbar1" class="form-inline ">
                        <button id="payBill" type="button" class="btn btn-success" onclick="payBill()">
                            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;还款
                        </button>
                        <input name="startTime1" onchange="findByCondition()" type="text"
                               class="form-control datepicker" id="startTime1" style="width: 140px;"
                               placeholder="出货开始时间">
                        <input name="endTime1" onchange="findByCondition()" type="text"
                               class="form-control datepicker" id="endTime1" style="width: 140px;"
                               placeholder="出货结束时间">
                    </div>
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
    <div class="modal fade" id="payOrderBill">
        <div class="modal-dialog" style="width: 50%;">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">确认还款</h4>
                </div>
                <form id="confirmReceivablesFrom">
                    <div class="modal-body">
                        <input type="hidden" id="amount"/>
                        <div class="row">
                            <div class="col-xs-12 col-md-5">
                            </div>
                            <div class="form-group">
                                <label for="name" id="amountMoney"></label>
                                <div id="amountMoney1"></div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="name">现金(元)</label>
                                    <input type="text" class="form-control" id="cashMoney"
                                           name="cashMoney">
                                </div>
                            </div>
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="name">POS金额(元)</label>
                                    <input type="text" class="form-control" id="posMoney"
                                           name="posMoney">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="name">其他金额(元)</label>
                                    <input type="text" class="form-control" id="otherMoney"
                                           name="otherMoney">
                                </div>
                            </div>
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="name">POS交易流水号</label>
                                    <input type="text" class="form-control" id="posNumber"
                                           name="posNumber">
                                </div>
                            </div>
                        <#-- <div class="col-xs-12 col-md-6">
                             <div class="form-group">
                                 <label for="name">微信(元)</label>
                                 <input type="text" class="form-control" id="weMoney"
                                        name="weMoney">
                             </div>
                         </div>-->
                        </div>
                        <div class="row">
                        <#--   <div class="col-xs-12 col-md-6">
                               <div class="form-group">
                                   <label for="name">支付宝(元)</label>
                                   <input type="text" class="form-control" id="alipayMoney"
                                          name="alipayMoney">
                               </div>
                           </div>-->
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="name">实际收款日期</label>
                                    <input type="text" class="form-control datepicker" id="repaymentTime"
                                           name="repaymentTime">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-md-4">
                            </div>
                            <div>
                                <span id="message" style="color: red;"></span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success " id="confirmSubmit">确定
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="orderDetail">
        <div class="modal-dialog" style="width: 60%;">
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

    var storeId = $('#storeId').val();
    var ids = [];
    var billorderDetailsRequest = new Array();
    reload();
    $(function () {
        inDataGrid1();
        inDataGrid2();
        $('#btn_back').on('click', function () {
            window.history.back()
        });

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
        var form = $('#confirmReceivablesFrom');
        form.bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                cashMoney: {
                    message: '现金校验失败',
                    validators: {
                        regexp: {
                            regexp: /^[+-]?\d+(\.\d+)?$/,
                            message: '现金称只能输入正数'
                        }
                    }
                },
                posMoney: {
                    message: 'pos金额校验失败',
                    validators: {
                        regexp: {
                            regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                            message: 'pos金额称只能输入正数'
                        }
                    }
                },
                otherMoney: {
                    message: '其他金额校验失败',
                    validators: {
                        regexp: {
                            regexp: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
                            message: '其他金额只能输入正数'
                        }
                    }
                },
                posNumber: {
                    message: '流水号校验失败',
                    validators: {
                        regexp: {
                            regexp: /^\d{6}$/,
                            message: '流水号为6位数字'
                        }
                    }
                },
                repaymentTime: {
                    message: '日期校验失败',
                    validators: {
                        notEmpty: {
                            message: '日期不允许为空!'
                        },
                        regexp: {
                            regexp: /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/,
                            message: '日期格式:yyyy-MM-dd'
                        }
                    }
                }
            }
        });
        $("#confirmSubmit").click(function () {
            var cashMoney = $('#cashMoney').val();
            var posMoney = $("#posMoney").val();
            var otherMoney = $("#otherMoney").val();
            /*         var weMoney = $("#weMoney").val();
                     var alipayMoney = $("#alipayMoney").val();*/
            var posNumber = $("#posNumber").val();
            var repaymentTime = $("#repaymentTime").val();
            var amount = $("#amount").val();
            var storeId = $("#storeId").val();
            var billNo = $("#billNo").val();
            if (null == cashMoney || '' == cashMoney) {
                cashMoney = 0
            }
            if (null == posMoney || '' == posMoney) {
                posMoney = 0
            }
            if (null == otherMoney || '' == otherMoney) {
                otherMoney = 0
            }
            var totalAmount = parseFloat(cashMoney) + parseFloat(posMoney) + parseFloat(otherMoney);
            var bv = form.data('bootstrapValidator');
            bv.validate();
            var data = {};
            data["cashMoney"] = cashMoney;
            data["posMoney"] = posMoney;
            data["otherMoney"] = otherMoney;
            data["posNumber"] = posNumber;
            data["repaymentTime"] = repaymentTime;
            data["billNo"] = billNo;
            data["billorderDetailsRequest"] = JSON.stringify(billorderDetailsRequest);
            if (bv.isValid()) {
                if (totalAmount != amount) {
                    $("#message").empty();
                    $("#message").html('填写总金额不等于应收金额,请检查');
                    return;
                } else {
                    $("#message").empty();
                }
                if (null != posMoney && '' != posMoney) {
                    if (null == posNumber || '' == posNumber) {
                        $("#message").empty();
                        $("#message").html('请填写pos流水号');
                        return;
                    }
                }
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/fitBill/payBill',
                        async: false,
                        processData: true,
                        type: 'GET',
                        data: data,
                        success: function (result) {
                            if (result.code == 10100) {
                                $("#message").html(result.message);
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                            } else if (result.code == -1) {
                                $("#message").html(result.message);
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                            } else if (result.code == 0) {
                                $("#message").html('');
                                window.location.reload();
                            }
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#confirmSubmit').bootstrapValidator('disableSubmitButtons', false);
                        }
                    })
                }
            }
        });
    });

    function inDataGrid1() {
        $("#dataGrid1").bootstrapTable('destroy');
        $grid.initBill($('#dataGrid1'), $('#toolbar1'), '/rest/fitBill/noPayOrderBill/page/' + storeId, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
                startTime: $('#startTime1').val(),
                endTime: $('#endTime1').val(),
            }
        }, [{
            checkbox: true,
            title: '选择',
            formatter: function (value, row) {
                if (null != row.returnNo && '' != row.returnNo) {
                    return {
                        disabled: true,//设置是否可用
                        checked: true//设置选中
                    };
                }
            }
        }, {
            field: 'orderId',
            title: 'ID',
            align: 'center',
            visible: false
        }, {
            field: 'orderType',
            title: 'orderType',
            align: 'center',
            visible: false
        }, {
            field: 'orderTime',
            title: '订单日期',
            align: 'center'
        }, {
            field: 'shipmentTime',
            title: '订单出货日',
            align: 'center'
        },
            {
                field: 'orderNo',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'returnNo',
                title: '退单号',
                align: 'center'
            },
            {
                field: 'orderCreditMoney',
                title: '金额',
                align: 'center'
            },
            {
                field: 'interestAmount',
                title: '滞纳金',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '0';
                    } else {
                        return value;
                    }
                }
            }
        ]);
        getBillInfo();
    }


    function getBillInfo() {
        var data = {
            'startTime': $('#startTime1').val(),
            'endTime': $('#endTime1').val(),
            'storeId': storeId,
        };
        $.ajax({
            url: '/rest/fitBill/billInfo',
            async: false,
            type: 'GET',
            data: data,
            success: function (result) {
                if (result.code == 0) {
                    repaymentDeadlineDate
                    $("#billStartDate").html(result.content.billStartDate);
                    $("#billEndDate").html(result.content.billEndDate);
                    $("#repaymentDeadlineDate").html(result.content.repaymentDeadlineDate);
                    $("#priorNotPaidInterestAmount").html('￥' + result.content.priorNotPaidInterestAmount);
                    $("#priorNotPaidBillAmount").html('￥' + result.content.priorNotPaidBillAmount);
                    $("#currentAdjustmentAmount").html('￥' + result.content.currentAdjustmentAmount);
                    $("#currentBillAmount").html('￥' + result.content.currentBillAmount);
                    $("#currentPaidAmount").html('￥' + result.content.currentPaidAmount);
                    $("#billTotalAmount").html('￥' + result.content.billTotalAmount);
                    $("#billTotalAmount").html('￥' + result.content.billTotalAmount);
                    $("#billNo").val("" + result.content.billNo);
//                    $("#remainAmount").html('￥' + accAdd(result.content.priorNotPaidInterestAmount, accAdd(result.content.currentUnpaidAmount, result.content.priorNotPaidBillAmount)));
                    var currentShouldReturn = parseFloat(result.content.billTotalAmount) - parseFloat(result.content.currentPaidAmount);
                    $("#remainAmount").html('￥' + currentShouldReturn);
                }
            },
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            }
        })
    }


    function inDataGrid2() {
        var billNo = $("#billNo").val();
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
                field: 'orderCreditMoney',
                title: '金额',
                align: 'center'
            }, {
                field: 'interestAmount',
                title: '滞纳金',
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

    function payBill() {
        $('#amountMoney').empty();
        var selected = $('#dataGrid1').bootstrapTable('getSelections');
        var billNos = [];
        for (var i = 0; i < selected.length; i++) {
            var data = selected[i];
            billNos.push(data.ordNumber);
        }
        if (null === billNos || 0 === billNos.length) {
            $notify.warning('请在点击按钮前选中至少一条数据');
            return;
        }
        var amountBill = 0;
        for (var i = 0; i < selected.length; i++) {
            var data = selected[i];
            //ids.push(data.ordId)
            billorderDetailsRequest.push({
                id: data.orderId,
                orderNo: data.orderNo,
                returnNo: data.returnNo,
                orderType: data.orderType
            })
            amountBill = accAdd(amountBill, data.orderCreditMoney);
            amountBill = accAdd(amountBill, data.interestAmount);
        }
        amountBill= Math.round(amountBill * 100) / 100
        $("#amount").val(amountBill);
        $('#amountMoney').html('应收金额(元):' + amountBill);
        $('#payOrderBill').modal();
    }

    function accAdd(arg1, arg2) {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        return (arg1 * m + arg2 * m) / m
    }


    function findByCondition() {
        var startTime1 = $('#startTime1').val();
        var endTime1 = $('#endTime1').val();
        var info1 = $('#info1').val();
        var billStartDate = $('#billStartDate').html();
        if (endTime1 != null && endTime1 != '') {
            if (endTime1 < billStartDate) {
                $notify.danger('请选择大于账单开始日期的出货结束日期');
                return;
            }
        }

        if (startTime1 == null || startTime1 == '') {
            var a = 0
        }
        if (endTime1 == null || endTime1 == '') {
            var b = 0
        }
        if (info1 == null || info1 == '') {
            var c = 0
        }
        if (a == 0 && b == 0 && c == 0) {
            return;
        } else {
            inDataGrid1();
        };
    }


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
        if (event.keyCode == 13) {
            if (a == 0 && b == 0 && c == 0) {
                return;
            } else {
                findRepaymentByCondition();
            }
        }
    }

    function reload() {
        $('#repaymentTime').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            language: 'zh-CN'
        }).on('hide', function (e) {
            $('#confirmReceivablesFrom').data('bootstrapValidator')
                    .updateStatus('repaymentTime', 'NOT_VALIDATED', null)
                    .validateField('repaymentTime');
        })
    }

</script>
</body>