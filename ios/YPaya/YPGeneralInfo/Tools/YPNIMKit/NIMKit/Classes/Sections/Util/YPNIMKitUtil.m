//
//  NIMUtil.m
//  YPNIMKit
//
//  Created by chris on 15/8/10.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMKitUtil.h"
#import "YPNIMKit.h"
#import "YPNIMKitInfoFetchOption.h"

@implementation YPNIMKitUtil

+ (NSString *)genFilenameWithExt:(NSString *)ext
{
    CFUUIDRef uuid = CFUUIDCreate(nil);
    NSString *uuidString = (__bridge_transfer NSString*)CFUUIDCreateString(nil, uuid);
    CFRelease(uuid);
    NSString *uuidStr = [[uuidString stringByReplacingOccurrencesOfString:@"-" withString:@""] lowercaseString];
    NSString *name = [NSString stringWithFormat:@"%@",uuidStr];
    return [ext length] ? [NSString stringWithFormat:@"%@.%@",name,ext]:name;
}

+ (NSString *)showNick:(NSString*)uid inMessage:(NIMMessage*)message
{
    if (!uid.length) {
        return nil;
    }
    YPNIMKitInfoFetchOption *option = [[YPNIMKitInfoFetchOption alloc] init];
    option.message = message;
    if (message.messageType == NIMMessageTypeRobot)
    {
        NIMRobotObject *object = (NIMRobotObject *)message.messageObject;
        if (object.isFromRobot) {
            return [[YPNIMKit sharedKit] infoByUser:object.robotId option:option].showName;
        }
    }
    return [[YPNIMKit sharedKit] infoByUser:uid option:option].showName;
}

+ (NSString *)showNick:(NSString*)uid inSession:(NIMSession*)session{
    if (!uid.length) {
        return nil;
    }
    YPNIMKitInfoFetchOption *option = [[YPNIMKitInfoFetchOption alloc] init];
    option.session = session;
    return [[YPNIMKit sharedKit] infoByUser:uid option:option].showName;
}


+ (NSString*)showTime:(NSTimeInterval) msglastTime showDetail:(BOOL)showDetail
{
    //今天的时间
    NSDate * nowDate = [NSDate date];
    NSDate * msgDate = [NSDate dateWithTimeIntervalSince1970:msglastTime];
    NSString *result = nil;
    NSCalendarUnit components = (NSCalendarUnit)(NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitWeekday|NSCalendarUnitHour | NSCalendarUnitMinute);
    NSDateComponents *nowDateComponents = [[NSCalendar currentCalendar] components:components fromDate:nowDate];
    NSDateComponents *msgDateComponents = [[NSCalendar currentCalendar] components:components fromDate:msgDate];
    
    NSInteger hour = msgDateComponents.hour;
    double OnedayTimeIntervalValue = 24*60*60;  //一天的秒数

    result = [YPNIMKitUtil getPeriodOfTime:hour withMinute:msgDateComponents.minute];
    if (hour > 12)
    {
        hour = hour - 12;
    }
    
    BOOL isSameMonth = (nowDateComponents.year == msgDateComponents.year) && (nowDateComponents.month == msgDateComponents.month);
    
    if(isSameMonth && (nowDateComponents.day == msgDateComponents.day)) //同一天,显示时间
    {
        result = [[NSString alloc] initWithFormat:@"%@ %zd:%02d",result,hour,(int)msgDateComponents.minute];
    }
    else if(isSameMonth && (nowDateComponents.day == (msgDateComponents.day+1)))//昨天
    {
        result = showDetail?  [[NSString alloc] initWithFormat:@"%@%@ %zd:%02d",NSLocalizedString(NIMKitYestday, nil) ,result,hour,(int)msgDateComponents.minute] : NSLocalizedString(NIMKitYestday, nil);
    }
    else if(isSameMonth && (nowDateComponents.day == (msgDateComponents.day+2))) //前天
    {
        result = showDetail? [[NSString alloc] initWithFormat:@"%@%@ %zd:%02d",NSLocalizedString(NIMKitBeforeYestdat, nil),result,hour,(int)msgDateComponents.minute] : NSLocalizedString(NIMKitBeforeYestdat, nil);
    }
    else if([nowDate timeIntervalSinceDate:msgDate] < 7 * OnedayTimeIntervalValue)//一周内
    {
        NSString *weekDay = [YPNIMKitUtil weekdayStr:msgDateComponents.weekday];
        result = showDetail? [weekDay stringByAppendingFormat:@"%@ %zd:%02d",result,hour,(int)msgDateComponents.minute] : weekDay;
    }
    else//显示日期
    {
        NSString *day = [NSString stringWithFormat:@"%zd-%zd-%zd", msgDateComponents.year, msgDateComponents.month, msgDateComponents.day];
        result = showDetail? [day stringByAppendingFormat:@"%@ %zd:%02d",result,hour,(int)msgDateComponents.minute]:day;
    }
    return result;
}

