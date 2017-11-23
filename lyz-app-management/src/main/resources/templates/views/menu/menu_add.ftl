<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css"
          rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>

    <script src="/javascript/menu_add.js"></script>
    <style>
        .select {
            -webkit-appearance: none;
        }
    </style>
</head>
<body>

<section class="content-header">
    <h1>添加新菜单</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1-1">
                <form id="menuForm">
                    <input type="hidden" id="id" name="id"
                    <#if menuDO?? && menuDO.id??>
                           value="${menuDO.id?c}"
                    <#else>
                           value="0"
                    </#if>
                    >
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="type">
                                    父级菜单
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="二级菜单选择指定父级菜单；一级菜单，请选择'无父级菜单'"></i>
                                </label>
                                <select id="parentId" name="parentId" class="form-control select"
                                        data-live-search="true">
                                    <option value="0">无父级菜单</option>
                                <#if menuVOList?? && menuVOList?size gt 0>
                                    <#list menuVOList as item>
                                        <option value="${(item.id!'0')?c}"
                                                data-icon="${item.iconStyle!'fa fa-circle-o'}"
                                                <#if menuDO?? && menuDO.id?? && menuDO.id?c == (item.id!'0')?c>selected</#if>>
                                        ${item.title!'加载失败...'}
                                        </option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="title">
                                    菜单标题
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="只能输入中文，长度在2~10之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="title" type="text" class="form-control" id="title" placeholder="菜单标题"
                                           value="<#if menuDO??>${menuDO.title!'加载失败...'}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="linkUri">
                                    链接地址
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="输入菜单跳转的链接，如果没有请输入'#'；
                                       长度在1~100之间，只能输入站内链接资源，
                                       且不允许带参数，例如：/example"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="linkUri" type="text" class="form-control" id="linkUri"
                                           placeholder="链接地址"
                                           value="<#if menuDO??>${menuDO.linkUri!'加载失败...'}</#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="iconStyle">
                                    图标样式
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="图标样式可参考:http://fontawesome.io/icons/；长度在0~50个之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="iconStyle" type="text" class="form-control" id="iconStyle"
                                           placeholder="图标样式"
                                           value="<#if menuDO??>${menuDO.iconStyle!''}</#if>">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="referenceTable">
                                    相关数据表
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="输入点击菜单后操作的主要数据表名，一级菜单可以不填写；长度在0~20之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="referenceTable" type="text" class="form-control" id="referenceTable"
                                           placeholder="相关数据表" value="<#if menuDO??>${menuDO.referenceTable!''}</#if>">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="sortId">
                                    排序号
                                    <i class="fa fa-question-circle i-tooltip" data-toggle="tooltip"
                                       data-content="菜单的排序标识，数字越小，排序越靠前，范围在1~999之间"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sortId" type="number" class="form-control" id="sortId"
                                           placeholder="排序号"
                                           value="<#if menuDO??>${(menuDO.sortId!'99999')?c}<#else>99999</#if>">
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

    function submitForm() {
        $("#add_user").submit();
    }

    function checkMenuType(menuType) {
        if (menuType == "CHILD") {
            $('#parent_info').show()

        } else if (menuType == "PARENT") {
            $('#parent_info').hide()

        }
    }
</script>
</body>