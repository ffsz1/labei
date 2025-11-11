//
//  YPTicketListInfoStorage.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPTicketListInfo.h"

@interface YPTicketListInfoStorage : NSObject
+ (YPTicketListInfo *)getCurrentTicketListInfo;
+ (void) saveTicketListInfo:(YPTicketListInfo *)ticketListInfo;
@end
