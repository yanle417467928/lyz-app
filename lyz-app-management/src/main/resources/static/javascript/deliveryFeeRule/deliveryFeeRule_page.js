$(function () {

    initDateGird('/rest/deliveryFeeRule/grid');
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
        field: 'cityName',
        title: '城市',
        align: 'left'
    },{
        field: 'condition',
        title: '运费标准金额',
        align: 'left'
    },{
        field: 'deliveryFee',
        title: '运费',
        align: 'center'
    },{
        field: 'tollObject',
        title: '收费对象',
        align: 'center'
    },{
        field: 'createDate',
        title: '创建时间',
        align: 'left',
        formatter: function (value, row, index) {
            if (null != value) {
                return $DateFormat.toString(value);
            }
        }
    },{
        field: 'includeSpecialGoods',
        title: '是否包含特殊商品',
        align: 'center',
        formatter: function (value,row,index) {
            var html = "";
            if(value == true){
                html = "<lable class='label label-danger'>是</lable>"
            }else if(value == false){
                html = "<lable class='label label-primary'>否</lable>"
            }
            return html;
        }
    },{
        field: 'status',
        title: '状态',
        align: 'center',
        formatter: function (value,row,index) {
            var html = "";
            if(value == true){
                html = "<lable class='label label-primary'>启用</lable>"
            }else if(value == false){
                html = "<lable class='label label-danger'>停用</lable>"
            }
            return html;
        }
    }

    ]);

    $('#btn_add').on('click', function () {
        $grid.add('/view/deliveryFeeRule/add/0');
    });
    $('#btn_edit').on('click', function () {
        $grid.modify($('#dataGrid'),'/view/deliveryFeeRule/edit/{id}');
    });

    $('#btn_copy').on('click',function () {
        //$grid.modify($('#dataGrid'),'/view/cashCoupon/add/{id}');
    })

    $('#btn_delete').on('click', function () {
        $modal.danger("删除","确认删除？删除后不可恢复！！！",deleteData);
    });

}


function deleteData() {
    var ids = $grid.getSelectedIds($('#dataGrid'));
    ids = JSON.stringify(ids);
    $.ajax({
        url: '/rest/deliveryFeeRule/delete',
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
                initDateGird('/rest/deliveryFeeRule/grid');
            } else {
                $notify.danger(result.message);
            }
        }
    });
}