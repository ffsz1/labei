package com.vslk.lbgx.room.widget.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.avroom.adapter.RoomConsumeListAdapter;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.vslk.lbgx.utils.NumberFormatUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tongdaxing.xchat_core.UriProvider.JAVA_WEB_URL;

/**
 * Created by MadisonRong on 13/01/2018.
 */

public class BigListDataDialog extends BaseDialogFragment implements BaseQuickAdapter.OnItemClickListener,
        RadioGroup.OnCheckedChangeListener {

    public static final String TYPE_ONLINE_USER = "ONLINE_USER";
    public static final String TYPE_CONTRIBUTION = "ROOM_CONTRIBUTION";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_TYPE = "KEY_TYPE";

    @BindView(R.id.user_rank)
    RadioGroup userRank;
    @BindView(R.id.rg_rich_charme_top)
    RadioGroup richCharmeTop;
    @BindView(R.id.bu_pay_tab)
    TextView buPayTab;
    @BindView(R.id.bu_imcome_tab)
    TextView buImcomeTab;
    @BindView(R.id.rv_pay_income_list)
    RecyclerView rvPayIncomeList;
    Unbinder unbinderRich;
    @BindView(R.id.iv_first_out)
    ImageView firstIv;


    private int type = 1;
    private int dataType = 1;
    private long roomId;
    private RoomConsumeListAdapter roomConsumeListAdapter;
    private View noDataView, richView;
    private ImageView goldAvatar, silverAvatar, copperAvatar;
    private TextView goldNick, silverNick, copperNick, goldNum, silverNum, copperNum;

    private RelativeLayout rlGold, rlSilver, rlCopper;
    private RadioButton rbTabDay, rbTabWeek, rbTabAll;
    private ImageView ivTopBg;
    private ImageView ivJt;
    private RadioGroup radioGroup;

    List<RoomConsumeInfo> roomConsumeInfoList;

    public static BigListDataDialog newOnlineUserListInstance(Context context) {
        return newInstance(context.getString(R.string.online_user_text), TYPE_ONLINE_USER);
    }

    public static BigListDataDialog newContributionListInstance(Context context) {
        return newInstance(context.getString(R.string.contribution_list_text), TYPE_CONTRIBUTION);
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, null);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    public static BigListDataDialog newInstance(String title, String type) {
        BigListDataDialog listDataDialog = new BigListDataDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_TYPE, type);
        listDataDialog.setArguments(bundle);
        return listDataDialog;
    }

    public BigListDataDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        richView = inflater.inflate(R.layout.dialog_big_list_data_new_rich,
                window.findViewById(android.R.id.content), false);
//
//        charmeView = inflater.inflate(R.layout.dialog_big_list_data_new_charme,
//                window.findViewById(android.R.id.content), false);


        unbinderRich = ButterKnife.bind(this, richView);
