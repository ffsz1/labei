//
//  CUIWaterfallView.m
//  
//
//  Created by daixiang on 13-12-26.
//  Copyright (c) 2013年 YY.inc. All rights reserved.
//

#import "CUIWaterfallView.h"
#import "CUIWaterfallViewCell.h"


@interface CUIWaterfallViewCell (Private)

@property (nonatomic, weak) CUIWaterfallView *waterfallView;

@end

//#pragma mark - CUIWaterfallViewCell1

//这个继承是非collectionview实现使用
//@interface CUIWaterfallViewCell1 : CUIWaterfallViewCell
//
//@property (nonatomic, weak) CUIWaterfallView *waterfallView;
//
//@end
//
//@implementation CUIWaterfallViewCell1
//
//- (id)initWithReuseIdentifier:(NSString *)reuseIdentifier
//{
//    if (self = [super initWithReuseIdentifier:reuseIdentifier])
//    {
////        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tap)];
////        [self addGestureRecognizer:tap];
//    }
//    return self;
//}
//
////- (void)tap
////{
////    NSLog(@"tap");
////    if ([self.waterfallView.delegate respondsToSelector:@selector(waterfallView:didSelectItemAtIndex:)])
////    {
////        [self.waterfallView.delegate waterfallView:self.waterfallView didSelectItemAtIndex:self.index];
////    }
////}
//
//- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touches began");
//}
//
//- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touches cancel");
//}
//
//- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touches end");
//    if ([self.waterfallView.delegate respondsToSelector:@selector(waterfallView:didSelectItemAtIndex:)])
//    {
//        [self.waterfallView.delegate waterfallView:self.waterfallView didSelectItemAtIndex:self.index];
//    }
//}
//
//@end

//#pragma mark - CUIWaterfallViewCell2

//这个继承是collectionview实现使用
//@interface CUIWaterfallViewCell2 : CUIWaterfallViewCell
//
////@property (nonatomic, weak) UICollectionViewCell *collectionViewCell;
//
//@end
//
//@implementation CUIWaterfallViewCell2
//
//- (id)initWithReuseIdentifier:(NSString *)reuseIdentifier
//{
//    if (self = [super initWithReuseIdentifier:reuseIdentifier])
//    {
//        self.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleTopMargin|UIViewAutoresizingFlexibleLeftMargin|UIViewAutoresizingFlexibleRightMargin|UIViewAutoresizingFlexibleBottomMargin;
//    }
//    return self;
//}
//
//@end

#pragma mark - collection view support

#define CUIWaterfallViewCollectionCellIdentifier @"CUIWaterfallViewCollectionCellIdentifier"

@interface CUIWaterfallViewCollectionCell : UICollectionViewCell

@property (nonatomic) CUIWaterfallViewCell *waterfallCell;

@end

@implementation CUIWaterfallViewCollectionCell


@end

@interface UICollectionViewWaterfallLayout : UICollectionViewLayout
{
    NSMutableArray *_attributes;
    CGFloat _contentHeight;
}

@property (nonatomic, weak) CUIWaterfallView *waterfallView;

@end

#pragma mark - CUIWaterfallCellItem

@interface CUIWaterfallCellItem : NSObject

@property (nonatomic) NSUInteger index;
@property (nonatomic) CGRect frame;
@property (nonatomic) BOOL visible;
@property (nonatomic) CUIWaterfallViewCell *cell;

@end

@implementation CUIWaterfallCellItem


@end

#pragma mark - CUIWaterfallView

@interface CUIWaterfallView () <UICollectionViewDataSource, UICollectionViewDelegate>
{
    NSUInteger _numberOfColumns;
    NSUInteger _numberOfItems;
    CGFloat _itemWidth;
    CGFloat _contentHeight;
    //CGFloat _lastOffsetY;
    
    NSMutableArray *_cellItems;
    NSMutableDictionary *_reusableCellsDictionary;
    NSMutableArray *_visibleCells;
    
    UIScrollView *_scrollView;
    