#pragma mark - Private

+ (NSString *)getPeriodOfTime:(NSInteger)time withMinute:(NSInteger)minute
{
    NSInteger totalMin = time *60 + minute;
    NSString *showPeriodOfTime = @"";
    if (totalMin > 0 && totalMin <= 5 * 60)
    {
        showPeriodOfTime = NSLocalizedString(NIMKitMidnight, nil);
    }
    else if (totalMin > 5 * 60 && totalMin < 12 * 60)
    {
        showPeriodOfTime = NSLocalizedString(NIMKitMorning, nil);
    }
    else if (totalMin >= 12 * 60 && totalMin <= 18 * 60)
    {
        showPeriodOfTime = NSLocalizedString(NIMKitNoon, nil);
    }
    else if ((totalMin > 18 * 60 && totalMin <= (23 * 60 + 59)) || totalMin == 0)
    {
        showPeriodOfTime = NSLocalizedString(NIMKitNight, nil);
    }
    return showPeriodOfTime;
}

+(NSString*)weekdayStr:(NSInteger)dayOfWeek
{
    static NSDictionary *daysOfWeekDict = nil;
    daysOfWeekDict = @{@(1):NSLocalizedString(NIMKitSunday, nil),
                       @(2):NSLocalizedString(NIMKitMonday, nil),
                       @(3):NSLocalizedString(NIMKitTuesday, nil),
                       @(4):NSLocalizedString(NIMKitWedsday, nil),
                       @(5):NSLocalizedString(NIMKitThirsday, nil),
                       @(6):NSLocalizedString(NIMKitFriday, nil),
                       @(7):NSLocalizedString(NIMKitSaturday, nil),};
    return [daysOfWeekDict objectForKey:@(dayOfWeek)];
}


+ (NSString *)messageTipContent:(NIMMessage *)message{
    
    NSString *text = nil;
    
    if (text == nil) {
        switch (message.messageType) {
            case NIMMessageTypeNotification:
                text =  [YPNIMKitUtil notificationMessage:message];
                break;
            case NIMMessageTypeTip:
                text = message.text;
                break;
            default:
                break;
        }
    }
    return text;
}


+ (NSString *)notificationMessage:(NIMMessage *)message{
    NIMNotificationObject *object = message.messageObject;
    switch (object.notificationType) {
        case NIMNotificationTypeTeam:{
            return [YPNIMKitUtil teamNotificationFormatedMessage:message];
        }
        case NIMNotificationTypeNetCall:{
            return [YPNIMKitUtil netcallNotificationFormatedMessage:message];
        }
        case NIMNotificationTypeChatroom:{
            return [YPNIMKitUtil chatroomNotificationFormatedMessage:message];
        }
        default:
            return @"";
    }
}


