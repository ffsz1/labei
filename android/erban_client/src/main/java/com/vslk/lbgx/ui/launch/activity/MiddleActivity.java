package com.vslk.lbgx.ui.launch.activity;

import android.os.Bundle;
import android.util.Log;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.microquation.linkedme.android.LinkedME;
import com.microquation.linkedme.android.util.LinkProperties;
import com.tongdaxing.xchat_core.linked.ILinkedCore;
import com.tongdaxing.xchat_core.linked.LinkedInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.SpUtils;

import java.util.HashMap;

/**
 * Created by chenran on 2017/8/5.
 */

public class MiddleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            //获取与深度链接相关的值
            LinkProperties linkProperties = getIntent().getParcelableExtra(LinkedME.LM_LINKPROPERTIES);
            if (linkProperties != null) {
                String linkerMeChannel = linkProperties.getChannel();
                Log.i("LinkedME-Demo", "Channel " + linkerMeChannel);
                Log.i("LinkedME-Demo", "control params " + linkProperties.getControlParams());
                Log.i("LinkedME-Demo", "link(深度链接) " + linkProperties.getLMLink());
                Log.i("LinkedME-Demo", "是否为新安装 " + linkProperties.isLMNewUser());
                //获取自定义参数封装成的hashmap对象,参数键值对由集成方定义
                HashMap<String, String> hashMap = linkProperties.getControlParams();
                //根据key获取传入的参数的值,该key关键字View可为任意值,由集成方规定,请与web端商议,一致即可


                LinkedInfo linkedInfo = new LinkedInfo();
                linkedInfo.setNewUser(linkProperties.isLMNewUser());
                String roomuid = hashMap.get("roomuid");
                String uid = hashMap.get("uid");


                String channel = hashMap.get("linkedmeChannel");
                String type = hashMap.get("type");
                Log.i("LinkedME-Demo", "roomuid:" + roomuid + " uid:" + uid + " type：" + type);
                if (roomuid != null) {
                    linkedInfo.setRoomUid(roomuid);
                }
                if (uid != null) {
                    SpUtils.put(MiddleActivity.this, SpEvent.linkedMeShareUid, uid);
                    linkedInfo.setUid(uid);
                }
                if (type != null) {
                    linkedInfo.setType(type);
                }
                if (channel != null) {
                    SpUtils.put(MiddleActivity.this, SpEvent.linkedMeChannel, channel);
                }
//                toast("linkedMe数据返回--->" + uid);
                CoreManager.getCore(ILinkedCore.class).setLinkedInfo(linkedInfo);

            }
        }
        finish();
    }
}
