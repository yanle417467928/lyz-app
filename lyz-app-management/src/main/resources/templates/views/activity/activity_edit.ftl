<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/plugins/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <link href="/plugins/iCheck/all.css" rel="stylesheet">


    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script src="/plugins/iCheck/icheck.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.js"></script>
    <script src="/plugins/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script type="text/javascript" src="/javascript/activity/activity_edit.js"></script>
</head>
<body>
<section class="content-header">
    <h1>编辑促销</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="activity_form">
                    <input type="hidden" name="id" value="<#if actBaseDO??>${actBaseDO.id?c}</#if>">
                    <input type="hidden" name="createTime"
                           value="<#if actBaseDO??>${(actBaseDO.createTime)?replace("T"," ")!""}</#if>">
                    <input type="hidden" name="status" value="<#if actBaseDO??>${actBaseDO.status!""}</#if>">

                    <div class="row" style="height: 150px">
                        <div class="col-md-6 col-xs-6">
                            <div class="form-group">
                                <label for="title">
                                    促销图片
                                </label>
                                <div class="form-group" id='coverImageShow'>
                                    <img src="<#if actBaseDO??>${actBaseDO.picUrl!''}</#if>" width="180" height="100">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    促销标题
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="title" type="text" class="form-control" id="cusName"
                                           placeholder="促销标题" value="<#if actBaseDO??>${actBaseDO.title!""}</#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    促销编码
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="actCode" type="text" class="form-control" id="nickName"
                                           placeholder="系统自动生成" readonly="readonly"
                                           value="<#if actBaseDO??>${actBaseDO.actCode!""}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    开始日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="beginTime" type="text" class="form-control" id="beginTime"
                                           readonly placeholder="开始日期"
                                           value="<#if actBaseDO??>${(actBaseDO.beginTime)?replace("T"," ")!""}</#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    结束日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="endTime" type="text" class="form-control" id="endTime"
                                           readonly placeholder="结束日期"
                                           value="<#if actBaseDO??>${(actBaseDO.endTime)?replace("T"," ")!""}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    目标对象
                                </label>
                                <div class="input-group">
                                    <input name="target" value="6" type="checkbox"
                                           class="flat-red" <#if actBaseDO??><#if actBaseDO.actTarget?? && actBaseDO.actTarget?contains('6')>checked</#if></#if>>顾客
                                    <input name="target" value="0" type="checkbox"
                                           class="flat-red" <#if actBaseDO??><#if actBaseDO.actTarget?? && actBaseDO.actTarget?contains('0')>checked</#if></#if>>导购
                                    <input name="target" value="2" type="checkbox"
                                           class="flat-red" <#if actBaseDO??><#if actBaseDO.actTarget?? && actBaseDO.actTarget?contains('2')>checked</#if></#if>>装饰公司经理
                                </div>

                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    使用限制
                                </label>
                                <div class="input-group">

                                    <input id="isReturnable" type="checkbox"
                                           class="flat-red" <#if actBaseDO??><#if actBaseDO.isReturnable?? && actBaseDO.isReturnable = true>checked</#if></#if>>可退货
                                    <input id="isDouble" type="checkbox"
                                           class="flat-red" <#if actBaseDO??><#if actBaseDO.isDouble?? && actBaseDO.isDouble == true >checked</#if></#if>>可叠加享受

                                    <input id="isGcOrder" type="checkbox"
                                           class="flat-red" <#if actBaseDO??><#if actBaseDO.isGcOrder?? && actBaseDO.isGcOrder == true>checked</#if></#if>>工程单可享受

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-2">
                            <label for="title">
                                城市
                            </label>
                            <select name="cityId" id="cityId" class="form-control select"
                                    onchange="findStoreByCity(this.value);">
                            <#if cityList??>
                                <#list cityList as item>
                                    <option value="${item.cityId}" <#if item.cityId == actBaseDO.cityId >
                                            selected</#if>>${item.name}</option>
                                </#list>
                            </#if>
                            </select>
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <label for="title">
                            </label>
                            <div class="box box-success ">
                                <div class="box-header with-border">
                                    <h3 class="box-title">选择门店</h3>
                                    <div class="box-tools pull-right">
                                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                    <!-- /.box-tools -->
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-2">
                                            <button type="button" class="btn btn-default btn-xs"
                                                    onclick="checkAllStore()">全选
                                            </button>

                                            <button type="button" class="btn btn-default btn-xs"
                                                    onclick="unCheckAllStore()">反选
                                            </button>
                                        </div>
                                    </div>

                                    <div id="stores" style="margin-top: 10px;">
                                    <#if stores??>
                                        <#list stores as item>
                                            <label id='${item.storeId}'

                                                <#if item.isSelected == true>
                                                   class='label label-success'
                                                <#else>
                                                   class='label label-default'
                                                </#if>
                                                   onclick='checkStore(this)'>${item.storeName}</label>
                                        </#list>
                                    </#if>
                                    </div>
                                </div>
                                <!-- /.box-body -->
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-2">
                            <label for="title">
                                促销类型
                            </label>
                            <select name="baseType" id="baseType" class="form-control select"
                                    onchange="changeBaseType(this.value);">
                                <option value="COMMON">普通</option>
                            </select>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <div class="form-group">
                                <label for="title">
                                    促销条件
                                </label>
                                <div class="form-group">
                                    <select name="conditionType" id="conditionType" class="form-control select"
                                            onchange="changeConditionType(this.value);">
                                        <option value="FQTY">满数量</option>
                                        <option value="FAMO">满金额</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <div class="form-group">
                                <label for="title">
                                    促销结果
                                </label>
                                <div class="form-group">
                                    <select name="promotionType" id="resultType" class="form-control select"
                                            onchange="changeResultType(this.value);">
                                        <option value="SUB">立减</option>
                                        <option value="GOO">送商品</option>
                                        <option value="ADD">加价购买</option>
                                        <option value="DIS">打折</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 选择本品table -->
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header">
                                    <h3 class="box-title">选择本品</h3>

                                    <div class="box-tools">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openGoodsModal('selectedGoodsTable')">
                                            选择商品
                                        </button>

                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                    class="fa fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <table class="table table-bordered">
                                        <thead>
                                        <tr>

                                            <th>GID</th>
                                            <th>sku</th>
                                            <th>商品名</th>
                                            <th>数量</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="selectedGoodsTable">
                                        <#if actGoodsMappingDO??>
                                            <#list actGoodsMappingDO as item>
                                            <tr>
                                                <td><input id="gid" type='text' value="${item.gid?c}"
                                                           style="width:90%;border: none;" readonly/></td>
                                                <td><input id='sku' type='text' value="${item.sku!''}"
                                                           style='width:90%;border: none;' readonly></td>
                                                <td><input id='title' type='text' value='${item.goodsTitile!''}'
                                                           style='width:90%;border: none;' readonly></td>
                                                <td><input id='qty' type='number' value='${item.qty?c}'></td>
                                                <td><a href='#' onclick='del_goods_comb(this);'>删除</td>
                                            </tr>
                                            </#list>
                                        </#if>
                                        </tbody>

                                    </table>
                                </div>
                                <!-- /.box-body -->
                                <div class="box-footer clearfix">
                                    <div class="row" id="fullNumber_div">

                                        <div class="col-xs-12 col-md-1">
                                            <div class="input-group">
                                                <input id="is_goods_optional_qty" type="checkbox"
                                                       class="flat-red" <#if actBaseDO??><#if actBaseDO.isGoodsOptionalQty?? && actBaseDO.isGoodsOptionalQty = true>checked</#if></#if>>任选数量
                                            </div>
                                        </div>
                                        <div id="goods_optional_qty_div" style="display: none;">
                                            <div class="col-xs-12 col-md-2">
                                                <div class="input-group">
                                                    <input name="fullNumber" type="number" class="form-control"
                                                           id="fullNumber" placeholder="总数量"
                                                           value="<#if actBaseDO??><#if actBaseDO.fullNumber??>${actBaseDO.fullNumber?c}</#if></#if>">
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <!-- /.box -->
                        </div>
                    </div>

                    <!-- 满足最低金额 -->
                    <div class="row" id="fullAmount_div" style="display: none;">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    最低金额￥
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-cny"></i></span>
                                    <input name="fullAmount" type="number" class="form-control" id="fullAmount"
                                           value="<#if actBaseDO?? && actBaseDO.fullAmount??>${actBaseDO.fullAmount?c}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 立减金额 -->
                    <div class="row" id="subAmount_div" style="display: none;">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    立减金额￥
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-cny"></i></span>
                                    <input name="subAmount" type="number" class="form-control" id="subAmount"
                                           value="<#if actSubAmountDO?? && actSubAmountDO.subAmount??>${actSubAmountDO.subAmount?c}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 打折 -->
                    <div class="row" id="discount_div" style="display: none;">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="discount">
                                    折扣比例（1~10）
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-cny"></i></span>
                                    <input name="discount" type="text" class="form-control" id="discount"
                                           value="<#if actSubAmountDO?? && actSubAmountDO.discount??>${actSubAmountDO.discount?c}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 选择的赠品table -->
                    <div class="row" id="Gift_div" style="display: none;">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header">
                                    <h3 class="box-title">选择赠品</h3>

                                    <div class="box-tools">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openGoodsModal('selectedGiftTable')">
                                            选择赠品
                                        </button>

                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                    class="fa fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <table class="table table-bordered">
                                        <thead>
                                        <tr>
                                            <th>GID</th>
                                            <th>sku</th>
                                            <th>商品名</th>
                                            <th>数量</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="selectedGiftTable">
                                        <#if actGiftDetailsDO??>
                                            <#list actGiftDetailsDO as item>
                                            <tr>
                                                <td><input id="gid" type='text' value="${item.giftId?c}"
                                                           style="width:90%;border: none;" readonly/></td>
                                                <td><input id='sku' type='text' value="${item.giftSku!''}"
                                                           style='width:90%;border: none;' readonly></td>
                                                <td><input id='title' type='text' value='${item.giftTitle!''}'
                                                           style='width:90%;border: none;' readonly></td>
                                                <td><input id='qty' type='number' value='${item.giftFixedQty?c}'></td>
                                                <td><a href='#' onclick='del_goods_comb(this);'>删除</td>
                                            </tr>
                                            </#list>
                                        </#if>
                                        </tbody>

                                    </table>
                                </div>
                                <!-- /.box-body -->

                                <div class="box-footer clearfix">
                                    <div class="row">

                                        <div class="col-xs-12 col-md-1">
                                            <div class="input-group">
                                                <input id="is_gift_optional_qty" type="checkbox" class="flat-red"
                                                       onclick="clickGiftFixedQty(this)" <#if actBaseDO??><#if actBaseDO.isGiftOptionalQty?? && actBaseDO.isGiftOptionalQty = true>checked</#if></#if>>任选数量
                                            </div>
                                        </div>
                                        <div id="gift_optional_qty_div" style="display: none;">
                                            <div class="col-xs-12 col-md-2">
                                                <div class="input-group">
                                                    <input name="giftChooseNumber" type="number" class="form-control"
                                                           id="giftChooseNumber" placeholder="赠品最大可选数量"
                                                           value="<#if actBaseDO??><#if actBaseDO.giftChooseNumber??>${actBaseDO.giftChooseNumber?c}</#if></#if>">
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <!-- /.box -->
                        </div>
                    </div>

                    <!-- 加价购金额 -->
                    <div class="row" id="addAmount_div" style="display: none;">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    加价购金额
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-cny"></i></span>
                                    <input name="addAmount" type="number" class="form-control" id="addAmount"
                                           value="<#if actBaseDO?? && actBaseDO.addAmount??>${actBaseDO.addAmount?c}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="fullAmount_div">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    排序号
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sortId" type="number" class="form-control" id="sortId"
                                           value="<#if actBaseDO??>${actBaseDO.sortId?c}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-2">
                            <button id="saveBtn" type="submit" class="btn btn-primary footer-btn">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button id="btn-cancel" type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 取消
                            </button>
                        </div>
                    </div>

                </form>

                <!-- 商品选择框 -->
                <div id="goodsModal" class="modal fade" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document" style="width: 60%">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4>选择商品</h4>
                            </div>
                            <div class="modal-body">
                                <!--  设置这个div的大小，超出部分显示滚动条 -->
                                <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                    <section class="content">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="box box-primary">
                                                    <div id="toolbar" class="form-inline">
                                                        <select name="brandCode" id="brandCode"
                                                                class="form-control select" style="width:auto;"
                                                                onchange="screenGoods()">
                                                            <option value="-1">选择品牌</option>
                                                        </select>
                                                        <select name="categoryCode" id="categoryCode"
                                                                class="form-control select" style="width:auto;"
                                                                onchange="screenGoods()">
                                                            <option value="-1">选择分类</option>
                                                        </select>
                                                        <select name="companyCode" id="companyCode"
                                                                class="form-control select" style="width:auto;"
                                                                onchange="screenGoods()">
                                                            <option value="-1">选择公司</option>
                                                            <option value="LYZ">乐易装</option>
                                                            <option value="HR">华润</option>
                                                            <option value="YR">莹润</option>
                                                        </select>
                                                        <div class="input-group col-md-3"
                                                             style="margin-top:0px positon:relative">
                                                            <input type="text" name="queryGoodsInfo" id="queryGoodsInfo"
                                                                   class="form-control" style="width:auto;"
                                                                   placeholder="请输入要查找的物料编码或物料名称..">
                                                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findGoodsByNameOrCode()">查找</button>
                        </span>
                                                        </div>
                                                    </div>
                                                    <div class="box-body table-reponsive">
                                                        <table id="goodsDataGrid"
                                                               class="table table-bordered table-hover">

                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="goodsModalConfirm" type="button" class="btn btn-primary">确定</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    $(function () {

        // 填充促销类型select
        var baseTypeVal = "<#if actBaseDO??>${actBaseDO.baseType!""}</#if>"
        var conditionTypeVal = "<#if actBaseDO??>${actBaseDO.conditionType!""}</#if>"
        var resultTypeVal = "<#if actBaseDO??>${actBaseDO.promotionType!""}</#if>"

        if (baseTypeVal != "") {
            $("#baseType").val(baseTypeVal);
        }

        if (conditionTypeVal != "") {
            $("#conditionType option[value='" + conditionTypeVal + "']").prop("selected", true);
        }

        if (resultTypeVal != null) {
            $("#resultType option[value='" + resultTypeVal + "']").prop("selected", true);
        }
        // js改变selectpicker值 需要refresh才生效
        $("select").each(function () {
            $(this).selectpicker('refresh');
        })
        changeConditionType($("#conditionType").val());
        changeResultType($("#resultType").val());
        clickGoodsFixedQty();
        clickGiftFixedQty();
        // 当前促销状态 如果已经发布 不能修改
        var status = "<#if actBaseDO??>${actBaseDO.status!""}</#if>"

        if (status == 'PUBLISH') {
            // 隐藏保存按钮
            $("#saveBtn").hide();
        }
    })

</script>
</body>
