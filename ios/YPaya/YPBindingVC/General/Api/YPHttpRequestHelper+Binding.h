//
//  YPHttpRequestHelper+Binding.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPHttpRequestHelper (Binding)

//获取绑定手机的验证码
+ (void)getBindingSmsCode:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure;

//校验绑定手机验证码
+ (void)bindingValidateCode:(NSString *)code success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure;

//绑定第三方 type 1.微信 2.QQ
+ (void)bindThird:(NSString *)openId unionId:(NSString *)unionId accessToken:(NSString *)accessToken type:(NSInteger)type success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure;

//解绑第三方 type 1.微信 2.QQ
+ (void)untiedThird:(NSInteger)type success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure;


@end

NS_ASSUME_NONNULL_END
