//
//  YPPurseViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPurseViewControllerFactory.h"
@implementation YPPurseViewControllerFactory

+(instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"YPPurse" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

//内购充值页面
- (UIViewController *)instantiateRechargeViewController;
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPRechargeViewController"];
}

- (UIViewController *)instantiateBillMenuViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPBillMenuViewController"];
}

- (UIViewController *)instantiateBillListViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPBillListController"];
}
//我的钱包
- (UIViewController *)instantiateHJMyWalletVC{
     return [self.storyboard instantiateViewControllerWithIdentifier:@"YPMyWalletVC"];
}
//账单
- (UIViewController *)instantiateHJBillCatalogueVC{
     return [self.storyboard instantiateViewControllerWithIdentifier:@"YPBillCatalogueVC"];
}

//提现
- (UIViewController *)instantiateFYGetCashVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPFYGetCashVC"];
}

//兑换金币
- (UIViewController *)instantiateFYExchangeGoldVC{
     return [self.storyboard instantiateViewControllerWithIdentifier:@"YPFYExchangeGoldVC"];
}
//填写支付宝信息
- (UIViewController *)instantiateFYBindingWeixinVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPFYBindingWeixinVC"];
}

//填写银行信息
- (UIViewController *)instantiateFYSetBankInfoVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPFYSetBankInfoVC"];
}


//规则
- (UIViewController *)instantiateFYRegularVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPFYRegularVC"];
}



@end
