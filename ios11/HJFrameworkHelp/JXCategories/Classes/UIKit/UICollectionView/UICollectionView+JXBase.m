//
//  UICollectionView+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UICollectionView+JXBase.h"
#import "NSArray+JXBase.h"
#import "NSIndexPath+JXBase.h"

@implementation UICollectionView (JXBase)

#pragma mark - Base
- (NSInteger)jx_firstSection {
    return self.numberOfSections == 0 ? -1 : 0;
}

- (NSInteger)jx_lastSection {
    return self.numberOfSections == 0 ? -1 : self.numberOfSections - 1;
}

- (NSInteger)jx_firstItemInSection:(NSUInteger)section {
    return [self numberOfItemsInSection:section] == 0 ? -1 : 0;
}

- (NSInteger)jx_lastItemInSection:(NSUInteger)section {
    NSInteger items = [self numberOfItemsInSection:section];
    return items == 0 ? -1 : items - 1;
}

- (NSIndexPath *)jx_firstIndexPath {
    NSInteger sections = [self numberOfSections];
    if (!sections) return nil;
    
    for (NSInteger i = 0; i < sections; i++) {
        NSIndexPath *indexPath = [self jx_firstIndexPathInSection:i];
        if (indexPath) return indexPath;
    }
    return nil;
}

- (NSIndexPath *)jx_lastIndexPath {
    NSInteger sections = [self numberOfSections];
    if (!sections) return nil;
    
    for (NSInteger i = sections - 1; i >= 0; i--) {
        NSIndexPath *indexPath = [self jx_lastIndexPathInSection:i];
        if (indexPath) return indexPath;
    }
    return nil;
}

- (NSIndexPath *)jx_firstIndexPathInSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return nil;
    
    NSInteger item = [self jx_firstItemInSection:section];
    if (item >= 0) return [NSIndexPath indexPathForItem:item inSection:section];
    
    return nil;
}

- (NSIndexPath *)jx_lastIndexPathInSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return nil;
    
    NSInteger item = [self jx_lastItemInSection:section];
    if (item >= 0) return [NSIndexPath indexPathForItem:item inSection:section];
    
    return nil;
}

