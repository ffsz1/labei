//
//  HJMessageListView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJMessageListView : UIView
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (weak, nonatomic) IBOutlet UIView *squareView;

@property (nonatomic, copy) void(^closeBtnActionBlock)(void);
//@property (weak, nonatomic) IBOutlet NSLayoutConstraint *center_arrow;
@property (weak, nonatomic) IBOutlet UIButton *msgBtn;
@property (weak, nonatomic) IBOutlet UIButton *squareBtn;

@property (weak, nonatomic) IBOutlet UIImageView *msgIcon;
@property (weak, nonatomic) IBOutlet UIImageView *squareIcon;
@end
