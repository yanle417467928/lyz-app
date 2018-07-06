<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>

    <style>
        b {
            line-height: 30px;
        }

        .span, .fa {
            margin-left: 10px;
        }

        .col-sm-6 {
            padding-left: 30px;
        }

        .th1, .td1 {
            text-align: center;
        }

        .cover {
            z-index: 98;
            width: 100%;
            height: 100%;
            display: none;
            float: left;
            position: absolute;
            top: 0px;
        }
    </style>
</head>
<body onload="window.print();">
<#if photoOrderVO??&&photoOrderVO.photos??>
    <#list photoOrderVO.photos as item>
    <div class="cover" id="big-img${item_index}">

        <img id="big${item_index}" class="big-img" style="margin-top: 20%; height: 40%;width: 80%;"
             onclick="outBig(${item_index})" src="${item!''}"/>
    </div>
    </#list>
</#if>
<div class="wrapper">
    <section class="invoice">
        <div class="row">
            <div class="col-xs-12">
                <h2 class="page-header">
                    拍照下单
                </h2>
            </div>
        </div>
        <div class="tab-pane" id="tab_1-2">
        <#--图片上传功能完善后再优化-->
            <div class="row">
                <div class="col-md-6 col-xs-12">
                    <div class="form-group">
                        <label for="exampleInputFile">轮播图片</label>
                        <input id="rotationImagefile" type="file" name="file" multiple class="file-loading">
                        <p class="help-block">支持jpg、jpeg、png格式，大小不超过2.0M</p>
                    </div>
                </div>
            </div>
            <div class="row" style="height: 150px">
                <div class="col-md-6 col-xs-12">
                    <div class="form-group" id='rotationImageShow'>
                        <input name="rotationImageUri" type="hidden" id="rotationImg" class="form-control">
                        <div class="img-box" id="rotationImageBox">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="box">
            <div class="row invoice-info">
                <div class="box-header">
                    <h3 class="box-title" style="padding-left: 20px;">基本信息</h3>
                </div>
                <div class="col-xs-12">
                    <button type="button" class="btn btn-primary btn-xs"
                            onclick="findCreatePhotoOrderPeople()" style="width:100px;height:30px">
                        选择下单人
                    </button>

                    <button type="button" class="btn btn-primary btn-xs"
                            onclick="findProxyPeople()" style="width:100px;height:30px">
                        选择代下单人
                    </button>
                </div>

                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    &nbsp; 店</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input name="storeName" type="text" class="form-control" id="storeName" value=""
                                           readonly style="width: 200px;"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人姓名</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input name="createPeopleName" type="text" class="form-control"
                                           id="createPeopleName" value="" readonly style="width: 200px;"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人电话</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="createOrderPhone" id="createOrderPhone"
                                           class="form-control" value="" readonly style="width: 200px;"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人身份类型</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="identityType" id="identityType" class="form-control"
                                           value="" readonly style="width: 200px;"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">联系人姓名</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="contactName" id="contactName" class="form-control" value=""
                                           style="width: 200px;"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">联系人电话</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="contactPhone" id="contactPhone" class="form-control"
                                           value="" style="width: 200px;"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">备注</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="remark" id="remark" class="form-control" value=""
                                           style="width: 550px;" maxlength="112"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">代下单人</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="proxyName" id="proxyName" class="form-control" readonly
                                           value="" style="width: 200px;"/>
                                </div>
                                <input type="hidden" id="proxyId" name="proxyId" value="-1">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <form id="form">
                <div class="row">
                    <div class="col-xs-12 table-responsive">

                        <input type="hidden" id="guideName" name="guideName" value="">
                        <input type="hidden" id="source" name="source" value="addPhotoOrder">

                        <button type="button" id="addDeliveryAddressButton" class="btn btn-primary btn-xs"
                                style="width:200px;height:50px;"
                                onclick="addDeliveryAddress()">
                            填写收货地址
                        </button>
                        <button type="button" id="cancelAddDeliveryAddressButton" class="btn btn-danger footer-btn"
                                style="width:200px;height:50px;display:none;"
                                onclick="cancelAddDeliveryAddress()">
                            取消收货地址
                        </button>

                        <button type="button" id="findDeliveryAddressButton" class="btn btn-primary btn-xs"
                                style="width:200px;height:50px;display:none;margin-left: 220px;margin-top: -50px;"
                                onclick="openAddressModal()">
                            从地址库查找
                        </button>

                        <button type="button" id="manuallyEnterDeliveryAddressButton" class="btn btn-primary btn-xs"
                                style="width:200px;height:50px;display:none;margin-left: 220px;margin-top: -50px;"
                                onclick="manuallyEnterAddress()">
                            手动输入地址
                        </button>


                        <input id="goAddDeliveryAddressType" name="goAddDeliveryAddressType" type="hidden" value="1"/>

                        <input id="storeCode" name="storeCode" type="hidden" value=""/>
                        <input id="guideId" name="guideId" type="hidden" value=""/>
                        <input id="peopleIdentityType" name="peopleIdentityType" type="hidden" value=""/>

                        <div id="writeDeliveryAddress">
                            <div class="row">
                                <div class="col-xs-12 col-md-3">
                                    <div class="form-group">
                                        <label>
                                            收货人姓名
                                        </label>
                                        <input type="text" name="receiverName" id="receiverName" class="form-control"
                                               maxlength="10"
                                               \>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                    <div class="form-group">
                                        <label>
                                            收货人电话
                                        </label>
                                        <input type="text" name="receiverPhone" id="receiverPhone" class="form-control"
                                               \>
                                    </div>
                                </div>


                            </div>

                            <div class="row">
                                <div class="col-xs-12 col-md-2">
                                    <div class="form-group">
                                        <label>
                                            省
                                        </label>
                                        <select name="province" id="province" class="form-control select"
                                                onchange="conditionalQueryAreaManagement(this.value,'1')">

                                        </select>
                                        <input type="hidden" name="deliveryId" id="deliveryId" class="form-control" \>
                                    </div>
                                </div>

                                <div class="col-xs-12 col-md-2">
                                    <div class="form-group">
                                        <label>
                                            市
                                        </label>
                                        <select name="city" id="city" class="form-control select"
                                                onchange="conditionalQueryAreaManagement(this.value,'2')">

                                        </select>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-md-2">
                                    <div class="form-group">
                                        <label>
                                            区/县
                                        </label>
                                        <select name="county" id="county" class="form-control select"
                                                onchange="conditionalQueryAreaManagement(this.value,'3')">

                                        </select>
                                    </div>
                                </div>

                                <div class="col-xs-12 col-md-4">
                                    <div class="form-group">
                                        <label>
                                            街道
                                        </label>
                                        <select name="street" id="street" class="form-control select">

                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-xs-12 col-md-3">
                                    <div class="form-group">
                                        <label>
                                            小区名
                                        </label>
                                        <input type="text" name="residenceName" id="residenceName" class="form-control"
                                                maxlength="25" \>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                    <div class="form-group">
                                        <label>
                                            楼盘信息
                                        </label>
                                        <input type="text" name="estateInfo" id="estateInfo" class="form-control" maxlength="25" \>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-md-6">
                                    <div class="form-group">
                                        <label>
                                            详细地址
                                        </label>
                                        <input type="text" name="detailedAddress" id="detailedAddress"
                                               class="form-control" maxlength="100" \>
                                    </div>
                                </div>
                            </div>

                        </div>


                        <div class="box">
                            <div class="box-header">
                                <h3 class="box-title">订单商品</h3>
                            </div>
                            <input type="hidden" name="photoId" id="photoId" <#if photoOrderVO?? && photoOrderVO.id??>
                                   value="${(photoOrderVO.id)?c}"
                            <#else>
                                   value="0"
                            </#if>/>
                            <input type="hidden" id="total" value="0"/>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th width="16%">商品编码</th>
                                    <th width="16%">商品名称</th>
                                    <th width="16%">商品类型</th>
                                    <th width="16%">单价</th>
                                    <th width="16%">数量</th>
                                    <th width="16%">操作</th>
                                </tr>
                                </thead>
                                <tbody id="tbody">


                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-md-8"></div>
                    <div class="col-xs-12 col-md-2">
                        <button type="button" class="btn btn-default footer-btn" onclick="cancel();">
                            <i class="fa fa-close"></i> 作废
                        </button>
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <button type="button" class="btn btn-danger footer-btn btn-cancel">
                            <i class="fa fa-close"></i> 返回
                        </button>
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <button type="submit" class="btn btn-primary footer-btn">
                            <i class="fa fa-check"></i> 提交到料单
                        </button>
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <button type="button" class="btn btn-primary footer-btn" onclick="inspectionStock()">
                            <i class="fa fa-check"></i> 下一步
                        </button>
                    </div>
                </div>
            </form>


            <!-- 库存检核框 -->
            <div id="inspectionStock" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 80%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>库存检核</h4>
                            <button type="button" name="search" class="btn btn-default pull-left"
                                    onclick="returnInspectionStock()" style="margin-left:700px;margin-top: -35px;">返回
                            </button>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="inspectionStockDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-primary">
                                                <div id="addressToolbar" class="form-inline">

                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative">

                                                    </div>
                                                </div>
                                                <div class="box-body table-reponsive">
                                                    <table id="inspectionStockDataGrid"
                                                           class="table table-bordered table-hover">
                                                        <thead>
                                                        <tr>
                                                            <th style="width: 5%;">序号</th>
                                                            <th style="width: 10%;">数量</th>
                                                            <th style="width: 20%;">商品内部编码</th>
                                                            <th style="width: 35%;">商品内部名称</th>
                                                            <th style="width: 10%;">库存</th>
                                                            <th style="width: 10%;">缺货数量</th>
                                                            <th style="width: 10%;">状态</th>
                                                        </tr>
                                                        </thead>

                                                        <tbody id="selectedGoodsTable">

                                                        </tbody>

                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                            <div class="modal-footer">
                                <div id='change'>
                                    <button id="update" class="btn btn-primary btn-xs" style="width: 100px;height: 40px;"
                                            onclick="selectGifts()">
                                        下一步 </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 赠品选择框 -->
            <div id="giftSelectionBox" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 80%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>赠品选择</h4>
                            <button type="button" name="search" class="btn btn-default pull-left"
                                    onclick="returnSelectGift()" style="margin-left:700px;margin-top: -35px;">返回
                            </button>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="giftDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-success">

                                                <!-- /.box-header -->
                                            <#--赠品促销标题-->
                                                <div id="giftMessage">


                                                </div>
                                                <!-- 立减金额 -->
                                                <div class="row" id="subAmount_div">

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                            <div class="modal-footer">
                                <div id='giftButtonDiv'>
                                    <button id="giftButton" class="btn btn-primary btn-xs" style="width: 100px;height: 40px;"
                                            onclick="openOrderDetail()">
                                        下一步 </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 确认订单框 -->
            <div id="orderDetail" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 80%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>确认订单</h4>
                            <button type="button" name="search" class="btn btn-default pull-left"
                                    onclick="returnOrderDetail()" style="margin-left:700px;margin-top: -35px;">返回
                            </button>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="orderDetailDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-success">
                                                <div class="box-header">
                                                    <div class="box-tools pull-right">

                                                    </div>
                                                </div>
                                            </div>
                                            <!-- /.box-header -->
                                        <#--订单商品信息-->
                                            <div id="goodsDetails">
                                                <table id="orderDetailDataGrid"
                                                       class="table table-bordered table-hover">
                                                    <thead>
                                                    <tr>
                                                        <th style="width:8%;">商品id</th>
                                                        <th style="width:20%;">商品编码</th>
                                                        <th style="width:35%;">商品名称</th>
                                                        <th style="width:9%;">数量</th>
                                                        <th style="width:9%;">零售价</th>
                                                        <th style="width:9%;">会员价</th>
                                                        <th style="width:10%;">商品类型</th>
                                                    </tr>
                                                    </thead>

                                                    <tbody id="GoodsListTable">

                                                    </tbody>

                                                </table>

                                            </div>
                                            <!-- 地址信息 -->
                                            <div class="box box-success">
                                                <div class="row" id="deliveryDetail" style="margin-left: 5px;">
                                                    <b>收货人姓名:</b>&nbsp;&nbsp;&nbsp;
                                                    <span id="setReceiverName"></span>
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <b>收货人电话:</b>&nbsp;&nbsp;&nbsp;
                                                    <span id="setReceiverPhone"></span>
                                                    </br>
                                                    <b>收货详细地址:</b>&nbsp;&nbsp;&nbsp;
                                                    <span id="setDetailedAddress"></span>
                                                </div>
                                            </div>


                                            <div class="box box-success">
                                                <div class="row" id="selectDistributionTime" style="margin-left: 5px;">
                                                    <div class="col-xs-12 col-md-4">
                                                        <label for="title">
                                                            配送时间
                                                        </label>
                                                        <select name="distributionTime" id="distributionTime" class="form-control select">
                                                            <option value="-1">选择配送时间</option>
                                                        </select>
                                                        <span id="pointDistributionTime" style="color: red">
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="box box-success">
                                                <div class="input-group col-md-6"
                                                     style="margin-top:0px positon:relative">
                                                    <b><h4>账单信息</h4></b>
                                                    <b>商&nbsp;品&nbsp;金&nbsp;额&nbsp;：</b>
                                                    <span id="totalGoodsAmount"
                                                          name="totalGoodsAmount"></span>
                                                    <br><br>
                                                    <b>冲账户余额：</b>
                                                    <span id="memberDiscount" name="memberDiscount"></span>
                                                    <br><br>
                                                    <b>订&nbsp;单&nbsp;折&nbsp;扣&nbsp;：</b>
                                                    <span id="promotionDiscount"
                                                          name="promotionDiscount"></span>
                                                    <br><br>
                                                    <b>运&nbsp;费&nbsp;金&nbsp;额&nbsp;：</b>
                                                    <span id="freight"
                                                          name="freight"></span>
                                                    <br><br>
                                                    <b>应&nbsp;付&nbsp;金&nbsp;额&nbsp;：</b>
                                                    <span id="amountsPayable"
                                                          name="amountsPayable"></span>
                                                </div>
                                                <div class="input-group col-md-6" id="payMsg"
                                                     style="margin-top:0px positon:relative;">
                                                    <h4>支付信息</h4>
                                                    <b>客户预存款：</b>
                                                    <span id="stPreDeposit" name="stPreDeposit"></span>
                                                    <input id="usePreDeposit" style="float: right" value="0.00" onblur="priceBlur('stPreDeposit')"/>
                                                    <span id="pointUsePreDeposit" style="color: red"></span>
                                                    <br><br><br>
                                                    <b>信&nbsp;&nbsp;&nbsp;&nbsp;用&nbsp;&nbsp;&nbsp;&nbsp;金：</b>
                                                    <span id="stCreditMoney" name="stCreditMoney"></span>
                                                    <input id="useCreditMoney" style="float: right" value="0.00" onblur="priceBlur('stCreditMoney')"/>
                                                    <span id="pointUseCreditMoney" style="color: red"></span>
                                                    <br><br><br>
                                                    <b>现&nbsp;金&nbsp;返&nbsp;利&nbsp;：</b>
                                                    <span id="stSubvention" name="stSubvention"></span>
                                                    <input id="useSubvention" style="float: right" value="0.00" onblur="priceBlur('stSubvention')"/>
                                                    <span id="pointUseSubvention" style="color: red"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                            <div class="modal-footer">
                                <div id='change'>
                                    <button id="update" class="btn btn-primary btn-xs" style="width: 100px;height: 40px;"
                                            onclick="goPay()">
                                        去支付 </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <!-- 地址选择框 -->
            <div id="selectAddressDataGrid" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 60%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>选择地址</h4>
                            <button type="button" name="search" class="btn btn-default pull-left"
                                    onclick="returnAddress()" style="margin-left:700px;margin-top: -35px;">返回
                            </button>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="addressDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-primary">
                                                <div id="addressToolbar" class="form-inline">

                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative">
                                                        <input type="text" name="sellerAddressConditions"
                                                               id="sellerAddressConditions"
                                                               class="form-control" style="width:300px;height:34px;"
                                                               placeholder="请输入收货人姓名、电话、小区、楼盘、详细地址">
                                                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="openAddressModal('/rest/order/photo/find/address')">查找</button>
                        </span>
                                                    </div>
                                                </div>
                                                <div class="box-body table-reponsive">
                                                    <table id="addressDataGrid"
                                                           class="table table-bordered table-hover">

                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <!-- 下单人选择框 -->
            <div id="selectCreateOrderPeopleGrid" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 60%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>选择下单人</h4>
                            <button type="button" name="search" class="btn btn-default pull-left"
                                    onclick="returnPeople()" style="margin-left:700px;margin-top: -35px;">返回
                            </button>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="createOrderPeopleDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-primary">
                                                <div id="peopleToolbar" class="form-inline">

                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative">

                                                        <select name="peopleType" id="peopleType"
                                                                class="form-control selectpicker" onchange="findCompany(this.value)">
                                                            <option value="-1" selected="selected">选择下单人类型</option>
                                                            <option value="会员">会&nbsp;&nbsp;&nbsp;员&nbsp;&nbsp;&nbsp;下&nbsp;&nbsp;&nbsp;单</option>
                                                            <option value="装饰公司">装饰公司下单</option>
                                                        </select>
                                                    </div>
                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative" id="com">
                                                        <select name="decorationCompany" id="decorationCompany"
                                                                class="form-control selectpicker" data-live-search="true" onchange="findCompanyPeople(this.value)">
                                                            <option value="-1" selected="selected">选择装饰公司</option>
                                                        </select>
                                                    </div>


                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative">
                                                        <input type="text" name="selectCreateOrderPeopleConditions"
                                                               id="selectCreateOrderPeopleConditions"
                                                               class="form-control" style="width:auto;"
                                                               placeholder="请输入导购姓名或电话">
                                                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findPeople()">查找</button>
                        </span>
                                                    </div>
                                                </div>
                                                <div class="box-body table-reponsive">
                                                    <table id="createOrderPeopleDataGrid"
                                                           class="table table-bordered table-hover">

                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                </div>
            </div>



            <!-- 代下单人选择框 -->
            <div id="selectProxyCreateOrderPeopleGrid" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document" style="width: 60%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>选择代下单人</h4>
                            <button type="button" name="search" class="btn btn-default pull-left"
                                    onclick="returnProxy()" style="margin-left:700px;margin-top: -35px;">返回
                            </button>
                        </div>
                        <div class="modal-body">
                            <!--  设置这个div的大小，超出部分显示滚动条 -->
                            <div id="proxyCreateOrderPeopleDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                <section class="content">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="box box-primary">
                                                <div id="proxyToolbar" class="form-inline">

                                                    <#--<div class="input-group col-md-3"-->
                                                         <#--style="margin-top:0px positon:relative">-->

                                                        <#--<select name="peopleType" id="peopleType"-->
                                                                <#--class="form-control selectpicker" onchange="findCompany(this.value)">-->
                                                            <#--<option value="-1" selected="selected">选择下单人类型</option>-->
                                                            <#--<option value="会员">会&nbsp;&nbsp;&nbsp;员&nbsp;&nbsp;&nbsp;下&nbsp;&nbsp;&nbsp;单</option>-->
                                                            <#--<option value="装饰公司">装饰公司下单</option>-->
                                                        <#--</select>-->
                                                    <#--</div>-->
                                                    <#--<div class="input-group col-md-3"-->
                                                         <#--style="margin-top:0px positon:relative" id="com">-->
                                                        <#--<select name="decorationCompany" id="decorationCompany"-->
                                                                <#--class="form-control selectpicker" data-live-search="true" onchange="findCompanyPeople(this.value)">-->
                                                            <#--<option value="-1" selected="selected">选择装饰公司</option>-->
                                                        <#--</select>-->
                                                    <#--</div>-->


                                                    <div class="input-group col-md-3"
                                                         style="margin-top:0px positon:relative">
                                                        <input type="text" name="selectProxyCreateOrderPeopleConditions"
                                                               id="selectProxyCreateOrderPeopleConditions"
                                                               class="form-control" style="width:auto;"
                                                               placeholder="请输入导购姓名或电话">
                                                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findProxyPeople()">查找</button>
                        </span>
                                                    </div>
                                                </div>
                                                <div class="box-body table-reponsive">
                                                    <table id="proxyCreateOrderPeopleDataGrid"
                                                           class="table table-bordered table-hover">

                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div class="col-sm-12 invoice-col" style="height: 20px; border-bottom-style: solid;"></div>
            <div class="col-sm-12 invoice-col" style="height: 5px; "></div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box" id="target">
                        <div class="box-header">
                            <h3 class="box-title">选择商品</h3>
                        </div>
                        <div class="col-sm-2 invoice-col"
                             style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                            <b><a id="WATER" name="category1" onclick="findCategory('WATER')">水</a></b>
                        </div>
                        <div class="col-sm-2 invoice-col"
                             style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                            <b><a id="ELECTRIC" name="category1" onclick="findCategory('ELECTRIC')">电</a></b>
                        </div>
                        <div class="col-sm-2 invoice-col"
                             style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                            <b><a id="WOOD" name="category1" onclick="findCategory('WOOD')">木</a></b>
                        </div>
                        <div class="col-sm-2 invoice-col"
                             style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                            <b><a id="TILE" name="category1" onclick="findCategory('TILE')">瓦</a></b>
                        </div>
                        <div class="col-sm-2 invoice-col"
                             style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                            <b><a id="OIL" name="category1" onclick="findCategory('OIL')">油</a></b>
                        </div>
                        <div class="col-sm-2 invoice-col"
                             style="border-bottom-style: solid; border-width: 1px; text-align: center;">
                            <b><a id="category0" name="category1" onclick="findGoodsByCategoryId(0)">专供</a></b>
                        </div>
                    <#--<div class="col-sm-1 invoice-col" style="border-bottom-style: solid; border-width: 1px;">
                        <b>&nbsp; </b>
                    </div>-->
                        <input id="categoryType" name="categoryType" type="hidden" value="WATER"/>
                        <input id="categoryString" name="categoryString" type="hidden" value=""/>
                        <input id="brandString" name="brandString" type="hidden" value=""/>
                        <input id="specificationString" name="specificationString" type="hidden" value=""/>
                        <input id="goodsTypeString" name="goodsTypeString" type="hidden" value=""/>
                        <div class="col-sm-12 invoice-col" style="height: 10px"></div>
                        <div class="col-sm-12 invoice-col">
                            <div class="col-sm-2 invoice-col">
                                <b>全部类型：</b>
                            </div>
                            <div class="col-sm-10 invoice-col" id="category">
                            </div>
                        </div>

                        <div class="col-sm-12 invoice-col">
                            <div class="col-sm-2 invoice-col">
                                <b>品牌：</b>
                            </div>
                            <div class="col-sm-10 invoice-col" id="brand">
                            </div>
                        </div>

                        <div class="col-sm-12 invoice-col">
                            <div class="col-sm-2 invoice-col">
                                <b>规格：</b>
                            </div>
                            <div class="col-sm-10 invoice-col" id="specification">
                            </div>
                        </div>

                        <div class="col-sm-12 invoice-col">
                            <div class="col-sm-2 invoice-col">
                                <b>类型：</b>
                            </div>
                            <div class="col-sm-10 invoice-col" id="goodsType">
                            </div>
                        </div>
                        <div class="col-sm-12 invoice-col" style="height: 10px"></div>
                        <div class="col-sm-12 invoice-col" id="goods">
                        <#--<div class="col-sm-3 invoice-col">-->
                            <#--<img src="http://img1.leyizhuang.com.cn/app/images/goods/2506/20170303114455297.jpg" style="height: 80px;width: 80px;" alt="First slide">-->
                        <#--</div>-->
                        <#--<div class="col-sm-9 invoice-col">-->
                            <#--<div class="col-sm-12 invoice-col">-->
                                <#--<b style="margin-left:-15%; ">全部类型asdsd</b>-->
                            <#--</div>-->
                            <#--<div class="col-sm-12 invoice-col">-->
                                <#--<div class="col-sm-6 invoice-col">-->
                                    <#--<span style="margin-left:-55%; ">规格：奥术大师多</span>-->
                                <#--</div>-->
                                <#--<div class="col-sm-6 invoice-col">-->
                                    <#--<span span style="margin-left:-51%; ">单位：奥</span>-->
                                <#--</div>-->
                            <#--</div>-->
                            <#--<div class="col-sm-12 invoice-col">-->
                                <#--<div class="col-sm-4 invoice-col">-->
                                    <#--<span style="margin-left:-70%; ">￥12.00</span>-->
                                <#--</div>-->
                                <#--<div class="col-sm-8 invoice-col">-->
                                    <#--<a onclick="changeQuantity('delete')">-</a>-->
                                    <#--<input type="text" min="0" id="quantity" value="0" onkeyup="keyup(this)" style="width: 20%; height: 18px; text-align: center;" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)" onchange="quantityChange(0)" />-->
                                    <#--<a onclick="changeQuantity('add')">+</a>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</div>-->
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-3">
                        <#--<button type="button" onclick="addCart();" class="btn btn-primary footer-btn">-->
                        <#--<i class="fa fa-check"></i> 一键添加-->
                        <#--</button>-->
                            <button id="test" class="btn btn-primary footer-btn" onclick="addCart();"
                                    style="position:fixed;right:100;bottom:0;width:100px;height:50px;">一键添加
                            </button>

                            <div class="col-xs-4 col-md-3">
                                <button id="backTop" class="btn btn-primary footer-btn"
                                        style="position:fixed;right:0;bottom:0;width:100px;height:50px;">回到顶部
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                $(function () {
//                $('#WATER').click();
//                findStoreList();
                    findProvince();
                    $("#writeDeliveryAddress").hide();
                    $("#com").hide();
                    initFileInput('rotationImagefile');
                });

                function addDeliveryAddress() {
                    findProvince();
                    $("#writeDeliveryAddress").show();
                    $("#goAddDeliveryAddressType").val(0);
                    var addBtn = document.getElementById("addDeliveryAddressButton");
                    var cancelBtn = document.getElementById("cancelAddDeliveryAddressButton");
                    var findBtn = document.getElementById("findDeliveryAddressButton");
                    cancelBtn.style.display = "block"; //style中的display属性
                    findBtn.style.display = "block"; //style中的display属性
                    addBtn.style.display = "none"; //style中的display属性
                }

                function cancelAddDeliveryAddress() {

                    $('#receiverName').val("").attr("readOnly", false);
                    $('#receiverPhone').val("").attr("readOnly", false);
                    $('#residenceName').val("").attr("readOnly", false);
                    $('#estateInfo').val("").attr("readOnly", false);
                    $('#detailedAddress').val("").attr("readOnly", false);
                    $('#deliveryId').val(-1);

                    jQuery('#province').attr("disabled", false);
                    jQuery('#city').attr("disabled", false);
                    jQuery('#county').attr("disabled", false);
                    jQuery('#street').attr("disabled", false);


                    $("#writeDeliveryAddress").hide();
                    $("#goAddDeliveryAddressType").val(1);
                    $("#deliveryId").val(-1);
                    var addBtn = document.getElementById("addDeliveryAddressButton");
                    var cancelBtn = document.getElementById("cancelAddDeliveryAddressButton");
                    var findBtn = document.getElementById("findDeliveryAddressButton");
                    var manuallyEnterBtn = document.getElementById("manuallyEnterDeliveryAddressButton");
                    cancelBtn.style.display = "none"; //style中的display属性
                    findBtn.style.display = "none"; //style中的display属性
                    addBtn.style.display = "block"; //style中的display属性
                    manuallyEnterBtn.style.display = "none"; //style中的display属性
                }


                //获取城市列表
                function findProvince() {
                    var province = "";
                    var city = "";
                    var county = "";
                    var street = "";
                    $("#province").empty();
                    $("#city").empty();
                    $("#county").empty();
                    $("#street").empty();
                    $.ajax({
                        url: '/rest/admin/fit/place/order/find/areaManagement',
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
                                if (item.level == 2) {
                                    province += "<option value=" + item.code + ">" + item.areaName + "</option>";
                                }
                                if (item.level == 3 && item.parentCode == '510000') {
                                    city += "<option value=" + item.code + ">" + item.areaName + "</option>";
                                }
                                if (item.level == 4 && item.parentCode == '510100') {
                                    county += "<option value=" + item.code + ">" + item.areaName + "</option>";
                                }
                                if (item.level == 5 && item.parentCode == '510104') {
                                    street += "<option value=" + item.areaName + ">" + item.areaName + "</option>";
                                }
                            })
                            $("#province").append(province);
                            $("#city").append(city);
                            $("#county").append(county);
                            $("#street").append(street);
                        }
                    });
                }


                function conditionalQueryAreaManagement(value, mag) {
                    if (1 == mag) {
                        var city = "";
                        var county = "";
                        var street = "";
                        $.ajax({
                            url: '/rest/admin/fit/place/order/find/areaManagement/1/' + value,
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
                                    if (item.level == 3) {
                                        city += "<option value=" + item.code + ">" + item.areaName + "</option>";
                                    }
                                    if (item.level == 4) {
                                        county += "<option value=" + item.code + ">" + item.areaName + "</option>";
                                    }
                                    if (item.level == 5) {
                                        street += "<option value=" + item.areaName + ">" + item.areaName + "</option>";
                                    }
                                })
                                document.getElementById('city').innerHTML = "";
                                document.getElementById('county').innerHTML = "";
                                document.getElementById('street').innerHTML = "";
                                $("#city").append(city);
                                $("#county").append(county);
                                $("#street").append(street);
                                $('#city').selectpicker('refresh');
                                $('#city').selectpicker('render');
                                $('#county').selectpicker('refresh');
                                $('#county').selectpicker('render');
                                $('#street').selectpicker('refresh');
                                $('#street').selectpicker('render');
                            }
                        });
                    } else if (2 == mag) {
                        var county = "";
                        var street = "";
                        $.ajax({
                            url: '/rest/admin/fit/place/order/find/areaManagement/2/' + value,
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
                                    if (item.level == 4) {
                                        county += "<option value=" + item.code + ">" + item.areaName + "</option>";
                                    }
                                    if (item.level == 5) {
                                        street += "<option value=" + item.areaName + ">" + item.areaName + "</option>";
                                    }
                                })
                                document.getElementById('county').innerHTML = "";
                                document.getElementById('street').innerHTML = "";
                                $("#county").append(county);
                                $("#street").append(street);
                                $('#county').selectpicker('refresh');
                                $('#county').selectpicker('render');
                                $('#street').selectpicker('refresh');
                                $('#street').selectpicker('render');
                            }
                        });
                    } else if (3 == mag) {
                        var street = "";
                        $.ajax({
                            url: '/rest/admin/fit/place/order/find/areaManagement/2/' + value,
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
                                    if (item.level == 5) {
                                        street += "<option value=" + item.areaName + ">" + item.areaName + "</option>";
                                    }
                                })
                                document.getElementById('street').innerHTML = "";
                                $("#street").append(street);
                                $('#street').selectpicker('refresh');
                                $('#street').selectpicker('render');
                            }
                        });
                    }
                }

                backTop.onclick = function () {
                    target.scrollIntoView();
                }

                function findCategory(categoryCode) {

                    var userMobile = $('#createOrderPhone').val();
                    var identityType = $('#identityType').val();
                    if (null == userMobile || '' == userMobile || null == identityType || '' == identityType) {
                        $notify.warning("请先选择下单人！");
                        return false;
                    }

                    document.getElementById("categoryType").value = categoryCode;
                    document.getElementById("categoryString").value = '';
                    document.getElementById("brandString").value = '';
                    document.getElementById("specificationString").value = '';
                    document.getElementById("goodsTypeString").value = '';
                    var guideId = $('#guideId').val();

                    var category = '';
                    var goods = '';
                    var brand = '';
                    var specification = '';
                    var goodsType = '';
                    var photoId = $('#photoId').val();
                    $("[name='category1']").css('color', '#72afd2');
                    $('#' + categoryCode).css('color', 'red');
                    $.ajax({
                        url: '/rest/order/photo/findCategory/goods',
                        method: 'GET',
                        data: {
                            categoryCode: categoryCode,
                            identityType: identityType,
                            guideId: guideId
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $.each(result.content.goodsCategory, function (i, item) {
                                category += '<div class="col-sm-3 invoice-col"><a id="category' + item.cid + '" name="category2" onclick="findGoodsByCategoryId(' + item.cid + ')">' + item.categoryName + '</a> </div>';
//                            category += '<input id="categoryString" name="categoryString" type="hidden" value="'+item[0].cid+'"/>';
                            });

                            $("#category").html(category);

                            $.each(result.content.brandList, function (i, item) {
                                brand += '<div class="col-sm-3 invoice-col"><a id="brand' + item.brandId + '" name="brand" onclick="findGoodsByBrandId(' + item.brandId + ')">' + item.brandName + '</a> </div>';
                            });
                            $("#brand").html(brand);

                            $.each(result.content.specificationList, function (i, item) {
                                specification += '<div class="col-sm-3 invoice-col"><a id="specification' + item.specificationName + '" name="specification" onclick="findGoodsBySpecification(';
                                specification += "'" + item.specificationName;
                                specification += "'";
                                specification += ')">' + item.specificationName + '</a> </div>';
                            });
                            $("#specification").html(specification);

                            $.each(result.content.goodsTypeList, function (i, item) {
                                goodsType += '<div class="col-sm-3 invoice-col"><a id="goodsType' + item.typeId + '" name="goodsType" onclick="findGoodsBytypeId(' + item.typeId + ')">' + item.typeName + '</a> </div>';
                            });
                            $("#goodsType").html(goodsType);


                            $.each(result.content.goods, function (i, item) {
                                goods += '<div class="col-sm-12 invoice-col">';
                                goods += '<div class="col-sm-3 invoice-col"><img src="' + item.coverImageUri + '" style="height: 80px;width: 80px;" alt="First slide"></div>';
                                goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">' + item.goodsName + '</b></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：' + item.goodsSpecification + '</span></div>';
                                goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：' + item.goodsUnit + '</span></div></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥' + item.retailPrice + '</span></div>';
                                goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'delete'";
                                goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                                goods += ' <input type="text" class="goodsSelectedQuantity" min="0" id="quantity' + item.id + '" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)"/>';
                                goods += '<a onclick="changeQuantity(' + item.id + ',';
                                goods += "'add'";
                                goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                                goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>';
                                goods += '<input type="hidden" id="sku' + item.id + '" value="' + item.sku + '"/>';
                                goods += '<input type="hidden" id="goodsName' + item.id + '" value="' + item.goodsName + '"/>';
                                goods += '<input type="hidden" id="typeName' + item.id + '" value="' + item.typeName + '"/>';
                                goods += '<input type="hidden" id="price' + item.id + '" value="' + item.retailPrice + '"/>';
                            });
                            $("#goods").html(goods);
                        }
                    });
                }

                function findGoodsByCategoryId(categoryId) {
                    document.getElementById("categoryString").value = categoryId;
                    var identityType = $('#identityType').val();
                    var categoryType = $('#categoryType').val();
//                var categoryString = $('#categoryString').val();
                    var brandString = $('#brandString').val();
                    var specificationString = $('#specificationString').val();
                    var goodsTypeString = $('#goodsTypeString').val();
                    var guideId = $('#guideId').val();

                    var goods = '';
                    if (categoryId == 0) {
                        $("#category").html('');
                        $("#brand").html('');
                        $("#specification").html('');
                        $("#goodsType").html('');
                        $("[name='category1']").css('color', '#72afd2');
                    }
                    $("[name='category2']").css('color', '#72afd2');
                    $('#category' + categoryId).css('color', 'red');
                    $.ajax({
                        url: '/rest/order/photo/findGoods/guideId',
                        method: 'GET',
                        data: {
                            categoryId: categoryId,
                            guideId: guideId,
                            categoryType: categoryType,
                            brandString: brandString,
                            specificationString: specificationString,
                            goodsTypeString: goodsTypeString,
                            identityType: identityType
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $.each(result.content, function (i, item) {
                                goods += '<div class="col-sm-12 invoice-col">';
                                goods += '<div class="col-sm-3 invoice-col"><img src="' + item.coverImageUri + '" style="height: 80px;width: 80px;" alt="First slide"></div>';
                                goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">' + item.goodsName + '</b></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：' + item.goodsSpecification + '</span></div>';
                                goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：' + item.goodsUnit + '</span></div></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥' + item.retailPrice + '</span></div>';
                                goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'delete'";
                                goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                                goods += '<input type="text" class="goodsSelectedQuantity" min="0" id="quantity' + item.id + '" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)" />';
                                goods += '<span>&nbsp;</span><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'add'";
                                goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                                goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>'
                                goods += '<input type="hidden" id="sku' + item.id + '" value="' + item.sku + '"/>';
                                goods += '<input type="hidden" id="goodsName' + item.id + '" value="' + item.goodsName + '"/>';
                                goods += '<input type="hidden" id="typeName' + item.id + '" value="' + item.typeName + '"/>';
                                goods += '<input type="hidden" id="price' + item.id + '" value="' + item.retailPrice + '"/>';
                            });
                            $("#goods").html(goods);
                        }
                    });
                }


                function findGoodsByBrandId(brandId) {
                    document.getElementById("brandString").value = brandId;
                    var categoryType = $('#categoryType').val();
                    var categoryId = $('#categoryString').val();
                    var specificationString = $('#specificationString').val();
                    var goodsTypeString = $('#goodsTypeString').val();
                    var identityType = $('#identityType').val();
                    var guideId = $('#guideId').val();
                    var goods = '';
                    if (categoryId == 0) {
                        $("[name='category1']").css('color', '#72afd2');
                    }
                    $("[name='brand']").css('color', '#72afd2');
                    $('#brand' + brandId).css('color', 'red');
                    $.ajax({
                        url: '/rest/order/photo/findGoods/guideId',
                        method: 'GET',
                        data: {
                            categoryId: categoryId,
                            guideId: guideId,
                            categoryType: categoryType,
                            brandString: brandId,
                            specificationString: specificationString,
                            goodsTypeString: goodsTypeString,
                            identityType: identityType
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $.each(result.content, function (i, item) {
                                goods += '<div class="col-sm-12 invoice-col">';
                                goods += '<div class="col-sm-3 invoice-col"><img src="' + item.coverImageUri + '" style="height: 80px;width: 80px;" alt="First slide"></div>';
                                goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">' + item.goodsName + '</b></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：' + item.goodsSpecification + '</span></div>';
                                goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：' + item.goodsUnit + '</span></div></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥' + item.retailPrice + '</span></div>';
                                goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'delete'";
                                goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                                goods += '<input type="text" class="goodsSelectedQuantity" min="0" id="quantity' + item.id + '" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)" />';
                                goods += '<span>&nbsp;</span><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'add'";
                                goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                                goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>'
                                goods += '<input type="hidden" id="sku' + item.id + '" value="' + item.sku + '"/>';
                                goods += '<input type="hidden" id="goodsName' + item.id + '" value="' + item.goodsName + '"/>';
                                goods += '<input type="hidden" id="typeName' + item.id + '" value="' + item.typeName + '"/>';
                                goods += '<input type="hidden" id="price' + item.id + '" value="' + item.retailPrice + '"/>';
                            });
                            $("#goods").html(goods);
                        }
                    });
                }


                function findGoodsBySpecification(specificationString) {
                    document.getElementById("specificationString").value = specificationString;
                    var categoryType = $('#categoryType').val();
                    var categoryId = $('#categoryString').val();
                    var brandString = $('#brandString').val();
//                var specificationString = $('#specificationString').val();
                    var goodsTypeString = $('#goodsTypeString').val();
                    var identityType = $('#identityType').val();
                    var guideId = $('#guideId').val();
                    var goods = '';
                    if (categoryId == 0) {
                        $("[name='category1']").css('color', '#72afd2');
                    }
                    $("[name='specification']").css('color', '#72afd2');
                    $('#specification' + specificationString).css('color', 'red');
                    $.ajax({
                        url: '/rest/order/photo/findGoods/guideId',
                        method: 'GET',
                        data: {
                            categoryId: categoryId,
                            guideId: guideId,
                            categoryType: categoryType,
                            brandString: brandString,
                            specificationString: specificationString,
                            goodsTypeString: goodsTypeString,
                            identityType: identityType
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $.each(result.content, function (i, item) {
                                goods += '<div class="col-sm-12 invoice-col">';
                                goods += '<div class="col-sm-3 invoice-col"><img src="' + item.coverImageUri + '" style="height: 80px;width: 80px;" alt="First slide"></div>';
                                goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">' + item.goodsName + '</b></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：' + item.goodsSpecification + '</span></div>';
                                goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：' + item.goodsUnit + '</span></div></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥' + item.retailPrice + '</span></div>';
                                goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'delete'";
                                goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                                goods += '<input type="text" class="goodsSelectedQuantity" min="0" id="quantity' + item.id + '" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)" />';
                                goods += '<span>&nbsp;</span><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'add'";
                                goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                                goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>'
                                goods += '<input type="hidden" id="sku' + item.id + '" value="' + item.sku + '"/>';
                                goods += '<input type="hidden" id="goodsName' + item.id + '" value="' + item.goodsName + '"/>';
                                goods += '<input type="hidden" id="typeName' + item.id + '" value="' + item.typeName + '"/>';
                                goods += '<input type="hidden" id="price' + item.id + '" value="' + item.retailPrice + '"/>';
                            });
                            $("#goods").html(goods);
                        }
                    });
                }


                function findGoodsBytypeId(typeId) {
                    document.getElementById("goodsTypeString").value = typeId;
                    var categoryType = $('#categoryType').val();
                    var categoryId = $('#categoryString').val();
                    var brandString = $('#brandString').val();
                    var specificationString = $('#specificationString').val();
//                var goodsTypeString = $('#goodsTypeString').val();
                    var identityType = $('#identityType').val();
                    var guideId = $('#guideId').val();
                    var goods = '';
                    if (categoryId == 0) {
                        $("[name='category1']").css('color', '#72afd2');
                    }
                    $("[name='goodsType']").css('color', '#72afd2');
                    $('#goodsType' + typeId).css('color', 'red');
                    $.ajax({
                        url: '/rest/order/photo/findGoods/guideId',
                        method: 'GET',
                        data: {
                            categoryId: categoryId,
                            guideId: guideId,
                            categoryType: categoryType,
                            brandString: brandString,
                            specificationString: specificationString,
                            goodsTypeString: typeId,
                            identityType: identityType
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $.each(result.content, function (i, item) {
                                goods += '<div class="col-sm-12 invoice-col">';
                                goods += '<div class="col-sm-3 invoice-col"><img src="' + item.coverImageUri + '" style="height: 80px;width: 80px;" alt="First slide"></div>';
                                goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">' + item.goodsName + '</b></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：' + item.goodsSpecification + '</span></div>';
                                goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：' + item.goodsUnit + '</span></div></div>';
                                goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥' + item.retailPrice + '</span></div>';
                                goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'delete'";
                                goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                                goods += '<input type="text" class="goodsSelectedQuantity" min="0" id="quantity' + item.id + '" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)" />';
                                goods += '<span>&nbsp;</span><a onclick="changeQuantity(' + item.id + ',';
                                goods += "'add'";
                                goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                                goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>'
                                goods += '<input type="hidden" id="sku' + item.id + '" value="' + item.sku + '"/>';
                                goods += '<input type="hidden" id="goodsName' + item.id + '" value="' + item.goodsName + '"/>';
                                goods += '<input type="hidden" id="typeName' + item.id + '" value="' + item.typeName + '"/>';
                                goods += '<input type="hidden" id="price' + item.id + '" value="' + item.retailPrice + '"/>';
                            });
                            $("#goods").html(goods);
                        }
                    });
                }


                // 改变商品数量的方法
                function changeQuantity(goodsId, operation) {
                    // 获取指定商品显示数量的输入框的id
                    var quantityElementId = "#quantity" + goodsId;
                    // 获取当前指定商品选择的数量
                    var quantity = $(quantityElementId).val();
                    // 获取商品的库存量的标签的id
                    // 如果是减少当前商品的数量
                    if ("delete" == operation) {
                        // 如果当前商品的数量已经是0了就不做任何处理
                        if (0 == quantity) {
                            $notify.info("亲，不能再少啦");
                            return;
                        }
                        // 正常减少数量
                        quantity = parseInt(quantity) - 1;
                    }

                    // 如果是增加商品数量的情况
                    if ("add" == operation) {
                        // 正常增加数量
                        quantity = parseInt(quantity) + 1;
                    }
                    // 把更新后的商品信息和已选数量显示到页面上
                    $(quantityElementId).val(quantity);
                }
                //限制输入 只能输入数字
                function keyup(obj) {
                    if (obj.value.length == 1) {
                        obj.value = obj.value.replace(/[^1-9]/g, '')
                    } else {
                        obj.value = obj.value.replace(/\D/g, '')
                    }
                    ;
                }
                //限制输入 只能输入数字
                function afterpaste(obj) {
                    if (obj.value.length == 1) {
                        obj.value = obj.value.replace(/[^1-9]/g, '')
                    } else {
                        obj.value = obj.value.replace(/\D/g, '')
                    }
                    ;
                }

                //商品数量输入框获取焦点时清空
                function clearQuantity(obj) {
                    obj.value = "";
                }

                //商品数量如果为空，则设为0
                function setQuantity(obj) {
                    if (obj.value.length == 0) {
                        obj.value = obj.min;
                    }
                }
                function addCart(isGoHistory) {
                    var params = "";
                    var flag = "";
                    var total = $('#total').val();
                    // 获取所有value值大于0的input标签（即获得了所有数量要大于0的商品）
                    $('.goodsSelectedQuantity').each(
                            // 获取标签之后拼接参数变量
                            function (i) {
                                var qty = $('.goodsSelectedQuantity').eq(i).val();
                                if (!isNaN(qty) && qty > 0) {
                                    var goodsId = $('.goodsSelectedQuantity').eq(i).attr("id").replace("quantity", "");
                                    var sku = $('#sku' + goodsId).val();
                                    var goodsName = $('#goodsName' + goodsId).val();
                                    var typeName = $('#typeName' + goodsId).val();
                                    var price = $('#price' + goodsId).val();

                                    var oldGoodsId = $('#gid' + goodsId).val();
                                    if (undefined == oldGoodsId || oldGoodsId == '') {
                                        params += '<tr><td><input type="hidden" id="gid' + goodsId + '" name="combList[' + total + '].gid" value="' + goodsId + '" />' + sku + '</td>';
                                        params += '<td>' + goodsName + '</td><td>' + typeName + '</td><td>' + price + '</td>';
                                        params += '<td ><input type="text" id="qty' + goodsId + '" min="1" name="combList[' + total + '].qty" value="' + qty + '" style="width:30%;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onblur="setQuantity(this)"/></td>';
                                        params += '<td><a title="删除" class="img-btn del operator" onclick="del_goods_comb(this);">删除</a></td></tr>';
                                        total = parseInt(total) + 1;
                                    } else {
                                        var oldQty = $('#qty' + goodsId).val();
                                        if (!isNaN(oldQty) && oldQty > 0) {
                                            qty = parseInt(oldQty) + parseInt(qty);
                                            $('#qty' + goodsId).val(qty);
                                        } else {
                                            $('#qty' + goodsId).val(qty);
                                        }
                                    }
                                    flag += '1';
                                }
                            });
                    if ("" == flag) {
                        $notify.info("亲，请先选择商品的数量");
                        return;
                    }
                    $('#total').val(total);
                    $('.goodsSelectedQuantity').val(0);
                    $("#tbody").append(params);
                }

                //删除商品组合节点
                function del_goods_comb(obj) {
                    $(obj).parent().parent().remove();
                }
                //看大图
                function showBig(obj) {
                    $("#big-img" + obj).fadeIn(500);
                }
                //关闭大图
                function outBig(obj) {
                    $("#big-img" + obj).fadeOut(500);
                }

                $(function () {
                    $('.btn-cancel').on('click', function () {
                        history.go(-1);
                    });
                    $('#form').bootstrapValidator({
                        framework: 'bootstrap',
                        feedbackIcons: {
                            valid: 'glyphicon glyphicon-ok',
                            invalid: 'glyphicon glyphicon-remove',
                            validating: 'glyphicon glyphicon-refresh'
                        },
                        verbose: false
                    }).on('success.form.bv', function (e) {
                        var residenceName = $("#residenceName").val();
                        var estateInfo = $("#estateInfo").val();
                        var detailedAddress = $("#detailedAddress").val();


                        var estateInfoLength = getInputLength(estateInfo);
                        var residenceNameLength = getInputLength(residenceName);
                        var detailedAddressLength = getInputLength(detailedAddress);
                        var remark = $("#remark").val();
                        var remarkLength = getInputLength(remark);
                        var guideId = $("#guideId").val();
                        var contactName = $("#contactName").val();
                        var contactPhone = $("#contactPhone").val();
                        var peopleIdentityType = $("#peopleIdentityType").val();
                        var rotationImg = $("#rotationImg").val();
                        var source = $("#source").val();

                        if (estateInfoLength > 50) {
                            $notify.danger('楼盘名称长度超长，请重新输入！');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                            return false;
                        }
                        if (detailedAddressLength > 200) {
                            $notify.danger('详细地址名称长度超长，请重新输入！');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                            return false;
                        }
                        if (remarkLength > 225) {
                            $notify.danger('备注长度超长，请重新输入！');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                            return false;
                        }
                        if (residenceNameLength > 50) {
                            $notify.danger('小区名长度超长，请重新输入！');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                            return false;
                        }

                        var proxyId = $('#proxyId').val();
                        e.preventDefault();
                        var $form = $(e.target);
                        var origin = $form.serializeArray();
                        var data = {};
                        var formData = new FormData($("#form")[0]);
                        formData.append("remark", remark);
                        formData.append("contactName", contactName);
                        formData.append("contactPhone", contactPhone);
                        formData.append("photoImgs", rotationImg);
                        formData.append("proxyId", proxyId);
                        formData.append("source", source);
                        if (null === $global.timer) {
                            $global.timer = setTimeout($loading.show, 2000);
                            var url = '/rest/order/photo/save/new';
                            $.ajax({
                                url: url,
                                method: 'POST',
                                data: formData,
                                async: false,
                                cache: false,
                                contentType: false,
                                processData: false,
                                error: function () {
                                    clearTimeout($global.timer);
                                    $loading.close();
                                    $global.timer = null;
                                    $notify.danger('网络异常，请稍后重试或联系管理员');
                                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                                },
                                success: function (result) {
                                    if (0 === result.code) {
                                        window.location.href = document.referrer;
                                    } else {
                                        clearTimeout($global.timer);
                                        $loading.close();
                                        $global.timer = null;
                                        $notify.danger(result.message);
                                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                                    }
                                }
                            });
                        }
                    });
                });

                function cancel() {
                    var photoId = $('#photoId').val();
                    $.ajax({
                        url: '/rest/order/photo/delete',
                        method: 'POST',
                        data: {
                            photoId: photoId
                        },
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                            }
                        }
                    });
                }


                function getInputLength(str) {
                    return str.replace(/[\u0391-\uFFE5]/g, "aa").length;
                }

                function findDeliveryAddress() {
                    var userMobile = $('#createOrderPhone').val();
                    var identityType = $('#identityType').val();
                    var sellerAddressConditions = $("#sellerAddressConditions").val();
                    var guideId = $("#guideId").val();
                    var url = '/rest/order/photo/find/address';
                    $("#addressDataGrid").bootstrapTable('destroy');
                    $grid.init($('#addressDataGrid'), $('#addressToolbar'), url, 'get', false, function (params) {
                        return {
                            offset: params.offset,
                            size: params.limit,
                            keywords: params.search,
                            userMobile: userMobile,
                            identityType: identityType,
                            guideId: guideId,
                            sellerAddressConditions: sellerAddressConditions
                        }
                    }, [{
                        field: 'id',
                        title: 'ID',
                        align: 'center',
                        visible: false
                    }, {
                        field: 'deliveryName',
                        title: '收货人姓名',
                        align: 'center',
                        events: {
                            'click .scan': function (e, value, row) {
                                filling(row);
                            }
                        },
                        formatter: function (value) {
                            return '<a class="scan" href="#' + value + '">' + value + '</a>';
                        }
                    }, {
                        field: 'deliveryPhone',
                        title: '收货人号码',
                        align: 'center'
//
//                    events: {
//                        'click .scan': function (e, value, row) {
//                            showSeller(row.empId, value, row.mobile, row.storeType, row.storeCode, row.balance);
//                        }
//                    },
//                    formatter: function (value) {
//                        if (null == value) {
//                            return '<a class="scan" href="#">' + '未知' + '</a>';
//                        } else {
//                            return '<a class="scan" href="#' + value + '">' + value + '</a>';
//                        }
//                    }
                    }, {
                        field: 'deliveryProvince',
                        title: '省份',
                        align: 'center'
                    }, {
                        field: 'deliveryCity',
                        title: '城市',
                        align: 'center'
                    }, {
                        field: 'deliveryCounty',
                        title: '区/县',
                        align: 'center'
//                    visible: false
                    }, {
                        field: 'deliveryStreet',
                        title: '街道',
                        align: 'center'
                    }, {
                        field: 'villageName',
                        title: '小区',
                        align: 'center'
                    }, {
                        field: 'detailedAddress',
                        title: '详细地址',
                        align: 'center'
                    }, {
                        field: 'estateInfo',
                        title: '楼盘信息',
                        align: 'center'
                    }
                    ]);
                }

                function openAddressModal() {
                    var userMobile = $('#createOrderPhone').val();
                    var identityType = $('#identityType').val();
                    if (null == userMobile || '' == userMobile || null == identityType || '' == identityType) {
                        $notify.warning("请先选择下单人！");
                        return false;
                    }
                    //查询地址列表
                    findDeliveryAddress();
                    $("#customerModalConfirm").unbind('click').click(function () {
                    });
                    $('#selectAddressDataGrid').modal('show');
                }

                function filling(row) {
                    $('#receiverName').val(row.deliveryName).attr("readOnly", "true");
                    $('#receiverPhone').val(row.deliveryPhone).attr("readOnly", "true");
                    $('#residenceName').val(row.villageName).attr("readOnly", "true");
                    $('#estateInfo').val(row.estateInfo).attr("readOnly", "true");
                    $('#detailedAddress').val(row.detailedAddress).attr("readOnly", "true");

                    $('#deliveryId').val(row.id);


                    jQuery('#province').append("<option value='" + row.deliveryProvince + "' selected='selected'>" + row.deliveryProvince + "</option>").attr("disabled", true);
                    jQuery('#city').append("<option value='" + row.deliveryCity + "' selected='selected'>" + row.deliveryCity + "</option>").attr("disabled", true);
                    jQuery('#county').append("<option value='" + row.deliveryCounty + "' selected='selected'>" + row.deliveryCounty + "</option>").attr("disabled", true);
                    jQuery('#street').append("<option value='" + row.deliveryStreet + "' selected='selected'>" + row.deliveryStreet + "</option>").attr("disabled", true);

                    var findBtn = document.getElementById("findDeliveryAddressButton");
                    var manuallyEnterBtn = document.getElementById("manuallyEnterDeliveryAddressButton");
                    findBtn.style.display = "none"; //style中的display属性
                    manuallyEnterBtn.style.display = "block"; //style中的display属性

                    $('#selectAddressDataGrid').modal('hide');
                }

                function manuallyEnterAddress() {
                    $('#receiverName').val("").attr("readOnly", false);
                    $('#receiverPhone').val("").attr("readOnly", false);
                    $('#residenceName').val("").attr("readOnly", false);
                    $('#estateInfo').val("").attr("readOnly", false);
                    $('#detailedAddress').val("").attr("readOnly", false);
                    $('#deliveryId').val(-1);

                    jQuery('#province').attr("disabled", false);
                    jQuery('#city').attr("disabled", false);
                    jQuery('#county').attr("disabled", false);
                    jQuery('#street').attr("disabled", false);

                    findProvince();
                    $("#deliveryId").val(-1);
                    var findBtn = document.getElementById("findDeliveryAddressButton");
                    var manuallyEnterBtn = document.getElementById("manuallyEnterDeliveryAddressButton");
                    findBtn.style.display = "block"; //style中的display属性
                    manuallyEnterBtn.style.display = "none"; //style中的display属性
                }

                function findCreatePhotoOrderPeople() {
                    cancelAddDeliveryAddress();
                    $('#selectCreateOrderPeopleGrid').modal('show');
                }

                function findPeople() {
                    var peopleType = $('#peopleType').val();
                    var selectCreateOrderPeopleConditions = $('#selectCreateOrderPeopleConditions').val();
                    if (-1 == peopleType || null == peopleType || '' == peopleType) {
                        $notify.warning("请选择下单人类型！");
                        return false;
                    }
                    var url = '/rest/order/photo/find/people';
                    $("#createOrderPeopleDataGrid").bootstrapTable('destroy');
                    $grid.init($('#createOrderPeopleDataGrid'), $('#peopleToolbar'), url, 'get', false, function (params) {
                        return {
                            offset: params.offset,
                            size: params.limit,
                            keywords: params.search,
                            peopleType: peopleType,
                            selectCreateOrderPeopleConditions: selectCreateOrderPeopleConditions
                        }
                    }, [{
                        field: 'peopleId',
                        title: 'ID',
                        align: 'center'
//                    visible:false
                    }, {
                        field: 'name',
                        title: '下单人姓名',
                        align: 'center',
                        events: {
                            'click .scan': function (e, value, row) {
                                fillingPeople(row);
                            }
                        },
                        formatter: function (value) {
                            return '<a class="scan" href="#' + value + '">' + value + '</a>';
                        }
                    }, {
                        field: 'phone',
                        title: '下单人电话',
                        align: 'center'
                    }, {
                        field: 'identityType',
                        title: '身份类型',
                        align: 'center'
                    }, {
                        field: 'storeName',
                        title: '下单人归属门店名',
                        align: 'center'
                    }, {
                        field: 'storeCode',
                        title: '下单人归属门店code',
                        align: 'center',
                        visible: false
                    }
                    ]);
                }

                function fillingPeople(row) {
                    $("#storeName").val(row.storeName);
                    if ('CUSTOMER' == row.identityType) {
                        $("#peopleIdentityType").val("顾客");
                        $("#identityType").val("顾客");
                    } else if('DECORATE_EMPLOYEE' == row.identityType){
                        $("#peopleIdentityType").val("装饰公司员工");
                        $("#identityType").val("装饰公司员工");
                    }else{
                        $("#peopleIdentityType").val("装饰公司经理");
                        $("#identityType").val("装饰公司经理");
                    }
//                document.getElementById("photoNo").va;
//                document.getElementById("contactName").innerText = row.name;
//                document.getElementById("createPeopleName").innerText = row.name;
//                document.getElementById("createPeopleTime").innerText = "";
//                document.getElementById("createOrderPhone").innerText = row.phone;
//                document.getElementById("contactPhone").innerText = row.phone;
//                document.getElementById("status").innerText = "";
                    $("#contactName").val(row.name);
                    $("#createPeopleName").val(row.name);
                    $("#createOrderPhone").val(row.phone);
                    $("#contactPhone").val(row.phone);
                    $("#storeCode").val(row.storeCode);
                    $("#guideId").val(row.peopleId);

                    $('#selectCreateOrderPeopleGrid').modal('hide');
                    findCategory("WATER");
                }

                function initFileInput(ctrlName) {
                    var control = $('#' + ctrlName);
                    control.fileinput({
                        language: 'zh', //设置语言
                        uploadUrl: "/rest/goods/photo", //上传的地址
                        allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
                        //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
                        uploadAsync: true, //默认异步上传
                        showUpload: false, //是否显示上传按钮
                        showRemove: true, //显示移除按钮
                        showPreview: false, //是否显示预览
                        showCaption: true,//是否显示标题
                        browseClass: "btn btn-primary", //按钮样式
                        dropZoneEnabled: false,//是否显示拖拽区域
                        //minImageWidth: 50, //图片的最小宽度
                        //minImageHeight: 50,//图片的最小高度
                        //maxImageWidth: 1000,//图片的最大宽度
                        //maxImageHeight: 1000,//图片的最大高度
                        maxFileSize:2048,//单位为kb，如果为0表示不限制文件大小
                        maxFileCount: 1,//表示允许同时上传的最大文件个数
                        enctype: 'multipart/form-data',
                        validateInitialCount: true,
                        previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
                        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！"
                    }).on('filebatchselected', function (event, data, id, index) {
                        $(this).fileinput("upload");
                    }).on("fileuploaded", function (event, data) {
                        if (data.response.code == 0) {
                            $("#rotationImageBox").append('<div class="col-md-3 text-center"  ><img  src="' + data.response.content + '"' + ' class="img-rounded" style="width: 100px;height: 50px;"/><div style="margin-top: 5px;"><button class="btn btn-default" onclick="delImg(this)">删除</button></div></div>');
                            getUrl("rotationImageBox", '#rotationImg');
                        }
                    });
                }


                function delImg(file) {
                    file.parentNode.parentNode.remove();
                    getUrl("rotationImageBox", '#rotationImg');
                }

                function getUrl(urlId, id) {
                    var url = '';
                    var charImg = document.all(urlId).getElementsByTagName("img");
                    if (charImg.length == 1) {
                        url = charImg[0].src;
                    } else {
                        for (var i = 0; i < charImg.length; i++) {
                            url += charImg[i].src + ',';
                        }
                        url = url.substring(0, url.length - 1);
                    }
                    $(id).val(url);
                }

                function returnAddress() {
                    $('#selectAddressDataGrid').modal('hide');
                }

                function findCompany(value) {
                    if (null != value && '' != value && '装饰公司' == value) {
                        $("#com").show();
                        var store = "";
                        $.ajax({
                            url: '/rest/stores/find/decorativeCompany',
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
                                $("#decorationCompany").append(store);
                                $('#decorationCompany').selectpicker('refresh');
                                $('#decorationCompany').selectpicker('render');
                            }
                        });
                    }else {
                        $("#com").hide();
                    }
                }


                function findCompanyPeople(storeId) {
                    var peopleType = $('#peopleType').val();
                    if (-1 == peopleType || null == peopleType || '' == peopleType) {
                        $notify.warning("请选择下单人类型！");
                        return false;
                    }
                    var url = '/rest/order/photo/find/people/storeId';
                    $("#createOrderPeopleDataGrid").bootstrapTable('destroy');
                    $grid.init($('#createOrderPeopleDataGrid'), $('#peopleToolbar'), url, 'get', false, function (params) {
                        return {
                            offset: params.offset,
                            size: params.limit,
                            keywords: params.search,
                            peopleType: peopleType,
                            storeId:storeId
                        }
                    }, [{
                        field: 'peopleId',
                        title: 'ID',
                        align: 'center'
//                    visible:false
                    }, {
                        field: 'name',
                        title: '下单人姓名',
                        align: 'center',
                        events: {
                            'click .scan': function (e, value, row) {
                                fillingPeople(row);
                            }
                        },
                        formatter: function (value) {
                            return '<a class="scan" href="#' + value + '">' + value + '</a>';
                        }
                    }, {
                        field: 'phone',
                        title: '下单人电话',
                        align: 'center'
                    }, {
                        field: 'identityType',
                        title: '身份类型',
                        align: 'center'
                    }, {
                        field: 'storeName',
                        title: '下单人归属门店名',
                        align: 'center'
                    }, {
                        field: 'storeCode',
                        title: '下单人归属门店code',
                        align: 'center',
                        visible: false
                    }
                    ]);
                }

                function returnPeople() {
                    $('#selectCreateOrderPeopleGrid').modal('hide');
                }
                function returnProxy() {
                    $('#selectProxyCreateOrderPeopleGrid').modal('hide');
                }
                function findProxyPeople() {
                    $('#selectProxyCreateOrderPeopleGrid').modal('show');
                    var selectProxyCreateOrderPeopleConditions = $('#selectProxyCreateOrderPeopleConditions').val();

                    var url = '/rest/order/photo/find/proxy';
                    $("#proxyCreateOrderPeopleDataGrid").bootstrapTable('destroy');
                    $grid.init($('#proxyCreateOrderPeopleDataGrid'), $('#proxyToolbar'), url, 'get', false, function (params) {
                        return {
                            offset: params.offset,
                            size: params.limit,
                            keywords: params.search,
                            selectProxyCreateOrderPeopleConditions: selectProxyCreateOrderPeopleConditions
                        }
                    }, [{
                        field: 'peopleId',
                        title: 'ID',
                        align: 'center'
//                    visible:false
                    }, {
                        field: 'name',
                        title: '代下单人姓名',
                        align: 'center',
                        events: {
                            'click .scan': function (e, value, row) {
                                fillingProxy(row);
                            }
                        },
                        formatter: function (value) {
                            return '<a class="scan" href="#' + value + '">' + value + '</a>';
                        }
                    }, {
                        field: 'phone',
                        title: '代下单人电话',
                        align: 'center'
                    }, {
                        field: 'identityType',
                        title: '身份类型',
                        align: 'center'
                    }, {
                        field: 'storeName',
                        title: '代下单人归属门店名',
                        align: 'center'
                    }, {
                        field: 'storeCode',
                        title: '代下单人归属门店code',
                        align: 'center',
                        visible: false
                    }
                    ]);

                    $('#selectProxyCreateOrderPeopleGrid').modal('show');
                }
                function fillingProxy(row) {
                    $('#proxyId').val(row.peopleId);
                    $('#proxyName').val(row.name);
                    $('#selectProxyCreateOrderPeopleGrid').modal('hide');
                }

                //检验库存
                function inspectionStock() {
                    $("#selectedGoodsTable").empty();
                    var formData = new FormData($("#form")[0]);
                    var identityType =  $("#identityType").val();
                    if ('' === identityType){
                        $notify.warning("请选择下单人！");
                        return ;
                    }

                    if ('导购' === identityType){
                        $notify.warning("导购下单请加入下料清单，在App端进行支付！");
                        return ;
                    }
                    if ('装饰公司员工' === identityType){
                        $notify.warning("装饰公司员工下单请加入下料清单，由经理在App端进行支付！");
                        return ;
                    }

                    if (null === $global.timer) {
                        $global.timer = setTimeout($loading.show, 2000);
                        var url = '/rest/order/photo/inspection/stock';
                        $.ajax({
                            url: url,
                            method: 'POST',
                            data: formData,
                            async: false,
                            cache: false,
                            contentType: false,
                            processData: false,
                            error: function () {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger('网络异常，请稍后重试或联系管理员');
                                $('#form').bootstrapValidator('disableSubmitButtons', false);
                            },
                            success: function (result) {
                                if (0 === result.code) {
                                    clearTimeout($global.timer);
                                    $loading.close();
                                    $global.timer = null;
                                    var stockOutStr = "";
                                    var str = "";
                                    // 计数
                                    var num = 0;
                                    var num2 = 0;
                                    $.each(result.content, function (i, item) {
                                        var status;
                                        if (item.errorType === 'GOODS_NOT_EXISTS') {
                                            status = "<span class='label label-danger'>商品不存在</span>";
                                        } else if (item.errorType === 'INV_NOT_ENOUGH') {
                                            status = "<span class='label label-danger'>库存不足</span>";
                                        } else if (item.errorType === 'PRICE_NOT_EXISTS') {
                                            status = "<span class='label label-danger'>没有价目表</span>";
                                        } else {
                                            status = "<span class='label label-success'>OK</span>";
                                        }

                                        if (status != "<span class='label label-success'>OK</span>") {
                                            num += 1;
                                            stockOutStr += "<tr>" +
                                                    "<td>" + (num) + "</td>" +
                                                    // "<td><input id='externalCode' type='text' value='" + item.externalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='qty' type='number' value='" + item.qty + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='internalCode' type='text' value='" + item.internalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='internalName' type='text' value='" + item.internalName + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='inventory' type='number' value='" + item.inventory + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='invDifference' type='number' value='" + item.invDifference + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td>" + status + "</td>" +
                                                    // "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                                                    /* "<td><input id='status' type='text' value='" + item.status + "' style='width:90%;border: none;' readonly></td>" +*/
                                                    "</tr>"
                                        }
                                    })
                                    $.each(result.content, function (i, item) {
                                        var status;
                                        if (item.errorType === 'GOODS_NOT_EXISTS') {
                                            status = "<span class='label label-danger'>商品不存在</span>";
                                        } else if (item.errorType === 'INV_NOT_ENOUGH') {
                                            status = "<span class='label label-danger'>库存不足</span>";
                                        } else if (item.errorType === 'PRICE_NOT_EXISTS') {
                                            status = "<span class='label label-danger'>没有价目表</span>";
                                        } else {
                                            status = "<span class='label label-success'>OK</span>";
                                        }

                                        if (status == "<span class='label label-success'>OK</span>") {
                                            num2 += 1;
                                            str += "<tr>" +
                                                    "<td>" + (num + num2) + "</td>" +
                                                    // "<td><input id='externalCode' type='text' value='" + item.externalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='qty' type='number' value='" + item.qty + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='internalCode' type='text' value='" + item.internalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='internalName' type='text' value='" + item.internalName + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='inventory' type='number' value='" + item.inventory + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td><input id='invDifference' type='number' value='" + item.invDifference + "' style='width:90%;border: none;' readonly></td>" +
                                                    "<td>" + status + "</td>" +
                                                    // "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                                                    /* "<td><input id='status' type='text' value='" + item.status + "' style='width:90%;border: none;' readonly></td>" +*/
                                                    "</tr>"
                                        }
                                    })
                                    $("#selectedGoodsTable").append(stockOutStr);
                                    $("#selectedGoodsTable").append(str);
                                    $('#inspectionStock').modal('show');
                                } else {
                                    $('#inspectionStock').modal('hide');
                                    clearTimeout($global.timer);
                                    $loading.close();
                                    $global.timer = null;
                                    $notify.danger(result.message);
                                }
                            }
                        });
                    }
                }

                function returnInspectionStock() {
                    $('#inspectionStock').modal('hide');
                }
                function returnSelectGift() {
                    $('#giftSelectionBox').modal('hide');
                }

                //查询促销
                function selectGifts() {
                    var identityType =  $("#identityType").val();
                    if ('装饰公司员工' === identityType){
                        openOrderDetail();
                        return ;
                    }


                    $('#inspectionStock').modal('hide');
                    //清空赠品信息
                    document.getElementById('giftMessage').innerHTML = "";
                    document.getElementById('subAmount_div').innerHTML = "";
                    $('#giftSelectionBox').modal('show');
                    var url = '/rest/order/photo/page/gifts';
                    var formData = new FormData($("#form")[0]);
                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            var title = "";
                            if (0 === result.code) {
                                var promotionsListResponse = result.content;
                                if (null === promotionsListResponse) {
                                    openOrderDetail();
                                    return;
                                }
                                var giftListResponse = promotionsListResponse.promotionGiftList;
                                if (null != giftListResponse) {
                                    for (var i = 0; i < giftListResponse.length; i++) {
                                        var isArbitraryChoice = '否';
                                        if (giftListResponse[i].isGiftOptionalQty) {
                                            isArbitraryChoice = '是';
                                        }
                                        title += "<div id='giftTitle'>" +
                                                "<b style='padding-left: 10px'>促销标题:</b>" +
                                                "<span id='actTitle' style='padding-left: 5px'>" + giftListResponse[i].promotionTitle + "</span>" +
                                                "<b style='padding-left: 150px'>最大可选数量:</b>" +
                                                "<span id='actMaxQty' style='padding-left: 5px'>" + giftListResponse[i].maxChooseNumber + "</span>" +
                                                "<b style='padding-left: 150px'>赠品数量是否任选:</b>" +
                                                "<span id='IsArbitraryChoice' style='padding-left: 5px'>" + isArbitraryChoice + "</span>" +
                                                "</div>" +
                                                "<div class='box-body table-responsive no-padding'>" +
                                                "<div class='col-xs-12'>" +
                                                "<table id='giftTable' class='table table-hover'>" +
                                                "<thead id='giftHeader'>" +
                                                "<tr>" +
                                                "<th>ID</th>" +
                                                "<th>商品价格</th>" +
                                                "<th>商品名</th>" +
                                                "<th>数量</th>" +
                                                "</tr>" +
                                                "</thead>" +
                                                "<tbody id='giftsTable'";


                                        if (null != giftListResponse[i].giftList && "" != giftListResponse[i].giftList) {
                                            var giftList = giftListResponse[i].giftList
                                            for (var j = 0; j < giftList.length; j++) {
                                                var price = giftList[j].retailPrice.toFixed(2);
                                                if ('是' == isArbitraryChoice) {
                                                    title += "<tr>" +
                                                            "<td><input type='text' id='gid'value=" + giftList[j].goodsId + " style='width:90%;border: none;' readonly /></td>" +
                                                            "<td><input id='retailPrice' type='text' value='" + price + "' style='width:90%;border: none;' readonly></td>" +
                                                            "<td><input id='title' type='text' value='" + giftList[j].skuName + "' style='width:90%;border: none;' readonly></td>" +
                                                            "<td><input id='giftQty' type='number' value='0'></td>" +
                                                            "<td><input id='promotionId' type='hidden' value='" + giftListResponse[i].promotionId + "'></td>" +
                                                            "<td><input id='enjoyTimes' type='hidden' value='" + giftListResponse[i].enjoyTimes + "'></td>" +
                                                            "<td><input id='maxChooseNumber' type='hidden' value='" + giftListResponse[i].maxChooseNumber + "'></td>" +
                                                            "</tr>"
                                                } else {
                                                    title += "<tr>" +
                                                            "<td><input type='text' id='gid'value=" + giftList[j].goodsId + " style='width:90%;border: none;' readonly /></td>" +
                                                            "<td><input id='retailPrice' type='text' value='" + price + "' style='width:90%;border: none;' readonly></td>" +
                                                            "<td><input id='title' type='text' value='" + giftList[j].skuName + "' style='width:90%;border: none;' readonly></td>" +
                                                            "<td><input id='giftQty' type='number' value='" + giftList[j].qty + "' readonly></td>" +
                                                            "<td><input id='promotionId' type='hidden' value='" + giftListResponse[i].promotionId + "'></td>" +
                                                            "<td><input id='enjoyTimes' type='hidden' value='" + giftListResponse[i].enjoyTimes + "'></td>" +
                                                            "<td><input id='maxChooseNumber' type='hidden' value='" + giftListResponse[i].maxChooseNumber + "'></td>" +
                                                            "</tr>"
                                                }

                                            }
                                        }
                                        title += "</tbody>" +
                                                "</table>" +
                                                "</div>" +
                                                "</div>";

                                    }
                                }
                                var promotionDiscountList = promotionsListResponse.promotionDiscountList;
                                if (null != promotionDiscountList) {
                                    var money = 0;
                                    var promotionDiscountTitle = "";
                                    for (var a = 0; a < promotionDiscountList.length; a++) {
                                        promotionDiscountTitle += "<div id='promotionDiscountTitle'>" +
                                                "<b style='padding-left: 10px'>立减促销标题:</b>" +
                                                "<span id='promotionDiscountTitle' style='padding-left: 5px'>" + promotionDiscountList[a].promotionTitle + "</span>" +
                                                "<b style='padding-left: 150px'>参与此促销次数:</b>" +
                                                "<span id='promotionDiscountenjoyTimesQty' style='padding-left: 5px'>" + promotionDiscountList[a].enjoyTimes + "</span>" +
                                                "<b style='padding-left: 150px'>优惠金额:</b>" +
                                                "<span id='discountPrice' style='padding-left: 5px'>" + promotionDiscountList[a].discountPrice + "</span>" +
                                                "<div name='aa' id='aa'>" +
                                                "<td><input id='promotionDiscountId' type='hidden' value='" + promotionDiscountList[a].promotionId + "'></td>" +
                                                "<td><input id='promotionDiscountPrice' type='hidden' value='" + promotionDiscountList[a].discountPrice + "'></td>" +
                                                "<td><input id='promotionDiscountenjoyTimes' type='hidden' value='" + promotionDiscountList[a].enjoyTimes + "'></td>" +
                                                "</div>" +
                                                "</div>";
                                        money += promotionDiscountList[a].discountPrice;

                                    }

                                    promotionDiscountTitle += "</br><div class='col-xs-12 col-md-6'>" +
                                            "<div class='form-group'>" +
                                            "<label for='description'>" +
                                            "总共立减金额￥" +
                                            "</label>" +
                                            "<div class='input-group'>" +
                                            "<span class='input-group-addon'><i class='fa fa-cny'></i></span>" +
                                            "<input name='subAmount' type='number' readonly class='form-control'id='subAmount' value='" + money.toFixed(2) + "'>" +
                                            "</div>" +
                                            "</div>" +
                                            "</div>";
                                }

                                $("#giftMessage").append(title);
                                $("#subAmount_div").append(promotionDiscountTitle);
                                //锁定input输入款与a标签按钮
                                $("#selectedGoodsTable").find("input,button,textarea,select").attr("readOnly", "readOnly");
                                $("#selectedGoodsTable").find("a").removeAttr("onclick");
//                document.getElementById("selectGoods").value="更改商品";
//                        innerHtml="更改商品";
//                $("#selectedGoodsTable").find("a").attr("onclick","del_goods_comb(this)");
//                //设置促销折扣
//                $("#promotionsDiscount").text((0 - money).toFixed(2));
//                //获取零售价总金额
//                var totalGoodsPrice1 = document.getElementById("totalGoodsPrice").innerHTML;
//                //获取会员折扣
//                var vipDiscount1 = document.getElementById("vipDiscount").innerHTML;
//                //计算应付金额
//                var amountsPayable1 = Number(totalGoodsPrice1) + Number(vipDiscount1) + Number(money);
//                //设置应付金额
//                $("#amountsPayable").text(amountsPayable1.toFixed(2))

                                //赠品促销标题
                            } else {
                                $notify.danger(result.message);
                                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                            }
                        }
                    });
                }

                function returnOrderDetail() {
                    $('#orderDetail').modal('hide');
                }

                //确认订单框
                function openOrderDetail() {
                    document.getElementById('GoodsListTable').innerHTML = "";
                     document.getElementById("payMsg").style.display="none";
                    document.getElementById("totalGoodsAmount").innerText = 0.00;
                    document.getElementById("memberDiscount").innerText = 0.00;
                    document.getElementById("promotionDiscount").innerText = 0.00;
                    document.getElementById("freight").innerText = 0.00;
                    document.getElementById("amountsPayable").innerText = 0.00;
                    document.getElementById("stPreDeposit").innerText = 0.00;
                    document.getElementById("stCreditMoney").innerText = 0.00;
                    document.getElementById("stSubvention").innerText = 0.00;


                    $('#orderDetail').modal('show');
                    var url = '/rest/order/photo/order/detail';
                    //获取赠品详情
                    var giftDetails = new Array();
                    var b = giftDetail(giftDetails, 'giftMessage');
                    if (b == 1) {
                        $loading.close();
                        return;
                    }
                    var formData = new FormData($("#form")[0]);
                    formData.append("giftDetails",JSON.stringify(giftDetails));

                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            var goodsDetails = "";
                            if (0 === result.code) {
                                var promotionsListResponse = result.content;

                                var goodsDetail = promotionsListResponse.maPhotoOrderGoodsDetailResponse;
                                var calulateAmount = promotionsListResponse.maOrderCalulatedAmountResponse;
                                if (null != goodsDetail && null != calulateAmount){
                                    for (var i = 0;i < goodsDetail.length;i++){
                                        goodsDetails += "<tr>" +
                                                "<td><input id='gdsId' type='number' value='" + goodsDetail[i].gid + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='gdsSku' type='text' value='" + goodsDetail[i].sku + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='gdsSkuName' type='text' value='" + goodsDetail[i].skuName + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='gdsQty' type='number' value='" + goodsDetail[i].qty + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='gdsRetailPrice' type='number' value='" + goodsDetail[i].retailPrice + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='gdsVipPrice' type='number' value='" + goodsDetail[i].vipPrice + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='gdsTye' type='text' value='" + goodsDetail[i].goodsType + "' style='width:90%;border: none;' readonly></td>" +
                                                "</tr>"

                                    }
                                    $("#GoodsListTable").append(goodsDetails);
                                    $("#setDetailedAddress").text(promotionsListResponse.detailedAddress);
                                    $("#setReceiverName").text(promotionsListResponse.receiverName);
                                    $("#setReceiverPhone").text(promotionsListResponse.receiverPhone);

                                    getDistributionTime(promotionsListResponse.cityName);

                                    document.getElementById("totalGoodsAmount").innerText = calulateAmount.totalGoodsAmount;
                                    document.getElementById("memberDiscount").innerText = calulateAmount.memberDiscount;
                                    document.getElementById("promotionDiscount").innerText = calulateAmount.promotionDiscount;
                                    document.getElementById("freight").innerText = calulateAmount.freight;
                                    var amountsPayable = (calulateAmount.totalGoodsAmount*100 - (calulateAmount.memberDiscount*100 + calulateAmount.promotionDiscount*100))/100
                                    document.getElementById("amountsPayable").innerText = calulateAmount.totalOrderAmount;

                                    if (null != promotionsListResponse.identityType && 2 == promotionsListResponse.identityType) {

                                        document.getElementById("payMsg").style.display="block";

                                        document.getElementById("stPreDeposit").innerText = calulateAmount.stPreDeposit;
                                        document.getElementById("stCreditMoney").innerText = calulateAmount.stCreditMoney;
                                        document.getElementById("stSubvention").innerText = calulateAmount.stSubvention;
                                    }
                                }
                            } else {
                                $('#giftSelectionBox').modal('hide');
                                $('#orderDetail').modal('hide');
                                $notify.danger(result.message);
//                             $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                            }
                        }
                    });

                }

                //提交保存获取赠品信息
                function giftDetail(details, divId) {

                    var tables = $("#" + divId).find("tbody");

                    var tabless = $('div[name="aa"]');


                    var subAmount = $("#subAmount").val();
                    var discountMoney = 0;
                    var num = 0;
                    //数量正则
                    var re = /^[0-9]+.?[0-9]*$/;

                    tabless.each(function (i, n) {
                        var promotionDiscountId = $(n).find("#promotionDiscountId").val();
                        var promotionDiscountPrice = $(n).find("#promotionDiscountPrice").val();
                        var promotionDiscountenjoyTimes = $(n).find("#promotionDiscountenjoyTimes").val();

                        details.push({
                            promotionId: promotionDiscountId,
                            discount: promotionDiscountPrice,
                            enjoyTimes: promotionDiscountenjoyTimes,
                            presentInfo: null
                        });

                    });
                    tables.each(function (i, n) {
                        var maxChooseNumber = 0;
                        var trs = $(n).find('tr');
                        var giftGoodsList = new Array();
                        var totalQty = 0;
                        var promotionId = 0;
                        var enjoyTimes = 0;
                        trs.each(function (i, m) {
                            var id = $(m).find("#gid").val();
                            var qty = $(m).find("#giftQty").val();
                            promotionId = $(m).find("#promotionId").val();
                            enjoyTimes = $(m).find("#enjoyTimes").val();
                            maxChooseNumber = $(n).find("#maxChooseNumber").val();
                            totalQty += Number(qty);
                            if (qty != '' && qty > 0 && qty != 0) {
                                giftGoodsList.push({
                                    id: id,
                                    qty: qty
                                });
                            }

                        });
                        if (Number(totalQty) > Number(maxChooseNumber)) {
                            $notify.warning("选择促销商品大于最大可选数量，请检查！");
                            num = 1;
                            return num;
                        }
                        if (subAmount != '' || subAmount > 0) {
                            discountMoney = subAmount;
                        } else {
                            discountMoney = null;
                        }

                        details.push({
                            promotionId: promotionId,
                            discount: discountMoney,
                            enjoyTimes: enjoyTimes,
                            presentInfo: giftGoodsList
                        });
                    });
                    return num;
                }

                //去支付
                function goPay() {
                    $loading.show();
                    var residenceName = $("#residenceName").val();
                    var estateInfo = $("#estateInfo").val();
                    var detailedAddress = $("#detailedAddress").val();
                    var pointDistributionTime = $("#distributionTime").val();

                    var estateInfoLength = getInputLength(estateInfo);
                    var residenceNameLength = getInputLength(residenceName);
                    var detailedAddressLength = getInputLength(detailedAddress);

                    if (null == pointDistributionTime || -1 == pointDistributionTime){
                        $loading.close();
                        $("#pointDistributionTime").text("请选择配送时间");
                        alert("请选择配送时间");
                        $('#orderDetail').modal('hide');
                        return false;
                    }else{
                        $("#pointDistributionTime").text("");
                    }

                    if (estateInfoLength > 50) {
                        $loading.close();
                        $('#giftSelectionBox').modal('hide');
                        $('#orderDetail').modal('hide');
                        $notify.danger('楼盘名称长度超长，请重新输入！');
//                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                        return false;
                    }
                    if (detailedAddressLength > 200) {
                        $loading.close();
                        $('#giftSelectionBox').modal('hide');
                        $('#orderDetail').modal('hide');
                        $notify.danger('详细地址长度超长，请重新输入！');
//                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                        return false;
                    }
                    if (residenceNameLength > 50) {
                        $loading.close();
                        $('#giftSelectionBox').modal('hide');
                        $('#orderDetail').modal('hide');
                        $notify.danger('小区名长度超长，请重新输入！');
//                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                        return false;
                    }
                    var usePreDeposit = $("#usePreDeposit").val();
                    var useCreditMoney = $("#useCreditMoney").val();
                    var useSubvention = $("#useSubvention").val();
                    var amountsPayable = $("#amountsPayable").text();
                    var freight = $("#freight").text();
                    var stPreDeposit = $("#stPreDeposit").text();
                    var stCreditMoney = $("#stCreditMoney").text();
                    var stSubvention = $("#stSubvention").text();
                    if (Number(usePreDeposit) > Number(stPreDeposit)){
                        $loading.close();
                        $('#orderDetail').modal('hide');
                        alert("使用客户预存款金额超出可用金额!");
//                        $notify.warning("使用客户预存款金额超出可用金额！");
                        return;
                    }
                    if (Number(useCreditMoney) > Number(stCreditMoney)){
                        $loading.close();
                        $('#orderDetail').modal('hide');
                        alert("使用信用金金额超出可用金额!");
//                        $notify.warning("使用信用金金额超出可用金额！");
                        return;
                    }
                    if (Number(useSubvention) > Number(stSubvention)){
                        $loading.close();
                        $('#orderDetail').modal('hide');
                        alert("使用现金返利金额超出可用金额!");
//                        $notify.warning("使用现金返利金额超出可用金额！");
                        return;
                    }



                    if (null != useCreditMoney && useCreditMoney > 0){
                        var totalAmount = (usePreDeposit*100 + useCreditMoney*100 + useSubvention*100)/100;
                        if (totalAmount != amountsPayable){
                            $loading.close();
                            $('#orderDetail').modal('hide');
                            alert("使用信用金支付必须一次性支付完毕!");
//                            $notify.warning("使用信用金支付必须一次性支付完毕");
                            return;
                        }
                    }

                    var billingMsgString = {
                        "stPreDeposit": usePreDeposit,
                        "storeCreditMoney": useCreditMoney,
                        "storeSubvention": useSubvention,
                        "freight":freight
                    }
                    var remark = $("#remark").val();
                    var remarkLength = getInputLength(remark);
                    var guideId = $("#guideId").val();
                    var contactName = $("#contactName").val();
                    var contactPhone = $("#contactPhone").val();
                    var peopleIdentityType = $("#peopleIdentityType").val();
                    var rotationImg = $("#rotationImg").val();


                    var url = '/rest/order/photo/ma/photo/create';
                    //获取赠品详情
                    var giftDetails = new Array();
                    var b = giftDetail(giftDetails, 'giftMessage');
                    if (b == 1) {
                        $loading.close();
                        return;
                    }
                    var formData = new FormData($("#form")[0]);
                    formData.append("giftDetails",JSON.stringify(giftDetails));
                    formData.append("billingMsg",JSON.stringify(billingMsgString));
                    formData.append("remark", remark);
                    formData.append("contactName", contactName);
                    formData.append("contactPhone", contactPhone);
                    formData.append("photoImgs", rotationImg);
                    formData.append("pointDistributionTime", pointDistributionTime);


                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $('#giftSelectionBox').modal('hide');
                            $('#orderDetail').modal('hide');
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#form').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                $loading.close();
                                window.location.href="/views/admin/order/photo/list";

                            } else {
                                $loading.close();
                                $('#giftSelectionBox').modal('hide');
                                $('#orderDetail').modal('hide');
                                $notify.danger(result.message);
//                             $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                            }
                        }
                    });
                }

                function priceBlur(type) {
                    if ('stPreDeposit' === type){
                        var stPreDeposit = $("#stPreDeposit").text();
                        var price = document.getElementById("usePreDeposit").value;
                        if (Number(price) > Number(stPreDeposit)){
                            $("#pointUsePreDeposit").text("输入金额大于可使用金额");
//                        $notify.warning("输入金额大于可使用金额，请重新输入！")
                            return;
                        }else{
                            $("#pointUsePreDeposit").text("");
                        }
                    }else if ('stCreditMoney' === type){
                        var stCreditMoney = $("#stCreditMoney").text();
                        var price = document.getElementById("useCreditMoney").value;
                        if (Number(price) > Number(stCreditMoney)){
                            $("#pointUseCreditMoney").text("输入金额大于可使用金额");
//                        $notify.warning("输入金额大于可使用金额，请重新输入！")
                            return;
                        }else{
                            $("#pointUseCreditMoney").text("");
                        }
                    }else if ('stSubvention' === type){
                        var stSubvention = $("#stSubvention").text();
                        var price = document.getElementById("useSubvention").value;
                        if (Number(price) > Number(stSubvention)){
                            $("#pointUseSubvention").text("输入金额大于可使用金额");
//                        $notify.warning("输入金额大于可使用金额，请重新输入！")
                            return;
                        }else{
                            $("#pointUseSubvention").text("");
                        }
                    }
                }

                //获取配送时间
                function getDistributionTime(cityName) {
                    var distributionTime = "";
                    $.ajax({
                        url: '/rest/order/photo/get/deliveryTime/'+cityName,
                        method: 'GET',
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            var a = result.content;
                            $.each(a, function (i, item) {
                                distributionTime += "<option value=" + item + ">" + item + "</option>";
                            })
                            $("#distributionTime").append(distributionTime);
                            $("#distributionTime").selectpicker('refresh');
                        }
                    });
                }
            </script>
    </section>
</div>
</body>
</html>
