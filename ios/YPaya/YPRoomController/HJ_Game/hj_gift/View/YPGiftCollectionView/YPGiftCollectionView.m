//
//  YPGiftCollectionView.m
//  HJLive
//
//  Created by apple on 2019/7/10.
//

#import "YPGiftCollectionView.h"

#import "YPGiftCell.h"

#import "YPGiftInfo.h"

#import "YPGiftCore.h"
#import "HJGiftCoreClient.h"


@interface YPGiftCollectionView ()<UICollectionViewDelegate,UICollectionViewDataSource>

@property (strong,nonatomic) UICollectionView *collectionView;

@property (nonatomic,strong) NSMutableArray *giftArr;

@property (nonatomic,strong) NSMutableArray *selArr;

@property (nonatomic,strong) YPGiftInfo *selModel;


@end

@implementation YPGiftCollectionView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
        AddCoreClient(HJGiftCoreClient, self);

    }
    return self;
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)setUI
{
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.mas_equalTo(self);
    }];
}

#pragma mark <UICollectionViewDelegate,UICollectionViewDataSource>
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.giftArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPGiftCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGiftCell" forIndexPath:indexPath];
    
    
    if (indexPath.item<self.giftArr.count) {
        YPGiftInfo *info = self.giftArr[indexPath.item];
        cell.giftModel = info;
        
        BOOL isSel = [self.selArr[indexPath.item] boolValue];
        [cell setSelStytle:isSel];
    }
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    BOOL isSel = [self.selArr[indexPath.item] boolValue];
    
    [self resetSelArr:NO];
    [self.selArr replaceObjectAtIndex:indexPath.item withObject:@(YES)];
    
    YPGiftInfo *info = self.giftArr[indexPath.item];
    self.selModel = info;
    
    [self.collectionView reloadData];

}

//全部选中、取消选中
- (void)resetSelArr:(BOOL)setSel
{
    for (int i =0; i<self.selArr.count; i++) {
        [self.selArr replaceObjectAtIndex:i withObject:@(setSel)];
    }
}

#pragma mark HJGiftCoreClient

- (void)onGiftIsRefresh {
    self.type = self.type;
    
//    [self getArrData];
//    [self.collectionView reloadData];
}

- (void)updateGiftList:(NSMutableArray *)giftInfos {
//    self.type = self.type;
    
    [self getArrData];
    [self.collectionView reloadData];
    
    
    //检测是否已经送完礼物，如果是重置选中
    BOOL hasGift = NO;
    for (int i = 0; i<self.giftArr.count; i++) {
        YPGiftInfo *info = self.giftArr[i];
        
        if (info.giftId == self.selModel.giftId) {
            hasGift = YES;
        }
    }
    if (!hasGift) {
        [self resetSelArr];
    }
    
    
    
}

- (void)resetSelArr
{
    self.selArr = [[NSMutableArray alloc] init];
    for (int i =0; i<self.giftArr.count; i++) {
        [self.selArr addObject:@(NO)];
    }
    
    if (self.selArr.count>0) {
        [self.selArr replaceObjectAtIndex:0 withObject:@(YES)];
        YPGiftInfo *info = self.giftArr[0];
        self.selModel = info;
    }else{
        self.selModel = nil;
    }
    
    [self.collectionView reloadData];
}

- (YPGiftInfo *)getGiftModel
{
    return self.selModel;
}

- (void)getArrData
{
    if (_type == XBDGiftBoxTypeNormal) {
        self.giftArr = [GetCore(YPGiftCore) getGameRoomGift];
    }
    
    if (_type == XBDGiftBoxTypeBag) {
        self.giftArr = [GetCore(YPGiftCore) getMysticGift];
        //计算总背包价值
              for (YPGiftInfo* model in self.giftArr) {
                  self.allBagNum = self.allBagNum + model.goldPrice* model.userGiftPurseNum;
                  
              }
              if (_allBagNumberBlack) {
                  _allBagNumberBlack(self.allBagNum);
                  self.allBagNum = 0;
              }
    }
    
    if (_type == XBDGiftBoxTypePoint) {
        self.giftArr = [GetCore(YPGiftCore) getDiandianGift];
    }
     //排序gift数组
      
    [self.giftArr sortUsingComparator:^NSComparisonResult(YPGiftInfo* _Nonnull obj1, YPGiftInfo* _Nonnull obj2)
    {
        if (obj1.goldPrice < obj2.goldPrice){
            return NSOrderedAscending;
        }else{
            return NSOrderedDescending;
        }
    }];
}

#pragma mark setter/getter
- (void)setType:(XBDGiftBoxType)type
{
    _type = type;
    
    [self getArrData];
    
//    if (_type == XBDGiftBoxTypeNormal) {
//        self.giftArr = [GetCore(YPGiftCore) getGameRoomGift];
//    }
//
//    if (_type == XBDGiftBoxTypeBag) {
//        self.giftArr = [GetCore(YPGiftCore) getMysticGift];
//    }
//
//    if (_type == XBDGiftBoxTypePoint) {
//        self.giftArr = [GetCore(YPGiftCore) getDiandianGift];
//    }
    
    [self resetSelArr];
}

- (UICollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        
        layout.itemSize = CGSizeMake((kScreenWidth -13*2-7*3)/4, 90);
        layout.minimumInteritemSpacing = 7;
        layout.minimumLineSpacing = 13;
//        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        
        layout.sectionInset = UIEdgeInsetsMake(0, 13, 0, 13);
        
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
//        _collectionView.pagingEnabled = YES;
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        [self addSubview:_collectionView];
        [_collectionView registerNib:[UINib nibWithNibName:@"YPGiftCell" bundle:nil] forCellWithReuseIdentifier:@"YPGiftCell"];
        
        
        _collectionView.backgroundColor = [UIColor clearColor];
        
    }
    return _collectionView;
}


@end
