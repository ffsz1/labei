package com.tongdaxing.xchat_framework.http_image.result;

/**
 * @author houzhenjing
 *         on 11/21/14.
 */
public interface RetCode {
    final int BSS_ERROR = -1;//业务错误
    final int DB_ERROR = -2;//数据库错误
    final int ARGS_ERROR = -4;//参数错误
    final int UNKNOWN_ERROR = -3;//未知错误
    final int SUCCESS = 0;//业务成功
    final int BSS_EXIST = 100;//已存在记录
    final int BSS_SONG_NOT_EXIST = 201; //歌曲不存在
}
