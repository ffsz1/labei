//
//  YPVerifyCodeCore.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

// 校验状态
typedef NS_ENUM(NSInteger, JXVerifyState) { ///< 校验状态
    JXVerifyStateBegin = 1,                 ///< 校验开始
    JXVerifyStateSuccess,                   ///< 校验成功
    JXVerifyStateFailure,                   ///< 校验失败
    JXVerifyStateCancel,                    ///< 校验取消
    JXVerifyStateFinish                     ///< 校验完成
};

/**
 校验视图回调处理
 
 @param state        校验状态
 @param valideCode   验证码, 当且仅当state为JXVerifyStateSuccess时返回
 @param errorMessage 错误信息, 当且仅当state为JXVerifyStateFailure时返回
 */
typedef void(^JXOpenVerifyCodeHandler)(JXVerifyState state, NSString *valideCode, NSString *errorMessage);

@interface YPVerifyCodeCore : YPBaseCore
/**
 显示验证界面

 @param completionHandler 处理回调
 */
- (void)openVerifyCodeViewWithCompletionHandler:(JXOpenVerifyCodeHandler)completionHandler;

@end
