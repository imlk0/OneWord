package top.imlk.oneword.util;

/**
 * Created by imlk on 2018/8/1.
 */
public class StringUtil {

    public static String butifyString(String s0) {
        if (s0 == null) {
            return "";
        }


        String s1 = s0
//                    .replace(" ", "\n")
                .replace("；", "；\n")
                .replace(";", ";\n")
                .replace("?", "?\n")
                .replace("？", "？\n")
                .replace("!", "!\n")
                .replace("！", "！\n");

        int count = 0;
        for (int i = 0; i < s1.length(); ++i) {
            if (s1.charAt(i) == '，') {
                count++;
            }
        }
        if (count <= 6) {
            s1 = s1.replace("，", "，\n");
        }
        s1 = s1.replace("。", "。\n");

        return s1.trim();
    }

}