- (NSInteger)jx_numberOfItemsInSections {
    NSInteger sections = [self numberOfSections];
    if (!sections) return 0;
    
    NSInteger items = 0;
    for (NSInteger i = 0; i < sections; i++) {
        items += [self numberOfItemsInSection:i];
    }
    return items;
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForItems {
    return [self jx_indexPathsForItemsFromIndexPath:nil toIndexPath:nil];
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForItemsInSection:(NSUInteger)section {
    NSMutableArray *indexPaths = @[].mutableCopy;
    if (![self jx_containsSection:section]) return indexPaths.copy;
    
    NSInteger items = [self numberOfItemsInSection:section];
    if (!items) return indexPaths.copy;
    
    NSIndexPath *fromIndexPath = [self jx_firstIndexPathInSection:section];
    if (!fromIndexPath) return indexPaths.copy;
    
    NSIndexPath *toIndexPath = [self jx_lastIndexPathInSection:section];
    if (!toIndexPath) return indexPaths.copy;
    
    return [self jx_indexPathsForItemsFromIndexPath:fromIndexPath toIndexPath:toIndexPath];
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForItemsFromIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
    NSMutableArray *indexPaths = @[].mutableCopy;
    NSIndexPath *aFromIndexPath = fromIndexPath ? fromIndexPath : self.jx_firstIndexPath;
    NSIndexPath *aToIndexPath = toIndexPath ? toIndexPath : self.jx_lastIndexPath;
    if (![self jx_containsIndexPath:aFromIndexPath]) return indexPaths.copy;
    if (![self jx_containsIndexPath:aToIndexPath]) return indexPaths.copy;
    
    NSComparisonResult result = [aFromIndexPath compare:aToIndexPath];
    switch (result) {
        case NSOrderedAscending:
        {
            for (NSInteger i = aFromIndexPath.section; i <= aToIndexPath.section; i++) {
                NSInteger items = [self numberOfItemsInSection:i];
                NSInteger from = 0;
                NSInteger to = items;
                if (i == aFromIndexPath.section) {
                    from = aFromIndexPath.item;
                } else if (i == aToIndexPath.section) {
                    to = aToIndexPath.item + 1;
                }
                for (NSInteger j = from; j < to; j++) {
                    NSIndexPath *indexPath = [NSIndexPath indexPathForItem:j inSection:i];
                    [indexPaths jx_addObject:indexPath];
                }
            }
        }
            break;
        case NSOrderedSame:
        {
            NSIndexPath *indexPath = [NSIndexPath indexPathForItem:aFromIndexPath.item inSection:aFromIndexPath.section];
            [indexPaths jx_addObject:indexPath];
        }
            break;
        case NSOrderedDescending:
            break;
    }
    return indexPaths.copy;
}

- (NSInteger)_jx_numberOfSectionsAfterReload {
    NSInteger sections = 0;
    if (self.dataSource && [self.dataSource respondsToSelector:@selector(numberOfSectionsInCollectionView:)]) {
        sections = [self.dataSource numberOfSectionsInCollectionView:self];
    }
    return sections;
}

- (NSInteger)_jx_numberOfItemsInSectionAfterReload:(NSInteger)section {
    NSInteger items = 0;
    if (self.dataSource && [self.dataSource respondsToSelector:@selector(collectionView:numberOfItemsInSection:)]) {
        items = [self.dataSource collectionView:self numberOfItemsInSection:section];
    }
    return items;
}

- (NSArray<NSIndexPath *> *)_jx_indexPathsForItemsAfterReload {
    NSMutableArray *indexPaths = @[].mutableCopy;
    NSInteger sections = [self _jx_numberOfSectionsAfterReload];
    if (!sections) return indexPaths.copy;
    
    for (NSInteger i = 0; i < sections; i++) {
        NSInteger items = [self _jx_numberOfItemsInSectionAfterReload:i];
        if (!items) continue;
        
        for (NSInteger j = 0; j < items; j++) {
            NSIndexPath *indexPath = [NSIndexPath indexPathForItem:j inSection:i];
            [indexPaths jx_addObject:indexPath];
        }
    }
    return indexPaths.copy;
}

- (BOOL)jx_containsSection:(NSUInteger)section {
    NSIndexSet *set = [NSIndexSet indexSetWithIndex:section];
    return [self jx_containsSections:set];
}

- (BOOL)jx_containsSections:(NSIndexSet *)sections {
    if (!sections.count) return NO;
    
    NSInteger aSections = [self numberOfSections];
    if (!aSections) return NO;
    
    NSIndexSet *set = [[NSIndexSet alloc] initWithIndexesInRange:NSMakeRange(0, aSections)];
    return [set containsIndexes:sections];
}

- (BOOL)jx_containsItem:(NSUInteger)item inSection:(NSUInteger)section {
    NSIndexPath *indexPath = [NSIndexPath indexPathForItem:item inSection:section];
    return [self jx_containsIndexPath:indexPath];
}

- (BOOL)jx_containsIndexPath:(NSIndexPath *)indexPath {
    NSArray *buffer = indexPath ? @[indexPath] : nil;
    return [self jx_containsIndexPaths:buffer];
}

- (BOOL)jx_containsIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (!indexPaths.count) return NO;
    
    NSInteger sections = [self numberOfSections];
    if (!sections) return NO;
    
    for (NSIndexPath *indexPath in indexPaths) {
        if (indexPath.section >= sections || indexPath.section < 0) return NO;
        
        NSInteger items = [self numberOfItemsInSection:indexPath.section];
        if (indexPath.item >= items || indexPath.item < 0) return NO;
    }
    return YES;
}

- (BOOL)_jx_containsSectionsAfterReload:(NSIndexSet *)sections {
    if (!sections.count) return NO;
    
    NSInteger aSections = [self _jx_numberOfSectionsAfterReload];
    if (!aSections) return NO;
    
    NSIndexSet *set = [[NSIndexSet alloc] initWithIndexesInRange:NSMakeRange(0, aSections)];
    return [set containsIndexes:sections];
}

- (BOOL)_jx_containsIndexPathAfterReload:(NSIndexPath *)indexPath {
    NSArray *buffer = indexPath ? @[indexPath] : nil;
    return [self _jx_containsIndexPathsAfterReload:buffer];
}

- (BOOL)_jx_containsIndexPathsAfterReload:(NSArray<NSIndexPath *> *)indexPaths {
    if (!indexPaths.count) return NO;
    
    NSInteger sections = [self _jx_numberOfSectionsAfterReload];
    if (!sections) return NO;
    
    BOOL contains = YES;
    for (NSIndexPath *indexPath in indexPaths) {
        if (indexPath.section >= sections || indexPath.section < 0) {
            contains = NO;
            break;
        }
        
        NSInteger items = [self _jx_numberOfItemsInSectionAfterReload:indexPath.section];
        if (indexPath.item >= items || indexPath.item < 0) {
            contains = NO;
            break;
        }
    }
    return contains;
}

- (void)_jx_logForInvalidSections:(NSIndexSet *)sections {
    NSLog(@"UICollectionView error, invalid sections to scroll/insert/delete/reload: \n%@,", sections);
}

- (void)_jx_logForInvalidIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    NSLog(@"UICollectionView error, invalid indexPathes to scroll/insert/delete/reload: \n%@,", indexPaths);
}

