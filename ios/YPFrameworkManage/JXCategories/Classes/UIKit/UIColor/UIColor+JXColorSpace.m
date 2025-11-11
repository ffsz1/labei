//
//  UIColor+JXColorSpace.m
//  JXCategories
//
//  Created by colin on 2019/2/3.
//

#import "UIColor+JXColorSpace.h"

/**
 *  比较颜色value值, 若在0-1之间返回value，若大于1返回1, 小于0返回0
 */
#ifndef JX_CLAMP_COLOR_VALUE
#define JX_CLAMP_COLOR_VALUE(v) (v) = (v) < 0 ? 0 : (v) > 1 ? 1 : (v)
#endif

@implementation UIColor (JXColorSpace)

#pragma mark - Info
- (CGColorSpaceModel)jx_colorSpaceModel {
    return CGColorSpaceGetModel(CGColorGetColorSpace(self.CGColor));
}

- (NSString *)jx_colorSpaceString {
    CGColorSpaceModel model =  CGColorSpaceGetModel(CGColorGetColorSpace(self.CGColor));
    switch (model) {
        case kCGColorSpaceModelUnknown:
            return @"kCGColorSpaceModelUnknown";
        case kCGColorSpaceModelMonochrome:
            return @"kCGColorSpaceModelMonochrome";
        case kCGColorSpaceModelRGB:
            return @"kCGColorSpaceModelRGB";
        case kCGColorSpaceModelCMYK:
            return @"kCGColorSpaceModelCMYK";
        case kCGColorSpaceModelLab:
            return @"kCGColorSpaceModelLab";
        case kCGColorSpaceModelDeviceN:
            return @"kCGColorSpaceModelDeviceN";
        case kCGColorSpaceModelIndexed:
            return @"kCGColorSpaceModelIndexed";
        case kCGColorSpaceModelPattern:
            return @"kCGColorSpaceModelPattern";
        default:
            return @"ColorSpaceInvalid";
    }
}

#pragma mark - Convert
void JXRGB2HSL(CGFloat red, CGFloat green, CGFloat blue,
               CGFloat *hue, CGFloat *saturation, CGFloat *lightness) {
    /* RGB -> HSL算法
     1.把RGB值转成[0,1]中数值;
     2.找出R,G和B中的最大值<maxColor>和最小值<minColor>;
     3.计算亮度:L=(maxColor + minColor)/2
     4.如果最大和最小的颜色值相同, 即表示灰色, 那么S定义为0, 而H未定义并在程序中通常写成0
     5.否则，根据明度L计算饱和度S:
     If L<0.5, S=(maxColor-minColor)/(maxColor+minColor)
     If L>=0.5, S=(maxColor-mincolor)/(2.0-maxColor-minColor)
     6.计算色相H[0°, 360°]/[0,1]:
     If R=maxColor, H=(G-B)/(maxColor-minColor)
     If G=maxColor, H=2.0+(B-R)/(maxColor-minColor)
     If B=maxColor, H=4.0+(R-G)/(maxColor-minColor)
     H=H*60°(H=H/6),如果H为负值，则加360°(1)
     
     说明:
     1.由步骤3的式子可以看出亮度仅与图像的最多颜色成分和最少的颜色成分的总量有关。亮度越低, 图像越趋于黑色。亮度越高图像越趋于明亮的白色
     2.由步骤5的式子可以看出饱和度与图像的最多颜色成分和最少的颜色成分的差量有关。饱和度越小, 图像越趋于灰度图像。饱和度越大, 图像越鲜艳, 给人的感觉是彩色的, 而不是黑白灰的图像。
     3.由第6步的计算看，H分成0～6区域(360°/0°红<Red>、60°黄(Yellow)、120°绿<Green>、180°青<Cyan>、240°蓝<Blue>、300°品红<Magenta>)。RGB颜色空间是一个立方体而HSL颜色空间是两个六角形锥体，其中的L是RGB立方体的主对角线。因此, RGB立方体的顶点:红、黄、绿、青、蓝和品红就成为HSL六角形的顶点，而数值0~6就告诉我们H在哪个部分
     */
    JX_CLAMP_COLOR_VALUE(red); // [0,1]
    JX_CLAMP_COLOR_VALUE(green);
    JX_CLAMP_COLOR_VALUE(blue);
    
    CGFloat max, min, delta, sum;
    max = fmaxf(red, fmaxf(green, blue)); // fmaxf -> 返回两个float参数最大值
    min = fminf(red, fminf(green, blue)); // fminf -> 返回两个float参数最小值
    delta = max - min; //  差值[0,1]
    sum = max + min; // [0,2]
    
    *lightness = sum / 2;           // Lightness -> 亮度[0,1] HLS -> L
    if (delta == 0) { // No Saturation, so Hue is undefined (achromatic) 无饱和度, 色相未定义(单色)
        *hue = *saturation = 0;
        return;
    }
    *saturation = delta / (sum < 1 ? sum : 2 - sum); // Saturation (sum < 1 ? sum : 2 - sum) -> [0,1]
    if (red == max) *hue = (green - blue) / delta / 6; // color between m & y 颜色在品与黄色红间[300°,60°]
    else if (green == max) *hue = (2 + (blue - red) / delta) / 6; // color between y & c 颜色在黄色与青色间[60°,180°]
    else *hue = (4 + (red - green) / delta) / 6; // color between c & m 颜色在青色与品红间[180°,300°]
    if (*hue < 0) *hue += 1;
}

