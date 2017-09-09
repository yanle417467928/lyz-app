$(function() {
    if (!$global.validateMobile()) {
        $('.select').selectpicker({
            size: '5'
        });
    }
    $('[name="isOnSale"]').bootstrapSwitch({
        onText: "上架",
        offText: "下架",
        onColor: "primary",
        offColor: "danger",
        // size:"normal",
        onSwitchChange: function (event, state) {
            if (state == true) {
                $(this).val("on");
            } else {
                $(this).val("off");
            }
        }
    });
    $('[name="isGift"]').bootstrapSwitch({
        onText: "是",
        offText: "否",
        onColor: "primary",
        offColor: "danger",
        // size:"normal",
        onSwitchChange: function (event, state) {
            if (state == true) {
                $(this).val("on");
            } else {
                $(this).val("off");
            }
        }
    });
    $('[name="isColorful"]').bootstrapSwitch({
        onText: "是",
        offText: "否",
        onColor: "primary",
        offColor: "danger",
        // size:"normal",
        onSwitchChange: function (event, state) {
            if (state == true) {
                $(this).val("on");
            } else {
                $(this).val("off");
            }
        }
    });
    $('[name="isColorPackage"]').bootstrapSwitch({
        onText: "是",
        offText: "否",
        onColor: "primary",
        offColor: "danger",
        size: "normal",
        onSwitchChange: function (event, state) {
            if (state == true) {
                $(this).val("on");
            } else {
                $(this).val("off");
            }
        }
    });
    $(function () {
        $('[data-toggle="tooltip"]').popover();
    });

    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd 00:00:00',
        language: 'zh-CN',
        autoclose: true
    });

    $('.switch').bootstrapSwitch();

    $('#goodsFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            goodsName: {
                message: '商品名称校验失败',
                validators: {
                    notEmpty: {
                        message: '商品名称不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 50,
                        message: '商品名称的长度必须在2~50位之间'
                    }
                }
            },
            title: {
                message: '商品简称校验失败',
                validators: {
                    notEmpty: {
                        message: '商品简称不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 50,
                        message: '商品简称的长度必须在2~50位之间'
                    }
                }
            },
            subTitle: {
                message: '商品全称校验失败',
                validators: {
                    notEmpty: {
                        message: '商品全称不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 50,
                        message: '商品全称的长度必须在2~50位之间'
                    }
                }
            },
            goodsCode: {
                message: '商品编码(SKU)校验失败',
                validators: {
                    notEmpty: {
                        message: '商品编码(SKU)不能为空'
                    },
                    regexp: {
                        regexp: /^[^\u4e00-\u9fa5]+$/,
                        message: '职位信息不能输入汉字！'
                    },
                    stringLength: {
                        min: 2,
                        max: 50,
                        message: '商品编码(SKU)的长度必须在2~50位之间'
                    }
                }
            },
            leftNumber: {
                message: '库存余量校验失败',
                validators: {
                    regexp: {
                        regexp: /^[0-9]+$/,
                        message: '库存余量只能输入数字！'
                    },
                    stringLength: {
                        min: 0,
                        max: 10,
                        message: '库存余量的长度必须在0~10位之间'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        $.each(origin, function () {
            data[this.name] = this.value;
        });

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/goods';

            if (null !== data.id && 0 !== data.id) {
                data._method = 'PUT';
                url += ('/' + data.id);
            }
            data.isGift = (undefined === data.isGift) ? false : data.isGift;
            data.isOnSale = (undefined === data.isOnSale) ? false : data.isOnSale;
            data.isColorful = (undefined === data.isColorful) ? false : data.isColorful;
            data.isColorPackage = (undefined === data.isColorPackage) ? false : data.isColorPackage;
            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#goodsFrom').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#goodsFrom').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });

    $('.btn-cancel').on('click', function () {
        history.go(-1);
    });

})
