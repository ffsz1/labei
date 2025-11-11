//
//  AlertInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AlertInfo : NSObject

@property (copy, nonatomic) NSString *actName;
@property (assign, nonatomic) NSInteger actId;
@property (assign ,nonatomic) BOOL alertWin;
@property (copy, nonatomic) NSString *alertWinPic;
@property (copy, nonatomic) NSString *alertWinLoc;
@property (copy, nonatomic) NSString *skipType;
@property (copy, nonatomic) NSString *skipUrl;
@property (copy, nonatomic) NSString *actAlertVersion;



@end
