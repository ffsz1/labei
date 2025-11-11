//
//  NSDate+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/4.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSDate+JXBase.h"
#import "NSArray+JXBase.h"

@implementation NSDate (JXBase)

#pragma mark - Base
- (NSInteger)jx_era {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitEra fromDate:self] era];
}

- (NSInteger)jx_year {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitYear fromDate:self] year];
}

- (NSInteger)jx_month {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitMonth fromDate:self] month];
}

- (NSInteger)jx_day {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitDay fromDate:self] day];
}

- (NSInteger)jx_hour {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitHour fromDate:self] hour];
}

- (NSInteger)jx_minute
{
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitMinute fromDate:self] minute];
}

- (NSInteger)jx_second {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitSecond fromDate:self] second];
}

- (NSInteger)jx_nanosecond {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitNanosecond fromDate:self] nanosecond];
}

- (NSInteger)jx_weekday {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitWeekday fromDate:self] weekday];
}

- (NSInteger)jx_weekdayOrdinal {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitWeekdayOrdinal fromDate:self] weekdayOrdinal];
}

- (NSInteger)jx_weekOfMonth {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitWeekOfMonth fromDate:self] weekOfMonth];
}

- (NSInteger)jx_weekOfYear {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitWeekOfYear fromDate:self] weekOfYear];
}

- (NSInteger)jx_yearForWeekOfYear {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitYearForWeekOfYear fromDate:self] yearForWeekOfYear];
}

- (NSInteger)jx_quarter {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitQuarter fromDate:self] quarter];
}

- (NSCalendar *)jx_calendar {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitCalendar fromDate:self] calendar];
}

- (NSTimeZone *)jx_timeZone {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitTimeZone fromDate:self] timeZone];
}

#pragma mark - Zodiac Sign
- (JXNSDateAstrologyChineseZodiacSign)jx_ChineseZodiacSign {
    NSCalendar *chineseCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierChinese];
    NSDateComponents *components = [chineseCalendar components:NSCalendarUnitYear fromDate:self];
    NSInteger year = components.year;
    return (year - 1) % 12;
}

- (JXNSDateAstrologyZodiacSign)jx_zodiacSign {
    NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitMonth|NSCalendarUnitDay fromDate:self];
    NSInteger buffer = components.month * 100 + components.day;
    
    static dispatch_once_t onceToken;
    static NSArray<NSNumber *> *signs;
    dispatch_once(&onceToken, ^{
        signs = @[@120, @219, @321, @420, @521, @621, @723, @823, @923, @1023, @1122, @1222];
    });
    
    __block JXNSDateAstrologyZodiacSign sign = JXNSDateAstrologyZodiacSignCapricorn;
    [signs enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (buffer < [obj integerValue]) {
            if (idx != 0) sign = idx - 1;
            *stop = YES;
        }
    }];
    return sign;
}

#pragma mark - Time Interval
- (NSInteger)jx_timeIntervalSince1970 {
    return self.timeIntervalSince1970 * 1000;
}

#pragma mark - Comparison
- (BOOL)jx_isLeapYear {
    NSUInteger year = self.jx_year;
    return ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0)));
}

- (BOOL)jx_isLastYearLeapYear {
    NSDate *added = [self jx_dateByAddingYears:-1];
    return [added jx_isLeapYear];
}

- (BOOL)jx_isNextYearLeapYear {
    NSDate *added = [self jx_dateByAddingYears:1];
    return [added jx_isLeapYear];
}

- (BOOL)jx_isLeapMonth {
    return [[[NSCalendar currentCalendar] components:NSCalendarUnitQuarter fromDate:self] isLeapMonth];
}

- (BOOL)jx_isLastMonthLeapMonth {
    NSDate *added = [self jx_dateByAddingMonths:-1];
    return [added jx_isLeapMonth];
}

- (BOOL)jx_isNextMonthLeapMonth {
    NSDate *added = [self jx_dateByAddingMonths:1];
    return [added jx_isLeapMonth];
}

- (BOOL)jx_isThisYear {
    return [self jx_isYearEqualToDate:[NSDate date]];
}

