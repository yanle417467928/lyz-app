$(function () {
    //表单验证初始化
    formValidate();

    // 初始化时间控件
    $startTimeAndEndTime.datatimePiker("effectiveStartTime", "effectiveEndTime", "productCoupon_form");

    // 初始话商品选择框
    initGoodsGrid("/rest/goods/page/grid", "goodsDataGrid");
    $("#goodsModalConfirm").unbind('click').click(function () {
        chooseOneGoods("goodsDataGrid", "selectedGoodsTable");
    });

    $("#chooseGoodsButton").on('click', function () {
        $("#goodsModal").modal('show');
    })

    $commonForm.goodsBrand("/rest/goodsBrand/page/brandGrid","brandCode");
    $commonForm.goodsCategory("/rest/goods/page/physicalClassifyGrid","categoryCode");

    $("select").each(function () {
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
    $('#productCoupon_form').bootstrapValidator({
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
                        message: '亲，请选择产品'
                    },
                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '标题在30个字以内'
                    }
                }
            },
            description: {
                validators: {
                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '描述在30个字以内'
                    }
                }
            },
            effectiveStartTime: {
                validators: {
                    notEmpty: {
                        message: '亲，请选择有效期开始时间'
                    }
                }
            },
            effectiveEndTime: {
                validators: {
                    notEmpty: {
                        message: '亲，请选择有效期结束时间'
                    }
                }
            },

            // denomination: {
            //     validators: {
            //         notEmpty: {
            //             message: '亲，面额都没填'
            //         }
            //     }
            // },
            initialQuantity: {
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

        var trs = $("#selectedGoodsTable").find("tr");
        //检查商品添加详情
        if (trs.length == 0) {
            $('#productCoupon_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("请选择商品啊，亲");
            return false;
        }

        //单价正则
        // var reg = /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/;
        //
        // var denomination = $("#denomination").val();
        //
        // if (!reg.test(denomination)) {
        //     $('#productCoupon_form').bootstrapValidator('disableSubmitButtons', false);
        //     $notify.danger("现金券面额有误啊，亲");
        //     return false;
        // }

        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        var url = '/rest/productCoupon/edit';

        $.each(origin, function () {
            data[this.name] = this.value;
        });

        $http.PUT(url, data, function (result) {
            if (0 === result.code) {
                $notify.info(result.message);
                window.location.href = "/view/productCoupon/page";
            } else {
                $notify.danger(result.message);
                $('#productCoupon_form').bootstrapValidator('disableSubmitButtons', false);
            }
        });
    });

    $('#btn-cancel').on('click', function () {
        history.go(-1);
    });
}

function chooseOneGoods(goodsGridID, tableId) {
    var tableData = $('#' + goodsGridID).bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else if (tableData.length > 1) {
        $notify.warning('只能选择一件商品');
    } else {
        //alert(tableData);
        var str = "";

        var item = tableData[0];

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
                "<td><input type='text' id='gid' name='gid' value=" + item.id + " style='width:90%;border: none;' readonly /></td>" +
                "<td><input id='sku' type='text' value='" + item.sku + "' style='width:90%;border: none;' readonly></td>" +
                "<td><input id='title' type='text' value='" + item.skuName + "' style='width:90%;border: none;' readonly></td>" +
                "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                "</tr>"
        }

        $("#title").val(item.sku + "产品券");
        $('#productCoupon_form').data('bootstrapValidator') .updateStatus("title", 'NOT_VALIDATED',null) .validateField("title");

        $("#" + tableId).html(str);

        // 取消所有选中行
        $('#' + goodsGridID).bootstrapTable("uncheckAll");
        $("#goodsModal").modal('hide');

    }
}