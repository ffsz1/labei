package com.erban.main.service.activity;

import com.erban.main.model.Users;
import com.erban.main.model.Valentine;
import com.erban.main.mybatismapper.ValentineMapper;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ValentineVo;
import com.erban.main.vo.activity.RankValentineVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ValentineActivityService extends CacheBaseService<Valentine, ValentineVo> {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ValentineMapper valentineMapper;
    @Autowired
    private ValentineActivityListService valentineActivityListService;

    @Override
    public ValentineVo getOneByJedisId(String jedisId) {
        return null;
    }

    public ValentineVo getOneByJedisId(String uid, String status){
        return getOne(RedisKey.valentine.getKey(), uid+status, "select * from valentine where valentine_status = ? and male_uid = ? and male_status = 1 union select * from valentine where valentine_status = ? and female_uid = ? and female_status = 1 limit 1", status, uid, status, uid);
    }

    @Override
    public ValentineVo entityToCache(Valentine entity) {
        ValentineVo valentineVo = new ValentineVo();
        valentineVo.setId(entity.getId());
        Users users = usersService.getUsersByUid(entity.getMaleUid().longValue());
        if(users!=null){
            valentineVo.setMaleUid(entity.getMaleUid());
            valentineVo.setMaleNo(users.getErbanNo());
            valentineVo.setMaleNick(users.getNick());
            valentineVo.setMaleAvatar(users.getAvatar());
        }
        users = usersService.getUsersByUid(entity.getFemaleUid().longValue());
        if(users!=null){
            valentineVo.setFemaleUid(entity.getFemaleUid());
            valentineVo.setFemaleNo(users.getErbanNo());
            valentineVo.setFemaleNick(users.getNick());
            valentineVo.setFemaleAvatar(users.getAvatar());
        }
        valentineVo.setValentineStatus(entity.getValentineStatus());
        valentineVo.setCreateTime(entity.getCreateTime());
        valentineVo.setUpdateTime(entity.getUpdateTime());
        return valentineVo;
    }

    public Long getTotal(ValentineVo valentineVo){
        String total = jedisService.hget(RedisKey.valentine_total.getKey(), valentineVo.getMaleUid().toString()+valentineVo.getFemaleUid().toString());
        if(StringUtils.isBlank(total)){
//            Long object = jdbcTemplate.queryForObject("select ifnull((select SUM(g.total_gold_num) from gift_send_record g where g.recive_uid = ? and g.create_time >= ?),0)+ifnull((select SUM(g.total_gold_num) from gift_send_record g where g.recive_uid = ? and g.create_time >= ?),0)", Long.class, valentineVo.getMaleUid(), valentineVo.getUpdateTime(), valentineVo.getFemaleUid(), valentineVo.getUpdateTime());
//            jedisService.hset(RedisKey.valentine_total.getKey(), valentineVo.getMaleUid().toString()+valentineVo.getFemaleUid().toString(), object.toString());
            return 0L;
        }
        return Long.valueOf(total);
    }

    public Integer getRankNo(ValentineVo valentineVo){
        String rankNo = jedisService.hget(RedisKey.valentine_no.getKey(), valentineVo.getMaleUid().toString()+valentineVo.getFemaleUid().toString());
        if(StringUtils.isBlank(rankNo)){
//            Integer object = jdbcTemplate.queryForObject("select COUNT(1)+1 from (select ifnull((select SUM(g.total_gold_num) from gift_send_record g where g.recive_uid = m.uid and g.create_time >= v.update_time),0)+ifnull((select SUM(g.total_gold_num) from gift_send_record g where g.recive_uid = v.female_uid and g.create_time >= v.update_time),0) as total from valentine v INNER JOIN users m on v.male_uid = m.uid INNER JOIN users f on v.female_uid = f.uid where v.valentine_status = 1 HAVING total > ?) a", Integer.class, valentineVo.getTotal());
//            jedisService.hset(RedisKey.valentine_no.getKey(), valentineVo.getMaleUid().toString()+valentineVo.getFemaleUid().toString(), object.toString());
            return 0;
        }
        return Integer.valueOf(rankNo);
    }

    public BusiResult get(Long uid){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        ValentineVo valentineVo = getOneByJedisId(uid.toString(), "1");
        if(valentineVo!=null){
            valentineVo.setTotal(getTotal(valentineVo));
            valentineVo.setRankNo(getRankNo(valentineVo));
            return new BusiResult(BusiStatus.SUCCESS, valentineVo);
        }
        valentineVo = getOneByJedisId(uid.toString(), "0");
        if(valentineVo!=null){
            return new BusiResult(BusiStatus.SUCCESS, valentineVo);
        }
        valentineVo = new ValentineVo();
        valentineVo.setMaleUid(users.getUid().intValue());
        valentineVo.setMaleAvatar(users.getAvatar());
        valentineVo.setMaleNick(users.getNick());
        valentineVo.setMaleNo(users.getErbanNo());
        valentineVo.setValentineStatus(new Byte("-1"));
        return new BusiResult(BusiStatus.SUCCESS, valentineVo);
    }

    public BusiResult build(Long uid, Long userNo){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Users femaleUsers = usersService.getUsersByErBanNo(userNo);
        if(femaleUsers==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if(users.getUid().intValue()==femaleUsers.getUid().intValue()){
            return new BusiResult(BusiStatus.NOAUTHORITY,"不允许自己绑定自己","");
        }
        ValentineVo valentineVo = getOneByJedisId(uid.toString(), "1");
        if(valentineVo!=null){
            return new BusiResult(BusiStatus.HOTEXISTS, "你已经绑定过关系", "");
        }
        valentineVo = getOneByJedisId(femaleUsers.getUid().toString(), "1");
        if(valentineVo!=null){
            return new BusiResult(BusiStatus.HOTEXISTS, "对方已经绑定过关系", "");
        }
        Valentine valentine;
        List<Valentine> valentineList = jdbcTemplate.query("select * from valentine where valentine_status = 0 and male_uid = ? and female_uid = ? and female_status = 1 union select * from valentine where valentine_status = 0 and male_uid = ? and female_uid = ? and male_status = 1", new BeanPropertyRowMapper<>(Valentine.class), users.getUid(), femaleUsers.getUid(), femaleUsers.getUid(), users.getUid());
        if(valentineList!=null&&valentineList.size()>0){
            valentine=valentineList.get(0);
            valentine.setMaleStatus(new Byte("1"));
            valentine.setFemaleStatus(new Byte("1"));
            valentine.setValentineStatus(new Byte("1"));
            valentine.setUpdateTime(new Date());
            valentineMapper.updateByPrimaryKeySelective(valentine);
            jedisService.hdel(RedisKey.valentine.getKey(), valentine.getMaleUid()+"0");
            jedisService.hdel(RedisKey.valentine.getKey(), valentine.getFemaleUid()+"0");
            valentineVo = entityToCache(valentine);
            valentineVo.setTotal(getTotal(valentineVo));
            valentineVo.setRankNo(getRankNo(valentineVo));
        }else{
            valentine = new Valentine();
            valentine.setMaleUid(users.getUid().intValue());
            valentine.setMaleStatus(new Byte("1"));
            valentine.setFemaleUid(femaleUsers.getUid().intValue());
            valentine.setFemaleStatus(new Byte("0"));
            valentine.setValentineStatus(new Byte("0"));
            valentine.setCreateTime(new Date());
            valentine.setUpdateTime(new Date());
            valentineMapper.insert(valentine);
            valentineVo = entityToCache(valentine);
        }
        return new BusiResult(BusiStatus.SUCCESS, valentineVo);
    }

    public BusiResult remove(Long uid){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        ValentineVo valentineVo = getOneByJedisId(uid.toString(), "1");
        if(valentineVo==null){
            return new BusiResult(BusiStatus.NOTEXISTS, "没有绑定过关系", "");
        }
        Valentine valentine = new Valentine();
        valentine.setId(valentineVo.getId());
        if(valentineVo.getMaleUid()==uid.intValue()){
            valentine.setMaleStatus(new Byte("2"));
        }
        if(valentineVo.getFemaleUid()==uid.intValue()){
            valentine.setFemaleStatus(new Byte("2"));
        }
        valentine.setValentineStatus(new Byte("2"));
        valentineMapper.updateByPrimaryKeySelective(valentine);
        jedisService.hdel(RedisKey.valentine.getKey(), valentineVo.getMaleUid()+"1");
        jedisService.hdel(RedisKey.valentine.getKey(), valentineVo.getFemaleUid()+"1");
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult getRank(){
        List<RankValentineVo> rankValentineVoList = valentineActivityListService.getListByJedisId("1");
        return new BusiResult(BusiStatus.SUCCESS, rankValentineVoList);
    }

}