- (BOOL)jx_isLastYear {
    NSDate *added = [self jx_dateByAddingYears:1];
    return [added jx_isThisYear];
}

- (BOOL)jx_isNextYear {
    NSDate *added = [self jx_dateByAddingYears:-1];
    return [added jx_isThisYear];
}

- (BOOL)jx_isThisMonth {
    if (!self.jx_isThisYear) return NO;
    
    return [self jx_isMonthEqualToDate:[NSDate date]]; 
}

- (BOOL)jx_isLastMonth {
    NSDate *added = [self jx_dateByAddingMonths:1];
    return [added jx_isThisMonth];
}

- (BOOL)jx_isNextMonth {
    NSDate *added = [self jx_dateByAddingMonths:-1];
    return [added jx_isThisMonth];
}

- (BOOL)jx_isThisWeek {
    if (fabs(self.timeIntervalSinceNow) >= 60 * 60 * 24 * 7) return NO;
    
    return [self jx_isEqualToDate:[NSDate date] toUnitGranularity:NSCalendarUnitWeekOfYear];
}

- (BOOL)jx_isLastWeek {
    NSDate *added = [self jx_dateByAddingWeeks:1];
    return [added jx_isThisWeek];
}

- (BOOL)jx_isNextWeek {
    NSDate *added = [self jx_dateByAddingWeeks:-1];
    return [added jx_isThisWeek];
}

- (BOOL)jx_isToday
{
    if (fabs(self.timeIntervalSinceNow) >= 60 * 60 * 24) return NO; // 日期与当前日期的时间差距
    return [self jx_isDayEqualToDate:[NSDate new]];
}

- (BOOL)jx_isYesterday {
    NSDate *added = [self jx_dateByAddingDays:1];
    return [added jx_isToday]; // 昨天 + 1 = 今天
}

- (BOOL)jx_isTomorrow {
    NSDate *added = [self jx_dateByAddingDays:-1];
    return [added jx_isToday]; // 明天 - 1 = 今天
}

- (BOOL)jx_isTheDayBeforeYesterday {
    NSDate *added = [self jx_dateByAddingDays:2];
    return [added jx_isToday]; // 前天 + 2 = 今天
}

- (BOOL)jx_isTheDayAfterTomorrow {
    NSDate *added = [self jx_dateByAddingDays:-2];
    return [added jx_isToday]; // 后天 - 2 = 今天
}

- (BOOL)jx_isThisHour {
    if (fabs(self.timeIntervalSinceNow) >= 60 * 60) return NO;
    
    return [self jx_isHourEqualToDate:[NSDate new]];
}

- (BOOL)jx_isLastHour {
    NSDate *added = [self jx_dateByAddingHours:1];
    return [added jx_isThisHour];
}

- (BOOL)jx_isNextHour {
    NSDate *added = [self jx_dateByAddingHours:-1];
    return [added jx_isThisHour];
}

- (BOOL)jx_isThisMinute {
    if (fabs(self.timeIntervalSinceNow) >= 60) return NO;
    
    return [self jx_isMinuteEqualToDate:[NSDate new]];
}

- (BOOL)jx_isLastMinute {
    NSDate *added = [self jx_dateByAddingMinutes:1];
    return [added jx_isThisMinute];
}

- (BOOL)jx_isNextMinute {
    NSDate *added = [self jx_dateByAddingMinutes:-1];
    return [added jx_isThisMinute];
}

- (BOOL)jx_isThisSecond {
    if (fabs(self.timeIntervalSinceNow) >= 1) return NO;
    
    return [self jx_isSecondEqualToDate:[NSDate new]];
}

- (BOOL)jx_isLastSecond {
    NSDate *added = [self jx_dateByAddingSeconds:1];
    return [added jx_isThisSecond];
}

- (BOOL)jx_isNextSecond {
    NSDate *added = [self jx_dateByAddingSeconds:-1];
    return [added jx_isThisSecond];
}

- (BOOL)jx_isEarlierThanDate:(NSDate *)date {
    return [self compare:date] == NSOrderedAscending;
}

- (BOOL)jx_isLaterThanDate:(NSDate *)date {
    return [self compare:date] == NSOrderedDescending;
}

