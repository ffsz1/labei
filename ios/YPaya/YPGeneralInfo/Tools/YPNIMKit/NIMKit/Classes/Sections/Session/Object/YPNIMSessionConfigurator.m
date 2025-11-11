//
//  YPNIMSessionConfigurator.m
//  YPNIMKit
//
//  Created by chris on 2016/11/7.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "YPNIMSessionConfigurator.h"
#import "YPNIMSessionMsgDatasource.h"
#import "YPNIMSessionInteractorImpl.h"
#import "YPNIMCustomLeftBarView.h"
#import "UIView+NIM.h"
#import "YPNIMMessageModel.h"
#import "NIMGlobalMacro.h"
#import "YPNIMSessionInteractorImpl.h"
#import "YPNIMSessionDataSourceImpl.h"
#import "YPNIMSessionLayoutImpl.h"
#import "YPNIMSessionTableAdapter.h"
/*
                                            YPNIMSessionViewController 类关系图
 
 
             .........................................................................
             .                                                                       .
             .                                                                       .
             .                                                                       .                  | ---> [NIMSessionDatasource]
             .                                                                       .
             .                                                       | ---> [NIMSessionInteractor] -->  |
             .
             .                                                                                          | ---> [NIMSessionLayout]
             .
             ↓
  [YPNIMSessionViewController]-------> [YPNIMSessionConfigurator] -----> |
             |
             |
             |
             |
             ↓                                                       | ---> [YPNIMSessionTableAdapter]
       [UITableView]                                                              .
            ↑                                                                     .
            .                                                                     .
            .                                                                     .
            .......................................................................
 */

@interface YPNIMSessionConfigurator()

@property (nonatomic,strong) YPNIMSessionInteractorImpl   *interactor;

@property (nonatomic,strong) YPNIMSessionTableAdapter     *tableAdapter;

@end

@implementation YPNIMSessionConfigurator

- (void)setup:(YPNIMSessionViewController *)vc
{
    NIMSession *session    = vc.session;
    id<NIMSessionConfig> sessionConfig = vc.sessionConfig;
    UITableView *tableView  = vc.tableView;
    YPNIMInputView *inputView = vc.sessionInputView;
    
    YPNIMSessionDataSourceImpl *datasource = [[YPNIMSessionDataSourceImpl alloc] initWithSession:session config:sessionConfig];
    YPNIMSessionLayoutImpl *layout         = [[YPNIMSessionLayoutImpl alloc] initWithSession:session config:sessionConfig];
    layout.tableView = tableView;
    layout.inputView = inputView;
    
    
    _interactor                          = [[YPNIMSessionInteractorImpl alloc] initWithSession:session config:sessionConfig];
    _interactor.delegate                 = vc;
    _interactor.dataSource               = datasource;
    _interactor.layout                   = layout;
    
    [layout setDelegate:_interactor];
    
    _tableAdapter = [[YPNIMSessionTableAdapter alloc] init];
    _tableAdapter.interactor = _interactor;
    _tableAdapter.delegate   = vc;
    vc.tableView.delegate = _tableAdapter;
    vc.tableView.dataSource = _tableAdapter;
    
    
    [vc setInteractor:_interactor];
}


@end
