//
//  conchWinningView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, HJConchWinType) {
    HJConchWinTypeFirst,//20
    HJConchWinTypeSecond,//200
    HJConchWinTypeThird//1000
};
typedef void(^TapConchWinTypeBlock)(HJConchWinType);
NS_ASSUME_NONNULL_BEGIN

@interface YPConchWinningView : UIView
@property (weak, nonatomic) IBOutlet UICollectionView *conchWinCollectionView;

@property (strong, nonatomic) NSMutableArray *tagList;
+ (void)showCall:(TapConchWinTypeBlock)tapConchWinTypeBlock;
@end

NS_ASSUME_NONNULL_END
