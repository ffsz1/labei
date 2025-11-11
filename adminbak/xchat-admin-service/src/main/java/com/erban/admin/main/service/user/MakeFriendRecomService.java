package com.erban.admin.main.service.user;

import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.MakeFriendDTO;
import com.erban.admin.main.mapper.MakeFriendRecomMapper;
import com.erban.admin.main.model.MakeFriendRecom;
import com.erban.admin.main.model.MakeFriendRecomExample;
import com.erban.main.model.Users;
import com.erban.main.service.user.UsersService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/11 001112:07
 */
@Service
public class MakeFriendRecomService {

    @Autowired
    private MakeFriendRecomMapper makeFriendRecomMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JedisService jedisService;

    /**
     * 列表查询
     * @param erbanNo
     * @param number
     * @param size
     * @return
     */
    public Map<String, Object> getList(Long erbanNo, Integer number, Integer size){
        Map<String, Object> map = Maps.newHashMap();
        if (erbanNo == null) {
            Set<Tuple> set = jedisService.zrevrangeWithScores(RedisKey.charm_recom_source.getKey(), (number - 1) * size, (number * size) -1);
            int total = makeFriendRecomMapper.countByExample(new MakeFriendRecomExample());
            List<MakeFriendDTO> list = Lists.newArrayList();
            set.stream().forEach(t -> {
                String uid = t.getElement();
                double source = t.getScore();
                Users user = usersService.getUsersByUid(Long.valueOf(uid));
                if (user != null) {
                    MakeFriendDTO vo = new MakeFriendDTO();
                    vo.setUid(user.getUid());
                    vo.setAvatar(user.getAvatar());
                    vo.setErbanNo(user.getErbanNo());
                    vo.setNick(user.getNick());
                    vo.setSource(source);
                    MakeFriendRecom recom = makeFriendRecomMapper.selectByPrimaryKey(Long.valueOf(uid));
                    if (recom != null) {
                        vo.setInitSource(recom.getSource());
                    } else {
                        vo.setInitSource(0L);
                    }
                    list.add(vo);
                }
            });
            map.put("total", total);
            map.put("rows", list);
            return map;
        }
        Users user = usersService.getUsresByErbanNo(erbanNo);
        if (user != null) {
            MakeFriendRecom recom = makeFriendRecomMapper.selectByPrimaryKey(user.getUid());
            if (recom != null) {
                Double source = jedisService.zscore(RedisKey.charm_recom_source.getKey(), user.getUid().toString());
                MakeFriendDTO vo = new MakeFriendDTO();
                vo.setUid(user.getUid());
                vo.setAvatar(user.getAvatar());
                vo.setErbanNo(user.getErbanNo());
                vo.setNick(user.getNick());
                vo.setSource(source);
                map.put("total", 1);
                map.put("rows", Lists.newArrayList(vo));
                return map;
            }
        }
        map.put("total", 0);
        map.put("rows", Lists.newArrayList());
        return map;
    }

    /**
     * 添加推荐用户
     * @param erbanNo
     * @param source
     * @return
     */
    public boolean saveRecomUser(Long uid, Long erbanNo, Long source) {
        source = source == null ? 0L : source;
        int count;
        if (uid == null) {
            // 添加
            Users users = usersService.getUsresByErbanNo(erbanNo);
            if (users == null) {
                throw new BusinessException("用户信息不存在!");
            }
            MakeFriendRecom recom = makeFriendRecomMapper.selectByPrimaryKey(users.getUid());
            if (recom != null) {
                throw new BusinessException("该有用户已经存在推荐列表");
            }
            recom = new MakeFriendRecom();
            recom.setCreateDate(new Date());
            recom.setUid(users.getUid());
            recom.setSource(source);
            count = makeFriendRecomMapper.insert(recom);
            if (count > 0) {
                // 添加到缓存中
                jedisService.hset(RedisKey.charm_recom_user.getKey(), users.getUid().toString(), source.toString());
                jedisService.zincrby(RedisKey.charm_recom_source.getKey(), users.getUid().toString(), source);
            }
            return count > 0;
        } else {
            // 修改
            MakeFriendRecom recom = new MakeFriendRecom();
            recom.setUid(uid);
            recom.setSource(source);
            count = makeFriendRecomMapper.updateByPrimaryKeySelective(recom);
            jedisService.hset(RedisKey.charm_recom_user.getKey(), uid.toString(), source.toString());
            return count > 0;
        }
    }

    /**
     * 删除推荐用户
     * @param uid
     * @return
     */
    public boolean delUser(Long uid) {
        if (uid == null) {
            return false;
        }
        int count = makeFriendRecomMapper.deleteByPrimaryKey(uid);
        if (count > 0) {
            jedisService.zrem(RedisKey.charm_recom_source.getKey(), uid.toString());
            jedisService.hdel(RedisKey.charm_recom_user.getKey(), uid.toString());
        }
        return count > 0;
    }

    /**
     * 添加排序分值
     * @param uid
     * @param source
     * @return
     */
    public boolean addSource(Long uid, Long source) {
        MakeFriendRecom recom = makeFriendRecomMapper.selectByPrimaryKey(uid);
        if (recom == null) {
            throw new BusinessException("该用户不在推荐列表");
        }
        if (source != null) {
            jedisService.zincrby(RedisKey.charm_recom_source.getKey(), uid.toString(), source);
        }
        return true;
    }

    public void initCharmRecomSource() {
        List<MakeFriendRecom> list = makeFriendRecomMapper.selectByExample(new MakeFriendRecomExample());
        jedisService.del(RedisKey.charm_recom_user.getKey());
        jedisService.del(RedisKey.charm_recom_source.getKey());
        if (list != null) {
            list.forEach(recomDO -> {
                Long source = recomDO.getSource() == null ? 0 : recomDO.getSource();
                jedisService.hincrBy(RedisKey.charm_recom_user.getKey(), recomDO.getUid().toString(), source);
            });
            list.stream()
                    .filter(r -> r.getSource() != null)
                    .forEach(recomDO -> jedisService.zincrby(RedisKey.charm_recom_source.getKey(), recomDO.getUid().toString(), recomDO.getSource()));
        }
    }

}
