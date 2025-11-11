//
//  HJWebSocketConfiguration.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJWebSocketConfiguration.h"

@implementation HJWebSocketConfiguration

- (instancetype)init {
    self = [super init];
    if (self) {
        _encryptType = JXCryptAlgorithmAES;//life-hj
//         _encryptType = JXCryptAlgorithmUndefine;
        _encryptKey = JX_AES_ENCRYPT_KEY;
        _encryptIv = JX_AES_ENCRYPT_IV;

        _decryptType = JXCryptAlgorithmAES;
//         _decryptType = JXCryptAlgorithmUndefine;
        _decryptKey = JX_AES_DECRYPT_KEY;
        _decryptIv = JX_AES_DECRYPT_IV;
    }
    return self;
}

@end
