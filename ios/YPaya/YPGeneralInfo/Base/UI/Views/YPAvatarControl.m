
//
//  YPAvatarControl.m
//  YYMobile
//
//  Created by wuwei on 14/6/20.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YPAvatarControl.h"

#import "UIImageView+YYWebImage.h"

#import "UIImage+Utils.h"
#import <Masonry/Masonry.h>
#import "UIImage+extension.h"
#import "YPYYDefaultTheme.h"

//#import "FXImageView.h"

#define kDefaultRadiusScale 1

@interface YPAvatarControl ()

@property (nonatomic, weak) YYWebImageOperation *currentOperation;
@property (nonatomic, strong) UIImage *renderImage;
//@property (nonatomic, strong) UIImage *avatarImage;
@property (nonatomic, strong) NSURL *currentAvatarURL;
@property (nonatomic, assign) CGFloat cornerRadius;
@property (nonatomic, assign) BOOL isSetCornerRadius;
@property (nonatomic, strong) CAShapeLayer *maskLayer;



@end

@implementation YPAvatarControl

- (id)initWithFrame:(CGRect)frame
{
    
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self _initSubviews];
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)coder
{
    self = [super initWithCoder:coder];
    if (self) {
        [self _initSubviews];
    }
    return self;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    [self _initSubviews];
}

- (void)_initSubviews
{
    
//    _maskLayer = [CAShapeLayer new];
//    _maskLayer.fillColor = [UIColor whiteColor].CGColor;
//    _maskLayer.fillRule = kCAFillRuleEvenOdd;
//    [self.layer addSublayer:_maskLayer];
    self.imageView = [[UIImageView alloc]init];
//    [self addSubview:_imageView];
    [self insertSubview:self.imageView atIndex:0];
    [_imageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.mas_top);
        make.leading.mas_equalTo(self.mas_leading);
        make.trailing.mas_equalTo(self.mas_trailing);
        make.bottom.mas_equalTo(self.mas_bottom);
    }];
    
    _imageView.layer.cornerRadius = 5;
    _imageView.layer.masksToBounds = YES;
    
//    self.layer.cornerRadius = self.frame.size.width / 2;
//    self.layer.masksToBounds = YES;
    
    self.backgroundColor = UIColor.clearColor;
    self.userInteractionEnabled = NO;
    self.imageView.userInteractionEnabled = NO;
//    [self layoutIfNeeded];
//    CGFloat widthHeight = MIN(self.frame.size.width - self.borderWidth, self.frame.size.height - self.borderWidth);
//    if(!self.isSetCornerRadius){
//        self.cornerRadius = kDefaultRadiusScale * widthHeight;
//    }
    
}



- (void)setMaskCornerRadius:(CGFloat)maskCornerRadius {
//    _cornerRadius = maskCornerRadius;
//    _maskLayer.borderWidth = 0.5;
//    _maskLayer.borderColor = [UIColor grayColor].CGColor;
//    _maskLayer.cornerRadius = maskCornerRadius;
}

- (void)layoutSubviews {
    [super layoutSubviews];
//    _maskLayer.frame = self.layer.bounds;
//    UIBezierPath *path = [UIBezierPath bezierPathWithRect:self.layer.bounds];
//    [path appendPath:[UIBezierPath bezierPathWithRoundedRect:self.layer.bounds cornerRadius:self.frame.size.width/2]];
//    _maskLayer.path = path.CGPath;
}



- (void)setImage:(UIImage *)image
{
    NSParameterAssert([NSThread isMainThread]);
    
    if (self.avatarImage != image) {
        _currentAvatarURL = nil;
        self.avatarImage = image;
        self.imageView.image = image;
    }
}



- (UIImage *)image
{
    return _avatarImage;
}

- (void)setBorderWidth:(CGFloat)borderWidth
{
    if (_borderWidth != borderWidth) {
        _borderWidth = borderWidth;
//        [self setNeedsDisplay];
        self.imageView.layer.borderWidth = borderWidth;
    }
}

- (void)setBorderColor:(UIColor *)borderColor
{
    if (_borderColor != borderColor) {
        _borderColor = borderColor;
//        [self setNeedsDisplay];
        self.imageView.layer.borderColor = borderColor.CGColor;
    }
    
}

#pragma mark - URL

- (void)setImageURL:(NSURL *)url
{
    [self setImageURL:url placeholderImage:[UIImage imageNamed:default_avatar]];
}

- (void)setImageURL:(NSURL *)url placeholderImage:(UIImage *)placeholderImage
{
    [self setImageURL:url placeholderImage:placeholderImage completed:nil];
}


- (void)setImageURL:(NSURL *)url
   placeholderImage:(UIImage *)placeholderImage
          completed:(YYWebImageCompletionBlock)completed
{
//    [_imageView was_setCircleImageWithUrlString:url.absoluteString placeholder:placeholderImage];
    [_imageView sd_setImageWithURL:url.absoluteString placeholderImage:placeholderImage];
}

- (void)clipTheAvatar:(UIImageView *)imageView {
    
}

- (void)setCornerRadius:(CGFloat)cornerRadius
{
    _cornerRadius = cornerRadius;
    _isSetCornerRadius = YES;
}


@end

@implementation YPAvatarControl (User)

- (void)setImageWithUserID:(UserID)userId size:(AvatarControlSize)size
{
//    NSString *url = @"";
//#if !OFFICIAL_RELEASE
//    url = TEST_ENV_HOST;
//#else
//    url = RELEASE_ENV_HOST;
//#endif
//    NSString *action = @"toUserAvatar";
//   
//    NSString *oriAvatar = @"";
//    RLMResults *result = [YYUserInfo objectsWhere:@"userId = %lld", userId];
//    if ([result firstObject] != nil) {
//        YYUserInfo *userInfo = [result firstObject];
//        if (userInfo.avatar && ![userInfo.avatar isEqualToString:@""]) {
//            oriAvatar = [[userInfo.avatar componentsSeparatedByString:@"/"] lastObject];
//        }
//    }
//    url = [NSString stringWithFormat:@"%@/%@?uid=%@&avaType=%@&avatar=%@", url, action, @(userId), @(size), oriAvatar];
//    [self setImageURL:[NSURL URLWithString:url] placeholderImage:[UIImage imageNamed:default_avatar]];
}

@end
