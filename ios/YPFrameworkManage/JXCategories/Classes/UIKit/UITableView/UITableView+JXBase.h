//
//  UITableView+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, JXUITableViewDeleteOrder) { ///< 删除顺序
    JXUITableViewDeleteOrderNone      = 0,             ///< 无序
    JXUITableViewDeleteOrderAscending = 1,             ///< 正序
    JXUITableViewDeleteOrderDecending = 2,             ///< 逆序
};

typedef NS_ENUM(NSInteger, JXUITableViewCellPosition) { ///< Cell所在的位置
    JXUITableViewCellPositionNone   = 0,                ///< 无
    JXUITableViewCellPositionTop    = 1 << 0,           ///< 顶部
    JXUITableViewCellPositionMiddle = 1 << 1,           ///< 中间范围
    JXUITableViewCellPositionBottom = 1 << 2,           ///< 底部
    JXUITableViewCellPositionSingle = JXUITableViewCellPositionTop | JXUITableViewCellPositionMiddle | JXUITableViewCellPositionBottom, ///< 单独
};

@interface UITableView (JXBase)

#pragma mark - Base
@property (nonatomic, readonly) NSInteger jx_firstSection; ///< 获取首个Section(无则返回-1)
@property (nonatomic, readonly) NSInteger jx_lastSection;  ///< 获取最末Section(无则返回-1)

/**
 获取指定Section的首行Row(无则返回-1)

 @param section Section
 @return 首行Row
 */
- (NSInteger)jx_firstRowInSection:(NSUInteger)section;

/**
 获取指定Section的最末Row(无则返回-1)

 @param section Section
 @return 获取指定Section的最末Row
 */
- (NSInteger)jx_lastRowInSection:(NSUInteger)section;

/**
 获取首个IndexPath(无则返回nil)
 */
@property (nullable, nonatomic, readonly) NSIndexPath *jx_firstIndexPath;

/**
 获取最末IndexPath(无则返回nil)
 */
@property (nullable, nonatomic, readonly) NSIndexPath *jx_lastIndexPath;

/**
 获取Section对应的首个IndexPath(无则返回nil)

 @param section Section
 @return Section对应的首个IndexPath(无则返回nil)
 */
- (nullable NSIndexPath *)jx_firstIndexPathInSection:(NSUInteger)section;

/**
 获取Section对应的最末IndexPath(无则返回nil)

 @param section Section
 @return Section对应的最末IndexPath(无则返回nil)
 */
- (nullable NSIndexPath *)jx_lastIndexPathInSection:(NSUInteger)section;

/**
 获取完整Sections中Rows的数量
 */
@property (nonatomic, readonly) NSInteger jx_numberOfRowsInSections;

/**
 获取完整Sections中Rows对应的IndexPath集(无则返回空数组)
 */
@property (nonatomic, readonly) NSArray<NSIndexPath *> *jx_indexPathsForRows;

/**
 获取指定Section中Rows对应的IndexPath集(无则返回空数组)

 @param section 指定Section
 @return 指定Section中Rows对应的IndexPath集
 */
- (NSArray<NSIndexPath *> *)jx_indexPathsForRowsInSection:(NSUInteger)section;

/**
 获取IndexPath范围内的IndexPath集(无则返回空数组)

 @param fromIndexPath IndexPath(nil则为首个IndexPath)
 @param toIndexPath IndexPath(nil则为最末IndexPath)
 @return IndexPath范围内的IndexPath集(无则返回空数组)
 */
- (NSArray<NSIndexPath *> *)jx_indexPathsForRowsFromIndexPath:(nullable NSIndexPath *)fromIndexPath toIndexPath:(nullable NSIndexPath *)toIndexPath;

/**
 是否包含指定Section

 @param section Section
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsSection:(NSUInteger)section;

/**
 是否包含指定Section集

 @param sections Section集
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsSections:(NSIndexSet *)sections;

/**
 指定Section是否包含指定Row

 @param row     Row
 @param section Section
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsRow:(NSUInteger)row inSection:(NSUInteger)section;

/**
 是否包含指定IndexPath(根据Section/Row判断)
 
 @param indexPath IndexPath
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsIndexPath:(NSIndexPath *)indexPath;

/**
 是否包含指定IndexPath集(根据Section/Row判断)

 @param indexPaths IndexPath集
 @return 包含返回YES, 否则返回NO
 */
- (BOOL)jx_containsIndexPaths:(NSArray<NSIndexPath *> *)indexPaths;

