//
//  HJZaJinDanRankList.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJZaJinDanRankList : UIView

@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UISegmentedControl *lvOrmlSeg;

@property (copy, nonatomic) void (^alertUserInformation)(long long uid);
- (void)removeCore;

@end
