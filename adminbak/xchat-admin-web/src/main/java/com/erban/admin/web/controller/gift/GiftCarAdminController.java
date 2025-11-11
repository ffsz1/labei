package com.erban.admin.web.controller.gift;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.GiftCarRecordDTO;
import com.erban.admin.main.dto.HeadwearRecordDTO;
import com.erban.admin.main.service.gift.GiftCarAdminService;
import com.erban.admin.main.utils.FileUtils;
import com.erban.admin.main.vo.GiveGiftcarVo;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.*;
import com.erban.main.service.SysConfService;
import com.erban.main.service.api.QiniuService;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/giftcar")
public class GiftCarAdminController extends BaseController {
    @Autowired
    private GiftCarAdminService giftCarAdminService;

    @Autowired
    SysConfService sysConfService;

    @Autowired
    protected JedisService jedisService;

    @Autowired
    QiniuService qiniuService;

    @RequestMapping(value = "/getall")
    @ResponseBody
    public BusiResult getAllGiftInfo() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<GiftCar> list = giftCarAdminService.getAllGift();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getAllGiftInfo error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    /**
     * 获取所有头饰列表
     *
     * @return
     */
    @RequestMapping(value = "/getAllGiftCar")
    @ResponseBody
    public BusiResult getAllGiftCar() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<GiftCar> list = giftCarAdminService.getAllGiftCar();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getAllGiftCar Error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(@RequestParam("type") Integer type, String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<GiftCar> pageInfo = giftCarAdminService.getGiftByPage((byte) type.intValue(), searchText,
                getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 获取赠送头饰记录
     *
     * @return
     */
    @RequestMapping(value = "/getGiftCarRecord")
    @ResponseBody
    public Map<String, Object> getGiftCarRecord(String erbanNo, String startDate, String endDate) {
        PageInfo<GiftCarGetRecord> page = giftCarAdminService.getGiftCarRecord(getPageNumber(), getPageSize(),
                erbanNo, startDate, endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", page.getTotal());
        jsonObject.put("rows", page.getList());
        return jsonObject;
    }

    /**
     * 保存、更新座驾
     *
     * @param GiftCar
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(GiftCar GiftCar) {
        try {
            int result = giftCarAdminService.saveCar(GiftCar);
            if (result > 0) {
                //删除缓存
                if (GiftCar.getCarId() != null) {
                    jedisService.hdel(RedisKey.gift_car.getKey(), GiftCar.getCarId().toString());
                }
                jedisService.del(RedisKey.gift_car_list.getKey());
                jedisService.del(RedisKey.gift_car_mall.getKey());
                writeJson(true, "保存成功");
                return;
            }
        } catch (BusinessException e) {
            logger.debug("Failed to save GiftCar. Cause by BusinessException");
            writeJson(false, e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Failed to save GiftCar. Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "保存失败");
    }

    /**
     * 删除座驾
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
            int result = giftCarAdminService.delete(ids.toArray(new Integer[]{}));
            if (result > 0) {
                //清楚缓存
                Integer[] integers = ids.toArray(new Integer[]{});
                for (Integer i : integers) {
                    jedisService.hdel(RedisKey.gift_car.getKey(), i.toString());
                }
                jedisService.del(RedisKey.gift_car_list.getKey());
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete GiftCar, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    /**
     * 获取某个座驾
     *
     * @param id
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id") Integer id) {
        JSONObject jsonObject = new JSONObject();
        try {
            GiftCar GiftCar = giftCarAdminService.getOne(id);
            if (GiftCar != null) {
                jsonObject.put("entity", GiftCar);
            }
        } catch (Exception e) {
            logger.error("获取某个座驾异常,e:{}", e);
            writeJson(false, e.getMessage());
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "getCurGiftVersion", method = RequestMethod.GET)
    @ResponseBody
    public void getCurGiftVersion() {
        JSONObject jsonObject = new JSONObject();
        SysConf sysConf = sysConfService.getSysConfById(Constant.SysConfId.cur_gift_car_version);
        if (sysConf != null) {
            jsonObject.put("entity", sysConf);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "getGiveList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(Long erbanNo, String erbanNoList) {
        JSONObject jsonObject = new JSONObject();
        if (erbanNo == null && StringUtils.isBlank(erbanNoList)) {
            jsonObject.put("total", 0);
            jsonObject.put("rows", Lists.newArrayList());
        } else {
            PageInfo<GiveGiftcarVo> pageInfo = giftCarAdminService.getList(erbanNo, getPageNumber(), getPageSize());
            jsonObject.put("total", pageInfo.getTotal());
            jsonObject.put("rows", pageInfo.getList());
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "give", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult give(String carUid, Integer date) {
        if (carUid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return giftCarAdminService.give(carUid, (date == null ? 1 : date));
        } catch (Exception e) {
            logger.error("give error,carUid=" + carUid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 批量赠送座驾
     *
     * @param carId 赠送座驾的Id
     * @param uids  接收座驾的Uid
     * @param date  赠送日期
     * @return
     */
    @RequestMapping(value = "giveList", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult giveList(String carId, String uids, Integer date) {
        if (StringUtils.isBlank(carId) || StringUtils.isBlank(uids)) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        try {
            return giftCarAdminService.giveList(carId, uids, (date == null ? 1 : date));
        } catch (Exception e) {
            logger.error("give error, carId = " + carId + "uids = " + uids, e);
            return new BusiResult(BusiStatus.BUSIERROR, uids);
        }
    }

    /**
     * 导出表格
     *
     * @param response
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param erbanNos   拉贝号
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response, String startDate, String endDate, String erbanNos) {
        List<GiftCarRecordDTO> giftCarDTOS = giftCarAdminService.getExportList(startDate, endDate, erbanNos);
        // 导出操作
        FileUtils.exportExcel(giftCarDTOS, "赠送座驾记录报表", "赠送座驾记录报表", GiftCarRecordDTO.class, "赠送座驾记录报表.xls",
                response);
    }
}
