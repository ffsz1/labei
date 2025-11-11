package com.juxiao.xchat.manager.common.user.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.user.ExpressWallDAO;
import com.juxiao.xchat.dao.user.domain.ExpressWallDO;
import com.juxiao.xchat.dao.user.dto.ExpressWallDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;
import com.juxiao.xchat.manager.common.user.ExpressWallService;
import com.juxiao.xchat.manager.common.user.UsersManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 表白墙
 */
@Service
public class ExpressWallServiceImpl implements ExpressWallService {

    @Autowired
    private ExpressWallDAO expressWallDAO;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private GiftManager giftManager;
    @Autowired
    private Gson gson;

    @Override
    public void save(GiftMessage message) {
        if (message == null) {
            return;
        }
        ExpressWallDO expressWall = new ExpressWallDO();
        expressWall.setCreateTime(new Date());
        expressWall.setMessage(message.getExpressMessage());
        expressWall.setGiftId(message.getGiftId());
        expressWall.setReceiveUid(message.getRecvUid());
        expressWall.setSendUid(message.getSendUid());
        expressWall.setGiftNum(message.getGiftNum());
        expressWall.setTotalGold(message.getGoldNum());
        expressWallDAO.insert(expressWall);
        ExpressWallDTO dto = buildDTO(expressWall);
        // 新记录添加到队列头部, 需要定时清理这个队列
        redisManager.lpush(RedisKey.express_wall_list.getKey(), gson.toJson(dto));
        String result = redisManager.get(RedisKey.express_wall_top.getKey());
        if (StringUtils.isBlank(result)) {
            refreshTopCache(dto);
        } else {
            ExpressWallDTO cacheVo = gson.fromJson(result, ExpressWallDTO.class);
            if (message.getGoldNum() >= cacheVo.getGiftGold()) {
                refreshTopCache(dto);
            }
        }
    }

    /**
     * 刷新表白墙置顶的记录
     * @param vo 表白记录
     */
    private void refreshTopCache(ExpressWallDTO vo) {
        // 更新缓存中置顶的记录-- 设置过期时间
        redisManager.set(RedisKey.express_wall_top.getKey(), gson.toJson(vo), 24 * 60 * 60, TimeUnit.SECONDS);
    }

    public ExpressWallDTO buildDTO(ExpressWallDO ew){
        UsersDTO sendUser = usersManager.getUser(ew.getSendUid());
        UsersDTO receiveUser = usersManager.getUser(ew.getReceiveUid());
        GiftDTO gift = giftManager.getValidGiftById(ew.getGiftId());
        if (gift == null) {
            return null;
        }
        ExpressWallDTO dto = new ExpressWallDTO();
        dto.setExpressMessage(StringUtils.isEmpty(ew.getMessage()) ? "" : ew.getMessage());
        dto.setCreateTime(ew.getCreateTime());
        // vo.setDate(DateUtil.date2Str(ew.getCreateTime(), DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
        dto.setGiftId(ew.getGiftId());
        dto.setGiftNum(ew.getGiftNum());
        dto.setGiftGold(ew.getTotalGold());
        dto.setPicUrl(gift.getPicUrl());
        dto.setGiftName(gift.getGiftName());
        dto.setSendUid(sendUser.getUid());
        dto.setSendAvatar(sendUser.getAvatar());
        dto.setSendNick(sendUser.getNick());
        dto.setReceiveUid(receiveUser.getUid());
        dto.setReceiveAvatar(receiveUser.getAvatar());
        dto.setReceiveNick(receiveUser.getNick());
        return dto;
    }

    @Override
    public ExpressWallDTO getTop() {
        // 查询当天表白金额最高的记录--记录在缓存中;
        // 同样金额的话, 显示后面刷的这个人
        String result = redisManager.get(RedisKey.express_wall_top.getKey());
        ExpressWallDTO dto;
        if (StringUtils.isNotBlank(result)) {
            dto = gson.fromJson(result, ExpressWallDTO.class);
        } else {
            // 查询今天的置顶记录
            // 不查数据库, 没有就返回 null
            dto = null;
        }
        return dto;
    }

    @Override
    public List<ExpressWallDTO> findByPage(Integer pageNum, Integer pageSize) {
        // 根据时间排序查询记录
        // 考虑第一页做实时更新,
        // 将记录加入到缓存中, 定时更新
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 20 : pageSize;
        List<ExpressWallDTO> list = Lists.newArrayList();
        ExpressWallDTO vo;
        if (pageNum == 1) {
            vo = getTop();
            if(vo != null) {
                pageSize --;
                // 第一页, 第一条显示置顶的数据
                list.add(vo);
            }
        }
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        List<String> result = redisManager.range(RedisKey.express_wall_list.getKey(), start, end);
        // redis中有多少数据就展示多少条数据,
//        if (result.isEmpty()) {
//            List<ExpressWallVo> resultVo = expressWallMapper.findByPage((pageNum - 1) * pageSize, pageSize);
//            list.addAll(resultVo);
//            for (ExpressWallVo v : resultVo) {
//                // 缓存中没有该页的数据,添加到列表尾部, 列头为新数据
//                jedisService.rpush(RedisKey.express_wall_list.getKey(), gson.toJson(v));
//            }
//        }
        if (list == null) {
            return list;
        }
        result.forEach(str-> {
            list.add(gson.fromJson(str, ExpressWallDTO.class));
        });
        return list;
    }
}
