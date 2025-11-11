
//
//  HJHomeMyFollowCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHomeMyFollowCell.h"
#import "HJHomeMyFollowCCell.h"

#import "HJMySpaceVC.h"

#import "Attention.h"

#import "UIView+getTopVC.h"

@implementation HJHomeMyFollowCell

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.myFollowArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    HJHomeMyFollowCCell *cell = (HJHomeMyFollowCCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HJHomeMyFollowCCell" forIndexPath:indexPath];
    
    Attention *model = self.myFollowArr[indexPath.item];
    cell.model = model;
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.item<self.myFollowArr.count) {
        Attention *model = self.myFollowArr[indexPath.item];
        HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
        vc.userID = model.uid;
        [[self topViewController].navigationController pushViewController:vc animated:YES];
    }
}



- (void)setMyFollowArr:(NSArray *)myFollowArr
{
    _myFollowArr = myFollowArr;
    
    [self.collectionView reloadData];
}


@end
