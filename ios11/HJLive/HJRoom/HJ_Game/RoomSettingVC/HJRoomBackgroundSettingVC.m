//
//  HJRoomBackgroundSettingVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomBackgroundSettingVC.h"

#import "HJRoomBgModel.h"

#import "HJRoomBackgroundSeetingCell.h"

#import "HJVersionCoreHelp.h"
#import "HJRoomCoreV2Help.h"
#import "HJImRoomCoreV2.h"

static const CGSize XCRoomBackgroundSettingDefaultImageSize = {750, 1334};

@interface HJRoomBackgroundSettingVC () <UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (nonatomic, strong) UICollectionView *collectionView;

@property (nonatomic, strong) __block NSMutableArray<HJRoomBgModel *> *items;
@property (nonatomic, assign) NSInteger selectedItemId;

@end

@implementation HJRoomBackgroundSettingVC

#pragma mark - Life cycle
- (void)dealloc {
    !self.didSelectedItemHandler ?: self.didSelectedItemHandler(self.selectedItemId);
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self commonInit];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setupNavigation];
    [self addControls];
    [self setupDatas];
    [self loadDatas];
    [self updateControls];
}

#pragma mark - Event

#pragma mark - <UICollectionViewDelegate && UICollectionViewDataSource && UICollectionViewDelegateFlowLayout>
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.items.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HJRoomBackgroundSeetingCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:HJRoomBackgroundSeetingCellID forIndexPath:indexPath];
    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    HJRoomBgModel *roomInfo = [self.items objectOrNilAtIndex:indexPath.row];
    self.selectedItemId = roomInfo.id;
    [self.collectionView reloadData];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat width = (collectionView.bounds.size.width - 18 * 2 - 10 *(3 - 1))/3;
    CGFloat height = XCRoomBackgroundSettingDefaultImageSize.height/XCRoomBackgroundSettingDefaultImageSize.width * width;
    
    return CGSizeMake(floor(width), height + 20);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 10.0;
}
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section{
    return 10.0;
}
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    return UIEdgeInsetsMake(10, 18, 0, 18);
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)setupDatas {
    self.selectedItemId = self.itemId;
}

- (void)clearDatas {
    [self.items removeAllObjects];
}

- (void)loadDatas {
    [self clearDatas];
    

        @weakify(self);
        [GetCore(HJRoomCoreV2Help) roomBgListWithRoomId:GetCore(HJImRoomCoreV2).currentRoomInfo.roomId success:^(NSArray *list) {
            @strongify(self);
            [self.items addObjectsFromArray:list];
            if (self.selectedItemId == 0) {
                [self.items addObject:[self getDefaultItem]];
            }
            
            
            if (self.items.count == 0) {
                [self loadSystemDatas];
            }
            
            [self updateControls];
        } failure:^(NSString *message) {
            @strongify(self);
            [self loadSystemDatas];
        }];
}

- (void)loadSystemDatas {
    [self.items addObject:[self getDefaultItem]];
}

- (HJRoomBgModel *)getDefaultItem {
    HJRoomBgModel *item = [HJRoomBgModel new];
    item.id = 0;
    item.picUrl = @"hj_room_bg_default";
    item.name = @"默认";
    return item;
}

- (void)updateControls {
    [self.collectionView reloadData];
}

- (void)setupNavigation {
    self.title = NSLocalizedString(XCRoomTopicBackgroud, nil);
}

- (void)commonInit {
    self.selectedItemId = 0;
}

- (void)configureCell:(HJRoomBackgroundSeetingCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    HJRoomBgModel *roomInfo = [self.items objectOrNilAtIndex:indexPath.row];
    BOOL selected = (self.selectedItemId == roomInfo.id);
    [cell configureWithBackgroundInfo:roomInfo selected:selected];
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.collectionView];
}

- (void)layoutControls {
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.equalTo(self.view);
    }];
}

#pragma mark - setters/getters
- (NSMutableArray<HJRoomBgModel *> *)items {
    if (!_items) {
        _items = @[].mutableCopy;
    }
    return _items;
}

- (UICollectionView *)collectionView {
    if (!_collectionView) {
        UICollectionViewFlowLayout *layout = [UICollectionViewFlowLayout new];
        
        _collectionView = [[UICollectionView alloc] initWithFrame:self.view.bounds collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        _collectionView.dataSource = self;
        _collectionView.delegate = self;
        [_collectionView registerClass:[HJRoomBackgroundSeetingCell class] forCellWithReuseIdentifier:HJRoomBackgroundSeetingCellID];
    }
    return _collectionView;
}

@end
