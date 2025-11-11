//
//  HJFamilyEditTableController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyEditTableController.h"
#import "AvatarControl.h"
#import "YYActionSheetViewController.h"
#import "ImagePickerUtils.h"
#import "NSDate+Util.h"
#import "HJVersionCoreHelp.h"
#import "HJFileCoreClient.h"
#import "HJFileCore.h"
#import "UIView+XCToast.h"

#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"

@interface HJFamilyEditTableController ()<UITextViewDelegate,HJFileCoreClient, UINavigationControllerDelegate, UIImagePickerControllerDelegate,HJFamilyCoreClient>

@property (weak, nonatomic) IBOutlet AvatarControl *avatar;

@property (weak, nonatomic) IBOutlet UITextView *textView;
@property (weak, nonatomic) IBOutlet UILabel *textViewPlaceHolderLabel;
@property (weak, nonatomic) IBOutlet UILabel *textCountLabel;

@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (nonatomic, assign) BOOL isAlreadyShowAlert;

@end

@implementation HJFamilyEditTableController

- (void)dealloc {
    RemoveCoreClientAll(self);
}
- (UIStatusBarStyle)preferredStatusBarStyle
{
//    return UIStatusBarStyleLightContent; //返回白色
    return UIStatusBarStyleDefault;    //返回黑色
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"编辑";
    [self setupSaveBtn];
    
    self.textView.delegate = self;
    AddCoreClient(HJFileCoreClient, self);
    AddCoreClient(HJFamilyCoreClient, self);
    
    self.avatar.layer.cornerRadius = 32.5;
    self.avatar.layer.masksToBounds = YES;
    
    if (self.creatTime > 0) {
        
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        dateFormatter.dateFormat = @"YYYY-MM-dd";
        NSString *dateStr = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:self.creatTime/1000]];
        self.timeLabel.text = dateStr;
    }
    
    if (self.imageUrl.length) {
        
        [self.avatar setImageURL:[NSURL URLWithString:self.imageUrl]];
    }
    
    
    self.textView.text = self.text;
    self.textViewPlaceHolderLabel.hidden = self.text.length;
    
    self.textCountLabel.text = [NSString stringWithFormat:@"%zd/50",self.text.length];
}

- (void)setupSaveBtn {
    
    CGSize size = [@"保存" boundingRectWithSize:CGSizeMake(CGFLOAT_MAX, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16.f]} context:nil].size;
    
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, size.width, size.height)];
    [btn setTitleColor:UIColorHex(666666) forState:UIControlStateNormal];
    [btn setTitle:@"保存" forState:UIControlStateNormal];
    btn.titleLabel.font = [UIFont systemFontOfSize:16.f];
    [btn addTarget:self action:@selector(saveBtnAction) forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btn];
}

- (void)saveBtnAction {
    
    if (!self.imageUrl.length) {
        [MBProgressHUD showError:@"请选择家族logo"];
        return;
    }
    
    if (!self.textView.text.length) {
        [MBProgressHUD showError:@"请输入家族广告"];
        return;
    }
    
    [MBProgressHUD showMessage:@""];
//    [GetCore(FamilyCore) familyEditFamilyTeamWithFamilyId:self.familyId logo:self.imageUrl ba notice:self.textView.text];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.row == 0) {
        long time = self.openTime - self.creatTime;
        CGFloat day = time / (24*60*60*1000.f);
        if (day >= 30.f) {
            
            [self showAvatarController];
        }
        else {
            NSString *alertStr = @"";
            if (!self.isAlreadyShowAlert) {
                alertStr = @"头像一个月只可以换一次";
            }
            else {
                alertStr = @"一月只可以更换一次";
            }
            
            [MBProgressHUD showError:alertStr];
            self.isAlreadyShowAlert = YES;
        }
    }
//    else if (indexPath.row == 2) {
//        [self showDateController];
//    }
}

-(void)showAvatarController {
    YYActionSheetViewController *actionSheet = [ImagePickerUtils showImagePickerSystemLibSheet:^{
        
    } title:nil view:self.view tailor:YES delegate:self];
    [actionSheet show];
}

