//
//  HJFamilyManagerHeaderView.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyManagerHeaderView.h"
#import "NSString+NIMKit.h"

NSString *const HJFamilyManagerHeaderViewID = @"HJFamilyManagerHeaderViewID";

@interface HJFamilyManagerHeaderView ()

@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *idLabel;
@property (nonatomic, strong) UILabel *timeLabel;
@property (nonatomic, strong) UIView *lineView;

@end


@implementation HJFamilyManagerHeaderView

#pragma mark - Life cycle
- (instancetype)initWithReuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithReuseIdentifier:reuseIdentifier];
    if (self) {
        [self commonInit];
        [self addControls];
        [self layoutControls];
    }
    return self;
}

#pragma mark - Event

#pragma mark - Public methods
- (void)configureWithFamilyLogo:(NSString *)familyLogo
                     familyName:(NSString *)familyName
                       familyId:(NSString *)familyId
                           time:(NSTimeInterval)time {
    [self.iconView sd_setImageWithURL:[NSURL URLWithString:[familyLogo cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.titleLabel.text = familyName;
    self.idLabel.text = [NSString stringWithFormat:@"ID:%@", familyId];
    
    NSTimeInterval buffer = time/1000;
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:buffer];
    NSString *stringTime = [date stringWithFormat:@"yyyy-MM-dd"];
    self.timeLabel.text = [NSString stringWithFormat:@"创建于/%@", stringTime];
}

#pragma mark - Private methods
- (void)commonInit {
    self.contentView.backgroundColor = [UIColor whiteColor];
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.iconView];
    [self.contentView addSubview:self.titleLabel];
    [self.contentView addSubview:self.idLabel];
    [self.contentView addSubview:self.timeLabel];
    [self.contentView addSubview:self.lineView];
}

- (void)layoutControls {
    CGSize iconSize = CGSizeMake(65, 65);
    
    [self.iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(@(15));
        make.left.equalTo(@(15));
        make.bottom.equalTo(@(-30)).priority(750);
        make.size.equalTo(@(iconSize));
    }];
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.iconView.mas_right).offset(10);
        make.top.equalTo(self.iconView).offset(15);
        make.right.equalTo(@(0));
    }];
    
    [self.idLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.iconView.mas_right).offset(10);
        make.bottom.equalTo(self.iconView).offset(-10);
    }];
    
    [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.idLabel.mas_right).offset(16);
        make.centerY.equalTo(self.idLabel);
    }];
    
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.bottom.equalTo(self.contentView);
        make.height.equalTo(@(10));
    }];
}

#pragma mark - setters/getters
- (UIImageView *)iconView {
    if (!_iconView) {
        _iconView = [UIImageView new];
        _iconView.layer.cornerRadius = 65/2.f;
        _iconView.layer.masksToBounds = YES;
    }
    return _iconView;
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.textColor = [UIColor colorWithHexString:@"#4B404F"];
        _titleLabel.font = [UIFont boldSystemFontOfSize:17];
    }
    return _titleLabel;
}

- (UILabel *)idLabel {
    if (!_idLabel) {
        _idLabel = [UILabel new];
        _idLabel.textColor = [UIColor colorWithHexString:@"#89858C"];
        _idLabel.font = [UIFont systemFontOfSize:12];
    }
    return _idLabel;
}

- (UILabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [UILabel new];
        _timeLabel.textColor = [UIColor colorWithHexString:@"#89858C"];
        _timeLabel.font = [UIFont systemFontOfSize:12];
    }
    return _timeLabel;
}

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _lineView;
}

@end
