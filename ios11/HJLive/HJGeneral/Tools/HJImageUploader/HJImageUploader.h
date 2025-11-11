//
//  HJImageUploader.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
typedef void(^HJImageUploaderSucceed)(NSString * url);
typedef void(^HJImageUploaderFailed)(int code,NSString *msg);
@interface HJImageUploader : NSObject
+ (void)uploadImage:(UIImage *)image succeed:(HJImageUploaderSucceed)succeedBlock  failed:(HJImageUploaderFailed)failedBlock;
@end

NS_ASSUME_NONNULL_END
