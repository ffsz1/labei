//
//  CarSysCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJCarSysCore : BaseCore

- (void)sendCarWithCarId:(NSString *)carId
               targetUid:(NSString *)targetUid;
- (void)giftCarUse:(NSString *)carId;
- (void)getCarSysListWithPageNum:(NSString *)pageNum PageSize:(NSString *)pageSize;
- (void)buyCarSysWithCarId:(NSString *)carId withType:(NSString *)type;


- (void)getUserCarSysList:(UserID)userId
                  PageNum:(NSString *)pageNum
                 PageSize:(NSString *)pageSize;

- (void)getUserHeadList:(UserID)userId
                PageNum:(NSString *)pageNum
               PageSize:(NSString *)pageSize;

@end
