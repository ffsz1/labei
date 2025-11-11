//
//  HJRoomPKSureView.h
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import <UIKit/UIKit.h>

#import "HJRoomPKJoinModel.h"

typedef void(^XBDRoomPKSureCloseBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomPKSureView : UIView

@property (nonatomic,copy) XBDRoomPKSureCloseBlock closeBlock;

@property (nonatomic,strong) HJRoomPKJoinModel *joinModel;


@end

NS_ASSUME_NONNULL_END
