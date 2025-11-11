//
//  HJGameRoomFaceContainerCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomFaceContainerCell.h"
#import "HJGameRoomFaceCell.h"
#import "HJFaceCore.h"
#import "RoomUIClient.h"


@interface HJGameRoomFaceContainerCell()<UICollectionViewDelegate, UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@end

@implementation HJGameRoomFaceContainerCell

- (void)awakeFromNib {
    [super awakeFromNib];
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJGameRoomFaceCell" bundle:nil] forCellWithReuseIdentifier:@"HJGameRoomFaceCell"];
    
    // Initialization code
}

#pragma mark - UICollectionViewDelegate

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    HJFaceConfigInfo *info = self.faceInfos[indexPath.row];
    //
    //
    if (![GetCore(HJFaceCore) getShowingFace]) {
        [GetCore(HJFaceCore)sendFace:info];
        NotifyCoreClient(RoomUIClient, @selector(dismissFaceView), dismissFaceView);
    }

}

#pragma mark - UICollectionViewDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.faceInfos.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HJGameRoomFaceCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJGameRoomFaceCell" forIndexPath:indexPath];
    [self configureCell:cell forItemAtIndexPath:indexPath];
    return cell;
}

- (void)configureCell:(HJGameRoomFaceCell *)cell
   forItemAtIndexPath:(NSIndexPath *)indexPath
{
    HJFaceConfigInfo *info = self.faceInfos[indexPath.row];
    

    //读取图片
    UIImage *face = [GetCore(HJFaceCore)findFaceIconImageById:info.id];
    
    [cell.faceImageView setImage:face];
    [cell.faceName setText:info.name];
    
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake([UIScreen mainScreen].bounds.size.width / 5, self.frame.size.height / 3);
}



@end
