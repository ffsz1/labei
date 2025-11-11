//
//  VersionInfo.h
//  HJLive
//
//  Created by FF on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
    Version_Online = 1,
    Version_Audting = 2,
    Version_Notice = 3,
    Version_Suggest = 4,
    Version_IsDeleted = 5
} VersionType;

@interface VersionInfo : NSObject
@property (assign, nonatomic) VersionType status;
@property (copy, nonatomic) NSString *updateVersionDesc;
@property (copy, nonatomic) NSString *updateVersion;
@property (copy, nonatomic) NSString *versionDesc;
@property (assign, nonatomic) NSInteger kickWaiting;
@end
