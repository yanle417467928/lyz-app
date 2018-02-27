$(function () {

    var url = "/rest/pre/deposit/withdraw/cus/grid";
    initDateGird(url);
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
        field: 'accountType',
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
        field: 'status',
        title: '状态',
        align: 'center'
    },{
        field: 'id',
        title: '操作',
        align: 'center',
        formatter: function (value,row,index) {
            return "<a href='#"+value+"'>审核</a>"
        }
    }

    ]);

}