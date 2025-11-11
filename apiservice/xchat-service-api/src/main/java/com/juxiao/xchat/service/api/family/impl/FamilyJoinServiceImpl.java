package com.juxiao.xchat.service.api.family.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.family.dao.FamilyJoinDAO;
import com.juxiao.xchat.dao.family.dao.FamilyTeamDAO;
import com.juxiao.xchat.dao.family.domain.FamilyJoinDO;
import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyTeamDO;
import com.juxiao.xchat.dao.family.dto.*;
import com.juxiao.xchat.manager.external.netease.NetEaseTeamManager;
import com.juxiao.xchat.manager.external.netease.ret.BaseTeamRet;
import com.juxiao.xchat.manager.external.netease.utils.NetEaseTeamUtils;
import com.juxiao.xchat.service.api.family.FamilyJoinService;
import com.juxiao.xchat.service.api.family.FamilyOperateRecordService;
import com.juxiao.xchat.service.api.family.bo.ApplyTeamParamBO;
import com.juxiao.xchat.service.api.family.bo.FamilyUserBannedBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family.impl
 * @date 2018/8/31
 * @time 11:34
 */
@Slf4j
@Service
public class FamilyJoinServiceImpl implements FamilyJoinService {

    private final Gson gson;

    private final FamilyJoinDAO familyJoinDAO;

    private final FamilyTeamDAO familyTeamDAO;

    private final NetEaseTeamManager netEaseTeamManager;

    private final FamilyOperateRecordService familyOperateRecordService;

    @Autowired
    public FamilyJoinServiceImpl(Gson gson, FamilyJoinDAO familyJoinDAO, FamilyTeamDAO familyTeamDAO, NetEaseTeamManager netEaseTeamManager, FamilyOperateRecordService familyOperateRecordService) {
        this.gson = gson;
        this.familyJoinDAO = familyJoinDAO;
        this.familyTeamDAO = familyTeamDAO;
        this.netEaseTeamManager = netEaseTeamManager;
        this.familyOperateRecordService = familyOperateRecordService;
    }


