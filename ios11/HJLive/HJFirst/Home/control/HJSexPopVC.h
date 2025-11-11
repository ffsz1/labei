//
//  HJSexPopVC.h
//  HJLive
//
//  Created by feiyin on 2020/9/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJSexPopVC : UIViewController
@property (copy,nonatomic) void(^allBtnBlock)(void);
@property (copy,nonatomic) void(^manBtnBlock)(void);
@property (copy,nonatomic) void(^wemanBtnBlock)(void);
@end

NS_ASSUME_NONNULL_END
