//
//  YPFYBankInfoListModel.h
//  XBD
//
//  Created by feiyin on 2019/11/26.
//银行信息列表模型

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPFYBankInfoListModel : NSObject
@property(nonatomic, strong) NSString *bankName;
@property(nonatomic, strong) NSString *openBankCode;
@property(nonatomic, strong) NSString *bankCard;

@end

NS_ASSUME_NONNULL_END
