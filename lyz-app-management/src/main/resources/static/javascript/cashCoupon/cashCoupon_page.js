$(function () {

    initDateGird('/rest/cashCoupon/grid');
})

function initDateGird(url) {

    $grid.init($('#dataGrid'), $('#toolbar'),url , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: params.search,
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
        formatter: function(value) {
            return $localDateTime.toString(value);
        }
    },{
        field: 'effectiveEndTime',
        title: '有效期结束',
        align: 'left',
        formatter: function(value) {
            return $localDateTime.toString(value);
        }
    },{
        field: 'descriptions',
        title: '使用说明',
        align: 'left'
    },{
        field: 'remainingQuantity',
        title: '剩余数量',
        align: 'center'
    }

    ]);

    $('#btn_add').on('click', function () {
        $grid.add('/view/activity/add/0');
    });
    $('#btn_edit').on('click', function () {
        $grid.modify($('#dataGrid'),'/view/activity/edit/{id}');
    });

    $('#btn_copy').on('click',function () {
        $grid.modify($('#dataGrid'),'/view/activity/add/{id}');
    })

    $('#btn_delete').on('click', function () {
        $modal.danger("删除","确认删除？删除后不可恢复！！！",invalid);
    });

}