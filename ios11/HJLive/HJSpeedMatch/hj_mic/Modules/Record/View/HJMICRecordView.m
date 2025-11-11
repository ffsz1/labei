//
//  HJMICRecordView.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMICRecordView.h"
#import "HJMICRecordInfoView.h"

#import "HJMICRecordInfoModel.h"

@interface HJMICRecordView ()

@property (nonatomic, strong) UIImageView *backgroudView;
@property (nonatomic, strong) UIButton *changeButton;
@property (nonatomic, strong) UIButton *doneButton;
@property (nonatomic, strong) UILabel *textLabel;
@property (nonatomic, strong) HJMICRecordInfoView *recordInfoView;
@property (nonatomic, strong) UIButton *recordButton;
@property (nonatomic, strong) UIButton *deleteButton;
@property (nonatomic, strong) UILabel *deleteLabel;
@property (nonatomic, strong) UIButton *playButton;
@property (nonatomic, strong) UILabel *playLabel;

@property (nonatomic, strong) UILabel *countLabel;
@property (nonatomic, strong) UILabel *countLabelTip;
@end

@implementation HJMICRecordView

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
- (void)tapAction:(UITapGestureRecognizer *)tap {
    
}

- (void)changeAction:(UIButton *)sender {
    !self.didTapChangeHandler ?: self.didTapChangeHandler();
}

- (void)doneAction:(UIButton *)sender {
    !self.didTapDoneHandler ?: self.didTapDoneHandler();
}

- (void)deleteAction:(UIButton *)sender {
    !self.didTapDeletedHandler ?: self.didTapDeletedHandler();
     
}

//- (void)recordTouchDownAction:(UIButton *)sender {
//
//    _recordLabel.text = @"录制中";
//
//    !self.didTouchRecordHandler ?: self.didTouchRecordHandler(UIControlEventTouchDown);
//}

- (void)recordTouchUpInsideAction:(UIButton *)sender {
    
    self.recordButton.userInteractionEnabled = NO;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.recordButton.userInteractionEnabled = YES;
    });
    
    if (self.recordButton.isSelected == YES) {
        
        if (self.saveBlock) {
            self.saveBlock();
        }
                
    }else{
        if ([_recordLabel.text isEqualToString:@"正在录制"]) {
            !self.didTouchRecordHandler ?: self.didTouchRecordHandler(YES);
        }else{
            _recordLabel.text = @"正在录制";
             [_recordButton setBackgroundImage:[UIImage imageNamed:@"hj_music_luyin_bg"] forState:UIControlStateNormal];
            [_recordButton setImage:nil forState:UIControlStateNormal];
            [_recordButton setTitle:@"停止" forState:UIControlStateNormal];
            !self.didTouchRecordHandler ?: self.didTouchRecordHandler(NO);
        }
    }
    
    
    

}

//- (void)recordTouchUpOutsideAction:(UIButton *)sender {
//    !self.didTouchRecordHandler ?: self.didTouchRecordHandler(UIControlEventTouchUpOutside);
//}

- (void)playAction:(UIButton *)sender {
    !self.didTapPlayHandler ?: self.didTapPlayHandler();
}

- (void)reset
{
    self.saveState = NO;
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    [self addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction:)]];
    self.exclusiveTouch = YES;
}

