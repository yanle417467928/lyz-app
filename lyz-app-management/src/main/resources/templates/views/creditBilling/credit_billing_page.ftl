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
                <#--<@shiro.hasPermission name="/views/admin/resource/add">
                    <button id="btn_add" type="button" class="btn btn-default" onclick="openBillModal($('#dataGrid'))">
                        生成账单
                    </button>
                </@shiro.hasPermission>-->
                    <#--<input name="startTime" onchange="findByCondition()" type="text" class="form-control datepicker" id="startTime" placeholder="开始时间">
                    <input name="endTime" onchange="findByCondition()" type="text" class="form-control datepicker" id="endTime" placeholder="结束时间">-->

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <select name="isPayOff" id="isPayOff" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findByCondition()" data-live-search="true">
                        <option value="">选择还款状态</option>
                        <option value="false">未还清</option>
                        <option value="true">已还清</option>
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

</section>
<script>

    $(function () {
        findStorelist();

        //获取数据
        initDateGird(null,null,null,null,null);
        //时间选择框样式
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
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
            }
        });
    }

    function initDateGird(keywords,startTime,endTime,storeId,isPayOff) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/decorationCompany/creditBilling/page', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                storeId: storeId,
                startTime: startTime,
                endTime: endTime,
                isPayOff: isPayOff
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'creditBillingNo',
            title: '账单单号',
            align: 'center'
        }, {
            field: 'storeName',
            title: '装饰公司',
            align: 'center'
        }, {
            field: 'cycleTime',
            title: '记账周期',
            align: 'center'
        },  {
            field: 'billName',
            title: '账单名称',
            align: 'center'
        }, {
            field: 'billAmount',
            title: '账单总额(￥)',
            align: 'center'
        }, {
            field: 'repaidAmount',
            title: '已还金额(￥)',
            align: 'center'
        }, {
            field: 'isPayOff',
            title: '还款状态',
            align: 'center',
            formatter: function (value, row, index) {
                if (value) {
                    return '已还清';
                } else {
                    return '未还清';
                }
            }
        }, {
            field: 'createTime',
            title: '创建时间',
            align: 'center'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            formatter: function(value,row) {
                return '<button class="btn btn-primary btn-xs" onclick="showDetails('+row.id+')"> 查看账单</button>';
            }
        }
        ]);
    }

    function findByCondition() {
        $("#queryCusInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var isPayOff = $('#isPayOff').val();
        var storeId = $("#storeCode").val();
        initDateGird(keywords,startTime,endTime,storeId,isPayOff);
    }

    function findByOrderNumber() {
        $('#startTime').val('');
        $('#endTime').val('');
        var isPayOff = $('#isPayOff').val("");
        var storeId = $("#storeCode").val(-1);
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var isPayOff = $('#isPayOff').val();
        var storeId = $("#storeCode").val();
        initDateGird(queryCusInfo,startTime,endTime,storeId,isPayOff);
    }



    function showDetails(id){
        window.location.href = '/views/decorationCompany/creditBilling/'+id;
    }

</script>
</body>