//记录删除商品ID
var deleteGoodsIds = new Array()

$(function () {
    // 初始化时间控件
    starAndEndDatetimepiker("beginTime","endTime");

    // 表单元素渲染
    //Flat red color scheme for iCheck
    $('input[type="checkbox"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass   : 'iradio_flat-green'
    })

    // 初始化商品grid
    initGoodsGrid("/rest/goods/page/grid");
    findGoodsBrand();
    findGoodsPhysical();

    changeConditionType($("#conditionType").val());
    changeResultType($("#resultType").val());

    //表单验证初始化
    formValidate();

    $("select").each(function () {
        $(this).selectpicker('refresh');
    })

    initFileInput("uploadQrcodeBtn",1);
})

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
    var store = "";
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
                store += "<label id='"+item.storeId+"' class='label label-default'  onclick='checkStore(this)'>"+item.storeName +"</label>";
            })
            $("#stores").html(store);
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

function initGoodsGrid(url){

    $grid.init($('#goodsDataGrid'), $('#toolbar'),url , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'id',
        title: 'ID',
        align: 'center',
        visible:false
    },{
        field: 'sku',
        title: 'sku',
        align: 'center'
    },{
        field: 'skuName',
        title: '名称',
        align: 'center'
    },{
        field: 'categoryName',
        title: '类型',
        align: 'center'
    },{
        field: 'goodsSpecification',
        title: '规格',
        align: 'center'
    },{
        field: 'materialsEnable',
        title: '状态',
        align: 'center',
        formatter: function (value, row, index) {
            if (null == value) {
                return '<span class="label label-danger">-</span>';
            }
        }
    }

    ]);
}
function openGoodsModal(id) {
    $("#goodsModalConfirm").unbind('click').click(function(){
        chooseGoods(id);
    });
    $('#goodsModal').modal('show');
}

function screenGoods() {
    var brandCode=$('#brandCode').val();
    var categoryCode=$('#categoryCode').val();
    var companyCode=$('#companyCode').val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    initGoodsGrid('/rest/goods/page/screenGoodsGrid?brandCode=' + brandCode+'&categoryCode='+categoryCode+'&companyCode='+companyCode);
}

function findGoodsByNameOrCode() {
    var queryGoodsInfo = $("#queryGoodsInfo").val();
    $("#goodsDataGrid").bootstrapTable('destroy');
    if (null == queryGoodsInfo || "" == queryGoodsInfo) {
        initGoodsGrid('/rest/goods/page/grid');
    }else{
        initGoodsGrid('/rest/goods/page/goodsGrid/' + queryGoodsInfo);
    }
}

function findGoodsBrand(){
    var brand = '';
    $.ajax({
        url: '/rest/goodsBrand/page/brandGrid',
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
                brand += "<option value=" + item.brdId + ">" + item.brandName + "</option>";
            })
            $("#brandCode").append(brand);
            $("#brandCode").selectpicker('refresh');
        }
    });
}

function findGoodsPhysical(){
    var physical = '';
    $.ajax({
        url: '/rest/goods/page/physicalClassifyGrid',
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
                physical += "<option value=" + item.id + ">" + item.physicalClassifyName + "</option>";
            })
            $("#categoryCode").append(physical);
        }
    });
}


function chooseGoods(tableId) {
    var tableData = $('#goodsDataGrid').bootstrapTable('getSelections');

    if(tableData.length == 0){
        $notify.warning('请先选择数据');
    }else{
        //alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++){
            var item = tableData[i];

            // 排除已选项
            var trs = $("#"+tableId).find("tr");
            var flag = true;
            trs.each(function(i,n){
                var id = $(n).find("#gid").val();
                if (id == item.id){
                    flag = false;
                    return false;
                }
            })

            // 此商品未添加过
            if(flag){
                str += "<tr>" +
                    "<td><input type='text' id='gid'value=" +item.id+ " style='width:90%;border: none;' readonly /></td>" +
                    "<td><input id='sku' type='text' value='"+item.sku+"' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='title' type='text' value='"+item.skuName+"' style='width:90%;border: none;' readonly></td>" +
                    "<td><input id='qty' type='number' value='0' onblur =\"gcOrderTip(this)\"></td>" +
                    "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                    "</tr>"
            }

        }
        $("#"+tableId).append(str);

        // 取消所以选中行
        $('#goodsDataGrid').bootstrapTable("uncheckAll");
    }
}

