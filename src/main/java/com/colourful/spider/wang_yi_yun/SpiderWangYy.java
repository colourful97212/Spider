package com.colourful.spider.wang_yi_yun;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class SpiderWangYy implements PageProcessor {
    private Site site = Site.me().setCharset("UTF-8").setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) { //#\33 746121570611758379  #\33 746121570611758379   #\33 746121570611758379 > td:nth-child(2) > div > div > div > span > a > b
        List<String> str  = page.getHtml().$(".even > td:nth-child(2) > div > div > div > span > a > b","title").all();
        System.out.println(str);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //selenium系统配置，其中的路径写自己config文件的路径
        System.setProperty("selenuim_config", "C:\\Users\\Administrator\\Desktop\\config.ini");
        Spider.create(new SpiderWangYy())
                .addUrl("https://music.163.com/#/playlist?id=2161970231")
                //浏览器驱动（动态网页信息通过模拟浏览器启动获取）
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"))
                .run();
    }
}
