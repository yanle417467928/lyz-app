$(function () {

    initDateGird('/rest/activity/page/grid');
});

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
        field: 'cityName',
        title: '城市',
        align: 'center'
    },{
        field: 'actCode',
        title: '编码',
        align: 'left',
        visible:false
    },{
        field: 'title',
        title: '标题',
        align: 'left'
    },{
        field: 'type',
        title: '类型',
        align: 'left'
    },{
        field: 'createTime',
        title: '创建时间',
        align: 'left',
        formatter: function(value) {
            return $localDateTime.toString(value);
        }
    },{
        field: 'beginTime',
        title: '开始时间',
        align: 'left',
        formatter: function(value) {
            return $localDateTime.toString(value);
        }
    },{
        field: 'actTarget',
        title: '目标对象',
        align: 'left'
    },{
        field: 'status',
        title: '状态',
        align: 'center',
        formatter: function(value) {
            var html = "";
            if(value == "过期"){
                html = "<lable class='label label-danger'>"+value+"</lable>"
            }else if(value == "新建"){
                html = "<lable class='label label-primary'>"+value+"</lable>"
            }else{
                html = "<lable class='label label-success'>"+value+"</lable>"
            }
            return html
        }
    },{
        field: 'sortId',
        title: '排序号',
        align: 'center'
    },

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

    $('#btn_publish').on('click', function () {
        $modal.danger("发布","确认发布此促销？",publish);
    });

    $('#btn_delete').on('click', function () {
        $modal.danger("失效","确认失效？失效后不可恢复！！！",invalid);
    });

}

function publish() {
    var ids = $grid.getSelectedIds($('#dataGrid'));
    ids = JSON.stringify(ids);
    $.ajax({
        url: '/rest/activity/publish',
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
                initDateGird('/rest/activity/page/grid');
            } else {
                $notify.danger(result.message);
            }
        }
    });
}

function invalid() {
    var ids = $grid.getSelectedIds($('#dataGrid'));
    ids = JSON.stringify(ids);
    $.ajax({
        url: '/rest/activity/invalid',
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
                initDateGird('/rest/activity/page/grid');
            } else {
                $notify.danger(result.message);
            }
        }
    });
}

function statusChange(val) {
    $("#dataGrid").bootstrapTable('destroy');
    initDateGird('/rest/activity/page/grid');

}