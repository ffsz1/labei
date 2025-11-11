//
//  HJActivityCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "ActivityInfo.h"

@interface HJActivityCore : BaseCore

@property (strong, nonatomic) ActivityInfo *activityInfo;

//获取活动
- (void)getActivity:(NSInteger)type;

//获取所有活动
- (void)getAllActivity;
@end
