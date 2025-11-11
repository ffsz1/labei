//
//  FYGetCashInfoModel.h
//  XBD
//
//  Created by feiyin on 2019/10/31.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FYGetCashInfoModel : BaseObject

@property(nonatomic, assign) long cashNum;//100
@property(nonatomic, assign) long cashProdId;
@property(nonatomic, strong) NSString *cashProdName;
@property(nonatomic, assign) long diamondNum;//1000
@property(nonatomic, assign) long seqNo;

//cashNum = 100;
//   cashProdId = 1;
//   cashProdName = "1000\U94bb=\Uffe5100";
//   diamondNum = 1000;
//   seqNo = 1;



@end
@interface FYRedPacketGetCashInfoModel : BaseObject

@property(nonatomic, assign) long packetId;
@property(nonatomic, assign) long packetNum;
@property(nonatomic, assign) long prodStauts;
@property(nonatomic, assign) long seqNo;
@end

NS_ASSUME_NONNULL_END
