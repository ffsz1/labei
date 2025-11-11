package com.tongdaxing.erban.libcommon.tinderstack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;

import com.tongdaxing.erban.libcommon.tinderstack.bus.RxBus;
import com.tongdaxing.erban.libcommon.tinderstack.bus.events.TopCardMovedEvent;
import com.tongdaxing.erban.libcommon.tinderstack.utilities.DisplayUtility;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by etiennelawlor on 11/18/16.
 */

public class TinderStackLayout<T> extends FrameLayout {

    // region Constants
    private static final int DURATION = 300;

    /**
     * 用户全局控制卡片是否可以滑动
     */
    public static boolean canTouchToMove = false;

    // region Member Variables
    private PublishSubject<Integer> publishSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable;
    public int screenWidth;
    private int yMultiplier;

    // region Constructors
    public TinderStackLayout(Context context) {
        super(context);
        init();
    }

    public TinderStackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TinderStackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (publishSubject != null) {
            publishSubject.onNext(getChildCount());
        }
        TinderStackLayout.canTouchToMove = getChildCount() >= 2;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        if (publishSubject != null) {
            publishSubject.onNext(getChildCount());
        }
        if (null != onCardViewRemovedListener) {
            onCardViewRemovedListener.onCardViewRemoved();
        }
        TinderStackLayout.canTouchToMove = getChildCount() >= 2;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != compositeDisposable) {
            compositeDisposable.clear();
        }
    }

    /**
     * region Helper Methods
     */
    private void init() {
        setClipChildren(false);
        screenWidth = DisplayUtility.getScreenWidth(getContext());
        yMultiplier = DisplayUtility.dp2px(getContext(), 8);
        compositeDisposable = new CompositeDisposable();
        setUpRxBusSubscription();
    }

    private void setUpRxBusSubscription() {
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (o == null) {
                    return;
                }

                if (o instanceof TopCardMovedEvent) {
                    float posX = ((TopCardMovedEvent) o).getPosX();
                    int childCount = getChildCount();
                    for (int i = childCount - 2; i >= 0; i--) {
                        View childView = getChildAt(i);
                        T tt = (T) childView;
                        if (null == tt) {
                            continue;
                        }
                        if (Math.abs(posX) == (float) screenWidth) {
                            float scaleValue = 1 - ((childCount - 2 - i) / 50.0f);
                            setCarViewAnimProperty(childView, 0, (childCount - 2 - i) * yMultiplier, scaleValue);
                        }
                    }
                }
            }
        };
        // UI Thread
        Observable observable = RxBus.getInstance().toObserverable()
                .observeOn(AndroidSchedulers.mainThread());
        Disposable disposable = observable.subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public PublishSubject<Integer> getPublishSubject() {
        return publishSubject;
    }

    public void addCard(View tc) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int childCount = getChildCount();
        addView(tc, 0, layoutParams);
        float scaleValue = 1 - (childCount / 50.0f);
        setCarViewAnimProperty(tc, 0, childCount * yMultiplier, scaleValue);
    }


    private void setCarViewAnimProperty(View tc, int x, int y, float scale) {
        tc.animate().x(x).y(y)
                .scaleX(scale)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setDuration(DURATION);
    }


    private OnCardViewRemovedListener onCardViewRemovedListener;

    public void setOnCardViewRemovedListener(OnCardViewRemovedListener listener) {
        this.onCardViewRemovedListener = listener;
    }

    public interface OnCardViewRemovedListener {
        void onCardViewRemoved();
    }
}
