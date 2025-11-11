//
//  YPSearchTagView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
@protocol SearchTagViewDelegate <NSObject>

@optional

-(void)handleSelectTag:(NSInteger)keyWordTag;

@end
@interface YPSearchTagView : UIView
@property (nonatomic ,weak)id <SearchTagViewDelegate>delegate;

@property (nonatomic ,strong)NSArray * arr;
@end

NS_ASSUME_NONNULL_END
