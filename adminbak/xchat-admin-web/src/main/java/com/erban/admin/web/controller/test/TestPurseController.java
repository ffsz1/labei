package com.erban.admin.web.controller.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.FaceJsonDTO;
import com.erban.admin.main.model.TestPurse;
import com.erban.admin.main.service.user.UserPurseAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.UserPurse;
import com.erban.main.model.UserPurseExample;
import com.erban.main.mybatismapper.UserPurseMapper;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.user.UserPurseService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/test/purse")
public class TestPurseController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestPurseController.class);

    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseMapper userPurseMapper;
    @Autowired
    private UserPurseAdminService userPurseAdminService;
    @Autowired
    private JedisService jedisService;

    private Gson gson = new Gson();

    @RequestMapping( value = "/cleardb")
    @ResponseBody
    public BusiResult clearTestPurseDb() {
        int result = userPurseAdminService.clearTestPurseDb();
        return new BusiResult(BusiStatus.SUCCESS, result);
    }

    @RequestMapping( value = "/getnotequeal")
    @ResponseBody
    public BusiResult getNotEqual() {
        final long version = new Date().getTime();
        new Thread(){
            @Override
            public void run() {
                int page = 1;
                while (true) {
                    logger.info("compare userpurse current page: {}", page);
                    PageInfo<UserPurse> pageInfo = userPurseAdminService.getPurseByPage(page++, 100);
                    List<UserPurse> list = pageInfo.getList();
                    if (list == null || list.size() <= 0) {
                        break;
                    }
                    List<String> cachePurses = jedisService.hmread(RedisKey.user_purse.getKey(), getUidList(list));
                    List<UserPurse> list2 = Lists.newArrayList();
                    for (String json : cachePurses) {
                        UserPurse cachePurse = gson.fromJson(json, UserPurse.class);
                        list2.add(cachePurse);
                    }

                    for (int i = 0; i < list.size(); i++) {
                        UserPurse mysqlPurse = list.get(i);
                        for (int j = 0; j < list2.size(); j++) {
                            UserPurse cachePurse = list2.get(j);
                            try {
                                if (cachePurse != null && cachePurse.getUid().equals(mysqlPurse.getUid())
                                        && cachePurse.getGoldNum()!= null
                                        && mysqlPurse.getGoldNum() != null
                                        && !cachePurse.getGoldNum().equals(mysqlPurse.getGoldNum())) {
                                    logger.warn("compare userpurse, version: " + version +", uid:" + mysqlPurse.getUid()
                                            + ", cacheGold:"
                                            + cachePurse.getGoldNum() + ", mysqlGold:" + mysqlPurse.getGoldNum());
                                    TestPurse testPurse = new TestPurse();
                                    testPurse.setVersion(version);
                                    testPurse.setUid(mysqlPurse.getUid());
                                    testPurse.setCacheDiamond(cachePurse.getDiamondNum());
                                    testPurse.setCacheGold(cachePurse.getGoldNum());
                                    testPurse.setMysqlDiamond(mysqlPurse.getDiamondNum());
                                    testPurse.setMysqlGold(mysqlPurse.getGoldNum());
                                    userPurseAdminService.addTestPurse(testPurse);
                                }
                            } catch (Exception e) {
                                logger.warn("compare userpurse error, version: " + version +", uid:" + mysqlPurse.getUid()
                                        + ", cacheGold:"
                                        + cachePurse.getGoldNum() + ", mysqlGold:" + mysqlPurse.getGoldNum());
                            }
                        }
                    }
                    logger.info("compare userpurse finish");
                }
            }
        }.start();

        return new BusiResult(BusiStatus.SUCCESS, version);
    }

    @RequestMapping(value = "/notequal")
    @ResponseBody
    private BusiResult getNotEqual(int day) {
        UserPurseExample example = new UserPurseExample();
        example.createCriteria().andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.getNextDay(new Date(), day));
        List<UserPurse> list = userPurseMapper.selectByExample(example);
        JSONArray jsonArray = new JSONArray();
        for (UserPurse userPurse : list) {
            UserPurse cachePurse = userPurseService.getPurseByUid(userPurse.getUid());
            try {
                if (!cachePurse.getGoldNum().equals(userPurse.getGoldNum())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cacheGold", cachePurse.getGoldNum());
                    jsonObject.put("mysqlGold", userPurse.getGoldNum());
                    jsonObject.put("uid", userPurse.getUid());
                    jsonArray.add(jsonObject);
                    logger.info("compare=== {}", jsonObject.toJSONString());
                }
            } catch (Exception e) {
                logger.info("getNotEqual error, uid:" + userPurse.getUid() + ", goldNum:" + userPurse.getGoldNum());
            }
        }

        return new BusiResult(BusiStatus.SUCCESS, jsonArray);
    }


    private String[] getUidList(List<UserPurse> purseList) {
        List<String> list = Lists.newArrayList();
        String[] uids = new String[purseList.size()];

        for (int i = 0; i < uids.length; i++) {
            uids[i] = purseList.get(i).getUid().toString();
        }
        return uids;
    }

    @Autowired
    private SendSysMsgService sendSysMsgService;

   private static final String tickets = "tiantian_uid_ticket";

    @RequestMapping("test")
    @ResponseBody
    public String test(@RequestParam("uid")String uid) {
        String title = "小喵推荐";
        String desc = "官方充值渠道";
        String ticket = jedisService.hget(tickets,uid);
        String picUrl = "http://res.91fb.com/preferential_recharge.png";
        String webUrl = "http://beta.47huyu.cn/mm/charge/index.html?uid=" + uid + "&ticket=" + ticket;
        int status = sendSysMsgService.sendAppleMishu(uid,title,desc,picUrl,webUrl);
        return "success -- > [" + status + "]";
    }

    public static void main(String[] args) throws Exception{
        String json  = "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";
        String result = DESUtils.DESAndBase64Decrypt(json,"1ea53d260ecf11e7b56e00163e046a26");
        Gson gson = new Gson();
        FaceJsonDTO faceJsonDTO = gson.fromJson(result, FaceJsonDTO.class);
        System.out.println(faceJsonDTO.getFaces().size());
        System.out.println(faceJsonDTO.getZipMd5());
    }
}
