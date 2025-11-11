package com.juxiao.xchat.service.api.family.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.family.dao.FamilyApplyJoinRecordDAO;
import com.juxiao.xchat.dao.family.dao.FamilyJoinDAO;
import com.juxiao.xchat.dao.family.dao.FamilyTeamDAO;
import com.juxiao.xchat.dao.family.domain.FamilyApplyJoinRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyJoinDO;
import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyTeamDO;
import com.juxiao.xchat.manager.external.netease.NetEaseTeamManager;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseTeamBO;
import com.juxiao.xchat.manager.external.netease.ret.BaseTeamRet;
import com.juxiao.xchat.manager.external.netease.utils.NetEaseTeamUtils;
import com.juxiao.xchat.service.api.family.FamilyApplyJoinRecordService;
import com.juxiao.xchat.service.api.family.FamilyOperateRecordService;
import com.juxiao.xchat.service.api.family.bo.FamilyApplyJoinParamBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family.impl
 * @date 2018/8/30
 * @time 17:53
 */
@Slf4j
@Service
public class FamilyApplyJoinRecordServiceImpl implements FamilyApplyJoinRecordService {

    private final FamilyApplyJoinRecordDAO familyApplyRecordDAO;

    private final FamilyTeamDAO familyTeamDAO;

    private final FamilyJoinDAO familyJoinDAO;

    private final Gson gson;

    private final NetEaseTeamManager netEaseTeamManager;

    private final FamilyOperateRecordService familyOperateRecordService;

    @Autowired
    public FamilyApplyJoinRecordServiceImpl(FamilyApplyJoinRecordDAO familyApplyRecordDAO, FamilyTeamDAO familyTeamDAO,
                                            FamilyJoinDAO familyJoinDAO, Gson gson,
                                            NetEaseTeamManager netEaseTeamManager, FamilyOperateRecordService familyOperateRecordService) {
        this.familyApplyRecordDAO = familyApplyRecordDAO;
        this.familyTeamDAO = familyTeamDAO;
        this.familyJoinDAO = familyJoinDAO;
        this.gson = gson;
        this.netEaseTeamManager = netEaseTeamManager;
        this.familyOperateRecordService = familyOperateRecordService;
    }

