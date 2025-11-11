//
//  YPGiftViewContainer.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGiftViewContainer.h"
#import "YPGiftCore.h"
#import "YPGiftInfo.h"
#import "YPGiftCollectionViewCell.h"
#import "UIImageView+YYWebImage.h"
#import "YPYYDefaultTheme.h"
#import "HJPurseCoreClient.h"
#import "YPPurseCore.h"

@interface YPGiftViewContainer()<UICollectionViewDelegate, UICollectionViewDataSource, HJPurseCoreClient>
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UIButton *rechargeBtn;
@property (weak, nonatomic) IBOutlet UILabel *balanceCornLabel;
@property (weak, nonatomic) IBOutlet UIButton *sendBtn;
@property (weak, nonatomic) IBOutlet UIView *containerBg;

@property (nonatomic, strong) NSMutableArray *giftInfos;
- (IBAction)onRechageBtnClicked:(id)sender;
- (IBAction)onSendGiftBtnClicked:(id)sender;
@end

@implementation YPGiftViewContainer
{
    NSInteger _currentIndex;
}
+ (instancetype)loadFromNIB
{
    return [[NSBundle mainBundle] loadNibNamed:@"YPGiftViewContainer" owner:nil options:nil].firstObject;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    AddCoreClient(HJPurseCoreClient, self);
    [self initView];
}

- (void)dealloc
{
    RemoveCoreClient(HJPurseCoreClient, self);
}

- (void)initView
{
    self.rechargeBtn.layer.masksToBounds = YES;
    self.rechargeBtn.layer.borderWidth = 0.5f;
    self.rechargeBtn.layer.borderColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FED700" alpha:1.0f].CGColor;
    
    self.balanceCornLabel.text = [NSString stringWithFormat:@"%@金币",GetCore(YPPurseCore).balanceInfo.goldNum];
//    NSLog(@"%@",GetCore(YPPurseCore).balanceInfo.goldNum);
    
    [self.collectionView setDelegate:self];
    [self.collectionView setDataSource:self];
    
    [self.collectionView registerNib:[UINib nibWithNibName:@"YPGiftCollectionViewCell" bundle:[NSBundle mainBundle]] forCellWithReuseIdentifier:@"YPGiftCollectionViewCell"];
    
    self.giftInfos = [GetCore(YPGiftCore) getNormalRoomGift];
    [self.collectionView reloadData];
    self.containerBg.userInteractionEnabled = YES;
    UITapGestureRecognizer* rec = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onContainerBgClicked:)];
    [self.containerBg addGestureRecognizer:rec];
}

- (void)onContainerBgClicked:(UITapGestureRecognizer *)rec
{
    
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.giftInfos.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier = @"YPGiftCollectionViewCell";
    YPGiftCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    YPGiftInfo *info = [self.giftInfos safeObjectAtIndex:indexPath.row];
    [cell.giftLogo yy_setImageWithURL:[NSURL URLWithString:info.giftUrl] placeholder:nil];
    cell.gitftNameLabel.text = info.giftName;
    cell.giftPriceLabel.text = [NSString stringWithFormat:@"%ld金币",info.goldPrice];
    cell.collectionViewBg.layer.masksToBounds = YES;
    cell.collectionViewBg.layer.cornerRadius = 5.0f;
    if (indexPath.row == _currentIndex) {
        cell.collectionViewBg.layer.borderWidth = 0.5f;
        cell.collectionViewBg.layer.borderColor = [[YPYYDefaultTheme defaultTheme] colorWithHexString:@"#FED700" alpha:1.0f].CGColor;
    } else {
        cell.collectionViewBg.layer.borderWidth = 0;
        cell.collectionViewBg.layer.borderColor = [UIColor clearColor].CGColor;
    }
    
    cell.collectionViewBg.userInteractionEnabled = YES;
    cell.collectionViewBg.tag = indexPath.row;
    UITapGestureRecognizer *rec = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onCollectionViewBgClicked:)];
    [cell.collectionViewBg addGestureRecognizer:rec];
    return cell;
}

- (void) onCollectionViewBgClicked:(UITapGestureRecognizer *)rec
{
    _currentIndex = rec.view.tag;
    [self.collectionView reloadData];
}

#pragma mark - HJPurseCoreClient
- (void)onBalanceInfoUpdate:(YPBalanceInfo *)balanceInfo
{
    self.balanceCornLabel.text = [NSString stringWithFormat:@"%@金币",GetCore(YPPurseCore).balanceInfo.goldNum];
}

- (IBAction)onRechageBtnClicked:(id)sender {
    if (self.delegate != nil) {
        [self.delegate onHJGiftViewContainerRechargeClicked];
    }
}

- (IBAction)onSendGiftBtnClicked:(id)sender {
    if (self.delegate != nil) {
        self.giftInfos = [GetCore(YPGiftCore) getNormalRoomGift];
        if (self.giftInfos.count != 0 && self.giftInfos != nil) {
            [self.delegate onSendGiftClicked:[self.giftInfos safeObjectAtIndex:_currentIndex]];
        }
    }
}
@end
