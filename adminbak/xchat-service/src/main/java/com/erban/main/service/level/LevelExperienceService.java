package com.erban.main.service.level;

import com.erban.main.model.LevelExperience;
import com.erban.main.model.LevelExperienceExample;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.mybatismapper.LevelExperienceMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.util.StringUtils;
import com.xchat.common.redis.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户等级服务
 */
@Service
public class LevelExperienceService extends BaseService {

    @Autowired
    private LevelExperienceMapper levelExperienceMapper;
    @Autowired
    private GiftSendRecordService giftSendRecordService;

    /**
     * 获取用户经验等级
     *
     * @param uid
     * @return
     */
    public LevelExerpenceVo getLevelExperience(Long uid) {
        //获取用户经验值
        Long levelExer = giftSendRecordService.getLevelExerpence(uid);
        LevelExperienceExample example = new LevelExperienceExample();
        example.setOrderByClause(" level_seq desc ");
        example.createCriteria().andNeedMinLessThanOrEqualTo(levelExer).andStatusEqualTo(Byte.valueOf("1"));
        List<LevelExperience> levelExperiences = levelExperienceMapper.selectByExample(example);

        //用户对应的等级
        LevelExerpenceVo levelExerpenceVo = new LevelExerpenceVo();
        if (levelExperiences == null || levelExperiences.size() == 0) {//经验值不够评级：0级
            LevelExperience lv1 = getLEFromCasheByName("LV1");
            if(lv1==null){
                logger.error("数据库找不到此经验等级：{}","LV1");
                return null;
            }
            levelExerpenceVo.setLevelName("LV0");
            levelExerpenceVo.setLeftGoldNum(lv1.getNeedMin()-levelExer);
            levelExerpenceVo.setLevelPercent(levelExer/ Double.valueOf(lv1.getNeedMin()));
        } else {
            LevelExperience levelExperience = levelExperiences.get(0);
            levelExerpenceVo.setLevelName(levelExperience.getLevelName());
            levelExerpenceVo.setLeftGoldNum(levelExperience.getNeedMax() - levelExer);
            levelExerpenceVo.setLevelPercent((levelExer-levelExperience.getNeedMin()) / Double.valueOf((levelExperience.getNeedMax()-levelExperience.getNeedMin())));
        }
        return levelExerpenceVo;
    }


    /**
     * 根据等级名获取 LevelExperience (cash-->DB)
     *
     * @param levelName
     * @return
     */
    public LevelExperience getLEFromCasheByName(String levelName) {
        String str = jedisService.hget(RedisKey.level_exper_list.getKey(), levelName);
        if (StringUtils.isBlank(str)) {
            LevelExperience levelExperience = getLEFromDBByName(levelName);
            if (levelExperience != null) {
                jedisService.hwrite(RedisKey.level_exper_list.getKey(), levelName, gson.toJson(levelExperience));
                return levelExperience;
            }
        }else {
            return gson.fromJson(str,LevelExperience.class);
        }
        return null;
    }

    /**
     * 根据等级名获取 LevelExperience (DB)
     *
     * @param levelName
     * @return
     */
    public LevelExperience getLEFromDBByName(String levelName) {
        LevelExperienceExample example = new LevelExperienceExample();
        example.createCriteria().andLevelNameEqualTo(levelName).andStatusEqualTo(Byte.valueOf("1"));
        List<LevelExperience> levelExperiences = levelExperienceMapper.selectByExample(example);
        if (levelExperiences != null && levelExperiences.size() == 1) {
            return levelExperiences.get(0);
        }
        return null;
    }
}
