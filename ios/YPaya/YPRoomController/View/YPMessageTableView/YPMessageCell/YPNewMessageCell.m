//
//  YPNewMessageCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPNewMessageCell.h"

static CGFloat const NewMessageCellCotaninerLeft = 5;
static CGFloat const NewMessageCellMessageLeft = 10;
static CGFloat const NewMessageCellMessageRight = 10;

@interface YPNewMessageCell ()

@property (nonatomic, strong) MASConstraint *userTopConstraint;

@end

@implementation YPNewMessageCell

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

- (void)layoutSubviews {
    [super layoutSubviews];
    
    [self updatePreferredMaxLayoutWidth];
}

- (void)updatePreferredMaxLayoutWidth {
    self.messageLabel.preferredMaxLayoutWidth = floor(self.bounds.size.width - NewMessageCellMessageLeft - NewMessageCellMessageRight - NewMessageCellCotaninerLeft);
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.userTopOffset = 0;
    self.backgroundColor = [UIColor clearColor];
    self.contentView.backgroundColor = [UIColor clearColor];
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.userLabel];
    [self.contentView addSubview:self.labelContentView];
    [self.labelContentView addSubview:self.bgImage];
    [self.labelContentView addSubview:self.messageLabel];
}

- (void)layoutControls {
    [self.userLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        self.userTopConstraint = make.top.equalTo(self.contentView).offset(self.userTopOffset);
        make.left.equalTo(self.contentView).offset(5);
        make.right.equalTo(self.contentView).offset(-0);
    }];
    
    [self.labelContentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.userLabel.mas_bottom).offset(5);
        make.left.equalTo(self.contentView).offset(NewMessageCellCotaninerLeft);
        make.right.lessThanOrEqualTo(self.contentView).offset(0);
        make.bottom.equalTo(self.contentView).offset(-6);
    }];
    
    [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.labelContentView).offset(2);
        make.left.equalTo(self.labelContentView).offset(NewMessageCellMessageLeft);
        make.right.equalTo(self.labelContentView).offset(-NewMessageCellMessageRight);
        make.bottom.equalTo(self.labelContentView).offset(-2);
    }];
    
    [self.bgImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.labelContentView);
        make.left.equalTo(self.labelContentView);
        make.right.equalTo(self.labelContentView);
        make.bottom.equalTo(self.labelContentView);
    }];
}

#pragma mark - setters/getters
- (void)setUserTopOffset:(CGFloat)userTopOffset {
    _userTopOffset = userTopOffset;
    
    self.userTopConstraint.offset = userTopOffset;
    [self setNeedsUpdateConstraints];
}

- (YYLabel *)userLabel {
    if (!_userLabel) {
        _userLabel = [YYLabel new];
        _userLabel.numberOfLines = 1;
        _userLabel.backgroundColor = [UIColor clearColor];
    }
    return _userLabel;
}

- (UIView *)labelContentView {
    if (!_labelContentView) {
        _labelContentView = [UIView new];
    }
    return _labelContentView;
}

- (UIImageView *)bgImage {
    if (!_bgImage) {
        _bgImage = [UIImageView new];
        _bgImage.userInteractionEnabled = YES;
    }
    return _bgImage;
}

- (YYLabel *)messageLabel {
    if (!_messageLabel) {
        _messageLabel = [YYLabel new];
        _messageLabel.textColor = [UIColor colorWithHexString:@"#F2F2F2"];
        _messageLabel.font = [UIFont systemFontOfSize:12];
        _messageLabel.numberOfLines = 0;
        _messageLabel.backgroundColor = [UIColor clearColor];
    }
    return _messageLabel;
}

@end
