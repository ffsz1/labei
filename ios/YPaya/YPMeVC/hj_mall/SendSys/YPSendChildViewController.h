//
//  YPSendChildViewController.h
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYTableViewController.h"

@interface YPSendChildViewController : UITableViewController
@property (nonatomic, assign) BOOL isFriend;
@property (nonatomic, copy) NSString *proId;
@property (nonatomic, assign) BOOL isCarSys;
@property (nonatomic, copy) NSString *sendName;
@end
