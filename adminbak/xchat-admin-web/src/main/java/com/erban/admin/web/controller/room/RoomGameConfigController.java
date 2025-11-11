package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.room.RoomGameConfigService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.RoomGameConfigDTO;
import com.erban.main.model.RoomGameConfig;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 下午5:41
 */
@Controller
@RequestMapping("/admin/room/game")
public class RoomGameConfigController extends BaseController {

    @Autowired
    private RoomGameConfigService roomGameConfigService;

    @Autowired
    private JedisService jedisService;

    @RequestMapping("/getList")
    @ResponseBody
    public void getAll(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<RoomGameConfigDTO> pageInfo = roomGameConfigService.getListWithPage(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value="/save")
    @ResponseBody
    public void save(RoomGameConfig roomGameConfig){
        int result = -1;
        if(roomGameConfig != null){
            try{
                result = roomGameConfigService.save(roomGameConfig,getAdminId());
                if(result > 0){
                    if(DateUtil.betweenStrToDate(roomGameConfig.getStart(),roomGameConfig.getEnd())){
                        Gson gson = new Gson();
                        jedisService.hset(RedisKey.room_game_config.getKey(),String.valueOf(roomGameConfig.getUid()),gson.toJson(roomGameConfig));
                    }
                }
            } catch (Exception e){
                logger.warn("save fail,cause by " + e.getMessage(),e);
            }
        }else{
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 删除
     * @param id
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public void del(Integer id)
    {
        try {
            RoomGameConfig roomGameConfig = roomGameConfigService.selectById(id);
            int result = roomGameConfigService.delete(id);
            if(result > 0) {
                jedisService.hdel(RedisKey.room_game_config.getKey(),String.valueOf(roomGameConfig.getUid()));
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete gift, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }
}
