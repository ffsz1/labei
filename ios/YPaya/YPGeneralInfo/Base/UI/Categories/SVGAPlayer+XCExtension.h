//
//  SVGAPlayer+XCExtension.h
//  HJLive
//
//  Created by feiyin on 2020/5/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "SVGAPlayer.h"

@interface SVGAPlayer (XCExtension)

- (void)xc_setImageWithURL:(NSURL *)URL forKey:(NSString *)aKey configureHandler:(UIImage* (^)(UIImage *image))configureHandler;

@end
