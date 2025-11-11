
//
//  YPHomeMyFollowCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeMyFollowCell.h"
#import "YPHomeMyFollowCCell.h"

#import "YPMySpaceVC.h"

#import "YPAttention.h"

#import "UIView+getTopVC.h"

@implementation YPHomeMyFollowCell

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.myFollowArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPHomeMyFollowCCell *cell = (YPHomeMyFollowCCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"YPHomeMyFollowCCell" forIndexPath:indexPath];
    
    YPAttention *model = self.myFollowArr[indexPath.item];
    cell.model = model;
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.item<self.myFollowArr.count) {
        YPAttention *model = self.myFollowArr[indexPath.item];
        YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
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
