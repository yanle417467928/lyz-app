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
                <div id="toolbar" class="btn-group">
                    <button id="btn_add" type="button" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <#--<button id="btn_delete" type="button" class="btn btn-default">-->
                        <#--<span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除-->
                    <#--</button>-->
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
                        <a id="companyTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="resourceDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>装饰公司名称</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>装饰公司编码</b> <a class="pull-right" id="code"></a>
                        </li>
                        <li class="list-group-item">
                            <b>公司地址</b> <a class="pull-right" id="address"></a>
                        </li>
                        <li class="list-group-item">
                            <b>公司电话</b> <a class="pull-right" id="phone"></a>
                        </li>
                        <li class="list-group-item">
                            <b>信用金余额</b> <a class="pull-right" id="credit"></a>
                        </li>
                        <li class="list-group-item">
                            <b>赞助金余额</b> <a class="pull-right" id="promotionMoney"></a>
                        </li>
                        <li class="list-group-item">
                            <b>钱包金额</b> <a class="pull-right" id="walletMoney"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否冻结</b> <a class="pull-right" id="frozen"></a>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/company/page/grid', 'get', true, function(params) {
            return {
                offset: params.offset ,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'id',
            title: '序号',
            align: 'center'
        }, {
            field: 'name',
            title: '装饰公司名称',
            align: 'center',
            events: {
                'click .scan': function(e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function(value) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
        },{
            field: 'code',
            title: '装饰公司编码',
            align: 'center'
        },{
            field: 'address',
            title: '公司地址',
            align: 'center'
        },{
            field: 'phone',
            align: 'center',
            title: '公司电话'
        },{
            field: 'credit',
            title: '信用金余额',
            align: 'center'
        },{
            field: 'promotionMoney',
            align: 'center',
            title: '赞助金余额'
        },{
            field: 'walletMoney',
            align: 'center',
            title: '钱包金额'
        }, {
            field: 'frozen',
            align: 'center',
            title: '是否冻结',
            formatter: function (value) {
                if (true === value) {
                    return '<span class="label label-primary">是</span>'
                } else if (false === value) {
                    return '<span class="label label-danger">否</span>'
                } else {
                    return '<span class="label label-danger">-</span>'
                }
            }
        }
        ]);

        $('#btn_add').on('click', function () {
            $grid.add('/view/company/edit/0?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function() {
            $grid.modify($('#dataGrid'), '/view/company/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#btn_delete').on('click', function() {
            $grid.remove($('#dataGrid'), '/rest/company', 'delete');
        });
    });

    var $page = {
        information: {
            show: function (id) {
                var URL = '/rest/goods/' + id;
                var success = function (result) {
                    if (0 === result.code) {
                        var data = result.content;
                        $('#companyTitle').html("装饰公司详情");

                        if (null === data.name) {
                            data.name = '-';
                        }
                        $('#name').html(data.name);

                        if (null === data.code) {
                            data.code = '-';
                        }
                        $('#code').html(data.code);
                        if (null === data.address) {
                            data.address = '-';
                        }
                        $('#address').html(data.address);

                        if (null === data.phone) {
                            data.phone = '-';
                        }
                        $('#phone').html(data.phone);
                        if (null === data.credit) {
                            data.credit = '-';
                        }
                        $('#credit').html(data.credit);
                        if (null === data.promotionMoney) {
                            data.promotionMoney = '-';
                        }
                        $('#promotionMoney').html(data.promotionMoney);
                        if (null === data.walletMoney) {
                            data.walletMoney = '-';
                        }
                        $('#walletMoney').html(data.walletMoney);
                        if (true === data.frozen) {
                            $('#frozen').html('<span class="label label-primary">是</span>');
                        } else if (false === data.frozen) {
                            $('#frozen').html('<span class="label label-danger">否</span>');
                        } else {
                            $('#frozen').html('-');
                        }
                        $('#information').modal();
                    } else {
                        $notify.danger(result.message);
                    }
                };
                $http.GET(URL, null, success);
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }
</script>
</body>