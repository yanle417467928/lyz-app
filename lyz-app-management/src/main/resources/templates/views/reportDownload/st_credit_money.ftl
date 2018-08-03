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
            <h1>装饰公司信用金余额</h1>
    </#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                            onchange="findStorelist()" data-live-search="true">
                    </select>

                    <select name="store" id="storeCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                    <#--onchange="findByCondition()"--> data-live-search="true">
                        <option value="-1">选择门店</option>
                        </select>
                                <div class="input-group col-md-2" style="margin-top:0px; positon:relative">
                                    <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control" style="width:auto;"
                                           placeholder="请输姓名或电话">
                                    <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findByOrderNumber()">查找</button>
                        </span>
                                </div>
                                <#--<@shiro.hasPermission name="/views/admin/resource/add">-->
                                <button id="btn_add" type="button" class="btn btn-default pull-left" onclick="openBillModal()">
                                    <i class="fa fa-download"></i>
                                    下载报表
                                </button>
                                <#--</@shiro.hasPermission>-->

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
        findCitylist();
        findStorelist();

        //获取数据
//        initDateGird(null,null,null,null,null,null);
        //时间选择框样式
//        $('.datepicker').datepicker({
//            format: 'yyyy-mm-dd',
//            language: 'zh-CN',
//            autoclose: true
//        });

    });

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
                });

                $("#cityCode").append(city);
                $("#cityCode").selectpicker('refresh');
                $("#cityCode").selectpicker('render');
            }
        });
    }

    function findStorelist() {
        initSelect("#storeCode", "选择门店");
        var store = "";
        var cityId = $('#cityCode').val();
        if (null == cityId || '' == cityId){
            cityId = 1;
        }
        $.ajax({
            url: '/rest/stores/find/company/StoresListByCityId/'+cityId,
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
                });
                $("#storeCode").append(store);
                $('#storeCode').selectpicker('refresh');
                $('#storeCode').selectpicker('render');
//                findByCondition();
            }
        });
    }

    function initDateGird(keywords,startTime,endTime,storeId,cityId) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/stcredit/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                storeId: storeId,
                startTime: startTime,
                endTime: endTime,
                cityId: cityId
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'city',
            title: '城市',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        },{
            field: 'maxCreditMoney',
            title: '信用额度',
            align: 'center'
        },{
            field: 'avaliableCreditMoney',
            title: '可用余额',
            align: 'center'
        }, {
            field: 'lastChangeTime',
            title: '上一次更新时间',
            align: 'center'
        }
        ]);
    }

    function findByCondition() {
        $("#queryCusInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var keywords = $('#queryCusInfo').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();
        initDateGird(keywords,startTime,endTime,storeId,storeType,cityId);
    }

    function findByOrderNumber() {
        /*$('#startTime').val('');
         $('#endTime').val('');
         $("#storeCode").val(-1);
         $('#cityCode').val(-1);
         $('#storeType').val('');
         $('#storeCode').selectpicker('refresh');
         $('#storeCode').selectpicker('render');
         $('#cityCode').selectpicker('refresh');
         $('#cityCode').selectpicker('render');
         $('#storeType').selectpicker('refresh');
         $('#storeType').selectpicker('render');*/
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        initDateGird(queryCusInfo,startTime,endTime,storeId,cityId);
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function openBillModal() {
        var keywords = $("#queryCusInfo").val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var storeId = $("#storeCode").val();
        var cityId = $('#cityCode').val();
        var storeType = $('#storeType').val();

        var url = "/rest/reportDownload/store/credit?keywords="+ keywords + "&storeId=" + storeId + "&startTime=" + startTime
            + "&endTime=" + endTime + "&cityId=" + cityId;
        var escapeUrl=url.replace(/\#/g,"%23");
        window.open(escapeUrl);

    }

</script>
</body>