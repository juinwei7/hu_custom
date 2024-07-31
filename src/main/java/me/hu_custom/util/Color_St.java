package me.hu_custom.util;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Color_St {


    public static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

    public static List<String> Color_List(List<String> ST_LIST){
     for (int i = 0 ; i<ST_LIST.size() ; i++){
         ST_LIST.set(i,Color_st(ST_LIST.get(i)));
        }
     return ST_LIST;
    }

    public static String Color_st(String textToTranslate) {
        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        String st = matcher.appendTail(buffer).toString();
        if (st.contains("&")){
            st = st.replaceAll("&","§");
        }
        return st;

    }

}
