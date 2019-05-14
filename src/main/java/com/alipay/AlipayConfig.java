package com.alipay;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2018121562511976";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCndfu/eiavG6/HoFZdKz4jmuxyAiMgUfh0FZK62Yzw5lAZHwddqVVedj8TA2V7YJ985ys03G8aST3ifcNtcQbGUk+Y85HhvKvZPYS3nyCUfnbkaoaiD76MTnq/yk+KPpTqqIMj+WJJ1z6AFxRAnOIGIDEZKIkJRtS6OofyReG5gv3xHPXhAUsuherKCI/45vxjV3y1zO8A00i0vcTluhJ9uLp3gYNptcsmF321Qok25dyCRQbaUcpLYTnZNIIkYW4cfEH+AivD8YuWDF3C3eyVCMnAUe7pmkN+EXdICLU31CV7uwmIf4h5Rak2UvsiruMgiIKi4rbkvSQUfcK7l3rXAgMBAAECggEAc2YAh+C7S6w+o8g/+5BTD+Iw0hfa8OjL1+MN7CkC0PdFdypEvyxzodbGfGm4wx7AiSgcRB8riOez96JGy+I5ku0h4ProKrEDk36rOIY1w16qu5kVEhTbcICcYhIeYdvibxqU1HOEYfIsJcTBtgzmTOZeL0IZvQTMj1DKYg/6DwWxDz1kFBP7b3AwEy7EKseuMmCZ5r1G9moNqzneIB3xz2yI+0YjdcehGiQymn29KkPTaBC8uJIIRiKrW8BPUqQNDm3DEiDpjbGQqNzWL1/TZ+5TYD2lnRSM+cYiziey0/B+FCra7fcdtGGaznS2iMoe2TGf8iPXDPPdvM6sWqePgQKBgQDka4OMaidvsGwl4ZZok/+mhRxmrDgk7XBlGdN/CQuCujlcY1SvK+i2V4mD2ch7AAk6rIkubO5RVnBK25oftFNXQElXY3zQhSHiqNrRyiJtofAzSYEm6r0P1twvCaa48AX+0qbdq3W4tsOJ0BdNc120+3IEo1jUqUIqimhlIcjTIQKBgQC7rjeRBZ33iyPrI8pHoLIXbHuGGIFcB5FBvXM9Cr5auLm3cBbAeF9+QNXNg7oLFdTm9+Qnu7kENTGXVLIMzeIgUuRsD57a5WmxdfoUlLHpZ7LAcRLAeZvDtcrhqkj+RDoFqok1PATnjxZ1yWORT6y9WJD7S7Cx2l/DG1d3AsEG9wKBgQCmMiPw0nwHI9JMbarGRIjKXCeh/MSM+cXBidz8nVQmteBufEdtfQFBSsffi2H3PLt6WXcmtFNiEwZ5MPcfAlFxNnEmiFiUT4msJTexVOVf52ehqWWzLzCIiciC4P7RqLJ0CkknGE37K99yMloCHGKeACWhgkhr0ws1cKcsjjxUgQKBgEPYP7gKCsewocRfxMDN43M6QREBjMnC5oyYOq9HUfajrmcUevUKzqJqN5+6TaeyNatk0zJV1xUhUZL+B/s+9c5viKlToD5LDkEbf9Kuyf2xAdo1Re0USO2fqv6WuxpHeZ4Qvtbionc+r4TWz0p5Pd2YxZhCDVbNtS5nlN+tvC+TAoGBAJG0SNGsC5p1ykR8rGXrk615lN5qSVx1lAlR1my3lklPuHQBWCeXhVoZZc4vXOVk+Vuoi3i0Y9pO6+o7VNOqo9LAAMYLrTx449tzh8/ql80lI2UmXGh0ktnQvIMtd9f/m7BsuP/H3PQs8SWontIFqBA2DIHNW0F95Rp8u8okzrSM";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "https://daoyinjiaoyu.com/guidesound/order/pay_callback";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "https://daoyinjiaoyu.com/guidesound/order/pay_callback";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp3X7v3omrxuvx6BWXSs+I5rscgIjIFH4dBWSutmM8OZQGR8HXalVXnY/EwNle2CffOcrNNxvGkk94n3DbXEGxlJPmPOR4byr2T2Et58glH525GqGog++jE56v8pPij6U6qiDI/liSdc+gBcUQJziBiAxGSiJCUbUujqH8kXhuYL98Rz14QFLLoXqygiP+Ob8Y1d8tczvANNItL3E5boSfbi6d4GDabXLJhd9tUKJNuXcgkUG2lHKS2E52TSCJGFuHHxB/gIrw/GLlgxdwt3slQjJwFHu6ZpDfhF3SAi1N9Qle7sJiH+IeUWpNlL7Iq7jIIiCouK25L0kFH3Cu5d61wIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}