- (void)configureWithRecordInfo:(HJMICRecordInfoModel *)recordInfo {
    BOOL showPlay = NO;
    BOOL animated = NO;
    BOOL shouldUpload = NO;
    NSString *playImage = nil;
    HJMICRecordInfoViewAnimationState animationState = HJMICRecordInfoViewAnimationStateStop;
    
    switch (recordInfo.state) {
        case MICRecordStateUndefine:
        {
            playImage = @"hj_match_shiting";
            animationState = HJMICRecordInfoViewAnimationStateStop;
        }
             break;
        case MICRecordStateBegin:
        {
            playImage = @"hj_match_shiting";
            animationState = HJMICRecordInfoViewAnimationStateStart;
        }
            break;
        case MICRecordStateProgress:
        {
            playImage = @"hj_match_shiting";
            animationState = HJMICRecordInfoViewAnimationStateStart;
        }
            break;
        case MICRecordStateFinished:
        {
            playImage = @"hj_match_shiting";
            showPlay = YES;
            animationState = HJMICRecordInfoViewAnimationStateStop;
        }
            break;
        case MICRecordStateCancel:
        {
            playImage = @"hj_match_shiting";
            animationState = HJMICRecordInfoViewAnimationStateStop;
        }
            break;
        case MICRecordStatePlaying:
        {
            playImage = @"hj_match_shiting_ing";
            showPlay = YES;
            animationState = HJMICRecordInfoViewAnimationStateStart;
        }
            break;
    }
    
    if (self.recordInfo.localPath.length && self.recordInfo.filePath.length) {
        shouldUpload |= YES;
    } else if (!self.recordInfo.localPath.length && self.recordInfo.filePath.length) {
        shouldUpload = NO;
    } else if (self.recordInfo.localPath.length && !self.recordInfo.filePath.length) {
        if (self.recordInfo.state == MICRecordStateFinished) shouldUpload |= YES;
        if (self.recordInfo.state == MICRecordStatePlaying) shouldUpload |= YES;
    } else {
        shouldUpload = NO;
    }
    
    self.doneButton.enabled = shouldUpload;
    self.deleteButton.hidden = !showPlay;
    self.deleteLabel.hidden = !showPlay;
    self.playButton.hidden = !showPlay;
    self.playLabel.hidden = !showPlay;
    [self.playButton setImage:[UIImage imageNamed:playImage] forState:UIControlStateNormal];
    self.recordInfoView.animationState = animationState;
    [self configRecordTime:recordInfo.time];
}

#pragma mark - setters/getters
- (void)configRecordTime:(NSTimeInterval)recordTime {
//    NSInteger minutes = (NSInteger)recordTime / 60;
    NSInteger seconds = (NSInteger)recordTime % 60;
    self.countLabel.text = [NSString stringWithFormat:@"00:%02zd", seconds];
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.backgroudView];
    [self addSubview:self.changeButton];
    [self addSubview:self.doneButton];
    [self addSubview:self.textLabel];
    [self addSubview:self.recordInfoView];
    [self addSubview:self.recordButton];
    [self addSubview:self.deleteButton];
    [self addSubview:self.playButton];
    [self addSubview:self.recordLabel];
    [self addSubview:self.deleteLabel];
    [self addSubview:self.playLabel];
    [self addSubview:self.countLabel];
    [self addSubview:self.countLabelTip];
}

- (void)setSaveState:(BOOL)saveState {
    _saveState = saveState;
    self.recordButton.selected = saveState;
    
    if (saveState) {
        _recordLabel.text = @"已完成录制";
         [_recordButton setTitle:@"上传" forState:UIControlStateNormal];
    }else{
        _recordLabel.text = @"点击开始录制";
        [_recordButton setImage:[UIImage imageNamed:@"hj_music_luyin_icon"] forState:UIControlStateNormal];
           [_recordButton setTitle:@"" forState:UIControlStateNormal];
        self.countLabel.text = @"最多60S";
    }
    

}

- (void)layoutControls {
    CGSize recordInfoSize = CGSizeMake(203, 40);
    CGSize playSize = CGSizeMake(109, 109);
    CGSize deleteSize = CGSizeMake(46, 46);
    
    [self.backgroudView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.equalTo(self);
        make.bottom.equalTo(self).priority(750);
    }];
    
    [self.changeButton mas_makeConstraints:^(MASConstraintMaker *make) {
          make.right.equalTo(self).offset(-60);
              make.top.equalTo(self).offset(iPhoneX?300:265);
        make.width.mas_equalTo(66);
        make.height.mas_equalTo(30);
    }];
    
    [self.doneButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self).offset(-19);
        make.centerY.equalTo(self.changeButton);
    }];
    
    [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
               make.top.equalTo(self).offset(iPhoneX?140:120);
                make.centerX.equalTo(self);
    }];
    
    [self.recordInfoView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self);
        make.size.equalTo(@(recordInfoSize));
        make.top.equalTo(self).offset(210);
    }];
    
    [self.recordLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self);
        make.bottom.equalTo(self).offset(-60);
    }];
    
    [self.recordButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self);
        make.bottom.equalTo(self.recordLabel.mas_top).offset(-20);
        make.size.equalTo(@(playSize));
    }];
    
    [self.deleteButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(deleteSize));
        make.centerY.equalTo(self.recordButton);
        make.left.equalTo(self).offset(54);
    }];
    
    [self.deleteLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(deleteSize));
        make.top.equalTo(self.deleteButton.mas_bottom).offset(8);
        make.centerX.equalTo(self.deleteButton);
    }];
    
    [self.playButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(deleteSize));
        make.centerY.equalTo(self.recordButton);
        make.right.equalTo(self).offset(-54);
    }];
    
    [self.playLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(deleteSize));
        make.top.equalTo(self.playButton.mas_bottom).offset(8);
        make.centerX.equalTo(self.playButton);
    }];
    
    [self.countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self);
        make.height.equalTo(@16);
        make.width.equalTo(@(kScreenWidth));
        make.top.equalTo(self.recordLabel.mas_bottom).offset(8);
    }];
    
    [self.countLabelTip mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self);
        make.height.equalTo(@20);
        make.width.equalTo(@(kScreenWidth));
        make.top.equalTo(self.countLabel.mas_bottom).offset(10);
    }];
}