- (void)jx_setupDataSourceDelegate:(id)dataSourceDelegate {
    self.dataSource = dataSourceDelegate;
    self.delegate = dataSourceDelegate;
}

- (void)jx_removeDataSourceDelegate {
    self.dataSource = nil;
    self.delegate = nil;
}

- (UICollectionViewCell *)_jx_cellForView:(UIView *)view {
    if (!view.superview) return nil;
    if ([view.superview isKindOfClass:[UICollectionViewCell class]]) return (UICollectionViewCell *)view.superview;
    
    return [self _jx_cellForView:view.superview];
}

- (NSIndexPath *)jx_indexPathForViewInItem:(UIView *)view {
    if (!view) return nil;
    if (![view isKindOfClass:[UIView class]]) return nil;
    
    UICollectionViewCell *cell = [self _jx_cellForView:view];
    if (cell) return [self indexPathForCell:cell];
    
    return nil;
}

#pragma mark - Visible
- (BOOL)jx_isVisibleItemForIndexPath:(NSIndexPath *)indexPath {
    if (![self jx_containsIndexPath:indexPath]) return NO;
    
    NSArray *visibleItemIndexPaths = self.indexPathsForVisibleItems;
    for (NSIndexPath *visibleIndexPath in visibleItemIndexPaths) {
        if ([indexPath jx_isEqualToIndexPath:visibleIndexPath]) return YES;
    }
    return NO;
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForVisibleItems {
    NSArray<NSIndexPath *> *visibleItems = [self indexPathsForVisibleItems];
    if (!visibleItems.count) return visibleItems;
    
    return [visibleItems jx_sortedArrayInAscendingWithKeys:@[
                                                             @"section",
                                                             @"item"
                                                             ]];
}

- (NSIndexPath *)jx_indexPathForFirstVisibleItem {
    return [self.jx_indexPathsForVisibleItems jx_objectOrNilAtIndex:0];
}

- (NSIndexPath *)jx_indexPathForLastVisibleItem {
    return [self.jx_indexPathsForVisibleItems lastObject];
}

#pragma mark - Register
- (void)jx_registerNib:(UINib *)nib forHeaderViewWithReuseIdentifier:(NSString *)identifier {
    [self registerNib:nib forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:identifier];
}

- (void)jx_registerNib:(UINib *)nib forFooterViewWithReuseIdentifier:(NSString *)identifier {
    [self registerNib:nib forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:identifier];
}

- (void)jx_registerClass:(Class)viewClass forHeaderViewWithReuseIdentifier:(NSString *)identifier {
    [self registerClass:viewClass forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:identifier];
}

- (void)jx_registerClass:(Class)viewClass forFooterViewWithReuseIdentifier:(NSString *)identifier {
    [self registerClass:viewClass forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:identifier];
}

- (__kindof UICollectionReusableView *)jx_dequeueReusableHeaderViewWithReuseIdentifier:(NSString *)identifier forIndexPath:(NSIndexPath *)indexPath {
    return [self dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:identifier forIndexPath:indexPath];
}

- (__kindof UICollectionReusableView *)jx_dequeueReusableFooterViewWithReuseIdentifier:(NSString *)identifier forIndexPath:(NSIndexPath *)indexPath {
    return [self dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:identifier forIndexPath:indexPath];
}

#pragma mark - Scroll
- (void)jx_scrollToSection:(NSUInteger)section atScrollPosition:(UICollectionViewScrollPosition)scrollPosition animated:(BOOL)animated {
    [self jx_scrollToItem:0 inSection:section atScrollPosition:scrollPosition animated:animated];
}

- (void)jx_scrollToItem:(NSUInteger)item inSection:(NSUInteger)section {
    [self jx_scrollToItem:item inSection:section atScrollPosition:UICollectionViewScrollPositionTop animated:YES];
}

- (void)jx_scrollToItem:(NSUInteger)item inSection:(NSUInteger)section animated:(BOOL)animated {
    [self jx_scrollToItem:item inSection:section atScrollPosition:UICollectionViewScrollPositionTop animated:animated];
}

- (void)jx_scrollToItem:(NSUInteger)item inSection:(NSUInteger)section atScrollPosition:(UICollectionViewScrollPosition)scrollPosition animated:(BOOL)animated {
    NSIndexPath *indexPath = [NSIndexPath indexPathForItem:item inSection:section];
    [self jx_scrollToItemAtIndexPath:indexPath atScrollPosition:scrollPosition animated:animated];
}

- (void)jx_scrollToItemAtIndexPath:(NSIndexPath *)indexPath atScrollPosition:(UICollectionViewScrollPosition)scrollPosition animated:(BOOL)animated {
    if (![self _jx_containsIndexPathAfterReload:indexPath]) {
        [self _jx_logForInvalidIndexPaths:@[indexPath]];
        return;
    }
    [self scrollToItemAtIndexPath:indexPath atScrollPosition:scrollPosition animated:animated];
}

#pragma mark - Insert
/*
 - (void)moveSection:(NSInteger)section toSection:(NSInteger)newSection;
 
 - (void)moveItemAtIndexPath:(NSIndexPath *)indexPath toIndexPath:(NSIndexPath *)newIndexPath;
 */

- (NSIndexSet *)_jx_sectionsByInsertingSections:(NSIndexSet *)sections {
    NSInteger reloadSections = [self _jx_numberOfSectionsAfterReload];
    if (!reloadSections) return nil;
    if (![self _jx_containsSectionsAfterReload:sections]) return nil;
    
    NSInteger currentSections = [self numberOfSections];
    NSInteger offset = currentSections + sections.count;
    if (offset != reloadSections) return nil;
    
    return [NSIndexSet indexSetWithIndexesInRange:NSMakeRange(0, reloadSections)];
}

- (NSArray<NSIndexPath *> *)_jx_indexPathsForItemsByInsertingItems:(NSArray<NSIndexPath *> *)indexPaths {
    NSArray<NSIndexPath *> *reloadedItems = [self _jx_indexPathsForItemsAfterReload];
    if (!reloadedItems.count) return nil;
    if (![self _jx_containsIndexPathsAfterReload:indexPaths]) return nil;
    
    return reloadedItems;
}

- (BOOL)_jx_shouldInsertSections:(NSIndexSet *)sections {
    NSInteger count = sections.count;
    if (!count) return NO;
    
    NSIndexSet *targetSections = [self _jx_sectionsByInsertingSections:sections];
    if (!targetSections) return NO;
    
    return YES;
}

- (BOOL)_jx_shouldInsertItemsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (!indexPaths.count) return NO;
    
    NSArray<NSIndexPath *> *targetIndexPaths = [self _jx_indexPathsForItemsByInsertingItems:indexPaths];
    if (!targetIndexPaths) return NO;
    
    return YES;
}

