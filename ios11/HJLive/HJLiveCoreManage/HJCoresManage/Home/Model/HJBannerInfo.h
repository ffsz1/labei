//
//  HJBannerInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h" 

typedef enum : NSUInteger {
    BannerInfoSkipTypePage = 1,
    BannerInfoSkipTypeRoom,
    BannerInfoSkipTypeWeb,
} BannerInfoSkipType;

@interface HJBannerInfo : BaseObject
@property (nonatomic, strong)NSString *bannerId;
@property (nonatomic, strong)NSString *bannerName;
@property (nonatomic, strong)NSString *bannerPic;
@property (nonatomic, strong)NSString *skipUri;
@property (nonatomic, assign)NSInteger skipType;// 1跳app页面，2跳聊天室，3跳h5页面
@property (nonatomic, assign)NSInteger seqNo;
@end
