package com.colourful.spider.webmegicspider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.Selectable;

import javax.swing.text.Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WangYiNews implements PageProcessor {
    private Site site = Site.me().setCharset("UTF-8").setRetryTimes(1000).setSleepTime(1000);

    @Override
    public void process(Page page) {
/*
        String url = page.getUrl().toString();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String body = EntityUtils.toString(entity,"GBK");
                body = body.replace("data_callback(", "");
                body = body.substring(0, body.lastIndexOf(")"));
                JSONArray jsonArray = JSON.parseArray(body);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    System.out.println("文章标题：" + data.getString("title") + " ,文章链接：" + data.getString("docurl"));
                }
            }else {
                System.out.println("处理失败！！！返回状态码：" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
/*        String url = page.getUrl().toString();
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(url);*/
        if (page.getUrl().toString().contains("/")) {
            page.addTargetRequests(page.getHtml().links().regex("https://news\\.163\\.com/.*").all());
            List<String> listUrl = page.getHtml().$("#index2016_wrap > div:nth-child(1) > div.ns-bg-wrap > div.index2016_content > div.ns_area.clearfix.index_main > div.main_center_news > div.mod_datalist > div > ul > li:nth-child(1) > div > div > div > div.news_title > h3 > a"
                    ,"href").all();
            List<String> listText = page.getHtml().$("#index2016_wrap > div:nth-child(1) > div.ns-bg-wrap > div.index2016_content > div.ns_area.clearfix.index_main > div.main_center_news > div.mod_datalist > div > ul > li:nth-child(1) > div > div > div > div.news_title > h3 > a"
                    ,"text").all();
            Map<String,String> map = new HashMap<>();
            for (int i=0; i<listText.size(); i++){
                map.put(listText.get(i),listUrl.get(i));
                System.out.println(listText.get(i)+" ： "+listUrl.get(i));
            }
        } else {
            page.getResultItems().setSkip(true);
        }
/*        for (int i = 0; i < gg.size(); i ++){            //#index2016_wrap > div:nth-child(1) > div.ns-bg-wrap > div.index2016_content > div.ns_area.clearfix.index_main > div.main_center_news > div.mod_datalist > div > ul > li:nth-child(1) > div > div:nth-child(2) > div > div.news_title > h3 > a
            System.out.println(page.getHtml().$("#index2016_wrap > div:nth-child(1) > div.ns-bg-wrap > div.index2016_content > div.ns_area.clearfix.index_main > div.main_center_news > div.mod_datalist > div > ul > li:nth-child(1) > div > div:nth-child("+i+") > div > div.news_title > h3 > a"
            ,"text") + " ：" +page.getHtml().$("#index2016_wrap > div:nth-child(1) > div.ns-bg-wrap > div.index2016_content > div.ns_area.clearfix.index_main > div.main_center_news > div.mod_datalist > div > ul > li:nth-child(1) > div > div:nth-child("+i+") > div > div.news_title > h3 > a"
            ,"href"));
        }*/
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        System.setProperty("selenuim_config","C:\\Users\\Administrator\\Desktop\\config.ini");
        Spider.create(new WangYiNews())
                .addUrl("https://news.163.com/")
                //浏览器驱动（动态网页信息通过模拟浏览器启动获取）
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"))
                .run();
    }
}
