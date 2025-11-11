//
//  YPMICMatchVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMICMatchVC.h"
#import "YPMICMatchUserVC.h"
#import "YPMICRecordVC.h"

#import "SVGAImageView.h"

#import "YPMICUserInfo.h"

#import <POP/POP.h>
#import "YPMICCore.h"
#import "HJMICCoreClient.h"
#import "NSString+YPNIMKit.h"
#import "SVGAPlayer+XCExtension.h"
#import "YPUserCoreHelp.h"
#import "UIButton+WebCache.h"
#import "YPYYViewControllerCenter.h"
#import "YYWeakProxy.h"

@interface YPMICMatchVC () <HJMICCoreClient>

@property (nonatomic, strong) UIButton *myAvaterImageView;
@property (nonatomic, strong) UIImageView *leftMatchUserView;
@property (nonatomic, strong) UIImageView *rightMatchUserView;
@property (nonatomic, strong) UIImageView *bottomMatchUserView;
@property (nonatomic, strong) UIImageView *recordTipsView;
@property (nonatomic, strong) UIImageView *matchTipsView;
@property (nonatomic, strong) SVGAImageView *animationView;
@property (nonatomic, strong) UILabel *matchTitleLabel;
@property (nonatomic, strong) UILabel *matchDetailsLabel;

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, strong) CADisplayLink *springLink;
@property (nonatomic, assign) BOOL springAnimated;

@property (nonatomic, strong) NSMutableArray<YPMICUserInfo *> *users;
@property (nonatomic, assign) BOOL prepareForSkip;
@property (nonatomic, assign) BOOL isFirstMatch;

@end

@implementation YPMICMatchVC

#pragma mark - Life cycle
- (void)dealloc {
    if (_springLink) {
        [_springLink invalidate];
        _springLink = nil;
    }
    
    if (_timer) {
        [_timer invalidate];
        _timer = nil;
    }
    
    [self removeCores];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self commonInit];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setupNaivigation];
    [self addControls];
    [self layoutControls];
    
    [GetCore(YPMICCore) getLinkPool];
    
    
    [self addCores];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:animated];
    [self applyLinkAnimation:YES];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [self applyLinkAnimation:NO];
}

#pragma mark - <MICCoreClient>
- (void)getLinkPoolListSuccessWithList:(NSArray *)list {
    [self.users removeAllObjects];
    [self.users addObjectsFromArray:list];
    [self updateControls];
}

- (void)getLinkPoolListFailthWithMessage:(NSString *)message {
    
}

#pragma mark - Event
- (void)onRightButtonItemClicked {
    [self skipToRecord];
}

- (void)myAvaterImageViewTapAction:(UIButton *)sender {
    if (self.prepareForSkip) return;
    
    NSTimeInterval delay = 0;
    if (!self.isFirstMatch) {
        [self.animationView startAnimation];
        [self removeSpringAnimation];
        [self startUsersAnimation];
        self.isFirstMatch = YES;
        delay = 3;
    }

    @weakify(self);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delay * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        @strongify(self);
        self.prepareForSkip = NO;
        [self skipToMatchUser];
    });
}

- (void)skipToMatchUser {
    if (![[YPYYViewControllerCenter currentViewController] isKindOfClass:[YPMICMatchVC class]]) return;
    
    YPMICMatchUserVC *vc = [YPMICMatchUserVC new];
    vc.users = [NSMutableArray arrayWithArray:self.users];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)skipToRecord {
    YPMICRecordVC *MICRecordViewController = [YPMICRecordVC new];
    [self.navigationController presentViewController:MICRecordViewController animated:YES completion:nil];
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.prepareForSkip = NO;
    self.isFirstMatch = NO;
}

- (UIImageView *)getMatchUserView {
    UIImageView *userView = [UIImageView new];
    userView.layer.masksToBounds = YES;
    userView.layer.cornerRadius = 45.f/2;
    userView.layer.borderWidth = 3.f;
    userView.layer.borderColor = [UIColor colorWithHexString:@"#ffffff"].CGColor;
    userView.backgroundColor = [UIColor greenColor];
    return userView;
}

- (void)setupNaivigation {
    self.title = @"聆听你的心声";
    UIBarButtonItem *rightButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"上传录音" style:UIBarButtonItemStylePlain target:self action:@selector(onRightButtonItemClicked)];
    rightButtonItem.tintColor = [UIColor colorWithHexString:@"#1a1a1a"];
    self.navigationItem.rightBarButtonItem = rightButtonItem;
}

NS_INLINE CGPoint getCircleCoordinate(CGFloat angle, CGFloat radius) {
    CGFloat x = radius*cosf(angle*M_PI/180);
    CGFloat y = radius*sinf(angle*M_PI/180);
    return CGPointMake(x, y);
}