/**
 设置DataSource和Delegate执行者, 执行者须遵循<UITableViewDataSource, UITableViewDelegate>协议

 @param dataSourceDelegate DataSource和Delegate执行者
 */
- (void)jx_setupDataSourceDelegate:(id)dataSourceDelegate;

/**
 *  移除DataSource和Delegate执行者(nil)
 */
- (void)jx_removeDataSourceDelegate;

/**
 标记一个tableView的动画块,  增、删、选中rows或sections时使用, 协调UITableView的动画效果(在动画块内, 不建议使用reloadData方法, 会影响动画效果)

 @param block 动画块
 */
- (void)jx_updateWithBlock:(void(^)(UITableView *tableView))block;

/**
 获取Sectionf对应的Rows集的Rect

 @param section Sectionf
 @return Sectionf对应的Rows集的Rect
 */
- (CGRect)jx_rectForRowsInSection:(NSInteger)section;

/**
 获取Point对应的Header所在的Section(无则返回-1)

 @param point Point
 @return Point对应的Header所在的Section(无则返回-1)
 */
- (NSInteger)jx_sectionForHeaderAtPoint:(CGPoint)point;

/**
 获取Point对应的Footer所在的Section(无则返回-1)

 @param point Point
 @return Point对应的Footer所在的Section(无则返回-1)
 */
- (NSInteger)jx_sectionForFooterAtPoint:(CGPoint)point;

/**
 获取HeaderView对应的Section(HeaderView不可视/无则返回-1)

 @param headerView HeaderView
 @return HeaderView对应的Section(HeaderView不可视/无则返回-1)
 */
- (NSInteger)jx_sectionForHeaderView:(UIView *)headerView;

/**
 获取FooterView对应的Section(FooterView不可视/无则返回-1)

 @param footerView FooterView
 @return FooterView对应的Section(FooterView不可视/无则返回-1)
 */
- (NSInteger)jx_sectionForFooterView:(UIView *)footerView;

/**
 获取指定范围内, 对应的Header集所在的Section集(无则返回空数组)

 @param rect 指定范围
 @return 指定范围内, 对应的Header集所在的Section集(无则返回空数组)
 */
- (NSArray<NSNumber *> *)jx_sectionsForHeadersInRect:(CGRect)rect;

/**
 获取指定范围内, 对应的Footer集所在的Section集(无则返回空数组)

 @param rect 指定范围
 @return 指定范围内, 对应的Footer集所在的Section集(无则返回空数组)
 */
- (NSArray<NSNumber *> *)jx_sectionsForFootersInRect:(CGRect)rect;

/**
 获取在Row内的View对应的IndexPath(无则返回nil)

 @param view Cell内的View
 @return Cell内的View对应的IndexPath(无则返回nil)
 */
- (nullable NSIndexPath *)jx_indexPathForViewInRow:(UIView *)view;

/**
 获取在Header内的View对应的Section(无则返回-1)

 @param view Header内的View
 @return Header内的View对应的Section(无则返回-1)
 */
- (NSInteger)jx_sectionForViewInHeader:(UIView *)view;

/**
 获取在Footer内的View对应的Section(无则返回-1)
 
 @param view Footer内的View
 @return Footer内的View对应的Section(无则返回-1)
 */
- (NSInteger)jx_sectionForViewInFooter:(UIView *)view;

/**
 获取Section对应的HeaderView(无则返回nil)

 @param section Section
 @return Section对应的HeaderView(无则返回nil)
 */
- (nullable UIView *)jx_headerViewForSection:(NSUInteger)section;

/**
 获取Section对应的FooterView(无则返回nil)
 
 @param section Section
 @return Section对应的FooterView(无则返回nil)
 */
- (nullable UIView *)jx_footerViewForSection:(NSUInteger)section;

#pragma mark - Estimated Height
//@property (nonatomic, assign) BOOL jx_estimatedRowHeightEnabled;           ///< 是否开启Row预估高度
//@property (nonatomic, assign) BOOL jx_estimatedSectionHeaderHeightEnabled; ///< 是否开启Section Header预估高度
//@property (nonatomic, assign) BOOL jx_estimatedSectionFooterHeightEnabled; ///< 是否开启Section Header预估高度

#pragma mark - Stick
/**
 判断Section对应的Header是否粘附在顶部(Style须为UITableViewStylePlain, Header将要进入/离开顶部为非粘附状态)

 @param section Section
 @return 粘附返回YES, 否则返回NO
 */
- (BOOL)jx_isStickyHeaderForSection:(NSUInteger)section;

