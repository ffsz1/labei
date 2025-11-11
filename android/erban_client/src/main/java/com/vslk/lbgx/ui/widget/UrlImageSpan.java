package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tongdaxing.erban.R;

import java.lang.reflect.Field;

/**
 * Function:
 * Author: Edward on 2019/1/28
 */
public class UrlImageSpan extends ImageSpan {
    private boolean picShowed;
    private String url;
    private Context context;
    private TextView textView;
    private int imgWidth = -1;
    private int imgHeight = -1;
    private int level = 0;

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public UrlImageSpan(Context context, @DrawableRes int resourceId) {
        super(context, resourceId);
    }

    public UrlImageSpan(Context context, String url, TextView textView) {//加载远程图片构造方法
        super(context, R.mipmap.ic_launcher);
        this.url = url;
        this.context = context;
        this.textView = textView;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY;
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    @Override
    public Drawable getDrawable() {
        if (!picShowed && !TextUtils.isEmpty(url)) {
            loadNetworkImg();
        }
        Drawable drawable = super.getDrawable();
        drawable.setBounds(0, 0,
                imgWidth == -1 ? drawable.getIntrinsicWidth() : getImgWidth(),
                imgHeight == -1 ? drawable.getIntrinsicHeight() : getImgHeight());
        return drawable;
    }

    private void loadNetworkImg() {
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                Resources resources = context.getResources();
                BitmapDrawable b = new BitmapDrawable(resources, ((BitmapDrawable) resource).getBitmap());
                b.setBounds(0, 0, imgWidth == -1 ? b.getIntrinsicWidth() : getImgWidth(), imgHeight == -1 ? b.getIntrinsicHeight() : getImgHeight());
                Field mDrawable;
                Field mDrawableRef;
                try {
                    mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
                    mDrawable.setAccessible(true);
                    mDrawable.set(UrlImageSpan.this, b);

                    mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
                    mDrawableRef.setAccessible(true);
                    mDrawableRef.set(UrlImageSpan.this, null);

                    picShowed = true;
                    textView.setText(textView.getText());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
