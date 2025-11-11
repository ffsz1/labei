//
//  YPContributionListView.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPContributionListView.h"
#import "YPAuctionListEmptyCell.h"
#import "YPAuctionListerCell.h"
#import "YPRoomBounsListInfo.h"
#import "HJImRoomCoreClientV2.h"
#import "HJRoomCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "YPRoomCoreV2Help.h"
#import "UIView+XCToast.h"
//view
#import "YPZJScrollSegmentView.h"

@interface YPContributionListView()<
UITableViewDelegate,
UITableViewDataSource,
HJRoomCoreClient,
HJImRoomCoreClientV2
>
@property (weak, nonatomic) IBOutlet UIView *topContentView;
@property (strong, nonatomic) NSMutableArray *contribution;
@property (strong, nonatomic) YPChatRoomInfo *roomInfo;
@property (copy, nonatomic) NSString *type;
@property (copy, nonatomic) NSString *dataType;
@property (nonatomic, strong) YPZJScrollSegmentView *segmentView;
@end

@implementation YPContributionListView

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)removeCore {
    RemoveCoreClientAll(self);
}

- (void)awakeFromNib {
    [super awakeFromNib];
    self.layer.cornerRadius = 8;
    self.layer.masksToBounds = YES;
    
    self.type = @"1";
    self.dataType = @"1";
    
    self.RiBtn.titleLabel.font = [UIFont boldSystemFontOfSize:16];
    self.ZhBtn.titleLabel.font = [UIFont boldSystemFontOfSize:16];
    self.AllBtn.titleLabel.font = [UIFont boldSystemFontOfSize:16];

    
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPAuctionListerCell" bundle:nil] forCellReuseIdentifier:@"YPAuctionListerCell"];
    [self.tableView registerNib:[UINib nibWithNibName:@"YPAuctionListEmptyCell" bundle:nil] forCellReuseIdentifier:@"YPAuctionListEmptyCell"];
    [self updateView];
    
    self.lvOrmlSeg.layer.masksToBounds = YES;
    self.lvOrmlSeg.layer.cornerRadius = 15;
    self.lvOrmlSeg.layer.borderColor = UIColorHex(00C2FF).CGColor;
    self.lvOrmlSeg.layer.borderWidth = 1;
    [self.lvOrmlSeg setTitleTextAttributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16]}forState:UIControlStateSelected];
    [self.lvOrmlSeg setTitleTextAttributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16]}forState:UIControlStateNormal];
    
    @weakify(self);
    [[self.lvOrmlSeg rac_newSelectedSegmentIndexChannelWithNilValue:@0] subscribeNext:^(id x) {
        @strongify(self);
        if ([x intValue] == 0) {
            self.type = @"1";
        } else if ([x intValue] == 1) {
            self.type = @"2";
        }
        [self updateView];
    }];
    
    [self.topContentView addSubview:self.segmentView];
    [self.segmentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self.topContentView);
        make.height.equalTo(@44);
        make.width.equalTo(@200);
    }];
}

- (void)updateView {
    self.roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
    if (self.roomInfo != nil) {
        [GetCore(YPRoomCoreV2Help) getNewRoomBounsList:self.type dataType:self.dataType];
    }
}

