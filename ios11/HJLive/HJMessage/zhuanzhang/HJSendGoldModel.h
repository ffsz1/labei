//
//  HJSendGoldModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJSendGoldModel : NSObject
@property (nonatomic,assign)long goldNum;
@property (nonatomic,assign)long recvUid;
@property (nonatomic,assign)long sendUid;
@property (nonatomic,strong)NSString* recvAvatar;
@property (nonatomic,strong)NSString* sendAvatar;
@property (nonatomic,strong)NSString* sendName;
@property (nonatomic,strong)NSString* recvName;
@end

NS_ASSUME_NONNULL_END
