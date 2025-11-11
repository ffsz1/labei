//
//  YPImPageImageView.m
//  YYMobile
//
//  Created by James Pend on 15/1/19.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YPImPageImageView.h"
#import "UIImageView+YYWebImage.h"


static  CGFloat  SystemIndependentScreenWidth()
{
    CGRect frame = [UIScreen mainScreen].bounds;
    // orientation independent
    CGFloat width = (MIN(frame.size.width, frame.size.height));
    return width;
}

static  CGFloat  SystemIndependentScreenHeight()
{
    CGRect frame = [UIScreen mainScreen].bounds;
    CGFloat height = (MAX(frame.size.width, frame.size.height));
    return height;
}
#define MRScreenWidth      (SystemIndependentScreenWidth())
#define MRScreenHeight     (SystemIndependentScreenHeight())

@interface YPImPageImageView()<UIScrollViewDelegate>
{
    UIImageView *_imageView;
    UIActivityIndicatorView *_activityView;
    
    CGPoint _pointToCenterAfterResize;
    CGFloat _scaleToRestoreAfterResize;
}
@property (strong, nonatomic) UITapGestureRecognizer *doubleTap;
@property (strong , nonatomic) UITapGestureRecognizer *singleTap;

@end

@implementation YPImPageImageView

+ (YPImPageImageView*)view
{
    YPImPageImageView* view = [[YPImPageImageView alloc] init];
    return view;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self _configScrollView];
        [self _configImageView];
        self.backgroundColor = [UIColor blackColor];
    }
    return self;
}
- (void)awakeFromNib
{
    [self _configScrollView];
    [self _configImageView];
    self.backgroundColor = [UIColor blackColor];

}
- (void)dealloc
{
    _imageUrl = nil;
    _imageData = nil;
 
    if (nil != _activityView) {
        [_activityView removeFromSuperview];
        _activityView = nil;
    }
    
    if (nil != _imageView) {
        [_imageView removeFromSuperview];
        _imageView = nil;
    }
    
    if(_doubleTap)
    {
        [_doubleTap removeTarget:nil action:NULL];
        _doubleTap = nil;
        
    }
    
    if (_singleTap) {
        [_singleTap removeTarget:nil action:NULL];
        _singleTap = nil;
        
    }
}

- (void)_configScrollView
{
    self.backgroundColor = [UIColor blackColor];
 
    self.delegate = self;
    self.userInteractionEnabled = YES;
    self.showsHorizontalScrollIndicator = NO;//是否显示侧边的滚动栏
    self.showsVerticalScrollIndicator = NO;
    self.scrollsToTop = NO;
    self.scrollEnabled = YES;
    self.bouncesZoom = YES;
    self.decelerationRate = UIScrollViewDecelerationRateFast;
 
    [self _addViewGestures];
}

- (void) _configImageView
{
    if (_activityView == nil) {
        _activityView = [[UIActivityIndicatorView alloc] init];
        _activityView.frame = CGRectMake((MRScreenWidth - 50)/2, (MRScreenHeight - 50)/2, 50, 50);
        //_activityView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        [self addSubview:_activityView];
        _activityView.hidden = YES;
    }
    
    if (_imageView == nil) {
        _imageView = [[UIImageView alloc] init];
        _imageView.contentMode = UIViewContentModeCenter;
        // do not add subview here
        //        [self addSubview:_imageView];
    }
}

- (void)_addImageGestures
{
    UITapGestureRecognizer *doubleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onHandlerDoubleTap:)];
    doubleTap.numberOfTapsRequired = 2;
    doubleTap.numberOfTouchesRequired = 1;
    
    self.doubleTap = doubleTap;
    [self addGestureRecognizer:doubleTap];
    
    [self.singleTap requireGestureRecognizerToFail:self.doubleTap]; //区分单击，双击
    
    UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(onPageLongPress:)];
    [self addGestureRecognizer:longPress];
}

- (void) _addViewGestures
{
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onHandlerSingleTap:)];
    singleTap.numberOfTapsRequired = 1;
    self.singleTap = singleTap;
    [self addGestureRecognizer:singleTap];
}

- (void)configureForImage:(UIImage*)image
{
    _imageData = image;
    
    _imageView.frame = (CGRect){.origin=CGPointMake(0.0f, 0.0f), .size = image.size};
    
    [self addSubview:_imageView];
    
    self.contentSize = image.size;
 
    [self setMaxMinZoomScalesForCurrentBounds];
  
    [self configScrollViewContentsIfZoomScaleChanged];
    
    _imageView.alpha = 0.1;
    __typeof(_imageView) __weak wImgView = _imageView;
    [UIView animateWithDuration:0.2 animations:^{
        wImgView.alpha = 1.0;
    }];
    
    return;
}

- (void)setMaxMinZoomScalesForCurrentBounds
{
    CGRect scrollViewFrame = self.frame;
    CGFloat scaleWidth = scrollViewFrame.size.width / self.contentSize.width;
    CGFloat scaleHeight = scrollViewFrame.size.height / self.contentSize.height;
    CGFloat minScale = MIN(scaleWidth, scaleHeight);
    
    self.minimumZoomScale = minScale;
    self.maximumZoomScale = minScale * 2;//MAX(minScale, 4.0f);
    self.zoomScale = self.minimumZoomScale;
}

- (void)resetScale
{
    [self setZoomScale:self.minimumZoomScale animated:NO];
    
}

