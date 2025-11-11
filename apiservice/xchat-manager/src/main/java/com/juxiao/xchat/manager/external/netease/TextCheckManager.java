package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.params.TextCheckParams;
import com.juxiao.xchat.manager.external.netease.ret.CheckRet;

/**
 * 调用易盾反垃圾云服务文本在线检测接口
 * @author chris
 * @date 2019-07-14
 */
public interface TextCheckManager {


    /**
     * 在线检测文本内容
     * @param params params
     * @return CheckRet
     * @throws  Exception Exception
     */
    CheckRet checkText(TextCheckParams params)throws Exception;

}
