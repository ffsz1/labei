package com.vslk.lbgx.ui.home.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.home.BannerInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.home.IHomeCoreClient;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_core.room.IRoomCore;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.home.HomePresenter;
import com.vslk.lbgx.presenter.home.IHomeView;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.find.activity.SquareActivity;
import com.vslk.lbgx.ui.home.GenderSelectWindow;
import com.vslk.lbgx.ui.home.GradientsTool;
import com.vslk.lbgx.ui.home.TabLayoutAnimUtils;
import com.vslk.lbgx.ui.home.adpater.BannerAdapter;
import com.vslk.lbgx.ui.home.adpater.HomeHotHeaderAdapter;
import com.vslk.lbgx.ui.home.adpater.HomeVpAdapter;
import com.vslk.lbgx.ui.home.fragment.hot.HomeHTFragment;
import com.vslk.lbgx.ui.home.fragment.hot.HomePeiPeiFragment;
import com.vslk.lbgx.ui.home.view.HomeHotHeaderView;
import com.vslk.lbgx.ui.home.view.HomeHotSquareRollMF;
import com.vslk.lbgx.ui.home.view.HomeHotSquareRollMarqueeView;
import com.vslk.lbgx.ui.home.view.SquareRollInfo;
import com.vslk.lbgx.ui.rank.activity.RankingListActivity;
import com.vslk.lbgx.ui.search.SearchActivity;
import com.vslk.lbgx.ui.sign.dialog.SignInDialog;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.widget.Banner;
import com.vslk.lbgx.ui.widget.OnPageChangeListener;
import com.vslk.lbgx.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页
 *
 * @zwk
 */
@CreatePresenter(HomePresenter.class)
public class HomeFragment extends BaseMvpFragment<IHomeView, HomePresenter> implements IHomeView, View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener
        /* ,HomeMagicIndicatorAdapter.OnItemSelectListener*/ {
    public static final String TAG = "HomeFragment";
    //    @BindView(R.id.fl_lucky)
//    FrameLayout flLucky;
    @BindView(R.id.iv_ranking)
    ImageView ivRanking;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    Unbinder unbinder;
    @BindView(R.id.iv_home_room)
    ImageView ivHomeRoom;
    @BindView(R.id.img_rank_rich)
    ImageView imgRankRich;
    @BindView(R.id.img_rank_ml)
    ImageView imgRankMl;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.img_home_search)
    ImageView imgHomeSearch;
    @BindView(R.id.img_home_my_room)
    ImageView imgHomeMyRoom;
    private ImageView ivSearch;
    private Banner bRecommendBanner;
    //滑动分类
    private ViewPager vpHome;
    private ImageView ivSignInEntrance;
    @BindView(R.id.ll_top_bar)
    LinearLayout llTopBar;
    private Banner topBanner;
    private HomeHTFragment homeHTFragment;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    //    private HomeAttentionFragment attention;
    private TabLayoutAnimUtils tabLayoutAnimUtils;
    private HomeVpAdapter vpAdapter;
    private AppBarLayout mAppBar;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onFindViews() {
        bRecommendBanner = mView.findViewById(R.id.b_recommend_banner);
        ivSignInEntrance = mView.findViewById(R.id.iv_sign_in_entrance);
        vpHome = mView.findViewById(R.id.vp_home_content);
        ivSearch = mView.findViewById(R.id.iv_home_search);
        topBanner = mView.findViewById(R.id.top_banner);
        mAppBar = mView.findViewById(R.id.app_bar);
        initHead();
        initViewPage();
    }

    private void initViewPage() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
