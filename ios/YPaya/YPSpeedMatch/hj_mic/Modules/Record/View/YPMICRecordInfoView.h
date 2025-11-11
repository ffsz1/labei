//
//  YPMICRecordInfoView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, HJMICRecordInfoViewAnimationState) {
    HJMICRecordInfoViewAnimationStateStart,
    HJMICRecordInfoViewAnimationStatePause,
    HJMICRecordInfoViewAnimationStateStop,
};

@interface YPMICRecordInfoView : UIView

@property (nonatomic, assign) HJMICRecordInfoViewAnimationState animationState;
@property (nonatomic, assign) NSTimeInterval recordTime;

@end