//删除商品节点
function del_goods_comb(obj) {
    var deleteGoodsId = $(obj).parent().parent().find("#gid").val()
    if(deleteGoodsId != 0){
        deleteGoodsIds.push(deleteGoodsId);
    }
    $(obj).parent().parent().remove();
}

// checkBox 绑定点击事件
$(function () {
    $('#is_goods_optional_qty').on('ifChecked', function(event){
        //alert($(this).prop('checked'));
        clickGoodsFixedQty();
    });
    $('#is_goods_optional_qty').on('ifUnchecked', function(event){
        //alert($(this).prop('checked'));
        clickGoodsFixedQty();
    });

    $('#is_gift_optional_qty').on('ifChecked', function(event){
        //alert($(this).prop('checked'));
        clickGiftFixedQty();
    });
    $('#is_gift_optional_qty').on('ifUnchecked', function(event){
        //alert($(this).prop('checked'));
        clickGiftFixedQty();
    });
})

function clickGoodsFixedQty() {
    var val = $('#is_goods_optional_qty').prop("checked");
    if(val){
        $("#goods_optional_qty_div").fadeIn(1000);
    }else{
        $("#fullNumber").val('');
        $("#goods_optional_qty_div").fadeOut(1);
    }
}

function clickGiftFixedQty() {
    var val = $('#is_gift_optional_qty').prop("checked");
    if(val){
        $("#gift_optional_qty_div").fadeIn(1000);
    }else{
        $("giftChooseNumber").val();
        $("#gift_optional_qty_div").fadeOut(1);
    }
}

function changeBaseType(val) {

}

function changeConditionType(val) {
    var baseVal = $("#baseType").val();

    if(baseVal == "COMMON"){
        if(val == "FQTY"){
            $("#fullNumber_div").fadeIn(1000);
            $("#fullAmount_div").fadeOut(1);
        }else if(val == "FAMO"){
            $("#fullNumber_div").fadeOut(1);
            $("#fullAmount_div").fadeIn(1000);
        }
    }else{

    }
}

