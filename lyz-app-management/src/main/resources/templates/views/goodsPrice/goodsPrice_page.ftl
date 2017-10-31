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
                <#--<div id="toolbar" class="btn-group">-->
                    <#--<button id="btn_add" type="button" class="btn btn-default">-->
                        <#--<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增-->
                    <#--</button>-->
                    <#--<button id="btn_edit" type="button" class="btn btn-default">-->
                        <#--<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑-->
                    <#--</button>-->
                <#--&lt;#&ndash;<button id="btn_delete" type="button" class="btn btn-default">&ndash;&gt;-->
                <#--&lt;#&ndash;<span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除&ndash;&gt;-->
                <#--&lt;#&ndash;</button>&ndash;&gt;-->
                <#--</div>-->
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
                    <span class="goodsPrice" style="margin-left: 0px;">
                        <a id="companyUserTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="resourceDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>商品名称</b> <a class="pull-right" id="skuName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品编码</b> <a class="pull-right" id="sku"></a>
                        </li>
                        <li class="list-group-item">
                            <b>商品规格</b> <a class="pull-right" id="goodsSpecification"></a>
                        </li>
                        <li class="list-group-item">
                            <b>价目行ID</b> <a class="pull-right" id="priceLineId"></a>
                        </li>
                        <li class="list-group-item">
                            <b>零售价</b> <a class="pull-right" id="retailPrice"></a>
                        </li>
                        <li class="list-group-item">
                            <b>会员价</b> <a class="pull-right" id="VIPPrice"></a>
                        </li>
                        <li class="list-group-item">
                            <b>经销价</b> <a class="pull-right" id="wholesalePrice"></a>
                        </li>
                        <li class="list-group-item">
                            <b>开始时间</b> <a class="pull-right" id="startTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>结束时间</b> <a class="pull-right" id="endTime"></a>
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
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/goodsPrice/page/grid', 'get', true, function(params) {
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
            field: 'skuName',
            title: '商品名称',
            align: 'center'
        },{
            field: 'sku',
            title: '商品编码',
            align: 'center'
        },{
            field: 'goodsSpecification',
            title: '商品规格',
            align: 'center'
        },{
            field: 'priceLineId',
            title: '价目行ID',
            align: 'center'
        },{
            field: 'retailPrice',
            title: '零售价',
            align: 'center'
        },{
            field: 'VIPPrice',
            title: '会员价',
            align: 'center'
        },{
            field: 'wholesalePrice',
            title: '经销价',
            align: 'center'
        },{
            field: 'startTime',
            align: 'center',
            title: '开始时间'
        },{
            field: 'endTime',
            title: '结束时间',
            align: 'center'
        }
        ]);

        <#--$('#btn_add').on('click', function () {-->
            <#--$grid.add('/view/companyUser/edit/0?parentMenuId=${(parentMenuId!'0')}');-->
        <#--});-->

        <#--$('#btn_edit').on('click', function() {-->
            <#--$grid.modify($('#dataGrid'), '/view/companyUser/edit/{id}?parentMenuId=${parentMenuId!'0'}')-->
        <#--});-->

        <#--$('#btn_delete').on('click', function() {-->
            <#--$grid.remove($('#dataGrid'), '/rest/company', 'delete');-->
        <#--});-->
    });

//    var $page = {
//        information: {
//            show: function (id) {
//                var URL = '/rest/companyUser/' + id;
//                var success = function (result) {
//                    if (0 === result.code) {
//                        var data = result.content;
//                        $('#companyUserTitle').html("装饰公司员工详情");
//
//                        if (null === data.userName) {
//                            data.userName = '-';
//                        }
//                        $('#userName').html(data.userName);
//
//                        if (null === data.mobile) {
//                            data.mobile = '-';
//                        }
//                        $('#mobile').html(data.mobile);
//                        if (null === data.companyName) {
//                            data.companyName = '-';
//                        }
//                        $('#companyName').html(data.companyName);
//
//                        if (true === data.isMain) {
//                            $('#isMain').html('<span class="label label-primary">主账号</span>');
//                        } else if (false === data.frozen) {
//                            $('#isMain').html('<span class="label label-danger">子账号</span>');
//                        } else {
//                            $('#isMain').html('-');
//                        }
//                        if (true === data.sex) {
//                            $('#sex').html('<span class="label label-primary">男</span>');
//                        } else if (false === data.frozen) {
//                            $('#sex').html('<span class="label label-danger">女</span>');
//                        } else {
//                            $('#sex').html('-');
//                        }
//                        if (null === data.age) {
//                            data.age = '-';
//                        }
//                        $('#age').html(data.age);
//                        if (true === data.frozen) {
//                            $('#frozen').html('<span class="label label-primary">是</span>');
//                        } else if (false === data.frozen) {
//                            $('#frozen').html('<span class="label label-danger">否</span>');
//                        } else {
//                            $('#frozen').html('-');
//                        }
//                        $('#information').modal();
//                    } else {
//                        $notify.danger(result.message);
//                    }
//                };
//                $http.GET(URL, null, success);
//            },
//            close: function () {
//                $('#information').modal('hide');
//            }
//        }
//    }
</script>
</body>