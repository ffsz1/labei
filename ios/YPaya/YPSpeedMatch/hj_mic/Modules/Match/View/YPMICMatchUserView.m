//
//  YPMICMatchUserView.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMICMatchUserView.h"
#import "YPMICMatchRecordView.h"

#import "YPMICPlayInfoModel.h"
#import "YPMICUserInfo.h"

#import "NSDate+Util.h"

@interface YPMICMatchUserView ()

@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIImageView *experLevelimageView;
@property (nonatomic, strong) UIImageView *charmLevelimageView;
@property (nonatomic, strong) UIImageView *flagImageView;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) YPMICMatchRecordView *recordView;
@property (nonatomic, strong) UIButton *sexView;
@property (nonatomic, strong) UILabel *constellationLabel;
@property (nonatomic, strong) UIButton *attentionView;
@property (nonatomic, strong) UILabel *storyLabel;
@property (nonatomic, strong) UILabel *desLabel;

@property (nonatomic, strong) MASConstraint *nameRightConstraint;
@property (nonatomic, strong) MASConstraint *nameRightToRecordConstraint;

@property (nonatomic, strong) UIView *reportView;
@property (nonatomic, strong) UIImageView *reportLine;
@property (nonatomic, strong) UIButton *reportBtn;
@property (nonatomic, strong) UIButton *blackListBtn;

@property (nonatomic,strong) UILabel *charmLabel;
@property (nonatomic,strong) UILabel *extendLabel;


@end

@implementation YPMICMatchUserView

#pragma mark - Life cycle
- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self commonInit];
        [self addControls];
        [self layoutControls];
        self.backgroundColor = [UIColor whiteColor];
    }
    return self;
}

#pragma mark - Event
- (void)recordAction:(YPMICMatchRecordView*)matchRecordView {
    !self.didTapRecordHandler ?: self.didTapRecordHandler(matchRecordView);
}

- (void)attentionAction:(id)sender {
    !self.didTapAttentionHandler ?: self.didTapAttentionHandler();
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.backgroundColor = [UIColor  colorWithHexString:@"#FFFFFF"];
    self.layer.borderColor = [UIColor colorWithHexString:@"#E5E5E5"].CGColor;
    self.layer.borderWidth = .5f;
    self.layer.cornerRadius = 18.f;
    self.layer.masksToBounds = YES;
}

- (void)confifureWithUserInfo:(YPMICUserInfo *)userInfo {
    
    _userInfo = userInfo;
    
    [self initData];
}

- (void)configureWithAttenion:(BOOL)isAttenion {
//    NSString *attentionName = nil;
//    if (isAttenion) {
//        attentionName = @"yp_mic_icon_attentioned";
//    } else {
//        attentionName = @"yp_mic_icon_attention";
//    }
//    [self.attentionView setImage:[UIImage imageNamed:attentionName] forState:UIControlStateNormal];
//    [self.attentionView setImage:[UIImage imageNamed:attentionName] forState:UIControlStateHighlighted];
}

- (void)configureWithPlayInfo:(YPMICPlayInfoModel *)playInfo {
    BOOL show = (playInfo.voicePath.length && playInfo.duration >= 1);
    self.recordView.hidden = !show;
    [self.recordView configureWithAudioInfo:playInfo];
    if (show) {
        [self.nameRightConstraint deactivate];
        [self.nameRightToRecordConstraint activate];
    } else {
        [self.nameRightConstraint activate];
        [self.nameRightToRecordConstraint deactivate];
    }
    [self setNeedsUpdateConstraints];
}

- (CGFloat)getSexWidth {
    CGFloat width = [@"000岁" widthForFont:self.sexView.titleLabel.font];
    width += (6 * 2 + 10);
    return width;
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.imageView];
    [self addSubview:self.nameLabel];
    [self addSubview:self.charmLevelimageView];
    [self addSubview:self.experLevelimageView];
     [self.charmLevelimageView addSubview:self.charmLabel];
    [self.experLevelimageView addSubview:self.extendLabel];
       [self addSubview:self.flagImageView];
    
    [self addSubview:self.recordView];
    [self addSubview:self.sexView];
    [self addSubview:self.constellationLabel];
    [self addSubview:self.attentionView];
    [self addSubview:self.storyLabel];
    
    [self addSubview:self.reportView];
    
    [self initData];
    
}

