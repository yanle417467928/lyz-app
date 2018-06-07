$(function () {
    // 初始化城市信息
    findCitylist();
    //初始化门店信息
    findStorelist();
    //获取品牌列表（下拉框）
    findGoodsBrand();
    //物理分类列表（下拉框）
    findGoodsPhysical();
    //初始化时间选择框
    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose: true
    });

    $("#goPay").hide();
    $("#preDeposit").hide();
    $("#offlinePayments").show();
})

//获取城市列表
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
            })
            $("#cityId").append(city);
            $("#cityId").selectpicker('refresh');
        }
    });
}

//获取门店列表
function findStorelist() {
    var store = "";
    $.ajax({
        url: '/rest/stores/findStoresListByStoreId',
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
        }
    });
}
//获取品牌列表（下拉框）
function findGoodsBrand() {
    var brand = '';
    $.ajax({
        url: '/rest/goodsBrand/page/brandGrid',
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
                brand += "<option value=" + item.brdId + ">" + item.brandName + "</option>";
            })
            $("#brandCode").append(brand);
            $("#brandCode").selectpicker('refresh');
        }
    });
}
//物理分类列表（下拉框）
function findGoodsPhysical() {
    var physical = '';
    $.ajax({
        url: '/rest/goods/page/physicalClassifyGrid',
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
                physical += "<option value=" + item.id + ">" + item.physicalClassifyName + "</option>";
            })
            $("#categoryCode").append(physical);
        }
    });
}

//根据城市查询门店
function findStoreByCity(cityId) {
    //如果有选择城市查询所有门店
    if (cityId == -1) {
        findStorelist();
        return false;
    }

    initSelect("#storeId", "选择门店");
    var cityId = $("#cityId").val();
    var store = "";
    $.ajax({
        url: '/rest/stores/findStoresListByCityIdAndStoreId/' + cityId,
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
        }
    });
}

//选择导购
function openSellerModal() {

    //关闭支付部分
    $("#goPay").hide();
    $("#goPayType").val(1);
    var price = 0.00;
    //初始化账单明细
    $("#totalGoodsPrice").text(price.toFixed(2))
    $("#vipDiscount").text(price.toFixed(2))
    $("#promotionsDiscount").text(price.toFixed(2))
    $("#amountsPayable").text(price.toFixed(2))
    //清空赠品信息
    document.getElementById('giftMessage').innerHTML = "";
    document.getElementById('subAmount_div').innerHTML = "";


    //查询导购列表
    initSeller('/rest/employees/select/seller');
    $("#sellerModalConfirm").unbind('click').click(function () {
    });
    $('#selectSeller').modal('show');
}

//条件查询导购
function findSellerByNameOrMobil() {
    var sellerQueryConditions = $("#sellerQueryConditions").val();
    $("#sellerDataGrid").bootstrapTable('destroy');
    if (null == sellerQueryConditions || "" == sellerQueryConditions) {
        initSeller('/rest/employees/select/seller');
    } else {
        initSeller('/rest/employees/select/seller/' + sellerQueryConditions);
    }
}
//按条件查询商品
function screenGoods() {
    var storeId = $('#storeId').val();
    var cusId = $("#customerId").html();
    var sellerId = $("#sellerId").html();

    if (-1 === storeId) {
        $notify.warning("请先选择门店");
        return false;
    }
    var brandCode = $('#brandCode').val();
    var categoryCode = $('#categoryCode').val();
    var companyCode = $('#companyCode').val();
    var productType = $('#productType').val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    initGoodsGrid('/rest/goods/page/screenGoodsGrid/buy/coupon?storeId='
        + storeId + '&brandCode=' + brandCode + '&categoryCode='
        + categoryCode + '&companyCode=' + companyCode + '&productType=' + productType
        + '&cusId=' + cusId + '&sellerId=' + sellerId);
}
//初始化商品信息
function initGoodsGrid(url) {
//        alert("jinru")
    $grid.init($('#goodsDataGrid'), $('#toolbar'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search
        }
    }, [{
        checkbox: true,
        title: '选择'
    }, {
        field: 'gid',
        title: 'ID',
        align: 'center'
    }, {
        field: 'sku',
        title: 'sku',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return '<span class="scan" >' + '-' + '</span>';
            } else {
                return '<sapn class="scan" >' + value + '</sapn>';
            }
        }
    }, {
        field: 'skuName',
        title: '名称',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return '<span class="scan" >' + '-' + '</span>';
            } else {
                return '<sapn class="scan" >' + value + '</sapn>';
            }
        }
    }, {
        field: 'vipPrice',
        title: '会员价',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return '<span class="scan" >' + '-' + '</span>';
            } else {
                return '<sapn class="scan" >' + value + '</sapn>';
            }
        }
    }, {
        field: 'retailPrice',
        title: '零售价',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return '<span class="scan" >' + '-' + '</span>';
            } else {
                return '<sapn class="scan" >' + value + '</sapn>';
            }
        }
    }, {
        field: 'categoryName',
        title: '类型',
        align: 'center',
        visible: false
    }, {
        field: 'goodsSpecification',
        title: '规格',
        align: 'center',
        visible: false
    }, {
        field: 'materialsEnable',
        title: '状态',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return '<span class="scan" >' + '-' + '</span>';
            } else {
                return '<sapn class="scan" >' + value + '</sapn>';
            }
        }
    }, {
        field: 'priceType',
        title: '产品类型',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return '<span class="scan" >' + '-' + '</span>';
            } else if (value == "A" || value == "C") {
                return '<sapn class="scan" >钻石专供</sapn>';
            } else if (value == "B" || value == "D") {
                return '<sapn class="scan" >黄金专供</sapn>';
            } else {
                return '<sapn class="scan" >普通</sapn>';
            }
        }
    }

    ]);
}


