package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.params.ImageCheckParams;
import com.juxiao.xchat.manager.external.netease.ret.CheckImageRet;

/**
 * @author chris
 * @date 2019-07-14
 */
public interface ImageCheckManager {

    /**
     * 图片反垃圾检测
     * @param imageCheckParams imageCheckParams
     * @return CheckImageRet
     * @throws Exception Exception
     */
    CheckImageRet checkImage(ImageCheckParams imageCheckParams)throws Exception;
}
