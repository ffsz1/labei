package com.erban.admin.main.service.room;

import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.UserRoomConsumeDTO;
import com.erban.admin.main.dto.UserRoomDTO;
import com.erban.admin.main.mapper.UserRoomConsumeConfDAO;
import com.erban.admin.main.mapper.UserRoomConsumeDAO;
import com.erban.admin.main.service.room.vo.UserRoomConsumeVO;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/10 001015:43
 */
@Service
public class StatRoomConsumeService {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserRoomConsumeConfDAO userRoomConsumeConfDAO;
    @Autowired
    private UserRoomConsumeDAO userRoomConsumeDAO;

    private static int max_days = 30;

    /**
     * 统计房间消费情况
     */
    public BusiResult countRoomConsume(Long erbanNo) {
        Users user = new Users();
        if (erbanNo != null) {
            user = usersService.getUsersByErBanNo(erbanNo);
            if (user == null) {
                throw new BusinessException("该用户信息不存在");
            }
        }
        Date endDate = DateUtil.addDay(new Date(), 1);
        Date beginDate = DateUtil.addDay(endDate, - max_days);
        // 查询记录信息
        List<UserRoomConsumeDTO> list = userRoomConsumeDAO.countUserConsume(
                user.getUid(),
                DateUtil.date2Str(beginDate, DateUtil.DateFormat.YYYY_MM_DD),
                DateUtil.date2Str(endDate, DateUtil.DateFormat.YYYY_MM_DD));
        Map<String, Integer> dateMap = Maps.newHashMap();
        Date temp;
        for(int i = 0; i < max_days; i ++) {
            temp = DateUtil.addDay(endDate, - i);
            dateMap.put(DateUtil.date2Str(temp, DateUtil.DateFormat.YYYY_MM_DD), i);
        }
        Long roomUid = 1L;
        UserRoomConsumeVO vo = new UserRoomConsumeVO();
        Integer index;
        List<UserRoomConsumeVO> result = Lists.newArrayList();
        Class<?> voClass = UserRoomConsumeVO.class;
        for(UserRoomConsumeDTO dto : list) {
            if (!roomUid.equals(dto.getRoomUid())) {
                if (vo.getRoomUid() != null) {
                    result.add(vo);
                }
                // roomUid 不同, 表示是新的一个分组
                roomUid = dto.getRoomUid();
                vo = new UserRoomConsumeVO();
                vo.setRoomUid(dto.getRoomUid());
                vo.setTitle(dto.getTitle());
                vo.setErbanNo(dto.getErbanNo());
            }
            index = dateMap.get(dto.getFirstDate());
            if (index != null) {
                try {
                    Method setDay = voClass.getMethod("setDay" + index, Integer.class);
                    setDay.invoke(vo, dto.getUserNum());
                } catch (Exception e) {

                }
            }
        }
        result.add(vo);
        Map<String, Object> map = Maps.newHashMap();
        map.put("rows", result);
        map.put("total", result.size());
        return new BusiResult(BusiStatus.SUCCESS, map);
    }

    /**
     *
     * @param erbanNo
     * @return
     * @throws BusinessException
     */
    public boolean addRoom(Long erbanNo) throws BusinessException {
        Users users = usersService.getUsresByErbanNo(erbanNo);
        if (users == null) {
            throw new BusinessException("用户信息不存在");
        }
        Room room = roomService.getRoomByUid(users.getUid());
        if (room == null) {
            throw new BusinessException("房间信息不存在");
        }
        Long roomUid = userRoomConsumeConfDAO.getByUid(users.getUid());
        if (roomUid != null) {
            throw new BusinessException("房间信息已经存在");
        }
        int count = userRoomConsumeConfDAO.insert(users.getUid());
        return count > 0;
    }

    /**
     *
     * @param uid
     * @return
     * @throws BusinessException
     */
    public boolean delRoom(Long uid) throws BusinessException {
        int count = userRoomConsumeConfDAO.delete(uid);
        return count > 0;
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param erbanNo
     * @return
     */
    public Map<String, Object> listRoom(Integer pageNum, Integer pageSize, Long erbanNo) {
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = Maps.newHashMap();
        List<UserRoomDTO> list = userRoomConsumeConfDAO.listRoom(erbanNo);
        PageInfo page = new PageInfo(list);
        map.put("rows", page.getList());
        map.put("total", page.getTotal());
        return map;
    }
}
