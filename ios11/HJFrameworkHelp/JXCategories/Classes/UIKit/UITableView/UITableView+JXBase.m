//
//  UITableView+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UITableView+JXBase.h"
#import "JXCoreGraphicHelper.h"
#import "NSArray+JXBase.h"
#import "NSIndexPath+JXBase.h"
#import "NSObject+JXBase.h"
#import "UIScrollView+JXBase.h"
#import "UIView+JXBase.h"

static const int JX_UI_TABLE_VIEW_HEADERS_FOR_SECTIONS_KEY;
static const int JX_UI_TABLE_VIEW_FOOTERS_FOR_SECTIONS_KEY;

@interface UITableView ()

@property (nonatomic, strong) NSMapTable<NSNumber *, UIView *> *_jx_headersForSections;
@property (nonatomic, strong) NSMapTable<NSNumber *, UIView *> *_jx_footersForSections;

@end

@implementation UITableView (JXBase)

#pragma mark - Base
+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SEL selectors[] = {
            @selector(setDelegate:),
        };
        JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix(self, selectors, @"_jx_ui_table_view_");
    });
}

- (CGSize)_jx_adjustedContentSize {
    if (!self.dataSource || !self.delegate) return CGSizeZero;
    
    CGSize contentSize = self.contentSize;
    CGFloat footerViewMaxY = CGRectGetMaxY(self.tableFooterView.frame);
    CGSize adjustedContentSize = CGSizeMake(contentSize.width, footerViewMaxY);
    
    if (!self.jx_lastSection) return adjustedContentSize;
    
    CGRect lastSectionRect = [self rectForSection:self.jx_lastSection];
    adjustedContentSize.height = fmax(adjustedContentSize.height, CGRectGetMaxY(lastSectionRect));
    return adjustedContentSize;
}

- (BOOL)jx_canScroll {
    return [self jx_canScroll:JXUIScrollViewScrollDirectionVertical];
}

- (BOOL)jx_canScroll:(JXUIScrollViewScrollDirection)direction {
    if (CGRectGetHeight(self.bounds) <= 0) return NO;
    
    if ([self.tableHeaderView isKindOfClass:[UISearchBar class]]) {
        BOOL canScroll = self._jx_adjustedContentSize.height + JXUIEdgeInsetsGetValuesInVertical(self.jx_contentInset) > CGRectGetHeight(self.bounds);
        return canScroll;
    }
    return [super jx_canScroll:JXUIScrollViewScrollDirectionVertical];
}

- (NSMapTable<NSNumber *,UIView *> *)_jx_headersForSections {
    NSMapTable *buffer = [self jx_getAssociatedValueForKey:&JX_UI_TABLE_VIEW_HEADERS_FOR_SECTIONS_KEY];
    if (!buffer) {
        buffer = [NSMapTable mapTableWithKeyOptions:NSMapTableWeakMemory valueOptions:NSMapTableWeakMemory];
        [self jx_setAssociatedValue:buffer withKey:&JX_UI_TABLE_VIEW_HEADERS_FOR_SECTIONS_KEY];
    }
    return buffer;
}

- (NSMapTable<NSNumber *,UIView *> *)_jx_footersForSections {
    NSMapTable *buffer = [self jx_getAssociatedValueForKey:&JX_UI_TABLE_VIEW_FOOTERS_FOR_SECTIONS_KEY];
    if (!buffer) {
        buffer = [NSMapTable mapTableWithKeyOptions:NSMapTableWeakMemory valueOptions:NSMapTableWeakMemory];
        [self jx_setAssociatedValue:buffer withKey:&JX_UI_TABLE_VIEW_FOOTERS_FOR_SECTIONS_KEY];
    }
    return buffer;
}

- (NSInteger)jx_firstSection {
    return self.numberOfSections == 0 ? -1 : 0;
}

- (NSInteger)jx_lastSection {
    return self.numberOfSections == 0 ? -1 : self.numberOfSections - 1;
}

- (NSInteger)jx_firstRowInSection:(NSUInteger)section {
    return [self numberOfRowsInSection:section] == 0 ? -1 : 0;
}

- (NSInteger)jx_lastRowInSection:(NSUInteger)section {
    NSInteger rows = [self numberOfRowsInSection:section];
    return rows == 0 ? -1 : rows - 1;
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

    NSInteger row = [self jx_firstRowInSection:section];
    if (row >= 0) return [NSIndexPath indexPathForRow:row inSection:section];
    
    return nil;
}

- (NSIndexPath *)jx_lastIndexPathInSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return nil;
    
    NSInteger row = [self jx_lastRowInSection:section];
    if (row >= 0) return [NSIndexPath indexPathForRow:row inSection:section];
    
    return nil;
}

