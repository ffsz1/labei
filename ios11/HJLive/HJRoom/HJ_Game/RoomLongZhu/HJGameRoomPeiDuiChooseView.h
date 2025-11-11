//
//  HJGameRoomPeiDuiChooseView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJGameRoomPeiDuiChooseView : UIView

@property (nonatomic, copy) void(^randomBtnActionBlock)();
@property (nonatomic, copy) void(^chooseBtnActionBlock)();
@property (nonatomic, copy) void(^showBtnActionBlock)();
@property (nonatomic, copy) void(^ruleBtnActionBlock)();

@property (nonatomic, assign) NSInteger selectedNum;

@end
