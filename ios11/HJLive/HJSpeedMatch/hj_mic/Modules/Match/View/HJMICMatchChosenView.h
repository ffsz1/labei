//
//  HJMICMatchChosenView.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, XCMICMatchChosenType) {
    XCMICMatchChosenTypeLike,
    XCMICMatchChosenTypeDislike,
};

typedef void(^HJMICMatchChosenViewDidTapHandler)(void);

@interface HJMICMatchChosenView : UIView

@property (nonatomic, copy) HJMICMatchChosenViewDidTapHandler didTapHandler;

- (instancetype)initWithChosenType:(XCMICMatchChosenType)chosenType;

@end
