$(function () {
    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose: true
    });

    initDateGird('/rest/cashCoupon/grid');

    $('#btn_add').on('click', function () {
        $grid.add('/view/cashCoupon/add/0');
    });
    $('#btn_edit').on('click', function () {
        $grid.modify($('#dataGrid'),'/view/cashCoupon/edit/{id}');
    });

    $('#btn_copy').on('click',function () {
        //$grid.modify($('#dataGrid'),'/view/cashCoupon/add/{id}');
    })

    $('#btn_delete').on('click', function () {
        $modal.danger("删除","确认删除？删除后不可恢复！！！",deleteCashCoupon);
    });
})

function initDateGird(url) {

    $grid.init($('#dataGrid'), $('#toolbar'),url , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: $("#Info").val(),
            startTime: $("#startTime").val(),
            endTime: $("#endTime").val(),
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
        field: 'title',
        title: '标题',
        align: 'left'
    },{
        field: 'condition',
        title: '满减条件',
        align: 'left'
    },{
        field: 'denomination',
        title: '面额',
        align: 'center'
    },{
        field: 'effectiveStartTime',
        title: '有效期开始时间',
        align: 'left',
        formatter: function (value, row, index) {
            if (null != value) {
                return $DateFormat.toString(value);
            }
        }
    },{
        field: 'effectiveEndTime',
        title: '有效期结束时间',
        align: 'left',
        formatter: function (value, row, index) {
            if (null != value) {
                return $DateFormat.toString(value);
            }
        }
    },{
        field: 'description',
        title: '使用说明',
        align: 'left'
    },{
        field: 'remainingQuantity',
        title: '剩余数量',
        align: 'center'
    },{
        field: 'id',
        title: '操作',
        align: 'center',
        formatter: function (value,row,index) {
            return "<a href='/view/cashCoupon/send/"+value+"'>发放</a>"
    }
    }

    ]);

}

function  findByInfo() {
    $("#dataGrid").bootstrapTable('destroy');
    initDateGird('/rest/cashCoupon/grid');
}


function deleteCashCoupon() {
    var ids = $grid.getSelectedIds($('#dataGrid'));
    ids = JSON.stringify(ids);
    $.ajax({
        url: '/rest/cashCoupon/delete',
        method: 'PUT',
        data:{'ids':ids},
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
                initDateGird('/rest/cashCoupon/grid');
            } else {
                $notify.danger(result.message);
            }
        }
    });
}