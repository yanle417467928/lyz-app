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
                <div id="toolbar" class="form-inline">
                    <select name="city" id="cityCode" class="form-control selectpicker" data-width="120px" style="width:auto;"
                             data-live-search="true">
                        <option value="-1">选择城市</option>
                    </select>

                    <input name="startTime" <#--onchange="findByCondition()"--> type="text" class="form-control datepicker" id="startTime" style="width: 120px;" placeholder="开始时间">
                    <input name="endTime" <#--onchange="findByCondition()"--> type="text" class="form-control datepicker" id="endTime" style="width: 120px;" placeholder="结束时间">

                    <div class="input-group col-md-3" style="margin-top:0px; positon:relative">
                        <input type="text" name="queryInfo" id="queryInfo" class="form-control" style="width:auto;"
                               placeholder="请输入要查找的单号/客服名字">
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

        //获取数据
//        initDateGird(null,null,null,null,null,null);
        //时间选择框样式
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

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


    function initDateGird(keywords,startTime,endTime,cityId) {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/reportDownload/photoOrderCheck/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                startTime: startTime,
                endTime: endTime,
                cityId: cityId
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'cityName',
            title: '城市',
            align: 'center'
        }, {
            field: 'photoOrderNo',
            title: '单号',
            align: 'center'
        }, {
            field: 'userName',
            title: '下单人姓名',
            align: 'center'
        },  {
            field: 'orderType',
            title: '订单类型',
            align: 'center'
        }, {
            field: 'createTime',
            title: '创建时间',
            align: 'center'
        }, {
            field: 'finishTime',
            title: '完结时间',
            align: 'center'
        }, {
            field: 'operationUser',
            title: '完结人',
            align: 'center'
        }
        ]);
    }

    function findByOrderNumber() {

        var queryInfo = $("#queryInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var cityId = $('#cityCode').val();
        initDateGird(queryInfo,startTime,endTime,cityId);
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=''>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function openBillModal() {
        var keywords = $("#queryInfo").val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var cityId = $('#cityCode').val();

        var url = "/rest/reportDownload/photoOrderCheck/download?keywords="+ keywords + "&startTime=" + startTime
                + "&endTime=" + endTime + "&cityId=" + cityId;
        var escapeUrl=url.replace(/\#/g,"%23");
        window.open(escapeUrl);

    }

</script>
</body>