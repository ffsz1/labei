package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillTransferDO;
import com.juxiao.xchat.dao.bill.dto.BillUserTransferDTO;
import com.juxiao.xchat.dao.bill.dto.TransFerUsersDTO;
import com.juxiao.xchat.dao.bill.query.BillUserQuery;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: BillTransferDao.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public interface BillTransferDao {

    /**
     * 保存提现账单
     * @param transferDo transferDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_transfer` (`uid`, `tran_type`, `real_tran_type`, `cost`, `money`, `bill_status`, `create_time`, `update_time`, `bill_id`, `bank_card`, `bank_card_name`) VALUES (#{uid}, #{tranType}, #{realTranType}, #{cost}, #{money}, #{billStatus}, #{createTime}, #{updateTime}, #{billId}, #{bankCard}, #{bankCardName})")
    void save(BillTransferDO transferDo);

    /**
     * 查询用户提现账单记录
     *
     * @param query
     * @return
     * @author: chenjunsheng
     * @date 2018/5/16
     */
    @TargetDataSource(name = "ds2")
    List<BillUserTransferDTO> listUserTransfer(BillUserQuery query);

    @TargetDataSource(name = "ds2")
    @Select("select uid,create_time as createTime from bill_transfer where uid = #{uid} order by create_time desc")
    TransFerUsersDTO selectBillTransfer(@Param("uid")Long uid);

    /**
     * 保存账单
     * @param transferDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `bill_transfer` (`uid`,`tran_type`,`cost`,`money`,`bill_status`,`create_time`,`update_time`,`bill_id`,`apply_withdrawal_account`,`apply_withdrawal_name`)VALUES(#{uid},#{tranType},#{cost},#{money},#{billStatus},#{createTime},#{updateTime},#{billId},#{applyWithdrawalAccount},#{applyWithdrawalName})")
    void insert(BillTransferDO transferDo);
}
