package com.juxiao.xchat.manager.common.mcoin.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DataUtils;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.BillMcoinDrawDao;
import com.juxiao.xchat.dao.mcoin.McoinDrawIssueDao;
import com.juxiao.xchat.dao.mcoin.McoinDrawPrizeDao;
import com.juxiao.xchat.dao.mcoin.McoinDrawTicketDao;
import com.juxiao.xchat.dao.mcoin.domain.BillMcoinDrawDO;
import com.juxiao.xchat.dao.mcoin.domain.McoinDrawPrizeDO;
import com.juxiao.xchat.dao.mcoin.domain.McoinDrawTicketDO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawConstant;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawIssueDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawPrizeDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinDrawTicketDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.mcoin.McoinDrawManager;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawRecordSaveDTO;
import com.juxiao.xchat.manager.common.mcoin.dto.McoinDrawWiningDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Slf4j
@Service
public class McoinDrawManagerImpl implements McoinDrawManager {
    @Autowired
    private Gson gson;

    @Autowired
    private BillMcoinDrawDao billMcoinDrawDao;
    @Autowired
    private McoinDrawIssueDao mcoinDrawIssueDao;
    @Autowired
    private McoinDrawPrizeDao mcoinDrawPrizeDao;
    @Autowired
    private McoinDrawTicketDao mcoinDrawTicketDao;
    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;

    @Override
    public McoinDrawRecordSaveDTO saveUserMcoinDrawTicket(Long uid, int drawNum, McoinDrawIssueDTO issueDto) throws WebServiceException {
        // 写成manager，从缓存中读取
        int ticketCount = mcoinDrawTicketDao.countIssueTickets(issueDto.getId());
        if (ticketCount + drawNum > issueDto.getDrawNum()) {
            throw new WebServiceException(WebServiceCode.MCOIN_DRAW_TICKET_NOT_ENOUGH);
        }

        int mcoinCost = drawNum * issueDto.getPrice();
        mcoinManager.updateReduceMcoin(uid, mcoinCost);

        //先对数据保存到 bill_mcoin_draw 表中，分配好recordId之后；
        Date now = new Date();
        BillMcoinDrawDO recordDo = new BillMcoinDrawDO(null, uid, issueDto.getId(), drawNum, mcoinCost, now);
        billMcoinDrawDao.save(recordDo);

        // 保存 mcoin_draw_ticket 表，保存完成之后，生成 ticketNo（按照主键最后三位插入3位随机数），再次保存数据库；
        McoinDrawTicketDO ticketDo;
        for (int i = 0; i < drawNum; i++) {
            ticketDo = new McoinDrawTicketDO(null, null, issueDto.getId(), recordDo.getId(), uid, now, now);
            mcoinDrawTicketDao.save(ticketDo);

            ticketDo.setTicketNo(DataUtils.randomNmberById(String.valueOf(ticketDo.getId())));
            mcoinDrawTicketDao.updateTicketNo(ticketDo);

            // 把 mcoin_draw_ticket 的主键写入 mcoin_draw_ticket_zset_{issueId}缓存中。
            redisManager.zadd(RedisKey.mcoin_draw_ticket_zset.getKey(String.valueOf(issueDto.getId())), now.getTime(), gson.toJson(ticketDo));
        }

        issueDto.setLeftCount(issueDto.getDrawNum() - (ticketCount + drawNum));
        redisManager.set(RedisKey.mcoin_draw_issue_string.getKey(String.valueOf(issueDto.getId())), gson.toJson(issueDto));
        return new McoinDrawRecordSaveDTO(recordDo.getId(), ticketCount + drawNum);
    }

