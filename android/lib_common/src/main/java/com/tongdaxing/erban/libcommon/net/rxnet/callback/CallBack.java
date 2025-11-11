package com.tongdaxing.erban.libcommon.net.rxnet.callback;

public abstract interface CallBack<T>
{
  public abstract void onSuccess(T paramT);

  public abstract void onFail(String paramString);
}

