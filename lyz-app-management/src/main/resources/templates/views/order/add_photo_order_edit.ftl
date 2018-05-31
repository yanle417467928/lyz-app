<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
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
                </div>
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-5 invoice-col">
                        <b>门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;店:</b>
                        <br>
                        <b>下单人身份类型:</b>
                        <br>
                        <#--<b>拍&nbsp;照&nbsp;下&nbsp;单&nbsp;单&nbsp;号:</b>-->
                        <br>
                        <br>
                        <br>
                        <b>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</b>
                        <br>
                    </div>
                    <div class="col-sm-7 invoice-col">
                        <b></b>
                        <spanp id="storeName" class="span"></spanp>
                        <br>
                        <b></b>
                        <spanp id="identityType" class="span"></spanp>
                        <br>
                        <b></b>
                        <spanp id="photoNo" class="span"></spanp>
                        <br>
                        <b></b>
                        <input id ="remark" class="input" style="width:300px"></input>
                        <br>
                    </div>
                </div>
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-4 invoice-col">
                        <b>下单人姓名:</b>
                        <br>
                        <b>联系人姓名:</b>
                        <br>
                        <#--<b>下&nbsp;单&nbsp;&nbsp;时&nbsp;&nbsp;间:</b>-->
                        <br>
                    </div>
                    <div class="col-sm-8 invoice-col">
                        <b></b>
                        <spanp id="createPeopleName" class="span"></spanp>
                        <br>
                        <b></b>
                        <spanp id="contactName" class="span"></spanp>
                        <br>
                        <b></b>
                        <spanp id="createPeopleTime" class="span"></spanp>
                        <br>
                        <input id="storeCode" type="hidden"/>
                        <input id="peopleId" type="hidden"/>
                    </div>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-5 invoice-col">
                        <b>下单人手机号码:</b>
                        <br>
                        <b>联&nbsp;&nbsp;&nbsp;系&nbsp;&nbsp;人&nbsp;&nbsp;电&nbsp;&nbsp;话:</b>
                        <br>
                        <#--<b>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                            <#--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态:</b>-->
                        <br>
                    </div>
                    <div class="col-sm-7 invoice-col">
                        <b></b>
                        <spanp id="createOrderPhone" class="span"></spanp>
                        <br>
                        <b></b>
                        <spanp id="contactPhone" class="span"></spanp>
                        <br>
                        <b></b>
                        <spanp id="status" class="span"></spanp>
                        <br>
                    </div>
                </div>
            </div>
        </div>
        <form id="form">
            <div class="row">
                <div class="col-xs-12 table-responsive">


                    <input type="hidden" id="guideName" name="guideName" value="">

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

                    <div id="writeDeliveryAddress">
                        <div class="row">
                            <div class="col-xs-12 col-md-3">
                                <div class="form-group">
                                    <label>
                                        收货人姓名
                                    </label>
                                    <input type="text" name="receiverName" id="receiverName" class="form-control"
                                           onkeyup="value=value=value.replace(/[\d]/g, '只能输入汉子和字母')" maxlength="10" \>
                                </div>
                            </div>
                            <div class="col-xs-12 col-md-3">
                                <div class="form-group">
                                    <label>
                                        收货人电话
                                    </label>
                                    <input type="text" name="receiverPhone" id="receiverPhone" class="form-control" \>
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
                                           onkeyup="value=value.replace(/[^\w\u4E00-\u9FA5]/g, '只能输入汉子、字母和数字')" \>
                                </div>
                            </div>
                            <div class="col-xs-12 col-md-3">
                                <div class="form-group">
                                    <label>
                                        楼盘信息
                                    </label>
                                    <input type="text" name="estateInfo" id="estateInfo" class="form-control" \>
                                </div>
                            </div>
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label>
                                        详细地址
                                    </label>
                                    <input type="text" name="detailedAddress" id="detailedAddress" class="form-control"
                                           \>
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
                <div class="col-xs-12 col-md-6"></div>
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
                        <i class="fa fa-check"></i> 提交保存
                    </button>
                </div>
            </div>
        </form>


        <!-- 地址选择框 -->
        <div id="selectAddressDataGrid" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document" style="width: 60%">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>选择地址</h4>
                    </div>
                    <div class="modal-body">
                        <!--  设置这个div的大小，超出部分显示滚动条 -->
                        <div id="addressDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                            <section class="content">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="box box-primary">
                                            <div id="addressToolbar" class="form-inline">

                                            <#--<div class="input-group col-md-3"-->
                                                     <#--style="margin-top:0px positon:relative">-->
                                                    <#--<input type="text" name="sellerQueryConditions"-->
                                                           <#--id="sellerQueryConditions"-->
                                                           <#--class="form-control" style="width:auto;"-->
                                                           <#--placeholder="请输入导购姓名或电话">-->
                                                    <#--<span class="input-group-btn">-->
                            <#--<button type="button" name="search" id="search-btn" class="btn btn-info btn-search"-->
                                    <#--onclick="return findSellerByNameOrMobil()">查找</button>-->
                        <#--</span>-->
                                                <#--</div>-->
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
                                                            class="form-control selectpicker">
                                                        <option value="-1" selected="selected">选择下单人类型</option>
                                                        <option value="会员">会&nbsp;&nbsp;&nbsp;员&nbsp;&nbsp;&nbsp;下&nbsp;&nbsp;&nbsp;单</option>
                                                        <option value="装饰公司">装饰公司下单</option>
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

            function findStoreList() {
//                $("#storeId").empty()
                var store = "";
                $.ajax({
                    url: '/rest/stores/findStoresListByLoginAdministrator/fit',
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
                        $("#storeId").append(store);
                        $('#storeId').selectpicker('refresh');
                        $('#storeId').selectpicker('render');
                        findOrderCreator();
                    }
                });
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
                    url: '/rest/order/photo/findCategory',
                    method: 'GET',
                    data: {
                        categoryCode: categoryCode,
                        id: photoId,
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
                var identityType = $('#identityType').text();
                var peopleId = $('#peopleId').val();

                var categoryType = $('#categoryType').val();
//                var categoryString = $('#categoryString').val();
                var brandString = $('#brandString').val();
                var specificationString = $('#specificationString').val();
                var goodsTypeString = $('#goodsTypeString').val();
                var guideId = $('#guideId').val();

                var goods = '';
                var photoId = $('#photoId').val();
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
                    url: '/rest/order/photo/findGoods',
                    method: 'GET',
                    data: {
                        categoryId: categoryId,
                        id: photoId,
                        guideId: guideId,
                        categoryType: categoryType,
                        brandString: brandString,
                        specificationString: specificationString,
                        goodsTypeString: goodsTypeString,
                        identityType:identityType,
                        peopleId:peopleId
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
//                var brandString = $('#brandString').val();
                var specificationString = $('#specificationString').val();
                var goodsTypeString = $('#goodsTypeString').val();

                var goods = '';
                var photoId = $('#photoId').val();
                if (categoryId == 0) {
                    $("[name='category1']").css('color', '#72afd2');
                }
                $("[name='brand']").css('color', '#72afd2');
                $('#brand' + brandId).css('color', 'red');
                $.ajax({
                    url: '/rest/order/photo/findGoods',
                    method: 'GET',
                    data: {
                        categoryId: categoryId,
                        id: photoId,
                        categoryType: categoryType,
                        brandString: brandId,
                        specificationString: specificationString,
                        goodsTypeString: goodsTypeString
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

                var goods = '';
                var photoId = $('#photoId').val();
                if (categoryId == 0) {
                    $("[name='category1']").css('color', '#72afd2');
                }
                $("[name='specification']").css('color', '#72afd2');
                $('#specification' + specificationString).css('color', 'red');
                $.ajax({
                    url: '/rest/order/photo/findGoods',
                    method: 'GET',
                    data: {
                        categoryId: categoryId,
                        id: photoId,
                        categoryType: categoryType,
                        brandString: brandString,
                        specificationString: specificationString,
                        goodsTypeString: goodsTypeString
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

                var goods = '';
                var photoId = $('#photoId').val();
                if (categoryId == 0) {
                    $("[name='category1']").css('color', '#72afd2');
                }
                $("[name='goodsType']").css('color', '#72afd2');
                $('#goodsType' + typeId).css('color', 'red');
                $.ajax({
                    url: '/rest/order/photo/findGoods',
                    method: 'GET',
                    data: {
                        categoryId: categoryId,
                        id: photoId,
                        categoryType: categoryType,
                        brandString: brandString,
                        specificationString: specificationString,
                        goodsTypeString: typeId
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
                $("#total").val(parseInt($("#total").val()) - 1);
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

                    if (estateInfoLength > 50) {
                        $notify.danger('楼盘名称长度超长，请重新输入！');
                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                        return false;
                    }
                    if (detailedAddressLength > 200) {
                        $notify.danger('楼盘名称长度超长，请重新输入！');
                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                        return false;
                    }
                    if (residenceNameLength > 50) {
                        $notify.danger('小区名长度超长，请重新输入！');
                        $('#form').bootstrapValidator('disableSubmitButtons', false);
                        return false;
                    }

                    e.preventDefault();
                    var $form = $(e.target);
                    var origin = $form.serializeArray();
                    var data = {};
                    var formData = new FormData($("#form")[0]);
                    if (null === $global.timer) {
                        $global.timer = setTimeout($loading.show, 2000);
                        var url = '/rest/order/photo/save';
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


            function findOrderCreator() {
                var storeId = $("#storeId").val();
                if (null == storeId || -1 == storeId) {
                    return false;
                }
                findCategory("WATER");
                $("#guideId").empty();
                var guide;
                $.ajax({
                    url: '/rest/employees/findSellerByStoreId/' + storeId,
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
                            guide += "<option value=" + item.id + ">" + item.name + "</option>";
                        })
                        $("#guideId").append(guide);
                        $('#guideId').selectpicker('refresh');
                        $('#guideId').selectpicker('render');
                    }
                });
            }

            function getInputLength(str) {
                return str.replace(/[\u0391-\uFFE5]/g, "aa").length;
            }

            function findDeliveryAddress() {
                var userMobile = $('#createOrderPhone').text();
                var identityType = $('#identityType').text();
                var url = '/rest/order/photo/find/address';
                $("#addressDataGrid").bootstrapTable('destroy');
                $grid.init($('#addressDataGrid'), $('#addressToolbar'), url, 'get', false, function (params) {
                    return {
                        offset: params.offset,
                        size: params.limit,
                        keywords: params.search,
                        userMobile: userMobile,
                        identityType: identityType
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
                var userMobile = $('#createOrderPhone').text();
                var identityType = $('#identityType').text();
                if (null == userMobile || '' == userMobile || null == identityType || '' == identityType){
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

            function  fillingPeople(row) {
                document.getElementById("storeName").innerText = row.storeName;
                if ('CUSTOMER' == row.identityType) {
                    document.getElementById("identityType").innerText = "顾客";
                }else{
                    document.getElementById("identityType").innerText = "装饰公司";
                }
                document.getElementById("photoNo").innerText = "";
                document.getElementById("contactName").innerText = row.name;
                document.getElementById("createPeopleName").innerText = row.name;
                document.getElementById("createPeopleTime").innerText = "";
                document.getElementById("createOrderPhone").innerText = row.phone;
                document.getElementById("contactPhone").innerText = row.phone;
                document.getElementById("status").innerText = "";
                $("#storeCode").val(row.storeCode);
                $("#peopleId").val(row.peopleId);
                $('#selectCreateOrderPeopleGrid').modal('hide');
            }
        </script>
    </section>
</div>
</body>
</html>
