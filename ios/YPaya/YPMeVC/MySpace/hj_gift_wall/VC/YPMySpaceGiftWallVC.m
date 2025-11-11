//
//  YPMySpaceGiftWallVC.m
//  HJLive
//
//  Created by feiyin on 2020/5/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMySpaceGiftWallVC.h"

#import "YPMySpaceGiftCCell.h"

#import "YPHttpRequestHelper+User.h"
#import "UIView+XCToast.h"

#import "YPEmptyView.h"

#import "YPHttpRequestHelper+CarSys.h"

#import "YPMySpaceCarModel.h"
#import "YPMySpaceHeadWearModel.h"

@interface YPMySpaceGiftWallVC ()<UICollectionViewDelegateFlowLayout,UIScrollViewDelegate,UICollectionViewDataSource>

@property (nonatomic,strong) NSArray *dataArr;

@property (nonatomic,strong) YPEmptyView *tipView;

@property (nonatomic,strong) UICollectionReusableView *footerView;

@property (nonatomic,strong) NSArray *carArr;
@property (nonatomic,strong) NSArray *headArr;


@end

@implementation YPMySpaceGiftWallVC


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPMySpaceGiftCCell" bundle:nil] forCellWithReuseIdentifier:@"YPMySpaceGiftCCell"];
    
    self.collectionView.backgroundColor = [UIColor whiteColor];
    
    self.collectionView.showsHorizontalScrollIndicator = NO;
    self.collectionView.showsVerticalScrollIndicator = NO;
    
    self.canScroll = NO;
    
//    self.collectionView.bounces = NO;
    
//    self.collectionView.scrollEnabled = NO;
    
}

#pragma mark - <UIScrollViewDelegate>
-(void)scrollViewDidScroll:(UIScrollView *)scrollView {
    if (scrollView == self.collectionView) {
        CGFloat offsetY = scrollView.contentOffset.y;
        
        if (!self.canScroll) {
            self.collectionView.contentOffset = CGPointZero;
        }
        
        if (self.isShow) {
            if (offsetY <= 0) {
                self.canScroll = NO;
                self.collectionView.contentOffset = CGPointZero;
                
                if (self.updateScrollBlock) {
                    self.updateScrollBlock();
                }
            }
        }
        
    }
}

#pragma mark <UICollectionViewDataSource>

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    
    if (self.type == 1) {
        return self.headArr.count;
    }
    
    if (self.type == 2) {
        return self.carArr.count;
    }
    
    return self.dataArr.count;
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(15, 30, 15, 30);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake((kScreenWidth -90)/3, 130);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 15;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 15;
}


- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    YPMySpaceGiftCCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPMySpaceGiftCCell" forIndexPath:indexPath];
    
    if (self.type == 0) {
        YPUserGift *model = self.dataArr[indexPath.item];
        [cell.logoImageView qn_setImageImageWithUrl:model.picUrl placeholderImage:placeholder_image_square type:ImageTypeRoomGift];
        cell.numLabel.text = [NSString stringWithFormat:@"x %@",model.reciveCount];
        cell.giftNameLabel.text = model.giftName;
    }
    
    if (self.type == 1) {
        YPMySpaceHeadWearModel *model = self.headArr[indexPath.item];
        [cell.logoImageView qn_setImageImageWithUrl:model.picUrl placeholderImage:placeholder_image_square type:ImageTypeRoomGift];
        cell.numLabel.text = @"";
        cell.giftNameLabel.text = model.headwearName;
    }
    
    if (self.type == 2) {
        YPMySpaceCarModel *model = self.carArr[indexPath.item];
        [cell.logoImageView qn_setImageImageWithUrl:model.picUrl placeholderImage:placeholder_image_square type:ImageTypeRoomGift];
        cell.numLabel.text = @"";
        cell.giftNameLabel.text = model.carName;
    }
    return cell;
}


- (void)getData
{
    [YPHttpRequestHelper getReceiveGift:self.userID orderType:2 type:0 success:^(NSArray * userGiftList){
        

        self.dataArr = userGiftList;
        [self.collectionView reloadData];
        
        
        //累计总数
        NSInteger allCout = 0;
        for (int i = 0; i<userGiftList.count; i++) {
            YPUserGift *model = userGiftList[i];
            allCout = allCout+[model.reciveCount integerValue];
        }
        
        
        if (self.updateBlock) {
            self.updateBlock(allCout,self.dataArr.count);
        }
        
        if (self.dataArr.count==0) {
            
            self.tipView.hidden = NO;
            
        }else{
            
            if (self.tipView) {
                [self.tipView removeAllSubviews];
            }
        }
        
        [self updateContentInset:userGiftList];
        
        
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
        if (self.updateBlock) {
            self.updateBlock(0,0);
        }
        
        
        [self setNodataView:self.dataArr];

    }];
}

