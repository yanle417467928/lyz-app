<head>
    <link href="https://cdn.bootcss.com/bootstrap-select/1.12.2/css/bootstrap-select.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet">
    <link href="/stylesheet/devkit.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.12.2/js/i18n/defaults-zh_CN.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datepicker/1.6.4/locales/bootstrap-datepicker.zh-CN.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js"></script>
 <#--   <script src="/javascript/goods_edit.js"></script>-->
    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap-fileinput-master/css/fileinput.css" />
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script type="text/javascript" src="/plugins/bootstrap-fileinput-master/js/locales/zh.js"></script>


    <script type="text/javascript" charset="utf-8" src="/mag/js/kindeditor-min.js"></script>
    <script type="text/javascript" charset="utf-8" src="/mag/js/zh_CN.js"></script>

    <style type="text/css">
        .box-default {
            border-top-width: 0px;
            margin-bottom: 0px;
            position: absolute;
            z-index: 99;
        }

        .box-title {
            font-size: 12px !important;
        }

        .box-body {
            display: block;
        }

        .box-footer {
            text-align: right;
        }
    </style>
</head>
<body>
<section class="content-header">
    <h1>编辑商品信息</h1>
</section>
<section class="content">
    <div class="nav-tabs-custom">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_1-1" data-toggle="tab">基本信息</a></li>
            <li><a href="#tab_1-3" data-toggle="tab">商品详情页</a></li>
            <li><a href="#tab_1-2" data-toggle="tab">扩展选项</a></li>
        </ul>
        <form id="goodsFrom" class="bv-form tab-content">
            <div class="tab-content">
                <div class="tab-pane active" id="tab_1-1">
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsName">商品ID
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入商品ID"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="id" type="text" class="form-control" id="id" readonly
                                           placeholder="商品ID"
                                           value="${(goodsVO.id)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">物料编号
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入物料编号"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sku" type="text" class="form-control" id="sku" readonly
                                           placeholder="物料编号"
                                           value="${(goodsVO.sku)!''}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsName">物料名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入物料名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="materialsName" type="text" class="form-control" id="materialsName" readonly
                                           placeholder="物料名称"
                                           value="${(goodsVO.materialsName)!''}">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="title">电商名称
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入电商名称"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="skuName" type="text" class="form-control" id="skuName"
                                           placeholder="电商名称"
                                           value="${(goodsVO.skuName)!''}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="goodsCategoryCode">电商分类
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入电商分类"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <select id="goodsCategoryCode" name="categoryName" class="form-control" data-live-search="true">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sortId">排序号
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="输入排序号"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="sortId" type="text" class="form-control" id="sortId"
                                           placeholder="排序号"
                                           value="${(goodsVO.sortId)!''}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="onSaleTime">搜索关键字
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="搜索关键字"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <input name="searchKeyword" type="text" class="form-control"
                                           value="${(goodsVO.searchKeyword)!''}"
                                           id="searchKeyword" placeholder="搜索关键字">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label>产品档次
                                    <i class="fa fa-question-circle i-tooltip hidden-xs" data-toggle="tooltip"
                                       data-content="选择产品档次"></i>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                    <select id="productGrade" name="productGrade" class="form-control select" data-live-search="true">
                                        <option value="1"
                                                <#if goodsVO?? && goodsVO.productGrade?? && (goodsVO.productGrade) =='1'>selected</#if>>
                                            高档
                                        </option>
                                        <option value="2"
                                                <#if goodsVO?? && goodsVO.productGrade?? && (goodsVO.productGrade) == '2'>selected</#if>>
                                            中档
                                        </option>
                                        <option value="3"
                                                <#if goodsVO?? && goodsVO.productGrade?? && (goodsVO.productGrade) == '3'>selected</#if>>
                                            低档
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为热门商品</label>
                                <div class="input-group">
                                    <input name="isHot" id="isHot" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isHot?? && goodsVO.isHot)>checked</#if>>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="sex">是否为调色商品</label>
                                <div class="input-group">
                                    <input name="isColorMixing" id="isColorMixing" class="switch" type="checkbox"
                                           <#if !goodsVO?? || (goodsVO.isColorMixing?? && goodsVO.isColorMixing)>checked</#if>>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="tab-pane" id="tab_1-2">
                <#--图片上传功能完善后再优化-->
                    <div class="row" >
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="exampleInputFile">封面图片</label>
                                 <input id="coverImagefile"  type="file" name="coverImage"  multiple class="file-loading">
                           </div>
                        </div>
                        <div class="col-md-6 col-xs-12">
                            <div class="form-group">
                                <label for="exampleInputFile">轮播图片</label>
                                <input  id="rotationImagefile"type="file" name="rotationImage" multiple class="file-loading" >
                            </div>
                        </div>
                    </div>
                    <div class="row" style="height: 150px">
                        <div class="col-md-6 col-xs-12"  >
                            <div class="form-group" id ='coverImageShow' >
                                <input name="coverImageUri" type="hidden"  id="coverImg" class="form-control">
                                <div class="img-box" id="coverImageBox">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-xs-12" >
                            <div class="form-group" id ='rotationImageShow'>
                                <input  name="rotationImageUri" type="hidden"  id="rotationImg" class="form-control">
                                    <div class="img-box" id="rotationImageBox">
                                    </div>
                            </div>
                        </div>
                     </div>
                  </div>

                <div class="tab-pane" id="tab_1-3">
                    <div class="row" >
                        <div class="col-md-12 col-xs-12">
                          <div class="form-group">
                             <label for="leftNumber">商品详情页
                             </label>
                              <div>
                                  <textarea name="goodsDetial" class="editor"><#if goodsVO??>${goodsVO.goodsDetial!""}</#if></textarea>
                              </div>
                         </div>
                        </div>
                    </div>
                </div>

                <div class="row">
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
            </div>



            <!-- =================================下面是弹框======================================= -->
            <div class="modal modal-primary fade" id="modal-primary">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">正在添加信息</h4>
                        </div>
                        <div class="modal-body">
                            <p id="primaryTitle">正在执行添加操作，您可以点击确认继续，点击关闭则退出。</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" id="primaryCloseBtn" class="btn btn-outline pull-left"
                                    data-dismiss="modal">关闭
                            </button>
                            <button type="button" id="modalAddBtn" class="btn btn-outline">确认添加</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
            <!-- /.modal -->
            <div class="modal modal-danger fade" id="modal-danger">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">正在删除信息</h4>
                        </div>
                        <div class="modal-body">
                            <p id="dangerTitle">是否将此职位信息从数据中移除，点击确认继续，点击关闭取消操作。</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline pull-left" id="modalCloseBtn"
                                    data-dismiss="modal">关闭
                            </button>
                            <button type="button" id="modalDelBtn" class="btn btn-outline">确定，我要删除</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
        </form>
    </div>
