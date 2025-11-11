//
//  YPMMHomeInfoModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPMMHomeItemModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPMMHomeInfoModel : YPBaseObject

@property (nonatomic, copy) NSString *mcoinNum; ///< 萌币数
@property (nonatomic, strong) NSArray<YPMMHomeItemModel *> *beginnerMissions; ///< 新手任务
@property (nonatomic, strong) NSArray<YPMMHomeItemModel *> *dailyMissions; ///< 每日任务
@property (nonatomic, strong) NSArray<YPMMHomeItemModel *> *weeklyMissions; ///< 每周任务


@end

NS_ASSUME_NONNULL_END