//-(void)showDateController{
//    YYActionSheetViewController *sheet = [[YYActionSheetViewController alloc] init];
//    UIDatePicker *datePicker = [[UIDatePicker alloc] init];
//    datePicker.datePickerMode = UIDatePickerModeDate;
//    NSLocale *locale = [[NSLocale alloc]initWithLocaleIdentifier:@"zh_CN"];
//    datePicker.locale = locale;
//    datePicker.date = [NSDate dateWithTimeIntervalSince1970:self.creatTime / 1000];
//    datePicker.maximumDate = [NSDate date];
//    datePicker.backgroundColor = [UIColor whiteColor];
//    [sheet addTitleView:datePicker];
//
//    @weakify(self)
//    [sheet addButtonWithTitle:NSLocalizedString(XCRoomConfirm, nil) block:^(YYActionSheetViewController *controller) {
//        @strongify(self)
//        NSDate *theDate = datePicker.date;
//        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
//        dateFormatter.dateFormat = @"YYYY-MM-dd";
//        NSString *dateStr = [dateFormatter stringFromDate:theDate];
//        self.timeLabel.text = dateStr;
//        self.creatTime = [theDate timeIntervalSince1970] * 1000;
//    }];
//    [sheet show];
//}
//
//- (int)intervalFromLastDate: (NSDate *) d1 toTheDate:(NSDate *) d2
//
//{
//
//    NSTimeInterval late1=[d1 timeIntervalSince1970]*1;
//
//    NSTimeInterval late2=[d2 timeIntervalSince1970]*1;
//
//    NSTimeInterval cha=late2-late1;
//
//    NSString *timeString=@"";
//
//    int day = (int)cha/3600/24;
//
//    return day;
//
//}
//
//-(NSDate *)getNDay:(NSInteger)n{
//
//    NSDate*nowDate = [NSDate date];
//    NSDate* theDate;
//    if(n!=0){
//
//        NSTimeInterval  oneDay = 24*60*60*1;  //1天的长度
//        theDate = [nowDate initWithTimeIntervalSinceNow: oneDay*n ];//initWithTimeIntervalSinceNow是从现在往前后推的秒数
//
//    }else{
//
//        theDate = nowDate;
//    }
//
//    return theDate;
//}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerEditedImage];
        if (selectedPhoto) {
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }
            [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
            [GetCore(HJFileCore) qiNiuUploadImage:selectedPhoto uploadType:UploadImageTypeAvtor];
        }
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - FileCoreClient user 7N
- (void)didUploadAvtorImageSuccessUseQiNiu:(NSString *)key{
    NSLog(@"didUploadAvtorImageSuccessUseQiNiu");
    [MBProgressHUD hideHUD];
    NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
    self.imageUrl = url;
    [self.avatar setImageURL:[NSURL URLWithString:self.imageUrl]];
}
- (void)didUploadAvtorImageFailUseQiNiu:(NSString *)message{
    NSLog(@"didUploadAvtorImageFailUseQiNiu:%@",message);
    [MBProgressHUD hideHUD];
    [self.view showToast:@"请求失败，请检查网络" position:YYToastPositionCenter];
}

#pragma mark - FamilyCoreClient
// 修改家族信息
- (void)familyEditFamilyTeamSuccess {
    
    [MBProgressHUD showSuccess:@"更新成功"];
    
    if (self.update) {
        self.update();
    }
    
    if (self.navigationController.viewControllers.count >= 2) {
//        UIViewController *vc = self.navigationController.viewControllers[self.navigationController.viewControllers.count - 2];
//        if ([vc isKindOfClass:[XCFamilyWKWebViewController class]]) {
//            XCFamilyWKWebViewController *webVC = (XCFamilyWKWebViewController *)vc;
//            NSURLRequest *request = [NSURLRequest requestWithURL:webVC.url];
//            [webVC.webview loadRequest:request];
//        }
    }
    
    [self.navigationController popViewControllerAnimated:YES];
    
}
- (void)familyEditFamilyTeamFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
    
}

#pragma mark - UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView {
    
    if ([textView isEqual:self.textView]) {
        self.textViewPlaceHolderLabel.hidden = textView.text.length;
        
        self.textCountLabel.text = [NSString stringWithFormat:@"%zd/50",textView.text.length];
    }
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    NSString *newString = [textView.text stringByReplacingCharactersInRange:range withString:text];
    
    if ([textView isEqual:self.textView]) {
        if (newString.length > 50) {
            return NO;
        }
    }
    
    return YES;
}

@end
