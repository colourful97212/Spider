package com.colourful.spider.studyJavaSpider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class learnJsoup {

    /**
     *jsoup方式 获取虎扑新闻列表页
     * @param url 虎扑新闻列表页url
     */
    public void jsoupList(String url){

        try {
            Document document = Jsoup.connect(url).get();
            // 使用 css选择器 提取列表新闻 a 标签
            // <a href="https://voice.hupu.com/nba/2485864.html" target="_blank">海佐尼亚：我会尽我所能帮助球队赢得总冠军</a>
            Elements elements = document.select("div.news-list > ul > li > div.list-hd > h4 > a");
            log.info("请求数据完成");
            File file = new File("D:/data.txt");
            log.info("data.txt创建完成");
            //创建excel文件
            File fileExcel = new File("D://data.xls");
            log.info("data.xls创建完成");
            //定义表头
            String[] titleExcel={"标题","连接"};
            //创建excel工作簿
            HSSFWorkbook workbook=new HSSFWorkbook();
            //创建工作表sheet
            HSSFSheet sheet=workbook.createSheet();
            //创建第一行
            HSSFRow row=sheet.createRow(0);
            HSSFCell cell=null;

            //插入第一行数据的表头
            for(int i=0;i<titleExcel.length;i++){
                cell=row.createCell(i);
                cell.setCellValue(titleExcel[i]);
            }
            int i = 1;
            StringBuilder data = new StringBuilder();
            for (Element element : elements){
                i ++;
                //log.info(element.toString());
                // 获取详情页链接
                String d_url = element.attr("href");
                // 获取标题
                String title = element.ownText();
                data.append(title + " : " + d_url + "\r\n");

                log.info("data字符串中追加一条数据");

                //data.xlsx写入数据
                HSSFRow nrow=sheet.createRow(i);
                HSSFCell ncell=nrow.createCell(0);
                ncell.setCellValue(title);
                ncell=nrow.createCell(1);
                ncell.setCellValue(d_url);
                log.info("工作簿里加一行");

            }
            FileUtils.writeStringToFile(file, String.valueOf(data),"UTF-8",true);
            FileOutputStream stream= FileUtils.openOutputStream(fileExcel);
            workbook.write(stream);
            log.info("全部写出");
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void httpClientList(String url){
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String body = EntityUtils.toString(entity,"utf-8");

                if (body != null){
                    /*
                     * 替换掉换行符、制表符、回车符，去掉这些符号，正则表示写起来更简单一些
                     * 只有空格符号和其他正常字体
                     */
                    Pattern p = Pattern.compile("\t|\r|\n");
                    Matcher m = p.matcher(body);
                    body = m.replaceAll("");
                    /*
                     * 提取列表页的正则表达式
                     * 去除换行符之后的 li
                     * <div class="list-hd">    <h4>   <a href="https://voice.hupu.com/nba/2485167.html"  target="_blank">与球迷亲切互动！凯尔特人官方晒球队开放训练日照片</a>   </h4>                                </div>
                     */
                    Pattern pattern = Pattern
                            .compile("<div class=\"list-hd\">\\s* <h4>\\s* <a href=\"(.*?)\"\\s* target=\"_blank\">(.*?)</a>\\s* </h4>\\s* </div>" );
                    Matcher matcher = pattern.matcher(body);
                    // 匹配出所有符合正则表达式的数据
                    while (matcher.find()){
//                        String info = matcher.group(0);
//                        System.out.println(info);
                        // 提取出链接和标题
                        System.out.println("详情页链接："+matcher.group(1)+" ,详情页标题："+matcher.group(2));
                    }
                }else {
                    System.out.println("处理失败！！！获取正文内容为空");
                }
            }else {
                System.out.println("处理失败！！！返回状态码：" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        learnJsoup learnJsoup = new learnJsoup();
        learnJsoup.jsoupList("https://voice.hupu.com/nba");
/*        learnJsoup.httpClientList("https://voice.hupu.com/nba");*/
    }
}
