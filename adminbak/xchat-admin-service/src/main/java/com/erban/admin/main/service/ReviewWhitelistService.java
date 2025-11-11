package com.erban.admin.main.service;

import com.erban.main.dto.ReviewWhitelistDTO;
import com.erban.main.model.ReviewWhitelist;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.ReviewWhitelistMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/19
 * @time 14:03
 */
@Service
public class ReviewWhitelistService {

    @Autowired
    private JedisService jedisService;

    @Autowired
    private ReviewWhitelistMapper reviewWhitelistMapper;

    @Autowired
    private UsersMapper usersMapper;

    public PageInfo<ReviewWhitelistDTO> getList(Integer pageNumber, Integer pageSize, String search){
        PageHelper.startPage(pageNumber,pageSize);
        List<ReviewWhitelistDTO> reviewWhitelistList = reviewWhitelistMapper.selectByPage(search);
        return new PageInfo<>(reviewWhitelistList);
    }


    public int save(ReviewWhitelist reviewWhitelist){
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andErbanNoEqualTo(reviewWhitelist.getUid());
        Users users = usersMapper.selectByExample(usersExample).get(0);
        reviewWhitelist.setCreateTime(new Date());
        reviewWhitelist.setUid(users.getUid());
        int status = reviewWhitelistMapper.insert(reviewWhitelist);
        if(status > 0 ){
            jedisService.del(RedisKey.review_whitelist.getKey());
        }
        return status;
    }

    public int delete(Long id){
        jedisService.del(RedisKey.review_whitelist.getKey());
        return reviewWhitelistMapper.deleteByPrimaryKey(id);
    }
}
