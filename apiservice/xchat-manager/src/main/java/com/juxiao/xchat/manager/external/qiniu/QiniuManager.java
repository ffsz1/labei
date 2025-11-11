package com.juxiao.xchat.manager.external.qiniu;

import java.io.InputStream;

/**
 * 七牛文件上传
 */
public interface QiniuManager {

    /**
     *  构建成可访问的文件地址
     *
     * @param fileName
     * @return
     */
    String mergeUrl(String fileName);

    /**
     *  构建成可访问的文件地址，并加上瘦身的参数
     *
     * @param fileName
     * @return
     */
    String mergeUrlAndSlim(String fileName);

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url
     * @return
     * @throws Exception
     */
    String uploadByUrl(String url) throws Exception;

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url      网络URL
     * @param fileName 指定保存的文件名
     * @return
     * @throws Exception
     */
    String uploadByUrl(String url, String fileName) throws Exception;

    /**
     * 上传到七牛对象存储，以文件内容的hash值作为文件名
     *
     * @param is
     * @return 返回上传后的文件名
     * @throws Exception
     */
    String uploadByStream(InputStream is) throws Exception;

    /**
     *  上传到七牛对象存储，指定上传后的文件名
     *
     * @param is        数据流
     * @param fileName 上传后的文件名，默认不指定的情况下，以文件内容的hash值作为文件名
     * @return 返回上传后的文件名
     * @throws Exception
     */
    String uploadByStream(InputStream is,String fileName) throws Exception;

    /**
     * 获取本地的文件，上传到七牛
     *
     * @param localPath
     * @return
     */
    String uploadByLocal(String localPath);

    /**
     * 获取本地的文件，上传到七牛
     *
     * @param localPath 本地文件名
     * @param fileName  保存到七牛的文件名
     * @return 返回上传后的文件名
     */
    String uploadByLocal(String localPath, String fileName);

    /**
     * 七牛图片审核
     * @param uri
     * @return
     */
    boolean imageCensor(String uri);

}
