package com.skysec.soc.rds.dc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    // String pattern = "\\#\\{" + placeholder + "\\}";
    public static String replace(String input, String pattern, Object replace) {
        String replaceString = String.valueOf(replace);
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        return matcher.replaceAll(replaceString);
    }
}
