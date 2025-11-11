package com.juxiao.xchat.service.api.guild.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.GuildHallApplyRecordDao;
import com.juxiao.xchat.dao.guild.GuildHallMemberDao;
import com.juxiao.xchat.dao.guild.domain.GuildDO;
import com.juxiao.xchat.dao.guild.domain.GuildHallApplyRecordDO;
import com.juxiao.xchat.dao.guild.domain.GuildHallMemberDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallApplyRecordDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMemberDTO;
import com.juxiao.xchat.dao.guild.enumeration.GuildHallApplyStatus;
import com.juxiao.xchat.dao.guild.enumeration.GuildHallApplyType;
import com.juxiao.xchat.dao.guild.enumeration.GuildHallMemberType;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.guild.GuildHallManager;
import com.juxiao.xchat.manager.common.guild.GuildHallMemberManager;
import com.juxiao.xchat.manager.common.guild.GuildManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import com.juxiao.xchat.service.api.guild.GuildHallApplyRecordService;
import com.juxiao.xchat.service.api.guild.bo.ApplyHallParamBo;
import com.juxiao.xchat.service.api.guild.bo.VerifyParamBo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @创建时间： 2020/10/14 16:48
 * @作者： carl
 */
@Slf4j
@Service
public class GuildHallApplyRecordServiceImpl implements GuildHallApplyRecordService {

    private final String myGuildIndexPageUrl = "/front/association/index.html";

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private GuildManager guildManager;

    @Autowired
    private GuildHallManager guildHallManager;

    @Autowired
    private GuildHallMemberManager guildHallMemberManager;

    @Autowired
    private GuildHallApplyRecordDao guildHallApplyRecordDao;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private GuildHallMemberDao guildHallMemberDao;

