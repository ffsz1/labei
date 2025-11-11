//
//  HJAlerTipView.h
//  HJLive
//
//  Created by feiyin on 2020/8/21.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJGiftAllMicroSendInfo.h"


typedef NS_ENUM(NSUInteger, HJAlerTipType) {
    HJAlerTipfirmType,//确定
    HJAlerTipCancelType,//cancel
  
};
typedef void(^AlerTipBlock)(HJAlerTipType);
NS_ASSUME_NONNULL_BEGIN

@interface HJAlerTipView : UIView
+ (void)show:(AlerTipBlock)menuBlock content:(NSString*)content nick:(NSString*)nick isAttribute:(BOOL)isAttribute;
@end

NS_ASSUME_NONNULL_END
