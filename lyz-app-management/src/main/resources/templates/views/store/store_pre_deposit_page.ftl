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
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">

                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            onchange="findStorePreByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="storeType" id="storeType" class="form-control select" style="width:auto;"
                            onchange="findStorePreByStoreType(this.value)">
                        <option value="-1">选择门店类型</option>
                        <#if storeTypes??>
                            <#list storeTypes as storeType>
                                <option value="${storeType.value}">${storeType.description}</option>
                            </#list>
                        </#if>
                    </select>

                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                               placeholder="请输入要查找的店名或编码..">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findStorePreByNameOrCode()">查找</button>
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
                    <ul id="storePreDepositDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>门店id</b> <a class="pull-right" id="storeId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店编码</b> <a class="pull-right" id="storeCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>所属城市</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否是默认门店</b> <a class="pull-right" id="isDefault"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店类型</b> <a class="pull-right" id="storeTypeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否启用</b> <a class="pull-right" id="enable"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否支持门店自提</b> <a class="pull-right" id="isSelfDelivery"></a>
                        </li>
                        <li class="list-group-item">
                            <b>省</b> <a class="pull-right" id="province"></a>
                        </li>
                        <li class="list-group-item">
                            <b>市</b> <a class="pull-right" id="city"></a>
                        </li>
                        <li class="list-group-item">
                            <b>区</b> <a class="pull-right" id="area"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店地址详细地址</b> <a class="pull-right" id="detailedAddress"></a>
                        </li>
                        <li class="list-group-item">
                            <b>门店电话</b> <a class="pull-right" id="phone"></a>
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
        initDateGrid(null,null,'-1');
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
                    city += "<option value='" + item.cityId + "'>" + item.name + "</option>";
                });
                $("#cityCode").append(city);
            }
        });
    }


    function initDateGrid(keywords,cityId,storeType) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/store/preDeposit/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                cityId: cityId,
                storeType: storeType
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'storeId',
            title: '门店id',
            align: 'center',
            visible: false
        }, {
            field: 'storeCode',
            title: '门店编码',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.storeId);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
            field: 'storeType',
            title: '门店类型',
            align: 'center'
        }, {
            field: 'city',
            title: '所属城市',
            align: 'center'
        }, {
            field: 'balance',
            title: '预存款余额(￥)',
            align: 'center'
        }, {
            field: 'storeId',
            title: '操作',
            align: 'center',
            formatter: function(value,row) {
                return '<button class="btn btn-primary btn-xs" onclick="showDetails('+row.storeId+')"> 查看明细</button><button class="btn  btn-danger btn-xs" style="margin-left: 10px" onclick="changePre('+row.storeId+')"> 变更</button>';
            }
        }
        ]);
    }


    var $page = {
        information: {
            show: function (storeId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/stores/' + storeId,
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
                                $('#menuTitle').html("门店详情");

                                if (null === data.storeId) {
                                    data.storeId = '-';
                                }
                                $('#storeId').html(data.storeId);

                                if (null === data.storeCode) {
                                    data.storeCode = '-';
                                }
                                $('#storeCode').html(data.storeCode);

                                if (null === data.cityCode.name) {
                                    data.cityCode.name = '-';
                                }
                                $('#cityName').html(data.cityCode.name);

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (true === data.isDefault) {
                                    data.isDefault = '<span class="label label-primary">是</span>';
                                } else if (false === data.isDefault) {
                                    data.isDefault = '<span class="label label-danger">否</span>';
                                } else {
                                    data.isDefault = '<span class="label label-danger">-</span>';
                                }
                                $('#isDefault').html(data.isDefault);

                                if (true === data.enable) {
                                    data.enable = '<span class="label label-primary">是</span>';
                                }else if(false === data.enable){
                                    data.enable = '<span class="label label-danger">否</span>';
                                }else{
                                    data.enable  = '<span class="label label-danger">-</span>';
                                }
                                $('#enable').html(data.enable);

                                if ('ZY' === data.storeType) {
                                    data.storeType = '直营';
                                } else if ('ZS' === data.storeType) {
                                    data.storeType = '装饰公司';
                                } else if ('JM' === data.storeType) {
                                    data.storeType = '加盟店';
                                } else {
                                    data.storeType = '-';
                                }
                                $('#storeTypeName').html(data.storeType);

                                if (null === data.createTime) {
                                    $('#createTime').html('-');
                                } else {
                                    $('#createTime').html(formatDateTime(data.createTime));
                                }

                                if (true === data.isSelfDelivery) {
                                    data.isSelfDelivery = '是';
                                } else if (false === data.isSelfDelivery) {
                                    data.isSelfDelivery = '否';
                                } else {
                                    data.isSelfDelivery = '-';
                                }
                                $('#isSelfDelivery').html(data.isSelfDelivery);

                                if (null === data.province) {
                                    data.province = '-';
                                }
                                $('#province').html(data.province);

                                if (null === data.city) {
                                    data.city = '-';
                                }
                                $('#city').html(data.city);

                                if (null === data.area) {
                                    data.area = '-';
                                }
                                $('#area').html(data.area);

                                if (null === data.detailedAddress) {
                                    data.detailedAddress = '-';
                                }
                                $('#detailedAddress').html(data.detailedAddress);

                                if (null === data.phone) {
                                    data.phone = '-';
                                }
                                $('#phone').html(data.phone);

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

    var formatDateTimeBirthday = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        return y + '-' + m + '-' + d ;
    };


    function findStorePreByCity(cityId) {
        $("#queryCusInfo").val('');
        var keywords = $('#queryCusInfo').val();
        $("#dataGrid").bootstrapTable('destroy');
        var storeType = $("#storeType").val();
        findStorePreByCityIdOrKeywords(keywords,cityId,storeType);
    }

    function findStorePreByNameOrCode() {
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var storeType = $("#storeType").val();
        findStorePreByCityIdOrKeywords(queryCusInfo,cityId,storeType);
    }

    function findStorePreByStoreType(storeType) {
        $("#queryCusInfo").val('');
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        findStorePreByCityIdOrKeywords(queryCusInfo,cityId,storeType);
    }

    function findStorePreByCityIdOrKeywords(keywords,cityId,storeType){
        initDateGrid(keywords,cityId,storeType);
    }

    function showDetails(storeId){
        window.location.href = '/views/admin/store/preDeposit/log/list/'+storeId;
    }
    function changePre(storeId){
        window.location.href = '/views/admin/store/preDeposit/edit/'+storeId;
    }
</script>
</body>