#pragma mark - setters/getters
- (void)setText:(NSString *)text {
    _text = text;
    
    self.textLabel.text = text;
}

- (void)setRecordInfo:(HJMICRecordInfoModel *)recordInfo {
    _recordInfo = recordInfo;
    
    [self configureWithRecordInfo:recordInfo];
}

- (UILabel *)countLabel {
    if (!_countLabel) {
        _countLabel = [UILabel new];
        _countLabel.font = [UIFont systemFontOfSize:12];
        _countLabel.textColor = [UIColor colorWithHexString:@"#cccccc"];
        _countLabel.textAlignment = NSTextAlignmentCenter;
        _countLabel.text = @"最多60S";
    }
    return _countLabel;
}

- (UILabel *)countLabelTip {
    if (!_countLabelTip) {
        _countLabelTip = [UILabel new];
        _countLabelTip.font = [UIFont systemFontOfSize:16];
        _countLabelTip.textColor = [UIColor whiteColor];
        _countLabelTip.alpha = 0.6;
        _countLabelTip.textAlignment = NSTextAlignmentCenter;
        _countLabelTip.text = @"最多60S";
    }
    return _countLabelTip;
}

- (UIButton *)changeButton {
    if (!_changeButton) {
        _changeButton = [UIButton new];
        _changeButton.hidden = NO;
        [_changeButton setTitle:@"换一换" forState:UIControlStateNormal];
        [_changeButton setImage:[UIImage imageNamed:@"hj_music_change"] forState:UIControlStateNormal];
        [_changeButton setBackgroundColor:[UIColor colorWithWhite:1 alpha:0.16]];
        _changeButton.layer.cornerRadius = 15;
        _changeButton.layer.masksToBounds = YES;
        
        [_changeButton setTitleColor:[UIColor colorWithHexString:@"#FFFFFF"] forState:UIControlStateNormal];
        _changeButton.titleLabel.font = [UIFont systemFontOfSize:11];
        
        [_changeButton setImageEdgeInsets:UIEdgeInsetsMake(0, 0, 0, 6)];
        
        [_changeButton addTarget:self action:@selector(changeAction:) forControlEvents:UIControlEventTouchUpInside];
        _changeButton.exclusiveTouch = YES;
    }
    return _changeButton;
}

- (UIButton *)doneButton {
    if (!_doneButton) {
        _doneButton = [UIButton new];
        _doneButton.hidden = YES;
        [_doneButton setTitle:@"上传" forState:UIControlStateNormal];
        [_doneButton setTitleColor:[UIColor colorWithHexString:@"#23CFB1"] forState:UIControlStateNormal];
        [_doneButton setTitleColor:[UIColor colorWithHexString:@"#999999"] forState:UIControlStateDisabled];
        _doneButton.titleLabel.font = [UIFont boldSystemFontOfSize:15];
        [_doneButton addTarget:self action:@selector(doneAction:) forControlEvents:UIControlEventTouchUpInside];
        _doneButton.exclusiveTouch = YES;
        _doneButton.enabled = NO;
    }
    return _doneButton;
}

