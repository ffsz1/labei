package com.vslk.lbgx.room.avroom.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.custom.bean.LotteryBoxAttachment;
import com.tongdaxing.xchat_framework.util.util.ResolutionUtils;
import com.vslk.lbgx.room.widget.dialog.NewUserInfoDialog;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 礼物特效布局
 *
 * @author xiaoyu
 * @date 2017/12/20
 */

public class BoxGiftView extends ConstraintLayout {

    @BindView(R.id.img_header)
    CircleImageView imgHeader;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_get_tips)
    TextView tvGetTips;
    @BindView(R.id.img_gift)
    ImageView imgGift;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_gift_name)
    TextView tvGiftName;
    @BindView(R.id.cl_main)
    ConstraintLayout clMain;
    private Context context;
    private int mScreenWidth;
    String unit;

    private boolean isShowing = false;


    public BoxGiftView(Context context) {
        this(context, null);

    }

    public BoxGiftView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public BoxGiftView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_box_gift_effect, this);
        ButterKnife.bind(view);
        unit = context.getString(R.string.gift_expend_gold);
        mScreenWidth = ResolutionUtils.getScreenWidth(getContext());
    }

    public void showGift(LotteryBoxAttachment boxAttachment, OnBoxClickListener onBoxClickListener) {
        setShowing(true);
        ImageLoadUtils.loadCircleImage(context, boxAttachment.getAvatar(), imgHeader, R.drawable.ic_default_avatar);
        ImageLoadUtils.loadCircleImage(context, boxAttachment.getGiftUrl(), imgGift, R.mipmap.gift_record_title_icon);
        tvNick.setText(boxAttachment.getNick());
        tvNum.setText("X" + boxAttachment.getCount());
        tvGiftName.setText(boxAttachment.getGiftName() + "（" + boxAttachment.getGoldPrice() + unit + "）");
        clMain.setVisibility(VISIBLE);
        final Point center = new Point();
        center.x = ResolutionUtils.getScreenWidth(getContext()) / 2;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(clMain, "translationX", -mScreenWidth, 0).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(clMain, "alpha", 0.0F, 2.0F).setDuration(800);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator1.start();
        clMain.postDelayed(() -> {
            if (context != null) {
                deleteAnim();
            }
        }, 1300);
        clMain.setOnClickListener(v -> {
            if (onBoxClickListener!=null){
                onBoxClickListener.onBoxClick(boxAttachment.getAccount());
            }
        });
    }


    private void deleteAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(clMain, "translationX", clMain.getX(),
                ResolutionUtils.getScreenWidth(getContext())).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(clMain, "alpha", 1.0F, 0.0F).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (context != null) {
                    clMain.setVisibility(GONE);
                    setShowing(false);
                }
            }
        });
        objectAnimator1.start();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (clMain != null) {
            clMain.clearAnimation();
        }
    }

    public interface OnBoxClickListener {
        public void onBoxClick(long uid);
    }
}
