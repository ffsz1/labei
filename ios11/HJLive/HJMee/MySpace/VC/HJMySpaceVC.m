//
//  HJMySpaceVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMySpaceVC.h"

#import "HJUserCoreHelp.h"
#import "NSDate+Util.h"
#import "HJImRoomCoreV2.h"
#import "HJRoomViewControllerCenter.h"
#import "HJRoomCoreV2Help.h"

#import "HJEditPersonalPhotosVC.h"
#import "HJUserViewControllerFactory.h"
#import "SDPhotoBrowser.h"

#import "HJFollowListViewController.h"
#import "HJFansListViewController.h"
#import "HJMICRecordVC.h"
#import "HJSessionViewController.h"
#import "HJPersonalEditViewController.h"
#import "HJUserViewControllerFactory.h"

#import "HJMediaCoreClient.h"
#import "HJFileCoreClient.h"
#import "HJFileCore.h"
#import "HJMediaCore.h"
#import "HJPraiseCoreClient.h"
#import "HJPraiseCore.h"

#import "HJHttpRequestHelper+Praise.h"
#import "HJRoomPusher.h"
#import "HJUserHandler.h"

#import "HJMySpaceHeaderView.h"
#import "HJMySpaceGiftWallVC.h"
#import "HJBaseGestureTableView.h"
#import "HJUserViewControllerFactory.h"

#import "SDCycleScrollView.h"


#define XBDSpaceSectionHeight 77
#define XBDSpaceCellHeight kScreenHeight


@interface HJMySpaceVC ()<UITableViewDelegate,UITableViewDataSource,SDPhotoBrowserDelegate,HJMediaCoreClient,HJFileCoreClient,HJPraiseCoreClient,UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UIView *naviBarView;
@property (weak, nonatomic) IBOutlet UILabel *naviTitleLabel;
@property (weak, nonatomic) IBOutlet UIButton *backBtn;


@property (weak, nonatomic) IBOutlet UIButton *editBtn;
@property (weak, nonatomic) IBOutlet UIButton *moreBtn;
@property (weak, nonatomic) IBOutlet HJBaseGestureTableView *bgTableView;
@property (weak, nonatomic) IBOutlet UIButton *attentBtn;
@property (weak, nonatomic) IBOutlet GGImageView *bottomView;

@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet UIView *whiteCardBgView;
@property (weak, nonatomic) IBOutlet UIImageView *whiteCardImageView;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;
@property (weak, nonatomic) IBOutlet UIImageView *charmLevelImageView;
@property (weak, nonatomic) IBOutlet UIImageView *richLevelImageView;

//声音
@property (weak, nonatomic) IBOutlet UIImageView *playGifImageView;
@property (weak, nonatomic) IBOutlet UILabel *playTimeLabel;
@property (weak, nonatomic) IBOutlet UIView *voiceView;
@property (weak, nonatomic) IBOutlet UIControl *recordVoiceView;


//关注、粉丝、活跃
@property (weak, nonatomic) IBOutlet UIButton *followBtn;
@property (weak, nonatomic) IBOutlet UIButton *fanBtn;
@property (weak, nonatomic) IBOutlet UIButton *activeBtn;
@property (weak, nonatomic) IBOutlet UIButton *findHimBtn;
@property (weak, nonatomic) IBOutlet UIButton *hisRoomBtn;

//CP
@property (weak, nonatomic) IBOutlet GGImageView *myCPAvatarImageView;
@property (weak, nonatomic) IBOutlet GGImageView *hisCPAvatarImageView;

//签名
@property (weak, nonatomic) IBOutlet UILabel *signLabel;


@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;

@property (weak, nonatomic) IBOutlet UIView *photoView;
@property (weak, nonatomic) IBOutlet UIScrollView *photoScrollView;
@property (weak, nonatomic) IBOutlet UIButton *addBtn;

@property (strong,nonatomic) HJMySpaceHeaderView *sectionView;

//动态
@property (weak, nonatomic) IBOutlet UILabel *noCircelTipLabel;
@property (weak, nonatomic) IBOutlet GGButton *addCircleBtn;


//其他约束
@property (weak, nonatomic) IBOutlet UIView *photoBGView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heigt_photoBgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_photoView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_footerView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_topView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_richLevel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_circleView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_signView;

