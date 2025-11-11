//
//  GiftBillInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GiftBillInfo : NSObject

//"targetNick":"小鱼",
//"goldNum":-10,
//"giftPic":"http://nim.nos.netease.com/NDI3OTA4NQ==/bmltd18wXzE0OTk5Mzg0NDU5ODZfNmUwN2Q5MDMtOWI1OC00YWJmLWE1MzItOWE4MTY1OWEyYjM2",
//"giftNum":1,
//"recordTime":1504751506000,
//"gainType":5

//gainType:6

@property (copy, nonatomic) NSString *targetNick;
@property (assign, nonatomic) NSString *goldNum;
@property (assign ,nonatomic) NSString *diamondNum;
@property (copy, nonatomic) NSString *giftPict;
@property (assign, nonatomic) NSInteger giftNum;
@property (copy, nonatomic) NSString *giftName;
@property (assign, nonatomic) NSInteger recordTime;
//@property (assign, nonatomic) NSInteger gainType;
//@property (assign, nonatomic) NSInteger expendType;

@end
