$(function() {
    if (!$global.validateMobile()) {
        $('.select').selectpicker({
            size:'5'
        });
    }
    $('[name="isOnSale"]').bootstrapSwitch({
        onText:"上架",
        offText:"下架",
        onColor:"primary",
        offColor:"danger",
        // size:"normal",
        onSwitchChange:function(event,state){
            if(state==true){
                $(this).val("on");
            }else{
                $(this).val("off");
            }
        }
    });
    $('[name="isGift"]').bootstrapSwitch({
        onText:"是",
        offText:"否",
        onColor:"primary",
        offColor:"danger",
        // size:"normal",
        onSwitchChange:function(event,state){
            if(state==true){
                $(this).val("on");
            }else{
                $(this).val("off");
            }
        }
    });
    $('[name="isColorful"]').bootstrapSwitch({
        onText:"是",
        offText:"否",
        onColor:"primary",
        offColor:"danger",
        // size:"normal",
        onSwitchChange:function(event,state){
            if(state==true){
                $(this).val("on");
            }else{
                $(this).val("off");
            }
        }
    });
    $('[name="isColorPackage"]').bootstrapSwitch({
        onText:"是",
        offText:"否",
        onColor:"primary",
        offColor:"danger",
        size:"normal",
        onSwitchChange:function(event,state){
            if(state==true){
                $(this).val("on");
            }else{
                $(this).val("off");
            }
        }
    });
    $(function () {
        $('[data-toggle="tooltip"]').popover();
    });

    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'zh-CN',
        autoclose: true
    });

    $('.switch').bootstrapSwitch();
    $('#passwordFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            password: {
                message: '密码校验失败',
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._-]){0,10}$/,
                        message: '只能输入以字母、数字开头，可带“_”、“.”、“-”'
                    },
                    stringLength: {
                        min: 6,
                        max: 6,
                        message: '密码的长度必须是6位'
                    }
                }
            },
            password2: {
                message: '二次输入密码校验失败',
                validators: {
                    notEmpty: {
                        message: '二次输入密码不能为空'
                    },
                    identical: {
                        field: 'password',
                        message: '两次密码输入不一致'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};

        $.each(origin, function() {
            data[this.name] = this.value;
        });

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/employee/revisepassword';

            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function() {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#employeeFrom').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function(result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#employeeFrom').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });

    $('#employeeFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            name: {
                message: '员工姓名校验失败',
                validators: {
                    notEmpty: {
                        message: '员工姓名不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z\u4E00-\u9FA5]+$/,
                        message: '员工姓名只能输入字母或汉字'
                    },
                    stringLength: {
                        min: 2,
                        max: 10,
                        message: '员工姓名的长度必须在2~10位之间'
                    }
                }
            },
            mobile: {
                message: '联系电话校验失败',
                validators: {
                    notEmpty: {
                        message: '联系电话不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入正确的联系电话'
                    },
                    regexp: {
                        regexp: /^(1[3584]\d{9})$/,
                        message: '请输入正确的联系电话'
                    },
                    threshold: 11,
                    remote: {
                        type: 'POST',
                        url: '/rest/employee/validator/mobile',
                        message: '该联系电话已经存在',
                        delay: 500,
                        data: function() {
                            return {
                                mobile: $('#mobile').val(),
                                id: $('#id').val()
                            }
                        }
                    }
                }
            },
            email: {
                message: '企业邮箱校验失败',
                validators: {
                    notEmpty: {
                        message: '企业邮箱不能为空'
                    },
                    regexp: {
                        regexp: /.*@zghuarun.com.*?(?=\b)/,
                        message: '邮箱格式不正确'
                    }
                }
            },
            identityCard: {
                message: '身份证校验失败',
                validators: {
                    notEmpty: {
                        message: '身份证号不能为空'
                    },
                    regexp: {
                        regexp: /\d{15}|\d{18}/,
                        message: '请核对身份证号后，再重新输入！'
                    },
                    threshold: 18
                    //         remote: {
                    //             type: 'POST',
                    //             url: '/rest/employee/validator/number',
                    //             message: '员工工号已经存在',
                    //             delay: 500,
                    //             data: function() {
                    //                 return {
                    //                     number: $('#number').val(),
                    //                     id: $('#id').val()
                    //                 }
                    //             }
                    //         }
                }
            },
            emergencyContact: {
                message: '紧急联系人电话校验失败',
                validators: {
                    notEmpty: {
                        message: '紧急联系人电话不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入正确的联系电话'
                    },
                    regexp: {
                        regexp: /^(1[3584]\d{9})$/,
                        message: '输入格式不正确'
                    },
                    threshold: 11
                }
            },
            addPosition: {
                message:"‘职位信息’校验失败！",
                validators:{
                    notEmpty: {
                        message: '职位信息不能为空'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z\u4E00-\u9FA5]+$/,
                        message: '职位信息能输入字母或汉字！'
                    },
                    stringLength:{
                        min: 2,
                        max: 10,
                        message: '职位的长度必须在2~10位之间'
                    },
                    remote: {
                        type: 'POST',
                        url: '/rest/employee/validator/position',
                        message: '该职位已经存在',
                        delay: 500,
                        data: function() {
                            return {
                                title: $("#addPosition").val(),
                                id: $("#positionType").val().split(",")[0]
                            }
                        }
                    }
                }
            },
            addPositionType: {
                message:"岗位类型校验失败",
                validators:{
                    notEmpty: {
                        message: '岗位类型不能为空'
                    },
                    regexp: {
                        regexp: /^[\u4e00-\u9fa5a-zA-Z]+$/,
                        message: '岗位类型只能输入字母或汉字！'
                    },
                    stringLength:{
                        min:2,
                        max:10,
                        message:'岗位类型的长度必须在2~10位之间'
                    },
                    remote: {
                        type: 'POST',
                        url: '/rest/employee/validator/positionType',
                        message: '该岗位类型已经存在',
                        delay: 500,
                        data: function() {
                            return {
                                title: $("#addPositionType").val()
                            }
                        }
                    }
                }
            },
            // workplace: {
            //     message:"工作地点校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /[\u4e00-\u9fa5a-zA-Z\d]/,
            //             message: '工作地点只能输入数字，字母或汉字！'
            //         },
            //         stringLength:{
            //             max:20,
            //             message:'岗位类型的长度不能超过20个字节'
            //         }
            //     }
            // },
            // personalMail: {
            //     message:"个人邮箱校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /(\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}|)/,
            //             message: '邮箱格式错误！正确格式：XXX@qq.com/XXX@163.com'
            //         }
            //     }
            // },
            // graduateInstitutions: {
            //     message:"毕业院校校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /[\u4e00-\u9fa5]/,
            //             message: '毕业院校只能输入汉字！'
            //         },
            //         stringLength:{
            //             max:10,
            //             message:'毕业院校的长度不能超过10个字节'
            //         }
            //     }
            // },
            // major: {
            //     message:"专业校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /[\u4e00-\u9fa5]/,
            //             message: '专业只能输入汉字！'
            //         },
            //         stringLength:{
            //             max:10,
            //             message:'专业的长度不能超过10个字节'
            //         }
            //     }
            // },
            // applicationMethod: {
            //     message:"招聘方式校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /[\u4e00-\u9fa5\d]/,
            //             message: '招聘方式只能输入字母/数字或汉字！'
            //         },
            //         stringLength:{
            //             max:10,
            //             message:'招聘方式的长度不能超过10个字节'
            //         }
            //     }
            // },
            // homePhone: {
            //     message:"家庭电话校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /(\d{3}-\d{8}|\d{4}-\d{7}|)/,
            //             message: '家庭电话填写错误！如：‘028-12345678’'
            //         }
            //     }
            // },
            // interests: {
            //     message:"兴趣爱好校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /[\u4e00-\u9fa5]/,
            //             message: '兴趣爱好只能输入汉字！'
            //         },
            //         stringLength:{
            //             max:10,
            //             message:'兴趣爱好的长度不能超过10个字节'
            //         }
            //     }
            // },
            // probation: {
            //     message:"试用期校验失败",
            //     validators:{
            //         regexp: {
            //             regexp: /[\u4e00-\u9fa5a-zA-Z\d]/,
            //             message: '试用期只能输入字母或汉字！'
            //         },
            //         stringLength:{
            //             max:10,
            //             message:'试用期的长度不能超过10个字节'
            //         }
            //     }
            // },
            password: {
                message: '密码校验失败',
                validators: {
                    regexp: {
                        regexp: /^[a-zA-Z0-9]([a-zA-Z0-9]|[._-]){0,10}$/,
                        message: '只能输入以字母、数字开头，可带“_”、“.”、“-”'
                    },
                    stringLength: {
                        min: 6,
                        max: 6,
                        message: '密码的长度必须是6位'
                    }
                }
            },
            password2: {
                message: '二次输入密码校验失败',
                validators: {
                    identical: {
                        field: 'password',
                        message: '两次密码输入不一致'
                    }
                }
            }
        }
    }).on('success.form.bv', function(e) {
        e.preventDefault();
        var $form = $(e.target);
        var origin = $form.serializeArray();
        var data = {};
        $.each(origin, function() {
            data[this.name] = this.value;
        });

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/employee';

            if (null !== data.id && 0 !== data.id) {
                data._method = 'PUT';
                url += ('/' + data.id);
            }
            data.headImageUri = $('#headImageUri').attr("src");
            data.status = (undefined === data.status) ? false : data.status;
            data.sex = (undefined === data.sex) ? false : data.sex;
            data.isSocialSecurityCards = (undefined === data.isSocialSecurityCards) ? false : data.isSocialSecurityCards;
            data.number = $('#number').text();
            data.namePinYin = $('#namePinYin').text();
            data.structureTitle = $('#structureTitle').text();
            data.education = $('#education').val();
            data.subsidiary = $('#subsidiary').val();
            data.department = $('#department').val();
            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function() {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#employeeFrom').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function(result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger(result.message);
                        $('#employeeFrom').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });

    $('.btn-cancel').on('click', function() {
        history.go(-1);
    });

    var $town = $('#citySelected select[name="town"]');
    var $employeePresentAddress = $('#employeePresentAddress').val();
    var townFormat = function (info) {
        $town.hide().empty();
        if (info['code'] % 1e4 && info['code'] < 7e5) {	//是否为“区”且不是港澳台地区
            $.ajax({
                url: 'http://passer-by.com/data_location/town/' + info['code'] + '.json',
                dataType: 'json',
                success: function (town) {
                    $town.show();
                    for (i in town) {
                        $town.append('<option value="' + town[i] + '">' + town[i] + '</option>');
                    }
                }
            });
        }
    };
    if (null !== $employeePresentAddress && $employeePresentAddress !== "") {
        var $addr = $employeePresentAddress.split("-");
        if ($addr.length === 5) {
            $('#street').val($addr[4])
        }
        $('#citySelected').citys({
            province: $addr[0],
            city: $addr[1],
            area: $addr[2],
            onChange: function (info) {
                if ($addr !== null && $addr[3] !== null && $addr[3] !== "") {
                    $town.val($addr[3]);
                    $addr = null;
                }
                townFormat(info);
            }
        }, function (api) {
            var info = api.getInfo();
            townFormat(info);
        });
    } else {
        $('#citySelected').citys({
            province: '四川',
            city: '成都',
            area: '',
            onChange: function (info) {
                townFormat(info);
            }
        }, function (api) {
            var info = api.getInfo();
            townFormat(info);
        });
    }
    loadPosition();

    search();

    $("#name").blur(function () {
        var name = $('#name').val();
        var $namePinYin = $('#namePinYin');
        $.ajax({
            url: '/rest/employee/namePinYin',
            data: {'name': name},
            method: 'POST',
            dataType: 'json',
            success: function (resoult) {
                var $chinesePinyin = resoult.content;
                if (null !== $chinesePinyin) {
                    $namePinYin.text("");
                    $namePinYin.text($chinesePinyin);
                    $('#email').val($chinesePinyin + '@zghuarun.com')
                }
            }
        })
    });

    var $BOX = {
        textPt          : $('#addPositionType'),
        textP           : $('#addPosition'),
        titlePt         : $('#boxTitlePT'),
        titleP          : $('#boxTitleP')

    };
    var $MODEL = {
        addBtn          : $('button#modalAddBtn'),
        delBtn          : $('#modalDelBtn'),
        titlePir        : $('p#primaryTitle'),
        titleDan        : $('p#dangerTitle'),
        danger          : $('#modal-danger'),
        primary         : $('#modal-primary'),
        CloseDan        : $('button#modalCloseBtn'),
        ClosePri        : $('button#primaryCloseBtn')

    };
    var $ELE= {
        hideP           : $('#positionId'),
        hidePt          : $('#positionTypeId'),
        pt              : $('#positionType'),
        p               : $('#position')

    };

    $(".showModal").on('click',function () {

        var $flag = $(this).attr("name");

        $ELE.hideP.attr("message",$flag);

    });

    $BOX.textPt.focus(function () { $BOX.textP.val("")});

    $BOX.textP.focus(function () { $BOX.textPt.val("")});

    $MODEL.addBtn.click(function () {

        var textPtv = $BOX.textPt.val();
        var textPv = $BOX.textP.val();
        var ptv = $ELE.pt.val();
        var reg = /^[a-zA-Z\u4E00-\u9FA5]+$/;
        var URL;
        var DATA;

        if (null !== textPtv && "" !==textPtv ) {
            if (reg.test(textPtv)) {

                URL = '/rest/employee/positionType';
                DATA = {'positionTypeTitle': textPtv};

                function success(data) {
                    $BOX.textPt.val("");
                    $MODEL.primary.modal('hide');
                    $ELE.pt.append("<option value='" + data.content.id + "," + data.content.title + "'>" + data.content.title + "</option>")

                    var child = $("#positionType option:first-child");

                    if (null !== ptv && "" !== ptv) {

                        $BOX.titlePt.text(ptv.split(",")[1]);

                    } else {
                        $BOX.titlePt.text(child.text());
                    }
                    $('.box-hide-show').selectpicker('refresh')
                }

                $http.POST(URL, DATA, success)
            } else {
                $MODEL.titlePir.text("信息验证未通过！");
                $MODEL.addBtn.addClass('disabled')
            }
        }
        if (null !== textPv && "" !==textPv && "" !== ptv) {
            if (reg.test(textPv)) {

                var ptId = ptv.split(",")[0];
                URL = '/rest/employee/position';
                DATA = {'positionTitle': textPv, 'positionTypeId': ptId};

                function success(data) {

                    $BOX.textP.val("");
                    $MODEL.primary.modal('hide');
                    $ELE.p.append("<option value='" + data.content.id + "," + data.content.title + "'>" + data.content.title + "</option>");
                    $ELE.p.selectpicker('refresh');

                    var child = $("#position option:first-child");
                    var pv = $ELE.p.val();

                    if (null !== pv && "" !== pv) {
                        $BOX.titleP.text(pv.split(",")[1])
                    } else {
                        $BOX.titleP.text(child.text())
                    }

                    $('.box-hide-show').selectpicker('refresh')

                }

                $http.POST(URL, DATA, success)
            } else {
                $MODEL.titlePir.text("信息验证未通过！");
                $MODEL.addBtn.addClass('disabled')
            }
        }
    });
    $MODEL.delBtn.click(function () {
        var $flag = $ELE.hideP.attr("message");
        var URL;

        if ($flag == 'position'){

            var hidePv = $ELE.hideP.val();
            var pv = $ELE.p.val();

            if ( null !== pv && "" !== pv){
                hidePv = pv.split(",")[0];
                URL = '/rest/employee/position/delete/'+hidePv;

                function success() {
                    $MODEL.danger.modal('hide');

                    $("#position option").each(function () {
                        if ($(this).val() == pv) {
                            $(this).remove();
                            return false;
                        }
                    });

                    var child = $("#position option:first-child");

                    if (null === child || "" === child){
                        $BOX.titleP.text("");
                        $ELE.hidePt.val(0);
                        $ELE.hideP.val("")
                    }else {
                        $BOX.titleP.text(child.text());
                    }

                    $('.box-hide-show').selectpicker('refresh')
                }

                $http.GET(URL,null,success)

            }else {
                $MODEL.titleDan.text("没有可以删除的选项！");
                $MODEL.delBtn.addClass('disabled');
                $ELE.hidePt.val(0)
            }
        }
        if ($flag == 'positionType'){
            var hidePtv = $ELE.hidePt.val();
            var ptv = $ELE.pt.val();

            if ( null !== ptv && ""!== ptv){

                var pv = $ELE.p.val();
                if (null === pv || "" === pv){

                    hidePtv = ptv.split(",")[0];
                    URL = '/rest/employee/positionType/delete/'+hidePtv;

                    function success() {
                        $MODEL.danger.modal('hide');
                        $("#positionType option").each(function () {

                            if($(this).val() == ptv){
                                $(this).remove();
                                return false;
                            }
                        });

                        $BOX.titlePt.text($("#positionType option:first-child").text());
                        $('.box-hide-show').selectpicker('refresh');

                        loadPosition();
                    }
                    $http.GET(URL,null,success);
                }else {
                    var msg = $ELE.hidePt.attr("message");
                    $MODEL.titleDan.text(msg);
                    $MODEL.delBtn.addClass('disabled')
                }
            }else {
                $MODEL.titleDan.text("没有可以删除的选项！");
                $MODEL.delBtn.addClass('disabled');
            }
        }
    });

    $MODEL.CloseDan.on('click',function () {
        $MODEL.delBtn.removeClass('disabled');
        $MODEL.titleDan.text("是否将此职位信息从数据中移除，点击确认继续，点击关闭取消操作。");
        $MODEL.danger.modal('hide');
    });

    $MODEL.ClosePri.on('click',function () {
        $MODEL.addBtn.removeClass('disabled');
        $MODEL.titlePir.text("正在执行添加操作，您可以点击确认继续，点击关闭则退出。");
        $MODEL.primary.modal('hide');
    });
});

