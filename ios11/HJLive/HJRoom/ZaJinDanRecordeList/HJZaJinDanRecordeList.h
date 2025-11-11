//
//  HJZaJinDanRecordeList.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJZaJinDanRecordeList : UIView

@property (nonatomic, copy) void(^closeActionBlock)();
- (void)removeCore;

@end
