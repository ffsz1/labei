//
//  YPFYSetBankInfoVC.h
//  XBD
//
//  Created by feiyin on 2019/11/20.
//

#import <UIKit/UIKit.h>
#import "YPFYBankInfoListModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPFYSetBankInfoVC : UIViewController
@property (copy, nonatomic) void (^takeBankCashWayBlock)(YPFYBankInfoListModel* model);
@end

NS_ASSUME_NONNULL_END
