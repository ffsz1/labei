//
//  HJToolBarButton.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJToolBarButton.h"

@interface HJToolBarButton ()

@property (nonatomic, strong) UIView *redView;

@end

@implementation HJToolBarButton


- (void)setNormalIcon:(UIImage *)normalIcon {
    _normalIcon = normalIcon;
    [self setImage:self.normalIcon forState:UIControlStateNormal];
}

- (void)setSelectedIcon:(UIImage *)selectedIcon {
    _selectedIcon = selectedIcon;
    [self setImage:self.selectedIcon forState:UIControlStateSelected];
}

- (void)setDisableIcon:(UIImage *)disableIcon {
    _disableIcon = disableIcon;
    [self setImage:self.disableIcon forState:UIControlStateDisabled];
}

- (void)setIsShowRed:(BOOL)isShowRed {
    _isShowRed = isShowRed;
    self.redView.hidden = !isShowRed;
}

- (UIView *)redView {
    if (!_redView) {
        
        _redView = [UIView new];
        _redView.backgroundColor = UIColorHex(FF2C4A);
//        _redView.frame = CGRectMake(self.width - 10.f, 6.f, 7.f, 7.f);
        _redView.layer.masksToBounds = YES;
        _redView.layer.cornerRadius = 3.5f;
        [self addSubview:_redView];
        [_redView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.width.mas_equalTo(7);
            make.top.mas_equalTo(10);
            make.right.mas_equalTo(-6);
        }];
        
    }
    return _redView;
}

@end
