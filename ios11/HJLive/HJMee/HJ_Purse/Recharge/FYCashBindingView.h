//
//  FYCashBindingView.h
//  XBD
//
//  Created by feiyin on 2019/10/31.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, FYCachType) {
    FYCashType,//
     FYBankCashType,//
    FYCancelType,//
  
};
typedef void(^RoomMenuBlock)(FYCachType);
NS_ASSUME_NONNULL_BEGIN

@interface FYCashBindingView : UIView
+ (void)show:(RoomMenuBlock)menuBlock;
@end

NS_ASSUME_NONNULL_END
