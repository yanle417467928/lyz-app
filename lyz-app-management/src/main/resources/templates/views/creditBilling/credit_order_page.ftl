<head>

    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>

</head>

<body>
<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parentResourceName??>
            <li><a href="javascript:void(0);">${selectedMenu.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>加载中...</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                   <#-- <@shiro.hasPermission name="/views/admin/resource/add">-->
                        <button id="btn_add" type="button" class="btn btn-default" onclick="openBillModal($('#dataGrid'))">
                            生成账单
                        </button>
                    <#--</@shiro.hasPermission>-->
                    <input name="startTime" onchange="findByCondition()" type="text" class="form-control datepicker" id="startTime" placeholder="开始时间">
                    <input name="endTime" onchange="findByCondition()" type="text" class="form-control datepicker" id="endTime" placeholder="结束时间">

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                    </select>

                    <div class="input-group col-md-3" style="margin-top:0px; positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                               placeholder="请输入要查找的单号">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findByOrderNumber()">查找</button>
                        </span>
                    </div>

                </div>

                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="billModal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document" style="width: 30%">
            <div class="modal-content">
                <div class="modal-header">
                    <h4>新建账单</h4>
                </div>
                <form id="createCreditBilling">
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-5">
                                <label for="billName">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;
                                    名&nbsp;&nbsp;&nbsp;&nbsp;称</label>
                                </div><div class="col-md-6">
                                <input type="text" class="form-control" id="billName" name="billName">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-5">
                                <label for="storeName">装&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;饰&nbsp;&nbsp;&nbsp;&nbsp;
                                    公&nbsp;&nbsp;&nbsp;&nbsp;司</label>
                                </div><div class="col-md-6">
                                <input type="text" class="form-control" id="storeName" name="storeName" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-5">
                                <label for="allCreditMoney">账&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;总&nbsp;&nbsp;&nbsp;金&nbsp;&nbsp;额</label>
                                </div><div class="col-md-6">
                                <input type="text" class="form-control" id="billAmount" name="billAmount" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-5">
                                <label for="name">记账周期开始日期</label>
                                </div><div class="col-md-6">
                                <input type="text" class="form-control datepicker" id="startTime" name="startTime">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-5">
                                <label for="name">记账周期结束日期</label>
                                </div><div class="col-md-6">
                                <input type="text" class="form-control datepicker" id="endTime" name="endTime">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success "
                                class="btn btn-default" id="confirmSubmit">确定
                        </button>
                        <button type="button" class="btn btn-default"
                                data-dismiss="modal">取消
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<script>

    $(function () {
        findStorelist();

        $('#createCreditBilling').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                billName: {
                    message: '账单名称校验失败',
                    validators: {
                        notEmpty: {
                            message: '账单名称为空!'
                        },
                        stringLength: {
                            min: 2,
                            max: 20,
                            message: '账单名称在2~20位之间'
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
            var orderNumbers = [];
            var selected = $('#dataGrid').bootstrapTable('getSelections');
            for (var i = 0; i < selected.length; i++) {
                var data1 = selected[i];
                orderNumbers.push(data1.orderNumber);
            }
            if (null === orderNumbers || 0 === orderNumbers.length) {
                $notify.warning('请在点击按钮前选中至少一条数据');
                return;
            }
            var storeId = $("#storeCode").val();
            data["orderNumbers"] = orderNumbers;
            data["storeId"] = storeId;

            if (null === $global.timer) {
                $global.timer = setTimeout($loading.show, 2000);

                var url = '/rest/decorationCompany/creditBilling/create/creditBilling';
                $.ajax({
                    url: url,
                    method: 'POST',
                    data: data,
                    error: function () {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger('网络异常，请稍后重试或联系管理员');
                        $('#createCreditBilling').bootstrapValidator('disableSubmitButtons', false);
                    },
                    success: function (result) {
                        if (0 === result.code) {
                            window.location.href = document.referrer;
                        } else {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger(result.message);
                            $('#createCreditBilling').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }
                });
            }
        });
    });

    function findStorelist() {
        var store = "";
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
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
                //获取数据
                initDateGird(null,null,null);
                //时间选择框样式
                $('.datepicker').datepicker({
                    format: 'yyyy-mm-dd',
                    language: 'zh-CN',
                    autoclose: true
                });
            }
        });
    }

    function initDateGird(keywords,startTime,endTime) {
        var storeId = $("#storeCode").val();
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/decorationCompany/creditBilling/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                storeId: storeId,
                startTime: startTime,
                endTime: endTime
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'orderNumber',
            title: '订单/退单编号',
            align: 'center'
        }, {
            field: 'storeName',
            title: '装饰公司',
            align: 'center'
        }, {
            field: 'creatorName',
            title: '下单人',
            align: 'center'
        },  {
            field: 'createTime',
            title: '出/退货时间',
            align: 'center'
        }, {
            field: 'deliveryAddress',
            title: '送货地址',
            align: 'center',
            width: '25%'
        }, {
            field: 'creditMoney',
            title: '欠款金额(￥)',
            align: 'center'
        }
        ]);
    }

    /*function findByTimeOrstoreIdOrKeywords(keywords,startTime,endTime){
        initDateGird(keywords,startTime,endTime);
    }*/

    function findByCondition() {
        $("#queryCusInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        initDateGird(keywords,startTime,endTime);

    }

    function findByOrderNumber() {
        $('#startTime').val('');
        $('#endTime').val('');
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        initDateGird(queryCusInfo,startTime,endTime);
    }

    function openBillModal(container) {
        var orderNumbers = [];
        var selected = container.bootstrapTable('getSelections');
        for (var i = 0; i < selected.length; i++) {
            var data = selected[i];
            orderNumbers.push(data.orderNumber);
        }
        if (null === orderNumbers || 0 === orderNumbers.length) {
            $notify.warning('请在点击按钮前选中至少一条数据');
            return;
        }
        var data = {};
        var storeId = $("#storeCode").val();
        var storeName = $("#storeCode").find("option:selected").text().trim();
        $("#storeName").val(storeName);
        data["orderNumbers"] = orderNumbers;
        data["storeId"] = storeId;
        $.ajax({
            url: '/rest/decorationCompany/creditBilling/billInfo',
            method: 'GET',
            data: data,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);
                $("#billAmount").val(parseFloat(result));
                $("#billModal").unbind('click').click(function(){
                    /*chooseCity(id);*/
                });
                $('#billModal').modal('show');
            }
        });

    }
</script>
</body>