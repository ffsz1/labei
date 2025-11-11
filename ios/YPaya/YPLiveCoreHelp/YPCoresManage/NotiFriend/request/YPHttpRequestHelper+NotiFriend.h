//
//  YPHttpRequestHelper+NotiFriend.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPHttpRequestHelper (NotiFriend)
/**
 请求首页tag数据
 */
+ (void)requestLobbyChatInfo:(void (^)(NSArray *))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure;
@end

NS_ASSUME_NONNULL_END
