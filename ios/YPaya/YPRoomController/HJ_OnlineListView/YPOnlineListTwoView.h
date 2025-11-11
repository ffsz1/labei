//
//  YPOnlineListTwoView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPOnlineListTwoView : UIView
@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
- (void)removeCore;
@property (copy, nonatomic) void (^alertUserInformation)(long long uid);
@property (copy, nonatomic) void (^sendGiftBlock)(long long uid,NSString *nick);

@end
