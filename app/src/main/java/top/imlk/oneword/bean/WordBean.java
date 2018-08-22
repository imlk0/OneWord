package top.imlk.oneword.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imlk on 2018/6/3.
 */
public class WordBean implements Parcelable {

    public static final String FIELD_CONTENT = "[content]";
    public static final String FIELD_REFERENCE = "[reference]";
    public static final String FIELD_TARGET_URL = "[target_url]";
    public static final String FIELD_TARGET_TEXT = "[target_text]";

    public int id;
    public String content;
    public String reference;
    public String target_url;
    public String target_text;

    public WordBean() {
    }

    public WordBean(String content, String reference) {
        this.content = content;
        this.reference = reference;
    }

    public WordBean(int id, String content, String reference, String target_url, String target_text) {
        this.id = id;
        this.content = content;
        this.reference = reference;
        this.target_url = target_url;
        this.target_text = target_text;
    }

    public WordBean(WordBean wordBean) {
        this.id = wordBean.id;
        this.content = wordBean.content;
        this.reference = wordBean.reference;
        this.target_url = wordBean.target_url;
        this.target_text = wordBean.target_text;
    }


    protected WordBean(Parcel in) {
        id = in.readInt();
        content = in.readString();
        reference = in.readString();
        target_url = in.readString();
        target_text = in.readString();
    }

    public static final Creator<WordBean> CREATOR = new Creator<WordBean>() {
        @Override
        public WordBean createFromParcel(Parcel in) {
            return new WordBean(in);
        }

        @Override
        public WordBean[] newArray(int size) {
            return new WordBean[size];
        }
    };

    @Override
    public String toString() {
        return "WordBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", reference='" + reference + '\'' +
                ", target_url='" + target_url + '\'' +
                ", target_text='" + target_text + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeString(reference);
        dest.writeString(target_url);
        dest.writeString(target_text);
    }


    public static WordBean generateDefaultBean() {
        return new WordBean("这是一个默认句子，\n喵喵喵喵喵喵喵喵", "来源");
    }
}
