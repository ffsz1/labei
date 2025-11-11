package com.xchat.common.utils;

import com.google.common.collect.Lists;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by liuguofu on 2017/7/17.
 */
public class FileUtils {

    public static List<InputStream> downloadFileInputStream(List<String> filePathList) throws Exception{

        List<InputStream> fileInputSteamList= Lists.newArrayList();
        for(int i=0;i<filePathList.size();i++){
            String urlString=filePathList.get(i);
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(3 * 1000);
            InputStream is = con.getInputStream();
            fileInputSteamList.add(is);
        }
        return fileInputSteamList;

    }
    public static InputStream downloadFileInputStream(String fileUrl) throws Exception{
        URL url = new URL(fileUrl);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(3 * 1000);
        InputStream is = con.getInputStream();
        return is;
    }
}
