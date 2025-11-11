package com.juxiao.xchat.task.scheduler.item;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.task.room.GiftDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GiftDrawTask {


    @Autowired
    private GiftDrawService drawService;

    /**
     * 每30s刷新今天捡海螺排行到缓存
     * @throws WebServiceException
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void refreshNow() throws WebServiceException {
        drawService.refreshRank(1);
    }

    /**
     * 每天0点10分刷新昨天捡海螺排行到缓存
     * @throws WebServiceException
     */
    @Scheduled(cron = "0 10 0 ? * *")
    public void refreshLastDay() throws WebServiceException {
        drawService.refreshRank(2);
    }

    /**
     * 每30s刷新本周捡海螺排行到缓存
     * @throws WebServiceException
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void refreshLastWeek() throws WebServiceException {
        drawService.refreshRank(3);
    }

    /**
     * 清除捡海螺活动礼物的数量</br>
     * 每天更新
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void deleteDrawGiftNum() {
        // todo 2020-9-8 特殊要求 暂时注释
        drawService.deleteDrawGiftNum();
    }

    /**
     * 每2分钟统计捡海螺流水
     */
    @Scheduled(cron = "15 7 */1 * * ?")
    public void refreshWeekRoomFlowCache() {
        drawService.countGiftDayDraw();
    }

    /**
     * 每小时07分15秒统计用户捡海螺流水
     */
//    @Scheduled(cron = "15 7 */1 * * ?")
    @Scheduled(cron = "0 0 */1 * * ?")
    public void refreshUserDayDraw() {
        drawService.countUserDayDraw();
    }
}
