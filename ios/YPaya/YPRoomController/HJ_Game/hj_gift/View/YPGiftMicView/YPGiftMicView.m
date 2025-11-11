//
//  YPGiftMicView.m
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import "YPGiftMicView.h"

#import "YPGiftMicCell.h"

#import "YPRoomQueueCoreV2Help.h"
#import "YPImRoomCoreV2.h"


@interface YPGiftMicView ()<UICollectionViewDelegate,UICollectionViewDataSource>

@property (strong,nonatomic) UICollectionView *collectionView;

@property (nonatomic,strong) NSMutableArray *micArr;

@property (nonatomic,strong) NSMutableArray *selArr;


@end

@implementation YPGiftMicView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
        [self getOnMicUIDs];
        
    }
    return self;
    
}



- (void)setUI
{
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.mas_equalTo(self);
    }];
    
}

- (void)getOnMicUIDs
{
    NSMutableArray *resultArr = [self getArr];
    
    //添加全麦位
    YPIMQueueItem *allMic = [[YPIMQueueItem alloc] init];
    [resultArr insertObject:allMic atIndex:0];
    
    self.micArr = resultArr;
    
    //初始化选择数组
    [self setSelUID];

}

- (NSMutableArray *)getArr
{
    NSMutableArray *temp = [GetCore(YPRoomQueueCoreV2Help) findOnMicMember];
    NSMutableArray *resultArr = [[NSMutableArray alloc] init];
    
    UserInfo *ownerInfo = GetCore(YPImRoomCoreV2).roomOwnerInfo;
    UserInfo *myInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:GetCore(YPAuthCoreHelp).getUid.userIDValue];
    
    //筛选掉自己的麦位
    BOOL hadOwner = NO;
    for (int i = 0; i<temp.count; i++) {
        YPIMQueueItem *model = temp[i];
        if (model.queueInfo.chatRoomMember.account.userIDValue != myInfo.uid) {
            
            if (model.queueInfo.chatRoomMember.account.userIDValue == ownerInfo.uid) {
                hadOwner = YES;
            }
            
            [resultArr addObject:model];
        }
    }
    
    //添加房主位
    if (!hadOwner) {
        if (myInfo.uid != ownerInfo.uid) {
            YPIMQueueItem *ownerMic = [[YPIMQueueItem alloc] init];
            ownerMic.position = -1;
            
            YPRoomQueueInfo *queueInfo = [[YPRoomQueueInfo alloc] init];
            YPChatRoomMember *member = [[YPChatRoomMember alloc] init];
            member.account = [NSString stringWithFormat:@"%lld",ownerInfo.uid];
            member.avatar = ownerInfo.avatar;
            queueInfo.chatRoomMember = member;
            ownerMic.queueInfo = queueInfo;
            
            [resultArr insertObject:ownerMic atIndex:0];
        }
    }
    return resultArr;
}

//设置选中id
- (void)setSelUID
{
    self.selArr = [[NSMutableArray alloc] init];
    for (int i =0; i<self.micArr.count; i++) {
        [self.selArr addObject:@(NO)];
    }
    
    if (self.targetUID>0) {
        for (int i = 1; i<self.micArr.count; i++) {
            YPIMQueueItem *info = self.micArr[i];
            if (info.queueInfo.chatRoomMember.account.userIDValue == self.targetUID) {
                [self.selArr replaceObjectAtIndex:i withObject:@(YES)];
                break;
            }
        }
    }else{
        if (self.selArr.count>1) {
            [self.selArr replaceObjectAtIndex:1 withObject:@(YES)];
        }
    }
    
    [self.collectionView reloadData];
}


