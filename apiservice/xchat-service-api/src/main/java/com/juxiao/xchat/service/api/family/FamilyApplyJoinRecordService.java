package com.juxiao.xchat.service.api.family;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.family.bo.FamilyApplyJoinParamBO;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family
 * @date 2018/8/30
 * @time 17:53
 */
public interface FamilyApplyJoinRecordService {

    /**
     * 保存申请记录
     * @param familyApplyJoinParamBO
     * @return
     */
    WebServiceMessage save(FamilyApplyJoinParamBO familyApplyJoinParamBO);


}
