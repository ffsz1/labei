//
//  HJHomeMengxinModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJHomeMengxinModel : BaseObject
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *nick;
@property(nonatomic, assign) UserID uid;
@property (nonatomic, assign) long gender;
@property (nonatomic, assign) long signTime;
@property (nonatomic, assign) long glamour;
@property (nonatomic, assign) long signature;
@property (nonatomic, copy) NSString *userVoice;
@property (nonatomic, assign) long voiceDuration;
@property (nonatomic, copy) NSString *userDescription;
@property(nonatomic, assign) BOOL isFirstCharge;
@property (nonatomic, assign) long experLevel;


@end

NS_ASSUME_NONNULL_END
