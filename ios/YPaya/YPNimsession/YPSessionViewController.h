//
//  YPSessionViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNIMSessionViewController.h"
#import "YPNIMKit.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPSessionViewController : YPNIMSessionViewController
@property (nonatomic,copy) NSString *familyID;

@property (nonatomic, copy) UINavigationController *(^roomMessageListDidSelectCell)(void);

@end

NS_ASSUME_NONNULL_END
