//
//  HJMICMatchUserView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HJMICUserInfo, HJMICPlayInfoModel,HJMICMatchRecordView;

typedef void(^HJMICMatchUserViewDidTapRecordHandler)(HJMICMatchRecordView* matchRecordView);
typedef void(^HJMICMatchUserViewDidTapAttentionHandler)(void);

@interface HJMICMatchUserView : UIView

@property (nonatomic, copy) HJMICMatchUserViewDidTapRecordHandler didTapRecordHandler;
@property (nonatomic, copy) HJMICMatchUserViewDidTapAttentionHandler didTapAttentionHandler;
@property (nonatomic, copy) HJMICMatchUserViewDidTapAttentionHandler blackListBlock;
@property (nonatomic, copy) HJMICMatchUserViewDidTapAttentionHandler reportBlock;

@property (nonatomic,strong) HJMICUserInfo *userInfo;


- (void)confifureWithUserInfo:(HJMICUserInfo *)userInfo;
- (void)configureWithAttenion:(BOOL)isAttenion;
- (void)configureWithPlayInfo:(HJMICPlayInfoModel *)playInfo;

@end
