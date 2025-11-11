//
//  HJSendGoldVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/27.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
@class HJSendGoldModel;
NS_ASSUME_NONNULL_BEGIN

@interface HJSendGoldVC : UIViewController
@property (nonatomic,assign) UserID userID;
@property (nonatomic,strong)UserInfo * userInfo;
@property (nonatomic,assign) UserID recvUid;//收账用户uid
@property (nonatomic,copy) void (^sendGlodBlock)(HJSendGoldModel *model);

@end

NS_ASSUME_NONNULL_END
