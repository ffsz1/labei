package com.juxiao.xchat.task.item;

import com.juxiao.xchat.service.task.room.GiftDrawService;
import com.juxiao.xchat.task.configer.TaskWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: alwyn
 * @Description: 首页测试
 * @Date: 2018/10/17 11:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskWebApplication.class)
public class GiftDrawTest {
    private final Logger logger = LoggerFactory.getLogger(GiftDrawTest.class);

    @Autowired
    private GiftDrawService drawService;

    @Test
    public void countGiftDayDraw() throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("[ 统计捡海螺流水 ] 更新当天捡海螺流水 start");
        drawService.countGiftDayDraw();
        System.out.println("[ 统计捡海螺流水 ] 更新当天捡海螺流水 finish，耗时:>" + (System.currentTimeMillis() - startTime));
    }
}
