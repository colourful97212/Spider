package com.colourful.spider.RequestYibu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.List;

public class Hello {
    /**
     * selenium 用chromeDriver解决数据异步加载问题
     * https://npm.taobao.org/mirrors/chromedriver/
     *
     * @param url   页面地址
     */
    public void selenium(String url) {
        // 设置 chromedirver 的存放位置
        System.getProperties().setProperty("selenuim_config", "C:\\Users\\Administrator\\Desktop\\config.ini");
/*        // 设置无头浏览器，这样就不会弹出浏览器窗口
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");*/

        /*WebDriver webDriver = new ChromeDriver(chromeOptions);*/
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(url);
        // 获取到要闻新闻列表
        List<WebElement> webElements = webDriver.findElements(By.cssSelector("#index2016_wrap > div:nth-child(1) > div.ns-bg-wrap > div.index2016_content > div.ns_area.clearfix.index_main > div.main_center_news > div.mod_datalist > div > ul > li:nth-child(1) > div > div > div > div.news_title > h3 > a"));
        for (WebElement webElement : webElements){
            // 提取新闻连接
            String article_url = webElement.getAttribute("href");
            // 提取新闻标题
            String title = webElement.getText();
            if (article_url.contains("https://news.163.com/")) {
                System.out.println("文章标题：" + title + " ,文章链接：" + article_url);
            }
        }
        webDriver.close();
    }


    /**
     * 使用反向解析法 解决数据异步加载的问题
     * 抓Json
     * @param url Ajax地址
     */
    public void httpclientMethod(String url) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "GBK");
            // 先替换掉最前面的 data_callback(
            body = body.replace("data_callback(", "");
            // 过滤掉最后面一个 ）右括号
            body = body.substring(0, body.lastIndexOf(")"));
            // 将 body 转换成 JSONArray
            JSONArray jsonArray = JSON.parseArray(body);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                System.out.println("文章标题：" + data.getString("title") + " ,文章链接：" + data.getString("docurl"));
            }
        } else {
            System.out.println("处理失败！！！返回状态码：" + response.getStatusLine().getStatusCode());
        }
    }

    public static void main(String[] args) throws IOException {
        new Hello().httpclientMethod("https://temp.163.com/special/00804KVA/cm_yaowen.js?callback=data_callback");
    }
}
