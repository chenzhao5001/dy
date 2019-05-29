<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <title>导音视频分享</title>
    <link rel="stylesheet" href="../css/h5/h5.css">
    <script src="../js/jquery-2.1.4.js"></script>

    <style>

        .top {
            position: absolute;
            top: 0;
            left: 0;
            background-color: rgba(127, 93, 0, .8);
            height: .6rem;
            padding: .07rem .13rem .08rem .12rem;

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
            margin-left: 0.08rem;
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

        .vodeo_info {
            position: absolute;
            bottom: 0.25rem;
            right: 0;
            width: .45rem;
        }

        .vodeo_info .header {
            width: .36rem;
            height: .36rem;
            border-radius: .18rem;
            margin-left: .05rem;
        }

        .vodeo_info .item {
            margin: .2rem .1rem .15rem .1rem;
            width: .25rem;
            /*height:.25rem;*/
        }

        .vodeo_info .item img {
            width: .25rem;
            height: .25rem;
            margin-bottom: .08rem;
        }

        .vodeo_info .item p {
            text-align: center;
            color: white;
        }

        .video_content {
            position: absolute;
            left: 0;
            bottom: 0;
            margin-left: .09rem;
        }

        .video_content a {
            display: block;
            width: .56rem;
            height: .23rem;
            line-height: .23rem;
            background-color: #09BB07;
            margin-bottom: .20rem;
            border-radius: .05rem;
            font-size: .14rem;
            color: white;
            text-align: center;
        }

        .video_content ul {
            width: 3rem;
            overflow: hidden;
            margin-bottom: .03rem;
        }

        .video_content li.subject {
            font-size: .12rem;
            color: white;
        }

        .video_content li {
            font-size: .14rem;
            color: white;
            margin-bottom: .08rem;
        }

        .bk_other {
            position: fixed;
            right: 0;
            bottom: 0;
            width: 100%;
            height: 100%;
            background-size: cover;
        }

    </style>
</head>
<body>


<video
        class="bk_other"
        id="videoALL"
        src="${video_show_url}"
        poster="${pic_up_path}"
        preload="auto"
        webkit-playsinline="true"
        playsinline="true"
        x-webkit-airplay="allow"
        x5-video-player-type="h5"
        x5-video-player-fullscreen="true"
        x5-video-orientation="portraint"
        style="object-fit:fill">
</video>


<%--<video autoplayloopposter="${pic_up_path}" class="bk_other video">--%>
<%--<source src="${video_show_url}" type="video/mp4">--%>
<%--</video>--%>

<div class="top clear">
    <img class="header fl" src="${user_head}" alt="">
    <div class="content f1">
        <p class="name">${user_name}</p>
        <p class="sign_name">${user_sing}</p>
    </div>
    <a href="${store_url}" class="get_app fl">打开看看</a>
</div>
<div class="vodeo_info">

    <img src="${user_head}" class="header">
    <div class="item">
        <img src="../icon/h5/video_1.png" alt="">
        <p>${praise_count}</p>
    </div>
    <div class="item">
        <img src="../icon/h5/video_2.png" alt="">
        <p>${chat_count}</p>
    </div>
    <div class="item">
        <img src="../icon/h5/video_3.png" alt="">
        <p>${collection_count}</p>
    </div>
    <div class="item">
        <img src="../icon/h5/video_4.png" alt="">
        <p>${shared_count}</p>
    </div>
</div>

<div class="video_content">
    <a href="#">+关注</a>
    <ul>
        <li class="subject">${video_subject}</li>
        <li class="user_name">${video_user_name}</li>
        <li class="title">${video_title}</li>
    </ul>
</div>

<script>
    var play_flag = false;
    $("video").on("click", function () {
        if(play_flag == false) {
            ($("video")[0]).play();
            play_flag = true;
        } else {
            ($("video")[0]).pause();
            play_flag = false;
        }

    })

</script>
</body>
</html>
