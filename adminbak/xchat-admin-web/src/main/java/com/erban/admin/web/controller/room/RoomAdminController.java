package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.RoomSearchAdminService;
import com.erban.admin.main.vo.RoomVo;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Room;
import com.erban.main.model.RoomTag;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.api.QiniuService;
import com.erban.main.service.room.RoomService;
import com.github.pagehelper.PageInfo;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.InitQueueRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin/roomAdmin")
@ResponseBody
public class RoomAdminController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RoomAdminController.class);

    @Autowired
    private RoomSearchAdminService roomSearchAdminService;

    @Autowired
    private QiniuService qiuniuService;

    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @Autowired
    private RoomService roomService;

    @RequestMapping("/getAll")
    @ResponseBody
    public void getAllRoom(String erbanNo, Byte type, Byte isPermitRoom) {
        PageInfo<Room> pageInfo = roomSearchAdminService.getAllRoomList(getPageNumber(), getPageSize(), erbanNo, type
                , isPermitRoom);
        List<RoomVo> list = roomSearchAdminService.convertToRoomVoList(pageInfo.getList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 房间展示/不展示
     *
     * @param uid
     * @return
     */
    @RequestMapping("/changeRoomStatus")
    @ResponseBody
    public BusiResult changeRoomStatus(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = roomSearchAdminService.setRoomStatus(uid);
        return busiResult;
    }

    @RequestMapping("/getRoomTags")
    @ResponseBody
    public BusiResult<List<RoomTag>> getRoomTags(String name) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<RoomTag> list = roomSearchAdminService.getRoomTag(name);
            busiResult.setData(list);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }

    @RequestMapping("/saveRoom")
    @ResponseBody
    public BusiResult<Room> saveRoom(Integer uid, Integer tagId, String title, String badge, String backPic,
                                     Byte isPermitRoom, Long rewardMoney, Integer audioLevel, Integer charmOpen,
                                     Integer faceType) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        if (rewardMoney == null) {
            rewardMoney = 0L;
        } else if (rewardMoney < 0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            Room room = roomSearchAdminService.saveRoom(uid, tagId, title, badge, backPic, isPermitRoom, rewardMoney,
                    audioLevel, charmOpen, faceType);
            busiResult.setData(room);
        } catch (Exception e) {
            logger.error("SaveRoom failed..uid:{},e:{}", uid, e);
            BusiResult br = new BusiResult(BusiStatus.SERVERERROR);
            br.setData(e.getMessage());
            return br;
        }

        return busiResult;
    }

    @RequestMapping(value = "/headBadge")
    @ResponseBody
    public void uploadBadge(@RequestParam("uploadBadge") MultipartFile uploadFile) {
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            try {
                String filepath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                if (!BlankUtil.isBlank(filepath)) {
                    jsonObject.put("path", qiuniuService.mergeUrlAndSlim(filepath));
                }
            } catch (Exception e) {
                logger.error("upload fail, " + e.getMessage());
                msg = "上传失败，I/O流异常";
            }
        } else {
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg", msg);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/headBackground")
    @ResponseBody
    public void uploadBackground(@RequestParam("uploadBackground") MultipartFile uploadFile) {
        String msg = null;
        JSONObject jsonObject = new JSONObject();
        if (!uploadFile.isEmpty()) {
            try {
                String filepath = qiuniuService.uploadByStream(uploadFile.getInputStream());
                if (!BlankUtil.isBlank(filepath)) {
                    jsonObject.put("path", qiuniuService.mergeUrlAndSlim(filepath));
                }
            } catch (Exception e) {
                logger.error("upload fail, " + e.getMessage());
                msg = "上传失败，I/O流异常";
            }
        } else {
            msg = "上传失败，表单类型不正确！";
        }
        jsonObject.put("msg", msg);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getRoomMicList")
    @ResponseBody
    public BusiResult getRoomMicList(@RequestParam(value = "erbanNo", required = false) Long erbanNo) {
        if (erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return roomSearchAdminService.getRoomMicList(erbanNo);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
    }

    @RequestMapping("initQueue")
    @ResponseBody
    public BusiResult initQueue(Long uid) {
        // /nimserver/chatroom/queueInit.action
        try {
            Room room = roomService.getRoomByUid(uid);
            if (room == null) {
                return new BusiResult(BusiStatus.ROOMNOTEXIST);
            }
            InitQueueRet ret = erBanNetEaseService.initQueue(room.getRoomId(), 100);
            if (ret.getCode() == 200) {
                return new BusiResult(BusiStatus.SUCCESS);
            } else {
                return new BusiResult(ret.getCode(), String.valueOf(ret.getDesc()), null);
            }
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }
}
