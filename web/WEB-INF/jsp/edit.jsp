<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="../../css/publisharticle.css" />
    <script type="text/javascript" src="../../js/jquery-2.1.4.js"></script>
    <script src="/statics/ckeditor/ckeditor.js"></script>
    <script>
        function myFunction() {
            var titleNumber = $(".title input").val().length
            $(".title .numbers").text(titleNumber + "/30")
        }
        function publish(){
            alert(1);
        }
        $(function(){
            var selectId = "";
            $(".title input").on('focus',function(){
                $(".closex").css("visibility","visible");
                return false;
            })
            $(".title input").on('blur',function(){
                $(".closex").css("visibility","hidden");
                return false;
            })
            $(".closex").on("click",function () {
                console.log(323)
                $(".title input").val('');
                $(".title .numbers").text("0/30")
                return false;
            })

            var json={
                // 头像地址
                photo: 'http://www.nvsay.com/uploads/allimg/150608/34-15060QFG2S1.png',
                // 用户名
                userName: '黄家驹',
                // 输入框的内容
                inputVal: '',
                // input右侧的数字
                num: 0,
                // 封面图单选框
                covers: '自定义',
                // 待选学科
                subs: '',
                // 适用年级
                grands: '',
                // 选择图片碳层
                centerDialogVisible: false,
                // 定义选中图片div容器id
                conid: 0,
                // 定义选中图片src
                backgroundSrc: ''
            }
            $(".imgInner").click(function () {
                selectId = $(this).attr("id");
//    	    $.ajax({
//                url: "",
//                type: post,
//                success:function (data) {
//                    if(data.status == 200){
//                        $.each(json1, function (n, value) {
//                            var dos = "<div class='eveimg'><img src="+value+"><div class='dmz'></div></div>";
//                            $(".dm").append(dos);
//                            $(".dmz").click(function () {
//                                $(".dmz").css('opacity', 0)
//                                this.style.opacity = 0.6
//                                var imgn=$(this).siblings().attr("src");  //获取图片路径
//
//
//
//                                $(".dbs").click(function () {
//                                    $(".dilog").css('visibility', 'hidden')
//                                })
//                            })
//                        });
//                    }
//
//                }
//            })
                $(".dilog").css('visibility', 'visible')
            })
            $(".t2").click(function () {
                $(".dilog").css('visibility', 'hidden');
                $(".dmz").css('opacity', 0);
            })
            $(".dbq").click(function () {
                $(".dilog").css('visibility', 'hidden');
                $(".dmz").css('opacity', 0);
            })


            // 循环追加图片节点
            var json1 = ['http://www.nvsay.com/uploads/allimg/150608/34-15060QFG2S1.png','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067569402&di=315bb829be86326bbd013597005ea525&imgtype=0&src=http%3A%2F%2Fwww.ithov.com%2Fuploads%2Fallimg%2F120831%2F200SB526-2.jpg','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067664045&di=c5dae8d86bd1c9be6ff70a2bfc18fc30&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D487155973%2C1932661456%26fm%3D214%26gp%3D0.jpg','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067569398&di=2b777b167970d2a14094e0680a0151cc&imgtype=0&src=http%3A%2F%2Fimage002.server110.com%2F20170527%2Ff2e940bbe60c9b5d335b74c42986dee0.jpg','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067569396&di=6c05cb35a1607d26341cbd7c5839b667&imgtype=0&src=http%3A%2F%2F00imgmini.eastday.com%2Fmobile%2F20180930%2F20180930103513_1b17a97d7242224608653885a6d83b6e_2.jpeg']
            $.each(json1, function (n, value) {
                var dos = "<div class='eveimg'><img src="+value+"><div class='dmz'></div></div>";
                $(".dm").append(dos);

            });
            $(".dmz").click(function () {
                console.log($(this))
                $(".dmz").css('opacity', 0)
                this.style.opacity = 0.6

            })

            $(".dbs").click(function () {
                var imgn = "";
                $(".dmz").each(function(index,ele){
                    console.log($(ele).css("opacity"))
                    if($(ele).css("opacity") == 0.6){
                        imgn=$(this).siblings().attr("src");  //获取图片路径
                    }
                })
                console.log(imgn)
                if (imgn != "") {
                    console.log($("#"+selectId+" .ximg").append("<img class='selectedImg' src='"+imgn+"' alt=''>"))
                }
                $(".dilog").css('visibility', 'hidden')
            })

            /* 选中图片返显到下方*/

            $(".coverStyles input").click(function(){
                if($(this).attr("value")=="1")
                {
                    $(".imgInner").css('display', 'none')
                    $("#img1").css('display', 'block');
                } else if($(this).attr("value")=="2")
                {
                    $(".imgInner").css('display', 'block');
                }else{

                    $(".imgInner").css('display', 'none')
                }

            })

            $(".imgs imgInner").click(function(){
                var ts= $(this).attr("id");

            })
            var data= [
                {
                    id: 1,
                    level: "小学前",
                    list: [
                        {
                            id: 101,
                            gradeName: "1-3岁"
                        },
                        {
                            id: 102,
                            gradeName: "4-6岁"
                        },
                        {
                            id: 199,
                            gradeName: "1-6岁"
                        }
                    ]
                },
                {
                    id: 2,
                    level: "小学",
                    list: [
                        {
                            id: 201,
                            gradeName: "1年级"
                        },
                        {
                            id: 202,
                            gradeName: "2年级"
                        },
                        {
                            id: 203,
                            gradeName: "3年级"
                        },
                        {
                            id: 204,
                            gradeName: "4年级"
                        },
                        {
                            id: 205,
                            gradeName: "5年级"
                        },
                        {
                            id: 206,
                            gradeName: "6年级"
                        },
                        {
                            id: 299,
                            gradeName: "小学"
                        }
                    ]
                },
                {
                    id: 3,
                    level: "初中",
                    list: [
                        {
                            id: 301,
                            gradeName: "初一"
                        },
                        {
                            id: 302,
                            gradeName: "初二"
                        },
                        {
                            id: 303,
                            gradeName: "初三"
                        },
                        {
                            id: 399,
                            gradeName: "初中"
                        }
                    ]
                },
                {
                    id: 4,
                    level: "高中",
                    list: [
                        {
                            id: 401,
                            gradeName: "高一"
                        },
                        {
                            id: 402,
                            gradeName: "高二"
                        },
                        {
                            id: 403,
                            gradeName: "高三"
                        },
                        {
                            id: 499,
                            gradeName: "高中"
                        }
                    ]
                }
            ]
            data.forEach(function(ele,index){
                var docs=" <option value ='"+ele.level+"'>"+ele.level+"</option>"
                $(".subs").append(docs);

            })
            var fau= [
                {
                    id: 1,
                    level: "文化课",
                    list: [
                        {
                            id: 101,
                            gradeName: "语文"
                        },
                        {
                            id: 102,
                            gradeName: "数学"
                        },
                        {
                            id: 103,
                            gradeName: "英语"
                        },
                        {
                            id: 104,
                            gradeName: "物理"
                        },
                        {
                            id: 105,
                            gradeName: "化学"
                        },
                        {
                            id: 106,
                            gradeName: "生物"
                        },
                        {
                            id: 107,
                            gradeName: "历史"
                        },
                        {
                            id: 108,
                            gradeName: "地理"
                        },
                        {
                            id: 109,
                            gradeName: "政治"
                        }
                    ]
                },
                {
                    id: 2,
                    level: "音乐",
                    list: [
                        {
                            id: 201,
                            gradeName: "钢琴"
                        },
                        {
                            id: 202,
                            gradeName: "吉他"
                        },
                        {
                            id: 203,
                            gradeName: "古筝"
                        },
                        {
                            id: 204,
                            gradeName: "架子鼓"
                        },
                        {
                            id: 205,
                            gradeName: "尤克里里"
                        },
                        {
                            id: 299,
                            gradeName: "全部"
                        }
                    ]
                },
                {
                    id: 3,
                    level: "美术",
                    list: [
                        {
                            id: 301,
                            gradeName: "素描"
                        },
                        {
                            id: 302,
                            gradeName: "油画"
                        },
                        {
                            id: 303,
                            gradeName: "水粉"
                        },
                        {
                            id: 304,
                            gradeName: "漫画"
                        },
                        {
                            id: 305,
                            gradeName: "水彩"
                        },
                        {
                            id: 306,
                            gradeName: "速写"
                        },
                        {
                            id: 307,
                            gradeName: "少儿美术"
                        },
                        {
                            id: 399,
                            gradeName: "全部"
                        }
                    ]
                },
                {
                    id: 4,
                    level: "舞蹈",
                    list: [
                        {
                            id: 401,
                            gradeName: "芭蕾"
                        },
                        {
                            id: 402,
                            gradeName: "民族"
                        },
                        {
                            id: 403,
                            gradeName: "古典"
                        },
                        {
                            id: 404,
                            gradeName: "爵士"
                        },
                        {
                            id: 405,
                            gradeName: "拉丁"
                        },
                        {
                            id: 406,
                            gradeName: "街舞"
                        },
                        {
                            id: 499,
                            gradeName: "全部"
                        }
                    ]
                },
                {
                    id: 5,
                    level: "武术",
                    list: [
                        {
                            id: 501,
                            gradeName: "跆拳道"
                        },
                        {
                            id: 502,
                            gradeName: "武术"
                        },
                        {
                            id: 503,
                            gradeName: "散打"
                        },
                        {
                            id: 504,
                            gradeName: "柔道"
                        },
                        {
                            id: 599,
                            gradeName: "全部"
                        }
                    ]
                },
                {
                    id: 6,
                    level: "其他",
                    list: [
                        {
                            id: 601,
                            gradeName: "奥数"
                        },
                        {
                            id: 602,
                            gradeName: "国学"
                        },
                        {
                            id: 603,
                            gradeName: "智力开发"
                        },
                        {
                            id: 604,
                            gradeName: "速算"
                        },
                        {
                            id: 699,
                            gradeName: "全部"
                        }
                    ]
                }
            ]
            fau.forEach(function(ele,index){
                var docs=" <option value ='"+ele.level+"'>"+ele.level+"</option>"
                $(".grands").append(docs);

            })
            // 请求年级列表
            $.ajax({
                url:"http://139.199.123.168/ss/type/grade_type_list",
                type:"post",
                success:function (data){
                    console.log(data)
                    if(data.status == 200){
//                        data.data.forEach(function(ele,index){
//                            console.log(ele.level)
//                            var docs=" <option value ='"+ele.level+"'>"+ele.level+"</option>"
//                            $(".subs").append(docs);
//
//                        })
                    }
                }
            })

            // 请求学科列表
            $.ajax({
                url:"http://139.199.123.168/type/subject_type_list",
                type:"post",
                success:function (data){
                    console.log(data)
                    if(data.status == 200){
//                    data.data.forEach(function(ele,index){
//                        console.log(ele.level)
//                        var docs=" <option value ='"+ele.level+"'>"+ele.level+"</option>"
//                        $(".grands").append(docs);
//
//                    })
                    }
                }
            })

            // 点击预览
            $(".preview").on("click",function(){

                var content = CKEDITOR.instances.editor1.getData();
                console.log(content);
//            $.ajax({
//                url:"",
//                type:post,
//                success:function (data) {
                $(".zzc").css("display","block");
                $(".headLine h5").text($(".title input").val())
//                }
//            })
//
            })
            $(".closeCurcle").on("click",function(){
                $(".zzc").css("display","none");
            })

            // 点击发表
            $(".publish").on("click",function(){
                // 先判断标题是否有
                if($(".title input").val() == ""){
                    alert("请填写标题")

                }else if($(".text textarea").val() == ""){
                    alert("请填写内容")
                }else {
                    //            $.ajax({
//                url:"",
//                type:post,
//                success:function (data) {
                    $(".coverStyles input").each(function (index,ele) {
//                console.log(ele.checked +"==="+index)
                        if(ele&&ele.checked){
//                    console.log(index+1)
                            var indexval=index+1;
                            // 值为2选择三图
                            if(indexval == 2){
                                if($(".imgInner .ximg img").length<3){
                                    alert("图片不要少于三张")
                                }else{
                                    console.log($(".imgInner .ximg img"))
                                    $(".imgInner .ximg img").each(function(index,ele){
                                        console.log($(ele).attr("src"));
                                    })
                                }
                            }else if(indexval == 1){
                                if($(".imgInner .ximg img").length<1){
                                    alert("请选择图片")
                                }else{
                                    console.log($(".imgInner .ximg img").attr("src"))
                                }
                            }else if(indexval == 3){

                            }
                        }
                    })
//                }
//            })
//            location.href="./publish-success.html";
                }


            })

        })
    </script>
    <title>发表文章</title>