- (IBAction)mlorlvClick:(UIButton *)sender {
    if (sender.tag == 3) {
        [self.RiBtn setTitleColor:UIColorHex(7E1EFF) forState:UIControlStateNormal];
        [self.ZhBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [self.AllBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        self.dataType = @"1";
    } else if (sender.tag == 4) {
        [self.RiBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [self.ZhBtn setTitleColor:UIColorHex(7E1EFF) forState:UIControlStateNormal];
        [self.AllBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        self.dataType = @"2";
    } else if (sender.tag == 5) {
        [self.RiBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [self.ZhBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [self.AllBtn setTitleColor:UIColorHex(7E1EFF) forState:UIControlStateNormal];
        self.dataType = @"3";
    }
    
    [self updateView];
    
    [UIView animateWithDuration:0.3 animations:^{
        if (sender.tag == 1 || sender.tag == 2) {
        } else {
            self.bLineImg.centerX = sender.centerX;
        }
    }];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.contribution.count == 0) {
        YPAuctionListEmptyCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPAuctionListEmptyCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
    } else {
        YPAuctionListerCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPAuctionListerCell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        YPRoomBounsListInfo *list = self.contribution[indexPath.row];
        
        //等级赋值
        if ([self.type isEqualToString:@"1"]) {
            list.experLevel == 0 ? (cell.lvImageCons.constant = 0) : (cell.lvImageCons.constant = 30);
//            cell.lvImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"Lv%ld",(long)list.experLevel]];
            cell.lvImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:list.experLevel]];
            cell.countLeftImg.image = [UIImage imageNamed:@"yp_charts_icon_jinbin"];
        } else if ([self.type isEqualToString:@"2"]) {
            list.charmLevel == 0 ? (cell.lvImageCons.constant = 0) : (cell.lvImageCons.constant = 30);
//            cell.lvImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"ml%ld",(long)list.charmLevel]];
            cell.lvImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:list.charmLevel]];
            cell.countLeftImg.image = [UIImage imageNamed:@"yp_charts_icon_jinbin"];
        }
        
        
        if (indexPath.row == 0) {
            cell.rankListImageView.hidden = NO;
            cell.rankListImageView.image = [UIImage imageNamed:@"yp_charts_icon_no_1_1"];
//            cell.nicknameLabel.textColor = cell.noBottomView.backgroundColor = UIColorHex(ff6164);
            cell.noBottomView.hidden = false;
            cell.rankNumLabel.textColor = [UIColor whiteColor];
            cell.rankNumLabel.hidden = YES;
        } else if (indexPath.row == 1) {
            cell.rankNumLabel.hidden = YES;
            cell.rankListImageView.hidden = NO;
            cell.rankListImageView.image = [UIImage imageNamed:@"yp_charts_icon_no_2_1"];
//            cell.nicknameLabel.textColor = cell.noBottomView.backgroundColor = UIColorHex(ffc107);
            cell.noBottomView.hidden = false;
            cell.rankNumLabel.textColor = [UIColor whiteColor];
        } else if (indexPath.row == 2) {
            cell.rankNumLabel.hidden = YES;
            cell.rankListImageView.hidden = NO;
            cell.rankListImageView.image = [UIImage imageNamed:@"yp_charts_icon_no_3_1"];
//            cell.nicknameLabel.textColor = cell.noBottomView.backgroundColor = UIColorHex(339cfe);
            cell.noBottomView.hidden = false;
            cell.rankNumLabel.textColor = [UIColor whiteColor];
        } else {
            cell.rankListImageView.hidden = YES;
            cell.rankNumLabel.hidden = NO;
            cell.nicknameLabel.textColor = [[YPYYTheme defaultTheme]colorWithHexString:@"#FFFFFF" alpha:1];
            cell.noBottomView.hidden = YES;
            cell.nicknameLabel.textColor = cell.rankNumLabel.textColor = UIColorHex(1a1a1a);
        }
        
        
        cell.rankNumLabel.text = [NSString stringWithFormat:@"%ld",indexPath.row + 1];
        
        [cell.avatarImageView qn_setImageImageWithUrl:list.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        cell.uid = list.uid;
        cell.nicknameLabel.text = list.nick;
        if (list.gender == UserInfo_Male) {
            cell.sexImageView.image = [UIImage imageNamed:@"yp_sex_male_logo"];
        }else {
            cell.sexImageView.image = [UIImage imageNamed:@"yp_sex_female_logo"];
        }
        
//        if (list.sumGold.length > 4) {
//            NSString *newCoin = [list.sumGold substringWithRange:NSMakeRange(0, list.sumGold.length-4)];
//            cell.coinNumberlabel.text = [NSString stringWithFormat:@"%@万贡献",newCoin];
//        } else {
        
        if ([self.type isEqualToString:@"1"]) {
            cell.coinNumberlabel.text = [NSString stringWithFormat:@"%@",list.sumGold];
            YPChatRoomInfo *roomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
//            if (roomInfo.giftDrawEnable == 1) {
//                cell.countLeftImg.image = [UIImage imageNamed:@""];
//            }
        } else if ([self.type isEqualToString:@"2"]) {
            cell.coinNumberlabel.text = [NSString stringWithFormat:@"%@",list.sumGold];
        }
        
        CGFloat wid = [cell.coinNumberlabel.text widthForFont:[UIFont systemFontOfSize:14]];
        
        cell.colingWidCons.constant = wid;
//        }
        return cell;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (self.contribution.count > 0) {
        return self.contribution.count;
    } else {
        return 1;
    }
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (self.contribution.count == 0) {return;}
    YPRoomBounsListInfo *list = self.contribution[indexPath.row];
    if (list) {
        [self avatarClick:list.ctrbUid];
    }
}

#pragma mark - ImRoomCoreClientV2
//房间信息变更
- (void)onCurrentRoomInfoChanged{
    [self updateView];
}

#pragma mark - RoomCoreClient
- (void)onGetRoomBounsListSuucess:(NSMutableArray *)arr type:(NSString *)type dateType:(NSString *)dateType {
    self.contribution = arr;
//    YPChatRoomInfo *roomInfo = [GetCore(YPRoomCoreV2Help) getCurrentRoomInfo];
//    if ([type isEqualToString:@"1"] && roomInfo.giftDrawEnable == 1) {
//        // 神豪
//        for (YPRoomBounsListInfo *info in self.contribution) {
//            info.sumGold = @"";
//        }
//    }
    [self.tableView reloadData];
}

- (void)onGetRoomBounsListFailth:(NSString *)message type:(NSString *)type dateType:(NSString *)dateType {
    [UIView showToastInKeyWindow:message duration:1 position:YYToastPositionCenter];
}

#pragma mark - private method
- (void)avatarClick:(UserID)uid {
    if(self.alertUserInformation) {
        self.alertUserInformation(uid);
    }
}


- (YPZJScrollSegmentView *)segmentView {
    if (!_segmentView) {
        __weak typeof(self) weakSelf = self;
        
        YPZJSegmentStyle *style = [[YPZJSegmentStyle alloc] init];
        //显示滚动条
        style.showLine = YES;
        // 颜色渐变
        style.gradualChangeTitleColor = YES;
        style.normalTitleColor = UIColorHex(ff90bd);
        style.selectedTitleColor = UIColorHex(FEFEFE);
        style.titleFont = [UIFont fontWithName:@"PingFang-SC-Medium" size:16];
        style.segmentViewComponent = SegmentViewComponentAdjustCoverOrLineWidth;
        style.scrollTitle = false;
        style.scrollLineColor = UIColorHex(6E42D2);
        style.titleMargin = 6;
        style.scrollLineHeight = 3;
        style.scrollLineColor = [UIColor clearColor];

        
        @weakify(self);
        YPZJScrollSegmentView *segment = [[YPZJScrollSegmentView alloc] initWithFrame:CGRectMake(0, 0, 200, 44) segmentStyle:style delegate:nil titles:@[@"神豪榜",@"心动榜"] titleDidClick:^(YPZJTitleView *titleView, NSInteger index) {
            @strongify(self);
            if (index == 0) {
                self.type = @"1";
            } else if (index == 1) {
                self.type = @"2";
            }
            [self updateView];
        }];
        _segmentView = segment;
    }
    return _segmentView;
}

@end
