//
//  XCFamilyDetailsView.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HJFamilyModel;

NS_ASSUME_NONNULL_BEGIN

typedef void(^XCFamilyDetailsViewDidTapAnnouncementHandler)(void);
typedef void(^XCFamilyDetailsViewDidTapChatHandler)(void);
typedef void(^XCFamilyDetailsViewDidTapMembersHandler)(void);
typedef void(^XCFamilyDetailsViewDidTapPhotoHandler)(void);
typedef void(^XCFamilyDetailsViewDidTapLogoPhotoHandler)(void);
typedef void(^XCFamilyDetailsViewDidHeaderRefreshHandler)(void);

@interface XCFamilyDetailsView : UIView

@property (nonatomic, copy) XCFamilyDetailsViewDidTapAnnouncementHandler didTapAnnouncementHandler;
@property (nonatomic, copy) XCFamilyDetailsViewDidTapChatHandler didTapChatHandler;
@property (nonatomic, copy) XCFamilyDetailsViewDidTapMembersHandler didTapMembersHandler;
@property (nonatomic, copy) XCFamilyDetailsViewDidTapPhotoHandler didTapPhotoHandler;
@property (nonatomic, copy) XCFamilyDetailsViewDidTapLogoPhotoHandler didTapLogoPhotoHandler;
@property (nonatomic, copy) XCFamilyDetailsViewDidHeaderRefreshHandler didHeaderRefreshHandler;

- (void)configureWithDetails:(HJFamilyModel *)detailsInfo;

- (void)endHeaderRefreshing;

@end

NS_ASSUME_NONNULL_END
