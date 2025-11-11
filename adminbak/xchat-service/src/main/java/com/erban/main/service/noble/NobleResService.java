package com.erban.main.service.noble;

import com.erban.main.model.NobleRes;
import com.erban.main.model.NobleResExample;
import com.erban.main.model.NobleRight;
import com.erban.main.mybatismapper.NobleResMapper;
import com.erban.main.mybatismapper.NobleResMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.vo.noble.NobleResVo;
import com.google.common.collect.Lists;
import com.xchat.common.redis.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NobleResService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(NobleResService.class);
    public static final int SIZE = 12;

    @Autowired
    private NobleResMapper nobleResMapper;
    @Autowired
    private NobleResMapperMgr nobleResMapperMgr;


    public List<NobleRes> getDefNobleResList(Integer nobleId) {
        NobleResExample example = new NobleResExample();
        example.createCriteria().andNobleIdEqualTo(nobleId).andIsDefEqualTo((byte) 1).andStatusEqualTo((byte) 1);
        return nobleResMapper.selectByExample(example);
    }

    public NobleRes getResOne(int id) {
        return nobleResMapper.selectByPrimaryKey(id);
    }

    public List<NobleRes> getResListByPage(int type, int defId, Integer page) {
        int skip = (page - 1) * SIZE;
        return nobleResMapperMgr.selectResList(type, defId, skip, SIZE);
    }


    public List<NobleRes> getResListByPage(int type, int defId, int skip, int size) {
        return nobleResMapperMgr.selectResList(type, defId, skip, size);
    }

//
//    public List<NobleResVo> converToVo(List<NobleRes> list) {
//        Map<String, String> nobleRights = jedisService.hgetAll(RedisKey.noble_right.getKey());
//        List<NobleResVo> volist = Lists.newArrayList();
//        for (int i = 0; i < list.size(); i++) {
//            NobleResVo vo = new NobleResVo();
//            NobleRes res = list.get(i);
//            vo.setId(res.getId());
//            vo.setIsDef(res.getIsDef());
//            vo.setIsDyn(res.getIsDyn());
//            vo.setName(res.getName());
//            vo.setPreview(res.getPreview());
//            vo.setResType(res.getResType());
//            vo.setValue(res.getValue());
//            vo.setSeq(res.getSeq());
//            vo.setTmpstr(res.getTmpstr());
//            vo.setNobleId(res.getNobleId());
//            if(res.getNobleId() > 0) {
//                try {
//                    String json = nobleRights.get(res.getNobleId().toString());
//                    NobleRight nobleRight = gson.fromJson(json, NobleRight.class);
//                    vo.setNobleName(nobleRight.getName());
//                } catch (Exception e) {
//                    logger.error("nobleRights error", e);
//                }
//            }
//            volist.add(vo);
//        }
//        return volist;
//    }
}
