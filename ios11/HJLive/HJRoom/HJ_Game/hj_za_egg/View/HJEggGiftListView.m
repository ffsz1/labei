//
//  HJEggGiftListView.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJEggGiftListView.h"

#import "HJEggGiftCCell.h"

#import "HJHttpRequestHelper+Egg.h"

@implementation HJEggGiftListView

+ (void)showGift
{
    [HJEggGiftListView loadXib];
}

+ (void)loadXib
{
    HJEggGiftListView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJEggGiftListView" owner:self options:nil].lastObject;
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    shareView.backgroundColor = [UIColor clearColor];
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    [shareView show];
    
    
    CGFloat height = - 470;
    shareView.bottom_BgView.constant = height;
    
    //展示底部上浮动画
    [shareView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
            shareView.bottom_BgView.constant = -12;
        [shareView layoutIfNeeded];
    }];
    
    //展示底部下沉动画
    __weak typeof(shareView)weakView = shareView;
    shareView.dismissBlock = ^{
        [weakView layoutIfNeeded];
        [UIView animateWithDuration:0.3 animations:^{
            
                weakView.bottom_BgView.constant = height;
            
            [weakView layoutIfNeeded];
        }];
    };
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJEggGiftCCell" bundle:[NSBundle mainBundle]] forCellWithReuseIdentifier:@"HJEggGiftCCell"];
    [self getData];
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake((kScreenWidth-40)/3, 120);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(6.5+10, 20, 0, 20);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 0;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 0;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    HJEggGiftCCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJEggGiftCCell" forIndexPath:indexPath];
    cell.info = self.dataArr[indexPath.item];
    return cell;
}

- (void)getData
{
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper getPrizePoolGift:^(NSArray * _Nonnull arr) {
        weakSelf.dataArr = arr;
        [weakSelf.collectionView reloadData];
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        
    }];
}


- (IBAction)closeAction:(id)sender {
    
    [self dismiss];
    
}



@end