    //ios6.0以上可以用collectionview
    UICollectionView *_collectionView;
}

@end

@implementation CUIWaterfallView

BOOL waterfallShouldUseCollectionView()
{
#if !WATERFALL_USE_COLLECTIONVIEW_IF_AVAILABLE
    return NO;
#else
    static BOOL result = NO;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        float version = [[[UIDevice currentDevice] systemVersion] floatValue];
        result = (version > 5.99);
    });
    
    return result;
#endif
}

- (void)_init
{
    self.clipsToBounds = YES;
    _contentPaddingInset = UIEdgeInsetsMake(0, 0, 0, 0);
    //_contentHeight = self.frame.size.height;
    _reusableCellsDictionary = [NSMutableDictionary dictionaryWithCapacity:1];
    
    if (waterfallShouldUseCollectionView())
    {
        //NSLog(@"ios 6 above, using collection");
        UICollectionViewWaterfallLayout *layout = [[UICollectionViewWaterfallLayout alloc] init];
        layout.waterfallView = self;
        _collectionView = [[UICollectionView alloc] initWithFrame:self.bounds collectionViewLayout:layout];
        [_collectionView registerClass:[CUIWaterfallViewCollectionCell class] forCellWithReuseIdentifier:CUIWaterfallViewCollectionCellIdentifier];
        _collectionView.dataSource = self;
        _collectionView.delegate = self;
        _collectionView.backgroundColor = [UIColor clearColor];
        [self addSubview:_collectionView];
        _scrollView = _collectionView;
    }
    else
    {
        //NSLog(@"ios 5, using scrollview");
        _scrollView = [[UIScrollView alloc] initWithFrame:self.bounds];
        //[self addObserver:self forKeyPath:@"contentOffset" options:NSKeyValueObservingOptionNew context:NULL];
        _visibleCells = [NSMutableArray arrayWithCapacity:10];
        _scrollView.canCancelContentTouches = YES;
        _scrollView.delaysContentTouches = YES;
        _scrollView.delegate = self;
        [self addSubview:_scrollView];
    }
    
    _scrollView.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleTopMargin|UIViewAutoresizingFlexibleLeftMargin|UIViewAutoresizingFlexibleRightMargin|UIViewAutoresizingFlexibleBottomMargin;
    //_scrollView.contentSize = self.frame.size;
    _scrollView.alwaysBounceVertical = YES;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    [self _init];
    return self;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        
        [self _init];
    }
    return self;
}

- (void)setFrame:(CGRect)frame
{
    CGRect currentFrame = self.frame;
    [super setFrame:frame];
    if (!CGSizeEqualToSize(currentFrame.size, frame.size))
    {
        if (!waterfallShouldUseCollectionView())
        {
            [self calculateContentHeight];
            [self didScroll];
        }
    }
}

- (void)setBounds:(CGRect)bounds
{
    CGRect currentBounds = self.bounds;
    [super setBounds:bounds];
    if (!CGSizeEqualToSize(currentBounds.size, bounds.size))
    {
        if (!waterfallShouldUseCollectionView())
        {
            [self calculateContentHeight];
            [self didScroll];
        }
    }
}

- (void)setDataSource:(id<CUIWaterfallDataSource>)dataSource
{
    id old = _dataSource;
    _dataSource = dataSource;
    
    if (_dataSource && old != _dataSource && _delegate) {
        [self reloadData];
    }
}

- (void)setDelegate:(id<CUIWaterfallDelegate>)delegate
{
    id old = _delegate;
    _delegate = delegate;
    
    if (_dataSource && _delegate && old != _delegate) {
        [self reloadData];
    }
}

- (UIScrollView *)scrollView
{
    return _scrollView;
}

