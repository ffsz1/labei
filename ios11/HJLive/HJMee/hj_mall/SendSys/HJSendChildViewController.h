//
//  HJSendChildViewController.h
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYTableViewController.h"

@interface HJSendChildViewController : UITableViewController
@property (nonatomic, assign) BOOL isFriend;
@property (nonatomic, copy) NSString *proId;
@property (nonatomic, assign) BOOL isCarSys;
@property (nonatomic, copy) NSString *sendName;
@end
