package com.tongdaxing.xchat_framework.util.util.anim;

import android.animation.Animator;
import android.animation.ValueAnimator;

import com.tongdaxing.xchat_framework.util.util.log.MLog;

/**
 * Created by lijun on 2015/7/24.
 *
 * 可暂停恢复的Animator管理，因为 ValueAnimator.pause, resume 接口需要 SDK19以上才能调用
 */
public class PauseableAnimManager {

    private ValueAnimator valueAnimator;
    private boolean isAnimPause;
    private long mAnimPauseTime;

    private ValueAnimator.AnimatorUpdateListener updateListener;
    private ValueAnimator.AnimatorListener animatorListener;

    public PauseableAnimManager(ValueAnimator valueAnimator) {
        if (null == valueAnimator) {
            throw new IllegalArgumentException("valueAnimator should not be null");
        }

        this.valueAnimator = valueAnimator;
        init();
    }

    public void setUpdateListener(ValueAnimator.AnimatorUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setAnimatorListener(ValueAnimator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void start() {
        valueAnimator.start();
    }

    public void pause() {
        if (null != valueAnimator && valueAnimator.isRunning()) {
            isAnimPause = true;
            mAnimPauseTime = -1;
        }
    }

    public void resume() {
        if (isAnimPause) {
            MLog.debug(this, "resumed: %d", mAnimPauseTime);
            valueAnimator.setCurrentPlayTime(mAnimPauseTime);
            isAnimPause = false;
        }
    }

    public void cancel() {
        valueAnimator.cancel();
    }

    public void end() {
        valueAnimator.end();
    }

    private void init() {
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                MLog.debug(this, "onAnimationUpdate: %d", animation.getCurrentPlayTime());
                if (!isAnimPause) {
                    if (null != updateListener) {
                        updateListener.onAnimationUpdate(animation);
                    }
                } else {
                    if (mAnimPauseTime < 0) {
                        mAnimPauseTime = animation.getCurrentPlayTime();
                        MLog.debug(this, "mAnimPauseTime: %d", mAnimPauseTime);
                    }
                }
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isAnimPause && null != animatorListener) {
                    animatorListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isAnimPause && null != animatorListener) {
                    animatorListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (!isAnimPause && null != animatorListener) {
                    animatorListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (!isAnimPause && null != animatorListener) {
                    animatorListener.onAnimationRepeat(animation);
                }
            }
        });
    }

}
