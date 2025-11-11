package com.vslk.lbgx.ui.home;

import android.app.Activity;

public class TransparentActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