</head>
<body>

<div class="container">
    <!--顶部导航栏-->
    <div class="topBar">
        <div class="logo">
            <img src="../static/dy-logo.png" alt="">
        </div>
        <!--头像区域-->
        <div class="loginBar">
            <!--头像-->
            <div class="photo">
                <%--<img src= '../static/timg.jpg' alt="">--%>
                <img src= '${requestScope.user.head }' alt="">
            </div>
            <!--用户名-->
            <div class="userName">
                ${requestScope.user.name }
            </div>
            <!--推出-->
            <div class="exit" >退出</div>
        </div>
    </div>
    <!--主体内容区域-->
    <div class="inner">
        <div class="topLine">
            <span>发表文章</span>
        </div>
        <!--标题-->
        <div class="title">
            <input type="text" placeholder="请输入文章标题" maxlength="30" value='' onkeydown="myFunction()">
            <div class="numbers">0/30</div>
            <span class="closex">×</span>
        </div>


        <!--输入域-->
        <div class="text">
            <textarea name="" id="editor1" cols="30" rows="10" class="textarea"></textarea>
        </div>
        <!--封面选项以及图片上传-->
        <div class="coverAndImgs">
            <!--单选框部分-->
            <div class="coverStyles">
                <div class="coverTitle" style="margin-right: 10px;">文章封面图：</div>
                <input type="radio" name="choose" id="r1" value="1" >
                <label for="r1">单图</label>
                <input type="radio" name="choose" id="r2" value="2">
                <label for="r2">三图</label>
                <input type="radio" name="choose" id="r3" value="3" checked="checked">
                <label for="r3">自动</label>
            </div>
            <!--上传图片的方框-->
            <div class="imgs">
                <div class="imgInner" id="img1"  style="position: relative;">
                    <span class="addSig">+</span>
                    <div class="cimg1 ximg" style="position: absolute; width: 100%; height: 64px; top: 0; left: 0;">
                    </div>
                </div>
                <div class="imgInner" id="img2" style="position: relative;">
                    <span class="addSig">+</span>
                    <div class="cimg2 ximg" style="position: absolute; width: 100%; height: 64px; top: 0; left: 0;"></div>
                </div>
                <div class="imgInner" id="img3"  style="position: relative;">
                    <span class="addSig">+</span>
                    <div class="cimg3 ximg" style="position: absolute; width: 100%; height: 64px; top: 0; left: 0;"></div>
                </div>
            </div>
        </div>
        <!--学科-->
        <div class="subjects">
            <!--单选框部分-->
            <div class="subInner">
                <div class="subTitle">适用学科：</div>
            </div>
            <select v-model="subs" class="subs">
                <option value="" disabled selected>请选择学科</option>
                <!--<option value ="volvo">Volvo</option>-->
                <!--<option value ="saab">Saab</option>-->
                <!--<option value="opel">Opel</option>-->
                <!--<option value="audi">Audi</option>-->
            </select>
        </div>
        <!--适用年级-->
        <div class="grand">
            <!--单选框部分-->
            <div class="grandInner">
                <div class="grandTitle">适用年级：</div>
            </div>
            <select v-model="grands" class="grands">
                <option value="" disabled selected>请选择年级</option>
                <!--<option value ="volvo">Volvo</option>-->
                <!--<option value ="saab">Saab</option>-->
                <!--<option value="opel">Opel</option>-->
                <!--<option value="audi">Audi</option>-->
            </select>
        </div>
    </div>
    <!--选择图片弹窗-->
    <div class="dilog">
        <div class="dilogtitle">
            <div class="t1" style="float:left;">正文图片</div>
            <div class="t2" style="float:right;    margin-right: 10px;cursor: pointer;">X</div>
        </div>
        <div class="dm">
        </div>
        <div class="db">
            <button class="dbq">取消</button>
            <button class="dbs">确认</button>
        </div>
    </div>



    <!--底部-->
    <div class="footer">
        <div class="btns">
            <button class="preview">预览</button>
            <button class="publish">发表</button>
        </div>
    </div>
