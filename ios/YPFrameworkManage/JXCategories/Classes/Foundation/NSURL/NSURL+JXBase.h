//
//  NSURL+JXBase.h
//  JXCategories
//
//  Created by colin on 2019/2/2.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSURL (JXBase)

#pragma mark - Base
/**
 获取当前URL Query的参数列表
 */
@property (nonatomic, copy, readonly) NSDictionary<NSString *, NSString *> *jx_queryItems;

@end

NS_ASSUME_NONNULL_END
