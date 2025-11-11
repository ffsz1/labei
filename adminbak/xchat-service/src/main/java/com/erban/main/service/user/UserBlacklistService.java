package com.erban.main.service.user;

import com.erban.main.model.UserBlacklist;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.UserBlacklistMapper;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserBlacklistVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserBlacklistService extends CacheBaseService<UserBlacklist, UserBlacklist> {
    @Autowired
    private UserBlacklistMapper userBlacklistMapper;
    @Autowired
    private UsersService usersService;

    @Override
    public UserBlacklist getOneByJedisId(String jedisId) {
        return getOne(RedisKey.user_blacklist.getKey(), jedisId, "select * from user_blacklist where black_id = ? and status = 1 ", jedisId);
    }

    @Override
    public UserBlacklist entityToCache(UserBlacklist entity) {
        return entity;
    }

    public String refreshBlacklistListCache(String jedisCode, String jedisKey) {
        return refreshListCacheByKey(null, jedisCode, jedisKey, "getBlackId", "select * from user_blacklist where uid = ? and status = 1 ", jedisKey);
    }

    public List<UserBlacklistVo> toVo(List<UserBlacklist> userBlacklistList){
        List<UserBlacklistVo> userBlacklistVoList = new ArrayList<>();
        if(userBlacklistList==null){
            return userBlacklistVoList;
        }
        UserBlacklistVo userBlacklistVo;
        for (UserBlacklist userBlacklist:userBlacklistList){
            userBlacklistVo=new UserBlacklistVo();
            userBlacklistVo.setBlackId(userBlacklist.getBlackId());
            userBlacklistVo.setBlacklistUid(userBlacklist.getBlacklistUid());
            Users users = usersService.getUsersByUid(userBlacklist.getBlacklistUid());
            if(users==null){
                userBlacklistVo.setUserNo(0L);
                userBlacklistVo.setNick("");
                userBlacklistVo.setAvatar("");
            }else {
                userBlacklistVo.setUserNo(users.getErbanNo());
                userBlacklistVo.setNick(users.getNick());
                userBlacklistVo.setAvatar(users.getAvatar());
            }
            userBlacklistVoList.add(userBlacklistVo);
        }
        return userBlacklistVoList;
    }

    public List<UserBlacklist> getBlacklist(Long uid, Integer pageNum, Integer pageSize){
        String str = jedisService.hget(RedisKey.user_blacklist_list.getKey(), uid.toString());
        if(StringUtils.isBlank(str)){
            str = refreshBlacklistListCache(RedisKey.user_blacklist_list.getKey(), uid.toString());
        }
        if (StringUtils.isBlank(str)) {
            return new ArrayList<>();
        }
        List<String> ids = StringUtils.splitToList(str, ",");
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        Integer size = ids.size();
        Integer skip = (pageNum - 1) * pageSize;
        if (skip < size) {
            if (skip + pageSize > size) {
                return getList(ids.subList(skip, size));
            } else {
                return getList(ids.subList(skip, skip + pageSize));
            }
        }
        return new ArrayList<>();
    }

    public BusiResult add(Long uid, Long blacklistUid){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Users users1 = usersService.getUsersByUid(blacklistUid);
        if(users1==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Integer isCan = jdbcTemplate.queryForObject("select COUNT(1) from user_blacklist where uid = ? and blacklist_uid = ? and `status` = 1", Integer.class, uid, blacklistUid);
        if(isCan>0){
            return new BusiResult(BusiStatus.NOTEXISTS, "已经拉黑过该用户", "");
        }
        UserBlacklist userBlacklist = new UserBlacklist();
        userBlacklist.setUid(uid);
        userBlacklist.setBlacklistUid(blacklistUid);
        userBlacklist.setStatus(new Byte("1"));
        userBlacklist.setCreateTime(new Date());
        userBlacklistMapper.insertSelective(userBlacklist);
        jedisService.hdel(RedisKey.user_blacklist_list.getKey(), uid.toString());
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult del(Long uid, String blacklistIds){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        UserBlacklist userBlacklist;
        StringBuffer data=new StringBuffer();
        StringBuffer fail=new StringBuffer();
        String[] idList = blacklistIds.split(",");
        for(String id:idList){
            userBlacklist = getOneByJedisId(id);
            if(userBlacklist==null){
                if(fail.length()==0){
                    fail.append(id);
                }else {
                    fail.append(","+id);
                }
            }else{
                userBlacklistMapper.deleteByPrimaryKey(Integer.valueOf(id));
                jedisService.hdel(RedisKey.user_blacklist.getKey(), id);
                if(data.length()==0){
                    data.append(id);
                }else {
                    data.append(","+id);
                }
            }
        }
        if(data.length()==0){
            return new BusiResult(BusiStatus.NOTEXISTS, "拉黑名单不存在", "");
        }
        jedisService.hdel(RedisKey.user_blacklist_list.getKey(), uid.toString());
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