+ (NSString*)teamNotificationFormatedMessage:(NIMMessage *)message{
    NSString *formatedMessage = @"";
    NIMNotificationObject *object = message.messageObject;
    if (object.notificationType == NIMNotificationTypeTeam)
    {
        NIMTeamNotificationContent *content = (NIMTeamNotificationContent*)object.content;
        NSString *source = [YPNIMKitUtil teamNotificationSourceName:message];
        NSArray *targets = [YPNIMKitUtil teamNotificationTargetNames:message];
        NSString *targetText = [targets count] > 1 ? [targets componentsJoinedByString:@","] : [targets firstObject];
        NSString *teamName = [YPNIMKitUtil teamNotificationTeamShowName:message];
        
        switch (content.operationType) {
            case NIMTeamOperationTypeInvite:{
                NSString *str = [NSString stringWithFormat:@"%@%@%@",source,NSLocalizedString(NIMKitInvite, nil),targets.firstObject];
                if (targets.count>1) {
                    str = [str stringByAppendingFormat:@"%@%zd%@",NSLocalizedString(NIMKitSoOn, nil),targets.count,NSLocalizedString(NIMKitPerson, nil)];
                }
                str = [str stringByAppendingFormat:@"%@%@",NSLocalizedString(NIMKitEntered, nil),teamName];
                formatedMessage = str;
            }
                break;
            case NIMTeamOperationTypeDismiss:
                formatedMessage = [NSString stringWithFormat:@"%@%@%@",source,NSLocalizedString(NIMKitDismiss, nil),teamName];
                break;
            case NIMTeamOperationTypeKick:{
                NSString *str = [NSString stringWithFormat:@"%@%@%@",source,NSLocalizedString(NIMKitBring, nil),targets.firstObject];
                if (targets.count>1) {
                    str = [str stringByAppendingFormat:@"%@%zd%@",NSLocalizedString(NIMKitSoOn, nil),targets.count,NSLocalizedString(NIMKitPerson, nil)];
                }
                str = [str stringByAppendingFormat:@"%@%@",NSLocalizedString(NIMKitBringOut, nil),teamName];
                formatedMessage = str;
            }
                break;
            case NIMTeamOperationTypeUpdate:
            {
                id attachment = [content attachment];
                if ([attachment isKindOfClass:[NIMUpdateTeamInfoAttachment class]]) {
                    NIMUpdateTeamInfoAttachment *teamAttachment = (NIMUpdateTeamInfoAttachment *)attachment;
                    formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitImforemation, nil)];
                    //如果只是单个项目项被修改则显示具体的修改项
                    if ([teamAttachment.values count] == 1) {
                        NIMTeamUpdateTag tag = [[[teamAttachment.values allKeys] firstObject] integerValue];
                        switch (tag) {
                            case NIMTeamUpdateTagName:
                                formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitName, nil)];
                                break;
                            case NIMTeamUpdateTagIntro:
                                formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitIntroduction, nil)];
                                break;
                            case NIMTeamUpdateTagAnouncement:
                                formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitAd, nil)];
                                break;
                            case NIMTeamUpdateTagJoinMode:
                                formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitVerifyWay, nil)];
                                break;
                            case NIMTeamUpdateTagAvatar:
                                formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitHeadImage, nil)];
                                break;
                            case NIMTeamUpdateTagInviteMode:
                                formatedMessage = [NSString stringWithFormat:@"%@%@",source,NSLocalizedString(NIMKitUpdatateOthersInvition, nil)];
                                break;
                            case NIMTeamUpdateTagBeInviteMode:
                                formatedMessage = [NSString stringWithFormat:@"%@%@",source,NSLocalizedString(NIMKitUpdateOthersID, nil)];
                                break;
                            case NIMTeamUpdateTagUpdateInfoMode:
                                formatedMessage = [NSString stringWithFormat:@"%@%@",source,NSLocalizedString(NIMKitUpdateOthersInfoChange, nil)];
                                break;
                            case NIMTeamUpdateTagMuteMode:{
                                NIMTeam *team = [[NIMSDK sharedSDK].teamManager teamById:message.session.sessionId];
                                BOOL inAllMuteMode = [team inAllMuteMode];
                                formatedMessage = inAllMuteMode? [NSString stringWithFormat:@"%@%@",source,NSLocalizedString(NIMKitSetQuiet, nil)]: [NSString stringWithFormat:@"%@%@",source,NSLocalizedString(NIMKitSetUnQuiet, nil)];
                                break;
                            }
                            default:
                                break;
                                
                        }
                    }
                }
                if (formatedMessage == nil){
                    formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitUpdate, nil),teamName,NSLocalizedString(NIMKitImforemation, nil)];
                }
            }
                break;
            case NIMTeamOperationTypeLeave:
                formatedMessage = [NSString stringWithFormat:@"%@%@%@",source,NSLocalizedString(NIMKitDidLeft, nil),teamName];
                break;
            case NIMTeamOperationTypeApplyPass:{
                if ([source isEqualToString:targetText]) {
                    //说明是以不需要验证的方式进入
                    formatedMessage = [NSString stringWithFormat:@"%@%@%@",source,NSLocalizedString(NIMKitEntered, nil),teamName];
                }else{
                    formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitDidPass, nil),targetText,NSLocalizedString(NIMKitInterview, nil)];
                }
            }
                break;
            case NIMTeamOperationTypeTransferOwner:
                formatedMessage = [NSString stringWithFormat:@"%@%@%@",source,NSLocalizedString(NIMKitRemoverGroupOwnerFor, nil),targetText];
                break;
            case NIMTeamOperationTypeAddManager:
                formatedMessage = [NSString stringWithFormat:@"%@%@",targetText,NSLocalizedString(NIMKitHadBennAddedBeManager, nil)];
                break;
            case NIMTeamOperationTypeRemoveManager:
                formatedMessage = [NSString stringWithFormat:@"%@%@",targetText,NSLocalizedString(NIMKitHadBennRemovedBeManager, nil)];
                break;
            case NIMTeamOperationTypeAcceptInvitation:
                formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",source,NSLocalizedString(NIMKitDidAccept, nil),targetText,NSLocalizedString(NIMKitEnterGroupOfInvition, nil)];
                break;
            case NIMTeamOperationTypeMute:{
                id attachment = [content attachment];
                if ([attachment isKindOfClass:[NIMMuteTeamMemberAttachment class]])
                {
                    BOOL mute = [(NIMMuteTeamMemberAttachment *)attachment flag];
                    NSString *muteStr = mute? NSLocalizedString(NIMKitQueit, nil) : NSLocalizedString(NIMKitRemoveQueit, nil);
                    NSString *str = [targets componentsJoinedByString:@","];
                    formatedMessage = [NSString stringWithFormat:@"%@%@%@%@",str,NSLocalizedString(NIMKitHadBeen, nil),source,muteStr];
                }
            }
                break;
            default:
                break;
        }
        
    }
    if (!formatedMessage.length) {
        formatedMessage = [NSString stringWithFormat:@"%@",NSLocalizedString(NIMKitUnknowMessage, nil)];
    }
    return formatedMessage;
}


