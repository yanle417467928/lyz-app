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
    <h1>订单运费变更详情</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i>首页</a></li>
        <li><a href="javascript:history.back();">运费列表</a></li>
        <li class="active">订单运费变更详情</li>
    </ol>
</section>

<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab" ">运费变更详情</a></li>
        </ul>
        <div  id="toolbar"  class="form-inline " >
            <button id="btn_back" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回
            </button>
            <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                <input type="text" name="keywords" id="keywords" class="form-control "
                       style="width:auto;" placeholder="请输入单号、下单人或下单人电话.." onkeypress="findBykey()">
                <span class="input-group-btn">
                <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                        onclick="findByKeywords();">查找</button>
            </span>
            </div>
        </div>

        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
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
        initDateGird('/rest/orderFreight/page/freightChangeyGrid',null);
        $('#btn_back').on('click', function () {
            window.history.back()
        });
    });

    function initDateGird(url,keywords) {
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: keywords
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'id',
            title: 'ID',
            align: 'center',
            visible: false
        }, {
            field: 'cityName',
            title: '城市',
            align: 'center'
        }, {
            field: 'storeId.storeName',
            title: '门店',
            align: 'center'
        }, {
            field: 'ordNo',
            title: '订单号',
            align: 'center'
        }, {
            field: 'creatorName',
            title: '下单人',
            align: 'center',
        },  {
            field: 'orderFreightChange.freight',
            title: '修改前',
            align: 'center',
            formatter: function (value, row) {
                return '¥ '+changeDecimalBuZero(value,2);
            }
        },{
            field: 'orderFreightChange.changeAmount',
            title: '修改金额',
            align: 'center',
            formatter: function (value, row) {
                return '¥ '+changeDecimalBuZero(value,2);
            }
        },{
            field: 'orderFreightChange.freightChangeAfter',
            title: '修改后',
            align: 'center',
            formatter: function (value, row) {
                return '¥ '+changeDecimalBuZero(value,2);
            }
        },{
            field: 'orderFreightChange.changeType',
            title: '变更类型',
            align: 'center'
        },{
            field: 'orderFreightChange.modifier',
            title: '修改人',
            align: 'center'
        },{
            field: 'orderFreightChange.modifyTime',
            title: '修改时间',
            align: 'center',
            formatter: function (value, row) {
             return  formatDateTime(value);
        }
        }]);
    }
    function findByKeywords() {
        var keywords = $("#keywords").val();
        $("#dataGrid").bootstrapTable('destroy');
        initDateGird('/rest/orderFreight/page/freightChangeyGrid', keywords);
    }
    function findBykey(){
        if(event.keyCode==13){
            findByKeywords();
        }
    }

    var formatDateTime = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = dt.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = dt.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        var second = dt.getSeconds();
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
    };

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