- (NSInteger)jx_numberOfRowsInSections {
    NSInteger sections = [self numberOfSections];
    if (!sections) return 0;
    
    NSInteger rows = 0;
    for (NSInteger i = 0; i < sections; i++) {
        rows += [self numberOfRowsInSection:i];
    }
    return rows;
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForRows {
    return [self jx_indexPathsForRowsFromIndexPath:nil toIndexPath:nil];
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForRowsInSection:(NSUInteger)section {
    NSMutableArray *indexPaths = @[].mutableCopy;
    if (![self jx_containsSection:section]) return indexPaths.copy;
    
    NSInteger rows = [self numberOfRowsInSection:section];
    if (!rows) return indexPaths.copy;
    
    NSIndexPath *fromIndexPath = [self jx_firstIndexPathInSection:section];
    if (!fromIndexPath) return indexPaths.copy;
    
    NSIndexPath *toIndexPath = [self jx_lastIndexPathInSection:section];
    if (!toIndexPath) return indexPaths.copy;
    
    return [self jx_indexPathsForRowsFromIndexPath:fromIndexPath toIndexPath:toIndexPath];
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForRowsFromIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
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
                NSInteger rows = [self numberOfRowsInSection:i];
                NSInteger from = 0;
                NSInteger to = rows;
                if (i == aFromIndexPath.section) {
                    from = aFromIndexPath.row;
                } else if (i == aToIndexPath.section) {
                    to = aToIndexPath.row + 1;
                }
                for (NSInteger j = from; j < to; j++) {
                    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:j inSection:i];
                    [indexPaths jx_addObject:indexPath];
                }
            }
        }
            break;
        case NSOrderedSame:
        {
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:aFromIndexPath.row inSection:aFromIndexPath.section];
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
    if (self.dataSource && [self.dataSource respondsToSelector:@selector(numberOfSectionsInTableView:)]) {
        sections = [self.dataSource numberOfSectionsInTableView:self];
    }
    return sections;
}

- (NSInteger)_jx_numberOfRowsInSectionAfterReload:(NSInteger)section {
    NSInteger rows = 0;
    if (self.dataSource && [self.dataSource respondsToSelector:@selector(tableView:numberOfRowsInSection:)]) {
        rows = [self.dataSource tableView:self numberOfRowsInSection:section];
    }
    return rows;
}

- (NSArray<NSIndexPath *> *)_jx_indexPathsForRowsAfterReload {
    NSMutableArray *indexPaths = @[].mutableCopy;
    NSInteger sections = [self _jx_numberOfSectionsAfterReload];
    if (!sections) return indexPaths.copy;
    
    for (NSInteger i = 0; i < sections; i++) {
        NSInteger rows = [self _jx_numberOfRowsInSectionAfterReload:i];
        if (!rows) continue;
        
        for (NSInteger j = 0; j < rows; j++) {
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:j inSection:i];
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

- (BOOL)jx_containsRow:(NSUInteger)row inSection:(NSUInteger)section {
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:row inSection:section];
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
        
        NSInteger rows = [self numberOfRowsInSection:indexPath.section];
        if (indexPath.row >= rows || indexPath.row < 0) return NO;
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
        
        NSInteger rows = [self _jx_numberOfRowsInSectionAfterReload:indexPath.section];
        if (indexPath.row >= rows || indexPath.row < 0) {
            contains = NO;
            break;
        }
    }
    return contains;
}

- (void)_jx_logForInvalidSections:(NSIndexSet *)sections {
    NSLog(@"UITableView error, invalid sections to scroll/insert/delete/reload: \n%@,", sections);
}

- (void)_jx_logForInvalidIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    NSLog(@"UITableView error, invalid indexPathes to scroll/insert/delete/reload: \n%@,", indexPaths);
}

- (void)jx_setupDataSourceDelegate:(id)dataSourceDelegate {
    self.dataSource = dataSourceDelegate;
    self.delegate = dataSourceDelegate;
}

- (void)jx_removeDataSourceDelegate {
    self.dataSource = nil;
    self.delegate = nil;
}

- (void)jx_updateWithBlock:(void(^)(UITableView *tableView))block {
    __weak typeof(self) weakSelf = self;
    [self beginUpdates];
    block(weakSelf);
    [self endUpdates];
}

- (CGRect)jx_rectForRowsInSection:(NSInteger)section {
    CGRect rectForSection = [self rectForSection:section];
    if (CGRectGetHeight(rectForSection) <= 0) return rectForSection;
    
    CGFloat y, height;
    CGRect rectForHeader = [self rectForHeaderInSection:section];
    CGRect rectForFooter = [self rectForFooterInSection:section];
    y = CGRectGetMaxY(rectForHeader);
    height = CGRectGetHeight(rectForSection) - CGRectGetHeight(rectForHeader) - CGRectGetHeight(rectForFooter);
    return CGRectMake(rectForSection.origin.x, y, rectForSection.size.width, height);
}

- (CGRect)_jx_rectForHeaderInSection:(NSUInteger)section {
    CGRect rect = [self rectForHeaderInSection:section];
    if (CGRectGetHeight(rect) <= 0) return CGRectNull;
    
    if (self.style == UITableViewStylePlain) {
        rect = [self rectForSection:section];
    }
    return rect;
}

- (CGRect)_jx_rectForFooterInSection:(NSUInteger)section {
    CGRect rect = [self rectForFooterInSection:section];
    if (CGRectGetHeight(rect) <= 0) return CGRectNull;
    
    if (self.style == UITableViewStylePlain) {
        rect = [self rectForSection:section];
    }
    return rect;
}

- (NSInteger)jx_sectionForHeaderAtPoint:(CGPoint)point {
    NSInteger count = [self numberOfSections];
    if (!count) return -1;
    
    for (NSInteger i = 0; i < count; i++) {
        CGRect rectForHeader = [self _jx_rectForHeaderInSection:i];
        if (CGRectGetMinY(rectForHeader) > point.y) return -1;
        if(CGRectContainsPoint(rectForHeader, point)) return i;
    }
    return -1;
}

