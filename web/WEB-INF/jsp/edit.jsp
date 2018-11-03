<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex, nofollow">
    <title>导音文章编辑</title>
    <script src="/statics/ckeditor/ckeditor.js"></script>

    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <style>
        .title{
            border: 0;
            border-bottom: none;
            font-weight: 400;
            font-size: 20px;
            line-height: 58px;
            height: 58px;
            padding-left: 20px;
            padding-right: 55px;
            color: #595959;
            outline: 0;
            position: relative;
            display: inline-block;
            padding: 6px 7px;
            width: 100%;
            text-align: left;
            cursor: text;
            background-color: #fff;
            background-image: none;
            border-radius: 2px;
            box-sizing: border-box;
            -webkit-writing-mode: horizontal-tb !important;
        }

        .src-image-select {
            max-width: 240px;
            max-height: 135px;

            min-width: 240px;
            min-height: 135px;
        }
        .src-image-size {
            width: 240px;
            height: 135px;
            border: solid 1px;
            border-color: grey;
            float: left;
            margin: 5px 5px;
            text-align: center;
            text-align: center;
        }
        .header-image {
            max-width: 65px;
            max-height:65px;

            min-width: 65px;
            min-height:65px;
        }
    </style>


</head>

<body>


<i type="add">+</i>
<div style="font-size: 20px; margin:20px 20px;border-bottom: 2px solid  ">发表文章</div>
<form method="post" action="/article/add">
    <div>
        <input class = "title" placeholder="请输入文章标题（5-30个汉字）" type="text" name = "head" value>
    </div>

    <br>
    <div style="margin: 0 auto; width: 75%">
        <textarea  style="text-align:center" id="editor1" name="content"  rows="30" cols="50" ></textarea>
    </div>

    <br>
    <br>
    <span style= "font-size: 20px; margin: 20px 20px"> 标题图片</span>
    <br>

    <div>
        <span>封面</span>
            <input id= "pic_s1" name="pic_type" type="radio"  value="1" checked> 单图
            <input id= "pic_s2" name="pic_type" type="radio"  value="2"> 多图
            <input id= "pic_s3" name="pic_type" type="radio"  value="3"> 自动
    </div>

    <div>
        <div style = "margin: 0 auto">
            <div class="src-image-size" id="pic_div1">
                <a data-toggle="modal" data-target="#myModal" onclick= select_pic1()  href="javacript:void(0)"><div><img class="src-image-select"  id="pic_1" src="" alt="选择标题图片1"></div></a>
            </div>
            <div class="src-image-size" id="pic_div2" style="display:none ">
                <a data-toggle="modal" data-target="#myModal" onclick= select_pic2()  href="javacript:void(0)"><div><img class="src-image-select" id="pic_2" src="" alt="选择标题图片2"></div></a>
            </div>
            <div class="src-image-size" id="pic_div3" style="display:none ">
                <a data-toggle="modal" data-target="#myModal" onclick= select_pic3() href="javacript:void(0)"><div><img class="src-image-select" id ="pic_3" src="" alt="选择标题图片3"></div></a>
            </div>
        </div>
    </div>

    <br style="clear: both">

    <div style="display: inline;margin-right: 20px">
        学科：
        <select>
            <option value ="volvo">Volvo</option>
            <option value ="saab">Saab</option>
            <option value="opel">Opel</option>
            <option value="audi">Audi</option>
        </select>
    </div>

    <div style="display: inline;margin-right: 20px">
        阶段：
        <select>
            <option value ="volvo">Volvo</option>
            <option value ="saab">Saab</option>
            <option value="opel">Opel</option>
            <option value="audi">Audi</option>
        </select>
    </div>

    <div style="display: inline;margin-right: 20px">
        年级：
        <select>
            <option value ="volvo">Volvo</option>
            <option value ="saab">Saab</option>
            <option value="opel">Opel</option>
            <option value="audi">Audi</option>
        </select>
    </div>

    <input id = "h_pic_1" type="text" name="head_pic1" style="display: none" value>
    <input id = "h_pic_2" type="text" name="head_pic2" style="display: none" value>
    <input id = "h_pic_3" type="text" name="head_pic3" style="display: none" value>


    <br>
    <br>
    <div>
            <%--<div style="clear: both;margin-bottom: 20px;display: inline">--%>
                <%--<button class="btn btn-success">预览</button>--%>
            <%--</div>--%>

            <div style="clear: both; text-align:center;margin-bottom: 20px ">
                <span  data-toggle="modal" data-target="#myModal_2" id="pre_view" class="btn btn-success">预览</span>
                <input type="submit" class="btn btn-success">
            </div>
    </div>