- (void)dealloc
{
    _scrollView.delegate = nil;
    //[self removeObserver:self forKeyPath:@"contentOffset"];
    _collectionView.dataSource = nil;
    _collectionView.delegate = nil;
    
//    NSArray *allKeys = [_reusableCellsDictionary allKeys];
//    for (NSString *identifier in allKeys) {
//        NSDictionary *reusableCells = [_reusableCellsDictionary objectForKey:identifier];
//        NSArray *keys = [reusableCells allKeys];
//        
//        for (id key in keys) {
//            CUIWaterfallViewCell *cell = [reusableCells objectForKey:key];
//            [cell.tapGesture removeTarget:self action:@selector(tapGesture:)];
//            [cell removeGestureRecognizer:cell.tapGesture];
//        }
//    }
}

//- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
//{
//    if (object == self && [keyPath isEqualToString:@"contentOffset"])
//    {
//        if (self.contentOffset.y != _lastOffsetY)
//        {
//            _lastOffsetY = self.contentOffset.y;
//            
//            [self didScroll];
//        }
//    }
//}

- (void)reloadData
{
    //NSLog(@"reloadData");
    _numberOfColumns = [self.dataSource numberOfColumnsInWaterfallView:self];
    _numberOfItems = [self.dataSource numberOfItemsInWaterfallView:self];
    
    [self calculateContentHeight];
    
    if (waterfallShouldUseCollectionView())
    {
        [_collectionView reloadData];
    }
    else
    {
        [self recycelAllCells];
        [self didScroll];
    }
    
//    for (int i = 0; i < [_cellItems count]; i++) {
//        CUIWaterfallCellItem *item = [_cellItems objectAtIndex:i];
//        NSLog(@"index %u frame %f %f %f %f", item.index, item.frame.origin.x, item.frame.origin.y, item.frame.size.width, item.frame.size.height);
//    }
}

- (id)dequeueReusableCellWithIdentifier:(NSString *)identifier forIndex:(NSUInteger)index
{
    if (!identifier)
        return nil;
    
//    if (isIOS6OrAbove())
//    {
//        NSIndexPath *indexPath = [NSIndexPath indexPathForItem:index inSection:0];
//        CUIWaterfallViewCollectionCell *cell = [_collectionView dequeueReusableCellWithReuseIdentifier:CUIWaterfallViewCollectionCellIdentifier forIndexPath:indexPath];
//        
//        if (!cell.waterfallCell)
//        {
//            cell.waterfallCell = [[CUIWaterfallViewCell2 alloc] initWithReuseIdentifier:identifier];
//            cell.waterfallCell.frame = cell.contentView.bounds;
//            cell.waterfallCell.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleTopMargin|UIViewAutoresizingFlexibleLeftMargin|UIViewAutoresizingFlexibleRightMargin|UIViewAutoresizingFlexibleBottomMargin;
//            [cell.contentView addSubview:cell.waterfallCell];
//        }
//        
//        return cell.waterfallCell;
//    }
//    else
    {
        NSMutableDictionary *reusableCells = [_reusableCellsDictionary objectForKey:identifier];
        if (!reusableCells)
        {
            reusableCells = [NSMutableDictionary dictionaryWithCapacity:10];
            [_reusableCellsDictionary setObject:reusableCells forKey:identifier];
        }
        
        CUIWaterfallViewCell *cell = nil;
        if ([reusableCells count] > 0)
        {
            id key = [NSNumber numberWithUnsignedInteger:index];
            cell = [reusableCells objectForKey:key];
            if (!cell)
            {
                key = [[reusableCells allKeys] lastObject];
                cell = [reusableCells objectForKey:key];
            }
            [reusableCells removeObjectForKey:key];
//            cell = [reusableCells lastObject];
//            [reusableCells removeLastObject];
        }
//        else
//        {
//            if (isIOS6OrAbove())
//            {
//                cell = [[CUIWaterfallViewCell2 alloc] initWithReuseIdentifier:identifier];
//            }
//            else
//            {
//                cell = [[CUIWaterfallViewCell1 alloc] initWithReuseIdentifier:identifier];
//            }
//        }
        
        //NSLog(@"request dequeue %d cell %p index %d", index, cell, cell.index);
        return cell;
    }
}