- (void)jx_insertItem:(NSUInteger)item inSection:(NSUInteger)section {
    NSIndexPath *toIndexPath = [NSIndexPath indexPathForItem:item inSection:section];
    [self jx_insertItemAtIndexPath:toIndexPath];
}

- (void)jx_insertItemAtIndexPath:(NSIndexPath *)indexPath {
    [self jx_insertItemsAtIndexPaths:@[indexPath]];
}

- (void)jx_insertItemsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (![self _jx_shouldInsertItemsAtIndexPaths:indexPaths]) {
        [self _jx_logForInvalidIndexPaths:indexPaths];
        return;
    }
    [self insertItemsAtIndexPaths:indexPaths];
}

- (void)jx_insertSection:(NSUInteger)section {
    NSIndexSet *sections = [NSIndexSet indexSetWithIndex:section];
    [self jx_insertSections:sections];
}

- (void)jx_insertSections:(NSIndexSet *)sections {
    if (![self _jx_shouldInsertSections:sections]) {
        [self _jx_logForInvalidSections:sections];
        return;
    }
    [self insertSections:sections];
}

#pragma mark - Delete
- (NSIndexSet *)_jx_sectionsByDeletingSections:(NSIndexSet *)sections {
    NSInteger currentSections = [self numberOfSections];
    if (!currentSections) return nil;
    if (![self jx_containsSections:sections]) return nil;
    
    NSInteger offset = currentSections - sections.count;
    if (offset < 0) return nil;
    
    return [NSIndexSet indexSetWithIndexesInRange:NSMakeRange(0, offset)];
}