/**
 判断Section对应的Footer是否粘附在底部(Style须为UITableViewStylePlain, Footer将要进入/离开顶部为非粘附状态)

 @param section Section
 @return 粘附返回YES, 否则返回NO
 */
- (BOOL)jx_isStickyFooterForSection:(NSUInteger)section;

/**
 获取粘附在顶部的Header对应的Section(Style须为UITableViewStylePlain, Header将要进入/离开顶部为非粘附状态, 无则返回-1)

 @return 粘附在顶部的Header对应的Section(Style须为UITableViewStylePlain, Header将要进入/离开顶部为非粘附状态, 无则返回-1)
 */
- (NSInteger)jx_sectionForStickyHeader;

/**
 获取粘附在底部的Footer对应的Section(Style须为UITableViewStylePlain, Footer将要进入/离开底部为非粘附状态, 无则返回-1)

 @return 粘附在底部的Footer对应的Section(Style须为UITableViewStylePlain, Footer将要进入/离开底部为非粘附状态, 无则返回-1)
 */
- (NSInteger)jx_sectionForStickyFooter;

#pragma mark - Visible
/**
 判断IndexPath对应的Row是否可视
 
 @param indexPath IndexPath
 @return 可视返回YES, 否则返回NO
 */
- (BOOL)jx_isVisibleRowForIndexPath:(NSIndexPath *)indexPath;

/**
 判断Section对应的Header是否可视
 
 @param section Section
 @return 可视返回YES, 否则返回NO
 */
- (BOOL)jx_isVisibleHeaderInSection:(NSUInteger)section;

/**
 判断Section对应的Footer是否可视
 
 @param section 判断Section对应的Header是否可视
 @return 可视返回YES, 否则返回NO
 */
- (BOOL)jx_isVisibleFooterInSection:(NSUInteger)section;

/**
 获取可视Row集对应的IndexPath集(类似`indexPathsForVisibleRows`, 升序排序, 无则返回空数组)
 */
@property (nonatomic, readonly) NSArray<NSIndexPath *> *jx_indexPathsForVisibleRows;

/**
  获取可视Header集对应的Section集(无则返回空数组)
 */
@property (nonatomic, readonly) NSArray<NSNumber *> *jx_sectionsForVisibleHeaders;

/**
 获取可视Footer集对应的Section集(无则返回空数组)
 */
@property (nonatomic, readonly) NSArray<NSNumber *> *jx_sectionsForVisibleFooters;

/**
 获取首个可视Row对应的IndexPath(无则返回nil)
 */
@property (nullable, nonatomic, readonly) NSIndexPath *jx_indexPathForFirstVisibleRow;

/**
 获取最末可视Row对应的IndexPath(无则返回nil)
 */
@property (nullable, nonatomic, readonly) NSIndexPath *jx_indexPathForLastVisibleRow;

/**
 获取首个可视Header对应的Section(无则返回-1)
 */
@property (nonatomic, readonly) NSInteger jx_sectionForFirstVisibleHeader;

/**
 获取最末可视Header对应的Section(无则返回-1)
 */
@property (nonatomic, readonly) NSInteger jx_sectionForLastVisibleHeader;

/**
 获取首个可视Footer对应的Section(无则返回-1)
 */
@property (nonatomic, readonly) NSInteger jx_sectionForFirstVisibleFooter;

/**
 获取最末可视Footer对应的Section(无则返回-1)
 */
@property (nonatomic, readonly) NSInteger jx_sectionForLastVisibleFooter;

#pragma mark - Scroll
/**
 滑动到指定的Section和Row(滑动到Row顶部, 默认动画, 越界则不执行)

 @param row     Row
 @param section Section
 */
- (void)jx_scrollToRow:(NSUInteger)row inSection:(NSUInteger)section;

/**
 *  滑动到指定的Section和Row(滑动到Row顶部, 越界则不执行)
 *
 *  @param row      Row
 *  @param section  Section
 *  @param animated 动画
 */
- (void)jx_scrollToRow:(NSUInteger)row
             inSection:(NSUInteger)section
              animated:(BOOL)animated;

/**
 滑动到指定的Section和Row(越界则不执行)

 @param row            Row
 @param section        Section
 @param scrollPosition 滑动位置
 @param animated       动画
 */
- (void)jx_scrollToRow:(NSUInteger)row
             inSection:(NSUInteger)section
      atScrollPosition:(UITableViewScrollPosition)scrollPosition
              animated:(BOOL)animated;

/**
 滑动到指定的IndexPath位置(类似`scrollToRowAtIndexPath:atScrollPosition:animated:`, 越界则不执行)

 @param indexPath IndexPath
 @param scrollPosition 滑动位置
 @param animated 动画
 */
