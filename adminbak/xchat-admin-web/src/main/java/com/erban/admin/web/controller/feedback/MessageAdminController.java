
package com.erban.admin.web.controller.feedback;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.MsgPushAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.MsgPushRecord;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.api.QiniuService;
import com.github.pagehelper.PageInfo;
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

/**
 * 后台消息管理
 * Created by fxw on 2018/01/02
 */


@Controller
@RequestMapping("/admin/*")
public class MessageAdminController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MessageAdminController.class);

    @Autowired
    private MsgPushAdminService msgPushAdminService;

    @Autowired
    private QiniuService qiuniuService;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    /**
     * 消息列表
     * @param msgType   消息类型
     * @param erbanNOs  拉贝号(多个)
     */
    @RequestMapping(value = "msgAdmin/getMsgList")
    @ResponseBody
    public void getMsgList(Byte msgType, String erbanNOs) {
        PageInfo<MsgPushRecord> pageInfo = msgPushAdminService.getMsgList(getPageNumber(), getPageSize(), msgType,
                erbanNOs);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 保存消息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "messageAdmin/saveMessage")
    @ResponseBody
    public BusiResult saveMsg(String erbanNos, Byte msgType, Byte toObjType, String words, String pic, String title,
                              String desc, String picUrl, String webUrl, Byte skipType, String skipContent) {
        int adminId = getAdminId();
        if (adminId == -1) {
            return new BusiResult(BusiStatus.NOAUTHORITY);
        }
        return msgPushAdminService.saveMsg(erbanNos, msgType, toObjType, words, pic, title, desc, picUrl, webUrl,
                skipType, String.valueOf(adminId), skipContent);
    }

    @RequestMapping("deleteFlag")
    @ResponseBody
    public BusiResult deleteFlag() {
        // 删除正在发送消息的标记
        return msgPushAdminService.deleteFlag();
    }

    /**
     * 上传图片
     * @param uploadFile
     */
    @RequestMapping(value = "msg/headimg")
    @ResponseBody
    public void uploadImage(@RequestParam("uploadFile") MultipartFile uploadFile) {
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

    /**
     * 广播接口
     * @param from
     * @param param
     */
    @RequestMapping(value = "messageAdmin/broadCastMsg")
    @ResponseBody
    public void broadCastMsg(String from, String param) {
        sendSysMsgService.broadCastMsg(from, param);
    }
}
