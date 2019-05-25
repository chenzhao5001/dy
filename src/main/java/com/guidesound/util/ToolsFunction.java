package com.guidesound.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.qcloud.Utilities.Json.JSONException;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolsFunction {

    private static final OkHttpClient client = new OkHttpClient();


    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }

    //执行命令
    public static String exec(String command) throws InterruptedException {
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            System.err.println("Create runtime false!");
        }
        try {
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return returnString;
    }

    /**
     *判断是数字字符串
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     *视频转码
     */
    public static boolean videoChange(String from,String to) {

        try {
            ToolsFunction.exec("/home/ubuntu/wu/dyFFmpeg "
                    + from + " " + to);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public static String changeVideo(String source_url) throws IOException, InterruptedException {

        FormBody formBody = new FormBody
                .Builder()
                .add("convert_url",source_url)
                .add("sign","guide_sound")
                .build();

        Request req = new Request.Builder()
                .url("http://139.199.123.168/fileservice/convert_video")
                .post(formBody)
                .build();
        Response resp;
        OkHttpClient client_temp = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        resp = client_temp.newCall(req).execute();
        String jsonString = resp.body().string();
        JSONObject json = new JSONObject(jsonString);
        String url = json.getString("data");
        return url;
    }

    /**
     *上传文件到文件服务器
     */
    public static String upFileToServicer(File file) throws IOException {

        RequestBody fileBody = RequestBody.create(MediaType.parse("video/mp4"), file);
        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload","show_video_"+ getRandomString(5) + ".mp4",fileBody)
                .addFormDataPart("sign","guide_sound")
                .build();

        Request req = new Request.Builder()
                .url("http://139.199.123.168/fileservice/upload")
                .post(formBody)
                .build();
        Response resp;
        resp = client.newCall(req).execute();
        String jsonString = resp.body().string();
        JSONObject json = new JSONObject(jsonString);
        String url = json.getString("data");
        return url;
    }


    /**
     *上传文件到文件服务器
     */
    public static String upTextToServicer(String content) throws IOException {

        RequestBody fileBody = RequestBody.create(MediaType.parse("text/html"), content);
        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload","article_"+ getRandomString(5) ,fileBody)
                .addFormDataPart("sign","guide_sound")
                .build();

        Request req = new Request.Builder()
                .url("http://139.199.123.168/fileservice/upload")
                .post(formBody)
                .build();
        Response resp;
        resp = client.newCall(req).execute();
        String jsonString = resp.body().string();
        JSONObject json = new JSONObject(jsonString);
        String url = json.getString("data");
        return url;
    }





    /**
     *生成随机字符串
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     *生成随机字符串
     */
    public static String getNumRandomString(int length){
        String str="0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean paramCheck(BindingResult result,StringBuilder errMsg) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String err = "";
            for (ObjectError error : errors) {
                err += error.getDefaultMessage();
            }
            errMsg.append(err);
            return false;
        }
        return true;

    }



    public static String httpGet(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();

        Call call = okHttpClient.newCall(request);
        String content;
        Response response = call.execute();
        return response.body().string();
    }

    public static void zoomImage(String src,String dest,int w,int h) throws Exception {

        double wr=0,hr=0;
        File srcFile = new File(src);
        File destFile = new File(dest);

        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板

        wr=w*1.0 / bufImg.getWidth();     //获取缩放比例
        hr=h*1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        try {
            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //写入缩减后的图片
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String trimStr(String str, String indexStr){
        if(str == null){
            return null;
        }
        StringBuilder newStr = new StringBuilder(str);
        if(newStr.indexOf(indexStr) == 0){
            newStr = new StringBuilder(newStr.substring(indexStr.length()));

        }else if(newStr.indexOf(indexStr) == newStr.length() - indexStr.length()){
            newStr = new StringBuilder(newStr.substring(0,newStr.lastIndexOf(indexStr)));

        }else if(newStr.indexOf(indexStr) < (newStr.length() - indexStr.length())){
            newStr =  new StringBuilder(newStr.substring(0,newStr.indexOf(indexStr))
                   + newStr.substring(newStr.indexOf(indexStr)+indexStr.length(),newStr.length()));
        }
        return newStr.toString();
    }

    public static String sendSMS(String phone,String content) {
        SmsSingleSenderResult result = null;
        try {
            SmsSingleSender ssender = new SmsSingleSender(1400162470, "412f551d35f00a8cf95ba5f63e1f8ffd");
            result = ssender.send(0, "86", phone,
                    content, "", "");

            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    //保留两位小数
    public static double formatDouble2(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }

 }