- (BOOL)jx_isDateBetweenStartDate:(NSDate *)startDate endDate:(NSDate *)endDate {
    if ([self jx_isEarlierThanDate:startDate]) return NO;
    if ([self jx_isLaterThanDate:endDate]) return NO;
    
    return YES;
}

- (BOOL)jx_isInPast {
    return [self jx_isEarlierThanDate:[NSDate date]];
}

- (BOOL)jx_isInFuture {
    return [self jx_isLaterThanDate:[NSDate date]];
}

#pragma mark - Equal
- (BOOL)jx_isEqualToDate:(NSDate *)other toUnitGranularity:(NSCalendarUnit)unit {
    if (!other) return NO;
    if (![other isKindOfClass:[NSDate class]]) return NO;
    
    if (unit & NSCalendarUnitEra) {
        if (self.jx_era != other.jx_era) return NO;
    }
    
    if (unit & NSCalendarUnitYear) {
        if (self.jx_year != other.jx_year) return NO;
    }
    
    if (unit & NSCalendarUnitMonth) {
        if (self.jx_month != other.jx_month) return NO;
    }
    
    if (unit & NSCalendarUnitDay) {
        if (self.jx_day != other.jx_day) return NO;
    }
    
    if (unit & NSCalendarUnitHour) {
        if (self.jx_hour != other.jx_hour) return NO;
    }
    
    if (unit & NSCalendarUnitMinute) {
        if (self.jx_minute != other.jx_minute) return NO;
    }
    
    if (unit & NSCalendarUnitSecond) {
        if (self.jx_second != other.jx_second) return NO;
    }
    
    if (unit & NSCalendarUnitWeekday) {
        if (self.jx_weekday != other.jx_weekday) return NO;
    }
    
    if (unit & NSCalendarUnitWeekdayOrdinal) {
        if (self.jx_weekdayOrdinal != other.jx_weekdayOrdinal) return NO;
    }
    
    if (unit & NSCalendarUnitQuarter) {
        if (self.jx_quarter != other.jx_quarter) return NO;
    }
    
    if (unit & NSCalendarUnitWeekOfMonth) {
        if (self.jx_weekOfMonth != other.jx_weekOfMonth) return NO;
    }
    
    if (unit & NSCalendarUnitWeekOfYear) {
        if (self.jx_weekOfYear != other.jx_weekOfYear) return NO;
    }
    
    if (unit & NSCalendarUnitYearForWeekOfYear) {
        if (self.jx_yearForWeekOfYear != other.jx_yearForWeekOfYear) return NO;
    }
    
    if (unit & NSCalendarUnitNanosecond) {
        if (self.jx_nanosecond != other.jx_nanosecond) return NO;
    }
    
    if (unit & NSCalendarUnitCalendar) {
        if (![self.jx_calendar isEqual:other.jx_calendar]) return NO;
    }
    
    if (unit & NSCalendarUnitTimeZone) {
        if (![self.jx_timeZone isEqualToTimeZone:other.jx_timeZone]) return NO;
    }
    
    return YES;
}

- (BOOL)jx_isEqualToDateIgnoringTime:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay];
}

- (BOOL)jx_isQuarterEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitQuarter];
}

- (BOOL)jx_isYearEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitYear];
}

- (BOOL)jx_isMonthEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitMonth];
}

- (BOOL)jx_isWeekdayEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitWeekday];
}

- (BOOL)jx_isDayEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitDay];
}

- (BOOL)jx_isHourEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitHour];
}

- (BOOL)jx_isMinuteEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitMinute];
}

- (BOOL)jx_isSecondEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitSecond];
}

- (BOOL)jx_isNanosecondEqualToDate:(NSDate *)other {
    return [self jx_isEqualToDate:other toUnitGranularity:NSCalendarUnitNanosecond];
}

#pragma mark - Creation
+ (NSDate *)jx_dateYesterday {
    return [[NSDate date] jx_dateByAddingDays:-1];
}

+ (NSDate *)jx_dateTomorrow {
    return [[NSDate date] jx_dateByAddingDays:1];
}

