//
//  HJSessionListViewController.h
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
//  消息列表界面

#import "NIMKit.h"
#import "ZJScrollPageViewDelegate.h"

@interface HJSessionListViewController : NIMSessionListViewController <ZJScrollPageViewChildVcDelegate>

@property (nonatomic, copy) void(^roomMessageListDidSelectCell)(NSInteger row);

@property (nonatomic,assign) BOOL isFromRoom;

@end
