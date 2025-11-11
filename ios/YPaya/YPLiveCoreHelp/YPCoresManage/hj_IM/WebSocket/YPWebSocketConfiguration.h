//
//  YPWebSocketConfiguration.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPEnvironmentDefine.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPWebSocketConfiguration : YPBaseObject

@property (nonatomic, assign) JXCryptAlgorithm encryptType;
@property (nonatomic, copy) NSString *encryptKey;
@property (nonatomic, copy) NSString *encryptIv;

@property (nonatomic, assign) JXCryptAlgorithm decryptType;
@property (nonatomic, copy) NSString *decryptKey;
@property (nonatomic, copy) NSString *decryptIv;

@end

NS_ASSUME_NONNULL_END
