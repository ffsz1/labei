package com.erban.admin.main.service;

import com.erban.main.dto.AndroidReviewWhitelistDTO;
import com.erban.main.model.AndroidReviewWhitelist;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.AndroidReviewWhitelistMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/23
 * @time 17:38
 */
@Service
public class AndroidReviewWhitelistService {

    @Autowired
    private AndroidReviewWhitelistMapper androidReviewWhitelistMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UsersMapper usersMapper;


    public PageInfo<AndroidReviewWhitelistDTO> getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<AndroidReviewWhitelistDTO> list = androidReviewWhitelistMapper.selectByList(searchText);
        list.forEach(item ->{
        });
        return new PageInfo<>(list);
    }

    public int deleteById(Integer id) {
        AndroidReviewWhitelist androidReviewWhitelist =   androidReviewWhitelistMapper.selectByPrimaryKey(id);
        int status = androidReviewWhitelistMapper.deleteByPrimaryKey(androidReviewWhitelist.getId());
        if (status > 0){
            jedisService.hdel(RedisKey.general_review_whitelist.getKey(),androidReviewWhitelist.getUid().toString());
        }
        return status;
    }

    public int save(AndroidReviewWhitelist androidReviewWhitelist) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andErbanNoEqualTo(androidReviewWhitelist.getUid());
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if(usersList.size() > 0){
            androidReviewWhitelist.setUid(usersList.get(0).getUid());
            int status = androidReviewWhitelistMapper.insert(androidReviewWhitelist);
            if (status > 0){
                Gson gson = new Gson();
                jedisService.hset(RedisKey.general_review_whitelist.getKey(),androidReviewWhitelist.getUid().toString(),gson.toJson(androidReviewWhitelist));
            }
            return status;
        }else{
            return -1;
        }

    }
}
