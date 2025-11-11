package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.service.api.sysconf.enumeration.SensitiveWordEnum;

import java.util.List;

public interface SensitiveWordService {

    /**
     * 敏感词真正字符串
     * @return
     */
    String regex();

    /**
     * 敏感词列表
     * @return
     */
    List<String> list();

    /**
     * 敏感词列表
     * @return
     */
    List<String> list(SensitiveWordEnum type);

    /**
     * 获取敏感词
     * @return
     */
    String getWords(SensitiveWordEnum type);
}
