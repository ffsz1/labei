//
//  GGImageView.h
//  HJLive
//
//  Created by apple on 2018/10/23.
//  Copyright © 2018 XC. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GGImageView : UIImageView

@property (nonatomic, assign) IBInspectable CGFloat connerRadius;
@property (nonatomic, assign) IBInspectable CGFloat borderWidth;
@property (nonatomic, assign) IBInspectable UIColor *borderColor;
@property (nonatomic, assign) IBInspectable BOOL masksToBounds;

@end

NS_ASSUME_NONNULL_END
