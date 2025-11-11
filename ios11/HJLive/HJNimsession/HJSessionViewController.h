//
//  HJSessionViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "NIMSessionViewController.h"
#import "NIMKit.h"
NS_ASSUME_NONNULL_BEGIN

@interface HJSessionViewController : NIMSessionViewController
@property (nonatomic,copy) NSString *familyID;

@property (nonatomic, copy) UINavigationController *(^roomMessageListDidSelectCell)(void);

@end

NS_ASSUME_NONNULL_END
