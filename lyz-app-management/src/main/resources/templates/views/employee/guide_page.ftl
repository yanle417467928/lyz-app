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
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline ">
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="clearTime_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 自动清零设置
                    </button>
                    <select name="city" id="cityCode" class="form-control select" style="margin-left:30px width:auto;"
                            onchange="findStoreByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="storeCode" id="storeCode" class="form-control selectpicker" data-width="120px" style="margin-left:10px width:auto;"
                      onchange=" findGuideCreditMoneyByCondition()"  data-live-search="true" >
                        <option value="-1">选择门店</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryGuideVOInfo" id="queryGuideVOInfo" class="form-control" style="width:auto;"
                               placeholder="请输入姓名或登录名...">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="findGuideCreditMoneyByInfo()">查找</button>
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


<div class="modal fade" id="delcfmModel" >
    <div class="modal-dialog" style="width: 30%;">
        <div class="modal-content message_align">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title">提示信息</h4>
            </div>
            <div class="modal-body">
                <p>您确认要将临时额度清零吗？</p>
            </div>
            <div class="modal-footer">
                <input type="hidden" id="tempCreditLimit"/>
                <a  onclick="clearSubmit(this.value)" class="btn btn-danger" data-dismiss="modal" id="delId" value="">确定</a>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div id="information" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="user-block">
                    <span class="username" style="margin-left: 0px;">
                        <a id="menuTitle" href="#"></a>
                        <a href="javascript:$page.information.close();" class="pull-right btn-box-tool">
                            <i class="fa fa-times"></i>
                        </a>

                    </span>
                    <ul id="employeesDetail" class="list-group list-group-unbordered" style="margin-top:10px;">
                        <li class="list-group-item">
                            <b>员工ID</b> <a class="pull-right" id="id"></a>
                        </li>
                        <li class="list-group-item">
                            <b>登陆名</b> <a class="pull-right" id="loginName"></a>
                        </li>
                        <li class="list-group-item">
                            <b>姓名</b> <a class="pull-right" id="name"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属城市</b> <a class="pull-right" id="city"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属门店</b> <a class="pull-right" id="store"></a>
                        </li>
                        <li class="list-group-item">
                            <b>固定额度</b> <a class="pull-right" id="creditLimitModal"></a>
                        </li>
                        <li class="list-group-item">
                            <b>临时额度</b> <a class="pull-right" id="tempCreditLimitModal"></a>
                        </li>
                        <li class="list-group-item">
                            <b>额度余额</b> <a class="pull-right" id="creditLimitAvailableModal"></a>
                        </li>
                        <li class="list-group-item">
                            <b>上次修改时间</b> <a class="pull-right" id="lastUpdateTime"></a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:$page.information.close();" role="button" class="btn btn-primary">关闭</a>
            </div>
        </div>
    </div>
