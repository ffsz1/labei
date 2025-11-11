//
//  YPFamilyManageItemCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyManageItemCell.h"
#import "YPFamilyManageItemModel.h"

NSString *const HJFamilyManageItemCellID = @"HJFamilyManageItemCellID";

@interface YPFamilyManageItemCell ()

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UIImageView *arrowView;
@property (nonatomic, strong, readwrite) UISwitch *switchView;
@property (nonatomic, strong) UIView *lineView;

@end


@implementation YPFamilyManageItemCell

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
- (void)configureWithItem:(YPFamilyManageItemModel *)item {
    self.titleLabel.text = item.title;
    switch (item.type) {
        case XCFamilyManageItemTypeManageFamily:
        case XCFamilyManageItemTypeExit:
        case XCFamilyManageItemTypeApplicationRecord:
        {
            self.arrowView.hidden = NO;
            self.switchView.hidden = YES;
        }
            break;
        case XCFamilyManageItemTypeMessageNotice:
        {
            self.arrowView.hidden = YES;
            self.switchView.hidden = NO;
            [self.switchView setOn:item.enable animated:NO];
        }
            break;
    }
}

#pragma mark - Private methods
- (void)commonInit {
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.titleLabel];
    [self.contentView addSubview:self.arrowView];
    [self.contentView addSubview:self.switchView];
    [self.contentView addSubview:self.lineView];
}

- (void)layoutControls {
    CGSize switchSize = CGSizeMake(41, 23);
    CGSize arrowSize = CGSizeMake(6, 11);
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(16));
        make.top.equalTo(@(20));
        make.bottom.equalTo(@(-20)).priority(750);
    }];
    
    [self.arrowView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(@(-16));
        make.centerY.equalTo(self.titleLabel);
        make.size.equalTo(@(arrowSize));
    }];
    
    [self.switchView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(@(-16));
        make.centerY.equalTo(self.titleLabel);
        make.size.equalTo(@(switchSize));
    }];
    
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(@(0));
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

- (UIImageView *)arrowView {
    if (!_arrowView) {
        _arrowView = [UIImageView new];
        _arrowView.image = [UIImage imageNamed:@"yp_family_icon_arrow"];
        _arrowView.hidden = YES;
    }
    return _arrowView;
}

- (UISwitch *)switchView {
    if (!_switchView) {
        _switchView = [UISwitch new];
        _switchView.tintColor = [UIColor colorWithHexString:@"#DCDCDC"];
        _switchView.onTintColor = [UIColor colorWithHexString:@"#E5A5F2"];
        _switchView.hidden = YES;
        @weakify(self);
        [_switchView addBlockForControlEvents:UIControlEventValueChanged block:^(UISwitch *sender) {
            @strongify(self);
            !self.didSwitchHandler ?: self.didSwitchHandler(sender.isOn);
        }];
    }
    return _switchView;
}

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _lineView;
}

@end