- (void)updateMyAvater {
    UserID uid = [[GetCore(YPAuthCoreHelp) getUid] userIDValue];
    UserInfo *userInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:uid];
    if (userInfo.avatar.length) {
        [self.myAvaterImageView sd_setImageWithURL:[NSURL URLWithString:[self getAdjustImagePath:userInfo.avatar]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:default_avatar]];
    }
}

- (void)updateUsers {
    if (!self.users.count) return;
    YPMICUserInfo *leftUser = nil;
    YPMICUserInfo *rightUser = nil;
    YPMICUserInfo *bottomUser = nil;
    BOOL shouldChange = NO;
    
    if (self.users.count == 1) {
        leftUser = [self.users objectAtIndex:0];
    } else if (self.users.count == 2) {
        leftUser = [self.users objectAtIndex:0];
        rightUser = [self.users objectAtIndex:1];
    } else {
        leftUser = [self.users objectAtIndex:0];
        rightUser = [self.users objectAtIndex:1];
        bottomUser = [self.users objectAtIndex:2];
        shouldChange = YES;
    }

    [self.leftMatchUserView sd_setImageWithURL:[NSURL URLWithString:[self getAdjustImagePath:leftUser.avatar]] placeholderImage:[UIImage imageNamed:default_avatar]];
    [self.rightMatchUserView sd_setImageWithURL:[NSURL URLWithString:[self getAdjustImagePath:rightUser.avatar]] placeholderImage:[UIImage imageNamed:default_avatar]];
    [self.bottomMatchUserView sd_setImageWithURL:[NSURL URLWithString:[self getAdjustImagePath:bottomUser.avatar]] placeholderImage:[UIImage imageNamed:default_avatar]];
    
    if (shouldChange) {
        NSMutableArray *buffer = @[].mutableCopy;
        [buffer addObject:leftUser];
        [buffer addObject:rightUser];
        [buffer addObject:bottomUser];
        
        [self.users removeObjectAtIndex:0];
        [self.users removeObjectAtIndex:0];
        [self.users removeObjectAtIndex:0];
        
        [self.users appendObjects:buffer];
    }
}

- (void)startUsersAnimation {
    self.timer = [NSTimer timerWithTimeInterval:2.6 target:[YYWeakProxy proxyWithTarget:self] selector:@selector(updateUsersAnimation) userInfo:nil repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:self.timer forMode:NSRunLoopCommonModes];
    [self.timer fire];
}

- (void)updateUsersAnimation {
    @weakify(self);
    [self resetUsersAnimation:NO completion:^(BOOL finished) {
        @strongify(self);
        [self updateUsers];
        [self applyUsersAnimation];
    }];
}

- (void)showSpringAnimation {
    self.springLink = [CADisplayLink displayLinkWithTarget:[YYWeakProxy proxyWithTarget:self] selector:@selector(applySpringAnimation)];
    [self.springLink addToRunLoop:[NSRunLoop mainRunLoop] forMode:NSRunLoopCommonModes];
}

- (void)removeSpringAnimation {
    self.matchTipsView.hidden = YES;
    [self.springLink invalidate];
    _springLink = nil;
}

- (void)applySpringAnimation {
    if (self.springAnimated) return;
    
    self.springAnimated = YES;
    
    POPSpringAnimation *spring = [POPSpringAnimation animationWithPropertyNamed:kPOPLayerPositionY];
    spring.beginTime = CACurrentMediaTime() + 0.1;
    spring.fromValue = @(self.matchTipsView.center.y);
    spring.toValue = @(self.matchTipsView.center.y);
    
    spring.springSpeed = 0.f;
    spring.velocity = @27;
    spring.springBounciness = 20.f;
    @weakify(self);
    spring.completionBlock = ^(POPAnimation *anim, BOOL finished) {
        @strongify(self);
        self.springAnimated = NO;
    };
    [self.matchTipsView.layer pop_addAnimation:spring forKey:@"layerPositionAnimation"];
}

- (void)resetUsersAnimation:(BOOL)animated completion:(void (^)(BOOL finished))completion {
    self.leftMatchUserView.alpha = 0;
    self.rightMatchUserView.alpha = 0;
    self.bottomMatchUserView.alpha = 0;
    !completion ?: completion(YES);
}

- (void)applyUsersAnimation {
    [self applyAnimationForUserView:self.leftMatchUserView duration:2 delay:0 completion:nil];
    [self applyAnimationForUserView:self.rightMatchUserView duration:2 delay:0.3f completion:nil];
    [self applyAnimationForUserView:self.bottomMatchUserView duration:2 delay:0.6f completion:nil];
}

