//
//  YPHomePeipeiTableCell.h
//  HJLive
//
//  Created by feiyin on 2020/9/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "YPHomePeipeiModel.h"
#import <UIKit/UIKit.h>
#define HJHomePeipeiTableCellWidth (XC_SCREE_W-39)/2.0
#define HJHomePeipeiTableCellHeight 80+22
NS_ASSUME_NONNULL_BEGIN

@interface YPHomePeipeiTableCell : UITableViewCell
@property (nonatomic,strong) NSArray *roomArr;
@property (nonatomic,strong)UIButton *voiceButton;
@property (nonatomic,strong)UIButton *voiceButton2;
@property (nonatomic,strong) UIImageView *peopleImageView;
@property (nonatomic,strong) UIImageView *peopleImageView2;
@property  (nonatomic, copy)  void(^tapVoiceBtnForPeiPeiBlock)(YPHomePeipeiModel* peipeiModel, YPHomePeipeiTableCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapGotoTaLeftBlock)(YPHomePeipeiModel* peipeiModel, YPHomePeipeiTableCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapGotoTaRightBlock)(YPHomePeipeiModel* peipeiModel, YPHomePeipeiTableCell* voiceCell ) ;

@property  (nonatomic, copy)  void(^tapTilteLeftBlock)(YPHomePeipeiModel* peipeiModel, YPHomePeipeiTableCell* voiceCell ) ;
@property  (nonatomic, copy)  void(^tapTilteRightBlock)(YPHomePeipeiModel* peipeiModel, YPHomePeipeiTableCell* voiceCell ) ;



@property (nonatomic,strong) YPHomePeipeiModel *peipeiModel1;
@property (nonatomic,strong) YPHomePeipeiModel *peipeiModel2;
@property (nonatomic, strong) NSString *filePath;


@end

NS_ASSUME_NONNULL_END
