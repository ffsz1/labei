package com.vslk.lbgx.ui.me.withdraw;

public interface IDiamondsCore {
    void getFinancialAccount(ISuccessListener<BindingBean> listener);

    void isAuthentication(ISuccessListener<Boolean> listener);

    void bindWithdrawAccount(ISuccessListener<Boolean> listener, String... args);
}
