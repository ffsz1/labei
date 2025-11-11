//
//  YPReportItemView.h
//  YPReportItemView
//
//  Created by apple on 2019/6/26.
//  Copyright © 2019 apple. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^BlackListCancelFollow)(BOOL isFoloow);


#define HJReportItemViewAnimationTime 0.3f

NS_ASSUME_NONNULL_BEGIN

@interface YPReportItemView : UIView

@property (nonatomic,assign) UserID uid;

@property (nonatomic,copy) BlackListCancelFollow roomOwnerFollowBlock;
@property (nonatomic,copy) BlackListCancelFollow spaceCardViewCancelBlock;




- (void)show;

@end

NS_ASSUME_NONNULL_END
