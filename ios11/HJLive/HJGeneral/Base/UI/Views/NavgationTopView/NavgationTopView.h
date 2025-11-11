//
//  NavgationTopView.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface NavgationTopView : UIView
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UIButton *rightBtn;

@property (nonatomic, copy) void(^goBackBlock)(void);
@property (nonatomic, copy) void(^rightClickBlock)(void);
@property (weak, nonatomic) IBOutlet UIButton *leftBtn;

@end

NS_ASSUME_NONNULL_END