    /**
     * 设置管理员
     *
     * @param applyTeamParamBO
     * @return
     */
    @Override
    public WebServiceMessage setupAdministrator(ApplyTeamParamBO applyTeamParamBO) {
        long startTime = System.currentTimeMillis();
        if(applyTeamParamBO == null || (applyTeamParamBO.getUserId() == null && applyTeamParamBO.getUserIds() == null)){
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(applyTeamParamBO.getFamilyId());
        if(familyTeamDO != null){
            if (applyTeamParamBO.getUserIds() == null) {
                Long[] userIds = new Long[] {applyTeamParamBO.getUserId()};
                applyTeamParamBO.setUserIds(userIds);
            }
            for(Long uid : applyTeamParamBO.getUserIds()) {
                FamilyJoinDTO familyJoinDO = this.familyJoinDAO.selectByFamilyJoin(uid, familyTeamDO.getId());
                if (familyJoinDO != null) {
                    int count = this.familyJoinDAO.getRoleStatusCount(familyTeamDO.getId());
                    if (count <= 4) {
                        //普通成员
                        if (familyJoinDO.getRoleStatus() == 3) {
                            try {
                                String members = "[" + uid + "]";
                                BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.addManager(familyTeamDO.getRoomId().toString(), familyTeamDO.getUid().toString(), members));
                                if (baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()) {
                                    return WebServiceMessage.failure(baseTeamRet.getMsg());
                                } else {
                                    int status = familyJoinDAO.setupAdministrator(familyTeamDO.getId(), uid);
                                    if (status > 0) {
                                        familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), uid, "设置或取消管理员记录", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(applyTeamParamBO), new Date()));
                                        log.info("[ 设置管理员 ]接收内容:>{},耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                                    } else {
                                        log.info("[ 设置管理员 ]数据保存失败 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                                        return WebServiceMessage.failure("数据保存失败!");
                                    }
                                }
                            } catch (Exception e) {
                                log.error("设置管理员出现异常! 异常信息:{}", e);
                                return WebServiceMessage.failure("设置管理员失败!");
                            }
                        } else {
                            return WebServiceMessage.failure("该成员已是管理员或者族长!");
                        }
                    } else {
                        return WebServiceMessage.failure("管理员人数已满!");
                    }

                } else {
                    return WebServiceMessage.failure("数据不存在");
                }
            }
        }else {
            return WebServiceMessage.failure("该家族不存在!");
        }
        return WebServiceMessage.success(null);
    }


    /**
     * 根据uid 查询加入家族信息
     *
     * @param uid
     * @return
     */
    @Override
    public WebServiceMessage getJoinFamilyInfo(Long uid) {
        long startTime = System.currentTimeMillis();
        if(uid == null){
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyJoinsDTO familyTeamJoinDTO = this.familyTeamDAO.selectByUid(uid);
        if(familyTeamJoinDTO != null){
            List<FamilyTeamDTO> familyTeamDTOList = familyTeamDAO.selectByRankingList();
            familyTeamDTOList.stream().forEach(item -> {
                if(item.getFamilyId().longValue() == familyTeamJoinDTO.getFamilyId().longValue()){
                    familyTeamJoinDTO.setRanking(item.getRanking());
                    familyTeamJoinDTO.setMember(this.familyJoinDAO.countMemberByFamilyId(item.getFamilyId()));
                    familyTeamJoinDTO.setOpe(familyTeamJoinDTO.getOpe());
                }
            });
            log.info("[ 根据uid 查询加入家族信息 ] 获取数据成功 -> 接收内容:>{} , 耗时:>{} /ms", uid, (System.currentTimeMillis() - startTime));
            return WebServiceMessage.success(familyTeamJoinDTO);
        }else{
            log.info("[ 根据uid 查询加入家族信息 ]暂无数据 -> 接收内容:>{} , 耗时:>{} /ms", uid, (System.currentTimeMillis() - startTime));
            return WebServiceMessage.failure("不是该家族成员");
        }
    }


    /**
     * 根据uid检测是否加入家族
     *
     * @param uid
     * @return
     */
    @Override
    public WebServiceMessage checkFamilyJoin(Long uid) {
        long startTime = System.currentTimeMillis();
        if(uid == null){
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyJoinsDTO familyTeamJoinDTO = this.familyTeamDAO.selectByUid(uid);
        if(familyTeamJoinDTO == null){
            log.info("[ 根据uid检测是否加入家族 ] 获取数据成功 -> 接收内容:>{} , 耗时:>{} /ms", uid, (System.currentTimeMillis() - startTime));
            return WebServiceMessage.success(null);
        }else{
            FamilyDTO familyDTO = this.familyTeamDAO.selectFamilyByUid(familyTeamJoinDTO.getUid());
            familyDTO.setRoleStatus(this.familyJoinDAO.selectRoleStatusByUid(familyTeamJoinDTO.getUid()));
            familyDTO.setFamilyUsersDTOS(familyTeamDAO.selectFamilyMemberByTeamId(familyDTO.getId(),6));
            log.info("[ 根据uid检测是否加入家族 ] 获取数据成功 -> 接收内容:>{} , 耗时:>{} /ms", uid, (System.currentTimeMillis() - startTime));
            return WebServiceMessage.success(familyDTO);
        }
    }


    /**
     * 设置消息提醒
     *
     * @param familyId 家族ID
     * @param uid      用户uid
     * @param ope      1：关闭消息提醒，2：打开消息提醒，其他值无效
     * @return
     */
    @Override
    public WebServiceMessage setMsgNotify(Long familyId, Long uid, Integer ope) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        if(familyId != null && uid != null && ope != null){
            FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(familyId);
            if(familyTeamDO != null){
                FamilyJoinDO familyJoinDO = familyJoinDAO.selectByUserId(uid);
                if(familyJoinDO != null){
                    try {
                        BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.muteTeam(familyTeamDO.getRoomId().toString(),familyTeamDO.getUid().toString(),ope));
                        if(baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()){
                            endTime = System.currentTimeMillis();
                            log.info("[设置消息提醒 -> 请求调用云信响应结果] 请求参数:{} ,响应信息:{},耗时:{} /ms", Arrays.asList(familyId,uid,ope),gson.toJson(baseTeamRet),(endTime - startTime));
                            return WebServiceMessage.failure(baseTeamRet.getMsg());
                        }else{
                            //更新设置消息提醒
                            int result = familyJoinDAO.updateFamilyJoinByOpe(familyJoinDO.getId(),ope);
                            if(result > 0){
                                familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),uid,uid,"设置消息提醒", String.valueOf((System.currentTimeMillis() - startTime)),gson.toJson(Arrays.asList(familyId,uid,ope)),new Date()));
                                return WebServiceMessage.success(null);
                            }else{
                                endTime = System.currentTimeMillis();
                                log.info("[设置消息提醒 ->  操作数据库更新数据失败] 请求参数:{},耗时:{} /ms", Arrays.asList(familyId,uid,ope),(endTime - startTime));
                                return WebServiceMessage.failure("操作失败!");
                            }
                        }
                    }catch (Exception e){
                        endTime = System.currentTimeMillis();
                        log.error("[设置消息提醒 -> 请求调用云信出现异常] 请求参数:{} ,异常信息:{},耗时:{} /ms", Arrays.asList(familyId,uid,ope),e,(endTime - startTime));
                        return WebServiceMessage.failure("操作失败!");
                    }
                }else{
                    endTime = System.currentTimeMillis();
                    log.info("[设置消息提醒 -> 该用户未加入该家族] 请求参数:{} ,耗时:{} /ms", Arrays.asList(familyId,uid,ope),(endTime - startTime));
                    return WebServiceMessage.failure("该家族不存在!");
                }
            }else{
                endTime = System.currentTimeMillis();
                log.info("[设置消息提醒 -> 该家族不存在] 请求参数:{} ,耗时:{} /ms", Arrays.asList(familyId,uid,ope),(endTime - startTime));
                return WebServiceMessage.failure("该家族不存在!");
            }
        }else{
            endTime = System.currentTimeMillis();
            log.info("[设置消息提醒] 请求参数:{} ,耗时:{} /ms", Arrays.asList(familyId,uid,ope),(endTime - startTime));
            return WebServiceMessage.failure("请求参数为空");
        }
    }

    /**
     * 设置禁言及解禁
     *
     * @param faimilyUserBannedBO
     * @return
     */
    @Override
    public WebServiceMessage settingBanned(FamilyUserBannedBO faimilyUserBannedBO) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        if(faimilyUserBannedBO != null){
            FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(faimilyUserBannedBO.getFamilyId());
            if(familyTeamDO != null){
                //根据当前操作人uid查询是否有加入家族
                FamilyJoinDO familyJoinDO = familyJoinDAO.selectByUserId(faimilyUserBannedBO.getUid());
                if(familyJoinDO != null){
                    //根据被操作人uid查询是否有加入家族
                    FamilyJoinDO joinDO = familyJoinDAO.selectByUserId(faimilyUserBannedBO.getOperatorUid());
                    if(joinDO != null){
                        try {
                            BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.muteList(familyTeamDO.getRoomId().toString(),familyTeamDO.getUid().toString(),faimilyUserBannedBO.getOperatorUid().toString(),faimilyUserBannedBO.getMute()));
                            if(baseTeamRet.getCode() == WebServiceCode.SUCCESS.getValue()){
                                //更新设置禁言及解禁
                                int result = familyJoinDAO.updateFamilyJoinByMute(joinDO.getId(),faimilyUserBannedBO.getMute());
                                if(result > 0){
                                    familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),faimilyUserBannedBO.getOperatorUid(),"设置禁言及解禁", String.valueOf((System.currentTimeMillis() - startTime)),gson.toJson(faimilyUserBannedBO),new Date()));
                                    return WebServiceMessage.success(null);
                                }else{
                                    endTime = System.currentTimeMillis();
                                    log.info("[设置禁言及解禁 ->  操作数据库更新数据失败] 请求参数:{} ,响应结果:{},耗时:{} /ms", gson.toJson(faimilyUserBannedBO),gson.toJson(baseTeamRet),(endTime - startTime));
                                    return WebServiceMessage.failure("操作失败!");
                                }
                            }else {
                                endTime = System.currentTimeMillis();
                                log.info("[设置禁言及解禁 -> 请求调用云信响应结果] 请求参数:{} ,响应信息:{},耗时:{} /ms",gson.toJson(faimilyUserBannedBO),gson.toJson(baseTeamRet),(endTime - startTime));
                                return WebServiceMessage.failure(baseTeamRet.getMsg());
                            }
                        }catch (Exception e){
                            endTime = System.currentTimeMillis();
                            log.error("[设置禁言及解禁 -> 请求调用云信出现异常] 请求参数:{} ,异常信息:{},耗时:{} /ms", gson.toJson(faimilyUserBannedBO),e,(endTime - startTime));
                            return WebServiceMessage.failure("操作失败!");
                        }
                    }else{
                        endTime = System.currentTimeMillis();
                        log.info("[设置禁言及解禁 -> 被操作用户未加入该家族] 请求参数:{} ,耗时:{} /ms", gson.toJson(faimilyUserBannedBO),(endTime - startTime));
                        return WebServiceMessage.failure("被操作用户未加入该家族!");
                    }
                }else{
                    endTime = System.currentTimeMillis();
                    log.info("[设置禁言及解禁 -> 该操作用户未加入该家族] 请求参数:{} ,耗时:{} /ms", gson.toJson(faimilyUserBannedBO),(endTime - startTime));
                    return WebServiceMessage.failure("该操作用户未加入该家族!");
                }
            }else{
                endTime = System.currentTimeMillis();
                log.info("[设置禁言及解禁 -> 该家族不存在] 请求参数:{} ,耗时:{} /ms", gson.toJson(faimilyUserBannedBO),(endTime - startTime));
                return WebServiceMessage.failure("该家族不存在!");
            }
        } else{
            endTime = System.currentTimeMillis();
            log.info("[设置禁言及解禁] 请求参数:{} ,耗时:{} /ms", gson.toJson(faimilyUserBannedBO),(endTime - startTime));
            return WebServiceMessage.failure("请求参数为空");
        }
    }

    /**
     * 根据家族Id获取家族信息
     * @param familyId
     * @return
     */
    @Override
    public WebServiceMessage getFamilyInfo(Long familyId) {
        if(familyId == null){
            return WebServiceMessage.failure("请求参数为空!");
        }
        FamilyDTO familyDTO = this.familyTeamDAO.selectFamilyById(familyId);
        if(familyDTO == null){
            return WebServiceMessage.failure("该家族不存在!");
        }else{
            familyDTO.setFamilyUsersDTOS(familyTeamDAO.selectFamilyMemberByTeamId(familyDTO.getId(),6));
            return WebServiceMessage.success(familyDTO);
        }
    }

}
