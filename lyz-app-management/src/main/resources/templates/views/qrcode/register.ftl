<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
        html, body {
            width: 100%;
            height: 100%;
        }
    </style>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <meta name="copyright" content=""/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta charset="utf-8">
    <title>扫码注册</title>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/datatables/1.10.15/css/dataTables.bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/skins/_all-skins.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/animate.css/3.5.2/animate.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet">
    

    <script src="https://cdn.bootcss.com/jquery/2.2.3/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://cdn.bootcss.com/jQuery-slimScroll/1.3.8/jquery.slimscroll.min.js"></script>
    <script src="https://cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>


    <script type="text/javascript">
        document.getElementsByTagName('html')[0].style.fontSize = window.screen.width / 10 + 'px';
    </script>

</head>
<style type="text/css">
    .reg_logo {
        width: 100%;
        text-align: center;
        margin-top: 0.6rem;
        margin-bottom: 0.6rem;
    }

    .reg_logo img {
        width: 26%;

    }

    .wechat_photo {
        width: 100%;
        text-align: center;
        margin-top: 0.6rem;
        margin-bottom: 0.3rem;
    }

    .wechat_photo img {
        border-radius:50%; overflow:hidden;
        width: 150px;
        height: 150px;
    }
</style>
<body ng-app="qrcode">
<header class="top_head">
    <div style="text-align: center;">扫码注册</div>
</header>
<section>
    <div class="reg_logo"><img src="/images/big_logo.png"/></div>
    <div class="wechat_photo"><img src="<#if employee?? && employee.picUrl??>${employee.picUrl!""}</#if>"></div>
    <div class="row">
        <!-- left column -->
        <div class="col-md-6">
            <!-- general form elements -->
            <div class="box box-solid">

                <!-- /.box-header -->
                <!-- form start -->
                <form role="form" id="qrcodeForm">
                    <div class="box-body margin">
                        <input type="hidden" name="empId" value="<#if empId??>${empId!''}</#if>">
                        <div class="form-group" >
                            <label for="name">姓名</label>
                            <input type="text" class="form-control" id="name" name="name" placeholder="请输入姓名 2~8位字符">
                        </div>
                        <div class="form-group" >
                            <label for="phone">手机号</label>
                            <input type="number" class="form-control" id="phone" name="phone" placeholder="请输入11位手机号">
                        </div>

                        <div class="form-group">
                            <label for="code">验证码</label>
                            <div class="input-group">
                                <input id="code" name="code" type="text" class="form-control">
                                <span class="input-group-btn">
                              <button id="sentBtn" type="button" class="btn btn-info btn-flat" onclick="sendsms()">获取验证码</button>
                            </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="sellerName">推荐人</label>
                            <input type="text" class="form-control" id="sellerName" name="sellerName" readonly="readonly" value="<#if employee??> ${employee.name!''}</#if>">
                        </div>
                        <div class="form-group">
                            <label for="store">推荐门店</label>
                            <input type="text" class="form-control" id="store" name="store" readonly="readonly" value="<#if storeName??> ${storeName!''}</#if>">
                        </div>
                        <div class="form-group" >
                            <label for="workNumber">推荐码</label>
                            <input type="text" class="form-control" id="workNumber" name="workNumber" placeholder="输入推荐码成为会员">
                        </div>
                    </div>
                    <!-- /.box-body -->

                    <div class="box-footer margin">
                        <button type="submit" class="btn btn-block btn-success btn-flat">注册</button>
                    </div>
                </form>
            </div>
            <!-- /.box -->

        </div>
        <!-- /.row -->

</section>


<script type="text/javascript">

    $(function () {

        formValidate();
    })

    /**
     * 表单验证
     */
    function formValidate() {
        /**
         * 表单验证器初始化
         */
        $('#qrcodeForm').bootstrapValidator({
            framework: 'bootstrap',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            verbose: false,
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: '姓名不能为空'
                        },
                        stringLength: {
                            min: 2,
                            max: 8,
                            message: '姓名长度在2~8位'
                        }
                    }
                },

                phone:{
                    validators:{
                        notEmpty: {
                            message: '手机号不能为空'
                        },
                        stringLength: {
                            min: 0,
                            max: 11,
                            message: '请输入11位手机号'
                        }
                    }
                },
                code:{
                    validators:{
                        notEmpty: {
                            message: '验证码不能为空'
                        },
                        stringLength:{
                            min:0,
                            max:6,
                            message: "金额不准确"
                        }
                    }
                },
                sellerName:{
                    validators:{
                        notEmpty: {
                            message: '推荐人为空'
                        }
                    }
                },
                store:{
                    validators:{
                        notEmpty: {
                            message: '推荐门店为空'
                        }
                    }
                },
                workNumber:{
                    validators:{
                        stringLength:{
                            min:0,
                            max:15,
                            message: "推荐码不正确"
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {

            e.preventDefault();
            var $form = $(e.target);
            var origin = $form.serializeArray();
            var data = {};
            var url = '/rest/qrcode/save';

            $.each(origin, function () {
                data[this.name] = this.value;
            });

            $.ajax({
                type: "POST",
                url: url,
                data: data,
                dataType: "json",
                success: function(result){
                    if (0 == result.code){
                        window.location.href = "/qrcode/register/success";
                    }else{
                        alert(result.message);
                        $('#qrcodeForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }
            })
        });

    }
    
    var countdown=60;
    function sendsms(){
        var phone = $("#phone").val();

        if (phone == "" || phone == undefined){
            alert("请填写手机号");
            return;
        }

        var rex = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
        var re = new RegExp(rex);
        if (!re.test(phone)){
            alert("请填写正确的手机号");
            return;
        }

        var url = "/rest/qrcode/send/code";
        var data = {};
        data["mobile"] = phone;

        var obj = $("#sentBtn");
        settime(obj);

        $.ajax({
            type: "POST",
            url: url,
            data: data,
            dataType: "json",
            success: function(result){
                if (0 == result.code){
                    alert("发送成功！");
                }else{
                    alert(result.message);
                }
            }
        })
    }
    function settime(obj) { //发送验证码倒计时
        if (countdown == 0) {
            obj.attr('disabled',false);
            //obj.removeattr("disabled");
            obj.html("获取验证码");
            countdown = 60;
            return;
        } else {
            obj.attr('disabled',true);
            obj.html("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function() {
                    settime(obj) }
                ,1000)
    }
</script>
</body>
</html>

