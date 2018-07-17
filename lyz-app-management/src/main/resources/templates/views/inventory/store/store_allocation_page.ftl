<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
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
    <h1>调拨单管理</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <form class="form-horizontal" id="formSearch">
                    <div id="" class="box-body ">
                        <div class="row">
                            <div class="col-xs-12 col-md-2 ">
                                <select name="company" id="company" class="form-control select" style="width:auto;"
                                        onchange="findByInStructureCode()">
                                    <option value="-1">选择分公司</option>
                                <#if structureList?? && structureList?size gt 0 >
                                    <#list structureList as structure>
                                        <option value="${structure.number!''}">${structure.structureName!''}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                            <div class="col-xs-12 col-md-2 " style="margin-left: -5.5%">
                                <select name="inStoreCode" id="inStoreCode" class="form-control selectpicker"
                                        data-width="140px" data-live-search="true">
                                    <option value="-1">选择调入门店</option>
                                <#if storeList?? && storeList?size gt 0 >
                                    <#list storeList as store>
                                        <option value="${store.id!''}">${store.storeName!''}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                            <div class="col-xs-12 col-md-2" style="margin-left: -3.5%">
                                <select name="outStoreCode" id="outStoreCode" class="form-control selectpicker"
                                        data-width="140px" data-live-search="true">
                                    <option value="-1">选择调出门店</option>
                                <#if storeList?? && storeList?size gt 0 >
                                    <#list storeList as store>
                                        <option value="${store.id!''}">${store.storeName!''}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                            <div class="col-xs-12 col-md-2" style="margin-left: -3.5%">
                                <select name="selectStatus" id="selectStatus" class="form-control select"
                                        style="width:auto;">
                                    <option value="-1">选择状态</option>
                                    <option value="NEW" <#if status?? && status==1>selected</#if>>新&nbsp;&nbsp;&nbsp;&nbsp;建</option>
                                    <option value="SENT" <#if status?? && status==2>selected</#if>>已出库</option>
                                    <option value="ENTERED" <#if status?? && status==3>selected</#if>>已入库</option>
                                    <option value="CANCELLED" <#if status?? && status==4>selected</#if>>已作废</option>
                                </select>
                            </div>
                            <div class="col-xs-12 col-md-2" style="margin-left: -6.8%">
                                <input name="startDateTime" type="text" class="form-control datepicker"
                                       id="startDateTime"
                                       placeholder="开始时间">
                            </div>
                            <div class="col-xs-12 col-md-1" style="margin-left: -2%;margin-top: 0.5%">
                                至
                            </div>
                            <div class="col-xs-12 col-md-2" style="margin-left: -6%">
                                <input name="endDateTime" type="text" class="form-control datepicker" id="endDateTime"
                                       placeholder="结束时间">
                            </div>
                            <div class="col-xs-12 col-md-2" style="margin-left: -2%">
                                <input type="text" name="queryInfo" id="queryInfo" class="form-control "
                                       placeholder="请输入商品编码">
                            </div>
                        </div>
                        <div class="row" style="margin-top: 1%">
                            <div class="col-xs-12 col-md-9">
                            </div>
                            <div class="col-xs-12 col-md-1" style="margin-left: 4%">
                                <button type="button" id="btn_query" class="btn btn-primary">
                                    <i class="fa fa-search"></i> 查询
                                </button>
                            </div>
                            <div class="col-xs-12 col-md-1" style="margin-left: -1.5%">
                                <button type="reset" class="btn btn-default" onclick="clearAll()">
                                    <i class="fa fa-print"></i> 重置
                                </button>
                            </div>
                        </div>
                        <input type="hidden" id="formName" name="formName">
                        <input type="hidden" id="toName" name="toName">
                        <input type="hidden" id="city" name="city">
                        <input type="hidden" id="allocationTypeEnum" name="allocationTypeEnum">
                    </div>
                    <div id="toolbar" class="form-inline">
                        <button id="btn_add" type="button" class="btn btn-default">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                        </button>
                        <div id="toolbar" class="form-inline">
                        </div>
                    </div>
                </form>
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
                            <b>单号</b> <a class="pull-right" id="number"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市id</b> <a class="pull-right" id="cityId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>城市名称</b> <a class="pull-right" id="cityName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>调出门店ID</b> <a class="pull-right" id="allocationFrom"></a>
                        </li>
                        <li class="list-group-item">
                            <b>调出门店名称</b> <a class="pull-right" id="allocationFromName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>调入门店ID</b> <a class="pull-right" id="allocationTO"></a>
                        </li>
                        <li class="list-group-item">
                            <b>调入门店名称</b> <a class="pull-right" id="allocationToName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>状态</b> <a class="pull-right" id="status"></a>
                        </li>
                        <li class="list-group-item">
                            <b>备注</b> <a class="pull-right" id="comment"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建者</b> <a class="pull-right" id="creator"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>修改者</b> <a class="pull-right" id="modifier"></a>
                        </li>
                        <li class="list-group-item">
                            <b>修改时间</b> <a class="pull-right" id="modifyTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>调拨明细</b>
                            <table id="details" class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>商品编码</th>
                                    <th>商品名称</th>
                                    <th>数量</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </li>
                        <li class="list-group-item">
                            <b>调拨经办人</b> <a class="pull-right" id=""></a>
                            <table id="trails" class="table table-bordered">

                            </table>
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

        initDateGrid('/rest/allocation/queryPage/grid');

        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/inventory/allocation/add?parentMenuId=${(parentMenuId!'0')}')
        });

        $('#btn_query').on('click', function () {
            initDateGrid('/rest/allocation/queryPage/grid');
        });
    });


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
                        url: '/rest/store/allocation/' + id,
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
                                $('#menuTitle').html("调拨详情");

                                if (null === data.cityId) {
                                    data.cityId = '-';
                                }
                                $('#cityId').html(data.cityId);

                                if (null === data.number) {
                                    data.number = '-';
                                }
                                $('#number').html(data.number);

                                if (null === data.cityName) {
                                    data.cityName = '-';
                                }
                                $('#cityName').html(data.cityName);

                                if (null === data.allocationFrom) {
                                    data.allocationFrom = '-';
                                }
                                $('#allocationFrom').html(data.allocationFrom);

                                if (null === data.allocationTo) {
                                    data.allocationTo = '-';
                                }
                                $('#allocationTo').html(data.allocationTo);
                                if (null === data.status) {
                                    data.status = '-';
                                }
                                if ('NEW' === data.status) {
                                    $('#status').html("新建")
                                } else if ('SENT' === data.status) {
                                    $('#status').html("已出库")
                                } else if ('ENTERED' === data.status) {
                                    $('#status').html("已入库")
                                } else if ('CANCELLED' === data.status) {
                                    $('#status').html("已作废")
                                }
                                if (null === data.allocationFromName) {
                                    data.allocationFromName = '-';
                                }
                                $('#allocationFromName').html(data.allocationFromName);
                                if (null === data.allocationToName) {
                                    data.allocationToName = '-';
                                }
                                $('#allocationToName').html(data.allocationToName);
                                if (null === data.comment) {
                                    data.comment = '-';
                                }
                                $('#comment').html(data.comment);

                                if (null === data.creator) {
                                    data.creator = '-';
                                }
                                $('#creator').html(data.creator);

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                }
                                $('#createTime').html(data.createTime);

                                if (null === data.modifier) {
                                    data.modifier = '-';
                                }
                                $('#modifier').html(data.modifier);

                                if (null === data.modifyTime) {
                                    data.modifyTime = '-';
                                }
                                $('#modifyTime').html(data.modifyTime);

                                if (null === data.details) {
                                    data.details = '-';
                                }
                                var str = "";
                                $.each(data.details, function (i, item) {
                                    str += "<tr><td>" + item.sku + "</td><td>" + item.skuName + "</td><td>" + item.qty + "</td></tr>";
                                });
                                $('#details').find('tbody').append(str);

                                if (null === data.trails) {
                                    data.trails = '-';
                                }
                                var temp = "";
                                $.each(data.trails, function (j, item) {
                                    temp += "<tr><td>" + item.operator + "</td><td>" + item.operation + "</td><td>" + item.operateTime + "</td></tr>";
                                });
                                $('#trails').find('tbody').append(temp);

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

    function turnDetail(id) {
        window.location.href = "/views/admin/inventory/allocation/detail/" + id;
    }


    function findByInStructureCode() {
        findStoreList();
    }

    function findStoreList() {
        initSelect("#inStoreCode", "选择调入门店");
        initSelect("#outStoreCode", "选择调出门店");
        var store = "";
        var toName = $("#inStoreCode");
        var fromName = $("#outStoreCode");
        var company = $('#company').val();
        var data = {
            "companyCode": company
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
                toName.append(store);
                fromName.append(store);
                toName.selectpicker('refresh');
                toName.selectpicker('render');
                fromName.selectpicker('refresh');
                fromName.selectpicker('render');
            }
        });
    }

    function clearAll() {
        var company = $('#company').val('-1');
        findStoreList();
    }

    function initDateGrid(url) {
        $("#dataGrid").bootstrapTable('destroy');
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: $('#queryInfo').val(),
                company: $('#company').val(),
                outStore: $('#outStoreCode').val(),
                inStore: $('#inStoreCode').val(),
                selectStatus: $('#selectStatus').val(),
                startDateTime: $('#startDateTime').val(),
                endDateTime: $('#endDateTime').val(),
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'id',
            title: 'ID',
            align: 'center'
        }, {
            field: 'number',
            title: '单号',
            events: {
                'click .scan': function (e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function (value) {
                return '<a class="scan" href="#">' + value + '</a>';
            },
            align: 'center'
        }, {
            field: 'cityName',
            title: '城市',
            align: 'center'
        }, {
            field: 'allocationFromName',
            title: '调出门店',
            align: 'center'
        }, {
            field: 'allocationToName',
            title: '调入门店',
            align: 'center'
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            formatter: function (value, row, index) {
                if ('NEW' === value) {
                    return '<span class="">新建</span>';
                } else if ('SENT' === value) {
                    return '<span class="">已出库</span>';
                } else if ('ENTERED' === value) {
                    return '<span class="">已入库</span>';
                } else if ('CANCELLED' === value) {
                    return '<span class="">已作废</span>';
                }
            }
        }, {
            field: 'modifyTime',
            title: '修改时间',
            align: 'center'
        },
            {
                title: '操作',
                align: 'center',
                formatter: function (value, row) {
                    return '<button class="btn btn-primary btn-sm" onclick="turnDetail(' + row.id + ')"> 调拨</button>';
                }
            }
        ]);
    }
</script>
</body>