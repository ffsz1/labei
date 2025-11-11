package com.erban.admin.main.vo;


import com.erban.main.model.Users;
import com.erban.main.vo.UserPurseVo;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.vo.AccountVo;


public class UsersAdminVo {
    private Users users;
    private UserPurseVo userPurseVo;
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }



    public UserPurseVo getUserPurseVo() {
        return userPurseVo;
    }

    public void setUserPurseVo(UserPurseVo userPurseVo) {
        this.userPurseVo = userPurseVo;
    }
}
