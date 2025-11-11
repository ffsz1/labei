//
//  YPFamilyApplicationRecordCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyApplicationRecordCell.h"
#import "YPFamilyMessage.h"
#import "NSString+YPNIMKit.h"

NSString *const HJFamilyApplicationRecordCellID = @"HJFamilyApplicationRecordCellID";

@interface YPFamilyApplicationRecordCell ()

@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) UIButton *agreeButton;
@property (nonatomic, strong) UIButton *ignoreButton;
@property (nonatomic, strong) UIImageView *levelView;
@property (nonatomic, strong) UIImageView *charmLevelView;
@property (nonatomic, strong) UILabel *stateLabel;
@property (nonatomic, strong) UIView *lineView;

@property (nonatomic, strong) MASConstraint *nameLabelRightToAgreeConstraint;
@property (nonatomic, strong) MASConstraint *nameLabelRightToStateConstraint;
@property (nonatomic, strong) MASConstraint *charmLeveLeftToIconConstraint;
@property (nonatomic, strong) MASConstraint *charmLeveLeftToLevelConstraint;

@end

@implementation YPFamilyApplicationRecordCell

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
- (void)configureWithApplicationInfo:(YPFamilyMessage *)info {
    NSString *stateText = nil;
    BOOL shouldUpdate = NO;
    switch (info.status) {
        case XCFamilyApplicationStatusUndefie:
        {
            shouldUpdate = YES;
        }
            break;
        case XCFamilyApplicationStatusSuccess:
        {
            shouldUpdate = NO;
            stateText = @"已同意";
        }
            break;
        case XCFamilyApplicationStatusFailure:
        {
            shouldUpdate = NO;
            stateText = @"已忽略";
        }
            break;
        case XCFamilyApplicationStatusAutoExit:
        {
            shouldUpdate = NO;
            stateText = @"已同意";
        }
            break;
    }
    
    if (shouldUpdate) {
        self.stateLabel.hidden = YES;
        self.agreeButton.hidden = NO;
        self.ignoreButton.hidden = NO;
        
        [self.nameLabelRightToStateConstraint deactivate];
        [self.nameLabelRightToAgreeConstraint activate];
    } else {
        self.stateLabel.hidden = NO;
        self.agreeButton.hidden = YES;
        self.ignoreButton.hidden = YES;
        
        [self.nameLabelRightToStateConstraint activate];
        [self.nameLabelRightToAgreeConstraint deactivate];
    }
    
    self.levelView.hidden = !info.level;
    self.charmLevelView.hidden = !info.charm;
    
    NSString *levelImage = @"";
    NSString *charmImage = @"";
    
    if (info.level && info.charm) {
        levelImage = [self setExperMent:info.level];
//        charmImage = [NSString stringWithFormat:@"ml%zd", info.charm];
        charmImage = [NSString getCharmLevelImageName:info.charm];
        
        [self.charmLeveLeftToIconConstraint deactivate];
        [self.charmLeveLeftToLevelConstraint activate];
    } else if (info.level) {
        levelImage = [self setExperMent:info.level];
        
        [self.charmLeveLeftToIconConstraint deactivate];
        [self.charmLeveLeftToLevelConstraint activate];
    } else if (info.charm) {
//        charmImage = [NSString stringWithFormat:@"ml%zd", info.charm];
        charmImage = [NSString getCharmLevelImageName:info.charm];
        
        [self.charmLeveLeftToIconConstraint activate];
        [self.charmLeveLeftToLevelConstraint deactivate];
    }
    
    [self.iconView sd_setImageWithURL:[NSURL URLWithString:[info.avatar cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.nameLabel.text = info.nick;
    self.levelView.image = [UIImage imageNamed:levelImage];
    self.charmLevelView.image = [UIImage imageNamed:charmImage];
    self.stateLabel.text = stateText;
    
    [self setNeedsUpdateConstraints];
}

#pragma mark - Private methods
- (void)commonInit {
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

-(NSString *)setExperMent:(NSInteger)level {
    return [NSString getMoneyLevelImageName:level];
//    NSInteger num = level /5;
//    NSInteger nuber = level % 5;
//    if (level %5 == 0) {
//        num = num -1;
//        nuber = nuber + 5;
//    }
//    NSString *dengji;
//    if (num == 0) {
//        dengji = @"qingtong";
//    }else if(num == 1){
//        dengji = @"baiyin";
//    }else if(num == 2){
//        dengji = @"huangjin";
//    }else if(num == 3){
//        dengji = @"bojin";
//    }else if(num == 4){
//        dengji = @"zuanshi";
//    }else if(num == 5){
//        dengji = @"xingyao";
//    }else if(num == 6){
//        dengji = @"wangzhe";
//    }
//    NSString *name = [NSString stringWithFormat:@"%@%ld",dengji,nuber];
//    return name;
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.iconView];
    [self.contentView addSubview:self.nameLabel];
    [self.contentView addSubview:self.levelView];
    [self.contentView addSubview:self.charmLevelView];
    [self.contentView addSubview:self.agreeButton];
    [self.contentView addSubview:self.ignoreButton];
    [self.contentView addSubview:self.stateLabel];
    [self.contentView addSubview:self.lineView];
}

- (void)layoutControls {
    CGSize iconSize = CGSizeMake(60, 60);
    CGSize buttonSize = CGSizeMake(50, 30);
    
    [self.iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(15));
        make.top.equalTo(@(20));
        make.bottom.equalTo(@(-20)).priority(750);
        make.size.equalTo(@(iconSize));
    }];
    
    [self.ignoreButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(buttonSize));
        make.right.equalTo(@(-15));
        make.centerY.equalTo(self.iconView);
    }];
    
    [self.agreeButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(buttonSize));
        make.right.equalTo(self.ignoreButton.mas_left).offset(-10);
        make.centerY.equalTo(self.iconView);
    }];
    
    [self.stateLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(@(-15));
        make.centerY.equalTo(self.iconView);
    }];
    
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.iconView.mas_right).offset(10);
        make.top.equalTo(self.iconView).offset(5);
        self.nameLabelRightToAgreeConstraint = make.right.equalTo(self.agreeButton.mas_left).offset(-10).priority(750);
        self.nameLabelRightToStateConstraint = make.right.equalTo(self.stateLabel.mas_left).offset(-10).priority(500);
    }];
    
    [self.levelView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.iconView.mas_right).offset(10);
        make.top.equalTo(self.nameLabel.mas_bottom).offset(7);
    }];
    
    [self.charmLevelView mas_makeConstraints:^(MASConstraintMaker *make) {
        self.charmLeveLeftToIconConstraint = make.left.equalTo(self.iconView.mas_right).offset(10).priority(500);
        self.charmLeveLeftToLevelConstraint = make.left.equalTo(self.levelView.mas_right).offset(10).priority(750);
        make.top.equalTo(self.nameLabel.mas_bottom).offset(7);
    }];
    
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(88));
        make.height.equalTo(@(0.5));
        make.bottom.equalTo(@(0));
        make.right.equalTo(@(0));
    }];
    
    [self.nameLabel setContentHuggingPriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
    [self.stateLabel setContentHuggingPriority:UILayoutPriorityDefaultHigh forAxis:UILayoutConstraintAxisHorizontal];
    
    [self.nameLabel setContentCompressionResistancePriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
    [self.stateLabel setContentCompressionResistancePriority:UILayoutPriorityDefaultHigh forAxis:UILayoutConstraintAxisHorizontal];
    
    [self.nameLabelRightToStateConstraint activate];
    [self.nameLabelRightToAgreeConstraint deactivate];
    [self.charmLeveLeftToIconConstraint deactivate];
    [self.charmLeveLeftToLevelConstraint activate];
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

- (UIButton *)ignoreButton {
    if (!_ignoreButton) {
        _ignoreButton = [UIButton new];
        _ignoreButton.backgroundColor = [UIColor colorWithHexString:@"#FED6E4"];
        _ignoreButton.exclusiveTouch = YES;
        [_ignoreButton setTitle:@"忽略" forState:UIControlStateNormal];
        [_ignoreButton setTitleColor:[UIColor colorWithHexString:@"#F57495"] forState:UIControlStateNormal];
        _ignoreButton.titleLabel.font = [UIFont boldSystemFontOfSize:14];
        _ignoreButton.layer.cornerRadius = 30/2.f;
        _ignoreButton.layer.masksToBounds = YES;
        _ignoreButton.hidden = YES;
        @weakify(self);
        [_ignoreButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapIgnoreHandler ?: self.didTapIgnoreHandler();
        }];
    }
    return _ignoreButton;
}

- (UIButton *)agreeButton {
    if (!_agreeButton) {
        _agreeButton = [UIButton new];
        _agreeButton.backgroundColor = [UIColor colorWithHexString:@"#EEE9FD"];
        _agreeButton.exclusiveTouch = YES;
        [_agreeButton setTitle:@"同意" forState:UIControlStateNormal];
        [_agreeButton setTitleColor:[UIColor colorWithHexString:@"#9574F5"] forState:UIControlStateNormal];
        _agreeButton.titleLabel.font = [UIFont boldSystemFontOfSize:14];
        _agreeButton.layer.cornerRadius = 30/2.f;
        _agreeButton.layer.masksToBounds = YES;
        _agreeButton.hidden = YES;
        @weakify(self);
        [_agreeButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapAgreeHandler ?: self.didTapAgreeHandler();
        }];
    }
    return _agreeButton;
}

- (UILabel *)stateLabel {
    if (!_stateLabel) {
        _stateLabel = [UILabel new];
        _stateLabel.font = [UIFont boldSystemFontOfSize:14];
        _stateLabel.textColor = [UIColor colorWithHexString:@"#A9A8A9"];
        _stateLabel.hidden = YES;
    }
    return _stateLabel;
}

- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.font = [UIFont boldSystemFontOfSize:18];
        _nameLabel.textColor = [UIColor colorWithHexString:@"#37353B"];
    }
    return _nameLabel;
}

- (UIImageView *)levelView {
    if (!_levelView) {
        _levelView = [UIImageView new];
    }
    return _levelView;
}

- (UIImageView *)charmLevelView {
    if (!_charmLevelView) {
        _charmLevelView = [UIImageView new];
    }
    return _charmLevelView;
}

- (UIView *)lineView {
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _lineView;
}
@end
