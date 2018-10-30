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
    <a  data-toggle="modal" data-target="#myModal" onclick= select_pic1() style="float: left;margin-right: 20px" href="javacript:void(0)"><div><img id="pic_1" src="" alt="选择标题图片1"></div></a>
    <a  data-toggle="modal" data-target="#myModal" onclick= select_pic2() style="float: left;margin-right: 20px" href="javacript:void(0)"><div><img id="pic_2" src="" alt="选择标题图片2"></div></a>
    <a  data-toggle="modal" data-target="#myModal" onclick= select_pic3() href="javacript:void(0)"><div><img id ="pic_3" src="" alt="选择标题图片3"></div></a>


    <input id = "h_pic_1" type="text" name="head_pic1" style="display: none" value>
    <input id = "h_pic_2" type="text" name="head_pic2" style="display: none" value>
    <input id = "h_pic_3" type="text" name="head_pic3" style="display: none" value>
    <div style="text-align: center">
        <input type="submit" class="btn btn-success">
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
            <div class="modal-body" id="test"
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

<script>

    function select_pic1() {


        $("#test").empty();
        var str  = '';
        var arr = $("iframe").eq(0).contents().find("figure img");
        arr.each(function (i,o) {
            var dom = $(o);
            dom.attr('src');
            str += "<img src=" +  dom.attr('src') + " img_width=\"240\" img_height=\"135\" onclick=set_pic(\"pic_1\",this) \">"
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
            str += "<img src=" +  dom.attr('src') + " img_width=\"240\" img_height=\"135\" onclick=set_pic(\"pic_2\",this) \">"
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
            str += "<img src=" +  dom.attr('src') + " img_width=\"240\" img_height=\"135\" onclick=set_pic(\"pic_3\",this) \">"
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