@property (assign,nonatomic) BOOL isMySpace;
@property (strong,nonatomic) UserInfo *userInfo;
@property (nonatomic, strong) ChatRoomInfo *myRoomInfo;
@property (nonatomic, strong) NSString *filePath;

@property (nonatomic, assign) BOOL isAttentioned;

@property (nonatomic,strong) HJMySpaceGiftWallVC *wallVC;
@property (nonatomic,strong) HJMySpaceGiftWallVC *carVC;
@property (nonatomic,strong) HJMySpaceGiftWallVC *headerVC;


@property (nonatomic,strong) UIView *wallContentView;

@property (nonatomic,strong) UIScrollView *itemScrollView;///tableView的父视图


@property (nonatomic, assign) BOOL canScroll;
@property (nonatomic, strong) SDCycleScrollView *banaCommonView;
@property (nonatomic,assign) BOOL isThis;
@property (weak, nonatomic) IBOutlet GGImageView *headWearImgView;

@end

@implementation HJMySpaceVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.headerView addSubview:self.banaCommonView];
    [self.headerView insertSubview:self.banaCommonView atIndex:1];
    self.canScroll = YES;
    
    self.height_topView.constant = XC_Height_NavBar;

    self.bgTableView.bounces = NO;
    
    self.bgTableView.estimatedRowHeight= 0;
    
    self.bgTableView.estimatedSectionHeaderHeight= 0;
    
    self.bgTableView.estimatedSectionFooterHeight= 0;
    
    [_bgTableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"itemCell"];
    
    self.isAttentioned = NO;
    
    [self setSpaceStytle];
    
    AddCoreClient(HJMediaCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJPraiseCoreClient, self);
    
    [self setItemVC];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(jumpPrivateChatPageAction:) name:@"jumpPrivateChatPageNotification" object:nil];
    
}
- (void)jumpPrivateChatPageAction:(NSNotification *)noti {
    if (!self.isThis) {
        return;
    }
    if (noti) {
        if (noti.object) {
            UserID uid = [noti.object longLongValue];
            NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",uid] type:0];
                      HJSessionViewController *vc = [[HJSessionViewController alloc] initWithSession:session];
                      [self.navigationController pushViewController:vc animated:YES];
        }
    }
}
- (void)setItemVC
{
//    self.itemScrollView.backgroundColor = [UIColor yellowColor];
//    self.wallVC.view.backgroundColor = [UIColor redColor];
    
    [self.wallVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.itemScrollView.mas_left);
        make.top.mas_equalTo(self.itemScrollView.mas_top);
        make.bottom.mas_equalTo(self.itemScrollView.mas_bottom);
        make.width.mas_equalTo(kScreenWidth);
        make.height.mas_equalTo(XBDSpaceCellHeight);
    }];
    
    [self.headerVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.wallVC.view.mas_right);
        make.top.mas_equalTo(self.itemScrollView.mas_top);
        make.bottom.mas_equalTo(self.itemScrollView.mas_bottom);
        make.width.mas_equalTo(kScreenWidth);
        make.height.mas_equalTo(XBDSpaceCellHeight);
    }];
    
    [self.carVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.headerVC.view.mas_right);
        make.top.mas_equalTo(self.itemScrollView.mas_top);
        make.bottom.mas_equalTo(self.itemScrollView.mas_bottom);
        make.width.mas_equalTo(kScreenWidth);
        make.height.mas_equalTo(XBDSpaceCellHeight);
    }];
    
}

- (void)setSpaceStytle
{
    //判断是否自己的空间
    if (self.isMySpace) {
        
        self.editBtn.hidden = NO;
        self.moreBtn.hidden = YES;
        self.bottom_footerView.constant = -70;
        
        self.naviTitleLabel.text = @"我的空间";
        
        self.noCircelTipLabel.hidden = YES;
        self.height_circleView.constant = 96;
        
        self.findHimBtn.hidden = YES;
        self.hisRoomBtn.selected = YES;
        
    }else{
        
        self.editBtn.hidden = YES;
        self.moreBtn.hidden = NO;
        //隐藏底部、请求关注状态
        self.bottom_footerView.constant = 20;
        
        self.addCircleBtn.hidden = YES;
        self.height_circleView.constant = 38;
        
        _bottomView.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.05].CGColor;
        _bottomView.layer.shadowOffset = CGSizeMake(0,3);
        _bottomView.layer.shadowRadius = 10;
        _bottomView.layer.shadowOpacity = 1;
        
        [self checkAttend];
        
    }
    
    _whiteCardBgView.layer.shadowColor = [UIColor colorWithRed:194/255.0 green:128/255.0 blue:255/255.0 alpha:0.1].CGColor;
    _whiteCardBgView.layer.shadowOffset = CGSizeMake(0,10);
    _whiteCardBgView.layer.shadowRadius = 15;
    _whiteCardBgView.layer.shadowOpacity = 1;
}



