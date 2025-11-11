//
//  XCFamilyDetailsView.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "XCFamilyDetailsView.h"
#import "HJFamilyMembersView.h"

#import "HJFamilyModel.h"

#import "NSString+NIMKit.h"
#import "MJRefresh.h"

#import "FamilyDefines.h"

@interface XCFamilyDetailsView ()

@property (nonatomic, strong) UIScrollView *scrollView;
@property (nonatomic, strong) UIView *contentView;
@property (nonatomic, strong) UIImageView *topBackgroundImageView;
@property (nonatomic, strong) UIVisualEffectView *effectView;
@property (nonatomic, strong) UIButton *photoButton;
@property (nonatomic, strong) UIImageView *infoCardView;
@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) UIButton *logoPhotoButton;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) UILabel *IDLabel;
@property (nonatomic, strong) UIButton *chatButton;
@property (nonatomic, strong) UIView *noticeContentView;
@property (nonatomic, strong) UILabel *noticeTitleLabel;
@property (nonatomic, strong) UIImageView *noticeArrowView;
@property (nonatomic, strong) UILabel *noticeLabel;
@property (nonatomic, strong) UIView *noticeLineView;
@property (nonatomic, strong) UIView *membersContentView;
@property (nonatomic, strong) UILabel *memberTitleLabel;
@property (nonatomic, strong) UIImageView *memberArrowView;
@property (nonatomic, strong) HJFamilyMembersView *memebersView;
@property (nonatomic, strong) UIView *memberLineView;

@end


@implementation XCFamilyDetailsView

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
- (void)configureWithDetails:(HJFamilyModel *)detailsInfo {
    if (!detailsInfo) return;
    
    switch (detailsInfo.roleStatus) {
        case XCFamilyRoleStatusLeader:
        {
            self.photoButton.hidden = NO;
            self.logoPhotoButton.hidden = NO;
        }
            break;
        case XCFamilyRoleStatusManager:
        case XCFamilyRoleStatusMember:
        {
            self.photoButton.hidden = YES;
            self.logoPhotoButton.hidden = YES;
        }
            break;
    }
    
    [self.topBackgroundImageView sd_setImageWithURL:[NSURL URLWithString:JX_STR_AVOID_nil(detailsInfo.bgimg)] placeholderImage:[UIImage imageNamed:default_avatar]];
    [self.iconView sd_setImageWithURL:[NSURL URLWithString:[detailsInfo.familyLogo cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.nameLabel.text = detailsInfo.familyName;
    self.IDLabel.text = [NSString stringWithFormat:@"ID:%@", detailsInfo.familyId];
    self.chatButton.hidden = !detailsInfo.familyId.length;
    self.noticeTitleLabel.text = @"家族公告";
    self.noticeLabel.text = detailsInfo.familyNotice;
    self.memberTitleLabel.text = [NSString stringWithFormat:@"家族成员/%@", detailsInfo.member];
    self.memebersView.items = detailsInfo.familyUsersDTOS;
}

- (void)endHeaderRefreshing {
    [self.scrollView.mj_header endRefreshing];
}

#pragma mark - Private methods
- (void)commonInit {
    
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.scrollView];
    [self.scrollView addSubview:self.contentView];
    [self.contentView addSubview:self.topBackgroundImageView];
    [self.contentView addSubview:self.effectView];
    [self.contentView addSubview:self.photoButton];
    [self.contentView addSubview:self.infoCardView];
    [self.contentView addSubview:self.iconView];
    [self.contentView addSubview:self.logoPhotoButton];
    [self.contentView addSubview:self.nameLabel];
    [self.contentView addSubview:self.IDLabel];
    [self.contentView addSubview:self.chatButton];
    [self.contentView addSubview:self.noticeContentView];
    [self.noticeContentView addSubview:self.noticeTitleLabel];
    [self.noticeContentView addSubview:self.noticeArrowView];
    [self.noticeContentView addSubview:self.noticeLabel];
    [self.noticeContentView addSubview:self.noticeLineView];
    [self.contentView addSubview:self.membersContentView];
    [self.membersContentView addSubview:self.memberTitleLabel];
    [self.membersContentView addSubview:self.memberArrowView];
    [self.membersContentView addSubview:self.memebersView];
    [self.membersContentView addSubview:self.memberLineView];
}

- (void)layoutControls {
    CGSize iconSize = CGSizeMake(90, 90);
    CGSize chatSize = CGSizeMake(104, 42);
    CGSize photoSize = CGSizeMake(44, 44);
    CGSize arrowSize = CGSizeMake(6, 11);
    UIView *lastView = nil;
    
    [self.scrollView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.equalTo(self);
    }];
    
    [self.contentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.scrollView);
        make.width.equalTo(self.scrollView);
    }];
    
    [self.topBackgroundImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.equalTo(@(0));
        make.height.equalTo(@(95));
    }];
    
    [self.effectView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.height.equalTo(self.topBackgroundImageView);
    }];
    
    [self.photoButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(photoSize));
        make.top.equalTo(@(5));
        make.right.equalTo(@(-5));
    }];
    
    [self.infoCardView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(15));
        make.right.equalTo(@(-15));
        make.height.equalTo(@(130));
        make.top.equalTo(@(90));
    }];
    
    [self.iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(iconSize));
        make.centerX.equalTo(self.infoCardView);
        make.centerY.equalTo(self.infoCardView.mas_top);
    }];
    
    [self.logoPhotoButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(photoSize));
        make.centerX.equalTo(self.iconView.mas_right).offset(-15);
        make.centerY.equalTo(self.iconView.mas_bottom).offset(-15);
    }];
    
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.iconView.mas_bottom).offset(10);
        make.left.right.equalTo(@(0));
    }];
    
    [self.IDLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.nameLabel.mas_bottom).offset(7);
        make.left.right.equalTo(@(0));
    }];
    
    [self.chatButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(chatSize));
        make.centerX.equalTo(self.infoCardView);
        make.centerY.equalTo(self.infoCardView.mas_bottom).offset(-5);
    }];
    
    [self.noticeContentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.infoCardView.mas_bottom).offset(38);
        make.left.equalTo(@(15));
        make.right.equalTo(@(-15));
        make.height.equalTo(@(100));
    }];
    
    [self.noticeTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.equalTo(self.noticeContentView);
        make.right.equalTo(self.noticeArrowView.mas_left).offset(-5);
    }];
    
    [self.noticeArrowView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(arrowSize));
        make.centerY.equalTo(self.noticeTitleLabel);
        make.right.equalTo(self.noticeContentView);
    }];
    
    [self.noticeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.noticeTitleLabel.mas_bottom).offset(15);
        make.left.right.equalTo(self.noticeContentView);
    }];
    
    [self.noticeLineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.equalTo(@(0.5));
        make.left.right.bottom.equalTo(self.noticeContentView);
    }];
    
    [self.membersContentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.noticeContentView.mas_bottom).offset(20);
        make.left.equalTo(@(15));
        make.right.equalTo(@(-15));
        make.height.equalTo(@(100));
    }];
    
    [self.memberTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.equalTo(self.membersContentView);
        make.right.equalTo(self.memberArrowView.mas_left).offset(-5);
    }];
    
    [self.memberArrowView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(arrowSize));
        make.centerY.equalTo(self.memberTitleLabel);
        make.right.equalTo(self.membersContentView);
    }];
    
    [self.memebersView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.memberTitleLabel.mas_bottom).offset(15);
        make.left.right.equalTo(self.membersContentView);
    }];
    
    [self.memberLineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.equalTo(@(0.5));
        make.left.right.bottom.equalTo(self.membersContentView);
    }];
    
    lastView = self.membersContentView;
    [self.contentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(lastView.mas_bottom).priority(750);
    }];
}

