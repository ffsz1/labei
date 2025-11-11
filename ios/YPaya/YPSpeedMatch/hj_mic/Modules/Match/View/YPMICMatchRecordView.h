//
//  YPMICMatchRecordView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YPMICPlayInfoModel;
@class YPMICMatchRecordView;
typedef void(^HJMICMatchRecordViewDidTapHandler)(YPMICMatchRecordView* matchRecordView);

@interface YPMICMatchRecordView : UIView
@property (nonatomic, strong) UIImageView *playView;
@property (nonatomic, copy) HJMICMatchRecordViewDidTapHandler didTapHandler;

- (void)configureWithAudioInfo:(YPMICPlayInfoModel *)audioInfo;

@end
