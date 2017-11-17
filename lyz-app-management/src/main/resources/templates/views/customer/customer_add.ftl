<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/select2/4.0.3/css/select2.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">


    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/bootstrap-fileinput-master/css/fileinput.css" />
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/bootstrap-fileinput-master/js/fileinput.js"></script>
   <#-- <script type="text/javascript" src="/javascript/fileinput_locale_zh.js"></script>-->


    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script>
        $(function () {
            $(".select2").select2();
            $('.btn-cancel').on('click', function () {
                history.go(-1);
            });

            if (!$global.validateMobile()) {
                $('.select').selectpicker();
            }

            $(function () {
                $('[data-toggle="tooltip"]').popover();
            });

            $('.datepicker').datepicker({
                format: 'yyyy-mm-dd',
                language: 'zh-CN',
                autoclose: true
            });

            $('.switch').bootstrapSwitch();

            $('#customer_add').bootstrapValidator({
                framework: 'bootstrap',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                verbose: false,
                fields: {
                    name: {
                        message: '顾客姓名校验失败',
                        validators: {
                            notEmpty: {
                                message: '顾客姓名不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9\u4E00-\u9FA5]+$/,
                                message: '顾客姓名只能输入字母或汉字'
                            },
                            stringLength: {
                                min: 2,
                                max: 10,
                                message: '顾客姓名的长度必须在2~10位之间'
                            },
                        }
                    },
                    nickName: {
                        message: '微信昵称校验失败',
                        validators: {
                            stringLength: {
                                min: 2,
                                max: 10,
                                message: '微信昵称的长度必须在2~10位之间'
                            },
                        }
                    },
                    mobile: {
                        message: '手机号码校验失败',
                        validators: {
                            notEmpty: {
                                message: '手机号码不能为空'
                            },
                            regexp: {
                                regexp: /^1[34578]\d{9}$/,
                                message: '手机号码只能输入11位数字'
                            },
                            remote: {
                                type: 'POST',
                                url:  '/rest/customers/isExistPhoneNumber',
                                message: '该电话已被使用',
                                delay: 500,
                                data: function () {
                                    return {
                                        mobile: $('#mobile').val(),
                                    }
                                }
                            }
                        }
                    },
                }
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                var $form = $(e.target);
                var origin = $form.serializeArray();
                var data = {};
                var formData = new FormData($( "#customer_add" )[0]);
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    var url = '/rest/customers';
                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#customer_add').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#customer_add').bootstrapValidator('disableSubmitButtons', false);
                            }
                        }
                    });
                }
            });
        });
    </script>
    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
</head>
<body>

<section class="content-header">
    <h1>新增顾客</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="customer_add" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    顾客姓名
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中英文字符，长度在2~20之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="name" type="text" class="form-control" id="cusName"
                                           placeholder="顾客姓名">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    微信昵称
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="nickName" type="text" class="form-control" id="nickName"
                                           placeholder="微信昵称">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    电话号码
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入11位数字"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="mobile" type="text" class="form-control" id="mobile"
                                           placeholder="电话号码">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    性别
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择用户性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="sex" id="sex" >
                                    <option value="MALE">男</option>
                                    <option value="FEMALE">女</option>
                                    <option value="SECRET">保密</option>
                                </select>
                            </div>
    <#--                        <div class="col-xs-6 col-md-6">
                                <div class="form-group">
                                    <label for="description">
                                        顾客头像
                                    </label>
                                    <div class="form-inline">
                                        <span class="input-group-addon form-control"><i class="fa fa-pencil"></i></span>
                                        <input name="picUrltext" type="text" class="form-input form-control" id="picUrltext"
                                               value="www.baidu.com" >
                                    </div>
                                </div>
                            </div>-->
