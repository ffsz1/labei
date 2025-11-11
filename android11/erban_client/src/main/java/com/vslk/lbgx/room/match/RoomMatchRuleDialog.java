package com.vslk.lbgx.room.match;

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

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.tongdaxing.erban.R;

/**
 * 龙珠 — 速配功能
 * Created by zwk on 13/08/2018.
 */
public class RoomMatchRuleDialog extends BaseDialogFragment implements View.OnClickListener {
    private Button btnRule;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.layout_room_match_rule, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initClickListener();
    }

    private void initView(View view) {
        btnRule = view.findViewById(R.id.btn_match_rule_close);
    }

    private void initClickListener() {
        btnRule.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_match_rule_close:
                dismiss();
                break;
        }
    }

}