- (NSArray<NSIndexPath *> *)_jx_indexPathsForItemsByDeletingItems:(NSArray<NSIndexPath *> *)indexPaths {
    NSArray<NSIndexPath *> *currentItems = self.jx_indexPathsForItems;
    if (!currentItems.count) return nil;
    if (![self jx_containsIndexPaths:indexPaths]) return nil;
    
    __block NSMutableArray *buffer = @[].mutableCopy;
    if ([currentItems isEqualToArray:indexPaths]) return buffer.copy;
    
    NSIndexPath *aFromIndexPath = self.jx_firstIndexPath;
    NSIndexPath *aToIndexPath = self.jx_lastIndexPath;
    NSMutableDictionary<NSNumber *, NSNumber *> *offsets = @{}.mutableCopy;
    for (NSIndexPath *indexPath in indexPaths) {
        NSNumber *key = @(indexPath.section);
        NSNumber *itemsValue = [offsets objectForKey:key];
        if (!itemsValue) {
            [offsets setObject:@1 forKey:key];
            continue;
        }
        
        NSInteger items = itemsValue.integerValue + 1;
        [offsets setObject:@(items) forKey:key];
    }
    
    for (NSInteger i = aFromIndexPath.section; i <= aToIndexPath.section; i++) {
        NSInteger items = [self numberOfItemsInSection:i];
        NSInteger itemsOffset = [offsets objectForKey:@(i)].integerValue;
        NSInteger from = 0;
        NSInteger to = items;
        if (i == aFromIndexPath.section) {
            from = aFromIndexPath.item;
        } else if (i == aToIndexPath.section) {
            to = aToIndexPath.item + 1;
        }
        to -= itemsOffset;
        for (NSInteger j = from; j < to; j++) {
            NSIndexPath *indexPath = [NSIndexPath indexPathForItem:j inSection:i];
            [buffer jx_addObject:indexPath];
        }
    }
    return buffer.copy;
}

- (BOOL)_jx_shouldDeleteSections:(NSIndexSet *)sections {
    NSInteger count = sections.count;
    if (!count) return NO;
    
    NSIndexSet *targetSections = [self _jx_sectionsByDeletingSections:sections];
    if (!targetSections) return NO;
    
    NSInteger reloadSections = [self _jx_numberOfSectionsAfterReload];
    if (reloadSections != targetSections.count) return NO;
    
    return YES;
}

- (BOOL)_jx_shouldDeleteItemsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (!indexPaths.count) return NO;
    
    NSArray<NSIndexPath *> *perviousIndexPaths = [self jx_indexPathsForItems];
    if (![perviousIndexPaths jx_containsObjects:indexPaths]) return NO;
    
    NSArray<NSIndexPath *> *targetIndexPaths = [self _jx_indexPathsForItemsByDeletingItems:indexPaths];
    if (!targetIndexPaths) return NO;
    
    NSArray<NSIndexPath *> *reloadIndexPaths = [self _jx_indexPathsForItemsAfterReload];
    if (![reloadIndexPaths isEqualToArray:targetIndexPaths]) return NO;
    
    return YES;
}

- (void)jx_deleteItem:(NSUInteger)item inSection:(NSUInteger)section {
    NSIndexPath *toIndexPath = [NSIndexPath indexPathForItem:item inSection:section];
    [self jx_deleteItemAtIndexPath:toIndexPath];
}

- (void)jx_deleteItemAtIndexPath:(NSIndexPath *)indexPath {
    [self jx_deleteItemsAtIndexPaths:@[indexPath]];
}

