$(function () {
    
    $(".select").each(function () {
        $(this).selectpicker('refresh');
    })

    $("#sent_btn").on("click",function () {
        $modal.info("出库","确认出库？",sent);
    })

    $("#entered_btn").on("click",function () {
        $modal.success("入库","确认入库？",entered);
    })

    $("#cancelled_btn").on("click",function () {
        $modal.warning("作废","确认作废？",cancelled);
    })

    $('#btn-cancel').on('click', function() {
        history.go(-1);
    });
})

// 出库
function sent() {

    var allocationId = $("#allocationId").val();
    if (allocationId == undefined || allocationId == ""){
        $notify.warning("调拨单数据有误，请联系管理员");
        return false;
    }

    //检查商品添加详情
    var goodsDetails = new Array();
    var checkFlag = cheackGoodsDetail(goodsDetails, 'selectedGoodsTable');
    if (!checkFlag) {
        return false;
    }

    var data = {};
    data["allocationId"] = allocationId;
    data["details"] = JSON.stringify(goodsDetails);

    $("#sent_btn").on("click",function(){});
    $http.PUT("/rest/store/allocation/sent",data,function (result) {
        $("#sent_btn").on("click",function(){
            $modal.info("出库","确认出库？",sent);
        });
        if (0 === result.code) {
            $notify.info(result.message);
            window.location.href = "/views/admin/inventory/allocation";
        } else {
            $notify.danger(result.message);
        }
    });

}

// 入库
function entered() {
    var allocationId = $("#allocationId").val();
    if (allocationId == undefined || allocationId == ""){
        $notify.warning("调拨单数据有误，请联系管理员");
        return false;
    }
    var data = {};
    data["allocationId"] = allocationId;

    $("#entered_btn").on("click",function(){});
    $http.PUT("/rest/store/allocation/entered",data,function (result) {
        $("#entered_btn").on("click",function(){
            $modal.info("出库","确认出库？",entered);
        });
        if (0 === result.code) {
            $notify.info(result.message);
            window.location.href = "/views/admin/inventory/allocation";
        } else {
            $notify.danger(result.message);
        }
    });
}

// 作废
function cancelled() {
    var allocationId = $("#allocationId").val();
    if (allocationId == undefined || allocationId == ""){
        $notify.warning("调拨单数据有误，请联系管理员");
        return false;
    }
    var data = {};
    data["allocationId"] = allocationId;

    $("#entered_btn").on("click",function(){});
    $http.PUT("/rest/store/allocation/cancelled",data,function (result) {
        $("#entered_btn").on("click",function(){
            $modal.info("出库","确认出库？",entered);
        });
        if (0 === result.code) {
            $notify.info(result.message);
            window.location.href = "/views/admin/inventory/allocation";
        } else {
            $notify.danger(result.message);
        }
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
        var id = $(n).find("#goodsId").val();
        goodsSku = $(n).find("#sku").val();

        if(tableId == "selectedGoodsTable"){
            var num = $(n).find("#qty").val();
            var realNum = $(n).find("#realQty").val();

            if(num < realNum){
                validateFlag = false;
                $notify.warning("亲，商品"+goodsSku+"数量大于申请数量");
                return false;
            }

            if(realNum=='' || realNum == 0 ||!re.test(realNum)) {
                validateFlag = false;
                $notify.warning("亲，商品"+goodsSku+"数量不正确");
                return false;
            }
            details.push({
                goodsId:id,
                qty: num,
                sku: goodsSku,
                skuName: $(n).find("#skuName").val(),
                realQty: realNum
            });
        }

    });
    return validateFlag;
}