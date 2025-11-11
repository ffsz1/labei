//
//  HJHttpRequestHelper+Face.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Face.h"
#import "NSObject+YYModel.h"
#import "FaceInfo.h"
#import "HJFaceConfigInfo.h"
#import "YYWebResourceDownloader.h"
#import "HJFaceCore.h"
#import "NSString+JsonToDic.h"
#import "DESEncrypt.h"
#import "HJAdCore.h"

#define DESSalt @"1ea53d260ecf11e7b56e00163e046a26"

@implementation HJHttpRequestHelper (Face)

+ (void)getTheFaceListsuccess:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"face/list";
    [HJHttpRequestHelper GET:method params:nil success:^(id data) {
        NSArray *listData = data[@"faces"];
        NSArray *faces = [NSArray yy_modelArrayWithClass:[FaceInfo class] json:listData];
        success(faces);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

+ (void)getTheFaceJson:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"client/init";
    [HJHttpRequestHelper GET:method params:nil success:^(id data) {
        NSString *json = data[@"faceJson"][@"json"];
        GetCore(HJAdCore).splash = [AdInfo yy_modelWithJSON:data[@"splashVo"]];
//        NSString *deJson = [DESEncrypt decryptUseDES:json key:@"1ea53d260ecf11e7b56e00163e046a26"];
        NSString *deJson = [DESEncrypt decryptUseDES:json key:@"VvXh6qc55O292wdw68cvgQ"];
        
        
        data = [NSString dictionaryWithJsonString:deJson];
        if (data) {
            NSArray *arr = [NSArray yy_modelArrayWithClass:[HJFaceConfigInfo class] json:data[@"faces"]];
            GetCore(HJFaceCore).version = [NSString stringWithFormat:@"%@",data[@"version"]];
            GetCore(HJFaceCore).zipMd5 = [[NSString stringWithFormat:@"%@",data[@"zipMd5"]] uppercaseString];
            GetCore(HJFaceCore).zipUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@",data[@"zipUrl"]]];
            
            success(arr);
            
        }else {
            failure(@(999999),NSLocalizedString(HttpRequstDataDecodeFailed, nil));
        }
        } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}



@end