//        unbinderCharme = ButterKnife.bind(this, charmeView);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.ErbanCommonWindowAnimationStyle);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);

        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            roomId = roomInfo.getUid();
        }
        noDataView = richView.findViewById(R.id.tv_no_data);
        initView();
        initRv();
        return richView;
    }

    private void initRv() {
        rvPayIncomeList.setLayoutManager(new LinearLayoutManager(getContext()));
        roomConsumeListAdapter = new RoomConsumeListAdapter(getContext());
        roomConsumeListAdapter.setOnItemClickListener(this);
        rvPayIncomeList.setAdapter(roomConsumeListAdapter);
        getData();

    }

    private void initView() {
        buPayTab.setSelected(true);

        buPayTab.setOnClickListener(v -> typeChange(1));
        buImcomeTab.setOnClickListener(v -> typeChange(2));

        userRank.setOnCheckedChangeListener(this);
        richCharmeTop.setOnCheckedChangeListener(this);

        goldAvatar = richView.findViewById(R.id.iv_gold_avatar);
        silverAvatar = richView.findViewById(R.id.iv_silver_avatar);
        copperAvatar = richView.findViewById(R.id.iv_copper_avatar);

        goldNick = richView.findViewById(R.id.tv_gold_nick);
        silverNick = richView.findViewById(R.id.tv_silver_nick);
        copperNick = richView.findViewById(R.id.tv_copper_nick);

        goldNum = richView.findViewById(R.id.tv_gold_num);
        silverNum = richView.findViewById(R.id.tv_silver_num);
        copperNum = richView.findViewById(R.id.tv_copper_num);

        radioGroup = richView.findViewById(R.id.user_rank);

        rbTabDay = richView.findViewById(R.id.rb_tab_day);
        rbTabWeek = richView.findViewById(R.id.rb_tab_week);
        rbTabAll = richView.findViewById(R.id.rb_tab_all);

        ivTopBg = richView.findViewById(R.id.iv_top_bg);
        ivJt = richView.findViewById(R.id.iv_jt);

        rlGold = richView.findViewById(R.id.rl_gold);
        rlSilver = richView.findViewById(R.id.rl_silver);
        rlCopper = richView.findViewById(R.id.rl_copper);
        initClick();

    }

    private void initClick() {

        rlGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roomConsumeInfoList != null && roomConsumeInfoList.size() > 0) {
                    RoomConsumeInfo roomConsumeInfo = roomConsumeInfoList.get(0);
                    showUserDialog(roomConsumeInfo.getCtrbUid());
                }
            }
        });
        rlSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomConsumeInfoList != null && roomConsumeInfoList.size() > 1) {
                    RoomConsumeInfo roomConsumeInfo = roomConsumeInfoList.get(1);
                    showUserDialog(roomConsumeInfo.getCtrbUid());
                }
            }
        });
        rlCopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomConsumeInfoList != null && roomConsumeInfoList.size() > 2) {
                    RoomConsumeInfo roomConsumeInfo = roomConsumeInfoList.get(2);
                    showUserDialog(roomConsumeInfo.getCtrbUid());
                }
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {

            //日榜周榜总榜
            case R.id.rb_tab_day:
                dataTypeChange(1);
                break;
            case R.id.rb_tab_week:
                dataTypeChange(2);
                break;
            case R.id.rb_tab_all:
                dataTypeChange(3);
                break;

            //富豪榜心动榜
            case R.id.bu_pay_tab:
                typeChange(1);
                break;
            case R.id.bu_imcome_tab:
                typeChange(2);
                break;
            default:
                break;
        }
    }

    private void dataTypeChange(int i) {

        dataType = i;
        getData();

    }

    private void typeChange(int i) {
        if (roomConsumeListAdapter != null) {
            roomConsumeListAdapter.rankType = i;
        }
        type = i;
        userRank.check(R.id.rb_tab_day);
        viewBGChange(i);
        dataTypeChange(dataType);
    }

    private void getData() {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(roomId));
        param.put("dataType", String.valueOf(dataType));
        param.put("type", String.valueOf(type));
        String url = JAVA_WEB_URL + "/roomctrb/queryByType";
        OkHttpManager.getInstance().getRequest(url, param, new OkHttpManager.MyCallBack<ServiceResult<List<RoomConsumeInfo>>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<List<RoomConsumeInfo>> response) {
                if (response.isSuccess()) {
                    if (response.getData() != null) {
                        noDataView.setVisibility(response.getData().size() > 0 ? View.GONE : View.VISIBLE);
                        List<RoomConsumeInfo> data = response.getData();
                        roomConsumeInfoList = data;
//                        Log.e("onResponse ", "onResponse: " + data.size());
                        //截取第四条以后的数据展示
                        if (data.size() >= 4) {
                            roomConsumeListAdapter.setNewData(data.subList(3, data.size()));
                        } else {
                            roomConsumeListAdapter.setNewData(null);
                        }
                        setThirdData(response.getData());
                    }
                }
            }
        });


    }

    private void setThirdData(List<RoomConsumeInfo> list) {

        if (list.size() <= 0) {
            goldNick.setText("？？？");
            goldNum.setText(NumberFormatUtils.getBigDecimal(0));

            if (type == 1)
                goldAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);
            else
                goldAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);

            silverNick.setText("？？？");
            silverNum.setText(NumberFormatUtils.getBigDecimal(0));
            if (type == 1)
                silverAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);
            else
                silverAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);

            copperNick.setText("？？？");
            copperNum.setText(NumberFormatUtils.getBigDecimal(0));
            if (type == 1)
                copperAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);
            else
                copperAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);

            return;
        }
        if (list.size() == 1) {
            RoomConsumeInfo gold = list.get(0);
            goldNick.setText(gold.getNick());
            goldNum.setText(NumberFormatUtils.getBigDecimal(gold.getSumGold()));
            ImageLoadUtils.loadAvatar(getContext(), gold.getAvatar(), goldAvatar, true);

            silverNick.setText("？？？");
            silverNum.setText(NumberFormatUtils.getBigDecimal(0));
            if (type == 1)
                silverAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);
            else
                silverAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);

            copperNick.setText("？？？");
            copperNum.setText(NumberFormatUtils.getBigDecimal(0));
            if (type == 1)
                copperAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);
            else
                copperAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);


        } else if (list.size() == 2) {
            RoomConsumeInfo gold = list.get(0);
            goldNick.setText(gold.getNick());
            goldNum.setText(NumberFormatUtils.getBigDecimal(gold.getSumGold()));
            ImageLoadUtils.loadAvatar(getContext(), gold.getAvatar(), goldAvatar, true);
            RoomConsumeInfo silver = list.get(1);
            silverNick.setText(silver.getNick());
            silverNum.setText(NumberFormatUtils.getBigDecimal(silver.getSumGold()));
            ImageLoadUtils.loadAvatar(getContext(), silver.getAvatar(), silverAvatar, true);

            copperNick.setText("？？？");
            copperNum.setText(NumberFormatUtils.getBigDecimal(0));
            if (type == 1)
                copperAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);
            else
                copperAvatar.setImageResource(R.mipmap.icon_rangking_rich_no);

        } else {
            RoomConsumeInfo gold = list.get(0);
            goldNick.setText(gold.getNick());
            goldNum.setText(NumberFormatUtils.getBigDecimal(gold.getSumGold()));
            ImageLoadUtils.loadAvatar(getContext(), gold.getAvatar(), goldAvatar, true);


            RoomConsumeInfo silver = list.get(1);
            silverNick.setText(silver.getNick());
            silverNum.setText(NumberFormatUtils.getBigDecimal(silver.getSumGold()));
            ImageLoadUtils.loadAvatar(getContext(), silver.getAvatar(), silverAvatar, true);

            RoomConsumeInfo copper = list.get(2);
            copperNick.setText(copper.getNick());
            copperNum.setText(NumberFormatUtils.getBigDecimal(copper.getSumGold()));
            ImageLoadUtils.loadAvatar(getContext(), copper.getAvatar(), copperAvatar, true);

        }
    }

    //根据富豪榜和心动榜改变背景色
    private void viewBGChange(int changeType) {

        if (changeType == 1) {
            ivTopBg.setBackground(getResources().getDrawable(R.mipmap.img_rankinglist_rich_top_bg));
            ivJt.setImageResource(R.mipmap.bg_room_jt_cf);

            firstIv.setBackgroundResource(R.mipmap.caifu_first);
            rbTabDay.setTextColor(getResources().getColorStateList(R.color.rb_rich_rangking_selector));
            rbTabWeek.setTextColor(getResources().getColorStateList(R.color.rb_rich_rangking_selector));
            rbTabWeek.setTextColor(getResources().getColorStateList(R.color.rb_rich_rangking_selector));

            rbTabDay.setBackground(getResources().getDrawable(R.drawable.rangking_rich_tab_background));
            rbTabWeek.setBackground(getResources().getDrawable(R.drawable.rangking_rich_tab_background));
            rbTabAll.setBackground(getResources().getDrawable(R.drawable.rangking_rich_tab_background));

            goldAvatar.setBackground(getResources().getDrawable(R.mipmap.icon_rangking_rich_no));
            silverAvatar.setBackground(getResources().getDrawable(R.mipmap.icon_rangking_rich_no));
            copperAvatar.setBackground(getResources().getDrawable(R.mipmap.icon_rangking_rich_no));

            setSelectorColor(rbTabDay, getResources().getColor(R.color.rangking_rich_color), getResources().getColor(R.color.white));
            setSelectorColor(rbTabWeek, getResources().getColor(R.color.rangking_rich_color), getResources().getColor(R.color.white));
            setSelectorColor(rbTabAll, getResources().getColor(R.color.rangking_rich_color), getResources().getColor(R.color.white));

            setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_ranking_gold), goldNum);
            setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_ranking_gold), silverNum);
            setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_ranking_gold), copperNum);


        } else if (changeType == 2) {

            ivTopBg.setBackground(getResources().getDrawable(R.mipmap.img_rankinglist_charme_top_bg));

            firstIv.setBackgroundResource(R.mipmap.meili_first);

            rbTabDay.setTextColor(getResources().getColorStateList(R.color.rb_meili_rangking_selector));
            rbTabWeek.setTextColor(getResources().getColorStateList(R.color.rb_meili_rangking_selector));
            rbTabWeek.setTextColor(getResources().getColorStateList(R.color.rb_meili_rangking_selector));

            rbTabDay.setBackground(getResources().getDrawable(R.drawable.rangking_charme_tab_background));
            rbTabWeek.setBackground(getResources().getDrawable(R.drawable.rangking_charme_tab_background));
            rbTabAll.setBackground(getResources().getDrawable(R.drawable.rangking_charme_tab_background));

            ivJt.setImageResource(R.mipmap.bg_room_jt_ml);
            goldAvatar.setBackground(getResources().getDrawable(R.mipmap.icon_rangking_rich_no));
            silverAvatar.setBackground(getResources().getDrawable(R.mipmap.icon_rangking_rich_no));
            copperAvatar.setBackground(getResources().getDrawable(R.mipmap.icon_rangking_rich_no));


            setSelectorColor(rbTabDay,
                    getResources().getColor(R.color.rangking_charme_color),
                    getResources().getColor(R.color.white));
            setSelectorColor(rbTabWeek,
                    getResources().getColor(R.color.rangking_charme_color),
                    getResources().getColor(R.color.white));
            setSelectorColor(rbTabAll,
                    getResources().getColor(R.color.rangking_charme_color),
                    getResources().getColor(R.color.white));

            setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_charme_love), goldNum);
            setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_charme_love), silverNum);
            setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_charme_love), copperNum);


        }
    }

    /**
     * @param @param drw
     * @return void
     * @desc 设置左边图标
     */
    /**
     * @paramleft 图片在左边
     * @paramtop 图片在上边
     * @paramright 图片在右边
     * @parambottom 图片在下边
     */
    public void setCompoundDrawables(Drawable drawable, TextView view) {

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * @param radioButton 控件
     * @param nnormal     正常时的颜色值
     * @param checked     选中时的颜色值
     */
    public void setSelectorColor(RadioButton radioButton, int nnormal, int checked) {
        int[] colors = new int[]{nnormal, checked, nnormal};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_checked};
        states[2] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        radioButton.setTextColor(colorStateList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        List<RoomConsumeInfo> list = roomConsumeListAdapter.getData();
        if (ListUtils.isListEmpty(list)) {
            return;
        }


        RoomConsumeInfo roomConsumeInfo = list.get(i);
        showUserDialog(roomConsumeInfo.getCtrbUid());
    }

    private SelectOptionAction selectOptionAction;

    public void setSelectOptionAction(SelectOptionAction selectOptionAction) {
        this.selectOptionAction = selectOptionAction;
    }


    private void showUserDialog(long account) {
        NewUserInfoDialog.showUserDialog(getContext(), account);
    }


    public interface SelectOptionAction {

        void optionClick();

        void onDataResponse();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinderRich.unbind();
//        unbinderCharme.unbind();
    }
}
