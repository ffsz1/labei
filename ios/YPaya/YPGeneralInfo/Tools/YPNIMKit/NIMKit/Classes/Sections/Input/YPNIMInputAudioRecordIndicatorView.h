//
//  YPNIMInputAudioRecordIndicatorView.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMInputView.h"

@interface YPNIMInputAudioRecordIndicatorView : UIView

@property (nonatomic, assign) NIMAudioRecordPhase phase;

@property (nonatomic, assign) NSTimeInterval recordTime;

@end
