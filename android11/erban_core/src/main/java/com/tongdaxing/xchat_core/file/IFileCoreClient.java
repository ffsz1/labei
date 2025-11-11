package com.tongdaxing.xchat_core.file;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by zhouxiangfeng on 2017/5/16.
 */

public interface IFileCoreClient extends ICoreClient {

    public static final String METHOD_ON_UPLOAD= "onUpload";
    public static final String METHOD_ON_UPLOAD_FAITH= "onUploadFail";
    public static final String METHOD_ON_UPLOAD_PHOTO= "onUploadPhoto";
    public static final String METHOD_ON_UPLOAD_PHOTO_FAITH= "onUploadPhotoFail";
    public static final String METHOD_ON_DOWNLOAD = "onDownload";
    public static final String METHOD_ON_DOWNLOAD_FAITH = "onDownloadFail";

    void onUpload(String url);

    void onUploadFial();

    void onUploadPhoto(String url);

    void onUploadPhotoFaith();

    void onDownload();

    void onDownloadFail();
}
