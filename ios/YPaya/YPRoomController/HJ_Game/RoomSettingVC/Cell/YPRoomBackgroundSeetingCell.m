//
//  YPRoomBackgroundSeetingCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomBackgroundSeetingCell.h"

NSString *const HJRoomBackgroundSeetingCellID = @"HJRoomBackgroundSeetingCellID";

@interface YPRoomBackgroundSeetingCell ()

@property (nonatomic, strong) UIImageView *backgroundImageView;
@property (nonatomic, strong) UIButton *tickButton;
@property (nonatomic, strong) UILabel *titleLabel;

@end

@implementation YPRoomBackgroundSeetingCell

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
- (void)configureWithBackgroundInfo:(YPRoomBgModel *)roomInfo selected:(BOOL)selected {
    if ([roomInfo.picUrl hasPrefix:@"http:"] || [roomInfo.picUrl hasPrefix:@"https"]) {
        [self.backgroundImageView sd_setImageWithURL:[NSURL URLWithString:roomInfo.picUrl] placeholderImage:[UIImage imageNamed:placeholder_image_square]];
    } else {
        self.backgroundImageView.image = [UIImage imageNamed:roomInfo.picUrl];
    }
    self.titleLabel.text = roomInfo.name;
    self.tickButton.hidden = !selected;
}

#pragma mark - Private methods
- (void)commonInit {
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.backgroundImageView];
    [self.contentView addSubview:self.tickButton];
    [self.contentView addSubview:self.titleLabel];
}

- (void)layoutControls {
    [self.backgroundImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.equalTo(self.contentView);
        make.bottom.equalTo(self.contentView).priority(750).offset(-20);
    }];
    
    [self.tickButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(@(7));
        make.right.equalTo(@(-7));
        make.size.equalTo(@(20));
    }];
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self);
        make.bottom.equalTo(self.contentView);
    }];
}

#pragma mark - setters/getters
- (UIImageView *)backgroundImageView {
    if (!_backgroundImageView) {
        _backgroundImageView = [UIImageView new];
        _backgroundImageView.userInteractionEnabled = YES;
    }
    return _backgroundImageView;
}

- (UIButton *)tickButton {
    if (!_tickButton) {
        _tickButton = [UIButton new];
        [_tickButton setImage:[UIImage imageNamed:@"yp_room_gou_icon"] forState:UIControlStateNormal];
        _tickButton.hidden = YES;
    }
    return _tickButton;
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.textColor = [UIColor blackColor];
        _titleLabel.font = [UIFont systemFontOfSize:14];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _titleLabel;
}

@end
