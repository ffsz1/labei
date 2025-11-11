//
//  HttpRequestHelper+Log.h
//  XChat
//
//  Created by apple on 2019/4/16.
//  Copyright © 2019 XC. All rights reserved.
//

#import "YPHttpRequestHelper.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPHttpRequestHelper (Log)

+ (void)clientLogSaveWithUrl:(NSString *)url success:(void(^)())success failure:(void(^)(NSNumber *code, NSString *msg))failure;

@end

NS_ASSUME_NONNULL_END
