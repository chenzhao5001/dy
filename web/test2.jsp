<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex, nofollow">
    <title>Easy Image Plugin</title>
    <%--<script src="https://cdn.ckeditor.com/4.10.1/standard-all/ckeditor.js"></script>--%>
    <script src="statics/ckeditor/ckeditor.js"></script>
</head>

<body>


<form method="post">
    <p>
        <textarea cols="80" id="editor1" name="editor1" rows="10" >&lt;p&gt;This is some &lt;strong&gt;sample text&lt;/strong&gt;. You are using &lt;a href="https://ckeditor.com/"&gt;CKEditor&lt;/a&gt;.&lt;/p&gt;
	    </textarea>
    </p>
</form>


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
        height: 630,
        cloudServices_uploadUrl: '/edit/upload',
        // Note: this is a token endpoint to be used for CKEditor 4 samples only. Images uploaded using this token may be deleted automatically at any moment.
        // To create your own token URL please visit https://ckeditor.com/ckeditor-cloud-services/.
        cloudServices_tokenUrl: 'https://33333.cke-cs.com/token/dev/ijrDsqFix838Gh3wGO3F77FSW94BwcLXprJ4APSp3XQ26xsUHTi0jcb1hoBt'
    } );
</script>
</body>

</html>