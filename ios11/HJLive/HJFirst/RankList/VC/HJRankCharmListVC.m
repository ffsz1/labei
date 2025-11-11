//
//  HJRankCharmListVC.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRankCharmListVC.h"
#import "HJMySpaceVC.h"

#import "HJRankCell.h"
#import "HJMarqueeLabel.h"

#import "HJHttpRequestHelper+rank.h"

@interface HJRankCharmListVC ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (weak, nonatomic) IBOutlet UIView *tableViewHeaderView;
@property (weak, nonatomic) IBOutlet UIButton *dayRankBtn;
@property (weak, nonatomic) IBOutlet UIButton *weekRankBtn;
@property (weak, nonatomic) IBOutlet UIButton *monthRankBtn;
@property (weak, nonatomic) IBOutlet UIImageView *dayBgView;


@property (weak, nonatomic) IBOutlet UIImageView *headImageView1;
@property (weak, nonatomic) IBOutlet UIImageView *headerImageView2;
@property (weak, nonatomic) IBOutlet UIImageView *headerImageView3;


@property (weak, nonatomic) IBOutlet UIView *detailView1;
@property (weak, nonatomic) IBOutlet UIView *detailView2;
@property (weak, nonatomic) IBOutlet UIView *detailView3;

@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView1;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView2;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView3;

@property (weak, nonatomic) IBOutlet HJMarqueeLabel *nameLabel1;
@property (weak, nonatomic) IBOutlet HJMarqueeLabel *nameLabel2;
@property (weak, nonatomic) IBOutlet HJMarqueeLabel *nameLabel3;

@property (weak, nonatomic) IBOutlet UIImageView *sexImageView1;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView2;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView3;

@property (weak, nonatomic) IBOutlet UIImageView *levelImageView1;
@property (weak, nonatomic) IBOutlet UIImageView *levelImageView2;
@property (weak, nonatomic) IBOutlet UIImageView *levelImageView3;

@property (weak, nonatomic) IBOutlet UILabel *distanceLabel2;
@property (weak, nonatomic) IBOutlet UILabel *distanceLabel3;


//约束

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *center_rankTypeLine;//-80 0 80

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_1stDetailView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *right_2ndAvatar;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_2ndAvatar;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_3rdAvatar;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_3reAvatar;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *charm_pic_Height_layout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_topView;


@property (nonatomic,assign) NSInteger daySelType;
@property (nonatomic,strong) NSArray *dataArr;


@end

@implementation HJRankCharmListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self getData];
    
    [self setRunLabel];
    
    self.tableViewHeaderView.frame = CGRectMake(0, 0, kScreenWidth, kScreenWidth/375*277);
    self.tableView.tableHeaderView = self.tableViewHeaderView;
    
    //ps：比例缩放不能完美适配，只能改变关键约束值适配
    //适配plus
    if (kScreenWidth>375) {
        self.top_1stDetailView.constant = 25;
        
        self.right_2ndAvatar.constant = 50;
        self.bottom_2ndAvatar.constant = 36;
        
        self.left_3rdAvatar.constant = 50;
        self.bottom_3reAvatar.constant = 46;
        self.charm_pic_Height_layout.constant = 300;
    }else{
          self.charm_pic_Height_layout.constant = 277;
    }
    
    //适配320小屏
    if (kScreenWidth == 320) {
        self.right_2ndAvatar.constant = 20;
        self.bottom_2ndAvatar.constant = 12;
        
        self.left_3rdAvatar.constant = 20;
        self.bottom_3reAvatar.constant = 15;
    }
    
    
    //判断机型
    CGFloat height = UIApplication.sharedApplication.statusBarFrame.size.height;
    if (height >= 44.0) {
          // 是机型iPhoneX/iPhoneXR/iPhoneXS/iPhoneXSMax
        self.top_topView.constant = 70-44;
    }else{
        self.top_topView.constant = 50;
    }
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    HJLightStatusBar
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    if (@available(iOS 11.0, *)) {
        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
}

- (void)setRunLabel
{
    self.nameLabel1.marqueeType = MLContinuous;
    self.nameLabel1.scrollDuration = 8.0;
    self.nameLabel1.fadeLength = 15.0f;
    
    self.nameLabel2.marqueeType = MLContinuous;
    self.nameLabel2.scrollDuration = 8.0;
    self.nameLabel2.fadeLength = 15.0f;
    
    self.nameLabel3.marqueeType = MLContinuous;
    self.nameLabel3.scrollDuration = 8.0;
    self.nameLabel3.fadeLength = 15.0f;
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (self.dataArr.count>3) {
        return self.dataArr.count-3;
    }
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 74.5;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJRankCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJRankCell" forIndexPath:indexPath];
    
    if (
        indexPath.row<self.dataArr.count-3) {
        
        HJChartsModel *model = self.dataArr[indexPath.row+3];
        cell.num = indexPath.row+3;
        cell.charmModel = model;
    }
    
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (
        indexPath.row<self.dataArr.count-3) {
        
        HJChartsModel *model = self.dataArr[indexPath.row+3];
        
        if (model.uid > 0) {
            
            
            HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
            vc.userID = model.uid;
            [self.navigationController pushViewController:vc animated:YES];
        }
        
    }
}