    /**
     * 保存申请记录
     *
     * @param familyApplyJoinParamBO
     * @return
     */
    @Override
    public WebServiceMessage save(FamilyApplyJoinParamBO familyApplyJoinParamBO) {
        long start = System.currentTimeMillis();
        if(familyApplyJoinParamBO == null){
            log.info("[保存申请加入记录] 接收参数:{}" ,gson.toJson(familyApplyJoinParamBO));
            return WebServiceMessage.failure("对象参数为null!");
        }
        FamilyTeamDO teamDO =  familyTeamDAO.selectByUserId(familyApplyJoinParamBO.getUserId());
        if(teamDO == null){
            FamilyJoinDO familyJoinDO = familyJoinDAO.selectFamilyJoinByUid(familyApplyJoinParamBO.getUserId());
            if(familyJoinDO == null){
                FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(familyApplyJoinParamBO.getFamilyId());
                FamilyApplyJoinRecordDO familyApplyRecordDO = familyApplyRecordDAO.selectByUidAndTeamId(familyTeamDO.getId(),familyApplyJoinParamBO.getUserId());
                if(familyApplyRecordDO == null){
                    if(familyTeamDO.getVerification() == 0){
                        try {
                            NetEaseTeamBO netEaseTeamBO = new NetEaseTeamBO();
                            netEaseTeamBO.setTid(familyTeamDO.getRoomId().toString());
                            netEaseTeamBO.setOwner(familyTeamDO.getUid().toString());
                            netEaseTeamBO.setMembers(Arrays.asList(familyApplyJoinParamBO.getUserId().toString()));
                            netEaseTeamBO.setMagree(0);
                            netEaseTeamBO.setMsg("欢迎你的到来!");
                            BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.joinTeam(netEaseTeamBO));
                            log.info("调用云信返回回调参数:{}" , gson.toJson(baseTeamRet));
                            if(baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()){
                                log.info("[保存申请加入记录 ->调用云信失败]  申请加入失败! 接收参数:{} , 耗时:{} /ms",gson.toJson(familyApplyJoinParamBO),(System.currentTimeMillis() - start));
                                return WebServiceMessage.failure(baseTeamRet.getMsg());
                            }else{
                                familyJoinDO = new FamilyJoinDO();
                                familyJoinDO.setCreateTime(new Date());
                                familyJoinDO.setTeamId(familyTeamDO.getId());
                                familyJoinDO.setUid(familyApplyJoinParamBO.getUserId());
                                familyJoinDO.setRoleStatus(3);
                                familyJoinDO.setMagree(1);
                                familyJoinDO.setMute(0);
                                familyJoinDO.setOpe(2);
                                int result = this.familyJoinDAO.save(familyJoinDO);
                                if(result > 0){
                                    familyApplyRecordDO = new FamilyApplyJoinRecordDO();
                                    familyApplyRecordDO.setStatus(1);
                                    familyApplyRecordDO.setUpdateTime(new Date());
                                    familyApplyRecordDO.setCreateTime(new Date());
                                    familyApplyRecordDO.setUid(familyApplyJoinParamBO.getUserId());
                                    familyApplyRecordDO.setTeamId(familyTeamDO.getId());
                                    int status = familyApplyRecordDAO.insert(familyApplyRecordDO);
                                    if(status > 0){
                                        familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),familyApplyJoinParamBO.getUserId(),"保存申请加入记录", String.valueOf((System.currentTimeMillis() - start)),gson.toJson(familyApplyJoinParamBO),new Date()));
                                        log.info("[保存申请加入记录] 接收参数:{} , 耗时:{} /ms",gson.toJson(familyApplyJoinParamBO),(System.currentTimeMillis() - start));
                                        return WebServiceMessage.success(null);
                                    }
                                }else{
                                    log.info("[保存申请加入记录 -> 调用familyJoinDAO.save()方法出错]  申请加入失败! 接收参数:{} , 耗时:{} /ms",gson.toJson(familyApplyJoinParamBO),(System.currentTimeMillis() - start));
                                    return WebServiceMessage.failure("操作失败!");
                                }
                            }
                        }catch (Exception e){
                            log.info("[保存申请加入记录 -> 出现异常]  申请加入失败! 异常信息:{}, 接收参数:{} , 耗时:{} /ms",e,gson.toJson(familyApplyJoinParamBO),(System.currentTimeMillis() - start));
                            return WebServiceMessage.failure("操作失败");
                        }
                    }else{
                        familyApplyRecordDO = new FamilyApplyJoinRecordDO();
                        familyApplyRecordDO.setStatus(0);
                        familyApplyRecordDO.setUpdateTime(new Date());
                        familyApplyRecordDO.setCreateTime(new Date());
                        familyApplyRecordDO.setUid(familyApplyJoinParamBO.getUserId());
                        familyApplyRecordDO.setTeamId(familyTeamDO.getId());
                        int status = familyApplyRecordDAO.insert(familyApplyRecordDO);
                           if(status > 0){
                               familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(),familyTeamDO.getUid(),familyApplyJoinParamBO.getUserId(),"保存申请加入记录", String.valueOf((System.currentTimeMillis() - start)),gson.toJson(familyApplyJoinParamBO),new Date()));
                               log.info("[保存申请加入记录] 接收参数:{} , 耗时:{} /ms",gson.toJson(familyApplyJoinParamBO),(System.currentTimeMillis() - start));
                            return WebServiceMessage.success(null);
                        }
                    }
                }else{
                    return WebServiceMessage.failure("你已经提交申请...!");
                }
            }else{
                return WebServiceMessage.failure("该用户已加入过其他家族了,故不能申请加入该家族...!");
            }
        }else{
            return WebServiceMessage.failure("你已经提交创建家族申请,暂不能加入该家族...!");
        }
        log.info("[保存申请加入记录] 接收参数:{} , 耗时:{} /ms",gson.toJson(familyApplyJoinParamBO),(System.currentTimeMillis() - start));
        return WebServiceMessage.failure("操作数据库失败!");
    }

}
