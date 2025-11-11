package com.vslk.lbgx.ui.widget.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;

/**
 * Created by MadisonRong on 13/01/2018.
 */

public class NewUserDialog extends BaseDialogFragment implements View.OnClickListener{
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_IMG= "KEY_IMG";
    public static final String KEY_ROOM_ID= "KEY_ROOM_ID";
    private String title;
    private String imgUrl;
    private long roomId;

    public static NewUserDialog newInstance(String title, String imgUrl,long roomId) {
        NewUserDialog listDataDialog = new NewUserDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_IMG, imgUrl);
        bundle.putLong(KEY_ROOM_ID,roomId);
        listDataDialog.setArguments(bundle);
        return listDataDialog;
    }

    public NewUserDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String titleArg = arguments.getString(KEY_TITLE);
            this.title = titleArg != null ? titleArg : "";
            String typeArg = arguments.getString(KEY_IMG);
            this.imgUrl = typeArg != null ? typeArg : "";
            this.roomId = arguments.getLong(KEY_ROOM_ID,0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_new_user, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        LinearLayout llContainer = view.findViewById(R.id.ll_new_user);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) llContainer.getLayoutParams();
        rl.height = (ScreenUtil.getScreenWidth(getContext()) - ConvertUtils.dp2px(100))*680/533;
        llContainer.setLayoutParams(rl);
        view.findViewById(R.id.iv_close_dialog).setOnClickListener(this);
        view.findViewById(R.id.btn_new_user_go).setOnClickListener(this);
        ImageView ivAvatar = view.findViewById(R.id.iv_new_user_avatar);
        ImageLoadUtils.loadCircleImage(getContext(),imgUrl,ivAvatar,R.drawable.ic_default_avatar);
        Button btn = view.findViewById(R.id.btn_new_user_go);
        btn.setText(title);
        btn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_dialog:
                dismiss();
                break;
            case R.id.btn_new_user_go:
                AVRoomActivity.start(getContext(),roomId);
                dismiss();
                break;
        }
    }

}
