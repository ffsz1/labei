//
//  YPRoomMenuView.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSUInteger, XBDRoomMenuType) {
    XBDRoomMenuTypeSet,//房间设置
    XBDRoomMenuTypeOpenChat,//开关公屏
    XBDRoomMenuTypeOpenCar,//开关座驾特效
    XBDRoomMenuTypeOpenGift,//开关礼物特效
    XBDRoomMenuTypeTip,//进房提示
    XBDRoomMenuTypeManager,//管理员
    XBDRoomMenuTypeMin,//最小化
    XBDRoomMenuTypeOut,//退出房间
    XBDRoomMenuTypeReport,//举报
    XBDRoomMenuTypeFangjianCome,//弹出房间设置
    XBDRoomMenuTypeShare,//分享
    XBDRoomMenuTypeMusic,//音乐
    
};

typedef void(^RoomMenuBlock)(XBDRoomMenuType);


NS_ASSUME_NONNULL_BEGIN

@interface YPRoomMenuView : UIView

+ (void)show:(RoomMenuBlock)menuBlock;
+ (void)showUpViewForRoom:(RoomMenuBlock)menuBlock;
@end

NS_ASSUME_NONNULL_END
