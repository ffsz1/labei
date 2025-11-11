//
//  YPNIMInputEmoticonContainerView.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMPageView.h"
#import "NIMSessionConfig.h"

@class NIMInputEmoticonCatalog;
@class YPNIMInputEmoticonTabView;

@protocol NIMInputEmoticonProtocol <NSObject>

- (void)didPressSend:(id)sender;

- (void)selectedEmoticon:(NSString*)emoticonID catalog:(NSString*)emotCatalogID description:(NSString *)description;

@end


@interface YPNIMInputEmoticonContainerView : UIView<NIMPageViewDataSource,NIMPageViewDelegate>

@property (nonatomic, strong)  YPNIMPageView *emoticonPageView;
@property (nonatomic, strong)  UIPageControl  *emotPageController;
@property (nonatomic, strong)  NSArray                    *totalCatalogData;
@property (nonatomic, strong)  NIMInputEmoticonCatalog    *currentCatalogData;
@property (nonatomic, readonly)NSArray            *allEmoticons;
@property (nonatomic, strong)  YPNIMInputEmoticonTabView   *tabView;
@property (nonatomic, weak)    id<NIMInputEmoticonProtocol>  delegate;
@property (nonatomic, weak)    id<NIMSessionConfig> config;

@end