#pragma mark - setters/getters
- (UIScrollView *)scrollView {
    if (!_scrollView) {
        _scrollView = [UIScrollView new];
        _scrollView.showsVerticalScrollIndicator = NO;
        _scrollView.showsHorizontalScrollIndicator = NO;
        @weakify(self);
        _scrollView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
            @strongify(self);
            !self.didHeaderRefreshHandler ?: self.didHeaderRefreshHandler();
        }];
    }
    return _scrollView;
}

- (UIView *)contentView {
    if (!_contentView) {
        _contentView = [UIView new];
    }
    return _contentView;
}

- (UIImageView *)topBackgroundImageView {
    if (!_topBackgroundImageView) {
        _topBackgroundImageView = [UIImageView new];
        _topBackgroundImageView.contentMode = UIViewContentModeScaleAspectFill;
        _topBackgroundImageView.layer.masksToBounds = YES;
    }
    return _topBackgroundImageView;
}

- (UIVisualEffectView *)effectView {
    if (!_effectView) {
        UIBlurEffect *effect = [UIBlurEffect effectWithStyle:UIBlurEffectStyleLight];
        _effectView = [[UIVisualEffectView alloc] initWithEffect:effect];
    }
    return _effectView;
}

- (UIButton *)photoButton {
    if (!_photoButton) {
        _photoButton = [UIButton new];
        [_photoButton setImage:[UIImage imageNamed:@"hj_family_creat_photography_white"] forState:UIControlStateNormal];
        _photoButton.hidden = YES;
        @weakify(self);
        [_photoButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapPhotoHandler ?: self.didTapPhotoHandler();
        }];
    }
    return _photoButton;
}

- (UIButton *)logoPhotoButton {
    if (!_logoPhotoButton) {
        _logoPhotoButton = [UIButton new];
        [_logoPhotoButton setImage:[UIImage imageNamed:@"hj_family_creat_photography"] forState:UIControlStateNormal];
        _logoPhotoButton.hidden = YES;
        @weakify(self);
        [_logoPhotoButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapLogoPhotoHandler ?: self.didTapLogoPhotoHandler();
        }];
    }
    return _logoPhotoButton;
}

