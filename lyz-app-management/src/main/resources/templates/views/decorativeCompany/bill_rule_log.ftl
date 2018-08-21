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
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
</head>
<body>

<section class="content-header">
    <h1>账单规则变更日志</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:history.back();">账单规则</a></li>
        <li class="active">账单规则变更日志</li>
    </ol>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="nav-tabs-custom">
                <div id="toolbar" class="form-inline ">
                    <button id="btn_back" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
                    </button>
                    <input name="startTime" <#--onchange="findByCondition()"--> type="text"
                           class="form-control datepicker" id="startTime" style="width: 140px;" placeholder="开始时间"
                           readonly>
                    <input name="endTime" <#--onchange="findByCondition()"--> type="text"
                           class="form-control datepicker" id="endTime" style="width: 140px;" placeholder="结束时间"
                           readonly>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryInfo" id="queryInfo" class="form-control"
                               style="width:auto;"
                               placeholder="请输入要查找的姓名" onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return showRuleChangeLog()">查找</button>
                        </span>
                    </div>
                </div>
                <div class="box-body table-reponsive">
                    <input type="hidden" name="ruleId" id="ruleId" value="<#if id??>${id?c}<#else>0</#if>">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>

</section>
<div id="information" class="modal fade" tabindex="-1" role="dialog">
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
                    <ul id="preDepositLogDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>顾客姓名</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>顾客电话</b> <a class="pull-right" id="mobile"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属门店</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变更类型</b> <a class="pull-right" id="changeType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变更金额(￥)</b> <a class="pull-right" id="changeMoney"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变更后余额(￥)</b> <a class="pull-right" id="balance"></a>
                        </li>
                        <li class="list-group-item">
                            <b>使用订单号</b> <a class="pull-right" id="orderNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变更时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>备注</b> <a class="pull-right" id="remarks"></a>
                        </li>
                        <li class="list-group-item">
                            <b>到账时间</b> <a class="pull-right" id="transferTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商户订单号</b> <a class="pull-right" id="merchantOrderNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>操作人</b> <a class="pull-right" id="operatorId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>操作人员类型</b> <a class="pull-right" id="operatorType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>操作人员ip</b> <a class="pull-right" id="operatorIp"></a>
                        </li>
                        <li class="list-group-item">
                            <b>修改原因</b> <a class="pull-right" id="detailReason"></a>
                        </li>

                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>

<script>

    $(function () {
        showRuleChangeLog();

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });



    function showRuleChangeLog() {
        $("#dataGrid").bootstrapTable('destroy');
        var ruleId = $('#ruleId').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var ruleId = $('#ruleId').val();
        var queryInfo = $('#queryInfo').val();

        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/fitBill/billRuleChangelog', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                startTime: startTime,
                endTime: endTime,
                changeUser:queryInfo,
                id: ruleId
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'id',
                title: 'ID',
                align: 'center',
            }, {
                field: 'storeName',
                title: '门店名称',
                align: 'center'
            }, {
                field: 'updateUserName',
                title: '更新人',
                align: 'center'
            },
            {
                field: 'updateTime',
                title: '更新时间',
                align: 'center'
            },
            {
                field: 'billDate',
                title: '出账日',
                align: 'center'
            },
            {
                field: 'repaymentDeadlineDate',
                title: '还款截至日',
                align: 'center'
            },
            {
                field: 'interestRate',
                title: '利率(单位：万分之一/天 )',
                align: 'center'
            }
        ]);
    }


    function findBykey() {
        if (event.keyCode == 13) {
            findCusByNameOrPhone();
        }
    }



    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }


</script>
</body>