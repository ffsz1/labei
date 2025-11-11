//
//  YYMessageImageViewController.m
//  YYMobile
//
//  Created by James Pend on 14-8-27.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYMessageImageViewController.h"
#import "UIView+Toast.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import "YYAlertView.h"
#import "ImPageImageView.h"
#import "FBKVOController.h"
#import "YYPhotoListView.h"
#import "ImagePickerUtils.h"

@interface YYMessageImageViewController ()<YYPhotoListViewDelegate>
- (IBAction)onBackHandler:(id)sender;

- (IBAction)onSaveImageHandler:(id)sender;
@property (strong, nonatomic) UITapGestureRecognizer *doubleTap;
@property (strong , nonatomic) UITapGestureRecognizer *singleTap;
@property (weak, nonatomic) IBOutlet UIButton *moreBtn;
@property (weak, nonatomic) IBOutlet UILabel *pageLabel;
@property (assign, nonatomic) NSInteger currentPage;
@property (assign, nonatomic) CGRect visulRect;//可视化
@property (nonatomic,strong) FBKVOController *kvo;
@property (weak, nonatomic) IBOutlet YYPhotoListView *photoListView;

@end

@implementation YYMessageImageViewController

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
//    if (self) {
    
//    }
    return self;
}

- (void)dealloc
{
    _imageList = nil;
    if (_photoListView) {
        _photoListView.delegate = nil;
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.photoListView.delegate = self;
    self.photoListView.parentViewController = self;
    if (self.imageList != nil) {
        [self resetImageProvider];
    }
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
//    if ([_photoListView superview]) {
//        [_photoListView removeFromSuperview];
//    }
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)preferredNavigationBarHidden
{
    return YES;
}

//- (BOOL)prefersStatusBarHidden
//{
//    return YES;
//}

- (IBAction)onBackHandler:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onSaveImageHandler:(id)sender {
    if (self.delegate != nil) {
        [self.delegate onMoreBtnClicked];
    }

}

- (void)resetImageProvider
{
    YYPhotoListDataProvider *provider = [[YYPhotoListDataProvider alloc] init];
    provider.imageUrlList = self.imageList;
    provider.imageIndex = self.imageIndex;
    provider.totalNum = self.imageList.count;
    self.photoListView.dataProvider = provider;
    self.pageLabel.hidden = self.pageHidden;
    self.moreBtn.hidden = self.rightBarHidden;
}

- (void)setCurrentImage:(UIImage *)image
{
    [self.photoListView setCurrentImage:image];
}

- (void)saveCurrentImage
{
    UIImage *currentImage = [self.photoListView currentImage];
    if (currentImage != nil ) {
        ALAssetsLibrary *assetLibrary = [[ALAssetsLibrary alloc] init];
        __weak __typeof__(self) wself = self;
        [assetLibrary enumerateGroupsWithTypes:ALAssetsGroupSavedPhotos usingBlock:^(ALAssetsGroup *group, BOOL *stop) {
            if (group)
            {
                [assetLibrary writeImageToSavedPhotosAlbum:currentImage.CGImage orientation:ALAssetOrientationUp completionBlock:^(NSURL *assetURL, NSError *error) {
                    if (error) {
                        NSString *tips =@"保存图片失败";
                        [wself.view makeToast:tips];             }
                    else
                    {
                        NSString *tips =@"保存图片成功";
                        [wself.view makeToast:tips];
                    }
                }];
                *stop = YES;
            }
        } failureBlock:^(NSError *error) {
            switch (error.code) {
                case ALAssetsLibraryAccessUserDeniedError:
                case ALAssetsLibraryAccessGloballyDeniedError:
                {
                    NSString *title, *message, *btnOk;
                    title = @"无法访问相册";
                    message = @"需要在隐私中启用";
                    btnOk =@"我知道了";
                    
                    
                    YYAlertView *alertView = [[YYAlertView alloc] initWithTitle:title message:message];
                    [alertView addButtonWithTitle:btnOk
                                            block:nil];
                    [alertView show];
                    break;
                }
                    
                default:
                {
                    NSString *tips = @"保存图片失败";
                    [wself.view makeToast:tips];
                    break;
                }
            }
        }];
    }
}


#pragma mark YYPhotoListViewDelegate

- (void)yyphotolistView:(YYPhotoListView *)yyphotoListView page:(NSInteger)page allPage:(NSInteger)allPage
{
    self.pageLabel.text = [NSString stringWithFormat:@"%@/%@",@(page).stringValue,@(allPage).stringValue];
}

- (void)yyphotolistView:(YYPhotoListView *)yyphotoListView didSelecteIndex:(NSInteger)index
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setPageHidden:(BOOL)pageHidden
{
    _pageHidden = pageHidden;
}
@end
