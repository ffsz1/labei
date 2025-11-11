package com.vslk.lbgx.ui.praise.base;

import android.graphics.Canvas;

public interface IDrawTask {

    void start();

    void stop();

    void draw(Canvas canvas);

    void addDrawable(IDrawable drawable);

    void clearDrawable();

}
