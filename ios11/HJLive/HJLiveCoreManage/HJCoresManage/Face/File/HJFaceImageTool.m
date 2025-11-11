//
//  HJFaceImageTool.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFaceImageTool.h"
#import "HJFaceCoreClient.h"
#import "UIImage+Resize.h"

#define SPACES 15
@interface HJFaceImageTool () <CAAnimationDelegate>

@end


@implementation HJFaceImageTool {
    dispatch_queue_t faceQueue;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        faceQueue = dispatch_queue_create("com.yy.face.xcface.creatFace", DISPATCH_QUEUE_SERIAL);
    }
    return self;
}


+ (instancetype)shareFaceImageTool
{
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}


- (void)queryImage:(HJFaceReceiveInfo *)receiveInfo
         imageView:(UIImageView *)imageView
           success:(void (^)(HJFaceReceiveInfo *info))success
           failure:(void (^)(NSError *))failure {
    
    __block CGSize size = imageView.frame.size;
    if (receiveInfo.faceId == 19) {
        if (size.width == 80 || size.width == 64) {
            imageView.transform = CGAffineTransformMakeScale(0.6, 0.6);
        }
    } else {
        imageView.transform = CGAffineTransformMakeScale(1, 1);
    }
    size = CGSizeMake(80, 80);
//    @weakify(self);
//    dispatch_async(faceQueue, ^{
//        @strongify(self);
//
//    });
    if (receiveInfo.resultIndexes.count > 0) {
        __block UIImage * result = [self combineImageInOne:receiveInfo size:size];
        receiveInfo.resultImage = result;
    }
    dispatch_main_sync_safe(^{
        success(receiveInfo);
        [self addAnimateInImageView:imageView receiveInfo:receiveInfo];
    });
}

- (void)addAnimateInImageView:(UIImageView *)imageView receiveInfo:(HJFaceReceiveInfo *)receiveInfo {
    HJFaceConfigInfo *configInfo = [GetCore(HJFaceCore)findFaceInfoById:receiveInfo.faceId];
    if (receiveInfo.resultIndexes.count > 0) {
        /*==================== 动画数组 ================= */
        //创建CAKeyframeAnimation
        CAKeyframeAnimation *animation = [CAKeyframeAnimation animationWithKeyPath:@"contents"];
        animation.duration = configInfo.animDuration / 1000.0;
        animation.delegate = self;
        animation.repeatCount = configInfo.animRepeatCount;
        animation.removedOnCompletion = YES;
        animation.calculationMode = kCAAnimationDiscrete;
        
        //存放图片的数组
        NSMutableArray *faceArray = [NSMutableArray array];
        
        for (int i = (short)configInfo.animStartPos; i <= (short)configInfo.animEndPos; i ++) {
            UIImage *image = [GetCore(HJFaceCore) findFaceImageByConfig:configInfo index:i];
            if (image) {
                CGImageRef cgimg = image.CGImage;
                [faceArray addObject:(__bridge UIImage *)cgimg];
            }else {
                break;
            }
            
        }
        if (faceArray.count > 0) {
            animation.values = faceArray;
        }else {
            return;
        }
        
        
        /*==================== 结果数组 ================= */
        CAKeyframeAnimation *resultAnimation = [CAKeyframeAnimation animationWithKeyPath:@"contents"];
        resultAnimation.duration = 5;
        resultAnimation.delegate = self;
        resultAnimation.beginTime = configInfo.animRepeatCount * configInfo.animDuration / 1000.0;
        //存放图片的数组
        NSMutableArray *resultArray = [NSMutableArray array];

        if (receiveInfo.resultImage) {
            [resultArray addObject:(__bridge UIImage *)receiveInfo.resultImage.CGImage];
        }else {
            return;
        }
        
        if (resultArray.count > 0) {
           resultAnimation.values = resultArray;
        }else {
            return;
        }
        
        resultAnimation.removedOnCompletion = YES;

        CAAnimationGroup *group = [CAAnimationGroup animation];
        group.animations = @[animation,resultAnimation];
        group.duration = 5 + (configInfo.animDuration / 1000.0) * configInfo.animRepeatCount;
        [imageView.layer addAnimation:group forKey:nil];
//        if (receiveInfo.faceId == 22) {
//            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(6 + (configInfo.animDuration / 1000.0) * configInfo.animRepeatCount * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                imageView.transform = CGAffineTransformMakeScale(1, 1);
//            });
//        }
//        [imageView setImage:receiveInfo.resultImage];
    } else {
        /*==================== 动画数组 ================= */
        //创建CAKeyframeAnimation
        CAKeyframeAnimation *animation = [CAKeyframeAnimation animationWithKeyPath:@"contents"];
        animation.duration = configInfo.animDuration / 1000.0;
        animation.delegate = self;
        animation.repeatCount = configInfo.animRepeatCount;
        animation.removedOnCompletion = YES;
        animation.calculationMode = kCAAnimationDiscrete;
        //存放图片的数组
        NSMutableArray *faceArray = [NSMutableArray array];
        
        for (int i = (short)configInfo.animStartPos; i <= (short)configInfo.animEndPos; i ++) {
            UIImage *image = [GetCore(HJFaceCore)findFaceImageByConfig:configInfo index:i];
            if (image) {
                CGImageRef cgimg = image.CGImage;
                [faceArray addObject:(__bridge UIImage *)cgimg];
            }else {
                break;
            }
        }
        if (faceArray.count > 0) {
            animation.values = faceArray;
            [imageView.layer removeAnimationForKey:@"face"];
            [imageView.layer addAnimation:animation forKey:@"face"];
        }else {
            return;
        }
    }
    
}