//补空白，解决空页面不能滚动问题
- (void)updateContentInset:(NSArray *)userGiftList
{
    NSInteger row = (userGiftList.count/3+(userGiftList.count%3>0?1:0));
    
    
    
    CGFloat height = row*130 + (row-1)*15;
    CGFloat cellHeight = (kScreenHeight);
    
    if (row == 0) {
        height = 0;
    }
    
    //通过EdgeInsets补白
    if (height<= cellHeight) {
        self.collectionView.contentInset = UIEdgeInsetsMake(0, 0, cellHeight-height, 0);
    }else{
        //高度超过一页，预留72+15，不被私聊按钮挡住
        self.collectionView.contentInset = UIEdgeInsetsMake(0, 0, 72+15, 0);
        
    }
}

- (void)setNodataView:(NSArray *)tagArr
{
    tagArr = nil;
    [self.collectionView reloadData];
    self.tipView.hidden = NO;
    
    self.collectionView.contentInset = UIEdgeInsetsMake(0, 0, 0, 0);
}

- (void)getCarListData
{
    __weak typeof(self)weakSelf = self;

    [YPHttpRequestHelper getUserCarList:self.userID PageNum:@"1" PageSize:@"50" success:^(NSArray *list) {
        
        if (list.count>0) {
            NSMutableArray *tmpArr = [[NSMutableArray alloc] init];
            for (NSDictionary *dict in list) {
                YPMySpaceCarModel *model = [YPMySpaceCarModel yy_modelWithJSON:dict];
                [tmpArr addObject:model];
            }
            weakSelf.carArr = tmpArr;
            [weakSelf.collectionView reloadData];
        }
        
        if (self.carArr.count==0) {
            
            self.tipView.hidden = NO;
            
        }else{
            
            if (self.tipView) {
                [self.tipView removeAllSubviews];
            }
        }
        
        [self updateContentInset:list];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        self.carArr = nil;
        [self.collectionView reloadData];
        self.tipView.hidden = NO;
        
        self.collectionView.contentInset = UIEdgeInsetsMake(0, 0, 0, 0);
    }];
}

- (void)getHeadListData
{
    
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper getUserHeadList:self.userID PageNum:@"1" PageSize:@"50" success:^(NSArray *list) {
        
        if (list.count>0) {
            NSMutableArray *tmpArr = [[NSMutableArray alloc] init];
            for (NSDictionary *dict in list) {
                YPMySpaceHeadWearModel *model = [YPMySpaceHeadWearModel yy_modelWithJSON:dict];
                [tmpArr addObject:model];
            }
            weakSelf.headArr = tmpArr;
            [weakSelf.collectionView reloadData];
        }
        
        if (self.headArr.count==0) {
            
            self.tipView.hidden = NO;
            
        }else{
            
            if (self.tipView) {
                [self.tipView removeAllSubviews];
            }
        }
        
        [self updateContentInset:list];

        
    } failure:^(NSNumber *resCode, NSString *message) {
        self.headArr = nil;
        [self.collectionView reloadData];
        self.tipView.hidden = NO;
        
        self.collectionView.contentInset = UIEdgeInsetsMake(0, 0, 0, 0);
    }];
}


- (void)setType:(NSInteger)type
{
    _type = type;
    
    if (type == 0) {
        self.isShow = YES;
        [self.tipView setTitle:@"还木有收到礼物>_< 赶紧收集吧~" image:@"blank"];

        [self getData];
    }
    
    if (type == 1) {
        self.isShow = NO;
        [self.tipView setTitle:@"您还没有头饰喔～" image:@"blank"];

        [self getHeadListData];
    }
    
    if (type == 2) {
        self.isShow = NO;
        [self.tipView setTitle:@"您还没有坐骑喔～" image:@"blank"];

        [self getCarListData];
    }
}

- (YPEmptyView *)tipView
{
    if (!_tipView) {
        _tipView = [[YPEmptyView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 428)];
        [_tipView setTitle:@"还木有收到礼物>_< 赶紧收集吧~" image:@"blank"];
        _tipView.hidden = YES;
        [self.collectionView addSubview:_tipView];
    }
    return _tipView;
}

- (void)setUserID:(UserID)userID
{
    _userID = userID;
}


- (UICollectionReusableView *)footerView
{
    if (!_footerView) {
        _footerView = [[UICollectionReusableView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight - 40)];
    }
    return _footerView;
}

@end
