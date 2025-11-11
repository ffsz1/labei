//
//  YPPhotoAssetsUtility.h
//  YYMobile
//
//  Created by zhangji on 5/20/16.
//  Copyright © 2016 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPPhotoAssetsUtility : NSObject

+ (void)checkPhtotAssetsAvailable:(void(^)(void))available;

@end