#pragma mark - Modify
- (NSDate *)jx_dateByAddingQuarter:(NSInteger)quarters {
    NSCalendar *calendar =  [NSCalendar currentCalendar];
    NSDateComponents *components = [[NSDateComponents alloc] init];
    [components setQuarter:quarters];
    return [calendar dateByAddingComponents:components toDate:self options:0];
}

- (NSDate *)jx_dateByAddingYears:(NSInteger)years {
    NSCalendar *calendar =  [NSCalendar currentCalendar];
    NSDateComponents *components = [[NSDateComponents alloc] init];
    [components setYear:years];
    return [calendar dateByAddingComponents:components toDate:self options:0];
}

- (NSDate *)jx_dateByAddingMonths:(NSInteger)months {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *components = [[NSDateComponents alloc] init];
    [components setMonth:months];
    return [calendar dateByAddingComponents:components toDate:self options:0];
}

- (NSDate *)jx_dateByAddingWeeks:(NSInteger)weeks {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *components = [[NSDateComponents alloc] init];
    [components setWeekOfYear:weeks];
    return [calendar dateByAddingComponents:components toDate:self options:0];
}

- (NSDate *)jx_dateByAddingDays:(NSInteger)days {
    NSTimeInterval aTimeInterval = [self timeIntervalSinceReferenceDate] + 86400 * days; // 86400 = 60 * 60 * 24, timeIntervalSinceReferenceDate -> 以2001/01/01 00:00:00 UTC为基准时间, 返回实例保存的时间与2001/01/01 00:00:00 UTC的时间间隔
    NSDate *newDate = [NSDate dateWithTimeIntervalSinceReferenceDate:aTimeInterval];
    return newDate;
}

- (NSDate *)jx_dateByAddingHours:(NSInteger)hours {
    NSTimeInterval aTimeInterval = [self timeIntervalSinceReferenceDate] + 3600 * hours;
    NSDate *newDate = [NSDate dateWithTimeIntervalSinceReferenceDate:aTimeInterval];
    return newDate;
}

- (NSDate *)jx_dateByAddingMinutes:(NSInteger)minutes {
    NSTimeInterval aTimeInterval = [self timeIntervalSinceReferenceDate] + 60 * minutes;
    NSDate *newDate = [NSDate dateWithTimeIntervalSinceReferenceDate:aTimeInterval];
    return newDate;
}

- (NSDate *)jx_dateByAddingSeconds:(NSInteger)seconds {
    NSTimeInterval aTimeInterval = [self timeIntervalSinceReferenceDate] + seconds;
    NSDate *newDate = [NSDate dateWithTimeIntervalSinceReferenceDate:aTimeInterval];
    return newDate;
}

#pragma mark - Date Format
- (NSString *)jx_stringWithFormat:(NSString *)format {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:format];
    [formatter setLocale:[NSLocale currentLocale]];
    return [formatter stringFromDate:self];
}

- (NSString *)jx_stringWithFormat:(NSString *)format timeZone:(NSTimeZone *)timeZone locale:(NSLocale *)locale {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:format];
    if (timeZone) [formatter setTimeZone:timeZone];
    if (locale) [formatter setLocale:locale];
    return [formatter stringFromDate:self];
}

- (NSString *)jx_stringWithISOFormat {
    static NSDateFormatter *formatter = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        formatter = [[NSDateFormatter alloc] init];
        formatter.locale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US_POSIX"]; // Apple suggest
        formatter.dateFormat = @"yyyy-MM-dd'T'HH:mm:ssZ";
    });
    return [formatter stringFromDate:self];
}

- (NSString *)jx_stringForChineseZodiacSign {
    return [[self._jx_localizedChineseZodiacSignStrings jx_objectOrNilAtIndex:self.jx_ChineseZodiacSign] copy];
}

- (NSString *)jx_stringForZodiacSign {
    return [[self._jx_localizedZodiacSignStrings jx_objectOrNilAtIndex:self.jx_zodiacSign] copy];
}

