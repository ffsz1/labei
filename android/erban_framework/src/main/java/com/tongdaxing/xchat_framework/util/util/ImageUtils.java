package com.tongdaxing.xchat_framework.util.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtils {

	public static Bitmap path2Bitmap(String path, int w, int h){
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 设置为ture只获取图片大小
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// 返回为空
			BitmapFactory.decodeFile(path, opts);
			int width = opts.outWidth;
			int height = opts.outHeight;
			float scaleWidth = 0.f, scaleHeight = 0.f;
			if (width > w || height > h) {
				// 缩放
				scaleWidth = ((float) width) / w;
				scaleHeight = ((float) height) / h;
			}
			opts.inJustDecodeBounds = false;
			float scale = Math.max(scaleWidth, scaleHeight);
			opts.inSampleSize = (int)scale;
			WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
			return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	public static Bitmap ImageView2Bitmap(ImageView iv){
		if(null != iv && null != iv.getDrawable()){
			Bitmap image = ((BitmapDrawable)iv.getDrawable()).getBitmap();
			return image;
		}else{
			return null;
		}
	}

	public static void recycle(ImageView iv) {
		if(null != iv){
			Bitmap pBitmap = ImageView2Bitmap(iv);
			if(null != pBitmap){
				SoftReference<Bitmap> bitmap;
				bitmap = new SoftReference<Bitmap>(pBitmap);
				if (bitmap != null) {
					if (bitmap.get() != null && !bitmap.get().isRecycled()) {
//						bitmap.get().recycle();
						bitmap = null;
					}
				}
			}
		}
	}

	// 等比例缩放图片
	public static Bitmap zoomImg(String imgPath, int newWidth, int newHeight) {

		// 图片源
		Bitmap bm = BitmapFactory.decodeFile(imgPath);
		if (null != bm) {
			return zoomImg(bm, newWidth, newHeight);
		}

		return null;
	}

	public static Bitmap zoomImg(Context context, String img, int newWidth, int newHeight) {
		// 图片源
		try {
			Bitmap bm = BitmapFactory.decodeStream(context.getAssets().open(img));
			if (null != bm) {
				return zoomImg(bm, newWidth, newHeight);
			}
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		bm = null;
		return newbm;
	}

	/**
	 * 根据图片的url路径获得Bitmap对象
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap urlToBitmap(String url) {
		URL fileUrl = null;
		Bitmap bitmap = null;

		try {
			fileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;

	}

	/**
	 * @param urlpath
	 * @return Bitmap 根据图片url获取图片对象
	 */
	public static Bitmap getBitMBitmap(String urlpath) {
		Bitmap map = null;
		try {
			URL url = new URL(urlpath);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			map = BitmapFactory.decodeStream(in);
			// Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @param urlpath
	 * @return Bitmap 根据url获取布局背景的对�?
	 */
	public static Drawable getDrawable(String urlpath) {
		Drawable d = null;
		try {
			URL url = new URL(urlpath);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			d = Drawable.createFromStream(in, "background.jpg");
			// Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		return d;
	}



	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

}
