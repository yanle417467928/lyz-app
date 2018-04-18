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

    <script>
        $(function () {
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
                                    var str = "<tr>" +
                                            "<td><input id='externalCode' type='text' value='" + item.externalCode + "' style='width:90%;border: none;' readonly></td>" +
                                            "<td><input id='qty' type='number' value='" + item.qty + "' style='width:90%;border: none;' readonly></td>" +
                                            "<td><input id='internalCode' type='text' value='" + item.internalCode + "' style='width:90%;border: none;' readonly></td>" +
                                            "<td><input id='internalName' type='text' value='" + item.internalName + "' style='width:90%;border: none;' readonly></td>" +
                                            "<td><input id='inventory' type='number' value='" + item.inventory + "' style='width:90%;border: none;' readonly></td>" +
                                            "<td><input id='invDifference' type='number' value='" + item.invDifference + "' style='width:90%;border: none;' readonly></td>" +
                                            "<td>" + status + "</td>" +
                                            /* "<td><input id='status' type='text' value='" + item.status + "' style='width:90%;border: none;' readonly></td>" +*/
                                            "</tr>"
                                    $("#selectedGoodsTable").append(str)
                                })
                                $('#goods_import').bootstrapValidator('disableSubmitButtons', false);
                                if (result.submitFlag === true) {
                                    $("#submitGoods").removeAttr("disabled");
                                    $("#uploadSubmit").attr("disabled", "true");
                                }
                                $("#remark").val(result.remark);
                                $("#remarkDiv").attr("style", "display:block;");//显示div
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
                url: '/rest/stores/findStoresListByLoginAdministrator',
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
            var json = {
                "storeId": storeId,
                "guideId": guideId,
                "remark": remark,
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
                        setTimeout(function(){window.location.href = document.referrer}, 2000);

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
    <div class="nav-tabs-custom">
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
                                <select name="guideId" id="guideId" class="form-control select">
                                </select>
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
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group" id="remarkDiv">
                                <label>备注</label>
                                <textarea id="remark" class="form-control" rows="1" placeholder="Enter ..."
                                          disabled>${remark!''}</textarea>
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
                                    <th>商品外部编码</th>
                                    <th>数量</th>
                                    <th>商品内部编码</th>
                                    <th>商品内部名称</th>
                                    <th>库存</th>
                                    <th>库存差异</th>
                                    <th>状态</th>
                                </tr>
                                </thead>
                                <tbody id="selectedGoodsTable">

                                </tbody>

                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 取消
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-primary footer-btn" id="submitGoods"
                                    onclick="submitGoodsInfo()">
                                <i class="fa fa-check"></i> 下一步
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>


<script>

</script>
</body>