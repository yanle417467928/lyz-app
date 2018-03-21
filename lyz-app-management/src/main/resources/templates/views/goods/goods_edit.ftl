<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script type="text/javascript" charset="utf-8" src="/mag/js/kindeditor-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="/mag/js/zh_CN.js"></script>

    <style type="text/css">
        .box-default {
            border-top-width: 0px;
            margin-bottom: 0px;
            position: absolute;
            z-index: 99;
        }

        .box-title {
            font-size: 12px !important;
        }

        .box-body {
            display: block;
        }

        .box-footer {
            text-align: right;
        }
    </style>
</head>
<body>
<section class="content-header">
    <h1>编辑商品信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-3" data-toggle="tab">商品详情页</a></li>
            <li><a href="#tab_1-2" data-toggle="tab">扩展选项</a></li>
        </ul>
        <form id="goodsFrom" class="bv-form tab-content">
            <div class="tab-content">
                <div class="tab-pane active" id="tab_1-1">
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsName">商品ID
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品ID"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="id" type="text" class="form-control" id="id" readonly
                                           placeholder="商品ID"
                                           value="${(goodsVO.id)?c}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">物料编号
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入物料编号"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sku" type="text" class="form-control" id="sku" readonly
                                           placeholder="物料编号"
                                           value="${(goodsVO.sku)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsName">物料名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入物料名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="materialsName" type="text" class="form-control" id="materialsName"
                                           readonly
                                           placeholder="物料名称"
                                           value="${(goodsVO.materialsName)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">电商名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入电商名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="skuName" type="text" class="form-control" id="skuName"
                                           placeholder="电商名称"
                                           value="${(goodsVO.skuName)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsSpecification">商品规格
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品规格"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="goodsSpecification" type="text" class="form-control" id="goodsSpecification"
                                           placeholder="商品规格"
                                           value="${(goodsVO.goodsSpecification)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="brdName">商品品牌
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品品牌"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <select id="brdName" name="brdId" class="form-control"
                                            data-live-search="true">
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsCategoryCode">电商分类
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入电商分类"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <select id="goodsCategoryCode" name="categoryName" class="form-control"
                                            data-live-search="true">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sortId">排序号
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入排序号"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sortId" type="text" class="form-control" id="sortId"
                                           placeholder="排序号"
                                           value="${(goodsVO.sortId)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="onSaleTime">搜索关键字
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="搜索关键字"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="searchKeyword" type="text" class="form-control"
                                           value="${(goodsVO.searchKeyword)!''}"
                                           id="searchKeyword" placeholder="搜索关键字">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>产品档次
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="选择产品档次"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <select id="productGrade" name="productGrade" class="form-control select"
                                            data-live-search="true">
                                        <option value="1"
                                                <#if goodsVO?? && goodsVO.productGrade?? && (goodsVO.productGrade) =='1'>selected</#if>>
                                            高档
                                        </option>
                                        <option value="2"
                                                <#if goodsVO?? && goodsVO.productGrade?? && (goodsVO.productGrade) == '2'>selected</#if>>
                                            中档
                                        </option>
                                        <option value="3"
                                                <#if goodsVO?? && goodsVO.productGrade?? && (goodsVO.productGrade) == '3'>selected</#if>>
                                            低档
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="typeName">类型名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入类型名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="typeName" type="text" class="form-control" id="typeName"
                                           placeholder="类型名称"
                                           value="${(goodsVO.typeName)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为热门商品</label>
                                <div class="input-group">
                                    <input name="isHot" id="isHot" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isHot?? && goodsVO.isHot)>checked</#if>>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为调色商品</label>
                                <div class="input-group">
                                    <input name="isColorMixing" id="isColorMixing" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isColorMixing?? && goodsVO.isColorMixing)>checked</#if>>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="tab_1-2">
                <#--图片上传功能完善后再优化-->
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="exampleInputFile">封面图片</label>
                                <input id="coverImagefile" type="file" name="file" multiple class="file-loading">
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="exampleInputFile">轮播图片</label>
                                <input id="rotationImagefile" type="file" name="file" multiple class="file-loading">
                            </div>
                        </div>
                    </div>
                    <div class="row" style="height: 150px">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group" id='coverImageShow'>
                                <input name="coverImageUri" type="hidden" id="coverImg" class="form-control">
                                <div class="img-box" id="coverImageBox">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group" id='rotationImageShow'>
                                <input name="rotationImageUri" type="hidden" id="rotationImg" class="form-control">
                                <div class="img-box" id="rotationImageBox">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="tab_1-3">
                    <div class="row">
                        <div class="col-md-12 col-xs-12">
                            <div class="form-group">
                                <label for="leftNumber">商品详情页
                                </label>
                                <div>
                                    <textarea name="goodsDetail"
                                              class="editor"><#if goodsVO??>${goodsVO.goodsDetail!""}</#if></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-md-8"></div>
                    <div class="col-xs-12 col-md-2">
                        <button type="submit" class="btn btn-primary footer-btn">
                            <i class="fa fa-check"></i> 保存
                        </button>
                    </div>
                    <div class="col-xs-12 col-md-2">
                        <button type="button" class="btn btn-danger footer-btn btn-cancel">
                            <i class="fa fa-close"></i> 取消
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</section>

