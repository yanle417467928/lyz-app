<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>乐易装后台管理中心</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/datatables/1.10.15/css/dataTables.bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/admin-lte/2.3.11/css/skins/_all-skins.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/animate.css/3.5.2/animate.min.css" rel="stylesheet">

    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script src="https://cdn.bootcss.com/jquery/2.2.3/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://cdn.bootcss.com/jQuery-slimScroll/1.3.8/jquery.slimscroll.min.js"></script>
    <script src="https://cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
    <script src="https://cdn.bootcss.com/admin-lte/2.3.11/js/app.min.js"></script>

    <script src="https://cdn.bootcss.com/mouse0270-bootstrap-notify/3.1.7/bootstrap-notify.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-waitingfor/1.2.4/bootstrap-waitingfor.min.js"></script>

    <script src="/javascript/devkit.js"></script>

    <sitemesh:write property="head"/>

</head>
<body class="hold-transition skin-blue sidebar-mini">

<#include "./modal.ftl">
<#include "./loading.ftl">

<div class="wrapper">

<#include "./header.ftl">

<#include "./menu.ftl">

    <div class="content-wrapper">
        <sitemesh:write property="body"/>
    </div>

<#include "./footer.ftl">

</div>
</body>
</html>
