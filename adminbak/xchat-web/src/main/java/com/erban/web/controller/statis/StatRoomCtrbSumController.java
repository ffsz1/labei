package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatRoomCtrbSumService;
import com.erban.main.vo.StatRoomCtrbSumVo;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/roomctrb")
public class StatRoomCtrbSumController extends BaseController{
    @Autowired
    private StatRoomCtrbSumService statRoomCtrbSumService;

    /**
     * 查询房间贡献榜
     * @param uid
     * @return
     */
    @RequestMapping(value = "query",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult<List<StatRoomCtrbSumVo>> getRoomCtrbSumListByUid(Long uid, HttpServletRequest request){
        if(uid==null||uid==0L){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return statRoomCtrbSumService.getStatRoomCtrbSumListByUid(uid);
    }

    /**
     * 新版查询房间贡献榜
     * 分为财富、魅力分栏，下面有日、周、总榜
     */
    @RequestMapping(value = "queryByType",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getRoomCtrbSumListByType(Long uid, Integer dataType, Integer type){
        if(uid==null||dataType==null||type==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return statRoomCtrbSumService.getRoomCtrbSumListByType(uid, dataType, type);
    }
    
}
