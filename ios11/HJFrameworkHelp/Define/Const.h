#import <UIKit/UIKit.h>

/*---------------常量----------------------*/
UIKIT_EXTERN CGFloat const TitleHeight;


UIKIT_EXTERN NSString * const kHomeTagUserDefaultKey;//首页tag
UIKIT_EXTERN NSString * const kRoomTagUserDefaultKey;//room tag

UIKIT_EXTERN NSString * const kImageTypeRoomBg;         //房间背景20x20
UIKIT_EXTERN NSString * const kImageTypeRoomFace;       //房间表情
UIKIT_EXTERN NSString * const kImageTypeRoomGift;       //房间礼物
UIKIT_EXTERN NSString * const kImageTypeHomePageItem;   //首页144x144
UIKIT_EXTERN NSString * const kImageTypeHomeBanner;     //首页banner220x660
UIKIT_EXTERN NSString * const kImageTypeUserIcon;       //用户头像60x60
UIKIT_EXTERN NSString * const kImageTypeUserLibary;     //用户相册60x60
UIKIT_EXTERN NSString * const kImageTypeUserLibaryDetail;//用户相册大图nil





/*---------------通知----------------------*/
//退出账号
UIKIT_EXTERN NSString  * const kApplicationDidBecomeActiveNotification;

/*---------------枚举----------------------*/


\
typedef NS_ENUM(NSUInteger,ImageType){
    ImageTypeRoomBg,                //房间背景20x20
    ImageTypeRoomFace,              //房间表情
    ImageTypeRoomGift,              //房间礼物
    ImageTypeHomePageItem,          //首页144x144
    ImageTypeHomeBanner,            //首页banner220x660
    ImageTypeUserIcon,              //用户头像60x60
    ImageTypeUserLibary,            //用户相册60x60
    ImageTypeUserLibaryDetail,      //用户相册大图
    ImageTypeUserRoomTag,           //roomTag
    
};




