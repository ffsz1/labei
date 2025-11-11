//
//  UICollectionViewFlowLayout+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UICollectionViewFlowLayout (JXBase)

#pragma mark - Base
@property (nonatomic) CGFloat jx_itemSizeWidth;             ///< itemSize的宽度值 -> self.itemSize.width
@property (nonatomic) CGFloat jx_itemSizeHeight;            ///< itemSize的高度值 -> self.itemSize.height
@property (nonatomic) CGFloat jx_estimatedItemSizeWidth;    ///< estimatedItemSize的宽度值 -> self.estimatedItemSize.width
@property (nonatomic) CGFloat jx_estimatedItemSizeHeight;   ///< estimatedItemSize的高度值 -> self.estimatedItemSize.height
@property (nonatomic) CGFloat jx_headerReferenceSizeWidth;  ///< headerReferenceSize的宽度值 -> self.headerReferenceSize.width
@property (nonatomic) CGFloat jx_headerReferenceSizeHeight; ///< headerReferenceSize的高度值 -> self.headerReferenceSize.height
@property (nonatomic) CGFloat jx_footerReferenceSizeWidth;  ///< footerReferenceSize的宽度值 -> self.footerReferenceSize.width
@property (nonatomic) CGFloat jx_footerReferenceSizeHeight; ///< footerReferenceSize的高度值 -> self.footerReferenceSize.height
@property (nonatomic) CGFloat jx_sectionInsetTop;           ///< sectionInset的上部值 -> self.sectonInset.top
@property (nonatomic) CGFloat jx_sectionInsetLeft;          ///< sectionInset的左部值 -> self.sectonInset.left
@property (nonatomic) CGFloat jx_sectionInsetBottom;        ///< sectionInset的下部值 -> self.sectonInset.bottom
@property (nonatomic) CGFloat jx_sectionInsetRight;         ///< sectionInset的右部值 -> self.sectonInset.right

@end

NS_ASSUME_NONNULL_END
