//
//  PraiseCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJPraiseCore : BaseCore

//关注
- (void) praise:(UserID)paiseUid bePraisedUid:(UserID)bePraisedUid;
//取消关注
- (void) cancel:(UserID)cancelUid beCanceledUid:(UserID)beCanceledUid;
//delete
- (void) deleteFriend:(UserID)deleteUid beDeletedUid:(UserID)beDeletedUid;
//获取关注列表
- (void) requestAttentionListState:(int)state page:(int)page;


//获取粉丝列表
- (void) requestFansListState:(int)state page:(NSInteger)page;
//判断是否关注某人
- (void) isLike:(UserID)uid isLikeUid:(UserID)isLikeUid;

@end
