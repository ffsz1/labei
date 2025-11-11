package com.erban.web.controller.level;

import com.alibaba.fastjson.JSON;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.service.level.LevelExperienceService;
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
 * 用户经验值
 */
@Controller
@RequestMapping("/level/exeperience")
public class LevelExperienceController {
    private static final Logger logger = LoggerFactory.getLogger(LevelExperienceController.class);
    @Autowired
    private LevelExperienceService levelExperienceService;

    /**
     * 获取用户经验值
     * @param uid
     * @return
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getUserExeperience(Long uid){
        logger.info("接口调用：（/level/exeperience/get），获取用户经验值，接口入参：uid:{}",uid);
        BusiResult busiResult = null;
        if(uid==null){
            logger.error("接口入参uid为null");
        }
        try{
            LevelExerpenceVo levelExperienceVo = levelExperienceService.getLevelExperience(uid);
            if(levelExperienceVo==null){
                return new BusiResult(BusiStatus.LEVEL_EXPERIENCE_NOLEVEL);
            }
            busiResult = new BusiResult(BusiStatus.SUCCESS,levelExperienceVo);
        }catch (Exception e){
            logger.error("接口调用：（/level/exeperience/get），获取用户经验值异常，报错：{}",e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("接口调用：（/level/exeperience/get），获取用户经验值，接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }
}
