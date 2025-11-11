//
//  YPHomeTagView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeTagView.h"

#import "YPHomeTagCCell.h"

#import "YPHomeTag.h"
@implementation YPHomeTagView


- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        [self setUI];
    }
    return self;
}

- (void)setUI
{
    self.backgroundColor = [UIColor clearColor];
    __weak typeof(self)weakSelf = self;
    [self.collectView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(weakSelf);
    }];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{

    if (self.roomTagList.count == 0) {
        return CGSizeZero;
    }
    YPHomeTag *tag = [self.roomTagList safeObjectAtIndex:indexPath.item];
    
    CGSize size = [tag.name boundingRectWithSize:CGSizeMake(0, 38) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.stytle.selFont} context:nil].size;
    
    return CGSizeMake(size.width+8, 38);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 0;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 0;
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(0, 0, 0, 0);
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.roomTagList.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPHomeTagCCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPHomeTagCCell" forIndexPath:indexPath];
    
    cell.isHomeSectionStyle = self.isHomeSectionStyle;
    
    cell.isSel = indexPath.item == self.sel?YES:NO;
    
    
    YPHomeTag *tag = [self.roomTagList safeObjectAtIndex:indexPath.item];
    //    cell.logo.image = [UIImage imageNamed:[self getLogoImgName:tag.id]];
    cell.nameLabel.text = tag.name;
    cell.stytle = self.stytle;
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    //    [collectionView scrollRectToVisible:CGRectMake(indexPath.item*50, 0, 50, 38) animated:YES];
    
    if (self.sel == indexPath.item) {
        return;
    }
    
    [self setScrollTag:indexPath.item];
    
    //回调外部滚动block
    if (self.selItemCallBack) {
        self.selItemCallBack(indexPath.item);
    }
    
}

- (void)setScrollTag:(NSInteger)item
{
    [self.collectView scrollRectToVisible:CGRectMake(item*50, 0, 50, 38) animated:YES];
    
    //复位上一次选中的cell样式
    YPHomeTagCCell *cell = (YPHomeTagCCell *)[self.collectView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:self.sel inSection:0]];
    
    cell.isHomeSectionStyle = self.isHomeSectionStyle;
    
    cell.isSel = NO;
    
    self.sel = item;
    
    [self.collectView reloadData];
    
    //    [self.collectView reloadItemsAtIndexPaths:@[[NSIndexPath indexPathForItem:self.sel inSection:0]]];
    
    //设置当前选择cell样式
    //    YPGGHomeTagCCell *cell_sel = (YPGGHomeTagCCell *)[self.collectView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:self.sel inSection:0]];
    //    cell_sel.isSel = YES;
}

#pragma mark setter/getter
- (void)setRoomTagList:(NSMutableArray *)roomTagList
{
    _roomTagList = roomTagList;
    [_collectView reloadData];
}

- (void)setStytle:(YPFirstHomeTagStytleModel *)stytle
{
    _stytle = stytle;
    [self.collectView reloadData];
}

- (UICollectionView *)collectView
{
    if (!_collectView) {
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.minimumLineSpacing = 0;
        layout.minimumInteritemSpacing = 0;
        layout.headerReferenceSize = CGSizeZero;
        layout.footerReferenceSize = CGSizeZero;
        layout.sectionInset = UIEdgeInsetsZero;
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        layout.itemSize = CGSizeMake(50, 38);
        
        _collectView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
        _collectView.delegate = self;
        _collectView.dataSource = self;
        _collectView.showsHorizontalScrollIndicator = NO;
        _collectView.backgroundColor = [UIColor clearColor];
        [_collectView registerNib:[UINib nibWithNibName:@"YPHomeTagCCell" bundle:nil] forCellWithReuseIdentifier:@"YPHomeTagCCell"];
        
        [self addSubview:_collectView];
        
        
    }
    return _collectView;
}


@end
