//
//  YPRoomPKSureView.h
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import <UIKit/UIKit.h>

#import "YPRoomPKJoinModel.h"

typedef void(^XBDRoomPKSureCloseBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKSureView : UIView

@property (nonatomic,copy) XBDRoomPKSureCloseBlock closeBlock;

@property (nonatomic,strong) YPRoomPKJoinModel *joinModel;


@end

NS_ASSUME_NONNULL_END
