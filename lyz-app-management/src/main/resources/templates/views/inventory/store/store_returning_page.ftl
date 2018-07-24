<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
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
                    <select name="structureCode" id="structureCode" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findByStructureCode()">
                        <option value="-1">选择分公司</option>
                    <#if structureList?? && structureList?size gt 0 >
                        <#list structureList as structure>
                            <option value="${structure.number!''}">${structure.structureName!''}</option>
                        </#list>
                    </#if>
                    </select>
                    <select name="storeCode" id="storeCode" class="form-control selectpicker" data-width="120px"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryInfo" id="queryInfo" class="form-control "
                               style="width:auto;" placeholder="请输入商品编码或退货单号" onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findByCondition()">查找</button>
                        </span>
                    </div>
                <#--<button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>-->
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
                            <b>订单编号:</b> <a id="orderNumber"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品列表</b>

                            <table id="goodsList" class="table table-bordered table-responsive">
                                <thead>
                                <tr>
                                    <th style="text-align: center">商品编码</th>
                                    <th style="text-align: center">商品名称</th>
                                    <th style="text-align: center">数量</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </li>
                        <li class="list-group-item">
                            <b>收货地址</b>

                            <table id="detailAddress" class="table table-bordered">
                                <thead>
                                <tr style="text-align: center">
                                    <th style="text-align: center">门店名称</th>
                                    <th style="text-align: center">门店地址</th>
                                    <th style="text-align: center">门店电话</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </li>
                        <li class="list-group-item">
                            <b><label for="remarkInfo">备注信息</label></b>
                            <div>
                                <textarea id="remarkInfo" class="form-control" readonly></textarea>
                            </div>
                        </li>
                        <li class="list-group-item">
                            <b><label for="managerRemarkInfo">后台备注信息</label></b>
                            <div>
                                <textarea id="managerRemarkInfo" class="form-control"></textarea>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a role="button" class="btn btn-primary pull-left" href="javascript:$page.information.update();">修改</a>
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        findStoreSelection();
        initDateGird('/rest/storeInventory/goodReturn/page/grid');
    });

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/store/requiring/' + id,
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
                                $('#menuTitle').html("要货单详情");

                                if (null === data.orderNumber) {
                                    data.orderNumber = '-';
                                }
                                $('#orderNumber').html(data.orderNumber);

                                if (null === data.goodsList) {
                                    data.goodsList = '-';
                                }
                                var str = "";
                                $.each(data.goodsList, function (i, item) {
                                    str += "<tr><td>" + item.goodsCode + "</td><td>" + item.goodsTitle + "</td><td>" + item.quantity + "</td></tr>";
                                });
                                $('#goodsList').find('tbody').empty();
                                $('#goodsList').find('tbody').append(str);

                                if (null === data.detailAddress) {
                                    data.detailAddress = '-';
                                }
                                var $tr = "<tr><td>" + data.detailAddress.storeName + "</td><td>" + data.detailAddress.storeAddress + "</td><td>" + data.detailAddress.storePhone + "</td></tr>";
                                $('#detailAddress').find('tbody').empty();
                                $('#detailAddress').find('tbody').append($tr);

                                if (null === data.remarkInfo) {
                                    data.remarkInfo = '';
                                }
                                $('#remarkInfo').html(data.remarkInfo);

                                if (null === data.managerRemarkInfo) {
                                    data.managerRemarkInfo = '';
                                }
                                $('#managerRemarkInfo').val(data.managerRemarkInfo);

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
            },
            update: function () {
                var info = $('#managerRemarkInfo').val();
                var orderNumber = $('#orderNumber').text();
                $http.POST('/rest/store/requiring/update', {
                    "managerRemarkInfo": info,
                    "orderNumber": orderNumber
                }, function (result) {
                    if (0 === result.code) {
                        $notify.success("修改成功！")
                    } else {
                        $notify.danger(result.message);
                    }
                })
            }
        }
    }

    function findByStructureCode() {
        initSelect("#storeCode", "选择门店");
        findStoreSelection();
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird('/rest/storeInventory/goodReturn/page/grid');
    }

    function findByCondition() {
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird('/rest/storeInventory/goodReturn/page/grid');
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


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }


    function initDateGird(url) {
        var structureCode = $('#structureCode').val();
        var storeCode = $('#storeCode').val();
        var queryInfo = $('#queryInfo').val();
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
                structureCode: structureCode,
                storeId: storeCode,
                queryInfo: queryInfo
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        }, {
            field: 'transNumber',
            title: '退货编号',
            align: 'center'
        }, {
            field: 'itemCode',
            title: '商品编号',
            align: 'center'
        }, {
            field: 'skuName',
            title: '商品名称',
            align: 'center'
        }, {
            field: 'quantity',
            title: '商品数量',
            align: 'center'
        }, {
            field: 'shipDate',
            title: '退货时间',
            align: 'center',
            formatter:function (value, row, index) {
                if (null == value) {
                    return '-';
                } else {
                    return formatDateTime(value);
                }
            }
        }]);
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

    function findBykey(){
        if(event.keyCode==13){
            findByCondition();
        }
    }
</script>
</body>