+ (NSString *)netcallNotificationFormatedMessage:(NIMMessage *)message{
    NIMNotificationObject *object = message.messageObject;
    NIMNetCallNotificationContent *content = (NIMNetCallNotificationContent *)object.content;
    NSString *text = @"";
    NSString *currentAccount = [[NIMSDK sharedSDK].loginManager currentAccount];
    switch (content.eventType) {
        case NIMNetCallEventTypeMiss:{
            text = @"未接听";
            break;
        }
        case NIMNetCallEventTypeBill:{
            text =  ([object.message.from isEqualToString:currentAccount])? @"通话拨打时长 " : @"通话接听时长 ";
            NSTimeInterval duration = content.duration;
            NSString *durationDesc = [NSString stringWithFormat:@"%02d:%02d",(int)duration/60,(int)duration%60];
            text = [text stringByAppendingString:durationDesc];
            break;
        }
        case NIMNetCallEventTypeReject:{
            text = ([object.message.from isEqualToString:currentAccount])? @"对方正忙" : @"已拒绝";
            break;
        }
        case NIMNetCallEventTypeNoResponse:{
            text = @"未接通，已取消";
            break;
        }
        default:
            break;
    }
    return text;
}


+ (NSString *)chatroomNotificationFormatedMessage:(NIMMessage *)message{
    NIMNotificationObject *object = message.messageObject;
    NIMChatroomNotificationContent *content = (NIMChatroomNotificationContent *)object.content;
    NSMutableArray *targetNicks = [[NSMutableArray alloc] init];
    for (NIMChatroomNotificationMember *memebr in content.targets) {
        if ([memebr.userId isEqualToString:[[NIMSDK sharedSDK].loginManager currentAccount]]) {
           [targetNicks addObject:@"你"];
        }else{
           [targetNicks addObject:memebr.nick];
        }

    }
    NSString *opeText    = content.source.nick;
    NSString *targetText = [targetNicks componentsJoinedByString:@","];
    switch (content.eventType) {
        case NIMChatroomEventTypeEnter:
        {
            return [NSString stringWithFormat:@"欢迎%@进入直播间",targetText];
        }
        case NIMChatroomEventTypeAddBlack:
        {
            return [NSString stringWithFormat:@"%@被管理员拉入黑名单",targetText];
        }
        case NIMChatroomEventTypeRemoveBlack:
        {
            return [NSString stringWithFormat:@"%@被管理员解除拉黑",targetText];
        }
        case NIMChatroomEventTypeAddMute:
        {
            if (content.targets.count == 1 && [[content.targets.firstObject userId] isEqualToString:[[NIMSDK sharedSDK].loginManager currentAccount]])
            {
                return @"你已被禁言";
            }
            else
            {
                return [NSString stringWithFormat:@"%@被管理员禁言",targetText];
            }
        }
        case NIMChatroomEventTypeRemoveMute:
        {
            return [NSString stringWithFormat:@"%@被管理员解除禁言",targetText];
        }
        case NIMChatroomEventTypeAddManager:
        {
            return [NSString stringWithFormat:@"%@被任命管理员身份",targetText];
        }
        case NIMChatroomEventTypeRemoveManager:
        {
            return [NSString stringWithFormat:@"%@被解除管理员身份",targetText];
        }
        case NIMChatroomEventTypeRemoveCommon:
        {
            return [NSString stringWithFormat:@"%@被解除直播室成员身份",targetText];
        }
        case NIMChatroomEventTypeAddCommon:
        {
            return [NSString stringWithFormat:@"%@被添加为直播室成员身份",targetText];
        }
        case NIMChatroomEventTypeInfoUpdated:
        {
            return [NSString stringWithFormat:@"直播间公告已更新"];
        }
        case NIMChatroomEventTypeKicked:
        {
            return [NSString stringWithFormat:@"%@被管理员移出直播间",targetText];
        }
        case NIMChatroomEventTypeExit:
        {
            return [NSString stringWithFormat:@"%@离开了直播间",targetText];
        }
        case NIMChatroomEventTypeClosed:
        {
            return [NSString stringWithFormat:@"直播间已关闭"];
        }
        case NIMChatroomEventTypeAddMuteTemporarily:
        {
            if (content.targets.count == 1 && [[content.targets.firstObject userId] isEqualToString:[[NIMSDK sharedSDK].loginManager currentAccount]])
            {
                return @"你已被临时禁言";
            }
            else
            {
                return [NSString stringWithFormat:@"%@被管理员禁言",targetText];
            }
        }
        case NIMChatroomEventTypeRemoveMuteTemporarily:
        {
            return [NSString stringWithFormat:@"%@被管理员解除临时禁言",targetText];
        }
        case NIMChatroomEventTypeMemberUpdateInfo:
        {
            return [NSString stringWithFormat:@"%@更新了自己的个人信息",targetText];
        }
        case NIMChatroomEventTypeRoomMuted:
        {
            return @"全体禁言，管理员可发言";
        }
        case NIMChatroomEventTypeRoomUnMuted:
        {
            return @"解除全体禁言";
        }
        case NIMChatroomEventTypeQueueChange:
        case NIMChatroomEventTypeQueueBatchChange:
            return [NSString stringWithFormat:@"%@改变了聊天室队列",opeText];
        default:
            break;
    }
    return @"";
}


