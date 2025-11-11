//
//  YPYYPhotoListView.m
//  YYMobile
//
//  Created by James Pend on 15/3/18.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import "YPYYPhotoListView.h"
#import "FBKVOController.h"
#import "YPImPageImageView.h"
#import "UIView+IMLayOut.h"
#import "YPYYPhotoViewController.h"
#import <AssetsLibrary/ALAsset.h>
#import <AssetsLibrary/ALAssetRepresentation.h>

//static NSInteger const kMaxResuseImageNum = 3; //重用对象池最大个数
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

@interface YPYYPhotoListView()<ImPageDelegate, UIPageViewControllerDelegate, UIPageViewControllerDataSource>
{
    __weak YPYYPhotoViewController* _currentVC;
    UIPageViewController* _photoPageViewController;
    CGRect _photoPageViewControllerDisplayRect;
    
}
@end

@implementation YPYYPhotoListView

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.backgroundColor = [UIColor blackColor];
}

- (void)dealloc
{
    [self hideContentController:_photoPageViewController];
    _photoPageViewController = nil;
}

/**
 *  获取当前的Image
 *
 *  @return 当前显示的Image
 */
- (UIImage *)currentImage
{
    if (!_dataProvider) {
        return nil;
    }
    
    if (self.dataProvider.currentPage-1>=self.dataProvider.imageUrlList.count) {
        return nil;
    }
    
    NSArray *imageList = self.dataProvider.imageUrlList;
    id currentImageUrl = imageList[self.dataProvider.currentPage-1];
    
    if ([currentImageUrl isKindOfClass:UIImage.class])
    {
        return currentImageUrl;
    }

    for (UIView *obj in _currentVC.view.subviews)
    {
        if (obj && [obj isKindOfClass:[YPImPageImageView class]]) {
            YPImPageImageView *imageView = (YPImPageImageView*)obj;
            if ([currentImageUrl isKindOfClass:NSString.class] && [currentImageUrl isEqualToString:imageView.imageUrl]) {
                return imageView.imageData;
            }
        }
    }
    
    return nil;
}

- (void)setCurrentImage:(UIImage *)currentImage
{
    if (!_dataProvider) {
        return;
    }
    
    if (self.dataProvider.currentPage-1>=self.dataProvider.imageUrlList.count) {
        return;
    }
    
    NSMutableArray *imageList = [self.dataProvider.imageUrlList mutableCopy];
    id currentImageUrl = imageList[self.dataProvider.currentPage-1];
    
    for (UIView *obj in _currentVC.view.subviews)
    {
        if (obj && [obj isKindOfClass:[YPImPageImageView class]]) {
            YPImPageImageView *imageView = (YPImPageImageView*)obj;
            if ([currentImageUrl isKindOfClass:NSString.class] && [currentImageUrl isEqualToString:imageView.imageUrl]) {
                [imageView setImageUrl:nil];
                [imageView setImageData:currentImage];
                imageList[self.dataProvider.currentPage-1] = currentImage;
                self.dataProvider.imageUrlList = [imageList copy];
                return;
            } else if ([currentImageUrl isKindOfClass:UIImage.class] && currentImageUrl == imageView.imageData){
                [imageView setImageData:currentImage];
                imageList[self.dataProvider.currentPage-1] = currentImage;
                self.dataProvider.imageUrlList = [imageList copy];
                return;
            }
        }
    }
}

- (void)setDataProvider:(YPYYPhotoListDataProvider *)dataProvider
{
    if (dataProvider == nil) {
        return;
    }

    [self _configImageViewList:dataProvider];
    
    if ([self.delegate respondsToSelector:@selector(yyphotolistView:page:allPage:)]) {
        [self.delegate yyphotolistView:self page:self.dataProvider.currentPage allPage:dataProvider.totalNum];
    }
    
    
}

- (void) layoutSubviews
{
    [super layoutSubviews];
    
    if (_photoPageViewController && !CGRectEqualToRect(_photoPageViewControllerDisplayRect, self.frame)) {
        [self displayContentController:_photoPageViewController];
    }
    
}

#pragma mark private

- (YPYYPhotoViewController *)photoViewControllerForPageIndex:(NSUInteger)pageIndex
{
    if (pageIndex < self.dataProvider.imageCount)
    {
        YPYYPhotoViewController* vc = [[YPYYPhotoViewController alloc] initWithPageIndex:pageIndex];
        vc.view.frame = CGRectMake(0, 0, MRScreenWidth, MRScreenHeight);
        
        NSArray *imageList = self.dataProvider.imageUrlList;
        YPImPageImageView *view = [YPImPageImageView view];
//        view.imageManager = self.imageManager;
        view.pageDelegate = self;
        view.frame = CGRectMake(0, 0, MRScreenWidth, MRScreenHeight);
        id obj = imageList[pageIndex];
        if ([obj isKindOfClass:[NSString class]]) {
            view.imageUrl = obj;
        }
        else if ([obj isKindOfClass:[UIImage class]])
        {
            view.imageData = obj;
        }
        else if ([obj isKindOfClass:[ALAsset class]])
        {
            ALAsset *aSset = (ALAsset *)obj;
            view.imageData = [UIImage imageWithCGImage:aSset.defaultRepresentation.fullResolutionImage];;
        }

        [vc.view addSubview:view];
        
        return vc;
    }
    
    return nil;
}

- (UIViewController *)pageViewController:(UIPageViewController *)pvc viewControllerBeforeViewController:(YPYYPhotoViewController *)vc
{
    NSUInteger index = vc.pageIndex;
    if (0 == index) {
        return nil;
    }
    
    return [self photoViewControllerForPageIndex:(index - 1)];
}

