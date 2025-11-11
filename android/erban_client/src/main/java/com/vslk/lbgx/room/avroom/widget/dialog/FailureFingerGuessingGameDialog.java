package com.vslk.lbgx.room.avroom.widget.dialog;

import android.widget.ImageView;

import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.tongdaxing.erban.R;

/**
 * Function:
 * Author: Edward on 2019/6/20
 */
public class FailureFingerGuessingGameDialog extends BaseDialog {
    @Override
    public void convertView(ViewHolder viewHolder) {
        ImageView ivClose = viewHolder.getView(R.id.iv_close);
        ImageView ivStartFingerGuessingGame = viewHolder.getView(R.id.iv_start_finger_guessing_game);
        ivClose.setOnClickListener(v -> dismiss());
        ivStartFingerGuessingGame.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                StartFingerGuessingGameDialog dialog = new StartFingerGuessingGameDialog();
                dialog.show(getFragmentManager(), "");
                dismiss();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_finger_guessing_game_failure;
    }
}
