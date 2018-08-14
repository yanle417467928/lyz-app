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
        <div class="col-xs-12">
            <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#tab_1-1" data-toggle="tab" onclick="showOrderResend()">订单出货</a></li>
                    <li><a href="#tab_1-2" data-toggle="tab" onclick="showReturnOrderResend()">订单退货</a></li>
                    <li><a href="#tab_1-3" data-toggle="tab" onclick="showAllocationOutboundResend()">调拨出库</a></li>
                    <li><a href="#tab_1-4" data-toggle="tab" onclick="showAllocationInboundResend()">调拨入库</a></li>
                    <li><a href="#tab_1-5" data-toggle="tab" onclick="showSelfTakeOrderResend()">自提出货</a></li>
                    <li><a href="#tab_1-6" data-toggle="tab" onclick="showReturnStoreResend()">自提退货</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_1-1">
                        <div id="toolbar1" class="form-inline">
                            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                <input type="text" name="orderNumber" id="orderNumber" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输入要查找的单号" onkeypress="findBykey()">
                                <span class="input-group-btn">
                                    <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                            onclick="return showOrderResend()">查找</button>
                               </span>
                            </div>
                        </div>
                        <div class="box-body table-reponsive">
                            <table id="dataGrid1" class="table table-bordered table-hover">

                            </table>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_1-2">
                        <div id="toolbar2" class="form-inline">
                            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                <input type="text" name="returnNo" id="returnNo" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输入要查找的单号" onkeypress="findBykey()">
                                <span class="input-group-btn">
                                    <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                            onclick="return showReturnOrderResend()">查找</button>
                               </span>
                            </div>
                        </div>
                        <div class="box-body table-reponsive">
                            <table id="dataGrid2" class="table table-bordered table-hover">

                            </table>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_1-3">
                        <div id="toolbar3" class="form-inline">
                            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                <input type="text" name="allocationOutNo" id="allocationOutNo" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输入要查找的单号" onkeypress="findBykey()">
                                <span class="input-group-btn">
                                    <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                            onclick="return showAllocationOutboundResend()">查找</button>
                               </span>
                            </div>
                        </div>
                        <div class="box-body table-reponsive">
                            <table id="dataGrid3" class="table table-bordered table-hover">

                            </table>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_1-4">
                        <div id="toolbar4" class="form-inline">
                            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                <input type="text" name="allocationInNo" id="allocationInNo" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输入要查找的单号" onkeypress="findBykey()">
                                <span class="input-group-btn">
                                    <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                            onclick="return showAllocationInboundResend()">查找</button>
                               </span>
                            </div>
                        </div>
                        <div class="box-body table-reponsive">
                            <table id="dataGrid4" class="table table-bordered table-hover">

                            </table>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_1-5">
                        <div id="toolbar5" class="form-inline">
                            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                <input type="text" name="selfTakeOrderNo" id="selfTakeOrderNo" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输入要查找的单号" onkeypress="findBykey()">
                                <span class="input-group-btn">
                                    <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                            onclick="return showSelfTakeOrderResend()">查找</button>
                               </span>
                            </div>
                        </div>
                        <div class="box-body table-reponsive">
                            <table id="dataGrid5" class="table table-bordered table-hover">

                            </table>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_1-6">
                        <div id="toolbar6" class="form-inline">
                            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                                <input type="text" name="returnStoreNo" id="returnStoreNo" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输入要查找的单号" onkeypress="findBykey()">
                                <span class="input-group-btn">
                                    <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                            onclick="return showReturnStoreResend()">查找</button>
                               </span>
                            </div>
                        </div>
                        <div class="box-body table-reponsive">
                            <table id="dataGrid6" class="table table-bordered table-hover">

                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>


