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

            <img id="big${item_index}" class="big-img" style="height: 65%;width: 80%;" onclick="outBig(${item_index})" src="${item!''}"/>
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
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-5 invoice-col">
                        <b>门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;店:</b>
                        <br>
                        <b>下单人身份类型:</b>
                        <br>
                        <b>拍&nbsp;照&nbsp;下&nbsp;单&nbsp;单&nbsp;号:</b>
                        <br>
                    </div>
                    <div class="col-sm-7 invoice-col">
                        <b></b>
                        <spanp class="span">${photoOrderVO.storeName!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span">${photoOrderVO.identityType!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.photoOrderNo!""}</spanp>
                        <br>
                    </div>
                </div>
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-4 invoice-col">
                        <b>下单人姓名:</b>
                        <br>
                        <b>联系人姓名:</b>
                        <br>
                        <b>下&nbsp;单&nbsp;&nbsp;时&nbsp;&nbsp;间:</b>
                        <br>
                    </div>
                    <div class="col-sm-8 invoice-col">
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.username!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.contactName!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.createTime!""}</spanp>
                        <br>
                    </div>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col">
                    <div class="col-sm-5 invoice-col">
                        <b>下单人手机号码:</b>
                        <br>
                        <b>联&nbsp;&nbsp;&nbsp;系&nbsp;&nbsp;人&nbsp;&nbsp;电&nbsp;&nbsp;话:</b>
                        <br>
                        <b>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态:</b>
                        <br>
                    </div>
                    <div class="col-sm-7 invoice-col">
                        <b></b>
                        <spanp class="span">${photoOrderVO.userMobile!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.contactPhone!""}</spanp>
                        <br>
                        <b></b>
                        <spanp class="span"> ${photoOrderVO.status!""}</spanp>
                        <br>
                    </div>
                </div>
            </div>
        </div>
        <form id="form">
            <div class="row">
                <div class="col-xs-12 table-responsive">
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
                                <#--<tr>-->
                                    <#--<td><input type="hidden" id="number" name="combList[0].gid" class="td-input" value="1254" />sku</td>-->
                                    <#--<td>商品名称</td>-->
                                    <#--<td>商品类型</td>-->
                                    <#--<td>单价</td>-->
                                    <#--<td ><input type="text" id="number" name="combList[0].qty" class="td-input" value="1254" style="width:30%;"/></td>-->
                                    <#--<td>删除</td>-->
                                <#--</tr>-->
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
        <div class="col-sm-12 invoice-col" style="height: 20px; border-bottom-style: solid;"></div>
        <div class="col-sm-12 invoice-col" style="height: 5px; "></div>
        <div class="row">
            <div class="col-xs-4">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">照片</h3>
                    </div>

                    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                        <ol class="carousel-indicators">
                            <#if photoOrderVO??&&photoOrderVO.photos??>
                                <#list photoOrderVO.photos as photo>
                                    <li data-target="#carousel-example-generic" data-slide-to="${photo_index}" class="<#if photo_index == 0>active</#if>"></li>
                                </#list>
                            </#if>
                        </ol>
                        <div class="carousel-inner">
                            <#if photoOrderVO??&&photoOrderVO.photos??>
                                <#list photoOrderVO.photos as photo>
                                    <div class="item <#if photo_index == 0>active</#if>">
                                        <img src="${photo!''}" style="height: 300px;width: 325px;" id="show${photo_index}" onclick="showBig(${photo_index})" alt="First slide">

                                        <#--<div class="carousel-caption">
                                            First Slide
                                        </div>-->
                                    </div>
                                </#list>
                            </#if>
                        </div>
                        <a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
                            <span class="fa fa-angle-left"></span>
                        </a>
                        <a class="right carousel-control" href="#carousel-example-generic" style="margin-right:30px; " data-slide="next">
                            <span class="fa fa-angle-right" ></span>
                        </a>
                    </div>
                    <!-- /.box-body -->
                </div>
            </div>
            <!-- /.col -->
            <div class="col-xs-8">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">选择商品</h3>
                    </div>
                    <div class="col-sm-1 invoice-col" style="border-bottom-style: solid; border-right-style: solid; border-width: 1px;">
                        <b>&nbsp; </b>
                    </div>
                    <div class="col-sm-2 invoice-col" style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                        <b><a id="WATER" name="category1" onclick="findCategory('WATER')">水</a></b>
                    </div>
                    <div class="col-sm-2 invoice-col" style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                        <b><a id="ELECTRIC" name="category1" onclick="findCategory('ELECTRIC')">电</a></b>
                    </div>
                    <div class="col-sm-2 invoice-col" style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                        <b><a id="WOOD" name="category1" onclick="findCategory('WOOD')">木</a></b>
                    </div>
                    <div class="col-sm-2 invoice-col" style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                        <b><a id="TILE" name="category1" onclick="findCategory('TILE')">瓦</a></b>
                    </div>
                    <div class="col-sm-2 invoice-col" style="border-bottom-style: solid; border-right-style: solid; border-width: 1px; text-align: center;">
                        <b><a id="OIL" name="category1" onclick="findCategory('OIL')">油</a></b>
                    </div>
                    <div class="col-sm-1 invoice-col" style="border-bottom-style: solid; border-width: 1px;">
                        <b>&nbsp; </b>
                    </div>
                    <div class="col-sm-12 invoice-col" style="height: 10px"></div>
                    <div class="col-sm-12 invoice-col">
                        <div class="col-sm-2 invoice-col">
                            <b>全部类型：</b>
                        </div>
                        <div class="col-sm-10 invoice-col" id="category">
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
                        <button type="button" onclick="addCart();" class="btn btn-primary footer-btn">
                            <i class="fa fa-check"></i> 一键添加
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            $(function () {
                $('#WATER').click();
            });

            function findCategory(categoryCode) {
                var category = '';
                var goods = '';
                var photoId = $('#photoId').val();
                $("[name='category1']").css('color','#72afd2')
                $('#'+ categoryCode).css('color','red')
                $.ajax({
                    url: '/rest/order/photo/findCategory',
                    method: 'GET',
                    data:{
                        categoryCode: categoryCode,
                        id: photoId
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
                            category += '<div class="col-sm-3 invoice-col"><a id="category'+ item.cid +'" name="category2" onclick="findGoodsByCategoryId('+ item.cid +')">'+ item.categoryName +'</a> </div>';
                        });
                        $("#category").html(category);
                        $.each(result.content.goods, function (i, item) {
                            goods += '<div class="col-sm-12 invoice-col">';
                            goods += '<div class="col-sm-3 invoice-col"><img src="'+ item.coverImageUri +'" style="height: 80px;width: 80px;" alt="First slide"></div>';
                            goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">'+ item.goodsName +'</b></div>';
                            goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：'+ item.goodsSpecification +'</span></div>';
                            goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：'+ item.goodsUnit +'</span></div></div>';
                            goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥'+ item.retailPrice +'</span></div>';
                            goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity('+ item.id +',';
                            goods += "'delete'";
                            goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                            goods += ' <input type="text" class="goodsSelectedQuantity" min="0" id="quantity'+ item.id +'" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)"/>';
                            goods += '<a onclick="changeQuantity('+ item.id +',';
                            goods += "'add'";
                            goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                            goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>';
                            goods += '<input type="hidden" id="sku'+ item.id +'" value="'+ item.sku +'"/>';
                            goods += '<input type="hidden" id="goodsName'+ item.id +'" value="'+ item.goodsName +'"/>';
                            goods += '<input type="hidden" id="typeName'+ item.id +'" value="'+ item.typeName +'"/>';
                            goods += '<input type="hidden" id="price'+ item.id +'" value="'+ item.retailPrice +'"/>';
                        });
                        $("#goods").html(goods);
                    }
                });
            }

            function findGoodsByCategoryId(categoryId) {
                var goods = '';
                var photoId = $('#photoId').val();
                $("[name='category2']").css('color','#72afd2')
                $('#category'+ categoryId).css('color','red')
                $.ajax({
                    url: '/rest/order/photo/findGoods',
                    method: 'GET',
                    data:{
                        categoryId: categoryId,
                        id: photoId
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
                            goods += '<div class="col-sm-3 invoice-col"><img src="'+ item.coverImageUri +'" style="height: 80px;width: 80px;" alt="First slide"></div>';
                            goods += '<div class="col-sm-9 invoice-col"><div class="col-sm-12 invoice-col"><b style="margin-left:-15%; ">'+ item.goodsName +'</b></div>';
                            goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-6 invoice-col"><span style="margin-left:-55%; ">规格：'+ item.goodsSpecification +'</span></div>';
                            goods += '<div class="col-sm-6 invoice-col"><span span style="margin-left:-51%; ">单位：'+ item.goodsUnit +'</span></div></div>';
                            goods += '<div class="col-sm-12 invoice-col"><div class="col-sm-4 invoice-col"><span style="margin-left:-70%; ">￥'+ item.retailPrice +'</span></div>';
                            goods += '<div class="col-sm-8 invoice-col"><a onclick="changeQuantity('+ item.id +',';
                            goods += "'delete'";
                            goods += ')"><i class="fa fa-minus"></i></a><span>&nbsp;&nbsp;&nbsp;</span>';
                            goods += '<input type="text" class="goodsSelectedQuantity" min="0" id="quantity'+ item.id +'" value="0" style="width: 15%; height: 18px; text-align: center;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onfocus="clearQuantity(this)" onblur="setQuantity(this)" />';
                            goods += '<span>&nbsp;</span><a onclick="changeQuantity('+ item.id +',';
                            goods += "'add'";
                            goods += ')"><i class="fa fa-plus"></i></a></div></div></div></div>';
                            goods += '<div class="col-sm-12 invoice-col" style="height: 5px;"></div>'
                            goods += '<input type="hidden" id="sku'+ item.id +'" value="'+ item.sku +'"/>';
                            goods += '<input type="hidden" id="goodsName'+ item.id +'" value="'+ item.goodsName +'"/>';
                            goods += '<input type="hidden" id="typeName'+ item.id +'" value="'+ item.typeName +'"/>';
                            goods += '<input type="hidden" id="price'+ item.id +'" value="'+ item.retailPrice +'"/>';
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
            function keyup(obj){
                if(obj.value.length==1){obj.value=obj.value.replace(/[^1-9]/g,'')}else{obj.value=obj.value.replace(/\D/g,'')};
            }
            //限制输入 只能输入数字
            function afterpaste(obj){
                if(obj.value.length==1){obj.value=obj.value.replace(/[^1-9]/g,'')}else{obj.value=obj.value.replace(/\D/g,'')};
            }

            //商品数量输入框获取焦点时清空
            function clearQuantity(obj){
                obj.value="";
            }

            //商品数量如果为空，则设为0
            function setQuantity(obj){
                if(obj.value.length==0){
                    obj.value=obj.min;
                }
            }
            function addCart(isGoHistory) {
                var params = "";
                var total = $('#total').val();
                // 获取所有value值大于0的input标签（即获得了所有数量要大于0的商品）
                $('.goodsSelectedQuantity').each(
                        // 获取标签之后拼接参数变量
                        function(i) {
                            var qty = $('.goodsSelectedQuantity').eq(i).val();
                            if (!isNaN(qty) && qty > 0) {
                                var goodsId = $('.goodsSelectedQuantity').eq(i).attr("id").replace("quantity", "");
                                var sku = $('#sku' + goodsId).val();
                                var goodsName = $('#goodsName' + goodsId).val();
                                var typeName = $('#typeName' + goodsId).val();
                                var price = $('#price' + goodsId).val();

                                params += '<tr><td><input type="hidden" id="gid" name="combList[' + total + '].gid" value="' + goodsId + '" />' + sku + '</td>';
                                params += '<td>' + goodsName + '</td><td>' + typeName + '</td><td>' + price + '</td>';
                                params += '<td ><input type="text" id="qty" min="1" name="combList[' + total + '].qty" value="' + qty + '" style="width:30%;" onkeyup="keyup(this)" onafterpaste="afterpaste(this)" onblur="setQuantity(this)"/></td>';
                                params += '<td><a title="删除" class="img-btn del operator" onclick="del_goods_comb(this);">删除</a></td></tr>';
                                total = parseInt(total) + 1;
                            }
                        });
                if ("" == params) {
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
                $("#total").val(parseInt($("#total").val())-1);
            }
            //看大图
            function showBig(obj){
                $("#big-img"+ obj).fadeIn(500);
            }
            //关闭大图
            function outBig(obj){
                $("#big-img"+ obj).fadeOut(500);
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
                    data:{
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
        </script>
    </section>
</div>
</body>
</html>
