package com.vslk.lbgx.room.gift;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.common.ICommonClient;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.gift.IGiftCoreClient;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.IPayCoreClient;
import com.tongdaxing.xchat_core.pay.bean.DianDianCoinInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.common.adapter.CommonPagerAdapter;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.TextViewDrawableUtils;
import com.tongdaxing.xchat_framework.util.util.DisplayUtils;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.GiftNumPopWin;
import com.vslk.lbgx.room.avroom.activity.ActivityGiftPlayIntroduceActivity;
import com.vslk.lbgx.room.egg.bean.NimGiftUser;
import com.vslk.lbgx.room.gift.adapter.GiftAdapter;
import com.vslk.lbgx.room.gift.adapter.GiftAvatarAdapter;
import com.vslk.lbgx.room.gift.widget.PageIndicatorView;
import com.vslk.lbgx.room.widget.dialog.NewUserInfoDialog;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.sign.TaskCenterActivity;
import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * @author chenran
 * @date 2017/7/27
 */

public class GiftDialog extends BottomSheetDialog implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener,
        ViewPager.OnPageChangeListener, GiftAvatarAdapter.OnCancelAllMicSelectListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "GiftDialog";
    public static final String ACTIVITY_GIFT = "activity_gift";

    private int oldPageIndex = -1;
    private int oldPosition = -1;
    private final int ROWS = 2;
    private final int COLUMNS = 4;
    private Context context;
    private RecyclerView avatarListRecyclerView;
    private GiftAvatarAdapter avatarListAdapter;
    private OnGiftDialogBtnClickListener giftDialogBtnClickListener;
    private TextView goldText;
    private EasyPopup giftNumberEasyPopup;
    private TextView giftManText;
    private TextView giftNumberText;
    private int giftNumber = 1;
    private long uid;
    private List<MicMemberInfo> micMemberInfos;
    private MicMemberInfo defaultMicMemberInfo;
    private PageIndicatorView giftIndicator;
    private TextView userInfoText;
    private FrameLayout allMicHead;
    private TextView cbAllMic;
    private boolean singlePeople = false;
    private RadioGroup rgIndex;
    private int currentP = 0;//默认礼物
    private LinearLayout llGiftEmpty;
    private RelativeLayout rlGift;
    private RelativeLayout mGiftDialogHeader;
    private RelativeLayout llSinglePeople;
    private List<GiftInfo> giftInfoList;
    private List<GiftInfo> mysteryGiftInfo;
    private List<GiftInfo> diandianCoinGiftInfo;
    private TextView btnSend;
    private RelativeLayout giftDialogToManLayout;
    private ViewPager mViewPager;
    private GiftAdapter[] adapterNews;
    private int currentTab = 0;//有0是普通礼物、1是背包礼物、4活动礼物。默认0
    private int currentPageIndex = 0;//当前tab的下标
    private GiftInfo curSelectedGift;//当前选中礼物
    private TextView btnRecharge;
    private ImageView singleAvatar;
    private TextView singleName;
    private ImageView iv;
    private View quMcBg;
    private LinearLayout llSend;
    private boolean isInfoBtn = true;
    private RelativeLayout activityGiftRllt;
    private TextView introduceTv, allPriceTv;

    public void setGiftDialogBtnClickListener(OnGiftDialogBtnClickListener giftDialogBtnClickListener) {
        this.giftDialogBtnClickListener = giftDialogBtnClickListener;
    }

    public void setSinglePeople(boolean singlePeople) {
        this.singlePeople = singlePeople;
    }

    /**
     * 从这里进来，只能显示单个送礼对象
     *
     * @param context
     * @param uid
     * @param nick
     * @param avatar
     */
    public GiftDialog(Context context, long uid, String nick, String avatar) {
        this(context, uid, nick, avatar, true);
    }

    /**
     * 从这里进来，私聊
     *
     * @param context
     */
    public GiftDialog(Context context, NimGiftUser giftUser) {
        this(context, giftUser.getUid(), giftUser.getNick(), giftUser.getAvatar(), true);
        this.isInfoBtn = giftUser.isInfoBtn();
    }

    /**
     * 从这里进来，根据singlePeople控制送礼对象是单个还是多个
     *
     * @param context
     * @param uid
     * @param nick
     * @param avatar
     * @param singlePeople tue表示单个，false表示多个
     */
    public GiftDialog(Context context, long uid, String nick, String avatar, boolean singlePeople) {
        super(context, R.style.GiftBottomSheetDialog);
        this.uid = uid;
        this.context = context;
        this.singlePeople = singlePeople;
        if (singlePeople) {
            loadSinglePeople(uid, nick, avatar);
        } else {
            loadMultiPeople();
        }
    }

    private void loadSinglePeople(long uid, String nick, String avatar) {
        if (micMemberInfos == null) {
            micMemberInfos = new ArrayList<>();
        }
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid, false);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setNick(nick);
            userInfo.setAvatar(avatar);
        }
        defaultMicMemberInfo = new MicMemberInfo();
        defaultMicMemberInfo.setAvatar(userInfo.getAvatar());
        defaultMicMemberInfo.setNick(userInfo.getNick());
        defaultMicMemberInfo.setUid(uid);
        micMemberInfos.add(defaultMicMemberInfo);
    }

    private void loadMultiPeople() {
        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
        List<MicMemberInfo> micMemberInfos = new ArrayList<>();
        if (!checkHasOwner(mMicQueueMemberMap)) {
            UserInfo roomOwner = CoreManager.getCore(IAVRoomCore.class).getRoomOwner();
            MicMemberInfo micMemberInfo = new MicMemberInfo();
            micMemberInfo.setRoomOwnner(true);
            micMemberInfo.setNick(roomOwner.getNick());
            micMemberInfo.setAvatar(roomOwner.getAvatar());
            micMemberInfo.setMicPosition(-1);
            micMemberInfo.setUid(roomOwner.getUid());
            micMemberInfos.add(micMemberInfo);
        }
        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
            MicMemberInfo micMemberInfo = new MicMemberInfo();
            IMChatRoomMember mChatRoomMember = mMicQueueMemberMap.get(mMicQueueMemberMap.keyAt(i)).mChatRoomMember;
            if (mChatRoomMember == null) {
                continue;
            }
            // 合法判断
            String account = mChatRoomMember.getAccount();
            LogUtils.d("checkHasOwner", account + "   dd");
            if (TextUtils.isEmpty(account) ||
                    TextUtils.isEmpty(mChatRoomMember.getNick()) ||
                    TextUtils.isEmpty(mChatRoomMember.getAvatar())) {
                continue;
            }
            // 排除自己\
            if (AvRoomDataManager.get().isOwner(account)) {
                continue;
            }
            // 设置默认人员
            if (uid > 0 && String.valueOf(uid).equals(account)) {
                this.defaultMicMemberInfo = micMemberInfo;
            }
            // 设置房主
            if (AvRoomDataManager.get().isRoomOwner(account)) {
                micMemberInfo.setRoomOwnner(true);
            }
            micMemberInfo.setNick(mChatRoomMember.getNick());
            micMemberInfo.setAvatar(mChatRoomMember.getAvatar());
            micMemberInfo.setMicPosition(mMicQueueMemberMap.keyAt(i));
            micMemberInfo.setUid(JavaUtil.str2long(account));
            micMemberInfos.add(micMemberInfo);
        }
        this.micMemberInfos = micMemberInfos;
    }

    private boolean checkHasOwner(SparseArray<RoomQueueInfo> mMicQueueMemberMap) {
        UserInfo roomOwner = CoreManager.getCore(IAVRoomCore.class).getRoomOwner();
        if (roomOwner == null) {
            return true;
        }
        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.get(mMicQueueMemberMap.keyAt(i));
            if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null) {
                String account = roomQueueInfo.mChatRoomMember.getAccount();
                if ((roomOwner.getUid() + "").equals(account))
                    return true;
            }
        }
        return false;
    }

    private void setGaussianBlur() {
        Bitmap bitmap = DisplayUtils.getGiftDialogScreenShot((Activity) context);
        ImageLoadUtils.loadImageWithBlurTransformation(context, bitmap, iv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_bottom_gift);
        initView();
        setGaussianBlur();
        initAvatarListAdapter();
        setDialogAttributes();
        onRequestGiftList(null);
        updateData();
        CoreManager.getCore(IPayCore.class).getWalletInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());//fixme 即时刷新开心，临时解决方案
        CoreManager.getCore(IGiftCore.class).requestGiftInfos();//fixme 即时刷新礼物，临时解决方案
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onRequestGiftList(List<GiftInfo> newList) {
        giftInfoList = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(2);
        mysteryGiftInfo = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(3);
        diandianCoinGiftInfo = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(4);
        if (currentTab == 0) {
            initGiftAdapterNew(giftInfoList);
        } else if (currentTab == 1) {
            initGiftAdapterNew(mysteryGiftInfo);
        } else {
            initGiftAdapterNew(diandianCoinGiftInfo);
        }

        //背包总价值
        int allPrice = 0;
        for (int i = 0; i < mysteryGiftInfo.size(); i++) {
            GiftInfo giftInfo = mysteryGiftInfo.get(i);
            int goldPrice = giftInfo.getGoldPrice();
            int userGiftPurseNum = giftInfo.getUserGiftPurseNum();
            int price = goldPrice * userGiftPurseNum;
            allPrice += price;
        }
        allPriceTv.setText("背包总开心：" + allPrice);
    }

    private void updateMoneyData() {
        WalletInfo walletInfo = CoreManager.getCore(IPayCore.class).getCurrentWalletInfo();
        if (walletInfo != null) {
            goldText.setText(getContext().getString(R.string.gold_num_text, walletInfo.getGoldNum()));
        }
    }

    private void updateAvatarData() {
        if (isLoadAvatarList()) {
            if (defaultMicMemberInfo != null) {//如果有默认成员
                int position = micMemberInfos.indexOf(defaultMicMemberInfo);
                if (position >= 0) {
                    avatarListAdapter.setSelectCount(1);
                    micMemberInfos.get(position).setSelect(true);
                    giftManText.setText(micMemberInfos.get(position).getNick());
                } else {
                    Log.e(TAG, "init: default mic member info not in mic member info list");
                }
            } else {//如果没有默认成员
                avatarListAdapter.setSelectCount(1);
                micMemberInfos.get(0).setSelect(true);
                giftManText.setText(micMemberInfos.get(0).getNick());
            }
            avatarListAdapter.setNewData(micMemberInfos);
            mGiftDialogHeader.setVisibility(View.VISIBLE);
            llSinglePeople.setVisibility(View.GONE);
        } else {
            if (!ListUtils.isListEmpty(micMemberInfos)) {
                ImageLoadUtils.loadCircleImage(context, micMemberInfos.get(0).getAvatar(), singleAvatar, R.drawable.ic_default_avatar);
                singleName.setText(micMemberInfos.get(0).getNick());
                llSinglePeople.setVisibility(View.VISIBLE);
            } else {
                llSinglePeople.setVisibility(View.GONE);
            }
            mGiftDialogHeader.setVisibility(View.GONE);
        }
    }

    /**
     * 集中更新数据
     */
    private void updateData() {
        updateMoneyData();
        updateAvatarData();
    }

    private void initEasyPop(int currentP) {
        giftNumberEasyPopup = new EasyPopup(getContext())
                .setContentView(R.layout.dialog_gift_number)
                .setFocusAndOutsideEnable(true)
                .createPopup();

        giftNumberEasyPopup.getView(R.id.number_1).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_10).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_99).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_66).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_188).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_520).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_666).setOnClickListener(this);
        giftNumberEasyPopup.getView(R.id.number_1314).setOnClickListener(this);

        ((TextView) giftNumberEasyPopup.getView(R.id.allNumTv)).setText(currentP == 0 ? "自定义数量" : "ALL 全部");
        giftNumberEasyPopup.getView(R.id.number_all).setOnClickListener(v -> {
            if (currentP == 1) {
                int userGiftPurseNum = getCurrentSelectGift().getUserGiftPurseNum();
                if (userGiftPurseNum > 0) {
                    updateNumber(userGiftPurseNum);
                }
            } else if (currentP == 0) {
                giftNumberEasyPopup.dismiss();
                GiftNumPopWin.showPopFormBottom(findViewById(R.id.fl_top_view), (num -> updateNumber(num)));

            }
        });

    }

    private List<List<GiftInfo>> pagingData(List<GiftInfo> giftInfos) {
        List<List<GiftInfo>> results = new ArrayList<>();
        int count = 0;
        List<GiftInfo> perPageList = new ArrayList<>();//每一页的列表
        for (int i = 0; i < giftInfos.size(); i++) {
            if (count >= 8) {
                results.add(perPageList);
                perPageList = new ArrayList<>();
                count = 0;
            }
            perPageList.add(giftInfos.get(i));
            count++;

            if (i == giftInfos.size() - 1) {
                results.add(perPageList);
            }
        }
        return results;
    }

    private void initGiftAdapterNew(List<GiftInfo> giftInfoList) {
        Log.e("initGiftAdapterNew", JSONObject.toJSONString(giftInfoList));
        oldPageIndex = -1;
        oldPosition = -1;
        mViewPager.removeAllViews();
        List<View> pagerView = new ArrayList<>();
        List<List<GiftInfo>> list = pagingData(giftInfoList);
        adapterNews = new GiftAdapter[list.size()];
        currentPageIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_room_gift_list, mViewPager, false);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, COLUMNS);
            recyclerView.setLayoutManager(gridLayoutManager);
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//禁止动画
            adapterNews[i] = new GiftAdapter(R.layout.list_item_gift, list.get(i));
            adapterNews[i].setOnItemClickListener(this);
            recyclerView.setAdapter(adapterNews[i]);
            pagerView.add(view);
        }

        CommonPagerAdapter pagerAdapter = new CommonPagerAdapter(pagerView);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        setGiftIndicator(giftInfoList);
        setSelectedByPosition(getGiftAdapter(currentPageIndex), 0, false);
    }

    private boolean isRepeat(int position) {
        return currentPageIndex == oldPageIndex && position == oldPosition;
    }

    private GiftAdapter getGiftAdapter(int pageIndex) {
        if (adapterNews != null) {
            if (pageIndex >= 0 && pageIndex < adapterNews.length) {
                return adapterNews[pageIndex];
            } else {
                Log.e(TAG, "get GiftAdapter fail! ArrayIndexOutOfBoundsException!" + pageIndex + "   " + adapterNews.length);
                return null;
            }
        }
        Log.e(TAG, "gift GiftAdapter fail! giftAdapterNew is null" + pageIndex);
        return null;
    }

    private void setSelectedStatus(GiftAdapter adapter, boolean flag, int position, boolean isRefreshUi) {
        if (adapter == null) {
            Log.e(TAG, "Selected gift fail! adapter is null" + false + "   " + position);
            return;
        }
        GiftInfo giftInfo = adapter.getItem(position);
        if (giftInfo != null) {
            giftInfo.setSelected(flag);
            if (isRefreshUi) {
                adapter.notifyItemChanged(position);
            }
        } else {
            Log.e(TAG, "Selected gift fail! giftInfo is null" + position);
        }
    }

    /**
     * 清除所有选中状态
     *
     * @param giftAdapter
     * @param isRefreshAllGift
     */
    private void clearAllSelected(GiftAdapter giftAdapter, boolean isRefreshAllGift) {
        if (isRefreshAllGift) {
            List<GiftInfo> list = giftAdapter.getData();
            if (!ListUtils.isListEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                giftAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setSelectedByPosition(@Nullable GiftAdapter giftAdapter, int position, boolean isRefreshAllGift) {
        if (giftAdapter == null || isRepeat(position)) {
            return;
        }

        if (oldPageIndex != -1 && oldPosition != -1) {
            GiftAdapter oldAdapter = getGiftAdapter(oldPageIndex);
            if (oldAdapter != null) {
                setSelectedStatus(oldAdapter, false, oldPosition, true);
            } else {
                clearAllSelected(giftAdapter, isRefreshAllGift);
            }
        } else {
            clearAllSelected(giftAdapter, isRefreshAllGift);
        }
        setSelectedStatus(giftAdapter, true, position, true);
        oldPageIndex = currentPageIndex;
        oldPosition = position;
    }

    private void setGiftIndicator(List<GiftInfo> giftInfoList) {
        if (!ListUtils.isListEmpty(giftInfoList)) {
            giftIndicator.initIndicator((int) Math.ceil((float) giftInfoList.size() / (ROWS * COLUMNS)));
            curSelectedGift = giftInfoList.get(0);
        }
    }

    private boolean isLoadAvatarList() {
        return micMemberInfos != null && micMemberInfos.size() > 0 && !singlePeople;
    }

    private void initAvatarListAdapter() {
        if (isLoadAvatarList()) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            avatarListRecyclerView.setLayoutManager(mLayoutManager);
            avatarListRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.left = 10;
                    outRect.right = 10;
                }
            });
            avatarListAdapter = new GiftAvatarAdapter();
            avatarListRecyclerView.setAdapter(avatarListAdapter);
            avatarListAdapter.setOnCancelAllMicSelectListener(this);
        }
    }

    private void initView() {
        iv = findViewById(R.id.iv);
        singleName = findViewById(R.id.tv_name);
        quMcBg = findViewById(R.id.view_bg);
        singleAvatar = findViewById(R.id.iv_circle);
        llSinglePeople = findViewById(R.id.ll_single_people);
        mViewPager = findViewById(R.id.view_pager);
        rlGift = findViewById(R.id.rl_gift_container);
        llGiftEmpty = findViewById(R.id.ll_gift_empty);
        rgIndex = findViewById(R.id.rg_gift_indicator);
        mGiftDialogHeader = findViewById(R.id.gift_dialog_header);
        giftIndicator = findViewById(R.id.gift_layout_indicator);
        goldText = findViewById(R.id.text_gold);
        giftNumberText = findViewById(R.id.gift_number_text);
        btnSend = findViewById(R.id.btn_send);
        giftManText = findViewById(R.id.gift_man_text);
        avatarListRecyclerView = findViewById(R.id.avatar_list);
        userInfoText = findViewById(R.id.gift_dialog_info_text);
        allMicHead = findViewById(R.id.rl_all_mic);
        cbAllMic = findViewById(R.id.cb_gift_all);
        btnRecharge = findViewById(R.id.btn_recharge);
        activityGiftRllt = findViewById(R.id.activity_gift_rllt);//活动礼物-玩法介绍
        introduceTv = findViewById(R.id.introduce_tv);//玩法介绍
        allPriceTv = findViewById(R.id.bag_all_price_tv);
        giftDialogToManLayout = findViewById(R.id.gift_dialog_to_man_layout);
        llSend = findViewById(R.id.ll_send);

        introduceTv.setOnClickListener(this);
        rgIndex.setOnCheckedChangeListener(this);
        btnRecharge.setOnClickListener(this);
        giftDialogToManLayout.setOnClickListener(this);
        giftNumberText.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        userInfoText.setOnClickListener(this);
        cbAllMic.setOnClickListener(this);
        allMicHead.setOnClickListener(this);
        userInfoText.setVisibility(isInfoBtn ? View.VISIBLE : View.GONE);
        initEasyPop(0);
    }

    private void setDialogAttributes() {
        FrameLayout bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(
                    (int) context.getResources().getDimension(R.dimen.dialog_gift_height) +
                            (Utils.hasSoftKeys(context) ? Utils.getNavigationBarHeight(context) : 0));
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
        }
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);
        }
    }

    /**
     * 获取当前选中礼物
     *
     * @return
     */
    private GiftInfo getCurrentSelectGift() {
        if (adapterNews == null) {
            return null;
        }
        if (currentPageIndex >= adapterNews.length) {
            return null;
        }
        List<GiftInfo> lists = adapterNews[currentPageIndex].getData();
        if (ListUtils.isListEmpty(lists)) {
            return null;
        }

        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).isSelected()) {
                return lists.get(i);
            }
        }
        return null;
    }

    private void sendGift() {
        curSelectedGift = getCurrentSelectGift();
        if (curSelectedGift == null) {
            SingleToastUtil.showToast("请选择您需要赠送的礼物哦！");
            return;
        }
        if (curSelectedGift.getGiftType() == 3 && curSelectedGift.getUserGiftPurseNum() < giftNumber) {//捡海螺礼物
            SingleToastUtil.showToast("您的背包礼物数量不够哦！");
            return;
        }
        if (giftDialogBtnClickListener != null) {
            if (uid > 0 && singlePeople) {
                giftDialogBtnClickListener.onSendGiftBtnClick(curSelectedGift, uid, giftNumber, currentTab);
            } else if (avatarListAdapter != null && !ListUtils.isListEmpty(avatarListAdapter.getData())) {//其他人
                if (allMicHead != null && allMicHead.getVisibility() == View.VISIBLE && cbAllMic.isSelected()) {//勾选全麦
                    giftDialogBtnClickListener.onSendGiftBtnClick(curSelectedGift, micMemberInfos, giftNumber, currentTab);
                } else {//未勾选全麦
                    boolean noSend = true;
                    for (int i = 0; i < micMemberInfos.size(); i++) {
                        if (micMemberInfos.get(i).isSelect()) {
                            giftDialogBtnClickListener.onSendGiftBtnClick(curSelectedGift, micMemberInfos.get(i).getUid(), giftNumber, currentTab);
                            noSend = false;
                        }
                    }
                    if (noSend) {
                        if (context instanceof BaseActivity) {
                            ((BaseActivity) context).toast("请选择送礼成员");
                        } else if (context instanceof BaseMvpActivity) {
                            ((BaseMvpActivity) context).toast("请选择送礼成员");
                        }
                        dismiss();
                    }
                }
            } else {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).toast("暂无成员在麦上");
                } else if (context instanceof BaseMvpActivity) {
                    ((BaseMvpActivity) context).toast("暂无成员在麦上");
                }
                dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:
                if (giftDialogBtnClickListener != null) {
                    giftDialogBtnClickListener.onRechargeBtnClick();
                }
                dismiss();
                break;
            case R.id.btn_send:
                boolean activityGift = (boolean) SpUtils.get(context, ACTIVITY_GIFT, false);
                DialogManager dialogManager = new DialogManager(context);
                if (currentTab == 2 && !activityGift) {
                    dialogManager.showOkCancelDialog("赠送人气票不会增加送礼方财富值和收礼方魅力值，只增加收礼方的人气值。", true, () -> {
                        SpUtils.put(context, ACTIVITY_GIFT, true);
                        dialogManager.dismissDialog();
                        sendGift();
                    });
                } else {
                    sendGift();
                }

                break;
            case R.id.number_1:
                updateNumber(1);
                break;
            case R.id.number_10:
                updateNumber(10);
                break;
            case R.id.number_99:
                updateNumber(99);
                break;
            case R.id.number_66:
                updateNumber(66);
                break;
            case R.id.number_188:
                updateNumber(188);
                break;
            case R.id.number_520:
                updateNumber(520);
                break;
            case R.id.number_666:
                updateNumber(666);
                break;
            case R.id.number_1314:
                updateNumber(1314);
                break;
            case R.id.gift_number_text:
                showGiftNumberEasyPopup();
                giftNumberText.setCompoundDrawablesRelative(null, null, TextViewDrawableUtils.getCompoundDrawables(context, R.drawable.ic_downward_arrow), null);
                break;

            case R.id.gift_dialog_to_man_layout:
                showGiftAvatarListEasyPopup();
                break;

            case R.id.gift_dialog_info_text:
                displayUserInfo();
                break;
            case R.id.rl_all_mic:
                allMicHead.setEnabled(false);
                boolean isSelect = cbAllMic.isSelected();
                cbAllMic.setSelected(!isSelect);
                quMcBg.setBackgroundResource(!isSelect ? R.drawable.bg_room_call_user : R.drawable.bg_room_call_user_off);
                if (avatarListAdapter != null && !ListUtils.isListEmpty(avatarListAdapter.getData())) {
                    for (int i = 0; i < avatarListAdapter.getData().size(); i++) {
                        avatarListAdapter.getData().get(i).setSelect(!isSelect);
                        avatarListAdapter.notifyDataSetChanged();
                    }
                    avatarListAdapter.setAllSelect(!isSelect);
                }
                allMicHead.setEnabled(true);
                break;
            case R.id.introduce_tv:
                ActivityGiftPlayIntroduceActivity.start(context);
                break;
            default:
        }
    }

    private void updateNumber(int number) {
        giftNumber = number;
        giftNumberText.setText(giftNumber + "");
        giftNumberEasyPopup.dismiss();
    }

    private void displayUserInfo() {
        if (ListUtils.isListEmpty(micMemberInfos)) {
            SingleToastUtil.showToast("暂无数据!");
            return;
        }
        boolean flag = false;
        for (int i = 0; i < micMemberInfos.size(); i++) {
            if (micMemberInfos.get(i).isSelect()) {
                showUserDialog(micMemberInfos.get(i).getUid());
                flag = true;
                break;
            }
        }
        if (flag) {
            dismiss();
        } else {
            if (uid > 0) {
                showUserDialog(uid);
                dismiss();
            } else {
                SingleToastUtil.showToast("请选择成员");
            }
        }

    }

    private void showUserDialog(long uid) {
        NewUserInfoDialog.showUserDialog(context, uid);
    }

    private void showGiftAvatarListEasyPopup() {
        avatarListRecyclerView.setVisibility(View.VISIBLE);
        if (allMicHead != null) {
            if (singlePeople) {
                allMicHead.setVisibility(View.GONE);
            } else {
                if (avatarListAdapter != null && !ListUtils.isListEmpty(avatarListAdapter.getData())) {
                    if (avatarListAdapter.getData().size() == 1)
                        allMicHead.setVisibility(View.GONE);
                } else {
                    allMicHead.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void showGiftNumberEasyPopup() {
        giftNumberEasyPopup.showAtAnchorView(giftNumberText, VerticalGravity.ABOVE, HorizontalGravity.CENTER, 0, ConvertUtils.dp2px(-15));
        giftNumberEasyPopup.setOnDismissListener(() -> giftNumberText.setCompoundDrawablesRelative(null, null,
                TextViewDrawableUtils.getCompoundDrawables(context, R.drawable.ic_gift_number_top_arrow), null));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }

    /**
     * 设置用户金额
     *
     * @param price
     */
    private void setUserAmount(int res, double price) {
        Drawable drawable = TextViewDrawableUtils.getCompoundDrawables(context, res);
        goldText.setCompoundDrawablesRelative(drawable, null, null, null);
        goldText.setText(getContext().getString(R.string.gold_num_text, price));
    }

    @Override
    public void onChange(boolean isAllMic, int selectCount) {
        if (isAllMic) {
            cbAllMic.setSelected(true);
            quMcBg.setBackgroundResource(R.drawable.bg_room_call_user);
        } else {
            cbAllMic.setSelected(false);
            quMcBg.setBackgroundResource(R.drawable.bg_room_call_user_off);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == null) {
            return;
        }
        if (adapter instanceof GiftAdapter) {
            GiftAdapter current = (GiftAdapter) adapter;
            setSelectedByPosition(current, position, true);
        }
    }

    private void showDataUI() {
        if (rlGift.getVisibility() == View.GONE) {
            rlGift.setVisibility(View.VISIBLE);
        }
        if (llGiftEmpty.getVisibility() == View.VISIBLE) {
            llGiftEmpty.setVisibility(View.GONE);
        }
    }

    private void showNoDataUI() {
        if (llGiftEmpty.getVisibility() == View.GONE) {
            llGiftEmpty.setVisibility(View.VISIBLE);
        }
        if (rlGift.getVisibility() == View.VISIBLE) {
            rlGift.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setSelectedStatus(getGiftAdapter(oldPageIndex), false, oldPosition, false);//在切换tab之前，将原来tab中选中状态清除。
        double price;
        switch (checkedId) {
            case R.id.rb_gift_tab:
                activityGiftRllt.setVisibility(View.INVISIBLE);
                btnRecharge.setVisibility(View.VISIBLE);
                goldText.setVisibility(View.VISIBLE);
                llSend.setVisibility(View.VISIBLE);
                price = CoreManager.getCore(IPayCore.class).getCurrentWalletInfo().getGoldNum();
                changedTab(giftInfoList, 0, R.mipmap.ic_ranking_gold, price);
                initEasyPop(0);
                break;
            case R.id.rb_gift_pack_tab:
                activityGiftRllt.setVisibility(View.INVISIBLE);
                btnRecharge.setVisibility(View.VISIBLE);
                goldText.setVisibility(View.VISIBLE);
                llSend.setVisibility(View.VISIBLE);
                price = CoreManager.getCore(IPayCore.class).getCurrentWalletInfo().getGoldNum();
                changedTab(mysteryGiftInfo, 1, R.mipmap.ic_ranking_gold, price);
                initEasyPop(1);
                break;
            case R.id.rb_gift_dian_dian_coin_tab:
                activityGiftRllt.setVisibility(View.VISIBLE);
                btnRecharge.setVisibility(View.INVISIBLE);
                goldText.setVisibility(View.INVISIBLE);
                llSend.setVisibility(View.VISIBLE);
                price = CoreManager.getCore(IPayCore.class).getCurrentWalletInfo().getGoldNum();
                changedTab(diandianCoinGiftInfo, 2, R.mipmap.ic_ranking_gold, price);
                //点点币
//                DianDianCoinInfo dianDianCoinInfo = CoreManager.getCore(IPayCore.class).getDianDianCoinInfo();
//                if (dianDianCoinInfo != null) {
//                    price = dianDianCoinInfo.getMcoinNum();
//                    changedTab(diandianCoinGiftInfo, 2, R.mipmap.ic_ranking_gold, price);
//                } else {
//                    CoreManager.getCore(IPayCore.class).loadDianDianCoinInfos();//重新去拿一次数据
//                }
                break;
        }
    }

    private void changedTab(List<GiftInfo> list, int index, int res, double price) {
        if (currentTab == index)
            return;
        currentTab = index;
        setUserAmount(res, price);
        if (ListUtils.isListEmpty(list)) {
            showNoDataUI();
        } else {
            initGiftAdapterNew(list);
            showDataUI();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        giftIndicator.setSelectedPage(position);
        currentPageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnGiftDialogBtnClickListener {
        void onRechargeBtnClick();

        void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP);

        void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            setSelectedStatus(getGiftAdapter(oldPageIndex), false, oldPosition, false);//退出之前恢复未选中状态
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新礼物列表
     *
     * @param giftInfoList
     */
    private void refreshGiftList(List<GiftInfo> giftInfoList) {
        List<List<GiftInfo>> lists = pagingData(giftInfoList);
        if (adapterNews != null) {
            GiftAdapter giftAdapter = adapterNews[currentPageIndex];
            if (giftAdapter != null) {
                if (currentPageIndex < lists.size()) {//当背包礼物全部消耗完毕，这里的lists数组会变为0，因此需要添加次判断
                    giftAdapter.setNewData(lists.get(currentPageIndex));
                    Log.i(TAG, "gift refresh succeed!");
                } else {
                    Log.e(TAG, "gift refresh fail! ArrayIndexOutOfBoundsException!");
                }
            } else {
                Log.e(TAG, "gift refresh fail! giftAdapter is null");
            }
        } else {
            Log.e(TAG, "gift refresh fail! adapterNews is null");
        }
    }

    @CoreEvent(coreClientClass = ICommonClient.class)
    public void onRecieveNeedRecharge() {
        DialogManager dialogManager = new DialogManager(context);
        String title;
        if (currentP == 2) {
            title = "点点币不足，可通过任务获取";
        } else {
            title = "余额不足，是否充值";
        }
        String okLabel;
        if (currentP == 2) {
            okLabel = "做任务";
        } else {
            okLabel = "确定";
        }
        dialogManager.showOkCancelDialog(title, okLabel, "取消", true, () -> {
            if (currentP == 2) {
                TaskCenterActivity.start(context);
            } else {
                WalletActivity.start(context);
            }
        });
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onDianDianCoinInfoUpdate(DianDianCoinInfo dianDianCoinInfo) {
        if (dianDianCoinInfo != null) {
            setUserAmount(R.mipmap.ic_ranking_gold, dianDianCoinInfo.getMcoinNum());
        }
    }


    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onWalletInfoUpdate(WalletInfo walletInfo) {
        if (walletInfo != null) {
            setUserAmount(R.mipmap.ic_ranking_gold, walletInfo.getGoldNum());
        }
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onGetDianDianCoinInfo(DianDianCoinInfo dianDianCoinInfo) {
        if (dianDianCoinInfo != null) {
            //点点币
            setUserAmount(R.mipmap.ic_ranking_gold, dianDianCoinInfo.getMcoinNum());
        }
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onGetWalletInfo(WalletInfo walletInfo) {
        if (walletInfo != null) {
            setUserAmount(R.mipmap.ic_ranking_gold, walletInfo.getGoldNum());
        }
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void refreshFreeGift() {
        if (currentTab == 0) {
            giftInfoList = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(2);
            refreshGiftList(giftInfoList);
        } else if (currentTab == 1) {//更新礼物数量
            List<GiftInfo> gifts = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(3);
            if (ListUtils.isListEmpty(gifts)) {
                showNoDataUI();
            } else {
                if (mysteryGiftInfo != null && gifts.size() < mysteryGiftInfo.size()) {
                    int newPosition = 0;
                    if (oldPosition == 0) {
                        newPosition = 1;
                    }
                    setSelectedByPosition(getGiftAdapter(currentPageIndex), newPosition, true);
                }
            }
            mysteryGiftInfo = gifts;
            refreshGiftList(mysteryGiftInfo);
        } else if (currentTab == 2) {
            List<GiftInfo> gifts = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(4);
            if (ListUtils.isListEmpty(gifts)) {
                showNoDataUI();
            } else {
                if (diandianCoinGiftInfo != null && gifts.size() < diandianCoinGiftInfo.size()) {
                    setSelectedByPosition(getGiftAdapter(currentPageIndex), 0, true);
                }
            }
            diandianCoinGiftInfo = gifts;
            refreshGiftList(diandianCoinGiftInfo);
//            diandianCoinGiftInfo = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(5);
//            refreshGiftList(diandianCoinGiftInfo);
        }
    }


    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onGiftPastDue() {
        ((BaseActivity) getContext()).toast("该礼物已过期");
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onGiftMysteryNotEnough() {
        SingleToastUtil.showToast("您的神秘礼物数量不够哦！");
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onSendGiftFail(int code, String message) {
        SingleToastUtil.showToast(message);
    }


}