- (void)initData
{
    
    if (self.userInfo == nil) {
        return;
    }
    
    NSString *sexName = nil;
    NSString *sexColor = nil;
    NSString *ageText = @"0";
    BOOL shouldAttention = YES;
    
    if (_userInfo.gender == UserInfo_Male) {
        sexName = @"yp_sex_male_logo";
        sexColor = @"#00B7EE";
    } else {
        sexName = @"yp_sex_female_logo";
        sexColor = @"#FF9CC8";
    }
    
    UserID myUserId = [[GetCore(YPAuthCoreHelp) getUid] userIDValue];
    if (_userInfo.uid == myUserId) {
        shouldAttention = NO;
    }
    
    ageText = [NSString stringWithFormat:@" %ld", [NSDate getAge:_userInfo.birth]];
    
    [self.imageView sd_setImageWithURL:[NSURL URLWithString:_userInfo.avatar] placeholderImage:[UIImage imageNamed:default_avatar] completed:nil];
    
    self.nameLabel.text = _userInfo.nick;
    
    [self.sexView setImage:[UIImage imageNamed:sexName] forState:UIControlStateNormal];
//    [self.sexView setTitle:ageText forState:UIControlStateNormal];
    
    self.constellationLabel.text = [NSString stringWithFormat:@"%@",[NSDate calculateConstellationWithMonth:_userInfo.birth]];
    
    self.attentionView.hidden = !shouldAttention;
    if (_userInfo.userDesc != nil ) {
        self.storyLabel.text = _userInfo.userDesc;
    }else{
         self.storyLabel.text = @"这个人很懒，什么都没写...";
    }
   
    if (_userInfo.userDesc.length<1) {
        self.flagImageView.hidden = YES;
    }else{
        self.flagImageView.hidden = NO;
    }
    
    self.charmLabel.text = [NSString stringWithFormat:@"Lv.%ld",_userInfo.charmLevel];
    self.extendLabel.text = [NSString stringWithFormat:@"Lv.%ld",_userInfo.experLevel];
    [self configureWithAttenion:_userInfo.isLike];
}

- (void)layoutControls {
//    CGFloat imageHeightRatio = 292.5 / 382.f;
//    CGSize sexSize = CGSizeMake([self getSexWidth], 20);
     CGSize sexSize = CGSizeMake(12, 12);
    CGSize recordSize = CGSizeMake(130, 32);
    
      CGSize flagSize = CGSizeMake(16, 14);
    
    [self.imageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.right.equalTo(self);
        make.bottom.equalTo(self).offset(-160);
//        make.height.equalTo(self.mas_width).multipliedBy(imageHeightRatio);
    }];
    
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.imageView.mas_bottom).offset(13);
        make.left.equalTo(self).offset(13);
//         make.height.equalTo(@(19));
//        self.nameRightConstraint = make.right.equalTo(self).offset(-17);
//        self.nameRightToRecordConstraint = make.right.equalTo(self.recordView.mas_left).offset(-46).priority(750);
    }];
    
    [self.nameRightConstraint activate];
    [self.nameRightToRecordConstraint deactivate];


    
    [self.sexView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self.nameLabel.mas_centerY);
        make.left.equalTo(self.nameLabel.mas_right).offset(10);
        make.size.equalTo(@(sexSize));
    }];
    
     [self.charmLevelimageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.equalTo(self.nameLabel.mas_centerY);
            make.left.equalTo(self.sexView.mas_right).offset(20);
//            make.size.equalTo(@(charmSize));
        }];
    [self.experLevelimageView mas_makeConstraints:^(MASConstraintMaker *make) {
               make.centerY.equalTo(self.nameLabel.mas_centerY);
               make.left.equalTo(self.charmLevelimageView.mas_right).offset(10);
//               make.size.equalTo(@(charmSize));
           }];
    [self.charmLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                      make.centerY.equalTo(self.nameLabel.mas_centerY);
            make.right.equalTo(self.charmLevelimageView.mas_right).offset(-10);
       //               make.size.equalTo(@(charmSize));
                  }];
     [self.extendLabel
      mas_makeConstraints:^(MASConstraintMaker *make) {
                   make.centerY.equalTo(self.nameLabel.mas_centerY);
         make.right.equalTo(self.experLevelimageView.mas_right).offset(-10);
    //               make.size.equalTo(@(charmSize));
               }];
    
    
    
     [self.flagImageView mas_makeConstraints:^(MASConstraintMaker *make) {
                   make.top.equalTo(self.nameLabel.mas_bottom).offset(15);
                   make.left.equalTo(self.nameLabel.mas_left).offset(0);
                   make.size.equalTo(@(flagSize));
               }];
    
    
