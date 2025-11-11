package com.erban.web.controller.noble;

import com.erban.main.model.NobleRecord;
import com.erban.main.model.NobleRes;
import com.erban.main.service.noble.NobleRecordService;
import com.erban.main.service.noble.NobleResService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.vo.BillSearchVo;
import com.erban.main.vo.noble.NobleRecordVo;
import com.erban.web.common.BaseController;
import com.google.common.collect.Maps;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/noble/record")
public class NobleRecordController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NobleRecordController.class);

    @Autowired
    private NobleRecordService nobleRecordService;


    /**
     * 获取某种贵族资源的列表，分页返回
     *
     * @return
     */
    @RequestMapping(value = "/get")
    @ResponseBody
    @Authorization
    public BusiResult getRecordList(long uid, long date, int pageNo, Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            pageSize = 50;
        }
        try {
            List<NobleRecord> list = nobleRecordService.getNobleRecordByDate(uid, date, pageNo, pageSize);
            List<LinkedHashMap<Long, List<NobleRecordVo>>> reList = nobleRecordService.transToRecordVo(list);
            LinkedHashMap<String, List<LinkedHashMap<Long, List<NobleRecordVo>>>> reMap = Maps.newLinkedHashMap();
            reMap.put("billList", reList);
            return new BusiResult(BusiStatus.SUCCESS, reMap);
        } catch (Exception e) {
            logger.error("getRecordList error, uid:"+ uid + ", date:" + date, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }


}