- (void)updateTableViewsScroll {
    
    CGFloat offsetY = self.bgTableView.contentOffset.y;
    
    CGFloat offsetY_section = [self.bgTableView rectForSection:0].origin.y;
    
    NSLog(@"%f",offsetY);
    NSLog(@"offsetY_section:%f",offsetY_section);

    
    if (offsetY >= offsetY_section) {
        
        if (self.canScroll == YES) {
            self.canScroll = NO;
            
//            if (self.wallVC.isShow) {
                self.wallVC.canScroll = YES;
//                self.wallVC.collectionView.contentOffset = CGPointMake(0, 1);
                self.wallVC.collectionView.contentOffset = CGPointZero;

//            }
            
//            if (self.carVC.isShow) {
                self.carVC.canScroll = YES;
//                self.carVC.collectionView.contentOffset = CGPointMake(0, 1);
                self.carVC.collectionView.contentOffset = CGPointZero;

//            }
        
//            if (self.headerVC.isShow) {
                self.headerVC.canScroll = YES;
//                self.headerVC.collectionView.contentOffset = CGPointMake(0, 1);
                self.headerVC.collectionView.contentOffset = CGPointZero;

//            }
        
            
            
        }
        self.bgTableView.contentOffset = CGPointMake(0, offsetY_section);
        
    }else{
        
        if (self.canScroll == NO) {
            self.bgTableView.contentOffset = CGPointMake(0, offsetY_section);
        }
    }
    
}



- (void)dealloc
{
    RemoveCoreClientAll(self);
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    self.isThis = NO;
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.isThis = YES;
    @weakify(self);
    [[GetCore(HJRoomCoreV2Help) requestRoomInfo:self.userID] subscribeNext:^(id x) {
        @strongify(self)
        self.myRoomInfo = (ChatRoomInfo *)x;
    }];
        
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    if (@available(iOS 11.0, *)) {
        self.bgTableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
    [self getData];
}


#pragma mark <UITableViewDelegate,UITableViewDataSource>
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return XBDSpaceCellHeight;
//    return self.cellHeight;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return XBDSpaceSectionHeight;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return CGFLOAT_MIN;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return self.sectionView;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    return [UIView new];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell  = [tableView dequeueReusableCellWithIdentifier:@"itemCell"];
    
    [cell.contentView addSubview:self.itemScrollView];
    
    return cell;
}


#pragma mark UIScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == self.bgTableView) {
        
//        CGFloat offsetX = scrollView.contentOffset.x;
        
        CGFloat offsetY = self.bgTableView.contentOffset.y;
        [self updateNavBar:offsetY];
        
        [self updateTableViewsScroll];
        
    }
    
    if (scrollView == self.itemScrollView) {

        //过滤switchBar点击的回调
        if (!scrollView.isDecelerating && !scrollView.tracking) {
            return;
        }
        CGFloat offsetX = scrollView.contentOffset.x;
        CGFloat index = offsetX / kScreenWidth;
        if ( index == 0 || index == 1 || index == 2) {
            
            
            self.wallVC.isShow = index == 0?YES:NO;
            self.carVC.isShow = index == 1?YES:NO;
            self.headerVC.isShow = index == 2?YES:NO;
            
            [self.itemScrollView setContentOffset:CGPointMake(index*kScreenWidth, 0) animated:YES];
            
            [self.sectionView setSelIndex:index];
        }
    }
}


//更新导航条状态
- (void)updateNavBar:(CGFloat)offsetY
{
    CGFloat picHeight = XC_Height_NavBar;
    
    if (offsetY <= XC_Height_NavBar) {
        
        CGFloat alpha = offsetY/picHeight;
        
        self.naviBarView.backgroundColor = [UIColor colorWithWhite:1 alpha:alpha];

        self.backBtn.selected = NO;
        self.editBtn.selected = NO;
        self.moreBtn.selected = NO;
        
        self.naviTitleLabel.hidden = YES;
        
        HJLightStatusBar
        
    }else{
        self.naviBarView.backgroundColor = [UIColor colorWithWhite:1 alpha:0.95];

        self.backBtn.selected = YES;
        self.editBtn.selected = YES;
        self.moreBtn.selected = YES;
        
        self.naviTitleLabel.hidden = NO;

        HJBlackStatusBar
    }
}



