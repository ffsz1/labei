package com.erban.admin.web.controller.family;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.FamilyTeamDTO;
import com.erban.admin.main.model.FamilyTeam;
import com.erban.admin.main.service.family.FamilyTeamService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.web.controller.family
 * @date 2018/9/3
 * @time 20:00
 */
@RestController
@RequestMapping("/admin/family")
public class FamilyController extends BaseController {

    @Autowired
    private FamilyTeamService familyTeamService;

    @RequestMapping(value = "/getall")
    public void getFamilyTeamList(String searchText,Integer type,String startTime, String endTime) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<FamilyTeamDTO> pageInfo = familyTeamService.getListWithPage(searchText, type,getPageNumber(), getPageSize(),startTime,endTime);
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }




    /**
     * 展示/不展示
     * @param teamId
     * @return
     */
    @RequestMapping("/changeDisplaysStatus")
    @ResponseBody
    public BusiResult changeDisplaysStatus(Long teamId){
        if(teamId==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = familyTeamService.DisplaysStatus(teamId);
        return busiResult;
    }

    /**
     * 审核通过
     * @param teamId
     * @return
     */
    @RequestMapping("/checkSuccess")
    @ResponseBody
    public BusiResult checkSuccess(Long teamId){
        if(teamId==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = familyTeamService.updateCheckSuccessStatus(teamId);
        return busiResult;
    }

    /**
     * 审核不通过
     * @param teamId
     * @return
     */
    @RequestMapping("/checkFailure")
    @ResponseBody
    public BusiResult checkFailure(Long teamId){
        if(teamId==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = familyTeamService.updateCheckFailureStatus(teamId);
        return busiResult;
    }


    /**
     * 获取某个数据
     * @param id
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")Long id)
    {
        JSONObject jsonObject = new JSONObject();
        FamilyTeam team = familyTeamService.get(id);
        if(team != null) {
            jsonObject.put("entity", team);
        }
        writeJson(jsonObject.toJSONString());
    }


    /**
     * 保存、更新
     * @param familyTeam
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(FamilyTeam familyTeam) {
        try {
                int result = familyTeamService.update(familyTeam);
                if (result > 0) {
                    writeJson(true, "保存成功");
                    return;
                }
        } catch(BusinessException e) {
            logger.debug("Failed to save gift. Cause by BusinessException");
            writeJson(false, "该家族ID已存在,请确认...");
            return;
        } catch(Exception e) {
            logger.error("Failed to save gift. Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "保存失败或该家族ID已存在,请确认...");
    }


    /**
     * 解散家族
     * @param teamId
     * @return
     */
    @RequestMapping("/disband")
    @ResponseBody
    public BusiResult disband(Long teamId){
        if(teamId==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return familyTeamService.disband(teamId);
    }


}
