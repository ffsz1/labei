//
//  HJMICRecordInfoView.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMICRecordInfoView.h"
#import "SVGAImageView.h"

@interface HJMICRecordInfoView ()

@property (nonatomic, strong) SVGAImageView *animationView;
@property (nonatomic, strong) UILabel *countLabel;

@end

@implementation HJMICRecordInfoView

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

- (void)layoutSubviews {
    [super layoutSubviews];
    
    [self configureRadius];
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.backgroundColor = [UIColor colorWithHexString:@"#F4FFF9"];
    self.layer.borderWidth = .5f;
    self.layer.borderColor = [UIColor colorWithHexString:@"#23CFB1"].CGColor;
}

- (void)configureRadius {
    self.layer.cornerRadius = self.bounds.size.height/2;
    self.layer.masksToBounds = YES;
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.animationView];
    [self addSubview:self.countLabel];
}

- (void)layoutControls {
    [self.animationView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self).offset(20);
        make.top.equalTo(self);
        make.bottom.equalTo(self).priority(750);
        make.right.equalTo(self.countLabel.mas_left).offset(-5);
    }];
    
    [self.countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self).offset(-20);
        make.centerY.equalTo(self);
    }];
    
    [self.animationView setContentCompressionResistancePriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
    [self.countLabel setContentCompressionResistancePriority:UILayoutPriorityDefaultHigh forAxis:UILayoutConstraintAxisHorizontal];
    
    [self.animationView setContentHuggingPriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
    [self.countLabel setContentHuggingPriority:UILayoutPriorityDefaultHigh forAxis:UILayoutConstraintAxisHorizontal];
}

#pragma mark - setters/getters
- (void)setRecordTime:(NSTimeInterval)recordTime {
    _recordTime = recordTime;
    NSInteger minutes = (NSInteger)recordTime / 60;
    NSInteger seconds = (NSInteger)recordTime % 60;
    _countLabel.text = [NSString stringWithFormat:@"%02zd:%02zd", minutes, seconds];
}

- (void)setAnimationState:(HJMICRecordInfoViewAnimationState)animationState {
    _animationState = animationState;
    switch (animationState) {
        case HJMICRecordInfoViewAnimationStateStart:
        {
            [self.animationView startAnimation];
        }
            break;
        case HJMICRecordInfoViewAnimationStatePause:
        {
            [self.animationView pauseAnimation];
        }
            break;
        case HJMICRecordInfoViewAnimationStateStop:
        {
            [self.animationView stopAnimation];
            [self.animationView stepToFrame:0 andPlay:NO];
        }
            break;
    }
}

- (UILabel *)countLabel {
    if (!_countLabel) {
        _countLabel = [UILabel new];
        _countLabel.font = [UIFont systemFontOfSize:13];
        _countLabel.textColor = [UIColor colorWithHexString:@"#23CFB1"];
        _countLabel.textAlignment = NSTextAlignmentCenter;
        _countLabel.text = @"00:00";
    }
    return _countLabel;
}

- (SVGAImageView *)animationView {
    if (!_animationView) {
        _animationView = [SVGAImageView new];
        _animationView.imageName = @"hj_mic_effect_match_record_green";
    }
    return _animationView;
}

@end
