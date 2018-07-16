<head>

    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
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
                <div id="toolbar" class="form-inline" >
                    <form>
                    <#--<@shiro.hasPermission name="/views/admin/resource/add">-->
                        <button id="btn_add" type="button" class=" btn btn-default" onclick="openBillModal()">
                            <i class="fa fa-download"></i>
                            下载报表
                        </button>
                    <#--</@shiro.hasPermission>-->
                        <select name="company" id="companyCode" class="form-control" data-width="120px"
                                style="width:auto;"
                                onchange="findStorelist()" data-live-search="true">
                            <option value="-1">选择分公司</option>
                        <#if structureList?? && structureList?size gt 0 >
                            <#list structureList as structure>
                                <option value="${structure.number!''}">${structure.structureName!''}</option>
                            </#list>
                        </#if>
                        </select>
                        <select name="storeType" id="storeType" class="form-control" style="width:auto;"
                                onchange="findStorelist()" data-live-search="true">
                            <option value="-1">选择门店类型</option>
                            <option value="ZY">直营</option>
                            <option value="JM">加盟</option>
                            <option value="FX">分销</option>
                        </select>
                        <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px"
                                style="width:auto;"
                        <#--onchange="findByCondition()"--> data-live-search="true">
                            <option value="-1">选择门店</option>
                        </select>
                        <input name="endTime" onchange="" type="text" class="form-control datepicker"
                               id="endTime" style="width: 120px;" placeholder="截止时间">
                        <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                onclick="return findByCondition()">查找
                        </button>
                        <button type="reset" class="btn btn-default" onclick="clearAll()">重置
                        </button>
                    </form>
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
        //时间选择框样式
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });
        findStorelist();
    });


    function initDateGird(keywords, companyCode, storeType,storeCode,endTime) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/arrearsReport/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                companyCode: companyCode,
                storeType: storeType,
                storeId: storeCode,
                endTime: endTime
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'cityName',
            title: '城市',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        }, {
            field: 'name',
            title: '导购名称',
            align: 'center'
        }, {
            field: 'customerName',
            title: '会员名称',
            align: 'center'
        }, {
            field: 'ordNo',
            title: '订单号',
            align: 'center'
        }, {
            field: 'orderArrearage',
            title: '订单欠款',
            align: 'center'
        }, {
            field: 'payUpMoney',
            title: '订单已支付总金额',
            align: 'center'
        }, {
            field: 'isPayUp',
            title: '是否结清',
            align: 'center'
        }, {
            field: 'payUpTime',
            title: '结清时间',
            align: 'center'
        }
        ]);
    }

    function findByCondition() {
        $("#queryCusInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        var storeType = $('#storeType').val();
        var companyCode = $('#companyCode').val();
        var storeCode = $('#storeCode').val();
        var endTime = $('#endTime').val();
        initDateGird(keywords, companyCode, storeType,storeCode,endTime);
    }


    function openBillModal() {
        var companyCode = $('#companyCode').val();
        var storeType = $('#storeType').val();
        var storeCode = $('#storeCode').val();
        var url = "/rest/reportDownload/arrearsReport/download?companyCode=" + companyCode
                + "&storeType=" + storeType+"&storeId="+storeCode;
        var escapeUrl = url.replace(/\#/g, "%23");
        window.open(escapeUrl);

    }

    function clearAll() {
        $('select').prop('selectedIndex', 0);
        $("select").selectpicker('refresh');
        $("#dataGrid").bootstrapTable('destroy');
    }

    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function findStorelist() {
        initSelect("#storeCode", "选择门店");
        var store = "";
        var companyCode = $('#companyCode').val();
        var storeType = $('#storeType').val();
  /*      if('FX'==storeType){
            $('#companyCode').val(-1);
            $('#storeCode').val(-1);
            companyCode=-1;
            $('#companyCode').selectpicker('refresh');
            $('#companyCode').selectpicker('render');
        }*/
        $.ajax({
            url: '/rest/stores/findStoresListByCompanyCodeAndStoreType',
            method: 'GET',
            data: {
                storeType: storeType,
                companyCode: companyCode
            },
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
                });
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
            }
        });
    }

</script>
</body>