- (CUIWaterfallViewCell *)cellAtIndex:(NSUInteger)index
{
    if (waterfallShouldUseCollectionView())
    {
        CUIWaterfallViewCollectionCell *cell = (CUIWaterfallViewCollectionCell *)[_collectionView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:index inSection:0]];
        return cell.waterfallCell;
    }
    else
    {
        for (CUIWaterfallViewCell *cell in _visibleCells) {
            if (cell.index == index) {
                return cell;
            }
        }
    }
    return nil;
}

- (NSArray *)visibleCells
{
    if (waterfallShouldUseCollectionView())
    {
        NSArray *visibleCells = [_collectionView visibleCells];
        NSMutableArray *realVisibleCells = [NSMutableArray arrayWithCapacity:[visibleCells count]];
        for (int i = 0; i < [visibleCells count]; i++) {
            CUIWaterfallViewCell *c = ((CUIWaterfallViewCollectionCell *)[visibleCells objectAtIndex:i]).waterfallCell;
            [realVisibleCells addObject:c];
        }
        return realVisibleCells;
    }
    else
    {
        return [NSArray arrayWithArray:_visibleCells];
    }
}

#pragma mark - private

- (void)recycelAllCells
{
    //NSLog(@"recycelAllCells");
    //倒序加入是为了reloaddata的时候，尽量重用相同的cell
//    for (int i = [_cellItems count] - 1; i >= 0; i--)
//    {
//        CUIWaterfallCellItem *item = [_cellItems objectAtIndex:i];
//        [self recycleItem:item];
//    }
    
    for (CUIWaterfallCellItem *item in _cellItems) {
        [self recycleItem:item];
    }
}

- (void)recycleItem:(CUIWaterfallCellItem *)item
{
    if (item.cell)
    {
        item.visible = NO;
        [self recycleCell:item.cell];
        item.cell = nil;
    }
}

- (void)recycleCell:(CUIWaterfallViewCell *)cell
{
    if (!cell.reuseIdentifier)
        return;
    
    NSMutableDictionary *reusableCells = [_reusableCellsDictionary objectForKey:cell.reuseIdentifier];
    
    [reusableCells setObject:cell forKey:[NSNumber numberWithUnsignedInteger:cell.index]];
    //[reusableCells addObject:cell];
    [cell removeFromSuperview];
    [_visibleCells removeObject:cell];
    //NSLog(@"recycle(%@) %d, reusable %d", cell.reuseIdentifier, cell.index, [reusableCells count]);
}

- (void)didScroll
{
    //NSLog(@"didScroll");
    CGRect visibleRect = CGRectMake(0, _scrollView.contentOffset.y, self.frame.size.width, self.frame.size.height);
    for (int i = 0; i < [_cellItems count]; i++) {
        CUIWaterfallCellItem *item = [_cellItems objectAtIndex:i];
        if (CGRectIntersectsRect(visibleRect, item.frame))
        {
            if (!item.visible)
            {
                item.visible = YES;
                CUIWaterfallViewCell *cell = (CUIWaterfallViewCell *)[self.dataSource waterfallView:self cellForItemAtIndex:item.index];
                
                if (cell)
                {
                    cell.index = item.index;
                    cell.frame = item.frame;
                    cell.waterfallView = self;
                    [_scrollView addSubview:cell];
                    [_visibleCells addObject:cell];
                    
//                    if (![cell.gestureRecognizers containsObject:cell.tapGesture])
//                    {
//                        NSLog(@"add gesture");
//                        [cell.tapGesture addTarget:self action:@selector(tapGesture:)];
//                        [cell addGestureRecognizer:cell.tapGesture];
//                    }
                }
                item.cell = cell;
            }
        }
        else
        {
            if (item.visible)
            {
                item.visible = NO;
                [self recycleItem:item];
            }
        }
    }
    
    //NSLog(@"visible %d", [_visibleCells count]);
}

