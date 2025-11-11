//
//  HJLinkedMeCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "HJLinkMEModel.h"

@interface HJLinkedMeCore : BaseCore

@property (strong, nonatomic) HJLinkMEModel *linkme;
@property (strong, nonatomic) NSString *channel;
@property (strong, nonatomic) NSString *H5URL;
@property (strong, nonatomic) NSString *linkedmeChannel;

- (BOOL)judgeDeepLinkWith:(NSURL *)url;
- (BOOL)judgeDeepLinkWithUniversal:(NSUserActivity *)userActivity;
- (void)initLinkedMEWithLaunchOptions:(NSDictionary *)launchOptions;

@end
