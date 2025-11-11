//
//  SVGAPlayer+XCExtension.m
//  HJLive
//
//  Created by feiyin on 2020/5/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "SVGAPlayer+XCExtension.h"
#import <SDWebImage/SDWebImageManager.h>

@implementation SVGAPlayer (XCExtension)

- (void)xc_setImageWithURL:(NSURL *)URL forKey:(NSString *)aKey configureHandler:(UIImage* (^)(UIImage *image))configureHandler {
    [[SDWebImageManager sharedManager] loadImageWithURL:URL options:0 progress:nil completed:^(UIImage * _Nullable image, NSData * _Nullable data, NSError * _Nullable error, SDImageCacheType cacheType, BOOL finished, NSURL * _Nullable imageURL) {
        if (error == nil && image != nil) {
            if (configureHandler) {
                UIImage *buffer = configureHandler(image);
                if (buffer) {
                    image = buffer;
                }
            }
            if (image != nil) {
                [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                    [self setImage:image forKey:aKey];
                }];
            }
        }
    }];
}

@end