- (void)jx_scrollToRowAtIndexPath:(NSIndexPath *)indexPath atScrollPosition:(UITableViewScrollPosition)scrollPosition animated:(BOOL)animated;

#pragma mark - Insert
/**
 根据指定的Section和Row, 插入Row(无动画, 越界则不执行)

 @param row     Row
 @param section Section
 */
- (void)jx_insertRow:(NSUInteger)row inSection:(NSUInteger)section;

/**
 *  根据指定的Section和Row, 插入Row(越界则不执行)
 *
 *  @param row       Row
 *  @param section   Section
 *  @param animation 动画
 */
- (void)jx_insertRow:(NSUInteger)row
           inSection:(NSUInteger)section
    withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的IndexPath, 插入Row(无动画, 越界则不执行)

 @param indexPath IndexPath
 */
- (void)jx_insertRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 根据指定的IndexPath, 插入Row(越界则不执行)

 @param indexPath IndexPath
 @param animation 动画
 */
- (void)jx_insertRowAtIndexPath:(NSIndexPath *)indexPath withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的IndexPath集, 插入Row(类似`insertRowsAtIndexPaths:withRowAnimation`, 越界则不执行)

 @param indexPaths IndexPath集
 @param animation 动画
 */
- (void)jx_insertRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的Section, 插入Section(无动画, 越界则不执行)

 @param section Section
 */
- (void)jx_insertSection:(NSUInteger)section;

/**
 根据指定的Section, 插入Section(越界则不执行)

 @param section   Section
 @param animation 动画
 */
- (void)jx_insertSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的Section集, 插入Section(类似`insertSections:withRowAnimation:`, 越界则不执行)
 
 @param sections  Section集
 @param animation 动画
 */
- (void)jx_insertSections:(NSIndexSet *)sections withRowAnimation:(UITableViewRowAnimation)animation;

#pragma mark - Delete
/**
 根据指定的Section和Row, 删除Row(无动画, 越界则不执行)

 @param row     Row
 @param section Section
 */
- (void)jx_deleteRow:(NSUInteger)row inSection:(NSUInteger)section;

/**
 根据指定的Section和Row, 删除Row(越界则不执行)

 @param row       Row
 @param section   Section
 @param animation 动画
 */
- (void)jx_deleteRow:(NSUInteger)row
           inSection:(NSUInteger)section
    withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据IndexPath, 删除Row(无动画, 越界则不执行)

 @param indexPath IndexPath
 */
- (void)jx_deleteRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 根据IndexPath, 删除Row(越界则不执行)

 @param indexPath IndexPath
 @param animation 动画
 */
- (void)jx_deleteRowAtIndexPath:(NSIndexPath *)indexPath withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据IndexPaths集, 删除Row(无序/正序/逆序删除, 越界则不执行)

 @param indexPaths  IndexPaths集
 @param deleteOrder 删除顺序
 @param animation   动画
 */
- (void)jx_deleteRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths
                      deleteOrder:(JXUITableViewDeleteOrder)deleteOrder
                 withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据IndexPaths集, 删除Row(类似`deleteRowsAtIndexPaths:withRowAnimation:`, 越界则不执行)

 @param indexPaths IndexPaths集
 @param animation 动画
 */
- (void)jx_deleteRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的Section, 删除Section(无动画, 越界则不执行)

 @param section Section
 */
- (void)jx_deleteSection:(NSUInteger)section;

/**
 根据指定的Section, 删除Section(越界则不执行)

 @param section   Section
 @param animation 动画
 */
- (void)jx_deleteSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的Section集, 删除Section(类似`deleteSections:withRowAnimation:`, 越界则不执行)

 @param sections Section集
 @param animation 动画
 */
- (void)jx_deleteSections:(NSIndexSet *)sections withRowAnimation:(UITableViewRowAnimation)animation;

#pragma mark - Reload
/**
 根据指定的Section和Row, 刷新Row(无动画, 越界则不执行)

 @param row     Row
 @param section Section
 */
- (void)jx_reloadRow:(NSUInteger)row inSection:(NSUInteger)section;

/**
 根据指定的Section和Row, 刷新Row(越界则不执行)

 @param row Row
 @param section Section
 @param animation 动画
 */
- (void)jx_reloadRow:(NSUInteger)row
           inSection:(NSUInteger)section
    withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的IndexPath, 刷新Row(无动画, 越界则不执行)

 @param indexPath IndexPath
 */
