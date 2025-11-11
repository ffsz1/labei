//
//  HJAboutViewControllerFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYStoryboardViewControllerFactory.h"

@interface HJAboutViewControllerFactory : YYStoryboardViewControllerFactory

+ (instancetype)sharedFactory;

- (UIViewController *)instantiateAboutController;

@end
