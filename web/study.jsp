<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html style="font-size: 54.8px;">
<head>
    <title>导音内容编辑器</title>
    <meta name="viewport" content="initial-scale=1,maximum-scale=1, minimum-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="/statics/css/base.css">
    <style>
        html {
            max-width: 750px;
            margin: 0 auto
        }

        body {
            min-height: 100%;
            /*background: #161823*/
        }

        .pagelet-user-info {
            overflow: hidden;
            position: relative
        }

        .pagelet-user-info .bg {
            position: absolute;
            left: 0;
            top: 0;
            width: 110%;
            height: 110%;
            margin-left: -5%;
            margin-top: -5%;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: cover;
            background-color: #18181e;
            -webkit-filter: blur(25px);
            filter: blur(25px);
            z-index: -2
        }

        .pagelet-user-info .personal-card {
            padding: .5rem .3rem .55rem;
            font-size: .24rem;
            color: #000
        }

        .avatar {
            width: 1.8rem;
            height: 1.8rem;
            border-radius: 50%;
            vertical-align: middle;
            border: solid .04rem rgba(255, 255, 255, .15)
        }

        .focus-btn {
            display: inline-block;
            position: absolute;
            top: .6rem;
            right: .2rem;
            width: 1.68rem;
            height: .72rem;
            line-height: .72rem;
            border-radius: .04rem;
            background-color: #fe2c55;
            text-align: center;
            font-size: .3rem;
            color: rgba(255, 255, 255, .9)
        }

        .nickname {
            margin-top: .36rem;
            font-size: .48rem;
            line-height: .67rem
        }

        .shortid {
            line-height: .23rem;
        }

        .info1 {
            padding-bottom: .2rem;
            border-bottom: solid .01rem rgba(255, 255, 255, .15)
        }

        .info2 {
            margin-top: .2rem
        }

        .signature {
            margin-top: .06rem;
            line-height: 1.5;
            color: rgba(255, 255, 255, .6);
            font-size: .24rem;
            white-space: pre-wrap
        }

        .personal-card {
            padding: .5rem .3rem .55rem;
            font-size: .24rem;
            color: #fff
        }

        .extra-info {
            margin-top: .14rem
        }

        .location {
            padding: .03rem .16rem;
            font-size: .22rem;
            line-height: .3rem;
            opacity: .9;
            height: .36rem;
            border-radius: .18rem;
            background-color: #6759e9
        }

        .constellation {
            margin-left: .05rem;
            padding: .03rem .16rem;
            font-size: .22rem;
            line-height: .3rem;
            opacity: .9;
            height: .36rem;
            border-radius: .18rem;
            background-color: #e82c81
        }

        .follow-info {
            margin-top: .3rem
        }

        .follow-num {
            font-size: .42rem !important;
            letter-spacing: -.11rem
        }

        .iconfont {
            font-family: iconfont !important;
            font-size: .24rem;
            font-style: normal;
            letter-spacing: -.045rem;
            margin-left: -.085rem;
        }

        video-tab {
            font-size: 0;
            min-height: .8rem
        }

        .tab-wrap {
            -webkit-transform: translate3d(0px, 0, 0);
            transform: translate3d(0px, 0, 0)
        }


    </style>
</head>
<body>
<div class="pagelet-user-info">
    <div class="bg" style="background-image: url(https://p1.pstatp.com/aweme/720x720/bddb0013e74a104fadfb.jpeg)">
    </div>

    <div class="personal-card">
        <div class="info1">
              <span>
            <img class="avatar" src="https://p1.pstatp.com/aweme/720x720/bddb0013e74a104fadfb.jpeg">
        </span>
            <span class="focus-btn">
            hello
        </span>
            <p class="nickname">
                导音测试账号
            </p>
            <p class="shortid">
                导音id:123456
            </p>
        </div>


        <div class="info2">
            <p class="signature">亲子教育无止境，让我们一路同行！</p>
            <p class="extra-info"><span class="location">福建·福州</span> <span class="constellation">水瓶座</span></p>
            <p class="follow-info">
                <span class="focus block">
                    <span class="num">
                        <i class="icon iconfont follow-num"></i>
                        <i class="icon iconfont follow-num"></i>
                    </span><span class="text">关注</span>
                </span><span class="follower block"><span class="num">
                <i class="icon iconfont follow-num"></i>
                <i class="icon iconfont follow-num"></i>.<i class="icon iconfont follow-num"></i>w </span>
                <span class="text">粉丝</span>
            </span><span class="liked-num block"><span class="num">
                <i class="icon iconfont follow-num">  </i><i class="icon iconfont follow-num">  </i>.<i
                    class="icon iconfont follow-num"> 
            </i>w </span><span class="text">赞</span></span></p>
        </div>
    </div>


    <div class="video-tab" height="79.96875px">
        <div class="tab-wrap">
            <div class="user-tab active tab get-list" data-type="post">作品<span class="num">    <i
                    class="icon iconfont tab-num">  </i><i class="icon iconfont tab-num">  </i> </span></div>
            <div class="like-tab tab get-list" data-type="favorite">喜欢<span class="num">    <i
                    class="icon iconfont tab-num">  </i><i class="icon iconfont tab-num">  </i><i
                    class="icon iconfont tab-num">  </i> </span></div>
        </div>
    </div>

</div>

</body>
</html>