- (UILabel *)textLabel {
    if (!_textLabel) {
        _textLabel = [UILabel new];
        _textLabel.textColor = [UIColor colorWithHexString:@"#FFFFFF"];
        _textLabel.font = [UIFont systemFontOfSize:15];
        _textLabel.textAlignment = NSTextAlignmentCenter;
        _textLabel.numberOfLines = 0;
    }
    return _textLabel;
}

- (HJMICRecordInfoView *)recordInfoView {
    if (!_recordInfoView) {
        _recordInfoView = [HJMICRecordInfoView new];
        _recordInfoView.hidden = YES;
    }
    return _recordInfoView;
}

- (UIButton *)recordButton {
    if (!_recordButton) {
        _recordButton = [UIButton new];
//        [_recordButton setImage:[UIImage imageNamed:@"hj_match_luyin"] forState:UIControlStateNormal];
//        [_recordButton setImage:[UIImage imageNamed:@"hj_match_baochun"] forState:UIControlStateSelected];
         [_recordButton setImage:[UIImage imageNamed:@"hj_music_luyin_icon"] forState:UIControlStateNormal];
         [_recordButton setBackgroundImage:[UIImage imageNamed:@"hj_music_luyin_bg"] forState:UIControlStateNormal];

        _recordButton.userInteractionEnabled = YES;
        _recordButton.exclusiveTouch = YES;
//        [_recordButton addTarget:self action:@selector(recordTouchDownAction:) forControlEvents:UIControlEventTouchDown];
        [_recordButton addTarget:self action:@selector(recordTouchUpInsideAction:) forControlEvents:UIControlEventTouchUpInside];
//        [_recordButton addTarget:self action:@selector(recordTouchUpOutsideAction:) forControlEvents:UIControlEventTouchUpOutside];
    }
    return _recordButton;
}

- (UIButton *)deleteButton {
    if (!_deleteButton) {
        _deleteButton = [UIButton new];
        [_deleteButton setImage:[UIImage imageNamed:@"hj_match_chonglu"] forState:UIControlStateNormal];
        [_deleteButton addTarget:self action:@selector(deleteAction:) forControlEvents:UIControlEventTouchUpInside];
        _deleteButton.hidden = YES;
        _deleteButton.exclusiveTouch = YES;
    }
    return _deleteButton;
}

- (UIButton *)playButton {
    if (!_playButton) {
        _playButton = [UIButton new];
        [_playButton setImage:[UIImage imageNamed:@"hj_match_shiting_ing"] forState:UIControlStateNormal];
        [_playButton addTarget:self action:@selector(playAction:) forControlEvents:UIControlEventTouchUpInside];
        _playButton.hidden = YES;
        _playButton.exclusiveTouch = YES;
    }
    return _playButton;
}

- (UILabel *)recordLabel {
    if (!_recordLabel) {
        _recordLabel = [UILabel new];
        _recordLabel.text = @"点击开始录制";
        _recordLabel.textAlignment = NSTextAlignmentCenter;
        _recordLabel.textColor = [UIColor colorWithHexString:@"#B2B2B2"];
        _recordLabel.font = [UIFont systemFontOfSize:13];
    }
    return _recordLabel;
}

- (UILabel *)deleteLabel {
    if (!_deleteLabel) {
        _deleteLabel = [UILabel new];
        _deleteLabel.text = @"重录";
        _deleteLabel.textColor = [UIColor colorWithHexString:@"#1A1A1A"];
        _deleteLabel.font = [UIFont systemFontOfSize:13];
        _deleteLabel.textAlignment = NSTextAlignmentCenter;
        _deleteLabel.hidden = YES;
    }
    return _deleteLabel;
}

- (UILabel *)playLabel {
    if (!_playLabel) {
        _playLabel = [UILabel new];
        _playLabel.text = @"试听";
        _playLabel.textColor = [UIColor colorWithHexString:@"#1A1A1A"];
        _playLabel.font = [UIFont systemFontOfSize:13];
        _playLabel.textAlignment = NSTextAlignmentCenter;
        _playLabel.hidden = YES;
    }
    return _playLabel;
}

- (UIImageView *)backgroudView {
    if (!_backgroudView) {
        _backgroudView = [UIImageView new];
    }
    return _backgroudView;
}

@end
