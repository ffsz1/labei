//
//  HJRoomMemberCharmInfoModel.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface HJRoomMemberCharmInfoModel : NSObject

/**
 唯一id
 */
@property (nonatomic, copy) NSString *uid;

/**
 当前麦位成员魅力值
 */
@property (nonatomic, copy) NSString *value;

/**
 是否带帽
 */
@property (nonatomic, assign) BOOL withHat;


@end

@interface XCRoomCharmDataModel : NSObject

/**
 房间ID
 */
@property (nonatomic,copy) NSString *roomId;

/**
 最新魅力值
 */
@property (nonatomic,copy) NSDictionary *latestCharm;

/**
 收到的数据对应的时间戳
 */
@property (nonatomic,assign) NSUInteger timestamps;

@end