#pragma mark 点击事件

- (IBAction)copyBtnAction:(id)sender {
    
    UIPasteboard * pastboard = [UIPasteboard generalPasteboard];
       UserInfo *info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[GetCore(HJAuthCoreHelp) getUid].userIDValue];
       pastboard.string = info.erbanNo;
       [MBProgressHUD showSuccess:@"复制成功"];
}



- (IBAction)backAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)moreAction:(id)sender {
    
    if (self.isAttentioned) {
        __weak typeof(self)weakSelf = self;
        [HJUserHandler showReport:self.userID cancelFollowBlock:^{
            weakSelf.isAttentioned = NO;
            weakSelf.attentBtn.userInteractionEnabled = YES;
        }];
    }else{
        [HJUserHandler showReport:self.userID cancelFollowBlock:nil];
    }
    
}

//去找ta
- (IBAction)findHimAction:(id)sender {
    
    [HJRoomPusher pushUserInRoomByID:self.userID];
}

//ta的房间
- (IBAction)hisRoomAction:(id)sender {
    
    if (self.myRoomInfo.valid) {
        [HJRoomPusher pushRoomByID:self.userID];
    }
    else {
        [MBProgressHUD showError:NSLocalizedString(XCPersonalInfoNoRoom, nil)];
    }
    
}
- (IBAction)recordVoiceAction:(id)sender {
    
    HJMICRecordVC *MICRecordViewController = [HJMICRecordVC new];
    [self.navigationController pushViewController:MICRecordViewController animated:YES];
    
}

//播放声音
- (IBAction)playAction:(id)sender {
    
    if (!self.userInfo.userVoice) {
        
        [MBProgressHUD showError:@"暂无声音"];
        BOOL isOwner = [GetCore(HJAuthCoreHelp) getUid].userIDValue == self.userID;
        
        if (isOwner) {
            HJMICRecordVC *MICRecordViewController = [HJMICRecordVC new];
            [self.navigationController pushViewController:MICRecordViewController animated:YES];
        }
        
        return;
    }
    if (self.filePath == nil) {
        if (self.userInfo.voiceDura==0) {
                   [MBProgressHUD showError:@"还没有录音哦~"];
                   return;
               }
        [MBProgressHUD showMessage:@"下载中..."];
        [GetCore(HJFileCore) downloadVoice:self.userInfo.userVoice];
    } else {
        if ([GetCore(HJMediaCore) isPlaying]) {
            [GetCore(HJMediaCore) stopPlay];
        } else {
            [GetCore(HJMediaCore) play:self.filePath];
        }
    }
    
}

