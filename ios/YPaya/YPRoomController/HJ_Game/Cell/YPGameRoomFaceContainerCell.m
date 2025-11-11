//
//  YPGameRoomFaceContainerCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomFaceContainerCell.h"
#import "YPGameRoomFaceCell.h"
#import "YPFaceCore.h"
#import "RoomUIClient.h"


@interface YPGameRoomFaceContainerCell()<UICollectionViewDelegate, UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@end

@implementation YPGameRoomFaceContainerCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPGameRoomFaceCell" bundle:nil] forCellWithReuseIdentifier:@"YPGameRoomFaceCell"];
    
    // Initialization code
}

#pragma mark - UICollectionViewDelegate

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    YPFaceConfigInfo *info = self.faceInfos[indexPath.row];
    //
    //
    if (![GetCore(YPFaceCore) getShowingFace]) {
        [GetCore(YPFaceCore)sendFace:info];
        NotifyCoreClient(RoomUIClient, @selector(dismissFaceView), dismissFaceView);
    }

}

#pragma mark - UICollectionViewDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.faceInfos.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    YPGameRoomFaceCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGameRoomFaceCell" forIndexPath:indexPath];
    [self configureCell:cell forItemAtIndexPath:indexPath];
    return cell;
}

- (void)configureCell:(YPGameRoomFaceCell *)cell
   forItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPFaceConfigInfo *info = self.faceInfos[indexPath.row];
    

    //读取图片
    UIImage *face = [GetCore(YPFaceCore)findFaceIconImageById:info.id];
    
    [cell.faceImageView setImage:face];
    [cell.faceName setText:info.name];
    
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake([UIScreen mainScreen].bounds.size.width / 5, self.frame.size.height / 3);
}



@end
