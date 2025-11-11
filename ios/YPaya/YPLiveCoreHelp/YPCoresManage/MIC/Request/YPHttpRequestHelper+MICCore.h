//
//  YPHttpRequestHelper+MICCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

@interface YPHttpRequestHelper (MICCore)

+ (void)getCharmUserListWithSuccess:(void (^)(NSArray *list))success
                       failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getLinkPoolWithSuccess:(void (^)(NSArray *list))success
                       failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getMICLinkUserSuccess:(void (^)(NSDictionary *userInfo))success
                      failure:(void (^)(NSNumber *, NSString *))failure;

+ (void)getSoundMatchCharmUserWithSuccess:(void (^)(NSArray *list))success
                                  failure:(void (^)(NSNumber *, NSString *))failure;
@end