void JXHSL2RGB(CGFloat hue, CGFloat saturation, CGFloat lightness,
               CGFloat *red, CGFloat *green, CGFloat *blue) {
    /* HSL -> RGB算法
     1.If S=0, 表示灰, 定义R,G和B都为L
     2.否则, 测试L:
     If L<=0.5, temp2=L*(1.0+S)
     If L>0.5, temp2=L+S-L*S
     3.temp1=2.0*L-temp2
     4.把H转换到[0,1]
     5.对于R,G,B, 计算另外的临时值temp3, 方法如下:
     for R, temp3=H+1.0/3.0
     for G, temp3=H
     for B, temp3=H-1.0/3.0
     If temp3<0, temp3=temp3+1.0
     If temp3>1, temp3=temp3-1.0
     6.对于R,G,B做如下测试:
     If 6.0*temp3<1, color=temp1+(temp2-temp1)*6.0*temp3
     Else If 2.0*temp3<1,color=temp2
     Else If 3.0*temp3<2
     color=temp1+(temp2-temp1)*((2.0/3.0)-temp3)*6.0
     Else color=temp1
     */
    JX_CLAMP_COLOR_VALUE(hue); // [0,1]
    JX_CLAMP_COLOR_VALUE(saturation);
    JX_CLAMP_COLOR_VALUE(lightness);
    
    if (saturation == 0) { // No Saturation, Hue is undefined (achromatic) 无饱和度, 色相未定义(单色)
        *red = *green = *blue = lightness;
        return;
    }
    
    CGFloat q;
    q = (lightness <= 0.5) ? (lightness * (1 + saturation)) : (lightness + saturation - (lightness * saturation));
    if (q <= 0) {
        *red = *green = *blue = 0.0;
    } else {
        *red = *green = *blue = 0;
        int sextant;
        CGFloat m, sv, fract, vsf, mid1, mid2;
        m = lightness + lightness - q;
        sv = (q - m) / q;
        if (hue == 1) hue = 0;
        hue *= 6.0;
        sextant = hue;
        fract = hue - sextant;
        vsf = q * sv * fract;
        mid1 = m + vsf;
        mid2 = q - vsf;
        switch (sextant)
        {
            case 0: *red = q; *green = mid1; *blue = m; break;
            case 1: *red = mid2; *green = q; *blue = m; break;
            case 2: *red = m; *green = q; *blue = mid1; break;
            case 3: *red = m; *green = mid2; *blue = q; break;
            case 4: *red = mid1; *green = m; *blue = q; break;
            case 5: *red = q; *green = m; *blue = mid2; break;
        }
    }
}

