package com.erban.admin.main.service;


import com.erban.main.model.McoinDrawIssue;
import com.erban.main.model.McoinDrawPrize;
import com.erban.main.model.dto.McoinDrawIssueDTO;
import com.erban.main.mybatismapper.McoinDrawIssueMapper;
import com.erban.main.mybatismapper.McoinDrawPrizeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class McoinAdminService {

    private static final Logger log = LoggerFactory.getLogger(McoinAdminService.class);

    @Autowired
    private McoinDrawIssueMapper mcoinDrawIssueMapper;

    @Autowired
    private McoinDrawPrizeMapper mcoinDrawPrizeMapper;

    @Autowired
    private JedisService jedisService;

    public PageInfo<McoinDrawIssueDTO> getList(int pageNumber, int pageSize, int itemType, int issueStatus) {
        PageHelper.startPage(pageNumber, pageSize);
        List<McoinDrawIssueDTO> mcoinDrawIssues = mcoinDrawIssueMapper.findMcoinDrawIssue(itemType, issueStatus);

        mcoinDrawIssues.stream().forEach(item -> {
            McoinDrawPrize mcoinDrawPrize = mcoinDrawPrizeMapper.selectByPrimaryKey(item.getId());
            if (mcoinDrawPrize != null) {
                if (item.getIssueStatus() == 3) {
                    item.setStatus(4);
                    item.setUid(mcoinDrawPrize.getUid());
                } else {
                    item.setUid(mcoinDrawPrize.getUid());
                    item.setStatus(2);
                }
            } else {
                if (item.getIssueStatus() == 1) {
                    item.setStatus(3);
                } else if (item.getIssueStatus() == 2) {
                    item.setStatus(1);
                }
            }
        });
        return new PageInfo<>(mcoinDrawIssues);
    }


    public int saveMcoinDrawIssue(McoinDrawIssue mcoinDrawIssue, String startTimeString) {
        mcoinDrawIssue.setCreateTime(new Date());
        mcoinDrawIssue.setUpdateTime(new Date());
        mcoinDrawIssue.setStartTime(DateTimeUtil.convertStrToDate(startTimeString));
        mcoinDrawIssue.setPrice(20);
        int status = mcoinDrawIssueMapper.insert(mcoinDrawIssue);
        if (status > 0) {
            jedisService.del(RedisKey.mcoin_draw_issue_list_string.getKey());
        }
        return status;
    }

    public McoinDrawIssue getOne(Integer itemId) {
        return mcoinDrawIssueMapper.selectByPrimaryKey(Long.valueOf(itemId));
    }

    public int updateMcoinDrawIssue(McoinDrawIssue mcoinDrawIssue) {
        int status = 0;
        status = mcoinDrawIssueMapper.updateByPrimaryKeySelective(mcoinDrawIssue);
        if (status > 0) {
            jedisService.del(RedisKey.mcoin_draw_issue_list_string.getKey());
            jedisService.del(RedisKey.mcoin_draw_issue_string.getKey(mcoinDrawIssue.getId().toString()));
        }
        return status;
    }
}
