//
//  YPMMHomeItemModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPMMHomeItemModel : YPBaseObject

@property (nonatomic, copy) NSString *mcoinAmount;   ///< 金额
@property (nonatomic, copy) NSString *missionId;     ///< 任务ID
@property (nonatomic, copy) NSString *missionName;   ///< 任务名
@property (nonatomic, copy) NSString *missionStatus; ///< 任务状态
@property (nonatomic, copy) NSString *scheme;        ///< 路由
@property (nonatomic, copy) NSString *picUrl;        ///< 图片

@end

NS_ASSUME_NONNULL_END
