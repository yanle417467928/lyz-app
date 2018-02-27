$(function () {
    $(".select2").select2();
    $('.btn-cancel').on('click', function () {
        history.go(-1);
    });

    findCityList();
    initStoreGrid(null,null,'-1');
    initCityGrid(null);

    /*if (!$global.validateMobile()) {
     $('.select').selectpicker();
     }*/

    $(function () {
        $('[data-toggle="tooltip"]').popover();
    });

    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose: true
    });

    $('.switch').bootstrapSwitch();

    $('#user_add').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            loginName: {
                message: '登录名称校验失败',
                validators: {
                    notEmpty: {
                        message: '登录名称不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                        message: '登录名称只能输入字母、数字或汉字'
                    },
                    stringLength: {
                        min: 2,
                        max: 20,
                        message: '资源名称的长度必须在2~10位之间'
                    },
                }
            },
            name: {
                message: '用户姓名校验失败',
                validators: {
                    notEmpty: {
                        message: '用户姓名不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                        message: '用户姓名只能输入字母、数字或汉字'
                    },
                    stringLength: {
                        min: 2,
                        max: 20,
                        message: '资源名称的长度必须在2~20位之间'
                    }
                }
            },
            description: {
                message: '资源描述校验失败',
                validators: {
                    notEmpty: {
                        message: '资源描述不能为空！'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                        message: '资源描述只能输入字母或汉字'
                    },
                    stringLength: {
                        min: 2,
                        max: 10,
                        message: '资源描述的长度必须在2~10位之间'
                    }
                }
            },
            url: {
                message: '资源路径校验失败',
                validators: {
                    notEmpty: {
                        message: '资源路径不允许为空'
                    }
                }
            },
            seq: {
                message: '排序号校验失败',
                validators: {
                    notEmpty: {
                        message: '排序号不允许为空!'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};

        $.each(origin, function () {
            data[this.name] = this.value;
        });
        var reslist = $("#role").select2("data");
        var roleIds = [];
        $.each(reslist, function () {
            roleIds.push(this.id);
        });
        data["roleIdsStr"] = roleIds;

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/user/' + data.id;

            data.headImageUri = $('#headImageUri').attr("src");
            data.status = (undefined === data.status) ? false : data.status;
            $.ajax({
                url: url,
                method: 'PUT',
                data: data,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#user_add').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#user_add').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });
});

function initCityGrid(keywords){

    $grid.init($('#cityDataGrid'), $('#toolbar2'),"/rest/citys/list" , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: keywords
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'cityId',
        title: '城市ID',
        align: 'center',
        visible:false
    },{
        field: 'code',
        title: '城市编码',
        align: 'center'
    }, {
        field: 'name',
        title: '城市名称',
        align: 'center'
    },{
        field: 'structureTitle',
        title: '所属分公司',
        align: 'center'
    }
    ]);
}

function initStoreGrid(keywords,cityId,storeType){

    $grid.init($('#storeDataGrid'), $('#toolbar1'),"/rest/stores/list" , 'get', false, function (params) {
        return {
            offset: params.offset,
            size: params.limit,
            keywords: keywords,
            cityId: cityId,
            storeType: storeType
        }
    }, [{
        checkbox: true,
        title: '选择'
    },{
        field: 'storeId',
        title: '门店id',
        align: 'center',
        visible: false
    }, {
        field: 'storeCode',
        title: '门店编码',
        align: 'center'
    }, {
        field: 'storeName',
        title: '门店名称',
        align: 'center'
    }, {
        field: 'cityCode.name',
        title: '所属城市',
        align: 'center'
    }, {
        field: 'storeType',
        title: '门店类型',
        align: 'center'
    }

    ]);
}

function openStoreModal(id) {
    $("#storeModalConfirm").unbind('click').click(function(){
        chooseStore(id);
    });
    $('#storeModal').modal('show');
}

function openCityModal(id) {
    $("#cityModalConfirm").unbind('click').click(function(){
        chooseCity(id);
    });
    $('#cityModal').modal('show');
}

function findCityList() {
    var city = "";
    $.ajax({
        url: '/rest/citys/findCitylist',
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
                city += "<option value='" + item.cityId + "'>" + item.name + "</option>";
            });
            $("#cityCode").append(city);
        }
    });
}

function chooseStore(tableId) {
    var tableData = $('#storeDataGrid').bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else {
        //alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++) {
            var item = tableData[i];

            // 排除已选项
            var trs = $("#" + tableId + item.storeId).val();
            var flag = true;
            if (undefined != trs) {
                flag = false;
            }

            // 此商品未添加过
            if (flag) {
                str += "&nbsp;<label id='stores" +item.storeId+ "' style='margin-bottom: 12px;' class='label label-success'>"+item.storeName+"</label>"
            }

        }
        $("#" + tableId).append(str);
        // 取消所以选中行
        $('#storeDataGrid').bootstrapTable("uncheckAll");
    }
}

function chooseCity(tableId) {
    var tableData = $('#cityDataGrid').bootstrapTable('getSelections');

    if (tableData.length == 0) {
        $notify.warning('请先选择数据');
    } else {
        //alert(tableData);
        var str = "";
        for (var i = 0; i < tableData.length; i++) {
            var item = tableData[i];

            // 排除已选项
            var trs = $("#" + tableId + item.cityId).val();
            var flag = true;
            if (undefined != trs) {
                flag = false;
            }

            // 此商品未添加过
            if (flag) {
                str += "&nbsp;<label id='citys" +item.cityId+ "' style='margin-bottom: 12px;' class='label label-success'>"+item.name+"</label>"
            }

        }
        $("#" + tableId).append(str);
        // 取消所以选中行
        $('#cityDataGrid').bootstrapTable("uncheckAll");
    }
}

function findStoreByStoreType(storeType) {
    $("#queryCusInfo").val('');
    var queryStoreInfo = $("#queryStoreInfo").val();
    $("#storeDataGrid").bootstrapTable('destroy');
    var cityId = $("#cityCode").val();
    findStoreByCityIdOrKeywords(queryStoreInfo,cityId,storeType);
}

function findStoreByCity(cityId) {
    $("#queryStoreInfo").val('');
    var keywords = $('#queryStoreInfo').val();
    $("#storeDataGrid").bootstrapTable('destroy');
    var storeType = $("#storeType").val();
    findStoreByCityIdOrKeywords(keywords,cityId,storeType);
}

function findStoreByNameOrCode() {
    var queryStoreInfo = $("#queryStoreInfo").val();
    $("#storeDataGrid").bootstrapTable('destroy');
    var cityId = $("#cityCode").val();
    var storeType = $("#storeType").val();
    findStoreByCityIdOrKeywords(queryStoreInfo,cityId,storeType);
}

function findStoreByCityIdOrKeywords(keywords,cityId,storeType){
    initStoreGrid(keywords,cityId,storeType);
}

function findCityByNameOrCode() {
    var queryCityInfo = $("#queryCityInfo").val();
    $("#cityDataGrid").bootstrapTable('destroy');
    initCityGrid(queryCityInfo);
}

