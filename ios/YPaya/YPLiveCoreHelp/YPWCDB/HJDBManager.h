//
//  HJDBManager.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserInfo.h"
NS_ASSUME_NONNULL_BEGIN

@interface HJDBManager : NSObject
#pragma mark - Life Cycle
+ (HJDBManager *)defaultManager;
+ (void)destroy;

#pragma mark - CreatOrUpdate

- (void)creatOrUpdateUser:(UserInfo *)user complete:(void (^)(void))complete;
- (void)creatOrUpdateUsers:(NSArray *)users;

#pragma mark - Insert
- (BOOL)insertUser:(UserInfo *)user;

#pragma mark - Update
- (BOOL)updateUser:(UserInfo *)user;

#pragma mark - Delete
- (BOOL)deletUser:(UserInfo *)user;
- (BOOL)deletAllUsers;

#pragma mark - Select
- (void)getUserWithUserID:(UserID)userID success:(void (^)(UserInfo *userInfo))success;
- (UserInfo *)getUserWithUserID:(UserID)userID;
- (NSArray<UserInfo *> *)getUsersWithUserIDs:(NSArray *)uid;
- (NSArray *)getAllUser;
@end

NS_ASSUME_NONNULL_END
