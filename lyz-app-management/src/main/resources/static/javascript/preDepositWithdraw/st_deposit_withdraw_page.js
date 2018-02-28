$(function () {

    var url = "/rest/pre/deposit/withdraw/st/grid";
    initDateGird(url);
})

function initDateGird(url) {

    $grid.init($('#dataGrid'), $('#toolbar'),url , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: $("#queryApplyInfo").val(),
            status:$("#status").val()
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
        field: 'applyStName',
        title: '申请人姓名',
        align: 'left'
    },{
        field: 'applyStPhone',
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

{
    var passApplyId = "";

    /**
     * 通过
     * @param id
     */
    function  pass(id) {
        passApplyId = id;
        $modal.info("提示","确认通过此条提现申请？",affirmPass)
    }

    function affirmPass() {
        // 开启遮罩
        $loading.show;

        if(passApplyId == ""){
            $notify.danger('请刷新后再试');
            return false;
        }

        $.ajax({
            url: '/rest/pre/deposit/withdraw/st/pass',
            method: 'PUT',
            data:{'applyId':passApplyId},
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
                    initDateGird('/rest/pre/deposit/withdraw/st/grid');
                } else {
                    $notify.danger(result.message);
                }
            }
        });
    }


    var rejectApplyId = "";

    /**
     * 驳回
     * @param id
     */
    function  reject(id) {

        rejectApplyId = id;
        $modal.danger("提示","确认驳回此条提现申请？",affirmReject);
    }

    function affirmReject() {
        // 开启遮罩
        $loading.show;

        if(rejectApplyId == ""){
            $notify.danger('请刷新后再试');
            return false;
        }

        $.ajax({
            url: '/rest/pre/deposit/withdraw/st/reject',
            method: 'PUT',
            data:{'applyId':rejectApplyId},
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
                    initDateGird('/rest/pre/deposit/withdraw/st/grid');
                } else {
                    $notify.danger(result.message);
                }
            }
        });
    }

    var applyRemitId = "";

    /**
     * 打款
     */
    function remit(id) {
        applyRemitId = id;
        $modal.success("提示","确认打款？",affirmRemit);
    }

    function affirmRemit() {
        // 开启遮罩
        $loading.show;

        if(applyRemitId == ""){
            $notify.danger('请刷新后再试');
            return false;
        }

        $.ajax({
            url: '/rest/pre/deposit/withdraw/st/remit',
            method: 'PUT',
            data:{'applyId':applyRemitId},
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
                    initDateGird('/rest/pre/deposit/withdraw/st/grid');
                } else {
                    $notify.danger(result.message);
                }
            }
        });
    }
}


/**
 * 根据关键字搜索
 * **/
function findCusByNameOrPhoneOrderNumber(){
    $("#dataGrid").bootstrapTable('destroy');
    var url = "/rest/pre/deposit/withdraw/st/grid";
    initDateGird(url);
}