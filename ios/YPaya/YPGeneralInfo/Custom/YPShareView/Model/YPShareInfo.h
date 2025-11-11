//
//  YPShareInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef NS_ENUM(NSUInteger, HJShareType) {
    HJShareTypeNormol,
    HJShareTypeRoom,
};
NS_ASSUME_NONNULL_BEGIN

@interface YPShareInfo : NSObject
@property (copy, nonatomic) NSString *title;
@property (copy, nonatomic) NSString *imgUrl;
@property (copy, nonatomic) NSString *desc;
@property (copy, nonatomic) NSString *url;
@property (copy, nonatomic) NSString *showUrl;

@property (assign,nonatomic) HJShareType type;
@property (assign,nonatomic) UserID roomOwnerUID;

@end

NS_ASSUME_NONNULL_END