- (void)jx_deleteItemsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths deleteOrder:(JXUICollectionViewDeleteOrder)deleteOrder {
    if (!indexPaths.count) return;
    
    NSMutableArray *deletedIndexPaths = [NSMutableArray arrayWithArray:indexPaths];
    if (indexPaths.count > 1 && deleteOrder != JXUICollectionViewDeleteOrderNone) {
        if (deleteOrder == JXUICollectionViewDeleteOrderAscending) {
            [deletedIndexPaths jx_sortedArrayInAscending];
        }
        else if (deleteOrder == JXUICollectionViewDeleteOrderDecending) {
            [deletedIndexPaths jx_sortedArrayInDescending];
        }
    }
    [self jx_deleteItemsAtIndexPaths:deletedIndexPaths];
}

- (void)jx_deleteItemsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (![self _jx_shouldDeleteItemsAtIndexPaths:indexPaths]) {
        [self _jx_logForInvalidIndexPaths:indexPaths];
        return;
    }
    [self deleteItemsAtIndexPaths:indexPaths];
}

- (void)jx_deleteSection:(NSUInteger)section {
    NSIndexSet *sections = [NSIndexSet indexSetWithIndex:section];
    [self jx_deleteSections:sections];
}

- (void)jx_deleteSections:(NSIndexSet *)sections {
    if (![self _jx_shouldDeleteSections:sections]) {
        [self _jx_logForInvalidSections:sections];
        return;
    }
    [self deleteSections:sections];
}

#pragma mark - Reload
- (void)jx_reloadItem:(NSUInteger)item inSection:(NSUInteger)section {
    NSIndexPath *toIndexPath = [NSIndexPath indexPathForItem:item inSection:section];
    [self jx_reloadItemAtIndexPath:toIndexPath];
}

- (void)jx_reloadItemAtIndexPath:(NSIndexPath *)indexPath {
    [self jx_reloadItemsAtIndexPaths:@[indexPath]];
}

- (void)jx_reloadItemsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (![self _jx_containsIndexPathsAfterReload:indexPaths]) {
        [self _jx_logForInvalidIndexPaths:indexPaths];
        return;
    }
    [self reloadItemsAtIndexPaths:indexPaths];
}

- (void)jx_reloadSection:(NSUInteger)section {
    NSIndexSet *sections = [NSIndexSet indexSetWithIndex:section];
    [self jx_reloadSections:sections];
}

- (void)jx_reloadSections:(NSIndexSet *)sections {
    if (![self _jx_containsSectionsAfterReload:sections]) {
        [self _jx_logForInvalidSections:sections];
        return;
    }
    [self reloadSections:sections];
}

- (void)jx_reloadDataByKeepingSelection {
    NSArray *indexs = [self indexPathsForSelectedItems];
    [self reloadData];
    __weak typeof(self) weakSelf = self;
    [indexs enumerateObjectsUsingBlock:^(NSIndexPath *indexPath, NSUInteger idx, BOOL *stop) {
        [weakSelf selectItemAtIndexPath:indexPath animated:NO scrollPosition:UICollectionViewScrollPositionNone];
    }];
}

#pragma mark - Select
- (void)jx_clearSelectedItems:(BOOL)animated {
    NSArray *indexs = [self indexPathsForSelectedItems];
    __weak typeof(self) weakSelf = self;
    [indexs enumerateObjectsUsingBlock:^(NSIndexPath *indexPath, NSUInteger idx, BOOL *stop) {
        [weakSelf deselectItemAtIndexPath:indexPath animated:animated];
    }];
}

@end


@implementation NSIndexPath (JXUICollectionView)

- (BOOL)jx_isEqualToItem:(NSIndexPath *)indexPath {
    if (!indexPath) return NO;
    if (![indexPath isKindOfClass:[NSIndexPath class]]) return NO;
    if (self == indexPath) return YES;
    
    return self.item == indexPath.item;
}

- (NSIndexPath *)jx_indexPathByAddingItem:(NSInteger)item {
    return [self jx_indexPathByAddingItem:item section:0];
}

- (NSIndexPath *)jx_indexPathByAddingItem:(NSInteger)item section:(NSInteger)section {
    return [NSIndexPath indexPathForItem:self.item + item inSection:self.section + section];
}


@end
