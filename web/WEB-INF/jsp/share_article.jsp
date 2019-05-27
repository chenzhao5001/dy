<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="../css/h5/h5.css">
    <title>导音文章分享</title>

    <style>
        /*body {*/
            /*overflow-y:hidden*/
        /*}*/
        .top {
            background-color: rgba(127, 93, 0, .8);
            height: .6rem;
            padding: .07rem .1rem .08rem .12rem;
        }

        .top .header {
            width: .45rem;
            height: .45rem;
            border-radius: .05rem;
        }

        .top .content {
            height: .45rem;
            float: left;
            margin-left: 0.05rem;
            width: 2.43rem;
            overflow: hidden;
        }

        .top .name {
            height: 0.22rem;
            line-height: 0.22rem;
            font-size: 0.16rem;
            margin-top: 0.02rem;
            vertical-align: middle;
            color: white;
        }

        .top .sign_name {
            height: 0.17rem;
            line-height: 0.17rem;
            font-size: 0.12rem;
            margin-top: 0.03rem;
            vertical-align: middle;
            color: white;
        }

        .top .get_app {
            margin-top: 0.06rem;
            /*margin-left: 0.08rem;*/
            display: block;
            width: 0.83rem;
            height: 0.32rem;
            line-height: 0.32rem;
            background: #09BB07;
            border-radius: 0.05rem;
            font-size: .14rem;
            color: white;
            text-align: center;
        }

        .wrap {
            margin-left: .1rem;
            margin-right: .1rem;
            margin-bottom: .1rem;
        }

        .article_head {
            margin-top: .1rem;
            margin-bottom: .1rem;
            width: 3.87rem;
            height: .65rem;
            color: #333333;
            font-size: .24rem;

            word-wrap: break-word;
            word-break: break-all;
            overflow: hidden;
        }

        .user_info {
            margin-top: .17rem;
        }
        .user_info img {
            width: .34rem;
            height: .34rem;
            border-radius: .17rem;
        }
        .user_info p {
            margin-left: .09rem;
            margin-top: .09rem;
            font-size: .12rem;
        }
        .user_info a {
            display: block;
            width: .53rem;
            height: .24rem;
            line-height: .24rem;
            background-color: rgb(9,187,7);
            color: white;
            border-radius: .05rem;
            margin-top: .05rem;
            text-align: center;
        }
        .article_content p{
            font-size: .21rem;
            margin: 0;
        }
        .article_content img {
            width: 100%;
        }
    </style>
</head>
<body>

<div class="top clear">
    <img class="header fl" src="${user_head}" alt="">
    <div class="content f1">
        <p class="name">${user_name}</p>
        <p class="sign_name">${user_sing}</p>
    </div>
    <a href="#" class="get_app fr">打开看看</a>
</div>


<div class="wrap">
    <p class="article_head">${article_title}</p>

    <div class="user_info clear">
        <img class="fl" src="${user_head}" alt="">
        <p class="fl">${user_name}</p>
        <a class="fr" href="#">关注</a>
    </div>

    <div class="article_content">${article_content}</div>
</div>
</body>
</html>