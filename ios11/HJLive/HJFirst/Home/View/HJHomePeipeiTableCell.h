//
//  HJHomePeipeiTableCell.h
//  HJLive
//
//  Created by feiyin on 2020/9/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "HJHomePeipeiModel.h"
#import <UIKit/UIKit.h>
#define HJHomePeipeiTableCellWidth (XC_SCREE_W-39)/2.0
#define HJHomePeipeiTableCellHeight 80+22
NS_ASSUME_NONNULL_BEGIN

@interface HJHomePeipeiTableCell : UITableViewCell
@property (nonatomic,strong) NSArray *roomArr;
@property (nonatomic,strong)UIButton *voiceButton;
@property (nonatomic,strong)UIButton *voiceButton2;
@property (nonatomic,strong) UIImageView *peopleImageView;
@property (nonatomic,strong) UIImageView *peopleImageView2;
@property  (nonatomic, copy)  void(^tapVoiceBtnForPeiPeiBlock)(HJHomePeipeiModel* peipeiModel, HJHomePeipeiTableCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapGotoTaLeftBlock)(HJHomePeipeiModel* peipeiModel, HJHomePeipeiTableCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapGotoTaRightBlock)(HJHomePeipeiModel* peipeiModel, HJHomePeipeiTableCell* voiceCell ) ;

@property  (nonatomic, copy)  void(^tapTilteLeftBlock)(HJHomePeipeiModel* peipeiModel, HJHomePeipeiTableCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapTilteRightBlock)(HJHomePeipeiModel* peipeiModel, HJHomePeipeiTableCell* voiceCell ) ;



@property (nonatomic,strong) HJHomePeipeiModel *peipeiModel1;
@property (nonatomic,strong) HJHomePeipeiModel *peipeiModel2;
@property (nonatomic, strong) NSString *filePath;


@end

NS_ASSUME_NONNULL_END
