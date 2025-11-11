//
//  HJFamilyJoinWaySettingCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyJoinWaySettingCell.h"
#import "HJFamilyManageJoinWayModel.h"

NSString *const HJFamilyJoinWaySettingCellID = @"HJFamilyJoinWaySettingCellID";

@interface HJFamilyJoinWaySettingCell ()

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UIImageView *tickView;
@property (nonatomic, strong) UIView *lineView;

@end

@implementation HJFamilyJoinWaySettingCell

#pragma mark - Life cycle
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self commonInit];
        [self addControls];
        [self layoutControls];
    }
    return self;
}

#pragma mark - Event

#pragma mark - Public methods
- (void)configureWithItem:(HJFamilyManageJoinWayModel *)item {
    self.titleLabel.text = item.title;
    self.tickView.hidden = !item.selected;
}

#pragma mark - Private methods
- (void)commonInit {
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.titleLabel];
    [self.contentView addSubview:self.tickView];
    [self.contentView addSubview:self.lineView];
}

- (void)layoutControls {
    CGSize tickSize = CGSizeMake(20, 20);
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(16));
        make.top.equalTo(@(20));
        make.bottom.equalTo(@(-20)).priority(750);
    }];
    
    [self.tickView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(@(-15));
        make.centerY.equalTo(self.titleLabel);
        make.size.equalTo(@(tickSize));
    }];
    
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(15));
        make.right.equalTo(@(0));
        make.height.equalTo(@(0.5));
        make.bottom.equalTo(@(0));
    }];
}

#pragma mark - setters/getters
- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.font = [UIFont boldSystemFontOfSize:18];
        _titleLabel.textColor = [UIColor colorWithHexString:@"#37353B"];
    }
    return _titleLabel;
}

- (UIImageView *)tickView {
    if (!_tickView) {
        _tickView = [UIImageView new];
        _tickView.image = [UIImage imageNamed:@"hj_family_icon_tick_selected"];
        _tickView.hidden = YES;
    }
    return _tickView;
}

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _lineView;
}

@end