//初始化导购选择框
function initSeller(url) {
    var cityId = $('#cityId').val();
    var storeId = $('#storeId').val();
    $("#sellerDataGrid").bootstrapTable('destroy');
    $grid.init($('#sellerDataGrid'), $('#sellerToolbar'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search
        }
    }, [{
        checkbox: true,
        title: '选择'
    }, {
        field: 'empId',
        title: 'ID',
        align: 'center'
    }, {
        field: 'storeName',
        title: '门店名称',
        align: 'center'
    }, {
        field: 'name',
        title: '导购姓名',
        align: 'center',

        events: {
            'click .scan': function (e, value, row) {
                showSeller(row.empId, value, row.mobile, row.storeType, row.storeCode, row.balance);
            }
        },
        formatter: function (value) {
            if (null == value) {
                return '<a class="scan" href="#">' + '未知' + '</a>';
            } else {
                return '<a class="scan" href="#' + value + '">' + value + '</a>';
            }
        }
    }, {
        field: 'mobile',
        title: '导购电话',
        align: 'center',
        visible: false
    }, {
        field: 'loginName',
        title: '导购登录名',
        align: 'center'
    }, {
        field: 'storeType',
        title: '门店类型',
        align: 'center',
        visible: false
    }, {
        field: 'storeCode',
        title: '门店编码',
        align: 'center',
        visible: false
    }, {
        field: 'balance',
        title: '门店预存款',
        align: 'center',
        formatter: function (value) {
            if (null == value) {
                return 0;
            } else {
                return value;
            }
        },
        visible: false
    }
    ]);
}


function openCustomerModal() {
    //查询顾客列表
    initCustomer('/rest/customers/select/customer');
    $("#customerModalConfirm").unbind('click').click(function () {
    });
    $('#selectCustomer').modal('show');
    //清空赠品信息
    document.getElementById('giftMessage').innerHTML = "";
    document.getElementById('subAmount_div').innerHTML = "";
}

//条件查询顾客
function findCustomerByNameOrMobil() {
    var customerQueryConditions = $("#customerQueryConditions").val();
    $("#customerDataGrid").bootstrapTable('destroy');
    if (null == customerQueryConditions || "" == customerQueryConditions) {
        initCustomer('/rest/customers/select/customer');
    } else {
        initCustomer('/rest/customers/select/customer/' + customerQueryConditions);
    }
}

