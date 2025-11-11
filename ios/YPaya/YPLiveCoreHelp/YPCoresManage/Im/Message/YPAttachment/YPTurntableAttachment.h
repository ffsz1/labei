//
//  TurntableInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <NIMSDK/NIMSDK.h>
#import "YPAttachment.h"
#import "HJCustomAttachmentInfo.h"

@interface YPTurntableAttachment : YPAttachment <NIMCustomAttachment,HJCustomAttachmentInfo>

//leftDrawNum = 1;
//totalDrawNum = 1;
//totalWinDrawNum = 0;
//uid = 90971;

@property (nonatomic, assign) NSInteger leftDrawNum;
@property (nonatomic, assign) NSInteger totalDrawNum;
@property (nonatomic, assign) NSInteger totalWinDrawNum;
@property (nonatomic, assign) UserID uid;
@end
