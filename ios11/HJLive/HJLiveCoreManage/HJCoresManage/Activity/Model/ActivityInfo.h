//
//  ActivityInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ActivityInfo : NSObject



//actAlertVersion = "1.0.0";
//actId = 2;
//actName = "\U7ea2\U5305\U5927\U4f5c\U6218";
//alertWin = 1;
//alertWinLoc = 2;
//alertWinPic = "http://nim.nos.netease.com/NDI3OTA4NQ==/bmltd18wXzE1MDY4NzIxNjAyMzJfM2YyODgwOWQtMDNiYy00OWIwLTgyYTItYjM1YjI5MzI5Yzc4";
//skipType = 1;
//skipUrl = "http://beta.erbanyy.com/modules/bonus/fight.html";


@property (assign, nonatomic) NSInteger actId;
@property (copy, nonatomic) NSString *actName;
@property (assign, nonatomic) BOOL alertWin;
@property (copy, nonatomic) NSString *alertWinPic;
@property (assign, nonatomic) NSInteger alertWinLoc;
@property (assign, nonatomic) NSInteger skipType;
@property (copy, nonatomic) NSString *skipUrl;
@property (assign, nonatomic) NSInteger actAlertVersion;

@end