//初始化顾客选择框
function initCustomer(url) {
    var cityId = $('#cityId').val();
    var storeId = $('#storeId').val();
    $("#customerDataGrid").bootstrapTable('destroy');
    $grid.init($('#customerDataGrid'), $('#customerToolbar'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search
        }
    }, [{
        checkbox: true,
        title: '选择'
    }, {
        field: 'cusId',
        title: 'ID',
        align: 'center'
    }, {
        field: 'name',
        title: '顾客姓名',
        align: 'center',
        events: {
            'click .scan': function (e, value, row) {
                showCustomer(row.cusId, value, row.mobile, row.customerType);
            }
        },
        formatter: function (value) {
            if (null == value) {
                return '<a class="scan" href="#">' + '未知' + '</a>';
            } else {
                return '<a class="scan" href="#' + value + '">' + value + '</a>';
            }
        }
    }, {
        field: 'mobile',
        title: '顾客电话',
        align: 'center'
    }, {
        field: 'customerType',
        title: '顾客类型',
        align: 'center',
        visible: false
    }
    ]);
}

//显示选中的导购
function showSeller(id, name, phone, storeType, storeCode, balance) {
    $('#sellerId').text(id);
    $('#sellerName').text(name);
    $('#sellerPhone').text(phone);
    $('#storeType').text(storeType);
    $('#storeCode').text(storeCode);
    $('#balance').text(balance);
    $('#selectSeller').modal('hide');
}

//显示选中的顾客
function showCustomer(id, name, phone, type) {
    $('#customerId').text(id);
    $('#customerName').text(name);
    $('#customerPhone').text(phone);
    $('#customerType').text(type);
    $('#selectCustomer').modal('hide');
}

function initSelect(select, optionName) {
    $(select).empty();
    var selectOption = "<option value=-1>" + optionName + "</option>";
    $(select).append(selectOption);
}
/**
 * 检查商品详情-查看赠品
 */
function openGiftsModal() {
    //关闭支付部分
    $("#goPay").hide();
    $("#goPayType").val(1);
    var price = 0.00;
    //初始化账单明细
    $("#totalGoodsPrice").text(price.toFixed(2))
    $("#vipDiscount").text(price.toFixed(2))
    $("#promotionsDiscount").text(price.toFixed(2))
    $("#amountsPayable").text(price.toFixed(2))

    document.getElementById('giftMessage').innerHTML = "";
    document.getElementById('subAmount_div').innerHTML = "";

    var sellerId = $('#sellerId').text();
    var customerId = $('#customerId').text();
    //检查商品添加详情
    var goodsDetails = new Array();
    var c = cheackGoodsDetail(goodsDetails, 'selectedGoodsTable');
    if (goodsDetails.length == 0) {
        $notify.danger("请选择本品");
        return false;
    }
    if (c == 1) {
        return false;
    }
    if (sellerId == 0) {
        $notify.danger("请选择导购");
        return false;
    }
    if (customerId == 0) {
        $notify.danger("请选择顾客");
        return false;
    }

    var data = {};
    var url = '/rest/goods/page/gifts';
    data["goodsDetails"] = JSON.stringify(goodsDetails);
    data["sellerId"] = sellerId;
    data["customerId"] = customerId;

    $("#giftMessage").html("");
    $http.POST(url, data, function (result) {
        var title = "";
        if (0 === result.code) {
            var promotionsListResponse = result.content;
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
                        "<span id='promotionDiscountTitle' style='padding-left: 5px'>" + promotionDiscountList[i].promotionTitle + "</span>" +
                        "<b style='padding-left: 150px'>参与此促销次数:</b>" +
                        "<span id='promotionDiscountenjoyTimesQty' style='padding-left: 5px'>" + promotionDiscountList[i].enjoyTimes + "</span>" +
                        "<b style='padding-left: 150px'>优惠金额:</b>" +
                        "<span id='discountPrice' style='padding-left: 5px'>" + promotionDiscountList[i].discountPrice + "</span>" +
                        "<div name='aa' id='aa'>" +
                        "<td><input id='promotionDiscountId' type='hidden' value='" + promotionDiscountList[i].promotionId + "'></td>" +
                        "<td><input id='promotionDiscountPrice' type='hidden' value='" + promotionDiscountList[i].discountPrice + "'></td>" +
                        "<td><input id='promotionDiscountenjoyTimes' type='hidden' value='" + promotionDiscountList[i].enjoyTimes + "'></td>" +
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

            //修改选择商品按钮文字
            var currentBtn1 = document.getElementById("selectGoods");
            var currentBtn2 = document.getElementById("updateGoods");
            currentBtn1.style.display = "none";
            currentBtn2.style.display = "block";

            //赠品促销标题
        } else {
            $notify.danger(result.message);
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
        }
    });
}