- (NSUInteger)numberOfColumns
{
    //if (_numberOfColumns == 0)
    //    _numberOfColumns = [self.dataSource numberOfColumnsInWaterfallView:self];
    
    return _numberOfColumns;
}

- (NSUInteger)numberOfItems
{
    return _numberOfItems;
}

- (void)calculateContentHeight
{
    //_contentHeight = self.frame.size.height;
    _contentHeight = 0;
    
    if (_numberOfColumns == 0)
        return;
    
    //计算每个item宽度
//    if (_numberOfColumns > 0)
//    {
        _itemWidth = (self.bounds.size.width - _contentPaddingInset.left - _contentPaddingInset.right - (_numberOfColumns-1)*_columnPadding) / _numberOfColumns;
//    }
//    else
//    {
//        _itemWidth = 0;
//    }
    
    //用于纪录每列当前高度
    CGFloat *columnHeights = (CGFloat *)malloc(_numberOfColumns * sizeof(CGFloat));
    for (int i = 0; i < _numberOfColumns; i++) {
        columnHeights[i] = _contentPaddingInset.top;
    }
    
    //复用cellitem
    if (!_cellItems)
    {
        _cellItems = [NSMutableArray arrayWithCapacity:_numberOfItems];
    }
    else if ([_cellItems count] > _numberOfItems)
    {
        NSIndexSet *set = [NSIndexSet indexSetWithIndexesInRange:NSMakeRange(_numberOfItems, [_cellItems count] - _numberOfItems)];
        [_cellItems removeObjectsAtIndexes:set];
    }
    
    //计算所有item的布局
    for (int i = 0; i < _numberOfItems; i++)
    {
        CGFloat itemHeight = _itemWidth;
        if ([self.delegate respondsToSelector:@selector(waterfallView:heightForItemAtIndex:)])
        {
            itemHeight = [self.delegate waterfallView:self heightForItemAtIndex:i];
        }
        
        CGFloat minHeight = FLT_MAX;
        NSUInteger columnToAdd = 0;
        //找到当前高度最小的列
        for (int j = 0; j < _numberOfColumns; j++) {
            CGFloat h = columnHeights[j];
            if (h < minHeight)
            {
                minHeight = h;
                columnToAdd = j;
            }
        }
        
        CGFloat x = _contentPaddingInset.left + columnToAdd*(_itemWidth+_columnPadding);
        //不是第一行，则需要加上row padding
        CGFloat y = (minHeight==_contentPaddingInset.top) ? minHeight : (minHeight+_rowPadding);
        
        CUIWaterfallCellItem *item = nil;
        if (i < [_cellItems count])
        {
            item = [_cellItems objectAtIndex:i];
        }
        else
        {
            item = [[CUIWaterfallCellItem alloc] init];
            [_cellItems addObject:item];
        }
        item.index = i;
        item.frame = CGRectMake(x, y, _itemWidth, itemHeight);
        
        //列高度累加
        columnHeights[columnToAdd] = y + itemHeight;
        //NSLog(@"columnHeights[%d]=%f", columnToAdd, columnHeights[columnToAdd]);
    }
    
    //找出最高的列即为scrollview的content高度
    CGFloat maxHeight = 0;
    for (int i = 0; i < _numberOfColumns; i++) {
        if (columnHeights[i] > maxHeight)
            maxHeight = columnHeights[i];
    }
    free(columnHeights);
    maxHeight += _contentPaddingInset.bottom;
    _contentHeight = maxHeight;

    //NSLog(@"calculateContentHeight %f", _contentHeight);
    _scrollView.contentSize = CGSizeMake(self.frame.size.width, _contentHeight);
}

- (NSUInteger)contentHeight
{
    //[self calculateContentHeight];
    return _contentHeight;
}

- (NSArray *)cellItems;
{
    return _cellItems;
}