//关注列表
- (IBAction)followListAction:(id)sender {
    
    if (self.isMySpace) {
        HJFollowListViewController *vc = HJMessageBoard(@"HJFollowListViewController");
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}

//粉丝列表
- (IBAction)fansListAction:(id)sender {
    if (self.isMySpace) {
        HJFansListViewController *vc = HJMessageBoard(@"HJFansListViewController");
        [self.navigationController pushViewController:vc animated:YES];
    }
}

//添加图片
- (IBAction)addPhoto:(id)sender {
    
    HJEditPersonalPhotosVC *vc = (HJEditPersonalPhotosVC *)[[HJUserViewControllerFactory sharedFactory]instantiateEditPersonalPhotosViewController];
    vc.uid = self.userInfo.uid;
    [self.navigationController pushViewController:vc animated:YES];
}

//关注
- (IBAction)attendAction:(id)sender {
    if (!self.isAttentioned) {
        NSString * mine = [GetCore(HJAuthCoreHelp) getUid];
        
        __weak typeof(self)wealSelf = self;
        [HJUserHandler follow:self.userID followSucceed:^{
            [MBProgressHUD showSuccess:@"关注成功"];
            wealSelf.isAttentioned = YES;
        }];
        
//        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
//        [GetCore(HJPraiseCore) praise:mine.userIDValue bePraisedUid:self.userID];
    }else{
        [self showCancelAttentionAlert];
    }
    
}

//添加动态
- (IBAction)addCirCleBtnAction:(id)sender {
    
    [MBProgressHUD showSuccess:@"功能建设中，敬请期待"];
    
}



//聊天
- (IBAction)chatAction:(id)sender {
    
    NIMSession *session = [NIMSession session:[NSString stringWithFormat:@"%lld",self.userInfo.uid] type:0];
    HJSessionViewController *vc = [[HJSessionViewController alloc] initWithSession:session];
    [self.navigationController pushViewController:vc animated:YES];
}


#pragma mark httpRequest
- (void)getData
{
//    self.bgTableView.hidden = YES;
    
    __weak typeof(self)weakSelf = self;
    [GetCore(HJUserCoreHelp) getUserInfo:self.userID refresh:YES success:^(UserInfo *info) {
        weakSelf.userInfo = info;
//        weakSelf.bgTableView.hidden = NO;
    }];
}

- (void)checkAttend
{
    [HJHttpRequestHelper isLike:[GetCore(HJAuthCoreHelp).getUid userIDValue] isLikeUid:self.userID success:^(BOOL isLike) {
        
        self.isAttentioned = isLike;
        
    } failure:^(NSNumber *resCode, NSString *message) {
        self.isAttentioned = NO;
    }];
}

#pragma mark private method
- (void)updatePhotoView
{
    
    if (self.userInfo.privatePhoto.count == 0) {
        
        if (!self.isMySpace) {
            self.heigt_photoBgView.constant = 0;
            self.photoBGView.hidden = YES;
        }
        
        return;
    }
    
    
    //清除所有子视图
    for (UIView *subView in self.photoView.subviews) {
        if (subView.tag != 2000 && subView.tag != 3000) {
            [subView removeFromSuperview];
        }
    }
    
    CGFloat addBtnDisance = self.isMySpace?80:0;
    
    self.width_photoView.constant = addBtnDisance+ self.userInfo.privatePhoto.count*80+10+90;
    
    self.addBtn.hidden = !self.isMySpace;
    
    //初始化相册图片
    for (int i = 0; i<self.userInfo.privatePhoto.count; i++) {
        
        UIImageView *photo = [[UIImageView alloc] initWithFrame:CGRectMake( addBtnDisance+30+80*i, 0, 70, 70)];
        photo.contentMode = UIViewContentModeScaleAspectFill;
        photo.backgroundColor = UIColorHex(A1A1A1);
        photo.layer.cornerRadius = 15;
        photo.layer.masksToBounds = YES;
        photo.userInteractionEnabled = YES;
        photo.tag = 3000+i;
        
        
        //相册缩放tap
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            [self showPhotoAction:i view:photo];
        }];
        [photo addGestureRecognizer:tap];
        
        UserPhoto *model = self.userInfo.privatePhoto[i];
        [photo qn_setImageImageWithUrl:model.photoUrl placeholderImage:placeholder_image_square type:ImageTypeHomePageItem success:nil];
        
        [self.photoView addSubview:photo];
    }
    
}

//相册缩放
- (void)showPhotoAction:(int)i view:(UIImageView *)imageView
{
    
    UIImageView *tagView = [self.photoView viewWithTag:3000+i];
    
    SDPhotoBrowser *browser = [[SDPhotoBrowser alloc]init];
    browser.sourceImagesContainerView = self.photoView;
    browser.delegate = self;
    browser.imageCount = self.userInfo.privatePhoto.count;
    browser.currentImageIndex = i;
    [browser show];
}

//设置id、等级
- (void)setLevelText
{
    
    if (_userInfo.experLevel>=0) {
        self.richLevelImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:_userInfo.experLevel]];
    }else{
        self.richLevelImageView.hidden = YES;
    }
    
    if (_userInfo.charmLevel>=0) {
        self.charmLevelImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:_userInfo.charmLevel]];

    }else{
        self.charmLevelImageView.hidden = YES;
        self.left_richLevel.constant = -43;
    }
    
}


- (void)updateSignText
{
    if (!self.userInfo.userDesc) {
        self.userInfo.userDesc = @"这个人很懒，什么都没写...";
    }
    self.signLabel.text = self.userInfo.userDesc;
    
    CGSize size = [self.signLabel.text boundingRectWithSize:CGSizeMake(kScreenWidth-105, 0) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.signLabel.font} context:nil].size;
    
    
    //相册高度
    CGFloat photoHeight = 0;//118
    if (!self.isMySpace && self.userInfo.privatePhoto.count == 0) photoHeight = 0;
    
    //签名高度
    CGFloat signHeight = 38;
    if (size.height>18) signHeight = size.height +30;
    
    self.height_signView.constant = signHeight - 8;

    
    //动态高度
    CGFloat circleHeight = 58;
    if (self.isMySpace) circleHeight = 58;
    
