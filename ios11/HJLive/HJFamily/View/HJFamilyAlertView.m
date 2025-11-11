//
//  HJFamilyAlertView.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyAlertView.h"

@interface HJFamilyAlertView ()

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *messageLabel;
@property (nonatomic, strong) UIButton *cancelButton;
@property (nonatomic, strong) UIButton *actionButton;

@end


@implementation HJFamilyAlertView

#pragma mark - Life cycle
- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self commonInit];
        [self addControls];
        [self layoutControls];
    }
    return self;
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.backgroundColor = [UIColor whiteColor];
    self.layer.cornerRadius = 20.f;
    self.layer.masksToBounds = YES;
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.titleLabel];
    [self addSubview:self.messageLabel];
    [self addSubview:self.cancelButton];
    [self addSubview:self.actionButton];
}

- (void)layoutControls {
    CGSize buttonSize = CGSizeMake(106, 38);
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self).offset(25);
        make.centerX.equalTo(self);
    }];
    
    [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self).offset(35);
        make.right.equalTo(self).offset(-35);
        make.top.equalTo(self.titleLabel.mas_bottom).offset(20);
    }];
    
    [self.cancelButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(buttonSize));
        make.bottom.equalTo(self).offset(-20);
        make.left.equalTo(self).offset(25);
    }];
    
    [self.actionButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(buttonSize));
        make.bottom.equalTo(self).offset(-20);
        make.right.equalTo(self).offset(-25);
    }];
}

#pragma mark - setters/getters
- (void)setTitle:(NSString *)title {
    _title = title;
    
    self.titleLabel.text = title;
}

- (void)setMessage:(NSString *)message {
    _message = message;
    
    self.messageLabel.text = message;
}

- (void)setActionTitle:(NSString *)actionTitle {
    _actionTitle = actionTitle;
    
    [self.actionButton setTitle:actionTitle forState:UIControlStateNormal];
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.textColor = [UIColor colorWithHexString:@"#37353B"];
        _titleLabel.font = [UIFont boldSystemFontOfSize:18];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _titleLabel;
}

- (UILabel *)messageLabel {
    if (!_messageLabel) {
        _messageLabel = [UILabel new];
        _messageLabel.textColor = [UIColor colorWithHexString:@"#37353B"];
        _messageLabel.font = [UIFont boldSystemFontOfSize:15];
        _messageLabel.numberOfLines = 0;
    }
    return _messageLabel;
}

- (UIButton *)cancelButton {
    if (!_cancelButton) {
        _cancelButton = [UIButton new];
        [_cancelButton setTitle:@"再考虑下" forState:UIControlStateNormal];
        [_cancelButton setTitleColor:[UIColor colorWithHexString:@"#49464B"] forState:UIControlStateNormal];
        _cancelButton.layer.cornerRadius = 38.f/2;
        _cancelButton.layer.masksToBounds = YES;
        _cancelButton.layer.borderColor = [UIColor colorWithHexString:@"#A0A0A0"].CGColor;
        _cancelButton.layer.borderWidth = 1.f;
        _cancelButton.exclusiveTouch = YES;
        _cancelButton.titleLabel.font = [UIFont boldSystemFontOfSize:16];
        _cancelButton.backgroundColor = [UIColor colorWithHexString:@"#FFFFFF"];
        @weakify(self);
        [_cancelButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapCancelHandler ?: self.didTapCancelHandler();
        }];
    }
    return _cancelButton;
}

- (UIButton *)actionButton {
    if (!_actionButton) {
        _actionButton = [UIButton new];
        CAGradientLayer *layer = [CAGradientLayer layer];
        layer.frame = CGRectMake(0,0,105.5,38);
        layer.startPoint = CGPointMake(0, 0);
        layer.endPoint = CGPointMake(1, 1);
        layer.colors = @[(__bridge id)[UIColor colorWithRed:153/255.0 green:122/255.0 blue:249/255.0 alpha:1.0].CGColor,(__bridge id)[UIColor colorWithRed:99/255.0 green:50/255.0 blue:198/255.0 alpha:1.0].CGColor];
        layer.locations = @[@(0.0),@(1.0f)];
        [_actionButton.layer addSublayer:layer];
        
        [_actionButton setTitle:@"确认" forState:UIControlStateNormal];
        [_actionButton setTitleColor:[UIColor colorWithHexString:@"#FFFFFF"] forState:UIControlStateNormal];
        _actionButton.layer.cornerRadius = 38.f/2;
        _actionButton.layer.masksToBounds = YES;
        _actionButton.exclusiveTouch = YES;
        _actionButton.titleLabel.font = [UIFont boldSystemFontOfSize:16];
        @weakify(self);
        [_actionButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapActionHandler ?: self.didTapActionHandler();
        }];
    }
    return _actionButton;
}

@end
