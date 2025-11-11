//
//  HJRoomRankView.h
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import <UIKit/UIKit.h>

typedef void(^XBDRoomRankCardBlock)(UserID);

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomRankView : UIView

+ (void)show:(XBDRoomRankCardBlock)cardBlock;

@end

NS_ASSUME_NONNULL_END
