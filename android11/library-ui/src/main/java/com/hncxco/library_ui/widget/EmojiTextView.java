package com.hncxco.library_ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.util.AttributeSet;

/**
 * 显示Emoji的TextView（解决android自带textView显示某些emoji或者特殊字符会闪退的问题）
 */
public class EmojiTextView extends android.support.v7.widget.AppCompatTextView {

    private String textOnDrawError = "不支持该文本显示";

    public EmojiTextView(Context context) {
        super(context);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            setText(textOnDrawError);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null) {
            super.setText(new SpannableString(text), type);//转换一下spannableString
        }
        super.setText(text, type);
    }

    /**
     * 设置当draw错误时显示的文本
     *
     * @param textOnDrawError 当draw错误时显示的文本
     */
    public void setTextOnDrawError(String textOnDrawError) {
        this.textOnDrawError = textOnDrawError;
    }
}