- (IBAction)dayBtnAction:(id)sender {
    
    self.dayRankBtn.selected = YES;
    self.weekRankBtn.selected = NO;
    self.monthRankBtn.selected = NO;
    
    self.daySelType = 0;
    
    [self getData];
    self.dayBlock(self.daySelType);
    
    [self.tableView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        
        self.center_rankTypeLine.constant = -84;
        [self.tableView layoutIfNeeded];
    }];
    
}

- (IBAction)weekBtnAction:(id)sender {
    
    self.dayRankBtn.selected = NO;
    self.weekRankBtn.selected = YES;
    self.monthRankBtn.selected = NO;
    
    self.daySelType = 1;
    
    [self getData];
    self.dayBlock(self.daySelType);

    [self.tableView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        
        self.center_rankTypeLine.constant = 0;
        [self.tableView layoutIfNeeded];
    }];
    
}

- (IBAction)monthBtnAction:(id)sender {
    
    self.dayRankBtn.selected = NO;
    self.weekRankBtn.selected = NO;
    self.monthRankBtn.selected = YES;
    
    self.daySelType = 2;
    
    [self getData];
    self.dayBlock(self.daySelType);

    [self.tableView layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        
        self.center_rankTypeLine.constant = 84;
        [self.tableView layoutIfNeeded];
    }];
    
}
- (IBAction)firstHeaderAction:(id)sender {
    if (self.dataArr.count>0) {
        HJChartsModel *model = self.dataArr[0];
        HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
        vc.userID = model.uid;
        [self.navigationController pushViewController:vc animated:YES];
    }
}
- (IBAction)secondHeaderAction:(id)sender {
    
    if (self.dataArr.count>1) {
        HJChartsModel *model = self.dataArr[1];
        HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
        vc.userID = model.uid;
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}
- (IBAction)thirdHeaderAction:(id)sender {
    
    if (self.dataArr.count>2) {
        HJChartsModel *model = self.dataArr[2];
        HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
        vc.userID = model.uid;
        [self.navigationController pushViewController:vc animated:YES];
    }
}


- (void)updateData
{
    BOOL hasFirst = self.dataArr.count>0?YES:NO;
    BOOL hasSecond = self.dataArr.count>1?YES:NO;
    BOOL hasThird = self.dataArr.count>2?YES:NO;
    
//    self.avatarImageView1.hidden = !hasFirst;
//    self.avatarImageView2.hidden = !hasSecond;
//    self.avatarImageView3.hidden = !hasThird;
    self.avatarImageView1.image = [UIImage imageNamed:@"hj_room_rank_empty_rich"];
    self.avatarImageView2.image = [UIImage imageNamed:@"hj_room_rank_empty_rich"];
    self.avatarImageView3.image = [UIImage imageNamed:@"hj_room_rank_empty_rich"];
    
    self.avatarImageView1.layer.borderWidth = 2;
       self.avatarImageView1.layer.borderColor = [UIColor whiteColor].CGColor;
       self.avatarImageView2.layer.borderWidth = 2;
       self.avatarImageView2.layer.borderColor = [UIColor whiteColor].CGColor;
       self.avatarImageView3.layer.borderWidth = 2;
       self.avatarImageView3.layer.borderColor = [UIColor whiteColor].CGColor;
    
    self.headImageView1.hidden= !hasFirst;
    self.headerImageView2.hidden= !hasSecond;
    self.headerImageView3.hidden= !hasThird;
    
    self.detailView1.hidden = !hasFirst;
    self.detailView2.hidden = !hasSecond;
    self.detailView3.hidden = !hasThird;
    
    if (hasFirst) {
        HJChartsModel *model1 = self.dataArr[0];
        [self.avatarImageView1 qn_setImageImageWithUrl:model1.avatar placeholderImage:placeholder_image_square type:ImageTypeUserIcon];
        self.nameLabel1.text = model1.nick;
        self.sexImageView1.image = [UIImage imageNamed:model1.gender==1?@"hj_rank_man":@"hj_rank_women"];
        self.levelImageView1.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model1.charmLevel]];
    }
    
    if (hasSecond) {
        HJChartsModel *model1 = self.dataArr[1];
        [self.avatarImageView2 qn_setImageImageWithUrl:model1.avatar placeholderImage:placeholder_image_square type:ImageTypeUserIcon];
        self.nameLabel2.text = model1.nick;
        self.sexImageView2.image = [UIImage imageNamed:model1.gender==1?@"hj_rank_man":@"hj_rank_women"];
        self.levelImageView2.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model1.charmLevel]];
        
        self.distanceLabel2.text = [NSString stringWithFormat:@"%.0f",model1.distance];
    }
    
    if (hasThird) {
        HJChartsModel *model1 = self.dataArr[2];
        [self.avatarImageView3 qn_setImageImageWithUrl:model1.avatar placeholderImage:placeholder_image_square type:ImageTypeUserIcon];
        self.nameLabel3.text = model1.nick;
        self.sexImageView3.image = [UIImage imageNamed:model1.gender==1?@"hj_rank_man":@"hj_rank_women"];
        self.levelImageView3.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model1.charmLevel]];
        
        self.distanceLabel3.text = [NSString stringWithFormat:@"%.0f",model1.distance];
    }
    
    [self.tableView reloadData];
    
}


- (void)getData
{
    
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper getCharmRankData:self.daySelType+1 success:^(NSArray * _Nonnull list) {
        
        weakSelf.dataArr = list;
        [weakSelf updateData];
        
    } failure:^(NSNumber * _Nonnull code, NSString * _Nonnull errorMsg) {
        
    }];
}








@end
