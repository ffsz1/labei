package com.vslk.lbgx.ui.me.bills.fragment;

import android.animation.ObjectAnimator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseLazyFragment;
import com.vslk.lbgx.ui.me.wallet.activity.IBillNewListener;
import com.vslk.lbgx.ui.widget.RecyclerViewNoBugLinearLayoutManager;
import com.vslk.lbgx.ui.widget.itemdecotion.PowerGroupListener;
import com.vslk.lbgx.ui.widget.itemdecotion.PowerfulStickyDecoration;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 新皮：账单模块所有单个页面基类
 *
 * @author zwk 2018/5/31
 */
public abstract class BillBaseFragment extends BaseLazyFragment implements OnDateSetListener {

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    private LinearLayout llDate;
    private ImageView ivGoTop;
    private boolean isShow;
    public TextView tvTime;
    protected int mCurrentCounter = Constants.PAGE_START;
    protected static final int PAGE_SIZE = Constants.BILL_PAGE_SIZE;
    protected TimePickerDialog.Builder mDialogYearMonthDayBuild;
    protected long time = System.currentTimeMillis();
    protected List<BillItemEntity> mBillItemEntityList = new ArrayList<>();

    private Calendar mCalendar = Calendar.getInstance();

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    public void onFindViews() {
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mRefreshLayout = mView.findViewById(R.id.swipe_refresh);
        ivGoTop = mView.findViewById(R.id.iv_go_today);
        tvTime = mView.findViewById(R.id.tv_time);
        llDate = mView.findViewById(R.id.ll_date);
    }

    @Override
    public void onSetListener() {
        tvTime.setOnClickListener(this);
        ivGoTop.setOnClickListener(this);
        llDate.setOnClickListener(this);
        mView.findViewById(R.id.iv_go_today).setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(() -> {
            mCurrentCounter = Constants.PAGE_START;
            loadData();
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !isShow) {
                    isShow = true;
                    ObjectAnimator.ofFloat(ivGoTop, "translationY", 0, 300).setDuration(200).start();
                } else if (dy < 0 && isShow) {
                    isShow = false;
                    ObjectAnimator.ofFloat(ivGoTop, "translationY", 300, 0).setDuration(200).start();
                }
            }
        });
    }

    @Override
    public void initiate() {
        setDate(System.currentTimeMillis());
        mDialogYearMonthDayBuild = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setTitleStringId("日期选择")
                .setThemeColor(getResources().getColor(R.color.color_ffcccccc))
                .setWheelItemTextNormalColor(getResources().getColor(R.color
                        .timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.black))
                .setCallBack(this);

        PowerfulStickyDecoration decoration = PowerfulStickyDecoration.Builder.init(new PowerGroupListener() {
            @Override
            public String getGroupName(int position) {
                //获取组名，用于判断是否是同一组
                if (mBillItemEntityList.size() > position && position >= 0) {
                    return mBillItemEntityList.get(position).time;
                }
                return null;
            }

            @Override
            public View getGroupView(int position) {
                //获取自定定义的组View
                if (mBillItemEntityList.size() > position && position >= 0) {
                    View view = getLayoutInflater().inflate(R.layout.item_group, null, false);
                    TextView text = view.findViewById(R.id.tv);
                    text.setText(TimeUtils
                            .getDateTimeString(JavaUtil.str2long(mBillItemEntityList.get(position).time),
                                    "yyyy年MM月dd日"));
                    return view;
                } else {
                    return null;
                }
            }
        })
                //设置高度
                .setGroupHeight(UIUtil.dip2px(mContext, 42))
                //靠右边显示   默认左边
                .isAlignLeft(true)
                //设置背景   默认透明
                .setGroupBackground(getResources().getColor(R.color.colorPrimaryDark))
                .build();
        mRecyclerView.addItemDecoration(decoration);
        RecyclerViewNoBugLinearLayoutManager manager = new RecyclerViewNoBugLinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
    }

    protected void setDate(long time) {
        mCalendar.setTimeInMillis(time);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        tvTime.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new Date(time)));
    }

    private void firstLoadDate() {
        mCurrentCounter = Constants.PAGE_START;
        showLoading();
        loadData();
    }

    protected abstract void loadData();

    @Override
    protected void onLazyLoadData() {
        firstLoadDate();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        this.time = millseconds;
        setDate(millseconds);
        mCurrentCounter = Constants.PAGE_START;
        showLoading();
        loadData();
    }

    @Override
    public View.OnClickListener getLoadListener() {
        return v -> {
            mCurrentCounter = Constants.PAGE_START;
            showLoading();
            loadData();
        };
    }

    @Override
    public void showNetworkErr() {
        mRefreshLayout.setRefreshing(false);
        super.showNetworkErr();

    }

    private IBillNewListener listener;

    public void setBillNewListener(IBillNewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_go_today:
                mCurrentCounter = Constants.PAGE_START;
                time = System.currentTimeMillis();
                setDate(time);
                showLoading();
                loadData();
                break;
            case R.id.tv_time:
            case R.id.ll_date:
                mDialogYearMonthDayBuild.build().show(getFragmentManager(), "year_month_day");
                break;
            default:
                break;
        }
    }

    public void showDate() {
        mDialogYearMonthDayBuild.build().show(getFragmentManager(), "year_month_day");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogYearMonthDayBuild != null) {
            mDialogYearMonthDayBuild.setCallBack(null);
            mDialogYearMonthDayBuild = null;
        }
    }
}
