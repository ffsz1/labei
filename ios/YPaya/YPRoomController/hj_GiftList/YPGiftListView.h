//
//  YPGiftListView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPGiftListView : UIView

@property (nonatomic, copy) void(^didClickCloseBtnBlock)(void);
@property (copy, nonatomic) void (^alertUserInformation)(long long uid);

- (void)removeCore;

@end
