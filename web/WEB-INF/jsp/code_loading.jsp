<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>代码载入</title>
    <script src="https://cdn.staticfile.org/vue/2.4.2/vue.min.js"></script>
    <script src="https://cdn.staticfile.org/vue-resource/1.5.1/vue-resource.min.js"></script>
</head>
<body>

    <div id="loading">
       <button style="font-size: 30px" v-show="seen" @click="load">代码载入</button>
        <h1 style="font-size: 30px" v-show="loading">{{h1_content}}</h1>
    </div>

    <script>
        new Vue({
            el:"#loading",
            data: {
                seen:true,
                loading:false,
                h1_content:"loading........",
            },
            methods:{
                load:function () {
                    this.seen = false;
                    this.loading = true;
                    this.$http.get('/admin/loading').then(function(res){
                        this.h1_content = res.body.data;
                    },function(){
                        this.h1_content = '请求失败处理';
                    });
                }
            }

        });
    </script>
</body>
</html>