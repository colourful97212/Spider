package com.colourful.spider.webmegicspider;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class Hello implements PageProcessor {
    private Site site = Site.me().setCharset("GBK").setRetryTimes(3);
    private Connection connection = MySQLDBUtil.getConnection();
    private static int count = 0;
    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("^https://news\\.163\\.com/\\d+/\\d+/.*html$").all());
        String url = page.getUrl().toString();
        String name = page.getHtml().$("#epContentLeft > h1","text").get();
        String flow = page.getHtml().$("#post_comment_area > div.post_comment_toolbar > div.post_comment_tiecount > a","text").get();
        String participate = page.getHtml().$("#post_comment_area > div.post_comment_toolbar > div.post_comment_joincount > a","text").get();
                                                        //#post_comment_area > div.post_comment_toolbar > div.post_comment_joincount > a
        List<String> strs = page.getHtml().$("#endText > p","text").all();
        StringBuilder content = new StringBuilder();
        for (String str : strs){
            content.append(str);
        }
        if (name==null||url==null||flow==null||participate==null)
            //skip this page
            return;
        Passage passage = new Passage();
        passage
                .setName(name)
                .setContent(content.toString())
                .setFlow(Integer.parseInt(flow))
                .setParticipate(Integer.parseInt(participate))
                .setUrl(url);
        WangyiDao wangyiDao = new WangyiDao();
        try {
            wangyiDao.add(connection,passage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
/*        page.putField("name",name);
        page.putField("url",url);
        page.putField("flow",flow);
        page.putField("praticepate",participate);*/
/*        System.out.println(passage);
        System.out.println("文章："+name+"，跟帖人数："+flow+"，参与人数为："+participate+"，地址为："+url+"内容为："+content);*/
        count++;
        log.warn("抓取"+count+"数据");
        log.info("文章："+name);
        log.info("跟帖人数："+flow);
        log.info("参与人数为："+participate);
        log.info("地址为："+url);
        log.info("内容为："+content);
        log.warn("已存入数据库");
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        System.setProperty("selenuim_config","C:\\Users\\Administrator\\Desktop\\config.ini");
        Spider.create(new Hello())
                .addUrl("https://news.163.com")
                //浏览器驱动（动态网页信息通过模拟浏览器启动获取）
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"))
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                .thread(3)
                .run();
    }
}