//    [self.constellationLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.centerY.equalTo(self.sexView);
//        make.left.equalTo(self.sexView.mas_right).offset(6);
//    }];
    
    [self.attentionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self).offset(-15);
        make.top.equalTo(self.experLevelimageView.mas_bottom).offset(13);
        make.size.mas_equalTo(CGSizeMake(56, 24));
    }];
    
    [self.storyLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.flagImageView.mas_top).offset(0);
        make.left.equalTo(self.flagImageView.mas_right).offset(8);
        make.right.equalTo(self.attentionView.mas_left).offset(-12.5);

    }];
        [self.recordView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.equalTo(self.imageView);
            make.top.mas_equalTo(self.storyLabel.mas_bottom).offset(16);
    //        make.right.equalTo(self).offset(-15);
            make.size.equalTo(@(recordSize));
        }];
//    [self.reportView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.size.mas_equalTo(CGSizeMake(66, 24));
//        make.right.mas_equalTo(self).offset(-8);
//        make.top.mas_equalTo(self).offset(8);
//    }];
//    
//    [self.reportLine mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.size.mas_equalTo(CGSizeMake(1, 11));
//        make.center.mas_equalTo(self.reportView);
//    }];
//    
//    [self.reportBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.top.bottom.mas_equalTo(self.reportView);
//        make.right.mas_equalTo(self.reportLine);
//    }];
//    
//    [self.blackListBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.right.top.bottom.mas_equalTo(self.reportView);
//        make.left.mas_equalTo(self.reportLine);
//    }];
}

- (void)blackListAction
{
    if (self.blackListBlock) {
        self.blackListBlock();
    }
}

- (void)reportAction
{
    if (self.reportBlock) {
        self.reportBlock();
    }
}


#pragma mark - setters/getters
- (UIImageView *)imageView {
    if (!_imageView) {
        _imageView = [UIImageView new];
        _imageView.userInteractionEnabled = YES;
        _imageView.contentMode = UIViewContentModeScaleAspectFill;
        _imageView.layer.masksToBounds = YES;
    }
    return _imageView;
}

//魅力
- (UIImageView *)charmLevelimageView {
    if (!_charmLevelimageView) {
        _charmLevelimageView = [UIImageView new];
        _charmLevelimageView.userInteractionEnabled = YES;
        _charmLevelimageView.image = [UIImage imageNamed:@"yp_meili_charm_icon"];
        _charmLevelimageView.contentMode = UIViewContentModeScaleAspectFill;
        _charmLevelimageView.layer.masksToBounds = YES;
    }
    return _charmLevelimageView;
}
//等级
- (UIImageView *)experLevelimageView {
    if (!_experLevelimageView) {
        _experLevelimageView = [UIImageView new];
        _experLevelimageView.userInteractionEnabled = YES;
         _experLevelimageView.image = [UIImage imageNamed:@"yp_caifu_expert_icon"];
        _experLevelimageView.contentMode = UIViewContentModeScaleAspectFill;
        _experLevelimageView.layer.masksToBounds = YES;
    }
    return _experLevelimageView;
}
- (UIImageView *)flagImageView {
    if (!_flagImageView) {
        _flagImageView = [UIImageView new];
        _flagImageView.userInteractionEnabled = YES;
         _flagImageView.image = [UIImage imageNamed:@"yp_my_hongxin_icon"];
        _flagImageView.contentMode = UIViewContentModeScaleAspectFill;
        
    }
    return _flagImageView;
}

- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.numberOfLines = 1;
        _nameLabel.font = [UIFont fontWithName:@"PingFangSC-Medium" size:18];
        _nameLabel.textColor = [UIColor colorWithHexString:@"#1A1A1A"];
    }
    return _nameLabel;
}

- (YPMICMatchRecordView *)recordView {
    if (!_recordView) {
        _recordView = [YPMICMatchRecordView new];
        _recordView.exclusiveTouch = YES;
        @weakify(self);
        _recordView.didTapHandler = ^(YPMICMatchRecordView *matchRecordView){
            @strongify(self);
            [self recordAction:matchRecordView];
        };
        _recordView.hidden = YES;
    }
    return _recordView;
}

- (UIButton *)sexView {
    if (!_sexView) {
        _sexView = [UIButton new];
        _sexView.userInteractionEnabled = NO;
        [_sexView setImage:[UIImage imageNamed:@"yp_mic_icon_male"] forState:UIControlStateNormal];
        _sexView.backgroundColor = [UIColor clearColor];
        [_sexView setTitle:@"" forState:UIControlStateNormal];
        [_sexView setTitleColor:[UIColor colorWithHexString:@"#B2B2B2"] forState:UIControlStateNormal];
        _sexView.titleLabel.font = [UIFont fontWithName:@"PingFangSC-Light" size:14];
        _sexView.layer.masksToBounds = YES;
        _sexView.layer.cornerRadius = 3.f;
    }
    return _sexView;
}

