package com.erban.admin.web.controller.audit;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.ChannelIpDTO;
import com.erban.admin.main.service.audit.ChannelService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.*;
import com.erban.main.model.dto.RoomDTO;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: alwyn
 * @Description: 渠道审核管理
 * @Date: 2018/10/19 17:29
 */
@RequestMapping("/admin/channel")
@Controller
public class ChannelController extends BaseController {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private JedisService jedisService;

    @RequestMapping("list")
    @ResponseBody
    public void list() {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Gift> pageInfo = channelService.page(getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("get/{id}")
    @ResponseBody
    public BusiResult get(@PathVariable Integer id) {
        //
        return new BusiResult(BusiStatus.SUCCESS, channelService.get(id));
    }

    @RequestMapping("save")
    @ResponseBody
    public BusiResult save(Channel channel) {
        //
        try {
            int count = channelService.save(channel);
            if (count > 0) {
                jedisService.del(RedisKey.channel_audit.getKey());
            }
            return new BusiResult(count > 0 ? BusiStatus.SUCCESS : BusiStatus.SERVERERROR);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("addRoom")
    @ResponseBody
    public BusiResult addRoom(Integer id, String uids) {
        //
        try {
            channelService.saveChannelRoom(id, uids);
            jedisService.del(RedisKey.channel_audit_room.getKey());
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("delRoom")
    @ResponseBody
    public BusiResult delRoom(Integer channelId, Long roomUid) {
        //
        try {
            channelService.delChannelRoom(channelId, roomUid);
            jedisService.del(RedisKey.channel_audit_room.getKey());
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("delete/{id}")
    @ResponseBody
    public BusiResult delete(@PathVariable Integer id) {
        //
        int count = channelService.delete(id);
        return new BusiResult(count > 0 ? BusiStatus.SUCCESS : BusiStatus.SERVERERROR);
    }

    @RequestMapping("listRoom")
    @ResponseBody
    public void listRoom(Integer id) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<RoomDTO> pageInfo = channelService.pageRoom(id, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("code", 200);
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping("addIcon")
    @ResponseBody
    public BusiResult addIcon(Integer id, Integer iconId) {
        try {
            return channelService.saveChannelIcon(id, iconId);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("delIcon")
    @ResponseBody
    public BusiResult delIcon(Integer channelId, Long iconId) {
        try {
            channelService.delChannelIcon(channelId, iconId);
            jedisService.del(RedisKey.channel_audit_icon.getKey());
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }


    @RequestMapping("addBanner")
    @ResponseBody
    public BusiResult addBanner(Integer id, Integer bannerId) {
        try {
           return channelService.saveChannelBanner(id, bannerId);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("delBanner")
    @ResponseBody
    public BusiResult delBanner(Integer channelId, Long bannerId) {
        try {
            channelService.delChannelBanner(channelId, bannerId);
            jedisService.del(RedisKey.channel_audit_banner.getKey());
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }


    @RequestMapping("listIcon")
    @ResponseBody
    public void listIcon(Integer id) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Icon> pageInfo = channelService.pageIcon(id, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("code", 200);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("listBanner")
    @ResponseBody
    public void listBanner(Integer id) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Banner> pageInfo = channelService.pageBanner(id, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("code", 200);
        writeJson(jsonObject.toJSONString());
    }


    /**
     * 渠道审核用户列表
     * @param id id
     */
    @RequestMapping("listUsers")
    @ResponseBody
    public void listUsers(Integer id) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Users> pageInfo = channelService.pageUsers(id, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("code", 200);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 渠道审核增加用户
     * @param id id
     * @param erbanNo erbanNo
     * @return
     */
    @RequestMapping("addUsers")
    @ResponseBody
    public BusiResult addUsers(Integer id, Long erbanNo) {
        try {
            return channelService.saveChannelUsers(id, erbanNo);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    /**
     * 渠道审核删除用户
     * @param channelId channelId
     * @param uid uid
     * @return
     */
    @RequestMapping("delUsers")
    @ResponseBody
    public BusiResult delUsers(Integer channelId, Long uid) {
        try {
            channelService.delChannelUsers(channelId, uid);
            jedisService.del(RedisKey.channel_audit_users.getKey());
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }


    /**
     * 渠道审核ip列表
     * @param id id
     */
    @RequestMapping("listIp")
    @ResponseBody
    public void listIp(Integer id) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<ChannelIpDTO> pageInfo = channelService.pageIp(id, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("code", 200);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 渠道审核增加ip
     * @param id id
     * @param ip ip
     * @return
     */
    @RequestMapping("addIp")
    @ResponseBody
    public BusiResult addIp(Integer id, String ip) {
        try {
            return channelService.saveChannelIp(id, ip);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    /**
     * 渠道审核删除ip
     * @param channelId channelId
     * @param ip ip
     * @return
     */
    @RequestMapping("delIp")
    @ResponseBody
    public BusiResult delIp(Integer channelId, String ip) {
        try {
            channelService.delChannelIp(channelId, ip);
            jedisService.del(RedisKey.channel_audit_ip.getKey());
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }
}