- (UIImageView *)infoCardView {
    if (!_infoCardView) {
        _infoCardView = [UIImageView new];
        _infoCardView.image = [UIImage imageNamed:@"hj_family_my_bg"];
    }
    return _infoCardView;
}

- (UIImageView *)iconView {
    if (!_iconView) {
        _iconView = [UIImageView new];
        _iconView.layer.cornerRadius = 90/2.f;
        _iconView.layer.masksToBounds = YES;
        _iconView.layer.borderColor = [UIColor whiteColor].CGColor;
        _iconView.layer.borderWidth = 3.f;
    }
    return _iconView;
}

- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.textColor = [UIColor colorWithHexString:@"#4B404F"];
        _nameLabel.font = [UIFont boldSystemFontOfSize:20];
        _nameLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _nameLabel;
}

- (UILabel *)IDLabel {
    if (!_IDLabel) {
        _IDLabel = [UILabel new];
        _IDLabel.textColor = [UIColor colorWithHexString:@"#A091A7"];
        _IDLabel.font = [UIFont systemFontOfSize:13];
        _IDLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _IDLabel;
}

- (UIButton *)chatButton {
    if (!_chatButton) {
        _chatButton = [UIButton new];
        [_chatButton setBackgroundImage:[UIImage imageNamed:@"hj_family_bg_chat"] forState:UIControlStateNormal];
        [_chatButton setTitle:@"家族群聊" forState:UIControlStateNormal];
        [_chatButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _chatButton.titleLabel.font = [UIFont boldSystemFontOfSize:15];
        _chatButton.exclusiveTouch = YES;
        _chatButton.hidden = YES;
        @weakify(self);
        [_chatButton addBlockForControlEvents:UIControlEventTouchUpInside block:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapChatHandler ?: self.didTapChatHandler();
        }];
    }
    return _chatButton;
}

- (UIView *)noticeContentView {
    if (!_noticeContentView) {
        _noticeContentView = [UIView new];
        @weakify(self);
        [_noticeContentView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapAnnouncementHandler ?: self.didTapAnnouncementHandler();
        }]];
    }
    return _noticeContentView;
}

- (UILabel *)noticeTitleLabel {
    if (!_noticeTitleLabel) {
        _noticeTitleLabel = [UILabel new];
        _noticeTitleLabel.textColor = [UIColor colorWithHexString:@"#37353B"];
        _noticeTitleLabel.font = [UIFont boldSystemFontOfSize:18];
    }
    return _noticeTitleLabel;
}

- (UIImageView *)noticeArrowView {
    if (!_noticeArrowView) {
        _noticeArrowView = [UIImageView new];
        _noticeArrowView.image = [UIImage imageNamed:@"family_more"];
    }
    return _noticeArrowView;
}

- (UILabel *)noticeLabel {
    if (!_noticeLabel) {
        _noticeLabel = [UILabel new];
        _noticeLabel.textColor = [UIColor colorWithHexString:@"#89858C"];
        _noticeLabel.font = [UIFont systemFontOfSize:15];
        _noticeLabel.numberOfLines = 2;
    }
    return _noticeLabel;
}

- (UIView *)noticeLineView {
    if (!_noticeLineView) {
        _noticeLineView = [UIView new];
        _noticeLineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _noticeLineView;
}

- (UIView *)membersContentView {
    if (!_membersContentView) {
        _membersContentView = [UIView new];
        @weakify(self);
        [_membersContentView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            @strongify(self);
            !self.didTapMembersHandler ?: self.didTapMembersHandler();
        }]];
    }
    return _membersContentView;
}

- (UILabel *)memberTitleLabel {
    if (!_memberTitleLabel) {
        _memberTitleLabel = [UILabel new];
        _memberTitleLabel.textColor = [UIColor colorWithHexString:@"#37353B"];
        _memberTitleLabel.font = [UIFont boldSystemFontOfSize:18];
    }
    return _memberTitleLabel;
}

- (UIImageView *)memberArrowView {
    if (!_memberArrowView) {
        _memberArrowView = [UIImageView new];
        _memberArrowView.image = [UIImage imageNamed:@"family_more"];
    }
    return _memberArrowView;
}

- (HJFamilyMembersView *)memebersView {
    if (!_memebersView) {
        _memebersView = [HJFamilyMembersView new];
        @weakify(self);
        _memebersView.didTapHandler = ^{
            @strongify(self);
            !self.didTapMembersHandler ?: self.didTapMembersHandler();
        };
    }
    return _memebersView;
}

- (UIView *)memberLineView {
    if (!_memberLineView) {
        _memberLineView = [UIView new];
        _memberLineView.backgroundColor = [UIColor colorWithHexString:@"#EEEEEE"];
    }
    return _memberLineView;
}

@end
