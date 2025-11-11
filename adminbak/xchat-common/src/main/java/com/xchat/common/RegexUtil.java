package com.xchat.common;

import com.beust.jcommander.internal.Lists;
import org.apache.oro.text.regex.*;

import java.util.Iterator;
import java.util.List;

public class RegexUtil {

    private static PatternCompiler compiler = new Perl5Compiler();

    private static PatternMatcher matcher = new Perl5Matcher();

    private static Pattern pattern;
    private static List<String> levitPatterns = Lists.newArrayList();

    static {
        // 手机号、生日号、跟公司业务相关的号码
        levitPatterns.add("^(0|13|15|18|100|200|168|400|500|800)[0-9]*$");
        levitPatterns.add("^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$");
        levitPatterns.add("^\\d*(1688|2688|2088|2008|5188|10010|10001|666|888|668|686|688|866|868|886|999|5201314|1314520)\\d*$");
        // 重复号码，镜子号码
        levitPatterns.add("^(<a>\\d)(\\d)(\\d)\\1\\2\\3$");
        levitPatterns.add("^(\\d)(\\d)(\\d)\\3\\2\\1$");
        // AABB
        levitPatterns.add("^\\d*(\\d)\\1(\\d)\\2\\d*$");
        // AAABBB
        levitPatterns.add("^\\d*(\\d)\\1\\1(\\d)\\2\\2\\d*$");
        // ABABAB
        levitPatterns.add("^(\\d)(\\d)\\1\\2\\1\\2\\1\\2$");
        // ABCABC
        levitPatterns.add("^(\\d)(\\d)(\\d)\\1\\2\\3$");
        // ABBABB
        levitPatterns.add("^(\\d)(\\d)\\2\\1\\2\\2$");
        // AABAAB
        levitPatterns.add("^(\\d)\\1(\\d)\\1\\1\\2$");

        // 4-8 位置重复
        levitPatterns.add("^\\d*(\\d)\\1{2,}\\d*$");
        // 4位以上 位递增或者递减（7890也是递增）
        levitPatterns.add("(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)|9(?=0)){2,}|(?:0(?=9)|9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){2,})\\d");

        // 不能以 518 、918 结尾
        levitPatterns.add("^[0-9]*(518|918|520|1314)$");
    }

    /**
     * 根据正则过滤条件过滤
     *
     * @param input
     * @return
     * @throws MalformedPatternException
     */
    public static boolean contains(String input, String patternString) throws Exception {
        try {
            pattern = compiler.compile(patternString);
            if (matcher.contains(input, pattern)) {
                return true;
            }
        } catch (MalformedPatternException e) {
            return false;
        }
        return false;
    }

    public static boolean notContains(String input, List<String> patternStrings) throws Exception {
        return !contains(input, patternStrings);
    }

    /**
     * 根据批量正则过滤条件过滤
     *
     * @param input
     * @param patternStrings
     * @return
     * @throws MalformedPatternException
     */
    public static boolean contains(String input, List<String> patternStrings) throws Exception {
        for (Iterator<String> lt = patternStrings.listIterator(); lt.hasNext(); ) {
            if (contains(input, (String) lt.next())) {
                return true;
            }
            continue;
        }
        return false;
    }

    public static boolean checkPretty(String input) throws Exception {
        return contains(input, levitPatterns);
    }

    /**
     * 判断不是靓号，不是靓号返回true，是靓号返回false
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static boolean checkNotPretty(String input) throws Exception {
        return !contains(input, levitPatterns);
    }

    static private void init() {
        if (levitPatterns == null) {
            levitPatterns = Lists.newArrayList();
        } else {
            return;
        }

    }
}
