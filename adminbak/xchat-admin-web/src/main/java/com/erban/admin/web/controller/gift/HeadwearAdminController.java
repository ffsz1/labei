package com.erban.admin.web.controller.gift;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.HeadwearRecordDTO;
import com.erban.admin.main.dto.TreasureBoxReportDTO;
import com.erban.admin.main.service.gift.HeadwearAdminService;
import com.erban.admin.main.utils.FileUtils;
import com.erban.admin.main.vo.GiveHeadwearVo;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Headwear;
import com.erban.main.model.HeadwearGetRecord;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/headwear")
public class HeadwearAdminController extends BaseController {
    @Autowired
    private HeadwearAdminService headwearAdminService;

    @Autowired
    private JedisService jedisService;

    @RequestMapping(value = "/getall")
    @ResponseBody
    public BusiResult getAllGiftInfo() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<Headwear> list = headwearAdminService.getAllGift();
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
    @RequestMapping(value = "/getAllHeadwear")
    @ResponseBody
    public BusiResult getAllHeadwear() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<Headwear> list = headwearAdminService.getAllHeadwear();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getAllHeadwear error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }

    /**
     * 获取赠送头饰记录
     *
     * @return
     */
    @RequestMapping(value = "/getHeadwearRecord")
    @ResponseBody
    public Map<String, Object> getHeadwearRecord(String erbanNo, String startDate, String endDate) {
        PageInfo<HeadwearGetRecord> page = headwearAdminService.getHeadwearRecord(getPageNumber(), getPageSize(),
                erbanNo, startDate, endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", page.getTotal());
        jsonObject.put("rows", page.getList());
        return jsonObject;
    }

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(@RequestParam("type") Integer type, String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<Headwear> pageInfo = headwearAdminService.getGiftByPage((byte) type.intValue(), searchText,
                getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(Headwear headwear) {
        try {
            int result = headwearAdminService.saveHeadwear(headwear);
            if (result > 0) {
                //删除缓存
                if (headwear.getHeadwearId() != null) {
                    jedisService.hdel(RedisKey.headwear.getKey(), headwear.getHeadwearId().toString());
                }
                jedisService.del(RedisKey.headwear_list.getKey());
                jedisService.del(RedisKey.headwear_mall.getKey());
                writeJson(true, "保存成功");
                return;
            }
        } catch (BusinessException e) {
            logger.debug("Failed to save headwear. Cause by BusinessException");
            writeJson(false, e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Failed to save headwear. Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public void del(HttpServletRequest request) {
        List<Integer> ids = getRequestArray(request, "ids", Integer.class);
        if (CollectionUtils.isEmpty(ids)) {
            writeJson(false, "参数有误");
            return;
        }
        try {
            int result = headwearAdminService.delete(ids.toArray(new Integer[]{}));
            if (result > 0) {
                //清楚缓存
                Integer[] integers = ids.toArray(new Integer[]{});
                for (Integer i : integers) {
                    jedisService.hdel(RedisKey.headwear.getKey(), i.toString());
                }
                jedisService.del(RedisKey.headwear_list.getKey());
                jedisService.del(RedisKey.headwear_mall.getKey());
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete headwear, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id") Integer id) {
        JSONObject jsonObject = new JSONObject();
        try {
            Headwear headwear = headwearAdminService.getOne(id);
            if (headwear != null) {
                jsonObject.put("entity", headwear);
            }
        } catch (Exception e) {
            logger.error("获取异常,e:{}", e);
            writeJson(false, e.getMessage());
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 获取赠送头饰的头饰列表
     *
     * @param erbanNo
     * @param erbanNoList
     */
    @RequestMapping(value = "getGiveList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(Long erbanNo, String erbanNoList) {
        JSONObject jsonObject = new JSONObject();
        if (erbanNo == null && StringUtils.isBlank(erbanNoList)) {
            jsonObject.put("total", 0);
            jsonObject.put("rows", Lists.newArrayList());
        } else {
            PageInfo<GiveHeadwearVo> pageInfo = headwearAdminService.getList(erbanNo, getPageNumber(), getPageSize());
            jsonObject.put("total", pageInfo.getTotal());
            jsonObject.put("rows", pageInfo.getList());
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 赠送头饰
     *
     * @param headwearUid 赠送头饰的UID
     * @param date        赠送日期
     * @return
     */
    @RequestMapping(value = "give", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult give(String headwearUid, Integer date) {
        if (headwearUid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        try {
            return headwearAdminService.give(headwearUid, (date == null ? 1 : date));
        } catch (Exception e) {
            logger.error("give error,headwearUid=" + headwearUid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 批量赠送头饰
     *
     * @param headwearId 赠送头饰的Id
     * @param uids       接受头饰的Uid
     * @param date       赠送日期
     * @return
     */
    @RequestMapping(value = "giveList", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult giveList(String headwearId, String uids, Integer date) {
        if (StringUtils.isBlank(headwearId) || StringUtils.isBlank(uids)) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        try {
            return headwearAdminService.giveList(headwearId, uids, (date == null ? 1 : date));
        } catch (Exception e) {
            logger.error("give error,headwearId = " + headwearId + "uids = " + uids, e);
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
        List<HeadwearRecordDTO> headwearRecordDTOS = headwearAdminService.getExportList(startDate, endDate, erbanNos);
        // 导出操作
        FileUtils.exportExcel(headwearRecordDTOS, "赠送头饰记录报表", "赠送头饰记录报表", HeadwearRecordDTO.class, "赠送头饰记录报表.xls",
                response);
    }
}
