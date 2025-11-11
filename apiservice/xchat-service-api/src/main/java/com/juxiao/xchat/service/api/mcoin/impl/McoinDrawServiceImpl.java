package com.juxiao.xchat.service.api.mcoin.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.BillMcoinDrawDao;
import com.juxiao.xchat.dao.mcoin.McoinDrawIssueDao;
import com.juxiao.xchat.dao.mcoin.McoinDrawTicketDao;
import com.juxiao.xchat.dao.mcoin.dto.*;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.mcoin.McoinDrawManager;
import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawRecordSaveDTO;
import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawWiningDTO;
import com.juxiao.xchat.manager.common.mcoin.impl.IssueItemTypeEnum;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.mcoin.McoinDrawService;
import com.juxiao.xchat.service.api.mcoin.vo.McoinDrawIssueDetailVO;
import com.juxiao.xchat.service.api.mcoin.vo.McoinDrawResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 萌币抽奖服务
 */
@Service
public class McoinDrawServiceImpl implements McoinDrawService {
    private final int DEFAULT_PAGE_SIZE = 20;
    private final int DRAW_NUM_MAX = 100;
    @Autowired
    private Gson gson;
    @Autowired
    private BillMcoinDrawDao billMcoinDrawDao;
    @Autowired
    private McoinDrawIssueDao mcoinDrawIssueDao;
    @Autowired
    private McoinDrawTicketDao mcoinDrawTicketDao;
    @Autowired
    private McoinDrawManager mcoinDrawManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;

