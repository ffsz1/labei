package com.erban.main.service.statis;

import com.erban.main.model.StatSendGiftSumList;
import com.erban.main.model.StatSendGiftSumListExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.StatSendGiftSumListMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.StatSendGiftSumListVo;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Service
public class StatSendGiftSumListService {
    @Autowired
    private StatSendGiftSumListMapper statSendGiftSumListMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JedisService jedisService;
    private Gson gson = new Gson();


    public BusiResult queryGiftLists(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String sendGiftListStr = jedisService.hget(RedisKey.send_gift_sumlist.getKey(), roomUid.toString());
        List<StatSendGiftSumListVo> statSendGiftSumListVos = Lists.newArrayList();
        if (sendGiftListStr != null) {
            Type type = new TypeToken<List<StatSendGiftSumListVo>>() {
            }.getType();
            statSendGiftSumListVos = gson.fromJson(sendGiftListStr, type);
        } else {
            StatSendGiftSumListExample example = new StatSendGiftSumListExample();
            example.createCriteria().andRoomUidEqualTo(roomUid);
            List<StatSendGiftSumList> statSendGiftSumLists = statSendGiftSumListMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(statSendGiftSumLists)) {
                busiResult.setData(Lists.newArrayList());
                return busiResult;
            }
            for (StatSendGiftSumList statSendGiftSumList : statSendGiftSumLists) {
                StatSendGiftSumListVo statSendGiftSumListVo = converToSendGiftVo(statSendGiftSumList);
                statSendGiftSumListVos.add(statSendGiftSumListVo);
            }
        }
        Collections.sort(statSendGiftSumListVos);
        if (statSendGiftSumListVos.size() > 5) {
            statSendGiftSumListVos = statSendGiftSumListVos.subList(0, 5);
        }
        busiResult.setData(statSendGiftSumListVos);
        return busiResult;
    }

    private StatSendGiftSumListVo converToSendGiftVo(StatSendGiftSumList statSendGiftSumList) {
        StatSendGiftSumListVo statSendGiftSumListVo = new StatSendGiftSumListVo();
        statSendGiftSumListVo.setGoldNum(statSendGiftSumList.getTotalGold());
        statSendGiftSumListVo.setRoomUid(statSendGiftSumList.getRoomUid());
        statSendGiftSumListVo.setUid(statSendGiftSumList.getUid());
        Users users = usersService.getUsersByUid(statSendGiftSumList.getUid());
        statSendGiftSumListVo.setAvatar(users.getAvatar());
        statSendGiftSumListVo.setGender(users.getGender());
        statSendGiftSumListVo.setNick(users.getNick());
        return statSendGiftSumListVo;
    }

    public void recordGiftInfo(Long uid, Long roomUid, Long goldNum) throws Exception {
        StatSendGiftSumListExample example = new StatSendGiftSumListExample();
        example.createCriteria().andRoomUidEqualTo(roomUid);
        List<StatSendGiftSumList> statSendGiftSumLists = statSendGiftSumListMapper.selectByExample(example);
        StatSendGiftSumList statSendGiftSumList = new StatSendGiftSumList();
        if (CollectionUtils.isEmpty(statSendGiftSumLists)) {
            statSendGiftSumList.setUid(uid);
            statSendGiftSumList.setTotalGold(goldNum);
            statSendGiftSumList.setRoomUid(roomUid);
            statSendGiftSumListMapper.insertSelective(statSendGiftSumList);
        } else {
            statSendGiftSumList = statSendGiftSumLists.get(0);
            statSendGiftSumList.setTotalGold(statSendGiftSumList.getTotalGold() + goldNum);
            statSendGiftSumListMapper.updateByPrimaryKeySelective(statSendGiftSumList);
        }
        String sendGiftListStr = jedisService.hget(RedisKey.send_gift_sumlist.getKey(), roomUid.toString());
        StatSendGiftSumListVo statSendGiftSumListVo = converToSendGiftVo(statSendGiftSumList);
        List<StatSendGiftSumListVo> statSendGiftSumListVos = null;
        if (sendGiftListStr == null) {
            statSendGiftSumListVos = Lists.newArrayList();
            statSendGiftSumListVos.add(statSendGiftSumListVo);
            sendGiftListStr = gson.toJson(statSendGiftSumListVos);
            jedisService.hwrite(RedisKey.send_gift_sumlist.getKey(), roomUid.toString(), sendGiftListStr);
            return;
        }
        Type type = new TypeToken<List<StatSendGiftSumListVo>>() {
        }.getType();
        statSendGiftSumListVos = gson.fromJson(sendGiftListStr, type);
        if (statSendGiftSumListVos.contains(statSendGiftSumListVo)) {
            for (StatSendGiftSumListVo statSendGiftSumListVo2 : statSendGiftSumListVos) {
                if (statSendGiftSumListVo2.getUid().equals(statSendGiftSumList.getUid())) {
                    statSendGiftSumListVo2.setGoldNum(statSendGiftSumList.getTotalGold());
                }
            }
        } else {
            statSendGiftSumListVos.add(statSendGiftSumListVo);
            if (statSendGiftSumListVos.size() > 10) {
                Collections.sort(statSendGiftSumListVos);
                statSendGiftSumListVos.remove(statSendGiftSumListVos.size() - 1);
            }
        }
        sendGiftListStr = gson.toJson(statSendGiftSumListVos);
        jedisService.hwrite(RedisKey.send_gift_sumlist.getKey(), roomUid.toString(), sendGiftListStr);
    }
}
