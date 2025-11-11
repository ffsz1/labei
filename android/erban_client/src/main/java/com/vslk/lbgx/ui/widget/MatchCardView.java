package com.vslk.lbgx.ui.widget;

import android.animation.Animator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.netease.nim.uikit.glide.GlideApp;
import com.opensource.svgaplayer.SVGAImageView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.tinderstack.TinderStackLayout;
import com.tongdaxing.erban.libcommon.tinderstack.bus.RxBus;
import com.tongdaxing.erban.libcommon.tinderstack.bus.events.TopCardMovedEvent;
import com.tongdaxing.erban.libcommon.tinderstack.utilities.DisplayUtility;
import com.tongdaxing.xchat_core.find.MicroMatch;
import com.tongdaxing.xchat_core.utils.StarUtils;
import com.vslk.lbgx.utils.UIHelper;

import java.util.Date;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * Created by etiennelawlor on 11/17/16.
 */

public class MatchCardView extends FrameLayout implements View.OnTouchListener, View.OnClickListener {

    private static final float CARD_ROTATION_DEGREES = 40.0f;
    private static final float BADGE_ROTATION_DEGREES = 15.0f;
    public static final int DURATION = 300;

    // endregion

    // region Views
    private ImageView div_matchAva;
    private ImageView iv_sex;
    private DrawableTextView attention;
    //    private DrawableTextView attentioned;
    private ImageView iv_recordPlayer;

    private TextView tv_matchName;
    private TextView tv_time;
    //    private TextView tv_age;
//    private TextView tv_const;
    private TextView tv_dynamic;
    private View ll_recordPlayer;
    private boolean isAudioPlaying = false;

    private SVGAImageView svgaiv_recordAnim;

    private float oldX;
    private float oldY;
    private float newX;
    private float newY;
    private float dX;
    private float dY;
    private float rightBoundary;
    private float leftBoundary;
    private int screenWidth;
    private int padding;
    private LevelView level;

    public MatchCardView(Context context) {
        super(context);
        init(context, null);
    }

    public MatchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MatchCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        if (!TinderStackLayout.canTouchToMove) {
            return super.onTouchEvent(motionEvent);
        }
        TinderStackLayout tinderStackLayout = ((TinderStackLayout) view.getParent());
        MatchCardView topCard = (MatchCardView) tinderStackLayout.getChildAt(tinderStackLayout.getChildCount() - 1);
        if (null != topCard && topCard.equals(view)) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    oldX = motionEvent.getX();
                    oldY = motionEvent.getY();
                    // cancel any current animations
                    view.clearAnimation();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (isCardBeyondLeftBoundary(view)) {
                        RxBus.getInstance().send(new TopCardMovedEvent(-(screenWidth)));
                        dismissCard(view, -(screenWidth * 2));
                    } else if (isCardBeyondRightBoundary(view)) {
                        RxBus.getInstance().send(new TopCardMovedEvent(screenWidth));
                        dismissCard(view, (screenWidth * 2));
                    } else {
                        RxBus.getInstance().send(new TopCardMovedEvent(0));
                        resetCard(view);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    newX = motionEvent.getX();
                    newY = motionEvent.getY();
                    dX = newX - oldX;
                    dY = newY - oldY;
                    float posX = view.getX() + dX;
                    RxBus.getInstance().send(new TopCardMovedEvent(posX));
                    // Set new position
                    view.setX(view.getX() + dX);
                    view.setY(view.getY() + dY);
                    setCardRotation(view, view.getX());
                    updateAlphaOfBadges(posX);
                    return true;
                default:
                    return super.onTouchEvent(motionEvent);
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    // endregion

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnTouchListener(null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            inflate(context, R.layout.card_view_micro_match, this);
            div_matchAva = findViewById(R.id.div_matchAva);
            iv_sex = findViewById(R.id.iv_sex);
            iv_recordPlayer = findViewById(R.id.iv_recordPlayer);

            attention = findViewById(R.id.attention);
            level = findViewById(R.id.level_info);
//            attentioned = findViewById(R.id.attentioned);
            attention.setOnClickListener(this);
//            attentioned.setOnClickListener(this);
            tv_matchName = findViewById(R.id.tv_matchName);
            tv_time = findViewById(R.id.tv_time);
//            tv_age = findViewById(R.id.tv_age);
//            tv_const = findViewById(R.id.tv_const);
            tv_dynamic = findViewById(R.id.tv_dynamic);
            ll_recordPlayer = findViewById(R.id.ll_recordPlayer);
            ll_recordPlayer.setOnClickListener(this);

            screenWidth = DisplayUtility.getScreenWidth(context);
            // Left 3/6 of screen
            leftBoundary = screenWidth * (2.0f / 6.0f);
            // Right 3/6 of screen
            rightBoundary = screenWidth * (4.0f / 6.0f);
            padding = DisplayUtility.dp2px(context, 16);

            svgaiv_recordAnim = findViewById(R.id.svgaiv_recordAnim);

            setOnTouchListener(this);
        }
    }

    // Check if card's middle is beyond the left boundary
    private boolean isCardBeyondLeftBoundary(View view) {
        return (view.getX() + (view.getWidth() / 2) < leftBoundary);
    }

    // Check if card's middle is beyond the right boundary
    private boolean isCardBeyondRightBoundary(View view) {
        return (view.getX() + (view.getWidth() / 2) > rightBoundary);
    }

