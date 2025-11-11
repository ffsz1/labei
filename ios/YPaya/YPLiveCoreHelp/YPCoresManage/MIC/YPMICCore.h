//
//  YPMICCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

@interface YPMICCore : YPBaseCore

/**
 获取连麦池用户数
 */
- (void)getLinkPool;

- (void)getMICLinkUser;

//获取魅力用户列表
- (void)getCharmUserList;

- (void)getRandomUserList;
@end
