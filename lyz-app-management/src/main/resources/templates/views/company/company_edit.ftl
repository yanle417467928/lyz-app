<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="http://trentrichardson.com/examples/timepicker/css/jquery-ui-timepicker-addon.css" rel="stylesheet" />
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">

    <style type="text/css">
        .box-default{
            border-top-width: 0px;
            margin-bottom: 0px;
            position: absolute;
            z-index:99;
        }
        .box-title{
            font-size: 12px!important;
        }
        .box-body{
            display: block;
        }
        .box-footer{
            text-align: right;
        }
    </style>
</head>
<body>
<section class="content-header">
    <h1>编辑装饰公司信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-2" data-toggle="tab">扩展选项</a></li>
        </ul>
        <form id="goodsFrom" class="bv-form tab-content">
            <div class="tab-content">
                <div class="tab-pane active" id="tab_1-1">
                    <input type="hidden" name="id" id="id"
                        <#if goodsVO?? && goodsVO.id??>
                           value="${(goodsVO.id)?c}"
                        <#else>
                           value="0"
                        </#if>/>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsName">商品名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="goodsName" type="text" class="form-control" id="goodsName"
                                           placeholder="商品名称"
                                           value="${(goodsVO.goodsName)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">商品简称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品简称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="title" type="text" class="form-control" id="title"
                                           placeholder="商品简称"
                                           value="${(goodsVO.title)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="subTitle">商品全称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品全称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="subTitle" type="text" class="form-control" id="subTitle"
                                           placeholder="商品全称"
                                           value="${(goodsVO.subTitle)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsCode">商品编码(SKU)
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品编码(SKU)"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="goodsCode" type="text" class="form-control" id="goodsCode"
                                           placeholder="商品编码(SKU)"
                                           value="${(goodsVO.goodsCode)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>所属类别
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="选择是商品的所属类别"></i>
                                </label>
                                <#--应丛数据库动态读取-->
                                <select id="categoryTitle" name="categoryTitle"
                                        class="form-control select"
                                        data-live-search="true">
                                    <option value="1"
                                            <#if goodsVO?? && goodsVO.categoryId?? && (goodsVO.categoryId?c) == '1'>selected</#if>>
                                        PPR水管
                                    </option>
                                    <option value="2"
                                            <#if goodsVO?? && goodsVO.categoryId?? && (goodsVO.categoryId?c) == '2'>selected</#if>>
                                        吊顶造型
                                    </option>
                                    <option value="3"
                                            <#if goodsVO?? && goodsVO.categoryId?? && (goodsVO.categoryId?c) == '3'>selected</#if>>
                                        外墙漆
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>品牌
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="选择是商品的品牌"></i>
                                </label>
                            <#--应丛数据库动态读取-->
                                <select id="brandTitle" name="brandTitle"
                                        class="form-control select"
                                        data-live-search="true">
                                    <option value="1"
                                            <#if goodsVO?? && goodsVO.brandId?? && (goodsVO.brandId?c) == '1'>selected</#if>>
                                        华润
                                    </option>
                                    <option value="2"
                                            <#if goodsVO?? && goodsVO.brandId?? && (goodsVO.brandId?c) == '2'>selected</#if>>
                                        乐意装
                                    </option>
                                    <option value="3"
                                            <#if goodsVO?? && goodsVO.brandId?? && (goodsVO.brandId?c) == '3'>selected</#if>>
                                        莹润
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="leftNumber">库存余量
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入库存余量"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="leftNumber" type="text" class="form-control" id="leftNumber"
                                           placeholder="库存余量"
                                           value="${(goodsVO.leftNumber)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="onSaleTime">上架时间
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="选择商品的上架时间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="onSaleTime" type="text" class="form-control datepicker ui_timepicker"
                                           value="${(goodsVO.onSaleTime)!''}"
                                           id="onSaleTime" placeholder="上架时间">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为小辅料</label>
                                <div class="input-group">
                                    <input name="isGift" id="isGift" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isGift?? && goodsVO.isGift)>checked</#if>>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否上架</label>
                                <div class="input-group">
                                    <input name="isOnSale" id="isOnSale" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isOnSale?? && goodsVO.isOnSale)>checked</#if>>
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
                                <input type="file" id="coverImageUri">
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="exampleInputFile">展示图片</label>
                                <input type="file" id="showPictures">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为调色产品</label>
                                <div class="input-group">
                                    <input name="isColorful" id="isColorful" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isColorful?? && goodsVO.isColorful)>checked</#if>>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为调色包</label>
                                <div class="input-group">
                                    <input name="isColorPackage" id="isColorPackage" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isColorPackage?? && goodsVO.isColorPackage)>checked</#if>>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
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
            <!-- =================================下面是弹框======================================= -->
            <div class="modal modal-primary fade" id="modal-primary">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">正在添加信息</h4>
                        </div>
                        <div class="modal-body">
                            <p id="primaryTitle">正在执行添加操作，您可以点击确认继续，点击关闭则退出。</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" id="primaryCloseBtn" class="btn btn-outline pull-left" data-dismiss="modal">关闭</button>
                            <button type="button" id="modalAddBtn" class="btn btn-outline">确认添加</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
            <!-- /.modal -->
            <div class="modal modal-danger fade" id="modal-danger">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">正在删除信息</h4>
                        </div>
                        <div class="modal-body">
                            <p id="dangerTitle">是否将此职位信息从数据中移除，点击确认继续，点击关闭取消操作。</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline pull-left" id="modalCloseBtn" data-dismiss="modal">关闭</button>
                            <button type="button" id="modalDelBtn" class="btn btn-outline">确定，我要删除</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
        </form>
    </div>
</section>
<script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
<script src="/javascript/goods_edit.js"></script>


</body>