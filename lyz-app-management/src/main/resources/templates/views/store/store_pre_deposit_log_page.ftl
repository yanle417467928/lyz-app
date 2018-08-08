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
                <#if storeId?? && storeId != 0>
                    <button id="btn_back" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
                    </button>
                </#if>
                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findStorePreByCity(this.value)" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>


                    <select name="storeType" id="storeType" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findStorePreByStoreType(this.value)" data-live-search="true">
                        <option value="-1">选择门店类型</option>
                        <#if storeTypes??>
                            <#list storeTypes as storeType>
                                <option value="${storeType.value}">${storeType.description}</option>
                            </#list>
                        </#if>
                    </select>

                    <select name="changeType" id="changeType" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findStorePreByChangeType(this.value)" data-live-search="true">
                        <option value="-1">选择变更类型</option>
                        <#if changeTypes??>
                            <#list changeTypes as changeType>
                                <option value="${changeType.value}">${changeType.description}</option>
                            </#list>
                        </#if>
                    </select>

                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                               placeholder="请输入要查找的店名或编码、单号"
                               onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findStorePreByNameOrCode()">查找</button>
                        </span>
                    </div>
                </div>
                <div class="box-body table-reponsive">
                    <input type="hidden" name="storeId" id="storeId" value="<#if storeId??>${storeId?c}<#else>0</#if>">
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
                    <ul id="storePreDepositLogDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>门店名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店编码</b> <a class="pull-right" id="storeCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店类型</b> <a class="pull-right" id="storeTypeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市</b> <a class="pull-right" id="city"></a>
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
                        <li class="list-group-item">
                            <b>变更人</b> <a class="pull-right" id="operatorName"></a>
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
        findCitylist();
        showAvailableCredit(null,null,'-1','-1');
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
                });
                $("#cityCode").append(city);
                $("#cityCode").selectpicker('refresh');
                $("#cityCode").selectpicker('render');
            }
        });
    }


    function showAvailableCredit(keywords,cityId,storeType,changeType){
        $("#dataGrid").bootstrapTable('destroy');
        var storeId=$('#storeId').val();
        $grid.init($('#dataGrid'), $('#toolbar'),'/rest/store/preDeposit/log/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                cityId: cityId,
                storeId: storeId,
                storeType: storeType,
                changeType: changeType
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
                field: 'storeName',
                title: '门店名称',
                align: 'center'
            },{
                field: 'storeCode',
                title: '门店编码',
                align: 'center'
            },
            {
                field: 'storeType',
                title: '门店类型',
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
            ,
            {
                field: 'remarks',
                title: '修改原因',
                align: 'center'
            },
            {
                field: 'operatorName',
                title: '变更人',
                align: 'center'
            }
        ]);
    }
    function findStorePreByCity(cityId) {
        $("#queryCusInfo").val('');
        var keywords = $('#queryCusInfo').val();
        $("#dataGrid").bootstrapTable('destroy');
        var storeType = $("#storeType").val();
        var changeType = $("#changeType").val();
        findStorePreByCityIdOrKeywords(keywords,cityId,storeType,changeType);
    }

    function findBykey(){
        if(event.keyCode==13){
            findStorePreByNameOrCode();
        }
    }

    function findStorePreByNameOrCode() {
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var storeType = $("#storeType").val();
        var changeType = $("#changeType").val();
        findStorePreByCityIdOrKeywords(queryCusInfo,cityId,storeType,changeType);
    }

    function findStorePreByStoreType(storeType) {
        $("#queryCusInfo").val('');
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var changeType = $("#changeType").val();
        findStorePreByCityIdOrKeywords(queryCusInfo,cityId,storeType,changeType);
    }

    function findStorePreByChangeType(changeType) {
        $("#queryCusInfo").val('');
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var storeType = $("#storeType").val();
        findStorePreByCityIdOrKeywords(queryCusInfo,cityId,storeType,changeType);
    }

    function findStorePreByCityIdOrKeywords(keywords,cityId,storeType,changeType){
        showAvailableCredit(keywords,cityId,storeType,changeType);
    }

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/store/preDeposit/log/' + id,
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
                                $('#menuTitle').html("门店预存款变更详情");

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (null === data.storeCode) {
                                    data.storeCode = '-';
                                }
                                $('#storeCode').html(data.storeCode);

                                if (null == data.storeType) {
                                    data.storeType = '-';
                                }
                                $('#storeTypeName').html(data.storeType);

                                if (null == data.city) {
                                    data.city = '-';
                                }
                                $('#city').html(data.city);

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
                                if (null === data.operatorName) {
                                    data.operatorName = '-';
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