//- (void)tapGesture:(UITapGestureRecognizer *)tap
//{
//    NSLog(@"tapGesture");
//    CUIWaterfallViewCell *cell = (CUIWaterfallViewCell *)tap.view;
//    if ([self.delegate respondsToSelector:@selector(waterfallView:didSelectItemAtIndex:)])
//    {
//        [self.delegate waterfallView:self didSelectItemAtIndex:cell.index];
//    }
//}

#pragma mark - collection view datasource

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    //NSLog(@"numberOfSectionsInCollectionView");
	return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    //NSLog(@"collectionView numberOfItemsInSection %d", section);
    if ([self.dataSource numberOfColumnsInWaterfallView:self] == 0) {
        return 0;
    }
    return [self.dataSource numberOfItemsInWaterfallView:self];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"collectionView cellForItemAtIndexPath %d %d", indexPath.section, indexPath.item);
    CUIWaterfallViewCell *cell = (CUIWaterfallViewCell *)[self.dataSource waterfallView:self cellForItemAtIndex:indexPath.item];
    
    CUIWaterfallViewCollectionCell *collectionViewCell = [collectionView dequeueReusableCellWithReuseIdentifier:CUIWaterfallViewCollectionCellIdentifier forIndexPath:indexPath];
    //NSLog(@"  collection cell %p", collectionViewCell);
    
    if (collectionViewCell.waterfallCell && collectionViewCell.waterfallCell != cell)
    {
        //should not reach
        [self recycleCell:collectionViewCell.waterfallCell];
        collectionViewCell.waterfallCell = nil;
    }
    
    cell.index = indexPath.item;
    cell.frame = collectionViewCell.contentView.bounds;
    cell.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleTopMargin|UIViewAutoresizingFlexibleLeftMargin|UIViewAutoresizingFlexibleRightMargin|UIViewAutoresizingFlexibleBottomMargin;
    [collectionViewCell.contentView addSubview:cell];
    collectionViewCell.waterfallCell = cell;
    
    return collectionViewCell;
}

#pragma mark - collection view delegate

- (void)collectionView:(UICollectionView *)collectionView didEndDisplayingCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"collectionView didEndDisplayingCell %p forItemAtIndexPath %d %d", cell, indexPath.section, indexPath.item);
    CUIWaterfallViewCollectionCell *c = (CUIWaterfallViewCollectionCell *)cell;
    if (c.waterfallCell)
    {
        [self recycleCell:c.waterfallCell];
        c.waterfallCell = nil;
    }
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"collectionView didSelectItemAtIndexPath %d %d", indexPath.section, indexPath.item);
    if ([self.delegate respondsToSelector:@selector(waterfallView:didSelectItemAtIndex:)])
    {
        [self.delegate waterfallView:self didSelectItemAtIndex:indexPath.item];
    }
}

#pragma mark - scrollview delegate，将内部scrollview的回调转发到waterfallview的delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView;
{
    if (!waterfallShouldUseCollectionView())
    {
        [self didScroll];
    }
    
    if ([self.delegate respondsToSelector:@selector(scrollViewDidScroll:)])
    {
        [self.delegate scrollViewDidScroll:_scrollView];
    }
}

- (void)scrollViewDidZoom:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewDidZoom:)])
    {
        [self.delegate scrollViewDidZoom:_scrollView];
    }
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewWillBeginDragging:)])
    {
        [self.delegate scrollViewWillBeginDragging:_scrollView];
    }
}

- (void)scrollViewWillEndDragging:(UIScrollView *)scrollView withVelocity:(CGPoint)velocity targetContentOffset:(inout CGPoint *)targetContentOffset
{
    if ([self.delegate respondsToSelector:@selector(scrollViewWillEndDragging:withVelocity:targetContentOffset:)])
    {
        [self.delegate scrollViewWillEndDragging:_scrollView withVelocity:velocity targetContentOffset:targetContentOffset];
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    if ([self.delegate respondsToSelector:@selector(scrollViewDidEndDragging:willDecelerate:)])
    {
        [self.delegate scrollViewDidEndDragging:_scrollView willDecelerate:decelerate];
    }
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewWillBeginDecelerating:)])
    {
        [self.delegate scrollViewWillBeginDecelerating:_scrollView];
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewDidEndDecelerating:)])
    {
        [self.delegate scrollViewDidEndDecelerating:_scrollView];
    }
}

