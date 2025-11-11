//
//  HJTicketListInfoStorage.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "TicketListInfo.h"

@interface HJTicketListInfoStorage : NSObject
+ (TicketListInfo *)getCurrentTicketListInfo;
+ (void) saveTicketListInfo:(TicketListInfo *)ticketListInfo;
@end
