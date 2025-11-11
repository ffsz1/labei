//
//  HJFamilyManagerSettingCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyManagerSettingCell.h"
#import "HJFamilyTextTagViewLabel.h"
#import "HJFamilyInfoDetail.h"
#import "NSString+NIMKit.h"

NSString *const HJFamilyManagerSettingCellID = @"HJFamilyManagerSettingCellID";

@interface HJFamilyManagerSettingCell ()
    
    @property (nonatomic, strong) UIImageView *iconView;
    @property (nonatomic, strong) UILabel *nameLabel;
    @property (nonatomic, strong) HJFamilyTextTagViewLabel *tagView;
    @property (nonatomic, strong) UIView *lineView;
    @property (nonatomic, strong) UIButton *actionButon;
    @property (nonatomic, strong) UIButton *deleteButton;
    
    @end

@implementation HJFamilyManagerSettingCell
    
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
- (void)configureWithMemberInfo:(HJFamilyInfoDetail *)memberInfo {
    [self.iconView sd_setImageWithURL:[NSURL URLWithString:[memberInfo.avatar cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.nameLabel.text = memberInfo.nike;
    [self.tagView configureWithRoleStatus:memberInfo.roleStatus level:memberInfo.level];
    
    self.actionButon.hidden = YES;
    self.deleteButton.hidden = YES;
    switch (memberInfo.roleStatus) {
        case XCFamilyRoleStatusLeader:
        break;
        case XCFamilyRoleStatusManager:
        {
            self.actionButon.hidden = YES;
            self.deleteButton.hidden = NO;
        }
        break;
        case XCFamilyRoleStatusMember:
        {
            self.actionButon.hidden = NO;
            self.deleteButton.hidden = YES;
        }
        break;
    }
    self.actionButon.selected = memberInfo.isSelected;
}
    
#pragma mark - Private methods
- (void)commonInit {
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}
    
#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.iconView];
    [self.contentView addSubview:self.nameLabel];
    [self.contentView addSubview:self.tagView];
    [self.contentView addSubview:self.lineView];
    [self.contentView addSubview:self.actionButon];
    [self.contentView addSubview:self.deleteButton];
}
    
- (void)layoutControls {
    CGSize iconSize = CGSizeMake(60, 60);
    CGSize actionSize = CGSizeMake(40, 40);
    
    [self.iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(47));
        make.top.equalTo(@(20));
        make.bottom.equalTo(@(-20)).priority(750);
        make.size.equalTo(@(iconSize));
    }];
    
    [self.actionButon mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(actionSize));
        make.left.equalTo(@(5));
        make.centerY.equalTo(self.iconView);
    }];
    
    [self.deleteButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(actionSize));
        make.left.equalTo(@(5));
        make.centerY.equalTo(self.iconView);
    }];
    
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.iconView.mas_right).offset(10);
        make.top.equalTo(self.iconView).offset(5);
        make.right.equalTo(@(0));
    }];
    
    [self.tagView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.iconView.mas_right).offset(10);
        make.bottom.equalTo(self.iconView).offset(-4);
    }];
    
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(119));
        make.height.equalTo(@(0.5));
        make.bottom.equalTo(@(0));
        make.right.equalTo(@(0));
    }];
}
    
#pragma mark - setters/getters
- (UIImageView *)iconView {
    if (!_iconView) {
        _iconView = [UIImageView new];
        _iconView.layer.cornerRadius = 60/2.f;
        _iconView.layer.masksToBounds = YES;
    }
    return _iconView;
}
    
- (UIButton *)actionButon {
    if (!_actionButon) {
        _actionButon = [UIButton new];
        [_actionButon setImage:[UIImage imageNamed:@"hj_family_icon_tick_normal"] forState:UIControlStateNormal];
        [_actionButon setImage:[UIImage imageNamed:@"hj_family_icon_tick_selected"] forState:UIControlStateSelected];
        _actionButon.selected = NO;
        @weakify(self);
        [_actionButon addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapActionHandler ?: self.didTapActionHandler();
        }];
    }
    return _actionButon;
}
    
    - (UIButton *)deleteButton {
        if (!_deleteButton) {
            _deleteButton = [UIButton new];
            [_deleteButton setImage:[UIImage imageNamed:@"hj_family_icon_delete"] forState:UIControlStateNormal];
            @weakify(self);
            [_deleteButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
                @strongify(self);
                !self.didTapDeleteHandler ?: self.didTapDeleteHandler();
            }];
        }
        return _deleteButton;
    }
    
- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.font = [UIFont boldSystemFontOfSize:18];
        _nameLabel.textColor = [UIColor colorWithHexString:@"37353B"];
    }
    return _nameLabel;
}
    
- (HJFamilyTextTagViewLabel *)tagView {
    if (!_tagView) {
        _tagView = [HJFamilyTextTagViewLabel new];
    }
    return _tagView;
}
    
- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _lineView;
}
    
    @end
