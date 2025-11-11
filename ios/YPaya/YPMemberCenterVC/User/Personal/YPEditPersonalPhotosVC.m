//
//  YPEditPersonalPhotosVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPEditPersonalPhotosVC.h"
#import "YPPersonalPhotoCell.h"
#import "UIImageView+YYWebImage.h"
#import "YPImagePickerUtils.h"

#import "YPUserCoreHelp.h"
#import "YPFileCore.h"
#import "HJFileCoreClient.h"
#import "HJAuthCoreClient.h"
#import "HJUserCoreClient.h"
#import "YPUserPhoto.h"
#import "YPAuthCoreHelp.h"
#import "YPSDPhotoBrowser.h"
#import "YPYYAlertView.h"

@interface YPEditPersonalPhotosVC ()<UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, UIImagePickerControllerDelegate, UINavigationControllerDelegate, HJFileCoreClient, HJPersonalPhotoCellDelegate, HJUserCoreClient, SDPhotoBrowserDelegate>
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) NSMutableArray<YPUserPhoto *> *photos;
@property (strong, nonatomic) UserInfo *userinfo;
@property (assign, nonatomic) BOOL isEditing;
@property (assign, nonatomic) NSIndexPath *indexPath;
@property (assign, nonatomic) NSInteger successCout;
@end

@implementation YPEditPersonalPhotosVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJUserCoreClient, self);
//    self.userinfo = [GetCore(UserCore) getUserInfo:[GetCore(AuthCore) getUid].userIDValue refresh:YES];
    
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPPersonalPhotoCell" bundle:nil] forCellWithReuseIdentifier:@"YPPersonalPhotoCell"];
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPPersonalPhotoCell" bundle:nil] forCellWithReuseIdentifier:@"HJPersonalPhotoCell2"];
    [self initView];
    self.navigationItem.leftBarButtonItem.tintColor = [UIColor blackColor];

}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    YPBlackStatusBar
}


- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (BOOL)isOwenr {
    return [GetCore(YPAuthCoreHelp) getUid].userIDValue == self.uid;
}

- (void)initView {
    
    if ([self isOwenr]) {
        
        self.title = NSLocalizedString(XCPhotosTitle, nil);
        UIBarButtonItem *rightButtonItem = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(XCPhotosEditTitle, nil) style:UIBarButtonItemStylePlain target:self action:@selector(onRightBtnClicked)];
        self.navigationItem.rightBarButtonItem = rightButtonItem;
    }
    else {
        self.title = @"Ta的相册";
    }
    self.automaticallyAdjustsScrollViewInsets = NO;
    @weakify(self)
    [GetCore(YPUserCoreHelp)getUserInfo:self.uid refresh:YES success:^(UserInfo *info) {
        @strongify(self)
        self.userinfo = info;
        [self.collectionView reloadData];
        [MBProgressHUD hideHUD];
    }];
}

- (void)onRightBtnClicked {
    if (self.isEditing) {
        self.isEditing = NO;
        self.navigationItem.rightBarButtonItem.title = NSLocalizedString(XCPhotosEditTitle, nil);
    }else {
        self.isEditing = YES;
        self.navigationItem.rightBarButtonItem.title = NSLocalizedString(XCAlertDone, nil);
    }
    

    [self.collectionView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - PhotoSelectedController
-(void)showPhotosPickerController {
    YPYYActionSheetViewController *actionSheet = [YPImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:nil view:self.view tailor:NO delegate:self];
//    title:NSLocalizedString(XCPhotosAddPhotos, nil)
    [actionSheet show];

}

#pragma mark - UICollectionViewDelegate

#pragma mark - UICollectionViewDataSource
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
//    return self.photos.count + 1;
    return [self isOwenr] ? self.userinfo.privatePhoto.count + 1 : self.userinfo.privatePhoto.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    if ([self isOwenr]) {
        
        if (indexPath.row == 0) {
            YPPersonalPhotoCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJPersonalPhotoCell2" forIndexPath:indexPath];
            cell.personalPhotoImagwView.image = [UIImage imageNamed:@"yp_add_photo_logo"];
            [cell.deleteBtn setHidden:YES];
            return cell;
        } else if (indexPath.item > 0) {
            
            YPPersonalPhotoCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPPersonalPhotoCell" forIndexPath:indexPath];
            
            if (self.userinfo.privatePhoto.count > 0) {
                [cell.personalPhotoImagwView qn_setImageImageWithUrl:self.userinfo.privatePhoto[indexPath.item - 1].photoUrl placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
            }
            
            cell.indexPath = indexPath;
            cell.delegate = self;
            if (self.isEditing) {
                [cell.deleteBtn setHidden:NO];
            }else {
                [cell.deleteBtn setHidden:YES];
            }
            return cell;
            
        }
    }
    else {
        YPPersonalPhotoCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPPersonalPhotoCell" forIndexPath:indexPath];
        
        if (self.userinfo.privatePhoto.count > 0) {
            [cell.personalPhotoImagwView qn_setImageImageWithUrl:self.userinfo.privatePhoto[indexPath.item].photoUrl placeholderImage:default_bg type:ImageTypeUserIcon];
        }
        
        cell.indexPath = indexPath;
        cell.delegate = self;
        if (self.isEditing) {
            [cell.deleteBtn setHidden:NO];
        }else {
            [cell.deleteBtn setHidden:YES];
        }
        return cell;
    }
    YPPersonalPhotoCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPPersonalPhotoCell" forIndexPath:indexPath];
    return cell;
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((self.view.frame.size.width - 50) / 3, (self.view.frame.size.width - 50) / 3);
}

#pragma mark - UICollectionViewDelegate

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
    if ([self isOwenr]) {
        
        self.indexPath = indexPath;
        if (indexPath.row == 0) {
            if (self.userinfo.privatePhoto.count < 50) {
                [self showPhotosPickerController];
                [self cancelEditing];
            }else{
                [MBProgressHUD showError:@"最多只能上传50张照片"];
            }
        }else {
            if (!self.isEditing) {
                YPSDPhotoBrowser *browser = [[YPSDPhotoBrowser alloc]init];
                browser.sourceImagesContainerView = self.collectionView;
                browser.delegate = self;
                browser.imageCount = self.userinfo.privatePhoto.count;
                browser.currentImageIndex = indexPath.item - 1;
                [browser show];
            }
        }
    }
    else {
        YPSDPhotoBrowser *browser = [[YPSDPhotoBrowser alloc]init];
        browser.sourceImagesContainerView = self.collectionView;
        browser.delegate = self;
        browser.imageCount = self.userinfo.privatePhoto.count;
        browser.currentImageIndex = indexPath.item;
        [browser show];
    }
}

#pragma mark - SDPhotoBrowserDelegate

- (UIImage *)photoBrowser:(YPSDPhotoBrowser *)browser placeholderImageForIndex:(NSInteger)index {
    return [UIImage imageNamed:default_bg];
}

- (NSURL *)photoBrowser:(YPSDPhotoBrowser *)browser highQualityImageURLForIndex:(NSInteger)index {
    return [NSURL URLWithString:self.userinfo.privatePhoto[index].photoUrl];
}


#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{

    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerOriginalImage];
        if (selectedPhoto) {
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }

