$(function () {

    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    })

    // 初始化商品弹出框
    initGoodsGrid("/rest/goods/page/grid", "goodsDataGrid");
    $("#goodsModalConfirm").unbind('click').click(function () {
        chooseOneGoods("goodsDataGrid", "selectedGoodsTable");
    });
    $("#chooseGoodsButton").on('click', function () {
        $("#goodsModal").modal('show');
    });
    $commonForm.goodsBrand("/rest/goodsBrand/page/brandGrid","brandCode");
    $commonForm.goodsCategory("/rest/goods/page/physicalClassifyGrid","categoryCode");

    $('.switch').bootstrapSwitch();

    $("select").each(function () {
        $(this).selectpicker('refresh');
    })

    // 表单验证
    formValidate();
})

/**
 * 表单验证
 */
function formValidate() {
    /**
     * 表单验证器初始化
     */
    $('#delliveryFeeRule_form').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {

            condition: {
                validators: {
                    notEmpty: {
                        message: '亲，最低金额都没填'
                    },
                    regexp: {
                        regexp: /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/,
                        message: "最低金额格式不正确"
                    }
                }
            },
            deliveryFee: {
                validators: {
                    notEmpty: {
                        message: '亲，运费金额都没填'
                    },
                    regexp: {
                        regexp: /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/,
                        message: "运费金额格式不正确"
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {

        // 目标对象
        var target = "";
        $("input:checkbox[name='target']:checked").each(function (i) {
            if (i == 0) {
                target += $(this).val();
            } else {
                target += "," + $(this).val();
            }

        })
        if (target == "") {
            $notify.danger('请选择目标对象');
            return false;
        }

        // 特殊商品验证
        var includeSpecialGoods = $("#includeSpecialGoods").prop("checked");
        var goodsDetails = new Array();
        if (includeSpecialGoods == true) {
            var trs = $("#selectedGoodsTable").find("tr");
            //检查商品添加详情
            if (trs.length == 0) {
                $('#delliveryFeeRule_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择商品啊，亲");
                return false;
            }
            var checkFlag = cheackGoodsDetail(goodsDetails,'selectedGoodsTable');
            if (!checkFlag) {
                $('#cashCoupon_form').bootstrapValidator('disableSubmitButtons', false);
                return false;
            }
        }

        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        var url = '/rest/deliveryFeeRule/edit';

        $.each(origin, function () {
            data[this.name] = this.value;
        });

        data["tollObject"] = target;
        data["goodsDetails"] = JSON.stringify(goodsDetails);
        data["includeSpecialGoods"] = includeSpecialGoods;

        $http.POST(url, data, function (result) {
            if (0 === result.code) {
                $notify.info(result.message);
                window.location.href = "/view/deliveryFeeRule/page";
            } else {
                $notify.danger(result.message);
                $('#delliveryFeeRule_form').bootstrapValidator('disableSubmitButtons', false);
            }
        });
    });

    $('#btn-cancel').on('click', function () {
        history.go(-1);
    });
}

function AddSpecialGoods() {
    var val = $("#includeSpecialGoods").prop("checked");
    if (val == true) {
        $("#goods_div").fadeIn();
    } else {
        $("#goods_div").fadeOut(1000);
    }
}

function screenGoods() {
    var brandCode = $('#brandCode').val();
    var categoryCode = $('#categoryCode').val();
    var companyCode = $('#companyCode').val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    initGoodsGrid('/rest/goods/page/screenGoodsGrid?brandCode=' + brandCode + '&categoryCode=' + categoryCode + '&companyCode=' + companyCode, "goodsDataGrid");
}

function chooseOneGoods(goodsGridID, tableId) {
    var tableData = $('#' + goodsGridID).bootstrapTable('getSelections');

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
                    "<td><input type='text' id='gid'value=" + item.id + " style='width:90%;border: none;' readonly /></td>" +
                    "<td><input id='sku' type='text' value='" + item.sku + "' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='title' type='text' value='" + item.skuName + "' style='width:90%;border: none;' readonly></td>" +
                    "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                    "</tr>"
            }

        }
        $("#" + tableId).append(str);

        // 取消所有选中行
        $('#' + goodsGridID).bootstrapTable("uncheckAll");
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