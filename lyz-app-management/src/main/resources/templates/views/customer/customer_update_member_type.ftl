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

    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css"/>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
    <script src="https://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>



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

            $('#customer_edit').bootstrapValidator({
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
                                url: '/rest/customers/isExistPhoneNumberByCusId',
                                message: '该电话已被使用',
                                delay: 500,
                                data: function () {
                                    return {
                                        mobile: $('#mobile').val(),
                                        cusId: $('#cusId').val(),
                                    }
                                }
                            }
                        }
                    },
                }
            }).on('success.form.bv', function (e) {
                e.preventDefault();
                var mobile = $('#mobile').val();
                var cusId = $('#cusId').val();
                var birthday = $('#birthday').val();
                var sex = $('#sex').val();
                var name = $('#name').val();
                var memberType = $('#memberType').val();
                var sellerId = $('#sellerId').val();
                var sellerName = $('#sellerName').val();
                if (null == sellerId || '' == sellerId){
                    $notify.danger("导购id不能为空！");
                    return false;
                }
                var data = {
                    "mobile": mobile,
                    "cusId": cusId,
                    "birthday": birthday,
                    "sex": sex,
                    "name": name,
                    "memberType":memberType,
                    "sellerId":sellerId,
                    "sellerName":sellerName
                };
                var url = '/rest/customers/update/memberType';
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: url,
                        method: 'POST',
                        data: data,
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                            $('#customer_edit').bootstrapValidator('disableSubmitButtons', false);
                        },
                        success: function (result) {
                            if (0 === result.code) {
                                window.location.href = document.referrer;
                            } else {
                                clearTimeout($global.timer);
                                $loading.close();
                                $global.timer = null;
                                $notify.danger(result.message);
                                $('#customer_edit').bootstrapValidator('disableSubmitButtons', false);
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
    <h1>编辑顾客</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="customer_edit" enctype="multipart/form-data">
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
                                    <input name="cusId" type="hidden" class="form-control" id="cusId"
                                           value="${customer.cusId?c}">
                                    <input name="name" type="text" class="form-control" id="cusName"
                                           value="${customer.name!''}" placeholder="顾客姓名" readonly>
                                </div>
                            </div>
                        </div>
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
                                           value="${customer.phone!''}" placeholder="电话号码" readonly>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    性别
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择用户性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="sex" id="sex" disabled="disabled">
                                    <option value="MALE" <#if customer?? && customer.sex == '男'>selected</#if>>男</option>
                                    <option value="FEMALE"  <#if customer?? && customer.sex == '女'>selected</#if>>女</option>
                                    <option value="SECRET" <#if customer?? && customer.sex == '保密'>selected</#if>>保密</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    出生日期
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input name="birthday" type="text" class="form-control datepicker" id="birthday" value="${customer.birthday!''}" disabled="disabled">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    会员类型
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="选择用户性别（如不愿透露，可选“保密”）"></i>
                                </label>
                                <select class="form-control select" name="memberType" id="memberType">

                                <#if rankClassificationList?? && rankClassificationList?size gt 0>
                                    <#list rankClassificationList as item>
                                            <option value="${item.rankCode!''}"
                                                    <#if customer??&&(customer.memberType!'') == (item.rankCode!'')>selected<#else ></#if>>
                                            ${item.rankCode!'COMMON'}
                                            </option>
                                    </#list>
                                </#if>

                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label>
                                    <li style="list-style: none">
                                        <button type="button" class="btn btn-primary btn-xs"
                                                onclick="openSellerModal()">
                                            选择导购
                                        </button>
                                    </li>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sellerName" type="text" class="form-control" id="sellerName"
                                           value="${customer.sellerName!''}" placeholder="归属导购">
                                    <input name="sellerId" type="hidden" class="form-control" id="sellerId"
                                           value="<#if customer.sellerId??>${customer.sellerId?c}</#if>">
                                </div>
                                <#--<select class="form-control select" name="memberType" id="memberType">-->

                                <#--<#if rankClassificationList?? && rankClassificationList?size gt 0>-->
                                    <#--<#list rankClassificationList as item>-->
                                        <#--<option value="${item.rankCode!''}"-->
                                                <#--<#if customer??&&(customer.memberType!'') == (item.rankCode!'')>selected<#else ></#if>>-->
                                        <#--${item.rankCode!'COMMON'}-->
                                        <#--</option>-->
                                    <#--</#list>-->
                                <#--</#if>-->
                                <#--</select>-->
                            </div>
                        </div>
                    </div>
                    <!-- 导购选择框 -->
                    <div id="selectSeller" class="modal fade" tabindex="-1" role="dialog">
                        <div class="modal-dialog" role="document" style="width: 60%">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4>选择导购</h4>
                                </div>
                                <div class="modal-body">
                                    <!--  设置这个div的大小，超出部分显示滚动条 -->
                                    <div id="selectTree" class="ztree" style="height: 60%;overflow:auto; ">
                                        <section class="content">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="box box-primary">
                                                        <div id="sellerToolbar" class="form-inline">

                                                            <div class="input-group col-md-3"
                                                                 style="margin-top:0px positon:relative">
                                                                <input type="text" name="sellerQueryConditions"
                                                                       id="sellerQueryConditions"
                                                                       class="form-control" style="width:auto;"
                                                                       placeholder="请输入导购姓名或电话">
                                                                <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findSellerByNameOrMobil()">查找</button>
                        </span>
                                                            </div>
                                                        </div>
                                                        <div class="box-body table-reponsive">
                                                            <table id="sellerDataGrid"
                                                                   class="table table-bordered table-hover">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                    </div>
                                </div>
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
    });

    function initSeller(url) {

        var cityId = $('#cityId').val();
        var storeId = $('#storeId').val();

        $("#sellerDataGrid").bootstrapTable('destroy');
        $grid.init($('#sellerDataGrid'), $('#sellerToolbar'), url, 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        }, {
            field: 'empId',
            title: 'ID',
            align: 'center'
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center'
        }, {
            field: 'name',
            title: '导购姓名',
            align: 'center',

            events: {
                'click .scan': function (e, value, row) {
                    showSeller(row.empId, value);
                }
            },
            formatter: function (value) {
                if (null == value) {
                    return '<a class="scan" href="#">' + '未知' + '</a>';
                } else {
                    return '<a class="scan" href="#' + value + '">' + value + '</a>';
                }
            }
        }, {
            field: 'mobile',
            title: '导购电话',
            align: 'center'
        },{
            field: 'storeType',
            title: '门店类型',
            align: 'center',
            visible: false
        }
        ]);
    }

    //选择导购
    function openSellerModal() {

        //查询导购列表
        initSeller('/rest/employees/select/seller');
        $("#sellerModalConfirm").unbind('click').click(function () {
        });
        $('#selectSeller').modal('show');
    }

//选中导购
    function showSeller(id, name) {
        document.getElementById("sellerId").value = id;
        document.getElementById("sellerName").value = name;
        $('#sellerId').text(id);
        $('#sellerName').text(name);
        $('#selectSeller').modal('hide');
    }

    //条件查询导购
    function findSellerByNameOrMobil() {
        var sellerQueryConditions = $("#sellerQueryConditions").val();
        $("#sellerDataGrid").bootstrapTable('destroy');
        if (null == sellerQueryConditions || "" == sellerQueryConditions) {
            initSeller('/rest/employees/select/seller');
        } else {
            initSeller('/rest/employees/select/seller/' + sellerQueryConditions);
        }
    }
</script>
</body>