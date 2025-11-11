//
//  HJHomeTag.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
/*room/tag/top   home tag
 room/tag/all   room all tag
 "id":8,
 "name":"聊天",
 "pict":"https://img.erbanyy.com/tag%E8%81%8A%E5%A4%A9.png",
 "seq":3,
 "type":1,
 "status":true,
 "istop":true,
 "createTime":1511155717000
 */
@interface HJHomeTag : BaseObject
@property (nonatomic, assign) int id;  //quary use
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *pict;
@property (nonatomic, assign) int seq;
@property (nonatomic, assign) int type;
@property (nonatomic, assign) int status;
@property (nonatomic, assign) BOOL istop;
@property (nonatomic, copy) NSString *description;
@property (nonatomic, copy) NSString *tmpstr;
@property (nonatomic, assign) long createTime;
@property (nonatomic, copy) NSString *children;
@property (nonatomic, assign) NSInteger tmpint;
@end
