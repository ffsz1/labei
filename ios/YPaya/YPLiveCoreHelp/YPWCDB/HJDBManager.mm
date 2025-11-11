//
//  HJDBManager.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJDBManager.h"
#import "UserInfo+WCTTableCoding.h"
#import "YPAuthCoreHelp.h"
#import "CommonFileUtils.h"

static HJDBManager *instance = nil;
static NSString *const UserTable = @"user";
@interface HJDBManager ()
@property (nonatomic, strong) WCTDatabase *dataBase;
@property (nonatomic, strong) WCTTable    *table;

@end

@implementation HJDBManager
{
    dispatch_queue_t writeQueue;
}

#pragma mark - Life Cycle
+ (HJDBManager *)defaultManager {
    if (instance) {
        return instance;
    }
    @synchronized (self) {
        if (instance == nil) {
            instance = [[HJDBManager alloc] init];
            [instance creatUserDB];
        }
    }
    return instance;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        writeQueue = dispatch_queue_create("com.yy.face.YYFace.UserDb", DISPATCH_QUEUE_SERIAL);
    }
    return self;
}

#pragma mark - CreatOrUpdate

- (void)creatOrUpdateUser:(UserInfo *)user complete:(void (^)(void))complete {
    BOOL result = NO;
    dispatch_async(writeQueue, ^{
        [_table insertOrReplaceObject:user];
        if (complete) {
            complete();
        }
    });
}

- (void)creatOrUpdateUsers:(NSArray *)users {
    BOOL result = NO;
    if (users.count > 0) {
        dispatch_async(writeQueue, ^{
            [_table insertOrReplaceObjects:users];
        });
    }
}

#pragma mark - Creat UserDB
- (BOOL)creatUserDB {
    [WCTStatistics SetGlobalErrorReport:^(WCTError *error) {
        [YYLogger info:TWCDB message:@"create WCDB Table failure at Document  %@", error.description];
//        NSLog(@"[WCDB]%@", error.description);
    }];

    NSString *databasePath = [self getDBPath];
//    if (![CommonFileUtils isFileExists:databasePath]) {
        _dataBase = [[WCTDatabase alloc] initWithPath:databasePath];
//    }
    
//    BOOL isExist = [_dataBase isTableExists:NSStringFromClass(UserInfo.class)];
//    if (!isExist) {
        BOOL result = [_dataBase createTableAndIndexesOfName:NSStringFromClass(UserInfo.class)
                                                   withClass:UserInfo.class];
        if (result) {
            _table = [_dataBase getTableOfName:NSStringFromClass(UserInfo.class) withClass:UserInfo.class];
        }
//    }else {
//        _table = [_dataBase getTableOfName:NSStringFromClass(UserInfo.class) withClass:UserInfo.class];
//    }
    return YES;
}

- (NSString *)getDBPath {
    NSString *path = [NSString stringWithFormat:@"Documents/database/%@",[GetCore(YPAuthCoreHelp) getUid]];
    NSString *databasePath = [NSHomeDirectory() stringByAppendingPathComponent:path];
    NSString *dbName = [NSString stringWithFormat:@"%@.db",[GetCore(YPAuthCoreHelp)getUid]];
    databasePath = [databasePath stringByAppendingPathComponent:dbName];
    return databasePath;
}

+ (void)destroy {
    instance = nil;
}

#pragma mark - Insert
- (BOOL)insertUser:(UserInfo *)user {
    //    return [_table insertOrReplaceObject:user];
//    return [_table insertObject:user];
    return [_dataBase insertOrReplaceObject:user into:NSStringFromClass(UserInfo.class)];
}

#pragma mark - Update
- (BOOL)updateUser:(UserInfo *)user {
    return [_table updateRowsOnProperties:UserInfo.AllProperties
                               withObject:user
                                    where:UserInfo.uid == user.uid];
}

#pragma mark - Delete
- (BOOL)deletUser:(UserInfo *)user {
    return [_table deleteObjectsWhere:UserInfo.uid == user.uid];
}

- (BOOL)deletAllUsers {
    return [_table deleteAllObjects];
}

#pragma mark - Select
- (void)getUserWithUserID:(UserID)userID success:(void (^)(UserInfo *userInfo))success {
    dispatch_async(writeQueue, ^{
//        [_table insertOrReplaceObject:user];
        UserInfo *info = [_table getOneObjectWhere:UserInfo.uid == userID];
        success(info);
    });
//    return ;
}

- (UserInfo *)getUserWithUserID:(UserID)userID {
    return [_table getOneObjectWhere:UserInfo.uid == userID];
}

- (NSArray<UserInfo *> *)getUsersWithUserIDs:(NSArray *)uid {

    NSMutableArray *users = [NSMutableArray array];
    for (NSNumber *userId in uid) {
        UserInfo *userInfo = [self getUserWithUserID:userId.userIDValue];
        [users addObject:userInfo];
    }
    return users;
}

- (NSArray *)getAllUser {
    return [_table getAllObjects];
}


@end