- (NSInteger)jx_sectionForFooterAtPoint:(CGPoint)point {
    NSInteger count = [self numberOfSections];
    if (!count) return -1;
    
    for (NSInteger i = 0; i < count; i++) {
        CGRect rectForFooter = [self _jx_rectForFooterInSection:i];
        if (CGRectGetMinY(rectForFooter) > point.y) return -1;
        if(CGRectContainsPoint(rectForFooter, point)) return i;
    }
    return -1;
}

- (NSInteger)jx_sectionForHeaderView:(UIView *)headerView {
    if (!headerView) return -1;
    if (![headerView isKindOfClass:[UIView class]]) return -1;
    
    NSInteger count = [self numberOfSections];
    if (!count) return -1;
    
    for (NSInteger i = 0; i < count; i++) {
        UIView *buffer = [self jx_headerViewForSection:i];
        if ([buffer isEqual:headerView]) return i;
    }
    return -1;
}

- (NSInteger)jx_sectionForFooterView:(UIView *)footerView {
    if (!footerView) return -1;
    if (![footerView isKindOfClass:[UIView class]]) return -1;
    
    NSInteger count = [self numberOfSections];
    if (!count) return -1;
    
    for (NSInteger i = 0; i < count; i++) {
        UIView *buffer = [self jx_footerViewForSection:i];
        if ([buffer isEqual:footerView]) return i;
    }
    return -1;
}

- (NSArray<NSNumber *> *)jx_sectionsForHeadersInRect:(CGRect)rect {
    NSMutableArray *sections = @[].mutableCopy;
    NSInteger count = [self numberOfSections];
    if (!count) return sections.copy;
    if (CGRectEqualToRect(rect, CGRectZero)) return sections.copy;
    
    for (NSInteger i = 0; i < count; i++) {
        CGRect rectForHeader = [self _jx_rectForHeaderInSection:i];
        if (CGRectGetMinY(rectForHeader) > CGRectGetMaxY(rect)) return sections.copy;
        
        if(CGRectIntersectsRect(rect, rectForHeader)) {
            [sections jx_addObject:@(i)];
        }
    }
    return sections.copy;
}

- (NSArray<NSNumber *> *)jx_sectionsForFootersInRect:(CGRect)rect {
    NSMutableArray *sections = @[].mutableCopy;
    NSInteger count = [self numberOfSections];
    if (!count) return sections.copy;
    if (CGRectEqualToRect(rect, CGRectZero)) return sections.copy;
    
    for (NSInteger i = 0; i < count; i++) {
        CGRect rectForFooter = [self _jx_rectForFooterInSection:i];
        if (CGRectGetMinY(rectForFooter) > CGRectGetMaxY(rect)) return sections.copy;
        
        if(CGRectIntersectsRect(rect, rectForFooter)) {
            [sections jx_addObject:@(i)];
        }
    }
    return sections.copy;
}

- (UITableViewCell *)_jx_cellForView:(UIView *)view {
    if (!view.superview) return nil;
    if ([view.superview isKindOfClass:[UITableViewCell class]]) return (UITableViewCell *)view.superview;
    
    return [self _jx_cellForView:view.superview];
}

- (NSIndexPath *)jx_indexPathForViewInRow:(UIView *)view {
    if (!view) return nil;
    if (![view isKindOfClass:[UIView class]]) return nil;
    
    UITableViewCell *cell = [self _jx_cellForView:view];
    if (cell) return [self indexPathForCell:cell];
    
    return nil;
}

- (NSInteger)jx_sectionForViewInHeader:(UIView *)view {
    if (!view) return -1;
    if (![view isKindOfClass:[UIView class]]) return -1;
    if (!view.superview) return -1;
    
    NSInteger count = [self numberOfSections];
    if (!count) return -1;
    
    CGRect rect = [self jx_convertRect:view.frame fromViewOrWindow:view.superview];
    for (NSInteger i = 0; i < count; i++) {
        CGRect rectForHeader = [self _jx_rectForHeaderInSection:i];
        if (CGRectGetMinY(rectForHeader) > CGRectGetMaxY(rect)) return -1;
        
        if(CGRectContainsPoint(rectForHeader, rect.origin)) {
            UIView *headerView = [self jx_headerViewForSection:i];
            if (!headerView) return -1;
            if ([headerView jx_containsSubview:view]) return i;
            
            return -1;
        }
    }
    return -1;
}

- (NSInteger)jx_sectionForViewInFooter:(UIView *)view {
    if (!view) return -1;
    if (![view isKindOfClass:[UIView class]]) return -1;
    if (!view.superview) return -1;
    
    NSInteger count = [self numberOfSections];
    if (!count) return -1;
    
    CGRect rect = [self jx_convertRect:view.frame fromViewOrWindow:view.superview];
    for (NSInteger i = 0; i < count; i++) {
        CGRect rectForFooter = [self _jx_rectForFooterInSection:i];
        if (CGRectGetMinY(rectForFooter) > CGRectGetMaxY(rect)) return -1;
        
        if(CGRectContainsPoint(rectForFooter, rect.origin)) {
            UIView *footerView = [self jx_footerViewForSection:i];
            if (!footerView) return -1;
            if ([footerView jx_containsSubview:view]) return i;
            
            return -1;
        }
    }
    return -1;
}

- (UIView *)jx_headerViewForSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return nil;
    
    UIView *headerView = [self headerViewForSection:section];
    if (headerView) return headerView;
    
    headerView = [self._jx_headersForSections objectForKey:@(section)];
    if (headerView) return headerView;
    
    return nil;
}

