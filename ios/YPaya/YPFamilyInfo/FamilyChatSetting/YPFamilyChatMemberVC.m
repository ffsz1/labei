//
//  YPFamilyChatMemberVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyChatMemberVC.h"

#import "YPFamilyChatUserCell.h"

#import "YPNIMKitInfo.h"
#import "YPNIMKit.h"

#import "NSString+YPNIMKit.h"

@interface YPFamilyChatMemberVC ()<UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;

@end

@implementation YPFamilyChatMemberVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
}

#pragma mark - UICollectionViewDelegateFlowLayout
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.memberData.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(kScreenWidth/4, 89);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(15, 0, 0, 0);
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPFamilyChatUserCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPFamilyChatUserCell" forIndexPath:indexPath];
    
    NIMTeamMember *member = [self.memberData safeObjectAtIndex:indexPath.item];
    //    cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[self changeImageUrl:member.hea]]
    
    
    YPNIMKitInfo *info            = [[YPNIMKit sharedKit] infoByUser:member.userId option:nil];
    cell.nameLabel.text = info.showName;
    [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[info.avatarUrlString cutAvatarImageSize]]];
    
    NSString *imageName = @"";
    if (member.type == 1) {
        imageName = @"yp_family_owner";
    }else if(member.type == 2){
        imageName = @"yp_family_manager";
    }
    cell.roleImageView.image = [UIImage imageNamed:imageName];
    
    return cell;
}

//拦截云信图片链接，去掉后缀
- (NSString *)changeImageUrl:(NSString *)urlStr
{
    NSString *tmpStr = @"";
    if (urlStr != nil) {
        NSRange range = [urlStr rangeOfString:@"thumbnail"];
        
        if (range.location>0 && range.length>0) {
            tmpStr = [urlStr substringToIndex:range.location+range.length];
        }
        return tmpStr;
    }
    return tmpStr;
}





@end
