package com.erban.admin.main.service.room;

import com.erban.main.model.Room;
import com.erban.main.model.RoomExample;
import com.erban.main.model.RoomTag;
import com.erban.main.model.RoomTagExample;
import com.erban.main.mybatismapper.RoomTagMapper;
import com.erban.main.service.base.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("roomTagService2")
public class RoomTagService extends BaseService {
    @Autowired
    private RoomTagMapper roomTagMapper;

    public RoomTag getRoomTagById(Integer tagId) {
        return roomTagMapper.selectByPrimaryKey(tagId);
    }

    /**
     * 根据名称查询对应的房间标签
     *
     * @param name
     * @return
     */
    public List<RoomTag> getRoomTags(String name) {
        RoomTagExample example = new RoomTagExample();
        RoomTagExample.Criteria criteria = example.createCriteria().andStatusEqualTo(true);
        if (!BlankUtil.isBlank(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        example.setOrderByClause("seq desc");
        return roomTagMapper.selectByExample(example);
    }

    /**
     * 根据名称查询对应的房间标签
     *
     * @return
     */
    public PageInfo<RoomTag> getAllRoomTags(Integer pageNumber, Integer pageSize) {
        RoomTagExample example = new RoomTagExample();
        example.setOrderByClause("seq desc");
        PageHelper.startPage(pageNumber, pageSize);
        List<RoomTag> roomTagList = roomTagMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(roomTagList);
        return pageInfo;
    }

    public int insertRoomTag(RoomTag roomTag) {
        return roomTagMapper.insertSelective(roomTag);
    }

    public int updateRoomTag(RoomTag roomTag) {
        return roomTagMapper.updateByPrimaryKeySelective(roomTag);
    }

    public int deleteRoomTag(Integer tagId) {
        return roomTagMapper.deleteByPrimaryKey(tagId);
    }

    public int saveRoomTag(RoomTag roomTag, boolean isEdit) {
        if (isEdit) {
            return updateRoomTag(roomTag);
        } else {
            roomTag.setCreateTime(new Date());
            return insertRoomTag(roomTag);
        }
    }
}
