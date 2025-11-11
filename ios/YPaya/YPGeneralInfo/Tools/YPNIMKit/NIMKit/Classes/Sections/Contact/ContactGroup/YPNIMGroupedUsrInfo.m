//
//  NTESGroupedUsrInfo.m
//  NIM
//
//  Created by Xuhui on 15/3/24.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMGroupedUsrInfo.h"
#import "YPNIMKit.h"
#import "YPNIMSpellingCenter.h"
#import "YPNIMKitInfoFetchOption.h"

@interface NIMGroupUser()

@property (nonatomic,copy)   NSString *userId;

@property (nonatomic,strong) YPNIMKitInfo *info;

@end

@implementation NIMGroupUser

- (instancetype)initWithUserId:(NSString *)userId{
    self = [super init];
    if (self) {
        _userId = userId;
        _info = [[YPNIMKit sharedKit] infoByUser:userId option:nil];
    }
    return self;
}

- (NSString *)groupTitle{
    NSString *title = [[YPNIMSpellingCenter sharedCenter] firstLetter:self.info.showName].capitalizedString;
    unichar character = [title characterAtIndex:0];
    if (character >= 'A' && character <= 'Z') {
        return title;
    }else{
        return @"#";
    }
}

- (NSString *)showName{
    return self.info.showName;
}

- (NSString *)memberId{
    return self.userId;
}

- (id)sortKey{
    return [[YPNIMSpellingCenter sharedCenter] spellingForString:self.info.showName].shortSpelling;
}

@end




@interface NIMGroupTeamMember()

@property (nonatomic,strong) NIMTeamMember *member;

@end

@implementation NIMGroupTeamMember

- (instancetype)initWithUserId:(NSString *)userId teamId:(NSString *)teamId{
    self = [super init];
    if (self) {
        _member = [[NIMSDK sharedSDK].teamManager teamMember:userId inTeam:teamId];
    }
    return self;
}

- (NSString *)groupTitle{
    NSString *title = [[YPNIMSpellingCenter sharedCenter] firstLetter:self.showName].capitalizedString;
    unichar character = [title characterAtIndex:0];
    if (character >= 'A' && character <= 'Z') {
        return title;
    }else{
        return @"#";
    }
}

- (NSString *)memberId{
    return self.member.userId;
}

- (id)sortKey{
    return [[YPNIMSpellingCenter sharedCenter] spellingForString:self.showName].shortSpelling;
}

- (NSString *)showName{
    NIMSession *session = [NIMSession session:self.member.teamId type:NIMSessionTypeTeam];
    YPNIMKitInfoFetchOption *option = [[YPNIMKitInfoFetchOption alloc] init];
    option.session = session;
    YPNIMKitInfo *info = [[YPNIMKit sharedKit] infoByUser:self.memberId option:option];
    return info.showName;
}


@end

@interface NIMGroupTeam()

@property (nonatomic,strong) NIMTeam *team;

@end

@implementation NIMGroupTeam

- (instancetype)initWithTeam:(NSString *)teamId{
    self = [super init];
    if (self) {
        _team = [[NIMSDK sharedSDK].teamManager teamById:teamId];
    }
    return self;
}

- (NSString *)groupTitle{
    NSString *title = [[YPNIMSpellingCenter sharedCenter] firstLetter:self.team.teamName].capitalizedString;
    unichar character = [title characterAtIndex:0];
    if (character >= 'A' && character <= 'Z') {
        return title;
    }else{
        return @"#";
    }
}

- (NSString *)memberId{
    return self.team.teamId;
}

- (id)sortKey{
    return [[YPNIMSpellingCenter sharedCenter] spellingForString:self.team.teamName].shortSpelling;
}

- (NSString *)showName{
    return self.team.teamName;
}


@end


