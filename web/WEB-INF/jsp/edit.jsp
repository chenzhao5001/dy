
    <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
        <!doctype html>
        <html lang="en">
        <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <script src="/statics/ckeditor/ckeditor.js"></script>
            <style>
                .cke_inner {
                     position: relative !important
                     overflow: hidden;
                }
                #cke_1_top {
                    position: fixed;
                    width: 100%;
                    /*margin-top: -30px;*/
                }
                #cke_1_contents{
                    margin-top:30px;
                }


            </style>
        </head>


    <body>
    <div>111</div>
    <div>111</div>
    <div>111</div>
    <div id="editor1"><strong>Sample</strong> Text</div>
    <script>
        CKEDITOR.replace( 'editor1', {
        extraPlugins: ['easyimage','autogrow'],
        removePlugins: 'image',
        removeDialogTabs: 'link:advanced',
        toolbar: [
        { name: 'document', items: [ 'Undo', 'Redo' ] },
        { name: 'basicstyles', items: [] },
        { name: 'insert', items: [ 'EasyImageUpload' ] }
        ],
        imageStyleFormat:{
        title: 'Full size image',
        className: 'image-full-size'
        },
        removePlugins : 'elementspath',
        cloudServices_uploadUrl: '/edit/upload',
        cloudServices_tokenUrl: 'https://33333.cke-cs.com/token/dev/ijrDsqFix838Gh3wGO3F77FSW94BwcLXprJ4APSp3XQ26xsUHTi0jcb1hoBt'
        } );
        </script>

        </body>
        </html>