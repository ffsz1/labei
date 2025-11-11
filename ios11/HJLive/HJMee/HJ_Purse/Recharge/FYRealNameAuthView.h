//
//  FYRealNameAuthView.h
//  XBD
//
//  Created by feiyin on 2019/11/21.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, FYRealNameAuthType) {
    FYConfirmType,//确定
    FYRealNameCancelType,//cancel
  
};
typedef void(^RealNameAuthBlock)(FYRealNameAuthType);
NS_ASSUME_NONNULL_BEGIN

@interface FYRealNameAuthView : UIView
+ (void)show:(RealNameAuthBlock)menuBlock;
@end

NS_ASSUME_NONNULL_END