    public void dismissCardSelf() {
        dismissCard(this, -(screenWidth * 2));
    }

    private void dismissCard(final View view, int xPos) {
        view.animate().x(xPos).y(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (view instanceof SVGAImageView) {
                            SVGAImageView svgaImageView = (SVGAImageView) view;
                            svgaImageView.setImageDrawable(null);
                        }
                        ViewGroup viewGroup = (ViewGroup) view.getParent();
                        if (viewGroup != null) {
                            viewGroup.removeView(view);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    private void resetCard(View view) {
        view.animate()
                .x(0)
                .y(0)
                .rotation(0)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(DURATION);
        //原逻辑为将左右上角标识隐藏
    }

    private void setCardRotation(View view, float posX) {
        float rotation = (CARD_ROTATION_DEGREES * (posX)) / screenWidth;
        int halfCardHeight = (view.getHeight() / 2);
        if (oldY < halfCardHeight - (2 * padding)) {
            view.setRotation(rotation);
        } else {
            view.setRotation(-rotation);
        }
    }

    // set alpha of like and nope badges
    private void updateAlphaOfBadges(float posX) {
        float alpha = (posX - padding) / (screenWidth * 0.50f);
        //原代码逻辑为左右上角隐藏标识回显
    }

    private MicroMatch microMatch;

    public MicroMatch getMicroMatch() {
        return microMatch;
    }

    public void bind(MicroMatch microMatch) {
        if (null == microMatch) {
            return;
        }
        this.microMatch = microMatch;
        setTag(microMatch);
        if (!TextUtils.isEmpty(microMatch.getAvatar())) {
            GlideApp.with(div_matchAva.getContext().getApplicationContext()).load(microMatch.getAvatar()).centerCrop()
                    .into(div_matchAva);
        }
        tv_matchName.setText(microMatch.getNick());
        level.setCharmLevel(microMatch.getCharmLevel());
        level.setExperLevel(microMatch.getExperLevel());
        String dur = "";
        if (microMatch.getVoiceDura() >= 10) {
            dur = "00:".concat(String.valueOf(microMatch.getVoiceDura()));
        } else if (microMatch.getVoiceDura() > 0) {
            dur = "00:0".concat(String.valueOf(microMatch.getVoiceDura()));
        } else {
            dur = "00:0".concat(String.valueOf(microMatch.getVoiceDura()));
//            ll_recordPlayer.setVisibility(View.GONE);
        }
        tv_time.setText(dur);
        iv_sex.setImageDrawable(iv_sex.getContext().getResources()
                .getDrawable(microMatch.getGender() == 1 ? R.drawable.icon_man : R.drawable.icon_woman));

        tv_dynamic.setText((microMatch.getUserDesc() == null || microMatch.getUserDesc().isEmpty()) ? getString(R.string.user_info_desc_empty) : microMatch.getUserDesc());

        int charmLevel = microMatch.getCharmLevel();
        level.setCharmLevel(charmLevel);
//        int experLevel = microMatch.getExperLevel();
//        level.setExperLevel(experLevel);
        if (microMatch.getBirth() > 0) {
            int age = StarUtils.getAge(new Date(microMatch.getBirth()));
//            tv_age.setText(String.valueOf(age));
//            String star = StarUtils.getConstellation(new Date(microMatch.getBirth()));
//            if (null == star) {
//                tv_const.setVisibility(View.GONE);
//            } else {
//                tv_const.setText(star);
//                tv_const.setVisibility(View.VISIBLE);
//            }
        }

        updateAttentionStatus(microMatch.isLike());
    }

    private OnMatchCardViewClickListener onMatchCardViewClickListener;

    public void setOnMatchCardViewClickListener(OnMatchCardViewClickListener listener) {
        this.onMatchCardViewClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attention:
//            case R.id.attentioned:
//                if (onMatchCardViewClickListener != null) {
//                    onMatchCardViewClickListener.onAttentionIVClicked(this);
//                }
                UIHelper.showUserInfoAct(getContext(), microMatch.getUid());
                break;
            case R.id.ll_recordPlayer:
                if (onMatchCardViewClickListener != null) {
                    onMatchCardViewClickListener.onAudioPlayerViewClicked(MatchCardView.this, !isAudioPlaying);
                }
                break;
            default:
                break;
        }
    }

    public interface OnMatchCardViewClickListener {

        void onAttentionIVClicked(MatchCardView matchCardView);

        void onAudioPlayerViewClicked(MatchCardView matchCardView, boolean isPlaying);
    }

    public void updateAttentionStatus(boolean hasAttented) {
        attention.setVisibility(hasAttented ? INVISIBLE : VISIBLE);
//        attentioned.setVisibility(hasAttented ? VISIBLE : INVISIBLE);
    }

    public void updateAudioPlayStatus(boolean isPlaying) {
        isAudioPlaying = isPlaying;
        iv_recordPlayer.setImageDrawable(iv_recordPlayer.getContext().getResources().getDrawable(
                isPlaying ? R.drawable.ic_user_info_pause : R.drawable.ic_user_info_play));

        if (isPlaying) {
            Glide.with(svgaiv_recordAnim.getContext().getApplicationContext()).asGif().load(
                    R.drawable.micro_match_record_play_anim).into(svgaiv_recordAnim);
        } else {
            svgaiv_recordAnim.setImageDrawable(
                    svgaiv_recordAnim.getResources().getDrawable(R.drawable.icon_micro_match_record_default));
        }
    }
}
