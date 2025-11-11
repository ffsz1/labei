//
//  YPLevelModel.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPLevelModel : NSObject

@property (nonatomic,copy) NSString *avatar;
@property (nonatomic,copy) NSString *leftGoldNum;
@property (nonatomic,copy) NSString *levelName;
@property (nonatomic,assign) CGFloat levelPercent;
@property (nonatomic,assign) NSInteger level;
@end

NS_ASSUME_NONNULL_END
