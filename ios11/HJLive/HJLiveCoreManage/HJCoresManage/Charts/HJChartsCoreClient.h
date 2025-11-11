//
//  HJChartsCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol HJChartsCoreClient <NSObject>
@optional

- (void)onRequestAllrankListSuccessWithAllrankList:(NSArray *)allrankList type:(NSInteger)type datetype:(NSInteger)datetype;
- (void)onRequestAllrankFailthWithMsg:(NSString *)msg type:(NSInteger)type datetype:(NSInteger)datetype;

@end
