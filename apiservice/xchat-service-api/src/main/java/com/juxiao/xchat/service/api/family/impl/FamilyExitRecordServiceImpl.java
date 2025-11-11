package com.juxiao.xchat.service.api.family.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.family.dao.FamilyExitRecordDAO;
import com.juxiao.xchat.dao.family.dao.FamilyGiftRecordDAO;
import com.juxiao.xchat.dao.family.dao.FamilyJoinDAO;
import com.juxiao.xchat.dao.family.dao.FamilyTeamDAO;
import com.juxiao.xchat.dao.family.domain.FamilyExitRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyJoinDO;
import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyTeamDO;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.external.netease.NetEaseTeamManager;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseTeamBO;
import com.juxiao.xchat.manager.external.netease.ret.BaseTeamRet;
import com.juxiao.xchat.manager.external.netease.utils.NetEaseTeamUtils;
import com.juxiao.xchat.service.api.family.FamilyExitRecordService;
import com.juxiao.xchat.service.api.family.FamilyOperateRecordService;
import com.juxiao.xchat.service.api.family.bo.ApplyFamilyParamBO;
import com.juxiao.xchat.service.api.family.bo.ApplyTeamParamBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family.impl
 * @date 2018/8/31
 * @time 11:35
 */
@Slf4j
@Service
public class FamilyExitRecordServiceImpl implements FamilyExitRecordService {


    private final Gson gson;

    private final FamilyTeamDAO familyTeamDAO;

    private final FamilyExitRecordDAO familyExitRecordDAO;

    private final FamilyJoinDAO familyJoinDAO;

    private final LevelManager levelManager;

    private final FamilyGiftRecordDAO familyGiftRecordDAO;

    private final NetEaseTeamManager netEaseTeamManager;

    private final FamilyOperateRecordService familyOperateRecordService;

    @Autowired
    public FamilyExitRecordServiceImpl(Gson gson, FamilyTeamDAO familyTeamDAO, FamilyExitRecordDAO familyExitRecordDAO, FamilyJoinDAO familyJoinDAO, LevelManager levelManager, FamilyGiftRecordDAO familyGiftRecordDAO, NetEaseTeamManager netEaseTeamManager, FamilyOperateRecordService familyOperateRecordService) {
        this.gson = gson;
        this.familyTeamDAO = familyTeamDAO;
        this.familyExitRecordDAO = familyExitRecordDAO;
        this.familyJoinDAO = familyJoinDAO;
        this.levelManager = levelManager;
        this.familyGiftRecordDAO = familyGiftRecordDAO;
        this.netEaseTeamManager = netEaseTeamManager;
        this.familyOperateRecordService = familyOperateRecordService;
    }


    /**
     * 申请退出家族
     *
     * @param applyTeamParamBO
     * @return
     */
    @Override
    public WebServiceMessage applyExitTeam(ApplyTeamParamBO applyTeamParamBO) {
        long startTime = System.currentTimeMillis();
        if(applyTeamParamBO == null){
            return WebServiceMessage.failure("请求参数为空!");
        }
        FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(applyTeamParamBO.getFamilyId());
        if(familyTeamDO == null){
            return WebServiceMessage.failure("该家族不存在!");
        }
        FamilyExitRecordDO familyExitRecordDO = this.familyExitRecordDAO.selectByTeamIdAndUid(familyTeamDO.getId(),applyTeamParamBO.getUserId());
        if(familyExitRecordDO == null){
            familyExitRecordDO = new FamilyExitRecordDO();
            familyExitRecordDO.setCreateTime(new Date());
            familyExitRecordDO.setStatus(0);
            familyExitRecordDO.setType(1);
            familyExitRecordDO.setRemark("申请退出家族");
            familyExitRecordDO.setUpdateTime(new Date());
            familyExitRecordDO.setTeamId(familyTeamDO.getId());
            familyExitRecordDO.setUid(applyTeamParamBO.getUserId());
            int status = this.familyExitRecordDAO.save(familyExitRecordDO);
            if(status > 0){
                familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),applyTeamParamBO.getUserId(),"申请退出家族记录", String.valueOf((System.currentTimeMillis() - startTime)),gson.toJson(applyTeamParamBO),new Date()));
                log.info("[ 申请退出家族 ]接收内容:>{},耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                return WebServiceMessage.success(null);
            }else{
                log.info("[ 申请退出家族 ]数据保存失败 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                return WebServiceMessage.failure("申请退出家族操作数据失败!");
            }
        }else{
            log.info("[ 申请退出家族 ]数据保存失败 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
            return WebServiceMessage.failure("你已提交申请...!");
        }
    }

