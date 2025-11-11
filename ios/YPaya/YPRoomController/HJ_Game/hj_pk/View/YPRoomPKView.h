//
//  YPRoomPKView.h
//  HJLive
//
//  Created by apple on 2019/6/18.
//

#import <UIKit/UIKit.h>

#import "YPRoomPKJoinModel.h"

typedef NS_ENUM(NSUInteger, XBDRoomPKType) {
    XBDRoomPKTypeSend = 0,//发起pk
    XBDRoomPKTypeSure,
    XBDRoomPKTypeFail,
};

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKView : UIView

//展示 发起pk
+ (void)show;

//展示 接受、确认pk
+ (void)showSurePKView:(YPRoomPKJoinModel *)joinModel;

//展示 参与失败
+ (void)showFailView;


- (void)getGiftData;

@end

NS_ASSUME_NONNULL_END
