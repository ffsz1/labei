package com.tongdaxing.xchat_core.file;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.io.File;

/**
 * Created by zhouxiangfeng on 2017/5/16.
 */

public interface IFileCore extends IBaseCore{

    void upload(File file);

    void uploadPhoto(File file);

    void download();
}
