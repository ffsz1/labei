package com.tongdaxing.xchat_framework.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tongdaxing.xchat_framework.util.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Function:播放Svga动画工具类
 * Author: Edward on 2019/1/23
 */
public class SvgaUtils {

    public static final String HALL_ROOM_ITEM_PLAY_SVGA_ASSETS = "icon_music_play_white.svga";

    private static SVGAParser mSvgaParser;
    private static SvgaUtils mInstance;

    private SvgaUtils(Context context) {
        mSvgaParser = new SVGAParser(context);
    }

    public static SvgaUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SvgaUtils.class) {
                if (mInstance == null) {
                    mInstance = new SvgaUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 循环播放动画 svga来源于assets目录
     *
     * @param svgaImageView
     * @param localAssets   assets目录下的完整文件名
     */
    public void cyclePlayAssetsAnim(@NotNull final SVGAImageView svgaImageView, String localAssets) {
        if (mSvgaParser == null || TextUtils.isEmpty(localAssets)) {
            return;
        }
        mSvgaParser.parse(localAssets, new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                if (svgaImageView != null) {
                    SVGADrawable loadAnimDrawable = new SVGADrawable(videoItem);
                    svgaImageView.setImageDrawable(loadAnimDrawable);
                    svgaImageView.setVisibility(View.VISIBLE);
                    svgaImageView.startAnimation();
                }
            }

            @Override
            public void onError() {
                LogUtil.e("SvgaUtils", "onError");
            }
        });
    }

    /**
     * 循环播放动画 svga来源于网络
     *
     * @param context
     * @param svgaImageView
     * @param svgaUrl       网络svga图片
     */
    public static void cyclePlayWebAnim(Context context, final SVGAImageView svgaImageView, String svgaUrl) {
        if (svgaImageView == null) {
            return;
        }
        svgaImageView.setClearsAfterStop(true);
        svgaImageView.setLoops(1);
        svgaImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                svgaImageView.startAnimation();
            }

            @Override
            public void onRepeat() {
            }

            @Override
            public void onStep(int i, double v) {
            }
        });

        SVGAParser parser = new SVGAParser(context);
        try {
            parser.parse(new URL(svgaUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    svgaImageView.setImageDrawable(drawable);
                    svgaImageView.startAnimation();
                }

                @Override
                public void onError() {
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void closeSvga(SVGAImageView svgaImageView) {
        if (svgaImageView == null) {
            return;
        }
        svgaImageView.stopAnimation();
        svgaImageView.clearAnimation();
        svgaImageView = null;
    }
}
