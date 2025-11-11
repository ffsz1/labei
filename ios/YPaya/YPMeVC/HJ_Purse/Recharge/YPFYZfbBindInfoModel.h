//
//  YPFYZfbBindInfoModel.h
//  XBD
//
//  Created by feiyin on 2019/12/13.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPFYZfbBindInfoModel : NSObject

@property(nonatomic,strong) NSString* nick;
@property(nonatomic,assign) BOOL isBindAlipay;
@property(nonatomic,strong) NSString* alipayAccount;
@property(nonatomic,strong) NSString* alipayAccountName;

//diamondNum = "30339.5";
//   erbanNo = 2146933;
//   goldNum = 2874;
//   isBindAlipay = 0;
//   nick = "\U560e\U560e";
//   token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDAxMDAyMDciLCJpYXQiOjE1NzYyMzM4MTksImV4cCI6MTU3NjIzNDQxOX0.T4lkuh9nZvSUJXF1n-CGjAL5TiALdpIGNNQ1IeAanC2KQVKBk9-PQI9sNpsnQgct198v7rkWVSDoO_ZqyeLhiQ";
//   uid = 100100207;
@end

NS_ASSUME_NONNULL_END