function changeResultType(val) {

    if(val == "GOO"){
        $("#subAmount_div").fadeOut(1);
        $("#Gift_div").fadeIn(1000);
        $("#giftChooseNumber_div").fadeIn(1000);
        $("#addAmount_div").fadeOut(1);
        $("#discount_div").fadeOut(1);
    }else if(val == "SUB"){
        $("#subAmount_div").fadeIn(1000);
        $("#Gift_div").fadeOut(1);
        $("#giftChooseNumber_div").fadeOut(1);
        $("#addAmount_div").fadeOut(1);
        $("#discount_div").fadeOut(1);
    }else if(val == "ADD"){
        $("#subAmount_div").fadeOut(1);
        $("#Gift_div").fadeIn(1000);
        $("#giftChooseNumber_div").fadeIn(1000);
        $("#addAmount_div").fadeIn(1000);
        $("#discount_div").fadeOut(1);
    }else if(val == "DIS"){
        $("#subAmount_div").fadeOut(1);
        $("#Gift_div").fadeOut();
        $("#giftChooseNumber_div").fadeOut(1);
        $("#addAmount_div").fadeOut(1);
        $("#discount_div").fadeIn(1000);

        // 折扣 只能满金额
        changeConditionType("FAMO");
        $("#conditionType").val("FAMO");
        $("#conditionType").selectpicker('refresh');
    }else if(val == "PRO"){
        $("#subAmount_div").fadeOut(1);
        $("#Gift_div").fadeIn(1000);
        $("#giftChooseNumber_div").fadeIn(1000);
        $("#addAmount_div").fadeOut(1);
        $("#discount_div").fadeOut(1);
    }
}

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
                        message: '请选择促销开始时间'
                    }
                }
            },
            endTime:{
                validators:{
                    notEmpty:{
                        message: '请选择促销结束时间'
                    }
                }
            },
            subAmount:{
                validators:{
                    stringLength:{
                        min:0,
                        max:8,
                        message: "金额不准确"
                    }
                }
            },
            addAmount:{
                validators:{
                    stringLength:{
                        min:0,
                        max:8,
                        message: "金额不准确"
                    }
                }
            },
            fixedAmount:{
                validators:{
                    stringLength:{
                        min:0,
                        max:8,
                        message: "金额不准确"
                    }
                }
            },
            discount:{
                validators:{
                    regexp: {
                        regexp: /^[1-9](\.\d{0,2})?$/,
                        message: "只能输入1~10之间的数字,可保留两位小数"
                    }
                }
            },
            sortId: {
                message: '排序号校验失败',
                validators: {
                    notEmpty: {
                        message: '排序号不能为空'
                    },
                    regexp: {
                        regexp: /^[1-9]([0-9]{0,2})$/,
                        message: '您只能输入一个范围在1~999之间的整数'
                    }
                }
            }

        }
    }).on('success.form.bv', function (e) {
        var coverImg = $("#coverImg").val();

        if (coverImg == "" || coverImg == undefined){
            $notify.danger('请上传图片');
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            return false;
        }

        // 目标对象
        var target = "";
        $("input:checkbox[name='target']:checked").each(function (i) {
            if (i == 0){
                target += $(this).val();
            }else{
                target += ","+$(this).val();
            }

        })
        if (target == "" ){
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger('请选择目标对象');
            return false;
        }

        // 限制条件
        var isReturnable = $("#isReturnable").prop("checked");
        var isDouble = $("#isDouble").prop("checked");
        var isGcOrder = $("#isGcOrder").prop("checked");


        // 已选门店
        var stores = new Array();
        $("#stores > label[class='label label-success']").each(function () {
            stores.push(
                {
                    storeId:$(this).prop("id"),
                    storeTitle:$(this).html()
                }

            );
        })
        if(stores.length == 0){
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("请选择门店");
            return false;
        }

        //检查商品添加详情
        var goodsDetails = new Array();
        var checkFlag = cheackGoodsDetail(goodsDetails,'selectedGoodsTable');
        if (!checkFlag) {
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            return false;
        }
        if(goodsDetails.length == 0){
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            $notify.danger("请选择本品");
            return false;
        }
        // 是否任选数量
        var isGoodsOptionalQty = $("#is_goods_optional_qty").prop('checked');

        // 赠品
        var giftDetails = new Array();
        var checkFlag2 = cheackGoodsDetail(giftDetails,'selectedGiftTable');
        if (!checkFlag2) {
            $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
            return false;
        }
        // 是否任选数量
        var isGiftOptionalQty = $("#is_gift_optional_qty").prop('checked');

        // 根据选择促销类型判断
        var conditionType = $("#conditionType").val();
        var resultType = $("#resultType").val();

        //单价正则
        var reg = /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/;

        if (conditionType == "FAMO"){
            var price = $("#fullAmount").val();
            if(price == null || price.trim() == ""){

                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请填写需满足最低金额");
                return false;
            }

            if(!reg.test(price)){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("最低金额有误");
                return false;
            }
        }else if (conditionType == "FQTY"){
            if(isGoodsOptionalQty){
                var num = $('#fullNumber').val();
                if(num=='' || num == 0) {
                    $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                    $notify.warning("亲，本品任选数量不正确");
                    return false;
                }
            }else{
                for (var i = 0 ;i < goodsDetails.length ; i++){
                    var item = goodsDetails[i];
                    if(item.qty == 0){
                        $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                        $notify.danger("本品"+item.gid+"数量为0，请设置数量，或者修改促销条件");
                        return false;
                    }
                }
            }
        }
        if(resultType == "SUB"){
            var price = $("#subAmount").val();
            if(price == null || price.trim() == ""){

                    $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                    $notify.danger("请填写立减金额");
                    return false;
            }

            if(!reg.test(price)){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("立减金额有误");
                return false;
            }
        }
        else if (resultType == "GOO"){
            if(giftDetails.length == 0){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择赠品");
                return false;
            }

        }else if (resultType == "PRO"){
            if(giftDetails.length == 0){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择赠品作为产品券对象");
                return false;
            }

        }else if(resultType == "ADD"){
            if(giftDetails.length == 0){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请选择赠品");
                return false;
            }

            var price = $("#addAmount").val();
            if(price == null || price.trim() == ""){

                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请填写加价金额");
                return false;
            }

            if(!reg.test(price)){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("加价金额有误");
                return false;
            }
        }else if(resultType == "DIS"){
            // 折扣
            if (conditionType == "FQTY"){
                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("折扣促销只能满金额打折");
                return false;
            }

            var discount = $("#discount").val();

            if(discount == null || discount.trim() == ""){

                $('#activity_form').bootstrapValidator('disableSubmitButtons', false);
                $notify.danger("请填写折扣");
                return false;
            }
        }

        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        var url = '/rest/activity/save';

        $.each(origin, function () {
            data[this.name] = this.value;
        });
        data["isReturnable"] = isReturnable;
        data["isDouble"] = isDouble;
        data["isGcOrder"] = isGcOrder;
        data["isGoodsOptionalQty"] = isGoodsOptionalQty;
        data["isGiftOptionalQty"] = isGiftOptionalQty;
        data["actTarget"] = target;
        data["goodsDetails"] = JSON.stringify(goodsDetails);
        data["giftDetails"] = JSON.stringify(giftDetails);
        data["stores"] = JSON.stringify(stores);
        $http.POST(url, data, function (result) {
            if (0 === result.code) {
                $notify.info(result.message);
                window.location.href = "/view/activity/page";
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

/**
 * 检查商品详情
 */
function cheackGoodsDetail(details,tableId){
    var goodRepeatFlag = false;
    var validateFlag = true;

    //商品sku
    var goodsSkus = new Array();

    var trs = $("#"+tableId).find("tr");

    var goodsSku;
    //数量正则
    var re = /^[0-9]+.?[0-9]*$/;

    trs.each(function(i,n){
        var id = $(n).find("#gid").val();
        goodsSku = $(n).find("#sku").val();


        if($.inArray(goodsSku, goodsSkus) >= 0) {
            goodRepeatFlag = true;
            validateFlag = false;
            $notify.warning( "亲，【" + goodsSku + "】重复，请删除！");
            return false;
        }
        goodsSkus.push(goodsSku);

        if(tableId == "selectedGoodsTable"){
            var conditionType = $("#conditionType").val();
            if(conditionType == "FQTY"){
                // 是否任选数量
                var isGoodsOptionalQty = $("#is_goods_optional_qty").prop('checked');

                if(isGoodsOptionalQty){
                    var num = $('#fullNumber').val();
                    if(num=='' || num == 0 ||!re.test(num)) {
                        validateFlag = false;
                        $notify.warning("亲，本品任选数量不正确");
                        return false;
                    }
                }else{
                    var num = $(n).find("#qty").val();
                    if(num=='' || num == 0 ||!re.test(num)) {
                        validateFlag = false;
                        $notify.warning("亲，本品【" + goodsSku + "】数量不正确");
                        return false;
                    }
                }
            }

            details.push({
                gid:id,
                qty: $(n).find("#qty").val(),
                sku: goodsSku,
                goodsTitile: $(n).find("#title").val()
            });
        }else if(tableId == "selectedGiftTable"){


            // 是否任选数量
            var isGiftOptionalQty = $("#is_gift_optional_qty").prop('checked');

            var promotionType = $("#resultType").val();
            if (promotionType == "ADD"){
                // 加价购 赠品必填任选数量
                if (!isGiftOptionalQty){
                    $notify.warning("亲，加价购 任选数量必填");
                    return false;
                }
            }

            if(isGiftOptionalQty){
                var num = $('#giftChooseNumber').val();
                if(num=='' || num == 0 ||!re.test(num)) {
                    validateFlag = false;
                    $notify.warning("亲，赠品品任选数量不正确");
                    return false;
                }
            }else{
                var num = $(n).find("#qty").val();
                if(num=='' || num == 0 ||!re.test(num)) {
                    validateFlag = false;
                    $notify.warning("亲，赠品【" + goodsSku + "】数量不正确");
                    return false;
                }
            }
            details.push({
                giftId:id,
                giftFixedQty: $(n).find("#qty").val(),
                giftSku: goodsSku,
                giftTitle: $(n).find("#title").val()
            });
        }


    });
    return validateFlag;
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
        minImageWidth: 300, //图片的最小宽度
        minImageHeight:180,//图片的最小高度
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

/**
 * 工程单提示
 */
function gcOrderTip(node) {
    var num = $(node).val();
    var conditionType = $("#conditionType").val();
    var resultType = $("#resultType").val();
    var isGcOrder = $("#isGcOrder").prop("checked");

    if (isGcOrder == false){
        if (conditionType == "FQTY" && resultType == "SUB"){
            if (num > 20){
                // 大于20个 可能是工程单
                $notify.warning("亲，如果是工程单促销，请勾选上门的工程单勾勾！否则影响返利计算");
            }
        }
    }
}