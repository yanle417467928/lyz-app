

$(function () {

    findCityList();
    initStoreGrid(null,null,'-1');
    // 初始化时间控件
    starAndEndDatetimepiker("beginTime","endTime");

    // 表单元素渲染
    //Flat red color scheme for iCheck
 /*   $('input[type="radio"].flat-red').iCheck({
        radioClass   : 'iradio_flat-green'
    });*/
    $('input[type="checkbox"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green'
    });
    $(".select2").select2();

    $(function () {
        $('[data-toggle="tooltip"]').popover();
    });

    //表单验证初始化
    formValidate();

    $("select").each(function () {
        $(this).selectpicker('refresh');
    });
  /*  $('.switch').bootstrapSwitch();*/
    initFileInput("uploadQrcodeBtn",1);
});


//点击单选按钮后隐藏选项div
$(function(){
    $("input[type=radio][name=userType]").change(function(){
        var v = $(this).val();
        if (v =="0"){
            $("#Owner").css({"display":"none"});
        }else{
            $("#Owner").css({"display":"block"});
        }
    });
});

function findCityList() {
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
            })
            $("#cityId").append(city);
            $("#cityId").selectpicker('refresh');
            findStoreByCity();
        }
    });
}

function findStoreByCity(cityId) {
    var cityId = $("#cityId").val();
    var stores = "";
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
                stores += "<label id='"+item.storeId+"' class='label label-default'  onclick='checkStore(this)'>"+item.storeName +"</label>";
            })
            $("#stores").html(stores);
            $('#stores label').css({'white-space':'normal','margin':'5px','display':'inline-block'});
        }
    });
}

function checkStore(eleDom){
    var classStr = $(eleDom).attr("class");

    if(classStr == "label label-default"){
        $(eleDom).attr("class","label label-success");
    }else{
        $(eleDom).attr("class","label label-default");
    }
}

function checkAllStore() {
    $("#stores>label").each(function () {
        $(this).attr("class","label label-success");
    })
}
function unCheckAllStore() {
    $("#stores>label").each(function () {
        checkStore(this);
    })
}









var editor;
$(function () {
    //初始化编辑器
    var id = $('#id');
    editor = KindEditor.create('.editor', {
        width: '80%',
        height: '350px',
        resizeType: 1,
        uploadJson: '/rest/goods/updateGoodsDetial',
        fileManagerJson: '/rest/goods/updateGoodsDetial',
        allowFileManager: true
    });
});


/**
 * 表单验证
 */
function formValidate() {
    /**
     * 表单验证器初始化
     */
    $('#activity_form').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            title: {
                message: '主标题校验失败',
                validators: {
                    notEmpty: {
                        message: '标题不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 30,
                        message: '标题的长度必须在1~30位之间'
                    }
                }
            },
            beginTime:{
                validators:{
                    notEmpty:{
                        message: '请选择消息开始时间'
                    }
                }
            },
            endTime:{
                validators:{
                    notEmpty:{
                        message: '请选择消息结束时间'
                    }
                }
            },
        }
    }).on('success.form.bv', function (e) {
        var coverImg = $("#coverImg").val();

        var detailed = editor.html();
        // 目标对象
        var target = "";
        $("#target option:selected").each(function (i) {
            if (i == 0){
                target += $(this).val();
            }else{
                target += ","+$(this).val();
            }

        })
        if (target == "" ){
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger('请选择推送范围');
            return false;
        }



        var identityType = "";
        $("input:checkbox[name='identityType']:checked").each(function (i) {
            if (i == 0){
                identityType += $(this).val();
            }else{
                identityType += ","+$(this).val();
            }

        })

        var title=$("#title").val();

        var messageType=$("#messageType option:selected").val();




        // 已选会员
        var people = new Array();
        $("#people > label[class='label label-success']").each(function () {
            people.push(
                    $(this).prop("id").substring(6)
            );
        });

        // 已选门店
        var stores = new Array();
        $("#stores > label[class='label label-success']").each(function () {
            stores.push(

                    $(this).prop("id").substring(6)

            );
        })
        //已选员工
        var employees = new Array();
        $("#employees > label[class='label label-success']").each(function () {
            employees.push(

                $(this).prop("id").substring(9)

            );
        })



        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        var url = '/rest/message/save';

        $.each(origin, function () {
            data[this.name] = this.value;
        });

        data["title"] = title;
        data["messageType"] = messageType;
        data["detailed"] = detailed;
        data["scope"] = target;
        data["identityType"] = identityType;
        data["stores"] = JSON.stringify(stores);
        data["people"] = JSON.stringify(people);
        data["employees"] = JSON.stringify(employees);
        $http.POST(url, data, function (result) {
            if (0 === result.code) {
                $notify.info(result.message);
                window.location.href = "/view/message/list";
            } else {
                $notify.danger(result.message);
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            }
        });
    });

    $('#btn-cancel').on('click', function() {
        history.go(-1);
    });
}


