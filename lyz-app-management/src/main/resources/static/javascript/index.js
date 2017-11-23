var MoniterIndex = function (config) {
    var self = this;

    var showTreeSelectData = function () {
        $.ajax({
            url: 'data/role.json',
            method: 'get',
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                console.log(data);
                var datas = {};
                for (var i = 0; i < data.length; i++) {
                    datas.i = i + 1;
                    datas.rolename = data[i].rolename;
                    datas.createTime = data[i].createTime;
                    datas.roleDescription = data[i].roleDescription;
                    var source = $("#treeSelect-template").html();
                    var template = doT.template(source);
                    var htmlstr = template(datas);
                    $("#treeSelectBody").append(htmlstr);
                }

            },
            error: function (res) {
                console.log(res);
            }
        });


    }


    self.initData = function () {
        showTreeSelectData();
    }


}


$(function () {
    window.monitorIndex = new MoniterIndex();
    monitorIndex.initData();
});


$(function () {
    $("#treeSelectBody").on('click', '.grantAuthorityBtn', function () {
        var username = $(this).parents('tr').find('td').eq(1).text();
        var html = '<select id="authorifyselect" multiple="multiple">' +
            '<option  value="monitor_index" data-section="旅游管理" data-description="首页描述" selected>首页</option>' +
            '<option  value="manage_logs" data-section="旅游管理" data-description="用户日志描述" selected>用户信息</option>' +
            '<option  value="interface_logs" data-section="旅游管理" data-description="接口调用日志描述" selected>酒店信息</option>' +
            '<option  value="abnormal_logs" data-section="旅游管理">出行信息</option>' +
            '<option  value="empty_logs" data-section="旅游管理">景点信息</option>' +
            '<option  value="monitor_api" data-section="系统监控">用户监控</option>' +
            '<option  value="monitor_apiback" data-section="系统监控">车辆监控</option>' +
            '<option  value="monitor_usercert" data-section="系统监控">酒店监控</option>' +
            '<option  value="monitor_processor" data-section="系统监控">景点监控</option>' +
            '<option  value="monitor_connector" data-section="系统监控">导游监控</option>' +
            '<option  value="monitor_agent" data-section="系统监控">旅客监控</option>' +
            '<option  value="monitor_dispatcher" data-section="系统监控">地铁监控</option>' +
            '<option  value="monitor_dbreceive" data-section="系统监控">天气监控</option>' +
            '<option  value="monitor_dbquery" data-section="系统监控">就餐监控</option>' +
            '<option  value="monitor_backFile" data-section="系统监控">成员监控</option>' +
            '<option  value="manage_staff" data-section="用户权限">用户管理</option>' +
            '<option  value="manage_role" data-section="用户权限">角色管理</option>' +
            '<option  value="interface_test" data-section="接口测试">开始测试你麻痹</option>' +
            '</select>';
        $("#authorityBody").empty().append(html);
        $("#authorifyselect").treeMultiselect({searchable: true, hideSidePanel: true});
        $("#AuthorityTitle").text("给 " + username + " 分配权限");
        $("#grantAuthorityModal").modal('show');
    });


})