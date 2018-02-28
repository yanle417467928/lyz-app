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
    <h1>额度变更详情</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:history.back();">导购额度管理</a></li>
        <li class="active">额度变更详情</li>
    </ol>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#tab_1-1" data-toggle="tab" onclick="showAvailableCredit()">可用额度</a></li>
                    <li><a href="#tab_1-3" data-toggle="tab" onclick="showTempCredit()">临时额度</a></li>
                    <li><a href="#tab_1-2" data-toggle="tab" onclick="showFixedCredit()">固定额度</a></li>
                </ul>
                <div id="toolbar" class="form-inline ">
                    <button id="btn_back" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
                    </button>
                </div>
                <div class="box-body table-reponsive">
                    <input type="hidden" name="id" id="guideId" value="${guideId}">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>


<script>

    $(function () {
        showAvailableCredit();
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });

    function showAvailableCredit() {
        $("#dataGrid").bootstrapTable('destroy');
        var guideId = $('#guideId').val();
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/guideLine/availableCreditChangePage/grid/' + guideId, 'get', false, function (params) {
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
                title: '可用额度改变量',
                align: 'center'
            }, {
                field: 'availableCreditChangId.creditLimitAvailableAfterChange',
                title: '可用额度余量',
                align: 'center'
            },
            {
                field: 'referenceNumber',
                title: '相关单号',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '变更类型',
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
                    }else if ('FIXEDAMOUNT_ADJUSTMENT' == value) {
                        return '固定额度调整';
                    }else if ('AVALIABLED_CHANGE_BY_FIXE' == value) {
                        return '可用额度因固定额度调整修改';
                    }else if ('AVALIABLED_CHANGE_BY_TEMP' == value) {
                        return '可用额度因临时额度调整修改';
                    }
                }
            },
            {
                field: 'createTime',
                title: '变更时间',
                align: 'center',
                formatter: function (value, row) {
                    return formatDateTime(value);
                }
            },
            {
                field: 'operatorName',
                title: '变更人',
                align: 'center'
            }
        ]);
    }


    function showTempCredit() {
        $("#dataGrid").bootstrapTable('destroy');
        var guideId = $('#guideId').val();
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/guideLine/tempCreditChangePage/grid/' + guideId, 'get', false, function (params) {
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
                title: '临时额度改变量',
                align: 'center'
            }, {
                field: 'tempCreditChangeId.tempCreditLimitAfterChange',
                title: '临时额度余量',
                align: 'center',
            },
            {
                field: 'referenceNumber',
                title: '相关单号',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '变更类型',
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
                    }else if ('FIXEDAMOUNT_ADJUSTMENT' == value) {
                        return '固定额度调整';
                    }else if ('AVALIABLED_CHANGE_BY_FIXE' == value) {
                        return '可用额度因固定额度调整修改';
                    }else if ('AVALIABLED_CHANGE_BY_TEMP' == value) {
                        return '可用额度因临时额度调整修改';
                    }
                }
            },
            {
                field: 'createTime',
                title: '变更时间',
                align: 'center',
                formatter: function (value, row) {
                    return formatDateTime(value);
                }
            },
            {
                field: 'operatorName',
                title: '变更人',
                align: 'center'
            }
        ]);
    }

    function showFixedCredit() {
        $("#dataGrid").bootstrapTable('destroy');
        var guideId = $('#guideId').val();
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/guideLine/fixedCreditChangePage/grid/' + guideId, 'get', false, function (params) {
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
                field: 'fixedCreditChangeId.fixedCreditLimitChangeAmount',
                title: '固定额度改变量',
                align: 'center'
            }, {
                field: 'fixedCreditChangeId.fixedCreditLimitAfterChange',
                title: '固定额度余量',
                align: 'center',
            },
            {
                field: 'referenceNumber',
                title: '相关单号',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '变更类型',
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
                    }else if ('FIXEDAMOUNT_ADJUSTMENT' == value) {
                        return '固定额度调整';
                    }else if ('AVALIABLED_CHANGE_BY_FIXE' == value) {
                        return '可用额度因固定额度调整修改';
                    }else if ('AVALIABLED_CHANGE_BY_TEMP' == value) {
                        return '可用额度因临时额度调整修改';
                    }
                }
            },
            {
                field: 'createTime',
                title: '变更时间',
                align: 'center',
                formatter: function (value, row) {
                    return formatDateTime(value);
                }
            },
            {
                field: 'operatorName',
                title: '变更人',
                align: 'center'
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