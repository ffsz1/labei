package com.erban.main.service.noble;

import com.erban.main.model.NobleRecomRecord;
import com.erban.main.model.NobleRecomRecordExample;
import com.erban.main.model.NobleUsers;
import com.erban.main.mybatismapper.NobleRecomRecordMapper;
import com.erban.main.service.base.BaseService;
import com.google.common.collect.Lists;
import com.xchat.common.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NobleRecomService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(NobleRecomService.class);

    @Autowired
    private NobleRecomRecordMapper nobleRecomRecordMapper;
    @Autowired
    private NobleHelperService nobleHelperService;

    private ExecutorService executorService = Executors.newFixedThreadPool(8);


    public List<Long> getRecomRoom() {
        NobleRecomRecordExample example = new NobleRecomRecordExample();
        example.createCriteria().andEndTimeGreaterThan(new Date());
        example.setOrderByClause(" start_time desc");
        List<NobleRecomRecord> list = nobleRecomRecordMapper.selectByExample(example);
        List<Long> recomRoom = Lists.newArrayList();
        for (NobleRecomRecord nobleRecomRecord : list) {
            if(recomRoom.contains(nobleRecomRecord.getRoomUid())) continue;
            recomRoom.add(nobleRecomRecord.getRoomUid());
        }
        return recomRoom;
    }

    /**
     * 发送拉贝小秘书提醒通知，提醒热门推荐已生效
     */
    public void sendErbanNotice() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                NobleRecomRecordExample example = new NobleRecomRecordExample();
                example.createCriteria().andHasNoticeEqualTo((byte) 0).andEndTimeGreaterThan(new Date());
                List<NobleRecomRecord> list = nobleRecomRecordMapper.selectByExample(example);

                for (NobleRecomRecord recomRecord : list) {
                    try {
                        nobleHelperService.sendNobleRecomRoomMess(recomRecord.getUid());
                        recomRecord.setHasNotice((byte) 1);
                        nobleRecomRecordMapper.updateByPrimaryKeySelective(recomRecord);
                    } catch (Exception e) {
                        logger.error("sendMsg error, uid:" + recomRecord.getUid(), e);
                    }
                }
            }
        });
    }

    public void handleRecomRoom(final NobleUsers nobleUsers, final long roomUid) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                NobleRecomRecord recomRecord = new NobleRecomRecord();
                recomRecord.setRoomUid(roomUid);
                recomRecord.setHasNotice((byte) 0);
                recomRecord.setStartTime(new Date());
                recomRecord.setUid(nobleUsers.getUid());
                recomRecord.setNobleId(nobleUsers.getNobleId());
                recomRecord.setNobleName(nobleUsers.getNobleName());
                recomRecord.setEndTime(DateTimeUtil.getNextHour(new Date(), 2));
                nobleRecomRecordMapper.insertSelective(recomRecord);
            }
        });
    }

}
