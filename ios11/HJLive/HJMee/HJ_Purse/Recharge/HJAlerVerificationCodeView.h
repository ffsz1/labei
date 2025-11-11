//
//  HJAlerVerificationCodeView.h
//  HJLive
//
//  Created by feiyin on 2020/8/21.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJGiftAllMicroSendInfo.h"


typedef NS_ENUM(NSUInteger, HJAlerVerificationCodeType) {
    HJAlerVerificationCodeFirmType,//确定
     HJAlerGetVeriCodeType,//获取验证码
    HJAlerVerificationCodeCancelType,//cancel
  
};
typedef void(^HJAlerVerificationCodeBlock)(HJAlerVerificationCodeType,NSString*_Nullable);
NS_ASSUME_NONNULL_BEGIN

@interface HJAlerVerificationCodeView : UIView
+ (void)show:(HJAlerVerificationCodeBlock)menuBlock content:(NSString*)content nick:(NSString*)nick isAttribute:(BOOL)isAttribute;
@end

NS_ASSUME_NONNULL_END
