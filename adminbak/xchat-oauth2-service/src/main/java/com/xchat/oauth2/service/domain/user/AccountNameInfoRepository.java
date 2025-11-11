package com.xchat.oauth2.service.domain.user;

import com.xchat.oauth2.service.domain.shared.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuguofu
 *         on 8/2/15.
 */
public interface AccountNameInfoRepository extends Repository {

    void  saveAccountNameInfo(AccountNameInfo accountNameInfo);

    void removeAccountNameInfo(String accountName);

    AccountNameInfo loadByAccountName(String accountName);

    void updateAccountNameInfo(@Param("oldAccountName")String oldAccountName,@Param("newAccountName")String newAccountName);
}
