//
//  YPFloatingView.h
//  HJLive
//
//  Created by feiyin on 2020/6/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPFloatingView : UIView

@property (weak, nonatomic) IBOutlet GGImageView *logoImageView;

@property (nonatomic, assign) CGPoint startLocation;
@property (nonatomic, assign) CGPoint didMovePoint;
@property (weak, nonatomic) IBOutlet UILabel *nickLabel;

- (void)closeAction;


@end

NS_ASSUME_NONNULL_END
