package com.erban.main.service.noble;

import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleRightExample;
import com.erban.main.mybatismapper.NobleRightMapper;
import com.erban.main.service.base.BaseService;
import com.google.common.collect.Lists;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class NobleRightService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(NobleRightService.class);

    @Autowired
    private NobleRightMapper nobleRightMapper;

    /**
     * 获取所有有效的贵族特权列表，cache -> db -> cache
     *
     * @return
     */
    public List<NobleRight> getNobleRightList() {
        Map<String, String> map = jedisService.hgetAll(RedisKey.noble_right.getKey());
        if (map == null || map.size() == 0) {
            List<NobleRight> list = getNobleRightFromDB();
            for (NobleRight nobleRight : list) {
                jedisService.hset(RedisKey.noble_right.getKey(), nobleRight.getId().toString(), gson.toJson(nobleRight));
            }
            return list;
        }
        List<NobleRight> list = Lists.newLinkedList();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            list.add(gson.fromJson(map.get(key), NobleRight.class));
        }
        return list;
    }

    public List<NobleRight> getNobleRightFromDB(){
        NobleRightExample example = new NobleRightExample();
        example.createCriteria().andStatusEqualTo((byte)1);
        return nobleRightMapper.selectByExample(example);
    }

    public NobleRight getNobleRightByKey(Integer nobleId) {
        NobleRightExample example = new NobleRightExample();
        example.createCriteria().andIdEqualTo(nobleId).andStatusEqualTo((byte) 1);
        List<NobleRight> list = nobleRightMapper.selectByExample(example);
        if (BlankUtil.isBlank(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     *  获取贵族的权利信息， cache -> db -> cache
     *
     * @param nobleId
     * @return
     */
    public NobleRight getNobleRight(Integer nobleId) {
        String json = jedisService.hget(RedisKey.noble_right.getKey(), nobleId.toString());
        if (BlankUtil.isBlank(json)) {
            NobleRight nobleRight = getNobleRightByKey(nobleId);
            if (nobleRight != null) {
                jedisService.hset(RedisKey.noble_right.getKey(),nobleId.toString(), gson.toJson(nobleRight));
            } else {
                logger.warn("getNobleRight fail, nobleId:" + nobleId + " not exist");
                // 防止缓存穿透
                jedisService.hset(RedisKey.noble_right.getKey(),nobleId.toString(), "{}");
            }
            return nobleRight;
        } else if(json.equalsIgnoreCase("{}")){
            return null;
        }
        return gson.fromJson(json, NobleRight.class);
    }
}