#pragma mark <UICollectionViewDelegate,UICollectionViewDataSource>
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.micArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPGiftMicCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPGiftMicCell" forIndexPath:indexPath];
    
    cell.item = indexPath.item;
    
    if (indexPath.item>0) {
        YPIMQueueItem *info = self.micArr[indexPath.item];
        cell.info = info;
    }else{
        cell.info = nil;
    }
    
    BOOL isSel = [self.selArr[indexPath.item] boolValue];
    [cell setSelStytle:isSel];
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    BOOL isSel = [self.selArr[indexPath.item] boolValue];

    //全麦
    if (indexPath.item == 0) {
        
        //取消选中
        if (isSel) {
            [self resetSelArr:NO];
            [self.selArr replaceObjectAtIndex:1 withObject:@(YES)];
        }else{
            //选中全部
            [self resetSelArr:YES];
        }
    }else{
        
        //选择单个
        [self.selArr replaceObjectAtIndex:0 withObject:@(NO)];
        
        if (isSel) {
            [self.selArr replaceObjectAtIndex:indexPath.item withObject:@(NO)];
        }else{
            [self.selArr replaceObjectAtIndex:indexPath.item withObject:@(YES)];
        }
    }
    
    //检测是否选中全部麦上成员   是->选中全麦
    if (self.selArr.count>2) {
        int selCout = 0;
        for (int i = 1; i<self.selArr.count; i++) {
            BOOL isSel = [self.selArr[i] boolValue];
            if (isSel) selCout += 1;
        }
        //选中全麦
        if (selCout == self.selArr.count - 1) {
            [self.selArr replaceObjectAtIndex:0 withObject:@(YES)];
        }
    }
    
    
    
    
    [self.collectionView reloadData];

}



//全部选中、取消选中
- (void)resetSelArr:(BOOL)setSel
{
    for (int i =0; i<self.selArr.count; i++) {
        [self.selArr replaceObjectAtIndex:i withObject:@(setSel)];
    }
}

- (BOOL)isAllMic
{
    BOOL isSel = [self.selArr[0] boolValue];
    
    //需求：只有房主一人时，不算全麦
    if (self.selArr.count == 2) {
        return NO;
    }
    return isSel;
}

- (NSString *)getSelUidsString
{
    NSString *uids = [[NSString alloc]init];
    for (int i =1; i<self.selArr.count; i++) {
        BOOL isSel = [self.selArr[i] boolValue];
        if (isSel) {
            YPIMQueueItem *info = self.micArr[i];
//            [uids addObject:info.queueInfo.chatRoomMember.account];
            
            if (uids.length ==0) {
                if (info.queueInfo.chatRoomMember.account !=nil) {
                     uids = [uids stringByAppendingString:info.queueInfo.chatRoomMember.account];
                }
               
            }else{
                if (info.queueInfo.chatRoomMember.account !=nil) {
                     uids = [uids stringByAppendingString:[NSString stringWithFormat:@",%@",info.queueInfo.chatRoomMember.account]];
                }
               
            }

        }
    }
    return uids;
}


- (UserID)getFirstSelUID
{
    UserID firstUid = 0;
    for (int i =1; i<self.selArr.count; i++) {
        BOOL isSel = [self.selArr[i] boolValue];
        if (isSel) {
            YPIMQueueItem *info = self.micArr[i];
            firstUid = info.queueInfo.chatRoomMember.account.userIDValue;
            break;
        }
    }
    return firstUid;
}



#pragma mark setter/getter
- (void)setTargetUID:(UserID)targetUID
{
    _targetUID = targetUID;
    
    [self setSelUID];
    
}

- (UICollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.itemSize = CGSizeMake(44, 50);
        layout.minimumInteritemSpacing = 10;
        layout.minimumLineSpacing = 20;
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        
        
        layout.sectionInset = UIEdgeInsetsMake(20, 15, 0, 15);
        
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        [self addSubview:_collectionView];
        [_collectionView registerNib:[UINib nibWithNibName:@"YPGiftMicCell" bundle:nil] forCellWithReuseIdentifier:@"YPGiftMicCell"];
        
        
        _collectionView.backgroundColor = [UIColor clearColor];
        
    }
    return _collectionView;
}

@end
