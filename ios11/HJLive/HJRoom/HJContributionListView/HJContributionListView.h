//
//  HJContributionListView.h
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJContributionListView : UIView
@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIButton *RiBtn;
@property (weak, nonatomic) IBOutlet UIButton *ZhBtn;
@property (weak, nonatomic) IBOutlet UIButton *AllBtn;
@property (weak, nonatomic) IBOutlet UILabel *bLineT;
@property (weak, nonatomic) IBOutlet UISegmentedControl *lvOrmlSeg;

@property (weak, nonatomic) IBOutlet UIImageView *bLineImg;
@property (copy, nonatomic) void (^alertUserInformation)(long long uid);
- (void)removeCore;
@end
