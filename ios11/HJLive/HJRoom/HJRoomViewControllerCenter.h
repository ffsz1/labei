//
//  HJRoomViewControllerCenter.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ChatRoomInfo.h"



@interface HJRoomViewControllerCenter : NSObject
+ (instancetype)defaultCenter;
@property(nonatomic, strong)UINavigationController *current;
@property(nonatomic, strong)UINavigationController *mainNavController;
@property(nonatomic, assign)BOOL systemOperationStatusBarIsShow;

- (void)openRoonWithType:(RoomType)type;//开启制定类型方法
- (void)presentRoomViewWithRoomOwnerUid:(UserID) ownerUid
                                   succ:(void (^)(ChatRoomInfo *roomInfo))succBlock
                                   fail:(void (^)(NSString *errorMsg))failBlock;  //根据房主id进入房间


- (void)presentRoomViewWithRoomInfo:(ChatRoomInfo *)roomInfo;//根据房间info present房间

- (void)dismissChannelViewWithQuitCurrentRoom:(BOOL)isQuit; //推出房间
- (void)dismissChannelViewWithQuitCurrentRoom:(BOOL)isQuit animation:(BOOL)animation;//推出房间
- (UIViewController *)getCurrentVC;
- (UINavigationController *)currentNavigationController;
- (void)startRoomTimer;
@property(nonatomic, strong) ChatRoomInfo *currentRoomInfo;
@end
