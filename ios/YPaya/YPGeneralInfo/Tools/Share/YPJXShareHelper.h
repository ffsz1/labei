//
//  YPJXShareHelper.h
//  XChat
//
//  Created by gzlx on 2019/3/18.
//  Copyright © 2019年 XC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JXShareDefine.h"
//#import "JXShareView.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPJXShareHelper : NSObject

+ (void)showShareViewWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr showFriends:(BOOL)showFriends successBlock:(void(^)())successBlock failedBlock:(void(^)())failedBlock;

+ (void)showShareViewWithType:(JXShareType)type WithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr showFriends:(BOOL)showFriends successBlock:(void(^)())successBlock failedBlock:(void(^)())failedBlock;

+ (void)shareRoom:(UserID)uid roomUid:(UserID)roomUid platform:(JXShareType)platform;

+ (void)shareH5WithTitle:(NSString *)title url:(NSString *)url imgUrl:(NSString *)imgUrl desc:(NSString *)desc platform:(JXShareType)platform;

+ (void)postShareSuccessDataShareType:(NSInteger)shareType sharePageId:(NSInteger)sharePageId targetUid:(NSInteger)targetUid;

@end

NS_ASSUME_NONNULL_END
