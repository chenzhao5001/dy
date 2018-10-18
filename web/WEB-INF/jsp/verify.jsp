<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.guidesound.models.Video" %>
<%@ page import="com.guidesound.util.SignMap" %>
<%@ page import="com.guidesound.util.ToolsFunction" %>
<!doctype html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="/statics/css/style.css"/>
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <title>视频审核</title>
</head>
<body>

<div id="popup" style="position:absolute;z-index:100; display:none; background-color:#fff;">
    <video id ="video_sign" src="" controls="controls">
        您的浏览器不支持 video 标签。
    </video>
    <br>
    <a id="commit" href="JavaScript:void(0)">通过</a>
    <a id="refuse" href="JavaScript:void(0)">拒绝</a>
    <a id="cancel" href="JavaScript:void(0)">取消</a>

</div>
<%
    List<Video> temp = (List<Video>) request.getAttribute("video_list");
%>


<div class="container">
    <div class="course-list">
        <div class="moco-course-list">

            <%
                if (temp != null && temp.size() > 0) {
                    for (Video video : temp) {%>
            <div class="course-card-container">
                <a href="JavaScript:void(0)" class="open_window" video_id="<% out.print(video.getId());%>"
                   video_path="<% out.print(video.getVideo_up_path());%>">
                    <div class="course-card-top">
                        <img class="course-banner lazy"
                             data-original="<% out.print(video.getPic_up_path());%>"
                             src="<% out.print(video.getPic_up_path());%>" style="display: inline;">
                        <div class="course-label">
                            <label>Angular</label>
                            <label>Node.js</label>
                        </div>
                    </div>
                    <div class="course-card-content">
                        <h3><%out.print("标题:" + video.getTitle());%></h3>
                        <h4><% out.print("科目:" + SignMap.getSubjectById(video.getSubject())); %></h4>
                        <h4><%out.print("观看类型:" + SignMap.getSubjectById(video.getWatch_type()));%></h4>
                        <h4><%
                            out.print("上传时间:" + ToolsFunction.timeStamp2Date(String.valueOf(video.getCreate_time()), null));%></h4>
                    </div>
                </a>
            </div>
            <%
                    }
                } else {
                        out.print("<h1>没有需要审核的视频</h1>");
                    }
            %>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {

        $(".open_window").click(function(){
            video_id = $(this).attr("video_id");
            video_path = $(this).attr("video_path");
            $("#video_sign").attr("src",video_path);
            generateFloatLayer();
        });

        $("#commit").click(function() {
            $.ajaxSettings.async = false;
            $.post("/video/set_status", {status: "1", id: video_id}, function (result) {
            });
            $.ajaxSettings.async = true;
            location.reload();
        });
        $("#refuse").click(function(){

            $.ajaxSettings.async = false;
            $.post("/video/set_status",{status:"2",id:video_id},function(result){});
            $.ajaxSettings.async = true;
            location.reload();
        });

        $("#cancel").click(function(){
            alert("cancel");
            closeFloat();
        });
    });

    function generateFloatLayer() {
        floatArea = document.getElementById("popup");
        x = event.clientX + document.body.scrollLeft;
        y = event.clientY + document.body.scrollTop;
        floatArea.style.left ="20%";
        floatArea.style.top ="10px";
        floatArea.style.width = "60%";
        floatArea.style.height = "500px";
        floatArea.style.margin="10px auto";
        floatArea.style.display = "";

        document.getElementById("bg").style.display="";
    }

    function closeFloat() {
        floatArea = document.getElementById("popup");
        /*floatArea.innerHTML = "";*/
        floatArea.style.display = "none";

        document.getElementById("bg").style.display="none";
    }
</script>
</body>
</html>