</div>
<script>

    $(function () {
        findCitySelection();
        findStoreSelection();
        initDateGird('/rest/employees/guidePage/grid');
        $('#btn_edit').on('click', function () {
            $grid.modify($('#dataGrid'), '/views/admin/guide/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });

        $('#clearTime_edit').on('click', function () {
            window.location.href = '/views/admin/guide/clearTimeEdit?parentMenuId=${parentMenuId!'0'}' ;
        });
    });

   function initDateGird(url) {
        $grid.init($('#dataGrid'), $('#toolbar'),url , 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'id',
                title: '员工ID',
                align: 'center'
            },{
            field: 'loginName',
            title: '登录名',
            align: 'center'
        }, {
            field: 'name',
            title: '姓名',
            align: 'center',
            events: {
                'click .scan': function(e, value, row) {
                    $page.information.show(row.id);
                }
            },
            formatter: function(value,row) {
                return '<a class="scan" href="#">' + value + '</a>';
            }
          },{
                field: 'cityId.name',
                title: '归属城市',
                align: 'center'
            },{
                field: 'storeId.storeName',
                title: '归属门店',
                align: 'center'
            }, {
                field: 'guideCreditMoney.tempCreditLimit',
                title: '临时额度',
                align: 'center'
            },{
                field: 'guideCreditMoney.creditLimitAvailable',
                title: '额度余额',
                align: 'center'
            },{
                title: '操作',
                align: 'center',
                formatter: function(value,row) {
                    return '<button class="btn btn-primary btn-sm" onclick="showDetails('+row.id+')"> 查看明细</button><button class="btn  btn-danger btn-sm" style="margin-left: 10px" onclick="clearTempCreditLimit('+row.id+','+row.guideCreditMoney.tempCreditLimit+')"> 手动清零</button>';
                }
            }
        ]);
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

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/employees/guide/' + id,
                        method: 'GET',
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            if (0 === result.code) {
                                var data = result.content;
                                $('#menuTitle').html("员工额度详情");

                                if (null === data.id) {
                                    data.id = '-';
                                }
                                $('#id').html(data.id);

                                if (null === data.loginName) {
                                    data.loginName = '-';
                                }
                                $('#loginName').html(data.loginName);

                                if (null === data.name) {
                                    data.name = '-';
                                }
                                $('#name').html(data.name);

                                if (null === data.guideCreditMoney) {
                                    $('#creditLimitModal').html('-');
                                }else if(null === data.guideCreditMoney.creditLimit) {
                                    $('#creditLimitModal').html('-');
                                }else{
                                    $('#creditLimitModal').html(data.guideCreditMoney.creditLimit);
                                }

                                if (null === data.guideCreditMoney) {
                                    $('#tempCreditLimitModal').html('-');
                                }else if(null===data.guideCreditMoney.tempCreditLimit){
                                    $('#tempCreditLimitModal').html('-');
                                }else{
                                    $('#tempCreditLimitModal').html(data.guideCreditMoney.tempCreditLimit);
                                }

                                if (null === data.guideCreditMoney) {
                                    $('#creditLimitAvailableModal').html('-');
                                } else if (null === data.guideCreditMoney.creditLimitAvailable){
                                    $('#creditLimitAvailableModal').html('-');
                                }else{
                                    $('#creditLimitAvailableModal').html(data.guideCreditMoney.creditLimitAvailable);
                                }


                                if (null === data.cityId) {
                                    $('#city').html('-');
                                }else if(null === data.cityId.name){
                                    $('#city').html('-');
                                }else{
                                    $('#city').html(data.cityId.name);
                                }

                                if (null === data.storeId) {
                                    $('#store').html('-');
                                }else if(null === data.storeId.storeName){
                                    $('#store').html('-');
                                }else{
                                    $('#store').html(data.storeId.storeName);
                                }


                                if (null === data.guideCreditMoney) {
                                    $('#lastUpdateTime').html('-');
                                }else if(null === data.guideCreditMoney.lastUpdateTime){
                                    $('#lastUpdateTime').html('-');
                                }else{
                                    $('#lastUpdateTime').html(formatDateTime(data.guideCreditMoney.lastUpdateTime));
                                }


                                $('#information').modal();
                            } else {
                                $notify.danger(result.message);
                            }
                        }
                    })
                }
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }

    function findCitySelection(){
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
                })
                $("#cityCode").append(city);
            }
        });
    }


    function initSelect(select, optionName) {
        $(select).empty();
        var selectOption = "<option value=-1>" + optionName + "</option>";
        $(select).append(selectOption);
    }

    function findStoreSelection() {
        var store = "";
        $.ajax({
            url: '/rest/stores/findStorelist',
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


    function findGuideCreditMoneyByCondition(){
        $("#queryGuideCreditMoneyInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
            initDateGird('/rest/employees/guidePage/conditionGrid?storeId='+storeId+
            '&cityId='+cityId);
    }


    function findGuideCreditMoneyByInfo() {
        var queryGuideVOInfo = $("#queryGuideVOInfo").val();

        $('#cityCode').val("-1");
        initSelect("#storeCode", "选择门店");
        findStoreSelection();
        $('#storeCode').val("-1");

        $("#dataGrid").bootstrapTable('destroy');
        if (null == queryGuideVOInfo || "" == queryGuideVOInfo) {
            initDateGird('/rest/employees/guidePage/grid');
        }else{
            initDateGird('/rest/employees/guidePage/infoGrid/' + queryGuideVOInfo);
        }
    }


    function findStoreByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        $("#queryCusInfo").val('');
        findGuideCreditMoneyByCondition();
        if(cityId==-1){
            findStoreSelection();
            return false;
        };
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityId/' + cityId,
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

     function clearTempCreditLimit(id,tempCreditLimit){
         $('#delId').val(id);
         $('#tempCreditLimit').val(tempCreditLimit);
         $('#delcfmModel').modal();
    }

    function clearSubmit(id){
        var tempCreditLimit= $('#tempCreditLimit').val();
        if(tempCreditLimit==0){
           $notify.warning('临时额度已为0');
           return false
        }
        data = {
            'empId': id,
        }
        $.ajax({
            url: '/rest/guideLine/clearTempCreditLimit',
            method: 'POST',
            data:data,
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                if (0 === result.code) {
                    clearTimeout($global.timer);
                    $notify.success('临时额度清零成功');
                    $("#dataGrid").bootstrapTable('destroy');
                    initDateGird('/rest/employees/guidePage/grid');
                } else {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger(result.message);
                    $('#guideCredit_edit').bootstrapValidator('disableSubmitButtons', false);
                }
            }
        });
    }

    function showDetails(id){
        window.location.href = '/views/admin/guide/creditChangesList/'+id;
    }

</script>
</body>