</section>

<script>
var categoryName;
  $(function () {
      //初始化编辑器
      var id =$('#id')
      var editor = KindEditor.create('.editor', {
          width: '98%',
          height: '350px',
          resizeType: 1,
          uploadJson: '/rest/goods/update?EditorFile&goodId=' + id,
          fileManagerJson: '//rest/goods/update?=EditorFile&goodId=' + id,
          allowFileManager: true
      });
      categoryName ='${(goodsVO.categoryName)}';
      findGoodsCategorySelection();
      var coverImageUri= '${(goodsVO.coverImageUri)}';
      if(coverImageUri!=null){
          $('#coverImg').val(coverImageUri);
          $('#coverImageBox').html('<img  src="' + coverImageUri + '"' + ' class="img-rounded" style="height: 100px;width: 100px;" >');
      }

      var rotationImageUris= '${(goodsVO.rotationImageUri)}';
      if(rotationImageUris!=null){
          var arr1 = new Array();
          $('#rotationImg').val(rotationImageUris);
          arr1 =    rotationImageUris.split(',');
        for(var a =0;a<arr1.length;a++){
            var rotationImageUri = arr1[a];
        $("#rotationImageBox").append('<img  src="' + rotationImageUri + '"' + ' class="img-rounded" style="height: 100px;width: 100px;margin-left: 5px;margin-top: 5px" onclick="delImg(this)"/>');
            }
          }

      initFileInput('coverImagefile','coverImg',1);
       initFileInput('rotationImagefile','rotationImg',2);
  });

