package com.erban.admin.main.service.user;

import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.vo.UsersVo;
import com.erban.main.model.UserConfigure;
import com.erban.main.model.UserConfigureExample;
import com.erban.main.mybatismapper.UserConfigureMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.service.user
 * @date 2018/8/16
 * @time 09:59
 */
@Service
public class UserConfiguresService {

    @Autowired
    private UserConfigureMapper userConfigureMapper;

    @Autowired
    private UserMapperExpand userMapperExpand;

    @Autowired
    private JedisService jedisService;

    public PageInfo<UserConfigure> getListWithPage(Long erbanNo, Integer roomType, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (pageNumber - 1) * pageSize;
        PageHelper.startPage(pageNumber, pageSize);
        UserConfigureExample example = new UserConfigureExample();
        UserConfigureExample.Criteria criteria = example.createCriteria();
        if(erbanNo != null){
            UsersVo users = userMapperExpand.selectUidByErbanNo(erbanNo);
            criteria.andUidEqualTo(users.getUid());
        }
        if (roomType != null) {
            switch (roomType) {
                case 1:
                    criteria.andNewUsersEqualTo((byte) 1);
                    break;
                case 2:
                    criteria.andGreenRecomEqualTo((byte) 1);
                    break;
                case 3:
                    criteria.andGameRoomEqualTo((byte) 1);
                    break;
                case 4:
                    criteria.andSuperiorBounsEqualTo((byte) 1);
                    break;
            }
        }
        List<UserConfigure> list = userConfigureMapper.selectByExample(example);
        list.stream().forEach(item -> {
            UsersVo users = userMapperExpand.selectUidByUsers(item.getUid());
            item.setUid(users.getErbanNo());
        });
        return new PageInfo<>(list);
    }

    public int saveUserConfigure(UserConfigure userConfigure, boolean isEdit) {
        if(isEdit){
            UsersVo users = userMapperExpand.selectUidByErbanNo(userConfigure.getUid());
            userConfigure.setUid(users.getUid());
            userConfigureMapper.updateByPrimaryKeySelective(userConfigure);
            jedisService.hdel(RedisKey.user_configure.getKey(),String.valueOf(users.getUid()));
        }else{
            UsersVo users = userMapperExpand.selectUidByErbanNo(userConfigure.getUid());
            userConfigure.setUid(users.getUid());
            userConfigure.setGameRoom((byte)0);
            userConfigure.setGreenRecom((byte)0);
            userConfigure.setNewUsers((byte)0);
            userConfigureMapper.insert(userConfigure);
        }
        return 1;
    }

    public UserConfigure getOne(Integer id) {
        UsersVo users = userMapperExpand.selectUidByErbanNo(Long.valueOf(id));
        UserConfigure userConfigure = userConfigureMapper.selectByPrimaryKey(users.getUid());
        userConfigure.setUid(Long.valueOf(id));
        return userConfigure;
    }

    public int delete(Long id) {
        if (id == null) {
            return 0;
        }
        UsersVo users = userMapperExpand.selectUidByErbanNo(id);
        if (users == null) {
            return 0;
        }
        int count = userConfigureMapper.deleteByPrimaryKey(users.getUid());
        if (count > 0) {
            jedisService.hdel(RedisKey.user_configure.getKey(),String.valueOf(users.getUid()));
        }
        return count;
    }
}
