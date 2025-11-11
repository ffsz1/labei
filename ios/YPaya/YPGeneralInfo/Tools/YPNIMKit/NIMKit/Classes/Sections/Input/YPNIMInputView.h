//
//  YPNIMInputView.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NIMInputProtocol.h"
#import "NIMSessionConfig.h"
#import "YPNIMInputToolBar.h"
#import "YPNIMInputAtCache.h"

@class YPNIMInputMoreContainerView;
@class YPNIMInputEmoticonContainerView;



typedef NS_ENUM(NSInteger, NIMAudioRecordPhase) {
    AudioRecordPhaseStart,
    AudioRecordPhaseRecording,
    AudioRecordPhaseCancelling,
    AudioRecordPhaseEnd
};



@protocol NIMInputDelegate <NSObject>

@optional

- (void)didChangeInputHeight:(CGFloat)inputHeight;

@end

@interface YPNIMInputView : UIView

@property (nonatomic, strong) NIMSession             *session;

@property (nonatomic, assign) NSInteger              maxTextLength;

@property (assign, nonatomic, getter=isRecording)    BOOL recording;

@property (strong, nonatomic)  YPNIMInputToolBar *toolBar;
@property (strong, nonatomic)  UIView *moreContainer;
@property (strong, nonatomic)  UIView *emoticonContainer;

@property (nonatomic, assign) NIMInputStatus status;
@property (nonatomic, strong) YPNIMInputAtCache *atCache;

- (instancetype)initWithFrame:(CGRect)frame
                       config:(id<NIMSessionConfig>)config;

- (void)reset;

- (void)refreshStatus:(NIMInputStatus)status;

- (void)setInputDelegate:(id<NIMInputDelegate>)delegate;

//外部设置
- (void)setInputActionDelegate:(id<NIMInputActionDelegate>)actionDelegate;

- (void)setInputTextPlaceHolder:(NSString*)placeHolder;
- (void)updateAudioRecordTime:(NSTimeInterval)time;
- (void)updateVoicePower:(float)power;

@end