void JXRGB2HSB(CGFloat red, CGFloat green, CGFloat blue,
               CGFloat *hue, CGFloat *saturation, CGFloat *value) {
    JX_CLAMP_COLOR_VALUE(red);
    JX_CLAMP_COLOR_VALUE(green);
    JX_CLAMP_COLOR_VALUE(blue);
    
    CGFloat max, min, delta;
    max = fmax(red, fmax(green, blue));
    min = fmin(red, fmin(green, blue));
    delta = max - min;
    
    *value = max;               // Brightness
    if (delta == 0) { // No Saturation, so Hue is undefined (achromatic)
        *hue = *saturation = 0;
        return;
    }
    *saturation = delta / max;       // Saturation
    
    if (red == max) *hue = (green - blue) / delta / 6;             // color between y & m
    else if (green == max) *hue = (2 + (blue - red) / delta) / 6;  // color between c & y
    else *hue = (4 + (red - green) / delta) / 6;                // color between m & c
    if (*hue < 0) *hue += 1;
}

void JXHSB2RGB(CGFloat hue, CGFloat saturation, CGFloat value,
               CGFloat *red, CGFloat *green, CGFloat *blue) {
    JX_CLAMP_COLOR_VALUE(hue);
    JX_CLAMP_COLOR_VALUE(saturation);
    JX_CLAMP_COLOR_VALUE(value);
    
    if (saturation == 0) {
        *red = *green = *blue = value; // No Saturation, so Hue is undefined (Achromatic)
    } else {
        int sextant;
        CGFloat f, p, q, t;
        if (hue == 1) hue = 0;
        hue *= 6;
        sextant = floor(hue);
        f = hue - sextant;
        p = value * (1 - saturation);
        q = value * (1 - saturation * f);
        t = value * (1 - saturation * (1 - f));
        switch (sextant)
        {
            case 0: *red = value; *green = t; *blue = p; break;
            case 1: *red = q; *green = value; *blue = p; break;
            case 2: *red = p; *green = value; *blue = t; break;
            case 3: *red = p; *green = q; *blue = value; break;
            case 4: *red = t; *green = p; *blue = value; break;
            case 5: *red = value; *green = p; *blue = q; break;
        }
    }
}

void JXRGB2CMYK(CGFloat red, CGFloat green, CGFloat blue,
                CGFloat *cyan, CGFloat *magenta, CGFloat *yellow, CGFloat *key) {
    JX_CLAMP_COLOR_VALUE(red);
    JX_CLAMP_COLOR_VALUE(green);
    JX_CLAMP_COLOR_VALUE(blue);
    
    *cyan = 1 - red;
    *magenta = 1 - green;
    *yellow = 1 - blue;
    *key = fmin(*cyan, fmin(*magenta, *yellow));
    
    if (*key == 1) {
        *cyan = *magenta = *yellow = 0;   // Pure black
    } else {
        *cyan = (*cyan - *key) / (1 - *key);
        *magenta = (*magenta - *key) / (1 - *key);
        *yellow = (*yellow - *key) / (1 - *key);
    }
}

void JXCMYK2RGB(CGFloat cyan, CGFloat magenta, CGFloat yellow, CGFloat key,
                CGFloat *red, CGFloat *green, CGFloat *blue) {
    JX_CLAMP_COLOR_VALUE(cyan);
    JX_CLAMP_COLOR_VALUE(magenta);
    JX_CLAMP_COLOR_VALUE(yellow);
    JX_CLAMP_COLOR_VALUE(key);
    
    *red = (1 - cyan) * (1 - key);
    *green = (1 - magenta) * (1 - key);
    *blue = (1 - yellow) * (1 - key);
}

void JXRGB2CMY(CGFloat red, CGFloat green, CGFloat blue,
               CGFloat *cyan, CGFloat *magenta, CGFloat *yellow) {
    JX_CLAMP_COLOR_VALUE(red);
    JX_CLAMP_COLOR_VALUE(green);
    JX_CLAMP_COLOR_VALUE(blue);
    
    *cyan = 1 - red;
    *magenta = 1 - green;
    *yellow = 1 - blue;
}