#pragma mark - Private
+ (NSString *)teamNotificationSourceName:(NIMMessage *)message{
    NSString *source;
    NIMNotificationObject *object = message.messageObject;
    NIMTeamNotificationContent *content = (NIMTeamNotificationContent*)object.content;
    NSString *currentAccount = [[NIMSDK sharedSDK].loginManager currentAccount];
    if ([content.sourceID isEqualToString:currentAccount]) {
        source = @"你";
    }else{
        source = [YPNIMKitUtil showNick:content.sourceID inSession:message.session];
    }
    return source;
}

+ (NSArray *)teamNotificationTargetNames:(NIMMessage *)message{
    NSMutableArray *targets = [[NSMutableArray alloc] init];
    NIMNotificationObject *object = message.messageObject;
    NIMTeamNotificationContent *content = (NIMTeamNotificationContent*)object.content;
    NSString *currentAccount = [[NIMSDK sharedSDK].loginManager currentAccount];
    for (NSString *item in content.targetIDs) {
        if ([item isEqualToString:currentAccount]) {
            [targets addObject:@"你"];
        }else{
            NSString *targetShowName = [YPNIMKitUtil showNick:item inSession:message.session];
            [targets addObject:targetShowName];
        }
    }
    return targets;
}


+ (NSString *)teamNotificationTeamShowName:(NIMMessage *)message{
    NIMTeam *team = [[NIMSDK sharedSDK].teamManager teamById:message.session.sessionId];
    NSString *teamName = team.type == NIMTeamTypeNormal ? @"讨论组" : @"群";
    return teamName;
}

+ (BOOL)canEditTeamInfo:(NIMTeamMember *)member
{
    NIMTeam *team = [[NIMSDK sharedSDK].teamManager teamById:member.teamId];
    if (team.updateInfoMode == NIMTeamUpdateInfoModeManager)
    {
        return member.type == NIMTeamMemberTypeOwner || member.type == NIMTeamMemberTypeManager;
    }
    else
    {
        return member.type == NIMTeamMemberTypeOwner || member.type == NIMTeamMemberTypeManager || member.type == NIMTeamMemberTypeNormal;
    }
}

+ (BOOL)canInviteMember:(NIMTeamMember *)member
{
    NIMTeam *team = [[NIMSDK sharedSDK].teamManager teamById:member.teamId];
    if (team.inviteMode == NIMTeamInviteModeManager)
    {
        return member.type == NIMTeamMemberTypeOwner || member.type == NIMTeamMemberTypeManager;
    }
    else
    {
        return member.type == NIMTeamMemberTypeOwner || member.type == NIMTeamMemberTypeManager || member.type == NIMTeamMemberTypeNormal;
    }

}

@end
