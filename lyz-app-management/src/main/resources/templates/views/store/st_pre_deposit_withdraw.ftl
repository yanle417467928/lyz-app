
<head>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">

    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script type="text/javascript" src="/javascript/preDepositWithdraw/st_deposit_withdraw_page.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script type="text/javascript" src="/javascript/preDepositWithdraw/st_deposit_withdraw_page.js"></script>
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
    <h1>门店预存款提现申请列表</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">

                <div id="toolbar" class="form-inline">
                <#--<button id="btn_edit" type="button" class="btn btn-default">-->
                <#--<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 查看详情-->
                <#--</button>-->
                    <input name="startDateTime" type="text" class="form-control datepicker" id="startDateTime"
                           placeholder="开始时间">
                    至
                    <input name="endDateTime" type="text" class="form-control datepicker" id="endDateTime"
                           placeholder="结束时间">
                    <select name="status" id="status" class="form-control select" style="width:auto;margin-left: 10px;"
                            onchange="findCusByNameOrPhoneOrderNumber()">
                        <option value="">选择状态</option>
                        <option value="CHECKING">待审核</option>
                        <option value="CHECKPASS">审核通过</option>
                        <option value="CHECKRETURN">申请退回</option>
                        <option value="REMITED">已打款</option>
                        <option value="CANCEL">已取消</option>
                    </select>

                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryApplyInfo" id="queryApplyInfo" class="form-control" style="width:auto;"
                               placeholder="关键字：单号、电话、姓名">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findCusByNameOrPhoneOrderNumber()">查找</button>
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
                        <a id="companyTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="resourceDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>申请单号</b> <a class="pull-right" id="applyNo"></a>
                        </li>
                        <li class="list-group-item">
                            <b>申请人</b> <a class="pull-right" id="applyStName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>电话</b> <a class="pull-right" id="applyStPhone"></a>
                        </li>
                        <li class="list-group-item">
                            <b>帐号类型</b> <a class="pull-right" id="accountType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>提现帐号</b> <a class="pull-right" id="account"></a>
                        </li>
                        <li class="list-group-item">
                            <b>提现金额</b> <a class="pull-right" id="withdrawAmount"></a>
                        </li>
                        <li class="list-group-item">
                            <b>申请时间</b> <a class="pull-right" id="createTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>状态</b> <a class="pull-right" id="status"></a>
                        </li>
                        <li class="list-group-item">
                            <b>操作</b> <a class="pull-right" id=""></a>
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


</body>