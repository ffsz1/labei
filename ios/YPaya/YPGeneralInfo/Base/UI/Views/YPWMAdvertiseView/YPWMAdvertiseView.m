//
//  AdvertiseView.m
//  JinSeShiJi
//
//  Created by zn on 16/8/10.
//
//

#import "YPWMAdvertiseView.h"
#import "HJAdUIClient.h"


@interface YPWMAdvertiseView()

@property (nonatomic, strong) UIImageView *adView;

@property (nonatomic, strong) UIButton *countBtn;

@property (nonatomic, strong) NSTimer *countTimer;

@property (nonatomic, assign) int count;

@end

// 广告显示的时间
static int const showtime = 3;

@implementation YPWMAdvertiseView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        
        // 1.广告图片
        _adView = [[UIImageView alloc] initWithFrame:frame];
        _adView.userInteractionEnabled = YES;
        _adView.contentMode = UIViewContentModeScaleAspectFill;
        _adView.clipsToBounds = YES;
        _adView.backgroundColor = [UIColor whiteColor];
        
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(pushToAd)];
        [_adView addGestureRecognizer:tap];
        
        // 2.跳过按钮
        CGFloat btnW = 60;
        CGFloat btnH = 30;
        _countBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        _countBtn.frame = CGRectMake(kscreenWidth - btnW - 24, btnH + 10, btnW, btnH);
        [_countBtn addTarget:self action:@selector(dismiss) forControlEvents:UIControlEventTouchUpInside];
        [_countBtn setTitle:[NSString stringWithFormat:@"%@%d",NSLocalizedString(XCADPass, nil) ,showtime] forState:UIControlStateNormal];
        _countBtn.titleLabel.font = [UIFont systemFontOfSize:15];
        [_countBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _countBtn.backgroundColor = [UIColor colorWithRed:38 /255.0 green:38 /255.0 blue:38 /255.0 alpha:0.6];
        _countBtn.layer.cornerRadius = 4;
        [self addSubview:_adView];
        [self addSubview:_countBtn];
    }
    return self;
}

- (void)setFilePath:(NSString *)filePath
{
    _filePath = filePath;
    _adView.image = [UIImage imageWithContentsOfFile:filePath];
}

- (void)setAdImage:(UIImage *)adImage {
    _adImage = adImage;
    _adView.image = adImage;
}

- (void)pushToAd{
    [self dismiss];
    if (self.oldAdInfo) {
        NotifyCoreClient(HJAdUIClient, @selector(onAdTap:), onAdTap:self.oldAdInfo);
        [self dismiss];
    }
}

- (void)countDown
{
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        self.count = self.count - 1;
        
        NSLog(@"timeout:%d",self.count);
                
        [self.countBtn setTitle:[NSString stringWithFormat:@"%@%d",NSLocalizedString(XCADPass, nil) ,self.count] forState:UIControlStateNormal];
        if (self.count == 0) {
            
            [self dismiss];
        }
        
    });
    
    
}

- (void)show
{
    // 倒计时方法1：GCD
//        [self startCoundown];
    
    // 倒计时方法2：定时器
    UIWindow *window = [UIApplication sharedApplication].delegate.window;
    self.tag = 9999;
    [window addSubview:self];
    
    [self startTimer];

    
}

// 定时器倒计时
- (void)startTimer
{
    _count = showtime;
    
    self.countTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(countDown) userInfo:nil repeats:YES];

    [[NSRunLoop mainRunLoop] addTimer:self.countTimer forMode:NSRunLoopCommonModes];
}

// 移除广告页面
- (void)dismiss
{
    [self.countTimer invalidate];
    self.countTimer = nil;
    
    [UIView animateWithDuration:0.5f animations:^{
        
        self.alpha = 0.f;
        
    } completion:^(BOOL finished) {
        
        [self removeFromSuperview];
        
    }];
    
}


@end
