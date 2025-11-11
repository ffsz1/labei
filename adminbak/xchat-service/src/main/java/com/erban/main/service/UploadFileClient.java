package com.erban.main.service;

import java.io.File;

/**
 * Created by liuguofu on 2017/9/16.
 */
public class UploadFileClient {

    public static void main(String args[])throws Exception{
        batchUploadFile();
    }
    private static void batchUploadFile() throws Exception{
        ErBanNetEaseService erBanNetEaseService=new ErBanNetEaseService();
        File file=new File("D:\\gift\\1205");
        String str=erBanNetEaseService.uploadImgBatch(file);
        System.out.println(str);
    }
}
