//
//  HJPackViewController.h
//  HJLive
//
//  Created by MacBook on 2020/8/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJPackViewController : UIViewController
@property (nonatomic ,assign) UserID sendToUid;
@property (nonatomic ,copy) NSString *sendToUserName;
@end

NS_ASSUME_NONNULL_END
