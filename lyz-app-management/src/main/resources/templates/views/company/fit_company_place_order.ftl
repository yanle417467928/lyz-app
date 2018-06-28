<head>

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
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">

    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">


    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
    <script>
        $(function () {
            findProvince();
            $("#writeDeliveryAddress").hide();
            $(".select2").select2();
            $('.btn-cancel').on('click', function () {
                history.go(-1);
            });
            initFileInput("file");

            findStoreList();

            $("#submitGoods").attr("disabled", "true");
            $("#remarkDiv").attr("style", "display:none;");//隐藏div

            $('#goods_import').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {}
            }).on('success.form.bv', function (e) {
                $("#selectedGoodsTable").empty();
                e.preventDefault();
                var $form = $(e.target);
                var origin = $form.serializeArray();
                var data = {};
                var formData = new FormData($("#goods_import")[0]);
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    var url = '/rest/admin/fit/place/order/import/goods';
                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: formData,
                        async: true,
                        cache: false,
                        contentType: false,
                        processData: false,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
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
                                                "<td><input id='externalCode' type='text' value='" + item.externalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='qty' type='number' value='" + item.qty + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='internalCode' type='text' value='" + item.internalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='internalName' type='text' value='" + item.internalName + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='inventory' type='number' value='" + item.inventory + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='invDifference' type='number' value='" + item.invDifference + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td>" + status + "</td>" +
                                                "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
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
                                                "<td><input id='externalCode' type='text' value='" + item.externalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='qty' type='number' value='" + item.qty + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='internalCode' type='text' value='" + item.internalCode + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='internalName' type='text' value='" + item.internalName + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='inventory' type='number' value='" + item.inventory + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td><input id='invDifference' type='number' value='" + item.invDifference + "' style='width:90%;border: none;' readonly></td>" +
                                                "<td>" + status + "</td>" +
                                                "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                                                /* "<td><input id='status' type='text' value='" + item.status + "' style='width:90%;border: none;' readonly></td>" +*/
                                                "</tr>"
                                    }
                                })
                                $("#selectedGoodsTable").append(stockOutStr);
                                $("#selectedGoodsTable").append(str);
                                $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                                if (result.submitFlag === true) {
                                    $("#submitGoods").removeAttr("disabled");
                                    //$("#uploadSubmit").attr("disabled", "true");
                                }
                                $("#remark").val(result.remark);
                                $("#remarkDiv").attr("style", "display:block;");//显示div
                                $("#downloadTemplate").attr("style", "display:none;");//显示div
                                //$("#submitGoods").attr("disabled","false");
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                            }
                        }
                    });
                }
            });
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
            var addBtn = document.getElementById("addDeliveryAddressButton");
            var cancelBtn = document.getElementById("cancelAddDeliveryAddressButton");
            var findBtn = document.getElementById("findDeliveryAddressButton");
            var manuallyEnterBtn = document.getElementById("manuallyEnterDeliveryAddressButton");
            cancelBtn.style.display = "none"; //style中的display属性
            findBtn.style.display = "none"; //style中的display属性
            addBtn.style.display = "block"; //style中的display属性
            manuallyEnterBtn.style.display = "none"; //style中的display属性
        }

        function submitForm() {
            var excelFile = $("#file").val();
            if (null === excelFile || excelFile === '') {
                $notify.danger('产品excel不能为空!');
                return false;
            }
            var guideName = $("#guideId").find("option:selected").text()
            console.log(guideName);
            $("#guideName").val(guideName);
            $("#goods_import").submit();

        }

        function findStoreList() {
            $("#storeId").empty()
            var store;
            $.ajax({
                url: '/rest/stores/findZSStoresListByStoreId',
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
                    findOrderCreator();
                }
            });
        }

        function findOrderCreator() {
            resetAddress();
            var storeId = $("#storeId").val();
            if (null == storeId) {
                return false;
            }
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
                }
            });

        }
        function resetAddress() {
            cancelAddDeliveryAddress();
        }

        function initFileInput(ctrlName) {
            var control = $('#' + ctrlName);
            control.fileinput({
                language: 'zh', //设置语言
                /* uploadUrl:"/rest/customers", //上传的地址*/
                allowedFileExtensions: ['xls', 'xlsx'],//接收的文件后缀
                //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
                uploadAsync: true, //默认异步上传
                showUpload: false, //是否显示上传按钮
                showRemove: true, //显示移除按钮
                showCancel: false,
                showPreview: false, //是否显示预览
                showCaption: true,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式
                dropZoneEnabled: false,//是否显示拖拽区域
                //minImageWidth: 50, //图片的最小宽度
                //minImageHeight: 50,//图片的最小高度
                //maxImageWidth: 1000,//图片的最大宽度
                //maxImageHeight: 1000,//图片的最大高度
                //maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
                //minFileCount: 0,
                maxFileCount: 1, //表示允许同时上传的最大文件个数
                enctype: 'multipart/form-data',
                validateInitialCount: true,
                previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
                msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            }).on("fileuploaded", function (event, data, previewId, index) {
                if (data.response) {
                    alert('处理成功');
                }
            });
        }

        function submitGoodsInfo() {
            var goodsDetails = new Array()
            var trs = $("#selectedGoodsTable").find("tr");
            trs.each(function (i, n) {
                var goodsSku = $(n).find("#internalCode").val();
                var num = $(n).find("#qty").val();
                goodsDetails.push({
                    qty: num,
                    sku: goodsSku
                });
            });
            var storeId = $("#storeId").val();
            var guideId = $("#guideId").val();
            var remark = $("#remark").val();
            var receiverName = $("#receiverName").val();
            var receiverPhone = $("#receiverPhone").val();
            var province = $("#province").val();
            var city = $("#city").val();
            var county = $("#county").val();
            var street = $("#street").val();
            var residenceName = $("#residenceName").val();
            var estateInfo = $("#estateInfo").val();
            var detailedAddress = $("#detailedAddress").val();
            var goAddDeliveryAddressType = $("#goAddDeliveryAddressType").val();
            var deliveryId = $("#deliveryId").val();

            if (0 == goAddDeliveryAddressType && (null == deliveryId || -1 == deliveryId || '' == deliveryId)) {
                if (null == receiverName || '' == receiverName) {
                    $notify.danger('收货人姓名不能为空！');
                    return false;
                }
                if (null == receiverPhone || '' == receiverPhone) {
                    $notify.danger('收货人电话不能为空!');
                    return false;
                }
                if (receiverPhone.length != 11) {
                    $notify.danger('请重新输入11位正确电话号码！');
                    return false;
                }
                if (null == province || '' == province) {
                    $notify.danger('省不能为空!');
                    return false;
                }
                if (null == city || '' == city) {
                    $notify.danger('市不能为空!');
                    return false;
                }
                if (null == county || '' == county) {
                    $notify.danger('区/县不能为空!');
                    return false;
                }
                if (null == street || '' == street) {
                    $notify.danger('街道不能为空!');
                    return false;
                }
                if (null == detailedAddress || '' == detailedAddress) {
                    $notify.danger('详细地址不能为空!');
                    return false;
                }
                if ((null == residenceName || '' == residenceName)) {
                    $notify.danger('小区名不能为空！');
                    return false;
                }
                var estateInfoLength = getInputLength(estateInfo);
                var residenceNameLength = getInputLength(residenceName);
                var detailedAddressLength = getInputLength(detailedAddress);

                if (estateInfoLength > 50) {
                    $notify.danger('楼盘名称长度超长，请重新输入！');
                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
                if (detailedAddressLength > 200) {
                    $notify.danger('详细地址长度超长，请重新输入！');
                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
                if (residenceNameLength > 50) {
                    $notify.danger('小区名长度超长，请重新输入！');
                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
            }

            var json = {
                "storeId": storeId,
                "guideId": guideId,
                "remark": remark,
                "receiverName": receiverName,
                "receiverPhone": receiverPhone,
                "province": province,
                "city": city,
                "county": county,
                "street": street,
                "detailedAddress": detailedAddress,
                "residenceName": residenceName,
                "estateInfo": estateInfo,
                "goAddDeliveryAddressType": goAddDeliveryAddressType,
                "deliveryId": deliveryId,
                "goodsDetails": JSON.stringify(goodsDetails)
            };
            var url = "/rest/admin/fit/place/order/submit/goods"
            $.ajax({
                url: url,
                dataType: "json",
                method: "POST",
                data: json,
                async: true,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        clearTimeout($global.timer);
                        $loading.close();
                        $notify.info("加入下料清单成功!");
                        $("#selectedGoodsTable").html("");
//                        setTimeout(function () {
//                            //window.location.href = document.referrer
//                            //location.reload();
//                        }, 2000);

                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
            console.log(goodsDetails);
        }

        function getInputLength(str) {
            return str.replace(/[\u0391-\uFFE5]/g, "aa").length;
        }

        //删除商品节点
        function del_goods_comb(obj) {
            $(obj).parent().parent().remove();

            var trs = $("#selectedGoodsTable").find("tr");
            var flag = true;
            trs.each(function (i, n) {
                var qty = $(n).find("#invDifference").val();
                if (qty < 0) {
                    flag = false;
                }
            });

            if (flag) {
                $("#submitGoods").removeAttr("disabled");
            }
        }

        //获取城市列表
        function findProvince() {
            var province = "";
            var city = "";
            var county = "";
            var street = "";
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


        function findDeliveryAddress(url) {
            var userMobile = $('#userMobile').text();
            var identityType = $('#identityType').text();
            var guideId = $("#guideId").val();
            var sellerAddressConditions = $("#sellerAddressConditions").val();

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

        function openAddressModal(url) {
            //查询地址列表
            findDeliveryAddress(url);
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

            jQuery('#province').append("<option value='" + row.deliveryProvince + "'selected='selected'>" + row.deliveryProvince + "</option>").attr("disabled", true);
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

        function returnAddress() {
            $('#selectAddressDataGrid').modal('hide');
        }


        //去支付
        function goPay() {
            goCalculatedAmount();
            $('#gotoPay').modal('show');
        }
        //计算账单信息
        function goCalculatedAmount() {
            var goodsDetails = new Array()
            var trs = $("#selectedGoodsTable").find("tr");
            trs.each(function (i, n) {
                var goodsSku = $(n).find("#internalCode").val();
                var num = $(n).find("#qty").val();
                goodsDetails.push({
                    qty: num,
                    sku: goodsSku
                });
            });
            var storeId = $("#storeId").val();
            var guideId = $("#guideId").val();

            var identityType = 2;
            var sysDeliveryType = 'HOUSE_DELIVERY';
            var data = {
                "storeId": storeId,
                "userId": guideId,
                "goodsList": JSON.stringify(goodsDetails),
                "identityType": identityType,
                "sysDeliveryType": sysDeliveryType
            };
            var url = "/rest/admin/fit/place/order/calculated/amount"
            $.ajax({
                url: url,
                dataType: "json",
                method: "POST",
                data: data,
                async: true,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        var orderAmount = result.content;
                        document.getElementById("totalGoodsAmount").innerText = orderAmount.totalGoodsAmount;
                        document.getElementById("memberDiscount").innerText = orderAmount.memberDiscount;
                        document.getElementById("promotionDiscount").innerText = orderAmount.promotionDiscount;
                        document.getElementById("stPreDeposit").innerText = orderAmount.stPreDeposit;
                        document.getElementById("stCreditMoney").innerText = orderAmount.stCreditMoney;
                        document.getElementById("stSubvention").innerText = orderAmount.stSubvention;
                        document.getElementById("freight").innerText = orderAmount.freight;
                        var amountsPayable = (orderAmount.totalGoodsAmount*100 - (orderAmount.memberDiscount*100 + orderAmount.promotionDiscount*100) + orderAmount.freight*100)/100
                        document.getElementById("amountsPayable").innerText = amountsPayable;

                        if (orderAmount.stCreditMoney > amountsPayable){
                            $("#useCreditMoney").val(amountsPayable);
                        }

                        clearTimeout($global.timer);
                        $loading.close();
                    } else {
                        $('#gotoPay').modal('hide');
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
            console.log(goodsDetails);
        }


        function maCreateOrder() {
            var goodsDetails = new Array()
            var trs = $("#selectedGoodsTable").find("tr");
            trs.each(function (i, n) {
                var goodsSku = $(n).find("#internalCode").val();
                var num = $(n).find("#qty").val();
                goodsDetails.push({
                    qty: num,
                    sku: goodsSku
                });
            });
            var storeId = $("#storeId").val();
            var guideId = $("#guideId").val();
            var remark = $("#remark").val();
            var receiverName = $("#receiverName").val();
            var receiverPhone = $("#receiverPhone").val();
            var province = $("#province").val();
            var city = $("#city").val();
            var county = $("#county").val();
            var street = $("#street").val();
            var residenceName = $("#residenceName").val();
            var estateInfo = $("#estateInfo").val();
            var detailedAddress = $("#detailedAddress").val();
            var goAddDeliveryAddressType = $("#goAddDeliveryAddressType").val();
            var deliveryId = $("#deliveryId").val();

            if (0 == goAddDeliveryAddressType && (null == deliveryId || -1 == deliveryId || '' == deliveryId)) {
                if (null == receiverName || '' == receiverName) {
                    $notify.danger('收货人姓名不能为空！');
                    return false;
                }
                if (null == receiverPhone || '' == receiverPhone) {
                    $notify.danger('收货人电话不能为空!');
                    return false;
                }
                if (receiverPhone.length != 11) {
                    $notify.danger('请重新输入11位正确电话号码！');
                    return false;
                }
                if (null == province || '' == province) {
                    $notify.danger('省不能为空!');
                    return false;
                }
                if (null == city || '' == city) {
                    $notify.danger('市不能为空!');
                    return false;
                }
                if (null == county || '' == county) {
                    $notify.danger('区/县不能为空!');
                    return false;
                }
                if (null == street || '' == street) {
                    $notify.danger('街道不能为空!');
                    return false;
                }
                if (null == detailedAddress || '' == detailedAddress) {
                    $notify.danger('详细地址不能为空!');
                    return false;
                }
                if ((null == residenceName || '' == residenceName)) {
                    $notify.danger('小区名不能为空！');
                    return false;
                }
                var estateInfoLength = getInputLength(estateInfo);
                var residenceNameLength = getInputLength(residenceName);
                var detailedAddressLength = getInputLength(detailedAddress);

                if (estateInfoLength > 50) {
                    $notify.danger('楼盘名称长度超长，请重新输入！');
                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
                if (detailedAddressLength > 200) {
                    $notify.danger('详细地址长度超长，请重新输入！');
                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
                if (residenceNameLength > 50) {
                    $notify.danger('小区名长度超长，请重新输入！');
                    $('#form').bootstrapValidator('disableSubmitButtons', false);
                    return false;
                }
            }
            var usePreDeposit = $("#usePreDeposit").val();
            var useCreditMoney = $("#useCreditMoney").val();
            var useSubvention = $("#useSubvention").val();
            var amountsPayable = $("#amountsPayable").text();
            var freight = $("#freight").text();


            var deliveryMsgString = {
                "receiver": receiverName,
                "receiverPhone": receiverPhone,
                "deliveryProvince": province,
                "deliveryCity": city,
                "deliveryCounty": county,
                "deliveryStreet": street,
                "residenceName": residenceName,
                "detailedAddress": detailedAddress,
                "estateInfo": estateInfo,
                "deliveryType": 'HOUSE_DELIVERY'
            }
            var billingMsgString = {
                "stPreDeposit": usePreDeposit,
                "storeCreditMoney": useCreditMoney,
                "storeSubvention": useSubvention
            }
            if (null != useCreditMoney && useCreditMoney > 0){
                var totalAmount = (usePreDeposit*100 + useCreditMoney*100 + useSubvention*100)/100;
                if (totalAmount != amountsPayable){
                    $notify.warning("使用信用金支付必须一次性支付完毕");
                    return;
                }
            }

            var identityType = 2;
            var sysDeliveryType = 'HOUSE_DELIVERY';
            var json = {
                "userId": guideId,
                "identityType": identityType,
                "goodsList": JSON.stringify(goodsDetails),
                "deliveryMsg":JSON.stringify(deliveryMsgString),
                "billingMsg": JSON.stringify(billingMsgString),
                "sysDeliveryType": sysDeliveryType,
                "storeId": storeId,
//                "cityId":city,
                "remark": remark
            };
            var url = "/rest/admin/fit/place/order/ma/create";
            $.ajax({
                url: url,
                dataType: "json",
                method: "POST",
                data: json,
                async: true,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        document.getElementById("totalGoodsAmount").innerText = 0.00;
                        document.getElementById("memberDiscount").innerText = 0.00;
                        document.getElementById("promotionDiscount").innerText = 0.00;
                        document.getElementById("stPreDeposit").innerText = 0.00;
                        document.getElementById("stCreditMoney").innerText = 0.00;
                        document.getElementById("stSubvention").innerText = 0.00;
                        document.getElementById("freight").innerText = 0.00;
                        document.getElementById("amountsPayable").innerText = 0.00;
                        $("#usePreDeposit").val(0.00);
                        $("#useCreditMoney").val(0.00);
                        $("#useSubvention").val(0.00);
                        clearTimeout($global.timer);
                        $loading.close();
                        $notify.info(result.message);
                        $('#gotoPay').modal('hide');
                        $("#selectedGoodsTable").html("");
                        window.location.href="";
                    } else {
                        $('#gotoPay').modal('hide');
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
            console.log(goodsDetails);
        }

        function returnGoPay() {
            $('#gotoPay').modal('hide');
        }
    </script>

</head>
<body>

<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parent??>
            <li><a href="javascript:void(0);">${selectedMenu.parent.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>加载中...</h1>
</#if>
</section>

<section class="content">
    <div class="nav-tabs-custom" id="target">
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary">
                </div>
            </div>
        </div>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="goods_import"
                      enctype="multipart/form-data">
                    <input type="hidden" id="guideName" name="guideName" value="">
                    <input type="hidden" name="deliveryId" id="deliveryId" class="form-control" \>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="storeId">
                                    下单装饰公司/门店
                                </label>
                                <select name="storeId" id="storeId"
                                        class="form-control select selectpicker"
                                        data-live-search="true" onchange="findOrderCreator()">
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="guideId">
                                    下单员工/导购
                                </label>
                                <select name="guideId" id="guideId" class="form-control select"
                                        onchange="resetAddress()">
                                </select>
                            </div>
                        </div>
                    </div>

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
                            onclick="openAddressModal('/rest/order/photo/find/address')">
                        从地址库查找
                    </button>

                    <button type="button" id="manuallyEnterDeliveryAddressButton" class="btn btn-primary btn-xs"
                            style="width:200px;height:50px;display:none;margin-left: 220px;margin-top: -50px;"
                            onclick="manuallyEnterAddress()">
                        手动输入地址
                    </button>

                    <input id="goAddDeliveryAddressType" name="goAddDeliveryAddressType" type="hidden" value="1"/>


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
                                                                       class="form-control"
                                                                       style="width:300px;height:34px;"
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


                    <!-- 去支付弹框 -->
                    <div id="gotoPay" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document" style="width: 60%">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h3>支付</h3>
                                    <button type="button" name="search" class="btn btn-default pull-left"
                                            onclick="returnGoPay()" style="margin-left:700px;margin-top: -35px;">返回
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <!--  设置这个div的大小，超出部分显示滚动条 -->
                                    <div id="payDataGridTree" class="ztree" style="height: 60%;overflow:auto; ">
                                        <section class="content">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="box box-primary">
                                                        <div id="addressToolbar" class="form-inline">

                                                            <div class="input-group col-md-6"
                                                                 style="margin-top:0px positon:relative">
                                                                <h4>账单信息</h4>
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
                                                            <br><br>
                                                            <div class="input-group col-md-8"
                                                                 style="margin-top:0px positon:relative">
                                                                <h4>支付信息</h4>
                                                                <b>客户预存款：</b>
                                                                <span id="stPreDeposit" name="stPreDeposit"></span>
                                                                <input id="usePreDeposit" style="float: right"/>
                                                                <br><br><br>
                                                                <b>信&nbsp;&nbsp;&nbsp;&nbsp;用&nbsp;&nbsp;&nbsp;&nbsp;金：</b>
                                                                <span id="stCreditMoney" name="stCreditMoney"></span>
                                                                <input id="useCreditMoney" style="float: right"/>
                                                                <br><br><br>
                                                                <b>现&nbsp;金&nbsp;返&nbsp;利&nbsp;：</b>
                                                                <span id="stSubvention" name="stSubvention"></span>
                                                                <input id="useSubvention" style="float: right"/>
                                                            </div>
                                                        </div>
                                                        <div class="input-group col-md-6"
                                                             style="margin-top:0px positon:relative">
                                                            <button type="button" class="btn btn-primary footer-btn"
                                                                    id="submitOrder"
                                                                    onclick="maCreateOrder()"
                                                                    style="position:fixed;right:-100;bottom:0;width:150px;height:40px;margin-left: 600px;">
                                                                <i class="fa fa-check"></i> 下单
                                                            </button>
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
                                <#--<input type="text" name="province" id="province" class="form-control" \>-->
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
                                <#--<input type="text" name="city" id="city" class="form-control" \>-->
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
                                <#--<input type="text" name="county" id="county" class="form-control" \>-->
                                </div>
                            </div>

                            <div class="col-xs-12 col-md-4">
                                <div class="form-group">
                                    <label>
                                        街道
                                    </label>
                                    <select name="street" id="street" class="form-control select">
                                    <#--onchange="findProvince(this.value)">-->

                                    </select>
                                <#--<input type="text" name="street" id="street" class="form-control" \>-->
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


                    <div class="row">
                        <div class="col-xs-12 col-md-4">
                            <div class="form-group">
                                <label for="file">
                                    导入产品
                                </label>
                                <div class="form-inline">
                                    <input name="file" type="file" class="form-control" id="file">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-primary footer-btn" id="uploadSubmit"
                                    style="margin-top: 18%;padding-left: 1%" onclick="submitForm();">
                                <i class="fa fa-check"></i> 上传
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-success pull-left" id="downloadTemplate"
                                    style="margin-top: 18%"
                                    onclick="window.location.href='/rest/admin/fit/place/order/download/sample'">
                                <i class="fa fa-download"></i> 下载模板
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group" id="remarkDiv">
                                <label>备注</label>
                                <textarea id="remark" class="form-control" rows="1" placeholder="Enter ..."
                                >${remark!''}</textarea>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-success">
                            </div>
                        </div>
                    </div>
                    <div class="box-body table-responsive no-padding">

                        <div class="col-xs-12">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>商品外部编码</th>
                                    <th>数量</th>
                                    <th>商品内部编码</th>
                                    <th>商品内部名称</th>
                                    <th>库存</th>
                                    <th>缺货数量</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody id="selectedGoodsTable">

                                </tbody>

                            </table>
                        </div>
                    </div>
                    <div class="row">
                    <#--<div class="col-xs-12 col-md-8"></div>-->
                    <#--<div class="col-xs-12 col-md-2">-->
                    <#--<button type="button" class="btn btn-danger footer-btn btn-cancel">-->
                    <#--<i class="fa fa-close"></i> 取消-->
                    <#--</button>-->
                    <#--</div>-->
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-primary footer-btn" id="submitGoods"
                                    onclick="submitGoodsInfo()">
                                <i class="fa fa-check"></i> 加入下料清单
                            </button>
                        </div>
                    </div>
                </form>
                <div class="col-xs-12 col-md-2">
                    <button type="button" class="btn btn-danger footer-btn btn-cancel"
                            style="position:fixed;right:-100;bottom:0;width:150px;height:40px;margin-left: 300px;">
                        <i class="fa fa-close"></i> 取消
                    </button>

                    <button type="button" class="btn btn-primary footer-btn" id="submitGoods"
                            onclick="submitGoodsInfo()"
                            style="position:fixed;right:-100;bottom:0;width:150px;height:40px;margin-left: 500px;">
                        <i class="fa fa-check"></i> 加入下料清单
                    </button>
                    <button type="button" class="btn btn-primary footer-btn" id="submitGoods"
                            onclick="goPay()"
                            style="position:fixed;right:-100;bottom:0;width:150px;height:40px;margin-left: 700px;">
                        <i class="fa fa-check"></i> 去支付
                    </button>
                <#--</div>-->
                <#--<div class="col-xs-12 col-md-2">-->
                    <button class="btn btn-primary footer-btn" id="backTop"
                            style="position:fixed;right:0;bottom:0;width:150px;height:40px;">
                        返回顶部
                    </button>
                </div>
            </div>
        </div>
    </div>
</section>


<script>

    backTop.onclick = function () {
        target.scrollIntoView();
    }

</script>
</body>