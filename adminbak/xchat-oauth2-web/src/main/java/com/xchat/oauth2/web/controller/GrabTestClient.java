package com.xchat.oauth2.web.controller;

import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.eac.EACException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/3/29.
 */
public class GrabTestClient {

    public static void main(String args[]) throws Exception {
        String tagsUrl="/s/2/remen,/s/2/fengmian,/s/2/gaoxiao,/s/2/yinyue,/s/2/mengchong,/s/2/nvsheng,/s/2/teji";
//        grabVideo();

    }

//    public static void grabVideo() throws Exception {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpResponse response = null;
//        HttpGet downloadHttpGet = getWexinHttpGet("http://wxrobt.applinzi.com/s/2/gaoxiao?sk=5000");
//        response = httpclient.execute(downloadHttpGet);
//        HttpEntity entity = response.getEntity();
//        InputStream is = entity.getContent();
//        String result = EntityUtils.toString(entity, "utf-8");
//        Elements element = null;
//        Document jsResult = Jsoup.parse(result);
//
//        Elements datalist = jsResult.select("[class='weui-cell_access weui-media-box weui-media-box_appmsg']");//获取别名信息
//        for (Element data : datalist) {
//            String tag="";
//            String videoUrl = data.attr("href");
//            String imgUrl = data.select("img[class=weui-media-box__thumb]").attr("src");
//            String title = data.select("h4[class='weui-media-box__title']").text();
//            System.out.println(videoUrl+"~~~~~~"+imgUrl+"~~~~~~"+title);
//        }
//
////        System.out.println(result);
//        int statusCode = response.getStatusLine().getStatusCode();
//    }
//
//    public static HttpGet getDownloadHttpGet(String url) {
//        HttpGet downloadHttpGet = new HttpGet(url);
//        downloadHttpGet.addHeader("Accept",
//                "*/*");
//        downloadHttpGet.addHeader("Accept-Language", "zh-CN");
//        downloadHttpGet.addHeader("Cookie", "qqmusic_fromtag=11;qqmusic_uin=1072220411;qqmusic_key=F0FC3545991E74E9B568AC0CF44F40A56B6D6D7E6A3AC945DF06B10DF28CAE08;");
//        downloadHttpGet.addHeader("User-Agent", "QMDL");
//        return downloadHttpGet;
//    }
//
//    public static HttpGet getWexinHttpGet(String url) {
//        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("Accept",
//                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
//        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//        httpGet.addHeader("Cache-Control", "max-age");
//        httpGet.addHeader("Connection", "keep-alive");
//        httpGet.addHeader("Cookie",
//                "ntes_nuid=168184eee884f0b4c0b87647514175a6; vjuids=3911f0cc7.14e25787a9a.0.9725c678; _ntes_nnid=32f53679c3fb87d8ef6df2eecc3cce62,1435147729570; usertrack=c+5+hVWLV8a+3UG4EuVeAg==; __gads=ID=c7f9c42c8e4a2509:T=1436268455:S=ALNI_MbaFlUe9pU247i7GJZsH2J0l0b5Aw; __oc_uuid=142f6290-2c25-11e5-bd10-614a753d5630; __utma=187553192.1808236820.1435801607.1435801607.1437097347.2; __utmz=187553192.1437097347.2.2.utmcsr=163.com|utmccn=(referral)|utmcmd=referral|utmcct=/; visited=true; Province=020; City=020; _ga=GA1.2.1808236820.1435801607; vjlast=1437097208.1445230118.11; ne_analysis_trace_id=1445230117516; vinfo_n_f_l_n3=b1305a0171d3ddd4.1.31.1435147729683.1445230316567.1445235061102; s_n_f_l_n3=b1305a0171d3ddd41445235058385; JSESSIONID-WYYY=5d06bc38f7911465a850ad9b1220fb10589966c819b3fd47af087dfc581b167a91f2269ae56c3804d2484a47de14b62d70f097c8f491559ac666c2666048b6fc84f88584eae3f123b81bad38da4b4894bd901e9e542c256337302f43a7f2ea200e3f0471f2b381cd40e71041bb2c34efe5deabad6762207511d708650f0bbbae1eb680fa%3A1445238637718; _iuqxldmzr_=25; __utma=94650624.662516890.1445224651.1445224651.1445235418.2; __utmb=94650624.67.10.1445235418; __utmc=94650624; __utmz=94650624.1445224651.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic|utmctr=%E7%BD%91%E6%98%93%E4%BA%91%E9%9F%B3%E4%B9%90");
//        httpGet.addHeader("User-Agent",
//                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 MicroMessenger/6.5.6 NetType/WIFI Language/zh_CN");
//        httpGet.addHeader("Upgrade-Insecure-Requests", "1");
//        httpGet.addHeader("Referer", "http://wxrobt.applinzi.com/s/2/gaoxiao");
//        httpGet.addHeader("Host", "wxrobt.applinzi.com");
//        RequestConfig
//                requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();//设置请求和传输超时时间
//        httpGet.setConfig(requestConfig);
//        return httpGet;
//    }
}
