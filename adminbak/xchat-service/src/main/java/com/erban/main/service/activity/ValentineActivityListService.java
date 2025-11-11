package com.erban.main.service.activity;

import com.erban.main.service.base.CacheListBaseService;
import com.erban.main.vo.activity.RankValentineVo;
import com.xchat.common.redis.RedisKey;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValentineActivityListService extends CacheListBaseService<RankValentineVo, RankValentineVo> {
    private static String sql = "select m.uid as maleUid,m.erban_no as maleNo,m.nick as maleNick,m.avatar as maleAvatar,f.uid as femaleUid,f.erban_no as femaleNo,f.nick as femaleNick,f.avatar as femaleAvatar,ifnull((select SUM(g.total_gold_num) from gift_send_record g where g.recive_uid = m.uid and g.create_time >= v.update_time and g.create_time < '2018-05-21'),0)+ifnull((select SUM(g.total_gold_num) from gift_send_record g where g.recive_uid = v.female_uid and g.create_time >= v.update_time and g.create_time < '2018-05-21'),0) as total from valentine v INNER JOIN users m on v.male_uid = m.uid INNER JOIN users f on v.female_uid = f.uid where v.valentine_status = 1 ORDER BY total desc";

    @Override
    public List<RankValentineVo> getListByJedisId(String jedisId) {
        if("1".equals(jedisId)){
            return getList(RedisKey.valentine_list.getKey(), jedisId, sql);
        }
        return new ArrayList<>();
    }

    @Override
    public RankValentineVo entityToCache(RankValentineVo entity) {
        return entity;
    }

    public void refresh(String jedisCode, String jedisId, String totalCode, String noCode, String sql) {
        List<RankValentineVo> entityList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RankValentineVo.class));
        RankValentineVo rankValentineVo;
        if (entityList != null&&entityList.size()>0) {
            for(Integer i=1;i<=entityList.size();i++){
                rankValentineVo = entityList.get(i-1);
                jedisService.hset(totalCode, rankValentineVo.getMaleUid().toString()+rankValentineVo.getFemaleUid().toString(), rankValentineVo.getTotal().toString());
                jedisService.hset(noCode, rankValentineVo.getMaleUid().toString()+rankValentineVo.getFemaleUid().toString(), i.toString());
            }
            jedisService.hwrite(jedisCode, jedisId, gson.toJson(entityList.subList(0, 50)));
        }
    }

    public void refreshAll(){
        refresh(RedisKey.valentine_list.getKey(), "1", RedisKey.valentine_total.getKey(), RedisKey.valentine_no.getKey(), sql);
    }

}
