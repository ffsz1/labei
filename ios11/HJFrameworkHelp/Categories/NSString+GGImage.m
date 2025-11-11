//
//  NSString+GGImage.m
//  HJLive
//
//  Created by FF on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "NSString+GGImage.h"

@implementation NSString (GGImage)

+ (NSString *)getLevelImageName:(NSInteger)level {
    return [self getMoneyLevelImageName:level];
}

+ (NSString *)getMoneyLevelText:(NSInteger)level {
    switch (level) {
        case 1:
            return @"60";
        case 2:
            return @"300";
        case 3:
            return @"1000";
        case 4:
            return @"5000";
        case 5:
            return @"1W";
        case 6:
            return @"2W";
        case 7:
            return @"4W";
        case 8:
            return @"6W";
        case 9:
            return @"8W";
        case 10:
            return @"10W";
        case 11:
            return @"20W";
        case 12:
            return @"40W";
        case 13:
            return @"60W";
        case 14:
            return @"80W";
        case 15:
            return @"100W";
        case 16:
            return @"140W";
        case 17:
            return @"180W";
        case 18:
            return @"220W";
        case 19:
            return @"260W";
        case 20:
            return @"300W";
        case 21:
            return @"350W";
        case 22:
            return @"400W";
        case 23:
            return @"450W";
        case 24:
            return @"500W";
        case 25:
            return @"550W";
        case 26:
            return @"600W";
        case 27:
            return @"700W";
        case 28:
            return @"800W";
        case 29:
            return @"1000W";
        case 30:
            return @"1200W";
        case 31:
            return @"1400W";
        case 32:
            return @"1600W";
        case 33:
            return @"1800W";
        case 34:
            return @"2000W";
        case 35:
            return @"2200W";
        case 36:
            return @"2400W";
        case 37:
            return @"2600W";
        case 38:
            return @"2800W";
        case 39:
            return @"3000W";
        case 40:
            return @"4000W";
    }
    return @"";
}

+ (NSString *)getMoneyLevelImageName:(NSInteger)level {
    NSInteger num = level /5;
    NSInteger nuber = level % 5;
    if (level %5 == 0) {
        num = num -1;
        nuber = nuber + 5;
    }
    NSString *name = [NSString stringWithFormat:@"hj_level_rich_%ld",level];
    return name;
}

+ (NSString *)getCharmLevelText:(NSInteger)level {
    switch (level) {
        case 1:
            return @"100";
        case 2:
            return @"400";
        case 3:
            return @"600";
        case 4:
            return @"800";
        case 5:
            return @"1000";
        case 6:
            return @"2000";
        case 7:
            return @"4000";
        case 8:
            return @"6000";
        case 9:
            return @"8000";
        case 10:
            return @"1W";
        case 11:
            return @"2W";
        case 12:
            return @"3W";
        case 13:
            return @"4W";
        case 14:
            return @"5W";
        case 15:
            return @"6W";
        case 16:
            return @"7W";
        case 17:
            return @"8W";
        case 18:
            return @"10W";
        case 19:
            return @"15W";
        case 20:
            return @"20W";
        case 21:
            return @"25W";
        case 22:
            return @"30W";
        case 23:
            return @"40W";
        case 24:
            return @"50W";
        case 25:
            return @"60W";
        case 26:
            return @"70W";
        case 27:
            return @"80W";
        case 28:
            return @"90W";
        case 29:
            return @"1000W";
        case 30:
            return @"200W";
        case 31:
            return @"400W";
        case 32:
            return @"600W";
        case 33:
            return @"800W";
        case 34:
            return @"1000W";
        case 35:
            return @"1200W";
        case 36:
            return @"14000W";
        case 37:
            return @"16000W";
        case 38:
            return @"18000W";
        case 39:
            return @"20000W";
        case 40:
            return @"30000W";
    }
    return @"";
}

+ (NSString *)getCharmLevelImageName:(NSInteger)level {
    NSInteger num = level /5;
    NSInteger nuber = level % 5;
    if (level %5 == 0) {
        num = num -1;
        nuber = nuber + 5;
    }
    NSString *name = [NSString stringWithFormat:@"hj_level_charm_%ld",level];
    return name;
}

@end
