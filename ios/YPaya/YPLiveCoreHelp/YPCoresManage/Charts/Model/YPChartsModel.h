//
//  YPChartsModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPChartsModel : NSObject

@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, assign) NSInteger charmLevel;
@property (nonatomic, assign) NSInteger erbanNo;
@property (nonatomic, assign) NSInteger experLevel;
@property (nonatomic, assign) NSInteger gender;
@property (nonatomic, copy) NSString *nick;
@property (nonatomic, assign) NSInteger totalNum;
@property (nonatomic, assign) UserID uid;

//@property (nonatomic, copy) NSString *distance;
@property (nonatomic, assign) double distance;

@end
