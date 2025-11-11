//
//  HJLoginSetAvatarVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^XBDSetAvatarBlock)(UIImage * _Nullable image);

NS_ASSUME_NONNULL_BEGIN

@interface HJLoginSetAvatarVC : UIViewController

@property (copy,nonatomic) XBDSetAvatarBlock setAvatarBlock;

@property (assign,nonatomic) BOOL isMan;




@end

NS_ASSUME_NONNULL_END
