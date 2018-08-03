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
    <h1>装饰公司信用金变更记录</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="nav-tabs-custom">
                <div id="toolbar" class="form-inline ">
                    <input type="hidden" is="storeCode" value="${storeId?c}">
                    <button id="btn_back" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
                    </button>
                    <select name="changeType" id="changeType" class="form-control selectpicker" data-width="140px"
                            style="width:auto;"
                            onchange="findStoreCreByChangeType(this.value)" data-live-search="true">
                        <option value="-1">选择变更类型</option>
                    <#if changeTypes??>
                        <#list changeTypes as changeType>
                            <option value="${changeType.value}">${changeType.description}</option>
                        </#list>
                    </#if>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCusInfo" id="queryCusInfo" class="form-control"
                               style="width:auto;"
                               placeholder="请输入相关单号"
                               onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findStoreCreByInfo()">查找</button>
                        </span>
                    </div>
                </div>
                <div class="box-body table-reponsive">
                    <input type="hidden" name="storeId" id="storeId" value="<#if storeId??>${storeId?c}<#else>0</#if>">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>

</section>


<script>

    $(function () {
        showAvailableCredit(null, null);
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });


    function showAvailableCredit(keywords, changeType) {
        $("#dataGrid").bootstrapTable('destroy');
        var storeId = $('#storeId').val();
        url = '/rest/decorativeCredit/fitCreditlog/page/grid/' + storeId
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords,
                changeType: changeType
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'storeId',
                title: 'ID',
                align: 'center',
                visible: false,
                formatter: function (value) {
                    if (null == value) {
                        return '未知';
                    } else {
                        return '' + value;
                    }
                }
            }, {
                field: 'storeName',
                title: '装饰公司名称',
                align: 'center'
            }, {
                field: 'storeCode',
                title: '装饰公司编码',
                align: 'center'
            },
            {
                field: 'storeType',
                title: '装饰公司类型',
                align: 'center'
            },
            {
                field: 'changeType',
                title: '变更类型',
                align: 'center'
            },
            {
                field: 'changeAmount',
                title: '变更金额(￥)',
                align: 'center'
            },
            {
                field: 'creditLimitAvailableAfterChange',
                title: '变更后余额(￥)',
                align: 'center'
            },
            {
                field: 'referenceNumber',
                title: '相关单号',
                align: 'center'
            },
            {
                field: 'createTime',
                title: '变更时间',
                align: 'center'
            },
            {
                field: 'operatorName',
                title: '变更人',
                align: 'center'
            },{
                field: 'remark',
                title: '备注',
                align: 'left'
            }
        ]);
    }


    function findBykey() {
        if (event.keyCode == 13) {
            findStoreCreByNameOrCode();
        }
    }

    function findStoreCreByInfo() {
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        var changeType = $("#changeType").val();
        showAvailableCredit(queryCusInfo, changeType);
    }


    function findStoreCreByChangeType(changeType) {
        $("#queryCusInfo").val('');
        var queryCusInfo = $("#queryCusInfo").val();
        $("#dataGrid").bootstrapTable('destroy');
        if('-1'==changeType){
            showAvailableCredit(null, null);
        }else{
            showAvailableCredit(null, changeType);
        }

    }

</script>
</body>