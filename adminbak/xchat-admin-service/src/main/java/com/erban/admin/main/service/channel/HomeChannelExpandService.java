package com.erban.admin.main.service.channel;

import com.erban.admin.main.common.BusinessException;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.HomeChannelGroupMapper;
import com.erban.main.mybatismapper.HomeChannelMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.service.room
 * @date 2018/8/16
 * @time 18:39
 */
@Service
public class HomeChannelExpandService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HomeChannelMapper homeChannelMapper;
    @Autowired
    private HomeChannelGroupMapper homeChannelGroupMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JedisService jedisService;
    @Value("${outputLink}")
    private String outputLink;
    @Value("${statFlowSecret}")
    private String statFlowSecret;

    public PageInfo<HomeChannel> getListWithPage(String channel, Integer groupId, int pageNumber, int pageSize) throws Exception{
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        HomeChannel channel1 = new HomeChannel();
        channel1.setChannel(channel);
        channel1.setGroupId(groupId);
        List<HomeChannel> list = homeChannelMapper.listChannel(channel1);
        if (list != null && list.size() > 0) {
            for (HomeChannel homeChannel : list) {
                String channelId = DESUtils.DESAndBase64Encrypt(homeChannel.getChannel(), statFlowSecret);
                String groupIdStr = DESUtils.DESAndBase64Encrypt(homeChannel.getGroupId().toString(), statFlowSecret);
                String outputLinks = "<a target='_blank' href='"+ outputLink + channelId +"&groupId="+ groupIdStr +"'>"+outputLink + channelId +"&groupId="+ groupIdStr +"</a>";
                homeChannel.setOutputLink(outputLinks);
                homeChannel.setChannelUrl("https://www.47huyu.cn/mm/download/download.html?shareUid=" + homeChannel.getChannel());
            }
        }
        return new PageInfo<>(list);
    }

    public int save(HomeChannel channel, boolean isEdit) {
        int status = 1;
        Gson gson = new Gson();
        if (isEdit) {
            HomeChannel temp = getOne(channel.getId());
            jedisService.hdel(RedisKey.home_channel.getKey(), temp.getChannel() + "_" + temp.getIsNews());
            channel.setChannel(channel.getChannel());
            channel.setHomeDefault(channel.getHomeDefault());
            channel.setIsNews(channel.getIsNews());
            channel.setCreateTime(new Date());
            status = homeChannelMapper.updateByPrimaryKey(channel);
            if (status > 0) {

                jedisService.hset(RedisKey.home_channel.getKey(), channel.getChannel() + "_" + channel.getIsNews(), gson.toJson(channel));
            }
        } else {
            HomeChannelExample example = new HomeChannelExample();
            example.createCriteria().andChannelEqualTo(channel.getChannel());
            List<HomeChannel> list = homeChannelMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                throw new BusinessException("渠道名称已经存在");
            }
            if (StringUtils.isNumeric(channel.getChannel()) && Long.valueOf(channel.getChannel()) > 1000000) {
                // uid 最小值为 1000000
                throw new BusinessException("请输入小于 1000000 的数字");
            }
            channel.setChannel(channel.getChannel());
            channel.setHomeDefault(channel.getHomeDefault());
            channel.setIsNews(channel.getIsNews());
            channel.setCreateTime(new Date());
            status = homeChannelMapper.insert(channel);
            if (status > 0) {
                jedisService.hset(RedisKey.home_channel.getKey(), channel.getChannel() + "_" + channel.getIsNews(), gson.toJson(channel));
            }
        }
        return status;
    }

    public HomeChannel getOne(Long id) {
        return homeChannelMapper.selectByPrimaryKey(id);
    }

    public int delete(Long id) {
        HomeChannel channel = getOne(id);
        int status = homeChannelMapper.deleteByPrimaryKey(id);
        if (status > 0) {
            jedisService.hdel(RedisKey.home_channel.getKey(), channel.getChannel() + "_" + channel.getIsNews());
        }
        return status;
    }

    public List<HomeChannel> channelList() {
        return jdbcTemplate.query("SELECT * from home_channel GROUP BY channel", new BeanPropertyRowMapper<>(HomeChannel.class));
    }

    public List<HomeChannelGroup> groupList() {
        return jdbcTemplate.query("SELECT * from home_channel_group", new BeanPropertyRowMapper<>(HomeChannelGroup.class));
    }

    /**
     * 添加分组
     * @param name
     * @return
     */
    public int saveGroup(String name) {
        HomeChannelGroupExample example = new HomeChannelGroupExample();
        example.createCriteria().andGroupNameEqualTo(name);
        List<HomeChannelGroup> list = homeChannelGroupMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            throw new BusinessException("分组名称已存在");
        }
        HomeChannelGroup group = new HomeChannelGroup();
        group.setGroupName(name);
        group.setCreateTime(new Date());
        return homeChannelGroupMapper.insert(group);
    }

}
