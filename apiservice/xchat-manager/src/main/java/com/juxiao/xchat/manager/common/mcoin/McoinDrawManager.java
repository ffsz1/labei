package com.juxiao.xchat.manager.common.mcoin;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssueDTO;
import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawRecordSaveDTO;
import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawWiningDTO;

/**
 *
 */
public interface McoinDrawManager {


    /**
     * 保存用户购买号码
     *
     * @param uid
     * @param drawNum
     * @param issueDto
     * @return
     * @throws WebServiceException
     */
    McoinDrawRecordSaveDTO saveUserMcoinDrawTicket(Long uid, int drawNum, McoinDrawIssueDTO issueDto) throws WebServiceException;

    /**
     * 开奖
     *
     * @param issueId
     */
    void issueDraw(Long issueId);

    /**
     * 获取某一期的数据
     *
     * @param issueId
     * @return
     */
    McoinDrawIssueDTO getMcoinDrawIssue(Long issueId);

    /**
     * 查询改期抽奖中奖用户
     *
     * @param issueId
     * @return
     */
    McoinDrawWiningDTO getMcoinDrawWining(Long issueId);

    /**
     *
     * @param issueId
     * @return
     */
    int countMcoinDrawTickets(Long issueId);

    void pushMsg(Byte itemType);
}