</form>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    选择标题图片
                </h4>
            </div>
            <div class="modal-body" id="test">
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭
            </button>
            <button type="button" class="btn btn-primary">
                提交更改
            </button>
        </div>
    </div><!-- /.modal-content -->
</div><!-- /.modal -->
</div>


<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal_2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 300px;margin: 0 auto">
            <div class="modal-header">
                <span style="margin: 0 100px;font-size: 20px">预览</span>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
            </div>
            <div style="width: 300px;height:450px">
                <div id="article_header">1111</div>
                <div style="float: left"><img class = "header-image" src="" alt="test" style="border: solid silver"></div>
                <div style="float: left">1111</div>
                <button style="float: right;margin-right: 20px">关注</button>

                <div id = "artile_content" style="clear: both">11111</div>
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<script>

    $(document).ready(function(){
        $("#pic_s1").click(function(){
            $("#pic_div1").css("display","");
            $("#pic_div2").css("display","none");
            $("#pic_div3").css("display","none");
            $("#pic_1").attr("src","");
            $("#pic_2").attr("src","");
            $("#pic_3").attr("src","");
        });
        $("#pic_s2").click(function(){
            $("#pic_div1").css("display","");
            $("#pic_div2").css("display","");
            $("#pic_div3").css("display","");
            $("#pic_1").attr("src","");
            $("#pic_2").attr("src","");
            $("#pic_3").attr("src","");
        });
        $("#pic_s3").click(function(){
            $("#pic_div1").css("display","none");
            $("#pic_div2").css("display","none");
            $("#pic_div3").css("display","none");
            $("#pic_1").attr("src","");
            $("#pic_2").attr("src","");
            $("#pic_3").attr("src","");
        });
    })

    function select_pic1() {
        $("#test").empty();
        var str  = '';
        var arr = $("iframe").eq(0).contents().find("figure img");
        arr.each(function (i,o) {
            var dom = $(o);
            dom.attr('src');
            str += "<img src=" +  dom.attr('src') + " width=\"240\" height=\"135\" onclick=set_pic(\"pic_1\",this) \">"
        });

        $("#test").append("<div>" + str + "</div>")
    }

    function select_pic2() {
        $("#test").empty();
        var str  = '';
        var arr = $("iframe").eq(0).contents().find("figure img");
        arr.each(function (i,o) {
            var dom = $(o);
            dom.attr('src');
            str += "<img src=" +  dom.attr('src') + " width=\"240\" height=\"135\" onclick=set_pic(\"pic_2\",this) \">"
        });

        $("#test").append("<div>" + str + "</div>")
    }

    function select_pic3() {
        $("#test").empty();
        var str  = '';
        var arr = $("iframe").eq(0).contents().find("figure img");
        arr.each(function (i,o) {
            var dom = $(o);
            dom.attr('src');
            str += "<img src=" +  dom.attr('src') + " width=\"240\" height=\"135\" onclick=set_pic(\"pic_3\",this) \">"
        });

        $("#test").append("<div>" + str + "</div>")
    }
    function set_pic(pic,object)
    {
        var img = $(object).attr("src");
        $("#"+ pic ).attr("src",img);
        $("#h_" + pic).val(img);
        $("#myModal").modal('hide');
    }
</script>
<script>
    CKEDITOR.replace( 'editor1', {
        extraPlugins: 'easyimage',
        removePlugins: 'image',
        removeDialogTabs: 'link:advanced',
        toolbar: [
            { name: 'document', items: [ 'Undo', 'Redo' ] },
            { name: 'styles', items: [ 'Format' ] },
            { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Strike', '-', 'RemoveFormat' ] },
            { name: 'paragraph', items: [ 'NumberedList', 'BulletedList' ] },
            { name: 'insert', items: [ 'EasyImageUpload' ] }
        ],
        imageStyleFormat:{
            title: 'Full size image',
            className: 'image-full-size'
        },
        height: 250,
    //        uiColor:'#FFF',

        cloudServices_uploadUrl: '/edit/upload',
        // Note: this is a token endpoint to be used for CKEditor 4 samples only. Images uploaded using this token may be deleted automatically at any moment.
        // To create your own token URL please visit https://ckeditor.com/ckeditor-cloud-services/.
        cloudServices_tokenUrl: 'https://33333.cke-cs.com/token/dev/ijrDsqFix838Gh3wGO3F77FSW94BwcLXprJ4APSp3XQ26xsUHTi0jcb1hoBt'
    } );
</script>
</body>

</html>