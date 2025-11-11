package com.tongdaxing.xchat_framework.util.util;


/**
 * Created by lijun on 2014/11/20.
 */
public class ColorUtil {

    public static String color2HexString(int intColor) {
        String strColor = String.format("#%06X", 0xFFFFFF & intColor);
        return strColor;
    }

    public static float brightness(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int V = Math.max(b, Math.max(r, g));

        return (V / 255.f);
    }


    /**
     * 有透明到白色过度色值变化
     * 参考ValueAnimator的ofArgb变化动画
     * @param f 0 - 1
     * @return
     */
    public static int changeColor(float f){
        int color = 0x00ffffff;
        int startInt = 0x00ffffff;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = 0xffffffff;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        color = (startA + (int)(f * (endA - startA))) << 24 |
                (startR + (int)(f * (endR - startR))) << 16 |
                (startG + (int)(f * (endG - startG))) << 8 |
                (startB + (int)(f * (endB - startB)));
        return color;
    }


    /**
     * 由开始色值到结束色值 0f -1f 范围内过度变化
     * 参考ValueAnimator的ofArgb变化动画
     * @param f 0f - 1f
     * @return
     */
    public static int changeColor(int starColor,int endColor,float f){
        int color = 0x00ffffff;
        int startInt = starColor;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endColor;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        color = (startA + (int)(f * (endA - startA))) << 24 |
                (startR + (int)(f * (endR - startR))) << 16 |
                (startG + (int)(f * (endG - startG))) << 8 |
                (startB + (int)(f * (endB - startB)));
        return color;
    }

}
