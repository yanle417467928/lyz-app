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
        <div class="col-xs-12">

            <div class="box box-primary">

                <div id="toolbar" class="btn-group">
                    <select name="city" id="cityCode" class="form-control select" data-width="120px"
                            style="width:auto;"
                            onchange="statusChange()" data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>
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
                    <ul id="citysDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>城市编码</b> <a class="pull-right" id="code"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市名称</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>所属分公司</b> <a class="pull-right" id="structureTitle"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否生效</b> <a class="pull-right" id="enable"></a>
                        </li>
                        <li class="list-group-item">
                            <b>失效时间</b> <a class="pull-right" id="enableFalseTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市名称拼音</b> <a class="pull-right" id="spell"></a>
                        </li>
                        <li class="list-group-item">
                            <b>所属分公司ID</b> <a class="pull-right" id="structureId"></a>
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
            }
        });
    }
    function statusChange() {
        $("#dataGrid").bootstrapTable('destroy');
        $(function () {
            $grid.init($('#dataGrid'), $('#toolbar'), '/rest/cityInventory/page/grid', 'get', true, function (params) {
                return {
                    offset: params.offset,
                    size: params.limit,
                    keywords: params.search,
                    cityId: $("#cityCode").val()
                }
            }, [{
                checkbox: true,
                title: '选择'
            },
                {
                    field: 'cityId',
                    title: '城市ID',
                    align: 'center',
                    visible: false
                }, {
                    field: 'code',
                    title: '城市编码',
                    align: 'center'
                }, {
                    field: 'name',
                    title: '城市名称',
                    align: 'center',
                    events: {
                        'click .scan': function (e, value, row) {
                            $page.information.show(row.cityId);
                        }
                    },
                    formatter: function (value) {
                        return '<a class="scan" href="#">' + value + '</a>';
                    }
                }, {
                    field: 'sku',
                    title: '商品编码',
                    align: 'center'
                }, {
                    field: 'skuName',
                    title: '商品名称',
                    align: 'center'
                }, {
                    field: 'availableIty',
                    title: '城市可用量',
                    align: 'center'
                }
            ]);
        });
    }



    $(function () {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/cityInventory/page/grid', 'get', true, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search,
                cityId: $("#cityCode").val()
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'cityId',
                title: '城市ID',
                align: 'center',
                visible: false
            }, {
                field: 'code',
                title: '城市编码',
                align: 'center'
            }, {
                field: 'name',
                title: '城市名称',
                align: 'center',
                events: {
                    'click .scan': function (e, value, row) {
                        $page.information.show(row.cityId);
                    }
                },
                formatter: function (value) {
                    return '<a class="scan" href="#">' + value + '</a>';
                }
            }, {
                field: 'sku',
                title: '商品编码',
                align: 'center'
            }, {
                field: 'skuName',
                title: '商品名称',
                align: 'center'
            }, {
                field: 'availableIty',
                title: '城市可用量',
                align: 'center'
            }
        ]);
    });
    findCitylist();
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

    var $page = {
        information: {
            show: function (cityId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/citys/' + cityId,
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
                                $('#menuTitle').html("城市详情");

                                if (null === data.code) {
                                    data.code = '-';
                                }
                                $('#code').html(data.code);

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);

                                if (null === data.spell) {
                                    data.spell = '-';
                                }
                                $('#spell').html(data.spell);

                                if (null === data.structureId) {
                                    data.structureId = '-';
                                }
                                $('#structureId').html(data.structureId);

                                if (null === data.structureTitle) {
                                    data.structureTitle = '-';
                                }
                                $('#structureTitle').html(data.structureTitle);

                                if (true == data.enable) {
                                    $('#enable').html('<span class="label label-primary">是</span>');
                                } else if (false == data.enable) {
                                    $('#enable').html('<span class="label label-danger">否</span>');
                                } else {
                                    $('#enable').html('<span class="label label-danger">-</span>');
                                }

                                if (null === data.enableFalseTime) {
                                    $('#enableFalseTime').html('-');
                                } else {
                                    $('#enableFalseTime').html(formatDateTime(data.enableFalseTime));
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
</script>
</body>