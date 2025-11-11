//
//  CUIWaterfallViewCell.h
//  MyWaterflow
//
//  Created by daixiang on 13-12-30.
//  Copyright (c) 2013年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CUIWaterfallViewCell : UIView

@property (nonatomic) NSUInteger index;
@property (nonatomic, copy) NSString *reuseIdentifier;

- (id)initWithReuseIdentifier:(NSString *)reuseIdentifier;

@end
