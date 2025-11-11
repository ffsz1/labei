//
//  HJMICMatchRecordView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HJMICPlayInfoModel;
@class HJMICMatchRecordView;
typedef void(^HJMICMatchRecordViewDidTapHandler)(HJMICMatchRecordView* matchRecordView);

@interface HJMICMatchRecordView : UIView
@property (nonatomic, strong) UIImageView *playView;
@property (nonatomic, copy) HJMICMatchRecordViewDidTapHandler didTapHandler;

- (void)configureWithAudioInfo:(HJMICPlayInfoModel *)audioInfo;

@end
