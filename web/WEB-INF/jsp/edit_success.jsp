<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <!--<link rel="stylesheet" type="text/css" href="../css/publisharticle.css" />-->
    <script type="text/javascript" src="../../js/jquery-2.1.4.js"></script>
    <title>导音教育</title>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        html,body{
            height: 100%;
        }
        .container {
            height: 100%;
            margin: 0;
            padding: 0;
        }
        .topBar {
            box-sizing: border-box;
            width: 100%;
            padding: 5px 20px;
            /*padding: 5px 20px;*/
            /*background: tan;*/
            overflow: hidden;
            border-bottom: 1px solid #e4e4e4;
        }
        .logo {
            display: inline-block;
            /*float: left;*/
            height: 50px;
        }
        .logo img{
            display: inline-block;
        }
        .loginBar {
            display: inline-block;
            width: 160px;
            float: right;
            overflow: hidden;
            padding-right: 50px;
        }
        .photo {
            display: inline-block;
            width:50px;
            height: 50px;
            float: left;
            overflow: hidden;
            border-radius: 50%;
        }
        .photo img {
            width: 50px;
            height: 50px;
            /*cursor: pointer;*/
        }
        .userName {
            display: inline-block;
            float: left;
            margin-left: 14px;
            line-height: 50px;
        }
        .exit {
            display: inline-block;
            cursor: pointer;
            margin-left: 14px;
            line-height: 50px;
        }
        /*主体内容区域*/
        .inner {
            height: 89%;
            width: 100%;
            margin: 0 auto;
            padding-bottom: 30px;
            background-color: #f7f9f8;
        }
        .topLine {
            /*position: relative;*/
            width: 660px;
            padding-top: 30px;
            padding-bottom: 10px;
            font-size: 16px;
            border-bottom: 1px solid #e4e4e4;
            position: absolute;
            left: 50%;
            margin-left:-330px;
        }
        .hsc{
            position: absolute;
            left: 50%;
            top: 25%;
            font-size: 16px;
            text-align: center;
        }
        .mxz{
            font-size:12px;
            color: #268BEF;
            margin-top: 14px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="container">
    <!--顶部导航栏-->
    <div class="topBar">
        <div class="logo">
            <img src="../static/dy-logo.png" alt="">
        </div>
        <!--头像区域-->
        <div class="loginBar">
            <!--头像-->
            <div class="photo">
                <img src= '${requestScope.user.head }' alt="">
            </div>
            <!--用户名-->
            <div class="userName">
                ${requestScope.user.name }
            </div>
            <!--推出-->
            <div class="exit" >退出</div>
        </div>

    </div>
    <!--主体内容区域-->
    <div class="inner">
        <div class="topLine">
            <span>发表文章</span>
        </div>
        <div class="hsc">
            <p>文章发表成功</p>
            <p class="mxz">继续发表文章</p>
        </div>
    </div>

</div>
<script>
    $(".mxz").on('click',function(){
        location.href="http://139.199.123.168/article/edit"
    })
    var d = []
    console.log(d[0])
</script>
</body>
</html>

