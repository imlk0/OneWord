package top.imlk.oneword.bean;

/**
 * Created by imlk on 2018/6/3.
 */
public class WordBean {
    public String from;
    public String content;
    public String source;// TODO 加入一言API来源

    public WordBean() {

    }

    public WordBean(String content, String from) {
        this.content = content;
        this.from = from;
    }
}
