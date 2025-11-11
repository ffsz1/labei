//
//  UICollectionViewFlowLayout+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UICollectionViewFlowLayout+JXBase.h"

@implementation UICollectionViewFlowLayout (JXBase)

#pragma mark - Base
- (CGFloat)jx_itemSizeWidth {
    return self.itemSize.width;
}

- (void)setJx_itemSizeWidth:(CGFloat)itemSizeWidth {
    CGSize itemSize = self.itemSize;
    itemSize.width = itemSizeWidth;
    self.itemSize = itemSize;
}

- (CGFloat)jx_itemSizeHeight {
    return self.itemSize.height;
}

- (void)setJx_itemSizeHeight:(CGFloat)itemSizeHeight {
    CGSize itemSize = self.itemSize;
    itemSize.height = itemSizeHeight;
    self.itemSize = itemSize;
}

- (CGFloat)jx_estimatedItemSizeWidth {
    return self.estimatedItemSize.width;
}

- (void)setJx_estimatedItemSizeWidth:(CGFloat)estimatedItemSizeWidth {
    CGSize estimatedItemSize = self.estimatedItemSize;
    estimatedItemSize.width = estimatedItemSizeWidth;
    self.estimatedItemSize = estimatedItemSize;
}

- (CGFloat)jx_estimatedItemSizeHeight {
    return self.estimatedItemSize.height;
}

- (void)setJx_estimatedItemSizeHeight:(CGFloat)estimatedItemSizeHeight {
    CGSize estimatedItemSize = self.estimatedItemSize;
    estimatedItemSize.height = estimatedItemSizeHeight;
    self.estimatedItemSize = estimatedItemSize;
}

- (CGFloat)jx_headerReferenceSizeWidth {
    return self.headerReferenceSize.width;
}

- (void)setJx_headerReferenceSizeWidth:(CGFloat)headerReferenceSizeWidth {
    CGSize headerReferenceSize = self.headerReferenceSize;
    headerReferenceSize.width = headerReferenceSizeWidth;
    self.headerReferenceSize = headerReferenceSize;
}

- (CGFloat)jx_headerReferenceSizeHeight {
    return self.headerReferenceSize.height;
}

- (void)setJx_headerReferenceSizeHeight:(CGFloat)headerReferenceSizeHeight {
    CGSize headerReferenceSize = self.headerReferenceSize;
    headerReferenceSize.height = headerReferenceSizeHeight;
    self.headerReferenceSize = headerReferenceSize;
}

- (CGFloat)jx_footerReferenceSizeWidth {
    return self.footerReferenceSize.width;
}

- (void)setJx_footerReferenceSizeWidth:(CGFloat)footerReferenceSizeWidth {
    CGSize footerReferenceSize = self.footerReferenceSize;
    footerReferenceSize.width = footerReferenceSizeWidth;
    self.footerReferenceSize = footerReferenceSize;
}

- (CGFloat)jx_footerReferenceSizeHeight {
    return self.footerReferenceSize.height;
}

- (void)setJx_footerReferenceSizeHeight:(CGFloat)footerReferenceSizeHeight {
    CGSize footerReferenceSize = self.footerReferenceSize;
    footerReferenceSize.height = footerReferenceSizeHeight;
    self.footerReferenceSize = footerReferenceSize;
}

- (CGFloat)jx_sectionInsetTop {
    return self.sectionInset.top;
}

- (void)setJx_sectionInsetTop:(CGFloat)sectionInsetTop {
    UIEdgeInsets sectionInset = self.sectionInset;
    sectionInset.top = sectionInsetTop;
    self.sectionInset = sectionInset;
}

- (CGFloat)jx_sectionInsetLeft
{
    return self.sectionInset.left;
}

- (void)setJx_sectionInsetLeft:(CGFloat)sectionInsetLeft
{
    UIEdgeInsets sectionInset = self.sectionInset;
    sectionInset.left = sectionInsetLeft;
    self.sectionInset = sectionInset;
}

- (CGFloat)jx_sectionInsetBottom
{
    return self.sectionInset.bottom;
}

- (void)setJx_sectionInsetBottom:(CGFloat)sectionInsetBottom
{
    UIEdgeInsets sectionInset = self.sectionInset;
    sectionInset.bottom = sectionInsetBottom;
    self.sectionInset = sectionInset;
}

- (CGFloat)jx_sectionInsetRight
{
    return self.sectionInset.right;
}

- (void)setJx_sectionInsetRight:(CGFloat)sectionInsetRight
{
    UIEdgeInsets sectionInset = self.sectionInset;
    sectionInset.right = sectionInsetRight;
    self.sectionInset = sectionInset;
}

@end
