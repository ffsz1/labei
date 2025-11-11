package com.erban.main.service.level;

import com.erban.main.model.LevelCharm;
import com.erban.main.model.LevelCharmExample;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.mybatismapper.LevelCharmMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.util.StringUtils;
import com.xchat.common.redis.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户魅力级别服务
 */
@Service
public class LevelCharmService extends BaseService{

    @Autowired
    private LevelCharmMapper levelCharmMapper;
    @Autowired
    private GiftSendRecordService giftSendRecordService;

    /**
     * 获取用户魅力等级
     * @param uid
     * @return
     */
    public LevelCharmVo getLevelCharm(Long uid){
        //获取用户魅力值
        Long levelChar = giftSendRecordService.getLevelCharm(uid);
        LevelCharmExample example = new LevelCharmExample();
        example.setOrderByClause(" level_seq desc ");
        example.createCriteria().andNeedMinLessThanOrEqualTo(levelChar);
        List<LevelCharm> levelCharms = levelCharmMapper.selectByExample(example);

        //用户对应的等级
        LevelCharmVo levelCharmVo=new LevelCharmVo();
        if(levelCharms==null ||  levelCharms.size()==0){//魅力值不够评级：0级
            LevelCharm lv1 = getLCFromCasheByName("LV1");
            if(lv1==null){
                logger.error("数据库找不到此魅力等级：{}","LV1");
                return null;
            }
            levelCharmVo.setLevelName("LV0");
            levelCharmVo.setLeftGoldNum(lv1.getNeedMin()-levelChar);
            levelCharmVo.setLevelPercent(levelChar/Double.valueOf(lv1.getNeedMin()));
        }else {
            LevelCharm levelCharm = levelCharms.get(0);
            levelCharmVo.setLevelName(levelCharm.getLevelName());
            levelCharmVo.setLeftGoldNum(levelCharm.getNeedMax()-levelChar);
            levelCharmVo.setLevelPercent((levelChar-levelCharm.getNeedMin())/Double.valueOf(levelCharm.getNeedMax()-levelCharm.getNeedMin()));
        }
        return levelCharmVo;
    }

    /**
     * 根据魅力等级名获取 LevelCharm (cash-->DB)
     *
     * @param levelName
     * @return
     */
    public LevelCharm getLCFromCasheByName(String levelName) {
        String str = jedisService.hget(RedisKey.level_charm_list.getKey(), levelName);
        if (StringUtils.isBlank(str)) {
            LevelCharm levelCharm = getLCFromDBByName(levelName);
            if (levelCharm != null) {
                jedisService.hwrite(RedisKey.level_charm_list.getKey(), levelName, gson.toJson(levelCharm));
                return levelCharm;
            }
        }else {
            return gson.fromJson(str,LevelCharm.class);
        }
        return null;
    }

    /**
     * 根据魅力等级名获取 LevelCharm (DB)
     *
     * @param levelName
     * @return
     */
    public LevelCharm getLCFromDBByName(String levelName) {
        LevelCharmExample example = new LevelCharmExample();
        example.createCriteria().andLevelNameEqualTo(levelName).andStatusEqualTo(Byte.valueOf("1"));
        List<LevelCharm> levelCharms = levelCharmMapper.selectByExample(example);
        if (levelCharms != null && levelCharms.size() == 1) {
            return levelCharms.get(0);
        }
        return null;
    }
}
