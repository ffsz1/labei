//
//  FYBankBindInfoModel.h
//  XBD
//
//  Created by feiyin on 2019/11/26.
//绑定银行卡信息

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FYBankBindInfoModel : NSObject
@property(nonatomic, strong) NSString *bankCard;
@property(nonatomic, strong) NSString *bankCardName;
@property(nonatomic, strong) NSString *createTime;
@property(nonatomic, assign) long id;
@property(nonatomic, assign) BOOL isUse;
@property(nonatomic, strong) NSString *openBankCode;
@property(nonatomic, assign) long uid;

//bankCard = 3652142556321445;
//  bankCardName = "\U9189\U5367";
//  createTime = 1574755371000;
//  id = 7;
//  isUse = 0;
//  openBankCode = 0100;
//  uid = 100100207;
@end

NS_ASSUME_NONNULL_END
