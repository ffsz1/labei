package com.juxiao.xchat.service.api.mcoin;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawInvolvedUserDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssuesDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserDrawRecordDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserHistoryRecordDTO;
import com.juxiao.xchat.service.api.mcoin.vo.McoinDrawIssueDetailVO;
import com.juxiao.xchat.service.api.mcoin.vo.McoinDrawResultVO;

import java.util.List;

/**
 * 萌币抽奖服务
 */
public interface McoinDrawService {

    /**
     * 1.4 参与萌币抽奖
     *
     * @param uid
     * @param issueId
     * @param drawNum
     * @throws WebServiceException
     */
    Long saveUserMcoinDrawTicket(Long uid, Long issueId, int drawNum) throws WebServiceException;

    /**
     * 1.1 萌币有效抽奖期列表
     *
     * @return
     */
    List<McoinDrawIssuesDTO> listIssues();

    /**
     * 1.2 查询萌币抽奖期号详细信息
     *
     * @param issueId
     * @return
     * @throws WebServiceException
     */
    McoinDrawIssueDetailVO getIssueDetail(Long issueId) throws WebServiceException;

    /**
     * 1.3 本期参与记录列表
     *
     * @param issueId
     * @param pageNum
     * @return
     */
    List<McoinDrawInvolvedUserDTO> listIssueRecords(Long issueId, Integer pageNum);

    /**
     * 1.5 用户参与记录列表
     *
     * @param uid
     * @param type
     * @param pageNum
     * @return
     * @throws WebServiceException
     */
    List<McoinUserDrawRecordDTO> listInvolveRecords(Long uid, Byte type, Integer pageNum) throws WebServiceException;


    /**
     * 1.6 订单详情接口
     *
     * @param uid
     * @param recordId
     * @return
     */
    McoinDrawResultVO getDrawResult(Long uid, Long recordId) throws WebServiceException;

    /**
     * 1.7 查询参与记录中的关联抽奖号码
     *
     * @param uid
     * @param recordId
     * @return
     * @throws WebServiceException
     */
    List<String> listRecordTickets(Long uid, Long recordId) throws WebServiceException;

    /**
     * 获取小红点
     * @param uid
     * @return
     */
    int getMcoinMessageCount(Long uid);

    /**
     *
     * @param pageNum
     * @return
     */
    List<McoinUserHistoryRecordDTO> listIssueHistroyRecords(Integer pageNum);
}