    @Override
    public Long saveUserMcoinDrawTicket(Long uid, Long issueId, int drawNum) throws WebServiceException {
        if (uid == null || issueId == null || drawNum <= 0 || drawNum > DRAW_NUM_MAX) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        McoinDrawIssueDTO issueDto = mcoinDrawManager.getMcoinDrawIssue(issueId);
        if (issueDto == null || issueDto.getStartTime() == null) {
            throw new WebServiceException(WebServiceCode.MCOIN_DRAW_ISSUE_NOT_EXITS);
        }

        if (issueDto.getIssueStatus() == null || issueDto.getIssueStatus() != McoinDrawConstant.ISSUE_STATUS_EFFECT) {
            throw new WebServiceException(WebServiceCode.MCOIN_DRAW_ISSUE_UNEFFECT);
        }

        if (issueDto.getStartTime() == null || System.currentTimeMillis() < issueDto.getStartTime().getTime()) {
            throw new WebServiceException(WebServiceCode.MCOIN_DRAW_ISSUE_NOT_START);
        }

        String redisKey = RedisKey.mcoin_draw_lock.getKey(String.valueOf(issueId));
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            McoinDrawRecordSaveDTO recordDto = mcoinDrawManager.saveUserMcoinDrawTicket(uid, drawNum, issueDto);
            // 这里进行开奖
            if (recordDto.getDrawCount() >= issueDto.getDrawNum()) {
                mcoinDrawManager.issueDraw(issueId);
            }
            return recordDto.getRecordId();
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }


    }

    @Override
    public List<McoinDrawIssuesDTO> listIssues() {
        String issuesArray = redisManager.get(RedisKey.mcoin_draw_issue_list_string.getKey());
        if (StringUtils.isNotBlank(issuesArray)) {
            try {
                List<McoinDrawIssuesDTO> list =  gson.fromJson(issuesArray, new TypeToken<List<McoinDrawIssuesDTO>>() {
                }.getType());
                list.forEach(issuesDto -> {
                    int ticketCount = mcoinDrawManager.countMcoinDrawTickets(issuesDto.getIssueId());
                    issuesDto.setDrawCount(ticketCount);
                });
                return list;
            } catch (Exception e) {
                redisManager.del(RedisKey.mcoin_draw_issue_list_string.getKey());
            }
        }
        List<McoinDrawIssuesDTO> list = mcoinDrawIssueDao.listIssues();
        if (list == null || list.size() == 0) {
            return list;
        }

        redisManager.set(RedisKey.mcoin_draw_issue_list_string.getKey(), gson.toJson(list));
        list.forEach(issuesDto -> {
            int ticketCount = mcoinDrawManager.countMcoinDrawTickets(issuesDto.getIssueId());
            issuesDto.setDrawCount(ticketCount);
        });
        return list;
    }

    @Override
    public McoinDrawIssueDetailVO getIssueDetail(Long issueId) throws WebServiceException {
        if (issueId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        McoinDrawIssueDTO issueDto = mcoinDrawManager.getMcoinDrawIssue(issueId);
        if (issueDto == null) {
            throw new WebServiceException(WebServiceCode.MCOIN_DRAW_ISSUE_NOT_EXITS);
        }

        McoinDrawIssueDetailVO detailVo = new McoinDrawIssueDetailVO();
        detailVo.setIssueId(issueDto.getId());
        detailVo.setIssueUrl(issueDto.getIssueUrl());
        detailVo.setIssueStatus(issueDto.getIssueStatus());
        detailVo.setItemTypeName(issueDto.getItemType());
        detailVo.setItemName(issueDto.getItemName());
        detailVo.setPrice(issueDto.getPrice());
        detailVo.setLeftCount(issueDto.getLeftCount());
        detailVo.setTotalCount(issueDto.getDrawNum());
//         detailVo.setInvolvedUsers(this.listIssueRecords(issueId, 1));
        return detailVo;
    }

    @Override
    public List<McoinDrawInvolvedUserDTO> listIssueRecords(Long issueId, Integer pageNum) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }

        int start = (pageNum - 1) * DEFAULT_PAGE_SIZE;
        List<McoinDrawInvolvedUserDTO> involvedUsers = billMcoinDrawDao.listDrawUsers(issueId, start, DEFAULT_PAGE_SIZE);
        if (involvedUsers == null) {
            return null;
        }

        UsersDTO usersDto;
        for (McoinDrawInvolvedUserDTO involvedUserDto : involvedUsers) {
            usersDto = usersManager.getUser(involvedUserDto.getUid());
            if (usersDto == null) {
                continue;
            }
            involvedUserDto.setAvatar(usersDto.getAvatar());
            involvedUserDto.setNick(usersDto.getNick());
        }

        return involvedUsers;
    }

    @Override
    public List<McoinUserDrawRecordDTO> listInvolveRecords(Long uid, Byte type, Integer pageNum) throws WebServiceException {
        if (uid == null || type == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }

        int start = (pageNum - 1) * DEFAULT_PAGE_SIZE;


        //类型：1，进行中；2，已开奖；3，已中奖
        List<McoinUserDrawRecordDTO> list;
        switch (type) {
            case 1:
                list = billMcoinDrawDao.listUserDrawingRecords(uid, start, DEFAULT_PAGE_SIZE);
                break;
            case 2:
                list = billMcoinDrawDao.listUserDrawedRecords(uid, start, DEFAULT_PAGE_SIZE);
                break;
            case 3:
                list = billMcoinDrawDao.listUserPrizeRecords(uid, start, DEFAULT_PAGE_SIZE);
                break;
            default:
                throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (list == null || list.size() == 0) {
            return list;
        }

        McoinDrawIssueDTO issuesDto;
        for (McoinUserDrawRecordDTO recordDto : list) {
            if (recordDto == null || recordDto.getIssueId() == null) {
                continue;
            }
            issuesDto = mcoinDrawManager.getMcoinDrawIssue(recordDto.getIssueId());
            if (issuesDto == null) {
                continue;
            }
            recordDto.setIssueUrl(issuesDto.getIssueUrl());
            recordDto.setItemType(issuesDto.getItemType());
            recordDto.setItemName(issuesDto.getItemName());
            recordDto.setDrawCount(issuesDto.getDrawNum());
            recordDto.setLeftCount(issuesDto.getLeftCount());
            recordDto.setUpdateTime(issuesDto.getUpdateTime());
        }
        list.sort((a, b) -> (int) (b.getIssueId() - a.getIssueId()));
        redisManager.hdel(RedisKey.mcoin_draw_user_issue_hash.getKey(), String.valueOf(uid));
        return list;
    }

    @Override
    public McoinDrawResultVO getDrawResult(Long uid, Long recordId) throws WebServiceException {
        if (recordId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        BillMcoinDrawDTO drawDto = billMcoinDrawDao.getBillMcoinDraw(recordId);
        if (drawDto == null || drawDto.getIssueId() == null) {
            return null;
        }

        McoinDrawIssueDTO issueDto = mcoinDrawManager.getMcoinDrawIssue(drawDto.getIssueId());
        if (issueDto == null) {
            return null;
        }

        McoinDrawResultVO resultVo = new McoinDrawResultVO();
        McoinDrawWiningDTO winingDto = mcoinDrawManager.getMcoinDrawWining(issueDto.getId());
        if (winingDto != null) {
            winingDto.setDrawCount(drawDto.getDrawCount());
            resultVo.setDrawTime(winingDto.getPrizeCreateTime());
            resultVo.setWinUser(winingDto);
        }

        IssueItemTypeEnum itemType = IssueItemTypeEnum.itemTypeOf(issueDto.getItemType());
        resultVo.setItemTypeName(itemType != null ? itemType.getItemTypeName() : "");
        resultVo.setRecordId(recordId);
        resultVo.setIssueId(drawDto.getIssueId());
        resultVo.setIssueUrl(issueDto.getIssueUrl());

        resultVo.setItemName(issueDto.getItemName());
        resultVo.setCreateTime(drawDto.getCreateTime());
        return resultVo;
    }

    @Override
    public List<String> listRecordTickets(Long uid, Long recordId) throws WebServiceException {
        if (uid == null || recordId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        // TODO:写缓存
        return mcoinDrawTicketDao.listUserRecordTickets(uid, recordId);
    }

    @Override
    public int getMcoinMessageCount(Long uid) {
        String countStr = redisManager.hget(RedisKey.mcoin_draw_user_issue_hash.getKey(), String.valueOf(uid));
        if (StringUtils.isBlank(countStr)) {
            return 0;
        }
        if (!StringUtils.isNumeric(countStr)) {
            redisManager.hdel(RedisKey.mcoin_draw_user_issue_hash.getKey(), String.valueOf(uid));
            return 0;
        }
        int missionCount;
        try {
            missionCount = Integer.parseInt(countStr);
        } catch (Exception e) {
            redisManager.hdel(RedisKey.mcoin_draw_user_issue_hash.getKey(), String.valueOf(uid));
            return 0;
        }
        if (missionCount <= 0) {
            redisManager.hdel(RedisKey.mcoin_draw_user_issue_hash.getKey(), String.valueOf(uid));
        }
        return missionCount;
    }

    @Override
    public List<McoinUserHistoryRecordDTO> listIssueHistroyRecords(Integer pageNum) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }

        int start = (pageNum - 1) * DEFAULT_PAGE_SIZE;
        List<McoinUserHistoryRecordDTO> list = billMcoinDrawDao.listPrizeHistroyRecords(start, DEFAULT_PAGE_SIZE);
        if (list != null && list.size() > 0) {
            list.forEach(recordDto -> {
                UsersDTO usersDto = usersManager.getUser(recordDto.getUid());
                if (usersDto == null) {
                    return;
                }
                recordDto.setNick(usersDto.getNick());
                recordDto.setAvatar(usersDto.getAvatar());
                recordDto.setUid(null);
            });
        }
        return list;
    }
}
