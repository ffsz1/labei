//
//  HJPersonGiftViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPersonGiftViewController.h"
#import "HJGiftReceiveCollectionViewCell.h"
#import "UserGift.h"
#import "HJUserCoreClient.h"
#import "HJUserCoreHelp.h"
#import "UIView+XCToast.h"

@interface HJPersonGiftViewController ()<HJUserCoreClient>

@property (nonatomic, strong) NSMutableArray *userGiftList;

@end

@implementation HJPersonGiftViewController

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AddCoreClient(HJUserCoreClient, self);
    
    self.collectionView.contentInset = UIEdgeInsetsMake(0, 0, XC_Height_TabBar, 0);
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJGiftReceiveCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HJGiftReceiveCollectionViewCell"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setupscrollDirection:(UICollectionViewScrollDirection)scrollDirection {
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.scrollDirection = scrollDirection;
    [self.collectionView setCollectionViewLayout:layout];
}

- (void)updateData {
    if (!self.userGiftList.count) {
        [GetCore(HJUserCoreHelp) getReceiveGift:self.userID orderType:2 type:0];
    }
}

#pragma mark - UserCoreClient
- (void)onGetReceiveGiftSuccess:(NSArray *)userGiftList uid:(UserID)uid type:(NSInteger)type {
    
    if (type == self.headerSelectType) {
        
        if (uid == self.userID) {
            
            self.userGiftList = [NSMutableArray arrayWithArray:userGiftList];
            [self.collectionView reloadData];
        }
        
        if (!self.userGiftList.count) {
            [self.collectionView showEmptyContentToastWithTitle:self.headerSelectType == 1 ? @"还木有收到神秘礼物>_< 赶紧收集吧~" : @"还木有收到礼物>_< 赶紧收集吧~" andImage:[UIImage imageNamed:@"blank"]];
        }
        else {
            [self.collectionView hideToastView];
        }
    }
    
}

- (void)onGetReceiveGiftFailth:(NSString *)message type:(NSInteger)type {
    if (type == self.headerSelectType) {
        if (!self.userGiftList.count) {
            [self.collectionView showEmptyContentToastWithTitle:self.headerSelectType == 1 ? @"还木有收到神秘礼物>_< 赶紧收集吧~" : @"还木有收到礼物>_< 赶紧收集吧~" andImage:[UIImage imageNamed:@"blank"]];
        }
    }
}


#pragma mark <UICollectionViewDataSource>

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.userGiftList.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HJGiftReceiveCollectionViewCell * cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"HJGiftReceiveCollectionViewCell" forIndexPath:indexPath];
    cell.giftImage.contentMode = UIViewContentModeScaleAspectFit;
    UserGift *gift = [self.userGiftList safeObjectAtIndex:indexPath.row];
    if (gift.picUrl) {
        [cell.giftImage qn_setImageImageWithUrl:gift.picUrl placeholderImage:default_bg type:ImageTypeRoomGift];
    }else {
        [cell.giftImage setImage:[UIImage imageNamed:default_bg]];
    }
    cell.giftName.text = gift.giftName;
    cell.giftNumber.text = gift.reciveCount;
    return cell;
}


#pragma mark <UICollectionViewDelegateFlowLayout>

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
//    return CGSizeMake(floor((XC_SCREE_W - 70.f) * 0.2f), floor((XC_SCREE_W - 70.f) * 0.2f) + 27);
    CGFloat w = (XC_SCREE_W-30 - 10*3)/4;
    return CGSizeMake(w, w+20);
}
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return CGFLOAT_MIN;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return 10;
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsMake(0, 15, 0, 15);
}
@end

