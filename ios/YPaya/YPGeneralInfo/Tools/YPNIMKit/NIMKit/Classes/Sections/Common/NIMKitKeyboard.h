//
//  NIMKitKeyboard.h
//  YPNIMKit
//
//  Created by chris on 2017/11/3.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPNIMKitKeyboardInfo : NSObject

//是否可见
@property (nonatomic,assign,readonly) CGFloat isVisiable;

//键盘高度
@property (nonatomic,assign,readonly) CGFloat keyboardHeight;

+ (instancetype)instance;


@end
