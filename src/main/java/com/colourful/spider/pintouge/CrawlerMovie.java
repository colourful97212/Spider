package com.colourful.spider.pintouge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlerMovie {

    public static void main(String[] args) {
        try {
            CrawlerMovie crawlerMovie = new CrawlerMovie();
            // 豆瓣电影链接
            List<String> movies = crawlerMovie.movieList();
            //创建10个线程的线程池
            ExecutorService exec = Executors.newFixedThreadPool(10);
            for (String url : movies) {
                //执行线程
                exec.execute(new CrawlMovieThread(url));
            }
            //线程关闭
            exec.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> movieList() throws Exception{
        //获取100条电影连接
        String url = "https://movie.douban.com/j/search_subjects?type=movie&tag=热门&sort=recommend&page_limit=200&page_start=0";
                    //https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=0
                    //https://movie.douban.com/explore#!type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=0
        CloseableHttpClient client = HttpClients.createDefault();
        List<String> movies = new ArrayList<>(100);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = client.execute(httpGet);
        System.out.println("获取豆瓣电影列表，返回验证码："+response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200){
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity,"utf-8");
            //将请求结果格式化成json
            JSONObject jsonObject = JSON.parseObject(body);
            JSONArray data = jsonObject.getJSONArray("subjects");
            for (int i = 0; i < data.size(); i++) {
                JSONObject movie = data.getJSONObject(i);
                movies.add(movie.getString("url"));
            }
        }
        response.close();
        client.close();
        return movies;
    }
}

class CrawlMovieThread extends Thread{
    String url;

    public CrawlMovieThread(String url){
        this.url = url;
    }

    public void run(){

        try {
            Connection connection = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .timeout(50000);
            Connection.Response response = connection.execute();
            System.out.println("采集豆瓣电影,返回状态码：" + response.statusCode());
        } catch (IOException e) {
            System.out.println("采集豆瓣电影,采集出异常：" + e.getMessage());
        }
    }
}
