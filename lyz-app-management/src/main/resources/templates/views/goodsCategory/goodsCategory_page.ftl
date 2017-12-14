<head>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
</head>
<body>

<section class="content-header">
<#if selectedMenu??>
    <h1>${selectedMenu.resourceName!'??'}</h1>
    <ol class="breadcrumb">
        <li><a href="/views"><i class="fa fa-home"></i> 首页</a></li>
        <#if selectedMenu.parent??>
            <li><a href="javascript:void(0);">${selectedMenu.parent.parentResourceName!'??'}</a></li>
        </#if>
        <li class="active">${selectedMenu.resourceName!'??'}</li>
    </ol>
<#else>
    <h1>加载中...</h1>
</#if>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-primary">
                <div id="toolbar" class="form-inline">
                    <button id="btn_add" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增
                    </button>
                    <button id="btn_edit" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑
                    </button>
                    <button id="btn_delete" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除
                    </button>
                    <select name="goodsCategoryCode" id="goodsCategoryCode"  class="form-control select" style="width:auto;"  data-live-search="true" onchange="findGoodsCategoryByPid(this.value);">
                        <option value="-1">选择父类分级</option>
                    </select>
                    <div class="input-group col-md-3" style="margin-top:0px positon:relative">
                        <input type="text" name="queryCategoryInfo" id="queryCategoryInfo" class="form-control " style="width:auto;"  placeholder="请输入分类名称..">
                        <span class="input-group-btn">
                            <button type="button" name="search" id="search-btn" class="btn btn-info btn-search"
                                    onclick="return findGoodsCategoryByPcode()">查找</button>
                        </span>
                    </div>
                </div>
                <div class="box-body table-reponsive">
                    <table id="dataGrid" class="table table-bordered table-hover">

                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    $(function() {
        initDateGird('/rest/goodsCategorys/page/grid');
        findGoodsCategorySelection();
        $('#btn_add').on('click', function () {
            $grid.add('/views/admin/goodsCategorys/add?parentMenuId=${(parentMenuId!'0')}');
        });

        $('#btn_edit').on('click', function() {
            $grid.modify($('#dataGrid'), '/views/admin/goodsCategorys/edit/{id}?parentMenuId=${parentMenuId!'0'}')
        });
        $('#btn_delete').on('click', function () {
            $grid.remove($('#dataGrid'), '/rest/goodsCategorys', 'delete');
        });
    });

    function findGoodsCategorySelection(){
        var goodsCategory = "";
        $.ajax({
            url: '/rest/goodsCategorys/findGoodsCategorySelection',
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
                    goodsCategory += "<option value=" + item.id + ">" +item.categoryName + "</option>";
                })
                $("#goodsCategoryCode").append(goodsCategory);
            }
        });
    }

    function initDateGird(url){
        $grid.init($('#dataGrid'), $('#toolbar'),url, 'get', false, function(params) {
            return {
                offset: params.offset ,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择',
            formatter:stateFormatter
        },
            {
                field: 'id',
                title: '分类id',
                align: 'center',
            },{
                field: 'categoryName',
                title: '分类名称',
                align: 'center'
            }, {
                field: 'paCategoryCode',
                title: '父级分类',
                align: 'center',
            },{
                field: 'sortId',
                title: '排序号',
                align: 'center'
            },
        ]);
    }

    function stateFormatter(value, row, index) {
        if (row.paCategoryCode =='-')
            return {
                disabled : true,//设置是否可用
            };
    }

    var formatDateTime = function (date) {
        var dt = new Date(date);
        var y = dt.getFullYear();
        var m = dt.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = dt.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = dt.getHours();
        h=h < 10 ? ('0' + h) : h;
        var minute = dt.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        var second=dt.getSeconds();
        second=second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
    };


    function findGoodsCategoryByPid(pid){
        $("#queryCategoryInfo").val('');
        $("#dataGrid").bootstrapTable('destroy');
        if('-1'==pid){
            initDateGird('/rest/goodsCategorys/page/grid');
        }else{
            initDateGird('/rest/goodsCategorys/findGoodsCategoryByPid/'+pid);
        }
    }

    function findGoodsCategoryByPcode(){
        $("#goodsCategoryCode").val('-1');
        $("#dataGrid").bootstrapTable('destroy');
        var queryCategoryInfo =$("#queryCategoryInfo").val();
        if(''==queryCategoryInfo||null==queryCategoryInfo){
           initDateGird('/rest/goodsCategorys/page/grid');
        }else{
            initDateGird('/rest/goodsCategorys/findGoodsCategoryByPcode/'+queryCategoryInfo);
         }
    }
</script>
</body>