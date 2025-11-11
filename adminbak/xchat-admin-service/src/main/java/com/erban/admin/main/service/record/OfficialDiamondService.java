package com.erban.admin.main.service.record;

import com.erban.admin.main.mapper.OfficialDiamondRecordMapper;
import com.erban.admin.main.model.AdminDict;
import com.erban.admin.main.model.OfficialDiamondRecord;
import com.erban.admin.main.model.OfficialGoldRecord;
import com.erban.admin.main.model.OfficialGoldRecordExample;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.system.AdminDictService;
import com.erban.admin.main.vo.GiveDiamondRecordDTO;
import com.erban.admin.main.vo.GiveDiamondRecordParam;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.BillRecord;
import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
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
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class OfficialDiamondService extends BaseService {
    @Autowired
    private OfficialDiamondRecordMapper officialDiamondRecordMapper;

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
     * 查询赠送钻石记录
     *
     * @param param 查询条件
     * @return
     */
    public Page<GiveDiamondRecordDTO> giveRecord(GiveDiamondRecordParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return officialDiamondRecordMapper.selectByParam(param);
    }

    /**
     * 统计赠送金币总数
     *
     * @param param 查询参数
     * @return
     */
    public long sumDiamond(GiveDiamondRecordParam param) {
        Long sum = officialDiamondRecordMapper.sumByParam(param);
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
    public BusiResult giveDiamond(String ernos, byte type, double num, Integer adminId, String remark) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);

        AdminDict adminDict = adminDictService.getOneAdminDict("official_diamond_limit_" + type, "limit_time");
        AdminDict adminDict2 = adminDictService.getOneAdminDict("official_diamond_limit_" + type, "limit_count");
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
            if (!isCanGiveDiamond(users, type, num, limitTime, limitCount)) {
                busiResult.setCode(-1);
                busiResult.setMessage(GlobalConfig.appName + "号为" + erNo + "的用户被赠送的次数大于" + limitTime + "或者钻石余额大于" + limitCount);
                return busiResult;
            }
            // 增加赠送记录
            int recordId = addDiamondRecord(users, type, num, adminId, remark);

            // 增加账单记录
            addBillRecord(users, (long) recordId, type, num);
            // 更新账户钻石数量
            int rechargeStatus = userPurseUpdateService.addDiamondDbAndCache(users.getUid(), num);
            if (rechargeStatus != 200) {
                busiResult.setCode(rechargeStatus);
                busiResult.setMessage("赠送钻石失败");
                return busiResult;
            }

            logger.info("giveDiamond success, erpan_no: {}, type: {}, num: {}, recordId: {}", erNo, type, num, recordId);
            // 发送消息给用户
//            NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
//            neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
//            neteaseSendMsgParam.setOpe(0);
//            neteaseSendMsgParam.setType(0);
//            neteaseSendMsgParam.setTo(users.getUid().toString());
//            neteaseSendMsgParam.setBody("恭喜您，获得官方赠送的" + num + "金币，请前往我的钱包查看！");
//            sendSysMsgService.sendMsg(neteaseSendMsgParam);
        }
        return busiResult;
    }

    /**
     * @param users
     * @param objId 对象ID，官方赠送记录表的key
     * @param type
     * @param num   钻石数量
     * @return
     */
    public int addBillRecord(Users users, Long objId, byte type, Double num) {
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setDiamondNum(num);
        billRecord.setObjId(objId.toString());
        billRecord.setObjType(type);
        billRecord.setTargetUid(users.getUid());
        billRecord.setUid(users.getUid());
        billRecord.setCreateTime(new Date());
        billRecord.setUpdateTime(new Date());
        return billRecordMapper.insert(billRecord);
    }

    public int addDiamondRecord(Users users, byte type, double num, Integer adminId, String remark) {
        OfficialDiamondRecord record = new OfficialDiamondRecord();
        record.setDiamondNum(num);
        record.setType(type);
        record.setUid(users.getUid());
        record.setCreateTime(new Date());
        record.setAdminId(adminId);
        record.setRemark(remark);
        officialDiamondRecordMapper.insert(record);
        return record.getRecordId();
    }

    /**
     *
     *
     * @param users
     * @return
     */
    private boolean isCanGiveDiamond(Users users, int type, double num, int limitTime, int limtCount) {
        Date date = new Date();
        Date startTime = DateTimeUtils.setTime(date, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(date, 24, 0, 0);
        GiveDiamondRecordParam example = new GiveDiamondRecordParam();
        example.setErbanNo(users.getErbanNo());
        example.setType(type);
        example.setBegin(startTime);
        example.setEnd(endTime);
        List<GiveDiamondRecordDTO> list = officialDiamondRecordMapper.selectByParam(example);
        if (CollectionUtils.isEmpty(list)){
            return true;
        }

        if (list.size() >= limitTime && limitTime > 0) {
            return false;
        }

        double totalNum = 0.0;
        for (GiveDiamondRecordDTO recordDTO : list){
            totalNum += recordDTO.getDiamondNum();
            if (totalNum > limtCount){
                return false;
            }
        }

        return true;
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
}
