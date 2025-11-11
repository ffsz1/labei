//
//  YPBillCatalogueVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBillCatalogueVC.h"
#import "YPBillListController.h"
#import "YPPurseViewControllerFactory.h"


@interface YPBillCatalogueVC ()

@end

@implementation YPBillCatalogueVC

- (void)viewDidLoad {
    [super viewDidLoad];
    YPBlackStatusBar
}


//收礼记录
- (IBAction)receivingGiftsAction:(id)sender {
    YPBillListController *vc = (YPBillListController *)[[YPPurseViewControllerFactory sharedFactory] instantiateBillListViewController];
    vc.type = BillType_GiftIn;
    vc.title = @"收礼记录";
    [self.navigationController pushViewController:vc animated:YES];
    
    
    
}
//送礼记录
- (IBAction)sendGiftsAction:(id)sender {
    YPBillListController *vc = (YPBillListController *)[[YPPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
    vc.type = BillType_GiftOut;
    vc.title = @"送礼记录";
     [self.navigationController pushViewController:vc animated:YES];
}
//充值记录
- (IBAction)rechargeRecordAction:(id)sender {
    YPBillListController *vc = (YPBillListController *)[[YPPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
       vc.type = BillType_Recharge;
    vc.title = @"金币收入";
        [self.navigationController pushViewController:vc animated:YES];
}
//提取记录
- (IBAction)fetchRecordAction:(id)sender {
    YPBillListController *vc = (YPBillListController *)[[YPPurseViewControllerFactory sharedFactory]instantiateBillListViewController];
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
