//
//  HJRechargeBillInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HJRechargeBillInfo : NSObject

@property (strong, nonatomic)NSString *money;
@property (strong, nonatomic)NSString *goldNum;
@property (assign, nonatomic)NSInteger recordTime;
@property (strong, nonatomic)NSString *showStr;
@property (strong, nonatomic)NSString *diamondNum;//砖石
//@property (assign, nonatomic)NSInteger expendType;
@property(nonatomic,strong) NSString* typeStr;
@property(nonatomic,assign) long date;
@property(nonatomic,assign) long packetNum;
//@property(nonatomic,assign) long recordTime;
@property(nonatomic,assign) long uid;


//代充模型
@property(nonatomic,assign) long createTime;
@property(nonatomic,assign) long diamondUserErnos;
@property(nonatomic,assign) long receiveUserErnos;
@property(nonatomic,assign) long exDiamondNum;
@property(nonatomic,assign) long exGoldNum;
@property (strong, nonatomic)NSString *diamondUserNick;
@property (strong, nonatomic)NSString *receiveUserNick;//接受者昵称
@property (strong, nonatomic)NSString *dataTime;
@end