    @Override
    public boolean applyJoinHall(ApplyHallParamBo paramBo) throws WebServiceException {
        if (paramBo.getUid() == null || paramBo.getHallId() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 限制填写120字符
        String reason = paramBo.getReason().trim();
        if (reason.length() > 120) {
            throw new WebServiceException(WebServiceCode.GUILD_APPLY_JOIN_REASON_TOO_LONG);
        }

        // 检查用户是否存在
        UsersDTO usersDTO = usersManager.getUser(paramBo.getUid());
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        GuildHallDTO hallDTO = guildHallManager.getHall(paramBo.getHallId());
        if (hallDTO == null) {
            throw new WebServiceException(WebServiceCode.GUILD_HALL_NOT_EXIST);
        }
        else if (hallDTO.getMemberCount() >= 200) { // 每个厅只能加入200人，若获取成员表的数量，要注意厅主没有记录，即算199
            throw new WebServiceException(WebServiceCode.GUILD_HALL_IS_FULL);
        }


        GuildHallMemberDTO memberDTO = guildHallMemberDao.getGuildMemberInfo(paramBo.getUid());
        if (memberDTO != null) {
            // 检查是否已加入其他公会
            if (memberDTO.getGuildId().compareTo(hallDTO.getGuildId()) != 0) {
                throw new WebServiceException(WebServiceCode.GUILD_ALREADY_JOIN_OTHER);
            }
            // 检查是否已加入其他厅
            if (memberDTO.getHallId() != null) {
                if (memberDTO.getHallId().compareTo(paramBo.getHallId()) != 0)
                    throw new WebServiceException(WebServiceCode.GUILD_HALL_ALREADY_JOIN_OTHER);
                else
                    throw new WebServiceException(WebServiceCode.GUILD_HALL_ALREADY_JOIN);
            }
        }

        // 是否已提交过申请（在同一公会内）
        List<GuildHallApplyRecordDTO> applyJoinRecordDTOs = guildHallApplyRecordDao.selectByUidAndTypeAndStatus(paramBo.getUid(), GuildHallApplyType.JOIN.getValue(), GuildHallApplyStatus.AUDITING.getValue());
        if (applyJoinRecordDTOs != null && applyJoinRecordDTOs.size() > 0) {
            Optional<GuildHallApplyRecordDTO> applyJoinRecordDTO = applyJoinRecordDTOs.stream().filter(r -> r.getGuildId().compareTo(hallDTO.getGuildId()) == 0).findFirst();

            if (applyJoinRecordDTO.isPresent()) {
                throw new WebServiceException(WebServiceCode.GUILD_ALREADY_APPLY);
            }
        }

        // 插入申请加入记录
        GuildHallApplyRecordDO recordDO = new GuildHallApplyRecordDO();
        Date now = new Date();
        recordDO.setCreateTime(now);
        recordDO.setUpdateTime(now);
        recordDO.setHallId(paramBo.getHallId());
        recordDO.setReason(reason);
        recordDO.setType((int) GuildHallApplyType.JOIN.getValue());
        recordDO.setStatus((int) GuildHallApplyStatus.AUDITING.getValue());
        recordDO.setUid(paramBo.getUid());
        int rows = guildHallApplyRecordDao.insertSelective(recordDO);

        if (rows > 0) {
            // 发送小秘书消息 to 会长
            GuildDTO guildDO = guildManager.getGuild(hallDTO.getGuildId());
            String message = usersDTO.getNick() + "（ID：" + usersDTO.getErbanNo() + "）申请加入" + hallDTO.getRoomTitle() + "，请前往我的公会审核";
//            asyncNetEaseTrigger.sendMsg(String.valueOf(guildDO.getPresidentUid()), message);
            asyncNetEaseTrigger.sendPicAndWordMsg(String.valueOf(guildDO.getPresidentUid()), "公会审核通知", message, Payload.SkipType.H5, this.myGuildIndexPageUrl);
            // 发送小秘书消息 to 厅长 (厅长和会长不是同一个人才会发送)
            if (guildDO.getPresidentUid().compareTo(hallDTO.getHallUid()) != 0) {
//                asyncNetEaseTrigger.sendMsg(String.valueOf(hallDTO.getHallUid()), message);
                asyncNetEaseTrigger.sendPicAndWordMsg(String.valueOf(hallDTO.getHallUid()), "公会审核通知", message, Payload.SkipType.H5, this.myGuildIndexPageUrl);
            }

            // 清除相关缓存
            this.delRedisKeysByApply(guildDO.getId(), guildDO.getPresidentUid(), hallDTO.getHallId(), hallDTO.getHallUid());

            return true;
        }
        else {
            log.error("用户uid【{}】申请加入厅uid【{}】操作失败(insert)：>{}", paramBo.getUid(), hallDTO.getHallUid(), gson.toJson(recordDO));
            throw new WebServiceException(WebServiceCode.GUILD_APPLY_ERROR);
        }
    }

    @Override
    public boolean applyExitHall(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 检查用户是否存在
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        GuildHallDTO memberHallDTO = guildHallManager.getHallByMemberUid(uid);
        //厅主不能申请
        if (memberHallDTO == null) {
            throw new WebServiceException(WebServiceCode.GUILD_MEMBER_NOT_EXIST);
        }
        else if (memberHallDTO.getHallUid() != null && memberHallDTO.getHallUid().compareTo(uid) == 0) {
            throw new WebServiceException(WebServiceCode.GUILD_HALL_USER_CANNOT_EXIT);
        }

        //会长也能申请
//        GuildDTO guildDTO = guildManager.getGuild(memberHallDTO.getGuildId());
//        if (guildDTO == null) {
//            throw new WebServiceException(WebServiceCode.GUILD_MEMBER_NOT_EXIST);
//        }
//        else if (guildDTO.getPresidentUid().compareTo(uid) == 0) {
//            throw new WebServiceException(WebServiceCode.GUILD_PRESIDENT_CANNOT_EXIT);
//        }

        // 是否已提交过申请（在同一公会内）
        List<GuildHallApplyRecordDTO> applyJoinRecordDTOs = guildHallApplyRecordDao.selectByUidAndTypeAndStatus(uid, GuildHallApplyType.EXIT.getValue(), GuildHallApplyStatus.AUDITING.getValue());
        if (applyJoinRecordDTOs != null && applyJoinRecordDTOs.size() > 0) {
            Optional<GuildHallApplyRecordDTO> applyJoinRecordDTO = applyJoinRecordDTOs.stream().filter(r -> r.getGuildId().compareTo(memberHallDTO.getGuildId()) == 0).findFirst();

            if (applyJoinRecordDTO.isPresent()) {
                throw new WebServiceException(WebServiceCode.GUILD_ALREADY_APPLY);
            }
        }

        // 插入申请退出记录
        GuildHallApplyRecordDO recordDO = new GuildHallApplyRecordDO();
        Date now = new Date();
        recordDO.setCreateTime(now);
        recordDO.setUpdateTime(now);
        recordDO.setHallId(memberHallDTO.getHallId());
        recordDO.setType((int) GuildHallApplyType.EXIT.getValue());
        recordDO.setStatus((int) GuildHallApplyStatus.AUDITING.getValue());
        recordDO.setUid(uid);

        int rows = guildHallApplyRecordDao.insertSelective(recordDO);

        if (rows > 0) {
            // 发送小秘书消息 to 会长
            GuildDTO guildDO = guildManager.getGuild(memberHallDTO.getGuildId());
            String message = usersDTO.getNick() + "（ID：" + usersDTO.getErbanNo() + "）申请退出" + memberHallDTO.getRoomTitle() + "，请前往我的公会审核";
//            asyncNetEaseTrigger.sendMsg(String.valueOf(guildDO.getPresidentUid()), message);
            asyncNetEaseTrigger.sendPicAndWordMsg(String.valueOf(guildDO.getPresidentUid()), "公会审核通知", message, Payload.SkipType.H5, this.myGuildIndexPageUrl);
            // 发送小秘书消息 to 厅长(厅长和会长不是同一个人才会发送)
            if (guildDO.getPresidentUid().compareTo(memberHallDTO.getHallUid()) != 0) {
//                asyncNetEaseTrigger.sendMsg(String.valueOf(memberHallDTO.getHallUid()), message);
                asyncNetEaseTrigger.sendPicAndWordMsg(String.valueOf(memberHallDTO.getHallUid()), "公会审核通知", message, Payload.SkipType.H5, this.myGuildIndexPageUrl);
            }

            // 清除相关缓存
            this.delRedisKeysByApply(guildDO.getId(), guildDO.getPresidentUid(), memberHallDTO.getHallId(), memberHallDTO.getHallUid());
            return true;
        }
        else {
            log.error("用户uid【{}】申请退出厅uid【{}】操作失败(insert)：>{}", uid, memberHallDTO.getHallUid(), gson.toJson(recordDO));
            throw new WebServiceException(WebServiceCode.GUILD_APPLY_ERROR);
        }
    }

    @Override
    public int getApplyJoinCount(Long uid) throws WebServiceException {
        String jsdata = redisManager.get(RedisKey.guild_apply_record_auditing_count.getKey(String.valueOf(uid)));
        if (StringUtils.isNotBlank(jsdata)) {
            return Integer.valueOf(jsdata).intValue();
        }

        // 若是会长，获取所有此公会的所有待审核的
        GuildDO guildDO = this.getGuildByPresidentUid(uid);
        if (guildDO != null) {
            int count = guildHallApplyRecordDao.countAuditingByGuildIdAndStatus(guildDO.getId(), GuildHallApplyStatus.AUDITING.getValue());
            redisManager.set(RedisKey.guild_apply_record_auditing_count.getKey(String.valueOf(uid)), String.valueOf(count), 5, TimeUnit.MINUTES);
            return count;
        }

        // 若是厅主
        GuildHallDTO hallDTO = this.getHallByHallUid(uid);
        if (hallDTO != null) {
            int count = guildHallApplyRecordDao.countAuditingByHallIdAndStatus(hallDTO.getHallId(), GuildHallApplyStatus.AUDITING.getValue());
            redisManager.set(RedisKey.guild_apply_record_auditing_count.getKey(String.valueOf(uid)), String.valueOf(count), 5, TimeUnit.MINUTES);
            return count;
        }

        return 0;
    }

    @Override
    public List<GuildHallApplyRecordDTO> getApplyJoinRecords(IndexParam param, Long uid) throws WebServiceException {
        // 若是会长，获取所有此公会的所有
        GuildDO guildDO = this.getGuildByPresidentUid(uid);
        if (guildDO != null) {
            return this.getApplyRecordByGuildId(guildDO.getId(), param.getPageNum(), param.getPageSize());
        }

        // 检查此用户是否厅主
        GuildHallDTO hallDTO = this.getHallByHallUid(uid);
        if (hallDTO != null) {
            return this.getApplyRecordByHallId(hallDTO.getHallId(), param.getPageNum(), param.getPageSize());
        }

        return null;
    }

    @Override
    public boolean verify(VerifyParamBo paramBo) throws WebServiceException {
        if (paramBo.getIsApproved() == null || paramBo.getApplyId() == null || paramBo.getUid() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 加锁
        String lock = redisManager.lock(RedisKey.guild_apply_record_verify.getKey(paramBo.getApplyId().toString()), 5 * 1000);
        if (StringUtils.isBlank(lock)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        GuildHallApplyRecordDO recordDO = null;
        Long guildId = null;
        Long hallId = null;
        Long presidentUid = null;
        Long hallUid = null;

        try {
            // 检查申请记录状态，若已被审核，提示
            recordDO = guildHallApplyRecordDao.selectByPrimaryKey(paramBo.getApplyId());
            if (recordDO == null) {
                throw new WebServiceException(WebServiceCode.GUILD_APPLY_NOT_EXIST);
            }

            // 若不是待审核状态
            if (recordDO.getStatus().byteValue() != GuildHallApplyStatus.AUDITING.getValue()) {
                throw new WebServiceException(WebServiceCode.GUILD_APPLY_HANDLED);
            }

            // 判断审核用户是否申请成员的厅主或会长，否则没有权限进行申请
            boolean hasPermission = false;
            GuildHallDTO hallDTO = guildHallManager.getHall(recordDO.getHallId());
            if (hallDTO == null) {
                throw new WebServiceException(WebServiceCode.GUILD_HALL_NOT_EXIST);
            }
            else if (hallDTO.getHallUid().compareTo(paramBo.getUid()) == 0) {
                hasPermission = true;
            }

            guildId = hallDTO.getGuildId();
            hallId = hallDTO.getHallId();
            hallUid = hallDTO.getHallUid();

            GuildDTO guildDTO = guildManager.getGuild(hallDTO.getGuildId());
            if (guildDTO == null) {
                throw new WebServiceException(WebServiceCode.GUILD_NOT_EXIST);
            }
            else if (hasPermission == false && guildDTO.getPresidentUid().compareTo(paramBo.getUid()) == 0) {
                hasPermission = true;
            }

            presidentUid = guildDTO.getPresidentUid();

            if (hasPermission == false) {
                throw new WebServiceException(WebServiceCode.GUILD_APPLY_NO_PERMISSION);
            }

            // 审核处理，根据不同类型做不同的处理
            if (recordDO.getType().byteValue() == GuildHallApplyType.JOIN.getValue()) {
                this.handleVerifyJoinRecord(recordDO, paramBo, hallDTO);
            }
            else if (recordDO.getType().byteValue() == GuildHallApplyType.EXIT.getValue()) {
                this.handleVerifyExitRecord(recordDO, paramBo, hallDTO);
            }
        }
        finally {
            this.delRedisKeysByApply(guildId, presidentUid, hallId, hallUid);
            this.delRedisKeysByVerify(recordDO, guildId);
            redisManager.unlock(RedisKey.guild_apply_record_verify.getKey(paramBo.getApplyId().toString()), lock);
        }

        return true;
    }

    /**
     * 处理申请加入(会长未加入厅：更新记录；新成员：新增记录。)
     * @param recordDO
     * @param paramBo
     */
    private void handleVerifyJoinRecord(GuildHallApplyRecordDO recordDO, VerifyParamBo paramBo, GuildHallDTO targetHallDTO) throws WebServiceException {
        // 检查成员记录
        GuildHallMemberDO member = guildHallMemberDao.getByUid(recordDO.getUid());
        if (member != null) {
            if (member.getGuildId() != null && member.getGuildId().compareTo(targetHallDTO.getGuildId()) != 0)
                throw new WebServiceException(WebServiceCode.GUILD_ALREADY_JOIN_OTHER);

            if (member.getHallId() != null)
                if (member.getHallId().compareTo(targetHallDTO.getHallId()) == 0)
                    throw new WebServiceException(WebServiceCode.GUILD_HALL_ALREADY_JOIN);
                else
                    throw new WebServiceException(WebServiceCode.GUILD_HALL_ALREADY_JOIN_OTHER);
        }

        String message;
        Date now = new Date();
        recordDO.setApproverUid(paramBo.getUid());
        recordDO.setApproveTime(now);
        recordDO.setUpdateTime(now);

        if (paramBo.getIsApproved()) {
            // 若是通过审核，变更状态，更新记录
            recordDO.setStatus((int) GuildHallApplyStatus.VERIFIED.getValue());
            message = "恭喜你成功加入" + targetHallDTO.getRoomTitle();
        }
        else {
            // 若是不通过，变更状态，更新记录
            recordDO.setStatus((int) GuildHallApplyStatus.AUDIT_FAILED.getValue());
            message = "很抱歉，" + targetHallDTO.getRoomTitle() + "拒绝了你的加入申请";
        }
        int rows = guildHallApplyRecordDao.updateByPrimaryKey(recordDO);

        if (rows <= 0) {
            log.error("审核申请加入厅的操作失败，申请记录id【{}】，操作记录(update)：>{}", paramBo.getApplyId(), gson.toJson(recordDO));
            throw new WebServiceException(WebServiceCode.GUILD_APPLY_HANDLE_FAILD);
        }

        // 添加成员记录
        if (paramBo.getIsApproved()) {
            GuildHallMemberDO memberDO = new GuildHallMemberDO();
            if (member == null) {
                memberDO.setGuildId(targetHallDTO.getGuildId());
                memberDO.setHallId(targetHallDTO.getHallId());
                memberDO.setUid(recordDO.getUid());
                memberDO.setCreateTime(now);
                memberDO.setUpdateTime(now);
                memberDO.setIsDel(false);
                memberDO.setMemberType((int)GuildHallMemberType.MEMBER.getValue());  // 新增记录都是成员

                guildHallMemberDao.insertSelective(memberDO);
            }
            else if (member.getHallId() == null) {
                memberDO.setId(member.getId());
                memberDO.setHallId(targetHallDTO.getHallId());
                memberDO.setUpdateTime(now);
                guildHallMemberDao.updateByPrimaryKeySelective(memberDO);
            }

            // 查找其他相同type和status为待审核的记录，标记为已失效
            List<GuildHallApplyRecordDO> otherRecords = guildHallApplyRecordDao.findByUidAndTypeAndStatus(recordDO.getUid()
                    , GuildHallApplyType.JOIN.getValue(), GuildHallApplyStatus.AUDITING.getValue());

            log.info("审核通过加入公会，此次处理记录：{}，其他待处理记录：> {}", gson.toJson(recordDO), gson.toJson(otherRecords));
            Set<String> updateKeys = new HashSet<>();
            for (GuildHallApplyRecordDO record : otherRecords) {
                updateKeys.add(RedisKey.guild_apply_record_list.getKey(String.valueOf(record.getHallId()) + "_1_10"));
                GuildHallDTO tempHall = guildHallManager.getHall(record.getHallId());
                if (tempHall != null) {
                    updateKeys.add(RedisKey.guild_apply_record_list.getKey(String.valueOf(tempHall.getGuildId()) + "_1_10"));
                }

                record.setStatus((int)GuildHallApplyStatus.INVALID.getValue());
                guildHallApplyRecordDao.updateByPrimaryKey(record);
            }

            redisManager.delete(updateKeys);
        }

        // 小秘书提示
        asyncNetEaseTrigger.sendMsg(String.valueOf(recordDO.getUid()), message);
    }

    /**
     * 处理申请退出(会长：更新记录；普通成员：更新逻辑删除)
     * @param recordDO
     */
    private void handleVerifyExitRecord(GuildHallApplyRecordDO recordDO, VerifyParamBo paramBo, GuildHallDTO targetHallDTO) throws WebServiceException {
        GuildHallMemberDO member = guildHallMemberDao.getByUid(recordDO.getUid());
        if (member == null) {
            throw new WebServiceException(WebServiceCode.GUILD_MEMBER_NOT_EXIST);
        }
        else {
            if (GuildHallMemberType.PRESIDENT.getValue() == member.getMemberType().byteValue() && member.getHallId() == null) {
                throw new WebServiceException(WebServiceCode.GUILD_MEMBER_NOT_JOIN_HALL);
            }
        }

        String message;
        Date now = new Date();
        recordDO.setApproverUid(paramBo.getUid());
        recordDO.setApproveTime(now);
        recordDO.setUpdateTime(now);

        if (paramBo.getIsApproved()) {
            // 若是通过审核，变更状态，更新记录
            recordDO.setStatus((int) GuildHallApplyStatus.VERIFIED.getValue());
            message = "你已退出" + targetHallDTO.getRoomTitle();
        }
        else {
            // 若是不通过，变更状态，更新记录
            recordDO.setStatus((int) GuildHallApplyStatus.AUDIT_FAILED.getValue());
            message = "很抱歉，" + targetHallDTO.getRoomTitle() + "拒绝了你的退出申请";
        }
        int rows = guildHallApplyRecordDao.updateByPrimaryKey(recordDO);

        if (rows <= 0) {
            log.error("审核申请退出厅的操作失败，申请记录id【{}】，操作记录(update)：>{}", paramBo.getApplyId(), gson.toJson(recordDO));
            throw new WebServiceException(WebServiceCode.GUILD_APPLY_HANDLE_FAILD);
        }

        // 更新成员记录
        if (paramBo.getIsApproved()) {
            GuildHallMemberDO updateDO = new GuildHallMemberDO();
            updateDO.setId(member.getId());
            // 会长处理
            if (GuildHallMemberType.PRESIDENT.getValue() == member.getMemberType().byteValue()) {
                updateDO.setHallId(null);
            }
            else {
                // 普通成员处理
                updateDO.setIsDel(true);
            }
            rows = guildHallMemberDao.updateByPrimaryKeySelective(updateDO);

            if (rows <= 0) {
                log.error("删除厅成员的操作失败，uid【{}】，hallID【{}】，触发记录(delete)：>{}", recordDO.getUid(), recordDO.getHallId(), gson.toJson(recordDO));
            }
        }

        // 小秘书提示
        asyncNetEaseTrigger.sendMsg(String.valueOf(recordDO.getUid()), message);
    }

    private GuildDO getGuildByPresidentUid(Long uid) {
        List<GuildDO> guilds = guildManager.getGuildValidList();
        if (guilds == null || guilds.isEmpty())
            return null;

        Optional<GuildDO> guildDO = guilds.stream().filter(g -> g.getPresidentUid().compareTo(uid) == 0).findFirst();

        if (guildDO.isPresent())
            return guildDO.get();

        return null;
    }

    private GuildHallDTO getHallByHallUid(Long uid) throws WebServiceException {
        GuildHallDTO hallDTO = guildHallManager.getHallByMemberUid(uid);
        if (hallDTO != null && hallDTO.getHallUid().compareTo(uid) == 0)
            return hallDTO;

        return null;
    }

    private List<GuildHallApplyRecordDTO> getApplyRecordByGuildId(Long guildId, int pageNum, int pageSize) {
        String jsdata = redisManager.get(RedisKey.guild_apply_record_list.getKey(guildId + "_" + pageNum + "_" + pageSize));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, new TypeToken<List<GuildHallApplyRecordDTO>>() {}.getType());
        }

        int startIndex = pageNum > 0 ? (pageNum - 1) * pageSize : 0;

        List<GuildHallApplyRecordDTO> recordDTOS = guildHallApplyRecordDao.getPageByGuildId(startIndex, pageSize, guildId);

        if (recordDTOS != null && recordDTOS.size() > 0) {
            redisManager.set(RedisKey.guild_apply_record_list.getKey(guildId + "_" + pageNum + "_" + pageSize), gson.toJson(recordDTOS), 5, TimeUnit.MINUTES);
        }
        return recordDTOS;
    }

    private List<GuildHallApplyRecordDTO> getApplyRecordByHallId(Long hallId, int pageNum, int pageSize) {
        String jsdata = redisManager.get(RedisKey.guild_hall_apply_record_list.getKey(hallId + "_" + pageNum + "_" + pageSize));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, new TypeToken<List<GuildHallApplyRecordDTO>>() {}.getType());
        }

