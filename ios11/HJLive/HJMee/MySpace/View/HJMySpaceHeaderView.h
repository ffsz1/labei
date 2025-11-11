//
//  HJMySpaceHeaderView.h
//  HJLive
//
//  Created by feiyin on 2020/5/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^XBDHeaderClick)(NSInteger index);

NS_ASSUME_NONNULL_BEGIN

@interface HJMySpaceHeaderView : UIView

@property (nonatomic,copy) XBDHeaderClick clickBlock;

- (void)updateNum:(NSInteger)num;

- (void)setSelIndex:(NSInteger)index;



@end

NS_ASSUME_NONNULL_END