//    CGFloat headerHeight = 334 + photoHeight + signHeight+circleHeight;
//    CGFloat headerHeight = 334+circleHeight +50;
    CGFloat headerHeight = 334+circleHeight - 70;
    
    self.headerView.frame = CGRectMake(0, 0, kScreenWidth, ceilf(headerHeight));
    self.bgTableView.tableHeaderView = self.headerView;
}

- (void)updataFanBtn
{
    
    [self.fanBtn setTitle:[NSString stringWithFormat:@"%ld",_userInfo.fansNum] forState:UIControlStateNormal];
    
    [self.followBtn setTitle:[NSString stringWithFormat:@"%ld ",_userInfo.followNum] forState:UIControlStateNormal];

    [self.activeBtn setTitle:[NSString stringWithFormat:@"%ld ",_userInfo.liveness] forState:UIControlStateNormal];

}

- (NSMutableAttributedString *)getAttrStr:(NSString *)str appendStr:(NSString *)appendStr
{
    NSMutableAttributedString *result = [[NSMutableAttributedString alloc] initWithString:str];
    
    NSAttributedString *result2 = [[NSAttributedString alloc] initWithString:appendStr attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11],NSForegroundColorAttributeName:[UIColor colorWithHexString:@"999999"]}];
    
    [result appendAttributedString:result2];
    

    return result;
}


- (void)updateSexView
{
    BOOL isMale = self.userInfo.gender == UserInfo_Male?YES:NO;
    self.sexImageView.image = [UIImage imageNamed:isMale?@"hj_home_attend_man":@"hj_home_attend_woman"];

}


//取消关注
- (void)showCancelAttentionAlert {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCPersonalInfoCancelPraiseTitle, nil) message:NSLocalizedString(XCPersonalInfoCancelPraiseMesseage, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCPersonalInfoCancelPraise, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
//        NSString* mine = [GetCore(HJAuthCoreHelp) getUid];
//        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
//        [GetCore(HJPraiseCore) cancel:mine.userIDValue beCanceledUid:self.userID];
        
        [HJUserHandler cancelFollow:self.userID cancelFollowBlock:^{
            [MBProgressHUD showSuccess:@"取消关注成功"];
            self.isAttentioned = NO;
        }];
        
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [self presentViewController:alert animated:YES completion:nil];
}

#pragma mark - SDPhotoBrowserDelegate

- (UIImage *)photoBrowser:(SDPhotoBrowser *)browser placeholderImageForIndex:(NSInteger)index {
    return [UIImage imageNamed:default_bg];
}

- (NSURL *)photoBrowser:(SDPhotoBrowser *)browser highQualityImageURLForIndex:(NSInteger)index {
    return [NSURL URLWithString:self.userInfo.privatePhoto[index].photoUrl];
}

#pragma mark - FileCoreClient
- (void)onDownloadVoiceSuccess:(NSString *)filePath
{
    self.filePath = filePath;
    
    if ([GetCore(HJMediaCore) isPlaying]) {
        [GetCore(HJMediaCore) stopPlay];
    }
    
    [MBProgressHUD hideHUD];
    
    
    [GetCore(HJMediaCore) play:filePath];
}

- (void)onDownloadVoiceFailth:(NSError *)error
{
    [MBProgressHUD showError:XCHudNetError];
}

#pragma mark - MediaCoreClient
- (void) onPlayAudioBegan:(NSString *)filePath
{
    if (self.filePath != nil && [self.filePath isEqualToString:filePath]) {
    }
    
    [self setPlayGif];
    
}

- (void)onPlayAudioComplete:(NSString *)filePath
{
    [self.playGifImageView stopAnimating];
}

- (void)setPlayGif {
    NSMutableArray *arr = [NSMutableArray new];
    for (int i = 0; i < 3; i ++) {
        [arr addObject:[UIImage imageNamed:[NSString stringWithFormat:@"hj_space_voice_play%d",i+1]]];
    }
    [self.playGifImageView setAnimationImages:arr];
    [self.playGifImageView setAnimationDuration:1];
    self.playGifImageView.animationRepeatCount = 0;
    [self.playGifImageView startAnimating];
}