        int startIndex = pageNum > 0 ? (pageNum - 1) * pageSize : 0;

        List<GuildHallApplyRecordDTO> recordDTOS = guildHallApplyRecordDao.getPageByHallId(startIndex, pageSize, hallId);

        if (recordDTOS != null && recordDTOS.size() > 0) {
            redisManager.set(RedisKey.guild_hall_apply_record_list.getKey(hallId + "_" + pageNum + "_" + pageSize), gson.toJson(recordDTOS), 5, TimeUnit.MINUTES);
        }
        return recordDTOS;
    }

    /**
     * 当【产生】申请记录时，删除相关缓存
     */
    private void delRedisKeysByApply(Long guildId, Long presidentUid, Long hallId, Long hallUid) {
        int pageNum = 1, pageSize = 10;   //目前默认分页规则

        Set<String> keys = new HashSet<>();

        if (guildId != null)
            keys.add(RedisKey.guild_apply_record_list.getKey(guildId + "_" + pageNum + "_" + pageSize));
        if (hallId != null)
            keys.add(RedisKey.guild_hall_apply_record_list.getKey(hallId + "_" + pageNum + "_" + pageSize));

        if (presidentUid != null)
            keys.add(RedisKey.guild_apply_record_auditing_count.getKey(String.valueOf(presidentUid)));
        if (hallUid != null)
            keys.add(RedisKey.guild_apply_record_auditing_count.getKey(String.valueOf(hallUid)));

        if (keys.size() > 0)
            redisManager.delete(keys);
    }

    /**
     * 当【审核】申请记录时，处理相关缓存
     */
    private void delRedisKeysByVerify(GuildHallApplyRecordDO recordDO, Long guildId) {
        Set<String> keys = new HashSet<>();

        if (recordDO != null) {
            keys.add(RedisKey.guild_member.getKey(String.valueOf(recordDO.getUid())));
            keys.add(RedisKey.guild_member_turnover.getKey(String.valueOf(recordDO.getUid())));
            keys.add(RedisKey.guild_hall_member_list.getKey(String.valueOf(recordDO.getHallId())));

            redisManager.hdel(RedisKey.guild_hall.getKey(), String.valueOf(recordDO.getHallId()));
            redisManager.hdel(RedisKey.guild_member_exist.getKey(), String.valueOf(recordDO.getUid()));
            redisManager.hdel(RedisKey.guild_member_in_hall.getKey(), String.valueOf(recordDO.getUid()));
        }

        if (guildId != null) {
            keys.add(RedisKey.guild_member_list.getKey(String.valueOf(guildId)));
            redisManager.hdel(RedisKey.guild.getKey(), String.valueOf(guildId));
        }


        if (keys.size() > 0)
            redisManager.delete(keys);
    }
}