//
//  YPMICMatchUserView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YPMICUserInfo, YPMICPlayInfoModel,YPMICMatchRecordView;

typedef void(^HJMICMatchUserViewDidTapRecordHandler)(YPMICMatchRecordView* matchRecordView);
typedef void(^HJMICMatchUserViewDidTapAttentionHandler)(void);

@interface YPMICMatchUserView : UIView

@property (nonatomic, copy) HJMICMatchUserViewDidTapRecordHandler didTapRecordHandler;
@property (nonatomic, copy) HJMICMatchUserViewDidTapAttentionHandler didTapAttentionHandler;
@property (nonatomic, copy) HJMICMatchUserViewDidTapAttentionHandler blackListBlock;
@property (nonatomic, copy) HJMICMatchUserViewDidTapAttentionHandler reportBlock;

@property (nonatomic,strong) YPMICUserInfo *userInfo;


- (void)confifureWithUserInfo:(YPMICUserInfo *)userInfo;
- (void)configureWithAttenion:(BOOL)isAttenion;
- (void)configureWithPlayInfo:(YPMICPlayInfoModel *)playInfo;

@end
