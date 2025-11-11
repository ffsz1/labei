//
//  SingleAudioCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
//#import "NIMAVChat.h"
@protocol HJPhoneCallCoreClient <NSObject>
@optional
- (void)onStartPhoneCallSuccess:(UserID)callUid;
- (void)onStartPhoneCallFailth;

- (void)onCallEstablished;
- (void)onCallDisconnected;

- (void)onRecievePhoneCall:(NSString *)from;
- (void)onRecievePhoneCall:(NSString *)from uid:(NSString *)uid extend:(NSString *)extend;
- (void)onResponsePhoneCall:(NSString *)from accept:(BOOL)accept;

- (void)onHangup:(NSString *)from;
//- (void)onNetStatus:(NIMNetCallNetStatus)status user:(NSString *)user;
- (void)onBusyLine:(NSString *)from;
@end
