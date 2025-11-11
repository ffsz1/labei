//
//  UIImageView+QiNiu.h
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Const.h"

@interface UIImageView (QiNiu)
- (void)qn_setImageImageWithUrl:(NSString *)url placeholderImage:(NSString *)imageName type:(ImageType)type;

- (void)qn_setImageImageWithUrl:(NSString *)url placeholderImage:(NSString *)imageName type:(ImageType)type success:(void (^)(UIImage *image))success;
@end