    @Async
    @Override
    public void issueDraw(Long issueId) {
        log.warn("[ 萌币抽奖 ] 开始开奖，issueId:>{}", issueId);
        McoinDrawIssueDTO issueDto = this.getMcoinDrawIssue(issueId);
        if (issueDto == null || issueDto.getDrawNum() == null) {
            log.warn("[ 萌币抽奖 ] 开奖错误，期号不存在，issueId:>{}", issueId);
            return;
        }

        String redisKey = RedisKey.mcoin_draw_ticket_zset.getKey(String.valueOf(issueDto.getId()));
        long ticketCount = redisManager.zcount(redisKey, 0, System.currentTimeMillis() + 2000);
        if (ticketCount < issueDto.getDrawNum()) {
            log.warn("[ 萌币抽奖 ] 开奖条件不满足，已有数量:>{}，开奖数量:>{}", ticketCount, issueDto.getDrawNum());
            return;
        }

        Set<String> tickets = redisManager.zrangeByScore(redisKey, 0, System.currentTimeMillis() + 10000);
        if (tickets == null || tickets.size() == 0) {
            log.warn("[ 萌币抽奖 ] 开奖失败，查询不到票据信息。");
            return;
        }

        McoinDrawTicketDTO ticketDto = this.getWinningTicket(tickets);
        if (ticketDto == null || ticketDto.getUid() == null) {
            log.warn("[ 萌币抽奖 ] 开奖失败，随机票据为空。");
            return;
        }
        // 保存中奖用户信息 mcoin_draw_prize
        Date now = new Date();
        mcoinDrawPrizeDao.save(new McoinDrawPrizeDO(issueId, ticketDto.getUid(), ticketDto.getId(), now));

        // 更改 mcoin_draw_issue 状态
        mcoinDrawIssueDao.updateIssueStatus(issueId, McoinDrawConstant.ISSUE_STATUS_FINISH);

        // 清理缓存
        redisManager.del(RedisKey.mcoin_draw_issue_string.getKey(String.valueOf(issueId)));
        redisManager.del(RedisKey.mcoin_draw_issue_list_string.getKey());
        redisManager.del(redisKey);

        // 发送奖励
        IssueItemTypeEnum itemTypeEnum = IssueItemTypeEnum.itemTypeOf(issueDto.getItemType());
        if (itemTypeEnum == null) {
            return;
        }
        itemTypeEnum.sendRewards(issueDto, ticketDto.getUid());
    }

    @Override
    public McoinDrawIssueDTO getMcoinDrawIssue(Long issueId) {
        String redisKey = RedisKey.mcoin_draw_issue_string.getKey(String.valueOf(issueId));
        String json = redisManager.get(redisKey);
        if (StringUtils.isNotBlank(json)) {
            try {
                return gson.fromJson(json, McoinDrawIssueDTO.class);
            } catch (Exception e) {
                redisManager.del(redisKey);
            }
        }

        McoinDrawIssueDTO issueDto = mcoinDrawIssueDao.getMcoinDrawIssue(issueId);
        if (issueDto != null) {
            int ticketCount = mcoinDrawTicketDao.countIssueTickets(issueId);
            int leftCount = issueDto.getDrawNum() - ticketCount;
            issueDto.setLeftCount(leftCount);
            redisManager.set(redisKey, gson.toJson(issueDto));
        }
        return issueDto;
    }

    @Override
    public McoinDrawWiningDTO getMcoinDrawWining(Long issueId) {
        if (issueId == null) {
            return null;
        }
        McoinDrawPrizeDTO prizeDto = mcoinDrawPrizeDao.getMcoinDrawPrize(issueId);
        if (prizeDto == null || prizeDto.getUid() == null) {
            return null;
        }

        UsersDTO usersDto = usersManager.getUser(prizeDto.getUid());
        if (usersDto == null) {
            return null;
        }

        String ticketNo = mcoinDrawTicketDao.getTicketNo(prizeDto.getTicketId());
        McoinDrawWiningDTO winingDto = new McoinDrawWiningDTO();
        winingDto.setUid(prizeDto.getUid());
        winingDto.setAvatar(usersDto.getAvatar());
        winingDto.setNick(usersDto.getNick());
        winingDto.setTicketNo(ticketNo);
        winingDto.setPrizeCreateTime(prizeDto.getCreateTime());
        return winingDto;
    }

    @Override
    public int countMcoinDrawTickets(Long issueId) {
        if (issueId == null) {
            return 0;
        }

        Long ticketCount = redisManager.zcount(RedisKey.mcoin_draw_ticket_zset.getKey(issueId.toString()), 0, System.currentTimeMillis());
        if (ticketCount != null) {
            return ticketCount.intValue();
        }

        return mcoinDrawTicketDao.countIssueTickets(issueId);
    }

    /**
     * 获取中奖的票据信息
     *
     * @param tickets
     * @return
     */
    private McoinDrawTicketDTO getWinningTicket(Set<String> tickets) {
        if (tickets == null || tickets.size() == 0) {
            return null;
        }

        if (tickets.size() == 1) {
            return gson.fromJson(new ArrayList<>(tickets).get(0), McoinDrawTicketDTO.class);
        }
        // 先随机生成一个用户，后期再按照算法进行修改
        int index = RandomUtils.randomRegionInteger(0, tickets.size());
        if (index >= tickets.size()){
            index = 0;
        }
        String ticketJson = new ArrayList<>(tickets).get(index);
        return gson.fromJson(ticketJson, McoinDrawTicketDTO.class);
    }

    /**
     * 竞猜开始发送全服
     * @param itemType
     */
    @Override
    public void pushMsg(Byte itemType){
        IssueItemTypeEnum itemTypeEnum = IssueItemTypeEnum.itemTypeOf(itemType);
        if (itemTypeEnum == null) {
            return;
        }
        itemTypeEnum.sendFullMsg(itemType);
    }
}
