package com.erban.admin.main.service.record;

import com.erban.admin.main.mapper.OfficialGoldRecordMapper;
import com.erban.admin.main.model.AdminDict;
import com.erban.admin.main.model.OfficialGoldRecord;
import com.erban.admin.main.model.OfficialGoldRecordExample;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.system.AdminDictService;
import com.erban.admin.main.vo.GiveGoldRecordDTO;
import com.erban.admin.main.vo.GiveGoldRecordParam;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.BillRecordMapper;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xchat.common.UUIDUitl;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OfficialGoldRecordService extends BaseService {
    @Autowired
    private OfficialGoldRecordMapper officialGoldRecordMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private BillRecordMapper billRecordMapper;

    @Autowired
    private UserPurseService userPurseService;

    @Autowired
    private AdminDictService adminDictService;

    @Autowired
    private ChargeRecordMapper chargeRecordMapper;

    @Autowired
    private UserPurseUpdateService userPurseUpdateService;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    /**
     * 查询赠送金币记录
     *
     * @param param 查询条件
     * @return
     */
    public Page<GiveGoldRecordDTO> giveRecord(GiveGoldRecordParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return officialGoldRecordMapper.selectByParam(param);
    }

    /**
     * 更新赠送金币记录
     *
     * @param recordId 记录ID
     * @param remark   备注
     * @return
     */
    public int updateGiveGoldRecord(Integer recordId, String remark) {
        OfficialGoldRecord officialGoldRecord = officialGoldRecordMapper.selectByPrimaryKey(recordId);
        officialGoldRecord.setRemark(remark);
        return officialGoldRecordMapper.updateByPrimaryKey(officialGoldRecord);
    }

    /**
     * 统计赠送金币总数
     *
     * @param param 查询参数
     * @return
     */
    public long sumGold(GiveGoldRecordParam param) {
        Long sum = officialGoldRecordMapper.sumByParam(param);
        return sum == null ? 0L : sum;
    }

    /**
     * 赠送金币
     *
     * @param ernos 拉贝号，多个拉贝号用换行符分隔
     * @param type  赠送类型
     * @param num   赠送数量
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult giveGold(String ernos, byte type, long num, Integer adminId, String remark) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);

        AdminDict adminDict = adminDictService.getOneAdminDict("official_gold_limit_" + type, "limit_time");
        AdminDict adminDict2 = adminDictService.getOneAdminDict("official_gold_limit_" + type, "limit_count");
        int limitTime = Integer.valueOf(adminDict.getDictval());
        int limitCount = Integer.valueOf(adminDict2.getDictval());

        // 分隔拉贝号，多个拉贝号用换行分隔
        String[] arr = ernos.split("\n");
        for (String erNo : arr) {
            Users users = getUserByErNo(Long.valueOf(erNo.trim()));
            if (users == null) {
                busiResult.setCode(-1);
                busiResult.setMessage(GlobalConfig.appName + "号为[" + erNo + "]的用户不存在");
                return busiResult;
            }
            if (!isCanGiveGold(users, type, num, limitTime, limitCount)) {
                busiResult.setCode(-1);
                busiResult.setMessage(GlobalConfig.appName + "号为" + erNo + "的用户被赠送的次数大于" + limitTime + "或者金币余额大于" + limitCount);
                return busiResult;
            }
            // 增加官方赠送活动记录
            int recordId = addGoldRecord(users, type, num, adminId, remark);
            if (Constant.BillType.chargeByCompanyAccount.equals(type)) {
                // 增加充值记录
                addChargeRecord(users.getUid(), num);
            }
            // 增加账单记录
            addBillRecord(users, (long) recordId, type, num);
            // 更新账户金币数量
            updateUserPurseGoldNum(users, num);
            logger.info("giveGold success, erpan_no: {}, type: {}, num: {}, recordId: {}", erNo, type, num, recordId);
            // 发送消息给用户
            NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
            neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
            neteaseSendMsgParam.setOpe(0);
            neteaseSendMsgParam.setType(0);
            neteaseSendMsgParam.setTo(users.getUid().toString());
            neteaseSendMsgParam.setBody("恭喜您，获得官方赠送的" + num + "金币，请前往我的钱包查看！");
            sendSysMsgService.sendMsg(neteaseSendMsgParam);
        }
        return busiResult;
    }

    public Users getUserByErNo(long erno) {
        UsersExample example = new UsersExample();
        example.createCriteria().andErbanNoEqualTo(erno);
        List<Users> list = usersMapper.selectByExample(example);
        if (!BlankUtil.isBlank(list)) {
            return list.get(0);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public BusiResult giveGold2(String ernos, byte type, long num, Integer adminId, String remark) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);

        // 分隔拉贝号, 多个拉贝号用换行分隔
        String[] arr = ernos.split("\n");
        for (String erNo : arr) {
            Users users = getUserByErNo(Long.valueOf(erNo.trim()));
            if (users == null) {
                busiResult.setCode(-1);
                busiResult.setMessage(GlobalConfig.appName + "号为[" + erNo + "]的用户不存在");
                return busiResult;
            }

            // 更新账户金币数量
            updateUserPurseGoldNum(users, num);
            // 发送消息给用户
            NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
            neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
            neteaseSendMsgParam.setOpe(0);
            neteaseSendMsgParam.setType(0);
            neteaseSendMsgParam.setTo(users.getUid().toString());
            neteaseSendMsgParam.setBody("恭喜您，获得官方赠送的" + num + "金币，请前往我的钱包查看！");
            sendSysMsgService.sendMsg(neteaseSendMsgParam);
        }
        return busiResult;
    }

    /**
     * @param users
     * @param objId 对象ID，官方金币赠送记录表的key
     * @param type
     * @param num
     * @return
     */
    public int addBillRecord(Users users, Long objId, byte type, long num) {
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setGoldNum(num);
        billRecord.setObjId(objId.toString());
        billRecord.setObjType(type);
        billRecord.setTargetUid(users.getUid());
        billRecord.setUid(users.getUid());
        billRecord.setCreateTime(new Date());
        billRecord.setUpdateTime(new Date());
        return billRecordMapper.insert(billRecord);
    }

    public int addGoldRecord(Users users, byte type, long num, Integer adminId, String remark) {
        OfficialGoldRecord record = new OfficialGoldRecord();
        record.setGoldNum(num);
        record.setType(type);
        record.setUid(users.getUid());
        record.setCreateTime(new Date());
        record.setAdminId(adminId);
        record.setRemark(remark);
        officialGoldRecordMapper.insert(record);
        return record.getRecordId();
    }

    public void updateUserPurseGoldNum(Users users, long num) {
        userPurseUpdateService.addGoldDbAndCache(users.getUid(), num);
    }

    /**
     * 满足赠送的条件：赠送次数少于4次，余额小于300
     *
     * @param users
     * @return
     */
    private boolean isCanGiveGold(Users users, byte type, long num, int limitTime, int limtCount) {
        OfficialGoldRecordExample example = new OfficialGoldRecordExample();
        example.createCriteria().andUidEqualTo(users.getUid()).andTypeEqualTo(type);
//        example.createCriteria().andTypeEqualTo(type);
        List<OfficialGoldRecord> list = officialGoldRecordMapper.selectByExample(example);
        if (list != null && list.size() >= limitTime && limitTime > 0) {
            return false;
        }
        UserPurse userPurse = userPurseService.getPurseByUid(users.getUid());
        if (userPurse.getGoldNum() + num > limtCount && limtCount > 0) {
            return false;
        }
        return true;
    }

    private int addChargeRecord(Long uid, Long totalGold) {
        String chargeRecordId = UUIDUitl.get();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId("company");
        chargeRecord.setChannel("company");
        chargeRecord.setBussType(Constant.PayBussType.charge);
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        chargeRecord.setChargeDesc("打款至公账充值");
        chargeRecord.setUid(uid);
        chargeRecord.setTotalGold(totalGold);
        chargeRecord.setAmount(totalGold * 10);
        chargeRecord.setCreateTime(new Date());
        return chargeRecordMapper.insertSelective(chargeRecord);
    }
}
