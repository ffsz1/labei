//
//  YPRoomLongZhuMsgModel.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPRoomLongZhuMsgModel : NSObject

@property (nonatomic, copy) NSString *uid;
@property (nonatomic, copy) NSString *nick;
@property (nonatomic, strong) NSArray *numArr;
@property (nonatomic, assign) BOOL isShowd;
@property (nonatomic, assign) NSInteger level;

@end
