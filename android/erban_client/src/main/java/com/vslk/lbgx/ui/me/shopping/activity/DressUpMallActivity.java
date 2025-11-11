package com.vslk.lbgx.ui.me.shopping.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.vslk.lbgx.presenter.shopping.DressUpPresenter;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.vslk.lbgx.ui.me.shopping.fragment.DressUpFragment;
import com.vslk.lbgx.ui.me.shopping.listener.OnHeadWearCallback;
import com.vslk.lbgx.ui.widget.magicindicator.MagicIndicator;
import com.vslk.lbgx.ui.widget.magicindicator.ViewPagerHelper;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.DESUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.tongdaxing.xchat_framework.util.util.DESUtils.giftCarSecret;

/**
 * 新改版装扮商城页面
 *
 * @author zwk 2018/10/16
 */
@CreatePresenter(DressUpPresenter.class)
public class DressUpMallActivity extends BaseMvpActivity<IMvpBaseView, DressUpPresenter> implements OnHeadWearCallback, IMvpBaseView, CommonMagicIndicatorAdapter.OnItemSelectListener {
    private ImageView ivHeadPic, ivHeadWear;
    private SVGAImageView svgaCarTry;
    private MagicIndicator mIndicator;
    private ViewPager vpDressMall;
    private boolean isMySelf = false;
    private boolean showVgg = false;//是否正在播放特效
    public static final int DRESS_HEADWEAR = 0;
    public static final int DRESS_CAR = 1;
    private AppToolBar mToolBar;

    private long targetUid;

    public static void start(Context context, boolean isMySelf, long targetUid) {
        Intent intent = new Intent(context, DressUpMallActivity.class);
        intent.putExtra("isMySelf", isMySelf);
        intent.putExtra("targetUid", targetUid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dress_up_mall);
        targetUid = getIntent().getLongExtra("targetUid", 0L);
        isMySelf = getIntent().getBooleanExtra("isMySelf", false);
        initView();
    }

    private void initView() {
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        svgaCarTry = (SVGAImageView) findViewById(R.id.svga_car_try);
        svgaCarTry.setClearsAfterStop(true);
        svgaCarTry.setLoops(1);
        ivHeadPic = (ImageView) findViewById(R.id.iv_user_head_pic);
        ivHeadWear = (ImageView) findViewById(R.id.iv_user_head_wear);
        mIndicator = (MagicIndicator) findViewById(R.id.mi_dress_type);
        vpDressMall = (ViewPager) findViewById(R.id.vp_dress_content);
        CommonMagicIndicatorAdapter mMsgIndicatorAdapter = new CommonMagicIndicatorAdapter(this, getMvpPresenter().getTabInfos(), 0);
        mMsgIndicatorAdapter.setSize(17);
        mMsgIndicatorAdapter.setSelectColorId(R.color.mm_theme);
        mMsgIndicatorAdapter.setOnItemSelectListener(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mMsgIndicatorAdapter);
        BaseIndicatorAdapter mTabAdapter = new BaseIndicatorAdapter(getSupportFragmentManager(), getFragmentsList(isMySelf));
        mIndicator.setNavigator(commonNavigator);
        vpDressMall.setAdapter(mTabAdapter);
        ViewPagerHelper.bind(mIndicator, vpDressMall);
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            ImageLoadUtils.loadCircleImage(this, userInfo.getAvatar(), ivHeadPic, R.drawable.ic_default_avatar);
            if (isMySelf && StringUtils.isNotEmpty(userInfo.getHeadwearUrl())) {
                ImageLoadUtils.loadImage(this, userInfo.getHeadwearUrl(), ivHeadWear);
            }
        }
        svgaCarTry.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                showVgg = false;
                svgaCarTry.setVisibility(View.GONE);
                svgaCarTry.clearAnimation();
            }

            @Override
            public void onRepeat() {
            }

            @Override
            public void onStep(int i, double v) {
            }
        });
        mToolBar.setOnBackBtnListener(view -> finish());
    }

    @Override
    public void onItemSelect(int position) {
        vpDressMall.setCurrentItem(position);
    }

    @Override
    public void onHeadWearChangeListener(String url) {
        if (StringUtils.isNotEmpty(url) && ivHeadWear != null) {
            ImageLoadUtils.loadImage(this, url, ivHeadWear);
        }
    }

    @Override
    public void onCarTryListener(String url) {
        if (showVgg) {
            SingleToastUtil.showToast(getString(R.string.txt_dress_up_car_try));
        }
        if (StringUtils.isNotEmpty(url) && svgaCarTry != null) {
            showVgg = true;
            SVGAParser parser = new SVGAParser(this);
            try {
                url = DESUtils.DESAndBase64Decrypt(url, giftCarSecret);
                parser.parse(new URL(url), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem);
                        svgaCarTry.setImageDrawable(drawable);
                        svgaCarTry.startAnimation();
                        svgaCarTry.setVisibility(View.VISIBLE);
                        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(svgaCarTry, "alpha", 0.0F, 2.0F).setDuration(800);
                        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
                        objectAnimator1.start();
                    }
                    @Override
                    public void onError() {
                        showVgg = false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Fragment> getFragmentsList(boolean isMySelf){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(DressUpFragment.newInstance(1,isMySelf, targetUid));
        fragments.add(DressUpFragment.newInstance(0,isMySelf, targetUid));
        return fragments;
    }


}
