package com.vslk.lbgx.room.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.avroom.fragment.OnlineUserFragment;
import com.vslk.lbgx.room.avroom.fragment.RoomContributeFragment;
import com.vslk.lbgx.room.egg.fragment.MyPoundEggRewordFragment;
import com.tongdaxing.erban.R;

/**
 * Created by MadisonRong on 13/01/2018.
 */

public class ListDataDialog extends BaseDialogFragment implements OnlineUserFragment.OnLineUserCallback {

    public static final String TYPE_ONLINE_USER = "ONLINE_USER";
    public static final String TYPE_CONTRIBUTION = "ROOM_CONTRIBUTION";
    public static final String TYPE_MIC_USER = "ROOM_MIC_USER";
    public static final String TYPE_MY_POUND_EGG_RECORD = "TYPE_MY_POUND_EGG_RECORD";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_TYPE = "KEY_TYPE";

    private String title;
    private String type;
    private OnlineUserFragment.OnlineItemClick onlineItemClick;

    public void setOnlineItemClick(OnlineUserFragment.OnlineItemClick onlineItemClick) {
        this.onlineItemClick = onlineItemClick;
    }

    public static ListDataDialog newOnlineUserListInstance(Context context) {
        return newInstance(context.getString(R.string.online_user_text), TYPE_ONLINE_USER);
    }

    public static ListDataDialog newContributionListInstance(Context context) {
        return newInstance(context.getString(R.string.contribution_list_text), TYPE_CONTRIBUTION);
    }


    public static ListDataDialog newMicUserInstance(Context context) {
        return newInstance(context.getString(R.string.mic_user_list_text), TYPE_MIC_USER);
    }

    public static ListDataDialog newPoundEggRecordInstance(Context context) {
        return newInstance(context.getString(R.string.my_pound_egg_reward), TYPE_MY_POUND_EGG_RECORD);
    }

    public static ListDataDialog newInstance(String title, String type) {
        ListDataDialog listDataDialog = new ListDataDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_TYPE, type);
        listDataDialog.setArguments(bundle);
        return listDataDialog;
    }

    public ListDataDialog() {
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String titleArg = arguments.getString(KEY_TITLE);
            this.title = titleArg != null ? titleArg : "";
            String typeArg = arguments.getString(KEY_TYPE);
            this.type = typeArg != null ? typeArg : "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_list_data, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        TextView titleTextView = view.findViewById(R.id.tv_list_data_title);
        titleTextView.setText(this.title);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getActivity().isFinishing()) {
            switch (this.type) {
                case TYPE_ONLINE_USER:
                    OnlineUserFragment onlineUserFragment = new OnlineUserFragment();
                    onlineUserFragment.setOnLineUserCallback(this);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fl_container, onlineUserFragment, ListDataDialog.TYPE_ONLINE_USER)
                            .commitAllowingStateLoss();
                    onlineUserFragment.firstLoad();
                    break;
                case TYPE_MIC_USER:
                    OnlineUserFragment micUserFragment = new OnlineUserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isMic", true);
                    micUserFragment.setArguments(bundle);
                    if (onlineItemClick != null)
                        micUserFragment.setOnlineItemClick(onlineItemClick);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fl_container, micUserFragment, ListDataDialog.TYPE_ONLINE_USER)
                            .commitAllowingStateLoss();
                    micUserFragment.firstLoad();
                    break;

                case TYPE_CONTRIBUTION:
                    RoomContributeFragment roomContributeFragment = new RoomContributeFragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fl_container, roomContributeFragment, ListDataDialog.TYPE_CONTRIBUTION)
                            .commitAllowingStateLoss();
                    roomContributeFragment.loadData();
                    break;
                case TYPE_MY_POUND_EGG_RECORD:
                    MyPoundEggRewordFragment eggRecord = new MyPoundEggRewordFragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fl_container, eggRecord, ListDataDialog.TYPE_MY_POUND_EGG_RECORD)
                            .commitAllowingStateLoss();
                    break;
            }
        }
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, this.type);
    }

    @Override
    public void onDismiss() {
        dismiss();
    }
}
