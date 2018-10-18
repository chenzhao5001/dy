<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
    <title>导音内容编辑器</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%--<script src="https://cdn.ckeditor.com/4.10.1/standard/ckeditor.js"></script>--%>
    <script src="statics/ckeditor/ckeditor.js"></script>
</head>
<body>
<form method="post">
    <p>
        导音内容编辑器:<br>
        <textarea name="editor1" id="editor1">&lt;p&gt;Initial editor content.&lt;/p&gt;</textarea>
        <script>
            CKEDITOR.replace( 'editor1', {
                filebrowserBrowseUrl: '/browser/browse.php',
                filebrowserUploadUrl: '/edit/upload'
            });

        </script>
        <%--<script type="text/javascript"> CKEDITOR.replace('editor1');</script>--%>
    </p>
    <p>
        <input type="submit">
    </p>
</form>
</body>
</html>