//
//  YPDaCallIntoModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPDaCallIntoModel : NSObject

@property (nonatomic, assign) UserID uid;
@property (nonatomic, assign) UserID roomUid;
@property (nonatomic,strong) NSString *targetUid;
@property (nonatomic,assign) NSInteger type;
@property (nonatomic,assign) NSInteger giftId;
@property (nonatomic,assign) NSInteger giftNum;
@property (nonatomic,strong) NSString* callName;
@property (nonatomic,strong) NSString* giftUrl;
@property (nonatomic,strong) NSString* sendName;
@property (nonatomic,strong) NSString* targetName;

@end

NS_ASSUME_NONNULL_END
