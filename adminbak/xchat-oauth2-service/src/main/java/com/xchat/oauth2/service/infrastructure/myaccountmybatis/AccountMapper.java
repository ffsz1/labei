package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountExample;
import com.xchat.oauth2.service.model.vo.UserAdminListVo;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.oauth2.service.param.AcountListAminParam;
import com.xchat.oauth2.service.vo.admin.AccountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
    int deleteByExample(AccountExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(Account record);

    //limself 2020 1010
    int insert2(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    //limself 2020 1010
    int updateByPrimaryKey2(Account record);

    List<AccountVo> selectByQuery(AccountParam accountParam);

    List<AccountVo> seleNumByQuery(AccountParam accountParam);

    List<UserAdminListVo> listAccountInAdmin(AcountListAminParam param);

    /**
     * 根据手机号算出账户数量
     * @param phone 手机号
     * @return
     */
    int countAccount(@Param("phone") String phone);

    List<Account> listByParam(@Param("openId") String openId, @Param("unionId") String unionId, @Param("type") Integer type);

    List<Account> selectByAppleUser(@Param("appleUser")String appleUser);
}
