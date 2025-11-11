//
//  YPMallHeaderView.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMallHeaderView.h"

@interface YPMallHeaderView ()

@property (nonatomic ,strong) UIImageView *avatar;
@property (nonatomic ,strong) UIImageView *headWearImageView;

@end

@implementation YPMallHeaderView

- (instancetype)init
{
    self = [super init];
    if (self) {
        [self initView];
    }
    return self;
}

- (void)initView {
    [self addSubview:self.avatar];
    [self addSubview:self.headWearImageView];
    self.headWearImageView.hidden = YES;
    self.avatar.hidden = YES;//life-hj
    [self.avatar mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(20);
        make.centerX.mas_equalTo(self);
        make.height.width.mas_equalTo(64);
    }];
    [self.headWearImageView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.center.equalTo(self.avatar);
        make.top.mas_equalTo(-40);
        make.right.mas_equalTo(-30);
        make.height.width.mas_equalTo((64));
    }];
}

- (void)setupAvatar:(NSString *)avatar headWear:(NSString *)headWear {
    
    [self.avatar qn_setImageImageWithUrl:avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
    [self setupheadWear:headWear];
}

- (void)setupheadWear:(NSString *)headWear {
    if (headWear.length) {
        self.headWearImageView.hidden = NO;
        [self.headWearImageView qn_setImageImageWithUrl:headWear placeholderImage:nil type:ImageTypeUserIcon];
    } else {
        self.headWearImageView.hidden = YES;
    }
}

- (UIImageView *)avatar {
    if (!_avatar) {
        _avatar = [[UIImageView alloc] initWithImage:[UIImage imageNamed:default_avatar]];
        _avatar.frame = CGRectMake(0, 0, 64, 64);
        _avatar.layer.cornerRadius = 32;
        _avatar.layer.masksToBounds = YES;
    }
    return _avatar;
}

- (UIImageView *)headWearImageView {
    if (!_headWearImageView) {
        _headWearImageView = [[UIImageView alloc] init];
        _headWearImageView.frame = CGRectMake(0, 0, 64, 64);
    }
    return _headWearImageView;
}

@end
