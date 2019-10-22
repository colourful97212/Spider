package com.colourful.spider.music;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class MusicContent implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setCharset("UTF-8");

    @Override
    public void process(Page page) {        //.m-table > tbody > tr
        String str = page.getHtml().$("#\\31 3002593511570789303575 > div.cntwrap > div:nth-child(1) > div > a").get();
        System.out.println(str);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new MusicContent())
                .addUrl("https://music.163.com/#/playlist?id=568201388")
                .run();
    }
}
