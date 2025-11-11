package com.vslk.lbgx.room.match;

import com.tongdaxing.erban.R;

import java.util.Random;

/**
 * 速配功能工具类
 */
public class RoomMatchUtil {


    /**
     * 获取速配对应id
     * @return
     */
    public static int getMatchResId(int index){
        int result = R.drawable.ic_match_question_mark;
        switch (index){
            case 1:
                result = R.drawable.ic_match_num_1;
                break;
            case 2:
                result = R.drawable.ic_match_num_2;
                break;
            case 3:
                result = R.drawable.ic_match_num_3;
                break;
            case 4:
                result = R.drawable.ic_match_num_4;
                break;
            case 5:
                result = R.drawable.ic_match_num_5;
                break;
            case 6:
                result = R.drawable.ic_match_num_6;
                break;
            case 7:
                result = R.drawable.ic_match_num_7;
                break;
            case 8:
                result = R.drawable.ic_match_num_8;
                break;
            case 9:
                result = R.drawable.ic_match_num_9;
                break;
        }
        return result;
    }

    /**
     * 生成随机数
     *
     * @param resultEndPos 结束数
     * @param resultStartPos 开始数
     * @return --
     */
    public static int getRandomNum(int resultStartPos,int resultEndPos) {
        // 结果的数量,1/2/3/4/5
        // 生成随机数结果下标
        Random r = new Random();
            // 可以重复,直接加进去
        return resultStartPos + r.nextInt(resultEndPos - resultStartPos + 1);
    }
}
