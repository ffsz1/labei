package com.vslk.lbgx.room.face.anim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.vslk.lbgx.room.match.RoomMatchUtil;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.xchat_core.room.face.FaceInfo;
import com.tongdaxing.xchat_core.room.face.FaceReceiveInfo;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoyu
 * @date 2017/11/30
 */

public class AnimFaceFactory {
    private static final String TAG = "AnimFaceFactory";

    private AnimFaceFactory() {
    }

    public static AnimationDrawable get(FaceReceiveInfo faceReceiveInfo, Context context, int width, int height) {
        long time = System.currentTimeMillis();
        AnimationDrawable drawable = new AnimationDrawable();
        FaceInfo faceInfo = CoreManager.getCore(IFaceCore.class).findFaceInfoById(faceReceiveInfo.getFaceId());
        // 如果找不到对应的表情,直接返回null
        if (faceInfo == null) {
            return null;
        }
        int startIndex = faceInfo.getAnimationIndexStart();
        int endIndex = faceInfo.getAnimationIndexEnd();
        int duration = (int) ((faceInfo.getAnimationDuration() + 0.F) / (endIndex - startIndex));
        int repeatCount = faceInfo.getRepeatCount();
        while (repeatCount > 0) {
            for (int i = startIndex; i <= endIndex; i++) {
                // 增加每一帧到drawable中
                OneFaceDrawable face = new OneFaceDrawable(context, faceInfo.getFacePath(i), width, height);
                drawable.addFrame(face, duration);
            }
            repeatCount--;
        }
        // 如果是普通表情,则直接返回
        if (faceInfo.getResultCount() <= 0) {
            drawable.addFrame(new ColorDrawable(Color.TRANSPARENT), 10);
            return drawable;
        }
        // 如果有结果,增加结果帧
        List<Integer> resultIndexes = faceReceiveInfo.getResultIndexes();
        duration = faceInfo.getResultDuration();
        // 找出所有结果图片的路径
        List<String> images = new ArrayList<>();
        for (int i = 0; i < resultIndexes.size(); i++) {
            LogUtil.e(TAG, faceInfo.getFacePath(resultIndexes.get(i)));
            images.add(faceInfo.getFacePath(resultIndexes.get(i)));
        }
        // 根据显示类型,产生对应的类型的结果图片
        int displayType = faceInfo.getDisplayType();
        if (displayType == FaceInfo.DISPLAY_TYPE_ONE_PIC || images.size() == 1) {
            OneFaceDrawable face = new OneFaceDrawable(context, images.get(0), width, height);
            drawable.addFrame(face, duration);
        } else if (displayType == FaceInfo.DISPLAY_TYPE_FLOW) {
            FlowFaceDrawable flowFaceDrawable = new FlowFaceDrawable(context, images, width, height);
            flowFaceDrawable.setBounds(0, 0, width, height);
            drawable.addFrame(flowFaceDrawable, duration);
        } else if (displayType == FaceInfo.DISPLAY_TYPE_OVERLAY) {
            OverlayFaceDrawable overlayFaceDrawable = new OverlayFaceDrawable(context, images, width, height);
            overlayFaceDrawable.setBounds(0, 0, width, height);
            drawable.addFrame(overlayFaceDrawable, duration);
        } else {
            // 未知类型,不显示动画
            return null;
        }

        //隐藏drawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        colorDrawable.setBounds(0, 0, width, height);
        drawable.addFrame(new ColorDrawable(Color.TRANSPARENT), 10);
        Log.e(TAG, "time consumed: " + (System.currentTimeMillis() - time));
        return drawable;
    }

    public static AnimationDrawable get(int[] attr, Context context, int width, int height,boolean isShow,int duration) {
       if (attr == null || attr.length <= 0)
           return null;

        AnimationDrawable drawable = new AnimationDrawable();
        // 如果找不到对应的表情,直接返回null
        // 找出所有结果图片的路径
        int[] images = new int[attr.length];
        for (int i = 0; i < attr.length; i++) {
            images[i] = RoomMatchUtil.getMatchResId(isShow?attr[i]:0);
        }
//
//        TriangleFaceDrawable flowFaceDrawable1 = new TriangleFaceDrawable(context, images, (int) (width*0.5), (int)(height*0.5));
//        flowFaceDrawable1.setBounds(0, 0, (int) (width*0.5), (int)(height*0.5));
//        drawable.addFrame(flowFaceDrawable1, 100);
//
//        TriangleFaceDrawable flowFaceDrawable2 = new TriangleFaceDrawable(context, images, (int) (width*0.8), (int)(height*0.8));
//        flowFaceDrawable2.setBounds(0, 0, (int) (width*0.8), (int)(height*0.8));
//        drawable.addFrame(flowFaceDrawable2, 100);
//
//        TriangleFaceDrawable flowFaceDrawable3 = new TriangleFaceDrawable(context, images, width, height);
//        flowFaceDrawable3.setBounds(0, 0, width, height);
//        drawable.addFrame(flowFaceDrawable3, 100);
//
//        TriangleFaceDrawable flowFaceDrawable = new TriangleFaceDrawable(context, images, (int) (width*1.1), (int)(height*1.1));
//        flowFaceDrawable.setBounds(0, 0, (int) (width*1.1), (int)(height*1.1));
//        drawable.addFrame(flowFaceDrawable, 100);
//
//        TriangleFaceDrawable flowFaceDrawable4 = new TriangleFaceDrawable(context, images, (int) (width*1.2), (int)(height*1.2));
//        flowFaceDrawable4.setBounds(0, 0, (int) (width*1.2), (int)(height*1.2));
//        drawable.addFrame(flowFaceDrawable4, 100);
//
        // 根据显示类型,产生对应的类型的结果图片
        TriangleFaceDrawable flowFaceDrawable5 = new TriangleFaceDrawable(context, images, width, height);
        flowFaceDrawable5.setBounds(0, 0, width, height);
        drawable.addFrame(flowFaceDrawable5, duration);
        //隐藏drawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        colorDrawable.setBounds(0, 0, width, height);
        drawable.addFrame(new ColorDrawable(Color.TRANSPARENT), 10);
        return drawable;
    }
}
