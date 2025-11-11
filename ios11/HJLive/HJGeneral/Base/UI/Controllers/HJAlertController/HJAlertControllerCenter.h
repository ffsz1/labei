//
//  HJAlertControllerCenter.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <TYAlertController.h>
NS_ASSUME_NONNULL_BEGIN

@interface HJAlertControllerCenter : NSObject

@property (nonatomic, assign) CGFloat alertViewOriginY;
@property (strong, nonatomic) TYAlertController *alert;
@property (nonatomic, copy) void (^dismissComplete)(void);
+ (instancetype)defaultCenter;
- (void)presentAlertWith:(UIViewController *)alertParaents view:(UIView *)view dismissBlock:(void(^)(void))dismissBlock;
- (void)presentAlertWith:(UIViewController *)alertParaents view:(UIView *)view preferredStyle:(TYAlertControllerStyle)preferredStyle dismissBlock:(void (^)(void))dismissBlock completionBlock:(void (^)(void))completionBlock;
- (void)dismissAlertNeedBlock:(BOOL)needBlock;

@end

NS_ASSUME_NONNULL_END