function divClick() {

    var show = $("#target option:selected").val();


    switch (show) {
        case 'ZDY':
            document.getElementById("xzyg").style.display = "block";
            document.getElementById("xzhy").style.display = "block";
            document.getElementById("xzmd").style.display = "block";
            document.getElementById("sflx").style.display = "none";
            break;
        case 'ALL':
            document.getElementById("xzyg").style.display = "none";
            document.getElementById("xzhy").style.display = "none";
            document.getElementById("xzmd").style.display = "none";
            document.getElementById("sflx").style.display = "block";
            break;
    }
}
    /**
 *开始时间 -- 结束时间 渲染方法
 */
function starAndEndDatetimepiker(startDateId,endDateId){
    $("#"+startDateId).datetimepicker({
        format: 'yyyy-mm-dd hh:ii:ss',
        language: 'zh-CN',
        autoclose: true
    }).on('changeDate',function(e){
        var startTime = e.date;
        $('#'+endDateId).datetimepicker('setStartDate',startTime);
    }).on('hide',function(e) {
        $('#activity_form').data('bootstrapValidator') .updateStatus(startDateId, 'NOT_VALIDATED',null) .validateField(startDateId);
    });


    $("#"+endDateId).datetimepicker({
        format: 'yyyy-mm-dd hh:ii:ss',
        language: 'zh-CN',
        autoclose: true
    }).on('changeDate',function(e){
        var startTime = e.date;
        $('#'+startDateId).datetimepicker('setEndDate',startTime);
    }).on('hide',function(e) {
        $('#activity_form').data('bootstrapValidator') .updateStatus(endDateId, 'NOT_VALIDATED',null) .validateField(endDateId);
    });
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
        minImageWidth: 100, //图片的最小宽度
        minImageHeight:100,//图片的最小高度
        maxImageWidth: 400,//图片的最大宽度
        maxImageHeight: 220,//图片的最大高度
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




function openSelectEmployeesModel() {
    var employeesType = "员工";
    // $('#peopleModal').modal('show');
    var queryEmployeesInfo = $('#queryEmployeesInfo').val();
    var url = '/rest/employees/select/seller';
    $("#EmployeesDataGrid").bootstrapTable('destroy');
    $grid.init($('#EmployeesDataGrid'), $('#toolbar3'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search,
            employeesType: employeesType,
            selectCreateOrdereEployeesTypeConditions: queryEmployeesInfo
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'empId',
        title: 'ID',
        align: 'center'
    }, {
        field: 'name',
        title: '员工人姓名',
        align: 'center'
    }, {
        field: 'mobile',
        title: '员工人电话',
        align: 'center'
    }, {
        field: 'identityType',
        title: '身份类型',
        align: 'center'
    }, {
        field: 'storeName',
        title: '员工归属门店名',
        align: 'center'
    }, {
        field: 'storeCode',
        title: '员工归属门店code',
        align: 'center',
        visible: false
    }
    ]);
}

