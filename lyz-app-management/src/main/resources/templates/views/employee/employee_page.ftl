<head>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/css/bootstrap-select.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/i18n/defaults-zh_CN.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>
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
                    <select name="city" id="cityCode" class="form-control select" style="margin-left:30px width:auto;"
                            onchange="findEmpByCity(this.value)">
                        <option value="-1">选择城市</option>
                    </select>
                    <select name="storeCode" id="storeCode" class="form-control selectpicker" data-width="120px"
                            style="margin-left:10px width:auto;"
                            onchange="findEmpByStore()" data-live-search="true">
                        <option value="-1">选择门店</option>
                    </select>
                    <select name="identityType" id="identityType" class="form-control select" style="width:auto;"
                            onchange="findEmpByCondition()">
                        <option value="-1">选择类型</option>
                    </select>
                    <select name="enabled" id="enabled" class="form-control select" style="width:auto;"
                            data-live-search="true"
                            onchange="findEmpByCondition()">
                        <option value="-1">是否可用</option>
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryEmpInfo" id="queryEmpInfo" class="form-control"
                               style="width:auto;"
                               placeholder="请输入姓名、电话或登录名.." onkeypress="findBykey()">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findEmpByNameOrPhone()">查找</button>
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
                            <b>员工类型</b> <a class="pull-right" id="empIdentityType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>员工电话</b> <a class="pull-right" id="mobile"></a>
                        </li>
                        <li class="list-group-item">
                            <b>出生日期</b> <a class="pull-right" id="birthday"></a>
                        </li>
                        <li class="list-group-item" style="height: 100px;">
                            <b>头像</b> <a class="pull-right" id="picUrl"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属城市</b> <a class="pull-right" id="city"></a>
                        </li>
                        <li class="list-group-item">
                            <b>归属门店</b> <a class="pull-right" id="store"></a>
                        </li>
                        <li class="list-group-item">
                            <b>是否生效</b> <a class="pull-right" id="status"></a>
                        </li>
                        <li class="list-group-item">
                            <b>性别</b> <a class="pull-right" id="sex"></a>
                        </li>
                        <li class="list-group-item">
                            <b>配送员编码</b> <a class="pull-right" id="deliveryClerkNo"></a>
                        </li>
                        <li class="list-group-item">
                            <b>导购类型</b> <a class="pull-right" id="sellerType"></a>
                        </li>
                        <li class="list-group-item">
                            <b>入职时间</b> <a class="pull-right" id="entryTime"></a>
                        </li>
                        <li class="list-group-item">
                            <b>创建时间</b> <a class="pull-right" id="createTime"></a>
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

<div id="qrcodeModal" class="modal modal-info fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="user-block">
                    <div class="row">
                        <div class="col-md-12 col-xs-12">
                            <div class="form-group">
                                <label for="exampleInputFile">上传头像</label>
                                <input id="uploadQrcodeBtn" type="file" name="file" multiple class="file-loading">
                                <p class="help-block">支持jpg、jpeg、png格式，大小不超过2.0M</p>
                            </div>
                        </div>
                    </div>
                    <div class="row" style="height: 150px">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group" id='coverImageShow'>
                                <input name="coverImageUri" type="hidden" id="coverImg" class="form-control">
                                <input id="empId" type="hidden">
                                <div class="img-box" id="coverImageBox">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <a href="javascript:$qrcode.saveQrcodePic();" role="button" class="btn btn-primary">保存</a>
                <a href="javascript:$qrcode.close();" role="button" class="btn btn-warning">关闭</a>
            </div>
        </div>
    </div>
