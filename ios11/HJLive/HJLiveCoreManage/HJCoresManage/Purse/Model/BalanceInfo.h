//
//  BalanceInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface BalanceInfo : BaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, copy)NSString *goldNum;
@property(nonatomic, assign)NSInteger amount;
@property(nonatomic, copy) NSString *diamondNum;
@property(nonatomic, copy)NSString *depositNum;
@property (nonatomic, copy) NSString *drawUrl;
@property (nonatomic, copy) NSString *drawMsg;
@property(nonatomic, copy)NSString *conchNum;

@end
//提现页面用户信息模型
@interface DrawExchangeModel : BaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, copy)NSString *diamondNum;
@property(nonatomic, assign)NSInteger withDrawType;
@property(nonatomic, assign) BOOL isNotBoundPhone;
@property(nonatomic, assign)BOOL hasWx;



//diamondNum = "1838.2";
//   hasWx = 0;
//   isNotBoundPhone = 0;
//   uid = 100100207;
//   withDrawType = 1;


@end
@interface redPacketcashModel : BaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, assign)double packetNum;
@end
@interface ChargeGoldModel : BaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, assign)long goldNum;
@property(nonatomic, assign)long chargeGoldNum;
@property(nonatomic, assign) double diamondNum;
@property(nonatomic, assign)long depositNum;
@property (nonatomic, assign) long nobleGoldNum;
//chargeGoldNum = 87380;
//depositNum = 0;
//diamondNum = "1818.2";
//goldNum = 87380;
//nobleGoldNum = 0;
//uid = 100100207;
@end

@interface HJFinancialAccountDataModel : BaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, strong)NSString* alipayAccount;
@property(nonatomic, strong)NSString* alipayAccountName;
@property(nonatomic, strong)NSString* bankCard;
@property(nonatomic, strong)NSString* bankCardName;
@end
