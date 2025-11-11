//
//  PraiseCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJPraiseCoreClient <NSObject>

@optional
//关注
- (void) onPraiseSuccess:(UserID)uid;
- (void) onPraiseFailth:(NSString *)msg;
//取消关注
- (void) onCancelSuccess:(UserID)uid;
- (void) onCancelFailth:(NSString *)msg;

- (void) onDeleteFriendSuccess:(UserID)uid;
- (void) onDeleteFriendFailth:(NSString *)msg;
//关注列表
- (void) onRequestAttentionListState:(int)state success:(NSArray *)attentionList;
- (void) onRequestAttentionListState:(int)state failth:(NSString *)msg;

- (void) onRequestIsLikeSuccess:(BOOL)isLike islikeUid:(UserID)islikeUid;
- (void) onRequestIsLikeFailth:(NSString *)msg;

//粉丝列表
- (void) onRequestFansListState:(int)state success:(NSArray *)fansList;
- (void) onRequestFansListState:(int)state failth:(NSString *)msg;
@end
