//
//  conchWinningView.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YPConchWinCollectionCell.h"
#import "YPConchWinningView.h"
#import "YPConchWinModel.h"
@interface YPConchWinningView()<UICollectionViewDelegate,
UICollectionViewDataSource,
UICollectionViewDelegateFlowLayout>

@end

@implementation YPConchWinningView

+ (void)showCall:(TapConchWinTypeBlock)tapConchWinTypeBlock{
    
    
    
    
}





#pragma mark - UICollectionViewDelegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.tagList.count;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    YPConchWinModel *tag = [self.tagList safeObjectAtIndex:indexPath.row];
    
    [self.conchWinCollectionView reloadData];
}

#pragma mark - UICollectionViewDataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    YPConchWinModel *tag = [self.tagList safeObjectAtIndex:indexPath.row];
    YPConchWinCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPConchWinCollectionCell" forIndexPath:indexPath];
    cell.desLabel.text = tag.name;
   
    
//    if (tag.id == selectedChildenId) {
//        cell.bottomView.backgroundColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#9F62FB" alpha:1.0];
//        cell.bottomView.layer.borderWidth = 0;
//        cell.roomTagLabel.textColor = UIColorHex(ffffff);
//    }else {
//        cell.roomTagLabel.textColor = UIColorHex(FFFFFF);
//        cell.bottomView.backgroundColor = UIColorHex(E5E5E5);
//        cell.bottomView.layer.borderWidth = 0.5;
//        cell.bottomView.layer.borderColor = UIColorHex(E5E5E5).CGColor;
//    }
    return cell;
}

#pragma mark - UICollectionViewDelegateFlowLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(60, 34);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 10.0;
}
@end
