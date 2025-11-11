//
//  HJAvatarMicroViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

typedef enum : NSUInteger {
    XCAvatarMicroViewTypeAll,
    XCAvatarMicroViewTypeOne,
} XCAvatarMicroViewType;

#import <UIKit/UIKit.h>


@interface HJAvatarMicroViewCell : UICollectionViewCell

- (void)setPositionText:(NSString *)text type:(XCAvatarMicroViewType)type isSelect:(BOOL)isSelect;
- (void)setupAvatarUrl:(NSString *)url;
- (void)resetAvatarSelectStyle;

@end
