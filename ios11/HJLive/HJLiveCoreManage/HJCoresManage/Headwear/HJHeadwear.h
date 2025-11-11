//
//  HJHeadwear.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJHeadwear : BaseCore
- (void)sendHeadwearWithHeadwearId:(NSString *)headwearId
                         targetUid:(NSString *)targetUid;
- (void)giftHeadwearUse:(NSString *)HeadwearID;
- (void)getHeadwearListWithPageNum:(NSString *)pageNum PageSize:(NSString *)pageSize;
- (void)buyHeadwearWithHeadwearID:(NSString *)HeadwearID withType:(NSString *)type;
@end
