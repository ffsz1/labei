//
//  YPSearchHistoryHeaderView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSearchHistoryHeaderView.h"


@interface YPSearchHistoryHeaderView ()

@property (nonatomic, strong) UILabel *titleLabel;

@end

@implementation YPSearchHistoryHeaderView

- (instancetype)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        
        self.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.titleLabel];
    }
    
    return self;
}

- (void)layoutSubviews {
    
    [super layoutSubviews];
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.trailing.bottom.mas_equalTo(0);
        make.leading.mas_equalTo(15);
    }];
}


- (UILabel *)titleLabel {
    
    if (!_titleLabel) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.textColor = UIColorHex(999999);
        _titleLabel.font = [UIFont boldSystemFontOfSize:15];
        _titleLabel.text = NSLocalizedString(XCSearchHistoryTitle, nil);
    }
    return _titleLabel;
}

@end
