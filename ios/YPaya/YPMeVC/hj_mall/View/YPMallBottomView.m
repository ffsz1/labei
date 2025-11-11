//
//  YPMallBottomView.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMallBottomView.h"

@interface YPMallBottomView ()

@property (nonatomic, strong) UIButton *buyBtn;

@property (nonatomic, strong) UILabel *priceLabel;
@property (nonatomic, strong) UIImageView *priceIcon;

@end

@implementation YPMallBottomView

- (instancetype)init
{
    self = [super init];
    if (self) {
        [self initView];
        [self setupLayout];
    }
    return self;
}

- (void)initView {
    [self addSubview:self.buyBtn];
    [self addSubview:self.sendBtn];
    [self addSubview:self.priceIcon];
    [self addSubview:self.priceLabel];
}

- (void)setupLayout {
    [self.buyBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(75);
        make.height.mas_equalTo(30);
        make.centerY.equalTo(self);
        make.right.equalTo(self).offset(-15);
    }];
    
    [self.sendBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(85);
        make.height.mas_equalTo(30);
        make.centerY.equalTo(self);
        make.right.equalTo(self.buyBtn.mas_left).offset(-15);
    }];
    
    [self.priceIcon mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(18);
        make.height.mas_equalTo(18);
        make.centerY.equalTo(self);
        make.left.equalTo(self).offset(20);
    }];
    
    [self.priceLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self);
        make.left.equalTo(self).offset(42);
    }];
}

- (void)setupSendStyle {
    self.buyBtn.hidden = YES;
    [self.sendBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(75);
        make.height.mas_equalTo(30);
        make.centerY.equalTo(self);
        make.right.equalTo(self).offset(-15);
    }];
}

- (void)setupGold:(NSString *)gold day:(NSString *)day {
    self.priceLabel.text = [NSString stringWithFormat:@"%@/%@天",gold,day];
}

- (void)setupBuyButtonText:(NSString *)text {
    [_buyBtn setTitle:text forState:UIControlStateNormal];
}

- (void)buyButtonAction {
    !self.buyGiftButtonAction ?:self.buyGiftButtonAction();
}

- (void)sendButtonAction{
    !self.sendGiftButtonAction ?:self.sendGiftButtonAction();
}

- (void)setIsPopularitySel:(BOOL)isPopularitySel
{
    _isPopularitySel = isPopularitySel;
    if (isPopularitySel) {
        [_sendBtn setTitle:@" 1 " forState:UIControlStateNormal];
        [_sendBtn setImage:[UIImage imageNamed:@"yp_triangle_up"] forState:UIControlStateNormal];
        [_sendBtn setImage:[UIImage imageNamed:@"yp_triangle_down"] forState:UIControlStateSelected];
        [_sendBtn setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, 33)];
        [_sendBtn setImageEdgeInsets:UIEdgeInsetsMake(0, 60, 0, 0)];
    }
    else {
        [_sendBtn setImage:nil forState:UIControlStateNormal];
        [_sendBtn setImage:nil forState:UIControlStateSelected];
        [_sendBtn setTitleEdgeInsets:UIEdgeInsetsZero];
        [_sendBtn setTitle:@"赠送" forState:UIControlStateNormal];
    }
}

#pragma mark - getter/setter

- (UIButton *)buyBtn {
    if (!_buyBtn) {
        _buyBtn = [[UIButton alloc] init];
        [_buyBtn setBackgroundImage:[UIImage imageNamed:@"yp_prop_icon_goumai"] forState:UIControlStateNormal];
        [_buyBtn setTitle:@"购买" forState:UIControlStateNormal];
        [_buyBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_buyBtn.titleLabel setFont:[UIFont boldSystemFontOfSize:14]];
        [_buyBtn addTarget:self action:@selector(buyButtonAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _buyBtn;
}

- (UIButton *)sendBtn {
    if (!_sendBtn) {
        _sendBtn = [[UIButton alloc] init];
        [_sendBtn setBackgroundImage:[UIImage imageNamed:@"yp_prop_icon_zengsong"] forState:UIControlStateNormal];
        [_sendBtn setTitle:@"赠送" forState:UIControlStateNormal];
        [_sendBtn setTitleColor:UIColorHex(7A9DFF) forState:UIControlStateNormal];
        [_sendBtn.titleLabel setFont:[UIFont boldSystemFontOfSize:14]];
        [_sendBtn addTarget:self action:@selector(sendButtonAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _sendBtn;
}

- (UIImageView *)priceIcon {
    if (!_priceIcon) {
        _priceIcon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"yp_prop_icon_jinbida"]];
    }
    return _priceIcon;
}

- (UILabel *)priceLabel {
    if (!_priceLabel) {
        _priceLabel = [[UILabel alloc] init];
        _priceLabel.textColor = UIColorHex(7A9DFF);
        _priceLabel.font = [UIFont boldSystemFontOfSize:14];
    }
    return _priceLabel;
}

@end
