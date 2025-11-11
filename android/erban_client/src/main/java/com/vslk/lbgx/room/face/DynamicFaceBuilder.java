package com.vslk.lbgx.room.face;

/**
 * Created by chenran on 2017/9/9.
 */

public class DynamicFaceBuilder {
//    public static final int FACE_ITEM_DAXIAO = 1;
//    public static final int FACE_ITEM_LIUHAN = 2;
//    public static final int FACE_ITEM_SEMIMI = 3;
//    public static final int FACE_ITEM_LIULEI = 4;
//    public static final int FACE_ITEM_FEIWEN = 5;
//    public static final int FACE_ITEM_JUSANG = 6;
//    public static final int FACE_ITEM_WUYAN = 7;
//    public static final int FACE_ITEM_KAXINTUSHETOU = 8;
//    public static final int FACE_ITEM_BAIBAI = 9;
//    public static final int FACE_ITEM_BISHI = 10;
//    public static final int FACE_ITEM_EMOHUAIXIAO = 11;
//    public static final int FACE_ITEM_TIANSHIWEIXIAO = 12;
//    public static final int FACE_ITEM_SHENGQI = 13;
//    public static final int FACE_ITEM_BAOBAO = 14;
//    public static final int FACE_ITEM_TINGYINYUE = 15;
//    public static final int FACE_ITEM_WABIKONG = 16;
//    public static final int FACE_ITEM_SHAIZI = 17;
//    public static final int FACE_ITEM_SHITOUJIANDAOBU = 18;
//    public static final int FACE_ITEM_CHOUQIAN = 19;
//
//    private HashMap<Integer, FaceResultItem> faceMap;
//
//    public DynamicFaceBuilder() {
//        faceMap = new HashMap<>();
//
//    }
//
//    public int[] daxiao_res = new int[]{
//            R.drawable.daxiao_1_1,
//            R.drawable.daxiao_1_2,
//            R.drawable.daxiao_1_3,
//            R.drawable.daxiao_1_4,
//            R.drawable.daxiao_1_5,
//            R.drawable.daxiao_1_6
//    };
//
//    public int[] liuhan_res = new int[]{
//            R.drawable.liuhan_2_1,
//            R.drawable.liuhan_2_2,
//            R.drawable.liuhan_2_3,
//            R.drawable.liuhan_2_4,
//            R.drawable.liuhan_2_5,
//            R.drawable.liuhan_2_6,
//            R.drawable.liuhan_2_7,
//            R.drawable.liuhan_2_8
//    };
//
//    public int[] semimi_res = new int[]{
//            R.drawable.semimi_3_1,
//            R.drawable.semimi_3_2,
//            R.drawable.semimi_3_3
//    };
//
//    public int[] liulei_res = new int[]{
//            R.drawable.liulei_4_1,
//            R.drawable.liulei_4_2,
//            R.drawable.liulei_4_3,
//            R.drawable.liulei_4_4
//    };
//
//    public int[] feiwen_res = new int[]{
//            R.drawable.feiwen_5_1,
//            R.drawable.feiwen_5_2,
//            R.drawable.feiwen_5_3,
//            R.drawable.feiwen_5_4,
//            R.drawable.feiwen_5_5,
//            R.drawable.feiwen_5_6,
//            R.drawable.feiwen_5_7,
//            R.drawable.feiwen_5_8
//    };
//
//    public int[] jusang_res = new int[]{
//            R.drawable.jusang_6_1,
//            R.drawable.jusang_6_2,
//            R.drawable.jusang_6_3,
//            R.drawable.jusang_6_4,
//            R.drawable.jusang_6_5,
//            R.drawable.jusang_6_6,
//            R.drawable.jusang_6_7,
//            R.drawable.jusang_6_8
//    };
//
//    public int[] wuyan_res = new int[]{
//            R.drawable.wuyan_7_1,
//            R.drawable.wuyan_7_2,
//            R.drawable.wuyan_7_3,
//            R.drawable.wuyan_7_4,
//            R.drawable.wuyan_7_5,
//            R.drawable.wuyan_7_6,
//            R.drawable.wuyan_7_7
//    };
//
//    public int[] kaixintushetou_res = new int[]{
//            R.drawable.kaxintushetou_8_1,
//            R.drawable.kaxintushetou_8_2,
//            R.drawable.kaxintushetou_8_3,
//            R.drawable.kaxintushetou_8_4,
//            R.drawable.kaxintushetou_8_5,
//            R.drawable.kaxintushetou_8_6,
//            R.drawable.kaxintushetou_8_7,
//            R.drawable.kaxintushetou_8_8
//    };
//
//    public int[] baibai_res = new int[]{
//            R.drawable.baibai_9_1,
//            R.drawable.baibai_9_2,
//            R.drawable.baibai_9_3,
//            R.drawable.baibai_9_4,
//            R.drawable.baibai_9_5,
//            R.drawable.baibai_9_6
//    };
//
//    public int[] bishi_res = new int[]{
//            R.drawable.bishi_10_1,
//            R.drawable.bishi_10_2
//    };
//
//    public int[] emohuaixiao_res = new int[]{
//            R.drawable.emohuaixiao_11_1,
//            R.drawable.emohuaixiao_11_2,
//            R.drawable.emohuaixiao_11_3,
//            R.drawable.emohuaixiao_11_4
//    };
//
//    public int[] tianshiweixiao_res = new int[]{
//            R.drawable.tianshiweixiao_12_1,
//            R.drawable.tianshiweixiao_12_2,
//            R.drawable.tianshiweixiao_12_3,
//            R.drawable.tianshiweixiao_12_4,
//            R.drawable.tianshiweixiao_12_5,
//            R.drawable.tianshiweixiao_12_6
//    };
//
//    public int[] shengqi_res = new int[]{
//            R.drawable.shengqi_13_1,
//            R.drawable.shengqi_13_2,
//            R.drawable.shengqi_13_3,
//            R.drawable.shengqi_13_4,
//            R.drawable.shengqi_13_5,
//            R.drawable.shengqi_13_6
//    };
//
//    public int[] baobao_res = new int[]{
//            R.drawable.baobao_14_1,
//            R.drawable.baobao_14_2,
//            R.drawable.baobao_14_3,
//            R.drawable.baobao_14_4,
//            R.drawable.baobao_14_5
//    };
//
//    public int[] tingyinyue_res = new int[]{
//            R.drawable.tingyinyue_15_1,
//            R.drawable.tingyinyue_15_2,
//            R.drawable.tingyinyue_15_3,
//            R.drawable.tingyinyue_15_4,
//            R.drawable.tingyinyue_15_5,
//            R.drawable.tingyinyue_15_6
//    };
//
//    public int[] wabikong_res = new int[]{
//            R.drawable.wabikong_21_1,
//            R.drawable.wabikong_21_2,
//            R.drawable.wabikong_21_3
//    };
//
//    public int[] shaizi_res = new int[]{
//            R.drawable.shaizi_17_1,
//            R.drawable.shaizi_17_2,
//            R.drawable.shaizi_17_3,
//            R.drawable.shaizi_17_4,
//            R.drawable.shaizi_17_5
//    };
//
//    public int[] shaizi_result_res = new int[] {
//            R.drawable.shaizi_17_6,
//            R.drawable.shaizi_17_7,
//            R.drawable.shaizi_17_8,
//            R.drawable.shaizi_17_9,
//            R.drawable.shaizi_17_10,
//            R.drawable.shaizi_17_11
//    };
//
//    public int[] shitoujiandaobu_res = new int[]{
//            R.drawable.shitoujiandaobu_18_1,
//            R.drawable.shitoujiandaobu_18_2,
//            R.drawable.shitoujiandaobu_18_3
//    };
//
//    public int[] shitoujiandaobu_result_res = new int[]{
//            R.drawable.shitoujiandaobu_18_1,
//            R.drawable.shitoujiandaobu_18_2,
//            R.drawable.shitoujiandaobu_18_3
//    };
//
//    public int[] chouqian_res = new int[]{
//            R.drawable.chouqian_20_1,
//            R.drawable.chouqian_20_2,
//            R.drawable.chouqian_20_3,
//            R.drawable.chouqian_20_4,
//            R.drawable.chouqian_20_5,
//            R.drawable.chouqian_20_6
//    };
//
//    public int[] chouqian_result_res = new int[]{
//            R.drawable.chouqian_20_7,
//            R.drawable.chouqian_20_8,
//            R.drawable.chouqian_20_9,
//            R.drawable.chouqian_20_10,
//            R.drawable.chouqian_20_11,
//            R.drawable.chouqian_20_12,
//            R.drawable.chouqian_20_13,
//            R.drawable.chouqian_20_14
//    };
//
//    public void createFaceItem(int faceId) {
//        FaceItem faceItem = new FaceItem(faceId);
//        if (faceId == FACE_ITEM_DAXIAO) {
//            faceItem.setFaceName("大笑");
//            faceItem.setFaceResId(R.drawable.daxiao_1_0);
//        } else if (faceId == FACE_ITEM_LIUHAN) {
//            faceItem.setFaceName("流汗");
//            faceItem.setFaceResId(R.drawable.liuhan_2_0);
//        } else if (faceId == FACE_ITEM_SEMIMI) {
//            faceItem.setFaceName("色眯眯");
//            faceItem.setFaceResId(R.drawable.semimi_3_0);
//        }else if (faceId == FACE_ITEM_LIULEI) {
//            faceItem.setFaceName("流泪");
//            faceItem.setFaceResId(R.drawable.liulei_4_0);
//        }else if (faceId == FACE_ITEM_FEIWEN) {
//            faceItem.setFaceName("飞吻");
//            faceItem.setFaceResId(R.drawable.feiwen_5_0);
//        }else if (faceId == FACE_ITEM_JUSANG) {
//            faceItem.setFaceName("沮丧");
//            faceItem.setFaceResId(R.drawable.jusang_6_0);
//        }else if (faceId == FACE_ITEM_WUYAN) {
//            faceItem.setFaceName("无言");
//            faceItem.setFaceResId(R.drawable.wuyan_7_0);
//        }else if (faceId == FACE_ITEM_KAXINTUSHETOU) {
//            faceItem.setFaceName("吐舌头");
//            faceItem.setFaceResId(R.drawable.kaxintushetou_8_0);
//        }else if (faceId == FACE_ITEM_BAIBAI) {
//            faceItem.setFaceName("拜拜");
//            faceItem.setFaceResId(R.drawable.baibai_9_0);
//        }else if (faceId == FACE_ITEM_BISHI) {
//            faceItem.setFaceName("鄙视");
//            faceItem.setFaceResId(R.drawable.bishi_10_0);
//        }else if (faceId == FACE_ITEM_EMOHUAIXIAO) {
//            faceItem.setFaceName("鄙视");
//            faceItem.setFaceResId(R.drawable.bishi_10_0);
//        }else if (faceId == FACE_ITEM_TIANSHIWEIXIAO) {
//        }else if (faceId == FACE_ITEM_SHENGQI) {
//        }else if (faceId == FACE_ITEM_BAOBAO) {
//        }else if (faceId == FACE_ITEM_TINGYINYUE) {
//        }else if (faceId == FACE_ITEM_WABIKONG) {
//        }else if (faceId == FACE_ITEM_SHAIZI) {
//        }else if (faceId == FACE_ITEM_SHITOUJIANDAOBU) {
//        }else if (faceId == FACE_ITEM_CHOUQIAN) {
//        }
//    }
//
//    public void createResultItem(int faceId, int resultNumber) {
//        FaceResultItem faceResultItem = new FaceResultItem();
//        faceResultItem.setFaceId(faceId);
//        faceResultItem.setResultNumber(resultNumber);
//
//        if (faceId == FACE_ITEM_DAXIAO) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(daxiao_res);
//        } else if (faceId == FACE_ITEM_LIUHAN) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(liuhan_res);
//        } else if (faceId == FACE_ITEM_SEMIMI) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(semimi_res);
//        }else if (faceId == FACE_ITEM_LIULEI) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(liulei_res);
//        }else if (faceId == FACE_ITEM_FEIWEN) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(feiwen_res);
//        }else if (faceId == FACE_ITEM_JUSANG) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(jusang_res);
//        }else if (faceId == FACE_ITEM_WUYAN) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(wuyan_res);
//        }else if (faceId == FACE_ITEM_KAXINTUSHETOU) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(kaixintushetou_res);
//        }else if (faceId == FACE_ITEM_BAIBAI) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(baibai_res);
//        }else if (faceId == FACE_ITEM_BISHI) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(bishi_res);
//        }else if (faceId == FACE_ITEM_EMOHUAIXIAO) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(emohuaixiao_res);
//        }else if (faceId == FACE_ITEM_TIANSHIWEIXIAO) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(tianshiweixiao_res);
//        }else if (faceId == FACE_ITEM_SHENGQI) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(shengqi_res);
//        }else if (faceId == FACE_ITEM_BAOBAO) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(baobao_res);
//        }else if (faceId == FACE_ITEM_TINGYINYUE) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(tingyinyue_res);
//        }else if (faceId == FACE_ITEM_WABIKONG) {
//            faceResultItem.setResultFace(false);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(wabikong_res);
//        }else if (faceId == FACE_ITEM_SHAIZI) {
//            faceResultItem.setResultFace(true);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(shaizi_res);
//            faceResultItem.setResultNumber(resultNumber);
//            faceResultItem.setFaceResultRes(shaizi_result_res[resultNumber]);
//            faceResultItem.setResultDuration(4000);
//        }else if (faceId == FACE_ITEM_SHITOUJIANDAOBU) {
//            faceResultItem.setResultFace(true);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(shitoujiandaobu_res);
//            faceResultItem.setResultNumber(resultNumber);
//            faceResultItem.setFaceResultRes(shaizi_result_res[resultNumber]);
//            faceResultItem.setResultDuration(4000);
//        }else if (faceId == FACE_ITEM_CHOUQIAN) {
//            faceResultItem.setResultFace(true);
//            faceResultItem.setFaceMoveDuration(3000);
//            faceResultItem.setFaceMoveRes(chouqian_res);
//            faceResultItem.setResultNumber(resultNumber);
//            faceResultItem.setFaceResultRes(chouqian_result_res[resultNumber]);
//            faceResultItem.setResultDuration(4000);
//        }
//    }
}
