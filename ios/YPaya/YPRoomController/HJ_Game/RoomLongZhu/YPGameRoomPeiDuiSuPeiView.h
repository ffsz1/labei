//
//  YPGameRoomPeiDuiSuPeiView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPGameRoomPeiDuiSuPeiView : UIView

@property (nonatomic, copy) void(^supeiBtnActionBlock)();
@property (nonatomic, copy) void(^showBtnActionBlock)();
@property (nonatomic, copy) void(^ruleBtnActionBlock)();
@property (nonatomic, copy) void(^cancelBtnActionBlock)();

@property (nonatomic, assign) NSInteger selectedNum;
@property (nonatomic, assign) BOOL isAnimation;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;

@end
