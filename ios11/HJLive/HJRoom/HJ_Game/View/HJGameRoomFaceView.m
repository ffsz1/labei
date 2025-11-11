//
//  HJGameRoomFaceView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomFaceView.h"
#import "HJGameRoomFaceCell.h"
#import "HJGameRoomFaceContainerCell.h"

#import "HJFaceCore.h"
#import "HJFaceSourceClient.h"

#import "FaceInfo.h"
#import "HJFaceConfigInfo.h"

#import "UIView+XCToast.h"

@interface HJGameRoomFaceView()
<
    UICollectionViewDelegate,
    UICollectionViewDataSource,
    UICollectionViewDelegateFlowLayout,
    UIScrollViewDelegate,
    HJFaceSourceClient
>

@property (strong, nonatomic) NSMutableArray<NSMutableArray *> *faceInfos;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;

@end

@implementation HJGameRoomFaceView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"HJGameRoomFaceView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    AddCoreClient(HJFaceSourceClient, self);
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJGameRoomFaceCell" bundle:nil] forCellWithReuseIdentifier:@"HJGameRoomFaceCell"];
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJGameRoomFaceContainerCell" bundle:nil] forCellWithReuseIdentifier:@"HJGameRoomFaceContainerCell"];
    if (GetCore(HJFaceCore).isLoadFace) {
        [self loadFace];
    }else {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomSetupFace, nil) duration:3.0 position:(YYToastPosition)YYToastPositionBottomWithRecordButton];
        [GetCore(HJFaceCore) requestFaceJson];
    }
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)loadFace {
    self.faceInfos = [GetCore(HJFaceCore)getFaceInfos];
    self.pageControl.hidden = NO;
    self.pageControl.numberOfPages = self.faceInfos.count;
    [self.collectionView reloadData];
}

#pragma mark - FaceSourceClient

- (void)loadFaceSourceSuccess {
    [self loadFace];
}

#pragma mark - UICollectionViewDataSource

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return self.faceInfos.count;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView
     numberOfItemsInSection:(NSInteger)section
{
    return 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView
                  cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    HJGameRoomFaceContainerCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJGameRoomFaceContainerCell" forIndexPath:indexPath];
    
    [self configureCell:cell forItemAtIndexPath:indexPath];
    
    
    return cell;
}

- (void)configureCell:(HJGameRoomFaceContainerCell *)cell
   forItemAtIndexPath:(NSIndexPath *)indexPath
{
    cell.faceInfos = self.faceInfos[indexPath.section];
//    [cell.collectionView reloadData];
    
}

#pragma mark - UICollectionViewDelegate
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake([UIScreen mainScreen].bounds.size.width, 160);
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    // 获得页码
    CGFloat doublePage = scrollView.contentOffset.x / scrollView.bounds.size.width;
    int intPage = (int)(doublePage + 0.5);
    
    // 设置页码
    self.pageControl.currentPage = intPage;

}

//#pragma mark - private method
//- (void)configureCell:(HJGameRoomFaceCell *)cell
//   forItemAtIndexPath:(NSIndexPath *)indexPath
//{
//    FaceInfo *info = self.faceInfos[indexPath.row];
//    [cell.faceImageView qn_setImageImageWithUrl:info.facePicUrl placeholderImage:nil type:ImageTypeRoomGift];
//    [cell.faceName setText:info.faceName];
//}


@end
