package com.vslk.lbgx.ui.me.shopping.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.base.fragment.BaseListFragment;
import com.vslk.lbgx.presenter.shopping.DressUpFragmentPresenter;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.me.shopping.adapter.GiveGoodsAdapter;
import com.vslk.lbgx.ui.me.shopping.fragment.FriendListGiftFragment;
import com.vslk.lbgx.ui.me.shopping.view.IDressUpFragmentView;
import com.vslk.lbgx.ui.widget.magicindicator.MagicIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.ViewPagerHelper;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dell
 */
@CreatePresenter(DressUpFragmentPresenter.class)
public class GiveGoodsActivity extends BaseMvpActivity<IDressUpFragmentView, DressUpFragmentPresenter> implements IDressUpFragmentView {

    @BindView(R.id.toolbar)
    AppToolBar mToolBar;
    @BindView(R.id.give_goods_indicator)
    MagicIndicator giveGoodsIndicator;
    @BindView(R.id.vp_give_goods)
    ViewPager vpGiveGoods;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private String carName;
    private String goodsId;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_goods);
        ButterKnife.bind(this);
        carName = getIntent().getStringExtra("carName");
        goodsId = getIntent().getStringExtra("goodsId");
        type = getIntent().getIntExtra("type", 0);

        BaseListFragment baseListFragment = new BaseListFragment();
        GiveGoodsAdapter shareFansAdapter = new GiveGoodsAdapter(new ArrayList<>());
        shareFansAdapter.itemAction = this::showEnsureDialog;
        baseListFragment.setEmptyStr("没有关注的用户");
        baseListFragment.pageNoParmasName = "pageNo";
        baseListFragment.setShortUrl(UriProvider.getAllFans());
        baseListFragment.setOtherParams(Json.parse("uid:" + CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        baseListFragment.setAdapter(shareFansAdapter);
        baseListFragment.setDataFilter(json -> json.jlist("data"));


        FriendListGiftFragment friendListGiftFragment = new FriendListGiftFragment();
        friendListGiftFragment.iGiveAction = this::showEnsureDialog;
        fragments.add(friendListGiftFragment);
        fragments.add(baseListFragment);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };

        List<TabInfo> mTabInfoList = new ArrayList<>();
        mTabInfoList.add(new TabInfo(1, "好友"));
        mTabInfoList.add(new TabInfo(2, "关注"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        CommonMagicIndicatorAdapter magicIndicatorAdapter = new CommonMagicIndicatorAdapter(this,
                mTabInfoList, UIUtil.dip2px(this, 4));
        magicIndicatorAdapter.setOnItemSelectListener(position -> vpGiveGoods.setCurrentItem(position));
        commonNavigator.setAdapter(magicIndicatorAdapter);
        commonNavigator.setAdjustMode(true);

        giveGoodsIndicator.setNavigator(commonNavigator);
        vpGiveGoods.setAdapter(fragmentPagerAdapter);
        ViewPagerHelper.bind(giveGoodsIndicator, vpGiveGoods);

        mToolBar.setOnBackBtnListener(view -> finish());
    }

    private void showEnsureDialog(String uid, String userName) {
        getDialogManager().showOkCancelDialog("确认购买“" + carName + "”并赠送给" + userName + "？", true, new DialogManager.AbsOkDialogListener() {

            @Override
            public void onOk() {
                requestGift(uid);
            }
        });
    }

    private void requestGift(String uid) {

        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(goodsId)) {
            toast("参数异常");
            return;
        }
        getDialogManager().showProgressDialog(this);
        getMvpPresenter().giveGift(type, uid, goodsId);
    }

    @Override
    public void giftGiveSuccess() {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast("赠送礼物成功");
    }

    @Override
    public void giftGiveFail(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(msg);
    }
}
