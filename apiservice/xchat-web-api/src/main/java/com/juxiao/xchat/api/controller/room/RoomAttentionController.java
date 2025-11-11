package com.juxiao.xchat.api.controller.room;

/**
 * @author chris
 * @Title:
 * @date 2018/11/28
 * @time 16:11
 */

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.room.RoomAttentionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @Title:
 * @date 2018/11/5
 * @time 10:46
 */
@RestController
@RequestMapping("/room/attention")
@Api(value="房间关注",tags={"房间关注相关接口"})
public class RoomAttentionController {
    @Autowired
    private RoomAttentionService attentionService;

    /**
     * 根据uid获取房间信息
     */
    @Authorization
    @RequestMapping(value = "getRoomAttentionByUid", method = RequestMethod.GET)
    @ApiOperation("根据uid获取关注房间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="pageNum",value="当前页",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="pageSize",value="每页显示数",dataType="long", paramType = "query")
    })
    public WebServiceMessage getRoomAttentionByUid(@RequestParam(value = "uid") Long uid, @RequestParam(value = "pageNum", required = false,defaultValue = "0") Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false ,defaultValue = "20") Integer pageSize){
        return WebServiceMessage.success(attentionService.selectUidByRoomAttentions(uid,pageNum,pageSize));
    }

    @RequestMapping(value = "attentions", method = RequestMethod.POST)
    @Authorization
    @ApiOperation("关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="roomId",value="房间ID",dataType="long", paramType = "query"),
    })
    public WebServiceMessage attentions(@RequestParam(value = "uid")Long uid,@RequestParam(value = "roomId")Long roomId){
        return attentionService.insert(uid,roomId);
    }

    /**
     * 检测
     */
    @RequestMapping(value = "checkAttentions", method = RequestMethod.POST)
    @Authorization
    @ApiOperation("检查是否已关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="roomId",value="房间ID",dataType="long", paramType = "query"),
    })
    public WebServiceMessage checkAttentions(@RequestParam(value = "uid")Long uid,@RequestParam(value = "roomId")Long roomId){
        return WebServiceMessage.success(attentionService.checkAttentions(uid,roomId));
    }


    /**
     * 删除关注
     */
    @RequestMapping(value = "delAttentions", method = RequestMethod.POST)
    @Authorization
    @ApiOperation("删除关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="roomId",value="房间ID",dataType="string", paramType = "query"),
    })
    public WebServiceMessage delAttentions(@RequestParam(value = "uid")Long uid,@RequestParam(value = "roomId")String roomId){
        return WebServiceMessage.success(attentionService.delAttentions(uid,roomId));
    }
}