/**
 * 检查商品详情
 */
function cheackGoodsDetail(details, tableId) {
    var validateFlag = 0;
    //商品sku
    var goodsSkus = new Array();

//        //商品零售价总额
//        var totalPrice = 0;
//        //商品会员价总额
//        var totalVipPrice = 0;

    var trs = $("#" + tableId).find("tr");

    var goodsSku;
    //数量正则
    var re = /^[0-9]+.?[0-9]*$/;

    trs.each(function (i, n) {
        var id = $(n).find("#gid").val();
        goodsSku = $(n).find("#sku").val();
        if ($.inArray(goodsSku, goodsSkus) >= 0) {
            goodRepeatFlag = true;
            validateFlag = 1;
            $notify.warning("亲，【" + goodsSku + "】重复，请删除！");
            return validateFlag;
        }
        var num = $(n).find("#qty").val();
        if (num == '' || num == 0 || !re.test(num)) {
            validateFlag = 1;
            $notify.warning("亲，本品【" + goodsSku + "】数量不正确");
            return validateFlag;
        }
//            totalPrice += $(n).find("#retailPrice").val() * num;
//            totalVipPrice += $(n).find("#vipPrice").val() * num;

//            $("#totalGoodsPrice").text(totalPrice.toFixed(2));
//            $("#vipDiscount").text((totalVipPrice - totalPrice).toFixed(2));

        goodsSkus.push(goodsSku);
        details.push({
            gid: id,
            qty: $(n).find("#qty").val(),
            sku: goodsSku,
            goodsTitile: $(n).find("#title").val(),
            vipPrice: $(n).find("#vipPrice").val(),
            retailPrice: $(n).find("#retailPrice").val()
        });
    });
    return validateFlag;
}
//选择商品
function openGoodsModal(id) {
    //关闭支付部分
    $("#goPay").hide();
    $("#goPayType").val(1);
    var price = 0.00;
    //初始化账单明细
    $("#totalGoodsPrice").text(price.toFixed(2))
    $("#vipDiscount").text(price.toFixed(2))
    $("#promotionsDiscount").text(price.toFixed(2))
    $("#amountsPayable").text(price.toFixed(2))
    //清空赠品信息
    document.getElementById('giftMessage').innerHTML = "";
    document.getElementById('subAmount_div').innerHTML = "";

    //开启商品操作
    $("#selectedGoodsTable").find("input,button,textarea,select").removeAttr("readOnly", "readOnly");
    $("#selectedGoodsTable").find("a").attr("onclick", "del_goods_comb(this)");

    var storeId = $('#storeId').val();

    if (-1 == storeId) {
        $notify.warning("请先选择门店");
        return false;
    }
    //初始化商品信息
    initGoodsGrid("/rest/goods/page/grid/buy/coupon?storeId=" + storeId);

    $("#goodsModalConfirm").unbind('click').click(function () {
        chooseGoods(id);
    });
    $('#goodsModal').modal('show');
}

function chooseGoods(tableId) {
    var tableData = $('#goodsDataGrid').bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else {
        //alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++) {
            var item = tableData[i];

            // 排除已选项
            var trs = $("#" + tableId).find("tr");
            var flag = true;
            trs.each(function (i, n) {
                var id = $(n).find("#gid").val();
                if (id == item.id) {
                    flag = false;
                    return false;
                }
            })

            // 此商品未添加过
            if (flag) {
                str += "<tr>" +
                    "<td><input type='text' id='gid'value=" + item.gid + " style='width:90%;border: none;' readonly /></td>" +
                    "<td><input id='sku' type='text' value='" + item.sku + "' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='title' type='text' value='" + item.skuName + "' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='retailPrice' type='number' value='" + item.retailPrice + "' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='vipPrice' type='number' value='" + item.vipPrice + "' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='qty' type='number' value='0'></td>" +
                    "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                    "</tr>"
            }

        }
        $("#" + tableId).append(str);

        // 取消所以选中行
        $('#goodsDataGrid').bootstrapTable("uncheckAll");
    }
}


