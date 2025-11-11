//
//  YPVerifyCodeCore.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPVerifyCodeCore.h"
#import <VerifyCode/NTESVerifyCodeManager.h>

@interface YPVerifyCodeCore() <NTESVerifyCodeManagerDelegate>
@property (nonatomic, strong) NTESVerifyCodeManager *manager;
@property (nonatomic, copy) JXOpenVerifyCodeHandler completionHandler;
@end

@implementation YPVerifyCodeCore
- (instancetype)init {
    self = [super init];
    if (self) {
//        [self initCore];
    }
    return self;
}

- (void)initCore {
    // sdk调用
    self.manager = [NTESVerifyCodeManager getInstance];
    // 设置语言
    self.manager.lang = NTESVerifyCodeLangCN;
    // 设置透明度
    self.manager.alpha = 0;
    // 设置frame
    self.manager.frame = CGRectNull;
    
    NSString *captchaId = JX_NTES_VERIFY_CAPTCHA_ID;
    [self.manager configureVerifyCode:captchaId timeout:10.0];
    self.manager.delegate = self;
}

- (void)openVerifyCodeViewWithCompletionHandler:(JXOpenVerifyCodeHandler)completionHandler {
    if (completionHandler) {
        self.completionHandler = completionHandler;
    }
    !self.completionHandler ?: self.completionHandler(JXVerifyStateBegin, nil, nil);
    [self.manager openVerifyCodeView];
}

#pragma mark - <NTESVerifyCodeManagerDelegate>
- (void)verifyCodeInitFinish {
}

- (void)verifyCodeInitFailed:(NSString *)error {
    !self.completionHandler ?: self.completionHandler(JXVerifyStateFailure, nil, error);
    !self.completionHandler ?: self.completionHandler(JXVerifyStateFinish, nil, nil);
}

- (void)verifyCodeValidateFinish:(BOOL)result validate:(NSString *)validate message:(NSString *)message {
    if (result) {
        !self.completionHandler ?: self.completionHandler(JXVerifyStateSuccess, validate, nil);
        !self.completionHandler ?: self.completionHandler(JXVerifyStateFinish, nil, nil);
        return;
    }
    
    !self.completionHandler ?: self.completionHandler(JXVerifyStateFailure, nil, message);
}

- (void)verifyCodeCloseWindow {
    !self.completionHandler ?: self.completionHandler(JXVerifyStateCancel, nil, nil);
    !self.completionHandler ?: self.completionHandler(JXVerifyStateFinish, nil, nil);
}

- (void)verifyCodeNetError:(NSError *)error {
    !self.completionHandler ?: self.completionHandler(JXVerifyStateFailure, nil, @"网络错误");
}

@end
