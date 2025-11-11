//
//  YPAlterShower.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^HJAlterShowerBlock)(void);

@interface YPAlterShower : UIView


/**
 显示提醒框
 
 tip:
 sure和cancel不传   显示文字 提醒样式 (2秒后自动消失)
 sure传和cancel不传   显示文字+确定按钮 提醒样式
 sure传和cancel传   显示文字+确定按钮+取消按钮 提醒样式

 @param text 文案
 @param sure 确定回调
 @param cancel 取消回调
 */
+ (void)showText:(NSString *)text sure:(HJAlterShowerBlock)sure cancel:(HJAlterShowerBlock)cancel;


@end

