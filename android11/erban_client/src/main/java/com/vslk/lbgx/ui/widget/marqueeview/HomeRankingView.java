package com.vslk.lbgx.ui.widget.marqueeview;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.RankingInfo;

import java.util.List;

/**
 * 首页排行榜
 * Created by ${Seven} on 2017/11/2.
 */
public class HomeRankingView extends MarqueeView {

    private SparseArray<List<RankingInfo.Ranking>> mListMap;
    private Context mContext;

    public HomeRankingView(Context context) {
        this(context, null);
    }

    public HomeRankingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setHomeNoticeInfoList(RankingInfo rankingInfo) {
        if (rankingInfo == null) return;
        initData(rankingInfo);
    }

    private void initData(RankingInfo rankingInfo) {
        if (mListMap == null)
            mListMap = new SparseArray<>();
        mListMap.put(0, rankingInfo.starList);
        mListMap.put(1, rankingInfo.nobleList);
//        mListMap.put(2, rankingInfo.roomList);
    }


    /**
     * 根据通知列表，启动翻页公告
     *
     * @param rankingInfo 字符串列表
     */
    public void startWithLists(RankingInfo rankingInfo) {
        startWithLists(rankingInfo, inAnimResId, outAnimResId);
    }

    /**
     * 根据通知列表，启动翻页公告
     *
     * @param rankingInfo  通知列表
     * @param inAnimResId  进入动画的resID
     * @param outAnimResID 离开动画的resID
     */
    public void startWithLists(RankingInfo rankingInfo, @AnimRes int inAnimResId, @AnimRes int outAnimResID) {
        setHomeNoticeInfoList(rankingInfo);
        start(inAnimResId, outAnimResID);
    }

    @Override
    protected boolean start(int inAnimResId, int outAnimResID) {
        removeAllViews();
        clearAnimation();

        position = 0;
        addView(createView(mListMap.get(position)));

        if (mListMap.size() > 1) {
            setInAndOutAnimation(inAnimResId, outAnimResID);
            startFlipping();
        }

        if (getInAnimation() != null) {
            getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    position++;
                    if (position >= mListMap.size()) {
                        position = 0;
                    }
                    if  (position < mListMap.size()) {
                        View view = createView(mListMap.get(position));
                        if (view.getParent() == null) {
                            addView(view);
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        return true;
    }

    private View createView(final List<RankingInfo.Ranking> rankingList) {
        int index = (getDisplayedChild() + 1) % 3;
        View view = getChildAt(index);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_notice, null);
//            textView.setGravity(gravity);
//            textView.setTextColor(textColor);
//            textView.setTextSize(textSize);
//            textView.setSingleLine(singleLine);
        }

//        textView.setText(text);
        TextView noticeTitle = (TextView) view.findViewById(R.id.tv_notice_title);
        ImageView noticeCover1 = (ImageView) view.findViewById(R.id.iv_cover_1);
        ImageView noticeCover2 = (ImageView) view.findViewById(R.id.iv_cover_2);
        ImageView noticeCover3 = (ImageView) view.findViewById(R.id.iv_cover_3);
        ImageView arrowRight = (ImageView) view.findViewById(R.id.iv_arrow_right);

        if (position == 0) {
            noticeTitle.setText(R.string.star_list_title);
            bindAvater(rankingList, noticeCover1, noticeCover2, noticeCover3);
        } else if (position == 1) {
            noticeTitle.setText(R.string.noble_list_title);
            bindAvater(rankingList, noticeCover1, noticeCover2, noticeCover3);
        } else if (position == 2) {
//            noticeTitle.setText(R.string.room_list_title);
//            bindAvater(rankingList, noticeCover1, noticeCover2, noticeCover3);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeClickListener != null) {
                    noticeClickListener.onNoticeClick(getPosition(), rankingList);
                }
            }
        });

        view.setTag(this.position);
        return view;
    }

    private void bindAvater(List<RankingInfo.Ranking> rankingList, ImageView noticeCover1, ImageView noticeCover2, ImageView noticeCover3) {
        if (rankingList != null) {
            int size = rankingList.size();
            if (size > 0) {
                bindAvater(noticeCover3, rankingList.get(0));
            }
            if (size > 1) {
                bindAvater(noticeCover2, rankingList.get(1));
            }
            if (size > 2) {
                bindAvater(noticeCover1, rankingList.get(2));
            }
        }
    }

    private void bindAvater(ImageView imageView, RankingInfo.Ranking ranking) {
        if (ranking != null) {
            GlideApp.with(mContext)
                    .load(ranking.getAvatar())
//                    .transform(new GlideCircleTransform(mContext))
                    .transform(new CircleCrop())
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.ic_default_avatar)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    private NoticeClickListener noticeClickListener;

    public void setNoticeClickListener(NoticeClickListener noticeClickListener) {
        this.noticeClickListener = noticeClickListener;
    }

    public interface NoticeClickListener {
        void onNoticeClick(int position, List<RankingInfo.Ranking> rankingList);
    }
}
