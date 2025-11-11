package com.tongdaxing.xchat_framework.http_image.image;

import android.graphics.drawable.Drawable;

/**
 * data数据转换drawable
 * @author zhongyongsheng on 14-9-11.
 */
public interface DrawableParser {

    Drawable parse(byte[] data);

}
