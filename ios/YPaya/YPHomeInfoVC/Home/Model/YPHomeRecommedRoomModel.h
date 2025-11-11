//
//  YPHomeRecommedRoomModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPHomeRecommedRoomModel : YPBaseObject
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *title;
@property(nonatomic, assign) UserID uid;
@property (nonatomic, assign) int onlineNum;
@end

NS_ASSUME_NONNULL_END
