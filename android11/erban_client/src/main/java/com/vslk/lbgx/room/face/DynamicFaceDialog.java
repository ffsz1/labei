package com.vslk.lbgx.room.face;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vslk.lbgx.room.face.adapter.DynamicFaceAdapter;
import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.face.FaceInfo;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_core.room.face.IFaceCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xiaoyu
 * @date 2017/12/8
 */

public class DynamicFaceDialog extends BottomSheetDialog
        implements DynamicFaceAdapter.OnFaceItemClickListener {
    private static final String TAG = "DynamicFaceDialog";
    private Context context;
    private static int USER_SELECTED_POS = -1;


    public DynamicFaceDialog(Context context) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setContentView(R.layout.dialog_bottom_face);
        CoreManager.addClient(this);
        init(findViewById(R.id.rl_dynamic_face_dialog_root));
        FrameLayout bottomSheet = (FrameLayout) findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(
                    (int) context.getResources().getDimension(R.dimen.dialog_face_height));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    private void init(View root) {
        List<FaceInfo> faceInfos = CoreManager.getCore(IFaceCore.class).getFaceInfos();
        if (faceInfos == null || faceInfos.size() == 0) {
            Toast.makeText(context, "表情准备中...", Toast.LENGTH_SHORT).show();
            return;
        }

        ViewPager vpMenu = root.findViewById(R.id.viewpager);

        List<View> pagerView = new ArrayList<>();
        List<List<FaceInfo>> lists = resolveData(faceInfos);
        int size = lists.size();
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_face_grid_view, vpMenu, false);
            GridView gridView = view.findViewById(R.id.gridView);
            DynamicFaceAdapter adapter = new DynamicFaceAdapter(context, lists.get(i));
            adapter.setOnFaceItemClickListener(this);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pagerView.add(view);
        }

        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(pagerView);
        vpMenu.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();

        // 准备indicator
        final LinearLayout llIndicator = root.findViewById(R.id.ll_dynamic_face_dialog_indicator);
        int indicatorDotLength = Utils.dip2px(context, 6);
        int indicatorDotSpace = Utils.dip2px(context, 5);
        for (int i = 0; i < size; i++) {
            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorDotLength, indicatorDotLength);
            if (i != 0) {
                params.leftMargin = indicatorDotSpace;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.bg_dynamic_face_dialog_selector);
            llIndicator.addView(view);
        }
        // 选中当前页的indicator
        if (USER_SELECTED_POS == -1) {
            USER_SELECTED_POS = 0;
        } else if (USER_SELECTED_POS + 1 > size) {
            USER_SELECTED_POS = 0;
        }
        selectIndicator(llIndicator, USER_SELECTED_POS);
        vpMenu.setCurrentItem(USER_SELECTED_POS);
        vpMenu.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectIndicator(llIndicator, position);
                USER_SELECTED_POS = position;
            }
        });
    }

    @CoreEvent(coreClientClass = IFaceCoreClient.class)
    public void onUnzipSuccess() {
        init(findViewById(R.id.rl_dynamic_face_dialog_root));
    }

    private void selectIndicator(ViewGroup viewGroup, int position) {
        int size = viewGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            viewGroup.getChildAt(i).setSelected(i == position);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }

    private static final int IMAGE_COUNT_PER_PAGE = 15;
    private static final int TYPE_NORMAL_FACE = 0;
    private static final int TYPE_LUCKY_FACE = 1;

    private List<List<FaceInfo>> resolveData(List<FaceInfo> faceInfos) {
        int size = faceInfos.size();
        // 最少要有两页
        List<List<FaceInfo>> tmp = new ArrayList<>();
        tmp.add(new ArrayList<FaceInfo>());
        tmp.add(new ArrayList<FaceInfo>());
        List<List<FaceInfo>> results = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int resultCount = faceInfos.get(i).getResultCount();
            if (resultCount > 0) {
                // 运气表情
                tmp.get(TYPE_LUCKY_FACE).add(faceInfos.get(i));
            } else {
                // 普通表情
                tmp.get(TYPE_NORMAL_FACE).add(faceInfos.get(i));
            }
        }
        // 分页
        results.add(new ArrayList<FaceInfo>());
        for (int i = 0; i < size; i++) {
            // 需要多页来显示
            if (results.get(results.size() - 1).size() == IMAGE_COUNT_PER_PAGE) {
                results.add(new ArrayList<FaceInfo>());
            }
            if (tmp.get(TYPE_NORMAL_FACE).size() > 0) {
                results.get(results.size() - 1).add(tmp.get(TYPE_NORMAL_FACE).remove(0));
            } else if (tmp.get(TYPE_LUCKY_FACE).size() > 0) {
                results.get(results.size() - 1).add(tmp.get(TYPE_LUCKY_FACE).remove(0));
            }
        }
        return results;
    }

    @Override
    public void onFaceItemClick(FaceInfo faceInfo) {
        if (faceInfo != null && !CoreManager.getCore(IFaceCore.class).isShowingFace()) {
            CoreManager.getCore(IFaceCore.class).sendFace(faceInfo);
            dismiss();
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private List<View> mLists;

        MyViewPagerAdapter(List<View> array) {
            this.mLists = array;
        }

        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mLists.get(position));
            return mLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

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
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
