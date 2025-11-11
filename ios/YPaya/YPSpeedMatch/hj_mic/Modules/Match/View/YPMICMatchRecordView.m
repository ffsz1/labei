//
//  YPMICMatchRecordView.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMICMatchRecordView.h"
#import "SVGAImageView.h"

#import "YPMICPlayInfoModel.h"

#import "YYWeakProxy.h"

typedef NS_ENUM(NSInteger, HJMICMatchRecordViewAnimationState) {
    HJMICMatchRecordViewAnimationStateStart,
    HJMICMatchRecordViewAnimationStatePause,
    HJMICMatchRecordViewAnimationStateStop,
};

@interface YPMICMatchRecordView ()


@property (nonatomic, strong) SVGAImageView *animationView;
@property (nonatomic, strong) UILabel *countLabel;
@property (nonatomic, strong) UIImageView *musiceLogoView;
@property (nonatomic, strong) UIImageView *imgBG;


@end

@implementation YPMICMatchRecordView

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
    
    [self setupLayer];
}

#pragma mark - Event
- (void)tapAction:(UITapGestureRecognizer *)tap {
    !self.didTapHandler ?: self.didTapHandler(self);
}

#pragma mark - Public methods
- (void)configureWithAudioInfo:(YPMICPlayInfoModel *)audioInfo {
    NSString *playName = nil;
    HJMICMatchRecordViewAnimationState animationState = HJMICMatchRecordViewAnimationStateStop;
    
    switch (audioInfo.playState) {
        case MICPlayStateUndefine:
        {
            playName = @"yp_mic_icon_play";
            self.musiceLogoView.hidden = NO;
        }
            break;
        case MICPlayStateBegin:
        {
            playName = @"yp_mic_icon_pause";
            self.musiceLogoView.hidden = YES;
            animationState = HJMICMatchRecordViewAnimationStateStart;
        }
            break;
        case MICPlayStateProgress:
        {
            playName = @"yp_mic_icon_pause";
            self.musiceLogoView.hidden = YES;
            animationState = HJMICMatchRecordViewAnimationStateStart;
        }
            break;
        case MICPlayStatePause:
        {
            playName = @"yp_mic_icon_play";
            self.musiceLogoView.hidden = NO;
            animationState = HJMICMatchRecordViewAnimationStatePause;
        }
            break;
        case MICPlayStateCancel:
        {
            playName = @"yp_mic_icon_play";
            self.musiceLogoView.hidden = NO;
        }
            break;
        case MICPlayStateFinished:
        {
            playName = @"yp_mic_icon_play";
            self.musiceLogoView.hidden = NO;
        }
            break;
    }
    
    self.playView.image = [UIImage imageNamed:playName];
    NSInteger minutes = audioInfo.duration / 60;
    NSInteger seconds = audioInfo.duration % 60;
    self.countLabel.text = [NSString stringWithFormat:@"%02zd:%02zd", minutes, seconds];
    
    switch (animationState) {
        case HJMICMatchRecordViewAnimationStateStart:
        {
            [self.animationView startAnimation];
        }
            break;
        case HJMICMatchRecordViewAnimationStatePause:
        {
            [self.animationView pauseAnimation];
        }
            break;
        case HJMICMatchRecordViewAnimationStateStop:
        {
             [self.animationView stopAnimation];
            [self.animationView stepToFrame:0 andPlay:NO];
        }
            break;
    }
}

#pragma mark - Private methods
- (void)commonInit {
//    self.backgroundColor = [UIColor colorWithHexString:@"#FF277E"];
//    self.backgroundColor = [UIColor colorWithHexString:@"#60A6FF"];
    
    [self addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction:)]];
}

- (void)setupLayer {
//    self.layer.cornerRadius = self.size.height/2.f;
//    self.layer.masksToBounds = YES;
}

#pragma mark - Layout
- (void)addControls {
      [self addSubview:self.imgBG];
    [self addSubview:self.playView];
//    [self addSubview:self.animationView];
//    [self addSubview:self.musiceLogoView];
//    [self addSubview:self.countLabel];
}

- (void)layoutControls {
    CGSize playSize = CGSizeMake(22, 22);
    
    [self.playView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self);
        make.left.equalTo(self).offset(3);
        make.size.equalTo(@(playSize));
    }];
    
    [self.imgBG mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.bottom.left.right.equalTo(self);
         
      }];
//    [self.countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.centerY.equalTo(self);
//        make.right.equalTo(self).offset(-8);
//    }];
    
//    [self.animationView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.equalTo(self);
//        make.left.equalTo(self.playView.mas_right);
//        make.right.equalTo(self.countLabel.mas_left);
//        make.bottom.equalTo(self).priority(750);
//    }];
    
//    [self.musiceLogoView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.edges.equalTo(self.animationView);
//    }];
    
    [self.countLabel setContentHuggingPriority:UILayoutPriorityDefaultHigh forAxis:UILayoutConstraintAxisHorizontal];
    [self.animationView setContentHuggingPriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
    
    [self.countLabel setContentCompressionResistancePriority:UILayoutPriorityDefaultHigh forAxis:UILayoutConstraintAxisHorizontal];
    [self.animationView setContentCompressionResistancePriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
}

#pragma mark - setters/getters
- (UIImageView *)playView {
    if (!_playView) {
        _playView = [UIImageView new];
        _playView.userInteractionEnabled = YES;
        _playView.image = [UIImage imageNamed:@"yp_mic_icon_play"];
        _playView.hidden = YES;
        _playView.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _playView;
}
- (UIImageView *)imgBG {
    if (!_imgBG) {
        _imgBG = [UIImageView new];
        _imgBG.userInteractionEnabled = YES;
        _imgBG.image = [UIImage imageNamed:@"yp_match_music_imgBG"];
        _imgBG.contentMode = UIViewContentModeScaleAspectFit;
        
        UILabel* label = [[UILabel alloc] init];
        label.frame = CGRectMake(27, 0, 64, 32);
        label.text = @"语音播放";
        
         
        label.textColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img"]];
        
        label.font = [UIFont systemFontOfSize:15];
        [_imgBG addSubview:label];
        UIImageView* flagimg = [[UIImageView alloc] init];
        flagimg.frame = CGRectMake(CGRectGetMaxX(label.frame)+10, 8, 16, 16);
        flagimg.image = [UIImage imageNamed:@"yp_music_flag_icon"];
        [_imgBG addSubview:flagimg];
        
    }
    return _imgBG;
}





- (UIImageView *)musiceLogoView
{
    if (!_musiceLogoView) {
        _musiceLogoView = [UIImageView new];
        _musiceLogoView.image = [UIImage imageNamed:@"pinlv_4"];
    }
    return _musiceLogoView;
}



- (SVGAImageView *)animationView {
    if (!_animationView) {
        _animationView = [SVGAImageView new];
        _animationView.imageName = @"yp_mic_effect_match_record_white";
    }
    return _animationView;
}

- (UILabel *)countLabel {
    if (!_countLabel) {
        _countLabel = [UILabel new];
        _countLabel.text = @"00:00";
        _countLabel.font = [UIFont systemFontOfSize:11];
        _countLabel.textColor = [UIColor colorWithHexString:@"#FFFFFF"];
    }
    return _countLabel;
}

@end
