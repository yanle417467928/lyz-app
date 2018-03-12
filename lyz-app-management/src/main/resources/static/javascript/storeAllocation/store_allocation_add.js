$(function () {
    //表单验证初始化
    formValidate();

    var storeId = $('#storeCode').val();
    // 初始化商品弹出框
    $commonForm.goodsGridModal("/rest/goods/page/grid/param?storeId="+storeId, "goodsDataGrid", "selectedGoodsTable", "chooseGoodsButton");
    $commonForm.goodsBrand("/rest/goodsBrand/page/brandGrid", "brandCode");
    $commonForm.goodsCategory("/rest/goods/page/physicalClassifyGrid", "categoryCode");

    $(".select").each(function () {
        $(this).selectpicker('refresh');
    })
})

/**
 * 表单验证
 */
function formValidate() {
    /**
     * 表单验证器初始化
     */
    $('#allocation_form').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            comment: {
                message: '校验失败',
                validators: {

                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '备注在30个字以内'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        //检查门店
        var storeId = $("#storeCode").val();

        if (storeId == -1){
            $('#allocation_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("请选择门店啊，亲");
            return false;
        }

        //检查商品添加详情
        var goodsDetails = new Array();
        var checkFlag = cheackGoodsDetail(goodsDetails, 'selectedGoodsTable');
        if (!checkFlag) {
            $('#allocation_form').bootstrapValidator('disableSubmitButtons', false);
            return false;
        }
        if (goodsDetails.length == 0) {
            $('#allocation_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("请选择商品啊，亲");
            return false;
        }

        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        var url = '/rest/store/allocation/save';

        $.each(origin, function () {
            data[this.name] = this.value;
        });

        data["goodsList"] = JSON.stringify(goodsDetails);
        $http.POST(url, data, function (result) {
            if (0 === result.code) {
                $notify.info(result.message);
                window.location.href = "/views/admin/inventory/allocation";
            } else {
                $notify.danger(result.message);
                $('#allocation_form').bootstrapValidator('disableSubmitButtons', false);
            }
        });
    });

    $('#btn-cancel').on('click', function () {
        history.go(-1);
    });
}

function findStorelist() {
    var store = "";
    $.ajax({
        url: '/rest/stores/findStorelist',
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
            $("#storeCode").append(store);
            $('#storeCode').selectpicker('refresh');
            $('#storeCode').selectpicker('render');

        }
    });
}

function screenGoods() {
    var storeCode = $('#storeCode').val();

    if (storeCode == -1 || storeCode == undefined){
        $notify.danger('请选择门店！');
    }

    var queryGoodsInfo = $("#queryGoodsInfo").val();
    var brandCode = $('#brandCode').val();
    var categoryCode = $('#categoryCode').val();
    var companyCode = $('#companyCode').val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    initGoodsGrid('/rest/goods/page/grid/param?keywords='+queryGoodsInfo+'&brandCode=' + brandCode
                                                   + '&categoryCode=' + categoryCode
                                                   + '&companyCode=' + companyCode+'&storeId='+storeCode, 'goodsDataGrid');

}

function findGoodsByNameOrCode() {
    var storeCode = $('#storeCode').val();

    if (storeCode == -1 || storeCode == undefined){
        $notify.danger('请选择门店！');
    }

    var queryGoodsInfo = $("#queryGoodsInfo").val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    if (null == queryGoodsInfo || "" == queryGoodsInfo) {
        initGoodsGrid('/rest/goods/page/grid/param?keywords='+queryGoodsInfo+'&storeId='+storeCode,'goodsDataGrid');
    } else {
        initGoodsGrid('/rest/goods/page/grid/param?keywords='+ queryGoodsInfo+'&storeId='+storeCode,"goodsDataGrid");
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
    //数量正则
    var re = /^[0-9]+.?[0-9]*$/;

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
            var num = $(n).find("#qty").val();
            if(num=='' || num == 0 ||!re.test(num)) {
                validateFlag = false;
                $notify.warning("亲，商品"+goodsSku+"数量不正确");
                return false;
            }
            details.push({
                goodsId:id,
                qty: $(n).find("#qty").val(),
                sku: goodsSku,
                skuName: $(n).find("#title").val()
            });
        }

    });
    return validateFlag;
}