function loadDepartment() {
    var $company = $("#subsidiary").val();
    var $companyId;
    var url = '/rest/structure/department/';
    var $department = $("#department");
    if (null !== $company) {
        $companyId = $company.split(",")[0];
        url += $companyId
    }
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            if (data.content !== null) {
                var $size = data.content.length;
                $department.empty();
                for (var i = 0; i < $size; i++) {
                    $department.append(
                        "<option value='" + data.content[i].id + "," + data.content[i].title + "," + data.content[i].number +
                        "'>" + data.content[i].title + "[" + data.content[i].number + "]</option>")
                }
                $('.select').selectpicker('refresh')
            }
        }
    })
}

function loadPosition() {
    var positionTypeIdElm = $("#positionTypeId");
    var positionTypeElm = $("#positionType");
    var positionTitle = $("#positionId").val();
    var positionTypeElmValue = positionTypeElm.val();
    var $positionTypeId;
    var $positionTypeTitle;
    var url = '/rest/employee/position/';
    var $position = $("#position");
    if (null !== positionTypeElmValue) {
        $positionTypeId = positionTypeElmValue.split(",")[0];
        $positionTypeTitle = positionTypeElmValue.split(",")[1];
        $('#boxTitlePT').text($positionTypeTitle);
        url += $positionTypeId
    }
    $http.GET(url,null,success);
    function success (data) {
        if (data.content !== null) {
            var $size = data.content.length;
            $position.empty();
            for (var i = 0; i < $size; i++) {
                if (null !== positionTitle && "" !==positionTitle && data.content[i].title == positionTitle) {
                    $position.append(
                        "<option value='" + data.content[i].id + "," + data.content[i].title + "' selected>" + data.content[i].title + "</option>")
                }else {
                    $position.append(
                        "<option value='" + data.content[i].id + "," + data.content[i].title + "'>" + data.content[i].title + "</option>")
                }
            }
            $('.box-hide-show').selectpicker('refresh');
            if ($size > 0){
                $('#boxTitleP').text($("#position option:first-child").text());
                positionTypeIdElm.val(-1);
                positionTypeIdElm.attr("message","该岗位类型“"+$positionTypeTitle+"”下还有其他职位，不可进行此操作，请删除相关联的职位后进行此操作，点击关闭则退出。")
            }else {
                $('#boxTitleP').text("");
                positionTypeIdElm.val($positionTypeId);
                positionTypeIdElm.attr("message","是否将该职位从数据中移除，点击确认继续，点击关闭取消操作。")
            }
        }
        hideInputGetValue();
    }
}