- (void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewDidEndScrollingAnimation:)])
    {
        [self.delegate scrollViewDidEndScrollingAnimation:_scrollView];
    }
}

- (UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(viewForZoomingInScrollView:)])
    {
        return [self.delegate viewForZoomingInScrollView:_scrollView];
    }
    return nil;
}

- (void)scrollViewWillBeginZooming:(UIScrollView *)scrollView withView:(UIView *)view
{
    if ([self.delegate respondsToSelector:@selector(scrollViewWillBeginZooming:withView:)])
    {
        [self.delegate scrollViewWillBeginZooming:_scrollView withView:view];
    }
}

- (void)scrollViewDidEndZooming:(UIScrollView *)scrollView withView:(UIView *)view atScale:(CGFloat)scale
{
    if ([self.delegate respondsToSelector:@selector(scrollViewDidEndZooming:withView:atScale:)])
    {
        [self.delegate scrollViewDidEndZooming:_scrollView withView:view atScale:scale];
    }
}

- (BOOL)scrollViewShouldScrollToTop:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewShouldScrollToTop:)])
    {
        return [self.delegate scrollViewShouldScrollToTop:_scrollView];
    }
    return YES;
}

- (void)scrollViewDidScrollToTop:(UIScrollView *)scrollView
{
    if ([self.delegate respondsToSelector:@selector(scrollViewDidScrollToTop:)])
    {
        [self.delegate scrollViewDidScrollToTop:_scrollView];
    }
}

@end

#pragma mark - collection view support

@implementation UICollectionViewWaterfallLayout

- (void)prepareLayout
{
    //NSLog(@"prepareLayout");
    [super prepareLayout];
    _contentHeight = [self.waterfallView contentHeight];
    
    NSArray *cellItems = [self.waterfallView cellItems];
    if (!_attributes)
    {
        _attributes = [NSMutableArray arrayWithCapacity:[cellItems count]];
    }
    
    for (int i = 0; i < [cellItems count]; i++) {
        CUIWaterfallCellItem *item = [cellItems objectAtIndex:i];
        
        UICollectionViewLayoutAttributes *attr = nil;
        if (i < [_attributes count])
        {
            attr = [_attributes objectAtIndex:i];
        }
        else
        {
            NSIndexPath *path = [NSIndexPath indexPathForItem:i inSection:0];
            attr = [UICollectionViewLayoutAttributes layoutAttributesForCellWithIndexPath:path];
            [_attributes addObject:attr];
        }
        
        attr.frame = item.frame;
    }
}

- (CGSize)collectionViewContentSize
{
    //NSLog(@"collectionViewContentSize");
    return CGSizeMake(self.collectionView.frame.size.width, _contentHeight);
}

- (UICollectionViewLayoutAttributes *)layoutAttributesForItemAtIndexPath:(NSIndexPath *)path
{
    //NSLog(@"layoutAttributesForItemAtIndexPath %d %d", path.section, path.item);
    return _attributes[path.item];
}

-(NSArray*)layoutAttributesForElementsInRect:(CGRect)rect
{
    //NSLog(@"layoutAttributesForElementsInRect %f %f %f %f", rect.origin.x, rect.origin.y, rect.size.width, rect.size.height);
    return [_attributes filteredArrayUsingPredicate:[NSPredicate predicateWithBlock:^BOOL(UICollectionViewLayoutAttributes *evaluatedObject, NSDictionary *bindings) {
        return CGRectIntersectsRect(rect, [evaluatedObject frame]);
    }]];
}

- (BOOL)shouldInvalidateLayoutForBoundsChange:(CGRect)newBounds
{
    //NSLog(@"shouldInvalidateLayoutForBoundsChange");
	return NO;
}

@end
