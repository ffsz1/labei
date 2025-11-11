//
//  HJPurseViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPurseViewControllerFactory.h"
@implementation HJPurseViewControllerFactory

+(instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"HJPurse" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

//内购充值页面
- (UIViewController *)instantiateRechargeViewController;
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJRechargeViewController"];
}

- (UIViewController *)instantiateBillMenuViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJBillMenuViewController"];
}

- (UIViewController *)instantiateBillListViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJBillListController"];
}
//我的钱包
- (UIViewController *)instantiateHJMyWalletVC{
     return [self.storyboard instantiateViewControllerWithIdentifier:@"HJMyWalletVC"];
}
//账单
- (UIViewController *)instantiateHJBillCatalogueVC{
     return [self.storyboard instantiateViewControllerWithIdentifier:@"HJBillCatalogueVC"];
}

//提现
- (UIViewController *)instantiateFYGetCashVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"FYGetCashVC"];
}

//兑换开心
- (UIViewController *)instantiateFYExchangeGoldVC{
     return [self.storyboard instantiateViewControllerWithIdentifier:@"FYExchangeGoldVC"];
}
//填写支付宝信息
- (UIViewController *)instantiateFYBindingWeixinVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"FYBindingWeixinVC"];
}

//填写银行信息
- (UIViewController *)instantiateFYSetBankInfoVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"FYSetBankInfoVC"];
}


//规则
- (UIViewController *)instantiateFYRegularVC {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"FYRegularVC"];
}



@end
