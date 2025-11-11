//
//  CoreError.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "CoreError.h"

@implementation CoreError

- (instancetype)initWithDomain:(NSErrorDomain)domain
                          code:(NSInteger)code
                       message:(NSString *)message
{
    NSDictionary *userInfo = @{NSLocalizedDescriptionKey: message};
    return [super initWithDomain:domain code:code userInfo:userInfo];
}

- (instancetype)initWithDomain:(NSErrorDomain)domain
                       resCode:(NSInteger)resCode
                    resMessage:(NSString *)resMessage
{
    NSString *errorMessage = [NSString stringWithFormat:@"resCode/msg %ld %@", (long)resCode, resMessage];
    return [self initWithDomain:domain code:CommonErrorCodeAPI_Error message:errorMessage];
}

- (instancetype)initWithDomain:(NSErrorDomain)domain
                       sdkCode:(NSInteger)sdkCode
{
    return [self initWithDomain:domain code:CommonErrorCoreSDK_Error message:nil];
}

- (NSString *)description
{
    NSDictionary *userInfo = self.userInfo;
    if (userInfo != nil) {
        return [userInfo objectForKey:NSLocalizedDescriptionKey];
    } else {
        return @"";
    }
}

@end