- (BOOL) zoomScaleChanged
{
    if (_imageData == nil) {
        return NO;
    }
    
    if (fabs(self.zoomScale - self.minimumZoomScale) > FLT_EPSILON) {
        return YES;
    }
    
    return NO;
}

- (void)setImageData:(UIImage *)image
{
    _imageView.image = image;
    
    if (image) {
        [self configureForImage:image];
        
        // Gestures
        [self _addImageGestures];
        
        [_activityView stopAnimating];
        _activityView.hidden = YES;
    } else {
        [_activityView stopAnimating];
        _activityView.hidden = YES;
        if (_imageView) {
            _imageView.image = nil;
            [_imageView removeFromSuperview];
        }
    }
}

-(void)setImageUrl:(NSString *)imageUrl
{
    _imageUrl = [imageUrl copy];
    
    if (_imageUrl) {
        __weak __typeof__(self) wself = self;
        _activityView.hidden = NO;
        [_activityView startAnimating];
//        [_imageView setImageWithURL:[NSURL URLWithString:_imageUrl] placeholderImage:nil completed:^(UIImage *image, NSError *error, YYWebImageCacheType cacheType) {
//            
//            __strong __typeof__(self) sself = wself;
//            if (sself) {
//                [sself configureForImage:image];
//                [sself _addImageGestures];
//                [sself->_activityView stopAnimating];
//                sself->_activityView.hidden = YES;
//
//            }
//
//        }];
        [_imageView yy_setImageWithURL:[NSURL URLWithString:_imageUrl] placeholder:nil options:YYWebImageOptionShowNetworkActivity completion:^(UIImage * _Nullable image, NSURL * _Nonnull url, YYWebImageFromType from, YYWebImageStage stage, NSError * _Nullable error) {
            __strong __typeof__(self) sself = wself;
            if (sself) {
                [sself configureForImage:image];
                [sself _addImageGestures];
                [sself->_activityView stopAnimating];
                sself->_activityView.hidden = YES;
                
            }
        }];
    }
    else
    {
        [_activityView stopAnimating];
        _activityView.hidden = YES;
        
        _imageView.image = nil;
        if (_imageView) {
            [_imageView removeFromSuperview];
        }
    }

}

#pragma mark - Rotation support
- (void)configScrollViewContentsIfZoomScaleChanged {
    CGSize boundsSize = self.frame.size;
    CGRect contentsFrame = _imageView.frame;
    
    if (contentsFrame.size.width < boundsSize.width) {
        contentsFrame.origin.x = fabs(boundsSize.width - contentsFrame.size.width) / 2.0f;
    } else {
        contentsFrame.origin.x = 0.0f;
    }
    
    if (contentsFrame.size.height < boundsSize.height) {
        contentsFrame.origin.y = fabs(boundsSize.height - contentsFrame.size.height) / 2.0f;
    } else {
        contentsFrame.origin.y = 0.0f;
    }
    
    _imageView.frame = contentsFrame;
    
    /*
     * when showing slim picture (horizontal、long、 thin）, if drag the picture downwards,
     * The UIScrollView will steal 'swipe' gesture from UIPageViewController,
     * hence you will not be able to swipe to next page and the library appears to freeze.
     */
    if (self.zoomScale > self.minimumZoomScale) {
        self.scrollEnabled = YES;
    } else {
        self.scrollEnabled = NO;
    }
}

//双击图片放大效果
- (void)onHandlerDoubleTap:(UITapGestureRecognizer*)tapGesture
{
    
    CGFloat newZoomScale = 1.;
    if (fabs(self.zoomScale - self.minimumZoomScale) < FLT_EPSILON) {
        newZoomScale = MIN(self.minimumZoomScale * 2.0, self.maximumZoomScale);
    }
    else {
        newZoomScale = self.minimumZoomScale;
    }
    
    CGRect zoomRect = [self _zoomRectForScale:newZoomScale withCenter:[tapGesture locationInView:tapGesture.view]];
    [self zoomToRect:zoomRect animated:YES];
    
}

- (CGRect)_zoomRectForScale:(float)scale withCenter:(CGPoint)center
{
    CGRect zoomRect;
    zoomRect.size.height = self.frame.size.height / scale;
    zoomRect.size.width  = self.frame.size.width  / scale;
    zoomRect.origin.x = center.x - (zoomRect.size.width  / 2.0);
    zoomRect.origin.y = center.y - (zoomRect.size.height / 2.0);
    return zoomRect;
}



- (void)onHandlerSingleTap:(UITapGestureRecognizer*)tapGesture
{
    if ([self.pageDelegate respondsToSelector:@selector(imPageDelegete_didTapImageView)]) {
        [self.pageDelegate imPageDelegete_didTapImageView];
    }
}

- (void)onPageLongPress:(id)sender {
    
    UILongPressGestureRecognizer *gesture = sender;
    // 避免两次触发
    if(UIGestureRecognizerStateBegan == gesture.state) {
        if ([self.pageDelegate respondsToSelector:@selector(onPageImageLongPress:)]) {
            [self.pageDelegate onPageImageLongPress:self];
        }
    }
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

#pragma mark - UIScrollViewDelegate

- (UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
{
    return _imageView;
}

- (void)scrollViewDidZoom:(UIScrollView *)scrollView {
    [self configScrollViewContentsIfZoomScaleChanged];
}


@end
