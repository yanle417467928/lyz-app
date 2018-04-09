/**
 *  开始时间 -- 结束时间
 *  startDateId：开始时间inputId
 *  endDateId: 结束时间inputId
 *  formId: 表单id
 * **/
var $startTimeAndEndTime = {
    datatimePiker : function(startDateId,endDateId,formId){
        $("#"+startDateId).datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss',
            language: 'zh-CN',
            autoclose: true
        }).on('changeDate',function(e){
            var startTime = e.date;
            $('#'+endDateId).datetimepicker('setStartDate',startTime);
        }).on('hide',function(e) {
            $('#'+formId).data('bootstrapValidator') .updateStatus(startDateId, 'NOT_VALIDATED',null) .validateField(startDateId);
        });


        $("#"+endDateId).datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss',
            language: 'zh-CN',
            autoclose: true
        }).on('changeDate',function(e){
            var startTime = e.date;
            $('#'+startDateId).datetimepicker('setEndDate',startTime);
        }).on('hide',function(e) {
            $('#'+formId).data('bootstrapValidator') .updateStatus(endDateId, 'NOT_VALIDATED',null) .validateField(endDateId);
        });
    }
}

/**
 * 公用表单方法
 * **/
var $commonForm = {
    /**
     * 初始化城市
     */
    city: function (url,cityId,initVal) {
        var city = "";
        $.ajax({
            url: url,
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
                    if (initVal == undefined || initVal == ''){
                        city += "<option value=" + item.cityId + ">" + item.name + "</option>";
                    }else if (initVal == item.cityId){
                        city += "<option value=" + item.cityId + " selected='true'>" + item.name + "</option>";
                    }else{
                        city += "<option value=" + item.cityId + ">" + item.name + "</option>";
                    }
                })
                $("#"+cityId).append(city);

                $("#"+cityId).selectpicker('refresh');
            }
        });
    },
    /**
     * 门店
     * url: 获取门店list 地址
     * citySelctId： 城市select id
     * storesID : 门店div id
     */
    store : function (url,citySelctId,storesID) {
        var cityId = $("#"+citySelctId).val();
        var store = "";
        $.ajax({
            url: url +"/"+ cityId,
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
                    store += "<label id='"+item.storeId+"' class='label label-default' onclick='checkStore(this)'>"+item.storeName +"</label>";
                })
                $("#"+storesID).html(store);
                $("#"+storesID+' label').css({'white-space':'normal','margin':'5px','display':'inline-block'});
            }
        });

        /**绑定全选、反选按钮、门店点击事件**/
        $("#checkAllStoreButton").on("click",function(){
            $("#"+storesID+">label").each(function () {
                $(this).attr("class","label label-success");
            })
        })
        $("#unCheckAllStoreButton").on("click",function(){
            $("#"+storesID+">label").each(function () {
                checkStore(this);
            })
        })
    },
    /**
     * url: 获取城市List 地址
     * cityId： 城市select Id
     *
     * **/
    cityAndStore: function (url,cityId,storeUrl,storesID){
        var city = "";
        $.ajax({
            url: url,
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
                $("#"+cityId).append(city);
                $("#"+cityId).selectpicker('refresh');
                loadStore(storeUrl,cityId,storesID);
            }
        });
    },
    /**
     * 商品品牌
     * 初始值数组
     * **/
    goodsBrand: function (url,brandSelectId,valArr) {
        var brand = '';
        $.ajax({
            url: url,
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
                $("#"+brandSelectId).append(brand);
                $("#"+brandSelectId).selectpicker('refresh');
                $("#"+brandSelectId).selectpicker('render');


                if (valArr != undefined && valArr.length > 0){
                    $("#"+brandSelectId).selectpicker('val',valArr);
                    $("#"+brandSelectId).selectpicker('refresh');
                }

            }
        });
    },
    /**
     * 商品分类
     *
     * **/
    goodsCategory: function (url,categorySelectId) {
        var physical = '';
        $.ajax({
            url: url,
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
                $("#"+categorySelectId).append(physical);
            }
        });
    },
    /**
     * 商品弹出框
     * @param url 商品grid地址
     * @param goodsGridId
     * @param goodsTableId
     * @param openButtonId 打开按钮id
     */
    goodsGridModal: function (url,goodsGridId,goodsTableId,openButtonId) {
        initGoodsGrid(url,goodsGridId);

        $("#goodsModalConfirm").unbind('click').click(function(){
            chooseGoods(goodsGridId,goodsTableId);
        });

        $("#"+openButtonId).on('click',function () {
            $("#goodsModal").modal('show');
        })
    }
}

function loadStore(url,citySelctId,storesID) {
    var cityId = $("#"+citySelctId).val();
    var store = "";
    $.ajax({
        url: url +"/"+ cityId,
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
                store += "<label id='"+item.storeId+"' class='label label-default' onclick='checkStore(this)'>"+item.storeName +"</label>";
            })
            $("#"+storesID).html(store);
            $("#"+storesID+' label').css({'white-space':'normal','margin':'5px','display':'inline-block'});
        }
    });

    /**绑定全选、反选按钮、门店点击事件**/
    $("#checkAllStoreButton").on("click",function(){
        $("#"+storesID+">label").each(function () {
            $(this).attr("class","label label-success");
        })
    })
    $("#unCheckAllStoreButton").on("click",function(){
        $("#"+storesID+">label").each(function () {
            checkStore(this);
        })
    })
}

/**
 * 单击门店
 * @param eleDom
 */
function checkStore(eleDom){
    var classStr = $(eleDom).attr("class");

    if(classStr == "label label-default"){
        $(eleDom).attr("class","label label-success");
    }else{
        $(eleDom).attr("class","label label-default");
    }
}


function initGoodsGrid(url,goodsGridId){

    $grid.init($('#'+goodsGridId), $('#toolbar'),url , 'get', false, function (params) {
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

function chooseGoods(goodsGridID,tableId) {
    var tableData = $('#'+goodsGridID).bootstrapTable('getSelections');

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
                    "<td><input id='qty' type='number' value='0'></td>" +
                    "<td><a href='#'onclick='del_goods_comb(this);'>删除</td>" +
                    "</tr>"
            }

        }
        $("#"+tableId).append(str);

        // 取消所有选中行
        $('#'+goodsGridID).bootstrapTable("uncheckAll");
    }
}
//删除商品节点
function del_goods_comb(obj) {
    var deleteGoodsId = $(obj).parent().parent().find("#gid").val()
    $(obj).parent().parent().remove();
}