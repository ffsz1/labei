package com.vslk.lbgx.im.transfer;

import android.annotation.SuppressLint;

import com.netease.nim.uikit.session.module.Container;

import lombok.Data;

@SuppressLint("ParcelCreator")
@Data
public class TransferBean{
    private String sessionId;
    private String gold;
    private Container container;
}
