package com.tongdaxing.xchat_core.utils;

import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/6/14.
 */

public class ArrayUtils<T> {

    private static final String TAG = "ArrayUtils";

    public static <T> List<T> deepCopy(List<T> src) {
        List<T> dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            MLog.error(TAG,"deep copy io exception!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            MLog.error(TAG,"deep copy classNotFoundException!");
        }
        return dest;
    }

    public static <T> boolean replace(List<T> mListData, T newT, T t) {
        if (mListData.contains(t)) {
            int position = mListData.indexOf(t);
            mListData.set(position, newT);
            return true;
        } else {
            return false;
        }
    }
}
