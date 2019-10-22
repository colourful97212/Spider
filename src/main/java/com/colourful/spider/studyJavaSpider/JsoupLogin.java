package com.colourful.spider.studyJavaSpider;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class JsoupLogin {

    /**
     * 手动设置 cookies
     * 先从网站上登录，然后查看 request headers 里面的 cookies
     * @param url
     * @throws IOException
     */
    public void setCookie(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .header("Cookie","bid=in2OAh7rUX0; douban-fav-remind=1; __yadk_uid=l8CUeDPQRKnrW0qCpLGc7nOMmQAy9Uui; ll=\"108288\"; dbcl2=\"140548412:GlRKMar0ICQ\"; ck=ikzt; ap_v=0,6.0; push_noty_num=0; push_doumail_num=0; douban-profile-remind=1")
                .get();
        Element element = document.select("#db-usr-profile > div.info > h1").first();
        String name = element.ownText();
        System.out.println(name);
    }

    /**
     * Jsoup 模拟登录豆瓣 访问个人中心
     * 在豆瓣登录时先输入一个错误的账号密码，查看到登录所需要的参数
     * 先构造登录请求参数，成功后获取到cookies
     * 设置request cookies，再次请求
     * @param loginUrl 登录url
     * @param userInfoUrl 个人中心url
     * @throws IOException
     */
    public void jsoupLogin(String loginUrl,String userInfoUrl) throws IOException {
        Map<String,String> data = new HashMap<>();
        data.put("name","18234187971");
        data.put("password","ccqq2016");
        data.put("remember","false");
        data.put("ticket","");
        data.put("ck","");
        Connection.Response login = Jsoup.connect(loginUrl)
                .ignoreContentType(true)// 忽略类型验证
                .followRedirects(false)// 禁止重定向
                .postDataCharset("utf-8")
                .header("Upgrade-Insecure-Requests","1")
                .header("Accept","application/json")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("X-Requested-With","XMLHttpRequest")
                .header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                .data(data)
                .method(Connection.Method.POST)
                .execute();
        System.out.println("登陆成功");
        login.charset("UTF-8");
        // login 中已经获取到登录成功之后的cookies
        // 构造访问个人中心的请求
        Document document = Jsoup.connect(userInfoUrl)
                // 取出login对象里面的cookies
                .cookies(login.cookies())
                .get();
        Element element = document.select("#db-usr-profile > div.info > h1").first();
        System.out.println(element.ownText());
    }

    /**
     * httpclient 的方式模拟登录豆瓣
     * httpclient 跟jsoup差不多，不同的地方在于 httpclient 有session的概念
     * 在同一个httpclient 内不需要设置cookies ，会默认缓存下来
     * @param loginUrl
     * @param userInfoUrl
     */
    public void httpClientLogin(String loginUrl,String userInfoUrl) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpUriRequest login = RequestBuilder.post()
                .setUri(new URI(loginUrl))// 登陆url
                .setHeader("Upgrade-Insecure-Requests", "1")
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("X-Requested-With", "XMLHttpRequest")
                .setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                // 设置账号信息
                .addParameter("name", "your_account")
                .addParameter("password", "your_password")
                .addParameter("remember", "false")
                .addParameter("ticket", "")
                .addParameter("ck", "")
                .build();
        // 模拟登陆
        CloseableHttpResponse response = httpclient.execute(login);
        if (response.getStatusLine().getStatusCode() == 200) {
            // 构造访问个人中心请求
            HttpGet httpGet = new HttpGet(userInfoUrl);
            CloseableHttpResponse user_response = httpclient.execute(httpGet);
            HttpEntity entity = user_response.getEntity();
            //
            String body = EntityUtils.toString(entity, "utf-8");

            // 偷个懒，直接判断 缺心眼那叫单纯 是否存在字符串中
            System.out.println("缺心眼那叫单纯是否查找到？" + (body.contains("缺心眼那叫单纯")));
        } else {
            System.out.println("httpclient 模拟登录豆瓣失败了!!!!");
        }
    }

    public static void main(String[] args) throws IOException {
       /* JsoupLogin jsoupLogin = new JsoupLogin();
        jsoupLogin.jsoupLogin("https://accounts.douban.com/j/mobile/login/basic","https://www.douban.com/people/140548412/");*/
        new JsoupLogin().jsoupLogin("https://accounts.douban.com/j/mobile/login/basic","https://www.douban.com/people/140548412/");
    }
}