//保存买券订单
function save() {
    $loading.show();
    var storeCode = $('#storeCode').text();
    var storeType = $('#storeType').text();
    var cashMoney = $('#cashMoney').val();
    var posMoney = $('#posMoney').val();
    var posNumber = $('#posNumber').val();
    var otherMoney = $('#otherMoney').val();
    var collectMoneyTime = $('#collectMoneyTime').val();
    var remarks = $('#remarks').val();
    var sellerId = $('#sellerId').text();
    var customerId = $('#customerId').text();
    var selectPaymnet = $('#selectPaymnet').val();
    var vipDiscount = $('#vipDiscount').text();
    var promotionsDiscount = $('#promotionsDiscount').text();

    var salesNumber = $('#salesNumber').val();
    if ('' == sellerId || null == sellerId) {
        $loading.close();
        $notify.warning("请选择导购");
        return;
    }
    if ('' == customerId || null == customerId) {
        $loading.close();
        $notify.warning("请选择顾客");
        return;
    }

    //获取当前时间
    var myDate = new Date();
    //获取当前小时
    var h = myDate.getHours();


    if ('FZY009' == storeCode || 'HLC004' == storeCode || 'ML001' == storeCode || 'QCMJ008' == storeCode || 'SB010' == storeCode
        || 'YC002' == storeCode || 'ZC002' == storeCode || 'RC005' == storeCode || 'FZM007' == storeCode || 'SH001' == storeCode
        || 'YJ001' == storeCode || 'HS001' == storeCode || 'XC001' == storeCode) {
        if (h < 6 || h > 19) {
            $loading.close();
            $notify.warning("成都直营门店此时间段不能购买产品券");
            return;
        }
    }
    var cityId = $('#cityId').val();

    if (-1 == cityId) {
        $loading.close();
        $notify.warning("请先选择城市");
        return;
    }

    if (1 == cityId && 'ZY' == storeType && ('' == salesNumber || null == salesNumber)) {
        $loading.close();
        $notify.warning("请填写销售纸质单号");
        return;

    }

    var totalMoneys = (Number(cashMoney) * 100 + Number(posMoney) * 100 + Number(otherMoney) * 100) / 100;

    var availableMoney = $('#availableMoney').val();
    var preDepositMoney = $('#preDepositMoney').val();
    var preDepositCollectMoneyTime = $('#preDepositCollectMoneyTime').val();
    var preDepositRemarks = $('#preDepositRemarks').val();

    var totalMoney = 0;

    if (selectPaymnet == -1) {
        $loading.close();
        $notify.warning("请选择支付方式！");
        return;
    }

    if (selectPaymnet == 'offlinePayments') {
        totalMoney = $('#totalMoney').val();
        if (null == collectMoneyTime) {
            $loading.close();
            $notify.warning("请填写收款时间！");
            return;
        }
        if (posMoney > 0) {
            if (null == posNumber || '' == posNumber) {
                $loading.close();
                $notify.warning("请输入POS流水号后六位！");
                return;
            }
        }
        if (cashMoney < 0) {
            if ((Number(cashMoney) * 100 + Number(posMoney) * 100) / 100 <= 0) {
                $loading.close();
                $notify.warning("当现金金额为负数时，POS金额+现金金额必须大于0");
                return;
            }
        }
        if (null != posNumber && '' != posNumber) {
            if ('' == posMoney || null == posMoney) {
                $loading.close();
                $notify.warning("请输入POS流水号对应的POS金额！");
                return;
            }
        }
        if (Number(totalMoney) != Number(totalMoneys)) {
            $loading.close();
            $notify.warning("现金、POS、其他收款总和与收款金额不相等，请检查！");
            return;
        }
    } else {
        totalMoney = Number(preDepositMoney);
        if (Number(availableMoney) < Number(preDepositMoney)) {
            $loading.close();
            $notify.warning("使用预存款金额大于可使用金额，请检查！");
            return;
        }

    }


    //检查商品添加详情
    var goodsDetails = new Array();
    var a = goodsAndPriceDetail(goodsDetails, 'selectedGoodsTable', totalMoney);
    if (goodsDetails.length == 0) {
        $loading.close();
        $notify.danger("请选择本品");
        return false;
    }
    if (a == 1) {
        $loading.close();
        return;
    }


    //获取赠品详情
    var giftDetails = new Array();
    var b = giftDetail(giftDetails, 'giftMessage');
    if (b == 1) {
        $loading.close();
        return;
    }
    var datas = {};


    datas["cashMoney"] = cashMoney;
    datas["posMoney"] = posMoney;
    datas["posNumber"] = posNumber;
    datas["collectMoneyTime"] = collectMoneyTime;
    datas["otherMoney"] = otherMoney;
    datas["remarks"] = remarks;
    datas["totalMoney"] = totalMoney;
    datas["preDepositMoney"] = preDepositMoney;
    datas["preDepositCollectMoneyTime"] = preDepositCollectMoneyTime;
    datas["preDepositRemarks"] = preDepositRemarks;
    datas["salesNumber"] = salesNumber;
    datas["memberDiscount"] = vipDiscount;
    datas["promotionDiscount"] = promotionsDiscount;

    datas["goodsDetails"] = JSON.stringify(goodsDetails);
    datas["giftDetails"] = JSON.stringify(giftDetails);
    datas["sellerId"] = sellerId;
    datas["customerId"] = customerId;

    $.ajax({
        url: '/rest/order/save/productCoupon',
        method: 'POST',
        data: datas,
        error: function () {
            clearTimeout($global.timer);
            $loading.close();
            $global.timer = null;
            $notify.danger('网络异常，请稍后重试或联系管理员');
        },
        success: function (result) {
            if (result.code === 0) {
                $loading.close();
                window.location.href = "/views/admin/order/buy/produtCoupon";
                $notify.info(result.message);
            } else {
                $loading.close();
                $notify.danger(result.message);
            }
        }
    });
}
//提交保存验证本品与金额
function goodsAndPriceDetail(details, tableId, totalMoney) {
    var cashMoney = $('#cashMoney').val();
    var posMoney = $('#posMoney').val();
    var posNumber = $('#posNumber').val();
    var otherMoney = $('#otherMoney').val();
    var collectMoneyTime = $('#collectMoneyTime').val();
    var customerType = $('#customerType').text();
    var subAmount = $("#subAmount").val();

    if ('' == customerType) {
        $notify.warning("顾客类型未知，请联系管理员！");
        return 1;
    }
    //零售应付总金额
    var retailTotalMoney = 0;
    //会员应付总金额
    var memberTotalMoney = 0;
    var validateFlag = true;
    //商品sku
    var goodsSkus = new Array();
    var trs = $("#" + tableId).find("tr");
    var goodsSku;
    //数量正则
    var re = /^[0-9]+.?[0-9]*$/;
    trs.each(function (i, n) {
        var id = $(n).find("#gid").val();
        goodsSku = $(n).find("#sku").val();
        if ($.inArray(goodsSku, goodsSkus) >= 0) {
            goodRepeatFlag = true;
            validateFlag = false;
            $notify.warning("亲，【" + goodsSku + "】重复，请删除！");
            return 1;
        }
        var num = $(n).find("#qty").val();
        if (num == '' || num == 0 || !re.test(num)) {
            validateFlag = false;
            $notify.warning("亲，本品【" + goodsSku + "】数量不正确");
            return 1;
        }
        var retailMoney = $(n).find("#retailPrice").val();
        var memberMoney = $(n).find("#vipPrice").val();
        if ('RETAIL' == customerType) {
            retailTotalMoney += (Number(retailMoney) * 100 * Number(num)) / 100;
        } else if ('MEMBER' == customerType) {
            memberTotalMoney += (Number(memberMoney) * 100 * Number(num)) / 100;
        }
        goodsSkus.push(goodsSku);
        details.push({
            gid: id,
            qty: $(n).find("#qty").val(),
            sku: goodsSku,
            goodsTitile: $(n).find("#title").val(),
            vipPrice: $(n).find("#vipPrice").val(),
            retailPrice: $(n).find("#retailPrice").val()
        });
    });


    if ('RETAIL' == customerType) {
        if (null == subAmount) {
            if (Number(retailTotalMoney) != Number(totalMoney)) {
                $notify.warning("应付金额与收款金额不等，请检查！");
                return 1;
            }
        } else {
            var mon = (Number(retailTotalMoney) * 100 - Number(subAmount) * 100) / 100;
            if (Number(mon) != Number(totalMoney)) {
                $notify.warning("应付金额与收款金额不等，请检查！");
                return 1;
            }
        }
    } else if ('MEMBER' == customerType) {
        if (null == subAmount) {
            if (Number(memberTotalMoney) != Number(totalMoney)) {
                $notify.warning("应付金额与收款金额不等，请检查！");
                return 1;
            }
        } else {
            var mone = (Number(memberTotalMoney) * 100 - Number(subAmount) * 100) / 100;
            if (Number(mone) != Number(totalMoney)) {
                $notify.warning("应付金额与收款金额不等，请检查！");
                return 1;
            }
        }
    }
    return 0;
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


//模糊查询商品信息
function findGoodsByNameOrCode() {
    var storeId = $('#storeId').val();
    var cusId = $("#customerId").html();
    var sellerId = $("#sellerId").html();

    if (-1 == storeId) {
        $notify.warning("请先选择门店");
        return false;
    }
    var queryGoodsInfo = $("#queryGoodsInfo").val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    // if (null == queryGoodsInfo || "" == queryGoodsInfo) {
    //     //初始化商品信息
    //     initGoodsGrid("/rest/goods/page/grid/" + storeId);
    // } else {
    //     initGoodsGrid('/rest/goods/page/query/goodsInfo?queryGoodsInfo=' + queryGoodsInfo + '&storeId=' + storeId);
    // }
    initGoodsGrid('/rest/goods/page/grid/buy/coupon?storeId=' + storeId + '&cusId=' + cusId + '&keywords=' + queryGoodsInfo + '&sellerId=' + sellerId);
}


//删除商品节点
function del_goods_comb(obj) {
    var deleteGoodsId = $(obj).parent().parent().find("#gid").val();
    $(obj).parent().parent().remove();
}

//失去焦点验证收款信息
function priceBlur(id) {
    var selectPaymnet = $('#selectPaymnet').val();

    if (selectPaymnet == -1) {
        $notify.warning("请选择支付方式！");
        document.getElementById(id).value = 0.00;
        return;
    }

    var price = document.getElementById(id).value;

    if ('posMoney' == id || 'otherMoney' == id || 'totalMoney' == id || 'preDepositMoney' == id) {
        if (price < 0) {
            $notify.warning("此处金额不能为负数，请正确填写！");
            document.getElementById(id).value = 0.00;
            return false;
        }
    }
    if (price.toString().indexOf(".") > 0 && Number(price.toString().split(".")[1].length) > 2) {
        $notify.warning("请输入正确金额，小数点后只能保留2位小数！");
        document.getElementById(id).value = 0.00;
        return false;
    }
    var selectPaymnet = $("#selectPaymnet").val();
    if (selectPaymnet == 'offlinePayments') {
        var posMoney = $("#posMoney").val();
        var otherMoney = $("#otherMoney").val();
        var cashMoney = $("#cashMoney").val();

        var totalMoney = (Number(posMoney) * 100 + Number(otherMoney) * 100 + Number(cashMoney) * 100) / 100;
        document.getElementById("totalMoney").value = totalMoney.toFixed(2);
    }


}

//展开支付模块
function openGoPay() {

    var storeType = $('#storeType').text();
    var sellerId = $('#sellerId').text();
    var customerType = $('#customerType').text();

    if ('' == customerType) {
        $notify.warning("顾客类型未知，请联系管理员！");
        return;
    }

    if ('' == sellerId || null == sellerId) {
        $notify.warning("请选择导购");
        return;
    }
    var cityId = $('#cityId').val();

    if (-1 == cityId) {
        $notify.warning("请先选择城市");
        return;
    }
    //判断是否显示销售纸质单号输入框

    if (1 == cityId && 'ZY' == storeType) {
        document.getElementById("salesNumTitle").style.display = "block";
        document.getElementById("salesNumber").style.display = "block";
    }

    //锁定input输入款与a标签按钮
    $("#selectedGoodsTable").find("input,button,textarea,select").attr("readOnly", "readOnly");
    $("#selectedGoodsTable").find("a").removeAttr("onclick");

    //改变选择商品按钮
    var currentBtn1 = document.getElementById("selectGoods");
    var currentBtn2 = document.getElementById("updateGoods");
    currentBtn1.style.display = "none";
    currentBtn2.style.display = "block";

    //获取商品信息
    //商品sku
    var goodsSkus = new Array();
    //商品零售价总额
    var totalPrice = 0;
    //商品会员价总额
    var totalVipPrice = 0;
    var trs = $("#selectedGoodsTable").find("tr");
    if (trs.length == 0) {
        $notify.warning("请选择商品！");
        return;
    }
    var goodsSku;
    //数量正则
    var re = /^[0-9]+.?[0-9]*$/;
    trs.each(function (i, n) {
        var id = $(n).find("#gid").val();
        goodsSku = $(n).find("#sku").val();
        if ($.inArray(goodsSku, goodsSkus) >= 0) {
            goodRepeatFlag = true;

            $notify.warning("亲，【" + goodsSku + "】重复，请删除！");
            return;
        }
        var num = $(n).find("#qty").val();
        if (num == '' || num == 0 || !re.test(num)) {
            $notify.warning("亲，本品【" + goodsSku + "】数量不正确");
            return;
        }
        totalPrice += ($(n).find("#retailPrice").val() * 100 * num) / 100;
        totalVipPrice += ($(n).find("#vipPrice").val() * 100 * num) / 100;

        $("#totalGoodsPrice").text(totalPrice.toFixed(2));
        $("#vipDiscount").text(((totalVipPrice * 100 - totalPrice * 100) / 100).toFixed(2));
    });
    var amountsPayable1 = 0.00;
    var subAmount = $("#subAmount").val();

    if ('RETAIL' == customerType) {
        $("#vipDiscount").text('0.00');
        if (null == subAmount) {
            //设置应付金额
            $("#amountsPayable").text(totalPrice.toFixed(2))
        } else {
            amountsPayable1 = (Number(totalPrice) * 100 - Number(subAmount) * 100) / 100;
            //设置应付金额
            $("#amountsPayable").text(amountsPayable1.toFixed(2))
            //设置促销折扣
            $("#promotionsDiscount").text(((0 - Number(subAmount) * 100) / 100).toFixed(2))
        }

    } else if ('MEMBER' == customerType) {
        if (null == subAmount) {
            //设置应付金额
            $("#amountsPayable").text(totalVipPrice.toFixed(2))
        } else {
            amountsPayable1 = (Number(totalVipPrice) * 100 - Number(subAmount) * 100) / 100;
            //设置应付金额
            $("#amountsPayable").text(amountsPayable1.toFixed(2))
            //设置促销折扣
            $("#promotionsDiscount").text(((0 - Number(subAmount) * 100) / 100).toFixed(2))
        }
    }


    $("#goPay").show();
    $("#goPayType").val(0);
}

//验证销售纸质单号
function verificationSalesNumber() {
    var salesNumber = $('#salesNumber').val();
    var re = /^[0-9a-zA-Z]*$/g;
    if (!re.test(salesNumber)) {
        $("#salesNumber").val("")
        $notify.warning("只能输入数字及字母");
        return;
    }
}

//选择支付方式
function selectPaymnets(id) {
    var storeType = $('#storeType').text();
    var balance = $('#balance').text();
    var preDeposit = 0;
    if (null != balance && '' != balance) {
        preDeposit = balance;
    }
    if ('offlinePayments' == id) {
        $("#preDeposit :input").each(function () {
            $(this).val("");
        });
        $("#preDeposit").hide();
        $("#offlinePayments").show();
    } else if ('preDeposit' == id) {
        if ('ZY' != storeType) {
            $notify.warning("此支付方式只有直营门店可用使用！");
            document.getElementById("selectPaymnet").options.selectedIndex = 0; //回到初始状态
            $("#selectPaymnet").selectpicker('refresh');//对selectPaymnet这个下拉框进行重置刷新

            return;
        }
        $("#offlinePayments :input").each(function () {
            $(this).val("");
        });
        document.getElementById("availableMoney").value = preDeposit;
        $("#offlinePayments").hide();
        $("#preDeposit").show();
    }
}


function storeChangeRefresh() {
    //关闭支付部分
    $("#goPay").hide();
    $("#goPayType").val(1);
    var price = 0.00;
    //初始化账单明细
    $("#totalGoodsPrice").text(price.toFixed(2))
    $("#vipDiscount").text(price.toFixed(2))
    $("#promotionsDiscount").text(price.toFixed(2))
    $("#amountsPayable").text(price.toFixed(2))
    //清空赠品信息
    document.getElementById('giftMessage').innerHTML = "";
    document.getElementById('subAmount_div').innerHTML = "";
}