function loadSuperiors(keywords) {
    var $superiors = $('#superiors');
    var url = '/rest/employee/superiors';
    if (null !== keywords && "" !== keywords){
        var data = {keywords:keywords};
        function success(data) {
            if (data.content !== null && data.content.length !== 0) {
                var $size = data.content.length;
                $superiors.empty();
                for (var i = 0; i < $size; i++) {
                    $superiors.append(
                        "<option value='" + data.content[i].name + "'>" + data.content[i].name + "</option>")
                }
                $superiors.selectpicker('refresh')
            }
        }
        $http.GET(url,data,success);
    }
}

function hideInputGetValue() {
    var positionElm = $("#position");
    var positionIdElm = $("#positionId");
    var $position = positionElm.val();
    if(""!== $position && null !== $position){
        var $positionId = $position.split(",")[0];
        var $positionTitle = $position.split(",")[1];
        positionIdElm.val($positionId);
        $('#boxTitleP').text($positionTitle)
    }else {
        positionIdElm.val("");
    }
}

function search() {
    var aResult = [];  //用于存放从后台接收到的匹配结果
    $.ajax({
        type: 'get',
        url: "/json/school.txt",
        dataType: "json",
        success: function (data) {
            if (data === null || data === "") {
                return;
            }
            aResult = data;
            $(".auto-complete").autocomplete({lookup: aResult})
        }
    });
}
