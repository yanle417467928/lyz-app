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
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                    <button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryStoreInfo" id="queryStoreInfo" class="form-control " style="width:auto;"  placeholder="请输入品牌名称..">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findBrandByName()">查找</button>
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
                    <ul id="citysDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>品牌ID</b> <a class="pull-right" id="brdId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>品牌名称</b> <a class="pull-right" id="brandName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>品牌编码</b> <a class="pull-right" id="brandCode"></a>
                        </li>
                        <li class="list-group-item">
                            <b>排序号</b> <a class="pull-right" id="sortId"></a>
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
    $(function() {
        initDateGird('/rest/goodsBrand/page/grid');
        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/goodsBrands/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function() {
            modify($('#dataGrid'),'/views/admin/goodsBrands/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
        $('#btn_delete').on('click', function () {
            remove($('#dataGrid'), '/rest/goodsBrand', 'delete');
        });
    });

    var formatDateTime = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = dt.getHours();
        h=h < 10 ? ('0' + h) : h;
        var minute = dt.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        var second=dt.getSeconds();
        second=second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
    };

    var $page = {
        information: {
            show: function (brdId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/goodsBrand/' + brdId,
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
                                $('#menuTitle').html("品牌详情");

                                if (null === data.brdId) {
                                    data.brdId = '-';
                                }
                                $('#brdId').html(data.brdId);

                                if (null === data.brandName) {
                                    data.brandName = '-';
                                }
                                $('#brandName').html(data.brandName);

                                if (null === data.brandCode) {
                                    data.brandCode = '-';
                                }
                                $('#brandCode').html(data.brandCode);

                                if (null === data.sortId) {
                                    data.sortId = '-';
                                }
                                $('#sortId').html(data.sortId);

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


    function findBrandByName() {
        $("#dataGrid").bootstrapTable('destroy');
        var queryStoreInfo = $("#queryStoreInfo").val();
        if ('' == queryStoreInfo || null == queryStoreInfo) {
            initDateGird('/rest/goodsBrand/page/grid');
        } else {
            initDateGird('/rest/goodsBrand/findBrandByName/' + queryStoreInfo);
        }
    }


    function remove(container, url, method) {
        if (null === $global.timer) {
            var selected = this.getSelectedIds(container);
            if (null === selected || 0 === selected.length) {
                $notify.warning('请在点击按钮前选中至少一条数据');
                return;
            }
            $modal.danger('危险操作', '数据删除后无法恢复，是否确认？', function () {

                $global.timer = setTimeout($loading.show, 2000);

                var data = {
                    ids: selected
                };
                var type = null;
                if ('delete' === method || 'DELETE' === method || 'put' === method || 'PUT' === method) {
                    data._method = method;
                    type = 'POST';
                } else {
                    type = method;
                }
                $.ajax({
                    url: url,
                    type: type,
                    traditional: true,
                    data: data,
                    error: function (result) {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        if (-1 === result.code) {
                            if (null !== result.message) {
                                $notify.success(result.message);
                            }
                        } else {
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        }

                    },
                    success: function (result) {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        if (0 === result.code) {
                            container.bootstrapTable('refresh');
                            if (null !== result.message) {
                                $notify.success(result.message);
                            }
                        } else {
                            $notify.danger(result.message);
                        }
                    }
                });
            });
        }
    }


    function modify(container, url) {
        var selected = this.getSelectedIds(container);
        var length = selected.length;
        if (length === 0) {
            $notify.warning('请在点击按钮前选中一条数据');
        } else if (length > 1) {
            $notify.warning('您每次只能选择一条数据进行修改');
        } else {
            var id = selected[0];
            url = url.replace('{id}', id);
            window.location.href = url;
        }
    }


    function getSelectedIds(container){
        var ids = [];
        var selected = container.bootstrapTable('getSelections');
        for (var i = 0; i < selected.length; i++ ) {
            var data = selected[i];
            ids.push(data.brdId);
        }
        return ids;
    }

     function initDateGird(url) {
         $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function(params) {
             return {
                 offset: params.offset ,
                 size: params.limit,
                 keywords: params.search
             }
         }, [{
             checkbox: true,
             title: '选择'
         },
             {
                 field: 'brdId',
                 title: '品牌ID',
                 align: 'center',
             },
             {
                 field: 'brandName',
                 title: '品牌名称',
                 align: 'center',
                 events: {
                     'click .scan': function(e, value, row) {
                         $page.information.show(row.brdId);
                     }
                 },
                 formatter: function(value) {
                     return '<a class="scan" href="#">' + value + '</a>';
                 }
             },{
                 field: 'sortId',
                 title: '排序号',
                 align: 'center'
             }
         ]);
     }
</script>
</body>