</div>


<!-- 遮罩层 -->
<div class="zzc">
    <div class="bk">
        <div class="nm">
            <div class="headLine">
                <h5>牛顿运动定律精讲</h5>
                <div class="headInfo">
                    <ul>
                        <li><img src="${requestScope.user.head }" alt="" style="width: 100%;height: 100%;"></li>
                        <li>${requestScope.user.name }</li>
                        <li>${requestScope.created_time }</li>
                    </ul>
                </div>
            </div>
            <div class="Hcontent">
                女人被指责十多分钟后，男人扑通一声摔倒在地。据估计，他或他因为感冒或中暑晕倒了，或他太受刺激了。这个女人也吓了他一跳，很快跪下来抱起她的丈夫，一直哭个不停。我不应该因为道歉而这样骂你……其实夫妻之间吵架其实没有必要在公共场合，回家后有啥事坐下来慢慢说，不要闹得不可收拾了才知道后悔。
            </div>
        </div>
        <div class="closeCurcle">
            <span>X</span>
        </div>
    </div>
</div>
<script>
    CKEDITOR.replace( 'editor1', {
        extraPlugins: ['easyimage','autogrow'],
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

        cloudServices_uploadUrl: '/edit/upload',
        cloudServices_tokenUrl: 'https://33333.cke-cs.com/token/dev/ijrDsqFix838Gh3wGO3F77FSW94BwcLXprJ4APSp3XQ26xsUHTi0jcb1hoBt'
    } );
</script>

</body>
</html>