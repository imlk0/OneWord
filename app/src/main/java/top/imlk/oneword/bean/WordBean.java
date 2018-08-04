package top.imlk.oneword.bean;

/**
 * Created by imlk on 2018/6/3.
 */
public class WordBean {

    public static final String FIELD_CONTENT = "[content]";
    public static final String FIELD_REFERENCE = "[reference]";
    public static final String FIELD_TARGET_URL = "[target_url]";

    public int id;
    public String content;
    public String reference;
    public String target_url;// TODO 相关链接

    public WordBean() {

    }

    public WordBean(String content, String reference) {
        this.content = content;
        this.reference = reference;
    }

    public WordBean(int id, String content, String reference, String target_url) {
        this.id = id;
        this.content = content;
        this.reference = reference;
        this.target_url = target_url;
    }

    @Override
    public String toString() {
        return "WordBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", reference='" + reference + '\'' +
                ", target_url='" + target_url + '\'' +
                '}';
    }
}