- (void)applyAnimationForUserView:(UIView *)userView duration:(NSTimeInterval)duration delay:(NSTimeInterval)delay completion:(void (^)(BOOL finished))completion {
    NSTimeInterval begin = CACurrentMediaTime() + delay;
    
    POPBasicAnimation *alpha = [POPBasicAnimation animationWithPropertyNamed:kPOPViewAlpha];
    alpha.beginTime = begin;
    alpha.fromValue = @(0);
    alpha.toValue = @(1.f);
    alpha.duration = duration;
    [UIView animateWithDuration:2 animations:^{
        
    }];
    POPBasicAnimation *scale = [POPBasicAnimation animationWithPropertyNamed:kPOPViewScaleXY];
    scale.beginTime = begin;
    scale.fromValue = @(CGSizeMake(0, 0));
    scale.toValue = @(CGSizeMake(1, 1));
    scale.duration = duration;
    scale.completionBlock = ^(POPAnimation *anim, BOOL finished) {
        !completion ?: completion(finished);
    };
    
    [userView pop_addAnimation:alpha forKey:@"layerAlphaAnimation"];
    [userView pop_addAnimation:scale forKey:@"layerScaleAnimation"];
}

- (void)updateControls {
    self.recordTipsView.hidden = NO;
    self.matchTipsView.hidden = NO;
    self.animationView.hidden = NO;
    self.myAvaterImageView.hidden = NO;
    self.matchTitleLabel.hidden = NO;
    self.matchDetailsLabel.hidden = NO;
    
    YPMICUserInfo *user = [self.users objectOrNilAtIndex:0];
    self.matchTitleLabel.text = [NSString stringWithFormat:@"目前有%ld个小哥哥小姐姐正在匹配...", user.linkNum];
    [self updateMyAvater];
    [self showSpringAnimation];
}

- (void)addCores {
    AddCoreClient(HJMICCoreClient, self);
}

- (void)removeCores {
    RemoveCoreClient(HJMICCoreClient, self);
}

- (void)applyLinkAnimation:(BOOL)animated {
    if (self.animationView.hidden) return;
    
    if (animated) {
        [self.animationView startAnimation];
    } else {
        [self.animationView pauseAnimation];
    }
}

- (NSString *)getAdjustImagePath:(NSString *)URLString {
    if (!URLString.length) return @"";
    
    NSString *buffer = [NSString stringWithString:URLString];
    buffer = [buffer cutAvatarImageSize];
    return buffer;
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.animationView];
    [self.view addSubview:self.myAvaterImageView];
    [self.view addSubview:self.leftMatchUserView];
    [self.view addSubview:self.rightMatchUserView];
    [self.view addSubview:self.bottomMatchUserView];
    [self.view addSubview:self.matchTitleLabel];
    [self.view addSubview:self.matchDetailsLabel];
    [self.view addSubview:self.recordTipsView];
    [self.view addSubview:self.matchTipsView];
}

- (void)layoutControls {
    CGFloat avaterTop = 230;
    CGSize avaterSize = CGSizeMake(80, 80);
    CGSize userSize = CGSizeMake(45, 45);
    CGFloat userRadius = 120;
    CGPoint rightCenterOffset = getCircleCoordinate(-35, userRadius);
    CGPoint leftCenterOffset = getCircleCoordinate(195, userRadius);
    CGPoint bottomCenterOffset = getCircleCoordinate(55, userRadius);
    CGFloat matchDetailsBottom = 81;
    CGFloat recordTipsWidth = XC_RATIO_WIDTH(334);
    CGFloat recordTipsHeight = recordTipsWidth * 47/334.f;
    CGSize recordTipsSize = CGSizeMake(recordTipsWidth, recordTipsHeight);
    if (iPhone5s) {
        matchDetailsBottom = 40;
        avaterTop = 180;
    }
    
    [self.animationView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self.myAvaterImageView);
        make.height.width.equalTo(self.view.mas_width);
    }];
    
    [self.myAvaterImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(avaterSize));
        make.top.equalTo(self.view).offset(avaterTop);
        make.centerX.equalTo(self.view);
    }];
    
    [self.rightMatchUserView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(userSize));
        make.centerX.equalTo(self.myAvaterImageView).offset(rightCenterOffset.x);
        make.centerY.equalTo(self.myAvaterImageView).offset(rightCenterOffset.y);
    }];
    
    [self.bottomMatchUserView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(userSize));
        make.centerX.equalTo(self.myAvaterImageView).offset(bottomCenterOffset.x);
        make.centerY.equalTo(self.myAvaterImageView).offset(bottomCenterOffset.y);
    }];
    
    [self.leftMatchUserView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(userSize));
        make.centerX.equalTo(self.myAvaterImageView).offset(leftCenterOffset.x);
        make.centerY.equalTo(self.myAvaterImageView).offset(leftCenterOffset.y);
    }];
    
    [self.matchTipsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.bottom.equalTo(self.myAvaterImageView.mas_top).offset(-30);
    }];
    
    [self.recordTipsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.equalTo(self.view).offset(6);
        make.size.equalTo(@(recordTipsSize));
    }];
    
    [self.matchDetailsLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view).offset(20);
        make.right.equalTo(self.view).offset(-20);
        make.bottom.equalTo(self.view).offset(-matchDetailsBottom);
    }];
    
    [self.matchTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view).offset(20);
        make.right.equalTo(self.view).offset(-20);
        make.bottom.equalTo(self.matchDetailsLabel.mas_top).offset(-15);
    }];
}

