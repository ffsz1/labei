package com.tongdaxing.xchat_framework.util.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 数据类型转换
 * @author hm
 */
public class JavaUtil {
	
	public static long str2long(String str){
		if(!TextUtils.isEmpty(str)){
			long result = 0;
			try {
				result = Long.parseLong(str);
			} catch (Exception e) {
				Log.e("JavaUtil","parseLong错误");
			}
			return result;
		}
		return 0;
	}
	
	public static Integer str2int(String str) {
		int result = 0;
		if (!TextUtils.isEmpty(str)) {
			try {
				result = Integer.parseInt(str);
			} catch (Exception e) {
				//数据转化问题
				Log.e("JavaUtil","parseInt错误");
			}
			return result;
		}
		return 0;
	}

	public static Double str2double(String str) {
		double result = 0.0;
		if (!TextUtils.isEmpty(str)) {
			try {
				result = Double.parseDouble(str);
			} catch (Exception e) {
				//数据转化问题
				Log.e("JavaUtil","str2double错误");
			}
			return result;
		}
		return 0.0;
	}
	
	public static String str2double2len(String str){
		try {
			DecimalFormat df = new DecimalFormat("######0.00");
			String format = df.format(str2double(str));
			return format;
		} catch (Exception e) {
			Log.e("JavaUtil","str2double2len错误");
		}
		return null;
	}
	
	public static String str2double0len(String str){
		try {
			DecimalFormat df = new DecimalFormat("######0");
			String format = df.format(str2double(str));
			return format;
		} catch (Exception e) {
			Log.e("JavaUtil","str2double0len错误");
		}
		return null;
	}

	public static Float str2flaot(String str) {
		float result = 0f;
		if (!TextUtils.isEmpty(str)) {
			try {
				result = Float.parseFloat(str);
			} catch (Exception e) {
				Log.e("JavaUtil","parseFloat错误");
			}
			return result;
		}
		return 0f;
	}
	
	/**
	 * float类型保留两位小数
	 */
	public static float float2(float num) {
		try {
			BigDecimal b = new BigDecimal(num);
			float f = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			return f;
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * double类型保留两位小数
	 */
	public static double double2(double f) {
		BigDecimal b = new BigDecimal(f);
		double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return df;
	}

	public static Float getMin(List<Float> list) {
		Float min = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			if (min > list.get(i))
				min = list.get(i);
		}
		return min;
	}

	public static Float getMax(List<Float> list) {
		Float max = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			if (max < list.get(i))
				max = list.get(i);
		}
		return max;
	}

	/**
	 * desc:将数组转为64编码
	 * 
	 * @return
	 */
	public static String objToBase64Str(Object obj) {
		if (obj == null) {
			return null;
		}
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		String base64Str = null;
		try {
			oos = new ObjectOutputStream(baos);
			// 将对象放到OutputStream中
			oos.writeObject(obj);
			base64Str = new String(Base64.encode(baos.toByteArray(), 0));
		} catch (IOException e) {
			base64Str = null;
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				base64Str = null;
			}
		}
		return base64Str;
	}

	/**
	 * desc:将64编码的数据转为对象
	 * 
	 * @param data
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static Object base64StrToObj(String data) {
		Object obj = null;
		if (TextUtils.isEmpty(data)) {
			return obj;
		}
		// 读取字节
		byte[] userByte = Base64.decode(data, 0);
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(userByte);
		ObjectInputStream ois = null;
		try {
			// 再次封装
			ois = new ObjectInputStream(bais);
			// 读取对象
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				obj = null;
			}
		}
		return obj;
	}

	public static int weekStr2int(String str) {
		int i = 0;
		if (str.equals("一")) {
			i = 1;
		} else if (str.equals("二")) {
			i = 2;
		} else if (str.equals("三")) {
			i = 3;
		} else if (str.equals("四")) {
			i = 4;
		} else if (str.equals("五")) {
			i = 5;
		} else if (str.equals("六")) {
			i = 6;
		} else if (str.equals("七") || str.equals("日")) {
			i = 7;
		}
		return i;
	}

}
