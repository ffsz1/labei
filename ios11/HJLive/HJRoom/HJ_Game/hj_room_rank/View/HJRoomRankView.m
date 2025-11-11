//
//  HJRoomRankView.m
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import "HJRoomRankView.h"

#import "HJRoomRankCell.h"
#import "HJRoomRankTopView.h"

#import "HJHttpRequestHelper+Room.h"

#define XBDRoomRankDateViewWidth 79//kScreenWidth/375*107

@interface HJRoomRankView ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIButton *richBtn;
@property (weak, nonatomic) IBOutlet UIButton *charmBtn;
@property (weak, nonatomic) IBOutlet GGImageView *richBtnBottomImageView;


@property (weak, nonatomic) IBOutlet UIButton *dayBtn;
@property (weak, nonatomic) IBOutlet UIButton *weekBtn;
@property (weak, nonatomic) IBOutlet UIButton *monthBtn;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIImageView *typeBgImageView;
@property (weak, nonatomic) IBOutlet UIImageView *dateTypeImageView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *centerX_type;//-53 53
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *centerX_day;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *top_TypeView;


@property (nonatomic,assign) NSInteger type;
@property (nonatomic,assign) NSInteger dateType;

@property (nonatomic,strong) NSMutableArray *dataArr;

@property (weak, nonatomic) IBOutlet UIView *topBgView;

@property (nonatomic,strong) HJRoomRankTopView *topView;

@property (copy,nonatomic) XBDRoomRankCardBlock cardBlock;


//@property (weak, nonatomic) IBOutlet UIView *topView;



@end

@implementation HJRoomRankView

+ (void)show:(XBDRoomRankCardBlock)cardBlock
{
    HJRoomRankView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJRoomRankView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    shareView.dataArr = [[NSMutableArray alloc] init];
    shareView.tableView.delegate = shareView;
    shareView.tableView.dataSource = shareView;
    shareView.cardBlock = cardBlock;
    
    
    [shareView getData];
    
    shareView.top_TypeView.constant = kScreenHeight;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.3 animations:^{
        shareView.top_TypeView.constant = 131;
        
        [shareView layoutIfNeeded];
    } completion:^(BOOL finished) {
    }];
    
    
}

- (void)close
{
    
    [self layoutIfNeeded];
    
    
    [UIView animateWithDuration:0.3 animations:^{
        
        self.top_TypeView.constant = kScreenHeight;
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.tableView.tableHeaderView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 15)];
    self.tableView.tableFooterView = [UIView new];
 
    self.dateType = 1;
    self.type = 1;

    self.centerX_day.constant = - XBDRoomRankDateViewWidth;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"HJRoomRankCell" bundle:nil] forCellReuseIdentifier:@"HJRoomRankCell"];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 70;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (self.dataArr.count<=3) {
        return 0;
    }
    
    return self.dataArr.count - 3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJRoomRankCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJRoomRankCell" forIndexPath:indexPath];
    
    if (indexPath.row <self.dataArr.count-3) {
        HJRoomBounsListInfo *model = self.dataArr[indexPath.row+3];
        cell.isCharm = self.charmBtn.selected;
        cell.indexPath = indexPath;
        cell.model = model;
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row <self.dataArr.count-3) {
        HJRoomBounsListInfo *model = self.dataArr[indexPath.row+3];
        
        self.cardBlock(model.ctrbUid);
        [self close];
        
    }
}


- (IBAction)richAction:(id)sender {
    self.type = 1;
    self.richBtn.selected = YES;
    self.charmBtn.selected = NO;
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.richBtnBottomImageView.image = [UIImage imageNamed:@"hj_room_rank_wealth_bottom"];
        self.centerX_type.constant = -53;
        [self layoutIfNeeded];
    }];
    
    [self getData];
    
    [self setCharmStytle:NO];


}
- (IBAction)charmAction:(id)sender {
    self.type = 2;
    self.richBtn.selected = NO;
    self.charmBtn.selected = YES;
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.richBtnBottomImageView.image = [UIImage imageNamed:@"hj_room_rank_charm_bottom"];
        self.centerX_type.constant = 53;
        [self layoutIfNeeded];
    }];
    
    [self getData];
    
    [self setCharmStytle:YES];

}

