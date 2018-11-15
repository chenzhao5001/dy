
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

        $(function(){
        function show(msg){
        $(".warn").text(msg);
        $(".warn").fadeIn(200);
        setTimeout(function(){
        $(".warn").fadeOut(300);
        },2000)
        }
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
        var arr = [];
        $(".imgInner").click(function () {
        $(".dilog").css('visibility', 'visible')
        $(".zhezhao").css('visibility', 'visible')
        selectId = $(this).attr("id");
        var con = CKEDITOR.instances.editor1.getData();
        $(".hideImg").html(con);
        $(".hideImg img").each(function(index,ele){
        console.log($(ele).attr("src"))
        arr[index]=$(ele).attr("src")
        })
        console.log(arr);
        // 循环追加图片节点
        //                var json1 = ['http://www.nvsay.com/uploads/allimg/150608/34-15060QFG2S1.png','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067569402&di=315bb829be86326bbd013597005ea525&imgtype=0&src=http%3A%2F%2Fwww.ithov.com%2Fuploads%2Fallimg%2F120831%2F200SB526-2.jpg','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067664045&di=c5dae8d86bd1c9be6ff70a2bfc18fc30&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D487155973%2C1932661456%26fm%3D214%26gp%3D0.jpg','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067569398&di=2b777b167970d2a14094e0680a0151cc&imgtype=0&src=http%3A%2F%2Fimage002.server110.com%2F20170527%2Ff2e940bbe60c9b5d335b74c42986dee0.jpg','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542067569396&di=6c05cb35a1607d26341cbd7c5839b667&imgtype=0&src=http%3A%2F%2F00imgmini.eastday.com%2Fmobile%2F20180930%2F20180930103513_1b17a97d7242224608653885a6d83b6e_2.jpeg']
        $(".dm").html("");
        $.each(arr, function (n, value) {
        var dos = "<div class='eveimg'><img src="+value+"><div class='dmz'><div class='induce'></div></div></div>";
        $(".dm").append(dos);

        });
        // 点击图片
        $(".dm .dmz").click(function () {
        //                    console.log($(this))
        $(".dm .dmz").css('background', 'rgba(0,0,0,0)')
        $(this).css('background', 'rgba(0,0,0,0.6)')
        $(".dmz .induce").css("visibility", "hidden")
        $(this).children(".induce").css("visibility", "visible")

        })
        })
        // 点击确认
        $(".dbs").click(function () {
        var imgn = "";
        console.log($(".dmz"))
        $(".dmz").each(function(index,ele){
        //                    console.log($(ele).children(".induce").css("visibility"))
        //                    console.log($(ele).children(".induce").css("visibility") == "visible")
        if($(ele).children(".induce").css("visibility") == "visible"){
        imgn=$(this).siblings().attr("src");  // 获取图片路径
        }
        })
        $(".dmz .induce").css("visibility", "hidden")
        console.log(imgn)
        if (imgn != "") {
        $("#"+selectId+" .ximg").html("");
        console.log($("#"+selectId+" .ximg").append("<img class='selectedImg' src='"+imgn+"' alt=''>"))
        }
        $(".dilog").css('visibility', 'hidden')
        $(".zhezhao").css('visibility', 'hidden')

        })
        // 图片层右上角的
        $(".t2").click(function () {
        $(".dilog").css('visibility', 'hidden');
        $(".zhezhao").css('visibility', 'hidden')
        $(".dmz").css('opacity', 0);
        })

        // 点击取消
        $(".dbq").click(function () {
        $(".dilog").css('visibility', 'hidden');
        $(".zhezhao").css('visibility', 'hidden')
        $(".dm .dmz").css('opacity', 0);
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
        // 请求年级列表
        var fau = [];
        $.ajax({
        url:"/type/grade_type_list",
        type:"post",
        success:function (data){
        console.log(data)
        if(data.status == 200){
        fau = data.data;
        fau.forEach(function(ele,index){
        var hocs=" <option value ='"+ele.level+"'>"+ele.level+"</option>"
        $(".grands").append(hocs);

        })
        }
        }
        })


        // 年级二级联动
        var secondLevel = [];
        var grade_class_id = "";
        $(".grands").on("change",function(){
        fau.forEach(function(ele,index){
        if(ele.level == $(".grands").val()){
        secondLevel = ele.list
        grade_class_id = ele.id
        }

        })
        $(".drands").html("");
        secondLevel.forEach(function(ele,index){
        var socs=" <option value ='"+ele.gradeName+"'>"+ele.gradeName+"</option>"
        $(".drands").append(socs);

        })
        canshu.grands = grade_class_id
        canshu.drands = fau[0].id
        })

        $(".drands").on("change",function(){
        secondLevel.forEach(function(ele,index){
        if(ele.gradeName == $(".drands").val()){
        canshu.drands = ele.id
        }
        })
        })

        // 请求学科列表
        var dat = [];
        $.ajax({
        url:"/type/subject_type_list",
        type:"post",
        success:function (data){
        console.log(data)
        if(data.status == 200){
        dat = data.data;
        dat.forEach(function(ele,index){
        var socs=" <option value ='"+ele.level+"'>"+ele.level+"</option>"
        $(".subs").append(socs);

        })
        }
        }
        })

        // 学科二级联动
        var twoLevel = [];
        var sub_class_id="";
        $(".subs").on("change",function(){
        dat.forEach(function(ele,index){
        if(ele.level == $(".subs").val()){
        twoLevel = ele.list
        sub_class_id = ele.id
        }

        })
        $(".dubs").html("");
        twoLevel.forEach(function(ele,index){
        var docs=" <option value ='"+ele.gradeName+"'>"+ele.gradeName+"</option>"
        $(".dubs").append(docs);

        })
        canshu.subs = sub_class_id
        canshu.dubs = twoLevel[0].id;
        console.log(twoLevel[0].id)
        })
        $(".dubs").on("change",function(){
        twoLevel.forEach(function(ele,index){
        if(ele.gradeName == $(".dubs").val()){
        canshu.dubs = ele.id
        }
        })
        })
        // 点击预览
        $(".preview").on("click",function(){

        $(".zzc").css("display","block");
        $(".headLine h5").text($(".title input").val())
        $(".headInfo li:eq(1)").text($(".userName").text())
        $(".headInfo li:eq(0) img").attr("src",$(".photo img").attr("src"))
        var content = CKEDITOR.instances.editor1.getData();
        $(".Hcontent").html(content)

        })
        $(".closeCurcle").on("click",function(){
        $(".zzc").css("display","none");
        })

        // 点击发表
        var canshu = {
        "head": '',
        "head_pic1": '',
        "head_pic2": '',
        "head_pic3": '',
        "content": '',
        "subs": 1,
        "dubs": 101,
        "grands": 1,
        "drands": 101
        }
        $(".publish").on("click",function(){
        var cent = CKEDITOR.instances.editor1.getData();
        canshu.content = cent;
        canshu.head = $(".title input").val();
        // 先判断标题是否有
        if($(".title input").val() == ""){
        show("请填写标题");

        }else if(cent == null || cent == ""){
        show("请填写内容");
        }else if($(".subjects .subs").val() == "" || $(".subjects .subs").val() == null ){
        show("请选择适用学科");
        }else if($(".subjects .dubs").val() == "" || $(".subjects .dubs").val() == null){
        show("请选择适用学科");
        }else if($(".grand .grands").val() == "" || $(".grand .grands").val() == null){
        show("请选择适用年级");
        }else if($(".grand .drands").val() == "" || $(".grand .drands").val() == null){
        show("请选择适用年级");
        }else {

        $(".coverStyles input").each(function (index,ele) {
        if(ele&&ele.checked){
        var indexval=index+1;
        // 值为2选择三图
        if(indexval == 2){
        if($(".imgInner .ximg img").length<3){
        show("图片不要少于三张")
        }else{
        var threeArr = [];
        $(".imgInner .ximg img").each(function(index,ele){
        console.log($(ele).attr("src"));
        threeArr[index] = $(ele).attr("src");
        })
        canshu.head_pic1 = threeArr[0];
        canshu.head_pic2 = threeArr[1];
        canshu.head_pic3 = threeArr[2];
        }
        }else if(indexval == 1){
        if($(".imgInner .ximg img").length<1){
        show("请选择图片")
        }else{
        canshu.head_pic1 = $(".imgInner .ximg img").attr("src")
        }
        }else if(indexval == 3){
        var cents = CKEDITOR.instances.editor1.getData();
        $(".hideImg").html(cents);
        $(".hideImg img").each(function(index,ele){
        arr[index]=$(ele).attr("src")
        })
        //                                console.log(arr.length);
        //                                console.log(arr.length>=1 && arr.length<3);
        if(arr.length>=1 && arr.length<3){
        // 文章包含1或2张图，封面就选第一张图
        canshu.head_pic1 = arr[0];

        }else if(arr.length>=3){
        // 文章包含大于等于3张图，选前三张；
        canshu.head_pic1 = arr[0];
        canshu.head_pic2 = arr[1];
        canshu.head_pic3 = arr[2];
        }

        }
        }
        })

        $.ajax({
        url:"/article/add",
        type:"post",
        data:{"head":canshu.head, "head_pic1": canshu.head_pic1, "head_pic2": canshu.head_pic2, "head_pic3": canshu.head_pic3,content:canshu.content, "subject_class":canshu.subs, "subject": canshu.dubs, "grade_class": canshu.grands, "grade": canshu.drands},
        success:function(data){
        //                            console.log(data);
        if(data.status == 200){
        location.href="/article/finish";
        } else {
        alert(23);
        show("没有发表成功")
        }
        }
        })
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
        <div class="content">
        <div class="inner">
        <div class="topLine">
        <span>发表文章</span>
        </div>
        <!--标题-->
        <div class="title">
        <input type="text" placeholder="请输入文章标题" maxlength="30" value='' onkeyup="myFunction()">
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
        <label for="r1" style="cursor: pointer">单图</label>
        <input type="radio" name="choose" id="r2" value="2">
        <label for="r2" style="cursor: pointer">三图</label>
        <input type="radio" name="choose" id="r3" value="3" checked="checked">
        <label for="r3" style="cursor: pointer">自动</label>
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
        <select class="subs">
        <option value="" disabled selected>请选择学科</option>
        <!--<option value ="volvo">Volvo</option>-->
        <!--<option value ="saab">Saab</option>-->
        <!--<option value="opel">Opel</option>-->
        <!--<option value="audi">Audi</option>-->
        </select>
        <select class="dubs">
        <option value="" disabled selected>请选择学科</option>
        </select>
        </div>
        <!--适用年级-->
        <div class="grand">
        <!--单选框部分-->
        <div class="grandInner">
        <div class="grandTitle">适用年级：</div>
        </div>
        <select class="grands">
        <option value="" disabled selected>请选择年级</option>
        <!--<option value ="volvo">Volvo</option>-->
        <!--<option value ="saab">Saab</option>-->
        <!--<option value="opel">Opel</option>-->
        <!--<option value="audi">Audi</option>-->
        </select>
        <select class="drands">
        <option value="" disabled selected>请选择年级</option>
        </select>
        </div>
        </div>

        <!--选择图片弹窗-->
        <div class="zhezhao">
        <div class="dilog">
        <div class="dilogtitle">
        <div class="t1" style="float:left;">正文图片</div>
        <div class="t2" style="float:right;margin-right: 10px;cursor: pointer;font-size: 16px">X</div>
        </div>
        <div class="dm">
        </div>
        <!--<div class="hideImg" style="display: none"></div>-->
        <div class="db">
        <button class="dbq">取消</button>
        <button class="dbs">确认</button>
        </div>
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
        <!--提示层-->
        <div class="warn">操作不正确</div>
        <!--隐藏区域-->
        <div class="hideImg" style="display: none"></div>
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
        removePlugins : 'elementspath',
        cloudServices_uploadUrl: '/edit/upload',
        cloudServices_tokenUrl: 'https://33333.cke-cs.com/token/dev/ijrDsqFix838Gh3wGO3F77FSW94BwcLXprJ4APSp3XQ26xsUHTi0jcb1hoBt'
        } );
        </script>

        </body>
        </html>