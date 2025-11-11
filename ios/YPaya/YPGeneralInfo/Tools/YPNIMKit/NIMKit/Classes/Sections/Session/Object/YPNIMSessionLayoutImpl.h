//
//  NIMSessionLayout.h
//  YPNIMKit
//
//  Created by chris on 2016/11/8.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "YPNIMSessionConfigurator.h"
#import "NIMSessionPrivateProtocol.h"

@interface YPNIMSessionLayoutImpl : NSObject<NIMSessionLayout>

@property (nonatomic,strong)  UITableView *tableView;

@property (nonatomic,strong)  YPNIMInputView *inputView;

- (instancetype)initWithSession:(NIMSession *)session
                         config:(id<NIMSessionConfig>)sessionConfig;

@end
