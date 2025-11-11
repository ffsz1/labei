package com.erban.main.service.statis;

import com.erban.main.model.StatSendGiftWeekList;
import com.erban.main.model.StatSendGiftWeekListExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.StatSendGiftWeekListMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.StatSendGiftWeekListVo;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Service
public class StatSendGiftWeekListService {

    @Autowired
    private UsersService usersService;
    @Autowired
    private StatSendGiftWeekListMapper statSendGiftWeekListMapper;
    @Autowired
    private JedisService jedisService;
    private Gson gson = new Gson();

    public BusiResult queryGiftLists(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String sendGiftListStr = jedisService.hget(RedisKey.send_gift_weeklist.getKey(), roomUid.toString());
        List<StatSendGiftWeekListVo> statSendGiftWeekListVos = Lists.newArrayList();
        if (sendGiftListStr != null) {
            Type type = new TypeToken<List<StatSendGiftWeekListVo>>() {
            }.getType();
            statSendGiftWeekListVos = gson.fromJson(sendGiftListStr, type);
        } else {
            StatSendGiftWeekListExample example = new StatSendGiftWeekListExample();
            example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesWeekmorning(), GetTimeUtils.getTimesWeeknight()).andRoomUidEqualTo(roomUid);
            List<StatSendGiftWeekList> statSendGiftWeekLists = statSendGiftWeekListMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(statSendGiftWeekLists)) {
                busiResult.setData(Lists.newArrayList());
                return busiResult;
            }
            for (StatSendGiftWeekList statSendGiftWeekList : statSendGiftWeekLists) {
                StatSendGiftWeekListVo statSendGiftWeekListVo = converToSendGiftVo(statSendGiftWeekList);
                statSendGiftWeekListVos.add(statSendGiftWeekListVo);
            }
        }
        Collections.sort(statSendGiftWeekListVos);
        if (statSendGiftWeekListVos.size() > 5) {
            statSendGiftWeekListVos = statSendGiftWeekListVos.subList(0, 5);
        }
        busiResult.setData(statSendGiftWeekListVos);
        return busiResult;
    }

    private void converToSendGiftVos(List<StatSendGiftWeekListVo> statSendGoldWeekListVos, Users users, StatSendGiftWeekList statSendGiftWeekList) {
        StatSendGiftWeekListVo statSendGoldWeekListVo = new StatSendGiftWeekListVo();
        statSendGoldWeekListVo.setUid(users.getUid());
        statSendGoldWeekListVo.setGoldNum(statSendGiftWeekList.getGoldNum());
        statSendGoldWeekListVo.setRoomUid(statSendGiftWeekList.getRoomUid());
        statSendGoldWeekListVo.setAvatar(users.getAvatar());
        statSendGoldWeekListVo.setGender(users.getGender());
        statSendGoldWeekListVo.setNick(users.getNick());
        statSendGoldWeekListVos.add(statSendGoldWeekListVo);
    }


    public void recordGiftInfo(Long uid, Long roomUid, Long goldNum) throws Exception {
        StatSendGiftWeekListExample example = new StatSendGiftWeekListExample();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesWeekmorning(), GetTimeUtils.getTimesWeeknight()).andRoomUidEqualTo(roomUid);
        List<StatSendGiftWeekList> statSendGiftWeekLists = statSendGiftWeekListMapper.selectByExample(example);
        StatSendGiftWeekList statSendGiftWeekList = new StatSendGiftWeekList();
        if (CollectionUtils.isEmpty(statSendGiftWeekLists)) {
            statSendGiftWeekList.setUid(uid);
            statSendGiftWeekList.setGoldNum(goldNum);
            statSendGiftWeekList.setRoomUid(roomUid);
            statSendGiftWeekListMapper.insertSelective(statSendGiftWeekList);
        } else {
            statSendGiftWeekList = statSendGiftWeekLists.get(0);
            statSendGiftWeekList.setGoldNum(statSendGiftWeekList.getGoldNum() + goldNum);
            statSendGiftWeekListMapper.updateByPrimaryKeySelective(statSendGiftWeekList);
        }
        String sendGiftListStr = jedisService.hget(RedisKey.send_gift_weeklist.getKey(), roomUid.toString());
        StatSendGiftWeekListVo statSendGiftWeekListVo = converToSendGiftVo(statSendGiftWeekList);
        List<StatSendGiftWeekListVo> statSendGiftWeekListVos = null;
        if (sendGiftListStr == null) {
            statSendGiftWeekListVos = Lists.newArrayList();
            statSendGiftWeekListVos.add(statSendGiftWeekListVo);
            sendGiftListStr = gson.toJson(statSendGiftWeekListVos);
            jedisService.hwrite(RedisKey.send_gift_weeklist.getKey(), roomUid.toString(), sendGiftListStr);
            return;
        }
        Type type = new TypeToken<List<StatSendGiftWeekListVo>>() {
        }.getType();
        statSendGiftWeekListVos = gson.fromJson(sendGiftListStr, type);
        if (statSendGiftWeekListVos.contains(statSendGiftWeekListVo)) {
            for (StatSendGiftWeekListVo statSendGiftWeekListVo2 : statSendGiftWeekListVos) {
                if (statSendGiftWeekListVo2.getUid().equals(statSendGiftWeekList.getUid())) {
                    statSendGiftWeekListVo2.setGoldNum(statSendGiftWeekList.getGoldNum());
                }
            }
        } else {
            statSendGiftWeekListVos.add(statSendGiftWeekListVo);
            if (statSendGiftWeekListVos.size() > 5) {
                Collections.sort(statSendGiftWeekListVos);
                statSendGiftWeekListVos.remove(statSendGiftWeekListVos.size() - 1);
            }
        }
        sendGiftListStr = gson.toJson(statSendGiftWeekListVos);
        jedisService.hwrite(RedisKey.send_gift_weeklist.getKey(), roomUid.toString(), sendGiftListStr);
    }

    private StatSendGiftWeekListVo converToSendGiftVo(StatSendGiftWeekList statSendGiftWeekList) {
        StatSendGiftWeekListVo statSendGiftWeekListVo = new StatSendGiftWeekListVo();
        statSendGiftWeekListVo.setGoldNum(statSendGiftWeekList.getGoldNum());
        statSendGiftWeekListVo.setRoomUid(statSendGiftWeekList.getRoomUid());
        statSendGiftWeekListVo.setUid(statSendGiftWeekList.getUid());
        Users users = usersService.getUsersByUid(statSendGiftWeekList.getUid());
        statSendGiftWeekListVo.setAvatar(users.getAvatar());
        statSendGiftWeekListVo.setGender(users.getGender());
        statSendGiftWeekListVo.setNick(users.getNick());
        return statSendGiftWeekListVo;
    }
}