- (void)jx_reloadRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 根据指定的IndexPath, 刷新Row(越界则不执行)

 @param indexPath IndexPath
 @param animation 动画
 */
- (void)jx_reloadRowAtIndexPath:(NSIndexPath *)indexPath withRowAnimation:(UITableViewRowAnimation)animation;

/**
 刷新当前可视的所有Rows(无动画, 越界则不执行)
 */
- (void)jx_reloadVisibleRows;

/**
 刷新当前可视的所有Rows(越界则不执行)

 @param animation 动画
 */
- (void)jx_reloadVisibleRowsWithRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的IndexPath集, 刷新Row(类似`reloadRowsAtIndexPaths:withRowAnimation:`, 越界则不执行)

 @param indexPaths IndexPath集
 @param animation 动画
 */
- (void)jx_reloadRowsAtIndexPaths:(NSArray<NSIndexPath *> *)indexPaths withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的Section, 刷新Section(无动画, 越界则不执行)

 @param section Section
 */
- (void)jx_reloadSection:(NSUInteger)section;

/**
 根据指定的Section, 刷新Section(越界则不执行)

 @param section   Section
 @param animation 动画
 */
- (void)jx_reloadSection:(NSUInteger)section withRowAnimation:(UITableViewRowAnimation)animation;

/**
 根据指定的Section集, 刷新Section(类似`reloadSections:withRowAnimation:`, 越界则不执行)

 @param sections Section集
 @param animation 动画
 */
- (void)jx_reloadSections:(NSIndexSet *)sections withRowAnimation:(UITableViewRowAnimation)animation;

#pragma mark - Select
/**
 清除所有选中Rows的选中状态

 @param animated 动画
 */
- (void)jx_clearSelectedRows:(BOOL)animated;

#pragma mark - Position
/**
 获取IndexPath对应的Row在所有Rows中的位置(无则返回JXUITableViewCellPositionNone)
 
 @param indexPath IndexPath
 @return IndexPath对应的Row在完整Sections中的位置(无则返回JXUITableViewCellPositionNone)
 */
- (JXUITableViewCellPosition)jx_positionForRowInRows:(NSIndexPath *)indexPath;

/**
 获取IndexPath对应的Row在当前Section的位置(无则返回JXUITableViewCellPositionNone)

 @param indexPath IndexPath
 @return IndexPath对应的Row在当前Section的位置(无则返回JXUITableViewCellPositionNone)
 */
- (JXUITableViewCellPosition)jx_positionForRowInSection:(NSIndexPath *)indexPath;

/**
 获取Row在所有Rows中的位置对应的IndexPath集(无则返回空数组)

 @param postion Row在所有Rows中的位置
 @return Row在所有Rows中的位置对应的IndexPath集(无则返回空数组)
 */
- (NSArray<NSIndexPath *> *)jx_indexPathsForPositionInRows:(JXUITableViewCellPosition)postion;

/**
 获取Row在当前Section的位置对应的IndexPath集(无则返回空数组)

 @param postion Row在当前Section的位置
 @param section Section
 @return Row在当前Section的位置对应的IndexPath集(无则返回空数组)
 */
- (NSArray<NSIndexPath *> *)jx_indexPathsForPosition:(JXUITableViewCellPosition)postion inSection:(NSUInteger)section;

@end


@interface NSIndexPath (JXUITableView)

/**
 判断两个IndexPath的Section是否相等

 @param indexPath 另一个IndexPath
 @return 相等返回YES, 否则返回NO
 */
- (BOOL)jx_isEqualToSection:(NSIndexPath *)indexPath;

/**
 判断两个IndexPath的Row是否相等

 @param indexPath 另一个IndexPath
 @return 相等返回YES, 否则返回NO
 */
- (BOOL)jx_isEqualToRow:(NSIndexPath *)indexPath;

/**
 指定添加Section数, 获取新IndexPath(Row不变)

 @param section 添加Section数
 @return 新IndexPath(Row不变)
 */
- (NSIndexPath *)jx_indexPathByAddingSection:(NSInteger)section;

/**
 指定添加Row数, 获取新的IndexPath(Section不变)

 @param row 添加Row数
 @return 新IndexPath(Section不变)
 */
- (NSIndexPath *)jx_indexPathByAddingRow:(NSInteger)row;

/**
 指定添加Row数和Section数, 获取新的IndexPath

 @param row 添加Row数
 @param section 添加Section数
 @return 新IndexPath
 */
- (NSIndexPath *)jx_indexPathByAddingRow:(NSInteger)row section:(NSInteger)section;

@end

NS_ASSUME_NONNULL_END
