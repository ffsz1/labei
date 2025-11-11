package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.RoomTagService;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.frame.MvcContext;
import com.erban.main.model.Room;
import com.erban.main.model.RoomTag;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 房间标签控制器
 */
@Controller
@RequestMapping("/admin/roomtag")
public class RoomTagController extends BaseController {
    @Autowired
    private RoomTagService roomTagService2;

    /**
     * 获取所有房间标签
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/getlist")
    @ResponseBody
    public BusiResult getRoomTagList(String name) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult.setData(roomTagService2.getRoomTags(name));
        } catch (Exception e) {
            logger.error("getRoomTagList error", e);
        }
        return busiResult;
    }

    /**
     * 获取所有房间标签
     *
     * @return
     */
    @RequestMapping(value = "/getRoomTagList")
    @ResponseBody
    public void getRoomTagList() {
        JSONObject jsonObject = new JSONObject();

        PageInfo<RoomTag> pageInfo = roomTagService2.getAllRoomTags(getPageNumber(), getPageSize());
        List<RoomTag> roomTagList = pageInfo.getList();

        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", roomTagList);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 根据TagID获取房间标签
     *
     * @param tagId
     * @return
     */
    @RequestMapping(value = "/getone")
    @ResponseBody
    public BusiResult getRoomTag(Integer tagId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            if (tagId == null) {
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            busiResult.setData(roomTagService2.getRoomTagById(tagId));
        } catch (Exception e) {
            logger.error("getRoomTagById error", e);
        }
        return busiResult;
    }

    /**
     * 保存/新增房间标签
     *
     * @param roomTag 房间标签类
     * @param isEdit  是否为编辑
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public BusiResult saveRoomTag(RoomTag roomTag, boolean isEdit) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult.setCode(roomTagService2.saveRoomTag(roomTag, isEdit));
        } catch (Exception e) {
            logger.error("saveRoomTag error", e);
            busiResult.setData(e.getMessage());
        }
        return busiResult;
    }

    /**
     * 删除房间标签
     *
     * @param tagId
     * @return
     */
    @RequestMapping(value = "/del")
    @ResponseBody
    public BusiResult delRoomTag(Integer tagId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            if (tagId == null) {
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            busiResult.setData(roomTagService2.deleteRoomTag(tagId));
        } catch (Exception e) {
            logger.error("delRoomTag error", e);
        }
        return busiResult;
    }

    protected HttpServletRequest getRequest() {
        return MvcContext.getRequest();
    }

    public int getPageNumber(){
        String tmp = getRequest().getParameter("pageNumber");
        if(BlankUtil.isBlank(tmp)){
            return 1;
        }
        return Integer.valueOf(tmp);

    }

    public int getPageSize(){
        String tmp = getRequest().getParameter("pageSize");
        if(BlankUtil.isBlank(tmp)){
            return 10;
        }
        return Integer.valueOf(tmp);
    }
}