#pragma mark - PraiseCoreClient
- (void)onPraiseSuccess:(UserID)uid
{
    [MBProgressHUD hideHUD];
    self.isAttentioned = YES;
    self.attentBtn.userInteractionEnabled = YES;
}

- (void)onPraiseFailth:(NSString *)msg
{
    [MBProgressHUD hideHUD];
    self.attentBtn.userInteractionEnabled = YES;
    [MBProgressHUD showError:msg];
}

- (void)onCancelSuccess:(UserID)uid;
{
    [MBProgressHUD hideHUD];
    self.isAttentioned = NO;
    self.attentBtn.userInteractionEnabled = YES;
}



- (void)onCancelFailth:(NSString *)msg
{
    [MBProgressHUD hideHUD];
    self.attentBtn.userInteractionEnabled = YES;

    [MBProgressHUD showError:msg];
}

#pragma mark setter/getter
- (void)setUserID:(UserID)userID
{
    _userID = userID;
    
    
    
    //判断是否自己的空间
    if ([GetCore(HJAuthCoreHelp).getUid userIDValue] == _userID) {
        self.isMySpace = YES;
    }
    
}

- (void)setUserInfo:(UserInfo *)userInfo
{
    _userInfo = userInfo;
    if (_userInfo) {
        
        
        __weak typeof(self)weakSelf = self;
        self.avatarImageView.layer.borderWidth = 2;
        self.avatarImageView.layer.borderColor = [UIColor whiteColor].CGColor;
        [self.avatarImageView qn_setImageImageWithUrl:self.userInfo.avatar placeholderImage:default_avatar type:ImageTypeUserIcon success:^(UIImage *image) {
            
            weakSelf.myCPAvatarImageView.image = image;
            
            
        }];
        
        if (userInfo.headwearUrl != nil && userInfo.headwearUrl.length>0) {
                   weakSelf.headWearImgView.hidden = NO;
                    [weakSelf.headWearImgView qn_setImageImageWithUrl:userInfo.headwearUrl placeholderImage:default_avatar type:ImageTypeUserIcon];
               }else{
                   weakSelf.headWearImgView.hidden = YES;
               }
        
        
        self.nameLabel.text = self.userInfo.nick;
        
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        
        if ((kScreenWidth-195)<size.width) {
            self.width_name.constant = kScreenWidth - 195;
        }else{
            self.width_name.constant = size.width;
        }
        
        
        [self setLevelText];
        
        if (self.userInfo.voiceDura > 0) {
            self.playTimeLabel.text = [NSString stringWithFormat:@"%ld s",(long)self.userInfo.voiceDura];
            self.recordVoiceView.hidden = YES;
        }else {
//            self.voiceView.hidden = YES;
        }
        
        if (!self.isMySpace) self.recordVoiceView.hidden = YES;
        
        self.idLabel.text = [NSString stringWithFormat:@"ID：%@",self.userInfo.erbanNo];
        
        [self updataFanBtn];
        
        [self updateSignText];
        
        [self updateSexView];
        
        [self updatePhotoView];

        
    }
}

- (void)setIsAttentioned:(BOOL)isAttentioned
{
    _isAttentioned = isAttentioned;
    
    self.attentBtn.selected = _isAttentioned;
}

- (HJMySpaceHeaderView *)sectionView
{
    if (!_sectionView) {
        _sectionView = [[NSBundle mainBundle] loadNibNamed:@"HJMySpaceHeaderView" owner:self options:nil][0];
        _sectionView.frame = CGRectMake(0, 0, kScreenWidth, XBDSpaceSectionHeight);
        
        __weak typeof(self)weakSelf = self;
        _sectionView.clickBlock = ^(NSInteger index) {
            weakSelf.wallVC.isShow = index == 0?YES:NO;
            weakSelf.carVC.isShow = index == 2?YES:NO;
            weakSelf.headerVC.isShow = index == 1?YES:NO;
            
            [weakSelf.itemScrollView setContentOffset:CGPointMake(index*kScreenWidth, 0) animated:YES];
        };
    }
    return _sectionView;
}