function findEmployees() {

    var employeesTypeType = "员工";
    var selectCreateOrderEmployeesConditions = $('#queryEmployeesInfo').val();
    var url = '/rest/order/photo/find/people';
    $("#EmployeesDataGrid").bootstrapTable('destroy');
    $grid.init($('#EmployeesDataGrid'), $('#toolbar3'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search,
            employeesTypeType: employeesTypeType,
            selectCreateOrderEmployeesTypeConditions: selectCreateOrderEmployeesConditions
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'empId',
        title: 'ID',
        align: 'center'
//                    visible:false
    }, {
        field: 'name',
        title: '员工姓名',
        align: 'center'
    }, {
        field: 'mobile',
        title: '会员电话',
        align: 'center'
    }, {
        field: 'identityType',
        title: '身份类型',
        align: 'center'
    }, {
        field: 'storeName',
        title: '员工归属门店名',
        align: 'center'
    }, {
        field: 'storeCode',
        title: '员工归属门店code',
        align: 'center',
        visible: false
    }
    ]);
}


function chooseEmployees(tableId) {
    var tableData = $('#EmployeesDataGrid').bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else {
       // alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++) {
            var item = tableData[i];

            // 排除已选项
            var trs = $("#" + tableId + item.empId).val();

            var flag = true;
            if (undefined != trs) {
                flag = false;
            }

            if (flag) {
                str += "<label id='employees" +item.empId+ "' style='margin-right: 6px;' class='label label-success'>"+item.name;
                str +="-";
                str +=item.mobile;
                str += '&nbsp;<span onclick="deleteHtml(';
                str += "'employees" +item.empId + "'";
                str += ')"';
                str += ">×</span></label>";
            }
        }
        $("#" + tableId).append(str);
        // 取消所以选中行
        $('#EmployeesDataGrid').bootstrapTable("uncheckAll");
    }
}

//选择员工
function openEmployeesModal() {
    openSelectEmployeesModel();
    $("#employeesModalConfirm").unbind('click').click(function(){
        chooseEmployees('employees');
    });
    $('#employeesModal').modal('show');
}












function openSelectPeopleModel() {
    var peopleType = "会员";
    // $('#peopleModal').modal('show');
    var queryPeopleInfo = $('#queryPeopleInfo').val();
    var url = '/rest/order/photo/find/people';
    $("#peopleDataGrid").bootstrapTable('destroy');
    $grid.init($('#peopleDataGrid'), $('#toolbar1'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search,
            peopleType: peopleType,
            selectCreateOrderPeopleConditions: queryPeopleInfo
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'peopleId',
        title: 'ID',
        align: 'center'
//                    visible:false
    }, {
        field: 'name',
        title: '会员人姓名',
        align: 'center'
    }, {
        field: 'phone',
        title: '会员人电话',
        align: 'center'
    }, {
        field: 'identityType',
        title: '身份类型',
        align: 'center'
    }, {
        field: 'storeName',
        title: '会员归属门店名',
        align: 'center'
    }, {
        field: 'storeCode',
        title: '会员归属门店code',
        align: 'center',
        visible: false
    }
    ]);
}

function findPeople() {
    var peopleType = "会员";
    var selectCreateOrderPeopleConditions = $('#queryPeopleInfo').val();


    var url = '/rest/order/photo/find/people';
    $("#peopleDataGrid").bootstrapTable('destroy');
    $grid.init($('#peopleDataGrid'), $('#toolbar1'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search,
            peopleType: peopleType,
            selectCreateOrderPeopleConditions: selectCreateOrderPeopleConditions
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'peopleId',
        title: 'ID',
        align: 'center'
//                    visible:false
    }, {
        field: 'name',
        title: '会员姓名',
        align: 'center'
    }, {
        field: 'phone',
        title: '会员电话',
        align: 'center'
    }, {
        field: 'identityType',
        title: '身份类型',
        align: 'center'
    }, {
        field: 'storeName',
        title: '会员归属门店名',
        align: 'center'
    }, {
        field: 'storeCode',
        title: '会员归属门店code',
        align: 'center',
        visible: false
    }
    ]);
}


