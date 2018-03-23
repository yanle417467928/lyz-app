<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>

</head>
<body>

<section class="content-header">
    <h1>订单运费详情</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i>首页</a></li>
        <li><a href="javascript:history.back();">运费列表</a></li>
        <li class="active">订单运费详情</li>
    </ol>
</section>

<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab" ">订单详情</a></li>
            <li><a href="#tab_1-2" data-toggle="tab" ">商品详情</a></li>
        </ul>
        <div class="form-inline " style="margin-top: 10px;margin-left: 10px">
            <button id="btn_back" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
            </button>
        </div>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="cityName">城市
                                <i class="fa fa-question-circle  hidden-xs"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="city" type="text" class="form-control" id="city" readonly
                                       value="${(orderFreightDetailVO.cityName)!''}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="title">门店
                                <i class="fa fa-question-circle  hidden-xs" data-toggle="tooltip"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="sku" type="text" class="form-control" id="sku" readonly
                                       value="<#if orderFreightDetailVO??><#if orderFreightDetailVO.storeId??>${orderFreightDetailVO.storeId.storeName!''}</#if></#if>">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="cityName">订单号
                                <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="city" type="text" class="form-control" id="city" readonly
                                       value="${(orderFreightDetailVO.ordNo)!''}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="title">下单人
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                ></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="sku" type="text" class="form-control" id="sku" readonly
                                       value="${(orderFreightDetailVO.creatorName)!''}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="cityName">下单人电话
                                <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="city" type="text" class="form-control" id="city" readonly
                                       value="${(orderFreightDetailVO.creatorPhone)!''}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="title">下单时间
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="sku" type="text" class="form-control" id="sku" readonly
                                       value="${(orderFreightDetailVO.createTime?string("yyyy-MM-dd HH:mm:ss"))!''}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="cityName">运费(元)
                                <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="city" type="text" class="form-control" id="city" readonly
                                       value="<#if orderFreightDetailVO??><#if orderFreightDetailVO.simpleOrderBillingDetails??>${orderFreightDetailVO.simpleOrderBillingDetails.freight!''}</#if></#if>">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="title">商品总金额(元)
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="sku" type="text" class="form-control" id="sku" readonly
                                       value="${(orderFreightDetailVO.totalGoodsPrice)!''}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="cityName">收货人
                                <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="city" type="text" class="form-control" id="city" readonly
                                       value="<#if orderFreightDetailVO??><#if orderFreightDetailVO.orderLogisticsInfo??>${orderFreightDetailVO.orderLogisticsInfo.receiver!''}</#if></#if>">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="title">收货人电话
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="sku" type="text" class="form-control" id="sku" readonly
                                       value="<#if orderFreightDetailVO??><#if orderFreightDetailVO.orderLogisticsInfo??>${orderFreightDetailVO.orderLogisticsInfo.receiverPhone!''}</#if></#if>">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="cityName">收货人地址
                                <i class="fa fa-question-circle i-tooltip hidden-xs"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="city" type="text" class="form-control" id="city" readonly
                                       value="<#if orderFreightDetailVO??><#if orderFreightDetailVO.orderLogisticsInfo??>${orderFreightDetailVO.orderLogisticsInfo.shippingAddress!''}</#if></#if>">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-group">
                            <label for="title">备注
                                <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"></i>
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                <input name="sku" type="text" class="form-control" id="sku" readonly
                                       value="${(orderFreightDetailVO.remark)!''}">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tab-pane" id="tab_1-2">
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">
                        <tr class="text-center">
                            <td>商品编码</td>
                            <td>商品名称</td>
                            <td>商品数量</td>
                            <td>商品单价</td>
                            <td>商品金额</td>
                        </tr>
                    <#if orderFreightDetailVO??>
                        <#if orderFreightDetailVO.orderGoodsInfoList??>
                            <#list orderFreightDetailVO.orderGoodsInfoList as being>
                                <tr class="text-center">
                                    <td>${being.sku!''}</td>
                                    <td>${being.skuName!''}</td>
                                    <td>${being.orderQty?c}</td>
                                    <td> ¥ ${being.returnPrice!''}</td>
                                    <td> ¥ ${being.settlementPrice!''}</td>
                                </tr>
                            </#list>
                        </#if>
                    </#if>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>


<script>
    $(function () {
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });
    var changeDecimalBuZero= function (number, bitNum) {
        /// <summary>
        /// 小数位不够，用0补足位数
        /// </summary>
        /// <param name="number">要处理的数字</param>
        /// <param name="bitNum">生成的小数位数</param>
        var f_x = parseFloat(number);
        if (isNaN(f_x)) {
            return 0;
        }
        var s_x = number.toString();
        var pos_decimal = s_x.indexOf('.');
        if (pos_decimal < 0) {
            pos_decimal = s_x.length;
            s_x += '.';
        }
        while (s_x.length <= pos_decimal + bitNum) {
            s_x += '0';
        }
        return s_x;
    }
</script>
</body>