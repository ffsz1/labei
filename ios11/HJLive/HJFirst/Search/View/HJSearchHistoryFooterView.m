//
//  HJSearchHistoryFooterView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSearchHistoryFooterView.h"


@interface HJSearchHistoryFooterView ()

@property (nonatomic, strong) UIButton *clearBtn;

@end


@implementation HJSearchHistoryFooterView

- (instancetype)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.clearBtn];
    }
    return self;
}

- (void)layoutSubviews {
    
    [super layoutSubviews];
    
    __weak typeof(self) weakSelf = self;
    [self.clearBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.center.equalTo(weakSelf);
    }];
}


- (void)clearBtnAction {
    
    if (self.clearBtnActionBlock) {
        self.clearBtnActionBlock();
    }
    
}

- (UIButton *)clearBtn {
    
    if (!_clearBtn) {
        _clearBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        _clearBtn.titleLabel.font = [UIFont systemFontOfSize:14];
        [_clearBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [_clearBtn setTitle:@"  清空历史记录" forState:UIControlStateNormal];
        [_clearBtn setImage:[UIImage imageNamed:@"hj_lajitong"] forState:UIControlStateNormal];
        [_clearBtn addTarget:self action:@selector(clearBtnAction) forControlEvents:UIControlEventTouchUpInside];
    }
    
    return _clearBtn;
}

@end
