//
//  YPRoomPopupView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomPopupView.h"
#import "YPRoomPopupCollectionViewCell.h"

#define HJRoomPopupViewItemH (100)
#define HJRoomPopupViewItemW ((XC_SCREE_W-(20*5))/4)

@interface YPRoomPopupView ()<UICollectionViewDelegate,UICollectionViewDataSource>
@property (nonatomic ,strong) UILabel *title;
@property (nonatomic ,strong) UIView *coverView;
@property (nonatomic ,strong) UIView *contentView;
@property (nonatomic ,strong) UICollectionView *collectionView;
@property (nonatomic ,strong) UIButton *closeButton;
@property (nonatomic ,strong) MASConstraint *contentViewTop;
@end

@implementation YPRoomPopupView

- (instancetype)init {
    self = [super init];
    if (self) {
        [self setupView];
        [self setupLayout];
    }
    return self;
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setupView];
        [self setupLayout];
    }
    return self;
}

- (void)setupView {
    [self addSubview:self.coverView];
    [self addSubview:self.contentView];
    [self.contentView addSubview:self.title];
    [self.contentView addSubview:self.collectionView];
    [self.contentView addSubview:self.closeButton];
}

- (void)setupLayout {
    
    [self.coverView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self);
    }];
//    @weakify(self);
//    [self.contentView mas_makeConstraints:^(MASConstraintMaker *make) {
//        @strongify(self)
//        make.left.right.bottom.equalTo(self);
//        self.contentViewTop = make.top.equalTo(self.mas_bottom);
//    }];
    [self.title mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(29);
        make.left.mas_equalTo(21);
        make.height.mas_equalTo(14);
    }];
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.contentView).offset(52);
        make.height.mas_equalTo(HJRoomPopupViewItemH);
        make.left.right.equalTo(self.contentView);
    }];
    [self.closeButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.collectionView.mas_bottom).offset(10);
        make.bottom.mas_equalTo(-12);
        make.left.mas_equalTo(15);
        make.right.mas_equalTo(-15);
        make.height.mas_equalTo(40);
    }];
}

#pragma mark - action
- (void)closeButtonClick:(id)sender {
    !self.cancelAction ?: self.cancelAction();
}

- (void)showWithContentView:(UIView * _Nullable)contenView {
    if (contenView) {
        self.frame = contenView.bounds;
        self.contentView.frame = CGRectMake(0, kScreenHeight, XC_SCREE_W, (((self.items.count-1)/4+1)*HJRoomPopupViewItemH) + 114);
        [contenView addSubview:self];
        [UIView animateWithDuration:0.3 animations:^{
            self.contentView.top = contenView.height - self.contentView.height;
        } completion:^(BOOL finished) {
        }];
    } else {
        self.frame = [UIApplication sharedApplication].keyWindow.bounds;
        self.contentView.frame = CGRectMake(0, kScreenHeight, XC_SCREE_W, (((self.items.count-1)/4+1)*HJRoomPopupViewItemH) + 114);
        [[UIApplication sharedApplication].keyWindow addSubview:self];
        [UIView animateWithDuration:0.3 animations:^{
            self.contentView.top = kScreenHeight - self.contentView.height;
        } completion:^(BOOL finished) {
        }];
    }
}

- (void)dismiss {
    [UIView animateWithDuration:0.3 animations:^{
        self.top = kScreenHeight;
        self.coverView.alpha = 0;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

#pragma mark - collection

- (nonnull __kindof UICollectionViewCell *)collectionView:(nonnull UICollectionView *)collectionView cellForItemAtIndexPath:(nonnull NSIndexPath *)indexPath {
    
    YPRoomPopupCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPRoomPopupCollectionViewCell" forIndexPath:indexPath];
    YPRoomPopupItem *item = [self.items safeObjectAtIndex:indexPath.row];
    [cell.icon setImage:[UIImage imageNamed:item.icon]];
    cell.titleLabel.text = item.title;
    
    return cell;
}

- (NSInteger)collectionView:(nonnull UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.items.count;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    [collectionView deselectItemAtIndexPath:indexPath animated:YES];
    YPRoomPopupItem *item = [self.items safeObjectAtIndex:indexPath.row];
    [self closeButtonClick:nil];
    !item.blockAction ?: item.blockAction();
}


#pragma mark - getter/setter
- (void)setItems:(NSArray<YPRoomPopupItem *> *)items {
    _items = items;
    [self.collectionView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(((items.count-1)/4+1)*HJRoomPopupViewItemH);
    }];
    [self.collectionView reloadData];
    [self layoutIfNeeded];
}

- (UIView *)contentView{
    if (!_contentView) {
        _contentView = [[UIView alloc] init];
        _contentView.backgroundColor = [UIColor whiteColor];
    }
    return _contentView;
}

- (UICollectionView *)collectionView {
    
    if (!_collectionView) {
        CGFloat w = HJRoomPopupViewItemW;
        CGFloat h = HJRoomPopupViewItemH;
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.itemSize = CGSizeMake(w,h);
        layout.minimumLineSpacing = 0;
        layout.minimumInteritemSpacing = 8;
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        layout.sectionInset = UIEdgeInsetsMake(0, 15, 0, 15);
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, XC_RATIO_WIDTH(160)) collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.dataSource = self;
        _collectionView.delegate = self;
        _collectionView.bounces = NO;
        [_collectionView registerNib:[UINib nibWithNibName:@"YPRoomPopupCollectionViewCell" bundle:[NSBundle mainBundle]] forCellWithReuseIdentifier:@"YPRoomPopupCollectionViewCell"];
    }
    return _collectionView;
}

- (UIButton *)closeButton {
    if (!_closeButton) {
        _closeButton = [[UIButton alloc] init];
        _closeButton.backgroundColor = UIColorHex(F2F2F2);
        [_closeButton setTitleColor:UIColorHex(545151) forState:UIControlStateNormal];
        [_closeButton.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_closeButton setTitle:@"取消" forState:UIControlStateNormal];
        [_closeButton addTarget:self action:@selector(closeButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        _closeButton.layer.cornerRadius = 20.f;
        _closeButton.layer.masksToBounds = YES;
    }
    return _closeButton;
}

- (UILabel *)title {
    if (!_title) {
        _title = [[UILabel alloc] init];
        [_title setTextColor:UIColorHex(605D5D)];
        [_title setFont:[UIFont systemFontOfSize:14]];
        [_title setText:@"房间菜单"];
    }
    return _title;
}

- (UIView *)coverView {
    if (!_coverView) {
        _coverView = [[UIView alloc] init];
        _coverView.backgroundColor = [UIColor blackColor];
        _coverView.alpha = 0.1f;
        @weakify(self);
        [_coverView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            @strongify(self);
            [self dismiss];
        }]];
    }
    return _coverView;
}

@end



