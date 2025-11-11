//
//  YPMICRecordView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YPMICRecordInfoModel;

typedef void(^HJMICRecordViewDidTapChangeHandler)(void);
typedef void(^HJMICRecordViewDidTapDoneHandler)(void);
typedef void(^HJMICRecordViewDidTapDeletedHandler)(void);
typedef void(^HJMICRecordViewDidTapPlayHandler)(void);
typedef void(^HJMICRecordViewDidTouchRecordHandler)(BOOL isFinish);
typedef void(^HJMICRecordViewSaveBlock)(void);

@interface YPMICRecordView : UIView

@property (nonatomic, copy) HJMICRecordViewDidTapChangeHandler didTapChangeHandler;
@property (nonatomic, copy) HJMICRecordViewDidTapDoneHandler didTapDoneHandler;
@property (nonatomic, copy) HJMICRecordViewDidTapDeletedHandler didTapDeletedHandler;
@property (nonatomic, copy) HJMICRecordViewDidTapPlayHandler didTapPlayHandler;
@property (nonatomic, copy) HJMICRecordViewDidTouchRecordHandler didTouchRecordHandler;
@property (nonatomic, copy) HJMICRecordViewSaveBlock saveBlock;


@property (nonatomic, strong) YPMICRecordInfoModel *recordInfo;
@property (nonatomic, copy) NSString *text;
@property (nonatomic, strong) UILabel *recordLabel;

@property (nonatomic, assign) BOOL saveState;

- (void)reset;

@end
