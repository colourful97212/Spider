package com.colourful.spider.testGon;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class TestPageProcessor implements PageProcessor {
    private Site site = Site.me().setCharset("UTF-8").setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {                                    //  /zhiguoliu11  \w+$
        page.putField("uid",page.getUrl().regex("\\w+$").toString());
        /*page.putField("name",page.getHtml().xpath("//*[@id='uid']/text()").toString());*/
        page.putField("name",page.getHtml().$("#uid","text").toString());
                                                                                                                                                                                                                                                                            //*[@id="uid"]
        System.out.println(page.getResultItems().getAll());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new TestPageProcessor())
                .addUrl("https://blog.csdn.net/zhiguoliu11")
                .run();
    }
}