- (UIView *)jx_footerViewForSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return nil;
    
    UIView *footerView = [self footerViewForSection:section];
    if (footerView) return footerView;
    
    footerView = [self._jx_footersForSections objectForKey:@(section)];
    if (footerView) return footerView;
    
    return nil;
}

#pragma mark - Estimated Height
- (void)setJx_estimatedRowHeightEnabled:(BOOL)jx_estimatedRowHeightEnabled {
//    CGFloat dimension = jx_estimatedRowHeightEnabled ? UITableViewAutomaticDimension : 0;
    if (jx_estimatedRowHeightEnabled) {
        self.rowHeight = UITableViewAutomaticDimension;
//        self.estimatedRowHeight = ;
    } else {
        self.estimatedRowHeight = 0;
    }
}

- (BOOL)jx_estimatedRowHeightEnabled {
    return self.estimatedRowHeight != 0;
}

- (void)setJx_estimatedSectionHeaderHeightEnabled:(BOOL)jx_estimatedSectionHeaderHeightEnabled {
    CGFloat dimension = jx_estimatedSectionHeaderHeightEnabled ? UITableViewAutomaticDimension : 0;
    self.estimatedSectionHeaderHeight = dimension;
}

- (BOOL)jx_estimatedSectionHeaderHeightEnabled {
    return self.estimatedSectionHeaderHeight != 0;
}

- (void)setJx_estimatedSectionFooterHeightEnabled:(BOOL)jx_estimatedSectionFooterHeightEnabled {
    CGFloat dimension = jx_estimatedSectionFooterHeightEnabled ? UITableViewAutomaticDimension : 0;
    self.estimatedSectionFooterHeight = dimension;
}

- (BOOL)isJx_estimatedSectionFooterHeightEnabled {
    return self.estimatedSectionFooterHeight != 0;
}

#pragma mark - Stick
- (BOOL)jx_isStickyHeaderForSection:(NSUInteger)section {
    if (self.style != UITableViewStylePlain) return NO;
    if (![self jx_containsSection:section]) return NO;
    
    CGRect rectForHeader = [self rectForHeaderInSection:section];
    if (CGRectGetHeight(rectForHeader) <= 0) return NO;
    
    CGRect rectForSection = [self rectForSection:section];
    CGRect rectForFooter = [self rectForHeaderInSection:section];
    CGFloat offsetHeight = CGRectGetHeight(rectForSection) - CGRectGetHeight(rectForHeader) - CGRectGetHeight(rectForFooter);
    CGRect rectForOffset = CGRectMake(rectForSection.origin.x, rectForSection.origin.y, rectForSection.size.width, offsetHeight);
    CGPoint point = self._jx_visibleContentRect.origin;
    return JXCGRectContainsPoint(rectForOffset, point);
}

- (BOOL)jx_isStickyFooterForSection:(NSUInteger)section {
    if (self.style != UITableViewStylePlain) return NO;
    if (![self jx_containsSection:section]) return NO;
    
    CGRect rectForFooter = [self rectForFooterInSection:section];
    if (CGRectGetHeight(rectForFooter) <= 0) return NO;
    
    CGRect rectForSection = [self rectForSection:section];
    CGRect rectForHeader = [self rectForHeaderInSection:section];
    CGFloat offsetHeight = CGRectGetHeight(rectForSection) - CGRectGetHeight(rectForHeader) - CGRectGetHeight(rectForFooter);
    CGRect rectForOffset = CGRectMake(rectForSection.origin.x, rectForSection.origin.y + CGRectGetHeight(rectForHeader) + CGRectGetHeight(rectForFooter), rectForSection.size.width, offsetHeight);
    
    CGPoint point = CGPointMake(self._jx_visibleContentRect.origin.x, CGRectGetMaxY(self._jx_visibleContentRect));
    return JXCGRectContainsPoint(rectForOffset, point);
}

- (NSInteger)jx_sectionForStickyHeader {
    if (self.style != UITableViewStylePlain) return -1;
    
    NSInteger section = self.jx_sectionForFirstVisibleHeader;
    if (section < 0) return -1;
    if ([self jx_isStickyHeaderForSection:section]) return section;
    
    return -1;
}

- (NSInteger)jx_sectionForStickyFooter {
    if (self.style != UITableViewStylePlain) return -1;
    
    NSInteger section = self.jx_sectionForLastVisibleFooter;
    if (section < 0) return -1;
    if ([self jx_isStickyFooterForSection:section]) return section;
    
    return -1;
}

#pragma mark - Visible
- (CGRect)_jx_visibleContentRect {
    CGFloat x, y, width, height;
    x = self.contentOffset.x + self.jx_contentInset.left - self.jx_contentInset.right;
    y = self.contentOffset.y + self.jx_contentInset.top - self.jx_contentInset.bottom;
    width = self.bounds.size.width - self.jx_contentInset.left + self.jx_contentInset.right;
    height = self.bounds.size.height - self.jx_contentInset.top + self.jx_contentInset.bottom;
    return CGRectMake(x, y, width, height);
}

- (BOOL)jx_isVisibleRowForIndexPath:(NSIndexPath *)indexPath {
    if (![self jx_containsIndexPath:indexPath]) return NO;
    
    NSArray *visibleRowIndexPaths = self.indexPathsForVisibleRows;
    for (NSIndexPath *visibleIndexPath in visibleRowIndexPaths) {
        if ([indexPath jx_isEqualToIndexPath:visibleIndexPath]) return YES;
    }
    return NO;
}

