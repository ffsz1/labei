package com.erban.main.service;

import com.erban.main.model.StatWeekList;
import com.erban.main.model.StatWeekListExample;
import com.erban.main.model.StatWeekLists;
import com.erban.main.mybatismapper.StatWeekListMapper;
import com.erban.main.mybatismapper.StatWeekListsMapper;
import com.erban.main.vo.WeekListVo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service
public class ConverService {

    @Autowired
    private StatWeekListMapper statWeekListMapper;
    @Autowired
    private StatWeekListsMapper statWeekListsMapper;

    private Gson gson = new Gson();

    public BusiResult converWeek() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatWeekListExample example = new StatWeekListExample();
        example.createCriteria().andRoomUidIsNotNull();
        List<StatWeekList> statWeekLists = statWeekListMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statWeekLists)) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        for (StatWeekList statWeekList : statWeekLists) {
            Type type = new TypeToken<List<WeekListVo>>() {
            }.getType();
            List<WeekListVo> weekListVos = gson.fromJson(statWeekList.getJsonstr(), type);
            for (WeekListVo weekListVo : weekListVos) {
                StatWeekLists statWeekListss = new StatWeekLists();
                statWeekListss.setRoomUid(weekListVo.getRoomUid());
                statWeekListss.setAvatar(weekListVo.getAvatar());
                statWeekListss.setGender(weekListVo.getGender());
                statWeekListss.setDealUid(weekListVo.getUid());
                statWeekListss.setProdId(weekListVo.getProdId());
                statWeekListss.setPrice(weekListVo.getPrice());
                statWeekListss.setNick(weekListVo.getNick());
                statWeekListss.setCreateTime(new Date());
                statWeekListsMapper.insertSelective(statWeekListss);
            }
        }
        return busiResult;
    }
}
