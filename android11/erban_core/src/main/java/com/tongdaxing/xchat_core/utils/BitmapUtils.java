package com.tongdaxing.xchat_core.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Rowand jj
 *ѹ��ͼƬ�Ĺ�����
 */
public final class BitmapUtils
{
	/**
	 * Ĭ�ϲ�����
	 */
	private static final int DEFAULT_SAMPLE_SIZE = 1;
	private static final String TAG = "BitmapUtils";
	
	/**
	 * ���ݲ�����ѹ��ͼƬ
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId,opts);
		opts.inSampleSize = caclulateInSampleSize(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, opts);
	}
	/**
	 * ��decodeSampledBitmapFromResource������ͬ����ȡbitmap�ķ�ʽ��ͬ
	 */
	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, opts);
		opts.inSampleSize = caclulateInSampleSize(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds = false;
		Log.i(TAG,"OPTS = "+opts.inSampleSize);
		return BitmapFactory.decodeFile(pathName, opts);
	}
	/**
	 * ��decodeSampledBitmapFromResource������ͬ����ȡbitmap�ķ�ʽ��ͬ
	 */
	public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		opts.inSampleSize = caclulateInSampleSize(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}
	/**
	 * ���������ε���decodeStream,�ڶ��ε���InptutStreamʱ��ָ���Ѿ�ָ��ĩβ��.
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromStream(InputStream in, int reqWidth, int reqHeight)
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] data = null;
		try
		{
			int len = 0;
			byte[] buf = new byte[1024];
			while((len = in.read(buf)) != -1)
			{
				bout.write(buf, 0, len);
			}
			data = bout.toByteArray();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return decodeSampledBitmapFromByteArray(data, reqWidth, reqHeight);
	}
	
	
	/**
	 * ���������
	 */
	private static int caclulateInSampleSize(BitmapFactory.Options opts, int reqWidth, int reqHeight)
	{
		if(opts == null)
			return DEFAULT_SAMPLE_SIZE;
		int width = opts.outWidth;
		int height = opts.outHeight;
		int sampleSize = DEFAULT_SAMPLE_SIZE;
		if(width > reqWidth || height > reqHeight)
		{
			int widthRatio = (int) (width/(float)reqWidth);
			int heightRatio = (int) (height/(float)reqHeight);
			
			sampleSize = (widthRatio > heightRatio) ? heightRatio : widthRatio;
		}
		return sampleSize;
	}
}























