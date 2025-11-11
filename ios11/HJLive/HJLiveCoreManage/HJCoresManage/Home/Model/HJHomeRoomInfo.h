//
//  HJHomeRoomInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"

@interface HJHomeRoomInfo : BaseObject

@property (nonatomic, copy)NSString *avatar;
@property (nonatomic, copy)NSString *ID;
@property (nonatomic, copy) NSString *roomTag;
@property (nonatomic, copy) NSString *roomId;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *uid;
@property (nonatomic, copy) NSString *createTime;
@property (nonatomic, copy) NSString *erbanNo;
@property (nonatomic, assign) BOOL isSelect; // 是否被选中
@property (nonatomic, assign) NSInteger onlineNum;
@property (nonatomic, assign) NSInteger status; ///< 1.已关注  0  未关注

@end
