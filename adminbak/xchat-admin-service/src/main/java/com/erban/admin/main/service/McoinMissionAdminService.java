package com.erban.admin.main.service;

import com.erban.main.model.McoinMission;
import com.erban.main.model.McoinMissionExample;
import com.erban.main.mybatismapper.McoinMissionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class McoinMissionAdminService {

    private static final Logger log = LoggerFactory.getLogger(McoinMissionAdminService.class);

    @Autowired
    private McoinMissionMapper mcoinMissionMapper;

    @Autowired
    private JedisService jedisService;

    public PageInfo<McoinMission> getList(int pageNumber, int pageSize, Byte freebiesType, Byte missionType) {
        PageHelper.startPage(pageNumber, pageSize);
        McoinMissionExample example = new McoinMissionExample();
        McoinMissionExample.Criteria criteria = example.createCriteria();
        if (freebiesType != null) {
            criteria.andFreebiesTypeEqualTo(freebiesType);
        }
        if(missionType != null) {
            criteria.andMissionTypeEqualTo(missionType);
        }
        example.setOrderByClause("ID DESC");
        List<McoinMission> mcoinMissions = mcoinMissionMapper.selectByExample(example);
        return new PageInfo<>(mcoinMissions);
    }


    public int saveMcoinMission(McoinMission mcoinMission) {
        mcoinMission.setMissionScope((byte)2);
        int status = mcoinMissionMapper.insert(mcoinMission);
        return status;
    }

    public McoinMission getOne(Integer itemId) {
        return mcoinMissionMapper.selectByPrimaryKey(Integer.valueOf(itemId));
    }

    public int updateMcoinMission(McoinMission mcoinMission) {
        int status = 0;
        status = mcoinMissionMapper.updateByPrimaryKeySelective(mcoinMission);
        return status;
    }
}
