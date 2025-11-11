package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.AccompanyManualDO;
import com.juxiao.xchat.dao.user.domain.AccompanyTypeDO;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/14
 * @time 16:08
 */
public interface AccompanyManualDao {


    int save(AccompanyManualDO accompanyManualDO);


    List<AccompanyTypeDO> selectAccompanyTypeByList();


}
