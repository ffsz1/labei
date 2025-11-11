package com.vslk.lbgx.ui.me.shopping.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hncxco.library_ui.widget.AppToolBar;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.DESUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.vslk.lbgx.presenter.shopping.DressUpPresenter;
import com.vslk.lbgx.ui.me.shopping.fragment.ShopFragment;
import com.vslk.lbgx.utils.ImageLoadUtils;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tongdaxing.xchat_framework.util.util.DESUtils.giftCarSecret;

/**
 * @author Administrator
 * @date 2018/3/30
 */

@CreatePresenter(DressUpPresenter.class)
public class ShopActivity extends BaseMvpActivity<IMvpBaseView, DressUpPresenter> implements IMvpBaseView, ViewPager.OnPageChangeListener {

    @BindView(R.id.toolbar)
    AppToolBar mToolBar;
    @BindView(R.id.vp_shop)
    ViewPager vpShop;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.iv_headwear)
    ImageView mHeadWear;
    @BindView(R.id.svga_imageview)
    SVGAImageView svgaImageview;
    @BindView(R.id.avatar_gaussian)
    ImageView avatarGaussian;
    @BindView(R.id.car_list)
    TextView carList;
    @BindView(R.id.headwear_list)
    TextView headwearList;
    @BindView(R.id.rb_tab)
    RadioGroup rbTab;

    private boolean showVgg = false;
    private UserInfo userInfo;

    private boolean isMySelf;
    private long targetUid;

    public static final int DRESS_HEADWEAR = 0;
    public static final int DRESS_CAR = 1;

    /**
     * @param isMySelf  是否是自己
     * @param targetUid 赠送人的uid
     */
    public static void start(Context context, boolean isMySelf, long targetUid) {
        Intent intent = new Intent(context, ShopActivity.class);
        intent.putExtra("targetUid", targetUid);
        intent.putExtra("isMySelf", isMySelf);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundle(getIntent());
        setContentView(R.layout.activity_car);
        ButterKnife.bind(this);
        findViews();
        initUserInfo();
        BaseIndicatorAdapter baseIndicatorAdapter = new BaseIndicatorAdapter(getSupportFragmentManager(), getFragmentsList());
        vpShop.setOverScrollMode(ViewPager.OVER_SCROLL_ALWAYS);
        vpShop.setAdapter(baseIndicatorAdapter);
        vpShop.addOnPageChangeListener(this);
        rbTab.check(R.id.car_list);
        changedSelect(0);
        rbTab.setOnCheckedChangeListener((g, id) -> {
            switch (id) {
                case R.id.car_list:
                    vpShop.setCurrentItem(0);
                    changedSelect(0);
                    break;
                case R.id.headwear_list:
                    vpShop.setCurrentItem(1);
                    changedSelect(1);
                    break;
                default:
                    break;
            }
        });
    }

    private void initBundle(Intent intent) {
        targetUid = intent.getLongExtra("targetUid", 0);
        isMySelf = intent.getBooleanExtra("isMySelf", false);
    }

    private void findViews() {
        mToolBar.setOnBackBtnListener(view -> finish());
        initSvgaImageView();
    }

    private void initSvgaImageView() {
        svgaImageview.setVisibility(View.GONE);
        svgaImageview.setClearsAfterStop(true);
        svgaImageview.setLoops(1);
        svgaImageview.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                showVgg = false;
                svgaImageview.setVisibility(View.GONE);
                svgaImageview.clearAnimation();
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private void initUserInfo() {
        if (isMySelf) {
            targetUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        }
        if (targetUid == 0) {
            return;
        }
        userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(targetUid, true);
        showUserHeadWear();
    }

    private void showUserHeadWear() {
        if (userInfo != null) {
            if (!StringUtil.isEmpty(userInfo.getAvatar())) {
//                ImageLoadUtils.loadImageWithBlurTransformationAll(avatarGaussian.getContext(), userInfo.getAvatar(),
//                        avatarGaussian);
                ImageLoadUtils.loadCircleImage(mAvatar.getContext(), userInfo.getAvatar(), mAvatar,
                        R.drawable.ic_default_avatar);
            }
            if (!StringUtil.isEmpty(userInfo.getHeadwearUrl())) {
                ImageLoadUtils.loadImage(mHeadWear.getContext(), userInfo.getHeadwearUrl(), mHeadWear);
            }
        }
    }

    private void drawSvgaEffect(String url) throws Exception {
        if (showVgg) {
            SingleToastUtil.showToast(getString(R.string.txt_dress_up_car_try));
        }
        if (StringUtils.isNotEmpty(url) && svgaImageview != null) {
            showVgg = true;
            SVGAParser parser = new SVGAParser(this);
            try {
                url = DESUtils.DESAndBase64Decrypt(url, giftCarSecret);
                parser.parse(new URL(url), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem);
                        svgaImageview.setImageDrawable(drawable);
                        svgaImageview.startAnimation();
                        svgaImageview.setVisibility(View.VISIBLE);
                        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(svgaImageview, "alpha", 0.0F, 2.0F)
                                .setDuration(800);
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

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void showCarAnim(String carUrl) {
        try {
            drawSvgaEffect(carUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示用户选中的头饰
     */
    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void showHeadWear(String headWear) {
        if (!StringUtil.isEmpty(headWear)) {
            ImageLoadUtils.loadImage(mHeadWear.getContext(), headWear, mHeadWear);
        }
    }

    public List<Fragment> getFragmentsList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ShopFragment.newInstance(DRESS_CAR, isMySelf, targetUid));
        fragments.add(ShopFragment.newInstance(DRESS_HEADWEAR, isMySelf, targetUid));
        return fragments;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changedSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changedSelect(int position) {
        if (position == 0) {
            rbTab.check(R.id.car_list);
            carList.setTextColor(ContextCompat.getColor(this, R.color.back_font));
            headwearList.setTextColor(ContextCompat.getColor(this, R.color.color_CCCCCC));
        } else {
            rbTab.check(R.id.headwear_list);
            headwearList.setTextColor(ContextCompat.getColor(this, R.color.back_font));
            carList.setTextColor(ContextCompat.getColor(this, R.color.color_CCCCCC));
        }
    }

}
