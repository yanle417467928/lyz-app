<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
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
        <#if selectedMenu.parentResourceName??>
            <li><a href="javascript:void(0);">${selectedMenu.parentResourceName!'??'}</a></li>
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
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                    <button id="btn_add" type="button" class=" btn btn-default" onclick="openBillModal()">
                        <i class="fa fa-download"></i>
                        下载报表
                    </button>
                    <select name="structureCode" id="structureCode" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findByStructureCode()">
                        <option value="-1">选择分公司</option>
                    <#if structureList?? && structureList?size gt 0 >
                        <#list structureList as structure>
                            <option value="${structure.number!''}">${structure.structureName!''}</option>
                        </#list>
                    </#if>
                    </select>
                    <select name="storeCode" id="storeCode" class="form-control selectpicker" data-width="120px" onchange="findByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    <#if storeList?? && storeList?size gt 0 >
                        <#list storeList as store>
                            <option value="${store.storeId!''}">${store.storeName!''}</option>
                        </#list>
                    </#if>
                    </select>
                    <input name="endDateTime" type="text" class="form-control datepicker" id="endDateTime"
                           placeholder="截止日期">
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryInfo" id="queryInfo" class="form-control "
                               style="width:auto;" placeholder="请输入商品编码或名称" onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findByCondition()">查找</button>
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
                    <ul id="structureAttributes" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>城市id</b> <a class="pull-right" id="cityId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市名称</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店id</b> <a class="pull-right" id="storeId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店编码</b> <a class="pull-right" id="storeCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品id</b> <a class="pull-right" id="gid"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品编码</b> <a class="pull-right" id="sku"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品名称</b> <a class="pull-right" id="skuName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动数量</b> <a class="pull-right" id="changeQty"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动后数量</b> <a class="pull-right" id="afterChangeQty"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动类型</b> <a class="pull-right" id="changeTypeDesc"></a>
                        </li>
                        <li class="list-group-item">
                            <b>关联单号</b> <a class="pull-right" id="referenceNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>变动时间</b> <a class="pull-right" id="changeTime"></a>
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

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

        $('.selectpicker').selectpicker({
            size:'10'
        });
        initDateGird('/rest/invoicing/page/grid')
    });

    function initDateGird(url) {
        var structureCode = $('#structureCode').val();
        var storeCode = $('#storeCode').val();
        var queryInfo = $('#queryInfo').val();
        var endDateTime = $('#endDateTime').val();
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: queryInfo,
                structureCode: structureCode,
                storeId: storeCode,
                endDateTime: endDateTime
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        }, {
            field: 'sku',
            title: '商品编码',
            align: 'center'
        }, {
            field: 'skuName',
            title: '商品名称',
            align: 'center'
        }, {
            field: 'initialIty',
            title: '期初',
            align: 'center'
        },{
            field: 'orderDeliveryQty',
            title: '出货数',
            align: 'center'
        }, {
            field: 'selfTakeOrderReturnQty',
            title: '退货数',
            align: 'center'
        }, {
            field: 'storeAllocateInboundQty',
            title: '调拨入库',
            align: 'center'
        }, {
            field: 'storeAllocateOutboundQty',
            title: '调拨出库',
            align: 'center'
        }, {
            field: 'storeGoodsQty',
            title: '门店要/退货',
            align: 'center'
        }, {
            field: 'storePutGoodsQty',
            title: '盘盈/亏',
            align: 'center'
        },{
            field: 'surplusInventory',
            title: '本期库存',
            align: 'center'
        },{
            field: 'realIty',
            title: '门店库存',
            align: 'center'
        },{
            field: 'changeTime',
            title: '变更时间',
            align: 'center',
        }]);
    }


    function findStoreSelection() {
        var store = "";
        var structureCode = $('#structureCode').val();
        var data = {
            "companyCode": structureCode
        }
        $.ajax({
            url: '/rest/stores/findStoresListByCompanyCodeAndStoreType',
            method: 'GET',
            data: data,
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


    function findByStructureCode() {
        initSelect("#storeCode", "选择门店");
        findStoreSelection();
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird('/rest/invoicing/page/grid');
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function findByCondition() {
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird('/rest/invoicing/page/grid');
    }

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/inventory/log' + id,
                        method: 'GET',
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            if (0 === result.code) {
                                var data = result.content;
                                $('#menuTitle').html("库存变动详情");

                                if (null === data.cityId) {
                                    data.cityId = '-';
                                }
                                $('#cityId').html(data.cityId);

                                if (null === data.cityName) {
                                    data.cityName = '-';
                                }
                                $('#cityName').html(data.cityName);

                                if (null === data.storeId) {
                                    data.storeId = '-';
                                }
                                $('#storeId').html(data.storeId);

                                if (null === data.storeCode) {
                                    data.storeCode = '-';
                                }
                                $('#storeCode').html(data.storeCode);

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (null === data.gid) {
                                    data.gid = '-';
                                }
                                $('#gid').html(data.gid);

                                if (null === data.sku) {
                                    data.sku = '-';
                                }
                                $('#sku').html(data.sku);

                                if (null === data.skuName) {
                                    data.skuName = '-';
                                }
                                $('#skuName').html(data.skuName);

                                if (null === data.changeQty) {
                                    data.changeQty = '-';
                                }
                                $('#changeQty').html(data.changeQty);

                                if (null === data.afterChangeQty) {
                                    data.afterChangeQty = '-';
                                }
                                $('#afterChangeQty').html(data.afterChangeQty);

                                if (null === data.changeTypeDesc) {
                                    data.changeTypeDesc = '-';
                                }
                                $('#changeTypeDesc').html(data.changeTypeDesc);

                                if (null === data.referenceNumber) {
                                    data.referenceNumber = '-';
                                }
                                $('#referenceNumber').html(data.referenceNumber);

                                if (null === data.changeTime) {
                                    data.changeTime = '-';
                                }
                                $('#changeTime').html(data.changeTime);

                                $('#information').modal();
                            } else {
                                $notify.danger(result.message);
                            }
                        }
                    })
                }
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }

    function findBykey(){
        if(event.keyCode==13){
            findByCondition();
        }
    }


    function openBillModal() {
        var keywords = $("#queryInfo").val();
        var endDateTime = $('#endDateTime').val();
        var storeId = $("#storeCode").val();
        var structureCode = $('#structureCode').val();

        var url = "/rest/reportDownload/storeInvoicing/download?storeId=" + storeId + "&endDateTime=" + endDateTime + "&structureCode=" + structureCode + "&keywords=" + keywords;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);
    }
</script>
</body>