- (IBAction)dayBtnAction:(id)sender {
    
    self.dateType = 1;
    self.dayBtn.selected = YES;
    self.weekBtn.selected = NO;
    self.monthBtn.selected = NO;

    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.centerX_day.constant = - XBDRoomRankDateViewWidth;
        [self layoutIfNeeded];
    }];
    
    [self getData];

    
}
- (IBAction)weekBtnAction:(id)sender {
    
    self.dateType = 2;
    self.dayBtn.selected = NO;
    self.weekBtn.selected = YES;
    self.monthBtn.selected = NO;
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.centerX_day.constant = 0;
        [self layoutIfNeeded];
    }];
    [self getData];

}
- (IBAction)monthBtnAction:(id)sender {
    
    self.dateType = 3;
    self.dayBtn.selected = NO;
    self.weekBtn.selected = NO;
    self.monthBtn.selected = YES;
    
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.3 animations:^{
        self.centerX_day.constant = XBDRoomRankDateViewWidth;
        [self layoutIfNeeded];
    }];
    [self getData];

}
- (IBAction)closeAction:(id)sender {
    
    [self close];
}

- (void)getData
{
    NSString *type = [NSString stringWithFormat:@"%ld",(long)self.type];
    NSString *dataTpye = [NSString stringWithFormat:@"%ld",(long)self.dateType];

    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper requestNewRoomBounsListWithType:type withDataType:dataTpye Success:^(NSMutableArray *bounsInfoList) {
        
        
        
        weakSelf.dataArr = bounsInfoList;
        
//        if (weakSelf.dataArr.count>0) {
            weakSelf.topView.dataArr = weakSelf.dataArr;
        
        
        
//            weakSelf.tableView.tableHeaderView = weakSelf.topView;
//        }else{
//            weakSelf.tableView.tableHeaderView = nil;
//        }
        
        [weakSelf.tableView reloadData];
        
    } failure:^(NSNumber *code, NSString *message) {
        
    }];
}

- (void)setCharmStytle:(BOOL)isCharm
{
    self.typeBgImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charmbkg":@"hj_room_rank_wealthbkg"];
    self.dateTypeImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charmslider":@"hj_room_rank_wealthslider"];
    
//    [self.dayBtn setTitleColor:isCharm?UIColorHex(FF80B3):UIColorHex(C280FF) forState:UIControlStateNormal];
//    [self.weekBtn setTitleColor:isCharm?UIColorHex(FF80B3):UIColorHex(C280FF) forState:UIControlStateNormal];
//
//    [self.monthBtn setTitleColor:isCharm?UIColorHex(FF80B3):UIColorHex(C280FF) forState:UIControlStateNormal];

    if (!isCharm) {
        [self.dayBtn setTitleColor:UIColorHex(F1847F) forState:UIControlStateNormal];
        [self.weekBtn setTitleColor:UIColorHex(F1847F) forState:UIControlStateNormal];
        [self.monthBtn setTitleColor:UIColorHex(F1847F) forState:UIControlStateNormal];
    }else{
        [self.dayBtn setTitleColor:UIColorHex(7A9DFF) forState:UIControlStateNormal];
        [self.weekBtn setTitleColor:UIColorHex(7A9DFF) forState:UIControlStateNormal];
        [self.monthBtn setTitleColor:UIColorHex(7A9DFF) forState:UIControlStateNormal];
    }
    
    self.topView.isCharm = isCharm;
    

}

- (HJRoomRankTopView *)topView
{
    if (!_topView) {
        _topView = [[NSBundle mainBundle] loadNibNamed:@"HJRoomRankTopView" owner:self options:nil].firstObject;
        _topView.frame = CGRectMake(0, 110, kScreenWidth, 180);
        
        
        __weak typeof(self)weakSelf = self;
        _topView.cardBlock = ^(UserID uid) {
            weakSelf.cardBlock(uid);
            [weakSelf close];
        };
        [self.topBgView addSubview:_topView];
    }
    return _topView;
}

@end
