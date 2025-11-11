//
//  YPMallViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPMallViewController : UIViewController
@property (nonatomic ,assign) UserID sendToUid;
@property (nonatomic ,copy) NSString *sendToUserName;
@property (assign, nonatomic) BOOL isZuojia;
@end

NS_ASSUME_NONNULL_END