<#--                            <div class="col-xs-6 col-md-6">
                                <div class="form-group">
                                    <label>&nbsp;</label>
                                    <div class="input-group">
                                        <input name="picUrl" type="file" style="display:inline;width: 10%" class="form-control" id="picUrl">
                                    </div>
                                </div>
                            </div>-->
                        </div>
                    </div>


                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    顾客类型
                                </label>
                                <select class="form-control select" name="customerType" id="customerType" >
                                    <option value="MEMBER">会员</option>
                                    <option value="RETAIL">零售</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    出生日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="birthday" type="text" class="form-control datepicker" id="birthday"
                                           readonly   placeholder="出生日期">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    归属城市
                                </label>
                                <select name="cityId" id="cityId" class="form-control select"   onchange="findStoreByCity(this.value);" >
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    归属门店
                                </label>
                                <select name="storeId" id="storeId" class="form-control select"   onchange="findGuide()">
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    归属导购
                                </label>
                                <select name="salesConsultId" id="guideId" class="form-control select" >
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                            <label>
                                是否货到付款
                            </label>
                            <select class="form-control select" name="isCashOnDelivery" id="isCashOnDelivery" >
                                <option value="true">是</option>
                                <option value="false">否</option>
                            </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="description">
                                    顾客头像
                                </label>
                                <div class="form-inline">
                                    <input name="file" type="file"  class="form-control" id="file">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                    <div class="form-group">
                                        <label for="status">是否启用</label>
                                        <br>
                                        <input name="status" class="switch" id="status" type="checkbox" checked
                                               data-on-text="启用" data-off-text="停用"/>
                                    </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-8"></div>
                        <div class="col-xs-12 col-md-2">
                            <button type="submit" class="btn btn-primary footer-btn">
                                <i class="fa fa-check"></i> 保存
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-2">
                            <button type="button" class="btn btn-danger footer-btn btn-cancel">
                                <i class="fa fa-close"></i> 取消
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<script>
    $(function () {
        if (!$global.validateMobile()) {
            $('.select').selectpicker();
        }

        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            language: 'zh-CN',
            autoclose: true
        });

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
                    city += "<option value=" + item.cityId + ">" +item.name + "</option>";
                })
                $("#cityId").append(city);
                $('#cityId').selectpicker('refresh');
                findStoreByCity(1);
            }
        });
      initFileInput("file", "/api/OrderApi/ImportOrder");
    });


    function  findStoreByCity(cityId){
        $("#storeId").empty()
        var store;
        $.ajax({
            url: '/rest/stores/findStoresListByCityId/'+cityId,
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
                    store += "<option value=" + item.storeId + ">" +item.storeName + "</option>";
                })
                $("#storeId").append(store);
                $('#storeId').selectpicker('refresh');
                findGuide();
            }
        });
    }

    function findGuide(){
        var storeId= $("#storeId").val();
        if(null==storeId){
            return false ;
        }
        $("#guideId").empty();
        var guide ;
        $.ajax({
            url: '/rest/employees/findGuidesListById/'+storeId,
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
                    guide += "<option value=" + item.empId + ">" +item.name + "</option>";
                })
                $("#guideId").append(guide);
                $('#guideId').selectpicker('refresh');
            }
        });
    }



    function initFileInput(ctrlName,uploadUrl) {
        var control = $('#' + ctrlName);
        control.fileinput({
            language: 'zh', //设置语言
           /* uploadUrl:"/rest/customers", //上传的地址*/
            allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
            //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
            uploadAsync: true, //默认异步上传
            showUpload:false, //是否显示上传按钮
            showRemove :true, //显示移除按钮
            showPreview :false, //是否显示预览
            showCaption:true,//是否显示标题
            browseClass:"btn btn-primary", //按钮样式
            dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            //maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
            //minFileCount: 0,
            maxFileCount:1, //表示允许同时上传的最大文件个数
            enctype:'multipart/form-data',
            validateInitialCount:true,
            previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        }).on("fileuploaded", function (event, data, previewId, index){
            if(data.response) {
                alert('处理成功');
            }
        });
    }

</script>
</body>