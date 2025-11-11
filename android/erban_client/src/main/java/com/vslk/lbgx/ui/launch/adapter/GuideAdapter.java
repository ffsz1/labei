package com.vslk.lbgx.ui.launch.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class GuideAdapter extends PagerAdapter{
    private List<ImageView> mData=new ArrayList<ImageView>();
    public GuideAdapter(Context context){
        initData(context);
    }

    //取得适配器装配的数据
    public List<ImageView>  getData(){
        return mData;
    }
    //添加视图项
    public void addItem(ImageView imageView){
        mData.add(imageView);
    }

    //初始化视图项的数据
    private void initData(Context context) {
        ImageView imageView=new ImageView(context);
//	   imageView.setImageResource(resId);//设置ImageView的前景图片
//        imageView.setBackgroundResource(R.drawable.guide_one);//设置背景
        mData.add(imageView);
        imageView=new ImageView(context);
//        imageView.setBackgroundResource(R.drawable.guide_two);//设置背景
        mData.add(imageView);
        imageView=new ImageView(context);
//        imageView.setBackgroundResource(R.drawable.guide_three);//设置背景
        mData.add(imageView);

    }
    //取得视图项的数量
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
    //销毁视图项
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //从父控件中移除子控件
        container.removeView((View)object);
    }
    //初始化视图项
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //容器（ViewPager--它是ViewGroup的子类）
        container.addView(mData.get(position));
        return mData.get(position);
    }
}
