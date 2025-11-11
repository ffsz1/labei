//
//  HJImFriendCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJImFriendCoreClient <NSObject>
@optional
- (void)onRequestFriendSuccess;
- (void)onRequestFriendFailth;
- (void)onDeleteFriendSuccess;
- (void)onDeleteFirendFailth;
- (void)onAddToBlackListSuccess;
- (void)onAddToBlackListFailth;
- (void)onRemoveFromBlackListSuccess;
- (void)onRemoveFromBlackListFailth;

- (void)onFriendChanged;
- (void)onBlackListChanged;
- (void)onMuteListChanged;
- (void)onRecieveFriendAddNoti:(NSString *)uid;

- (void)onFriendAdded;
@end
