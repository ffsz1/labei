package com.erban.main.wechat.message.resp;


/**
 * Created by 北岭山下 on 2017/7/31.
 */
/*
        模板消息对象




您好，您已成功进行话费充值。

手机号：13912345678
充值金额：50元
充值状态：充值成功
备注：如有疑问，请致电13912345678联系我们。

 */
public class ModelMsg {

        private String touser;
        private String template_id;
        private String url;
        private String appid;
        private String pagepath;
        private ModelData data;
        //以下非必须
        private String miniprogram;
        private String color;

}
