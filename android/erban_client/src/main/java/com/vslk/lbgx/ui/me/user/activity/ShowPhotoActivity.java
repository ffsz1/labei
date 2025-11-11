package com.vslk.lbgx.ui.me.user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.me.user.adapter.PhotoAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.ArrayList;

import io.realm.RealmList;

public class ShowPhotoActivity extends BaseActivity {
    private ImageView mImageView;
    private TextView imgCount;
    private ViewPager viewPager;
    private UserPhoto userPhoto;
    private PhotoAdapter photoAdapter;
    private ShowPhotoActivity mActivity;
    private int position;
    private ArrayList<UserPhoto> photoUrls;
    private RealmList<UserPhoto> userPhotos = new RealmList<>();
//    private RealmList<UserPhoto> userPhotoRealmList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        mActivity = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
//        mImageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                viewPager.setCurrentItem(position);
//                imgCount.setText((position + 1) + "/" + photoAdapter.getCount());
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imgCount.setText((position + 1) + "/" + (photoAdapter == null ? 0 : photoAdapter.getCount()));
                }
                imgCount.setText((position + 1) + "/" + (photoAdapter == null ? 0 : photoAdapter.getCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (photoAdapter != null)
            photoAdapter.setmImageOnclickListener(new PhotoAdapter.imageOnclickListener() {
                @Override
                public void onClick() {
                    finish();
                }
            });
    }

    private void initData() {
        position = getIntent().getIntExtra("position", 1);
        String photoJsonData = getIntent().getStringExtra("photoJsonData");
        if (!TextUtils.isEmpty(photoJsonData)) {
            photoUrls = json2PhotoList(Json.parse(photoJsonData));
        } else {
            try {
                photoUrls = (ArrayList<UserPhoto>) getIntent().getSerializableExtra("photoList");
            } catch (Exception e) {

            }

        }
        if (photoUrls != null) {
            photoAdapter = new PhotoAdapter(mActivity, photoUrls);
            viewPager.setAdapter(photoAdapter);
            viewPager.setCurrentItem(position);
            imgCount.setText((position + 1) + "/" + photoAdapter.getCount());
        }
    }

    private ArrayList<UserPhoto> json2PhotoList(Json json) {
        if (json == null || json.key_names().length == 0) {
            finish();
            return null;
        }
        ArrayList<UserPhoto> userPhotos = new ArrayList<>();
        String[] keys = json.key_names();

        for (int i = 0; i < keys.length; i++) {
            Json j = json.json_ok(keys[i]);
            UserPhoto userPhoto = new UserPhoto(j.num_l("pid"), j.str("photoUrl"));
            userPhotos.add(userPhoto);
        }

        return userPhotos;
    }

    private void initView() {
//        mImageView = (ImageView) findViewById(R.id.photoview);
        imgCount = (TextView) findViewById(R.id.tv_count);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected boolean needSteepStateBar() {
        return true;
    }
}
