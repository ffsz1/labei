package com.erban.admin.web.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.AdminConstants;
import com.erban.admin.main.service.dingtalk.DingtalkChatbotService;
import com.erban.admin.main.service.dingtalk.bo.DingtalkTextMessageBO;
import com.erban.admin.main.service.record.OfficialGoldRecordService;
import com.erban.admin.main.service.record.UserService;
import com.erban.admin.main.service.system.AdminLogService;
import com.erban.admin.main.vo.GiveGoldRecordDTO;
import com.erban.admin.main.vo.GiveGoldRecordParam;
import com.erban.admin.main.vo.UsersVo;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.Page;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.DateUtil;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/goldcoin")
public class GoldCoinController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GoldCoinController.class);

    @Autowired
    private UserService userService2;

    @Autowired
    private OfficialGoldRecordService officialGoldRecordService;

    @Autowired
    private AdminLogService adminLogService;

    @Autowired
    private DingtalkChatbotService chatbotService;

    @Autowired
    private JedisService jedisService;

    /**
     * 赠送金币
     *
     * @param session
     * @param ernos   拉贝号 [多个号用换行符分隔]
     * @param type    活动类型
     * @param num     赠送数量
     * @param remark  备注
     * @return
     */
    @RequestMapping("/give")
    @ResponseBody
    public BusiResult giveGoldCoin(HttpSession session, String ernos, byte type, int num, String remark) {
        if (BlankUtil.isBlank(ernos) || num < 1) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        long count =
                jedisService.incr(RedisKey.goldcoin_give.getKey(String.valueOf(session.getAttribute(AdminConstants.ADMIN_ID))), 3);
        if (count > 1) {
            return new BusiResult(BusiStatus.OP_TO_FAST);
        }

        BusiResult busiResult;
        try {
            int adminId = getAdminId();
            if (adminId < 1000 || 10000 < adminId) {
                busiResult = officialGoldRecordService.giveGold(ernos, type, num, getAdminId(), remark);
                adminLogService.insertLog(getAdminId(), getClass().getCanonicalName(), "giveGoldCoin"
                        , "params===>>ernos:" + ernos.split("\n").length + ", type:" + type + ", num:" + num + ", " +
                                "remark:" + remark);//
                // 日志不详细记录耳伴号，只记录耳伴号数量，详细可以从金币日志那边查看
            } else {
                busiResult = officialGoldRecordService.giveGold2(ernos, type, num, getAdminId(), remark);
            }
        } catch (Exception e) {
            logger.error("giveGoldCoin error, ernos:" + ernos + ", type:" + type + ", num:" + num + ", remark:" + remark, e);
            return new BusiResult(BusiStatus.SERVEXCEPTION);
        }

        if (num >= 20000) {
            try {
                String adminName = (String) session.getAttribute(AdminConstants.ADMIN_NAME);
                String typeDesc;
                switch (type) {
                    case 12:
                        typeDesc = "官方赠送";
                        break;
                    case 20:
                        typeDesc = "公款充值";
                        break;
                    case 37:
                        typeDesc = "活动奖励";
                        break;
                    case 55:
                        typeDesc = "员工体验使用";
                        break;
                    case 69:
                        typeDesc = "运营使用";
                        break;
                    default:
                        typeDesc = "未知";
                }

                StringBuilder builder = new StringBuilder();
                builder.append("[ 管理后台预警 ]").append(adminName);
                builder.append("在").append(GlobalConfig.envName);
                builder.append("通过").append(typeDesc);
                builder.append("操作给【").append(ernos);
                builder.append("】赠送了").append(num).append("个金币");
                chatbotService.send(new DingtalkTextMessageBO(builder.toString(), null, false));
            } catch (Exception e) {
            }
        }
        return busiResult;
    }

    /**
     * 获取用户信息
     *
     * @param ernos 拉贝号，多个号用换行符分隔
     * @return
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public BusiResult<List<UsersVo>> getUserInfoByErNos(String ernos) {
        List<UsersVo> list = userService2.getUserList(ernos);
        return new BusiResult<List<UsersVo>>(BusiStatus.SUCCESS, list);
    }

    /**
     * 获取金币赠送记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "giveRecord")
    @ResponseBody
    public Map<String, Object> findRecord(GiveGoldRecordParam param) {
        checkDate(param);
        Page<GiveGoldRecordDTO> page = officialGoldRecordService.giveRecord(param);
        // Long sumGold = officialGoldRecordService.sumGold(param);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", page.getTotal());
        jsonObject.put("rows", page.getResult());
        // jsonObject.put("sumGold", sumGold);
        return jsonObject;
    }

    /**
     * 统计金币赠送金额, 和赠送记录查询条件一样
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "totalGold")
    @ResponseBody
    public Long totalGold(GiveGoldRecordParam param) {
        checkDate(param);
        return officialGoldRecordService.sumGold(param);
    }

    /**
     * 更新赠送金币记录
     *
     * @param recordId 记录ID
     * @param remark   备注
     * @return
     */
    @RequestMapping(value = "updateGiveGoldRecord")
    @ResponseBody
    public int updateGiveGoldRecord(@Param("recordId") Integer recordId, @Param("remark") String remark) {
        return officialGoldRecordService.updateGiveGoldRecord(recordId, remark);
    }

    /**
     * 检查日期
     *
     * @param param 赠送金币参数
     */
    public void checkDate(GiveGoldRecordParam param) {
        try {
            if (StringUtils.isNotBlank(param.getBeginDate())) {
                param.setBegin(DateTimeUtil.convertStrToDate(param.getBeginDate(), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotBlank(param.getEndDate())) {
                Date end = DateTimeUtil.convertStrToDate(param.getEndDate(), "yyyy-MM-dd");
                param.setEnd(DateUtil.addDay(end, 1));
            }
        } catch (Exception e) {
            logger.error("时间转换异常", e);
        }
    }
}
