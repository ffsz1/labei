//
//  YPPurseViewControllerFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYStoryboardViewControllerFactory.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPPurseViewControllerFactory : YPYYStoryboardViewControllerFactory
+ (instancetype)sharedFactory;
- (UIViewController *)instantiateRechargeViewController;
- (UIViewController *)instantiateBillMenuViewController;
- (UIViewController *)instantiateBillListViewController;
- (UIViewController *)instantiateHJMyWalletVC;//我的钱包
- (UIViewController *)instantiateHJBillCatalogueVC;//账单目录

- (UIViewController *)instantiateFYGetCashVC;//提现
- (UIViewController *)instantiateFYExchangeGoldVC;//兑换金币
- (UIViewController *)instantiateFYSetBankInfoVC;//填写银行信息
- (UIViewController *)instantiateFYBindingWeixinVC;//填写支付宝信息
- (UIViewController *)instantiateFYRegularVC;//规则
@end

NS_ASSUME_NONNULL_END
