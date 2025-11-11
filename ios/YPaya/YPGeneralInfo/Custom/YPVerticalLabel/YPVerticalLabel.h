//
//  YPVerticalLabel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM (NSInteger ,HJVerticalAlignment){
    HJVerticalAlignmentTop = 0,  //上居中
    HJVerticalAlignmentMiddle, //中居中
    HJVerticalAlignmentBottom //低居中
};
NS_ASSUME_NONNULL_BEGIN

@interface YPVerticalLabel : UILabel
@property (nonatomic,assign)HJVerticalAlignment verticalAlignment;
@end

NS_ASSUME_NONNULL_END
