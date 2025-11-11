package com.tongdaxing.xchat_core.count;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by Administrator on 2018/3/24.
 */

public interface IChargeClient extends ICoreClient {
    String chargeAction = "chargeAction";

    public String chargeAction(String type);

}
