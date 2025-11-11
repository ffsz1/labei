package com.juxiao.xchat.service.task.user.impl;

import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.dao.task.domain.StatDailyUserPurseDO;
import com.juxiao.xchat.dao.user.UserGiftPurseDao;
import com.juxiao.xchat.dao.user.UserPurseDao;
import com.juxiao.xchat.dao.user.dto.UserPurseSumDTO;
import com.juxiao.xchat.service.task.user.UserPurseDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title:
 * @date 2019-05-14
 * @time 09:45
 */
@Service
public class UserPurseDailyServiceImpl implements UserPurseDailyService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserPurseDao userPurseDao;

    @Autowired
    private UserGiftPurseDao userGiftPurseDao;

    /**
     * 保存每日钻石金币报表
     */
    @Override
    public void saveDailyPurse() {
        UserPurseSumDTO sumDto = userPurseDao.sumUserPurse();
        Double nomalDiamondSum = userGiftPurseDao.sumGiftDiamond(2);
        Double drawDiamondSum = userGiftPurseDao.sumGiftDiamond(3);

        StatDailyUserPurseDO dailyPurseDo = new StatDailyUserPurseDO();
        dailyPurseDo.setGoldSum(sumDto == null ? 0 : sumDto.getGoldSum());
        dailyPurseDo.setDiamondSum(sumDto == null ? 0 : sumDto.getDiamondSum());
        dailyPurseDo.setGiftNomalDiamondSum(nomalDiamondSum == null ? 0 : nomalDiamondSum);
        dailyPurseDo.setGiftDrawDiamondSum(drawDiamondSum == null ? 0 : drawDiamondSum);
        taskDao.saveUserDailyPurse(dailyPurseDo);
    }
}
