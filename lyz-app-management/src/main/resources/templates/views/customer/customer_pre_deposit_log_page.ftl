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
            <div class="nav-tabs-custom">
                <div id="toolbar" class="form-inline ">
                    <#if cusId?? && cusId != 0>
                        <button id="btn_back" type="button" class="btn btn-default">
                            <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
                        </button>
                    </#if>
                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            onchange="findCusByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>


                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findCusByStoreId()"   data-live-search="true" >
                        <option value="-1">选择门店</option>
                    </select>

                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                               placeholder="请输入要查找的姓名或电话、单号">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCusByNameOrPhone()">查找</button>
                        </span>
                    </div>
                </div>
                <div class="box-body table-reponsive">
                    <input type="hidden" name="cusId" id="cusId" value="<#if cusId??>${cusId?c}<#else>0</#if>">
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
                    <ul id="cusDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
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
        findCitylist()
        findStorelist();
        showAvailableCredit(null,null,null);
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });

    function findCitylist() {
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


    function findStorelist() {
        var store = "";
        $.ajax({
            url: '/rest/stores/findStorelist',
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

    function showAvailableCredit(keywords,cityId,storeId){
        $("#dataGrid").bootstrapTable('destroy');
        var cusId=$('#cusId').val();
        $grid.init($('#dataGrid'), $('#toolbar'),'/rest/customer/preDeposit/log/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                cityId: cityId,
                storeId: storeId,
                cusId: cusId
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'id',
                title: 'ID',
                align: 'center',
                events: {
                    'click .scan': function (e, value, row) {
                        $page.information.show(row.id);
                    }
                },
                formatter: function (value) {
                    if(null==value){
                        return '<a class="scan" href="#">' + '未知' + '</a>';
                    }else{
                        return '<a class="scan" href="#">' + value + '</a>';
                    }
                }
            },{
                field: 'name',
                title: '顾客姓名',
                align: 'center'
            },{
                field: 'mobile',
                title: '顾客电话',
                align: 'center'
            },
            {
                field: 'storeName',
                title: '归属门店',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '变更类型',
                align: 'center'
            },
            {
                field: 'changeMoney',
                title: '变更金额(￥)',
                align: 'center'
            },
            {
                field: 'balance',
                title: '变更后余额(￥)',
                align: 'center'
            },
            {
                field: 'orderNumber',
                title: '使用单号',
                align: 'center'
            },
            {
                field: 'createTime',
                title: '变更时间',
                align: 'center'
            }
        ]);
    }
    function findCusByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var keywords = $('#queryCusInfo').val();
        $("#dataGrid").bootstrapTable('destroy');
        findCusPreLogByCityIdOrstoreIdOrKeywords(keywords,cityId,storeId);
        if(cityId==-1){
            findStorelist();
            return false;
        };
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityId/' + cityId,
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

    function findCusPreLogByCityIdOrstoreIdOrKeywords(keywords,cityId,storeId){
        showAvailableCredit(keywords,cityId,storeId);
    }

    function findCusByStoreId() {
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        findCusPreLogByCityIdOrstoreIdOrKeywords(keywords,cityId,storeId);

    }

    function findCusByNameOrPhone() {
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        findCusPreLogByCityIdOrstoreIdOrKeywords(queryCusInfo,cityId,storeId);
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }
    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/customer/preDeposit/log/' + id,
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

                            if (0 === result.code) {
                                $global.timer = null;
                                var data = result.content;
                                $('#menuTitle').html(" 顾客预存款变更详情");

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);

                                if (null === data.mobile) {
                                    data.mobile = '-';
                                }
                                $('#mobile').html(data.mobile);

                                if (null == data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (null == data.changeType) {
                                    data.changeType = '-';
                                }
                                $('#changeType').html(data.changeType);

                                if (null === data.changeMoney) {
                                    data.changeMoney = '-';
                                }
                                $('#changeMoney').html(data.changeMoney);

                                if (null === data.balance) {
                                    data.balance = '-';
                                }
                                $('#balance').html(data.balance);

                                if (null === data.orderNumber) {
                                    data.orderNumber = '-';
                                }
                                $('#orderNumber').html(data.orderNumber);

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                }
                                $('#createTime').html(data.createTime);

                                if (null === data.remarks) {
                                    data.remarks = '-';
                                }
                                $('#remarks').html(data.remarks);

                                if (null === data.transferTime) {
                                    data.transferTime = '-';
                                }
                                $('#transferTime').html(data.transferTime);


                                if (null === data.merchantOrderNumber) {
                                    data.merchantOrderNumber = '-';
                                }
                                $('#merchantOrderNumber').html(data.merchantOrderNumber);

                                if (null === data.operatorId) {
                                    data.operatorId = '-';
                                }
                                $('#operatorId').html(data.operatorId);

                                if (null === data.operatorType) {
                                    data.operatorType = '-';
                                }
                                $('#operatorType').html(data.operatorType);

                                if (null === data.operatorIp) {
                                    data.operatorIp = '-';
                                }
                                $('#operatorIp').html(data.operatorIp);

                                if (null === data.detailReason) {
                                    data.detailReason = '-';
                                }
                                $('#detailReason').html(data.detailReason);

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


</script>
</body>