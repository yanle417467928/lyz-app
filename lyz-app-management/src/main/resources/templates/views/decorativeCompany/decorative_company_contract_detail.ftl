<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <style>
        th {
            text-align: center;
            width: 20%;
            vertical-align: middle !important;
        }

    </style>

</head>
<body onload="window.print();">

<div id="information" class="modal fade" tabindex="-1" role="dialog" id="modalShow">
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
                </div>
            </div>
        </div>
    </div>
</div>

<section class="content">
<div class="cantent">
    <#--<section class="invoice">-->
        <div class="row">
            <div class="col-xs-12">
                <h2 class="page-header">
                    合同管理
                </h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 table-responsive">
                <div class="box">
                    <div class="box-header" style="text-align: center;">
                        <h3>合同详情</h3>
                    </div>
                    <table class="table table-striped table-bordered table-hover">
                        <tr>
                            <th>公司名称</th>
                            <td colspan="2">${decorativeCompanyContract.companyName!""}</td>
                        </tr>
                        <tr>
                            <th>联系电话</th>
                            <td colspan="2">${decorativeCompanyContract.companyPhone!""}</td>
                        </tr>
                        <tr>
                            <th>入场费</th>
                            <td colspan="2">${decorativeCompanyContract.admissionFee!'0.00'} &nbsp;元</td>
                        </tr>
                        <tr>
                            <th>质保金</th>
                            <td colspan="2">${decorativeCompanyContract.qualityAssuranceMoney!'0.00'} &nbsp;元</td>
                        </tr>
                        <tr>
                            <th>合同开始日期</th>
                            <td colspan="2">${decorativeCompanyContract.contractStartDate?string("yyyy-MM-dd")!""}</td>
                        </tr>
                        <tr>
                            <th>合同结束日期</th>
                            <td colspan="2">${decorativeCompanyContract.contractEndDate?string("yyyy-MM-dd")!""}</td>
                        </tr>
                        <tr>
                            <th>业务经理编号</th>
                            <td colspan="2">${decorativeCompanyContract.managerCode!""}</td>
                        </tr>
                        <tr>
                            <th>业务经理姓名</th>
                            <td colspan="2">${decorativeCompanyContract.managerName!""}</td>
                        </tr>
                        <tr>
                            <th>是否开票</th>
                            <td colspan="2"><#if decorativeCompanyContract.isBilling>是<#else >否</#if></td>
                        </tr>
                        <tr>
                            <th rowspan="6" style=" top:50%;" >开票信息</th>
                        </tr>
                        <tr>
                            <td style="width: 10%;"><b>名称</b></td>
                            <td>${decorativeCompanyContract.invoiceName!""}</td>
                        </tr>
                        <tr>
                            <td><b>税号</b></td>
                            <td>${decorativeCompanyContract.invoiceNumber!""}</td>
                        </tr>
                        <tr>
                            <td><b>单位地址</b></td>
                            <td>${decorativeCompanyContract.unitAddress!""}</td>
                        </tr>
                        <tr>
                            <td><b>开户银行</b></td>
                            <td>${decorativeCompanyContract.bank!""}</td>
                        </tr>
                        <tr>
                            <td><b>银行账户</b></td>
                            <td>${decorativeCompanyContract.bankNumber!""}</td>
                        </tr>
                        <tr>
                            <th>账期</th>
                            <#if decorativeCompanyContract.isNowKnot>
                            <td colspan="2">现结</td>
                            <#else >
                            <td colspan="2">非现结（${decorativeCompanyContract.notNowKnotDays}）天</td>
                            </#if>
                        </tr>
                        <tr>
                            <th>对账日期</th>
                            <td colspan="2">${decorativeCompanyContract.checkMoneyDate!""}</td>
                        </tr>
                        <tr>
                            <th>回款日期</th>
                            <td colspan="2">${decorativeCompanyContract.repaymentsDate!""}</td>
                        </tr>

                    </table>
                </div>
            </div>
        </div>

    </section>
</div>
</body>
</html>
