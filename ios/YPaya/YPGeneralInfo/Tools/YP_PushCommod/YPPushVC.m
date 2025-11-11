//
//  HJHJPushVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPushVC.h"
#import "YPWKWebViewController.h"
#import <objc/runtime.h>
#import "YPRankListVC.h"
#import "YPRoomViewControllerCenter.h"
#import "YPVersionCoreHelp.h"

@implementation YPPushVC

+ (instancetype)shared {
    static YPPushVC *push = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        push = [[self alloc] init];
    });
    return push;
}

- (BOOL)checkIsExistPropertyWithInstance:(id)instance verifyPropertyName:(NSString *)verifyPropertyName
{
    unsigned int outCount, i;
    // 获取对象里的属性列表
    objc_property_t * properties = class_copyPropertyList([instance
                                                           class], &outCount);
    for (i = 0; i < outCount; i++) {
        objc_property_t property =properties[i];
        //  属性名转成字符串
        NSString *propertyName = [[NSString alloc] initWithCString:property_getName(property) encoding:NSUTF8StringEncoding];
        // 判断该属性是否存在
        if ([propertyName isEqualToString:verifyPropertyName]) {
            free(properties);
            return YES;
        }
    }
    free(properties);
    return NO;
}

- (void)pushWithJson:(id)json withvc:(UIViewController *)vc {
    @try {
        NSString *url = json[@"skipUri"];
        
        NSDictionary *jsonDict = [JSONTools ll_dictionaryWithJSON:json];
        if ([jsonDict containsObjectForKey:@"skipUrl"]) {
            url = jsonDict[@"skipUrl"];
        }
        
        NSInteger type = [json[@"skipType"] integerValue];
        NSString *iosActivity = json[@"iosActivity"];
        NSDictionary *params = [JSONTools ll_dictionaryWithJSON:json[@"params"]];
        NSLog(@"%@",json[@"title"]);
        
        NSString *title = json[@"title"];
        
        if (type == 1) {
            
            
            if ([iosActivity isEqualToString:@"XCChartsContainerViewController"]) {
                //排行榜
                YPRankListVC *jumpVC = YPHomeStoryBoard(@"YPRankListVC");
                [vc.navigationController pushViewController:jumpVC animated:YES];
                return;
            }
            
            //app页面
            Class name = NSClassFromString(iosActivity);
            if (name) {
                
                    UIViewController *activity = name.new;
                    activity.hidesBottomBarWhenPushed = YES;
                    [self configParamsWith:params withVc:activity];
                    [vc.navigationController pushViewController:activity animated:YES];
            }
        }else if (type == 2){
            //聊天室
//            url
            [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
            [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:url.userIDValue succ:^(YPChatRoomInfo *roomInfo) {
                if (roomInfo != nil) {
                    [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
                }else {
                    [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
                }
            } fail:^(NSString *errorMsg) {
                [MBProgressHUD showError:errorMsg];
            }];
            
        }else if (type == 3){
            //h5页面
            if (url.length > 0) {
                url = [url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                YPWKWebViewController *web = [[YPWKWebViewController alloc]init];
                web.url = [NSURL URLWithString:url];
                [params enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
                    if ([self checkIsExistPropertyWithInstance:web verifyPropertyName:key]) {//如果传过来的属性  包含在 property_t里面 证明 该对象有这个属性  然后赋值
                        [web setValue:obj forKey:key];
                    }
                }];
                web.hidesBottomBarWhenPushed = YES;
                [vc.navigationController pushViewController:web animated:YES];
            }
        }
        
        

    } @catch (NSException *exception) {}
}

- (void)pushWithModel:(YPHomeIcons *)icon withvc:(UIViewController *)vc
{
    @try {
        if (icon.skipType == 1) {
            //跳app页面
            Class name = NSClassFromString(icon.iosActivity);
            if (name) {
                if ([icon.iosActivity isEqualToString:@"XCChartsContainerViewController"]) {
                    //排行榜
                    YPRankListVC *jumpVC = YPHomeStoryBoard(@"YPRankListVC");
                    [vc.navigationController pushViewController:jumpVC animated:YES];
                } else {
                    UIViewController *activity = name.new;
                    activity.hidesBottomBarWhenPushed = YES;
                    [self configParamsWith:icon.params withVc:activity];
                    [vc.navigationController pushViewController:activity animated:YES];
                }
            }
        }else if (icon.skipType == 2){
            //聊天室
        }else if (icon.skipType == 3){
            //web
            if (icon.url.length > 0) {
                icon.url = [icon.url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                YPWKWebViewController *web = [[YPWKWebViewController alloc]init];
                web.url = [NSURL URLWithString:icon.url];
                [icon.params enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
                    if ([self checkIsExistPropertyWithInstance:web verifyPropertyName:key]) {//如果传过来的属性  包含在 property_t里面 证明 该对象有这个属性  然后赋值
                        [web setValue:obj forKey:key];
                    }
                }];
                web.hidesBottomBarWhenPushed = YES;
                [vc.navigationController pushViewController:web animated:YES];
            }
        }
    } @catch (NSException *exception) {} @finally {}
}

- (void)configParamsWith:(NSDictionary *)params
                  withVc:(UIViewController *)vc {
    @weakify(self);
    [params enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
        @strongify(self);
        if ([self checkIsExistPropertyWithInstance:vc verifyPropertyName:key]) {//如果传过来的属性  包含在 property_t里面 证明 该对象有这个属性  然后赋值
            [vc setValue:obj forKey:key];
        }
    }];
}


@end