void JXCMY2RGB(CGFloat cyan, CGFloat magenta, CGFloat yellow,
               CGFloat *red, CGFloat *green, CGFloat *blue) {
    JX_CLAMP_COLOR_VALUE(cyan);
    JX_CLAMP_COLOR_VALUE(magenta);
    JX_CLAMP_COLOR_VALUE(yellow);
    
    *red = 1 - cyan;
    *green = 1 - magenta;
    *blue = 1 - yellow;
}

void JXCMY2CMYK(CGFloat cyan, CGFloat magenta, CGFloat yellow,
                CGFloat *cCyan, CGFloat *mMagenta, CGFloat *yYellow, CGFloat *kKey) {
    JX_CLAMP_COLOR_VALUE(cyan);
    JX_CLAMP_COLOR_VALUE(magenta);
    JX_CLAMP_COLOR_VALUE(yellow);
    
    *kKey = fmin(cyan, fmin(magenta, yellow));
    if (*kKey == 1) {
        *cCyan = *mMagenta = *yYellow = 0;   // Pure black
    } else {
        *cCyan = (cyan - *kKey) / (1 - *kKey);
        *mMagenta = (magenta - *kKey) / (1 - *kKey);
        *yYellow = (yellow - *kKey) / (1 - *kKey);
    }
}

void JXCMYK2CMY(CGFloat cyan, CGFloat magenta, CGFloat yellow, CGFloat key,
                CGFloat *cCyan, CGFloat *mMagenta, CGFloat *yYellow) {
    JX_CLAMP_COLOR_VALUE(cyan);
    JX_CLAMP_COLOR_VALUE(magenta);
    JX_CLAMP_COLOR_VALUE(yellow);
    JX_CLAMP_COLOR_VALUE(key);
    
    *cCyan = cyan * (1 - key) + key;
    *mMagenta = magenta * (1 - key) + key;
    *yYellow = yellow * (1 - key) + key;
}

void JXHSB2HSL(CGFloat hue, CGFloat saturation, CGFloat brightness,
               CGFloat *hHue, CGFloat *sSaturation, CGFloat *lLightness) {
    JX_CLAMP_COLOR_VALUE(hue);
    JX_CLAMP_COLOR_VALUE(saturation);
    JX_CLAMP_COLOR_VALUE(brightness);
    
    *hHue = hue;
    *lLightness = (2 - saturation) * brightness / 2;
    if (*lLightness <= 0.5) {
        *sSaturation = (saturation) / ((2 - saturation));
    } else {
        *sSaturation = (saturation * brightness) / (2 - (2 - saturation) * brightness);
    }
}

void JXHSL2HSB(CGFloat hue, CGFloat saturation, CGFloat lightness,
               CGFloat *hHue, CGFloat *sSaturation, CGFloat *bBrightness) {
    JX_CLAMP_COLOR_VALUE(hue);
    JX_CLAMP_COLOR_VALUE(saturation);
    JX_CLAMP_COLOR_VALUE(lightness);
    
    *hHue = hue;
    if (lightness <= 0.5) {
        *bBrightness = (saturation + 1) * lightness;
        *sSaturation = (2 * saturation) / (saturation + 1);
    } else {
        *bBrightness = lightness + saturation * (1 - lightness);
        *sSaturation = (2 * saturation * (1 - lightness)) / *bBrightness;
    }
}

#pragma mark - CMYK
- (CGFloat)jx_cyanOfCMYK {
    CGFloat cyan = 0, magenta, yellow, key, alpha;
    [self jx_getCyan:&cyan magenta:&magenta yellow:&yellow key:&key alpha:&alpha];
    return cyan;
}

- (CGFloat)jx_magentaOfCMYK
{
    CGFloat cyan, magenta = 0, yellow, key, alpha;
    [self jx_getCyan:&cyan magenta:&magenta yellow:&yellow key:&key alpha:&alpha];
    return magenta;
}

