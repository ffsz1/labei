package com.erban.admin.web.controller.gift;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.service.gift.GiftAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Gift;
import com.erban.main.model.SysConf;
import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.service.SysConfService;
import com.erban.main.service.api.QiniuService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 拉贝后台--礼物管理controller
 */
@Controller
@RequestMapping("/admin/gift")
public class GiftAdminController extends BaseController {
    @Autowired
    private GiftAdminService giftAdminService;

    @Autowired
    private UsersService usersService;

    @Autowired
    SysConfService sysConfService;

    @Autowired
    protected JedisService jedisService;

    @Autowired
    QiniuService qiniuService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/getall")
    @ResponseBody
    public BusiResult getAllGiftInfo() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<Gift> list = giftAdminService.getAllGift();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getAllGiftInfo error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    /**
     * 获取520及以上金币的礼物
     *
     * @param giftType 礼物类型
     * @return
     */
    @RequestMapping(value = "/getHighPriceGift")
    @ResponseBody
    public BusiResult getHighPriceGiftInfo(@RequestParam("giftType") Integer giftType) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<Gift> list = giftAdminService.getHighPriceGift((byte) giftType.intValue());
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getAllGiftInfo error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    /**
     * 补送钻石，并加上收到礼物的记录
     *
     * @param uid
     * @param targetUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     */
    @RequestMapping(value = "/repairdiamond")
    @ResponseBody
    public BusiResult mockRepairDiamond(Long uid, Long targetUid, Long roomUid, Integer giftId, Integer giftNum) {
        if (uid == null || targetUid == null || roomUid == null || giftId == null || giftNum == null || giftNum <= 0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.NOTEXISTS, "uid不存在", null);
        }
        users = usersService.getUsersByUid(targetUid);//修复bug by zhaomiao 2018/2/26
        if (users == null) {
            return new BusiResult(BusiStatus.NOTEXISTS, "tagetUid不存在", null);
        }

        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            UserPurse userPurse = giftAdminService.repairDiamond(uid, targetUid, roomUid, giftId, giftNum);
            busiResult.setData(userPurse);
        } catch (Exception e) {
            logger.error("mockRepairDiamond error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(@RequestParam("type") Integer type, @RequestParam("gift") Integer giftType, String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Gift> pageInfo = giftAdminService.getGiftByPage((byte) type.intValue(), (byte) giftType.intValue(),
                searchText, getPageNumber(),
                getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 保存、更新礼物
     *
     * @param gift
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(Gift gift) {
        try {
            int result = giftAdminService.saveGift(gift);
            if (result <= 0) {
                return;
            }

            if (gift.getGiftId() == null) {
                writeJson(true, "保存成功");
                return;
            }

            jedisService.del(RedisKey.gift_all.getKey());

            // 普通礼物
            if (gift.getGiftStatus() == 2) {
                jedisService.hdel(RedisKey.gift_mystic.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_point.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_activity.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift.getKey(), String.valueOf(gift.getGiftId()));
                writeJson(true, "保存成功");
                return;
            }

            // 3.捡海螺礼物; 5.相亲礼物; 4.活动礼物
            if (gift.getGiftType() == 3) {
                jedisService.hset(RedisKey.gift_mystic.getKey(), String.valueOf(gift.getGiftId()), gson.toJson(gift));
                jedisService.hdel(RedisKey.gift.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_point.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_activity.getKey(), String.valueOf(gift.getGiftId()));
            } else if (gift.getGiftType() == 5) {
                jedisService.hset(RedisKey.gift_point.getKey(), String.valueOf(gift.getGiftId()), gson.toJson(gift));
                jedisService.hdel(RedisKey.gift_mystic.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_activity.getKey(), String.valueOf(gift.getGiftId()));
            } else if (gift.getGiftType() == 4) {
                jedisService.hset(RedisKey.gift_activity.getKey(), String.valueOf(gift.getGiftId()), gson.toJson(gift));
                jedisService.hdel(RedisKey.gift_mystic.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_point.getKey(), String.valueOf(gift.getGiftId()));
            } else {
                jedisService.hdel(RedisKey.gift.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_mystic.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_point.getKey(), String.valueOf(gift.getGiftId()));
                jedisService.hdel(RedisKey.gift_activity.getKey(), String.valueOf(gift.getGiftId()));
            }

            writeJson(true, "保存成功");
        } catch (BusinessException e) {
            logger.debug("Failed to save gift. Cause by BusinessException");
            writeJson(false, e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Failed to save gift. Cause by {}", e.getCause().getMessage());
        }

        writeJson(false, "保存失败");
    }

    /**
     * 删除礼物
     *
     * @param request
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public void del(HttpServletRequest request) {
        List<Integer> ids = getRequestArray(request, "ids", Integer.class);
        if (CollectionUtils.isEmpty(ids)) {
            writeJson(false, "参数有误");
            return;
        }

        try {
            int result = giftAdminService.delete(ids.toArray(new Integer[]{}));
            if (result > 0) {
                //清楚缓存
                Integer[] integers = ids.toArray(new Integer[]{});
                for (Integer i : integers) {
                    jedisService.hdel(RedisKey.gift.getKey(), i.toString());
                    jedisService.hdel(RedisKey.gift_mystic.getKey(), i.toString());
                    jedisService.hdel(RedisKey.gift_point.getKey(), i.toString());
                }
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete gift, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    /**
     * 根据礼物ID获取礼物信息
     *
     * @param id 礼物ID
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id") Integer id) {
        JSONObject jsonObject = new JSONObject();
        Gift gift = giftAdminService.get(id);
        if (gift != null) {
            jsonObject.put("entity", gift);
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 获取当前礼物版本
     */
    @RequestMapping(value = "getCurGiftVersion", method = RequestMethod.GET)
    @ResponseBody
    public void getCurGiftVersion() {
        JSONObject jsonObject = new JSONObject();
        SysConf sysConf = sysConfService.getSysConfById(Constant.SysConfId.cur_gift_version);
        if (sysConf != null) {
            jsonObject.put("entity", sysConf);
        }
        writeJson(jsonObject.toJSONString());
    }
}
