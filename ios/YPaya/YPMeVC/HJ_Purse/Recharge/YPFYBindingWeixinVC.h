//
//  YPFYBindingWeixinVC.h
//  XBD
//
//  Created by feiyin on 2019/11/1.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPFYBindingWeixinVC : UIViewController
@property (nonatomic,copy) void(^selectZfbBlock)(NSString* alipayAccount,NSString* alipayAccountName);
@end

NS_ASSUME_NONNULL_END
