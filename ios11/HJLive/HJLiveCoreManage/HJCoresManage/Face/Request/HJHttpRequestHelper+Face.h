//
//  HJHttpRequestHelper+Face.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"

@interface HJHttpRequestHelper (Face)



/**
 获取表情

 @param success 成功
 @param failure 失败
 */
+ (void)getTheFaceListsuccess:(void (^)(NSArray *faceList))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;



/**
 获取表情Json

 @param success 成功
 @param failure 失败
 */
+ (void)getTheFaceJson:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure;


@end
