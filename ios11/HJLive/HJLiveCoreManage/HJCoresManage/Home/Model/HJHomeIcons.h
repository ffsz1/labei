//
//  HJHomeIcons.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"

@interface HJHomeIcons : BaseObject
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *pic;
@property (nonatomic, copy) NSString *activity;
@property (nonatomic, copy) NSString *iosActivity;
@property (nonatomic, copy) NSString *url;
@property (nonatomic, copy) NSDictionary *params;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, copy) NSString *skipUri;
@property (nonatomic, assign) NSInteger skipType;


@end
