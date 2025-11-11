//
//  MeetingCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJMeetingCoreClient <NSObject>
@optional
- (void)onReserveMeetingSuccess;
- (void)onReserveMeetingFailth;

- (void)onJoinMeetingSuccess;
- (void)onJoinMeetingFailth;

- (void)onLeaveMeetingSuccess;

- (void)onSpeakingUsersReport:(NSMutableArray *)uids;
- (void)onMySpeakingStateUpdate:(BOOL)speaking;

- (void)onMeetingQualityBad;
- (void)onMeetingQualityDown;
@end
