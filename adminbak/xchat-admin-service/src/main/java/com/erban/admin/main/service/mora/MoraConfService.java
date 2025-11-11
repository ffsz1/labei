package com.erban.admin.main.service.mora;

import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.MoraConfDTO;
import com.erban.main.model.MoraConfig;
import com.erban.main.model.Room;
import com.erban.main.mybatismapper.MoraConfigMapper;
import com.erban.main.service.room.RoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-30
 * @time 09:49
 */
@Service
public class MoraConfService {

    @Autowired
    private MoraConfigMapper moraConfigMapper;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RoomService roomService;

    
    public PageInfo<MoraConfDTO> getListWithPage(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<MoraConfDTO> list = moraConfigMapper.selectByPage(searchText);
        if(list.size() > 0){
            list.forEach(item ->{
                Room room = roomService.getRoomByUid(item.getUid());
                if(room != null){
                    item.setRoomTitle(room.getTitle());
                }
                if(item.getAdminId() != null){
                    item.setAdminName(adminUserService.getAdminUserById(item.getAdminId()).getUsername());
                }
            });
        }
        return new PageInfo<>(list);
    }

    public MoraConfig getByUid(Long uid) {
        return moraConfigMapper.selectByUid(uid);
    }

    public int save(MoraConfig moraConfig, boolean isEdit, int adminId) {
        int result = 0;
        if(!isEdit){
            moraConfig.setAdminId(adminId);
            moraConfig.setStatus(1);
            moraConfig.setCreateTime(new Date());
            result = moraConfigMapper.insert(moraConfig);
        }else{
            moraConfig.setAdminId(adminId);
            moraConfig.setStatus(1);
            result = moraConfigMapper.updateByPrimaryKeySelective(moraConfig);
        }
        return result;
    }

    public MoraConfig getById(Integer id) {
        return moraConfigMapper.selectByPrimaryKey(id);
    }

    public int delete(Integer id) {
        return moraConfigMapper.deleteByPrimaryKey(id);
    }
}
