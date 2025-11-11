//
//  HJMMHomeInfoModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "HJMMHomeItemModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJMMHomeInfoModel : BaseObject

@property (nonatomic, copy) NSString *mcoinNum; ///< 萌币数
@property (nonatomic, strong) NSArray<HJMMHomeItemModel *> *beginnerMissions; ///< 新手任务
@property (nonatomic, strong) NSArray<HJMMHomeItemModel *> *dailyMissions; ///< 每日任务
@property (nonatomic, strong) NSArray<HJMMHomeItemModel *> *weeklyMissions; ///< 每周任务


@end

NS_ASSUME_NONNULL_END