</div>
<script>

    $(function () {
        findCitySelection();
        findStoreSelection();
        findTypeSelection();
        initDateGird('/rest/employees/page/grid');
        initFileInput("uploadQrcodeBtn",1);
    });

    function initDateGird(url) {
        $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
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
            }, {
                field: 'loginName',
                title: '登录名',
                align: 'center'
            }, {
                field: 'name',
                title: '姓名',
                align: 'center',
                events: {
                    'click .scan': function (e, value, row) {
                        $page.information.show(row.id);
                    }
                },
                formatter: function (value) {
                    return '<a class="scan" href="#">' + value + '</a>';
                }
            }, {
                field: 'identityType',
                title: '员工类型',
                align: 'center'
            }, {
                field: 'cityId.name',
                title: '归属城市',
                align: 'center'
            }, {
                field: 'storeId.storeName',
                title: '归属门店',
                align: 'center'
            }, {
                field: 'qrCode',
                title: '二维码地址',
                align: 'left',
                width: '5%'
            }, {
                field: 'status',
                title: '是否生效',
                align: 'center',
                formatter: function (value, row, index) {
                    if (true == value) {
                        return '<span class="label label-primary">是</span> ';
                    } else if (false == value) {
                        return '<span class="label label-danger">否</span> ';
                    } else {
                        return '<span class="label label-danger">-</span> ';
                    }
                }
            },
            {
                field: 'id',
                title: '上传二维码',
                align: 'center',
                formatter: function (value, row, index) {
//                        return '<buttun class="btn-sm btn-success" onclick="$qrcode.open(' + value + ')">上传二维码</buttun>' +
//                                '<buttun class="btn-sm btn-success" onclick="$qrcode.create(' + value + ')">生成二维码</buttun>';
                    return '<buttun class="btn-sm btn-primary" onclick="$qrcode.open(' + value + ')">上传头像</buttun>' +
                            '<buttun class="btn-sm btn-success" onclick="$qrcode.create(' + value + ')">生成二维码</buttun>';
                }
            },
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

    var formatDateTimeBirthday = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        return y + '-' + m + '-' + d;
    };

    var $page = {
        information: {
            show: function (id) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/employees/' + id,
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
                                $('#menuTitle').html("员工详情");

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

                                if (null === data.identityType) {
                                    data.identityType = '-';
                                }
                                $('#empIdentityType').html(data.identityType);

                                if (null === data.mobile) {
                                    data.mobile = '-';
                                }
                                $('#mobile').html(data.mobile);

                                if (null === data.birthday) {
                                    data.birthday = '-';
                                } else {
                                    data.birthday = formatDateTimeBirthday(data.birthday)
                                }
                                $('#birthday').html(data.birthday);


                                if (null === data.picUrl) {
                                    data.picUrl = '-';
                                }
                                $('#picUrl').html('<img  src="' + data.picUrl + '"' + ' class="img-rounded" style="height: 80px;width: 80px;" >');

                                if (null === data.cityId) {
                                    $('#city').html('-');
                                } else if (null === data.cityId.name) {
                                    $('#city').html('-');
                                } else {
                                    $('#city').html(data.cityId.name);
                                }

                                if (null === data.storeId) {
                                    $('#store').html('-');
                                } else if (null === data.storeId.storeName) {
                                    $('#store').html('-');
                                } else {
                                    $('#store').html(data.storeId.storeName);
                                }

                                if (null === data.sex) {
                                    data.sex = '-';
                                }
                                $('#sex').html(data.sex);

                                if (null === data.deliveryClerkNo) {
                                    data.deliveryClerkNo = '-';
                                }
                                $('#deliveryClerkNo').html(data.deliveryClerkNo);

                                if (null === data.sellerType) {
                                    data.sellerType = '-';
                                }

                                if (null == data.sellerType) {
                                    $('#sellerType').html('-');
                                } else {
                                    if ('SELLER' == data.sellerType) {
                                        $('#sellerType').html('导购');
                                    } else if ('SUPERVISOR' == data.sellerType) {
                                        $('#sellerType').html('店长');
                                    } else {
                                        $('#sellerType').html('-');
                                    }
                                }

                                if (null === data.entryTime) {
                                    data.entryTime = '-';
                                } else {
                                    data.entryTime = formatDateTime(data.entryTime)
                                }
                                $('#entryTime').html(data.entryTime);

                                if (null === data.createTime) {
                                    data.createTime = '-';
                                } else {
                                    data.createTime = formatDateTime(data.createTime)
                                }
                                $('#createTime').html(data.createTime);

                                if (true == data.status) {
                                    $('#status').html('<span class="label label-primary">是</span>');
                                } else if (false == data.status) {
                                    $('#status').html('<span class="label label-danger">否</span>');
                                } else {
                                    $('#status').html('<span class="label label-danger">-</span>');
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

    function findCitySelection() {
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

    function findTypeSelection() {
        var emptype;
        $.ajax({
            url: '/rest/employees/findEmpTypeList',
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
                    emptype += "<option value=" + item.identityType + ">" + item.identityType + "</option>";
                })
                $("#identityType").append(emptype);
            }
        });
    }

    function findStoreSelection() {
        var store = "";
        $.ajax({
            url: '/rest/stores/findStoresListByStoreId',
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


    function findEmpByCondition() {
        $("#queryEmpInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        var identityType = $("#identityType").val();
        var storeId = $("#storeCode").val();
        var cityId = $("#cityCode").val();
        var enabled = $("#enabled").val();
        initDateGird('/rest/employees/page/conditionGrid?identityType=' + identityType + '&storeId=' + storeId +
                '&cityId=' + cityId + '&enabled=' + enabled);
    }

    function findBykey(){
        if(event.keyCode==13){
            findEmpByNameOrPhone();
        }
    }


    function findEmpByNameOrPhone() {
        var queryEmpInfo = $("#queryEmpInfo").val();
        $('#cityCode').val("-1");
        $('#enabled').val("-1");
        initSelect("#storeCode", "选择门店");
        findStoreSelection();
        initSelect("#identityType", "选择类型");
        findTypeSelection();
        $("#dataGrid").bootstrapTable('destroy');
        if (null == queryEmpInfo || "" == queryEmpInfo) {
            initDateGird('/rest/employees/page/grid');
        } else {
            initDateGird('/rest/employees/page/infoGrid/' + queryEmpInfo);
        }
    }


    function findEmpByCity(cityId) {
        initSelect("#storeCode", "选择门店");
        initSelect("#identityType", "选择类型");
        findEmpByCondition();
        $("#queryCusInfo").val('');
        if (cityId == -1) {
            findStoreSelection();
            findTypeSelection();
            return false;
        }
        ;
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityIdAndStoreId/' + cityId,
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
        findIdentityTypeByCity();
    }


    function findIdentityTypeByCity() {
        $("#queryEmpInfo").val('');
        var cityId = $("#cityCode").val();
        initSelect("#identityType", "选择类型")
        var emptype;
        $.ajax({
            url: '/rest/employees/findEmpTypeByCityId/' + cityId,
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
                    emptype += "<option value=" + item.identityType + ">" + item.identityType + "</option>";
                })
                $("#identityType").append(emptype);
            }
        });
    }

    function findEmpByStore() {
        $("#queryEmpInfo").val('');
        var storeId = $("#storeCode").val();
        initSelect("#identityType", "选择类型");
        findEmpByCondition();
        if (storeId == -1) {
            findTypeSelection();
            return false;
        }
        ;
        var emptype;
        $.ajax({
            url: '/rest/employees/findEmpTypeByStoreId/' + storeId,
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
                    emptype += "<option value=" + item.identityType + ">" + item.identityType + "</option>";
                })
                $("#identityType").append(emptype);
            }
        });
    }

    var $qrcode = {
        open: function (id) {
            $("#empId").val(id);
            $("#qrcodeModal").modal();
        },
        close: function(){
            $("#empId").val("");
            $("#coverImageBox").html("");
            $("#coverImg").val("");
            $("#qrcodeModal").modal('hide');
        },
        saveQrcodePic: function () {

            var empId = $("#empId").val();
            var picUrl = $('#coverImg').val();

            if (empId == null || empId == "" || empId == undefined){
                $notify.warning("保存失败，请重新上传");
                return false;
            }
            if (picUrl == null || picUrl == "" || picUrl == undefined){
                $notify.warning("保存失败，请重新上传")
                return false;
            }

            var data = {"url":picUrl,"empId":empId}

            $http.PUT("/rest/employees/update/photo",data,uploadSuccess);

            $("#empId").val("");
            $("#coverImageBox").html("");
            $("#coverImg").val("");
        },
        create: function (id) {
            $.ajax({
                url: '/rest/employees/create/qrcode?empId=' + id,
                method: 'POST',
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                },
                success: function (result) {
                    clearTimeout($global.timer);
                    if (result.code == 0){
                        $notify.success("生成成功！")
                        $("#qrcodeModal").modal('hide');
                        $("#dataGrid").bootstrapTable('destroy');
                        //initDateGird('/rest/employees/page/grid');
                        findEmpByCondition();
                    }else {
                        $notify.warning(result.message);
                    }
                }
            });
        }
    }


    function uploadSuccess(result) {
        if (result.code == 0){
            $notify.success("保存成功！")
            $("#qrcodeModal").modal('hide');
            $("#dataGrid").bootstrapTable('destroy');
            //initDateGird('/rest/employees/page/grid');
            findEmpByCondition();
        }else {
            $notify.warning(result.message);
        }


    }

    function initFileInput(ctrlName, type) {
        var control = $('#' + ctrlName);
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: "/rest/goods/uploadQrcode", //上传的地址
            allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
            //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
            uploadAsync: true, //默认异步上传
            showUpload: false, //是否显示上传按钮
            showRemove: true, //显示移除按钮
            showPreview: false, //是否显示预览
            showCaption: true,//是否显示标题
            browseClass: "btn btn-primary", //按钮样式
            dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 500,//图片的最大宽度
            //maxImageHeight: 500,//图片的最大高度
            maxFileSize:2048,//单位为kb，如果为0表示不限制文件大小
            maxFileCount: 1,//表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount: true,
            previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！"
        }).on('filebatchselected', function (event, data, id, index) {
            if (data.length == 0) {
                $notify.warning("图片大小或格式不正确，请检查")
                return;
            }
            $(this).fileinput("upload");
        }).on("fileuploaded", function (event, data) {
            if (data.response.code == 0) {
                if (1 == type) {
                    $('#coverImageBox').html('<img  src="' + data.response.content + '"' + ' class="img-rounded" style="height: 100px;width: 100px;" />')
                    $('#coverImg').val(data.response.content);
                }
            }
        });
    }
</script>
</body>