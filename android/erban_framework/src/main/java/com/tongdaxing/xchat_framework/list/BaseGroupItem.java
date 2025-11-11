package com.tongdaxing.xchat_framework.list;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijun on 2014/11/7.
 */
public abstract class BaseGroupItem extends BaseListItem implements GroupItem {

    protected OnIndicatorClickListener mIndicatorListener;
    protected int mGroupPos, mChildPos;
    protected List<ListItem> mChildItems = new ArrayList<ListItem>(1);

    public BaseGroupItem(Context mContext) {
        super(mContext);
    }

    public BaseGroupItem(Context mContext, int viewType) {
        super(mContext, viewType);
    }

    public void setMoreItem(ListItem item) {
        mChildItems.add(0, item);
    }

    @Override
    public List<ListItem> getChildItems() {
        return mChildItems;
    }

    public static <E, T> List<E> createLineItems(Context context, Class<E> lineItemClazz, List<T> list, Class<T> voClazz, Integer viewType, Integer column) {
        List<E> items = new ArrayList<E>();
        try {
            if (list != null) {
                int size = list.size();
                int rows = size % column == 0 ? size / column : size / column + 1;
                for (int i = 0; i < rows; i++) {
                    T[] albums;
                    if (i != rows - 1) {
                        albums = (T[]) Array.newInstance(voClazz, column);
                        for (int index = 0; index < column; ++index) {
                            albums[index] = list.get(i * column + index);
                        }
                    } else {
                        albums = (T[]) Array.newInstance(voClazz, column);
                        int j = 0;
                        while (size > i * column + j) {
                            albums[j] = list.get(i * column + j++);
                        }
                    }

                    Constructor<E> constructor = lineItemClazz.getDeclaredConstructor(Context.class, Integer.class, albums.getClass());
                    E lineItem = constructor.newInstance(context, viewType, albums);
                    items.add(lineItem);
                }
            }
        } catch (NoSuchMethodException e) {
            Log.e("BaseGroupItem:", "createLineItems NoSuchMethodException", e);
        } catch (InvocationTargetException e) {
            Log.e("BaseGroupItem:", "createLineItems InvocationTargetException", e);
        } catch (InstantiationException e) {
            Log.e("BaseGroupItem:", "createLineItems InstantiationException", e);
        } catch (IllegalAccessException e) {
            Log.e("BaseGroupItem:", "createLineItems IllegalAccessException", e);
        }

        return items;
    }

    public static interface OnIndicatorClickListener {
        void onClick(View v, int groupPos, int childPos);
    }
}