- (UIViewController *)pageViewController:(UIPageViewController *)pvc viewControllerAfterViewController:(YPYYPhotoViewController *)vc
{
    NSUInteger index = vc.pageIndex;
    return [self photoViewControllerForPageIndex:(index + 1)];
}

- (void)pageViewController:(UIPageViewController *)pageViewController didFinishAnimating:(BOOL)finished previousViewControllers:(NSArray *)previousViewControllers transitionCompleted:(BOOL)completed
{
    if (!completed) {
        return;
    }
    
    NSLog(@"Current Page = %@", pageViewController.viewControllers);
    
    YPYYPhotoViewController *curVC = [pageViewController.viewControllers firstObject];
    
    if (curVC && [curVC isKindOfClass:[YPYYPhotoViewController class]]) {
        NSUInteger idx =  curVC.pageIndex;
       
        NSInteger page =  idx + 1;
        if (page == self.dataProvider.currentPage || page == 0 ) {
            return;
        }
        self.dataProvider.currentPage = page;
        self.dataProvider.imageIndex = page-1;
        _currentVC = curVC;
        if ([self.delegate respondsToSelector:@selector(yyphotolistView:page:allPage:)]) {
            [self.delegate yyphotolistView:self page:page allPage:self.dataProvider.totalNum];
        }
    }
   
}

- (void) hideContentController: (UIViewController*) content
{
    [content willMoveToParentViewController:nil];  // 1
    [content.view removeFromSuperview];            // 2
    [content removeFromParentViewController];      // 3
}

- (void) displayContentController: (UIViewController*) content;
{
    if (nil == self.parentViewController || nil == content) {
        return;
    }
    
    _photoPageViewControllerDisplayRect = self.frame;
    
    [content willMoveToParentViewController:self.parentViewController];
    
    [self.parentViewController addChildViewController:content];
    content.view.frame = [self frameForContentController];
    [self addSubview:content.view];
    [self sendSubviewToBack:content.view];
    [content didMoveToParentViewController:self.parentViewController];
}

- (CGRect) frameForContentController
{
    return CGRectMake(0, 0, MRScreenWidth, MRScreenHeight);
}

- (void)setParentViewController:(UIViewController *)parentViewController
{
    _parentViewController = parentViewController;
    
    if (_photoPageViewController == nil) {
        return;
    }
    if (_parentViewController == nil) {
        [self hideContentController:_photoPageViewController];
    } else {
        [self displayContentController:_photoPageViewController];
    }
}

- (void)_configImageViewList:(YPYYPhotoListDataProvider *)dataProvider
{
    BOOL refreshAll = YES;
    
    if (_photoPageViewController == nil) {
        NSDictionary *options = @{UIPageViewControllerOptionSpineLocationKey:[NSNumber numberWithInteger:UIPageViewControllerSpineLocationMin],
                                  UIPageViewControllerOptionInterPageSpacingKey:[NSNumber numberWithInteger:12]};

        _photoPageViewController = [[UIPageViewController alloc] initWithTransitionStyle:UIPageViewControllerTransitionStyleScroll navigationOrientation:UIPageViewControllerNavigationOrientationHorizontal options:options];
        _photoPageViewController.dataSource = self;
        _photoPageViewController.delegate = self;
        _photoPageViewController.view.backgroundColor = [UIColor blackColor];
        
        [self displayContentController:_photoPageViewController];
    } else {
        if (!CGRectEqualToRect(_photoPageViewControllerDisplayRect, self.frame)) {
            [self displayContentController:_photoPageViewController];
        }
        
        if ((_dataProvider != nil) && (_dataProvider.imageUrlList != nil)
            && (_dataProvider != dataProvider) /*can not differentiate*/
            && (_dataProvider.imageUrlList.count > 0)
            && (_dataProvider.imageIndex == dataProvider.imageIndex)
            && (_dataProvider.imageUrlList.count <= dataProvider.imageUrlList.count)
            && (_dataProvider.imageIndex < _dataProvider.imageUrlList.count)) {
          
            NSArray *headerPart = [dataProvider.imageUrlList subarrayWithRange:NSMakeRange(0, _dataProvider.imageUrlList.count)];
            if ([_dataProvider.imageUrlList isEqualToArray:headerPart]) {
                refreshAll = NO;
            }
        }
        
    }
    
    _dataProvider = dataProvider;
    self.dataProvider.currentPage = self.dataProvider.imageIndex + 1;
    
    if (refreshAll || (_currentVC == nil)) {
        
        YPYYPhotoViewController *curPage = [self photoViewControllerForPageIndex:self.dataProvider.imageIndex];
        _currentVC = curPage;
        
        if (curPage != nil) {
            [_photoPageViewController setViewControllers:@[curPage]
                                               direction:UIPageViewControllerNavigationDirectionForward
                                                animated:NO
                                              completion:NULL];
        }

    } else {
        [_photoPageViewController setViewControllers:@[_currentVC]
                                           direction:UIPageViewControllerNavigationDirectionForward
                                            animated:NO
                                          completion:NULL];
    }
}



#pragma mark ImPageDelegate
- (void)imPageDelegete_didTapImageView;
{
    if ([self.delegate respondsToSelector:@selector(yyphotolistView:didSelecteIndex:)]) {
        [self.delegate yyphotolistView:self didSelecteIndex:self.dataProvider.currentPage -1];
    }
}

- (void)onPageImageLongPress:(YPImPageImageView *)imageView {
    if ([self.delegate respondsToSelector:@selector(yyphotolistView:didLongPressAtIndex:)]) {
        [self.delegate yyphotolistView:self didLongPressAtIndex:self.dataProvider.currentPage -1];
    }
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
