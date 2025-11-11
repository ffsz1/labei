//
//  AdInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
//    BannerInfoSkipTypePage = 1,
//    BannerInfoSkipTypeRoom,
//    BannerInfoSkipTypeWeb,
    SplashInfoSkipTypPage = 1,
    SplashInfoSkipTypRoom,
    SplashInfoSkipTypWeb,
} SplashInfoSkipType;

@interface AdInfo : NSObject

@property (nonatomic, strong) NSString *link;
@property (nonatomic, assign) SplashInfoSkipType type;// 1跳app页面，2跳聊天室，3跳h5页面
@property (nonatomic, copy)   NSString *pict;
@end