- (BOOL)jx_isVisibleHeaderInSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return NO;
    
    CGRect rectForHeader = [self _jx_rectForHeaderInSection:section];
    if (CGRectGetHeight(rectForHeader) <= 0) return NO;
    
    return CGRectIntersectsRect(self._jx_visibleContentRect, rectForHeader);
}

- (BOOL)jx_isVisibleFooterInSection:(NSUInteger)section {
    if (![self jx_containsSection:section]) return NO;
    
    CGRect rectForFooter = [self _jx_rectForFooterInSection:section];
    if (CGRectGetHeight(rectForFooter) <= 0) return NO;
    
    return CGRectIntersectsRect(self._jx_visibleContentRect, rectForFooter);
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForVisibleRows {
    NSArray<NSIndexPath *> *visibleRows = [self indexPathsForVisibleRows];
    if (!visibleRows.count) return @[];
    
    return [visibleRows jx_sortedArrayInAscendingWithKeys:@[
                                                             @"section",
                                                             @"row"
                                                             ]];
}

- (NSArray<NSNumber *> *)jx_sectionsForVisibleHeaders {
    return [self jx_sectionsForHeadersInRect:[self _jx_visibleContentRect]];
}

- (NSArray<NSNumber *> *)jx_sectionsForVisibleFooters {
    return [self jx_sectionsForFootersInRect:[self _jx_visibleContentRect]];
}

- (NSIndexPath *)jx_indexPathForFirstVisibleRow {
    return [self.jx_indexPathsForVisibleRows jx_objectOrNilAtIndex:0];
}

- (NSIndexPath *)jx_indexPathForLastVisibleRow {
    return [self.jx_indexPathsForVisibleRows lastObject];
}

- (NSInteger)jx_sectionForFirstVisibleHeader {
    NSNumber *buffer = [self.jx_sectionsForVisibleHeaders jx_objectOrNilAtIndex:0];
    return buffer ? buffer.integerValue : -1;
}

- (NSInteger)jx_sectionForLastVisibleHeader {
    NSNumber *buffer = [self.jx_sectionsForVisibleHeaders lastObject];
    return buffer ? buffer.integerValue : -1;
}

- (NSInteger)jx_sectionForFirstVisibleFooter {
    NSNumber *buffer = [self.jx_sectionsForVisibleFooters jx_objectOrNilAtIndex:0];
    return buffer ? buffer.integerValue : -1;
}

- (NSInteger)jx_sectionForLastVisibleFooter {
    NSNumber *buffer = [self.jx_sectionsForVisibleFooters lastObject];
    return buffer ? buffer.integerValue : -1;
}

#pragma mark - Scroll
- (void)jx_scrollToRow:(NSUInteger)row inSection:(NSUInteger)section {
    [self jx_scrollToRow:row inSection:section atScrollPosition:UITableViewScrollPositionTop animated:YES];
}

- (void)jx_scrollToRow:(NSUInteger)row inSection:(NSUInteger)section animated:(BOOL)animated {
    [self jx_scrollToRow:row inSection:section atScrollPosition:UITableViewScrollPositionTop animated:animated];
}

- (void)jx_scrollToRow:(NSUInteger)row inSection:(NSUInteger)section atScrollPosition:(UITableViewScrollPosition)scrollPosition animated:(BOOL)animated {
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:row inSection:section];
    [self jx_scrollToRowAtIndexPath:indexPath atScrollPosition:scrollPosition animated:animated];
}

- (void)jx_scrollToRowAtIndexPath:(NSIndexPath *)indexPath atScrollPosition:(UITableViewScrollPosition)scrollPosition animated:(BOOL)animated {
    if (![self _jx_containsIndexPathAfterReload:indexPath]) {
        [self _jx_logForInvalidIndexPaths:@[indexPath]];
        return;
    }
    [self scrollToRowAtIndexPath:indexPath atScrollPosition:scrollPosition animated:animated];
}

#pragma mark - Insert
- (NSIndexSet *)_jx_sectionsByInsertingSections:(NSIndexSet *)sections {
    NSInteger reloadSections = [self _jx_numberOfSectionsAfterReload];
    if (!reloadSections) return nil;
    if (![self _jx_containsSectionsAfterReload:sections]) return nil;

    NSInteger currentSections = [self numberOfSections];
    NSInteger offset = currentSections + sections.count;
    if (offset != reloadSections) return nil;
    
    return [NSIndexSet indexSetWithIndexesInRange:NSMakeRange(0, reloadSections)];
}

- (NSArray<NSIndexPath *> *)_jx_indexPathsForRowsByInsertingRows:(NSArray<NSIndexPath *> *)indexPaths {
    NSArray<NSIndexPath *> *reloadedRows = [self _jx_indexPathsForRowsAfterReload];
    if (!reloadedRows.count) return nil;
    if (![self _jx_containsIndexPathsAfterReload:indexPaths]) return nil;
    
    return reloadedRows;
}

- (BOOL)_jx_shouldInsertSections:(NSIndexSet *)sections {
    NSInteger count = sections.count;
    if (!count) return NO;
    
    NSIndexSet *targetSections = [self _jx_sectionsByInsertingSections:sections];
    if (!targetSections) return NO;
    
    return YES;
}

- (BOOL)_jx_shouldInsertRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (!indexPaths.count) return NO;
    
    NSArray<NSIndexPath *> *targetIndexPaths = [self _jx_indexPathsForRowsByInsertingRows:indexPaths];
    if (!targetIndexPaths) return NO;
    
    return YES;
}

