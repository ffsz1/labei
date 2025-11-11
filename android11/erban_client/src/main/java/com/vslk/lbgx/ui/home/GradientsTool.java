package com.vslk.lbgx.ui.home;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.widget.RadioButton;
import android.widget.TextView;

public class GradientsTool {

    public static void setGradients(TextView view, String start, String end) {
        int[] colors = {Color.parseColor(start), Color.parseColor(start), Color.parseColor(end)};//颜色的数组
        float[] position = {0f, 0.5f, 1.0f};//颜色渐变位置的数组
        LinearGradient mLinearGradient = new LinearGradient(0, 0, view.getPaint().getTextSize() * view.getText().length(), 0, colors, position, Shader.TileMode.CLAMP);
        view.getPaint().setShader(mLinearGradient);
        view.invalidate();
    }


    public static void setGradients(RadioButton view, String start, String end) {
        int[] colors = {Color.parseColor(start), Color.parseColor(start), Color.parseColor(end)};//颜色的数组
        float[] position = {0f, 0.2f, 1.0f};//颜色渐变位置的数组
        LinearGradient mLinearGradient = new LinearGradient(0, 0, view.getPaint().getTextSize() * view.getText().length(), 0, colors, position, Shader.TileMode.CLAMP);
        view.getPaint().setShader(mLinearGradient);
        view.invalidate();
    }

}
