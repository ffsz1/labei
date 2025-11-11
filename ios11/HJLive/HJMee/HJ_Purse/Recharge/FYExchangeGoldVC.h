//
//  FYExchangeGoldVC.h
//  XBD
//
//  Created by feiyin on 2019/10/30.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FYExchangeGoldVC : UIViewController
@property (copy, nonatomic) void (^changeGoldBlock)(NSString* diamondNum);
@end

NS_ASSUME_NONNULL_END