- (CGFloat)jx_yellowOfCMYK
{
    CGFloat cyan, magenta, yellow = 0, key, alpha;
    [self jx_getCyan:&cyan magenta:&magenta yellow:&yellow key:&key alpha:&alpha];
    return yellow;
}

- (CGFloat)jx_keyOfCMYK
{
    CGFloat cyan, magenta, yellow, key = 0, alpha;
    [self jx_getCyan:&cyan magenta:&magenta yellow:&yellow key:&key alpha:&alpha];
    return key;
}

- (BOOL)jx_getCyan:(CGFloat *)cyan magenta:(CGFloat *)magenta yellow:(CGFloat *)yellow alpha:(CGFloat *)alpha {
    CGFloat red, green, blue, aAlpha;
    if (![self getRed:&red green:&green blue:&blue alpha:&aAlpha]) {
        return NO;
    }
    JXRGB2CMY(red, green, blue, cyan, magenta, yellow);
    *alpha = aAlpha;
    return YES;
}

- (BOOL)jx_getCyan:(CGFloat *)cyan magenta:(CGFloat *)magenta yellow:(CGFloat *)yellow key:(CGFloat *)key alpha:(CGFloat *)alpha {
    CGFloat red, green, blue, aAlpha;
    if (![self getRed:&red green:&green blue:&blue alpha:&aAlpha]) {
        return NO;
    }
    JXRGB2CMYK(red, green, blue, cyan, magenta, yellow, key);
    *alpha = aAlpha;
    return YES;
}

+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan magenta:(CGFloat)magenta yellow:(CGFloat)yellow {
    return [UIColor jx_colorWithCyan:cyan magenta:magenta yellow:yellow alpha:1];
}