<script>

    $(function () {
        showOrderResend();
    });

    function showOrderResend() {
        $("#dataGrid1").bootstrapTable('destroy');
        $grid.init($('#dataGrid1'), $('#toolbar1'), '/rest/interface/ebs/order/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: $('#orderNumber').val().trim()
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
                field: 'orderNumber',
                title: '订单号',
                align: 'center'
            }, {
                field: 'status',
                title: '订单状态',
                align: 'center'
            },
            {
                field: 'isGenerate',
                title: '是否生成',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'isSend',
                title: '是否传输成功',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'createTime',
                title: '生成时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-'
                    } else {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'errorMsg',
                title: '错误信息',
                align: 'center'
            },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (false == row.isGenerate) {
                        return '<button class="btn btn-primary btn-xs" onclick="generateOrderInfo(' + "'" + row.orderNumber + "'" + ')">生成接口</button>'
                    } else if (true == row.isGenerate && false == row.isSend) {
                        return '<button class="btn btn-warning btn-xs" onclick="sendOrderInfo(' + '\'' + row.orderNumber + '\'' + ')">传输接口</button>'
                    }
                    s
                }
            }
        ]);
    }

    function generateOrderInfo(orderNumber) {
        url = '/rest/interface/ebs/order/generate/' + orderNumber
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showOrderResend()
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('拆单失败，请稍后重试或联系管理员');
                }
            }
        });
    }

    function sendOrderInfo(orderNumber) {
        url = '/rest/interface/ebs/order/resend/' + orderNumber
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showOrderResend()
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('重传失败，请稍后重试或联系管理员');
                }
            }
        });
    }


    function showReturnOrderResend() {
        $("#dataGrid2").bootstrapTable('destroy');
        $grid.init($('#dataGrid2'), $('#toolbar2'), '/rest/interface/ebs/returnOrder/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: $('#returnNo').val().trim()
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
                field: 'returnNo',
                title: '退单号',
                align: 'center'
            }, {
                field: 'returnStatus',
                title: '退单状态',
                align: 'center'
            },
            {
                field: 'isGenerate',
                title: '是否生成',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'isSend',
                title: '是否传输成功',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'createTime',
                title: '生成时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-'
                    } else {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'errorMsg',
                title: '错误信息',
                align: 'center'
            },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (false == row.isGenerate) {
                        return '<button class="btn btn-primary btn-xs" onclick="generateReturnOrderInfo(' + "'" + row.returnNo + "'" + ')">生成接口</button>'
                    } else if (true == row.isGenerate && false == row.isSend) {
                        return '<button class="btn btn-warning btn-xs" onclick="sendReturnOrderInfo(' + '\'' + row.returnNo + '\'' + ')">传输接口</button>'
                    }
                    s
                }
            }
        ]);
    }


    function generateReturnOrderInfo(returnNo) {
        url = '/rest/interface/ebs/returnOrder/generate/' + returnNo
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showReturnOrderResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('拆单失败，请稍后重试或联系管理员');
                }
            }
        });
    }

    function sendReturnOrderInfo(returnNo) {
        url = '/rest/interface/ebs/returnOrder/resend/' + returnNo
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showReturnOrderResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('重传失败，请稍后重试或联系管理员');
                }
            }
        });
    }


    function showAllocationInboundResend() {
        $("#dataGrid4").bootstrapTable('destroy');
        $grid.init($('#dataGrid4'), $('#toolbar4'), '/rest/interface/ebs/allocationInbound/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: $('#allocationInNo').val().trim()
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
                field: 'number',
                title: '调拨单号',
                align: 'center'
            }, {
                field: 'status',
                title: '调拨状态',
                align: 'center'
            },
            {
                field: 'isGenerate',
                title: '是否生成',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'isSend',
                title: '是否传输成功',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'createTime',
                title: '生成时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-'
                    } else {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'errorMsg',
                title: '错误信息',
                align: 'center'
            },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (false == row.isGenerate) {
                        return '<button class="btn btn-primary btn-xs" onclick="generateAllocationInboundInfo(' + "'" + row.number + "'" + ')">生成接口</button>'
                    } else if (true == row.isGenerate && false == row.isSend) {
                        return '<button class="btn btn-warning btn-xs" onclick="sendAllocationInboundInfo(' + '\'' + row.number + '\'' + ')">传输接口</button>'
                    }
                    s
                }
            }
        ]);
    }

    function generateAllocationInboundInfo(returnNo) {
        //TODO
    }

    function sendAllocationInboundInfo(returnNo) {
        url = '/rest/interface/ebs/allocationInbound/resend/' + returnNo
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showAllocationInboundResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('重传失败，请稍后重试或联系管理员');
                }
            }
        });
    }


    function showAllocationOutboundResend() {
        $("#dataGrid3").bootstrapTable('destroy');
        $grid.init($('#dataGrid3'), $('#toolbar3'), '/rest/interface/ebs/allocationOutbound/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords:$('#allocationOutNo').val().trim()
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
                field: 'number',
                title: '调拨单号',
                align: 'center'
            }, {
                field: 'status',
                title: '调拨状态',
                align: 'center'
            },
            {
                field: 'isGenerate',
                title: '是否生成',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'isSend',
                title: '是否传输成功',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'createTime',
                title: '生成时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-'
                    } else {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'errorMsg',
                title: '错误信息',
                align: 'center'
            },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (false == row.isGenerate) {
                        return '<button class="btn btn-primary btn-xs" onclick="generateAllocationOutboundInfo(' + "'" + row.number + "'" + ')">生成接口</button>'
                    } else if (true == row.isGenerate && false == row.isSend) {
                        return '<button class="btn btn-warning btn-xs" onclick="sendAllocationOutboundInfo(' + '\'' + row.number + '\'' + ')">传输接口</button>'
                    }
                    s
                }
            }
        ]);
    }


    function generateAllocationOutboundInfo(returnNo) {
        //TODO
    }

    function sendAllocationOutboundInfo(returnNo) {
        url = '/rest/interface/ebs/allocationOutbound/resend/' + returnNo
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showAllocationOutboundResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('重传失败，请稍后重试或联系管理员');
                }
            }
        });
    }

    function showSelfTakeOrderResend() {
        $("#dataGrid5").bootstrapTable('destroy');
        $grid.init($('#dataGrid5'), $('#toolbar5'), '/rest/interface/ebs/selfTakeOrder/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: $('#selfTakeOrderNo').val().trim()
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
                field: 'orderNumber',
                title: '订单号',
                align: 'center'
            }, {
                field: 'status',
                title: '订单状态',
                align: 'center'
            },
            {
                field: 'isGenerate',
                title: '是否生成',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'isSend',
                title: '是否传输成功',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'createTime',
                title: '生成时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-'
                    } else {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'errorMsg',
                title: '错误信息',
                align: 'center'
            },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (false == row.isGenerate) {
                        return '<button class="btn btn-primary btn-xs" onclick="generateSelfTakeOrderInfo(' + "'" + row.orderNumber + "'" + ')">生成接口</button>'
                    } else if (true == row.isGenerate && false == row.isSend) {
                        return '<button class="btn btn-warning btn-xs" onclick="sendSelfTakeOrderInfo(' + '\'' + row.orderNumber + '\'' + ')">传输接口</button>'
                    }
                    s
                }
            }
        ]);
    }


    function generateSelfTakeOrderInfo(orderNumber) {
        url = '/rest/interface/ebs/selfTakeOrder/generate/' + orderNumber
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showSelfTakeOrderResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('生成信息失败，请稍后重试或联系管理员');
                }
            }
        });
    }

    function sendSelfTakeOrderInfo(orderNumber) {
        url = '/rest/interface/ebs/selfTakeOrder/resend/' + orderNumber
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showSelfTakeOrderResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('重传失败，请稍后重试或联系管理员');
                }
            }
        });
    }


    function showReturnStoreResend() {
        $("#dataGrid6").bootstrapTable('destroy');
        $grid.init($('#dataGrid6'), $('#toolbar6'), '/rest/interface/ebs/returnStoreOrder/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords:$('#returnStoreNo').val().trim()
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
                field: 'returnNo',
                title: '退单号',
                align: 'center'
            }, {
                field: 'returnStatus',
                title: '退单状态',
                align: 'center'
            },
            {
                field: 'isGenerate',
                title: '是否生成',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'isSend',
                title: '是否传输成功',
                align: 'center',
                formatter: function (value, row) {
                    if (false == value) {
                        return '否';
                    } else if (true == value) {
                        return '是';
                    }
                }
            },
            {
                field: 'createTime',
                title: '生成时间',
                align: 'center',
                formatter: function (value, row) {
                    if (null == value) {
                        return '-'
                    } else {
                        return formatDateTime(value);
                    }
                }
            },
            {
                field: 'errorMsg',
                title: '错误信息',
                align: 'center'
            },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (false == row.isGenerate) {
                        return '<button class="btn btn-primary btn-xs" onclick="generateReturnStoreOrderInfo(' + "'" + row.returnNo + "'" + ')">生成接口</button>'
                    } else if (true == row.isGenerate && false == row.isSend) {
                        return '<button class="btn btn-warning btn-xs" onclick="sendReturnStoreOrderInfo(' + '\'' + row.returnNo + '\'' + ')">传输接口</button>'
                    }
                    s
                }
            }
        ]);
    }


    function generateReturnStoreOrderInfo(returnNo) {
        url = '/rest/interface/ebs/returnStoreOrder/generate/' + returnNo
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showSelfTakeOrderResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('生成信息失败，请稍后重试或联系管理员');
                }
            }
        });
    }

    function sendReturnStoreOrderInfo(returnNo) {
        url = '/rest/interface/ebs/returnStoreOrder/resend/' + returnNo
        $.ajax({
            url: url,
            method: 'POST',
            data: null,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    showReturnStoreResend();
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('重传失败，请稍后重试或联系管理员');
                }
            }
        });
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