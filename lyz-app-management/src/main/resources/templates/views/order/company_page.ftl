<head>

    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/lightbox2/2.10.0/css/lightbox.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/lightbox2/2.10.0/js/lightbox.js"></script>

    <script type="text/javascript" src="/javascript/order/company_page.js"></script>
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
    <div class="box box-primary" style="padding: 10px  2%">
        <form id="">
            <div class="form-inline">
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">开始时间:</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input name="beginTime" type="text" class="form-control datepicker" id="beginTime"
                                        placeholder="开始时间">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">结束时间：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input name="endTime" type="text" class="form-control datepicker" id="endTime"
                                           placeholder="开始时间">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人姓名：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="creatorName" id="creatorName" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">收货地址:</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="shippingAddress" id="shippingAddress" class="form-control"
                                           \>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">收货人姓名:</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="receiverName" id="receiverName" class="form-control" \>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">下单人电话：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="creatorPhone" id="creatorPhone" class="form-control" \>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">

                        <div class="col-xs-4">
                            <div class="col-xs-11">
                                <label class="col-xs-5" style="padding-right: 0px">收货人电话：</label>
                                <div class=" col-xs-6" style="padding-left: 0px">
                                    <input type="text" name="receiverPhone" id="receiverPhone" class="form-control" \>
                                </div>
                            </div>
                        </div>
                        <div class=" col-xs-3">
                            <select name="status" id="status" class="form-control select"
                                    onchange="findOrderByOrderNumber()">
                                <option value="">选择订单状态</option>
                            <#if orderStatusList?? && orderStatusList?size gt 0 >
                                <#list orderStatusList as orderStatus>
                                    <option value="${orderStatus.value!''}">${orderStatus.description!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="margin-top: 5px">
                        <div class="col-xs-7">
                            <div class="col-xs-12">
                                <div class=" col-xs-3">
                                    <select name="city" id="cityCode" class="form-control select"
                                            onchange="findStoreByCity(this.value)">
                                        <option value="-1">选择城市</option>
                                    </select>
                                </div>
                                <div class=" col-xs-6">
                                    <select name="store" id="storeCode" class="form-control selectpicker"
                                            onchange="findOrderByOrderNumber()" data-live-search="true">
                                        <option value="-1">选择门店</option>
                                    </select>
                                </div>

                                <div class=" col-xs-3">
                                    <select name="company" id="company" class="form-control select"
                                            onchange="findOrderByOrderNumber()">
                                        <option value="">选择装饰公司类型</option>
                                        <option value="MONTHLY">大型装饰公司</option>
                                        <option value="CASH">小型装饰公司</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-5">
                            <div class="col-xs-12">
                                <input type="text" name="orderNumber" id="orderNumber" class="form-control"
                                       style="width:auto;"
                                       placeholder="请输订单号" onkeypress="findBykey()">
                                <span class="">
                                <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                        onclick="return findOrderByOrderNumber()">查找</button>
                        </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                <#--<button id="btn_edit" type="button" class="btn btn-default">-->
                <#--<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑-->
                <#--</button>-->
                </div>
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<script>
    $(function () {

        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/goods/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
    });

</script>
</body>