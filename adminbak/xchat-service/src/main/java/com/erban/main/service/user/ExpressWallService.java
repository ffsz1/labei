package com.erban.main.service.user;

import com.erban.main.model.ExpressWall;
import com.erban.main.model.Gift;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.ExpressWallMapper;
import com.erban.main.service.gift.GiftService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ExpressWallVo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 表白墙
 */
@Service
public class ExpressWallService {

    @Autowired
    private ExpressWallMapper expressWallMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private GiftService giftService;
    private Gson gson = new Gson();
    /**
     * 保存表白记录
     * @param sendUid 发送人
     * @param recvUid 接收人
     * @param giftId 礼物ID
     * @param giftNum 礼物数量
     * @param giftGold 礼物总金额
     * @param expressMessage 表白留言
     */
    public void save(Long sendUid, Long recvUid, Integer giftId, Integer giftNum, Long giftGold, String expressMessage) {
        ExpressWall expressWall = new ExpressWall();
        expressWall.setCreateTime(new Date());
        expressWall.setMessage(expressMessage);
        expressWall.setGiftId(giftId);
        expressWall.setReceiveUid(recvUid);
        expressWall.setSendUid(sendUid);
        expressWall.setGiftNum(giftNum);
        expressWall.setTotalGold(giftGold);
        expressWallMapper.insert(expressWall);
        ExpressWallVo vo = buildVo(expressWall);
        // 新记录添加到队列头部, 需要定时清理这个队列
        jedisService.lpush(RedisKey.express_wall_list.getKey(), gson.toJson(vo));
        String result = jedisService.get(RedisKey.express_wall_top.getKey());
        if (StringUtils.isBlank(result)) {
            refreshTopCache(vo);
        } else {
            ExpressWallVo cacheVo = gson.fromJson(result, ExpressWallVo.class);
            if (giftGold >= cacheVo.getGiftGold()) {
                refreshTopCache(vo);
            }
        }
    }

    /**
     * 刷新表白墙置顶的记录
     * @param vo 表白记录
     */
    private void refreshTopCache(ExpressWallVo vo) {
        // 更新缓存中置顶的记录-- 设置过期时间
        jedisService.set(RedisKey.express_wall_top.getKey(), gson.toJson(vo), 24 * 60 * 60 * 1000);
    }

    public ExpressWallVo buildVo(ExpressWall ew){
        Users sendUser = usersService.getUsersByUid(ew.getSendUid());
        Users receiveUser = usersService.getUsersByUid(ew.getReceiveUid());
        Gift gift = giftService.getGiftById(ew.getGiftId());
        ExpressWallVo vo = new ExpressWallVo();
        vo.setExpressMessage(StringUtils.isEmpty(ew.getMessage()) ? "" : ew.getMessage());
        vo.setCreateTime(ew.getCreateTime());
        // vo.setDate(DateUtil.date2Str(ew.getCreateTime(), DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
        vo.setGiftId(ew.getGiftId());
        vo.setGiftNum(ew.getGiftNum());
        vo.setGiftGold(ew.getTotalGold());
        vo.setPicUrl(gift.getPicUrl());
        vo.setGiftName(gift.getGiftName());
        vo.setSendUid(sendUser.getUid());
        vo.setSendAvatar(sendUser.getAvatar());
        vo.setSendNick(sendUser.getNick());
        vo.setReceiveUid(receiveUser.getUid());
        vo.setReceiveAvatar(receiveUser.getAvatar());
        vo.setReceiveNick(receiveUser.getNick());
        return vo;
    }

    /**
     * 查询- 置顶的记录
     * @return 缓存中没有的话,返回 null
     */
    public ExpressWallVo getTop() {
        // 查询当天表白金额最高的记录--记录在缓存中;
        // 同样金额的话, 显示后面刷的这个人
        String result = jedisService.get(RedisKey.express_wall_top.getKey());
        ExpressWallVo vo;
        if (StringUtils.isNotBlank(result)) {
            vo = gson.fromJson(result, ExpressWallVo.class);
        } else {
            // 查询今天的置顶记录
            // 不查数据库, 没有就返回 null
            vo = null;
        }
        return vo;
    }

    /**
     * 查询用户表白列表
     */
    public BusiResult findByPage(Integer pageNum, Integer pageSize) {
        // 根据时间排序查询记录
        // 考虑第一页做实时更新,
        // 将记录加入到缓存中, 定时更新
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 20 : pageSize;
        List<ExpressWallVo> list = Lists.newArrayList();
        ExpressWallVo vo;
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
        List<String> result = jedisService.lrange(RedisKey.express_wall_list.getKey(), start, end);
        // redis中有多少数据就展示多少条数据,
//        if (result.isEmpty()) {
//            List<ExpressWallVo> resultVo = expressWallMapper.findByPage((pageNum - 1) * pageSize, pageSize);
//            list.addAll(resultVo);
//            for (ExpressWallVo v : resultVo) {
//                // 缓存中没有该页的数据,添加到列表尾部, 列头为新数据
//                jedisService.rpush(RedisKey.express_wall_list.getKey(), gson.toJson(v));
//            }
//        }
        ExpressWallVo ewVo;
        for (String str : result) {
            ewVo = gson.fromJson(str, ExpressWallVo.class);
            list.add(ewVo);
        }
        return new BusiResult(BusiStatus.SUCCESS, list);
    }
}
