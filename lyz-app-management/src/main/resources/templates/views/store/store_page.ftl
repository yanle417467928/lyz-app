<head>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
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
        <div class=" col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                    <select name="city" id="cityCode" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findStoreByCity(this.value);">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="enabled" id="enabled" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findStoreByEnable(this.value);">
                        <option value="-1">是否可用</option>
                    </select>
                    <input type="text" name="queryStoreInfo" id="queryStoreInfo" class="form-control "
                           style="width:auto;" placeholder="请输入要查找的店名或编码..">
                    <button type="button" name="search" id="search-btn" class="btn btn-flat "
                            onclick="return findStoreByNameOrCode()">
                        <i class="fa fa-search"></i>
                    </button>
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
                    <ul id="storeDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
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
                            <b>门店类型</b> <a class="pull-right" id="storeType"></a>
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
    });


    $(function () {
        initDateGird('/rest/stores/page/grid');
    });

    function initDateGird(url) {
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
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
            field: 'cityId.name',
            title: '所属城市',
            align: 'center'
        }, {
            field: 'enable',
            title: '是否生效',
            align: 'center',
            formatter: function (value, row, index) {
                if (true === value) {
                    return '是'
                } else if (false === value) {
                    return '否'
                } else {
                    return '-';
                }
            }
        },
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

                                if (null === data.cityId.name) {
                                    data.cityId.name = '-';
                                }
                                $('#cityName').html(data.cityId.name);

                                if (null === data.storeName) {
                                    data.storeName = '-';
                                }
                                $('#storeName').html(data.storeName);

                                if (true === data.isDefault) {
                                    data.isDefault = '是';
                                } else if (false === data.isDefault) {
                                    data.isDefault = '否';
                                } else {
                                    data.isDefault = '-';
                                }
                                $('#isDefault').html(data.isDefault);

                                if (true === data.enable) {
                                    data.enable = '是';
                                } else if (false === data.enable) {
                                    data.enable = '否';
                                } else {
                                    data.enable = '-';
                                }
                                $('#enable').html(data.enable);

                                if ('ZY' === data.storeType) {
                                    data.storeType = '自营';
                                } else if ('ZS' === data.storeType) {
                                    data.storeType = '装饰公司';
                                } else if ('JM' === data.storeType) {
                                    data.storeType = '加盟店';
                                } else {
                                    data.storeType = '-';
                                }
                                $('#storeType').html(data.storeType);

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


    function findStoreByCity(cityId) {
        $("#enabled").empty();
        var enabled = '';
        if (-1 == $("#cityCode").val()) {
            enabled += "<option value=-1>是否可用</option>";
        } else {
            enabled += "<option value=-1>是否可用</option><option value=1>可用</option><option value=0>不可用</option>";
        }
        $("#enabled").append(enabled);
        findStoreByCityId(cityId);
    }

    function findStoreByCityId(cityId) {
        $("#queryStoreInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        if (cityId == -1) {
            initDateGird('/rest/stores/page/grid');
        } else {
            initDateGird('/rest/stores/findStoresListByCity/' + cityId);
        }
    }

    function findStoreByEnable(enabled) {
        $("#queryStoreInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        if (enabled == -1) {
            initDateGird('/rest/stores/findStoresListByCity/' + cityId);
        } else {
            initDateGird('/rest/stores/findStoresListByEnable?enabled=' + enabled + '&cityId=' + cityId);
        }
    }

    function findStoreByNameOrCode() {
        var queryStoreInfo = $("#queryStoreInfo").val();
        if (null == queryStoreInfo || "" == queryStoreInfo) {
            $("#dataGrid").bootstrapTable('destroy');
            $('#cityCode').val("-1");
            initSelect("#enabled", "是否可用");
            initDateGird('/rest/stores/page/grid');
        } else {
            if (!checkCharacter(queryStoreInfo)) {
                $notify.warning('请检查输入是否为汉子或者字母');
                $('#cityCode').val("-1");
                initSelect("#enabled", "是否可用");
                return false;
            }
            $("#dataGrid").bootstrapTable('destroy');
            $('#cityCode').val("-1");
            initSelect("#enabled", "是否可用");
            initDateGird('/rest/stores/page/storeGrid/' + queryStoreInfo);
        }
    }


    function checkCharacter(theObj) {
        var reg = /^([A-Za-z]|[\u4E00-\u9FA5])+$/;
        if (reg.test(theObj)) {
            return true;
        }
        return false;
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
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