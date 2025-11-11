package com.erban.main.service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.StatWeekListsMapper;
import com.erban.main.service.user.UsersService;
import com.xchat.common.utils.GetTimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.vo.WeekListVo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;

/**
 * yanghaoyu 查询周榜
 */
@Service
public class WeekListService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WeekListService.class);
    @Autowired
    private JedisService jedisService;
    @Autowired
    private StatWeekListsMapper statWeekListMapper;
    @Autowired
    private AuctionDealService auctionDealService;
    @Autowired
    private UsersService usersService;

    private Gson gson = new Gson();

    public List<WeekListVo> queryList(Long roomUid) {
        String str = jedisService.hget(RedisKey.week_list.getKey(), roomUid.toString());
        List<WeekListVo> weekLists = Lists.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            Type type = new TypeToken<List<WeekListVo>>() {
            }.getType();
            weekLists = gson.fromJson(str, type);
        } else {
            weekLists = getWeekList(roomUid);
            if (weekLists == null) {
                return null;
            }
            Collections.sort(weekLists);
            if (weekLists.size() > 5) {
                weekLists = weekLists.subList(0, 5);
            }
            String weekListsStr = gson.toJson(weekLists);
            jedisService.hwrite(RedisKey.week_list.getKey(), roomUid.toString(), weekListsStr);
        }
        if (CollectionUtils.isEmpty(weekLists)) {
            return null;
        }
        return weekLists;
    }

    /*public void updateWeekList(Long roomUid, String auctId) throws Exception {
        logger.info("正在生产周榜数据,roomUid:" + roomUid);
        AuctionDeal auctionDeal = auctionDealService.getAuctionDealById(auctId);
        logger.info("得到刚完成拍卖订单，auctId:" + auctId);
        List<WeekListVo> weekListVos = Lists.newArrayList();
        if (auctionDeal == null) {
            return;
        }
        String weekListStr = jedisService.hget(RedisKey.week_list.getKey(), roomUid.toString());
        if (weekListStr == null) {
            StatWeekList weekList = getWeekList(roomUid);
            if (weekList == null) {
                weekList = new StatWeekList();
                weekList.setRoomUid(roomUid);
                addWeekListVo(roomUid, auctionDeal, weekListVos);
                weekList.setJsonstr(gson.toJson(weekListVos));
                statWeekListMapper.insertSelective(weekList);
                jedisService.hwrite(RedisKey.week_list.getKey(), roomUid.toString(), weekList.getJsonstr());
                return;
            }
            Type type = new TypeToken<List<WeekListVo>>() {
            }.getType();
            weekListVos = gson.fromJson(weekList.getJsonstr(), type);
        } else {
            Type type = new TypeToken<List<WeekListVo>>() {
            }.getType();
            weekListVos = gson.fromJson(weekListStr, type);
        }
        StatWeekList weekList2 = new StatWeekList();
        if (weekListVos.size() < 5 || CollectionUtils.isEmpty(weekListVos)) {
            weekList2.setRoomUid(roomUid);
            addWeekListVo(roomUid, auctionDeal, weekListVos);
            weekList2.setJsonstr(gson.toJson(weekListVos));
            statWeekListMapper.updateByPrimaryKeySelective(weekList2);
        } else if (weekListVos.size() >= 5) {
            addWeekListVo(roomUid, auctionDeal, weekListVos);
            Collections.sort(weekListVos);
            weekListVos.remove(weekListVos.size() - 1);
            weekList2.setRoomUid(roomUid);
            weekList2.setJsonstr(gson.toJson(weekListVos));
            statWeekListMapper.updateByPrimaryKeySelective(weekList2);
        }
        jedisService.hwrite(RedisKey.week_list.getKey(), roomUid.toString(), gson.toJson(weekListVos));
    }*/

    public void updateWeekList(Long roomUid, String auctId) {
        logger.info("正在生产周榜数据,roomUid:" + roomUid);
        AuctionDeal auctionDeal = auctionDealService.getAuctionDealById(auctId);
        logger.info("得到刚完成拍卖订单，auctId:" + auctId);
        List<WeekListVo> weekListVos = Lists.newArrayList();
        if (auctionDeal == null) {
            return;
        }
        String weekListStr = jedisService.hget(RedisKey.week_list.getKey(), roomUid.toString());
        if (weekListStr == null) {
            weekListVos = getWeekList(roomUid);
            if (CollectionUtils.isEmpty(weekListVos)) {
                weekListVos = Lists.newArrayList();
                addWeekList(roomUid, auctionDeal, weekListVos);
                String weekListsStr = gson.toJson(weekListVos);
                jedisService.hwrite(RedisKey.week_list.getKey(), roomUid.toString(), weekListsStr);
                return;
            } else {
                String weekListsStr = gson.toJson(weekListVos);
                jedisService.hwrite(RedisKey.week_list.getKey(), roomUid.toString(), weekListsStr);
            }
        } else {
            Type type = new TypeToken<List<WeekListVo>>() {
            }.getType();
            weekListVos = gson.fromJson(weekListStr, type);
        }
        if (weekListVos.size() < 5 || CollectionUtils.isEmpty(weekListVos)) {
            addWeekList(roomUid, auctionDeal, weekListVos);
        } else if (weekListVos.size() >= 5) {
            addWeekList(roomUid, auctionDeal, weekListVos);
            Collections.sort(weekListVos);
            weekListVos.remove(weekListVos.size() - 1);
        }
        jedisService.hwrite(RedisKey.week_list.getKey(), roomUid.toString(), gson.toJson(weekListVos));
    }

    private void addWeekList(Long roomUid, AuctionDeal auctionDeal, List<WeekListVo> weekListVos) {
        StatWeekLists statWeekList = new StatWeekLists();
        statWeekList.setRoomUid(roomUid);
        statWeekList.setDealUid(auctionDeal.getDealUid());
        statWeekList.setPrice(auctionDeal.getDealMoney());
        statWeekList.setCreateTime(new Date());
        statWeekList.setProdId(auctionDeal.getAuctUid());
        Users users = usersService.getUsersByUid(auctionDeal.getDealUid());
        statWeekList.setAvatar(users.getAvatar());
        statWeekList.setGender(users.getGender());
        statWeekList.setNick(users.getNick());
        statWeekListMapper.insertSelective(statWeekList);
        ConverWeekListToVo(weekListVos, statWeekList);
    }

    private void ConverWeekListToVo(List<WeekListVo> weekListVos, StatWeekLists statWeekList) {
        WeekListVo weekListVo = new WeekListVo();
        weekListVo.setGender(statWeekList.getGender());
        weekListVo.setNick(statWeekList.getNick());
        weekListVo.setAvatar(statWeekList.getAvatar());
        weekListVo.setRoomUid(statWeekList.getRoomUid());
        weekListVo.setProdId(statWeekList.getProdId());
        weekListVo.setUid(statWeekList.getDealUid());
        weekListVo.setPrice(statWeekList.getPrice());
        weekListVo.setCreateTime(statWeekList.getCreateTime());
        weekListVos.add(weekListVo);
    }

    private List<WeekListVo> convertRoomListToVoList(List<StatWeekLists> statWeekLists) {
        List<WeekListVo> weekListVos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(statWeekLists)) {
            return weekListVos;
        }
        for (StatWeekLists statWeekList : statWeekLists) {
            WeekListVo weekListVo = convertStatWeekListToVo(statWeekList);
            weekListVos.add(weekListVo);
        }
        return weekListVos;
    }

    private WeekListVo convertStatWeekListToVo(StatWeekLists statWeekList) {
        WeekListVo weekListVo = new WeekListVo();
        weekListVo.setGender(statWeekList.getGender());
        weekListVo.setCreateTime(statWeekList.getCreateTime());
        weekListVo.setPrice(statWeekList.getPrice());
        weekListVo.setUid(statWeekList.getDealUid());
        weekListVo.setProdId(statWeekList.getProdId());
        weekListVo.setAvatar(statWeekList.getAvatar());
        weekListVo.setNick(statWeekList.getNick());
        weekListVo.setRoomUid(statWeekList.getRoomUid());
        return weekListVo;
    }


    private List<WeekListVo> getWeekList(Long roomUid) {
        StatWeekListsExample example = new StatWeekListsExample();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesWeekmorning(), GetTimeUtils.getTimesWeeknight()).andRoomUidEqualTo(roomUid);
        List<StatWeekLists> statWeekLists = statWeekListMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statWeekLists)) {
            logger.info("该房间没有周榜数据roomuid:" + roomUid);
            return null;
        }
        List<WeekListVo> weekListVos = convertRoomListToVoList(statWeekLists);
        return weekListVos;
    }

    private void addWeekListVo(Long roomUid, AuctionDeal auctionDeal, List<WeekListVo> weekListVos) throws Exception {
        WeekListVo weekListVo = new WeekListVo();
        weekListVo.setPrice(auctionDeal.getDealMoney());
        weekListVo.setProdId(auctionDeal.getAuctUid());
        weekListVo.setUid(auctionDeal.getDealUid());
        weekListVo.setRoomUid(roomUid);
        Users users = usersService.getUsersByUid(auctionDeal.getDealUid());
        weekListVo.setAvatar(users.getAvatar());
        weekListVo.setNick(users.getNick());
        weekListVo.setGender(users.getGender());
        weekListVo.setCreateTime(auctionDeal.getCreateTime());
        weekListVos.add(weekListVo);
    }

    public void deleteWeekList(Long roomUid) {
        if (roomUid == null) {
            logger.info("不存在的周榜数据id:" + roomUid);
            return;
        }
        statWeekListMapper.deleteByPrimaryKey(roomUid);
    }

    public WeekListVo packWeekList(AuctionDeal auctionDeal) {
        WeekListVo weekListVo = new WeekListVo();
        weekListVo.setCreateTime(auctionDeal.getCreateTime());
        weekListVo.setUid(auctionDeal.getDealUid());
        weekListVo.setProdId(auctionDeal.getAuctUid());
        weekListVo.setRoomUid(auctionDeal.getUid());
        return weekListVo;
    }
}
