package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.user.domain.UserBoxDrawRecordDO;
import com.juxiao.xchat.service.api.user.vo.*;

import java.util.List;

/**
 * @class: UserDrawService.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public interface UserDrawService {

    /**
     * 获取用户抽奖信息
     *
     * @param uid
     * @return
     */
    UserDrawVO getUserDraw(Long uid) throws WebServiceException;

    /**
     * 发起一次抽奖
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    UserDrawResultVO draw(Long uid) throws WebServiceException;

    /**
     * 获取已经中奖的记录20条，用于滚屏
     *
     * @return
     */
    List<UserDrawWinRecordVO> listUserDrawWinRecord();

    void insertBillRecord(Long uid, String objId, BillRecordType objType, Integer goldNum);

    /**
     * 执行抽礼盒逻辑
     *
     * @param uid 用户ID
     * @return 成功返回抽中的字体
     */
    BoxDrawVO doBoxDraw(Long uid) throws WebServiceException;

    /**
     * 查询指定用户礼盒纪录
     *
     * @param uid 用户ID
     * @return 成功返回抽中的字体
     */
    List<UserBoxDrawRecordDO>  listBoxDrawRecord(Long uid);

    UserBoxVO getUserBox(Long uid) throws WebServiceException;
}