$(function () {

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
        format: 'yyyy-MM-dd',
        language: 'zh-CN',
        autoclose: true
    });


    $('.switch').bootstrapSwitch();

    $('#goodsFrom').bootstrapValidator({
        framework: 'bootstrap',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        verbose: false,
        fields: {
            skuName: {
                message: '电商名称校验失败',
                validators: {
                    notEmpty: {
                        message: '电商名称不能为空'
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

        if (null === $global.timer) {
            $global.timer = setTimeout($loading.show, 2000);

            var url = '/rest/goods/update';

           if (null !== data.id && 0 != data.id) {
               data._method = 'PUT';
            }
            $.ajax({
                url: url,
                method: 'POST',
                data: data,
                error: function () {
                    clearTimeout($global.timer);
                    $loading.close();
                    $global.timer = null;
                    $notify.danger('网络异常，请稍后重试或联系管理员');
                    $('#cityDeliveryTime_edit').bootstrapValidator('disableSubmitButtons', false);
                },
                success: function (result) {
                    if (0 === result.code) {
                        window.location.href = document.referrer;
                    } else {
                        clearTimeout($global.timer);
                        $loading.close();
                        $global.timer = null;
                        $notify.danger('时间段与已有时间段冲突');
                        $('#cityDeliveryTime_edit').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            });
        }
    });
});



  function findGoodsCategorySelection(){
      var goodsCategory = "";
      $.ajax({
          url: '/rest/goodsCategorys/findEditGoodsCategory',
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
                  goodsCategory += "<option value=" + item.categoryName + ">" +item.categoryName + "</option>";
              })
              $("#goodsCategoryCode").append(goodsCategory);
            $("#goodsCategoryCode").val(categoryName);
          }
      });
  }

function initFileInput(ctrlName,urlId,type) {
    var control = $('#' + ctrlName);
    control.fileinput({
        language: 'zh', //设置语言
        uploadUrl:"/rest/goods/updateImg", //上传的地址
        allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
        //uploadExtraData:{"id": 1, "fileName":'123.mp3'},
        uploadAsync: true, //默认异步上传
        showUpload: false, //是否显示上传按钮
        showRemove: true, //显示移除按钮
        showPreview: false, //是否显示预览
        showCaption: true,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        dropZoneEnabled: false,//是否显示拖拽区域
        //minImageWidth: 50, //图片的最小宽度
        //minImageHeight: 50,//图片的最小高度
        //maxImageWidth: 1000,//图片的最大宽度
        //maxImageHeight: 1000,//图片的最大高度
        //maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
        maxFileCount:1,//表示允许同时上传的最大文件个数
        enctype: 'multipart/form-data',
        validateInitialCount: true,
        previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！"
    }).on('filebatchselected', function (event, data, id, index) {
        $(this).fileinput("upload");
    }).on("fileuploaded", function(event, data) {
       if(data.response.code==0) {
           var url = $('#' + urlId);
            if(1==type){
                $('#coverImageShow').html('<img  src="' + data.response.content + '"' + ' class="img-rounded" style="height: 100px;width: 100px;" >')
                url.val(data.response.content);
            }else if(2==type){
                if(null==url.val()||''==url.val()){
                    url.val(data.response.content)
                }else{
                    url.val(url.val()+','+data.response.content);
                }
                $("#rotationImageBox").append('<img  src="' + data.response.content + '"' + ' class="img-rounded" style="height: 100px;width: 100px;margin-left: 5px;margin-top: 5px" onclick="delImg(this)"/>');
            }
        }
    });
}

function  delImg(file) {
    file.remove();
}





</script>

</body>