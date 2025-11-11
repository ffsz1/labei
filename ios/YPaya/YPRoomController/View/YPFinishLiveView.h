//
//  YPFinishLiveView.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface YPFinishLiveView : UIView
@property (nonatomic, assign)UserID uid;
+ (instancetype)loadFromNIB;
- (void)setUid:(UserID)uid;
@end
