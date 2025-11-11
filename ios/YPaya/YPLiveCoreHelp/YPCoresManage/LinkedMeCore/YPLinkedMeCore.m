//
//  YPLinkedMeCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPLinkedMeCore.h"
#import <LinkedME_iOS/LinkedME.h>
#import "NSObject+YYModel.h"
#import "YPImLoginCore.h"
#import "HJLinkedMeClient.h"

#import "YPAuthCoreHelp.h"
#import "HJAuthCoreClient.h"

@interface YPLinkedMeCore ()<HJAuthCoreClient>

@end

@implementation YPLinkedMeCore


- (BOOL)judgeDeepLinkWith:(NSURL *)url {
    //判断是否是通过LinkedME的UrlScheme唤起App
    return [[LinkedME getInstance] handleDeepLink:url];
}

- (BOOL)judgeDeepLinkWithUniversal:(NSUserActivity *)userActivity {
    //判断是否是通过LinkedME的Universal Links唤起App
    return  [[LinkedME getInstance] continueUserActivity:userActivity];
}

- (void)initLinkedMEWithLaunchOptions:(NSDictionary *)launchOptions {
    //初始化及实例
    LinkedME* linkedme = [LinkedME getInstance];
    
    //    //注册需要跳转的viewController
    //    UIStoryboard * storyBoard=[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    //    DetailViewController  *dvc=[storyBoard instantiateViewControllerWithIdentifier:@"detailView"];
    
    //[自动跳转]如果使用自动跳转需要注册viewController
    //    [linkedme registerDeepLinkController:featureVC forKey:@"LMFeatureViewController"];

        //获取跳转参数
        [linkedme initSessionWithLaunchOptions:launchOptions automaticallyDisplayDeepLinkController:NO deepLinkHandler:^(NSDictionary *params, NSError* error) {
            if (!error) {
                //防止传递参数出错取不到数据,导致App崩溃这里一定要用try catch
                @try {
                    NSLog(@"LinkedME finished init with params = %@",[params description]);
                    //获取标题
                    NSString *title = [params objectForKey:@"$og_title"];
                    
                    NSDictionary *control = params[@"$control"];
                    if (![params[@"h5_url"] isEqualToString:@""]) {
                        NSString *channel = params[@"h5_url"];
                        self.H5URL = channel;
                    }
                    self.linkedmeChannel = control[@"linkedmeChannel"];
                    
                    if (title.length >0) {
                        YPLinkMEModel *linkme = [YPLinkMEModel yy_modelWithDictionary:control];
                        self.linkme = linkme;
                        for (NSString *channel in [params objectForKey:@"channel"]) {
                            self.channel = channel;
                        }
                        if ([linkme.type isEqualToString:@"2"]) {
                            NotifyCoreClient(HJLinkedMeClient, @selector(jumpInRoomWithRoomid:), jumpInRoomWithRoomid:linkme.roomuid);
                        }
                        
                        //[自动跳转]使用自动跳转
                        //SDK提供的跳转方法
                        /**
                         *  pushViewController : 类名
                         *  storyBoardID : 需要跳转的页面的storyBoardID
                         *  animated : 是否开启动画
                         *  customValue : 传参
                         *
                         *warning  需要在被跳转页中实现次方法 - (void)configureControlWithData:(NSDictionary *)data;
                         */
                        
                        //                    [LinkedME pushViewController:title storyBoardID:@"detailView" animated:YES customValue:@{@"tag":tag} completion:^{
                        ////
                        //                    }];
                        
                        //自定义跳转
                        //                    dvc.openUrl = params[@"$control"][@"ViewId"];
                        //                    [[LinkedME getViewController] showViewController:dvc sender:nil];
                        
                    }
                    
                } @catch (NSException *exception) {
                    
                } @finally {
                    
                }
                
            } else {
                NSLog(@"LinkedME failed init: %@", error);
            }
        }];
    
    //???
    [linkedme disableMatching];
    
}

//#pragma mark - AuthCoreClient
//
//- (void)onRegistSuccess {
//    NSString *url = GetCore(LinkedMeCore).H5URL;
//    [GetCore(AuthCore) statisticsWith:[NSURL URLWithString:url]];
//}


@end
