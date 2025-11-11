package com.vslk.lbgx.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.netease.nim.uikit.StatusBarUtil;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.room.IRoomCore;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 搜索界面
 *
 * @author chenran
 * @date 2017/10/3
 */
public class SearchActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private TextView cancel;
    private EditText searchEdit;
    private Handler handler = new SearchHandler();
    private TagFlowLayout flowLayout;
    private LayoutInflater mInflater;
    private TagAdapter<String> adapter;
    private Json searchCacheJson;
    private View searchHistoryBg;
    private View clearSearchHistory;

    private static class SearchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            CoreManager.getCore(IRoomCore.class).roomSearch(s);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        addView(StatusBarUtil.StatusBarLightMode(this));
        initView();
        initSearchHistory();

    }

    private void markSearchContent(String s) {
        if (TextUtils.isEmpty(s))
            return;

        if (searchCacheJson == null) {
            String searchCache = (String) SpUtils.get(this, SpEvent.search_history, "");
            searchCacheJson = Json.parse(searchCache);
        }
        String[] strings = searchCacheJson.key_names();
        if (strings.length > 9) {
            searchCacheJson.remove(strings[0]);
        }

        searchCacheJson.set(s, "");
        SpUtils.put(this, SpEvent.search_history, searchCacheJson.toString());

        refreshSearchHistory();
    }

    private void clearSearchHistory() {
        SpUtils.put(this, SpEvent.search_history, "");
        searchCacheJson = new Json();
        refreshSearchHistory();
    }


    private void initSearchHistory() {
        String searchCache = (String) SpUtils.get(this, SpEvent.search_history, "");
        searchCacheJson = Json.parse(searchCache);
        refreshSearchHistory();

    }

    private void refreshSearchHistory() {
        List<String> jsonList = new ArrayList<>();
        for (String key : searchCacheJson.key_names()) {
            jsonList.add(key);
        }


        if (jsonList.size() > 0) {
            Collections.reverse(jsonList);

            searchHistoryBg.setVisibility(View.VISIBLE);
        } else {
            searchHistoryBg.setVisibility(View.GONE);
            return;
        }


        mInflater = LayoutInflater.from(this);


        adapter = new TagAdapter<String>(jsonList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_flowlayout_menu_search,
                        flowLayout, false);
                tv.setText(s);
                return tv;
            }

        };
        flowLayout.setAdapter(adapter);
        flowLayout.setOnTagClickListener((view, position, parent) -> {
            searchEdit.setText(jsonList.get(position));
            searchEdit.setSelection(searchEdit.getText().length());
            return false;
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        flowLayout = (TagFlowLayout) findViewById(R.id.flow_search);
        cancel = (TextView) findViewById(R.id.cancel);
        searchHistoryBg = findViewById(R.id.ll_search_history_bg);
        clearSearchHistory = findViewById(R.id.iv_search_clear_history);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        searchEdit.addTextChangedListener(textWatcher);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(this);
        recyclerView.setAdapter(searchAdapter);
        cancel.setOnClickListener(v -> finish());
        clearSearchHistory.setOnClickListener(v -> clearSearchHistory());

        searchAdapter.baseAdapterItemListener = index -> markSearchContent(searchEdit.getText().toString().trim());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            LogUtil.i("afterTextChanged", s.toString());
            handler.removeMessages(0);
            searchAdapter.setNewData(null);
            searchAdapter.notifyDataSetChanged();
            if (StringUtil.isEmpty(s.toString())) {
                showNoData();
            } else {
                showLoading();
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = s.toString();
                handler.sendMessageDelayed(msg, 800);
            }
        }
    };

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onSearchRoom(List<HomeRoom> homeRooms) {
        if (homeRooms != null && homeRooms.size() > 0) {
            hideStatus();
            searchAdapter.setNewData(homeRooms);
        } else {
            showNoData();
        }
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onSearchRoomFail(String msg) {
        hideStatus();
        if (NetworkUtils.isNetworkAvailable(this)) {
            showNoData();
        } else {
            showNetworkErr();
        }
    }

    @Override
    public void showNoData() {
        showNoData(getString(R.string.dearch_no_data));
    }


    @Override
    protected void onDestroy() {
        handler.removeMessages(0);
        searchEdit.removeTextChangedListener(textWatcher);
        super.onDestroy();

    }
}
