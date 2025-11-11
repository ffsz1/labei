package com.erban.admin.main.service.room;

import com.erban.main.dto.RoomGameConfigDTO;
import com.erban.main.model.RoomGameConfig;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.RoomGameConfigMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 下午5:27
 */
@Service
public class RoomGameConfigService {

    @Autowired
    private RoomGameConfigMapper roomGameConfigMapper;

    @Autowired
    private UsersMapper usersMapper;

    public PageInfo<RoomGameConfigDTO> getListWithPage(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<RoomGameConfigDTO> list = roomGameConfigMapper.selectByPage(searchText);
        return new PageInfo<>(list);
    }

    public int save(RoomGameConfig roomGameConfig,Integer adminId){
        roomGameConfig.setStatus(1);
        roomGameConfig.setCreateTime(new Date());
        roomGameConfig.setAdminId(adminId);
        UsersExample example = new UsersExample();
        example.createCriteria().andErbanNoEqualTo(roomGameConfig.getUid());
        List<Users> users = this.usersMapper.selectByExample(example);
        roomGameConfig.setUid(users.get(0).getUid());
        return roomGameConfigMapper.save(roomGameConfig);
    }

    public int delete(int id){
        return roomGameConfigMapper.deleteByPrimaryKey(id);
    }

    public RoomGameConfig selectById(Integer id) {
        return roomGameConfigMapper.selectByPrimaryKey(id);
    }
}
