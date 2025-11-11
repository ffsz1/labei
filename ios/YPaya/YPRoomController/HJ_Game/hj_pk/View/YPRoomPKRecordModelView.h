//
//  YPRoomPKRecordModelView.h
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import <UIKit/UIKit.h>

typedef void(^XBDRoomPKSureCloseBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKRecordModelView : UIView

@property (nonatomic,copy) XBDRoomPKSureCloseBlock closeBlock;

@end

NS_ASSUME_NONNULL_END
