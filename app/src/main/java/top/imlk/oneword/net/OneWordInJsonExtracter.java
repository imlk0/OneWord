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

    public static void extractFromJSONObject(JSONObject form, JSONObject real, WordBean wordBean) throws JSONException {
        for (Iterator<String> it = form.keys(); it.hasNext(); ) {
            String key = it.next();
            if (real.has(key)) {
                direction(form.get(key), real.get(key), wordBean);
            }
        }
    }

    public static void extractFromJSONArray(JSONArray form, JSONArray real, WordBean wordBean) throws JSONException {
        int len = Math.min(form.length(), real.length());
        for (int index = 0; index < len; index++) {
            direction(form.get(index), real.get(index), wordBean);
        }

    }

    public static void direction(Object form, Object real, WordBean wordBean) throws JSONException {
        if (form != null && real != null) {
            if (form instanceof String) {
                if (real instanceof String) {
                    resolveAim(((String) form), ((String) real), wordBean);
                }
            } else if (form instanceof JSONObject) {
                if (real instanceof JSONObject) {
                    extractFromJSONObject(((JSONObject) form), ((JSONObject) real), wordBean);
                }
            } else if (form instanceof JSONArray) {
                if (real instanceof JSONArray) {
                    extractFromJSONArray(((JSONArray) form), ((JSONArray) real), wordBean);
                }
            }
        }
    }

    private static void resolveAim(String form, String real, WordBean wordBean) {
        switch (form) {
            case WordBean.FIELD_CONTENT:
                wordBean.content = real;
                break;
            case WordBean.FIELD_REFERENCE:
                wordBean.reference = real;
                break;
            case WordBean.FIELD_TARGET_URL:
                wordBean.target_url = real;
                break;
        }
    }


}
