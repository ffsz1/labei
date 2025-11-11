//
//  NTESCardMemberItem.m
//  NIM
//
//  Created by chris on 15/3/5.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMCardMemberItem.h"
#import "YPNIMUsrInfoData.h"
#import "YPNIMKitUtil.h"
#import "YPNIMKit.h"

@interface NIMTeamCardMemberItem()
@property (nonatomic,readwrite,strong) NIMTeamMember *member;
@property (nonatomic,copy)   NSString      *userId;
@end;

@implementation NIMTeamCardMemberItem

- (instancetype)initWithMember:(NIMTeamMember*)member{
    self = [self init];
    if (self) {
        _member  = member;
        _userId  = member.userId;
    }
    return self;
}

- (BOOL)isEqual:(id)object{
    if (![object isKindOfClass:[NIMTeamCardMemberItem class]]) {
        return NO;
    }
    NIMTeamCardMemberItem *obj = (NIMTeamCardMemberItem*)object;
    return [obj.memberId isEqualToString:self.memberId];
}

- (NSString *)imageUrl{
    return [[YPNIMKit sharedKit] infoByUser:_member.userId option:nil].avatarUrlString;
}

- (NIMTeamMemberType)type {
    return _member.type;
}

- (void)setType:(NIMTeamMemberType)type {
    _member.type = type;
}

- (NSString *)title {
    NIMSession *session = [NIMSession session:self.member.teamId type:NIMSessionTypeTeam];
    return [YPNIMKitUtil showNick:self.member.userId inSession:session];
}

- (NIMTeam *)team {
    return [[NIMSDK sharedSDK].teamManager teamById:_member.teamId];
}

#pragma mark - TeamCardHeaderData

- (UIImage*)imageNormal{
     YPNIMKitInfo *info = [[YPNIMKit sharedKit] infoByUser:self.member.userId option:nil];
    return info.avatarImage;
}

- (UIImage*)imageHighLight{
    YPNIMKitInfo *info = [[YPNIMKit sharedKit] infoByUser:self.member.userId option:nil];
    return info.avatarImage;
}

- (NSString*)memberId{
    return self.member.userId;
}

- (NIMKitCardHeaderOpeator)opera{
    return CardHeaderOpeatorNone;
}

@end



@interface NIMUserCardMemberItem()
@property (nonatomic,strong) YPNIMKitInfo *info;
@end;

@implementation NIMUserCardMemberItem

- (instancetype)initWithUserId:(NSString*)userId{
    self = [self init];
    if (self) {
        _info = [[YPNIMKit sharedKit] infoByUser:userId option:nil];
    }
    return self;
}

- (BOOL)isEqual:(id)object{
    if (![object isKindOfClass:[NIMUserCardMemberItem class]]) {
        return NO;
    }
    NIMUserCardMemberItem *obj = (NIMUserCardMemberItem*)object;
    return [obj.memberId isEqualToString:self.memberId];
}

#pragma mark - TeamCardHeaderData

- (UIImage*)imageNormal{
    return self.info.avatarImage;
}

- (NSString *)imageUrl{
    return self.info.avatarUrlString;
}

- (NSString*)title{
    return self.info.showName;
}

- (NSString*)memberId{
    return self.info.infoId;
}

- (NIMKitCardHeaderOpeator)opera{
    return CardHeaderOpeatorNone;
}

@end
