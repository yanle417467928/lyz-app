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

    <script type="text/javascript" src="/javascript/activity/activity_add.js"></script>
</head>
<body>
<section class="content-header">
    <h1>购买产品券</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="produtCoupon_form">
                    <div class="row">
                        <div class="col-xs-12 col-md-2">
                            <label for="title">
                                城市
                            </label>
                            <select name="city" id="cityId" class="form-control select"
                                    onchange="findStoreByCity(this.value)">
                                <option value="-1">选择城市</option>
                            </select>
                        </div>

                        <div class="col-xs-12 col-md-2" style="margin-left: 180px;">
                            <label for="title">
                                门店
                            </label>
                            <select name="store" id="storeId" class="form-control select">
                                <option value="-1">选择门店</option>
                            </select>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                    <#--<div class="col-xs-12 col-md-2">-->
                        <div class="col-md-4 invoice-col">
                            <ul style="line-height: 40px;">
                                <li style="list-style: none">
                                    <button type="button" class="btn btn-primary btn-xs"
                                            onclick="openSellerModal()">
                                        选择导购
                                    </button>
                                </li>
                                <li style="list-style: none">
                                    <b>导购id:</b>
                                    <spanp id="sellerId" class="span"></spanp>
                                </li>
                                <li style="list-style: none">
                                    <b>导购姓名:</b>
                                    <spanp id="sellerName" class="span"></spanp>
                                </li>
                                <li style="list-style: none">
                                    <b>导购电话:</b>
                                    <spanp id="sellerPhone" class="span"></spanp>
                                </li>
                            </ul>
                        </div>
                        <div class="col-md-4 invoice-col">
                            <ul style="line-height: 40px;">
                                <li style="list-style: none">
                                    <button type="button" class="btn btn-primary btn-xs"
                                            onclick="openCustomerModal()">
                                        选择顾客
                                    </button>
                                </li>
                                <li style="list-style: none">
                                    <b>顾客id:</b>
                                    <spanp id="customerId" class="span"></spanp>
                                </li>
                                <li style="list-style: none">
                                    <b>顾客姓名:</b>
                                    <spanp id="customerName" class="span"></spanp>
                                </li>
                                <li style="list-style: none">
                                    <b>顾客电话:</b>
                                    <spanp id="customerPhone" class="span"></spanp>
                                </li>
                            </ul>
                        </div>
                    <#--</div>-->
                    </div>
                    <di>


                        <!-- 选择商品table -->
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box box-success">
                                    <div class="box-header">
                                        <h3 class="box-title">选择商品</h3>

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

                                        <div class="col-xs-12">
                                            <table class="table table-hover">
                                                <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>sku</th>
                                                    <th>商品名</th>
                                                    <th>数量</th>
                                                    <th>操作</th>
                                                </tr>
                                                </thead>
                                                <tbody id="selectedGoodsTable">

                                                </tbody>

                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 选择赠品table -->
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box box-success">
                                    <div class="box-header">
                                        <h3 class="box-title">选择赠品</h3>

                                        <div class="box-tools">
                                            <button type="button" class="btn btn-primary btn-xs"
                                                    onclick="openGoodsModal('selectedGoodsTable')">
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

                                        <div class="col-xs-12">
                                            <table class="table table-hover">
                                                <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>sku</th>
                                                    <th>商品名</th>
                                                    <th>数量</th>
                                                    <th>操作</th>
                                                </tr>
                                                </thead>
                                                <tbody id="selectedGoodsTable">

                                                </tbody>

                                            </table>
                                        </div>
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
                                        <input name="sortId" type="number" class="form-control" id="sortId" value="99">
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

                <!-- 导购选择框 -->
                <div id="selectSeller" class="modal fade" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document" style="width: 60%">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4>选择导购</h4>
                            </div>
                            <div class="modal-body">
                                <!--  设置这个div的大小，超出部分显示滚动条 -->
                                <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                    <section class="content">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="box box-primary">
                                                    <div id="sellerToolbar" class="form-inline">

                                                        <div class="input-group col-md-3"
                                                             style="margin-top:0px positon:relative">
                                                            <input type="text" name="sellerQueryConditions" id="sellerQueryConditions"
                                                                   class="form-control" style="width:auto;"
                                                                   placeholder="请输入导购姓名或电话">
                                                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findSellerByNameOrMobil()">查找</button>
                        </span>
                                                        </div>
                                                    </div>
                                                    <div class="box-body table-reponsive">
                                                        <table id="sellerDataGrid"
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
                                <button id="sellerModalConfirm" type="button" class="btn btn-primary">确定</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- 顾客选择框 -->
                <div id="selectCustomer" class="modal fade" tabindex="-1" role="dialog">
                    <div class="modal-dialog" role="document" style="width: 60%">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4>选择顾客</h4>
                            </div>
                            <div class="modal-body">
                                <!--  设置这个div的大小，超出部分显示滚动条 -->
                                <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                    <section class="content">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="box box-primary">
                                                    <div id="customerToolbar" class="form-inline">

                                                        <div class="input-group col-md-3"
                                                             style="margin-top:0px positon:relative">
                                                            <input type="text" name="queryGoodsInfo" id="queryGoodsInfo"
                                                                   class="form-control" style="width:auto;"
                                                                   placeholder="请输入顾客姓名或电话">
                                                            <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findGoodsByNameOrCode()">查找</button>
                        </span>
                                                        </div>
                                                    </div>
                                                    <div class="box-body table-reponsive">
                                                        <table id="customerDataGrid"
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
                                <button id="customerModalConfirm" type="button" class="btn btn-primary">确定</button>
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
        // 初始化城市信息
        findCitylist();
        //初始化门店信息
        findStorelist();
    })

    //获取城市列表
    function findCitylist() {
        var city = "";
        $.ajax({
            url: '/rest/citys/findCitylist',
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
                    city += "<option value=" + item.cityId + ">" + item.name + "</option>";
                })
                $("#cityId").append(city);
                $("#cityId").selectpicker('refresh');
            }
        });
    }

    //获取门店列表
    function findStorelist() {
        var store = "";
        $.ajax({
            url: '/rest/stores/findAllStorelist',
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
                $('#storeId').selectpicker('render');
            }
        });
    }

    //根据城市查询门店
    function findStoreByCity(cityId) {

        if (cityId == -1) {
            findStorelist();
            return false;
        }

        initSelect("#storeId", "选择门店");
        var cityId = $("#cityId").val();
        var store = "";
        $.ajax({
            url: '/rest/stores/findStoresListByCityId/' + cityId,
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
                $('#storeId').selectpicker('render');
            }
        });
    }

    function openSellerModal() {
        //查询导购列表
        initSeller('/rest/employees/select/seller');
        $("#sellerModalConfirm").unbind('click').click(function () {
        });
        $('#selectSeller').modal('show');
    }

    //条件查询导购
    function findSellerByNameOrMobil() {
        var sellerQueryConditions = $("#sellerQueryConditions").val();
        $("#sellerDataGrid").bootstrapTable('destroy');
        if (null == sellerQueryConditions || "" == sellerQueryConditions) {
            initSeller();
        }else{
            initGoodsGrid('/rest/goods/page/goodsGrid/' + sellerQueryConditions);
        }
    }


    //初始化导购选择框
    function initSeller(url) {
        var cityId = $('#cityId').val();
        var storeId = $('#storeId').val();
        $("#sellerDataGrid").bootstrapTable('destroy');
        $grid.init($('#sellerDataGrid'), $('#sellerToolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'empId',
            title: 'ID',
            align: 'center'
        }, {
            field: 'name',
            title: '导购姓名',
            align: 'center',

            events: {
                'click .scan': function (e, value, row) {
                    showSeller(row.empId, value, row.mobile);
                }
            },
            formatter: function (value) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="#' + value + '">' + value + '</a>';
                }
            }
        }, {
            field: 'mobile',
            title: '导购电话',
            align: 'center'
        }
        ]);
    }


    function openCustomerModal() {
        //查询导购列表
        initCustomer();
        $("#customerModalConfirm").unbind('click').click(function () {
        });
        $('#selectCustomer').modal('show');
    }


    //初始化导购选择框
    function initCustomer() {
        var cityId = $('#cityId').val();
        var storeId = $('#storeId').val();
        $("#customerDataGrid").bootstrapTable('destroy');
        var url = '/rest/customers/select/customer?cityId=' + cityId + '&storeId=' + storeId;
        $grid.init($('#customerDataGrid'), $('#customerToolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'cusId',
            title: 'ID',
            align: 'center'
        }, {
            field: 'name',
            title: '顾客姓名',
            align: 'center',
            events: {
                'click .scan': function (e, value, row) {
                    showCustomer(row.cusId, value, row.mobile);
                }
            },
            formatter: function (value) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="#' + value + '">' + value + '</a>';
                }
            }
        }, {
            field: 'mobile',
            title: '顾客电话',
            align: 'center'
        }
        ]);
    }

    //显示选中的导购
    function showSeller(id, name, phone) {
        $('#sellerId').text(id);
        $('#sellerName').text(name);
        $('#sellerPhone').text(phone);
        $('#selectSeller').modal('hide');
    }

    //显示选中的导购
    function showCustomer(id, name, phone) {
        $('#customerId').text(id);
        $('#customerName').text(name);
        $('#customerPhone').text(phone);
        $('#selectCustomer').modal('hide');
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }
</script>
</body>
