package com.erban.web.controller.duty;

import com.erban.main.model.Users;
import com.erban.main.service.common.JedisLockService;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.dto.DutyResultDTO;
import com.erban.main.service.duty.vo.DutiesVo;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.google.common.collect.Lists;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.redis.JedisLock;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/duty")
public class DutyController {
    @Autowired
    private DutyService dutyService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private JedisLockService lockService;
    @Autowired
    private UsersService usersService;

    /**
     * 获取任务列表
     *
     * @return
     */
    @ResponseBody
//    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BusiResult<DutiesVo> listDuties(@RequestParam("uid") Long uid) {
        if (uid == null) {
            return new BusiResult<>(BusiStatus.PARAMERROR);
        }

        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult<>(BusiStatus.USERNOTEXISTS);
        }

//        List<DutyResultDTO> freshs = dutyService.listFreshDuties(uid);
        List<DutyResultDTO> dailies = dutyService.listDailyDuties(uid);
        List<DutyResultDTO> dailyTime = dutyService.listDailyTime(uid);

        DutiesVo dutiesVo = new DutiesVo();
        String roomTime = jedisService.hget(RedisKey.daily_room_time.getKey(), uid.toString());
        try {
            dutiesVo.setRoomTime(Integer.valueOf(roomTime));
        } catch (Exception e) {
            dutiesVo.setRoomTime(0);
        }

        dutiesVo.setFresh(Lists.newArrayList());
        dutiesVo.setDaily(dailies);
        dutiesVo.setDailyTime(dailyTime);
        return new BusiResult<>(BusiStatus.SUCCESS, dutiesVo);
    }

    /**
     * 获取任务奖励
     *
     * @param dutyId
     * @param uid
     * @return
     */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/achieve", method = RequestMethod.POST)
    public BusiResult achieve(@RequestParam("dutyId") Integer dutyId, @RequestParam("uid") Long uid) {
        return dutyService.achieve(dutyId, uid);
    }

    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/fresh/public", method = RequestMethod.POST)
    public BusiResult speakPublic(@RequestParam("uid") Long uid) {
        return dutyService.speakPublic(uid);
    }
}
