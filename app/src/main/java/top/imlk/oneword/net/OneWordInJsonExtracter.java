package top.imlk.oneword.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import top.imlk.oneword.bean.WordBean;

/**
 * Created by imlk on 2018/8/3.
 */
public class OneWordInJsonExtracter {

    public static void extractFromJSONObject(JSONObject temp, JSONObject real, WordBean wordBean) throws JSONException {
        for (Iterator<String> it = temp.keys(); it.hasNext(); ) {
            String key = it.next();
            if (real.has(key)) {
                direction(temp.get(key), real.get(key), wordBean);
            }

            Object value = temp.get(key);

            if (value != null && (value instanceof String)) {
                // 查找固定值
                switch (key) {
                    case WordBean.FIELD_CONTENT:
                        wordBean.content = (String) value;
                        break;
                    case WordBean.FIELD_REFERENCE:
                        wordBean.reference = (String) value;
                        break;
                    case WordBean.FIELD_TARGET_URL:
                        wordBean.target_url = (String) value;
                        break;
                    case WordBean.FIELD_TARGET_TEXT:
                        wordBean.target_text = (String) value;
                        break;
                }
            }
        }
    }

    public static void extractFromJSONArray(JSONArray temp, JSONArray real, WordBean wordBean) throws JSONException {
        int len = Math.min(temp.length(), real.length());
        for (int index = 0; index < len; index++) {
            direction(temp.get(index), real.get(index), wordBean);
        }

    }

    public static void direction(Object temp, Object real, WordBean wordBean) throws JSONException {
        if (temp != null && real != null) {
            if (temp instanceof String) {
                if (real instanceof String) {
                    resolveAim(((String) temp), ((String) real), wordBean);
                }
            } else if (temp instanceof JSONObject) {
                if (real instanceof JSONObject) {
                    extractFromJSONObject(((JSONObject) temp), ((JSONObject) real), wordBean);
                }
            } else if (temp instanceof JSONArray) {
                if (real instanceof JSONArray) {
                    extractFromJSONArray(((JSONArray) temp), ((JSONArray) real), wordBean);
                }
            }
        }
    }

    // 模板与请求结果之间匹配
    private static void resolveAim(String temp, String real, WordBean wordBean) {
        switch (temp.trim()) {
            case WordBean.FIELD_CONTENT:
                wordBean.content = real;
                break;
            case WordBean.FIELD_REFERENCE:
                wordBean.reference = real;
                break;
            case WordBean.FIELD_TARGET_URL:
                wordBean.target_url = real;
                break;
            case WordBean.FIELD_TARGET_TEXT:
                wordBean.target_text = real;
                break;
        }
    }


}