//            [GetCore(FileCore) uploadImage:selectedPhoto];
            [GetCore(YPFileCore) qiNiuUploadImage:selectedPhoto uploadType:UploadImageTypeLibary];
            [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        }
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:^{
        
    }];
}



#pragma mark - FileCoreClient

- (void)onUploadImageSuccess {
    
}

- (void)onUploadImageFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

#pragma mark - FileCoreClient 7N
- (void)didUploadPhotoImageSuccessUseQiNiu:(NSString *)key{
    NSLog(@"didUploadPhotoImageSuccessUseQiNiu");
    NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
    [GetCore(YPUserCoreHelp) uploadImageUrlToServer:url];
}
-(void)didUploadPhotoImageFailUseQiNiu:(NSString *)message{
    NSLog(@"didUploadPhotoImageFailUseQiNiu:%@",message);
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}


#pragma mark - UserCore

- (void)onUploadImageUrlToServerFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

- (void)onUploadImageUrlToServerSuccess {
    [self updateTheUserInfo];
    [MBProgressHUD showSuccess:NSLocalizedString(XCPhotosUploadPhotosSuccess, nil)];
}

- (void)deleteImageToServerSuccess {
    [self updateTheUserInfo];
//    [MBProgressHUD showSuccess:@"删除成功"];
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
}

- (void)deleteImageUrlToServerFailth:(NSString *)message {
//    [MBProgressHUD showError:message];
    [MBProgressHUD hideHUD];
}

#pragma mark - HJPersonalPhotoCellDelegate
- (void)deletePhoto:(NSIndexPath *)indexPath {
    YPYYAlertView *alertView = [[YPYYAlertView alloc]initWithTitle:NSLocalizedString(XCPhotosDeleteTitle, nil) message:NSLocalizedString(XCPhotosDeleteMesseage, nil)];
    
    [alertView addButtonWithTitle:NSLocalizedString(XCRoomCancel, nil) block:nil];
    [alertView addButtonWithTitle:NSLocalizedString(XCRoomConfirm, nil) block:^{
        [self deletePhotoWithIndexPath:indexPath];
    }];
    [alertView show];
    
}

#pragma mark - Private Method

- (void)cancelEditing {
    self.navigationItem.rightBarButtonItem.title = NSLocalizedString(XCPhotosEditTitle, nil);
    self.isEditing = NO;
    [self.collectionView reloadData];
}

- (void)updateTheUserInfo  {
//    [GetCore(UserCore) getUserInfo:[GetCore(AuthCore) getUid].userIDValue refresh:YES];
    [GetCore(YPUserCoreHelp) getUserInfo:[GetCore(YPAuthCoreHelp) getUid].userIDValue refresh:YES success:^(UserInfo *info) {
        self.userinfo = info;
        [self.collectionView reloadData];
        [MBProgressHUD hideHUD];
    }];
}

- (void)deletePhotoWithIndexPath:(NSIndexPath *)indexPath {
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    NSString *pid = self.userinfo.privatePhoto[indexPath.item - 1].pid;
    [GetCore(YPUserCoreHelp) deleteImageUrlToServerWithPid:pid];

}

@end