//        attention = new HomeAttentionFragment();
        homeHTFragment = new HomeHTFragment();
        homeHTFragment.setUpHeadListener(new HomeHTFragment.IUpHeadListener() {
            @Override
            public void onTopList(List<HomeRoom> agreeRecommendRooms) {
                setHotNewData(agreeRecommendRooms);
            }

            @Override
            public void onFriends(List<PublicChatRoomAttachment> list) {
                setSquareRoll(list);
            }

            @Override
            public void upTabView() {
                typeGroup.check(oldRbId);
                switchRBUI(oldRbId);
            }
        });
    }

    private TabLayout.OnTabSelectedListener mTabSelectedListener;


    public void initTabLayout(List<TabInfo> tabs) {
        if (fragments.size() == 0) {
            fragments.add(homeHTFragment);
//            fragments.add(attention);
//            titles.add("关注");
            titles.add("热门");
            if (!ListUtils.isListEmpty(tabs)) {
                Fragment fragment;
                for (int i = 0; i < tabs.size(); i++) {
                    fragment = new HomeMeetYouFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tabId", tabs.get(i).getId());
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                    titles.add(tabs.get(i).getName());
                }
            }
            vpAdapter = new HomeVpAdapter(fragments, titles, getChildFragmentManager());
            vpHome.setAdapter(vpAdapter);
            tablayout.setupWithViewPager(vpHome);
            vpHome.setOffscreenPageLimit(fragments.size());
            tabLayoutAnimUtils = new TabLayoutAnimUtils(getContext(), tablayout);
            isShowHotHead(true);
            vpHome.addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    isShowHotHead(position == 0);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });
            mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (titles.size() > 0) {
                        tabLayoutAnimUtils.changeTabSelect(tab, false);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    if (titles.size() > 0) tabLayoutAnimUtils.changeTabNormal(tab);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            };
            tablayout.addOnTabSelectedListener(mTabSelectedListener);
            tabLayoutAnimUtils.setTitleList(titles);
            tabLayoutAnimUtils.changeTabIndicatorWidth(tablayout, 15);
        }
    }

    @Override
    public void onSetListener() {
//        flLucky.setOnClickListener(this);
        ivRanking.setOnClickListener(this);
        ivSignInEntrance.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivHomeRoom.setOnClickListener(this);
        imgRankRich.setOnClickListener(this);
        imgRankMl.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        imgHomeMyRoom.setOnClickListener(this);
        imgHomeSearch.setOnClickListener(this);
    }


    public void setTopBannerData(List<BannerInfo> response) {
        if (!ListUtils.isListEmpty(response)) {
            topBanner.setVisibility(View.VISIBLE);
            BannerAdapter bannerAdapter = new BannerAdapter(topBanner, getContext(), response);
            bannerAdapter.setOnItemClickListener(bannerInfo -> {
                CoreManager.notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_AUTO_JUMP, bannerInfo);
            });
            topBanner.setAnimationDurtion(500);
            topBanner.setPlayDelay(3000);
            topBanner.setHintView(new ColorPointHintView(getContext(), Color.parseColor("#FE5D9D"), Color.parseColor("#88ffffff")));//更换圆点颜色
            topBanner.setAdapter(bannerAdapter);
        } else {
            topBanner.setVisibility(View.GONE);
        }
    }


    //------------------head-----------------------------------------
    private RadioGroup typeGroup;
    private ImageView openIv;
    private int oldRbId = RB_ITEM_ID;
    public static final int RB_ITEM_ID = 11020;
    private HomeHotSquareRollMarqueeView<SquareRollInfo> homeHotSquareRollMarqueeView;
    private HomeHotSquareRollMF homeHotSquareRollMF;
    private LinearLayout rlSquareRoll;
    private HomeHotHeaderAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvRecommendRanking;

    private LinearLayout linGender;
    private ImageView imgGender;
    private ImageView imgGenderFlag;

    private void initHead() {
        openIv = mView.findViewById(R.id.open_switch_first_iv);
        openIv.setOnClickListener(this);


        typeGroup = mView.findViewById(R.id.ll_group);
        rlSquareRoll = mView.findViewById(R.id.rl_square_roll);
        homeHotSquareRollMarqueeView = mView.findViewById(R.id.home_hot_square_roll_marquee_view);
        tvRecommendRanking = mView.findViewById(R.id.tv_recommend_ranking);
        recyclerView = mView.findViewById(R.id.recycler_view);
        linGender = mView.findViewById(R.id.lin_gender);
        imgGender = mView.findViewById(R.id.img_gender);
        imgGenderFlag = mView.findViewById(R.id.img_gender_flag);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new HomeHotHeaderAdapter(getContext());
        mAdapter.setOnItemClickListener(((adapter, view, position) -> {
            if (adapter != null && !ListUtils.isListEmpty(adapter.getData())) {
                HomeRoom homeRoom = mAdapter.getData().get(position);
                if (homeRoom == null) {
                    return;
                }
                if (homeRoom.getType() == 1 && TextUtils.isEmpty(homeRoom.getTitle())) {
                    CommonWebViewActivity.start(getContext(), WebUrl.HOME_RECOMMEND_GREEN_CONVERTION_URL);
                } else if (homeRoom.getType() == 2 && TextUtils.isEmpty(homeRoom.getTitle())) {
                    CommonWebViewActivity.start(getContext(), WebUrl.HOME_RECOMMEND_PLACEHOLDER_URL);
                } else {
                    AVRoomActivity.start(getContext(), homeRoom.getUid());
                }
            }
        }));
        recyclerView.setAdapter(mAdapter);
        rlSquareRoll.setOnClickListener(v -> SquareActivity.start(getContext()));
        setTypeName();

        linGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgGenderFlag.setImageResource(R.drawable.ic_triangle_up);
                GenderSelectWindow genderSelectWindow = new GenderSelectWindow(getActivity(), gender -> {
                    EventBus.getDefault().post(new HomePeiPeiFragment.GenderFilter(gender));
                    imgGender.setImageResource(BizUtils.getGenderIcon(gender));
                });
                genderSelectWindow.setOnDismissListener(() -> {
                    imgGenderFlag.setImageResource(R.drawable.ic_triangle_down);
                });
                genderSelectWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int xOffset = -(genderSelectWindow.getContentView().getMeasuredWidth() - linGender.getWidth());
                genderSelectWindow.showAsDropDown(linGender, xOffset - 80, 16);
            }
        });
    }


    private void isShowHotHead(boolean isShow) {
        mView.findViewById(R.id.hot_head).setVisibility(isShow ? View.VISIBLE : View.GONE);
        typeGroup.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (typeGroup.getCheckedRadioButtonId() == 1 + RB_ITEM_ID) {
            linGender.setVisibility(isShow ? View.VISIBLE : View.GONE);
        } else {
            linGender.setVisibility(View.GONE);
        }
    }


    @SuppressLint("ResourceType")
    public void setTypeName() {
        ArrayList<String> names = new ArrayList<>();
        names.add("热门房间");
        names.add("优质陪陪");
        names.add("萌新列表");
        typeGroup.removeAllViews();
        if (names.size() == 0) return;
        for (int i = 0; i < names.size(); i++) {
            RadioButton tItem = (RadioButton) LayoutInflater.from(this.getContext()).inflate(R.layout.item_hot_type, null);
            String name = names.get(i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = ConvertUtils.dp2px(10);
            tItem.setLayoutParams(params);
            tItem.setText(name);
            tItem.setId(i + RB_ITEM_ID);
            typeGroup.addView(tItem);
        }
        typeGroup.check(RB_ITEM_ID);
        switchRBUI(RB_ITEM_ID);
        typeGroup.setOnCheckedChangeListener((group, id) -> {
            switchRBUI(id);
            if (homeHTFragment != null) homeHTFragment.switchTab(id);
        });
    }


    private void switchRBUI(int id) {
        RadioButton rb = typeGroup.findViewById(id);
        RadioButton oldRb = typeGroup.findViewById(oldRbId);
        GradientsTool.setGradients(rb, "#000000", "#000000");
        if (oldRbId != id) GradientsTool.setGradients(oldRb, "#FF999999", "#FF999999");
        oldRbId = id;

        if (id == (1 + RB_ITEM_ID)) {
            linGender.setVisibility(View.VISIBLE);
        } else {
            linGender.setVisibility(View.GONE);
        }
    }

    public void setSquareRoll(List<PublicChatRoomAttachment> list) {
        homeHotSquareRollMF = new HomeHotSquareRollMF(getContext());
        homeHotSquareRollMF.setData(HomeHotHeaderView.convertInfo(list));
        homeHotSquareRollMarqueeView.setMarqueeFactory(homeHotSquareRollMF);
        homeHotSquareRollMarqueeView.startFlipping();
    }

    //设置数据
    public void setHotNewData(List<HomeRoom> roomList) {
        if (!ListUtils.isListEmpty(roomList)) {
            recyclerView.setVisibility(View.GONE);
//            tvRecommendRanking.setVisibility(View.VISIBLE);
            tvRecommendRanking.setVisibility(View.GONE);
            mAdapter.setNewData(addPlaceholderData(roomList));
        } else {
            mAdapter.setNewData(null);
            tvRecommendRanking.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private List<HomeRoom> addPlaceholderData(List<HomeRoom> roomList) {
        if (roomList.size() % 3 == 1) {
            HomeRoom homeRoom1 = new HomeRoom();
            homeRoom1.setType(1);
            homeRoom1.setAvatar(WebUrl.HOME_RECOMMEND_GREEN_CONVERTION);
            HomeRoom homeRoom2 = new HomeRoom();
            homeRoom2.setType(2);
            homeRoom2.setAvatar(WebUrl.HOME_RECOMMEND_PLACEHOLDER);
            roomList.add(homeRoom1);
            roomList.add(homeRoom2);
        } else if (roomList.size() % 3 == 2) {
            HomeRoom homeRoom = new HomeRoom();
            homeRoom.setType(2);
            homeRoom.setAvatar(WebUrl.HOME_RECOMMEND_PLACEHOLDER);
            roomList.add(homeRoom);
        }
        return roomList;
    }

    //------------------------------------------------------------------------------------------


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        showLoading();
        loadData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    protected void onLazyLoadData() {
        loadData();
    }

    private void loadData() {
        getMvpPresenter().getHomeRecommendBanner();
        getMvpPresenter().getTopBanner();
        getMvpPresenter().getHomeTabList();
    }

//    @Override
//    public void onItemSelect(int position) {
//        currentPosition = position;
//        vpHome.setCurrentItem(position);
//    }

    @Override
    public void getHomeTabListSuccess(List<TabInfo> tabs) {
        hideStatus();
        initTabLayout(tabs);
    }

    @Override
    public void getHomeTabListFail(String error) {
        hideStatus();
        initTabLayout(null);
    }

    @Override
    public void getHomeBannerSuccess(List<BannerInfo> response) {
        setTopBannerData(response);
    }

    @Override
    public void getRecommendBannerFailure() {
        if (bRecommendBanner != null) {
            bRecommendBanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void getRecommendBannerSuccess(List<BannerInfo> response) {
        if (mContext != null && !ListUtils.isListEmpty(response)) {
            if (bRecommendBanner != null) {
//                bRecommendBanner.setVisibility(View.VISIBLE);
                bRecommendBanner.setHintView(new ColorPointHintView(getContext(), Color.parseColor("#FF80B3"), Color.parseColor("#999999")));//隐藏圆点
                BannerAdapter bannerAdapter = new BannerAdapter(bRecommendBanner, mContext, response);
                bannerAdapter.setOnItemClickListener(bannerInfo -> {
                    if (bannerInfo.getSkipUri().equals("XBDSign")) {//fixme 临时解决方案，跳转到签到对话框。
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            SignInDialog signInDialog = new SignInDialog();
                            signInDialog.show(fragmentManager, "");
                        }
                    } else {
                        autoJump(bannerInfo.getSkipType(), bannerInfo.getSkipUri(), bannerInfo.getSkipUri(), null);
                    }
                });
                bRecommendBanner.setAdapter(bannerAdapter);
            }
        } else {
            if (bRecommendBanner != null) {
                bRecommendBanner.setVisibility(View.GONE);
            }
        }
    }

    @CoreEvent(coreClientClass = IHomeCoreClient.class)
    public void onRecyclerViewListener(int value) {
    }

    public static int alpha1;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.fl_lucky:
                CommonWebViewActivity.start(getActivity(), WebUrl.LUCKY_DRAW);
                break;*/
            case R.id.iv_ranking:
                RankingListActivity.start(getActivity(), 1);
                break;
            case R.id.iv_sign_in_entrance:
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    SignInDialog signInDialog = new SignInDialog();
                    signInDialog.show(fragmentManager, "");
                }
                break;
            case R.id.tv_search:
            case R.id.iv_home_search:
            case R.id.img_home_search:
                SearchActivity.start(mContext);
                break;
            case R.id.open_switch_first_iv://筛选性别
                View view = View.inflate(mContext, R.layout.top_bar_popwin_layout, null);
                final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);//参数为1.View 2.宽度 3.高度
                popupWindow.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow
                popupWindow.showAsDropDown(openIv);//设置popupWindow显示,并且告诉它显示在那个View下面

                view.findViewById(R.id.all_sex_tv);
                view.findViewById(R.id.woman_sex_tv);
                view.findViewById(R.id.man_sex_tv);
                break;
            case R.id.iv_home_room:
            case R.id.img_home_my_room:
                getDialogManager().showProgressDialog(getActivity(), "请稍后...");
                CoreManager.getCore(IRoomCore.class).requestRoomInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid(), 0);
                break;
            case R.id.img_rank_ml:
                RankingListActivity.start(getActivity(), 1);
                break;
            case R.id.img_rank_rich:
                RankingListActivity.start(getActivity(), 0);
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
