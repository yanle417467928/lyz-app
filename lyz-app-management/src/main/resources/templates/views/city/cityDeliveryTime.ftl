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
    $(function () {
        $grid.init($('#dataGrid'), $('#toolbar'), '/rest/citys/page/grid', 'get', false, function (params) {
            return {
                offset: params.offset,
                size: params.limit,
                keywords: params.search
            }
        }, [{
            checkbox: true,
            title: '选择'
        },
            {
                field: 'cityId',
                title: '城市ID',
                align: 'center',
            }, {
            field: 'name',
            title: '城市名称',
            align: 'center',
            formatter: function(value, row) {
                return '<a class="scan" href="cityDeliveryTimeList/'+row.cityId+'?parentMenuId=${(parentMenuId!'0')}">' + value + '</a>';
            }
        }
        ]);
    });

    var $page = {
        information: {
            show: function (cityId) {
                if (null === $global.timer) {
                    $global.timer = setTimeout($loading.show, 2000);
                    $.ajax({
                        url: '/rest/cityDeliveryTime/' + cityId,
                        method: 'GET',
                        error: function () {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            $notify.danger('网络异常，请稍后重试或联系管理员');
                        },
                        success: function (result) {
                            clearTimeout($global.timer);
                            $loading.close();
                            $global.timer = null;
                            if (0 === result.code) {
                                var data = result.content;
                                var deliveryLength = data.length;
                                StitchingTime(data,deliveryLength);
                                $('#menuTitle').html("城市配送时间详情");
                                if (null === data[0].cityId) {
                                    data[0].cityId = '-';
                                }
                                $('#cityId').html(data[0].cityId);

                                if (null === data[0].cityName) {
                                    data[0].cityName = '-';
                                }
                                $('#name').html(data[0].cityName);

                                $('#information').modal();
                            } else {
                                $notify.danger(result.message);
                            }
                        }
                    })
                }
            },
            close: function () {
                $('#information').modal('hide');
            }
        }
    }

    /**
     * 动态添加该城市所有配送时间
     * @param data
     * @param deliveryLength
     * @constructor
     */
    function StitchingTime(data,deliveryLength){
        $('.list-group-item').next().nextAll().remove();
        var deliveryTime = new Array();
        var deliveryTimeId ='#deliveryTime';
        for(var i=0;i<deliveryLength;i++){
             deliveryTime[i]=data[i].startTime+'至'+data[i].endTime;
            $("#deliveryTimeDetail").append("<li class='list-group-item' ><b>配送时间段"+(i+1)+"</b><a class='pull-right' id='deliveryTime"+i+"'></a></li>");
            $(deliveryTimeId+i).html(deliveryTime[i]);
        }
    }


    function modify(container, url) {
        var selected = this.getSelectedIds(container);
        var length = selected.length;
        if (length === 0) {
            $notify.warning('请在点击按钮前选中一条数据');
        } else if (length > 1) {
            $notify.warning('您每次只能选择一条数据进行修改');
        } else {
            var id = selected[0];
            url = url.replace('{id}', id);
            window.location.href = url;
        }
    }


    function getSelectedIds(container){
        var ids = [];
        var selected = container.bootstrapTable('getSelections');
        for (var i = 0; i < selected.length; i++ ) {
            var data = selected[i];
            ids.push(data.cityId);
        }
        return ids;
    }
</script>
</body>