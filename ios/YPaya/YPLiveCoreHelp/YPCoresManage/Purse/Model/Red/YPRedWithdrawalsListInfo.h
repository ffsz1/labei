//
//  YPRedWithdrawalsListInfo.h
//  HJLive
//
//  Created by FF on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPRedWithdrawalsListInfo : NSObject

//"packetId":1,
//"packetNum":100,
//"prodStauts":1,
//"seqNo":1

@property (assign, nonatomic) NSInteger packetId;
@property (assign, nonatomic) NSInteger packetNum;
@property (assign, nonatomic) NSInteger prodStauts;
@property (assign, nonatomic) NSInteger seqNo;

@end
