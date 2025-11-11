package com.erban.main.service.noble;

import com.erban.main.model.NobleZip;
import com.erban.main.model.NobleZipExample;
import com.erban.main.mybatismapper.NobleZipMapper;
import com.erban.main.service.base.BaseService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NobleZipService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(NobleZipService.class);

    @Autowired
    private NobleZipMapper nobleZipMapper;

    /**
     * 获取当前有效的配置, cache -> db -> cache
     *
     * @return
     */
    public NobleZip getValidNobleZip() {
        String json = jedisService.get(RedisKey.noble_zip.getKey());
        if (BlankUtil.isBlank(json)) {
            NobleZip nobleZip = getCurNobleZipFromDb();
            if (nobleZip != null) {
                jedisService.set(RedisKey.noble_zip.getKey(), gson.toJson(nobleZip));
            }
            return nobleZip;
        }
        return gson.fromJson(json, NobleZip.class);
    }

    public NobleZip getCurNobleZipFromDb(){
        NobleZipExample example = new NobleZipExample();
        example.createCriteria().andStatusEqualTo((byte) 1);
        List<NobleZip> list = nobleZipMapper.selectByExampleWithBLOBs(example);
        if (!BlankUtil.isBlank(list)) {
            return list.get(0);
        }
        return null;
    }
}