    /**
     * 踢出家族
     *
     * @param applyTeamParamBO
     * @return
     */
    @Override
    public WebServiceMessage kickOutTeam(ApplyTeamParamBO applyTeamParamBO, String currentUid){
        long startTime = System.currentTimeMillis();
        if(applyTeamParamBO == null){
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(applyTeamParamBO.getFamilyId());
        if(familyTeamDO == null){
            return WebServiceMessage.failure("该家族不存在!");
        }
        FamilyJoinDO currentFamilyJoin = familyJoinDAO.selectByUserId(Long.valueOf(currentUid));
        if(currentFamilyJoin == null){
            return WebServiceMessage.failure("你未加入家族!");
        }
        FamilyJoinDO familyJoinDO = familyJoinDAO.selectByUserId(applyTeamParamBO.getUserId());
        if(familyJoinDO == null){
            return WebServiceMessage.failure("该用户未家族家族!");
        }
        if(currentFamilyJoin.getRoleStatus().longValue() < familyJoinDO.getRoleStatus().longValue()){
            try {
                NetEaseTeamBO netEaseTeamBO = new NetEaseTeamBO();
                netEaseTeamBO.setTid(familyTeamDO.getRoomId().toString());
                netEaseTeamBO.setOwner(familyTeamDO.getUid().toString());
                netEaseTeamBO.setMember(applyTeamParamBO.getUserId().toString());
                BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.kickTeam(netEaseTeamBO));
                if(baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()) {
                    return WebServiceMessage.failure(baseTeamRet.getMsg());
                }else{
                    int status = familyJoinDAO.deleteByUid(applyTeamParamBO.getUserId());
                    if(status > 0){
                        FamilyExitRecordDO familyExitRecordDO = new FamilyExitRecordDO();
                        familyExitRecordDO.setCreateTime(new Date());
                        familyExitRecordDO.setStatus(1);
                        familyExitRecordDO.setType(2);
                        familyExitRecordDO.setTeamId(familyTeamDO.getId());
                        familyExitRecordDO.setUpdateTime(new Date());
                        familyExitRecordDO.setRemark("踢出家族");
                        familyExitRecordDO.setUid(applyTeamParamBO.getUserId());
                        int result = this.familyExitRecordDAO.save(familyExitRecordDO);
                        if(result > 0) {
                            familyGiftRecordDAO.removeUserGiftRecord(familyTeamDO.getId(), applyTeamParamBO.getUserId());
                            familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),applyTeamParamBO.getUserId(),"踢出家族记录", String.valueOf((System.currentTimeMillis() - startTime)),gson.toJson(applyTeamParamBO),new Date()));
                        }
                        log.info("[ 踢出家族 ]接收内容:>{},耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.success(null);
                    }else{
                        log.info("[ 踢出家族 ]数据保存失败 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.failure("踢出家族操作数据失败!");
                    }
                }
            }catch (Exception e){
                return WebServiceMessage.failure("踢出成员失败!");
            }
        }else{
            return WebServiceMessage.failure("没有该权限!");
        }
    }


    /**
     * 审核退出家族
     *
     * @param applyFamilyParamBO
     * @return
     */
    @Override
    public WebServiceMessage applyExitFamily(ApplyFamilyParamBO applyFamilyParamBO) {
        long startTime = System.currentTimeMillis();
        if(applyFamilyParamBO == null){
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(applyFamilyParamBO.getFamilyId());
        FamilyExitRecordDO familyExitRecordDO = this.familyExitRecordDAO.selectByTeamIdAndUid(familyTeamDO.getId(),applyFamilyParamBO.getUserId());
        //同意
        if(applyFamilyParamBO.getStatus() == 1){
            try {
                BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.leave(familyTeamDO.getRoomId().toString(),applyFamilyParamBO.getUserId().toString()));
                if(baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()){
                    return WebServiceMessage.failure(baseTeamRet.getMsg());
                }else{
                    familyExitRecordDO.setStatus(1);
                    familyExitRecordDO.setUpdateTime(new Date());
                    int status = this.familyExitRecordDAO.updateByStatus(familyExitRecordDO);
                    if(status > 0){
                        familyJoinDAO.deleteByUidAndTeamId(applyFamilyParamBO.getUserId(),familyTeamDO.getId());
                        familyGiftRecordDAO.removeUserGiftRecord(familyTeamDO.getId(),applyFamilyParamBO.getUserId());
                        familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),applyFamilyParamBO.getUserId(),"同意审核退出家族记录", String.valueOf((System.currentTimeMillis() - startTime)),gson.toJson(applyFamilyParamBO),new Date()));
                        log.info("[ 审核退出家族 ]接收内容:>{},耗时:>{} /ms", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.success(null);
                    }else{
                        log.info("[ 审核退出家族 -> 操作数据库] 接收内容:>{},耗时:>{} /ms", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.failure("操作失败!");
                    }
                }
            }catch (Exception e){
                return WebServiceMessage.failure("操作失败!");
            }
            //拒绝
        }else{
            familyExitRecordDO.setStatus(2);
            familyExitRecordDO.setUpdateTime(new Date());
            int status = this.familyExitRecordDAO.updateByStatus(familyExitRecordDO);
            if (status > 0){
                familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),applyFamilyParamBO.getUserId(),"拒绝审核退出家族记录", String.valueOf((System.currentTimeMillis() - startTime)),gson.toJson(applyFamilyParamBO),new Date()));
                log.info("[ 审核退出家族 ]接收内容:>{},耗时:>{} /ms", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));
                return WebServiceMessage.success(null);
            }else{
                log.info("[ 审核退出家族 -> 操作数据库]接收内容:>{},耗时:>{} /ms", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));
                return WebServiceMessage.failure("操作失败!");
            }
        }
    }

}
