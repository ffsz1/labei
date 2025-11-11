//
//  FYSetBankInfoVC.h
//  XBD
//
//  Created by feiyin on 2019/11/20.
//

#import <UIKit/UIKit.h>
#import "FYBankInfoListModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface FYSetBankInfoVC : UIViewController
@property (copy, nonatomic) void (^takeBankCashWayBlock)(FYBankInfoListModel* model);
@end

NS_ASSUME_NONNULL_END
