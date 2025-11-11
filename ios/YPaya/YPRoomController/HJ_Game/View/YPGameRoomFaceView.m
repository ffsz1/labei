//
//  YPGameRoomFaceView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomFaceView.h"
#import "YPGameRoomFaceCell.h"
#import "YPGameRoomFaceContainerCell.h"

#import "YPFaceCore.h"
#import "HJFaceSourceClient.h"

#import "YPFaceInfo.h"
#import "YPFaceConfigInfo.h"

#import "UIView+XCToast.h"

@interface YPGameRoomFaceView()
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

@implementation YPGameRoomFaceView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPGameRoomFaceView" owner:self options:nil].lastObject;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    AddCoreClient(HJFaceSourceClient, self);
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPGameRoomFaceCell" bundle:nil] forCellWithReuseIdentifier:@"YPGameRoomFaceCell"];
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPGameRoomFaceContainerCell" bundle:nil] forCellWithReuseIdentifier:@"YPGameRoomFaceContainerCell"];
    if (GetCore(YPFaceCore).isLoadFace) {
        [self loadFace];
    }else {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomSetupFace, nil) duration:3.0 position:(YYToastPosition)YYToastPositionBottomWithRecordButton];
        [GetCore(YPFaceCore) requestFaceJson];
    }
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)loadFace {
    self.faceInfos = [GetCore(YPFaceCore)getFaceInfos];
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
    YPGameRoomFaceContainerCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGameRoomFaceContainerCell" forIndexPath:indexPath];
    
    [self configureCell:cell forItemAtIndexPath:indexPath];
    
    
    return cell;
}

- (void)configureCell:(YPGameRoomFaceContainerCell *)cell
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
//- (void)configureCell:(YPGameRoomFaceCell *)cell
//   forItemAtIndexPath:(NSIndexPath *)indexPath
//{
//    YPFaceInfo *info = self.faceInfos[indexPath.row];
//    [cell.faceImageView qn_setImageImageWithUrl:info.facePicUrl placeholderImage:nil type:ImageTypeRoomGift];
//    [cell.faceName setText:info.faceName];
//}


@end
