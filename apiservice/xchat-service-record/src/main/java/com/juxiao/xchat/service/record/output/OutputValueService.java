package com.juxiao.xchat.service.record.output;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.output.bo.OutputValueParam;

/**
 * @author chris
 * @Title:
 * @date 2018/10/17
 * @time 16:32
 */
public interface OutputValueService {

    WebServiceMessage getList(OutputValueParam outputValueParam);

    WebServiceMessage getChannelList(String groupId);

    /**
     * 查询注册用户
     * @return
     */
    WebServiceMessage listRegister(OutputValueParam param);

    /**
     * 查询注册用户
     * @return
     */
    WebServiceMessage listCharge(OutputValueParam param);
}
