//
//  HJMICMatchChosenView.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMICMatchChosenView.h"

@interface HJMICMatchChosenView ()

@property (nonatomic, strong) UIImageView *backgroundView;
@property (nonatomic, strong) UIImageView *centerView;
@property (nonatomic, strong) UILabel *countLabel;

@property (nonatomic, assign) XCMICMatchChosenType chosenType;

@end

@implementation HJMICMatchChosenView

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

- (instancetype)initWithChosenType:(XCMICMatchChosenType)chosenType {
    self = [self initWithFrame:CGRectZero];
    if (self) {
        _chosenType = chosenType;
        [self configureWithType:chosenType connectionInfo:nil];
    }
    return self;
}

#pragma mark - Event
- (void)tapAction:(UITapGestureRecognizer *)tap {
    !self.didTapHandler ?: self.didTapHandler();
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.chosenType = XCMICMatchChosenTypeDislike;
    self.exclusiveTouch = YES;
    [self addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction:)]];
}

- (void)configureWithType:(XCMICMatchChosenType)type connectionInfo:(id)connectionInfo {
    NSString *backgroundName = nil;
    BOOL showCenter = NO;
    bool showCount = NO;
    
    if (type == XCMICMatchChosenTypeDislike) {
        backgroundName = @"hj_match_buxihuan";
    } else {
        backgroundName = @"hj_match_xihuan";
        showCenter = YES;
    }
    
    self.countLabel.hidden = !showCount;
    self.centerView.hidden = !showCenter;
    self.backgroundView.image = [UIImage imageNamed:backgroundName];
}

- (void)confongureCountWithTotal:(NSInteger)total residue:(NSInteger)residue {
    NSMutableAttributedString *attributeString = [[NSMutableAttributedString alloc] init];
    NSAttributedString *countString = [[NSAttributedString alloc] initWithString:[NSString stringWithFormat:@"%ld", residue] attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:21],NSForegroundColorAttributeName:[UIColor colorWithHexString:@"#FF4848"]}];
    NSAttributedString *suffix = [[NSAttributedString alloc] initWithString:@"\n恋接" attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12],NSForegroundColorAttributeName:[UIColor colorWithHexString:@"#FF4848"]}];
    [attributeString appendAttributedString:countString];
    [attributeString appendAttributedString:suffix];
    
    self.countLabel.attributedText = attributeString;
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.backgroundView];
    [self addSubview:self.centerView];
    [self addSubview:self.countLabel];
}

- (void)layoutControls {
    [self.backgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.bottom.equalTo(self);
    }];
    
    [self.centerView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self);
    }];
    
    [self.countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self);
    }];
}

#pragma mark - setters/getters
- (UIImageView *)backgroundView {
    if (!_backgroundView) {
        _backgroundView = [UIImageView new];
        _backgroundView.image = [UIImage imageNamed:@"hj_match_buxihuan"];
        _backgroundView.userInteractionEnabled = YES;
    }
    return _backgroundView;
}

- (UIImageView *)centerView {
    if (!_centerView) {
        _centerView = [UIImageView new];
        _centerView.image = [UIImage imageNamed:@"hj_mic_icon_heart"];
        _centerView.userInteractionEnabled = YES;
        _centerView.hidden = YES;
    }
    return _centerView;
}

- (UILabel *)countLabel {
    if (!_countLabel) {
        _countLabel = [UILabel new];
        _countLabel.hidden = YES;
        _countLabel.numberOfLines = 0;
    }
    return _countLabel;
}

@end
