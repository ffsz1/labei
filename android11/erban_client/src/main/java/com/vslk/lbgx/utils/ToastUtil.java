package com.vslk.lbgx.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tongdaxing.erban.R;

public class ToastUtil {
    public static ToastUtil instance;

    public static ToastUtil getInstance() {
        if (instance == null){
            synchronized (ToastUtil.class){
                if (instance == null){
                    instance = new ToastUtil();
                }
            }
        }
        return instance;
    }

    public void showTaskMsg(Context context, String content) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        View view = LayoutInflater.from(context).inflate(R.layout.toast_task_remind, null); //加載layout下的布局
        TextView text = view.findViewById(R.id.tv_task_toast_txt);
        text.setText(content); //toast内容
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }
}
