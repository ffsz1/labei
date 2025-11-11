//
//  HJFamilyMemberTableCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyMemberTableCell.h"
#import "HJFamilyTextTagViewLabel.h"
#import "HJFamilyInfoDetail.h"
#import "NSString+NIMKit.h"
#import "FamilyDefines.h"

NSString *const HJFamilyMemberTableCellID = @"HJFamilyMemberTableCellID";

@interface HJFamilyMemberTableCell ()

@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) HJFamilyTextTagViewLabel *tagView;
@property (nonatomic, strong) UIView *lineView;

@end

@implementation HJFamilyMemberTableCell

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
}

- (void)layoutControls {
    CGSize iconSize = CGSizeMake(60, 60);
    
    [self.iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(17));
        make.top.equalTo(@(20));
        make.bottom.equalTo(@(-20)).priority(750);
        make.size.equalTo(@(iconSize));
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
        make.left.equalTo(@(88));
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
        _tagView = [[HJFamilyTextTagViewLabel alloc] init];
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

