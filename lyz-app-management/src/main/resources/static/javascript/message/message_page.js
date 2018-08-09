$(function () {

    initDateGird('/rest/message/page/grid');
    findCitylist();
    $('#btn_add').on('click', function () {
        $grid.add('/view/message/add/0');
    });
    $('#btn_edit').on('click', function () {
        $grid.modify($('#dataGrid'), '/view/message/edit/{id}');
    });

    $('#btn_publish').on('click', function () {
        $modal.danger("发布", "确认发布此消息？", publish);
    });

    $('#btn_delete').on('click', function () {
        $modal.danger("失效", "确认失效？失效后不可恢复！！！", invalid);
    });
});

function initDateGird(url) {

    $grid.init($('#dataGrid'), $('#toolbar'), url, 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: $("#Info").val(),
            status: $("#status").val(),
            cityId: $("#cityCode").val()
        }
    }, [{
        checkbox: true,
        title: '选择'
    }, {
        field: 'id',
        title: 'ID',
        align: 'center',
        visible: false
    }, {
        field: 'title',
        title: '消息标题',
        align: 'center'
    }, {
        field: 'detailed',
        title: '消息详情',
        align: 'center',
        visible: false


    }, {
        field: 'beginTime',
        title: '开始时间',
        align: 'left',
        formatter: function (value) {
            return $localDateTime.toString(value);
        }
    }, {
        field: 'endTime',
        title: '结束时间',
        align: 'center',
        formatter: function (value) {
            return $localDateTime.toString(value);
        }
    }, {
        field: 'identityType',
        title: '身份类型',
        align: 'left'
    }, {
        field: 'createTime',
        title: '创建时间',
        align: 'left',
        formatter: function (value) {
            return $localDateTime.toString(value);
        }
    },  {
        field: 'scope',
        title: '推送范围',
        align: 'left'
    },  {
        field: 'messageType',
        title: '消息类型',
        align: 'left'
    }, {
        field: 'status',
        title: '状态',
        align: 'center',
        formatter: function (value) {
            var html = "";
            if (value == "失效") {
                html = "<lable class='label label-danger'>" + value + "</lable>"
            } else if (value == "新建") {
                html = "<lable class='label label-primary'>" + value + "</lable>"
            } else {
                html = "<lable class='label label-success'>" + value + "</lable>"
            }
            return html
        }
    }

    ]);

}

function publish() {
    var ids = $grid.getSelectedIds($('#dataGrid'));
    ids = JSON.stringify(ids);
    $.ajax({
        url: '/rest/message/publish',
        method: 'PUT',
        data: {'ids': ids},
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
                initDateGird('/rest/message/page/grid');
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
        url: '/rest/message/invalid',
        method: 'PUT',
        data: {'ids': ids},
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
                initDateGird('/rest/message/page/grid');
            } else {
                $notify.danger(result.message);
            }
        }
    });
}

function statusChange(val) {
    $("#dataGrid").bootstrapTable('destroy');
    initDateGird('/rest/message/page/grid');

}

function findBykey() {
    findActByInfo()
}

function findActByInfo() {
    $("#dataGrid").bootstrapTable('destroy');
    initDateGird('/rest/message/page/grid');
}