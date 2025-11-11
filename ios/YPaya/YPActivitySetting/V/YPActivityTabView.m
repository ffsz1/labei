//
//  YPActivityTabView.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPActivityTabView.h"


NSString *const ActivityTabViewHeaderViewID = @"ActivityTabViewHeaderViewID";

@interface YPActivityTabView()
@property (nonatomic, strong) UILabel *titleLabel;
@end

@implementation YPActivityTabView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

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

#pragma mark - Private methods
- (void)commonInit {
    self.contentView.backgroundColor = [UIColor whiteColor];
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView addSubview:self.titleLabel];
}

- (void)layoutControls {
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(@20);
        make.centerY.equalTo(self.contentView);
        make.height.equalTo(@20);
    }];
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.font = [UIFont systemFontOfSize:16];
        _titleLabel.textColor = UIColorHex(1A1A1A);
    }
    return _titleLabel;
}

@end
