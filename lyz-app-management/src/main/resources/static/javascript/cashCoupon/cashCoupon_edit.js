$(function () {

    // 初始化时间控件
    $startTimeAndEndTime.datatimePiker("effectiveStartTime","effectiveEndTime","cashCoupon_form");

    // 表单元素渲染
    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass   : 'iradio_flat-green'
    })

    // 初始化商品弹出框
    $commonForm.goodsGridModal("/rest/goods/page/grid","goodsDataGrid","selectedGoodsTable","chooseGoodsButton");
    $commonForm.goodsBrand("/rest/goodsBrand/page/brandGrid","brandCode");
    $commonForm.goodsCategory("/rest/goods/page/physicalClassifyGrid","categoryCode");

    $("select").each(function () {
        $(this).selectpicker('refresh');
    })

    // 不限门店按钮绑定事件
    $('#isSpecifiedStore').on('ifChecked', function(event){
        //alert($(this).prop('checked'));
        unlimitedStore();
    });
    $('#isSpecifiedStore').on('ifUnchecked', function(event){
        //alert($(this).prop('checked'));
        unlimitedStore();
    });
})

/**
 * 表单验证
 */
function formValidate() {
    /**
     * 表单验证器初始化
     */
    $('#cashCoupon_form').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            title: {
                message: '主标题校验失败',
                validators: {
                    notEmpty: {
                        message: '亲，优惠券叫个啥玩意儿'
                    },
                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '标题在30个字以内'
                    }
                }
            },
            description:{
                validators: {
                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '描述在30个字以内'
                    }
                }
            },
            effectiveStartTime:{
                validators:{
                    notEmpty:{
                        message: '亲，请选择有效期开始时间'
                    }
                }
            },
            effectiveEndTime:{
                validators:{
                    notEmpty:{
                        message: '亲，请选择有效期结束时间'
                    }
                }
            },
            condition:{
                validators:{
                    notEmpty:{
                        message: '亲，最低金额都没填'
                    }
                }
            },
            denomination:{
                validators:{
                    notEmpty:{
                        message: '亲，面额都没填'
                    }
                }
            },
            initialQuantity:{
                validators: {
                    notEmpty: {
                        message: '初始不能为空'
                    },
                    regexp: {
                        regexp: /^[1-9]([0-9]{0,3})$/,
                        message: '您只能输入一个范围在1~9999之间的整数'
                    }
                }
            },
            sortId: {
                message: '排序号校验失败',
                validators: {
                    notEmpty: {
                        message: '排序号不能为空'
                    },
                    regexp: {
                        regexp: /^[1-9]([0-9]{0,2})$/,
                        message: '您只能输入一个范围在1~999之间的整数'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {

        // 已选门店
        var stores = new Array();
        // 不限门店
        var unlimitedStore = $("#isSpecifiedStore").prop('checked');
        if(unlimitedStore){

        }else{

            $("#stores > label[class='label label-success']").each(function () {
                stores.push(
                    {
                        storeId:$(this).prop("id"),
                        storeName:$(this).html()
                    }

                );
            })
            if(stores.length == 0){
                $notify.danger("请选择门店");
                return false;
            }
        }

        // 优惠券类型
        var type = $("#type").val();

        var companys = $("#companySelect").val();
        var brands = $("#brandSelect").val();
        var goodsDetails = new Array();
        if(type == "GENERAL"){

        }else if (type == "COMPANY"){

            if(companys == null){
                $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择公司啊，亲");
                return false;
            }

        }else if (type == "BRAND"){

            if(brands == null){
                $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择品牌啊，亲");
                return false;
            }

        }else if (type == "GOODS"){
            //检查商品添加详情

            var checkFlag = cheackGoodsDetail(goodsDetails,'selectedGoodsTable');
            if (!checkFlag) {
                $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
                return false;
            }
            if(goodsDetails.length == 0){
                $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择商品啊，亲");
                return false;
            }
        }

        //单价正则
        var reg = /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/;

        var condition = $("#condition").val();
        var denomination = $("#denomination").val();

        if(!reg.test(condition)){
            $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("最低金额有误啊，亲");
            return false;
        }
        if(!reg.test(denomination)){
            $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("优惠券面额有误啊，亲");
            return false;
        }

        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        var url = '/rest/cashCoupon/edit';

        $.each(origin, function () {
            data[this.name] = this.value;
        });

        data["goodsDetails"] = JSON.stringify(goodsDetails);
        data["stores"] = JSON.stringify(stores);
        data["companys"] = companys;
        data["brands"] = brands;
        data["isSpecifiedStore"] = !unlimitedStore;
        $http.POST(url, data, function (result) {
            if (0 === result.code) {
                $notify.info(result.message);
                window.location.href = "/view/cashCoupon/page";
            } else {
                $notify.danger(result.message);
                $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
            }
        });
    });

    $('#btn-cancel').on('click', function() {
        history.go(-1);
    });
}


function screenGoods() {
    var brandCode=$('#brandCode').val();
    var categoryCode=$('#categoryCode').val();
    var companyCode=$('#companyCode').val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    initGoodsGrid('/rest/goods/page/screenGoodsGrid?brandCode=' + brandCode+'&categoryCode='+categoryCode+'&companyCode='+companyCode,"goodsDataGrid");

}

function findGoodsByNameOrCode() {
    var queryGoodsInfo = $("#queryGoodsInfo").val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    if (null == queryGoodsInfo || "" == queryGoodsInfo) {
        initGoodsGrid('/rest/goods/page/grid');
    }else{
        initGoodsGrid('/rest/goods/page/goodsGrid/' + queryGoodsInfo);
    }
}

// 不限门店
function unlimitedStore() {
    var val = $('#isSpecifiedStore').prop("checked");
    if(val){
        $("#stores").fadeOut(1);
    }else{
        $("#stores").fadeIn(1000);
    }
}

// 选择优惠券类型
function changeType(val) {
    if(val == "GENERAL"){
        $("#company_div").fadeOut(1);
        $("#brand_div").fadeOut(1);
        $("#goods_div").fadeOut(1);
    }else if (val == "COMPANY"){
        $("#company_div").fadeIn(1000);
        $("#brand_div").fadeOut(1);
        $("#goods_div").fadeOut(1);

    }else if (val == "BRAND"){
        $("#company_div").fadeOut(1);
        $("#brand_div").fadeIn(1000);
        $("#goods_div").fadeOut(1);
    }else if (val == "GOODS"){
        $("#company_div").fadeOut(1);
        $("#brand_div").fadeOut(1);
        $("#goods_div").fadeIn(1000);
    }
}

/**
 * 检查商品详情
 */
function cheackGoodsDetail(details,tableId){
    var goodRepeatFlag = false;
    var validateFlag = true;

    //商品sku
    var goodsSkus = new Array();
    var trs = $("#"+tableId).find("tr");
    var goodsSku;

    trs.each(function(i,n){
        var id = $(n).find("#gid").val();
        goodsSku = $(n).find("#sku").val();

        if($.inArray(goodsSku, goodsSkus) >= 0) {
            goodRepeatFlag = true;
            validateFlag = false;
            $notify.warning( "亲，【" + goodsSku + "】重复，请删除！");
            return false;
        }
        goodsSkus.push(goodsSku);

        if(tableId == "selectedGoodsTable"){

            details.push({
                goodsId:id,
                qty: $(n).find("#qty").val(),
                sku: goodsSku,
                goodsName: $(n).find("#title").val()
            });
        }


    });
    return validateFlag;
}


