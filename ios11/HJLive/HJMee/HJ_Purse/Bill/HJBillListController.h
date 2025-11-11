//
//  HJBillListController.h
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYViewController.h"
#import "ZJScrollPageViewDelegate.h"

@protocol HJBillListControllerDelegate <NSObject>
- (void)resetDateLabel;
@end

@interface HJBillListController : YYViewController
<ZJScrollPageViewChildVcDelegate>
@property (assign, nonatomic) BillType type;
@property (assign, nonatomic) NSInteger sortTiemStamp;
- (void)reloadDateByDate:(NSDate *)date;
@property (weak, nonatomic) id<HJBillListControllerDelegate>delegate;
@end
