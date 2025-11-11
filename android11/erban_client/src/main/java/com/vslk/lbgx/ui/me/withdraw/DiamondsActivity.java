package com.vslk.lbgx.ui.me.withdraw;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.me.MePresenter;
import com.vslk.lbgx.ui.me.task.view.IMeView;
import com.vslk.lbgx.ui.me.wallet.activity.ExchangeGoldFragment;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiamondsActivity extends BaseMvpActivity<IMeView, MePresenter> implements IMeView {
    @BindView(R.id.rg_gift_indicator)
    RadioGroup rgGiftIndicator;
    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.right_btn)
    TextView rightBtn;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rb_gift_tab)
    RadioButton rbGiftTab;
    @BindView(R.id.rb_gift_pack_tab)
    RadioButton rbGiftPackTab;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diamonds);
        ButterKnife.bind(this);
        rgGiftIndicator.setOnCheckedChangeListener((g, id) -> {
            switch (id) {
                case R.id.rb_gift_pack_tab:
                    vpContainer.setCurrentItem(1);
                    rbGiftTab.setTextSize(14f);
                    rbGiftPackTab.setTextSize(16f);
                    break;
                default:
                    rbGiftTab.setTextSize(16f);
                    rbGiftPackTab.setTextSize(14f);
                    vpContainer.setCurrentItem(0);
                    break;
            }
        });
        ivBack.setOnClickListener(v -> {
            finish();
        });
        rightBtn.setOnClickListener(v -> {
            CommonWebViewActivity.start(v.getContext(), WebUrl.WITTH_DRAW);
        });
        fragments = new ArrayList<>();
        fragments.add(new ExchangeGoldFragment());
        fragments.add(new WithdrawFragment());
        DiamondsAdapter diamondsAdapter = new DiamondsAdapter(getSupportFragmentManager());
        vpContainer.setAdapter(diamondsAdapter);
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rgGiftIndicator.check(position == 0 ? R.id.rb_gift_tab : R.id.rb_gift_pack_tab);
                hieInput();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void hieInput() {
        InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.hideSoftInputFromWindow(rgGiftIndicator.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private class DiamondsAdapter extends FragmentPagerAdapter {

        public DiamondsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
