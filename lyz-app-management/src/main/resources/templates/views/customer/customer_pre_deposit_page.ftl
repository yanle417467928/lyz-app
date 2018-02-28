<head>

    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
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
                               placeholder="请输入要查找的姓名或电话..">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCusByNameOrPhone()">查找</button>
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
                    <ul id="cusDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>顾客ID</b> <a class="pull-right" id="cusId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>顾客姓名</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>微信昵称</b> <a class="pull-right" id="nickName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>顾客电话</b> <a class="pull-right" id="mobile"></a>
                        </li>
                        <li class="list-group-item" style="height: 100px;">
                            <b>顾客头像</b> <a class="pull-right" id="picUrl"></a>
                        </li>
                        <li class="list-group-item">
                            <b>出生日期</b> <a class="pull-right" id="birthday"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属城市</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属门店</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属导购</b> <a class="pull-right" id="salesConsultName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否生效</b> <a class="pull-right" id="status"></a>
                        </li>
                        <li class="list-group-item">
                            <b>性别</b> <a class="pull-right" id="sex"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否允许货到付款</b> <a class="pull-right" id="isCashOnDelivery"></a>
                        </li>
                        <li class="list-group-item">
                            <b>顾客灯号</b> <a class="pull-right" id="light"></a>
                        </li>
                        <li class="list-group-item">
                            <b>顾客类型</b> <a class="pull-right" id="customerType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>注册来源</b> <a class="pull-right" id="createType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>上次签到时间</b> <a class="pull-right" id="lastSignTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>绑定时间</b> <a class="pull-right" id="bindingTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
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
        initDateGird(null,null,null);
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
            url: '/rest/stores/findStoresListByStoreId',
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

    function initDateGird(keywords,cityId,storeId) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/customer/preDeposit/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                cityId: cityId,
                storeId: storeId
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'cusId',
            title: '顾客ID',
            align: 'center'
        }, {
            field: 'name',
            title: '顾客姓名',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.cusId);
                }
            },
            formatter: function (value) {
                if(null==value){
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                }else{
                    return '<a class="scan" href="#">' + value + '</a>';
                }
            }
        }, {
            field: 'mobile',
            title: '顾客电话',
            align: 'center'
        }, {
            field: 'storeName',
            title: '归属门店',
            align: 'center'
        }, {
            field: 'balance',
            title: '预存款余额(￥)',
            align: 'center'
        }, {
            field: 'cusId',
            title: '操作',
            align: 'center',
            formatter: function(value,row) {
                return '<button class="btn btn-primary btn-xs" onclick="showDetails('+row.cusId+')"> 查看明细</button><button class="btn  btn-danger btn-xs" style="margin-left: 10px" onclick="changePre('+row.cusId+')"> 变更</button>';
            }
        }
        ]);
    }


    var $page = {
        information: {
            show: function (cusId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/customers/' + cusId,
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
                                $('#menuTitle').html(" 顾客详情");

                                if (null === data.cusId) {
                                    data.cusId = '-';
                                }
                                $('#cusId').html(data.cusId);

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);

                                if (null === data.nickName) {
                                    data.nickName = '-';
                                }
                                $('#nickName').html(data.nickName);

                                if (null === data.mobile) {
                                    data.mobile = '-';
                                }
                                $('#mobile').html(data.mobile);

                                if (null === data.picUrl || '' == data.picUrl) {
                                    $('#picUrl').html('-');
                                } else {
                                    $('#picUrl').html('<img  src="' + data.picUrl + '"' + ' class="img-rounded" style="height: 80px;width: 80px;" >');
                                }


                                if (null === data.birthday) {
                                    $('#birthday').html('-');
                                } else {
                                    $('#birthday').html(formatDateTimeBirthday(data.birthday));
                                }


                                if (null == data.city) {
                                    $('#cityName').html('-');
                                } else {
                                    if (null == data.city.name) {
                                        $('#cityName').html('-');
                                    } else {
                                        $('#cityName').html(data.city.name);
                                    }
                                }


                                if (null == data.store) {
                                    $('#storeName').html('-');
                                } else {
                                    if (null == data.store.storeName) {
                                        $('#storeName').html('-');
                                    } else {
                                        $('#storeName').html(data.store.storeName);
                                    }
                                }


                                if (null == data.salesConsultId) {
                                    $('#salesConsultName').html('-');
                                } else {
                                    if (null === data.salesConsultId.name) {
                                        $('#salesConsultName').html('-');
                                    } else {
                                        $('#salesConsultName').html(data.salesConsultId.name);
                                    }
                                }


                                if ('MALE' === data.sex) {
                                    data.sex = '男性';
                                } else if ('FEMALE' === data.sex) {
                                    data.sex = '女性';
                                } else if ('SECRET' === data.sex) {
                                    data.sex = '保密';
                                } else {
                                    data.sex = '-';
                                }
                                $('#sex').html(data.sex);

                                if (true === data.isCashOnDelivery) {
                                    data.isCashOnDelivery = '<span class="label label-primary">是</span>';
                                } else if (false === data.isCashOnDelivery) {
                                    data.isCashOnDelivery = '<span class="label label-danger">否</span>';
                                } else {
                                    data.isCashOnDelivery = '<span class="label label-danger">-</span>';
                                }
                                $('#isCashOnDelivery').html(data.isCashOnDelivery);


                                if ('绿灯' === data.light) {
                                    data.light = '<span class="label label-success">绿灯</span>';
                                }else if('红灯' === data.light){
                                    data.light = '<span class="label label-danger">红灯</span>';
                                } else if('黄灯' === data.light){
                                    data.light = '<span class="label label-warning">黄灯</span>';
                                } else if('熄灯' === data.light) {
                                    data.light = '<span class="label label-deafult">熄灯</span>';
                                }else{
                                    data.light = '<span class="label label-danger">-</span>';
                                }
                                $('#light').html(data.light);

                                if ('MEMBER' === data.customerType) {
                                    data.customerType = '会员';
                                } else if ('RETAIL' === data.customerType) {
                                    data.customerType = '零售';
                                } else {
                                    data.customerType = '-';
                                }
                                $('#customerType').html(data.customerType);

                                if ('APP_REGISTRY' === data.createType) {
                                    data.createType = 'APP';
                                } else if('Background add' === data.createType) {
                                    data.createType = '后台添加';
                                }else {
                                    data.createType = '-';
                                }
                                $('#createType').html(data.createType);

                                if (null === data.lastSignTime) {
                                    $('#lastSignTime').html('-');
                                } else {
                                    $('#lastSignTime').html(formatDateTime(data.lastSignTime));
                                }


                                if (null === data.bindingTime) {
                                    $('#bindingTime').html('-');
                                } else {
                                    $('#bindingTime').html(formatDateTime(data.bindingTime));
                                }


                                if (null === data.createTime) {
                                    $('#createTime').html('-');
                                } else {
                                    $('#createTime').html(formatDateTime(data.createTime));
                                }


                                if (true === data.status) {
                                    data.status = '<span class="label label-primary">是</span>';
                                }else if(false === data.status){
                                    data.status = '<span class="label label-danger">否</span>';
                                }else{
                                    data.status = '<span class="label label-danger">-</span>';
                                }
                                $('#status').html(data.status);


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


    function findCusByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var keywords = $('#queryCusInfo').val();
        $("#dataGrid").bootstrapTable('destroy');
        findCusByCityIdOrstoreIdOrKeywords(keywords,cityId,storeId);
        if(cityId==-1){
            findStorelist();
            return false;
        };
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityIdAndStoreId/' + cityId,
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

    function findCusByCityIdOrstoreIdOrKeywords(keywords,cityId,storeId){
        initDateGird(keywords,cityId,storeId);
    }

    function findCusByStoreId() {
        $("#queryCusInfo").val('');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        findCusByCityIdOrstoreIdOrKeywords(keywords,cityId,storeId);

    }

    function findCusByNameOrPhone() {
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        findCusByCityIdOrstoreIdOrKeywords(queryCusInfo,cityId,storeId);
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }
    function showDetails(cusId){
        window.location.href = '/views/admin/customer/preDeposit/log/list/'+cusId;
    }
    function changePre(cusId){
        window.location.href = '/views/admin/customer/preDeposit/edit/'+cusId;
    }
</script>
</body>