package com.juxiao.xchat.service.api.family.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.family.dao.*;
import com.juxiao.xchat.dao.family.domain.FamilyApplyJoinRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyJoinDO;
import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyTeamDO;
import com.juxiao.xchat.dao.family.dto.*;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.constant.AppClient;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseTeamManager;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseCreateTeamBO;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseTeamBO;
import com.juxiao.xchat.manager.external.netease.ret.BaseTeamRet;
import com.juxiao.xchat.manager.external.netease.utils.NetEaseTeamUtils;
import com.juxiao.xchat.service.api.family.FamilyOperateRecordService;
import com.juxiao.xchat.service.api.family.FamilyTeamService;
import com.juxiao.xchat.service.api.family.bo.*;
import com.juxiao.xchat.service.api.family.po.FamilyTeamPO;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author laizhilong
 * @Title: 家族service实现
 * @Package com.juxiao.xchat.service.api.family.impl
 * @date 2018/8/30
 * @time 18:09
 */
@Slf4j
@Service
public class FamilyTeamServiceImpl implements FamilyTeamService {

    private FamilyTeamDAO familyTeamDAO;

    private LevelManager levelManager;

    private UsersManager usersManager;

    private FamilyApplyJoinRecordDAO familyApplyJoinRecordDAO;

    private FamilyJoinDAO familyJoinDAO;

    private FamilyGiftRecordDAO familyGiftRecordDAO;

    private FamilyExitRecordDAO familyExitRecordDAO;

    private Gson gson;

    private AppVersionManager appVersionService;

    private NetEaseTeamManager netEaseTeamManager;

    private FamilyOperateRecordService familyOperateRecordService;


    /**
     * 黄金等级 黄金2，手上4个厅 ，后台改为LV12
     */
    private static final int glod_level = 1;

    @Autowired
    public FamilyTeamServiceImpl(FamilyTeamDAO familyTeamDAO, LevelManager levelManager, UsersManager usersManager,
                                 FamilyApplyJoinRecordDAO familyApplyJoinRecordDAO, FamilyJoinDAO familyJoinDAO,
                                 FamilyGiftRecordDAO familyGiftRecordDAO, FamilyExitRecordDAO familyExitRecordDAO,
                                 Gson gson, AppVersionManager appVersionService, NetEaseTeamManager netEaseTeamManager,
                                 FamilyOperateRecordService familyOperateRecordService) {
        this.familyTeamDAO = familyTeamDAO;
        this.levelManager = levelManager;
        this.usersManager = usersManager;
        this.familyApplyJoinRecordDAO = familyApplyJoinRecordDAO;
        this.familyJoinDAO = familyJoinDAO;
        this.familyGiftRecordDAO = familyGiftRecordDAO;
        this.familyExitRecordDAO = familyExitRecordDAO;
        this.gson = gson;
        this.appVersionService = appVersionService;
        this.netEaseTeamManager = netEaseTeamManager;
        this.familyOperateRecordService = familyOperateRecordService;
    }