- (UILabel *)constellationLabel {
    if (!_constellationLabel) {
        _constellationLabel = [UILabel new];
        _constellationLabel.font = [UIFont systemFontOfSize:15];
        _constellationLabel.textColor = [UIColor colorWithHexString:@"#B2B2B2"];
    }
    return _constellationLabel;
}

- (UIButton *)attentionView {
    if (!_attentionView) {
        _attentionView = [UIButton new];
        [_attentionView setBackgroundImage:[UIImage imageNamed:@"yp_match_zhuye"] forState:UIControlStateNormal];
        [_attentionView addTarget:self action:@selector(attentionAction:) forControlEvents:UIControlEventTouchUpInside];
        [_attentionView setTitle:@"主页" forState:UIControlStateNormal];
        [_attentionView setTitleColor:[UIColor colorWithHexString:@"#DB7EFF"] forState:UIControlStateNormal];
        _attentionView.titleLabel.font = [UIFont systemFontOfSize:14];
        _attentionView.exclusiveTouch = YES;
//        _attentionView.backgroundColor = [UIColor colorWithHexString:@"#FF0066"];
//        _attentionView.layer.masksToBounds = YES;
//        _attentionView.layer.cornerRadius = 29.f/2;
    }
    return _attentionView;
}

- (UILabel *)storyLabel {
    if (!_storyLabel) {
        _storyLabel = [UILabel new];
        _storyLabel.numberOfLines = 1;
        _storyLabel.font = [UIFont fontWithName:@"PingFangSC-Light" size:14];
        _storyLabel.textColor = [UIColor colorWithHexString:@"#333333"];
//        _storyLabel.backgroundColor = [UIColor redColor];
    }
    return _storyLabel;
}

- (UILabel *)desLabel
{
    if (!_desLabel) {
        _desLabel = [UILabel new];
        _desLabel.numberOfLines = 1;
        _desLabel.font = [UIFont fontWithName:@"PingFangSC-Light" size:13];
        _desLabel.textColor = [UIColor colorWithHexString:@"#B2B2B2"];
    }
    return _desLabel;
}

- (UIView *)reportView
{
    if (!_reportView) {
        _reportView = [UIView new];
//        _reportView.layer.cornerRadius = 12;
//        _reportView.layer.masksToBounds = YES;
//        _reportView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.35];
    }
    return _reportView;
}

- (UIImageView *)reportLine
{
    if (!_reportLine) {
        _reportLine = [[UIImageView alloc] init];
        _reportLine.backgroundColor = [UIColor whiteColor];
        _reportLine.alpha = 0.35;
        [self.reportView addSubview:self.reportLine];
    }
    return _reportLine;
}

- (UIButton *)reportBtn
{
    if (!_reportBtn) {
        _reportBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_reportBtn setTitle:@"举报" forState:UIControlStateNormal];
        [_reportBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _reportBtn.titleLabel.font = [UIFont fontWithName:@"PingFangSC-Light" size:10];
//        _reportBtn.alpha = 0.35;
        [_reportBtn addTarget:self action:@selector(blackListAction) forControlEvents:UIControlEventTouchUpInside];

        [self.reportView addSubview:_reportBtn];
    }
    return _reportBtn;
}

- (UIButton *)blackListBtn
{
    if (!_blackListBtn) {
        _blackListBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_blackListBtn setTitle:@"拉黑" forState:UIControlStateNormal];
        [_blackListBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _blackListBtn.titleLabel.font = [UIFont fontWithName:@"PingFangSC-Light" size:10];
//        _blackListBtn.alpha = 0.35;
        [_blackListBtn addTarget:self action:@selector(reportAction) forControlEvents:UIControlEventTouchUpInside];
        
        [self.reportView addSubview:_blackListBtn];
    }
    return _blackListBtn;
}

-(UILabel*)charmLabel{
    if (!_charmLabel) {
        _charmLabel = [[UILabel alloc] init];
        _charmLabel.font = [UIFont boldSystemFontOfSize:13];
        _charmLabel.textColor = [UIColor whiteColor];
        
    }
    return _charmLabel;
}
-(UILabel*)extendLabel{
    if (!_extendLabel) {
        _extendLabel = [[UILabel alloc] init];
        _extendLabel.font = [UIFont boldSystemFontOfSize:13];
        _extendLabel.textColor = [UIColor whiteColor];
        
    }
    return _extendLabel;
}

@end
