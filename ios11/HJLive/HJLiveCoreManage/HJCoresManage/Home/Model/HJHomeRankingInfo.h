//
//  HJHomeRankingInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"

@interface HJHomeRankingInfo : BaseObject
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *erbanNo;
@property (nonatomic, copy) NSString *nick;
@property (nonatomic, assign) int gender;
@property (nonatomic, assign) int totalNum;
@end