- (HJMySpaceGiftWallVC *)wallVC
{
    if (!_wallVC) {
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        layout.itemSize = CGSizeMake(XC_SCREE_W/4, 90);
        
        
        _wallVC = [[HJMySpaceGiftWallVC alloc] initWithCollectionViewLayout:layout];
        [self addChildViewController:_wallVC];
        

        
        _wallVC.userID = self.userID;
        
        _wallVC.type = 0;
        
        
        __weak typeof(self)weakSelf = self;
        _wallVC.updateBlock = ^(NSInteger giftNum,NSInteger giftTypeNum) {
            
            [weakSelf.sectionView updateNum:giftNum];
            [weakSelf.bgTableView reloadData];
        };
        
        _wallVC.updateScrollBlock = ^{
            
            weakSelf.canScroll = YES;
            weakSelf.wallVC.canScroll = NO;
            weakSelf.wallVC.collectionView.contentOffset = CGPointZero;
        };
        
        [self.itemScrollView addSubview:_wallVC.view];

    }
    return _wallVC;
}

- (HJMySpaceGiftWallVC *)carVC
{
    if (!_carVC) {
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        layout.itemSize = CGSizeMake(XC_SCREE_W/4, 90);
        
        
        _carVC = [[HJMySpaceGiftWallVC alloc] initWithCollectionViewLayout:layout];
        [self addChildViewController:_carVC];
        
        
        
        _carVC.userID = self.userID;
        
        _carVC.type = 2;
        
        
        __weak typeof(self)weakSelf = self;
        
        _carVC.updateScrollBlock = ^{
            
            weakSelf.canScroll = YES;
            weakSelf.carVC.canScroll = NO;
            weakSelf.carVC.collectionView.contentOffset = CGPointZero;
        };
        
        [self.itemScrollView addSubview:_carVC.view];
        
    }
    return _carVC;
}

- (HJMySpaceGiftWallVC *)headerVC
{
    if (!_headerVC) {
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        layout.itemSize = CGSizeMake(XC_SCREE_W/4, 90);
        
        
        _headerVC = [[HJMySpaceGiftWallVC alloc] initWithCollectionViewLayout:layout];
        [self addChildViewController:_headerVC];
        
        
        
        _headerVC.userID = self.userID;
        
        _headerVC.type = 1;
        
        
        __weak typeof(self)weakSelf = self;
        
        _headerVC.updateScrollBlock = ^{
            
            weakSelf.canScroll = YES;
            weakSelf.headerVC.canScroll = NO;
            weakSelf.headerVC.collectionView.contentOffset = CGPointZero;
        };
        
        [self.itemScrollView addSubview:_headerVC.view];
        
    }
    return _headerVC;
}

- (UIView *)wallContentView
{
    if (!_wallContentView) {
        _wallContentView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 0)];
        
    }
    return _wallContentView;
}

- (UIScrollView *)itemScrollView
{
    if (!_itemScrollView) {
        _itemScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, XBDSpaceCellHeight)];
        _itemScrollView.delegate = self;
        
        _itemScrollView.contentSize = CGSizeMake(kScreenWidth * 3,0);

        
        _itemScrollView.pagingEnabled = YES;
        _itemScrollView.showsVerticalScrollIndicator = NO;
        _itemScrollView.showsHorizontalScrollIndicator = NO;
        _itemScrollView.bounces = NO;
    }
    return _itemScrollView;
}
- (SDCycleScrollView *)banaCommonView
{
    if (!_banaCommonView) {
        _banaCommonView = [[SDCycleScrollView alloc] init];
        _banaCommonView.frame= CGRectMake(0, 0, kScreenWidth, 315);
       
        _banaCommonView.pageControlAliment = SDCycleScrollViewPageContolAlimentCenter;
        _banaCommonView.backgroundColor = [UIColor clearColor];
        _banaCommonView.bannerImageViewContentMode = UIViewContentModeScaleToFill;

//        _banaCommonView.localizationImageNamesGroup = @[placeholder_image_rectangle];
        _banaCommonView.layer.cornerRadius = 7;
        _banaCommonView.layer.masksToBounds = YES;
        
        
        
        if (self.userInfo.privatePhoto != nil && self.userInfo.privatePhoto.count > 0) {
               NSMutableArray *array = [NSMutableArray array];
               for (UserPhoto *info in self.userInfo.privatePhoto) {
                   [array addObject:info.photoUrl];
               }
               _banaCommonView.imageURLStringsGroup = [array copy];
           }
          
        
        

    }
    return _banaCommonView;
}
@end
