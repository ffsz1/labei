//
//  HJHomeRoomCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "HJHomePageInfo.h"
#import "HJHomePeipeiModel.h"
#import "HJHomeMengxinModel.h"
#define JXHomeRoomCellHeight (kScreenWidth-18)/357*92


NS_ASSUME_NONNULL_BEGIN

@interface HJHomeRoomCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *tagLabel;
@property (weak, nonatomic) IBOutlet UIImageView *channelLabel;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_id_width;
@property (weak, nonatomic) IBOutlet UIImageView *animationImageView;
@property (weak, nonatomic) IBOutlet UIImageView *colorImageView;
@property (weak, nonatomic) IBOutlet GGImageView *bgImageView;
@property (weak, nonatomic) IBOutlet GGImageView *peopleBgImageView;
@property (weak, nonatomic) IBOutlet UILabel *desLabel;

@property (nonatomic, strong) HJHomePageInfo  *model;
@property (nonatomic, strong) HJHomePeipeiModel  *peipeiModel;
@property (nonatomic, strong) HJHomeMengxinModel  *mengxinModel;

@property (weak, nonatomic) IBOutlet UIImageView *gifImageview;
@property (weak, nonatomic) IBOutlet UIButton *voiceButton;

@property  (nonatomic, copy)  void(^clickVoiceBtnBlock)(HJHomePeipeiModel* peipeiModel, HJHomeRoomCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapGotoTaBlock)(HJHomePeipeiModel* peipeiModel, HJHomeRoomCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapChatBlock)(HJHomeMengxinModel* mengxinModel, HJHomeRoomCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapAvatarBlock)(UserID uid, HJHomeRoomCell* voiceCell , NSInteger type ) ;
@property (weak, nonatomic) IBOutlet UILabel *voiceTime;
@property (nonatomic, strong) NSString *filePath;
@property (weak, nonatomic) IBOutlet UILabel *flagLabel;
@property (weak, nonatomic) IBOutlet UIButton *goToAction;//去找ta
@property (weak, nonatomic) IBOutlet UIImageView *mengxinBG;

@property (weak, nonatomic) IBOutlet UIImageView *mengxinStart;
@property (weak, nonatomic) IBOutlet UILabel *mengxinLabel;
@property  (nonatomic, assign) NSInteger type;
@property  (nonatomic, strong) UIImageView* sexImageView;

@property (weak, nonatomic) IBOutlet UIImageView *caifuLevImageView;

@end

NS_ASSUME_NONNULL_END
