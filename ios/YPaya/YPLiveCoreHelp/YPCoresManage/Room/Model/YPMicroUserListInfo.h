//
//  YPMicroUserListInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBaseObject.h"

@interface YPMicroUserListInfo : YPBaseObject

@property (assign, nonatomic) UserID uid; //uid
@property (copy, nonatomic) NSString *nick; //用户昵称
@property (copy, nonatomic) NSString *avatar; //用户头像
@property (copy, nonatomic) NSString *seqNo; //排序编号
@property (assign, nonatomic) NSInteger status; //用户状态  1房主发起邀请中，2在麦序上

@end
