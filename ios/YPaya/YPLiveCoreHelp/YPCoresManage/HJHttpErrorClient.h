//
//  HttpErrorClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJHttpErrorClient <NSObject>
- (void) onTicketInvalid;
- (void) networkDisconnect;
- (void) requestFailureWithMsg:(NSString *)msg;
@end