#pragma mark - setters/getters
- (NSMutableArray *)users {
    if (!_users) {
        _users = @[].mutableCopy;
    }
    return _users;
}

- (UIButton *)myAvaterImageView {
    if (!_myAvaterImageView) {
        _myAvaterImageView = [UIButton new];
        _myAvaterImageView.layer.masksToBounds = YES;
        _myAvaterImageView.layer.cornerRadius = 80.f/2;
        _myAvaterImageView.layer.borderWidth = 5.f;
        _myAvaterImageView.layer.borderColor = [UIColor colorWithHexString:@"#23cfb1"].CGColor;
        [_myAvaterImageView addTarget:self action:@selector(myAvaterImageViewTapAction:) forControlEvents:UIControlEventTouchUpInside];
        _myAvaterImageView.exclusiveTouch = YES;
        _myAvaterImageView.hidden = YES;
    }
    return _myAvaterImageView;
}

- (UIImageView *)leftMatchUserView {
    if (!_leftMatchUserView) {
        _leftMatchUserView = [self getMatchUserView];
        _leftMatchUserView.alpha = 0;
    }
    return _leftMatchUserView;
}

- (UIImageView *)rightMatchUserView {
    if (!_rightMatchUserView) {
        _rightMatchUserView = [self getMatchUserView];
        _rightMatchUserView.alpha = 0;
    }
    return _rightMatchUserView;
}

- (UIImageView *)bottomMatchUserView {
    if (!_bottomMatchUserView) {
        _bottomMatchUserView = [self getMatchUserView];
        _bottomMatchUserView.alpha = 0;
    }
    return _bottomMatchUserView;
}

- (SVGAImageView *)animationView {
    if (!_animationView) {
        _animationView = [SVGAImageView new];
        _animationView.imageName = @"yp_mic_effect_match_radio";
        _animationView.hidden = YES;
    }
    return _animationView;
}

- (UILabel *)matchTitleLabel {
    if (!_matchTitleLabel) {
        _matchTitleLabel = [UILabel new];
        _matchTitleLabel.font = [UIFont boldSystemFontOfSize:15];
        _matchTitleLabel.textColor = [UIColor colorWithHexString:@"#333333"];
        _matchTitleLabel.text = @"目前有XX个小哥哥小姐姐正在匹配...";
        _matchTitleLabel.textAlignment = NSTextAlignmentCenter;
        _matchTitleLabel.numberOfLines = 0;
        _matchTitleLabel.hidden = YES;
    }
    return _matchTitleLabel;
}

- (UILabel *)matchDetailsLabel {
    if (!_matchDetailsLabel) {
        _matchDetailsLabel = [UILabel new];
        _matchDetailsLabel.font = [UIFont systemFontOfSize:12];
        _matchDetailsLabel.textColor = [UIColor colorWithHexString:@"#999999"];
        _matchDetailsLabel.text = @"营造私密聊天环境，请将您的房间上座人数控制在3人以下";
        _matchDetailsLabel.textAlignment = NSTextAlignmentCenter;
        _matchDetailsLabel.numberOfLines = 0;
        _matchDetailsLabel.hidden = YES;
    }
    return _matchDetailsLabel;
}

- (UIImageView *)matchTipsView {
    if (!_matchTipsView) {
        _matchTipsView = [UIImageView new];
        _matchTipsView.image = [UIImage imageNamed:@"yp_mic_icon_match_tips"];
        _matchTipsView.hidden = YES;
    }
    return _matchTipsView;
}

- (UIImageView *)recordTipsView {
    if (!_recordTipsView) {
        _recordTipsView = [UIImageView new];
        _recordTipsView.image = [UIImage imageNamed:@"yp_mic_icon_record_tips"];
        _recordTipsView.hidden = YES;
        _recordTipsView.hidden = YES;
    }
    return _recordTipsView;
}

@end