- (NSArray<NSString *> *)_jx_localizedZodiacSignStrings {
    static NSArray *strings = nil;
    static NSDictionary *dic = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        dic = @{
                @"en" : @[@"Aquarius", @"Pisces", @"Aries", @"Taurus", @"Gemini", @"Cancer", @"Leo", @"Virgo", @"Libra", @"Scorpio", @"Sagittarius", @"Capricorn"],
                @"zh" : @[@"水瓶座", @"双鱼座", @"白羊座", @"金牛座", @"双子座", @"巨蟹座", @"狮子座", @"处女座", @"天秤座", @"天蝎座", @"射手座", @"摩羯座"],
                @"zh_CN" : @[@"水瓶座", @"双鱼座", @"白羊座", @"金牛座", @"双子座", @"巨蟹座", @"狮子座", @"处女座", @"天秤座", @"天蝎座", @"射手座", @"摩羯座"],
                @"zh_HK" : @[@"水瓶座", @"雙魚座", @"白羊座", @"金牛座", @"雙子座", @"巨蟹座", @"獅子座", @"處女座", @"天秤座", @"天蝎座", @"射手座", @"魔蝎座"],
                @"zh_TW" : @[@"水瓶座", @"雙魚座", @"白羊座", @"金牛座", @"雙子座", @"巨蟹座", @"獅子座", @"處女座", @"天秤座", @"天蝎座", @"射手座", @"魔蝎座"],
                };
    });
    if (!strings) strings = dic[@"zh"];
    return strings;
}

- (NSArray<NSString *> *)_jx_localizedChineseZodiacSignStrings {
    static NSArray *strings = nil;
    static NSDictionary *dic = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        dic = @{
                @"en" : @[@"Rat", @"Ox", @"Tiger", @"Rabbit", @"Dragon", @"Snake", @"Horse", @"Goat", @"Monkey", @"Rooster", @"Dog", @"Pig"],
                @"zh" : @[@"鼠", @"牛", @"虎", @"兔", @"龙", @"蛇", @"马", @"羊", @"猴", @"鸡", @"狗", @"猪"],
                @"zh_CN" : @[@"鼠", @"牛", @"虎", @"兔", @"龙", @"蛇", @"马", @"羊", @"猴", @"鸡", @"狗", @"猪"],
                @"zh_HK" : @[@"鼠", @"牛", @"虎", @"兔", @"龍", @"蛇", @"馬", @"羊", @"猴", @"雞", @"狗", @"豬"],
                @"zh_TW" : @[@"鼠", @"牛", @"虎", @"兔", @"龍", @"蛇", @"馬", @"羊", @"猴", @"雞", @"狗", @"豬"],
                };
    });
    if (!strings) strings = dic[@"zh"];
    return strings;
}

+ (NSDate *)jx_dateWithString:(NSString *)dateString format:(NSString *)format {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:format];
    return [formatter dateFromString:dateString];
}

+ (NSDate *)jx_dateWithString:(NSString *)dateString format:(NSString *)format timeZone:(NSTimeZone *)timeZone locale:(NSLocale *)locale {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:format];
    if (timeZone) [formatter setTimeZone:timeZone];
    if (locale) [formatter setLocale:locale];
    return [formatter dateFromString:dateString];
}

+ (NSDate *)jx_dateWithISOFormatString:(NSString *)dateString {
    static NSDateFormatter *formatter = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        formatter = [[NSDateFormatter alloc] init];
        formatter.locale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US_POSIX"];
        formatter.dateFormat = @"yyyy-MM-dd'T'HH:mm:ssZ";
    });
    return [formatter dateFromString:dateString];
}

/**
 *  是否为同一天
 */
+ (BOOL)jx_isSameDay:(NSDate*)date1 date2:(NSDate*)date2
{
    NSCalendar* calendar = [NSCalendar currentCalendar];

    unsigned unitFlags = NSCalendarUnitYear | NSCalendarUnitMonth |  NSCalendarUnitDay;
    NSDateComponents* comp1 = [calendar components:unitFlags fromDate:date1];
    NSDateComponents* comp2 = [calendar components:unitFlags fromDate:date2];

    return [comp1 day]   == [comp2 day] &&
    [comp1 month] == [comp2 month] &&
    [comp1 year]  == [comp2 year];

}

@end
