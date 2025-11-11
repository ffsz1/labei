//
//  HJRoomRankTopView.h
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import <UIKit/UIKit.h>

typedef void(^XBDRoomRankTopCardBlock)(UserID);

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomRankTopView : UIView

@property (assign,nonatomic) BOOL isCharm;

@property (nonatomic,strong) NSMutableArray *dataArr;

@property (copy,nonatomic) XBDRoomRankTopCardBlock cardBlock;


@end

NS_ASSUME_NONNULL_END
