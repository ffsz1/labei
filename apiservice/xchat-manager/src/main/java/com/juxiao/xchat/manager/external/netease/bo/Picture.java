package com.juxiao.xchat.manager.external.netease.bo;

import com.juxiao.xchat.base.utils.MD5Utils;

/**
 * Created by Administrator on 2018/1/5.
 */
public class Picture {
    //            "name":"图片发送于2015-05-07 13:59",   //图片name
//            "md5":"9894907e4ad9de4678091277509361f7",    //图片文件md5
//            "url":"http://nimtest.nos.netease.com/cbc500e8-e19c-4b0f-834b-c32d4dc1075e",    //生成的url
//            "ext":"jpg",    //图片后缀
//            "w":6814,    //宽
//            "h":2332,    //高
//            "size":388245    //图片大小
    private String name;
    private String md5;
    private String url;
    private String ext;
    private int w;
    private int h;
    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }


    public String getMd5() {
        return MD5Utils.encode(md5);
    }

    public void setMd5(String md5) {
        this.md5 = MD5Utils.encode(md5);
    }


}
