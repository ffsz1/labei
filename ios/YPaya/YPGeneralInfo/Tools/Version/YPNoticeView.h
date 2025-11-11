//
//  YPNoticeView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPNoticeView : UIView

@property (nonatomic, copy)NSString *title;
@property (nonatomic, copy)NSString *message;
@property (nonatomic, copy) void(^noticeBlock)(void);
+ (instancetype)loadFromNib;
@end
