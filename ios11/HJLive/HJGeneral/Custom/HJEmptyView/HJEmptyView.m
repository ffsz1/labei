//
//  HJEmptyView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJEmptyView.h"


@interface HJEmptyView ()

@property (nonatomic,strong) UIImageView *statusImageView;
@property (nonatomic,strong) UILabel *statusLabel;


@end

@implementation HJEmptyView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
    {
        self.backgroundColor = [UIColor clearColor];
        [self setUI];
        
    }
    return self;
}

- (void)setUI
{
    [self.statusImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.mas_equalTo(self);
        make.size.mas_equalTo(CGSizeMake(180, 180));
    }];
    
    [self.statusLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.mas_equalTo(self);
        make.top.mas_equalTo(self.statusImageView.mas_bottom);
        make.size.mas_equalTo(CGSizeMake(280, 18));
    }];
}
    
    
- (void)setTitle:(NSString *)title image:(NSString *)image
{
    self.statusLabel.text = title;
    self.statusImageView.image = [UIImage imageNamed:image];
}


- (UIImageView *)statusImageView
{
    if (!_statusImageView) {
        _statusImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
//        _statusImageView.center = self.center;
        _statusImageView.image = [UIImage imageNamed:@"hj_empty_noData"];
        [self addSubview:_statusImageView];
        _statusImageView.contentMode = UIViewContentModeCenter;

    }
    return _statusImageView;
}

- (UILabel *)statusLabel
{
    if (!_statusLabel) {
        _statusLabel = [[UILabel alloc] initWithFrame:CGRectZero];
//        _statusLabel.center = CGPointMake(self.centerX, self.centerY + 90);
        _statusLabel.font = [UIFont fontWithName:@"PingFang SC" size: 12];
        _statusLabel.textColor = [UIColor colorWithRed:204/255.0 green:204/255.0 blue:204/255.0 alpha:1.0];
        _statusLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_statusLabel];
    }
    return _statusLabel;
}

@end
