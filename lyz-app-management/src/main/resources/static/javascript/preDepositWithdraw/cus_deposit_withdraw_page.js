$(function () {

    var url = "/rest/pre/deposit/withdraw/cus/grid";
    initDateGird(url);
})

function initDateGird(url) {

    $grid.init($('#dataGrid'), $('#toolbar'),url , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: $("#queryApplyInfo").val()
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
        field: 'applyNo',
        title: '单号',
        align: 'left'
    },{
        field: 'applyCusName',
        title: '申请人姓名',
        align: 'left'
    },{
        field: 'applyCusPhone',
        title: '申请人电话',
        align: 'center'
    },{
        field: 'accountTypeStr',
        title: '帐号类型',
        align: 'center'
    },{
        field: 'account',
        title: '帐号',
        align: 'center'
    },{
        field: 'withdrawAmount',
        title: '提现金额',
        align: 'center'
    },{
        field: 'createTime',
        title: '申请时间',
        align: 'left',
        formatter: function (value, row, index) {
            if (null != value) {
                return $DateFormat.toString(value);
            }
        }
    },{
        field: 'statusStr',
        title: '状态',
        align: 'center'
    },{
        field: 'id',
        title: '操作',
        align: 'left',
        formatter: function (value,row,index) {
            var operation1 = "<button class='btn btn-primary btn-xs' onclick='pass("+value+")'>通过</button>&nbsp;<button class='btn btn-danger btn-xs' onclick='reject("+value+")'>驳回</button>";
            var operation2 = "<button class='btn btn-success btn-xs' onclick='remit("+value+")'>打款</button>";
            var operation3 = "<label class='label label-danger'><span class='glyphicon glyphicon-remove'></span></label>";
            var operation4 = "<label class='label label-success'><span class='glyphicon glyphicon-ok'></span></label>";

            var status = row.status;

            if (status == "CHECKING"){
                return operation1;
            }else if(status == "CHECKPASS"){
                return operation2;
            }else if(status == "CHECKRETURN" || status == "CANCEL"){
                return operation3;
            }else if (status == "REMITED"){
                return operation4;
            }
        }
    }

    ]);
}

/**
 * 通过
 * @param id
 */
function  pass(id) {

    function affirmPass() {

        $.ajax({
            url: '/rest/pre/deposit/withdraw/cus/pass',
            method: 'PUT',
            data:{'applyId':id},
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);

                if (0 === result.code) {
                    $notify.info(result.message);
                    $("#dataGrid").bootstrapTable('destroy');
                    initDateGird('/rest/pre/deposit/withdraw/cus/grid');
                } else {
                    $notify.danger(result.message);
                }
            }
        });
    }

    $modal.info("提示","确认通过此条提现申请？",affirmPass)
}



/**
 * 驳回
 * @param id
 */
function  reject(id) {

    function affirmReject() {
        $.ajax({
            url: '/rest/pre/deposit/withdraw/cus/reject',
            method: 'PUT',
            data:{'applyId':id},
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);

                if (0 === result.code) {
                    $notify.info(result.message);
                    $("#dataGrid").bootstrapTable('destroy');
                    initDateGird('/rest/pre/deposit/withdraw/cus/grid');
                } else {
                    $notify.danger(result.message);
                }
            }
        });
    }

    $modal.danger("提示","确认驳回此条提现申请？",affirmReject);
}


/**
 * 打款
 */
function remit(id) {

    function affirmRemit() {
        $.ajax({
            url: '/rest/pre/deposit/withdraw/cus/remit',
            method: 'PUT',
            data:{'applyId':id},
            error: function () {
                clearTimeout($global.timer);
                $loading.close();
                $global.timer = null;
                $notify.danger('网络异常，请稍后重试或联系管理员');
            },
            success: function (result) {
                clearTimeout($global.timer);

                if (0 === result.code) {
                    $notify.info(result.message);
                    $("#dataGrid").bootstrapTable('destroy');
                    initDateGird('/rest/pre/deposit/withdraw/cus/grid');
                } else {
                    $notify.danger(result.message);
                }
            }
        });
    }

    $modal.success("提示","确认打款？",affirmRemit);
}

/**
 * 根据关键字搜索
 * **/
function findCusByNameOrPhoneOrderNumber(){
    $("#dataGrid").bootstrapTable('destroy');
    var url = "/rest/pre/deposit/withdraw/cus/grid";
    initDateGird(url);
}