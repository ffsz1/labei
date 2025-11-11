//
//  HJBillCatalogueVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBillCatalogueVC.h"
#import "HJBillListController.h"
#import "HJPurseViewControllerFactory.h"


@interface HJBillCatalogueVC ()

@end

@implementation HJBillCatalogueVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
}


//收礼记录
- (IBAction)receivingGiftsAction:(id)sender {
    HJBillListController *vc = (HJBillListController *)[[HJPurseViewControllerFactory sharedFactory] instantiateBillListViewController];
    vc.type = BillType_GiftIn;
    vc.title = @"收礼记录";
    [self.navigationController pushViewController:vc animated:YES];
    
    
    
}
//送礼记录
- (IBAction)sendGiftsAction:(id)sender {
    HJBillListController *vc = (HJBillListController *)[[HJPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
    vc.type = BillType_GiftOut;
    vc.title = @"送礼记录";
     [self.navigationController pushViewController:vc animated:YES];
}
//充值记录
- (IBAction)rechargeRecordAction:(id)sender {
    HJBillListController *vc = (HJBillListController *)[[HJPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
       vc.type = BillType_Recharge;
    vc.title = @"开心收入";
        [self.navigationController pushViewController:vc animated:YES];
}
//提取记录
- (IBAction)fetchRecordAction:(id)sender {
    HJBillListController *vc = (HJBillListController *)[[HJPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
          vc.type = BillType_Withdraw;
    vc.title = @"主播收益分成兑换记录";
           [self.navigationController pushViewController:vc animated:YES];
    
}
//红包提取记录
- (IBAction)redPacketRecordAction:(id)sender {
//    XCBillListController *vc = (XCBillListController *)[[XCPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
//          vc.type = BillType_RedWithdraw;
//    vc.title = @"红包提取记录";
//           [self.navigationController pushViewController:vc animated:YES];
    
}
//代充记录
- (IBAction)daiChongRecordAction:(id)sender {
//    XCBillListController *vc = (XCBillListController *)[[XCPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
//             vc.type = BillType_daiChong;
//       vc.title = @"转赠记录";
//              [self.navigationController pushViewController:vc animated:YES];
    
}



@end
