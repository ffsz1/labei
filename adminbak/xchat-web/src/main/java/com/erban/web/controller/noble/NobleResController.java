package com.erban.web.controller.noble;

import com.erban.main.model.NobleRes;
import com.erban.main.model.NobleUsers;
import com.erban.main.model.Room;
import com.erban.main.service.noble.NobleResService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.room.RoomService;
import com.erban.web.common.BaseController;
import com.google.common.collect.Lists;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/noble/res")
public class NobleResController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NobleResController.class);
    private static final int SIZE = 12;

    @Autowired
    private NobleResService nobleResService;
    @Autowired
    private NobleRightService nobleRightService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private RoomService roomService;

    /**
     * 获取某种贵族资源的列表，分页返回
     *
     * @param type
     * @param page
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    @Authorization
    public BusiResult getResList(Long uid, byte type, Integer page) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (page == null || page < 1) {
            page = 1;
        }
        try {
            Integer defId = 0;
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            if (nobleUsers != null) {
                switch (type) {
                    case Constant.NobleResType.roombg:
                        defId = nobleUsers.getRoomBackgroundId();
                        break;
                    case Constant.NobleResType.bubble:
                        defId = nobleUsers.getChatBubbleId();
                        break;
                }
            }
            if(defId == null) defId = 0;
            NobleRes nobleRes = nobleResService.getResOne(defId);
            List<NobleRes> list = null;
            if(nobleRes == null) {
                list = nobleResService.getResListByPage(type, defId, page);
            } else {
                if (page == 1) {
                    nobleRes.setTmpint(1);
                    // TODO 页大小改成12
                    list = nobleResService.getResListByPage(type, defId, (page-1) * SIZE, SIZE - 1);
                    if (!BlankUtil.isBlank(list)) {
                        list.add(0, nobleRes);
                    } else {
                        list = Lists.newArrayList();
                        list.add(nobleRes);
                    }
                } else {
                    // TODO 页大小改成12
                    list = nobleResService.getResListByPage(type, defId, (page-1) * SIZE - 1, SIZE);
                }
            }
            return new BusiResult(BusiStatus.SUCCESS, list);
        } catch (Exception e) {
            logger.error("getResList error, type:" + type, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 获取某个特定的资源
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getone")
    @ResponseBody
    @Authorization
    public BusiResult getResOne(Long uid, int id) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            NobleRes nobleRes = nobleResService.getResOne(id);
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            if (nobleUsers != null) {
                switch (nobleRes.getResType()) {
                    case Constant.NobleResType.roombg:
                        if(nobleRes.getId().equals(nobleUsers.getRoomBackgroundId())){
                            nobleRes.setTmpint(1);
                        }
                        break;
                    case Constant.NobleResType.bubble:
                        if(nobleRes.getId().equals(nobleUsers.getChatBubbleId())){
                            nobleRes.setTmpint(1);
                        }
                        break;
                }
            }
            return new BusiResult(BusiStatus.SUCCESS, nobleRes);
        } catch (Exception e) {
            logger.error("getResOne error, id:" + id, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 选择房间背景
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/select/roombg")
    @ResponseBody
    @Authorization
    public BusiResult selectRoombg(Long uid, int id) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            NobleRes nobleRes = nobleResService.getResOne(id);
            if (nobleRes == null) {
                return new BusiResult(BusiStatus.NOTEXISTS);
            }
            if (nobleRes.getNobleId() > 0) {
                NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
                if (nobleUsers == null || nobleUsers.getNobleId() < nobleRes.getNobleId()) {
                    return new BusiResult(BusiStatus.NOAUTHORITY);
                }
                nobleUsers.setRoomBackground(nobleRes.getValue());
                nobleUsers.setRoomBackgroundId(nobleRes.getId());
                nobleUsersService.updateNobleUserDbAndCache(nobleUsers);
                logger.info("selectRoombg updateNobleUserDbAndCache success, uid:{}, id:{}", uid, id);
            }
            Room room = roomService.getRoomByUid(uid);
            room.setBackPic(nobleRes.getValue());
            room.setDefBackpic(nobleRes.getValue());
            roomService.updateRoomNeteaseAndDB(room);
            logger.info("selectRoombg updateRoomNeteaseAndDB success, uid:{}, id:{}", uid, id);
            return new BusiResult(BusiStatus.SUCCESS, nobleRes);
        } catch (Exception e) {
            logger.error("getResOne error, id:" + id, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}
