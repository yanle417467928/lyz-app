$(function () {

    initDateGird('/rest/productCoupon/grid');

    $('#btn_add').on('click', function () {
        $grid.add('/view/productCoupon/add/0');
    });

    $('#btn_edit').on('click', function () {
        $grid.modify($('#dataGrid'),'/view/productCoupon/edit/{id}');
    });

    $('#btn_copy').on('click',function () {
        //$grid.modify($('#dataGrid'),'/view/productCoupon/add/{id}');
    })

    $('#btn_delete').on('click', function () {
        $modal.danger("删除","确认删除？删除后不可恢复！！！",deleteproductCoupon);
    });
})

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
        title: '有效期结束',
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
            return "<a href='/view/productCoupon/send/"+value+"'>发放</a>"
        }
    }

    ]);

}

function deleteproductCoupon() {
    var ids = $grid.getSelectedIds($('#dataGrid'));
    ids = JSON.stringify(ids);
    $.ajax({
        url: '/rest/productCoupon/delete',
        method: 'POST',
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
                initDateGird('/rest/productCoupon/grid');
            } else {
                $notify.danger(result.message);
            }
        }
    });
}