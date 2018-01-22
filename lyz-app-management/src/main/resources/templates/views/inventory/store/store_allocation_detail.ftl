<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">

    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>

    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>

    <script type="text/javascript" src="/javascript/common/form_common.js"></script>
    <script type="text/javascript" src="/javascript/storeAllocation/store_allocation_detail.js"></script>

</head>
<body>

<section class="content-header">
    <h1>拨单详情</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="allocation_form" enctype="multipart/form-data">
                    <input type="hidden" id="allocationId" value="<#if allocation?? && allocation.id??>${allocation.id?c}</#if>"/>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    调出门店
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-truck"></i></span>
                                    <input type="text" class="form-control"
                                           value="<#if allocation?? && allocation.allocationFrom??>${allocation.allocationFromName!""}</#if>"
                                           readonly>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    调入门店
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-truck"></i></span>
                                    <input type="text" class="form-control"
                                           value="<#if allocation?? && allocation.allocationFrom??>${allocation.allocationToName!""}</#if>"
                                           readonly>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="comment">
                                    调拨单号
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="comment" type="text" class="form-control" id="comment"
                                           value="<#if allocation?? && allocation.number??>${allocation.number!""}</#if>"
                                           readonly>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="comment">
                                    备注
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="comment" type="text" class="form-control" id="comment"
                                           value="<#if allocation?? && allocation.comment??>${allocation.comment!""}</#if>"
                                           readonly>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 选择本品table -->
                    <div class="row" id="goods_div">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header with-border">
                                    <h3 class="box-title">调拨商品</h3>

                                    <div class="box-tools">

                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                    class="fa fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding" style="height: 300px;overflow: auto;">

                                    <div class="col-xs-12">
                                        <table class="table table-bordered">
                                            <thead>
                                            <tr>
                                                <th>产品ID</th>
                                                <th>产品编码</th>
                                                <th>产品名</th>
                                                <th>申请数量</th>
                                                <th>真实数量</th>

                                            </tr>
                                            </thead>
                                            <tbody id="selectedGoodsTable">
                                            <#if details??>
                                                <#list details as item>
                                                <tr>
                                                    <td><input id='goodsId' type='text' value="${item.goodsId?c}"
                                                               style='width:90%;border: none;' readonly></td>
                                                    <td><input id='sku' type='text' value="${item.sku!''}"
                                                               style='width:90%;border: none;' readonly></td>
                                                    <td><input id='skuName' type='text' value='${item.skuName!''}'
                                                               style='width:90%;border: none;' readonly></td>
                                                    <td><input id='qty' type='number' value='${item.qty?c}' readonly>
                                                    </td>
                                                    <td><input id='realQty' type='number' value='${item.qty?c}'></td>

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
                    <!--调拨轨迹-->

                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-success">
                                <div class="box-header with-border">
                                    <h3 class="box-title">调拨轨迹</h3>

                                    <div class="box-tools">

                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                                    class="fa fa-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <ul class="timeline">

                                    <#if trails??>

                                        <#list trails as item>
                                            <li class="time-label">
                                                    <span class="bg-red">
                                                    ${item.operateTime?string("yyyy-MM-dd")}
                                                    </span>
                                            </li>
                                            <#if item.operation == "NEW">
                                                <li>
                                                    <!-- timeline icon -->
                                                    <i class="fa fa-check bg-blue"></i>
                                                    <div class="timeline-item">
                                                        <span class="time"><i
                                                                class="fa fa-clock-o"></i> ${item.operateTime?string("hh:mm:ss")}</span>
                                                        <h3 class="timeline-header"><a>新建</a></h3>

                                                        <div class="timeline-body">
                                                            操作人 ： ${item.operator}
                                                        </div>
                                                    </div>
                                                </li>
                                            <#elseif item.operation == "SENT">
                                                <li>
                                                    <!-- timeline icon -->
                                                    <i class="fa fa-check bg-green"></i>
                                                    <div class="timeline-item">
                                                        <span class="time"><i
                                                                class="fa fa-clock-o"></i> ${item.operateTime?string("hh:mm:ss")}</span>
                                                        <h3 class="timeline-header"><a>出库</a></h3>

                                                        <div class="timeline-body">
                                                            操作人 ： ${item.operator}
                                                        </div>
                                                    </div>
                                                </li>
                                            <#elseif item.operation == "ENTERED">
                                                <li>
                                                    <!-- timeline icon -->
                                                    <i class="fa fa-check bg-green"></i>
                                                    <div class="timeline-item">
                                                        <span class="time"><i
                                                                class="fa fa-clock-o"></i> ${item.operateTime?string("hh:mm:ss")}</span>
                                                        <h3 class="timeline-header"><a>入库</a></h3>

                                                        <div class="timeline-body">
                                                            操作人 ： ${item.operator}
                                                        </div>
                                                    </div>
                                                </li>
                                            <#elseif item.operation == "CANCELLED">
                                                <li>
                                                    <!-- timeline icon -->
                                                    <i class="fa fa-check bg-red"></i>
                                                    <div class="timeline-item">
                                                        <span class="time"><i
                                                                class="fa fa-clock-o"></i> ${item.operateTime?string("hh:mm:ss")}</span>
                                                        <h3 class="timeline-header"><a>作废</a></h3>

                                                        <div class="timeline-body">
                                                            操作人 ： ${item.operator}
                                                        </div>
                                                    </div>
                                                </li>
                                            </#if>

                                        </#list>

                                    </#if>

                                        <!-- END timeline item -->
                                    </ul>

                                    <!-- /.box-body -->
                                </div>
                                <!-- /.box-body -->
                            </div>
                            <!-- /.box -->
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6"></div>

                    <#if allocation?? && allocation.status??>
                        <#if allocation.status == "NEW">
                            <div class="col-xs-12 col-md-2">
                                <button id="sent_btn" type="button" class="btn btn-primary footer-btn">
                                    <i class="fa fa-check"></i> 出库
                                </button>
                            </div>
                            <div class="col-xs-12 col-md-2">
                                <button id="cancelled_btn" type="button" class="btn btn-warning footer-btn">
                                    <i class="fa fa-close"></i> 作废
                                </button>
                            </div>
                        <#elseif allocation.status == "SENT">
                            <div class="col-xs-12 col-md-2">
                                <button id="entered_btn" type="button" class="btn btn-success footer-btn">
                                    <i class="fa fa-check"></i> 入库
                                </button>
                            </div>
                            <div class="col-xs-12 col-md-2">
                                <button id="cancelled_btn" type="button" class="btn btn-warning footer-btn">
                                    <i class="fa fa-close"></i> 作废
                                </button>
                            </div>
                        <#elseif allocation.status == "ENTERED">
                            <div class="col-xs-12 col-md-2">

                            </div>
                            <div class="col-xs-12 col-md-2">

                            </div>
                        <#elseif allocation.status == "CANCELLED">
                            <div class="col-xs-12 col-md-2">

                            </div>
                            <div class="col-xs-12 col-md-2">

                            </div>
                        </#if>
                    </#if>

                        <div class="col-xs-12 col-md-2">
                            <button id="btn-cancel" type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 返回
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