//- (void)saveImageWithArr:(NSMutableArray<HJFaceReceiveInfo *> *)faceRecieveInfos
//               imageView:(UIImageView *)imageView
//                 success:(void (^)(NSMutableArray<HJFaceReceiveInfo *> *))success
//                 failure:(void (^)(NSError *))failure{
//    __block CGSize size = imageView.frame.size;
//    @weakify(self);
//    dispatch_async(faceQueue, ^{
//        @strongify(self);
//        for (HJFaceReceiveInfo *item in faceRecieveInfos) {
//            if (item.resultIndexes.count > 0) { //运气表情
//
//                __block UIImage * result = [self combineImageInOne:item size:size];
//                item.resultImage = result;
//            }else { //普通表情
////                HJFaceConfigInfo *configInfo = [GetCore(FaceCore)findFaceInfoById:item.faceId];
////                UIImage *singleImage = [GetCore(FaceCore)findFaceImageById:faceInfo.faceId index:index];
////                item.resultImage =
//            }
//        }
//        dispatch_main_sync_safe(^{
//            success(faceRecieveInfos);
//        });
//    });
//
//}

- (UIImage *)combineImageInOne:(HJFaceReceiveInfo *)faceInfo size:(CGSize)size {
    
    UIImage *result;
    NSInteger faceCount = faceInfo.resultIndexes.count > 9 ? 9 : faceInfo.resultIndexes.count;
    
    CGFloat x = 0;
    CGFloat y = 0;
    
    CGFloat width = 0.0;
    CGFloat height;
    CGFloat whBit = 174.0 / 128.0;
    switch (faceInfo.resultIndexes.count) {
        case 1:
        {
            width = size.width;
            height = width / whBit;
        }
        break;
        
        case 2:
        case 3:
        case 4:
        {
            width = size.width / 2;
            height = width / whBit;
//            height = size.height / 2;
//            height =
        }
        break;
        
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        {
            width = size.width / 3;
            height = width / whBit;
        }
        break;
        
        default:
        width = 0;
        height = 0;
        break;
    }
    
    CGFloat spaceX3 = 0; //三张图时的X间距
    CGFloat spaceX2 = (size.width - width * 2) / 2; //两张图时的X边距
    CGFloat spaceX1 = (size.width - width) / 2; //一张图时的X边距
    
    CGFloat spaceY3 = 0; //三张图时的Y间距
    CGFloat spaceY2 = (size.height - height * 2) / 2; //两张图时的Y间距
    CGFloat spaceY1 = (size.height - height) / 2; //一张图时的Y间距
    
    y = faceCount > 6 ? spaceY3 : (faceCount >= 3 ? spaceY2 : spaceY1);
    x = faceCount % 3 == 0 && faceCount > 3 ? spaceX3 : (faceCount % 3 == 2 ? spaceX2 : spaceX1);
    

    UIGraphicsBeginImageContextWithOptions(size, false, [UIScreen mainScreen].scale);
    for (int i = 0; i < faceCount; i++) {
        NSInteger index = [faceInfo.resultIndexes[i] integerValue];
        HJFaceConfigInfo *configInfo = [GetCore(HJFaceCore)findFaceInfoById:faceInfo.faceId];
        UIImage *singleImage = [GetCore(HJFaceCore)findFaceImageById:faceInfo.faceId index:index];
        
        if (faceCount == 1) { //只有一张图片的时候直接返回不做处理
            return singleImage;
            break;
        }
        
        if (configInfo.displayType == XCFaceDisplayTypeFlow) {
            [singleImage drawInRect:CGRectMake(x, y, width, height)];
//            x = width * (i % 3);
            if (i % 3 == 0) {   // 换行
                y += (height + spaceY3);
                x = spaceX3;
            }
            else if (i == 2 && faceCount == 3) {  // 换行，只有三个时
                y += (height + spaceY3);
                x = spaceX2;
            }
            else {
                x += (width + spaceX3);
            }
//            y =
        }else if (configInfo.displayType == XCFaceDisplayTypeOverLay) {
            CGFloat whBit = singleImage.size.width / singleImage.size.height;
            width = size.width - (faceInfo.resultIndexes.count - 1) * SPACES;
            height = width / whBit;
            x = 0 + i * SPACES;
//            if (faceInfo.faceId == 22) {
//                if (i == 0) {
//                    x = 7;
//                } else {
//                    x = 7 + i * 8;
//                }
//            }
            y = size.height / 2 - height / 2;
            [singleImage drawInRect:CGRectMake(x, y, width, height)];
        }
    }
    result = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    return result;
}


@end
