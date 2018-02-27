<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
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
                            data-live-search="true" onchange="findDecorativeByCondition()">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="enabled" id="enabled" class="form-control select" style="width:auto;"
                            data-live-search="true" onchange="findDecorativeByCondition()">
                        <option value="-1">是否可用</option>
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>

                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryDecorativeInfo" id="queryDecorativeInfo" class="form-control "
                               style="width:auto;" placeholder="请输入公司名称或公司编码..">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findDecorativeByNameOrCode()">查找</button>
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
                    <ul id="storeDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>装饰公司ID</b> <a class="pull-right" id="storeId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>装饰公司编码</b> <a class="pull-right" id="storeCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>所属城市</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>公司名称</b> <a class="pull-right" id="storeName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>销售经理</b> <a class="pull-right" id="salesManager"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否启用</b> <a class="pull-right" id="enable"></a>
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
        initDateGird('/rest/decorativeInfo/page/grid');
        findCitySelection();
    });


    function findCitySelection() {
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
            field: 'storeCode',
            title: '装饰公司编码',
            align: 'center'
        }, {
            field: 'storeName',
            title: '公司名称',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        }, {
            field: 'cityCode.name',
            title: '所属城市',
            align: 'center'
        }, {
            field: 'enable',
            title: '是否启用',
            align: 'center',
            formatter: function (value, row, index) {
                if (true === value) {
                    return '<span class="label label-primary">是</span>';
                } else if (false === value) {
                    return '<span class="label label-danger">否</span>';
                } else {
                    return '<span class="label label-danger">-</span>';
                }
            }
        }, {
            field: 'id',
            title: '公司对账单',
            align: 'center',
            formatter: function (value) {
                return '<a class="scan" href="/views/admin/decorativeCredit/reconciliation/detail/' + value + '">查看对账单</a>';
            }
        }
        ]);
    }

    var $page = {
        information: {
            show: function (decorativeCompanyId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/decorativeInfo/' + decorativeCompanyId,
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
                                $('#menuTitle').html("装饰公司详情");

                                if (null === data.id) {
                                    data.id = '-';
                                }
                                $('#storeId').html(data.id);

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


                                if (true === data.enable) {
                                    data.enable = '<span class="label label-primary">是</span>';
                                } else if (false === data.enable) {
                                    data.enable = '<span class="label label-danger">否</span>';
                                } else {
                                    data.enable = '<span class="label label-danger">-</span>';
                                }
                                $('#enable').html(data.enable);


                                if (null === data.createTime) {
                                    $('#createTime').html('-');
                                } else {
                                    $('#createTime').html(formatDateTime(data.createTime));
                                }

                                if (null === data.salesManager) {
                                    $('#salesManager').html('-');
                                } else {
                                    $('#salesManager').html(data.salesManager);
                                }


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


    function findDecorativeByCondition() {
        $("#queryStoreInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var cityId = $("#cityCode").val();
        var enabled = $("#enabled").val();
        initDateGird('/rest/decorativeInfo/findDecorativeByCondition?enabled=' + enabled + '&cityId=' + cityId);
    }

    function findDecorativeByNameOrCode() {
        var queryDecorativeInfo = $("#queryDecorativeInfo").val();
        $('#cityCode').val("-1");
        $('#enabled').val("-1");
        if (null == queryDecorativeInfo || "" == queryDecorativeInfo) {
            $("#dataGrid").bootstrapTable('destroy');
            initDateGird('/rest/decorativeInfo/page/grid');
        } else {
            if (!checkCharacter(queryDecorativeInfo)) {
                $notify.warning('请检查输入是否为汉子或者字母');
                return false;
            }
            $("#dataGrid").bootstrapTable('destroy');
            initDateGird('/rest/decorativeInfo/page/infoGrid/' + queryDecorativeInfo);
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