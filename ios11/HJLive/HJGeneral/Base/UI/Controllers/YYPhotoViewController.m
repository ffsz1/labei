//
//  YYPhotoViewController.m
//  YYMobile
//
//  Created by huangshuqing on 4/18/15.
//  Copyright (c) 2015 YY.inc. All rights reserved.
//

#import "YYPhotoViewController.h"
#import "ImPageImageView.h"

@interface YYPhotoViewController ()
{
    NSUInteger _pageIndex;
}

@end

@implementation YYPhotoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (id)initWithPageIndex:(NSUInteger)pageIndex
{
    self = [super initWithNibName:nil bundle:nil];
    if (self) {
        _pageIndex = pageIndex;
    }
    return self;
}

- (NSInteger)pageIndex
{
    return _pageIndex;
}

- (BOOL) zoomScaleChanged
{
    __block BOOL changed = NO;
    
    [self.view.subviews enumerateObjectsUsingBlock:^(ImPageImageView* imageView, NSUInteger idx, BOOL *stop) {
        if (imageView && [imageView isKindOfClass:[ImPageImageView class]]) {
            changed = [imageView zoomScaleChanged];
            *stop = YES;
        }
    }];
    
    return changed;
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self.view.subviews enumerateObjectsUsingBlock:^(ImPageImageView* imageView, NSUInteger idx, BOOL *stop) {
        if (imageView && [imageView isKindOfClass:[ImPageImageView class]]) {
            if ([imageView zoomScaleChanged]) {
                [imageView resetScale];
            }
            *stop = YES;
        }
    }];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
