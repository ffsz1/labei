package com.tongdaxing.xchat_core.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * @author Rowand jj
 *
 *ʹ��Lrucache����bitmap�Ĺ�����
 */
public class BitmapLruCacheHelper
{
	private static final String TAG = "BitmapLruCacheHelper";
	private static BitmapLruCacheHelper instance = new BitmapLruCacheHelper();
	LruCache<String, Bitmap> cache = null;
	//����
	private BitmapLruCacheHelper()
	{
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / (float) 8);
		cache = new LruCache<String, Bitmap>(maxSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes()*value.getHeight();
			}
		};
	}

	public static BitmapLruCacheHelper getInstance()
	{
		return instance;
	}
	 /**
     *���뻺�� 
     * @param key
     * @param value
     */
    public void addBitmapToMemCache(String key, Bitmap value)
    {
        if(key == null || value == null)
        {
            return;
        }
        if(cache!=null && getBitmapFromMemCache(key)==null)
        {
            cache.put(key, value);
            Log.i(TAG,"put bitmap to lrucache success");
        }
    }
    
    /**
     * �ӻ����л�ȡͼƬ
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key)
    {
        if(key == null)
        {
            return null;
        }
        Bitmap bitmap = cache.get(key);
        Log.i(TAG,"get bitmap from lrucache,bitmap="+bitmap);
        return bitmap;
    }
    
    /**
     * ��ָ��bitmap�ӻ������Ƴ�
     * @param key
     * @return
     */
    public Bitmap removeBitmapFromMemCache(String key)
    {
    	if(key == null)
    	{
    		return null;
    	}
    	return cache.remove(key);
    }
}





