function choosePeople(tableId) {
    var tableData = $('#peopleDataGrid').bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else {
        //alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++) {
            var item = tableData[i];

            // 排除已选项
            var trs = $("#" + tableId + item.peopleId).val();
            var flag = true;
            if (undefined != trs) {
                flag = false;
            }

            if (flag) {
                str += "<label id='people" +item.peopleId+ "' style='margin-right: 6px;' class='label label-success'>"+item.name;
                str +="-";
                str +=item.phone;
                str += '&nbsp;<span onclick="deleteHtml(';
                str += "'people" +item.peopleId + "'";
                str += ')"';
                str += ">×</span></label>";
            }
        }
        $("#" + tableId).append(str);
        // 取消所以选中行
        $('#peopleDataGrid').bootstrapTable("uncheckAll");
        $('#people label').css({'white-space':'normal','margin':'5px','display':'inline-block'});
    }
}

//选择会员
function openPeopleModal() {
    openSelectPeopleModel();
    $("#peopleModalConfirm").unbind('click').click(function(){
        choosePeople('people');
    });
    $('#peopleModal').modal('show');
}





function initStoreGrid(keywords,cityId,storeType){

    $grid.init($('#storeDataGrid'), $('#toolbar2'),"/rest/stores/list" , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: keywords,
            cityId: cityId,
            storeType: storeType
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'storeId',
        title: '门店id',
        align: 'center',
        visible: false
    }, {
        field: 'storeCode',
        title: '门店编码',
        align: 'center'
    }, {
        field: 'storeName',
        title: '门店名称',
        align: 'center'
    }, {
        field: 'cityCode.name',
        title: '所属城市',
        align: 'center'
    }, {
        field: 'storeType',
        title: '门店类型',
        align: 'center'
    }

    ]);
}

function openStoreModal(id) {
    $("#storeModalConfirm").unbind('click').click(function(){
        chooseStore(id);
    });
    $('#storeModal').modal('show');
}


function findCityList() {
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
                city += "<option value='" + item.cityId + "'>" + item.name + "</option>";
            });
            $("#cityCode").append(city);
            $("#cityCode").selectpicker('refresh');
            $("#cityCode").selectpicker('render');
        }
    });
}

function chooseStore(tableId) {
    var tableData = $('#storeDataGrid').bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else {
       // alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++) {
            var item = tableData[i];

            // 排除已选项
            var trs = $("#" + tableId + item.storeId).val();
            var flag = true;
            if (undefined != trs) {
                flag = false;
            }

            // 此商品未添加过
            if (flag) {
                str += "<label id='stores" +item.storeId+ "' style='margin-right: 6px;' class='label label-success'>"+item.storeName;
                str += '&nbsp;<span onclick="deleteHtml(';
                str += "'stores" +item.storeId + "'";
                str += ')"';
                str += ">×</span></label>";
            }

        }
        $("#" + tableId).append(str);
        // 取消所以选中行
        $('#storeDataGrid').bootstrapTable("uncheckAll");
    }
}


function findStoreByStoreType(storeType) {
    $("#queryCusInfo").val('');
    var queryStoreInfo = $("#queryStoreInfo").val();
    $("#storeDataGrid").bootstrapTable('destroy');
    var cityId = $("#cityCode").val();
    findStoreByCityIdOrKeywords(queryStoreInfo,cityId,storeType);
}

function findStoreByCity(cityId) {
    $("#queryStoreInfo").val('');
    var keywords = $('#queryStoreInfo').val();
    $("#storeDataGrid").bootstrapTable('destroy');
    var storeType = $("#storeType").val();
    findStoreByCityIdOrKeywords(keywords,cityId,storeType);
}

function findStoreByNameOrCode() {
    var queryStoreInfo = $("#queryStoreInfo").val();
    $("#storeDataGrid").bootstrapTable('destroy');
    var cityId = $("#cityCode").val();
    var storeType = $("#storeType").val();
    findStoreByCityIdOrKeywords(queryStoreInfo,cityId,storeType);
}

function findStoreByCityIdOrKeywords(keywords,cityId,storeType){
    initStoreGrid(keywords,cityId,storeType);
}


function deleteHtml(id){
    $('#' + id).remove();
}

