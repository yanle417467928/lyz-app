<head>
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="/plugins/iCheck/all.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/plugins/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">

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
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>

    <script type="text/javascript" src="/javascript/common/form_common.js"></script>
    <script type="text/javascript" src="/javascript/deliveryFeeRule/deliveryFeeRule_edit.js"></script>

</head>

<body>
<section class="content-header">
    <h1>编辑运费规则</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="delliveryFeeRule_form">
                    <input name="id" type="hidden" value="<#if rule?? && rule.id??>${rule.id?c}</#if>">

                    <div class="row">
                        <div class="col-xs-12 col-md-2">
                            <div class="form-group">
                                <label for="title">
                                    城市
                                </label>
                                <select name="cityId" id="cityId" class="form-control select">
                                <#if cityList??>
                                    <#list cityList as item>
                                        <option value="${item.cityId}" <#if item.cityId == rule.cityId >
                                                selected</#if>>${item.name}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- 满足最低金额 -->
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    订单满足最低金额￥
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-cny"></i></span>
                                    <input name="condition" type="number" class="form-control" id="condition" value="<#if rule?? && rule.condition??>${rule.condition?c}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 面额 -->
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="deliveryFee">
                                    运费￥
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-cny"></i></span>
                                    <input name="deliveryFee" type="number" class="form-control" id="deliveryFee" value="<#if rule?? && rule.deliveryFee??>${rule.deliveryFee?c}</#if>">
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
                                    <input name="target" value="6" type="checkbox" class="flat-red" <#if rule??><#if rule.tollObject?? && rule.tollObject?contains('6')>checked</#if></#if>>顾客
                                    <input name="target" value="0" type="checkbox" class="flat-red" <#if rule??><#if rule.tollObject?? && rule.tollObject?contains('0')>checked</#if></#if>>导购
                                    <input name="target" value="2" type="checkbox" class="flat-red" <#if rule??><#if rule.tollObject?? && rule.tollObject?contains('2')>checked</#if></#if>>装饰公司经理
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="includeSpecialGoods">是否添加特殊商品</label>
                                <br>
                                <input name="includeSpecialGoods" class="switch" id="includeSpecialGoods" type="checkbox" onchange="AddSpecialGoods()"
                                       data-on-text="是" data-off-text="否" <#if rule??><#if rule.includeSpecialGoods?? && rule.includeSpecialGoods == true>checked</#if></#if>/>
                            </div>
                        </div>
                    </div>

                    <!-- 选择本品table -->
                    <div class="row" id="goods_div" style="display: none;">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header">
                                    <h3 class="box-title">选择特殊商品（不对此范围商品计算运费）</h3>

                                    <div class="box-tools">
                                        <button id="chooseGoodsButton" type="button" class="btn btn-primary btn-xs"
                                        >
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

                                    <div class="col-xs-12">
                                        <table class="table table-hover">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>sku</th>
                                                <th>商品名</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody id="selectedGoodsTable">
                                            <#if goods??>
                                                <#list goods as item>
                                                <tr>
                                                    <td><input id="gid" type='text'  value="${item.goodsId?c}" style="width:90%;border: none;" readonly /></td>
                                                    <td><input id='sku' type='text' value="${item.sku!''}" style='width:90%;border: none;' readonly></td>
                                                    <td><input id='title' type='text' value='${item.goodsName!''}' style='width:90%;border: none;' readonly></td>
                                                    <td><a href='#' onclick='del_goods_comb(this);'>删除</td>
                                                </tr>
                                                </#list>
                                            </#if>
                                            </tbody>

                                        </table>
                                    </div>


                                    <!-- /.box-body -->
                                </div>
                                <!-- /.box-body -->
                            </div>
                            <!-- /.box -->
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="status">是否启用</label>
                                <br>
                                <input name="status" class="switch" id="status" type="checkbox" checked
                                       data-on-text="启用" data-off-text="停用" <#if rule??><#if rule.status?? && rule.status == true>checked</#if></#if>/>
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
        // 初始化城市、门店信息
        $commonForm.city("/rest/citys/findCitylist", "cityId", "");

        // 特殊商品
        AddSpecialGoods();
    })

</script>
</body>