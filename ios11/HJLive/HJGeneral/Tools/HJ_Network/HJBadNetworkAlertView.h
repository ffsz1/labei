//
//  HJBadNetworkAlertView.h
//  HJLive
//
//  Created by feiyin on 2020/6/20.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJBadNetworkAlertView : UIView


@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *desc;
@property (nonatomic, copy) void(^leftBlock)(void);
@property (nonatomic, copy) void(^rightBlock)(void);


+ (instancetype)loadFromNib;

@end
