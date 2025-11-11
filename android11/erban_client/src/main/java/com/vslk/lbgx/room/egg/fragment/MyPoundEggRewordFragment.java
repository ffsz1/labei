package com.vslk.lbgx.room.egg.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vslk.lbgx.base.fragment.BaseNewListFragment;
import com.vslk.lbgx.room.egg.adapter.PoundEggRewordListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>  我的中奖记录 </p>
 *
 * @author zwk
 * @date 2017/12/4
 */
public class MyPoundEggRewordFragment extends BaseNewListFragment<PoundEggRewordListAdapter> {

    @Override
    protected RecyclerView.LayoutManager initManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    protected PoundEggRewordListAdapter initAdpater() {
        return new PoundEggRewordListAdapter();
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_my_pound_egg_reword;
    }

    @Override
    public void initData() {
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid()+"");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket()+"");
        params.put("pageNum",mPage + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getPoundEggRewordRecord(), params, new OkHttpManager.MyCallBack<ServiceResult<List<EggGiftInfo>>>() {
            @Override
            public void onError(Exception e) {
                dealFail(e);
            }

            @Override
            public void onResponse(ServiceResult<List<EggGiftInfo>> response) {
                dealSuccess(response,"暂时没有你的开盒子记录,快去开盒子看看今日运气吧");
            }
        });
    }
}