<script>
    var categoryName;
    var brandId;
    $(function () {
        //初始化编辑器
        var id = $('#id')
        var editor = KindEditor.create('.editor', {
            width: '80%',
            height: '350px',
            resizeType: 1,
            uploadJson: '/rest/goods/updateGoodsDetial',
            fileManagerJson: '/rest/goods/updateGoodsDetial',
            allowFileManager: true
        });
        categoryName = '${(goodsVO.categoryName)!''}';
        brandId = '${(goodsVO.brdId)!''}';
        findGoodsCategorySelection();
        findGoodsBrandSelection();
        var coverImageUri = '${(goodsVO.coverImageUri)!''}';
        if (coverImageUri != null && coverImageUri != '') {
            $('#coverImg').val(coverImageUri);
            $('#coverImageBox').html('<img  src="' + coverImageUri + '"' + ' class="img-rounded" style="height: 100px;width: 100px;" >');
        }
        var rotationImageUris = '${(goodsVO.rotationImageUri)!''}';
        if (rotationImageUris != null && rotationImageUris != '') {
            var arr1 = new Array();
            $('#rotationImg').val(rotationImageUris);
            arr1 = rotationImageUris.split(',');
            for (var a = 0; a < arr1.length; a++) {
                var rotationImageUri = arr1[a];
                $("#rotationImageBox").append('<div class="col-md-3 text-center" ><img  src="' + rotationImageUri + '"' + ' class="img-rounded" style="width: 100px;"/><div style="margin-top: 5px;"><button class="btn btn-default" onclick="delImg(this)">删除</button></div></div>');
            }
        }

        initFileInput('coverImagefile', 1);
        initFileInput('rotationImagefile', 2);
    });

    $(function () {

        $('.btn-cancel').on('click', function () {
            history.go(-1);
        });

        if (!$global.validateMobile()) {
            $('.select').selectpicker();
        }

        $(function () {
            $('[data-toggle="tooltip"]').popover();
        });

        $('.datepicker').datepicker({
            format: 'yyyy-MM-dd',
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
                skuName: {
                    message: '电商名称校验失败',
                    validators: {
                        notEmpty: {
                            message: '电商名称不能为空'
                        },
                        remote: {
                            type: 'POST',
                            url: '/rest/goods/isExistSkuName',
                            message: '该电商名称已被使用',
                            delay: 500,
                            data: function () {
                                return {
                                    skuName: $('#skuName').val(),
                                    id: $('#id').val().replace(/,/g, ''),
                                }
                            }
                        },
                        stringLength: {
                            min: 2,
                            max: 20,
                            message: '电商名称的长度必须在2~20位之间'
                        },
                    }
                }, categoryName: {
                    message: '分类名称校验失败',
                    validators: {
                        notEmpty: {
                            message: '分类名称不能为空'
                        }
                    }
                }, sortId: {
                    message: '排序号校验失败',
                    validators: {
                        notEmpty: {
                            message: '排序号不能为空'
                        },
                        regexp: {
                            regexp: /^[1-9]\d*$/,
                            message: '排序号只能输入数字'
                        },
                        remote: {
                            type: 'POST',
                            url: '/rest/goods/isExistSortId',
                            message: '该排序号已被使用',
                            delay: 500,
                            data: function () {
                                return {
                                    sortId: $('#sortId').val(),
                                    id: $('#id').val().replace(/,/g, ''),
                                }
                            }
                        },
                        stringLength: {
                            min: 1,
                            max: 10,
                            message: '电商名称的长度必须在1~10位之间'
                        },
                    }, searchKeyword: {
                        message: '关键字校验失败',
                        validators: {
                            stringLength: {
                                min: 1,
                                max: 30,
                                message: '关键字长度必须在1~30位之间'
                            },
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
            data['id'] = $('#id').val().replace(/,/g, '');
            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);
                var url = '/rest/goods/update';
                console.log(data);
                if (null !== data.id && 0 != data.id) {
                    data._method = 'PUT';
                }
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
                            $notify.danger('商品编辑失败');
                            $('#goodsFrom').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });


    function findGoodsCategorySelection() {
        var goodsCategory = "";
        $.ajax({
            url: '/rest/goodsCategorys/findEditGoodsCategory',
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
                    goodsCategory += "<option value=" + item.categoryName + ">" + item.categoryName + "</option>";
                })
                $("#goodsCategoryCode").append(goodsCategory);
                $("#goodsCategoryCode").val(categoryName);
            }
        });
    }


    function findGoodsBrandSelection() {
        var brdName = "";
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
                    brdName += "<option value=" + item.brdId + ">" + item.brandName + "</option>";
                })
                $("#brdName").append(brdName);
                $("#brdName").val(brandId);
            }
        });
    }

    function initFileInput(ctrlName, type) {
        var control = $('#' + ctrlName);
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: "/rest/goods/updateImg", //上传的地址
            allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
            //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
            uploadAsync: true, //默认异步上传
            showUpload: false, //是否显示上传按钮
            showRemove: true, //显示移除按钮
            showPreview: false, //是否显示预览
            showCaption: true,//是否显示标题
            browseClass: "btn btn-primary", //按钮样式
            dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            //maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
            maxFileCount: 1,//表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount: true,
            previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！"
        }).on('filebatchselected', function (event, data, id, index) {
            $(this).fileinput("upload");
        }).on("fileuploaded", function (event, data) {
            if (data.response.code == 0) {
                if (1 == type) {
                    $('#coverImageBox').html('<img  src="' + data.response.content + '"' + ' class="img-rounded" style="height: 100px;width: 100px;" />')
                    $('#coverImg').val(data.response.content);
                } else if (2 == type) {
                    $("#rotationImageBox").append('<div class="col-md-3 text-center"  ><img  src="' + data.response.content + '"' + ' class="img-rounded" style="width: 100px"/><div style="margin-top: 5px;"><button class="btn btn-default" onclick="delImg(this)">删除</button></div></div>');
                    getUrl("rotationImageBox", '#rotationImg');
                }
            }
        });
    }

    function delImg(file) {
        file.parentNode.parentNode.remove();
        getUrl("rotationImageBox", '#rotationImg');
    }

    function getUrl(urlId, id) {
        var url = '';
        var charImg = document.all(urlId).getElementsByTagName("img");
        if (charImg.length == 1) {
            url = charImg[0].src;
        } else {
            for (var i = 0; i < charImg.length; i++) {
                url += charImg[i].src + ',';
            }
            url = url.substring(0, url.length - 1);
        }
        $(id).val(url);
    }
</script>

</body>