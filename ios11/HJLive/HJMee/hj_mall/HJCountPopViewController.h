//
//  HJCountPopViewController.h
//  HJLive
//
//  Created by MacBook on 2020/8/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJCountPopViewController : UIViewController
@property (copy, nonatomic) void(^selectedCountAction)(NSInteger index,NSString * count);
@end

NS_ASSUME_NONNULL_END
