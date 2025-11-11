//
//  YPBindingPhoneVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYViewController.h"

@interface YPBindingPhoneVC : YPYYViewController
@property (assign ,nonatomic) BOOL isPush;
@property (assign, nonatomic) BOOL isBindPhone;

//标识是否来自登录后，进行绑定手机号
@property (assign,nonatomic) BOOL isFromLogin;
@end