    /**
     * 保存
     *
     * @param familyTeamParamBO
     * @return
     */
    @Override
    public WebServiceMessage save(FamilyTeamParamBO familyTeamParamBO) {
        long startTime = System.currentTimeMillis();
        FamilyTeamDO familyTeamDO = familyTeamDAO.selectByUserId(Long.valueOf(familyTeamParamBO.getUserId()));
        if (familyTeamDO == null) {
            FamilyJoinDO familyJoinDO = this.familyJoinDAO.selectFamilyJoinByUid(Long.valueOf(familyTeamParamBO.getUserId()));
            if (familyJoinDO == null) {
                int expLevel = levelManager.getUserExperienceLevelSeq(Long.valueOf(familyTeamParamBO.getUserId()));
                //expLevel 大于等于12的时候则为黄金等级
//            if(expLevel >= glod_level){
                UsersDTO usersDTO = usersManager.getUser(Long.valueOf(familyTeamParamBO.getUserId()));
                double random = Math.random() * 9000 + 1000;
                familyTeamDO = new FamilyTeamDO();
                familyTeamDO.setBgImg(familyTeamParamBO.getBgImg());
                familyTeamDO.setFamilyName(familyTeamParamBO.getName());
                familyTeamDO.setCreateTime(new Date());
                familyTeamDO.setFamilyLogo(familyTeamParamBO.getLogo());
                familyTeamDO.setFamilyNotice(familyTeamParamBO.getNotice());
                familyTeamDO.setHall(familyTeamParamBO.getHall());
                familyTeamDO.setPrestige(0);
                familyTeamDO.setStatus(0);
                familyTeamDO.setFamilyId((long) Math.ceil(random));
                familyTeamDO.setUid(usersDTO.getUid());
                familyTeamDO.setDisplay(1);
                familyTeamDO.setUpdateTime(familyTeamDO.getCreateTime());
                familyTeamDO.setVerification(0);
                familyTeamDO.setBeinvitemode(0);
                familyTeamDO.setInvitemode(0);
                familyTeamDO.setMuteType(0);
                familyTeamDO.setUptinfomode(0);
                int status = familyTeamDAO.save(familyTeamDO);
                if (status > 0) {
                    FamilyApplyJoinRecordDO applyJoinRecordDO = new FamilyApplyJoinRecordDO();
                    applyJoinRecordDO.setUpdateTime(new Date());
                    applyJoinRecordDO.setUid(usersDTO.getUid());
                    applyJoinRecordDO.setTeamId(familyTeamDO.getId());
                    applyJoinRecordDO.setStatus(0);
                    applyJoinRecordDO.setCreateTime(new Date());
                    familyApplyJoinRecordDAO.insert(applyJoinRecordDO);
                    familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), usersDTO.getUid(), usersDTO.getUid(), "创建家族", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(familyTeamParamBO), new Date()));
                    log.info("[ 创建家族 ]接收内容:>{},耗时:>{}", gson.toJson(familyTeamParamBO), (System.currentTimeMillis() - startTime));
                    return WebServiceMessage.success(null);
                } else {
                    log.info("[ 创建家族 ]数据保存失败 -> 接收内容:>{} , 耗时:>{}", gson.toJson(familyTeamParamBO), (System.currentTimeMillis() - startTime));
                    return WebServiceMessage.failure("数据保存失败!");
                }
//            }else{
//                log.info("[ 创建家族 ] 等级未达到 -> 接收内容:>{}, 当前等级:{} , 耗时:>{}", gson.toJson(familyTeamParamBO) , expLevel , (System.currentTimeMillis() - startTime));
//                return WebServiceMessage.failure("等级未达到!");
//            }
            } else {
                return WebServiceMessage.failure("你已加入过其他家族或者已创建过家族 !");
            }
        } else {
            return WebServiceMessage.failure("你已申请过创建家族!");
        }
    }


    /**
     * 更新
     *
     * @param familyTeamEditParamBO
     * @return
     */
    @Override
    public WebServiceMessage update(FamilyTeamEditParamBO familyTeamEditParamBO) {
        long startTime = System.currentTimeMillis();
        if (familyTeamEditParamBO == null) {
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = this.familyTeamDAO.selectByTeamId(familyTeamEditParamBO.getFamilyId());
        if (familyTeamDO != null) {
            if (DateUtils.inOneMonth(familyTeamDO.getUpdateTime()) || familyTeamDO.getFamilyLogo().equals(familyTeamEditParamBO.getLogo())) {
                try {
                    NetEaseCreateTeamBO netEaseCreateTeamBO = new NetEaseCreateTeamBO();
                    netEaseCreateTeamBO.setTid(familyTeamDO.getRoomId().toString());
                    netEaseCreateTeamBO.setOwner(familyTeamDO.getUid().toString());
                    netEaseCreateTeamBO.setAnnouncement(familyTeamEditParamBO.getNotice());
                    netEaseCreateTeamBO.setIcon(familyTeamEditParamBO.getLogo());
                    BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.update(netEaseCreateTeamBO));
                    if (baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()) {
                        log.info("[更新家族信息 -> 请求调用云信出现异常]  请求参数:{} , 响应数据:{} , 耗时:{} /ms", gson.toJson(familyTeamEditParamBO), gson.toJson(baseTeamRet), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.failure(baseTeamRet.getMsg());
                    } else {
                        familyTeamDO.setFamilyLogo(familyTeamEditParamBO.getLogo());
                        familyTeamDO.setFamilyNotice(familyTeamEditParamBO.getNotice());
                        familyTeamDO.setBgImg(familyTeamEditParamBO.getBgImg());
                        int status = familyTeamDAO.updateByLogoAndNotice(familyTeamDO);
                        if (status > 0) {
                            familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), familyTeamDO.getUid(), "更新家族资料", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(familyTeamEditParamBO), new Date()));
                            log.info("[ 更新家族资料 ]接收内容:>{},耗时:>{}", gson.toJson(familyTeamEditParamBO), (System.currentTimeMillis() - startTime));
                            return WebServiceMessage.success(null);
                        } else {
                            log.info("[ 更新家族资料 ]数据保存失败 -> 接收内容:>{} , 耗时:>{}", gson.toJson(familyTeamEditParamBO), (System.currentTimeMillis() - startTime));
                            return WebServiceMessage.failure("数据失败!");
                        }
                    }
                } catch (Exception e) {
                    log.error("[更新家族信息 -> 请求调用云信出现异常] 请求参数:{} ,异常信息:{},耗时:{}/ms", gson.toJson(familyTeamEditParamBO), e, (System.currentTimeMillis() - startTime));
                    return WebServiceMessage.failure("操作失败!");
                }
            } else {
                log.error("[更新家族信息 -> 一个月只能编辑一次] 请求参数:{} ,耗时:{}/ms", gson.toJson(familyTeamEditParamBO), (System.currentTimeMillis() - startTime));
                return WebServiceMessage.failure("暂不能编辑数据");
            }
        } else {
            log.error("[更新家族信息 -> 数据不存在] 请求参数:{} ,耗时:{}/ms", gson.toJson(familyTeamEditParamBO), (System.currentTimeMillis() - startTime));
            return WebServiceMessage.failure("数据不存在");
        }
    }

    /**
     * 根据族长 ID查询族长信息
     *
     * @param teamId
     * @return
     */
    @Override
    public FamilyTeamDTO getFamilyTeamInfo(Long teamId) {
        FamilyTeamDO familyTeamDO = this.familyTeamDAO.selectByTeamId(teamId);
        if (familyTeamDO != null) {
            int member = familyJoinDAO.countMember(familyTeamDO.getId());
            FamilyTeamDTO familyTeamDTO = new FamilyTeamDTO();
            familyTeamDTO.setFamilyId(familyTeamDO.getFamilyId());
            familyTeamDTO.setFamilyLogo(familyTeamDO.getFamilyLogo());
            familyTeamDTO.setFamilyName(familyTeamDO.getFamilyName());
            familyTeamDTO.setFamilyNotice(familyTeamDO.getFamilyNotice());
            familyTeamDTO.setPrestige(familyTeamDO.getPrestige());
            familyTeamDTO.setMember(member);
            familyTeamDTO.setTimes(familyTeamDO.getCreateTime().getTime());
            return familyTeamDTO;
        } else {
            return null;
        }
    }

    /**
     * 设置申请加入方式
     *
     * @param teamId
     * @return
     */
    @Override
    public WebServiceMessage setApplyJoinMethod(Long teamId, Integer joinmode) {
        long startTime = System.currentTimeMillis();
        if (teamId == null) {
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = this.familyTeamDAO.selectByTeamId(teamId);
        if (familyTeamDO == null) {
            return WebServiceMessage.failure("该家族不存在!");
        } else {
            try {
                NetEaseCreateTeamBO netEaseCreateTeamBO = new NetEaseCreateTeamBO();
                netEaseCreateTeamBO.setTid(familyTeamDO.getRoomId().toString());
                netEaseCreateTeamBO.setOwner(familyTeamDO.getUid().toString());
                netEaseCreateTeamBO.setJoinmode(joinmode);
                BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.update(netEaseCreateTeamBO));
                if (baseTeamRet.getCode() == WebServiceCode.SUCCESS.getValue()) {
                    int status = familyTeamDAO.setApplyJoinMethod(familyTeamDO.getId(), teamId, joinmode);
                    if (status > 0) {
                        familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), familyTeamDO.getUid(), "设置申请加入方式", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(Arrays.asList(teamId, joinmode)), new Date()));
                        log.info("[ 设置申请加入方式 ]接收内容:>{},耗时:>{}", teamId, (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.success(null);
                    } else {
                        log.info("[ 设置申请加入方式 ]数据保存失败 -> 接收内容:>{} , 耗时:>{}", gson.toJson(Arrays.asList(teamId, joinmode)), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.failure("操作失败!");
                    }
                } else {
                    log.info("[ 设置申请加入方式 ]数据保存失败 -> 接收内容:>{} , 耗时:>{}", gson.toJson(Arrays.asList(teamId, joinmode)), (System.currentTimeMillis() - startTime));
                    return WebServiceMessage.failure(baseTeamRet.getMsg());
                }
            } catch (Exception e) {
                log.error("[云信]设置申请加入方式失败! 异常信息:{}", e);
                return WebServiceMessage.failure("设置失败!");
            }
        }
    }


    /**
     * 获取家族成员列表
     *
     * @param teamParamsBO
     * @return
     */
    @Override
    public WebServiceMessage getFamilyTeamJoin(TeamParamsBO teamParamsBO) {
        if (teamParamsBO == null) {
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = this.familyTeamDAO.selectByTeamId(teamParamsBO.getFamilyId());
        if (familyTeamDO == null) {
            return WebServiceMessage.failure("数据不存在!");
        }
        log.info("【获取家族成员列表】请求参数:{}", gson.toJson(teamParamsBO));
        List<FamilyTeamJoinDTO> familyTeamJoinDTOS = this.familyJoinDAO.selectFamilyTeamJoinByTeamId(familyTeamDO.getId(), (teamParamsBO.getCurrent() * teamParamsBO.getPageSize()), teamParamsBO.getPageSize());
        familyTeamJoinDTOS.stream().forEach(item -> {
            item.setLevel(levelManager.getUserExperienceLevelSeq(item.getUid()));
            item.setPrestige(familyGiftRecordDAO.countPrestigeByUidAndTeamId(familyTeamDO.getId(), item.getUid()));
        });
        Collections.sort(familyTeamJoinDTOS, new Comparator<FamilyTeamJoinDTO>() {
            @Override
            public int compare(FamilyTeamJoinDTO o1, FamilyTeamJoinDTO o2) {
                int item = o1.getRoleStatus() - o2.getRoleStatus();
                if (item == 0) {
                    return o2.getLevel() - o1.getLevel();
                }
                return item;
            }
        });
        int member = familyJoinDAO.countMember(familyTeamDO.getId());
        FamilyTeamDTO familyTeamDTO = new FamilyTeamDTO();
        familyTeamDTO.setFamilyId(familyTeamDO.getFamilyId());
        familyTeamDTO.setFamilyLogo(familyTeamDO.getFamilyLogo());
        familyTeamDTO.setFamilyName(familyTeamDO.getFamilyName());
        familyTeamDTO.setFamilyNotice(familyTeamDO.getFamilyNotice());
        familyTeamDTO.setPrestige(familyGiftRecordDAO.countPrestige(familyTeamDO.getId()));
        familyTeamDTO.setMember(member);
        familyTeamDTO.setRoomId(familyTeamDO.getRoomId());
        familyTeamDTO.setTimes(familyTeamDO.getCreateTime().getTime());
        familyTeamDTO.setVerification(familyTeamDO.getVerification());
        familyTeamDTO.setMuteType(familyTeamDO.getMuteType());
        familyTeamDTO.setBeinvitemode(familyTeamDO.getBeinvitemode());
        familyTeamDTO.setInvitemode(familyTeamDO.getInvitemode());
        familyTeamDTO.setUptinfomode(familyTeamDO.getUptinfomode());
        familyTeamDTO.setFamilyTeamJoinDTOS(familyTeamJoinDTOS);
        return WebServiceMessage.success(familyTeamDTO);
    }


    /**
     * 审核加入家族
     *
     * @param applyFamilyParamBO
     * @return
     */
    @Override
    public WebServiceMessage applyJoinFamily(ApplyFamilyParamBO applyFamilyParamBO) {
        long startTime = System.currentTimeMillis();
        if (applyFamilyParamBO == null) {
            return WebServiceMessage.failure("请求参数为null");
        }
        FamilyTeamDO familyTeamDO = this.familyTeamDAO.selectByTeamId(applyFamilyParamBO.getFamilyId());
        if (familyTeamDO == null) {
            return WebServiceMessage.failure("该家族不存在!");
        }
        //同意
        if (applyFamilyParamBO.getStatus() == 1) {
            FamilyJoinDO familyJoinDTO = familyJoinDAO.selectByUserId(applyFamilyParamBO.getUserId());
            if (familyJoinDTO != null) {
                return WebServiceMessage.failure("该用户已加入家族!");
            } else {
                FamilyApplyJoinRecordDO familyApplyJoinRecordDO = this.familyApplyJoinRecordDAO.selectByUidAndTeamId(familyTeamDO.getId(), applyFamilyParamBO.getUserId());
                if (familyApplyJoinRecordDO == null) {
                    return WebServiceMessage.failure("没有申请记录!");
                } else {
                    try {
                        NetEaseTeamBO netEaseTeamBO = new NetEaseTeamBO();
                        netEaseTeamBO.setTid(familyTeamDO.getRoomId().toString());
                        netEaseTeamBO.setOwner(familyTeamDO.getUid().toString());
                        netEaseTeamBO.setMembers(Arrays.asList(applyFamilyParamBO.getUserId().toString()));
                        netEaseTeamBO.setMagree(0);
                        netEaseTeamBO.setMsg("欢迎你的到来!");
                        BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.joinTeam(netEaseTeamBO));
                        if (baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()) {
                            return WebServiceMessage.failure(baseTeamRet.getMsg());
                        } else {
                            FamilyJoinDO familyJoinDO = new FamilyJoinDO();
                            familyJoinDO.setRoleStatus(3);
                            familyJoinDO.setUid(applyFamilyParamBO.getUserId());
                            familyJoinDO.setTeamId(familyTeamDO.getId());
                            familyJoinDO.setCreateTime(new Date());
                            familyJoinDO.setMagree(1);
                            familyJoinDO.setMute(0);
                            familyJoinDO.setOpe(2);
                            int status = this.familyJoinDAO.save(familyJoinDO);
                            if (status > 0) {
                                status = this.familyApplyJoinRecordDAO.updateByStatus(familyTeamDO.getId(), familyApplyJoinRecordDO.getUid(), applyFamilyParamBO.getStatus());
                                if (status > 0) {
                                    familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), applyFamilyParamBO.getUserId(), "同意审核加入家族", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(applyFamilyParamBO), new Date()));
                                    return WebServiceMessage.success(null);
                                } else {
                                    return WebServiceMessage.failure("更新操作数据失败!");
                                }
                            } else {
                                return WebServiceMessage.failure("更新操作数据失败!");
                            }
                        }
                    } catch (Exception e) {
                        log.info("[ 审核加入家族 ] 异常唯一索引抛出 接收内容:>{},耗时:>{}", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.failure("该用户已加入过其他家族!");
                    }
                }
            }
            //拒绝
        } else {
            FamilyApplyJoinRecordDO familyApplyJoinRecordDO = this.familyApplyJoinRecordDAO.selectByUidAndTeamId(familyTeamDO.getId(), applyFamilyParamBO.getUserId());
            if (familyApplyJoinRecordDO != null) {
                int result = this.familyApplyJoinRecordDAO.updateByStatus(familyTeamDO.getId(), familyApplyJoinRecordDO.getUid(), applyFamilyParamBO.getStatus());
                if (result > 0) {
                    familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), applyFamilyParamBO.getUserId(), "拒绝审核加入家族", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(applyFamilyParamBO), new Date()));
                    log.info("[ 审核加入家族 ]接收内容:>{},耗时:>{}", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));
                    return WebServiceMessage.success(null);
                } else {
                    log.info("[ 审核加入家族 ]数据保存失败 -> 接收内容:>{} , 耗时:>{}", gson.toJson(applyFamilyParamBO), (System.currentTimeMillis() - startTime));

                    return WebServiceMessage.failure("更新操作数据失败!");
                }
            } else {
                return WebServiceMessage.failure("没有申请记录!");
            }
        }

    }


    /**
     * 获取家族列表信息
     *
     * @param teamParamsBO 搜索家族ID
     * @return
     */
    @Override
    public WebServiceMessage getList(TeamParamsBO teamParamsBO, String clientIp, String app) throws WebServiceException {
        if (appVersionService.checkAuditingVersion(teamParamsBO.getOs(), app, teamParamsBO.getAppVersion(), clientIp,teamParamsBO.getUid())) {
            // IOS新版本在审核期内的首页数据要做特殊处理
            List<FamilyDTO> familyDTOS = familyTeamDAO.selectAuditingVersion();
            familyDTOS.stream().forEach(item -> {
                item.setTimes(item.getUpdateTime().getTime());
                item.setRoomId(item.getRoomId());
                item.setNick(usersManager.getUser(item.getUid()).getNick());
            });
            FamilyJoinsDTO familyJoinsDTO = this.familyTeamDAO.selectByUidRanking(Long.valueOf(teamParamsBO.getUid()));
            if (familyJoinsDTO != null) {
                familyDTOS.stream().forEach(item -> {
                    if (item.getFamilyId().longValue() == familyJoinsDTO.getFamilyId().longValue()) {
                        int member = familyJoinDAO.countMemberByFamilyId(item.getFamilyId());
                        familyJoinsDTO.setMember(member);
                        familyJoinsDTO.setRanking(item.getRanking());
                        familyJoinsDTO.setRoomId(item.getRoomId());
                        familyJoinsDTO.setVerification(item.getVerification());
                        familyJoinsDTO.setNick(usersManager.getUser(item.getUid()).getNick());
                        familyJoinsDTO.setTimes(item.getCreateTime().getTime());
                    } else {
                        List<FamilyDTO> temp = this.familyTeamDAO.selectByList();
                        temp.forEach(temps -> {
                            temps.setTimes(temps.getUpdateTime().getTime());
                            temps.setRoomId(temps.getRoomId());
                            if (item.getFamilyId().longValue() == familyJoinsDTO.getFamilyId().longValue()) {
                                familyJoinsDTO.setRanking(item.getRanking());
                                familyJoinsDTO.setRoomId(item.getRoomId());
                                familyJoinsDTO.setNick(usersManager.getUser(temps.getUid()).getNick());
                                familyJoinsDTO.setVerification(item.getVerification());
                                familyJoinsDTO.setTimes(item.getCreateTime().getTime());
                            }
                        });
                    }
                });
            }
            FamilyTeamPO familyTeam = new FamilyTeamPO();
            familyTeam.setFamilyList(familyDTOS);
            familyTeam.setFamilyTeam(familyJoinsDTO);
            return WebServiceMessage.success(familyTeam);
        }
        if (teamParamsBO.getCurrent() == null && teamParamsBO.getPageSize() == null) {
            teamParamsBO.setCurrent(0);
            teamParamsBO.setPageSize(20);
        }
        long startTime = System.currentTimeMillis();
        List<FamilyDTO> familyTeamDTOS = this.familyTeamDAO.selectByPage(teamParamsBO.getFamilyId(), (teamParamsBO.getCurrent() * teamParamsBO.getPageSize()), teamParamsBO.getPageSize());

        familyTeamDTOS.stream().forEach(item -> {
            item.setTimes(item.getUpdateTime().getTime());
            item.setRoomId(item.getRoomId());
            UsersDTO usersDto = usersManager.getUser(item.getUid());
            item.setNick(usersDto == null ? null : usersDto.getNick());
            item.setFamilyUsersDTOS(familyTeamDAO.selectFamilyMemberByTeamId(item.getId(), 6));
        });
        FamilyJoinsDTO familyTeamJoinDTO = this.familyTeamDAO.selectByUidRanking(Long.valueOf(teamParamsBO.getUid()));
        if (familyTeamJoinDTO != null) {
            familyTeamDTOS.stream().forEach(item -> {
                if (item.getFamilyId().longValue() == familyTeamJoinDTO.getFamilyId().longValue()) {
                    int member = familyJoinDAO.countMemberByFamilyId(item.getFamilyId());
                    familyTeamJoinDTO.setMember(member);
                    familyTeamJoinDTO.setNick(usersManager.getUser(item.getUid()).getNick());
                    familyTeamJoinDTO.setRanking(item.getRanking());
                    familyTeamJoinDTO.setRoomId(item.getRoomId());
                    familyTeamJoinDTO.setVerification(item.getVerification());
                    familyTeamJoinDTO.setTimes(item.getCreateTime().getTime());
                } else {
                    List<FamilyDTO> temp = this.familyTeamDAO.selectByList();
                    temp.forEach(items -> {
                        items.setTimes(items.getUpdateTime().getTime());
                        items.setRoomId(items.getRoomId());
                        if (item.getFamilyId().longValue() == familyTeamJoinDTO.getFamilyId().longValue()) {
                            familyTeamJoinDTO.setRanking(item.getRanking());
                            familyTeamJoinDTO.setRoomId(item.getRoomId());
                            familyTeamJoinDTO.setNick(usersManager.getUser(items.getUid()).getNick());
                            familyTeamJoinDTO.setVerification(item.getVerification());
                            familyTeamJoinDTO.setTimes(item.getCreateTime().getTime());
                        }
                    });
                }
            });
        }
        FamilyTeamPO familyTeam = new FamilyTeamPO();
        familyTeam.setFamilyList(familyTeamDTOS);
        familyTeam.setFamilyTeam(familyTeamJoinDTO);
        log.info("[ 获取家族列表信息 ]获取成功! -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(teamParamsBO), (System.currentTimeMillis() - startTime));
        return WebServiceMessage.success(familyTeam);
    }


    /**
     * 获取家族消息
     *
     * @param familyId
     * @return
     */
    @Override
    public WebServiceMessage getFamilyMessage(Long familyId, Long uid, Integer current, Integer pageSize) {
        long start = System.currentTimeMillis();
        if (familyId == null && uid == null) {
            log.info("[获取家族消息] 接收参数:{}", familyId);
            return WebServiceMessage.failure("对象参数为null!");
        }
        List<FamilyRecordDTO> familyRecordDTOS = new ArrayList<>();
        FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(familyId);
        if (familyTeamDO == null) {
            return WebServiceMessage.failure("该家族不存在!");
        }
        FamilyJoinDTO familyJoinDTO = familyJoinDAO.selectByFamilyJoin(uid, familyTeamDO.getId());
        if (familyJoinDTO == null || familyJoinDTO.getRoleStatus() == 3) {
            return WebServiceMessage.failure("无权限访问!");
        }
        List<FamilyRecordDTO> familyRecordDTOList = this.familyApplyJoinRecordDAO.selectByFamilyId(familyTeamDO.getId(), (current * pageSize), pageSize);
        if (familyRecordDTOList != null && familyRecordDTOList.size() > 0) {
            familyRecordDTOList.forEach(item -> {
                item.setType(1);
                item.setCharm(levelManager.getUserCharmLevelSeq(item.getUid()));
                item.setLevel(levelManager.getUserExperienceLevelSeq(item.getUid()));
                familyRecordDTOS.add(item);
            });
        }
        List<FamilyRecordDTO> familyExitRecordDTOS = this.familyExitRecordDAO.selectByFamilyId(familyTeamDO.getId(), (current * pageSize), pageSize);
        if (familyExitRecordDTOS != null && familyExitRecordDTOS.size() > 0) {
            familyExitRecordDTOS.forEach(item -> {
                item.setType(2);
                item.setCharm(levelManager.getUserCharmLevelSeq(item.getUid()));
                item.setLevel(levelManager.getUserExperienceLevelSeq(item.getUid()));
                familyRecordDTOS.add(item);
            });
        }
        log.info("[获取家族消息] 接收参数:{} , 耗时:{} /ms", familyId, (System.currentTimeMillis() - start));
        return WebServiceMessage.success(familyRecordDTOS);

    }

    /**
     * 移除管理员
     *
     * @param applyTeamParamBO
     * @return
     */
    @Override
    public WebServiceMessage removeAdmin(ApplyTeamParamBO applyTeamParamBO) {
        long startTime = System.currentTimeMillis();
        if (applyTeamParamBO == null) {
            log.info("[获取加入申请记录] 接收参数:{}", gson.toJson(applyTeamParamBO));
            return WebServiceMessage.failure("请求参数为空!");
        }
        FamilyTeamDO familyTeamDO = this.familyTeamDAO.selectByTeamId(applyTeamParamBO.getFamilyId());
        FamilyJoinDTO familyJoinDO = this.familyJoinDAO.selectByFamilyJoin(applyTeamParamBO.getUserId(), familyTeamDO.getId());
        if (familyJoinDO != null) {
            try {
                String members = "[" + applyTeamParamBO.getUserId() + "]";
                BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.removeManager(familyTeamDO.getRoomId().toString(), familyTeamDO.getUid().toString(), members));
                if (baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()) {
                    log.info("[ 移除管理员(操作云信) ] 移除失败 -> 接收内容:>{} 响应结果:>{}, 耗时:>{} /ms", gson.toJson(applyTeamParamBO), gson.toJson(baseTeamRet), (System.currentTimeMillis() - startTime));
                    return WebServiceMessage.failure(baseTeamRet.getMsg());
                } else {
                    int status = familyJoinDAO.removeAdmin(familyTeamDO.getId(), applyTeamParamBO.getUserId());
                    if (status > 0) {
                        familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), applyTeamParamBO.getUserId(), "移除管理员", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(applyTeamParamBO), new Date()));
                        log.info("[ 移除管理员 ] 移除成功 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.success(null);
                    } else {
                        log.info("[ 移除管理员 ] 移除失败 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                        return WebServiceMessage.failure("移除失败!");
                    }
                }
            } catch (Exception e) {
                log.info("[ 移除管理员(操作云信) ] 移除失败 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
                return WebServiceMessage.failure("移除失败!");
            }
        } else {
            log.info("[ 移除管理员 ] 数据不存在 -> 接收内容:>{} , 耗时:>{} /ms", gson.toJson(applyTeamParamBO), (System.currentTimeMillis() - startTime));
            return WebServiceMessage.failure("数据不存在!");
        }
    }

    /**
     * 设置邀请人权限
     *
     * @param invitationPermissionBO
     * @return
     */
    @Override
    public WebServiceMessage invitationPermission(FamilyInvitationPermissionBO invitationPermissionBO) {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        if (invitationPermissionBO != null) {
            FamilyTeamDO familyTeamDO = familyTeamDAO.selectByTeamId(invitationPermissionBO.getFamilyId());
            if (familyTeamDO != null) {
                try {
                    NetEaseCreateTeamBO netEaseCreateTeamBO = new NetEaseCreateTeamBO();
                    netEaseCreateTeamBO.setTid(familyTeamDO.getRoomId().toString());
                    netEaseCreateTeamBO.setOwner(familyTeamDO.getUid().toString());
                    netEaseCreateTeamBO.setInvitemode(invitationPermissionBO.getInvitemode());
                    BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.update(netEaseCreateTeamBO));
                    if (baseTeamRet.getCode() == WebServiceCode.SUCCESS.getValue()) {
                        int result = familyTeamDAO.updateByInvoteMode(familyTeamDO.getId(), invitationPermissionBO.getInvitemode());
                        if (result > 0) {
                            familyOperateRecordService.save(new FamilyOperateRecordDO(familyTeamDO.getFamilyId(), familyTeamDO.getUid(), invitationPermissionBO.getUid(), "设置邀请人权限", String.valueOf((System.currentTimeMillis() - startTime)), gson.toJson(invitationPermissionBO), new Date()));
                            return WebServiceMessage.success(null);
                        } else {
                            endTime = System.currentTimeMillis();
                            log.info("[设置邀请人权限 ->  操作数据库更新数据失败] 请求参数:{} ,响应结果:{},耗时:{} /ms", gson.toJson(invitationPermissionBO), gson.toJson(baseTeamRet), (endTime - startTime));
                            return WebServiceMessage.failure("操作失败!");
                        }
                    } else {
                        endTime = System.currentTimeMillis();
                        log.info("[设置邀请人权限 -> 请求调用云信响应结果] 请求参数:{} ,响应信息:{},耗时:{} /ms", gson.toJson(invitationPermissionBO), gson.toJson(baseTeamRet), (endTime - startTime));
                        return WebServiceMessage.failure(baseTeamRet.getMsg());
                    }
                } catch (Exception e) {
                    endTime = System.currentTimeMillis();
                    log.error("[设置邀请人权限 -> 请求调用云信出现异常] 请求参数:{} ,异常信息:{},耗时:{} /ms", gson.toJson(invitationPermissionBO), e, (endTime - startTime));
                    return WebServiceMessage.failure("操作失败!");
                }
            } else {
                endTime = System.currentTimeMillis();
                log.info("[设置邀请人权限 -> 该家族不存在] 请求参数:{} ,耗时:{} /ms", gson.toJson(invitationPermissionBO), (endTime - startTime));
                return WebServiceMessage.failure("该家族不存在!");
            }
        } else {
            endTime = System.currentTimeMillis();
            log.info("[设置邀请人权限] 请求参数:{} ,耗时:{} /ms", gson.toJson(invitationPermissionBO), (endTime - startTime));
            return WebServiceMessage.failure("请求参数为空");
        }
    }
}


