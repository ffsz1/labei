package com.vslk.lbgx.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vslk.lbgx.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseYAdapter<T> extends RecyclerView.Adapter<BaseYAdapter.BaseViewHolder> {

    public Context mContext;
    public ArrayList<T> mListData;
    //子项点击事件

    public BaseYAdapter(Context context) {
        this.mContext = context;
        mListData = new ArrayList<T>();
    }

    public ArrayList<T> getListData() {
        return mListData;
    }

    /**
     * 删除列表�?��数据并刷�?
     */
    public void deleteAll() {
        mListData.clear();
    }

    /**
     * 获取指定未知的数�?
     */
    public T getItem(int position) {
        if (null == mListData) {
            return null;
        } else {
            return mListData.get(position);
        }
    }

    /**
     * 删除某项数据
     */
    public void deleteItemOf(int position) {
        mListData.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteItem(T t){
        mListData.remove(t);
        notifyDataSetChanged();
    }


    /**
     * 添加数据并刷新
     */
    public void addItem(Collection<? extends T> t) {
        if (null == t) {
            return;
        }
        if (t instanceof ArrayList) {
            mListData.addAll(t);
        } else {
            mListData.add((T) t);
        }
        notifyDataSetChanged();
    }

    public void add(T t) {
        mListData.add(t);
        notifyDataSetChanged();
    }

    public void add(int position,T t) {
        mListData.add(position,t);
        notifyDataSetChanged();
    }

    public void set(int position,T t) {
        mListData.set(position,t);
        notifyItemChanged(position);
    }

    public boolean replace(T newT,T t){
        if(mListData.contains(t)){
            int position = mListData.indexOf(t);
            mListData.set(position,newT);
            return true;
        }else{
            return false;
        }
    }

    private List<T> deepList;

    public List<T> deepCopy(){
        deepList = ArrayUtils.deepCopy(mListData);
        return deepList;
    }

    @Override
    public int getItemCount() {
        return (mListData == null) ? 0 : mListData.size();
    }


    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View arg0) {
            super(arg0);

        }
    }

}
