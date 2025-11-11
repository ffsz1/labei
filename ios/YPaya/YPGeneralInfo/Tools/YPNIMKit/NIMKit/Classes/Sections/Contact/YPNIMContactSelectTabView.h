//
//  YPNIMContactSelectTabView.h
//  YPNIMKit
//
//  Created by chris on 15/9/15.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>
@class YPNIMContactPickedView;

@interface YPNIMContactSelectTabView : UIView

@property (nonatomic,strong) YPNIMContactPickedView *pickedView;

@property (nonatomic,strong) UIButton *doneButton;

@end
