//
//  AccountInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

typedef NS_ENUM(NSInteger,XCThirdPartLoginType) {
    XCThirdPartLoginWc = 1,
    XCThirdPartLoginQQ = 2,
    XCPhoneRegister = 3,
    XCPhoneLogin = 4,
    XCPhonePwd = 5,
};

@interface AccountInfo : BaseObject<NSCopying>
@property(nonatomic, strong)NSString *access_token;
@property(nonatomic, strong)NSString *uid;
@property(nonatomic, strong)NSString *netEaseToken;
@property(nonatomic, strong)NSString *token_type;
@property(nonatomic, strong)NSString *refresh_token;
@property(nonatomic, strong)NSNumber *expires_in;
@property(nonatomic, strong)NSString *scope;
@property(nonatomic, strong)NSString *jti;
@end