+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan magenta:(CGFloat)magenta yellow:(CGFloat)yellow alpha:(CGFloat)alpha {
    CGFloat red, green, blue;
    JXCMY2RGB(cyan, magenta, yellow, &red, &green, &blue);
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan magenta:(CGFloat)magenta yellow:(CGFloat)yellow key:(CGFloat)key {
    return [UIColor jx_colorWithCyan:cyan magenta:magenta yellow:yellow key:key alpha:1];
}

+ (UIColor *)jx_colorWithCyan:(CGFloat)cyan magenta:(CGFloat)magenta yellow:(CGFloat)yellow key:(CGFloat)key alpha:(CGFloat)alpha {
    CGFloat red, green, blue;
    JXCMYK2RGB(cyan, magenta, yellow, key, &red, &green, &blue);
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

- (UIColor *)jx_colorByChangeCyanOfCMYK:(CGFloat)CyanDelta {
    return [self jx_colorByChangeCyan:CyanDelta magenta:0 yellow:0 key:0 alpha:0];
}

- (UIColor *)jx_colorByChangeMagentaOfCMYK:(CGFloat)magentaDelta {
    return [self jx_colorByChangeCyan:0 magenta:magentaDelta yellow:0 key:0 alpha:0];
}

- (UIColor *)jx_colorByChangeYellowOfCMYK:(CGFloat)yellowDelta {
    return [self jx_colorByChangeCyan:0 magenta:0 yellow:yellowDelta key:0 alpha:0];
}

- (UIColor *)jx_colorByChangeKeyOfCMYK:(CGFloat)keyDelta {
    return [self jx_colorByChangeCyan:0 magenta:0 yellow:0 key:keyDelta alpha:0];
}

- (UIColor *)jx_colorByChangeCyan:(CGFloat)cyanDelta magenta:(CGFloat)magentaDelta yellow:(CGFloat)yellowDelta alpha:(CGFloat)alphaDelta {
    CGFloat cyan, magenta, yellow, alpha;
    if (![self jx_getCyan:&cyan magenta:&magenta yellow:&yellow alpha:&alpha]) return nil;
    
    cyan += cyanDelta;
    magenta += magentaDelta;
    yellow += yellowDelta;
    alpha += alphaDelta;
    
    cyan = cyan < 0 ? 0 : cyan > 1 ? 1 : cyan; // [0,1]
    magenta = magenta < 0 ? 0 : magenta > 1 ? 1 : magenta; // [0,1]
    yellow = yellow < 0 ? 0 : yellow > 1 ? 1 : yellow; // [0,1]
    alpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha; // [0,1]
    
    return [UIColor jx_colorWithCyan:cyan magenta:magenta yellow:yellow alpha:alpha];
}

- (UIColor *)jx_colorByChangeCyan:(CGFloat)cyanDelta magenta:(CGFloat)magentaDelta yellow:(CGFloat)yellowDelta key:(CGFloat)keyDelta alpha:(CGFloat)alphaDelta {
    CGFloat cyan, magenta, yellow, key, alpha;
    if (![self jx_getCyan:&cyan magenta:&magenta yellow:&yellow key:&key alpha:&alpha]) return nil;
    
    cyan += cyanDelta;
    magenta += magentaDelta;
    yellow += yellowDelta;
    key += keyDelta;
    alpha += alphaDelta;
    
    cyan = cyan < 0 ? 0 : cyan > 1 ? 1 : cyan; // [0,1]
    magenta = magenta < 0 ? 0 : magenta > 1 ? 1 : magenta; // [0,1]
    yellow = yellow < 0 ? 0 : yellow > 1 ? 1 : yellow; // [0,1]
    key = key < 0 ? 0 : key > 1 ? 1 : key; // [0,1]
    alpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha; // [0,1]
    
    return [UIColor jx_colorWithCyan:cyan magenta:magenta yellow:yellow key:key alpha:alpha];
}

#pragma mark - HSB
- (CGFloat)jx_hueOfHSB {
    CGFloat hue = 0, saturation, brightness, alpha;
    [self getHue:&hue saturation:&saturation brightness:&brightness alpha:&alpha];
    return hue;
}

- (CGFloat)jx_saturationOfHSB {
    CGFloat hue, saturation = 0, brightness, alpha;
    [self getHue:&hue saturation:&saturation brightness:&brightness alpha:&alpha];
    return saturation;
}

- (CGFloat)jx_brightnessOfHSB {
    CGFloat hue, saturation, brightness = 0, alpha;
    [self getHue:&hue saturation:&saturation brightness:&brightness alpha:&alpha];
    return brightness;
}

+ (UIColor *)jx_colorWithHue:(CGFloat)hue saturation:(CGFloat)saturation brightness:(CGFloat)brightness {
    return [UIColor colorWithHue:hue saturation:saturation brightness:brightness alpha:1];
}

- (UIColor *)jx_colorByChangeHueOfHSB:(CGFloat)hueDelta {
    return [self jx_colorByChangeHue:hueDelta saturation:0 brightness:0 alpha:0];
}

- (UIColor *)jx_colorByChangeSaturationOfHSB:(CGFloat)saturationDelta {
    return [self jx_colorByChangeHue:0 saturation:saturationDelta brightness:0 alpha:0];
}

- (UIColor *)jx_colorByChangeBrightnessOfHSB:(CGFloat)brightnessDelta {
    return [self jx_colorByChangeHue:0 saturation:0 brightness:brightnessDelta alpha:0];
}

- (UIColor *)jx_colorByChangeHue:(CGFloat)hueDelta saturation:(CGFloat)saturationDelta brightness:(CGFloat)brightnessDelta alpha:(CGFloat)alphaDelta {
    CGFloat hue, saturation, brightness, alpha;
    if (![self getHue:&hue saturation:&saturation brightness:&brightness alpha:&alpha]) {
        return self;
    }
    
    hue += hueDelta;
    saturation += saturationDelta;
    brightness += brightnessDelta;
    alpha += alphaDelta;
    
    hue -= (int)hue; // 多减(hue:0.5, hueDelta:0.8 -> hue = 0.3) 色相以角度计算(1 -> 360°, +/- -> 顺/逆)
    hue = hue < 0 ? hue + 1 : hue; // 少补(hue:0.5, hueDelta:-0.8 -> hue = 0.7)
    saturation = saturation < 0 ? 0 : saturation > 1 ? 1 : saturation; // [0,1]
    brightness = brightness < 0 ? 0 : brightness > 1 ? 1 : brightness; // [0,1]
    alpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha; // [0,1]
    return [UIColor colorWithHue:hue saturation:saturation brightness:brightness alpha:alpha];
}

#pragma mark - HSL
- (CGFloat)jx_hueOfHSL {
    CGFloat hue = 0, saturation, lightness, alpha;
    [self jx_getHue:&hue saturation:&saturation lightness:&lightness alpha:&alpha];
    return hue;
}

- (CGFloat)jx_saturationOfHSL {
    CGFloat hue, saturation = 0, lightness, alpha;
    [self jx_getHue:&hue saturation:&saturation lightness:&lightness alpha:&alpha];
    return saturation;
}

- (CGFloat)jx_lightnessOfHSL {
    CGFloat hue, saturation, lightness = 0, alpha;
    [self jx_getHue:&hue saturation:&saturation lightness:&lightness alpha:&alpha];
    return lightness;
}

- (BOOL)jx_getHue:(CGFloat *)hue saturation:(CGFloat *)saturation lightness:(CGFloat *)lightness alpha:(CGFloat *)alpha {
    CGFloat red, green, blue, aAlpha;
    if (![self getRed:&red green:&green blue:&blue alpha:&aAlpha]) {
        return NO;
    }
    JXRGB2HSL(red, green, blue, hue, saturation, lightness); // 基于RGB颜色空间调配
    *alpha = aAlpha;
    return YES;
}

+ (UIColor *)jx_colorWithHue:(CGFloat)hue saturation:(CGFloat)saturation lightness:(CGFloat)lightness {
    return [UIColor jx_colorWithHue:hue saturation:saturation lightness:lightness alpha:1];
}

+ (UIColor *)jx_colorWithHue:(CGFloat)hue saturation:(CGFloat)saturation lightness:(CGFloat)lightness alpha:(CGFloat)alpha {
    CGFloat red, green, blue;
    JXHSL2RGB(hue, saturation, lightness, &red, &green, &blue);
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

- (UIColor *)jx_colorByChangeHueOfHSL:(CGFloat)hueDelta {
    return [self jx_colorByChangeHue:hueDelta saturation:0 lightness:0 alpha:0];
}

- (UIColor *)jx_colorByChangeSaturationOfHSL:(CGFloat)saturationDelta {
    return [self jx_colorByChangeHue:0 saturation:saturationDelta lightness:0 alpha:0];
}

- (UIColor *)jx_colorByChangeLightnessOfHSL:(CGFloat)lightnessDelta {
    return [self jx_colorByChangeHue:0 saturation:0 lightness:lightnessDelta alpha:0];
}

- (UIColor *)jx_colorByChangeHue:(CGFloat)hueDelta saturation:(CGFloat)saturationDelta lightness:(CGFloat)lightnessDelta alpha:(CGFloat)alphaDelta {
    CGFloat hue, saturation, lightness, alpha;
    if (![self jx_getHue:&hue saturation:&saturation lightness:&lightness alpha:&alpha]) return nil;
    
    hue += hueDelta;
    saturation += saturationDelta;
    lightness += lightnessDelta;
    alpha += alphaDelta;
    
    hue -= (int)hue; // 多减(hue:0.5, hueDelta:0.8 -> hue = 0.3) 色相以角度计算(1 -> 360°, +/- -> 顺/逆)
    hue = hue < 0 ? hue + 1 : hue; // 少补(hue:0.5, hueDelta:-0.8 -> hue = 0.7)
    saturation = saturation < 0 ? 0 : saturation > 1 ? 1 : saturation; // [0,1]
    lightness = lightness < 0 ? 0 : lightness > 1 ? 1 : lightness; // [0,1]
    alpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha; // [0,1]
    
    return [UIColor jx_colorWithHue:hue saturation:saturation lightness:lightness alpha:alpha];
}

@end
