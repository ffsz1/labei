package com.erban.web.controller.level;

import com.alibaba.fastjson.JSON;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.service.level.LevelCharmService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户魅力级别
 */
@Controller
@RequestMapping("/level/charm")
public class LevelCharmController {
    private static final Logger logger = LoggerFactory.getLogger(LevelCharmController.class);
    @Autowired
    private LevelCharmService levelCharmService;

    /**
     * 获取用户魅力级别
     * @param uid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public BusiResult getUserExeperience(Long uid){
        logger.info("接口调用：（/level/charm/get），获取用户魅力级别，接口入参：uid:{}",uid);
        BusiResult busiResult = null;
        if(uid==null){
            logger.error("接口入参uid为null");
        }
        try{
            LevelCharmVo levelCharmVo = levelCharmService.getLevelCharm(uid);
            if(levelCharmVo==null){
                return new BusiResult(BusiStatus.LEVEL_CHARM_NOLEVEL);
            }
            busiResult=new BusiResult(BusiStatus.SUCCESS,levelCharmVo);
        }catch (Exception e){
            logger.error("接口调用：（/level/charm/get），获取用户魅力级别异常，报错：{}",e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("接口调用：（/level/charm/get），获取用户魅力级别，接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }
}