- (void)jx_insertRow:(NSUInteger)row inSection:(NSUInteger)section {
    [self jx_insertRow:row inSection:section withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_insertRow:(NSUInteger)row inSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation {
    NSIndexPath *toInsert = [NSIndexPath indexPathForRow:row inSection:section];
    [self jx_insertRowAtIndexPath:toInsert withRowAnimation:animation];
}

- (void)jx_insertRowAtIndexPath:(NSIndexPath *)indexPath {
    [self jx_insertRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_insertRowAtIndexPath:(NSIndexPath *)indexPath withRowAnimation:(UITableViewRowAnimation)animation {
    [self jx_insertRowsAtIndexPaths:@[indexPath] withRowAnimation:animation];
}

- (void)jx_insertRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths withRowAnimation:(UITableViewRowAnimation)animation {
    if (![self _jx_shouldInsertRowsAtIndexPaths:indexPaths]) {
        [self _jx_logForInvalidIndexPaths:indexPaths];
        return;
    }
    [self insertRowsAtIndexPaths:indexPaths withRowAnimation:animation];
}

- (void)jx_insertSection:(NSUInteger)section {
    [self jx_insertSection:section withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_insertSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation {
    NSIndexSet *sections = [NSIndexSet indexSetWithIndex:section];
    [self jx_insertSections:sections withRowAnimation:animation];
}

- (void)jx_insertSections:(NSIndexSet *)sections withRowAnimation:(UITableViewRowAnimation)animation {
    if (![self _jx_shouldInsertSections:sections]) {
        [self _jx_logForInvalidSections:sections];
        return;
    }
    [self insertSections:sections withRowAnimation:animation];
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

- (NSArray<NSIndexPath *> *)_jx_indexPathsForRowsByDeletingRows:(NSArray<NSIndexPath *> *)indexPaths {
    NSArray<NSIndexPath *> *currentRows = self.jx_indexPathsForRows;
    if (!currentRows.count) return nil;
    if (![self jx_containsIndexPaths:indexPaths]) return nil;
    
    __block NSMutableArray *buffer = @[].mutableCopy;
    if ([currentRows isEqualToArray:indexPaths]) return buffer.copy;
    
    NSIndexPath *aFromIndexPath = self.jx_firstIndexPath;
    NSIndexPath *aToIndexPath = self.jx_lastIndexPath;
    NSMutableDictionary<NSNumber *, NSNumber *> *offsets = @{}.mutableCopy;
    for (NSIndexPath *indexPath in indexPaths) {
        NSNumber *key = @(indexPath.section);
        NSNumber *rowsValue = [offsets objectForKey:key];
        if (!rowsValue) {
            [offsets setObject:@1 forKey:key];
            continue;
        }
        
        NSInteger rows = rowsValue.integerValue + 1;
        [offsets setObject:@(rows) forKey:key];
    }
    
    for (NSInteger i = aFromIndexPath.section; i <= aToIndexPath.section; i++) {
        NSInteger rows = [self numberOfRowsInSection:i];
        NSInteger rowsOffset = [offsets objectForKey:@(i)].integerValue;
        NSInteger from = 0;
        NSInteger to = rows;
        if (i == aFromIndexPath.section) {
            from = aFromIndexPath.row;
        } else if (i == aToIndexPath.section) {
            to = aToIndexPath.row + 1;
        }
        to -= rowsOffset;
        for (NSInteger j = from; j < to; j++) {
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:j inSection:i];
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

- (BOOL)_jx_shouldDeleteRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths {
    if (!indexPaths.count) return NO;
    
    NSArray<NSIndexPath *> *perviousIndexPaths = [self jx_indexPathsForRows];
    if (![perviousIndexPaths jx_containsObjects:indexPaths]) return NO;
    
    NSArray<NSIndexPath *> *targetIndexPaths = [self _jx_indexPathsForRowsByDeletingRows:indexPaths];
    if (!targetIndexPaths) return NO;
    
    NSArray<NSIndexPath *> *reloadIndexPaths = [self _jx_indexPathsForRowsAfterReload];
    if (![reloadIndexPaths isEqualToArray:targetIndexPaths]) return NO;
    
    return YES;
}

- (void)jx_deleteRow:(NSUInteger)row inSection:(NSUInteger)section {
    [self jx_deleteRow:row inSection:section withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_deleteRow:(NSUInteger)row inSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation {
    NSIndexPath *toDelete = [NSIndexPath indexPathForRow:row inSection:section];
    [self jx_deleteRowAtIndexPath:toDelete withRowAnimation:animation];
}

- (void)jx_deleteRowAtIndexPath:(NSIndexPath *)indexPath {
    [self jx_deleteRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_deleteRowAtIndexPath:(NSIndexPath *)indexPath withRowAnimation:(UITableViewRowAnimation)animation {
    [self jx_deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:animation];
}

- (void)jx_deleteRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths deleteOrder:(JXUITableViewDeleteOrder)deleteOrder withRowAnimation:(UITableViewRowAnimation)animation {
    if (!indexPaths.count) return;
    
    NSMutableArray *deletedIndexPaths = [NSMutableArray arrayWithArray:indexPaths];
    if (indexPaths.count > 1 && deleteOrder != JXUITableViewDeleteOrderNone) {
        if (deleteOrder == JXUITableViewDeleteOrderAscending) {
            [deletedIndexPaths jx_sortedArrayInAscending];
        } else if (deleteOrder == JXUITableViewDeleteOrderDecending) {
            [deletedIndexPaths jx_sortedArrayInDescending];
        }
    }
    [self jx_deleteRowsAtIndexPaths:deletedIndexPaths.copy withRowAnimation:animation];
}

- (void)jx_deleteRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths withRowAnimation:(UITableViewRowAnimation)animation {
    if (![self _jx_shouldDeleteRowsAtIndexPaths:indexPaths]) {
        [self _jx_logForInvalidIndexPaths:indexPaths];
        return;
    }
    [self deleteRowsAtIndexPaths:indexPaths withRowAnimation:animation];
}

- (void)jx_deleteSection:(NSUInteger)section {
    [self jx_deleteSection:section withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_deleteSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation {
    NSIndexSet *sections = [NSIndexSet indexSetWithIndex:section];
    [self jx_deleteSections:sections withRowAnimation:animation];
}

- (void)jx_deleteSections:(NSIndexSet *)sections withRowAnimation:(UITableViewRowAnimation)animation {
    if (![self _jx_shouldDeleteSections:sections]) {
        [self _jx_logForInvalidSections:sections];
        return;
    }
    [self deleteSections:sections withRowAnimation:animation];
}

#pragma mark - Reload
- (void)jx_reloadRow:(NSUInteger)row inSection:(NSUInteger)section {
    [self jx_reloadRow:row inSection:section withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_reloadRow:(NSUInteger)row inSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation {
    NSIndexPath *toReload = [NSIndexPath indexPathForRow:row inSection:section];
    [self jx_reloadRowAtIndexPath:toReload withRowAnimation:animation];
}

- (void)jx_reloadRowAtIndexPath:(NSIndexPath *)indexPath {
    [self jx_reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_reloadRowAtIndexPath:(NSIndexPath *)indexPath withRowAnimation:(UITableViewRowAnimation)animation {
    [self jx_reloadRowsAtIndexPaths:@[indexPath] withRowAnimation:animation];
}

- (void)jx_reloadVisibleRows {
    [self jx_reloadRowsAtIndexPaths:self.indexPathsForVisibleRows withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_reloadVisibleRowsWithRowAnimation:(UITableViewRowAnimation)animation {
    [self jx_reloadRowsAtIndexPaths:self.indexPathsForVisibleRows withRowAnimation:animation];
}

- (void)jx_reloadRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths withRowAnimation:(UITableViewRowAnimation)animation {
    if (![self _jx_containsIndexPathsAfterReload:indexPaths]) {
        [self _jx_logForInvalidIndexPaths:indexPaths];
        return;
    }
    [self reloadRowsAtIndexPaths:indexPaths withRowAnimation:animation];
}

- (void)jx_reloadSection:(NSUInteger)section {
    [self jx_reloadSection:section withRowAnimation:UITableViewRowAnimationNone];
}

- (void)jx_reloadSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation {
    NSIndexSet *sections = [NSIndexSet indexSetWithIndex:section];
    [self jx_reloadSections:sections withRowAnimation:animation];
}

- (void)jx_reloadSections:(NSIndexSet *)sections withRowAnimation:(UITableViewRowAnimation)animation {
    if (![self _jx_containsSectionsAfterReload:sections]) {
        [self _jx_logForInvalidSections:sections];
        return;
    }
    [self reloadSections:sections withRowAnimation:animation];
}

#pragma mark - Select
- (void)jx_clearSelectedRows:(BOOL)animated {
    NSArray *indexs = [self indexPathsForSelectedRows];
    [indexs enumerateObjectsUsingBlock:^(NSIndexPath *indexPath, NSUInteger idx, BOOL *stop) {
        [self deselectRowAtIndexPath:indexPath animated:animated];
    }];
}

#pragma mark - Position
- (JXUITableViewCellPosition)jx_positionForRowInRows:(NSIndexPath *)indexPath {
    if (![self jx_containsIndexPath:indexPath]) return JXUITableViewCellPositionNone;
    
    NSInteger sections = [self numberOfSections];
    if (!sections) return JXUITableViewCellPositionNone;
    if (sections == 1) return  JXUITableViewCellPositionSingle;
    if ([indexPath jx_isEqualToIndexPath:self.jx_firstIndexPath]) return JXUITableViewCellPositionTop;
    if ([indexPath jx_isEqualToIndexPath:self.jx_lastIndexPath]) return JXUITableViewCellPositionBottom;
    
    return JXUITableViewCellPositionMiddle;
}

- (JXUITableViewCellPosition)jx_positionForRowInSection:(NSIndexPath *)indexPath {
    if (![self jx_containsIndexPath:indexPath]) return JXUITableViewCellPositionNone;
    if (![self numberOfSections]) return JXUITableViewCellPositionNone;
    
    NSInteger rows = [self numberOfRowsInSection:indexPath.section];
    if (!rows) return JXUITableViewCellPositionNone;
    if (rows == 1) return JXUITableViewCellPositionSingle;
    if (indexPath.row == 0) return JXUITableViewCellPositionTop;
    if (indexPath.row == rows - 1) return JXUITableViewCellPositionBottom;
    
    return JXUITableViewCellPositionMiddle;
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForPositionInRows:(JXUITableViewCellPosition)postion {
    NSMutableArray *indexPaths = @[].mutableCopy;
    
    NSInteger sections = [self numberOfSections];
    if (!sections) return indexPaths.copy;
    
    NSInteger rows = [self jx_numberOfRowsInSections];
    if (!rows) return indexPaths.copy;
    
    switch (postion) {
        case JXUITableViewCellPositionNone:
            break;
        case JXUITableViewCellPositionSingle:
        {
            if (rows == 1) {
                [indexPaths jx_addObject:self.jx_firstIndexPath];
            }
        }
            break;
        case JXUITableViewCellPositionTop:
        {
            [indexPaths jx_addObject:self.jx_firstIndexPath];
        }
            break;
        case JXUITableViewCellPositionMiddle:
        {
            NSIndexPath *firstIndexPath = self.jx_firstIndexPath;
            NSIndexPath *lastIndexPath = self.jx_lastIndexPath;
            if (firstIndexPath && lastIndexPath) {
                NSArray<NSIndexPath *> *buffer = [self jx_indexPathsForRowsFromIndexPath:firstIndexPath toIndexPath:lastIndexPath];
                [indexPaths jx_addObjectsFromArray:buffer];
                [indexPaths jx_removeObject:lastIndexPath];
                [indexPaths jx_removeObject:firstIndexPath];
            }
        }
            break;
        case JXUITableViewCellPositionBottom:
        {
            [indexPaths jx_addObject:self.jx_lastIndexPath];
        }
            break;
    }
    if (indexPaths.count <= 1) return indexPaths.copy;
    
    return [indexPaths jx_deduplicatedArray];
}

- (NSArray<NSIndexPath *> *)jx_indexPathsForPosition:(JXUITableViewCellPosition)postion inSection:(NSUInteger)section {
    NSMutableArray *indexPaths = @[].mutableCopy;
    if (![self jx_containsSection:section]) return indexPaths.copy;
    
    NSInteger rows = [self numberOfRowsInSection:section];
    if (!rows) return  indexPaths.copy;
    
    switch (postion) {
        case JXUITableViewCellPositionNone:
            break;
        case JXUITableViewCellPositionSingle:
        {
            if (rows == 1) {
                [indexPaths jx_addObject:[NSIndexPath indexPathForRow:0 inSection:section]];
            }
        }
            break;
        case JXUITableViewCellPositionTop:
        {
            [indexPaths jx_addObject:[NSIndexPath indexPathForRow:0 inSection:section]];
        }
            break;
        case JXUITableViewCellPositionMiddle:
        {
            for (NSInteger i = 1; i < rows - 1; i++) {
                [indexPaths jx_addObject:[NSIndexPath indexPathForRow:i inSection:section]];
            }
        }
            break;
        case JXUITableViewCellPositionBottom:
        {
            [indexPaths jx_addObject:[NSIndexPath indexPathForRow:rows - 1 inSection:section]];
        }
            break;
    }
    if (indexPaths.count <= 1) return indexPaths.copy;
    
    return [indexPaths jx_deduplicatedArray];
}

#pragma mark - Swizzle
- (void)_jx_ui_table_view_setDelegate:(id<UITableViewDelegate>)delegate {
    [self _jx_ui_table_view_setDelegate:delegate];

    [self _jx_swizzleUITableViewDelegateMethod:@selector(tableView:viewForHeaderInSection:)];
    [self _jx_swizzleUITableViewDelegateMethod:@selector(tableView:viewForFooterInSection:)];
}

- (void)_jx_swizzleUITableViewDelegateMethod:(SEL)selector {
    if (!self.delegate) return;
    if (![self.delegate respondsToSelector:selector]) return;
    
    SEL selectors[] = {
        selector,
    };
    JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix([self.delegate class], selectors, @"_jx_ui_table_view_");
}

@end


@implementation NSIndexPath (JXUITableView)

- (BOOL)jx_isEqualToSection:(NSIndexPath *)indexPath {
    if (!indexPath) return NO;
    if (![indexPath isKindOfClass:[NSIndexPath class]]) return NO;
    if (self == indexPath) return YES;
    
    return self.section == indexPath.section;
}

- (BOOL)jx_isEqualToRow:(NSIndexPath *)indexPath {
    if (!indexPath) return NO;
    if (![indexPath isKindOfClass:[NSIndexPath class]]) return NO;
    if (self == indexPath) return YES;
    
    return self.row == indexPath.row;
}

- (NSIndexPath *)jx_indexPathByAddingSection:(NSInteger)section {
    return [self jx_indexPathByAddingRow:0 section:section];
}

- (NSIndexPath *)jx_indexPathByAddingRow:(NSInteger)row {
    return [self jx_indexPathByAddingRow:row section:0];
}

- (NSIndexPath *)jx_indexPathByAddingRow:(NSInteger)row section:(NSInteger)section {
    return [NSIndexPath indexPathForRow:self.row + row inSection:self.section + section];
}

@end


@interface NSObject (JXUITableViewDelegate)
@end

@implementation NSObject (JXUITableViewDelegate)

- (UIView *)_jx_ui_table_view_tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *view = [self _jx_ui_table_view_tableView:tableView viewForHeaderInSection:section];
    if (view) {
        [tableView._jx_headersForSections setObject:view forKey:@(section)];
    } else {
        [tableView._jx_headersForSections removeObjectForKey:@(section)];
    }
    return view;
}

- (UIView *)_jx_ui_table_view_tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
    UIView *view = [self _jx_ui_table_view_tableView:tableView viewForFooterInSection:section];
    if (view) {
        [tableView._jx_footersForSections setObject:view forKey:@(section)];
    } else {
        [tableView._jx_footersForSections removeObjectForKey:@(section)];
    }
    return view;
}

@end
