//
//  HJHttpRequestHelper+Youth.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (Youth)

+ (void)getUsersTeensMode:(void(^)(BOOL hadSet))success
                  failure:(void(^)(NSNumber *resCode, NSString *message))failure;

@end

