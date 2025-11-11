//
//  NIMSessionPrivateProtocol.h
//  YPNIMKit
//
//  Created by chris on 2016/11/7.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#ifndef NIMSessionPrivateProtocol_h
#define NIMSessionPrivateProtocol_h

#import "YPNIMSessionViewController.h"

@class NIMMessage;
@class YPNIMMessageModel;

@interface NIMSessionMessageOperateResult : NSObject

@property (nonatomic,copy) NSArray *indexpaths;

@property (nonatomic,copy) NSArray *messageModels;

@end

@protocol NIMSessionDataSource <NSObject>

- (NSArray *)items;

- (NIMSessionMessageOperateResult *)addMessageModels:(NSArray *)models;

- (NIMSessionMessageOperateResult *)insertMessageModels:(NSArray *)models;

- (NIMSessionMessageOperateResult *)deleteMessageModel:(YPNIMMessageModel *)model;

- (NIMSessionMessageOperateResult *)updateMessageModel:(YPNIMMessageModel *)model;

- (YPNIMMessageModel *)findModel:(NIMMessage *)message;

- (NSInteger)indexAtModelArray:(YPNIMMessageModel *)model;

- (NSArray *)deleteModels:(NSRange)range;

- (void)resetMessages:(void(^)(NSError *error))handler;

- (void)loadHistoryMessagesWithComplete:(void(^)(NSInteger index, NSArray *messages , NSError *error))handler;

- (void)checkAttachmentState:(NSArray *)messages;

- (NSDictionary *)checkReceipt;

- (void)sendMessageReceipt:(NSArray *)messages;

- (void)cleanCache;

@end


@protocol NIMSessionLayoutDelegate <NSObject>

- (void)onRefresh;

@end

@protocol NIMSessionLayout <NSObject>

- (void)update:(NSIndexPath *)indexPath;

- (void)insert:(NSArray *)indexPaths animated:(BOOL)animated;

- (void)remove:(NSArray *)indexPaths;

- (BOOL)canInsertChatroomMessages;

- (void)calculateContent:(YPNIMMessageModel *)model;

- (void)reloadTable;

- (void)resetLayout;

- (void)changeLayout:(CGFloat)inputViewHeight;

- (void)setDelegate:(id<NIMSessionLayoutDelegate>)delegate;

- (void)layoutAfterRefresh;

@end





@interface YPNIMSessionViewController(Interactor)

- (void)setInteractor:(id<NIMSessionInteractor>) interactor;

- (void)setTableDelegate:(id<UITableViewDelegate, UITableViewDataSource>) tableDelegate;

@end


#endif /* NIMSessionPrivateProtocol_h */
